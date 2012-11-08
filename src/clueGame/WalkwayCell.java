package clueGame;

public class WalkwayCell extends BoardCell {
	public WalkwayCell(int row, int column, int index) {
		super(row, column, index);
	}

	@Override
	public boolean isWalkway() {
		return true;
	}
}
