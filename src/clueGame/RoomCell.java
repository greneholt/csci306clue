package clueGame;

public class RoomCell extends BoardCell {

		public enum DoorDirection { UP, DOWN, LEFT, RIGHT, NONE }

		private DoorDirection doorDirection;
		private char room;
		
		@Override
		public boolean isRoom() {return false;}

		@Override
		void draw() {
			// TODO Auto-generated method stub
		}

		public String getDoorDirection() {
			// TODO Auto-generated method stub
			return null;
		}

		public char getInitial() {
			// TODO Auto-generated method stub
			return room;
		}
		
		
}
