package com.noahgeren.adventofcode.problems.xxiii;

import java.util.ArrayList;
import java.util.List;

import com.noahgeren.adventofcode.Day;
import com.noahgeren.adventofcode.data.DataLoader;

public class Day13 extends Day {
	
	List<Map> maps = new ArrayList<>();

	@Override
	public void loadResources() throws Exception {
		List<String> lines = new ArrayList<>();
		for(String line : DataLoader.readLines("day13.txt")) {
			if(line.trim().isEmpty()) {
				maps.add(new Map(lines));
				lines = new ArrayList<>();
				continue;
			}
			lines.add(line);
		}
		if(!lines.isEmpty()) {
			maps.add(new Map(lines));
		}
	}

	@Override
	public String solve(boolean firstPart) throws Exception {
		long count = 0;
		for(int i = 0; i < maps.size(); i++) {
			count += maps.get(i).findCount(firstPart);
		}
		return String.valueOf(count);
	}
	
	private class Map {
		char[][] lines;
		// First index is 0 - firstPart, 1 - secondPart
		// Second index is 0 - vertical, 1 - horizontal
		int[][] reflectionIndex = new int[2][2];
		
		public Map(List<String> lines) {
			this.lines = new char[lines.size()][];
			for(int i = 0; i < lines.size(); i++) {
				this.lines[i] = lines.get(i).toCharArray();
			}
		}
		
		public long findCount(boolean firstPart) {
			Integer vertical = findVerticalReflectionIndex(firstPart);
			if(vertical != null) {
				return vertical;
			}
			return 100 * findHorizontalReflectionIndex(firstPart);
		}
		
		private Integer findVerticalReflectionIndex(boolean firstPart) {
			outer: for(int i = 1; i < lines[0].length; i++) {
				int size = Math.min(i, lines[0].length - i);
				boolean changed = false;
				for(int row = 0; row < lines.length; row++) {
					for(int col = i - size; col < i; col++) {
						char left = lines[row][col], right = lines[row][2 * i - col - 1];
						if(left != right) {
							if(!firstPart && !changed) {
								changed = true;
								continue;
							}
							continue outer;
						}
					}
				}
				if(i == reflectionIndex[0][0]) {
					continue outer;
				}
				reflectionIndex[firstPart ? 0 : 1][0] = i;
				return i;
			}
			return null;
		}
		
		private Integer findHorizontalReflectionIndex(boolean firstPart) {
			outer: for(int i = 1; i < lines.length; i++) {
				int size = Math.min(i, lines.length - i);
				boolean changed = false;
				for(int col = 0; col < lines[0].length; col++) {
					for(int row = i - size; row < i; row++) {
						char top = lines[row][col], bottom = lines[2 * i - row - 1][col];
						if(top != bottom) {
							if(!firstPart && !changed) {
								changed = true;
								continue;
							}
							continue outer;
						}
					}
				}
				if(i == reflectionIndex[0][1]) {
					continue outer;
				}
				reflectionIndex[firstPart ? 0 : 1][1] = i;
				return i;
			}
			return null;
		}
	}

}
