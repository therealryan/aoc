package dev.flowty.aoc.y23;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dev.flowty.aoc.Input;

class Day16Test {

	private static final String[] TEST_DATA = Input.linesOf( ""
			+ ".|...\\....\n"
			+ "|.-.\\.....\n"
			+ ".....|-...\n"
			+ "........|.\n"
			+ "..........\n"
			+ ".........\\\n"
			+ "..../.\\\\..\n"
			+ ".-.-/..|..\n"
			+ ".|....-|.\\\n"
			+ "..//.|...." );

	private static final String[] DATA = Input.linesFrom( "y23", "input_16.txt" );

	@Test
	void dirs() {
		assertEquals( 4, Day16.part1( Input.linesOf( ""
				+ "..\\.\n"
				+ "....\n" ) ) );

		assertEquals( 4, Day16.part1( Input.linesOf( ""
				+ "..|.\n"
				+ "....\n" ) ) );

		assertEquals( 7, Day16.part1( Input.linesOf( ""
				+ "..|.\n"
				+ "..-.\n" ) ) );

		assertEquals( 6, Day16.part1( Input.linesOf( ""
				+ "..\\.\n"
				+ "../.\n" ) ) );

		assertEquals( 5, Day16.part1( Input.linesOf( ""
				+ "..\\.\n"
				+ "..\\.\n" ) ) );

		assertEquals( 6, Day16.part1( Input.linesOf( ""
				+ "..\\.\n"
				+ "..\\/\n" ) ) );

		assertEquals( 6, Day16.part1( Input.linesOf( ""
				+ "..\\.\n"
				+ "\\./.\n" ) ) );
	}

	@Test
	void part1Test() {
		assertEquals( 46, Day16.part1( TEST_DATA ) );
	}

	@Test
	void part1() {
		assertEquals( 8551, Day16.part1( DATA ) );
	}

	@Test
	void part2Test() {
		assertEquals( 51, Day16.part2( TEST_DATA ) );
	}

	@Test
	void part2() {
		assertEquals( 8754, Day16.part2( DATA ) );
	}
}
