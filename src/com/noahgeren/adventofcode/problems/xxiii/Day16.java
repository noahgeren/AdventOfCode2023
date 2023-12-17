package com.noahgeren.adventofcode.problems.xxiii;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.noahgeren.adventofcode.Day;
import com.noahgeren.adventofcode.data.DataLoader;
import com.noahgeren.adventofcode.util.Direction;

public class Day16 extends Day {

	char[][] grid;

	@Override
	public void loadResources() throws Exception {
		List<String> lines = DataLoader.readLines("day16.txt");
		grid = new char[lines.size()][];
		for (int i = 0; i < lines.size(); i++) {
			grid[i] = lines.get(i).toCharArray();
		}
	}

	@Override
	public String solve(boolean firstPart) throws Exception {
		if (firstPart) {
			return solveFirstPart();
		}
		return solveSecondPart();
	}

	private String solveFirstPart() {
		boolean[][] energy = runSimulation(new int[] { 0, 0 }, Direction.RIGHT, new HashSet<>(),
				new boolean[grid.length][grid[0].length]);
		return String.valueOf(getTotalEnergy(energy));
	}

	private String solveSecondPart() {
		long maxEnergy = 0;
		for (int row = 0; row < grid.length; row++) {
			boolean[][] energy = runSimulation(new int[] { row, 0 }, Direction.RIGHT, new HashSet<>(),
					new boolean[grid.length][grid[0].length]);
			long totalEnergy = getTotalEnergy(energy);
			if (totalEnergy > maxEnergy) {
				maxEnergy = totalEnergy;
			}

			energy = runSimulation(new int[] { row, grid[row].length - 1 }, Direction.LEFT, new HashSet<>(),
					new boolean[grid.length][grid[0].length]);
			totalEnergy = getTotalEnergy(energy);
			if (totalEnergy > maxEnergy) {
				maxEnergy = totalEnergy;
			}
		}

		for (int col = 0; col < grid[0].length; col++) {
			boolean[][] energy = runSimulation(new int[] { 0, col }, Direction.DOWN, new HashSet<>(),
					new boolean[grid.length][grid[0].length]);
			long totalEnergy = getTotalEnergy(energy);
			if (totalEnergy > maxEnergy) {
				maxEnergy = totalEnergy;
			}

			energy = runSimulation(new int[] { grid.length - 1, col }, Direction.UP, new HashSet<>(),
					new boolean[grid.length][grid[0].length]);
			totalEnergy = getTotalEnergy(energy);
			if (totalEnergy > maxEnergy) {
				maxEnergy = totalEnergy;
			}
		}

		return String.valueOf(maxEnergy);
	}

	private long getTotalEnergy(boolean[][] energy) {
		long totalEnergy = 0;
		for (int row = 0; row < energy.length; row++) {
			for (int col = 0; col < energy[row].length; col++) {
				if (energy[row][col]) {
					totalEnergy++;
				}
			}
		}
		return totalEnergy;
	}

	private boolean[][] runSimulation(int[] coord, Direction direction, Set<DirectionalCell> seen, boolean[][] energy) {
		int row = coord[0], col = coord[1];

		DirectionalCell directionalCell = new DirectionalCell(row, col, direction);
		if (row < 0 || row >= grid.length || col < 0 || col >= grid[row].length || seen.contains(directionalCell)) {
			return energy;
		}
		seen.add(directionalCell);

		char cell = grid[row][col];
		energy[row][col] = true;
		Direction newDirection = getNewDirection(cell, direction);
		switch (cell) {
		case '.':
			runSimulation(getNextCoord(coord, direction), direction, seen, energy);
			break;
		case '/':
			runSimulation(getNextCoord(coord, newDirection), newDirection, seen, energy);
			break;
		case '\\':
			runSimulation(getNextCoord(coord, newDirection), newDirection, seen, energy);
			break;
		case '|':
			if (direction == Direction.UP || direction == Direction.DOWN) {
				runSimulation(getNextCoord(coord, direction), direction, seen, energy);
			} else {
				runSimulation(getNextCoord(coord, Direction.UP), Direction.UP, seen, energy);
				runSimulation(getNextCoord(coord, Direction.DOWN), Direction.DOWN, seen, energy);
			}
			break;
		case '-':
			if (direction == Direction.LEFT || direction == Direction.RIGHT) {
				runSimulation(getNextCoord(coord, direction), direction, seen, energy);
			} else {
				runSimulation(getNextCoord(coord, Direction.LEFT), Direction.LEFT, seen, energy);
				runSimulation(getNextCoord(coord, Direction.RIGHT), Direction.RIGHT, seen, energy);
			}
			break;
		}
		return energy;
	}

	private int[] getNextCoord(int[] startingCoord, Direction direction) {
		switch (direction) {
		case LEFT:
		case RIGHT:
			return new int[] { startingCoord[0], startingCoord[1] + (direction == Direction.LEFT ? -1 : 1) };
		case UP:
		case DOWN:
			return new int[] { startingCoord[0] + (direction == Direction.UP ? -1 : 1), startingCoord[1] };
		}
		return null;
	}

	private Direction getNewDirection(char cell, Direction direction) {
		Direction newDirection = null;
		switch (direction) {
		case LEFT:
			newDirection = Direction.DOWN;
			break;
		case RIGHT:
			newDirection = Direction.UP;
			break;
		case DOWN:
			newDirection = Direction.LEFT;
			break;
		case UP:
			newDirection = Direction.RIGHT;
			break;
		}
		if (cell == '/') {
			return newDirection;
		} else if (cell == '\\') {
			return getOppositeDirection(newDirection);
		}
		return null;
	}

	private Direction getOppositeDirection(Direction direction) {
		switch (direction) {
		case LEFT:
			return Direction.RIGHT;
		case RIGHT:
			return Direction.LEFT;
		case DOWN:
			return Direction.UP;
		case UP:
			return Direction.DOWN;
		}
		return null;
	}

	private static class DirectionalCell {
		int row, col;
		Direction direction;

		public DirectionalCell(int row, int col, Direction direction) {
			this.row = row;
			this.col = col;
			this.direction = direction;
		}

		@Override
		public int hashCode() {
			return Objects.hash(col, direction, row);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			DirectionalCell other = (DirectionalCell) obj;
			return col == other.col && direction == other.direction && row == other.row;
		}
	}

}
