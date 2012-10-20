package clueGame;

public class WalkwayCell extends BoardCell {
	
	public WalkwayCell() { super(); }

	public boolean isWalkway() { return true; }
	public boolean isRoom() { return false; }
	public boolean isDoorway() { return false; }
	
	void draw() {
		// TODO Auto-generated method stub

	}
	@Override
	public char getInitial() {
		// TODO Auto-generated method stub
		return 'W';
	}
}
