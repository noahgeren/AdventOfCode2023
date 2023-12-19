package com.noahgeren.adventofcode.problems.xxiii;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import com.noahgeren.adventofcode.Day;
import com.noahgeren.adventofcode.data.DataLoader;
import com.noahgeren.adventofcode.util.Direction;

public class Day18 extends Day {

	List<String> lines;
	List<Step> steps;

	@Override
	public void loadResources() throws Exception {
		lines = DataLoader.readLines("day18.txt");
	}

	@Override
	public String solve(boolean firstPart) throws Exception {
		steps = lines.stream().map(line -> new Step(line, firstPart)).collect(Collectors.toList());
		BigInteger volume = BigInteger.ONE;
		BigInteger column = findStartingColumn().add(BigInteger.ONE);
		for (Step step : steps) {
			BigInteger value;
			switch (step.direction) {
			case LEFT:
				column = column.subtract(step.amount);
				break;
			case RIGHT:
				value = step.amount;
//				System.out.println("Adding " + value);
				volume = volume.add(value);
				column = column.add(step.amount);
				break;
			case DOWN:
				value = column.multiply(step.amount);
//				System.out.println("Adding " + value);
				volume = volume.add(value);
				break;
			case UP:
				value = column.subtract(BigInteger.ONE).multiply(step.amount);
//				System.out.println("Subtracting " + value);
				volume = volume.subtract(value);
				break;
			}
		}
		return volume.toString();
	}

	public BigInteger findStartingColumn() {
		BigInteger column = BigInteger.ZERO;
		BigInteger min = BigInteger.valueOf(Long.MAX_VALUE);
		for (Step step : steps) {
			if (step.direction == Direction.RIGHT) {
				column = column.add(step.amount);
			} else if (step.direction == Direction.LEFT) {
				column = column.subtract(step.amount);
			}
			if (column.compareTo(min) < 0) {
				min = column;
			}
		}
		return min.abs();
	}

	private static class Step {
		Direction direction;
		BigInteger amount;

		public Step(String line, boolean firstPart) {
			String[] tokens = line.split(" ");
			if (firstPart) {
				direction = getDirection(tokens[0]);
				amount = new BigInteger(tokens[1]);
			} else {
				direction = getDirection(tokens[2].charAt(tokens[2].length() - 2));
				amount = BigInteger.valueOf(Long.parseLong(tokens[2].substring(2, tokens[2].length() - 2), 16));
			}
		}

		private static Direction getDirection(String firstChar) {
			for (Direction d : Direction.values()) {
				if (d.name().startsWith(firstChar)) {
					return d;
				}
			}
			return null;
		}

		private static Direction getDirection(char lastChar) {
			switch (lastChar) {
			case '0':
				return Direction.RIGHT;
			case '1':
				return Direction.DOWN;
			case '2':
				return Direction.LEFT;
			case '3':
				return Direction.UP;
			}
			return null;
		}

	}

}
