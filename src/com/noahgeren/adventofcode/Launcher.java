package com.noahgeren.adventofcode;

public class Launcher {

	private static final String PACKAGE = "xxiii";
	private static final int YEAR = 2023;
	private static final int DAY = 8;
	
	public static void main(String[] args) {
		try {
			final Day day = (Day) Class.forName(
					String.format("com.noahgeren.adventofcode.problems.%s.Day%02d", PACKAGE, DAY)).newInstance();
			loadResources(day);
			runPartOfDay(day, true);
			try {
				day.reset();
			} catch (Exception e) {
				System.out.printf("Error resetting class for day %d of %d\n", DAY, YEAR);
				e.printStackTrace();
			}
			runPartOfDay(day, false);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			System.out.printf("Error loading class for day %d of %d\n", DAY, YEAR);
			e.printStackTrace();
		}
	}
	
	private static void loadResources(final Day day) {
		try {
			System.out.printf("Loading resources for day %d of %d\n", DAY, YEAR);
			day.loadResources();
		} catch(Exception e) {
			System.out.printf("Error loading resources for day %d of %d\n", DAY, YEAR);
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	private static void runPartOfDay(final Day day, final boolean firstPart) {
		System.out.printf("Running the %s part of day %d of %d\n", firstPart ? "first" : "last", DAY, YEAR);
		try {
			final long startTime = System.currentTimeMillis();
			final String answer = day.solve(firstPart);
			final long duration = System.currentTimeMillis() - startTime;
			System.out.printf("Answer: %s\n", answer);
			System.out.printf("Took %d ms\n", duration);
		} catch (Exception e) {
			System.out.printf("Error running the %s part of day %d of %d\n", firstPart ? "first" : "last", DAY, YEAR);
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
