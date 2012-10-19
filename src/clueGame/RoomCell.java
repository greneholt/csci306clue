package clueGame;

public class RoomCell extends BoardCell {

		public enum DoorDirection { UP, DOWN, LEFT, RIGHT, NONE }

		private DoorDirection doorDirection;
		private char room;
		
		@Override
		public boolean isRoom() {
			if (this.getInitial() != 'X' && !this.isWalkway()) {
				return true;
			} else {
				return false;
			}
		}
		
		public boolean isWalkway() {
			if (this.getInitial() == 'W') {
				return true;
			} else {
				return false;
			}
		}
		
		public boolean isDoorway() {
			if(doorDirection != DoorDirection.NONE) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		void draw() {
			// TODO Auto-generated method stub
		}

		public String getDoorDirection() {
			String direction = "";
			if(doorDirection == DoorDirection.UP) {
				direction = "UP";
			} else if(doorDirection == DoorDirection.DOWN) {
				direction = "DOWN";
			} else if(doorDirection == DoorDirection.LEFT) {
				direction = "LEFT";
			} else if(doorDirection == DoorDirection.RIGHT) {
				direction = "RIGHT";
			} else if(doorDirection == DoorDirection.NONE) {
				direction = "NONE";
			}
			return direction;
		}

		public char getInitial() {
			return room;
		}
		
		
}
