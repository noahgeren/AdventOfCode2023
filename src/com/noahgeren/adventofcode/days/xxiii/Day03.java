package com.noahgeren.adventofcode.days.xxiii;

import java.util.List;

import com.noahgeren.adventofcode.data.DataLoader;
import com.noahgeren.adventofcode.days.Day;

public class Day03 extends Day {

	private char[][] schematic;

	@Override
	public void loadResources() throws Exception {
		List<String> lines = DataLoader.readLines("day03.txt");
		schematic = new char[lines.size()][];
		for (int i = 0; i < schematic.length; i++) {
			schematic[i] = lines.get(i).toCharArray();
		}
	}

	@Override
	public String solve(boolean firstPart) throws Exception {
		return String.valueOf(firstPart ? solveFirstPart() : solveSecondPart());
	}

	private long solveFirstPart() {
		long sum = 0;
		for (int row = 0; row < schematic.length; row++) {
			long number = 0;
			boolean adjacent = false;
			for (int col = 0; col < schematic[row].length; col++) {
				char c = schematic[row][col];
				if (c >= '0' && c <= '9') {
					number = (number * 10) + (c - '0');
					if (!adjacent) {
						adjacentCheck: for (int y = -1; y <= 1; y++) {
							for (int x = -1; x <= 1; x++) {
								if (row + y < 0 || row + y >= schematic.length || col + x < 0
										|| col + x >= schematic[row + y].length) {
									// out of bounds
									continue;
								}
								char s = schematic[row + y][col + x];
								if (s != '.' && !(s >= '0' && s <= '9')) {
									// is symbol
									adjacent = true;
									break adjacentCheck;
								}
							}
						}
					}
				} else {
					if (number != 0 && adjacent) {
						sum += number;
					}
					adjacent = false;
					number = 0;
				}
			}
			if (number != 0 && adjacent) {
				sum += number;
			}
		}
		return sum;
	}

	private long solveSecondPart() {
		long sum = 0;
		for (int row = 0; row < schematic.length; row++) {
			for (int col = 0; col < schematic[row].length; col++) {
				if (schematic[row][col] != '*')
					continue;
				// Found gear, need to check adjacent count
				int adjacentCount = 0;
				long ratio = 1;

				for (int y = -1; y <= 1; y++) {
					for (int x = -1; x <= 1; x++) {
						if (row + y < 0 || row + y >= schematic.length || col + x < 0
								|| col + x >= schematic[row + y].length) {
							// out of bounds
							continue;
						}
						char c = schematic[row + y][col + x];
						if (!(c >= '0' && c <= '9')) {
							// not number
							continue;
						}
						int leftEnd = findEnd(row + y, col + x, false), rightEnd = findEnd(row + y, col + x, true);
						long number = 0;
						for (int i = leftEnd; i <= rightEnd; i++) {
							number = (number * 10) + (schematic[row + y][i] - '0');
						}
						adjacentCount++;
						ratio *= number;
						x = rightEnd - col;
					}
				}
				if (adjacentCount == 2) {
					sum += ratio;
				}
			}
		}
		return sum;
	}

	private int findEnd(int row, int col, boolean right) {
		int end = col;
		for (; end < schematic[row].length && end >= 0; end += (right ? 1 : -1)) {
			char c = schematic[row][end];
			if (!(c >= '0' && c <= '9'))
				break;
		}
		return end + (right ? -1 : 1);
	}

}
