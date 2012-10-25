package clueGame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

import org.junit.runner.Computer;

import clueGame.RoomCell.DoorDirection;

public class Board {
	ArrayList<BoardCell> cells = new ArrayList<BoardCell>();
	Map<Character, String> rooms = new TreeMap<Character, String>();
	Map<Integer, LinkedList<Integer>> map = new HashMap<Integer, LinkedList<Integer>>();
	Set<BoardCell> targetSet = new HashSet<BoardCell>();
	LinkedList<Integer> path = new LinkedList<Integer>();
	Set<ComputerPlayer> opponents = new HashSet<ComputerPlayer>();
	HumanPlayer you = new HumanPlayer();
	Set<Card> deck = new HashSet<Card>();
	int numRows;
	int numColumns;
	int firstSpot;

	public void selectAnswer(){
		
	}
	public void deal(){

	}
	public void deal(ArrayList<String> cardlist){
		
	}
	public boolean handleSuggestion(String person, String weapon, String room, boolean accuse){
		//I don't see the need to have two separate functions for suggestions and accusation.
		return false;
	}

	public void loadConfigFiles(String legendFile, String boardFile) throws BadConfigFormatException, FileNotFoundException {
		loadLegend(legendFile);
		loadBoard(boardFile);
		calcAdjacencies();
	}

	private void loadBoard(String boardFile) throws BadConfigFormatException, FileNotFoundException {
		FileReader reader = new FileReader(boardFile);
		Scanner scan = new Scanner(reader);
		// populate cell list
		int j = 0;
		while (scan.hasNext()) {
			String wholeString = scan.nextLine();
			if (!wholeString.contains(",")) {
				throw new BadConfigFormatException();
			}
			String[] strArr = wholeString.split(",");
			for (int i = 0; i < strArr.length; i++) {
				String str = strArr[i];
				if (str.equalsIgnoreCase("W")) {
					WalkwayCell wc = new WalkwayCell();
					wc.setCol(i);
					wc.setRow(j);
					wc.setBoardWidth(strArr.length);
					cells.add(wc);
				} else {
					RoomCell rc = new RoomCell();
					if (str.length() == 2) {
						char d = str.charAt(1);
						rc.setDoorDirection(d);
					}
					rc.setCol(i);
					rc.setRow(j);
					rc.setBoardWidth(strArr.length);
					rc.setRoom(str.charAt(0));
					cells.add(rc);
				}
			}
			numColumns = strArr.length;
			++j;
		}
		numRows = j;
	}

	private void loadLegend(String legendFile) throws BadConfigFormatException, FileNotFoundException {
		FileReader reader = new FileReader(legendFile);
		Scanner scan = new Scanner(reader);
		// populate legend map
		while (scan.hasNext()) {
			String wholeString = scan.nextLine();
			if (!wholeString.contains(", ")) {
				throw new BadConfigFormatException();
			}
			String[] stringArr = wholeString.split(", ");
			String character = stringArr[0];
			String room = stringArr[1];
			char c = character.charAt(0);
			rooms.put(c, room);
		}
	}

	public int calcIndex(int row, int col) {
		int index = (row * numColumns) + col;
		return index;
	}

	public RoomCell getRoomCellAt(int row, int col) {
		if (cells.get(this.calcIndex(row, col)).isRoom()) {
			return (RoomCell) cells.get(this.calcIndex(row, col));
		} else {
			return null;
		}
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

	public void calcTargets(int calcIndex, int steps) {
		Map<Integer, LinkedList<Integer>> mapc = new HashMap<Integer, LinkedList<Integer>>(map); // create copy of map
		for (Integer neighbor : mapc.get(calcIndex)) {
			if (!path.contains(calcIndex)) { // add cell at calcIndex to path if not in path
				path.add(calcIndex);
			}
			if (path.size() >= steps) { // if number of steps (roll) has been reached, add the cell at target
				if (!path.contains(neighbor)) {
					targetSet.add(getCellAt(neighbor));
				}
			} else if (getCellAt(neighbor).isDoorway()) {
				targetSet.add(getCellAt(neighbor));
			} else {
				if (!path.contains(neighbor)) {
					mapc.get(neighbor).removeFirstOccurrence(calcIndex);
					calcTargets(neighbor, steps);
					mapc.get(neighbor).add(calcIndex);
				} else {
					break;
				}
			}
			path.removeLast();
		}
	}

	public Set<BoardCell> getTargets() {
		Set<BoardCell> targetSet1 = new HashSet<BoardCell>(targetSet);
		targetSet = new HashSet<BoardCell>();
		calcAdjacencies();
		path.clear();
		return targetSet1;
	}

	public void calcAdjacencies() {
		for (int i = 0; i < numRows * numColumns; i++) {
			LinkedList<Integer> cells = new LinkedList<Integer>();
			int column = i % numColumns;
			int row = i / numColumns;
			BoardCell adj;
			BoardCell cell = getCellAt(i);

			map.put(i, cells);

			// only doorways have adjacencies, and they only have one, so short-circuit
			if (cell.isRoom()) {
				// if a door was placed facing an edge, this would be invalid, but the board should not be set up that way
				if (cell.isDoorway()) {
					switch (((RoomCell) cell).getDoorDirection()) {
					case RIGHT:
						cells.add(i + 1);
						break;
					case LEFT:
						cells.add(i - 1);
						break;
					case UP:
						cells.add(i - numColumns);
						break;
					case DOWN:
						cells.add(i + numColumns);
						break;
					}
				}
				continue;
			}

			if (column > 0) {
				adj = getCellAt(i - 1);// the cell to the left

				if (adj.isWalkway() || (adj.isDoorway() && ((RoomCell) adj).getDoorDirection() == DoorDirection.RIGHT)) {
					cells.add(i - 1);
				}
			}

			if (column < numColumns - 1) {
				adj = getCellAt(i + 1);// the cell to the right

				if (adj.isWalkway() || (adj.isDoorway() && ((RoomCell) adj).getDoorDirection() == DoorDirection.LEFT)) {
					cells.add(i + 1);
				}
			}

			if (row > 0) {
				adj = getCellAt(i - numColumns);// the cell above

				if (adj.isWalkway() || (adj.isDoorway() && ((RoomCell) adj).getDoorDirection() == DoorDirection.DOWN)) {
					cells.add(i - numColumns);
				}
			}

			if (row < numRows - 1) {
				adj = getCellAt(i + numColumns);// the cell below

				if (adj.isWalkway() || (adj.isDoorway() && ((RoomCell) adj).getDoorDirection() == DoorDirection.UP)) {
					cells.add(i + numColumns);
				}
			}
		}
	}

	public Board() {
		try {
			loadConfigFiles("board.csv","legend.csv");
		} catch (BadConfigFormatException e) {
			System.out.println(e.getMessage());
		}catch (FileNotFoundException f){
			//The other students forgot this.
		}
		this.calcAdjacencies();
	}
	
	//The following are just for use by the JUnit tests.
	public Map<Integer, LinkedList<Integer>> getMap() {
		return map;
	}
	public Set<BoardCell> getTargetSet() {
		return targetSet;
	}
	public LinkedList<Integer> getPath() {
		return path;
	}
	public Set<ComputerPlayer> getOpponents() {
		return opponents;
	}
	public HumanPlayer getYou() {
		return you;
	}
	public Set<Card> getDeck() {
		return deck;
	}
	public int getFirstSpot() {
		return firstSpot;
	}

}
