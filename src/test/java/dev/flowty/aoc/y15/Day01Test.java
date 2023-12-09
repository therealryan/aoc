package dev.flowty.aoc.y15;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dev.flowty.aoc.Input;

class Day01Test {

	private static final String[] DATA = Input.linesFrom( "y15", "input_01.txt" );

	@Test
	void part1Test() {
		assertEquals( 0, Day01.part1( "(())" ) );
		assertEquals( 0, Day01.part1( "()()" ) );

		assertEquals( 3, Day01.part1( "(((" ) );
		assertEquals( 3, Day01.part1( "(()(()(" ) );

		assertEquals( 3, Day01.part1( "))(((((" ) );

		assertEquals( -1, Day01.part1( "())" ) );
		assertEquals( -1, Day01.part1( "))(" ) );

		assertEquals( -3, Day01.part1( ")))" ) );
		assertEquals( -3, Day01.part1( ")())())" ) );
	}

	@Test
	void part1() {
		assertEquals( 74, Day01.part1( DATA[0] ) );
	}

	@Test
	void part2Test() {
		assertEquals( 1, Day01.part2( ")" ) );
		assertEquals( 5, Day01.part2( "()())" ) );
	}

	@Test
	void part2() {
		assertEquals( 1795, Day01.part2( DATA[0] ) );
	}
}
