package dev.flowty.aoc.y23;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dev.flowty.aoc.Input;

class Day09Test {

	private static final String[] TEST_DATA = Input.linesOf( ""
			+ "0 3 6 9 12 15\n"
			+ "1 3 6 10 15 21\n"
			+ "10 13 16 21 30 45" );

	private static final String[] DATA = Input.linesFrom( "y23", "input_09.txt" );

	@Test
	void part1Test() {
		assertEquals( 114, Day09.part1( TEST_DATA ) );
	}

	@Test
	void part1() {
		assertEquals( 1939607039, Day09.part1( DATA ) );
	}

	@Test
	void part2Test() {
		assertEquals( 2, Day09.part2( TEST_DATA ) );
	}

	@Test
	void part2() {
		assertEquals( 1041, Day09.part2( DATA ) );
	}

}
