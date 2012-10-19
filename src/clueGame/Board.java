package clueGame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Board {

		ArrayList<BoardCell> cells;
		Map<Character, String> rooms;
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
