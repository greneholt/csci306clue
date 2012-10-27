package clueGame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

import clueGame.RoomCell.DoorDirection;

public class Board {
	ArrayList<BoardCell> cells = new ArrayList<BoardCell>();
	Map<Character, String> rooms = new TreeMap<Character, String>();
	Map<Integer, LinkedList<Integer>> adjacencies = new HashMap<Integer, LinkedList<Integer>>();
	Set<BoardCell> targets = new HashSet<BoardCell>();
	Set<Integer> path = new HashSet<Integer>();
	Set<Player> players = new HashSet<Player>(); // contains all players
	HumanPlayer human = new HumanPlayer();
	Set<Card> deck = new HashSet<Card>();
	Solution solution;
	
	private int numRows;

	private int numColumns;

	public int calcIndex(int row, int col) {
		int index = (row * numColumns) + col;
		return index;
	}
	
	private void calcTargets(int calcIndex, int steps) {
		for (Integer neighbor : getAdjList(calcIndex)) {
			if (path.contains(neighbor))
				continue;

			path.add(neighbor);

			// we include the initial cell in the path, so the path size has to exceed steps by one
			if (path.size() > steps || getCellAt(neighbor).isDoorway()) {
				targets.add(getCellAt(neighbor));
			} else {
				calcTargets(neighbor, steps);
			}
			path.remove(neighbor);
		}
	}

	public void deal() {

	}

	public void deal(ArrayList<String> cardlist) {

	}

	public LinkedList<Integer> getAdjList(int i) {
		if (adjacencies.containsKey(i)) {
			return adjacencies.get(i);
		}

		LinkedList<Integer> neighbors = new LinkedList<Integer>();
		int column = i % numColumns;
		int row = i / numColumns;
		BoardCell adj;
		BoardCell cell = getCellAt(i);

		adjacencies.put(i, neighbors);

		if (cell.isRoom()) {
			// only doorways have adjacencies, and they only have one, so short-circuit

			// if a door was placed facing an edge, this would be invalid, but the board should not be set up that way
			if (cell.isDoorway()) {
				switch (((RoomCell) cell).getDoorDirection()) {
				case RIGHT:
					neighbors.add(i + 1);
					break;
				case LEFT:
					neighbors.add(i - 1);
					break;
				case UP:
					neighbors.add(i - numColumns);
					break;
				case DOWN:
					neighbors.add(i + numColumns);
					break;
				}
			}
			return neighbors;
		}

		if (column > 0) {
			adj = getCellAt(i - 1);// the cell to the left

			if (adj.isWalkway() || (adj.isDoorway() && ((RoomCell) adj).getDoorDirection() == DoorDirection.RIGHT)) {
				neighbors.add(i - 1);
			}
		}

		if (column < numColumns - 1) {
			adj = getCellAt(i + 1);// the cell to the right

			if (adj.isWalkway() || (adj.isDoorway() && ((RoomCell) adj).getDoorDirection() == DoorDirection.LEFT)) {
				neighbors.add(i + 1);
			}
		}

		if (row > 0) {
			adj = getCellAt(i - numColumns);// the cell above

			if (adj.isWalkway() || (adj.isDoorway() && ((RoomCell) adj).getDoorDirection() == DoorDirection.DOWN)) {
				neighbors.add(i - numColumns);
			}
		}

		if (row < numRows - 1) {
			adj = getCellAt(i + numColumns);// the cell below

			if (adj.isWalkway() || (adj.isDoorway() && ((RoomCell) adj).getDoorDirection() == DoorDirection.UP)) {
				neighbors.add(i + numColumns);
			}
		}

		return neighbors;
	}

	public Set<Card> getDeck() {
		return deck;
	}

	public BoardCell getCellAt(int i) {
		return cells.get(i);
	}
	
	public BoardCell getCellAt(int row, int col) {
		return cells.get(calcIndex(row, col));
	}

	public ArrayList<BoardCell> getCells() {
		return cells;
	}

	public HumanPlayer getHuman() {
		return human;
	}
	
	public int getNumColumns() {
		return numColumns;
	}

	public int getNumRows() {
		return numRows;
	}
	
	public Set<Player> getPlayers() {
		return players;
	}
	
	public RoomCell getRoomCellAt(int row, int col) {
		if (cells.get(this.calcIndex(row, col)).isRoom()) {
			return (RoomCell) cells.get(this.calcIndex(row, col));
		} else {
			return null;
		}
	}

	public Map<Character, String> getRooms() {
		return rooms;
	}

	public Solution getSolution() {
		return solution;
	}

	public Set<BoardCell> getTargets(int i, int steps) {
		targets = new HashSet<BoardCell>();
		path.clear();
		path.add(i);
		calcTargets(i, steps);
		return targets;
	}

	public boolean checkAccusation(Card person, Card weapon, Card room) {
		return false;
	}

	public boolean handleSuggestion(Card person, Card weapon, Card room) {
		return false;
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

	public void loadCards(String cards) {
		// TODO stub
	}

	public void loadConfigFiles(String legendFile, String boardFile) throws BadConfigFormatException, FileNotFoundException {
		loadLegend(legendFile);
		loadBoard(boardFile);
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

	public void loadPlayers(String playersFile) {
		// TODO stub
	}

	public void selectAnswer() {

	}

	public void setSolution(Solution solution) {
		this.solution = solution;
	}
}
