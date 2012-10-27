package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;

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
		Person person = board.
	}
}
