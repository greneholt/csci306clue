package clueGame;

public class Card {
	String name;
	CardType type;

	public Card(String name, CardType type) {
		this.name = name;
		this.type = type;
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
