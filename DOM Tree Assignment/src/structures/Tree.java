package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {	
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file
	 */
	public void build() {
		/** COMPLETE THIS METHOD **/
		//stack to store tags and keep track of completion of tags
		Stack<TagNode> tags = new Stack<TagNode>();
		String first = sc.nextLine();
		root = new TagNode(first.substring(1, first.length()-1), null, null);
		tags.push(root);
		String curr = "";
		while (sc.hasNextLine()){
			curr = sc.nextLine();
						
			if (curr.charAt(0) == '<'){//itÕs a tag
				if (curr.charAt(1) == '/'){ //close tag
					tags.pop();
				}
				else{ //open tag
					TagNode temp = new TagNode(curr.substring(1, curr.length()-1), null, null);
					if (tags.peek().firstChild == null){
						tags.peek().firstChild = temp;		
						tags.push(temp);
					}
					else {
						TagNode ptr = tags.peek().firstChild;
						while (ptr.sibling != null){
							ptr = ptr.sibling;
						}
						ptr.sibling = temp;
						tags.push(temp);
					}
				}
			}
			
			else {//its text
				TagNode temp = new TagNode(curr, null, null);
				if (tags.peek().firstChild == null){
					tags.peek().firstChild = temp;
				}
				else {
					TagNode ptr = tags.peek().firstChild;
					while (ptr.sibling != null){
						ptr = ptr.sibling;
					}
					ptr.sibling = temp;
				}
			}
		}	

	}
	
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		/** COMPLETE THIS METHOD **/
		if ((oldTag.equals("p") || oldTag.equals("b") || oldTag.equals("em"))
				&& (newTag.equals("p") || newTag.equals("b") || newTag.equals("em"))){
			replaceRecursive(oldTag, newTag, root);
		}
		else if ((oldTag.equals("ol") || oldTag.equals("ul")) && (newTag.equals("ol") || newTag.equals("ul"))){
			replaceRecursive(oldTag, newTag, root);
		}
		else {
			return;
		}
	}
	
	private static void replaceRecursive(String oldT, String newT, TagNode theRoot){
		if (theRoot == null){
			return;
		}
		if (theRoot.tag.equals(oldT) && theRoot.firstChild != null){
			theRoot.tag = newT;
		}
		replaceRecursive(oldT, newT, theRoot.firstChild);
		replaceRecursive(oldT, newT, theRoot.sibling);
		return;
	}
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		/** COMPLETE THIS METHOD **/
		boldRowRecursive(row, root);
		
		//print tree
		printTree(root);
	}
	
	private static void printTree(TagNode root){
		if (root == null){
			return;
		}
		System.out.println(root.tag);
		printTree(root.firstChild);
		printTree(root.sibling);
	}
	
	private void boldRowRecursive(int row, TagNode theRoot){
		if (theRoot == null){
			return;
		}
		if (theRoot.tag.equals("table")){
			TagNode ptr = theRoot.firstChild;
			for (int i=1; i<row; i++){
				ptr = ptr.sibling;
			}
			TagNode teed = ptr.firstChild;
			TagNode bold = new TagNode("b", teed.firstChild, null);
			teed.firstChild = bold;
			while (teed.sibling != null){
				teed = teed.sibling;
				bold = new TagNode("b", teed.firstChild, null);
				teed.firstChild = bold;
			}
		}
		else { //not a table yet
			boldRowRecursive(row, theRoot.firstChild);
			boldRowRecursive(row, theRoot.sibling);
		}
		return;
	}
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		/** COMPLETE THIS METHOD **/
		removeTagHelper(tag, root);
		if (tag.equals("ol") || tag.equals("ul")){
			makeP(root);
		}
		printTree(root);
	}
	
	private static void makeP(TagNode root){
		if (root == null || root.firstChild == null){
			return;
		}
		if (root.firstChild.tag.equals("li") && !(root.tag.equals("ol") || root.tag.equals("ul"))){
//			System.out.println("convert!");
			root.firstChild.tag = "p";
		}
		else {
			makeP(root.firstChild);
			makeP(root.sibling);
		}
	}
	
	private static void removeTagHelper(String tag, TagNode root){
		if (root == null || (root.firstChild == null && root.sibling == null)){
			return;
		}
		if ((root.firstChild != null) && (root.firstChild.firstChild != null) && root.firstChild.tag.equals(tag)){ //child
			if (tag.equals("ol") || tag.equals("ul")){
				if (root.firstChild.firstChild.sibling != null){
//					root.firstChild.firstChild.sibling.tag = "p";
					TagNode ptr = root.firstChild.firstChild.sibling;
					if (ptr.tag.equals("li")){
						ptr.tag = "p";
					}
					while (ptr.sibling != null) {
						ptr = ptr.sibling;
						if (ptr.tag.equals("li")){
							ptr.tag = "p";
						}
					}
					ptr.sibling = root.firstChild.sibling;
					TagNode temp = root.firstChild.firstChild;
					if (temp.tag == "li"){
						temp.tag = "p";
					}
					root.firstChild = temp;
				}
				else {
					TagNode temp = root.firstChild.firstChild;
					if (temp.tag.equals("li")){
						temp.tag = "p";		
					}
					temp.sibling = root.firstChild.sibling;
					root.firstChild = temp;
				}	
				
				removeTagHelper(tag, root);
			}
			
			if (tag.equals("p") || tag.equals("em") || tag.equals("b")){
				if (root.firstChild.firstChild.sibling != null) {
					TagNode ptr = root.firstChild.firstChild.sibling;
					while (ptr.sibling != null) {
						ptr = ptr.sibling;
					}
					ptr.sibling = root.firstChild.sibling;
					TagNode temp = root.firstChild.firstChild;
					root.firstChild = temp;
				}
				else {
					TagNode temp = root.firstChild.firstChild;
					temp.sibling = root.firstChild.sibling;
					root.firstChild = temp;
				}
				
				removeTagHelper(tag, root);
			}
		}
		if ((root.sibling != null) && root.sibling.tag.equals(tag)) { //sibling
			if (tag.equals("ol") || tag.equals("ul")){
				if (root.sibling.firstChild.sibling != null){
					TagNode ptr = root.sibling.firstChild.sibling;
					if (ptr.tag.equals("li")){
						ptr.tag = "p";
					}
					while (ptr.sibling != null) {
						ptr = ptr.sibling;
						if (ptr.tag.equals("li")){
							ptr.tag = "p";
						}
					}
					ptr.sibling = root.sibling.sibling;
					if (root.sibling.firstChild.tag.equals("li")) {
						root.sibling.firstChild.tag = "p";
					}
					TagNode temp = root.sibling.firstChild;
					if (temp.tag == "li"){
						temp.tag = "p";
					}
					root.sibling = temp;
				}
				else {
					TagNode temp = root.sibling.firstChild;
					temp.sibling = root.firstChild.sibling;
					root.sibling = temp;
				}
				
				removeTagHelper(tag, root);
			}
			
			if (tag.equals("p") || tag.equals("em") || tag.equals("b")){
				if (root.sibling.firstChild.sibling != null) {
					TagNode ptr = root.sibling.firstChild.sibling;
					while (ptr.sibling != null) {
						ptr = ptr.sibling;
					}
					ptr.sibling = root.sibling.sibling;
					TagNode temp = root.sibling.firstChild;
					root.sibling = temp;
				}
				else {
					TagNode temp = root.sibling.firstChild;
					temp.sibling = root.sibling.sibling;
					root.sibling = temp;
				}
				removeTagHelper(tag, root);
			}
		}
		else {
//			System.out.println("call child");
			removeTagHelper(tag, root.firstChild);
//			System.out.println("call sib");
			removeTagHelper(tag, root.sibling);
		}
		return;
	}
	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
		/** COMPLETE THIS METHOD **/
		if (tag.equals("em") || tag.equals("b")){
			addTagHelper(word.toLowerCase(),tag,root);
		}
		return;
	}
	
	private static void addTagHelper(String word, String tag, TagNode root){
		
		if (root == null || (root.firstChild == null && root.sibling == null)){
			return;
		}
		
		else if (root.firstChild != null && root.firstChild.firstChild == null && root.firstChild.tag.toLowerCase().contains(word)){
			if (root.firstChild.tag.equalsIgnoreCase(word)) { //tag is all you need
				TagNode theTag = new TagNode(tag, root.firstChild, root.firstChild.sibling);
				root.firstChild = theTag;
			}
			else {
				
				int index = root.firstChild.tag.indexOf(word);
//				while (root.firstChild.tag.charAt(index+word.length()) == '-' || Character.isLetter(root.firstChild.tag.charAt(index+word.length()))
//						|| Character.isLetter(root.firstChild.tag.charAt(index-word.length()))){
//					index = root.firstChild.tag.substring(index+word.length()).indexOf(word);
//				}
				if (index < 0){
					return;
				}
////				else if (index == 0) {//word is at beginning
////				}
				else {
					int last = word.length();
					while (root.firstChild.tag.charAt(index + last) == '!' || root.firstChild.tag.charAt(index + last) == '.' 
							|| root.firstChild.tag.charAt(index + last) == ',' || root.firstChild.tag.charAt(index + last) == '?' || 
							root.firstChild.tag.charAt(index + last) == ';' || root.firstChild.tag.charAt(index + last) == ':'
							){
						last++;
					}					
					if (index == 0){//word is at beginning
						TagNode left = new TagNode(root.firstChild.tag.substring(0, last), root.firstChild.firstChild, null);
						TagNode right = new TagNode(root.firstChild.tag.substring(last), null, root.firstChild.sibling);
						TagNode theTag = new TagNode(tag, left, right);
						root.firstChild = theTag;
						
					}
					else if (index > 0 && index+last < root.firstChild.tag.length() - 1){ //word is in middle
						TagNode firstThird = new TagNode(root.firstChild.tag.substring(0, index), null, null);
						TagNode secondThird = new TagNode(root.firstChild.tag.substring(index, index+last), null, null); //theword
						TagNode finalThird = new TagNode(root.firstChild.tag.substring(index+last), null, root.firstChild.sibling);
						TagNode theTag = new TagNode(tag, secondThird, finalThird);
						firstThird.sibling = theTag;
						root.firstChild = theTag;
					}
					else {
						if (root.firstChild.tag.length() == index+last){
							TagNode firstHalf = new TagNode(root.firstChild.tag.substring(0, index), null, null);
							TagNode secondHalf = new TagNode(root.firstChild.tag.substring(index), null, null); //theword
							TagNode theTag = new TagNode(tag, secondHalf, root.firstChild.sibling);
							firstHalf.sibling = theTag;
							root.firstChild = theTag;
						}
					}
//					addTagHelper(word, tag, root);
				}
			}
		}
		
		else if (root.sibling != null && root.sibling.firstChild == null && root.sibling.tag.toLowerCase().equalsIgnoreCase(word)){
			int index = root.sibling.tag.indexOf(word);
			if (root.sibling.tag.equalsIgnoreCase(word)) { //tag is all you need
				TagNode theTag = new TagNode(tag, root.sibling, root.sibling.sibling);
				root.sibling = theTag;
			}
			else {
				int index1 = root.sibling.tag.indexOf(word);
//				while (root.sibling.tag.charAt(index+word.length()) == '-' || Character.isLetter(root.sibling.tag.charAt(index+word.length()))
//						|| Character.isLetter(root.sibling.tag.charAt(index-word.length()))){
//					index = root.sibling.tag.substring(index+word.length()).indexOf(word);
//				}
				if (index1 < 0){
					return;
				}
//				else if (index == 0) {//word is at beginning
//				}
				else {
					int last = word.length();
					while (root.sibling.tag.charAt(index1 + last) == '!' || root.sibling.tag.charAt(index1 + last) == '.' 
							|| root.sibling.tag.charAt(index1 + last) == ',' || root.sibling.tag.charAt(index1 + last) == '?' || 
							root.sibling.tag.charAt(index1 + last) == ';' || root.sibling.tag.charAt(index1 + last) == ':'
							){
						last++;
					}					
					if (index1 == 0){//word is at beginning
						TagNode left = new TagNode(root.sibling.tag.substring(0, last), root.sibling.firstChild, null);
						TagNode right = new TagNode(root.sibling.tag.substring(last), null, root.sibling.sibling);
						TagNode theTag = new TagNode(tag, left, right);
						root.sibling = theTag;
						
					}
					else if (index1 > 0 && index1+last < root.sibling.tag.length() - 1){ //word is in middle
						TagNode firstThird = new TagNode(root.sibling.tag.substring(0, index1), null, null);
						TagNode secondThird = new TagNode(root.sibling.tag.substring(index1, index1+last), null, null); //theword
						TagNode finalThird = new TagNode(root.sibling.tag.substring(index1+last), null, root.sibling.sibling);
						TagNode theTag = new TagNode(tag, secondThird, finalThird);
						firstThird.sibling = theTag;
						root.sibling = theTag;
					}
					else {
						if (root.sibling.tag.length() == index1+last){
							TagNode firstHalf = new TagNode(root.sibling.tag.substring(0, index1), null, null);
							TagNode secondHalf = new TagNode(root.sibling.tag.substring(index1), null, null); //theword
							TagNode theTag = new TagNode(tag, secondHalf, root.sibling.sibling);
							firstHalf.sibling = theTag;
							root.sibling = theTag;
						}
					}
//					addTagHelper(word, tag, root);
				}
			}
		}
		
		else{
			addTagHelper(word, tag, root.firstChild);
			addTagHelper(word, tag, root.sibling);
		}
		return;
	}
