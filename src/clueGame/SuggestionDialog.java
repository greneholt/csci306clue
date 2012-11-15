package clueGame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class SuggestionDialog extends JDialog {
	private JComboBox personGuess, weaponGuess;
	private JButton submitButton, cancelButton;
	private List<Card> cardDeck;
	
	
	public SuggestionDialog(final String room, List<Card> cards, GameControlPanel ctrl) {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setLayout(new GridLayout(0,2));
		
		cardDeck = cards;
		personGuess = loadComboBox(Card.CardType.PERSON);
		weaponGuess = loadComboBox(Card.CardType.WEAPON);
		
		JLabel roomLabel = new JLabel("Room:");
		JLabel roomName = new JLabel(room);
		JLabel personLabel = new JLabel("Person:");
		JLabel weaponLabel = new JLabel("Weapon:");
		
		submitButton = new JButton("Submit");
		cancelButton = new JButton("Cancel");
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO:
			}
		});
		
		add(personLabel, BorderLayout.WEST);
		add(personGuess, BorderLayout.CENTER);
		add(weaponLabel, BorderLayout.WEST);
		add(weaponGuess, BorderLayout.CENTER);
		add(roomLabel, BorderLayout.WEST);
		add(roomName, BorderLayout.CENTER);
		add(submitButton, BorderLayout.CENTER);
		add(cancelButton, BorderLayout.CENTER);
	}
	
	public JComboBox loadComboBox(Card.CardType type) {
		JComboBox guess = new JComboBox();
		for(Card c : cardDeck) {
			if (c.getType().equals(type)) {
				guess.addItem(c.getName());
			}
		}
		return guess;
	}
}
