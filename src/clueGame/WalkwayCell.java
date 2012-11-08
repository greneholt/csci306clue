package clueGame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class WalkwayCell extends BoardCell {
	public WalkwayCell(int row, int column, int index) {
		super(row, column, index);
	}

	@Override
	public boolean isWalkway() {
		return true;
	}

	@Override
	public void draw(Graphics2D g2d, float cellWidth, float cellHeight) {
		float x = cellWidth * getColumn();
		float y = cellHeight * getRow();
		g2d.setColor(new Color(0xFB993F));
		Rectangle2D.Float rect = new Rectangle2D.Float(x, y, cellWidth, cellHeight); 
		g2d.fill(rect);
		g2d.setColor(new Color(0xF77600));
		rect = new Rectangle2D.Float(x, y, cellWidth, cellHeight);
		g2d.draw(rect);
	}
}
