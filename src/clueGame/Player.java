package clueGame;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

public class Player {
	private String name;
	private Color pieceColor;
	private int cellIndex; // the index of the cell the player is at.
	private Set<Card> cards = new HashSet<Card>();

	public Player() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	/**
	 * @return the pieceColor
	 */
	public Color getPieceColor() {
		return pieceColor;
	}

	public int getCellIndex() {
		return cellIndex;
	}

	public Set<Card> getCards() {
		return cards;
	}

}
