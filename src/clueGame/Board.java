package clueGame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Board {

		ArrayList<BoardCell> cells;
		Map<Character, String> rooms;
		Map<Integer, LinkedList<Integer>> map = new HashMap<Integer, LinkedList<Integer>>();
		Map<Integer, LinkedList<Integer>> mapc;
		Set<BoardCell> targetSet = new HashSet<BoardCell>();
		LinkedList<Integer> seen = new LinkedList<Integer>();
		int numRows;
		int numColumns;
		
		public Board() {
			try {
				loadConfigFiles();
			} catch (BadConfigFormatException e) {
				System.out.println(e.getMessage());
			}
		}
		
		public void loadConfigFiles() throws BadConfigFormatException {
			FileReader reader = null;
			try {
				reader = new FileReader("ClueBoardLegend.txt");
			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			Scanner scan = new Scanner(reader);
			//TODO: populate legend map
			
			try {
				reader = new FileReader("ClueBoardLayout.csv");
			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			scan = new Scanner(reader);
			//TODO: populate cell list
			
		}
		
		public int calcIndex(int row, int col) {
			int index = (row + 1)*col;
			return index;
		}
		
		public RoomCell getRoomCellAt(int row, int col) {
			RoomCell room = new RoomCell();
			return room;
		}

		public ArrayList<BoardCell> getCells() {
			return cells;
		}

		public Map<Character, String> getRooms() {
			return rooms;
		}

		public int getNumRows() {
			return numRows;
		}

		public int getNumColumns() {
			return numColumns;
		}

		public BoardCell getCellAt(int calcIndex) {
			return cells.get(calcIndex);
		}

		public LinkedList<Integer> getAdjList(int calcIndex) {
			return map.get(calcIndex);
		}

		public void calcTargets(int calcIndex, int i) {
			while(mapc.get(this.getCellAt(calcIndex)).size() != 0) {
				int target = mapc.get(calcIndex).removeFirst();
				seen.add(target);
				if (seen.size() == i) {
					targetSet.add(this.getCellAt(target));
				} else {
					mapc.get(target).removeFirstOccurrence(calcIndex);
					calcTargets(target, i);
					mapc.get(target).add(calcIndex);
				}
				seen.removeLast();
			}
		}

		public Set<BoardCell> getTargets() {
			return targetSet;
		}
		
		public void calcAdjacencies() {
			for(int x=0; x < this.getNumColumns(); x++) {
				for(int y=0; y < this.getNumRows(); y++) {
					int index = calcIndex(x,y);
					LinkedList<Integer> listPoint = new LinkedList<Integer>();
					if (this.getCellAt(index).isWalkway()) {	// make sure cell is a walkway
						if (x != 0 && (this.getCellAt(calcIndex(x-1, y)).isWalkway() ||
								this.getCellAt(calcIndex(x-1, y)).isDoorway())) { // add left cell
							listPoint.add(this.getCellAt(calcIndex(x-1, y)).getIndex());
						}
						if (y != 0 && (this.getCellAt(calcIndex(x, y-1)).isWalkway() ||
								this.getCellAt(calcIndex(x, y-1)).isDoorway())) { // add above cell
							listPoint.add(this.getCellAt(calcIndex(x, y-1)).getIndex());
						}
						if (x != this.getNumColumns()-1 && (this.getCellAt(calcIndex(x+1, y)).isWalkway()
								|| this.getCellAt(calcIndex(x+1, y)).isDoorway())) { // add right cell
							listPoint.add(this.getCellAt(calcIndex(x+1, y)).getIndex());
						}
						if (y != this.getNumRows()-1 && (this.getCellAt(calcIndex(x,y+1)).isWalkway()
								|| this.getCellAt(calcIndex(x,y+1)).isDoorway())) { // add below cell
							listPoint.add(this.getCellAt(calcIndex(x,y+1)).getIndex());
						}
					} else if (this.getCellAt(index).isDoorway()) {
						if (this.getRoomCellAt(y, x).getDoorDirection() == "UP") {
							listPoint.add(this.getCellAt(calcIndex(x, y-1)).getIndex());
						} else if (this.getRoomCellAt(y, x).getDoorDirection() == "DOWN") {
							listPoint.add(this.getCellAt(calcIndex(x,y+1)).getIndex());
						} else if (this.getRoomCellAt(y, x).getDoorDirection() == "RIGHT") {
							listPoint.add(this.getCellAt(calcIndex(x+1, y)).getIndex());
						} else if (this.getRoomCellAt(y, x).getDoorDirection() == "LEFT") {
							listPoint.add(this.getCellAt(calcIndex(x-1, y)).getIndex());
						}
					}
					map.put(index, listPoint);	// add adjacency list for cell to map
				}
			}
			mapc = new HashMap<Integer, LinkedList<Integer>>(map);	// create copy of may for later use
		}
}
