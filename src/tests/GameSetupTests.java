package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Computer;

import clueGame.Board;
import clueGame.ComputerPlayer;

public class GameSetupTests {
	Board brd;
	ArrayList<String> names = new ArrayList<String>(6);
	@Before //TODO BeforeClass?
	public void setUp() throws Exception {
		brd = new Board();
		names.add("Colonel Mustard");
		names.add("Professor Plum");
		names.add("Miss White");
		names.add("Mrs. Peacock");
		names.add("Mr. Green");
		names.add("Miss Scarlet");
	}

	@Test
	public void peopleLoaded() {
		Set<ComputerPlayer> opp = brd.getOpponents();
		assertEquals(5, opp.size());
		if(!names.remove(brd.getYou().getName())) fail("Your name is invalid");
		for(ComputerPlayer comp : opp){
			assertTrue(comp.getName(),names.contains(comp.getName()));
			//assure all the computer players' names are in the list.
		}
	}

}
