package dev.flowty.aoc.y23;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Day05Test {

	private static final String[] TEST_DATA = Input.linesOf( ""
			+ "seeds: 79 14 55 13\n"
			+ "\n"
			+ "seed-to-soil map:\n"
			+ "50 98 2\n"
			+ "52 50 48\n"
			+ "\n"
			+ "soil-to-fertilizer map:\n"
			+ "0 15 37\n"
			+ "37 52 2\n"
			+ "39 0 15\n"
			+ "\n"
			+ "fertilizer-to-water map:\n"
			+ "49 53 8\n"
			+ "0 11 42\n"
			+ "42 0 7\n"
			+ "57 7 4\n"
			+ "\n"
			+ "water-to-light map:\n"
			+ "88 18 7\n"
			+ "18 25 70\n"
			+ "\n"
			+ "light-to-temperature map:\n"
			+ "45 77 23\n"
			+ "81 45 19\n"
			+ "68 64 13\n"
			+ "\n"
			+ "temperature-to-humidity map:\n"
			+ "0 69 1\n"
			+ "1 0 69\n"
			+ "\n"
			+ "humidity-to-location map:\n"
			+ "60 56 37\n"
			+ "56 93 4" );

	private static final String[] DATA = Input.linesFrom( "input_05.txt" );

	@Test
	void part1Test() {
		assertEquals( 35, Day05.part1( TEST_DATA ) );
	}

	@Test
	void part1() {
		assertEquals( 107430936, Day05.part1( DATA ) );
	}

}
