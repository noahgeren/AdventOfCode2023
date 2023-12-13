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
			count += maps.get(i).findCount();
		}
		return String.valueOf(count);
	}
	
	private class Map {
		char[][] lines;
		
		public Map(List<String> lines) {
			this.lines = new char[lines.size()][];
			for(int i = 0; i < lines.size(); i++) {
				this.lines[i] = lines.get(i).toCharArray();
			}
		}
		
		public long findCount() {
			Integer vertical = findVerticalReflectionIndex();
			if(vertical != null) {
				return vertical;
			}
			return 100 * findHorizontalReflectionIndex();
		}
		
		private Integer findVerticalReflectionIndex() {
			outer: for(int i = 1; i < lines[0].length; i++) {
				int size = Math.min(i, lines[0].length - i);
				for(int row = 0; row < lines.length; row++) {
					for(int col = i - size; col < i; col++) {
						char left = lines[row][col], right = lines[row][2 * i - col - 1];
						if(left != right) {
							continue outer;
						}
					}
				}
				return i;
			}
			return null;
		}
		
		private Integer findHorizontalReflectionIndex() {
			outer: for(int i = 1; i < lines.length; i++) {
				int size = Math.min(i, lines.length - i);
				for(int col = 0; col < lines[0].length; col++) {
					for(int row = i - size; row < i; row++) {
						char top = lines[row][col], bottom = lines[2 * i - row - 1][col];
						if(top != bottom) {
							continue outer;
						}
					}
				}
				return i;
			}
			return null;
		}
	}

}
