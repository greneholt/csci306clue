package tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Computer;

import clueGame.Board;
import clueGame.ComputerPlayer;
import clueGame.WalkwayCell;

public class GameSetupTests {
	Board brd;
	Map<String,Color> names;
	@Before //TODO BeforeClass?
	public void setUp() throws Exception {
		names = new HashMap<String,Color>(6);
		brd = new Board();
		names.put("Colonel Mustard",Color.yellow);
		names.put("Professor Plum",Color.decode("FF00FF"));
		names.put("Miss White",Color.white);
		names.put("Mrs. Peacock",Color.blue);
		names.put("Mr. Green",Color.green);
		names.put("Miss Scarlet",Color.red);
	}

	@Test
	public void peopleLoaded() {
		Set<ComputerPlayer> opp = brd.getOpponents();
		assertEquals(5, opp.size());
		if(names.remove(brd.getYou().getName())==null) fail("Your name is invalid");
		for(ComputerPlayer comp : opp){
			assertTrue(comp.getName()/*message*/,names.containsKey(comp.getName()));
			//assure all the computer players' names are in the list.
			assertTrue("Not in walkway",brd.getCellAt(comp.getWhere()) instanceof WalkwayCell);
			
		}
		
	}

}
