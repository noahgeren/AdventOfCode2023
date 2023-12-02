package com.noahgeren.adventofcode.problems.xxiii;

import java.util.List;

import com.noahgeren.adventofcode.Day;
import com.noahgeren.adventofcode.data.DataLoader;

public class Day02 extends Day {

	private static final int MAX_RED = 12, MAX_GREEN = 13, MAX_BLUE = 14;

	private List<String> games;

	@Override
	public void loadResources() throws Exception {
		games = DataLoader.readLines("day02.txt");
	}

	@Override
	public String solve(boolean firstPart) throws Exception {
		long sum = 0;
		for (String game : games) {
			int red = 0, green = 0, blue = 0;
			String[] data = game.split(": ");
			int id = Integer.valueOf(data[0].substring(5));
			String[] pulls = data[1].trim().split(";");
			for (String pull : pulls) {
				String[] cubes = pull.trim().split(", ");
				for (String cube : cubes) {
					if (cube.endsWith("red")) {
						int number = Integer.valueOf(cube.substring(0, cube.length() - 4));
						if (number > red) {
							red = number;
						}
					} else if (cube.endsWith("green")) {
						int number = Integer.valueOf(cube.substring(0, cube.length() - 6));
						if (number > green) {
							green = number;
						}
					} else if (cube.endsWith("blue")) {
						int number = Integer.valueOf(cube.substring(0, cube.length() - 5));
						if (number > blue) {
							blue = number;
						}
					}
				}
			}
			if (firstPart && red <= MAX_RED && green <= MAX_GREEN && blue <= MAX_BLUE) {
				sum += id;
			} else if (!firstPart) {
				sum += red * green * blue;
			}
		}
		return String.valueOf(sum);
	}

}
