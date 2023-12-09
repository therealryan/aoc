package dev.flowty.aoc.y15;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dev.flowty.aoc.Input;

class Day03Test {

	private static final String[] DATA = Input.linesFrom( "y15", "input_03.txt" );

	@Test
	void part1Test() {
		assertEquals( 2, Day03.part1( ">" ) );
		assertEquals( 4, Day03.part1( "^>v<" ) );
		assertEquals( 2, Day03.part1( "^v^v^v^v^v" ) );
	}

	@Test
	void part1() {
		assertEquals( 2081, Day03.part1( DATA[0] ) );
	}

	@Test
	void part2Test() {
		assertEquals( 3, Day03.part2( "^v" ) );
		assertEquals( 3, Day03.part2( "^>v<" ) );
		assertEquals( 11, Day03.part2( "^v^v^v^v^v" ) );
	}

	@Test
	void part2() {
		assertEquals( 2341, Day03.part2( DATA[0] ) );
	}
}
