package clueGame;

public abstract class BoardCell {
	
	int row;
	int col;
	
	public boolean isWalkway() { return true; }

	public boolean isRoom() { return true; }
	
	public boolean isDoorway() { return true; }
}
