package solitaire;

import java.io.IOException;
import java.util.Scanner;
import java.util.Random;
import java.util.NoSuchElementException;

/**
 * This class implements a simplified version of Bruce Schneier's Solitaire
 * Encryption algorithm.
 * 
 * @author RU NB CS112
 */
public class Solitaire {

	/**
	 * Circular linked list that is the deck of cards for encryption
	 */
	CardNode deckRear;

	/**
	 * Makes a shuffled deck of cards for encryption. The deck is stored in a
	 * circular linked list, whose last node is pointed to by the field deckRear
	 */
	public void makeDeck() {
		// start with an array of 1..28 for easy shuffling
		int[] cardValues = new int[28];
		// assign values from 1 to 28
		for (int i = 0; i < cardValues.length; i++) {
			cardValues[i] = i + 1;
		}

		// shuffle the cards
		Random randgen = new Random();
		for (int i = 0; i < cardValues.length; i++) {
			int other = randgen.nextInt(28);
			int temp = cardValues[i];
			cardValues[i] = cardValues[other];
			cardValues[other] = temp;
		}

		// create a circular linked list from this deck and make deckRear point
		// to its last node
		CardNode cn = new CardNode();
		cn.cardValue = cardValues[0];
		cn.next = cn;
		deckRear = cn;
		for (int i = 1; i < cardValues.length; i++) {
			cn = new CardNode();
			cn.cardValue = cardValues[i];
			cn.next = deckRear.next;
			deckRear.next = cn;
			deckRear = cn;
		}
	}

	/**
	 * Makes a circular linked list deck out of values read from scanner.
	 */
	public void makeDeck(Scanner scanner) throws IOException {
		CardNode cn = null;
		if (scanner.hasNextInt()) {
			cn = new CardNode();
			cn.cardValue = scanner.nextInt();
			cn.next = cn;
			deckRear = cn;
		}
		while (scanner.hasNextInt()) {
			cn = new CardNode();
			cn.cardValue = scanner.nextInt();
			cn.next = deckRear.next;
			deckRear.next = cn;
			deckRear = cn;
		}
	}

//	private CardNode prev(CardNode curr){
//		CardNode previous;
//		for (previous = curr.next; previous != curr; previous = previous.next);
//		return previous;
//	}
//	
//	private void add(CardNode rear, CardNode toAdd) {
//		toAdd.next = rear.next;
//		rear.next = toAdd;
//	}
//	
//	private void delete(CardNode rear){		
//		prev(rear).next = rear.next;
//	}
//
//	private CardNode search(int key, CardNode rear){		
//		//check to see if CLL is empty
//		if (rear == null){
//			return null;
//		}
//		CardNode value = rear;
//		//sequential search
//		do{
//			if (value.cardValue == key){
//				return value;
//			}
//			value = value.next;
//		}
//		while (value != rear);
//		return null;
//	}
//	
//	private void swap(CardNode first){
//		//delete node in between 1 and 3
//		CardNode temp = first;
//		add(first.next, temp);
//		delete(first);
//	}

	/**
	 * Implements Step 1 - Joker A - on the deck.
	 */
	void jokerA() {
		// COMPLETE THIS METHOD
		//printList(deckRear);
		
		//search for 27
		CardNode firstJoker;
		if (deckRear == null){
			return;
		}
		firstJoker = deckRear;
		do {
			if (firstJoker.cardValue == 27){
				break;
			}
			firstJoker = firstJoker.next;
		}
		while (firstJoker != deckRear);
		
		//switch the values of the first joker and the node after it
		int temp = firstJoker.cardValue;
		firstJoker.cardValue = firstJoker.next.cardValue;
		firstJoker.next.cardValue = temp;
		
		printList(deckRear);
		return;
	}

	/**
	 * Implements Step 2 - Joker B - on the deck.
	 */
	void jokerB() {
		// COMPLETE THIS METHOD
		//search for 28
		//printList(deckRear);
		
		CardNode secondJoker;
		if (deckRear == null){
			return;
		}
		secondJoker = deckRear;
		do {
			if (secondJoker.cardValue == 28){
				break;
			}
			secondJoker = secondJoker.next;
		}
		while (secondJoker != deckRear);

		//switch 28 with value in next node
		int temp = secondJoker.cardValue;
		secondJoker.cardValue = secondJoker.next.cardValue;
		secondJoker.next.cardValue = temp;

		//search for 28 again
		if (deckRear == null){
			return;
		}
		secondJoker = deckRear;
		do {
			if (secondJoker.cardValue == 28){
				break;
			}
			secondJoker = secondJoker.next;
		}
		while (secondJoker != deckRear);
	
		//switch 28 with the value of the next node
		temp = secondJoker.cardValue;
		secondJoker.cardValue = secondJoker.next.cardValue;
		secondJoker.next.cardValue = temp;
		
		printList(deckRear);
		return;		
	}

