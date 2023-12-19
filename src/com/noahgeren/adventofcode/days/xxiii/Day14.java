package com.noahgeren.adventofcode.days.xxiii;

import java.util.List;

import com.noahgeren.adventofcode.data.DataLoader;
import com.noahgeren.adventofcode.days.Day;

public class Day14 extends Day {

	char[][] lines;

	@Override
	public void loadResources() throws Exception {
		List<String> lines = DataLoader.readLines("day14.txt");
		this.lines = new char[lines.size()][];
		for (int i = 0; i < lines.size(); i++) {
			this.lines[i] = lines.get(i).toCharArray();
		}
	}

	@Override
	public void reset() throws Exception {
		loadResources();
	}

	@Override
	public String solve(boolean firstPart) throws Exception {

		if (firstPart) {
			tiltNorth();
		} else {
			for (int i = 0; i < 500_000; i++) {
				// This won't give you the answer, but it gives you enough information to finish
				// by hand
				System.out.println(i + ": " + getWeight());
				tiltNorth();
				tiltWest();
				tiltSouth();
				tiltEast();
			}
		}

		return String.valueOf(getWeight());
	}

	private long getWeight() {
		long weight = 0;
		for (int row = 0; row < lines.length; row++) {
			for (int col = 0; col < lines[row].length; col++) {
				if (lines[row][col] == 'O') {
					weight += lines.length - row;
				}
			}
		}
		return weight;
	}

	private void tiltNorth() {
		for (int row = 0; row < lines.length; row++) {
			for (int col = 0; col < lines[row].length; col++) {
				if (lines[row][col] != 'O')
					continue;
				int tempRow = row;
				while (tempRow >= 1 && lines[tempRow - 1][col] == '.') {
					tempRow--;
				}
				lines[row][col] = '.';
				lines[tempRow][col] = 'O';
			}
		}
	}

	private void tiltWest() {
		for (int col = 0; col < lines[0].length; col++) {
			for (int row = 0; row < lines.length; row++) {
				if (lines[row][col] != 'O')
					continue;
				int tempCol = col;
				while (tempCol >= 1 && lines[row][tempCol - 1] == '.') {
					tempCol--;
				}
				lines[row][col] = '.';
				lines[row][tempCol] = 'O';
			}
		}
	}

	private void tiltSouth() {
		for (int row = lines.length - 1; row >= 0; row--) {
			for (int col = 0; col < lines[row].length; col++) {
				if (lines[row][col] != 'O')
					continue;
				int tempRow = row;
				while (tempRow < lines.length - 1 && lines[tempRow + 1][col] == '.') {
					tempRow++;
				}
				lines[row][col] = '.';
				lines[tempRow][col] = 'O';
			}
		}
	}

	private void tiltEast() {
		for (int col = lines[0].length - 1; col >= 0; col--) {
			for (int row = 0; row < lines.length; row++) {
				if (lines[row][col] != 'O')
					continue;
				int tempCol = col;
				while (tempCol < lines[0].length - 1 && lines[row][tempCol + 1] == '.') {
					tempCol++;
				}
				lines[row][col] = '.';
				lines[row][tempCol] = 'O';
			}
		}
	}

}
