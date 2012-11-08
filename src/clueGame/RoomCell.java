package clueGame;

import java.awt.Color;
import java.awt.Graphics;

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
	public void draw(Graphics g, float cellWidth, float cellHeight) {
		int x = (int)(cellWidth * getColumn());
		int y = (int)(cellHeight * getRow());
		g.setColor(new Color(0x33B8CB));
		g.fillRect(x, y, (int)cellWidth + 2, (int)cellHeight + 2);
	}
}
