package com.noahgeren.adventofcode.problems.xxiii;

import java.util.ArrayList;
import java.util.List;

import com.noahgeren.adventofcode.Day;
import com.noahgeren.adventofcode.data.DataLoader;

public class Day11 extends Day {

	List<List<Character>> image = new ArrayList<>();
	List<Integer> emptyRows = new ArrayList<>();
	List<Integer> emptyCols = new ArrayList<>();
	List<Coordinate> galaxies = new ArrayList<>();

	@Override
	public void loadResources() throws Exception {
		List<String> lines = DataLoader.readLines("day11.txt");
		for (String line : lines) {
			List<Character> row = new ArrayList<>();
			for (char c : line.toCharArray()) {
				row.add(c);
			}
			image.add(row);
		}
	}

	@Override
	public String solve(boolean firstPart) throws Exception {
		if(firstPart) {
			findEmptyRowsAndColumns();
			for (int row = 0; row < image.size(); row++) {
				for (int col = 0; col < image.get(row).size(); col++) {
					if (image.get(row).get(col) == '#') {
						galaxies.add(new Coordinate(row, col));
					}
				}
			}
		}
		long totalDistance = 0;
		for (int a = 0; a < galaxies.size() - 1; a++) {
			Coordinate galaxyA = galaxies.get(a);
			for (int b = a + 1; b < galaxies.size(); b++) {
				Coordinate galaxyB = galaxies.get(b);
				totalDistance += galaxyA.distance(galaxyB, firstPart ? 2 : 1_000_000);
			}
		}
		return String.valueOf(totalDistance);
	}
	
	private void findEmptyRowsAndColumns() {
		emptyRows = new ArrayList<>();
		for (int row = 0; row < image.size(); row++) {
			boolean empty = true;
			for (char c : image.get(row)) {
				if (c != '.') {
					empty = false;
				}
			}
			if (empty) {
				emptyRows.add(row);
			}
		}
		emptyCols = new ArrayList<>();
		for (int col = 0; col < image.get(0).size(); col++) {
			boolean empty = true;
			for (int row = 0; row < image.size(); row++) {
				if (image.get(row).get(col) != '.') {
					empty = false;
				}
			}
			if (empty) {
				emptyCols.add(col);
			}
		}
	}

	private class Coordinate {
		private int row, col;

		public Coordinate(int row, int col) {
			this.row = row;
			this.col = col;
		}

		private long distance(Coordinate b, long emptyModifier) {
			long distance = 0;
			int rowStart = Math.min(row, b.row), rowEnd = Math.max(row, b.row);
			for(; rowStart < rowEnd; rowStart++) {
				if(emptyRows.contains(rowStart)) {
					distance += emptyModifier;
				} else {
					distance++;
				}
			}
			int colStart = Math.min(col, b.col), colEnd = Math.max(col, b.col);
			for(; colStart < colEnd; colStart++) {
				if(emptyCols.contains(colStart)) {
					distance += emptyModifier;
				} else {
					distance++;
				}
			}
			return distance;
		}
	}

}
