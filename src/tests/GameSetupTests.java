package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import clueGame.Board;

public class GameSetupTests {

	@Before //TODO BeforeClass?
	public void setUp() throws Exception {
		Board brd = new Board();
	}

	@Test
	public void peopleLoaded() {
		
		assertEquals(0, 2);
	}

}
