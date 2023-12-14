package dev.flowty.aoc.y23;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dev.flowty.aoc.Input;

class Day14Test {

	private static final String[] TEST_DATA = Input.linesOf( ""
			+ "O....#....\n"
			+ "O.OO#....#\n"
			+ ".....##...\n"
			+ "OO.#O....O\n"
			+ ".O.....O#.\n"
			+ "O.#..O.#.#\n"
			+ "..O..#O..O\n"
			+ ".......O..\n"
			+ "#....###..\n"
			+ "#OO..#...." );

	private static final String[] DATA = Input.linesFrom( "y23", "input_14.txt" );

	@Test
	void part1Test() {
		assertEquals( 136, Day14.part1( TEST_DATA ) );
	}

	@Test
	void part1() {
		assertEquals( 109638, Day14.part1( DATA ) );
	}

	@Test
	void part2Test() {
		assertEquals( 64, Day14.part2( TEST_DATA ) );
	}

	@Test
	void part2() {
		assertEquals( 102657, Day14.part2( DATA ) );
	}
}
