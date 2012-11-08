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
		float x = cellWidth * getColumn() + 1;
		float y = cellHeight * getRow() + 1;
		g2d.setColor(new Color(0xF5F5EE));
		Rectangle2D.Float rect = new Rectangle2D.Float(x, y, cellWidth, cellHeight);
		g2d.fill(rect);
		
		if (isDoorway()) {
			g2d.setColor(new Color(0x008597));
			switch (doorDirection) {
			case UP:
				rect = new Rectangle2D.Float(x, y, cellWidth - 1, DOOR_SIZE);
				break;
			case DOWN:
				rect = new Rectangle2D.Float(x, y + cellHeight - DOOR_SIZE - 1, cellWidth - 1, DOOR_SIZE + 1);
				break;
			case RIGHT:
				rect = new Rectangle2D.Float(x + cellWidth - DOOR_SIZE - 1, y, DOOR_SIZE + 1, cellHeight - 1);
				break;
			case LEFT:
				rect = new Rectangle2D.Float(x, y, DOOR_SIZE, cellHeight - 1);
				break;
			}
			g2d.fill(rect);
		}
	}
	
	private static final int DOOR_SIZE = 4;
}
