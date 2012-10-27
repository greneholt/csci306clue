package tests;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.ComputerPlayer;
import clueGame.Player;
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
		Card lastPerson = null;
		Card lastWeapon = null;
		Card lastRoom = null;
		Card firstPerson = null;
		Card firstWeapon = null;
		Card firstRoom = null;
		for (Card card : board.getCards()) {
			switch (card.getType()) {
			case PERSON:
				if (firstPerson == null) firstPerson = card;
				lastPerson = card;
				break;
			case WEAPON:
				if (firstWeapon == null) firstWeapon = card;
				lastWeapon = card;
				break;
			case ROOM:
				if (firstRoom == null) firstRoom = card;
				lastRoom = card;
				break;
			}
		}

		assertNotNull(lastPerson);
		assertNotNull(lastWeapon);
		assertNotNull(lastRoom);

		assertNotNull(firstPerson);
		assertNotNull(firstWeapon);
		assertNotNull(firstRoom);

		Solution solution = new Solution(lastPerson, lastWeapon, lastRoom);
		board.setSolution(solution);

		assertTrue("All correct", board.checkAccusation(lastPerson, lastWeapon, lastRoom));
		assertFalse("Wrong room", board.checkAccusation(lastPerson, lastWeapon, firstRoom));
		assertFalse("Wrong weapon", board.checkAccusation(lastPerson, firstWeapon, lastRoom));
		assertFalse("Wrong person", board.checkAccusation(firstPerson, lastWeapon, lastRoom));
		assertFalse("All wrong", board.checkAccusation(firstPerson, firstWeapon, firstRoom));
	}

	@Test
	public void testComputerSelectRandomTarget() {
		ComputerPlayer computer = new ComputerPlayer();
		
		// move them to the top left corner, where no rooms are accessible
		computer.setCellIndex(board.calcIndex(0, 0));
		Set<BoardCell> targets = board.getTargets(computer.getCellIndex(), 5);
		Map<BoardCell, Integer> choiceCounts = new HashMap<BoardCell, Integer>();
		
		// choose a cell 100 times, and then ensure each cell is chosen at least once
		for (int i = 0; i < 100; i++) {
			BoardCell target = computer.pickLocation(targets);
			assertTrue("Invalid target", targets.contains(target));
			
			int count = 0;
			if (choiceCounts.containsKey(target)) {
				count += choiceCounts.get(target);
			}
			choiceCounts.put(target, count);
		}
		
		int totalChoices = 0;
		for (int count : choiceCounts.values()) {
			totalChoices += count;
			if (count == 0) {
				fail("Cell choice not random");
			}
		}
		assertEquals(100, totalChoices);
	}
	
	@Test
	public void testComputerSelectRoomTarget() {
		ComputerPlayer computer = new ComputerPlayer();
		
		// move them to the top left corner, where no rooms are accessible
		computer.setCellIndex(board.calcIndex(5, 7));
		Set<BoardCell> targets = board.getTargets(computer.getCellIndex(), 3);
		
		// test that it picks the door every time
		for (int i = 0; i < 100; i++) {
			BoardCell target = computer.pickLocation(targets);
			assertEquals("Target door", board.calcIndex(4, 8), target.getIndex());
		}
	}
}
