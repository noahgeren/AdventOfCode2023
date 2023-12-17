package com.noahgeren.adventofcode.util;

public enum Direction {
	LEFT, RIGHT, UP, DOWN;
	
	public int[] getNextCoord(int[] startingCoord) {
		switch (this) {
		case LEFT:
		case RIGHT:
			return new int[] { startingCoord[0], startingCoord[1] + (this == Direction.LEFT ? -1 : 1) };
		case UP:
		case DOWN:
			return new int[] { startingCoord[0] + (this == Direction.UP ? -1 : 1), startingCoord[1] };
		}
		return null;
	}
}
