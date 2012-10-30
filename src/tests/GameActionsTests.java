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
import clueGame.HumanPlayer;
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
		board.loadConfigFiles("ClueBoardLegend.txt", "ClueBoardLayout.csv", "weapons.txt", "players.txt");
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

		// move them to the top left corner, where no rooms are accessible
		computer.setCellIndex(board.calcIndex(0, 4));
		checkRandomRoomTarget(computer, 5);
	}

	@Test
	public void testComputerSelectRoomTarget() {
		ComputerPlayer computer = new ComputerPlayer();

		// move them to within range of a door
		computer.setCellIndex(board.calcIndex(5, 7));
		Set<BoardCell> targets = board.getTargets(computer.getCellIndex(), 3);

		// test that it picks the door every time
		for (int i = 0; i < 100; i++) {
			BoardCell target = computer.pickLocation(targets);
			assertEquals("Target door", board.calcIndex(4, 8), target.getIndex());
		}
	}

	@Test
	public void testComputerSelectRoomRandomTarget() {
		ComputerPlayer computer = new ComputerPlayer();

		// move them to the top left corner, where no Rooms are accessible
		computer.setCellIndex(board.calcIndex(5, 7));
		computer.setLastRoomVisited('R'); // if the Room was the last one entered,
		checkRandomRoomTarget(computer, 3);
	}

	private void checkRandomRoomTarget(ComputerPlayer computer, int steps) {
		Set<BoardCell> targets = board.getTargets(computer.getCellIndex(), steps);
		Map<BoardCell, Integer> choiceCounts = new HashMap<BoardCell, Integer>();

		// choose a cell 100 times, and then ensure each cell is chosen at least once
		for (int i = 0; i < 100; i++) {
			BoardCell target = computer.pickLocation(targets);
			assertTrue("Invalid target", targets.contains(target));

			int count = 1;
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

			int count = 1;
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
		ComputerPlayer jim=new ComputerPlayer();
		jim.giveCard(conservatory);
		ComputerPlayer bob = new ComputerPlayer();
		bob.giveCard(mustard);
		board.getPlayers().add(jim);
		board.getPlayers().add(bob);

		Set<Card> options = new HashSet<Card>();
		options.add(conservatory);
		options.add(mustard);
		Map<Card, Integer> choiceCounts = new HashMap<Card, Integer>();

		// disprove 100 times, and then ensure each option is given at least once
		for (int i = 0; i < 100; i++) {
			board.handleSuggestion(mustard, pipe, conservatory);
			Card card = board.getLastshown();
			assertTrue("Invalid card", options.contains(card));

			int count = 1;
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
	public void testDontDisproveMyself() {
		ComputerPlayer Jorge = new ComputerPlayer();
		Jorge.giveCard(scarlet);
		Jorge.giveCard(ballroom);
		board.getPlayers().clear();
		//make sure someone doesn't randomly have one of those cards
		board.getPlayers().add(Jorge);
		for (int i = 0; i < 100; i++) {
			assertFalse(board.handleSuggestion(scarlet, confetti, ballroom));
			//make sure that, even after 100 times, a player does not "show themself" a card.
		}
	}

	@Test
	public void computerSuggestion(){
		ComputerPlayer Dumbo = new ComputerPlayer();
		Dumbo.giveCard(mustard);
		Dumbo.markSeen(ballroom);
		Dumbo.markSeen(scarlet);
		Dumbo.markSeen(pipe);
		for(int i =0; i<100;i++){
			Set<Card> DumbSeen = Dumbo.createSuggestion();
			assertFalse(DumbSeen.contains(mustard));
			assertFalse(DumbSeen.contains(ballroom));
			assertFalse(DumbSeen.contains(scarlet));
			assertFalse(DumbSeen.contains(pipe));
		}
	}
}
