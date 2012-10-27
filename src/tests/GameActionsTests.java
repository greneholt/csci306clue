package tests;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
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
	private static Card scarlet = new Card("Miss Scarlet", Card.CardType.PERSON);
	private static Card pipe = new Card("Lead pipe", Card.CardType.WEAPON);
	private static Card conservatory = new Card("Conservatory", Card.CardType.ROOM);
	private static Card mustard = new Card("Colonal Mustard", Card.CardType.PERSON);
	private static Card confetti = new Card("Confetti", Card.CardType.WEAPON);
	private static Card ballroom = new Card("Ballroom", Card.CardType.ROOM);

	@BeforeClass
	public static void setUp() throws Exception {
		board = new Board();
		board.loadConfigFiles("ClueBoardLegend.txt", "ClueBoardLayout.csv");
		board.loadPlayers("players.txt");
		board.loadCards("cards.txt");
	}

	@Test
	public void testAccusation() {
		Solution solution = new Solution(scarlet, pipe, conservatory);
		board.setSolution(solution);

		assertTrue("All correct", board.checkAccusation(scarlet, pipe, conservatory));
		assertFalse("Wrong conservatory", board.checkAccusation(scarlet, pipe, ballroom));
		assertFalse("Wrong pipe", board.checkAccusation(scarlet, confetti, conservatory));
		assertFalse("Wrong scarlet", board.checkAccusation(mustard, pipe, conservatory));
		assertFalse("All wrong", board.checkAccusation(mustard, confetti, ballroom));
	}

	@Test
	public void testComputerSelectRandomTarget() {
		ComputerPlayer computer = new ComputerPlayer();

		// move them to the top left corner, where no conservatorys are accessible
		computer.setCellIndex(board.calcIndex(0, 0));
		checkRandomconservatoryTarget(computer, 5);
	}

	@Test
	public void testComputerSelectconservatoryTarget() {
		ComputerPlayer computer = new ComputerPlayer();

		// move them to the top left corner, where no conservatorys are accessible
		computer.setCellIndex(board.calcIndex(5, 7));
		Set<BoardCell> targets = board.getTargets(computer.getCellIndex(), 3);

		// test that it picks the door every time
		for (int i = 0; i < 100; i++) {
			BoardCell target = computer.pickLocation(targets);
			assertEquals("Target door", board.calcIndex(4, 8), target.getIndex());
		}
	}

	@Test
	public void testComputerSelectconservatoryRandomTarget() {
		ComputerPlayer computer = new ComputerPlayer();

		// move them to the top left corner, where no conservatorys are accessible
		computer.setCellIndex(board.calcIndex(5, 7));
		computer.setLastRoomVisited('R'); // if the conservatory was the last one entered,
		checkRandomconservatoryTarget(computer, 3);
	}

	private void checkRandomconservatoryTarget(ComputerPlayer computer, int steps) {
		Set<BoardCell> targets = board.getTargets(computer.getCellIndex(), steps);
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
	public void testDisproveSuggestionOnePlayer() {
		ComputerPlayer computer = new ComputerPlayer();
		computer.giveCard(scarlet);
		computer.giveCard(mustard);
		computer.giveCard(ballroom);
		
		// test when only one could be returned
		Card hasCard = computer.disproveSuggestion(scarlet, pipe, conservatory);
		assertNotNull(hasCard);
		assertEquals(scarlet, hasCard);
		
		// test when multiple cards could be given
		Set<Card> options = new HashSet<Card>();
		options.add(scarlet);
		options.add(ballroom);
		Map<Card, Integer> choiceCounts = new HashMap<Card, Integer>();

		// disprove 100 times, and then ensure each option is given at least once
		for (int i = 0; i < 100; i++) {
			Card card = computer.disproveSuggestion(scarlet, confetti, ballroom);
			assertTrue("Invalid card", options.contains(card));

			int count = 0;
			if (choiceCounts.containsKey(card)) {
				count += choiceCounts.get(card);
			}
			choiceCounts.put(card, count);
		}

		int totalChoices = 0;
		for (int count : choiceCounts.values()) {
			totalChoices += count;
			if (count == 0) {
				fail("Card choice not random");
			}
		}
		assertEquals(100, totalChoices);
	}
	
	@Test
	public void testDisproveSuggestionTwoPlayers() {
		// TODO
	}
}
