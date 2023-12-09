package dev.flowty.aoc.y15;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dev.flowty.aoc.Input;

class Day02Test {

	private static final String[] DATA = Input.linesFrom( "y15", "input_02.txt" );

	@Test
	void part1Test() {
		assertEquals( 58, Day02.part1( "2x3x4" ) );
		assertEquals( 43, Day02.part1( "1x1x10" ) );
	}

	@Test
	void part1() {
		assertEquals( 1586300, Day02.part1( DATA ) );
	}

	@Test
	void part2Test() {
		assertEquals( 34, Day02.part2( "2x3x4" ) );
		assertEquals( 14, Day02.part2( "1x1x10" ) );
	}

	@Test
	void part2() {
		assertEquals( 3737498, Day02.part2( DATA ) );
	}
}
