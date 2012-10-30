package clueGame;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import clueGame.Card.CardType;
import clueGame.RoomCell.DoorDirection;

public class Board {
	private ArrayList<BoardCell> cells = new ArrayList<BoardCell>();
	private Map<Character, String> rooms = new TreeMap<Character, String>();
	private Map<Integer, LinkedList<Integer>> adjacencies = new HashMap<Integer, LinkedList<Integer>>();
	private Set<BoardCell> targets = new HashSet<BoardCell>();
	private Set<Integer> path = new HashSet<Integer>();
	private Set<Player> players = new HashSet<Player>(); // contains all players
	private HumanPlayer human = new HumanPlayer();
	private Set<Card> deck = new TreeSet<Card>();
	private Solution solution;
	private Card lastshown;

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

	/**
	 *
	 * @param person
	 * @param weapon
	 * @param room
	 * @return false if no card is shown
	 */
	public boolean handleSuggestion(Card person, Card weapon, Card room) {
		return false;
		// lastshown = randomperson.disproveSuggestion(...);
	}

	private void loadBoard(String boardFile) throws BadConfigFormatException, IOException {
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

		scan.close();
		reader.close();
	}

	private void loadWeapons(String weaponsFile) throws IOException {
		FileReader reader = new FileReader(weaponsFile);
		Scanner scan = new Scanner(reader);

		while (scan.hasNextLine()) {
			deck.add(new Card(scan.nextLine(), CardType.WEAPON));
		}

		scan.close();
		reader.close();
	}

	public void loadConfigFiles(String legendFile, String boardFile, String weaponsFile, String playersFile) throws BadConfigFormatException, IOException {
		loadLegend(legendFile);
		loadBoard(boardFile);
		loadPlayers(playersFile);
		loadWeapons(weaponsFile);
	}

	private void loadLegend(String legendFile) throws BadConfigFormatException, IOException {
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

		scan.close();
		reader.close();

		for (String room : rooms.values()) {
			deck.add(new Card(room, CardType.ROOM));
		}
	}

	private void loadPlayers(String playersFile) throws BadConfigFormatException, IOException {
		clearPlayers();
		FileReader reader = new FileReader(playersFile);
		Scanner scan = new Scanner(reader);
		String[] line = scan.nextLine().split(",");
		if (line.length != 3)
			throw new BadConfigFormatException("Wrong number of data in first line of file: " + playersFile + ".");
		HumanPlayer dude = new HumanPlayer();
		dude.setName(line[0]);
		dude.setPieceColor(Color.decode(line[1]));
		dude.setCellIndex(Integer.parseInt(line[2]));
		human = dude;
		players.add(dude);
		while (scan.hasNextLine()) {
			line = scan.nextLine().split(",");
			if (line.length != 3)
				throw new BadConfigFormatException("Wrong number of data in line " + " of file: " + playersFile + ".");
			ComputerPlayer droid = new ComputerPlayer();
			droid.setName(line[0]);
			droid.setPieceColor(Color.decode(line[1]));
			droid.setCellIndex(Integer.parseInt(line[2]));
			players.add(droid);
		}

		scan.close();
		reader.close();

		for (Player person : players) {
			deck.add(new Card(person.getName(), CardType.PERSON));
		}
	}

	public void clearPlayers() {
		players.clear();
	}

	public void selectAnswer() {

	}

	public void setSolution(Solution solution) {
		this.solution = solution;
	}

	public Card getLastshown() {
		return lastshown;
	}
}
