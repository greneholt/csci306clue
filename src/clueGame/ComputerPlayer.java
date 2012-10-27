package clueGame;

import java.util.HashSet;
import java.util.Set;

public class ComputerPlayer extends Player {
	private char lastRoomVisited;
	private Set<Card> seenCards = new HashSet<Card>();

	public ComputerPlayer() {
		// TODO Auto-generated constructor stub
	}

	public BoardCell pickLocation(Set<BoardCell> targets) {
		return new WalkwayCell();
	}

	public Set<Card> createSuggestion() {
		return seenCards;//TODO fix this to make tests pass
	}

	public void markSeen(Card seen) {
	}

	public char getLastRoomVisited() {
		return lastRoomVisited;
	}

	public void setLastRoomVisited(char lastRoomVisited) {
		this.lastRoomVisited = lastRoomVisited;
	}
}
