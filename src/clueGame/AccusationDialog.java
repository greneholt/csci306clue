package clueGame;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class AccusationDialog extends JDialog {
	private JComboBox personGuess, weaponGuess, roomGuess;
	private JButton submitButton, cancelButton;
	private List<Card> cardDeck;
	private boolean submitted;
	
	public AccusationDialog(Window parent, List<Card> cards) {
		super(parent, Dialog.DEFAULT_MODALITY_TYPE);
		setLayout(new GridLayout(0,2));
		setMinimumSize(new Dimension(300, 300));
		
		cardDeck = cards;
		personGuess = loadComboBox(Card.CardType.PERSON);
		weaponGuess = loadComboBox(Card.CardType.WEAPON);
		roomGuess = loadComboBox(Card.CardType.ROOM);
		
		JLabel roomLabel = new JLabel("Room:");
		JLabel personLabel = new JLabel("Person:");
		JLabel weaponLabel = new JLabel("Weapon:");
		
		submitButton = new JButton("Submit");
		cancelButton = new JButton("Cancel");
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				submitted = true;
				setVisible(false);
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				submitted = false;
				setVisible(false);
			}
		});
		
		add(personLabel);
		add(personGuess);
		add(weaponLabel);
		add(weaponGuess);
		add(roomLabel);
		add(roomGuess);
		add(submitButton);
		add(cancelButton);
	}
	
	private JComboBox loadComboBox(Card.CardType type) {
		JComboBox guess = new JComboBox();
		for(Card c : cardDeck) {
			if (c.getType().equals(type)) {
				guess.addItem(c.getName());
			}
		}
		return guess;
	}
	
	public CardSet prompt() {
		setVisible(true);
		
		if (submitted) {
			Card person = null;
			Card weapon = null;
			Card room = null;
			
			for (Card card : cardDeck) {
				if (card.getName().equals(personGuess.getSelectedItem().toString())) {
					person = card;
				}
				else if (card.getName().equals(weaponGuess.getSelectedItem().toString())) {
					weapon = card;
				}
				else if (card.getName().equals(roomGuess.getSelectedItem().toString())) {
					room = card;
				}
			}
			
			return new CardSet(person, weapon, room);
		}
		else {
			return null;
		}
	}
}
