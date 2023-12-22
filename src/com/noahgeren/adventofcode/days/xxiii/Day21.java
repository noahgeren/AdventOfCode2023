package com.noahgeren.adventofcode.days.xxiii;

import java.util.HashSet;
import java.util.List;

import com.noahgeren.adventofcode.data.DataLoader;
import com.noahgeren.adventofcode.days.Day;
import com.noahgeren.adventofcode.util.Coordinate;
import com.noahgeren.adventofcode.util.Direction;

public class Day21 extends Day {

	static final int STEPS_PART_ONE = 64;

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