	/**
	 * Implements Step 3 - Triple Cut - on the deck.
	 */
	void tripleCut() {
		// COMPLETE THIS METHOD
		//find first joker
		CardNode firstJoker = deckRear;
		do {
			if (firstJoker.cardValue == 27){
				break;
			}
			firstJoker = firstJoker.next;
		}
		while (firstJoker != deckRear);

		//find second joker
		CardNode secondJoker = deckRear;
		do {
			if (secondJoker.cardValue == 28){
				break;
			}
			secondJoker = secondJoker.next;
		}
		while (secondJoker != deckRear);
		
		
		//joker A is at front
		if (deckRear.next.cardValue == 27){
			deckRear = secondJoker;
			deckRear.next = secondJoker.next;
			printList(deckRear);
			return;
		}
		
		//joker B at front
		else if (deckRear.next.cardValue == 28){
			deckRear = firstJoker;
			deckRear.next = firstJoker.next;
			printList(deckRear);
			return;
		}
		
		//joker A is at rear
		else if (deckRear.cardValue == 27){
			CardNode previous = null;
			for (CardNode ptr = secondJoker.next; ptr != secondJoker; ptr = ptr.next){
				previous = ptr;
			}
			deckRear = previous;
			printList(deckRear);
			return;
		}
		
		//joker B is at rear
		else if (deckRear.cardValue == 28){
			CardNode previous = null;
			for (CardNode ptr = firstJoker.next; ptr != firstJoker; ptr = ptr.next){
				previous = ptr;
			}
//			deckRear.next = firstJoker;
			deckRear = previous;
			printList(deckRear);
			return;

		}
				
		//first at front AND second at rear
		else if (deckRear.next.cardValue == 27 && deckRear.cardValue == 28){
			return;
		}
				
		//otherwise - 27 and 28 are both somewhere in the middle
		else {

			boolean twentyEightFirst = false;
			for (CardNode ptr = secondJoker; ptr != deckRear; ptr = ptr.next){
				if (ptr.cardValue == 27){
					twentyEightFirst = true;
					break;
				}
			}
			
			int lastVal = deckRear.next.next.cardValue;
			
			//joker b comes first
			if (twentyEightFirst){
				deckRear = firstJoker;
				deckRear.next = firstJoker.next;
								
//				boolean engageTransfer = false;
				CardNode firstMove = deckRear.next;
				for (CardNode ptr = deckRear.next; ptr.cardValue != lastVal; ptr = ptr.next){
//					if (ptr.cardValue == lastVal){
//						engageTransfer = true;
//					}
//					
//					if (ptr.cardValue == 27){
//						engageTransfer = false;
//					}
					
//					while (engageTransfer){
					firstMove = ptr;
				}
				
				for (CardNode ptr = firstMove; ptr.cardValue != 27; ptr = ptr.next){
					CardNode temp = new CardNode();
					temp.cardValue = ptr.cardValue;
					temp.next = deckRear.next;
					deckRear.next = temp;
					deckRear = temp;
					
					//find previous , delete ptr
					
					CardNode previous = null;
					for (CardNode point = ptr.next; point != ptr; point = point.next){
						previous = point;
					}
					previous.next = ptr.next;

				}
				
				printList(deckRear);
				return;
			}
			
			//joker a comes first
			else{
				deckRear = secondJoker;
				deckRear.next = secondJoker.next;
				
//				boolean engageTransfer = false;
				CardNode firstMove = deckRear.next;
				for (CardNode ptr = deckRear.next; ptr.cardValue != lastVal; ptr = ptr.next){
//					if (ptr.cardValue == lastVal){
//						engageTransfer = true;
//					}
//					
//					if (ptr.cardValue == 27){
//						engageTransfer = false;
//					}
					
//					while (engageTransfer){
					firstMove = ptr;
				}
				
				for (CardNode ptr = firstMove; ptr.cardValue != 27; ptr = ptr.next){
					CardNode temp = new CardNode();
					temp.cardValue = ptr.cardValue;
					temp.next = deckRear.next;
					deckRear.next = temp;
					deckRear = temp;
					
					//find previous , delete ptr
					
					CardNode previous = null;
					for (CardNode point = ptr.next; point != ptr; point = point.next){
						previous = point;
					}
					previous.next = ptr.next;

				}
				
				printList(deckRear);
				return;
			}
		}
		
		
	}

