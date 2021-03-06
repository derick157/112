package structures;

import java.util.ArrayList;

/**
 * This class is a repository of sorting methods used by the interval tree.
 * It's a utility class - all methods are static, and the class cannot be instantiated
 * i.e. no objects can be created for this class.
 * 
 * @author runb-cs112
 */
public class Sorter {

	private Sorter() { }
	
	/**
	 * Sorts a set of intervals in place, according to left or right endpoints.  
	 * At the end of the method, the parameter array list is a sorted list. 
	 * 
	 * @param intervals Array list of intervals to be sorted.
	 * @param lr If 'l', then sort is on left endpoints; if 'r', sort is on right endpoints
	 */
	public static void sortIntervals(ArrayList<Interval> intervals, char lr) {
		// COMPLETE THIS METHOD
		ArrayList<Interval> temp = new ArrayList<Interval>();
		for (Interval y : helpSort(intervals, lr)){
			temp.add(y);
		}
		intervals.clear();
		for (Interval x : temp){
			intervals.add(x);
		}
		return;
	}
	
	private static ArrayList<Interval> helpSort(ArrayList<Interval> intervals, char lr){
		if (lr == 'l'){
			for (int i=1; i<intervals.size(); i++){
				Interval x = intervals.get(i);
				int j = i-1;
				while (j>=0 && intervals.get(j).leftEndPoint > x.leftEndPoint){
					intervals.set(j+1, intervals.get(j));
					j = j-1;
				}
				intervals.set(j+1, x);
//				System.out.println(" Left " + intervals);
			}
			return intervals;
		}
		else{
			for (int i=1; i<intervals.size(); i++){
				Interval x = intervals.get(i);
				int j = i-1;
				while (j>=0 && intervals.get(j).rightEndPoint > x.rightEndPoint){
					intervals.set(j+1, intervals.get(j));
					j = j-1;
				}
				intervals.set(j+1, x);
			}
			return intervals;
		}
	}
	
	/**
	 * Given a set of intervals (left sorted and right sorted), extracts the left and right end points,
	 * and returns a sorted list of the combined end points without duplicates.
	 * 
	 * @param leftSortedIntervals Array list of intervals sorted according to left endpoints
	 * @param rightSortedIntervals Array list of intervals sorted according to right endpoints
	 * @return Sorted array list of all endpoints without duplicates
	 */
	public static ArrayList<Integer> getSortedEndPoints(ArrayList<Interval> leftSortedIntervals, ArrayList<Interval> rightSortedIntervals) {
		// COMPLETE THIS METHOD
		ArrayList<Integer> endPoints = new ArrayList<Integer>();
		
		for (Interval X : leftSortedIntervals){
			endPoints.add(X.leftEndPoint);
			System.out.println("endpoint left add " + endPoints);
		}
		
		for (int i=0; i<endPoints.size()-2; i++){
			while (endPoints.get(i) == endPoints.get(i+1)){
				endPoints.remove(i+1);
				System.out.println("endpoint delete left dupes " + endPoints);
			}
		}
		
		int pos = -1;
		boolean canAdd = true;
		for (int i=0; i<rightSortedIntervals.size()-1; i++){
			for (int j=0; j<endPoints.size(); j++){
				if (endPoints.get(j) < rightSortedIntervals.get(i).rightEndPoint){
					pos = j;
					canAdd = true;
				}
				else if (endPoints.get(j) == rightSortedIntervals.get(i).rightEndPoint){
					canAdd = false;
					break;
				}
			}
			if (canAdd){
				endPoints.add(pos+1, rightSortedIntervals.get(i).rightEndPoint);
			}
			System.out.println("endpoint right add " + endPoints);
		}
		
		return endPoints;
	}
}
