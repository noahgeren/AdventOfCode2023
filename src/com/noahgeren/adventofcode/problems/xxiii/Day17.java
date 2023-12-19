package com.noahgeren.adventofcode.problems.xxiii;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.noahgeren.adventofcode.Day;
import com.noahgeren.adventofcode.data.DataLoader;
import com.noahgeren.adventofcode.util.Coordinate;
import com.noahgeren.adventofcode.util.Direction;

public class Day17 extends Day {

	static Map<Direction, Direction[]> leftsAndRights = new HashMap<>();

	long[][] grid;
	Map<DirectionalCoordinate, Long> cache = new HashMap<>();

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
		// TODO: Switch to Dijkstra's algorithm
		return String.valueOf(findShortestPath(new int[] { 0, 0 }, new HashSet<>(), Direction.RIGHT, 0) - grid[0][0]);
	}

	private long findShortestPath(int[] coord, Set<Coordinate> seen, Direction lastDirection, int currentLength) {
		long shortestPath = Integer.MAX_VALUE;
		int row = coord[0], col = coord[1];
		Coordinate coordObj = new Coordinate(row, col);
		if(row == grid.length - 1 && col == grid[0].length - 1) {
			return grid[row][col];
		}
		if (row < 0 || row >= grid.length || col < 0 || col >= grid[row].length || seen.contains(coordObj)) {
			return shortestPath;
		}
		seen = new HashSet<>(seen);
		seen.add(coordObj);

		DirectionalCoordinate directionalCoord = new DirectionalCoordinate(row, col, lastDirection, currentLength);
		if (cache.containsKey(directionalCoord)) {
			return grid[row][col] + cache.get(directionalCoord);
		}

		// Go straight
		if (currentLength < 4) {
			shortestPath = Math.min(shortestPath,
					findShortestPath(lastDirection.getNextCoord(coord), seen, lastDirection, currentLength + 1));
		}

		// Turn left or right
		if (lastDirection == Direction.LEFT || lastDirection == Direction.RIGHT) {
			shortestPath = Math.min(shortestPath,
					findShortestPath(Direction.UP.getNextCoord(coord), seen, Direction.UP, 1));
			shortestPath = Math.min(shortestPath,
					findShortestPath(Direction.DOWN.getNextCoord(coord), seen, Direction.DOWN, 1));
		} else {
			shortestPath = Math.min(shortestPath,
					findShortestPath(Direction.LEFT.getNextCoord(coord), seen, Direction.LEFT, 1));
			shortestPath = Math.min(shortestPath,
					findShortestPath(Direction.RIGHT.getNextCoord(coord), seen, Direction.RIGHT, 1));
		}

		cache.put(directionalCoord, shortestPath);
		return grid[row][col] + shortestPath;
	}

	private static class DirectionalCoordinate extends Coordinate {
		Direction lastDirection;
		int currentLength;

		public DirectionalCoordinate(int row, int col, Direction lastDirection, int currentLength) {
			super(row, col);
			this.lastDirection = lastDirection;
			this.currentLength = currentLength;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + Objects.hash(currentLength, lastDirection);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			DirectionalCoordinate other = (DirectionalCoordinate) obj;
			return currentLength == other.currentLength && lastDirection == other.lastDirection;
		}
	}

}
