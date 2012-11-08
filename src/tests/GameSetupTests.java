package tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.Computer;

import clueGame.Board;
import clueGame.Card;
import clueGame.Card.CardType;
import clueGame.ComputerPlayer;
import clueGame.Player;
import clueGame.WalkwayCell;

public class GameSetupTests {
	private static Board board;
	private static Map<String, Color> names;

	@BeforeClass
	public static void setUp() throws Exception {
		board = new Board();
		board.loadConfigFiles("ClueBoardLegend.txt", "ClueBoardLayout.csv", "weapons.txt", "players.txt");
		names = new HashMap<String, Color>(6);
		names.put("Colonel Mustard", Color.yellow);
		names.put("Professor Plum", new Color(0xFF00FF));
		names.put("Miss White", Color.white);
		names.put("Mrs. Peacock", Color.blue);
		names.put("Mr. Green", Color.green);
		names.put("Miss Scarlet", Color.red);
	}

	@Test
	public void peopleLoaded() {
		List<Player> guests = board.getPlayers();

		assertEquals(6, guests.size());

		assertTrue(names.containsKey(board.getHuman().getName()));

		for (Player p : guests) {
			assertTrue(p.getName()/* message */, names.containsKey(p.getName()));
			// assure all the players' names are in the list.
			assertTrue("Not in walkway", board.getCellAt(p.getCellIndex()) instanceof WalkwayCell);
		}
	}

	@Test
	public void cardsLoaded() {
		assertEquals(21, board.getCards().size());
		// 6 people, 6 weapons, and however many rooms we have.
		int room = 0, pers = 0, weap = 0;
		for (Card c : board.getCards()) {
			switch (c.getType()) {
			case PERSON:
				pers++;
				break;
			case WEAPON:
				weap++;
				break;
			case ROOM:
				room++;
				break;
			default:
				fail("Bad Card Type");
				break;
			}
		}
		assertEquals(6, pers);
		assertEquals(6, weap);
		assertEquals(9, room);
		assertTrue(board.getCards().contains(new Card("Kitchen", CardType.ROOM)));
		assertTrue(board.getCards().contains(new Card("Revolver", CardType.WEAPON)));
		assertTrue(board.getCards().contains(new Card("Colonel Mustard", CardType.PERSON)));
	}

	@Test
	public void testDeal() {
		if (board.getCards().isEmpty())
			fail("we should have cards right now");
		
		board.deal();
		
		Set<Card> deck = new HashSet<Card>(board.getCards());
		
		// now that we dealt, the deck should be empty.
		int normalNum = board.getHuman().getCards().size();
		// assume the human has the "normal" number of cards.
		for (Player me : board.getPlayers()) {
			assertTrue("Has cards", me.getCards().size() > 0);
			assertTrue("Invalid number of cards", Math.abs(normalNum - me.getCards().size()) < 2);// can't deviate more than 1.
			for (Card card : me.getCards()) {
				assertTrue(deck.remove(card));
			}
		}
		// ensure every card has been dealt
		assertEquals(0, deck.size());
	}
}
