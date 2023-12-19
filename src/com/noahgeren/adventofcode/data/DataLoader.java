package com.noahgeren.adventofcode.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.noahgeren.adventofcode.Launcher;

public class DataLoader {
	
	public static BufferedReader getReader(String name) {
		return new BufferedReader(new InputStreamReader(DataLoader.class.getResourceAsStream(Launcher.PACKAGE + "/" + name)));
	}
	
	public static List<String> readLines(String name) throws IOException {
		try (BufferedReader reader = getReader(name)){
			return reader.lines().collect(Collectors.toCollection(ArrayList<String>::new));
		}
	}

}
