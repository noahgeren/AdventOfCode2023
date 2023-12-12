package com.noahgeren.adventofcode.problems.xxiii;

import java.util.ArrayList;
import java.util.List;

import com.noahgeren.adventofcode.Day;
import com.noahgeren.adventofcode.data.DataLoader;

public class Day12 extends Day {

	char[][] arrangements;
	int[][] counts;

	@Override
	public void loadResources() throws Exception {
		List<String> linesTemp = DataLoader.readLines("day12.txt");
		arrangements = new char[linesTemp.size()][];
		counts = new int[linesTemp.size()][];
		for (int i = 0; i < linesTemp.size(); i++) {
			String[] line = linesTemp.get(i).split(" ");
			arrangements[i] = line[0].toCharArray();
			String[] strCounts = line[1].split(",");
			counts[i] = new int[strCounts.length];
			for (int j = 0; j < strCounts.length; j++) {
				counts[i][j] = Integer.valueOf(strCounts[j]);
			}
		}
	}

	@Override
	public String solve(boolean firstPart) throws Exception {
		long totalArrangements = 0;

		for (int i = 0; i < arrangements.length; i++) {
			long count = countArrangements(arrangements[i], counts[i], 0);
			totalArrangements += count;
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
