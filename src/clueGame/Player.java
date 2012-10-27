package clueGame;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

public class Player {
	private String name;
	private Color pieceColor;
	private int cellIndex;
	private Set<Card> cards = new HashSet<Card>();

	public Player() {
		// TODO Auto-generated constructor stub
	}

	public int getCellIndex() {
		return cellIndex;
	}
	
	public String getName() {
		return name;
	}

	public Color getPieceColor() {
		return pieceColor;
	}

	public void setCellIndex(int cellIndex) {
		this.cellIndex = cellIndex;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPieceColor(Color pieceColor) {
		this.pieceColor = pieceColor;
	}

	public void setCards(Set<Card> cards) {
		this.cards = cards;
	}

	public Set<Card> getCards() {
		return cards;
	}

}
