package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.Solution;

public class GameActionsTests {
	private static Board board;
	
	@BeforeClass
	public static void setUp() throws Exception {
		board = new Board();
		board.loadConfigFiles("ClueBoardLegend.txt", "ClueBoardLayout.csv");
		board.loadPlayers("players.txt");
		board.loadCards("cards.txt");
	}

	@Test
	public void testAccusation() {
		Card person = null;
		Card weapon = null;
		Card room = null;
		for (Card card : board.getCards()) {
			switch (card.getType()) {
			case PERSON:
				person = card;
				break;
			case WEAPON:
				weapon = card;
				break;
			case ROOM:
				room = card;
				break;
			}
		}
		
		assertNotNull(person);
		assertNotNull(weapon);
		assertNotNull(room);
		
		Solution solution = new Solution(person, weapon, room);
		board.setSolution(solution);
		
		
	}
}
