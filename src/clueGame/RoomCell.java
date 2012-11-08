package clueGame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class RoomCell extends BoardCell {

	public enum DoorDirection {
		UP, DOWN, LEFT, RIGHT, NONE
	}

	private DoorDirection doorDirection;
	private char initial;

	public RoomCell(int row, int column, int index, char initial, DoorDirection doorDirection) {
		super(row, column, index);
		this.initial = initial;
		this.doorDirection = doorDirection;
	}

	public DoorDirection getDoorDirection() {
		return doorDirection;
	}

	public char getInitial() {
		return initial;
	}

	@Override
	public boolean isRoom() {
		return true;
	}

	@Override
	public boolean isDoorway() {
		return doorDirection != DoorDirection.NONE;
	}

	@Override
	public void draw(Graphics2D g2d, float cellWidth, float cellHeight) {
		float x = cellWidth * getColumn();
		float y = cellHeight * getRow();
		g2d.setColor(new Color(0xdddddd));
		Rectangle2D.Float rect = new Rectangle2D.Float(x, y, cellWidth, cellHeight);
		g2d.fill(rect);
	}
}
