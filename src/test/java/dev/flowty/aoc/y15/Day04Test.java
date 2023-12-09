package dev.flowty.aoc.y15;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dev.flowty.aoc.Input;

class Day04Test {

	private static final String[] DATA = Input.linesFrom( "y15", "input_04.txt" );

	@Test
	void part1Test() {
		assertEquals( 609043, Day04.part1( "abcdef" ) );
		assertEquals( 1048970, Day04.part1( "pqrstuv" ) );
	}

	@Test
	void part1() {
		assertEquals( 282749, Day04.part1( DATA[0] ) );
	}

	@Test
	void part2() {
		assertEquals( 9962624, Day04.part2( DATA[0] ) );
	}
}
