package clueGame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public abstract class BoardCell {
	private int row, column, index;

	public BoardCell(int row, int column, int index) {
		this.row = row;
		this.column = column;
		this.index = index;
	}

	public boolean isWalkway() {
		return false;
	}

	public boolean isRoom() {
		return false;
	}

	public boolean isDoorway() {
		return false;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public int getIndex() {
		return index;
	}
	
	public abstract void draw(Graphics2D g2d, float cellWidth, float cellHeight);

	@Override
	public String toString() {
		return "Cell @ row=" + row + " column=" + column;
	}

	public void drawAsTarget(Graphics2D g2d, float cellWidth, float cellHeight) {
		float x = cellWidth * getColumn();
		float y = cellHeight * getRow();
		g2d.setColor(new Color(0x4284D3));
		Rectangle2D.Float rect = new Rectangle2D.Float(x, y, cellWidth, cellHeight); 
		g2d.fill(rect);
	}
}
