package com.noahgeren.adventofcode.util;

import java.util.Objects;

public class Coordinate {
	public long row, col;

	public Coordinate(long row, long col) {
		this.row = row;
		this.col = col;
	}
	
	public Coordinate getNextCoord(Direction direction) {
		return direction.getNextCoord(this);
	}
	
	@Override
	public String toString() {
		return String.format("(%d, %d)", row, col);
	}

	@Override
	public int hashCode() {
		return Objects.hash(col, row);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinate other = (Coordinate) obj;
		return col == other.col && row == other.row;
	}
}