package clueGame;

public class RoomCell extends BoardCell {

		enum DoorDirection { UP, DOWN, LEFT, RIGHT, NONE }

		DoorDirection doorDirection;
		char room;
		
		@Override
		public boolean isRoom() {return false;}

		@Override
		void draw() {
			// TODO Auto-generated method stub
		}
		
		
}
