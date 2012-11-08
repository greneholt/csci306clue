package clueGame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
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

	public void giveCard(Card card) {
		cards.add(card);
	}
	
	public Card disproveSuggestion(Card person, Card weapon, Card room) {
		List<Card> hasCards = new LinkedList<Card>();
		
		if (cards.contains(person)) {
			hasCards.add(person);
		}
		
		if (cards.contains(weapon)) {
			hasCards.add(weapon);
		}
		
		if (cards.contains(room)) {
			hasCards.add(room);
		}
		
		if (hasCards.size() > 0) {
			Random rand = new Random();
			return hasCards.get(rand.nextInt(hasCards.size()));
		} else {
			return null;
		}
	}
	
	public void draw(Graphics2D g2d, float cellWidth, float cellHeight, int numRows, int numCols) {
		float x = cellWidth * (cellIndex % numCols);
		float y = cellHeight * ((cellIndex-(cellIndex % numCols)) / numCols);
		g2d.setColor(pieceColor);
		Ellipse2D.Float circle = new Ellipse2D.Float(x, y, cellWidth, cellHeight); 
		g2d.fill(circle);
		g2d.setColor(new Color(0x000000));
		circle = new Ellipse2D.Float(x, y, cellWidth, cellHeight);
		g2d.draw(circle);
	}
}
