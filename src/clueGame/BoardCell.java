package clueGame;

public abstract class BoardCell {
	
	int row;
	int col;
	
	public abstract boolean isWalkway();
	public abstract boolean isRoom();
	public abstract boolean isDoorway();
	
	abstract void draw();
	
	public int getIndex() {
		return (row + 1)*col;
	}
}
