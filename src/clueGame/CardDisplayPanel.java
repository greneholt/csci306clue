package clueGame;

import java.awt.GridLayout;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.*;

public class CardDisplayPanel extends JPanel {
	private Set<Card> humanCards = new HashSet<Card>();
	private List<JTextField> people = new LinkedList<JTextField>();
	private List<JTextField> weapons = new LinkedList<JTextField>();
	private List<JTextField> rooms = new LinkedList<JTextField>();
	
	public CardDisplayPanel(Board board) {
		super();
		setLayout(new GridLayout(0,1));
		humanCards = board.getHuman().getCards();
	}
	
	public void loadHumanCards(){
		// Load person cards
		for(Card c : humanCards) {
			
		}
		// Load weapon cards
		for(Card c : humanCards) {
			
		}
		// Load room cards
		for(Card c : humanCards) {
			
		}
	}
	
	private class PeoplePanel extends JPanel {
		public PeoplePanel() {
			super();
			
		}
	}
	
	private class WeaponsPanel extends JPanel {
		public WeaponsPanel() {
			super();
			
		}
	}
	
	private class RoomsPanel extends JPanel {
		public RoomsPanel() {
			super();
		}
	}
}
