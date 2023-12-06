package dev.flowty.aoc.y23;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Day06Test {

	private static final String[] TEST_DATA = Input.linesOf( ""
			+ "Time:      7  15   30\n"
			+ "Distance:  9  40  200" );

	private static final String[] DATA = Input.linesFrom( "input_06.txt" );

	@Test
	void part1Test() {
		assertEquals( 288, Day06.part1( TEST_DATA ) );
	}

	@Test
	void part1() {
		assertEquals( 227850, Day06.part1( DATA ) );
	}

	@Test
	void part2Test() {
		assertEquals( 71503, Day06.part2( TEST_DATA ) );
	}

	@Test
	void part2() {
		assertEquals( 42948149, Day06.part2( DATA ) );
	}
}
