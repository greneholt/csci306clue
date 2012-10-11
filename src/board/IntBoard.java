package board;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class IntBoard {
	
	Map<Integer, LinkedList<Integer>> map = new HashMap<Integer, LinkedList<Integer>>();
	Map<Integer, LinkedList<Integer>> mapc;
	Set<Integer> targetSet = new HashSet<Integer>();
	LinkedList<Integer> seen = new LinkedList<Integer>();
	
	public static int BOARD_WIDTH = 4;
	public static int BOARD_HEIGHT = 4;
	
	public IntBoard() {
		super();
	}
	
	public void calcAdjacencies() {
		for(int x=0; x < BOARD_WIDTH; x++) {
			for(int y=0; y < BOARD_HEIGHT; y++) {
				LinkedList<Integer> listPoint = new LinkedList<Integer>();
				int index = calcIndex(x,y);
				if(x != 0) {
					listPoint.add(calcIndex(x-1, y));
				}
				if( y != 0) {
					listPoint.add(calcIndex(x, y-1));
				}
				if( x != BOARD_WIDTH-1) {
					listPoint.add(calcIndex(x+1, y));
				}
				if( y != BOARD_HEIGHT-1) {
					listPoint.add(calcIndex(x,y+1));
				}
				map.put(index, listPoint);
			}
		}
		mapc = new HashMap<Integer, LinkedList<Integer>>(map);
		
	}
	
	public void calcTargets(int location, int steps) {
		while(mapc.get(location).size() != 0) {
			int target = mapc.get(location).removeFirst();
			seen.add(target);
			if(seen.size() == steps) {
				targetSet.add(target);
			}else {
				mapc.get(target).removeFirstOccurrence(location);
				calcTargets(target, steps);
				mapc.get(target).add(location);
			}
			seen.removeLast();
		}
	}
	
	public Set<Integer> getTargets() {
		return targetSet;
	}
	
	public LinkedList<Integer> getAdjList(int index) {
		return map.get(index);
	}
	
	public int calcIndex(int row, int col) {
		int index = (row*BOARD_WIDTH) + col;
		return index;
	}
	
	public void printTarg() {
		for(int i : targetSet) {
			System.out.print(i + " ");
		}
		System.out.println();
	}
}
