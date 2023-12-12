package com.noahgeren.adventofcode.problems.xxiii;

import java.util.ArrayList;
import java.util.List;

import com.noahgeren.adventofcode.Day;
import com.noahgeren.adventofcode.data.DataLoader;

public class Day12 extends Day {

	char[][] arrangements;
	int[][] counts;
	long[][] arrangementCounts;

	@Override
	public void loadResources() throws Exception {
		List<String> linesTemp = DataLoader.readLines("day12.txt");
		arrangements = new char[linesTemp.size()][];
		counts = new int[linesTemp.size()][];
		arrangementCounts = new long[arrangements.length][];
		for (int i = 0; i < linesTemp.size(); i++) {
			String[] line = linesTemp.get(i).split(" ");
			arrangements[i] = line[0].toCharArray();
			String[] strCounts = line[1].split(",");
			counts[i] = new int[strCounts.length];
			for (int j = 0; j < strCounts.length; j++) {
				counts[i][j] = Integer.valueOf(strCounts[j]);
			}
			arrangementCounts[i] = new long[]{0, 0};
		}
	}

	@Override
	public String solve(boolean firstPart) throws Exception {
		if(!firstPart) {
			char[][] newArrangements = new char[arrangements.length][];
			for(int i = 0; i < arrangements.length; i++) {
				newArrangements[i] = new char[arrangements[i].length * 2 + 1];
				for(int j = 0; j < arrangements[i].length; j++) {
					newArrangements[i][j] = arrangements[i][j];
					newArrangements[i][j + arrangements[i].length + 1] = arrangements[i][j];
				}
				newArrangements[i][arrangements[i].length] = '?';
			}
			arrangements = newArrangements;
			int[][] newCounts = new int[counts.length][];
			for(int i = 0; i < counts.length; i++) {
				newCounts[i] = new int[counts[i].length * 2];
				for(int j = 0; j < counts[i].length; j++) {
					newCounts[i][j] = counts[i][j];
					newCounts[i][j + counts[i].length] = counts[i][j];
				}
			}
			counts = newCounts;
		}
		
		long totalArrangements = 0;

		for (int i = 0; i < arrangements.length; i++) {
			System.out.println((i + 1) + "/" + arrangements.length);
			long count = countArrangements(arrangements[i], counts[i], 0);
			arrangementCounts[i][firstPart ? 0 : 1] = count;
			if(!firstPart) {
				long ratio = arrangementCounts[i][1] / arrangementCounts[i][0];
				for(int j = 0; j < 3; j++) {
					count *= ratio;
				}
			}
			totalArrangements += count;
//			System.out.println(count);
		}

		return String.valueOf(totalArrangements);
	}

	private long countArrangements(char[] arr, int[] counts, int index) {
		if (index >= arr.length) {
			return checkValid(arr, counts) ? 1 : 0;
		}
		if (arr[index] == '?') {
			arr[index] = '#';
			long filled = countArrangements(arr, counts, index + 1);
			arr[index] = '.';
			long notFilled = countArrangements(arr, counts, index + 1);
			arr[index] = '?';
			return filled + notFilled;
		}
		return countArrangements(arr, counts, index + 1);
	}

	private boolean checkValid(char[] arr, int[] counts) {
		List<Integer> countCheck = new ArrayList<>();
		countCheck.add(0);
		for (char c : arr) {
			if (c == '#') {
				countCheck.set(countCheck.size() - 1, countCheck.get(countCheck.size() - 1) + 1);
			} else if (countCheck.get(countCheck.size() - 1) != 0) {
				countCheck.add(0);
			}
		}
		if(countCheck.get(countCheck.size() - 1) == 0) {
			countCheck.remove(countCheck.size() - 1);
		}
		if (countCheck.size() != counts.length) {
			return false;
		}
		for (int i = 0; i < counts.length; i++) {
			if (counts[i] != countCheck.get(i)) {
				return false;
			}
		}
		return true;
	}

}
