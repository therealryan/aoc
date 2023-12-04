package dev.flowty.aoc.y23;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dev.flowty.aoc.y23.Day03;

class Day03Test {

	private static final String[] TEST_DATA = Input.linesOf( ""
			+ "467..114..\n"
			+ "...*......\n"
			+ "..35..633.\n"
			+ "......#...\n"
			+ "617*......\n"
			+ ".....+.58.\n"
			+ "..592.....\n"
			+ "......755.\n"
			+ "...$.*....\n"
			+ ".664.598.." );

	private static final String[] DATA = Input.linesFrom( "input_03.txt" );

	@Test
	void part1Test() {
		assertEquals( 4361, Day03.part1( TEST_DATA ) );
	}

	@Test
	void part1() {
		assertEquals( 553079, Day03.part1( DATA ) );
	}

	@Test
	void part2Test() {
		assertEquals( 467835, Day03.part2( TEST_DATA ) );
	}

	@Test
	void part2() {
		assertEquals( 84363105, Day03.part2( DATA ) );
	}
}
