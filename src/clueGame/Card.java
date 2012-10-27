package clueGame;

public class Card {
	String name;
	CardType type;

	public Card() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public CardType getType() {
		return type;
	}

	public enum CardType {
		ROOM, WEAPON, PERSON
	}

	@Override
	public boolean equals(Object other) {
		// TODO stub
		return false;
	}
}
