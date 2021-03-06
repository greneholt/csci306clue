package clueGame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class ClueGame extends JFrame {
	private JMenuBar menuBar;
	private Board board;
	private DetectiveDialog detective;
	private CardDisplayPanel cardDisplay;
	private GameControlPanel gameControl;
	private Player currentPlayer;
	private int nextPlayerIndex;

	private boolean madeMove;
	private boolean madeSuggestion;
	private boolean madeAccusation;
	private boolean correctAccusation;
	private int dieRoll;
	
	private static final Random rand = new Random();
	private Set<BoardCell> targets;
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public ClueGame() {
		super();
		
		setTitle("Have you got a clue?");
		setSize(new Dimension(700, 700));
		setMinimumSize(new Dimension(500, 500));
		
		setLayout(new BorderLayout());
		board = new Board();
		board.setClueGame(this);

		try {
			board.loadConfigFiles("ClueBoardLegend.txt", "ClueBoardLayout.csv", "weapons.txt", "players.txt");
		} catch (BadConfigFormatException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Config File Invalid", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "IO Exception", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		
		board.deal();
		
		cardDisplay = new CardDisplayPanel(board.getHuman().getCards());
		gameControl = new GameControlPanel(board, this);
		
		add(board, BorderLayout.CENTER);
		add(cardDisplay, BorderLayout.EAST);
		add(gameControl, BorderLayout.SOUTH);
		
		buildMenu();
	}
	
	public void startGame() {
		Player human = board.getHuman();
		nextPlayerIndex = 0;
		JOptionPane.showMessageDialog(this, "You are " + human.getName() + ", press Next player to begin play", "Welcome to Clue", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void nextPlayer() {
		if (currentPlayer != null) {
			BoardCell currentCell = board.getCellAt(currentPlayer.getCellIndex()); 
			
			if (currentPlayer == board.getHuman()) {
				if (!madeMove && !madeSuggestion && !madeAccusation) {
					JOptionPane.showMessageDialog(this, "You have not made a move or a suggestion yet", "Cannot continue", JOptionPane.WARNING_MESSAGE);
					return;
				}
			}
			
			if (currentCell.isRoom()) {
				currentPlayer.setLastRoomVisited(((RoomCell) currentCell).getInitial());
			}
			else {
				currentPlayer.setLastRoomVisited('W');
			}
		}
		
		currentPlayer = board.getPlayers().get(nextPlayerIndex);
		nextPlayerIndex = (nextPlayerIndex + 1) % board.getPlayers().size();
		startTurn();
	}
	
	public void startTurn() {
		madeMove = false;
		madeSuggestion = false;
		madeAccusation = false;
		
		gameControl.updateCurrentPlayer(currentPlayer);
		
		dieRoll = rand.nextInt(6) + 1;
		gameControl.updateDiceRoll(dieRoll);
		
		targets = board.getTargets(currentPlayer.getCellIndex(), dieRoll);
		
		if (currentPlayer == board.getHuman()) {
			humanTurn();
		}
		else {
			computerTurn();
		}
	}

	private void humanTurn() {
		BoardCell currentCell = board.getCellAt(currentPlayer.getCellIndex());
		board.displayTargets(targets);
		
		if (currentCell.isRoom() && ((RoomCell)currentCell).getInitial() != currentPlayer.getLastRoomVisited()) {
			playerMadeSuggestion(((RoomCell)currentCell).getInitial());
			
			if (madeSuggestion) {
				board.clearTargets();
			}
		}
	}

	private void computerTurn() {
		ComputerPlayer computer = (ComputerPlayer)currentPlayer;
		
		CardSet accusation = computer.maybeMakeAccusation(board.getCards());
		
		if (accusation != null) {
			boolean correct = board.checkAccusation(accusation.getPerson(), accusation.getWeapon(), accusation.getRoom());
			if (correct) {
				JOptionPane.showMessageDialog(this, "The computer has won. It was " + accusation, "Computer Won", JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
			}
		}
		
		BoardCell target = computer.pickLocation(targets);
		computer.setCellIndex(target.getIndex());
		board.repaint();
		if (board.isRoom(target.getIndex())) {
			Card room = board.getCardForRoom(((RoomCell)target).getInitial());
			
			CardSet suggestion = computer.createSuggestion(room, board.getCards());
			handleSuggestion(suggestion);
		}
	}
	
	public void makeAccusation() {
		if(currentPlayer == board.getHuman()) {
			AccusationDialog dialog = new AccusationDialog(this, board.getCards());
			CardSet accusation = dialog.prompt();
			if (accusation == null) {
				return;
			}
			correctAccusation = handleAccusation(accusation);
			madeAccusation = true;
			
			if (correctAccusation) {
				JOptionPane.showMessageDialog(this, "You have won. It was " + accusation, "Player Won", JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
			} else {
				JOptionPane.showMessageDialog(this, accusation + ": This is incorrect.", "Incorrect", JOptionPane.INFORMATION_MESSAGE);
			}
			
			board.clearTargets();
		} else {
			JOptionPane.showMessageDialog(this, "You can't make an accusation when it is not your turn.", "Accusing Out of Order", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
	}
	
	private boolean handleAccusation(CardSet accusation) {
		if (accusation == null) {
			return false;
		}
		if(board.checkAccusation(accusation.getPerson(), accusation.getWeapon(), accusation.getRoom())) {
			return true;
		} else {
			return false;
		}
	}

	private Card handleSuggestion(CardSet suggestion) {
		if (suggestion == null) {
			return null;
		}
		
		for (Player player : board.getPlayers()) {
			if (player.getName().equals(suggestion.getPerson().getName())) {
				player.setCellIndex(currentPlayer.getCellIndex());
				board.repaint();
				break;
			}
		}
		
		Card result = board.disproveSuggestion(currentPlayer, suggestion.getPerson(), suggestion.getWeapon(), suggestion.getRoom());
		gameControl.updateSuggestion(suggestion, result);
		if (result != null) {
			for (Player player : board.getPlayers()) {
				if (player instanceof ComputerPlayer) {
					((ComputerPlayer)player).markSeen(result);
				}
			}
		}
		return result;
	}
	
	public void playerMadeSuggestion(char roomInitial) {
		String roomName = board.getRooms().get(roomInitial);
		SuggestionDialog dialog = new SuggestionDialog(this, roomName, board.getCards());
		CardSet suggestion = dialog.prompt();
		if (suggestion == null) {
			return;
		}
		handleSuggestion(suggestion);
		madeSuggestion = true;
	}
	
	private void buildMenu() {
		JMenuItem item = new JMenuItem("Quit");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		JMenuItem notes = new JMenuItem("Detective Notes");
		notes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(detective == null) {
					detective = new DetectiveDialog(board);
				} else {
					detective.setVisible(true);
				}
			}
		});
		
		JMenu menu = new JMenu("File");
		menu.add(notes);
		menu.add(item);
		menuBar = new JMenuBar();
		menuBar.add(menu);
		setJMenuBar(menuBar);
	}

	public static void main(String[] args) {
		try {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		} catch (Exception e) {
			// the program isn't running on a Mac, too bad for them
		}
		
		ClueGame game = new ClueGame();
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setVisible(true);
		game.startGame();
	}

	public void cellClicked(BoardCell cell) {
		if (targets.contains(cell)) {
			moveHumanPlayer(cell);
		}
		else {
			JOptionPane.showMessageDialog(this, "Invalid target", "Invalid target", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void moveHumanPlayer(BoardCell cell) {
		madeMove = true;
		currentPlayer.setCellIndex(cell.getIndex());
		board.clearTargets();
		board.repaint();
		if (cell.isRoom()) {
			playerMadeSuggestion(((RoomCell)cell).getInitial());
		}
	}
}
