package com.noahgeren.adventofcode.problems.xxiii;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.noahgeren.adventofcode.Day;
import com.noahgeren.adventofcode.data.DataLoader;

public class Day10 extends Day {

	char[][] map;

	char[] CONNECTS_LEFT = { '-', 'J', '7', 'S' };
	char[] CONNECTS_RIGHT = { '-', 'L', 'F', 'S' };
	char[] CONNECTS_TOP = { '|', 'L', 'J', 'S' };
	char[] CONNECTS_BOTTOM = { '|', '7', 'F', 'S' };

	LinkedHashSet<Integer> loop = new LinkedHashSet<>();
	HashMap<Integer, Boolean> reachOutsideMap = new HashMap<>();

	@Override
	public void loadResources() throws Exception {
		List<String> lines = DataLoader.readLines("day10.txt");
		map = new char[lines.size()][];
		for (int i = 0; i < lines.size(); i++) {
			map[i] = lines.get(i).toCharArray();
		}
	}

	@Override
	public String solve(boolean firstPart) throws Exception {
		if (!firstPart) {
			return String.valueOf(solveSecondPart());
		}
		int row = 0, col = 0;
		outer: for (row = 0; row < map.length; row++) {
			for (col = 0; col < map[row].length; col++) {
				if (map[row][col] == 'S') {
					break outer;
				}
			}
		}
		return String.valueOf(loopLength(row, col, 0, loop) / 2);
	}

	private Long loopLength(int row, int col, long length, LinkedHashSet<Integer> seen) {
		char c = map[row][col];
		if (seen.contains(hash(map, row, col))) {
			if (c == 'S' && length > 2) {
				return length;
			}
			return null;
		}
		seen.add(hash(map, row, col));
		for (int i = -1; i <= 1; i += 2) {
			Long newLength;
			if (row + i >= 0 && row + i < map.length
					&& piecesConnect(c, map[row + i][col], i == -1, i == 1, false, false)) {
				newLength = loopLength(row + i, col, length + 1, seen);
				if (newLength != null) {
					return newLength;
				}
			}
			if (col + i >= 0 && col + i < map[row].length
					&& piecesConnect(c, map[row][col + i], false, false, i == -1, i == 1)) {
				newLength = loopLength(row, col + i, length + 1, seen);
				if (newLength != null) {
					return newLength;
				}
			}
		}
		seen.remove(hash(map, row, col));
		return null;
	}

	private long solveSecondPart() {
		char[][] newMap = new char[map.length * 2][];
		for (int i = 0; i < map.length; i++) {
			newMap[i * 2] = new char[map[i].length * 2];
			for (int j = 0; j < map[i].length; j++) {
				newMap[i * 2][j * 2] = map[i][j];
				newMap[i * 2][j * 2 + 1] = '.';
			}
			char[] newLine = new char[map[i].length * 2];
			Arrays.fill(newLine, '.');
			newMap[i * 2 + 1] = newLine;
		}
		long contains = 0;
		for (int row = 0; row < newMap.length; row += 2) {
			for (int col = 0; col < newMap[row].length; col += 2) {
				if (loop.contains(hash(map, row / 2, col / 2))) {
					continue;
				}
				Set<Integer> path = new HashSet<>();
				boolean canReachOutside = canReachOutside(newMap, row, col, path);
				if (!canReachOutside) {
					contains++;
				}
				for (Integer node : path) {
					reachOutsideMap.put(node, canReachOutside);
				}
			}
		}
		return contains;
	}

	private boolean canReachOutside(char[][] newMap, int row, int col, Set<Integer> seen) {
		Boolean previousOutside = reachOutsideMap.get(hash(newMap, row, col));
		if (previousOutside != null) {
			return previousOutside;
		}
		if (row % 2 == 0 && col % 2 == 0 && loop.contains(hash(map, row / 2, col / 2))) {
			return false;
		}
		if (row == 0 || row == newMap.length - 1 || col == 0 || col == newMap[row].length - 1) {
			return true;
		}
		if (seen.contains(hash(newMap, row, col))) {
			return false;
		}
		seen.add(hash(newMap, row, col));
		for (int i = -1; i <= 1; i += 2) {
			// Vertical
			if (col % 2 == 0 || row % 2 == 0 || !piecesConnect(row + i, col - 1, row + i, col + 1, true, false)) {
				boolean outside = canReachOutside(newMap, row + i, col, seen);
				if (outside) {
					return true;
				}
			}
			// Horizontal
			if (col % 2 == 0 || row % 2 == 0 || !piecesConnect(row - 1, col + i, row + 1, col + i, false, true)) {
				boolean outside = canReachOutside(newMap, row, col + i, seen);
				if (outside) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean piecesConnect(int rowA, int colA, int rowB, int colB, boolean vertical, boolean horizontal) {
		if (rowA % 2 != 0 || colA % 2 != 0 || rowB % 2 != 0 || colB % 2 != 0) {
			return false;
		}
		if(!loop.contains(hash(map, rowA / 2, colA / 2)) || !loop.contains(hash(map, rowB / 2, colB / 2))) {
			return false;
		}
		return piecesConnect(map[rowA / 2][colA / 2], map[rowB / 2][colB / 2], false, horizontal, false, vertical);
	}

	private boolean piecesConnect(char a, char b, boolean top, boolean bottom, boolean left, boolean right) {
		boolean answer = true;
		if (top) {
			if (!contains(CONNECTS_TOP, a) || !contains(CONNECTS_BOTTOM, b)) {
				answer = false;
			}
		} else if (bottom) {
			if (!contains(CONNECTS_BOTTOM, a) || !contains(CONNECTS_TOP, b)) {
				answer = false;
			}
		} else if (left) {
			if (!contains(CONNECTS_LEFT, a) || !contains(CONNECTS_RIGHT, b)) {
				answer = false;
			}
		} else if (right) {
			if (!contains(CONNECTS_RIGHT, a) || !contains(CONNECTS_LEFT, b)) {
				answer = false;
			}
		}
		return answer;
	}

	private boolean contains(char[] arr, char c) {
		for (char a : arr) {
			if (c == a)
				return true;
		}
		return false;
	}

	private int hash(char[][] map, int row, int col) {
		return row * map[row].length + col;
	}

}
