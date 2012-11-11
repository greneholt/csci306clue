package clueGame;

import java.awt.GridLayout;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class CardDisplayPanel extends JPanel {
	private Set<Card> humanCards = new HashSet<Card>();
	private List<JTextField> people = new LinkedList<JTextField>();
	private List<JTextField> weapons = new LinkedList<JTextField>();
	private List<JTextField> rooms = new LinkedList<JTextField>();
	
	public CardDisplayPanel(Board board) {
		super();
		setLayout(new GridLayout(0,1));
		humanCards = board.getHuman().getCards();
		JLabel label = new JLabel("My Cards:");
		add(label);
		add(new PeoplePanel());
		add(new WeaponsPanel());
		add(new RoomsPanel());
	}
	
	public void loadHumanCards(){
		// Load person cards
		for(Card c : humanCards) {
			if(c.getType().equals(Card.CardType.PERSON)) {
				JTextField cField = new JTextField();
				cField.setText(c.getName());
				cField.setEditable(false);
				people.add(cField);
			}
		}
		// Load weapon cards
		for(Card c : humanCards) {
			if(c.getType().equals(Card.CardType.WEAPON)) {
				JTextField cField = new JTextField();
				cField.setText(c.getName());
				cField.setEditable(false);
				weapons.add(cField);
			}
		}
		// Load room cards
		for(Card c : humanCards) {
			if(c.getType().equals(Card.CardType.ROOM)) {
				JTextField cField = new JTextField();
				cField.setText(c.getName());
				cField.setEditable(false);
				rooms.add(cField);
		}
		}
	}
	
	private class PeoplePanel extends JPanel {
		public PeoplePanel() {
			super();
			setBorder(new TitledBorder (new EtchedBorder(), "People"));
			setLayout(new GridLayout(0,1));
			for(JTextField tf : people) {
				add(tf);
			}
		}
	}
	
	private class WeaponsPanel extends JPanel {
		public WeaponsPanel() {
			super();
			setBorder(new TitledBorder (new EtchedBorder(), "Weapons"));
			setLayout(new GridLayout(0,1));
			for(JTextField tf : weapons) {
				add(tf);
			}
		}
	}
	
	private class RoomsPanel extends JPanel {
		public RoomsPanel() {
			super();
			setBorder(new TitledBorder (new EtchedBorder(), "Rooms"));
			setLayout(new GridLayout(0,1));
			for(JTextField tf : rooms) {
				add(tf);
			}
		}
	}
}
