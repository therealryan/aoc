package dev.flowty.aoc.y23;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dev.flowty.aoc.y23.Day01;

class Day01Test {

	private static final String[] DATA = Input.linesFrom( "input_01.txt" );

	@Test
	void part1Test() {
		assertEquals( 142, Day01.part1( Input.linesOf( ""
				+ "1abc2\n"
				+ "pqr3stu8vwx\n"
				+ "a1b2c3d4e5f\n"
				+ "treb7uchet" ) ) );
	}

	@Test
	void part1() {
		assertEquals( 54573, Day01.part1( DATA ) );
	}

	@Test
	void part2Test() {
		assertEquals( 281, Day01.part2( Input.linesOf( ""
				+ "two1nine\n"
				+ "eightwothree\n"
				+ "abcone2threexyz\n"
				+ "xtwone3four\n"
				+ "4nineeightseven2\n"
				+ "zoneight234\n"
				+ "7pqrstsixteen" ) ) );
	}

	@Test
	void part2() {
		assertEquals( 54591, Day01.part2( DATA ) );
	}
}
