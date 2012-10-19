package clueGame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Board {

		ArrayList<BoardCell> cells;
		Map<Character, String> rooms;
		Map<BoardCell, LinkedList<BoardCell>> map = new HashMap<BoardCell, LinkedList<BoardCell>>();
		Map<BoardCell, LinkedList<BoardCell>> mapc;
		Set<BoardCell> targetSet = new HashSet<BoardCell>();
		LinkedList<BoardCell> seen = new LinkedList<BoardCell>();
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
			// TODO
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
			// TODO Auto-generated method stub
			return null;
		}

		public LinkedList<BoardCell> getAdjList(int calcIndex) {
			return map.get(calcIndex);
		}

		public void calcTargets(BoardCell calcIndex, int i) {
			while(mapc.get(calcIndex).size() != 0) {
				BoardCell target = mapc.get(calcIndex).removeFirst();
				seen.add(target);
				if(seen.size() == i) {
					targetSet.add(target);
				}else {
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
			for(int x=0; x < numColumns; x++) {
				for(int y=0; y < numRows; y++) {
					
					int index = calcIndex(x,y);
					if (this.getCellAt(index).isWalkway()) {	// make sure cell is a walkway
						LinkedList<BoardCell> listPoint = new LinkedList<BoardCell>();
						
						if (x != 0 && this.getCellAt(calcIndex(x-1, y)).isWalkway()) { // add left cell
							listPoint.add(this.getCellAt(calcIndex(x-1, y)));
						}
						if (y != 0 && this.getCellAt(calcIndex(x, y-1)).isWalkway()) { // add above cell
							listPoint.add(this.getCellAt(calcIndex(x, y-1)));
						}
						if (x != numColumns-1 && this.getCellAt(calcIndex(x+1, y)).isWalkway()) { // add right cell
							listPoint.add(this.getCellAt(calcIndex(x+1, y)));
						}
						if (y != numRows-1 && this.getCellAt(calcIndex(x,y+1)).isWalkway()) { // add below cell
							listPoint.add(this.getCellAt(calcIndex(x,y+1)));
						}
						map.put(this.getCellAt(index), listPoint);	// add adjacency list for cell to map
					}
				}
			}
			mapc = new HashMap<BoardCell, LinkedList<BoardCell>>(map);	// create copy of may for later use
		}
}
