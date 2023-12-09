package dev.flowty.aoc.y23;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dev.flowty.aoc.Input;
import dev.flowty.aoc.y23.Day04;

class Day04Test {

	private static final String[] TEST_DATA = Input.linesOf( ""
			+ "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53\n"
			+ "Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19\n"
			+ "Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1\n"
			+ "Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83\n"
			+ "Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36\n"
			+ "Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11" );

	private static final String[] DATA = Input.linesFrom( "y23", "input_04.txt" );

	@Test
	void part1Test() {
		assertEquals( 13, Day04.part1( TEST_DATA ) );
	}

	@Test
	void part1() {
		assertEquals( 21105, Day04.part1( DATA ) );
	}

	@Test
	void part2Test() {
		assertEquals( 30, Day04.part2( TEST_DATA ) );
	}

	@Test
	void part2() {
		assertEquals( 5329815, Day04.part2( DATA ) );
	}
}
