package com.noahgeren.adventofcode.problems.xxiii;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.noahgeren.adventofcode.Day;
import com.noahgeren.adventofcode.data.DataLoader;
import com.noahgeren.adventofcode.util.Coordinate;
import com.noahgeren.adventofcode.util.Direction;

public class Day18 extends Day {

	List<String> lines;

	@Override
	public void loadResources() throws Exception {
		lines = DataLoader.readLines("day18.txt");
	}

	@Override
	public String solve(boolean firstPart) throws Exception {
		Set<Coordinate> trench = new HashSet<>();
		Coordinate currentCoord = new Coordinate(0, 0);
		trench.add(currentCoord);
		for (String line : lines) {
			String[] tokens = line.split(" ");
			Direction direction = getDirection(tokens[0]);
			int times = Integer.valueOf(tokens[1]);
			for (int i = 0; i < times; i++) {
				currentCoord = currentCoord.getNextCoord(direction);
				trench.add(currentCoord);
			}
		}
		long maxRow = Long.MIN_VALUE, minRow = Long.MAX_VALUE;
		long maxCol = Long.MIN_VALUE, minCol = Long.MAX_VALUE;
		for (Coordinate coord : trench) {
			if (coord.row > maxRow) {
				maxRow = coord.row;
			}
			if (coord.row < minRow) {
				minRow = coord.row;
			}
			if (coord.col > maxCol) {
				maxCol = coord.col;
			}
			if (coord.col < minCol) {
				minCol = coord.col;
			}
		}
		long volume = 0;
		for (long row = minRow; row <= maxRow; row++) {
			for (long col = minCol; col <= maxCol; col++) {
				if (isInsideTrench(trench, new Coordinate(row, col), minRow, minCol)) {
					volume++;
				}
			}
		}
		return String.valueOf(volume);
	}

	private boolean isInsideTrench(Set<Coordinate> trench, Coordinate coord, long minRow, long minCol) {
		long crossingPoints = 0;
		if (trench.contains(coord)) {
			return true;
		}
		Coordinate temp = new Coordinate(coord.row, coord.col);
		while (temp.row >= minRow && temp.col >= minCol) {
			if (trench.contains(temp) && !isCorner(trench, temp)) {
				crossingPoints++;
			}
			temp.row--;
			temp.col--;
		}
		return crossingPoints % 2 == 1;
	}

	private boolean isCorner(Set<Coordinate> trench, Coordinate coord) {
		return (!trench.contains(new Coordinate(coord.row - 1, coord.col))
				&& !trench.contains(new Coordinate(coord.row, coord.col + 1)))
				|| (!trench.contains(new Coordinate(coord.row + 1, coord.col))
						&& !trench.contains(new Coordinate(coord.row, coord.col - 1)));
	}

	private Direction getDirection(String firstChar) {
		for (Direction d : Direction.values()) {
			if (d.name().startsWith(firstChar)) {
				return d;
			}
		}
		return null;
	}

}
