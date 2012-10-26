package clueGame;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

public class Player {
	private String name;
	private Color pieceColor;
	private int where;///the index of the cell the player is at.
	private Set<Card> myCards = new HashSet<Card>();
	public Player() {
		// TODO Auto-generated constructor stub
	}
	public String getName(){
		return name;
	}
	/**
	 * @return the pieceColor
	 */
	public Color getPieceColor() {
		return pieceColor;
	}
	/**
	 * @return the index of the cell the player is currently at
	 */
	public int getWhere() {
		return where;
	}
	/**
	 * @return the myCards
	 */
	public Set<Card> getMyCards() {
		return myCards;
	}

}
