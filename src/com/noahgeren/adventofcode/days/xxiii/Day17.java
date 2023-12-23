package com.noahgeren.adventofcode.days.xxiii;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

import com.noahgeren.adventofcode.data.DataLoader;
import com.noahgeren.adventofcode.days.Day;
import com.noahgeren.adventofcode.util.Coordinate;
import com.noahgeren.adventofcode.util.Direction;

public class Day17 extends Day {

	long[][] grid;

	@Override
	public void loadResources() throws Exception {
		List<String> lines = DataLoader.readLines("day17.txt");
		grid = new long[lines.size()][];
		for (int row = 0; row < lines.size(); row++) {
			char[] line = lines.get(row).toCharArray();
			grid[row] = new long[line.length];
			for (int col = 0; col < line.length; col++) {
				grid[row][col] = line[col] - '0';
			}
		}
	}

	@Override
	public String solve(boolean firstPart) throws Exception {
		HashSet<ExtraCoordinate> seen = new HashSet<>();
		TreeSet<ExtraCoordinate> shortestPaths = new TreeSet<>();
		shortestPaths.add(new ExtraCoordinate(new Coordinate(0, 0), Direction.DOWN, 0, 0));
		while (!shortestPaths.isEmpty()) {
			ExtraCoordinate path = shortestPaths.pollFirst();
			if (seen.contains(path)) {
				continue;
			}
//			System.out.println(path);
			seen.add(path);
			if (path.row == grid.length - 1 && path.col == grid[0].length - 1) {
				if(firstPart || path.stepsInLastDirection > 3) {
					System.out.println(path);
					return String.valueOf(path.currentLength - grid[0][0]);
				}
			}
			Coordinate nextCoord;
			if(firstPart) {
				if (path.stepsInLastDirection < 3) {
					nextCoord = path.getNextCoord(path.lastDirection);
					if (nextCoord.inBounds(grid.length, grid[0].length)) {
						shortestPaths.add(new ExtraCoordinate(nextCoord, path.lastDirection, path.stepsInLastDirection + 1,
								path.currentLength));
					}
				}
				if (path.lastDirection == Direction.UP || path.lastDirection == Direction.DOWN) {
					nextCoord = path.getNextCoord(Direction.LEFT);
					if (nextCoord.inBounds(grid.length, grid[0].length)) {
						shortestPaths.add(new ExtraCoordinate(nextCoord, Direction.LEFT, 1, path.currentLength));
					}
					nextCoord = path.getNextCoord(Direction.RIGHT);
					if (nextCoord.inBounds(grid.length, grid[0].length)) {
						shortestPaths.add(new ExtraCoordinate(nextCoord, Direction.RIGHT, 1, path.currentLength));
					}
				} else {
					nextCoord = path.getNextCoord(Direction.UP);
					if (nextCoord.inBounds(grid.length, grid[0].length)) {
						shortestPaths.add(new ExtraCoordinate(nextCoord, Direction.UP, 1, path.currentLength));
					}
					nextCoord = path.getNextCoord(Direction.DOWN);
					if (nextCoord.inBounds(grid.length, grid[0].length)) {
						shortestPaths.add(new ExtraCoordinate(nextCoord, Direction.DOWN, 1, path.currentLength));
					}
				}
			} else {
				if(path.stepsInLastDirection < 4) {
					nextCoord = path.getNextCoord(path.lastDirection);
					if (nextCoord.inBounds(grid.length, grid[0].length)) {
						shortestPaths.add(new ExtraCoordinate(nextCoord, path.lastDirection, path.stepsInLastDirection + 1,
								path.currentLength));
					}
				} else {
					if(path.stepsInLastDirection < 10) {
						nextCoord = path.getNextCoord(path.lastDirection);
						if (nextCoord.inBounds(grid.length, grid[0].length)) {
							shortestPaths.add(new ExtraCoordinate(nextCoord, path.lastDirection, path.stepsInLastDirection + 1,
									path.currentLength));
						}
					}
					if (path.lastDirection == Direction.UP || path.lastDirection == Direction.DOWN) {
						nextCoord = path.getNextCoord(Direction.LEFT);
						if (nextCoord.inBounds(grid.length, grid[0].length)) {
							shortestPaths.add(new ExtraCoordinate(nextCoord, Direction.LEFT, 1, path.currentLength));
						}
						nextCoord = path.getNextCoord(Direction.RIGHT);
						if (nextCoord.inBounds(grid.length, grid[0].length)) {
							shortestPaths.add(new ExtraCoordinate(nextCoord, Direction.RIGHT, 1, path.currentLength));
						}
					} else {
						nextCoord = path.getNextCoord(Direction.UP);
						if (nextCoord.inBounds(grid.length, grid[0].length)) {
							shortestPaths.add(new ExtraCoordinate(nextCoord, Direction.UP, 1, path.currentLength));
						}
						nextCoord = path.getNextCoord(Direction.DOWN);
						if (nextCoord.inBounds(grid.length, grid[0].length)) {
							shortestPaths.add(new ExtraCoordinate(nextCoord, Direction.DOWN, 1, path.currentLength));
						}
					}
				}
			}
		}
		return null;
	}

	private class ExtraCoordinate extends Coordinate implements Comparable<ExtraCoordinate> {

		Direction lastDirection;
		int stepsInLastDirection;
		long currentLength;

		public ExtraCoordinate(Coordinate coord, Direction lastDirection, int stepsInLastDirection,
				long currentLength) {
			super(coord.row, coord.col);
			this.lastDirection = lastDirection;
			this.stepsInLastDirection = stepsInLastDirection;
			this.currentLength = currentLength + grid[coord.row][coord.col];
		}

		@Override
		public int compareTo(ExtraCoordinate o) {
			int compare = Long.compare(currentLength, o.currentLength);
			if (compare != 0) {
				return compare;
			}
			compare = this.hashCode() - o.hashCode();
			if(compare != 0) {
				return compare;
			}
			compare = this.lastDirection.ordinal() - o.lastDirection.ordinal();
			if(compare != 0) {
				return compare;
			}
			return this.stepsInLastDirection - o.stepsInLastDirection;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(super.hashCode(), lastDirection, stepsInLastDirection);
		}

		@Override
		public String toString() {
			return "ExtraCoordinate [lastDirection=" + lastDirection + ", stepsInLastDirection=" + stepsInLastDirection
					+ ", currentLength=" + currentLength + ", row=" + row + ", col=" + col + "]";
		}

	}

}
