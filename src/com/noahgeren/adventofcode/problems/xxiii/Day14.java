package com.noahgeren.adventofcode.problems.xxiii;

import java.util.List;

import com.noahgeren.adventofcode.Day;
import com.noahgeren.adventofcode.data.DataLoader;

public class Day14 extends Day {
	
	char[][] lines;

	@Override
	public void loadResources() throws Exception {
		List<String> lines = DataLoader.readLines("day14.txt");
		this.lines = new char[lines.size()][];
		for(int i = 0; i < lines.size(); i++) {
			this.lines[i] = lines.get(i).toCharArray();
		}
	}

	@Override
	public String solve(boolean firstPart) throws Exception {
		long weight = 0;
		for(int row = 0; row < lines.length; row++) {
			for(int col = 0; col < lines[row].length; col++) {
				if(lines[row][col] != 'O') continue;
				int tempRow = row;
				while(tempRow >= 1 && lines[tempRow - 1][col] == '.') {
					tempRow--;
				}
				lines[row][col] = '.';
				lines[tempRow][col] = 'O';
				weight += lines.length - tempRow;
			}
		}
		return String.valueOf(weight);
	}
	
	private void print() {
		for(char[] line : lines) {
			for(char c : line) {
				System.out.print(c);
			}
			System.out.println();
		}
	}

}
