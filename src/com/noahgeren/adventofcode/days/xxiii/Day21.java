package com.noahgeren.adventofcode.days.xxiii;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;

import com.noahgeren.adventofcode.data.DataLoader;
import com.noahgeren.adventofcode.days.Day;
import com.noahgeren.adventofcode.util.Coordinate;
import com.noahgeren.adventofcode.util.Direction;

public class Day21 extends Day {

	static final int STEPS_PART_ONE = 64;
	static final int STEPS_PART_TWO = 26501365;

	char[][] map;

	@Override
	public void loadResources() throws Exception {
		List<String> lines = DataLoader.readLines("day21.txt");
		map = new char[lines.size()][];
		for (int i = 0; i < map.length; i++) {
			map[i] = lines.get(i).toCharArray();
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
		HashSet<Coordinate> edge = new HashSet<>();
		HashSet<Coordinate> newEdge = new HashSet<>();

		edge.add(findStart());

		for (int i = 0; i < STEPS_PART_ONE; i++) {
			newEdge.clear();

			for (Coordinate coord : edge) {
				for (Direction direction : Direction.values()) {
					Coordinate newCoord = coord.getNextCoord(direction);
					if (newCoord.inBounds(map.length, map[0].length) && map[newCoord.row][newCoord.col] != '#') {
						newEdge.add(newCoord);
					}
				}
			}

			edge.clear();
			edge.addAll(newEdge);
		}
		
		for (int row = 0; row < map.length; row++) {
			for (int col = 0; col < map[row].length; col++) {
				if(edge.contains(new Coordinate(row, col))) {
					System.out.print("O");
				} else {
					System.out.print(map[row][col]);
				}
			}
			System.out.println();
		}

		return String.valueOf(edge.size());
	}

	private String solveSecondPart() {
		BigInteger reachable = BigInteger.ZERO;
		long totalRows = STEPS_PART_TWO * 2 + 1;
		int startingCannonRow = (int)(((map.length - Math.floorDiv(totalRows + map.length, 2L)) % map.length + map.length) % map.length);
		for (long row = 0; row < totalRows; row++) {
			System.out.println(row);
			long rowLength = 2 * (STEPS_PART_TWO - Math.abs(STEPS_PART_TWO - row)) + 1;
			long fullyFilled = Math.max(0, 2 * Math.floorDiv(rowLength - map[0].length, 2 * map[0].length) + 1);
			int cannonRow = (int)((startingCannonRow + row) % map.length);
			long fullyFilledCount = 0;
			int offset = (int)(STEPS_PART_TWO % 2 == 0 ? (row % 2) : ((row + 1) % 2));
			for(int col = offset; col < map[0].length; col += 2) {
				if(map[cannonRow][col] != '#') {
					fullyFilledCount++;
				}
			}
			long partial = (rowLength - map.length * fullyFilled) / 2;
			long right = 0, left = 0;
			for(long i = 0; i < partial; i++) {
				if(map[cannonRow][(int)(map.length - i - 1 - ((offset + 1) % 2))] != '#') {
					right++;
				}
				if(map[cannonRow][(int)(i + ((offset + 1) % 2))] != '#') {
					left++;
				}
			}
			reachable = reachable.add(BigInteger.valueOf(fullyFilled * fullyFilledCount)).add(BigInteger.valueOf(right + left));
		}
		return reachable.toString();
	}

	private Coordinate findStart() {
		for (int row = 0; row < map.length; row++) {
			for (int col = 0; col < map[row].length; col++) {
				if (map[row][col] == 'S')
					return new Coordinate(row, col);
			}
		}
		return null;
	}

}
