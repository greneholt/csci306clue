package clueGame;

import java.awt.Color;
import java.awt.Graphics;

public class WalkwayCell extends BoardCell {
	public WalkwayCell(int row, int column, int index) {
		super(row, column, index);
	}

	@Override
	public boolean isWalkway() {
		return true;
	}

	@Override
	public void draw(Graphics g, float cellWidth, float cellHeight) {
		int x = (int)(cellWidth * getColumn());
		int y = (int)(cellHeight * getRow());
		g.setColor(new Color(0xFB993F));
		g.fillRect(x, y, (int)cellWidth, (int)cellHeight);
		g.setColor(new Color(0xF77600));
		g.drawRect(x, y, (int)cellWidth, (int)cellHeight);
	}
}