	/**
	 * Implements Step 4 - Count Cut - on the deck.
	 */
	void countCut() {
		// COMPLETE THIS METHOD
		
		/*
		 * copy value of last node
		 * delete last node, have deckRearPrev point to deckRear.next
		 * count "value" times from new rear pointer and add new node with old last value after
		 * make new node with old last value new deckRear
		 * */
		int lastValue = deckRear.cardValue, count = 0;
		//if last card is 28
		if (deckRear.cardValue == 28){
			count = 27;
		}
		else {
			count = deckRear.cardValue;
		}
		
		CardNode previous = null;
		for (CardNode ptr = deckRear.next; ptr != deckRear; ptr = ptr.next){
			previous = ptr;
		}
		
		previous.next = deckRear.next;
		deckRear = previous;
		
		CardNode newPrev = deckRear;
		for (int i=0; i<count; i++){
			newPrev = newPrev.next;
		}
		
		CardNode newLast = new CardNode();
		newLast.cardValue = lastValue;
		
		newLast.next = newPrev.next;
		newPrev.next = newLast;
		deckRear = newLast;
		
		printList(deckRear);
		return;		
	}

	/**
	 * Gets a key. Calls the four steps - Joker A, Joker B, Triple Cut, Count
	 * Cut, then counts down based on the value of the first card and extracts
	 * the next card value as key, but if that value is 27 or 28, repeats the
	 * whole process.
	 * 
	 * @return Key between 1 and 26
	 */
	int getKey() {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		int firstValue=deckRear.next.cardValue, key=0;
		if (firstValue == 28){
			firstValue = 27;
		}
		
		for (CardNode ptr = deckRear.next ; ptr != deckRear; ptr = ptr.next){
			
		}
		
		if (key == 27 || key == 28){
			jokerA();
			jokerB();
			tripleCut();
			countCut();
				
		
			CardNode ptr = deckRear;
		
		
			for (int i=0; i<=firstValue; i++){
				ptr = ptr.next;
			}
			key = ptr.next.cardValue;
		}
			
		//printList(deckRear);
//		System.out.println("^THE FINAL LIST AND the KEY " + key);
		return key;
	}

	/**
	 * Utility method that prints a circular linked list, given its rear pointer
	 * 
	 * @param rear
	 *            Rear pointer
	 */
	private static void printList(CardNode rear) {
		if (rear == null) {
			return;
		}
		System.out.print(rear.next.cardValue);
		CardNode ptr = rear.next;
		do {
			ptr = ptr.next;
			System.out.print("," + ptr.cardValue);
		} while (ptr != rear);
		System.out.println("\n");
	}

	/**
	 * Encrypts a message, ignores all characters except upper case letters
	 * 
	 * @param message
	 *            Message to be encrypted
	 * @return Encrypted message, a sequence of upper case letters only
	 */
	public String encrypt(String message) {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		
		//int length = 0;
		String onlyLetters = "";
		
		for (int i=0; i<message.length(); i++){
			if (Character.isLetter(message.charAt(i))){
				//length++;
				Character.toUpperCase(message.charAt(i));
				onlyLetters += message.charAt(i);
			}			
		}		
		
		String encrypted = "";
		for (int i=0; i<onlyLetters.length(); i++){
			int encryptNum = (onlyLetters.charAt(i)-64)+(getKey());
			if (encryptNum > 26){
				encryptNum -= 26;
			}
			encryptNum += 64;
			encrypted += (char)encryptNum;
		}
		return encrypted;
	}

	/**
	 * Decrypts a message, which consists of upper case letters only
	 * 
	 * @param message
	 *            Message to be decrypted
	 * @return Decrypted message, a sequence of upper case letters only
	 */
	public String decrypt(String message) {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		
		String onlyLetters = "";
		
		for (int i=0; i<message.length(); i++){
			if (Character.isLetter(message.charAt(i))){
				//length++;
				Character.toUpperCase(message.charAt(i));
				onlyLetters += message.charAt(i);
			}			
		}	
		
		String decrypted = "";
		for (int i=0; i<onlyLetters.length(); i++){
			
			int decryptNum = (int)onlyLetters.charAt(i);
			decryptNum -= 64;
			decryptNum -= getKey();
			if (decryptNum <= 0){
				decryptNum += 26;
			}
			decryptNum += 64;
			decrypted += (char)decryptNum;
		}
		return decrypted;
		
	}
}
