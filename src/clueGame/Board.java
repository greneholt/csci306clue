package clueGame;

import java.util.*;

public class Board {

		ArrayList<BoardCell> cells;
		Map<Character, String> rooms;
		int numRows;
		int numColumns;
		
		public void loadConfigFiles() {
			
		}
		
		public int calcIndex(int row, int col) {
			int test = 0;
			return test;
		}
		
		public RoomCell getRoomCellAt(int row, int col) {
			RoomCell test = new RoomCell();
			return test;
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

		public LinkedList<Integer> getAdjList(int calcIndex) {
			// TODO Auto-generated method stub
			return null;
		}

		public void calcTargets(int calcIndex, int i) {
			// TODO Auto-generated method stub
			
		}

		public Set<BoardCell> getTargets() {
			// TODO Auto-generated method stub
			return null;
		}
		
		
}
