package clueGame;

import java.awt.Graphics2D;

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
}
