package com.noahgeren.adventofcode.days;

public abstract class Day {
	
	public abstract void loadResources() throws Exception;
		
	public abstract String solve(boolean firstPart) throws Exception;
	
	public void reset() throws Exception {
		// Do nothing by default
	}
	
}
