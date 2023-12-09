package dev.flowty.aoc.y15;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dev.flowty.aoc.Input;

class Day06Test {

	private static final String[] DATA = Input.linesFrom( "y15", "input_06.txt" );

	@Test
	void part1Test() {
		assertEquals( 1, Day06.part1( Input.linesOf( ""
				+ "turn on 0,0 through 0,0" ) ) );
		assertEquals( 4, Day06.part1( Input.linesOf( ""
				+ "turn on 0,0 through 1,1" ) ) );
		assertEquals( 1000000, Day06.part1( Input.linesOf( ""
				+ "turn on 0,0 through 999,999" ) ) );
		assertEquals( 999000, Day06.part1( Input.linesOf( ""
				+ "turn on 0,0 through 999,999\n"
				+ "toggle 0,0 through 999,0" ) ) );
		assertEquals( 998996, Day06.part1( Input.linesOf( ""
				+ "turn on 0,0 through 999,999\n"
				+ "toggle 0,0 through 999,0\n"
				+ "turn off 499,499 through 500,500" ) ) );
	}

	@Test
	void part1() {
		assertEquals( 543903, Day06.part1( DATA ) );
	}

	@Test
	void part2Test() {
		assertEquals( 1, Day06.part2( Input.linesOf( ""
				+ "turn on 0,0 through 0,0" ) ) );
		assertEquals( 8, Day06.part2( Input.linesOf( ""
				+ "toggle 0,0 through 1,1" ) ) );
		assertEquals( 1000000, Day06.part2( Input.linesOf( ""
				+ "turn on 0,0 through 999,999" ) ) );
		assertEquals( 1002000, Day06.part2( Input.linesOf( ""
				+ "turn on 0,0 through 999,999\n"
				+ "toggle 0,0 through 999,0" ) ) );
		assertEquals( 1001996, Day06.part2( Input.linesOf( ""
				+ "turn on 0,0 through 999,999\n"
				+ "toggle 0,0 through 999,0\n"
				+ "turn off 499,499 through 500,500" ) ) );
	}

	@Test
	void part2() {
		assertEquals( 14687245, Day06.part2( DATA ) );
	}
}
