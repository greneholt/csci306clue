package clueGame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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
	
	public ClueGame() {
		super();
		
		setTitle("Have you got a clue?");
		setSize(new Dimension(600, 600));
		setMinimumSize(new Dimension(500, 500));
		
		setLayout(new BorderLayout());
		board = new Board();

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
		
		cardDisplay = new CardDisplayPanel(board);
		gameControl = new GameControlPanel(board);
		
		add(board, BorderLayout.CENTER);
		add(cardDisplay, BorderLayout.EAST);
		add(gameControl, BorderLayout.SOUTH);
		
		buildMenu();
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
			// the program isn't running on a Mac
		}
		
		ClueGame gui = new ClueGame();
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setVisible(true);
	}
}
