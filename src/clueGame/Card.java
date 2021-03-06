package clueGame;

public class Card {
	private String name;
	private CardType type;

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
		if (other == this) {
			return true;
		}
		
		if (other instanceof Card) {
			Card o = (Card) other;
			return o.name.equals(name) && o.type == type;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return name + " " + type.toString();
	}
	
	@Override
	public int hashCode() {
		return name.hashCode() + type.hashCode();
	}
}
