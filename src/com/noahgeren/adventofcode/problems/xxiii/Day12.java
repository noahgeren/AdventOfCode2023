package com.noahgeren.adventofcode.problems.xxiii;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.noahgeren.adventofcode.Day;
import com.noahgeren.adventofcode.data.DataLoader;

public class Day12 extends Day {

	private final int DUPLICATE = 5;
	private final int MAX = 15;

	char[][] arrangements;
	int[][] counts;
	BigInteger[][] arrangementCounts;

	@Override
	public void loadResources() throws Exception {
		List<String> linesTemp = DataLoader.readLines("day12.txt");
		arrangements = new char[linesTemp.size()][];
		counts = new int[linesTemp.size()][];
		if(arrangementCounts == null) {
			arrangementCounts = new BigInteger[arrangements.length][];
		}
		for (int i = 0; i < linesTemp.size(); i++) {
			String[] line = linesTemp.get(i).split(" ");
			arrangements[i] = line[0].toCharArray();
			String[] strCounts = line[1].split(",");
			counts[i] = new int[strCounts.length];
			for (int j = 0; j < strCounts.length; j++) {
				counts[i][j] = Integer.valueOf(strCounts[j]);
			}
			if(arrangementCounts[i] == null) {
				arrangementCounts[i] = new BigInteger[DUPLICATE];
			}
		}
	}

	@Override
	public String solve(boolean firstPart) throws Exception {
		for(int i = 1; i <= DUPLICATE; i++) {
			loadResources();
			findForDuplicate(i);
		}
		for(int i = 0; i < arrangementCounts.length && i < MAX; i++) {
			for(int j = 0; j < arrangementCounts[i].length; j++) {
				System.out.print(arrangementCounts[i][j] + " ");
			}
			System.out.println();
		}
		return null;
	}
	
	private void findForDuplicate(int duplicate) {
		char[][] newArrangements = new char[arrangements.length][];
		for (int i = 0; i < arrangements.length; i++) {
			List<Character> line = new ArrayList<>();
			for (int k = 0; k < duplicate; k++) {
				for (int j = 0; j < arrangements[i].length; j++) {
					line.add(arrangements[i][j]);
				}
				if (k != duplicate - 1) {
					line.add('?');
				}
			}
			newArrangements[i] = new char[line.size()];
			for (int j = 0; j < line.size(); j++) {
				newArrangements[i][j] = line.get(j);
			}
		}
		arrangements = newArrangements;

		int[][] newCounts = new int[counts.length][];
		for (int i = 0; i < counts.length; i++) {
			List<Integer> line = new ArrayList<>();
			for (int k = 0; k < duplicate; k++) {
				for (int j = 0; j < counts[i].length; j++) {
					line.add(counts[i][j]);
				}
			}
			newCounts[i] = new int[line.size()];
			for (int j = 0; j < line.size(); j++) {
				newCounts[i][j] = line.get(j);
			}
		}
		counts = newCounts;
		
		BigInteger totalArrangements = BigInteger.ZERO;
		for (int i = 0; i < arrangements.length && i < MAX; i++) {
			System.out.println(duplicate + " " + (i + 1) + "/" + arrangements.length);
			BigInteger count = countArrangements(arrangements[i], counts[i], 0);
			arrangementCounts[i][duplicate - 1] = count;
			totalArrangements = totalArrangements.add(count);
		}
	}

	private BigInteger countArrangements(char[] arr, int[] counts, int index) {
		if (index >= arr.length) {
			return checkValidUpTo(arr, counts, index) ? BigInteger.ONE : BigInteger.ZERO;
		}
		if (arr[index] == '?') {
			arr[index] = '#';
			BigInteger filled = countArrangements(arr, counts, index + 1);
			arr[index] = '.';
			BigInteger notFilled = BigInteger.ZERO;
			if (checkValidUpTo(arr, counts, index)) {
				notFilled = countArrangements(arr, counts, index + 1);
			}
			arr[index] = '?';
			return filled.add(notFilled);
		} else if (arr[index] == '.' && !checkValidUpTo(arr, counts, index)) {
			return BigInteger.ZERO;
		}
		return countArrangements(arr, counts, index + 1);
	}

	private boolean checkValidUpTo(char[] arr, int[] counts, int bound) {
		List<Integer> countCheck = new ArrayList<>();
		countCheck.add(0);
		for (int i = 0; i < arr.length && i < bound; i++) {
			char c = arr[i];
			if (c == '#') {
				countCheck.set(countCheck.size() - 1, countCheck.get(countCheck.size() - 1) + 1);
			} else if (countCheck.get(countCheck.size() - 1) != 0) {
				countCheck.add(0);
			}
		}
		if (countCheck.get(countCheck.size() - 1) == 0) {
			countCheck.remove(countCheck.size() - 1);
		}
		if (countCheck.size() > counts.length || (bound >= arr.length - 1 && countCheck.size() != counts.length)) {
//			printData(arr, countCheck);
			return false;
		}
		for (int i = 0; i < countCheck.size(); i++) {
			if (counts[i] != countCheck.get(i)) {
//				System.out.println("bound " + bound);
//				printData(arr, countCheck);
				return false;
			}
		}
		return true;
	}

	private void printData(char[] arr, List<Integer> countCheck) {
		for (char c : arr) {
			System.out.print(c);
		}
		System.out.println();
		System.out.println(countCheck.stream().map(String::valueOf).collect(Collectors.joining(", ")));
	}

}