package dev.flowty.aoc.y23;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dev.flowty.aoc.y23.Day02;

class Day02Test {

	private static final String[] TEST_DATA = Input.linesOf( ""
			+ "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green\n"
			+ "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue\n"
			+ "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red\n"
			+ "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red\n"
			+ "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green" );

	private static final String[] DATA = Input.linesFrom( "input_02.txt" );

	@Test
	void part1Test() {
		assertEquals( 8, Day02.part1( TEST_DATA ) );
	}

	@Test
	void part1() {
		assertEquals( 2162, Day02.part1( DATA ) );
	}

	@Test
	void part2Test() {
		assertEquals( 2286, Day02.part2( TEST_DATA ) );
	}

	@Test
	void part2() {
		assertEquals( 72513, Day02.part2( DATA ) );
	}
}
