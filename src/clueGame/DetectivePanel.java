package clueGame;

import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

public class DetectivePanel extends JPanel {
	private List<JCheckBox> peopleCB = new LinkedList<JCheckBox>();
	private List<JCheckBox> weaponsCB = new LinkedList<JCheckBox>();
	private List<JCheckBox> roomsCB = new LinkedList<JCheckBox>();
	private JComboBox personGuess, weaponGuess, roomGuess;
	private List<Card> cardDeck;
	
	public DetectivePanel() {
		super();
		
		// TODO: Change the size
		setSize(new Dimension(800, 800));
		setMinimumSize(new Dimension(500, 500));
		
		// Pull in the card names and associate with checkboxes
		
		// Put these checkboxes into groups
		
		// Add appropriate cards to combo boxes
		personGuess = createCardCombo(Card.CardType.PERSON);
		weaponGuess = createCardCombo(Card.CardType.WEAPON);
		roomGuess = createCardCombo(Card.CardType.ROOM);
		// Put combo boxes into groups
	}
	
	private JComboBox createCardCombo(Card.CardType type) {
		JComboBox guess = new JComboBox();
		// Step through card deck and check if the card is of the correct type.
		// Add cardname to comboBox if it is correct type.
		for (Card c : cardDeck) {
			if(c.getType().equals(type)) {
				guess.addItem(c.getName());
			}
		}
		guess.addItem("Unsure");
		return guess;
	}
}
