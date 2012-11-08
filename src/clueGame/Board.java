package clueGame;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.swing.JPanel;

import clueGame.Card.CardType;
import clueGame.RoomCell.DoorDirection;

public class Board extends JPanel {
	private Map<Integer, LinkedList<Integer>> adjacencies = new HashMap<Integer, LinkedList<Integer>>();
	private List<Card> cards = new LinkedList<Card>();
	private ArrayList<BoardCell> cells = new ArrayList<BoardCell>();
	private HumanPlayer human;
	private int numColumns;
	private int numRows;
	private Set<Integer> path = new HashSet<Integer>();
	private List<Player> players = new ArrayList<Player>(); // contains all players
	private Map<Character, String> rooms = new TreeMap<Character, String>();

	private CardSet solution;

	private Set<BoardCell> targets = new HashSet<BoardCell>();

	public int calcIndex(int row, int col) {
		int index = (row * numColumns) + col;
		return index;
	}

	public boolean checkAccusation(Card person, Card weapon, Card room) {
		if (person.equals(solution.getPerson()) && weapon.equals(solution.getWeapon()) && room.equals(solution.getRoom())) {
			return true;
		} else {
			return false;
		}
	}

	public void clearPlayers() {
		players.clear();
	}

	public void deal() {
		if (players.size() == 0)
			return;

		List<Card> deck = new LinkedList<Card>(cards); // duplicate the cards list for dealing
		Collections.shuffle(deck);

		int i = 0;
		while (deck.size() > 0) {
			Card card = deck.remove(0); // the deck is shuffled, so just remove the first card

			players.get(i).giveCard(card);

			i = (i + 1) % players.size();
		}
	}

	public Card disproveSuggestion(Player from, Card person, Card weapon, Card room) {
		List<Card> foundCards = new LinkedList<Card>();

		for (Player player : players) {
			if (player != from) {
				Card card = player.disproveSuggestion(person, weapon, room);
				if (card != null) {
					foundCards.add(card);
				}
			}
		}

		if (foundCards.size() > 0) {
			Random rand = new Random();
			return foundCards.get(rand.nextInt(foundCards.size()));
		} else {
			return null;
		}
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

	public List<Card> getCards() {
		return cards;
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

	public List<Player> getPlayers() {
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

	public CardSet getSolution() {
		return solution;
	}

	public Set<BoardCell> getTargets(int i, int steps) {
		targets = new HashSet<BoardCell>();
		path.clear();
		path.add(i);
		calcTargets(i, steps);
		return targets;
	}

	public void loadConfigFiles(String legendFile, String boardFile, String weaponsFile, String playersFile) throws BadConfigFormatException, IOException {
		loadLegend(legendFile);
		loadBoard(boardFile);
		loadPlayers(playersFile);
		loadWeapons(weaponsFile);
	}

	public void selectAnswer() {

	}

	public void setSolution(CardSet solution) {
		this.solution = solution;
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

	private void loadBoard(String boardFile) throws BadConfigFormatException, IOException {
		numRows = 0;
		numColumns = -1;
		String[] spaces;
		FileReader reader = new FileReader(boardFile);
		Scanner scan = new Scanner(reader);
		while (scan.hasNextLine()) {
			spaces = scan.nextLine().split(",");

			// if the number of columns changes, the file is invalid
			if (numColumns != -1 && numColumns != spaces.length) {
				throw new BadConfigFormatException("Inconsistent number of spaces in row " + numRows);
			}

			numColumns = spaces.length;

			for (int i = 0; i < numColumns; i++) {
				String space = spaces[i];
				if (space.equalsIgnoreCase("W")) {
					cells.add(new WalkwayCell(numRows, i, calcIndex(numRows, i)));
				} else {
					if (space.length() > 0 && space.length() <= 2) {
						char initial = space.charAt(0);

						if (!rooms.containsKey(initial)) {
							throw new BadConfigFormatException("Invalid room initial '" + initial + '"');
						}

						DoorDirection direction = DoorDirection.NONE;

						if (space.length() > 1) {
							switch (space.charAt(1)) {
							case 'R':
								direction = DoorDirection.RIGHT;
								break;
							case 'L':
								direction = DoorDirection.LEFT;
								break;
							case 'U':
								direction = DoorDirection.UP;
								break;
							case 'D':
								direction = DoorDirection.DOWN;
								break;
							default:
								throw new BadConfigFormatException("Invalid room direction '" + space.charAt(1) + "'");
							}
						}

						cells.add(new RoomCell(numRows, i, calcIndex(numRows, i), initial, direction));
					} else {
						throw new BadConfigFormatException("Wrong length of room '" + space + "'");
					}
				}
			}

			numRows++;
		}
		
		scan.close();
		reader.close();
	}

	private void loadLegend(String legendFile) throws BadConfigFormatException {
		Pattern legendLine = Pattern.compile("[A-Z],[A-Za-z ]+");

		try {
			FileReader f = new FileReader(legendFile);
			Scanner scan = new Scanner(f);
			while (scan.hasNextLine()) {
				String line = scan.nextLine().trim();

				if (!legendLine.matcher(line).matches()) {
					throw new BadConfigFormatException("Invalid legend line '" + line + "'");
				}

				char abbr = line.charAt(0); // the abbreviation for the room
				String room = line.substring(2).trim(); // trim off the abbreviation and comma to get the full name
				rooms.put(abbr, room);
			}
		} catch (FileNotFoundException e) {
			System.out.println("I'm sorry, but the " + legendFile + " file is a figment of your imagination.");
			System.out.println(e.getMessage());
		}

		for (String room : rooms.values()) {
			cards.add(new Card(room, CardType.ROOM));
		}
	}

	private void loadPlayer(Player player, Scanner scan) throws BadConfigFormatException {
		String[] line = scan.nextLine().split(",");
		if (line.length != 3) {
			throw new BadConfigFormatException("Wrong number of values in line " + line + " of players file");
		}
		player.setName(line[0]);
		player.setPieceColor(Color.decode(line[1]));
		player.setCellIndex(Integer.parseInt(line[2]));
	}

	private void loadPlayers(String playersFile) throws BadConfigFormatException, IOException {
		clearPlayers();
		FileReader reader = new FileReader(playersFile);
		Scanner scan = new Scanner(reader);
		human = new HumanPlayer();
		loadPlayer(human, scan);
		players.add(human);
		while (scan.hasNextLine()) {
			ComputerPlayer droid = new ComputerPlayer();
			loadPlayer(droid, scan);
			players.add(droid);
		}

		scan.close();
		reader.close();

		for (Player person : players) {
			cards.add(new Card(person.getName(), CardType.PERSON));
		}
	}

	private void loadWeapons(String weaponsFile) throws IOException {
		FileReader reader = new FileReader(weaponsFile);
		Scanner scan = new Scanner(reader);

		while (scan.hasNextLine()) {
			cards.add(new Card(scan.nextLine(), CardType.WEAPON));
		}

		scan.close();
		reader.close();
	}
}
