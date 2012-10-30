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
	private static Map<String,Color> names;

	@BeforeClass 
	public static void setUp() throws Exception {
		board = new Board();
		board.loadConfigFiles("ClueBoardLegend.txt", "ClueBoardLayout.csv","weapons.txt","players.txt");
		names = new HashMap<String,Color>(6);
		names.put("Colonel Mustard",Color.yellow);
		names.put("Professor Plum",new Color(0xFF00FF));
		names.put("Miss White",Color.white);
		names.put("Mrs. Peacock",Color.blue);
		names.put("Mr. Green",Color.green);
		names.put("Miss Scarlet",Color.red);
	}

	@Test
	public void peopleLoaded() {
		Set<Player> guests = board.getPlayers();

		assertEquals(6, guests.size());

		assertTrue(names.containsKey(board.getHuman().getName()));

		for(Player p : guests){
			assertTrue(p.getName()/*message*/,names.containsKey(p.getName()));
			//assure all the players' names are in the list.
			assertTrue("Not in walkway",board.getCellAt(p.getCellIndex()) instanceof WalkwayCell);

		}

	}

	@Test
	public void cardsLoaded() {
		assertEquals(12+board.getRooms().size(), board.getDeck().size());
		//6 people, 6 weapons, and however many rooms we have.
		int room=0,pers=0,weap=0;
		for(Card c : board.getDeck()){
			switch(c.getType()){
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
		assertEquals(board.getRooms().size(), room);
		assertTrue(board.getDeck().contains(new Card("Kitchen", CardType.ROOM)));
		assertTrue(board.getDeck().contains(new Card("Revolver", CardType.WEAPON)));
		assertTrue(board.getDeck().contains(new Card("Colonel Mustard", CardType.PERSON)));
	}

	@Test
	public void testDeal(){
		if(board.getDeck().isEmpty()) fail("we should have cards right now");
		board.deal();
		assertEquals(0, board.getDeck().size());
		//now that we dealt, the deck should be empty.
		int normalNum = board.getHuman().getCards().size();
		//assume the human has the "normal" number of cards.
		for (Player me : board.getPlayers()){
			assertTrue(Math.abs(normalNum-me.getCards().size())<2);//can't deviate more than 1.
			Set<Player> others = board.getPlayers();
			others.remove(me);
			for(Card card : me.getCards()){
				for (Player o : others){
					assertFalse(o.getCards().contains(card));
					//should not have same card as someone else.
				}
			}
		}
	}
	
}
