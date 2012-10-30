package clueGame;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {
	private char lastRoomVisited;
	private Set<Card> seenCards = new HashSet<Card>();

	public ComputerPlayer() {
		// TODO Auto-generated constructor stub
	}

	public BoardCell pickLocation(Set<BoardCell> targets) {
		if (targets.size() == 0) return null;
		
		// look for a room in the targets
		for (BoardCell target : targets) {
			// if its a room that wasn't the last one visited, choose it
			if (target.isRoom() && target.getInitial() != lastRoomVisited) {
				return target;
			}
		}
		
		Random rand = new Random();
		return (BoardCell) (targets.toArray())[rand.nextInt(targets.size())];
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
