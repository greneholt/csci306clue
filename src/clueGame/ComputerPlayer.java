package clueGame;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {
	private Set<Card> seenCards = new HashSet<Card>();

	public ComputerPlayer() {
		// TODO Auto-generated constructor stub
	}

	public BoardCell pickLocation(Set<BoardCell> targets) {
		if (targets.size() == 0)
			return null;

		// look for a room in the targets
		for (BoardCell target : targets) {
			// if its a room that wasn't the last one visited, choose it
			if (target.isRoom() && ((RoomCell) target).getInitial() != lastRoomVisited) {
				return target;
			}
		}

		Random rand = new Random();
		return (BoardCell) (targets.toArray())[rand.nextInt(targets.size())];
	}

	/**
	 * Can return null if every card of a certain type has been seen, but so long as the solution cards weren't dealt to a player this shouldn't happen.
	 */
	public CardSet createSuggestion(Card room, List<Card> cards) {
		List<Card> weapons = new LinkedList<Card>();
		List<Card> people = new LinkedList<Card>();

		for (Card card : cards) {
			if (!seenCards.contains(card) && !getCards().contains(card)) {
				switch (card.getType()) {
				case WEAPON:
					weapons.add(card);
					break;
				case PERSON:
					people.add(card);
					break;
				}
			}
		}

		if (weapons.size() == 0 || people.size() == 0) {
			return null;
		}  

		Random rand = new Random();

		Card weapon = weapons.get(rand.nextInt(weapons.size()));
		Card person = people.get(rand.nextInt(people.size()));

		return new CardSet(person, weapon, room);
	}

	public void markSeen(Card seen) {
		seenCards.add(seen);
	}

	// if the computer wants to make an accusation, it will return a CardSet, otherwise it will return null
	public CardSet maybeMakeAccusation(List<Card> cards) {
		// if there are only three cards that we haven't seen and that aren't in our deck, they are the correct accusation
		if (cards.size() - seenCards.size() - getCards().size() == 3) {
			Card person = null;
			Card weapon = null;
			Card room = null;

			for (Card card : cards) {
				if (!seenCards.contains(card) && !getCards().contains(card)) {
					switch (card.getType()) {
					case WEAPON:
						weapon = card;
						break;
					case PERSON:
						person = card;
						break;
					case ROOM:
						room = card;
						break;
					}
				}
			}

			return new CardSet(person, weapon, room);
		}

		return null;
	}
}
