package dev.flowty.aoc.y23;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dev.flowty.aoc.Input;

class Day21Test {

	private static final String[] TEST_DATA = Input.linesOf( ""
			+ "...........\n"
			+ ".....###.#.\n"
			+ ".###.##..#.\n"
			+ "..#.#...#..\n"
			+ "....#.#....\n"
			+ ".##..S####.\n"
			+ ".##..#...#.\n"
			+ ".......##..\n"
			+ ".##.#.####.\n"
			+ ".##..##.##.\n"
			+ "..........." );

	private static final String[] DATA = Input.linesFrom( "y23", "input_21.txt" );

	@Test
	void part1Test() {
		assertEquals( 1, Day21.part1( 0, TEST_DATA ) );
		assertEquals( 2, Day21.part1( 1, TEST_DATA ) );
		assertEquals( 4, Day21.part1( 2, TEST_DATA ) );
		assertEquals( 6, Day21.part1( 3, TEST_DATA ) );
		assertEquals( 16, Day21.part1( 6, TEST_DATA ) );
	}

	@Test
	void part1() {
		assertEquals( 3841, Day21.part1( 64, DATA ) );
	}

	@Test
	void part2() {
		assertEquals( 0, Day21.part2( DATA ) );
	}

}
