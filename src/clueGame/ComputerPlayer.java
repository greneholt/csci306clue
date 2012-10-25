package clueGame;

import java.util.HashSet;
import java.util.Set;
public class ComputerPlayer extends Player {
	private char lastRoomVisited;
	private Set<Card> seenCards = new HashSet<Card>();
	public ComputerPlayer() {
		// TODO Auto-generated constructor stub
	}
	public void pickLocation(Set<BoardCell> targets){
	}
	public void createSuggestion(){
	}
	public void updateSeen(Card seen){
	}
}
