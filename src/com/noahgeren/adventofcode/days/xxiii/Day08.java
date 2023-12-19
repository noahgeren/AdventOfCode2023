package com.noahgeren.adventofcode.days.xxiii;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.noahgeren.adventofcode.data.DataLoader;
import com.noahgeren.adventofcode.days.Day;

public class Day08 extends Day {

	private char[] instructions;
	private HashMap<String, Node> nodeMap = new HashMap<>();

	@Override
	public void loadResources() throws Exception {
		List<String> lines = DataLoader.readLines("day08.txt");
		instructions = lines.get(0).toCharArray();
		for (int i = 2; i < lines.size(); i++) {
			String[] line = lines.get(i).split(" = ");
			String value = line[0];
			String[] children = line[1].substring(1, line[1].length() - 1).split(", ");
			Node node = nodeMap.computeIfAbsent(value, Node::new);
			Node left = nodeMap.computeIfAbsent(children[0], Node::new);
			Node right = nodeMap.computeIfAbsent(children[1], Node::new);
			node.left = left;
			node.right = right;
		}
	}

	@Override
	public String solve(boolean firstPart) throws Exception {
		if (!firstPart) {
			return solveSecondPart();
		}
		Node current = nodeMap.get("AAA");
		long steps = 0;
		for (;; steps++) {
			if (current.value.trim().equals("ZZZ")) {
				return String.valueOf(steps);
			}
			if (instructions[(int) (steps % instructions.length)] == 'L') {
				current = current.left;
			} else {
				current = current.right;
			}
		}
	}

	static long steps = 0;
	static long start = System.currentTimeMillis();

	private String solveSecondPart() {
		final Node[] current = nodeMap.values().stream().filter(node -> node.endsA).collect(Collectors.toList())
				.toArray(new Node[0]);
		final Thread timer = new Thread(new Runnable() {
			long currentBillions = 0;
			@Override
			public void run() {
				try {
					while (true) {
						if (steps > currentBillions * 1_000_000_000L) {
							System.out.println(currentBillions + " billion");
							currentBillions++;
							System.out.println((System.currentTimeMillis() - start) / 1000 + " seconds");
						}
						Thread.sleep(5000L);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		});
		timer.start();
		boolean allZ = true;
		boolean left = true;
		int i = 0, j = 0;
		while (true) {
			for (i = 0; i < instructions.length; i++, steps++) {
				allZ = true;
				left = instructions[i] == 'L';
				for (j = 0; j < current.length; j++) {
					if (!current[j].endsZ) {
						allZ = false;
					}
					if (left) {
						current[j] = current[j].left;
					} else {
						current[j] = current[j].right;
					}
				}
				if (allZ) {
					timer.stop();
					return String.valueOf(steps);
				}
			}
		}

	}

	private class Node {
		private String value;
		private Node left;
		private Node right;

		private boolean endsA;
		private boolean endsZ;

		public Node(String value) {
			this.value = value;
			endsA = value.endsWith("A");
			endsZ = value.endsWith("Z");
		}
	}

}
