package clueGame;

public class Card implements Comparable<Card>{
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
		Card o = (Card) other;
		return (o.name.equals(name)&&o.type==type);
	}

	@Override
	public int compareTo(Card o) {
		if(equals(o))return 0;
		else return name.compareTo(o.name);
	}
}
