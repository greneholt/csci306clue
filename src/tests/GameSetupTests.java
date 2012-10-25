package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import clueGame.Board;
import clueGame.ComputerPlayer;

public class GameSetupTests {
	Board brd;
	ArrayList<String> names = new ArrayList<String>(6);
	@Before //TODO BeforeClass?
	public void setUp() throws Exception {
		brd = new Board();
	}

	@Test
	public void peopleLoaded() {
		Set<ComputerPlayer> opp = brd.getOpponents();
		assertEquals(5, opp.size());
	}

}
