package dev.flowty.aoc.y23;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dev.flowty.aoc.Input;

class Day18Test {

	private static final String[] TEST_DATA = Input.linesOf( ""
			+ "R 6 (#70c710)\n"
			+ "D 5 (#0dc571)\n"
			+ "L 2 (#5713f0)\n"
			+ "D 2 (#d2c081)\n"
			+ "R 2 (#59c680)\n"
			+ "D 2 (#411b91)\n"
			+ "L 5 (#8ceee2)\n"
			+ "U 2 (#caa173)\n"
			+ "L 1 (#1b58a2)\n"
			+ "U 2 (#caa171)\n"
			+ "R 2 (#7807d2)\n"
			+ "U 3 (#a77fa3)\n"
			+ "L 2 (#015232)\n"
			+ "U 2 (#7a21e3)" );

	private static final String[] DATA = Input.linesFrom( "y23", "input_18.txt" );

	@Test
	void part1Test() {
		assertEquals( 62, Day18.part1( TEST_DATA ) );
	}

	@Test
	void part1() {
		assertEquals( 47527, Day18.part1( DATA ) );
	}

	@Test
	void part2Test() {
		assertEquals( 952408144115L, Day18.part2( TEST_DATA ) );
	}

	@Test
	void part2() {
		assertEquals( 52240187443190L, Day18.part2( DATA ) );
	}

}
