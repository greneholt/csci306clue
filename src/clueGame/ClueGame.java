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
				if (!madeMove && !madeSuggestion) {
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
		
		dieRoll = rand.nextInt(6) + 1;
		
		targets = board.getTargets(currentPlayer.getCellIndex(), dieRoll);
		
		BoardCell currentCell = board.getCellAt(currentPlayer.getCellIndex());
		
		if (currentPlayer == board.getHuman()) {
			board.displayTargets(targets);
			
			if (currentCell.isRoom() && ((RoomCell)currentCell).getInitial() != currentPlayer.getLastRoomVisited()) {
				playerMadeSuggestion();
			}
		}
		else {
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
				Card result = board.disproveSuggestion(currentPlayer, suggestion.getPerson(), suggestion.getWeapon(), suggestion.getRoom());
				
				gameControl.updateSuggestion(suggestion, result);
				
				if (result != null) {
					computer.markSeen(result);
				}
			}
		}
	}
	
	public void playerMadeSuggestion() {
		JOptionPane.showMessageDialog(this, "Make a suggestion", "Do it!", JOptionPane.INFORMATION_MESSAGE);
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
			playerMadeSuggestion();
		}
	}
}