//		if ((root.firstChild != null) && (root.firstChild.firstChild == null) && (root.firstChild.tag.toLowerCase().contains(word))){ //child
//			String curr = root.firstChild.tag.toLowerCase();
//			if (curr.equalsIgnoreCase(word)){ //whole word
//				TagNode temp = new TagNode(tag, root.firstChild, root.firstChild.sibling);
//				root.firstChild = temp;
////				addTagHelper(word, tag, root);
//			}
//
//			else if (curr.contains(word) && curr.length() > word.length()){ //txtwordtxt
//
//				String part = "";
//				int k = curr.indexOf(word.charAt(0));
//				part = curr.substring(k,curr.indexOf(word.charAt(0))+word.length()-1);
//				String left = ""; String right = "";
//				while (!part.equalsIgnoreCase(word)){
//					k++;
//					left = curr.substring(0, k);
//					part = curr.substring(k,k+word.length()-1);
//				}
//				if (k == 0){//check for text AFTER
//					int i = word.length();
//					if (!Character.isLetter(curr.charAt(word.length()))){//some punctuation
//						while (!Character.isLetter(curr.charAt(i)) && !Character.isDigit(curr.charAt(i)) && i<curr.length()){
//							part += curr.charAt(i);
//							i++;
//						}
//						
//						if (curr.equalsIgnoreCase(part)){//treat all of part like it's the whole word							
//							TagNode temp = new TagNode(tag, root.firstChild, root.firstChild.sibling);
//							root.firstChild = temp;
//							addTagHelper(word, tag, root);
//						}
//						else {//wordpunct+txt
//							TagNode rest = new TagNode(curr.substring(i), null, root.firstChild.sibling);
//							TagNode temp = new TagNode(tag, root.firstChild, rest);
//							root.firstChild.tag = root.firstChild.tag.substring(0,i);
//							root.firstChild = temp;
//							addTagHelper(word, tag, root);
//						}
//					}
//					else { //wordtxt
//						TagNode rest = new TagNode(curr.substring(i), null, root.firstChild.sibling);
//						TagNode temp = new TagNode(tag, root.firstChild, rest);
//						root.firstChild.tag = root.firstChild.tag.substring(0,i);
//						root.firstChild = temp;
//						addTagHelper(word, tag, root);
//					}
//				}
//				
//				else {//txtword or txtwordtxt or txtword!
//					root.firstChild.tag = part;
//					TagNode rest = new TagNode(left, null, root.firstChild);
//					root.firstChild = rest;
//					addTagHelper(word, tag, root);
//				}
//			}
//		}
//		if ((root.sibling != null) && (root.sibling.firstChild == null) && (root.sibling.tag.toLowerCase().contains(word))){ //sibling
//			String curr = root.sibling.tag.toLowerCase();
//			if (curr.equalsIgnoreCase(word)){ //whole word
//				TagNode temp = new TagNode(tag, root.sibling, root.sibling.sibling);
//				root.sibling = temp;
//				addTagHelper(word, tag, root);
//			}
//
//			else if (curr.contains(word) && curr.length() > word.length()){ //txtwordtxt
//
//				String part = "";
//				int k = curr.indexOf(word.charAt(0));
//				part = curr.substring(k,curr.indexOf(word.charAt(0))+word.length()-1);
//				String left = ""; String right = "";
//				while (!part.equalsIgnoreCase(word)){
//					k++;
//					left = curr.substring(0, k);
//					part = curr.substring(k,k+word.length()-1);
//				}
//				if (k == 0){//check for text AFTER
//					int i = word.length();
//					if (!Character.isLetter(curr.charAt(word.length()))){//some punctuation
//						while (!Character.isLetter(curr.charAt(i)) && !Character.isDigit(curr.charAt(i)) && i<curr.length()){
//							part += curr.charAt(i);
//							i++;
//						}
//						
//						if (curr.equalsIgnoreCase(part)){//treat all of part like it's the whole word							
//							TagNode temp = new TagNode(tag, root.sibling, root.sibling.sibling);
//							root.sibling = temp;
//							addTagHelper(word, tag, root);
//						}
//						else {//wordpunct+txt
//							TagNode rest = new TagNode(curr.substring(i), null, root.sibling.sibling);
//							TagNode temp = new TagNode(tag, root.sibling, rest);
//							root.sibling.tag = root.sibling.tag.substring(0,i);
//							root.sibling = temp;
//							addTagHelper(word, tag, root);
//						}
//					}
//					else { //wordtxt
//						TagNode rest = new TagNode(curr.substring(i), null, root.sibling.sibling);
//						TagNode temp = new TagNode(tag, root.sibling, rest);
//						root.sibling.tag = root.sibling.tag.substring(0,i);
//						root.sibling = temp;
//						addTagHelper(word, tag, root);
//					}
//				}
//				
//				else {//txtword or txtwordtxt or txtword!
//					root.sibling.tag = part;
//					TagNode rest = new TagNode(left, null, root.sibling);
//					root.sibling = rest;
//					addTagHelper(word, tag, root);
//				}
//			}
//		}
			
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
}
