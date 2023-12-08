package dev.flowty.aoc.y23;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Day08Test {

	private static final String[] TEST_DATA_1 = Input.linesOf( ""
			+ "RL\n"
			+ "\n"
			+ "AAA = (BBB, CCC)\n"
			+ "BBB = (DDD, EEE)\n"
			+ "CCC = (ZZZ, GGG)\n"
			+ "DDD = (DDD, DDD)\n"
			+ "EEE = (EEE, EEE)\n"
			+ "GGG = (GGG, GGG)\n"
			+ "ZZZ = (ZZZ, ZZZ)" );

	private static final String[] TEST_DATA_2 = Input.linesOf( ""
			+ "LLR\n"
			+ "\n"
			+ "AAA = (BBB, BBB)\n"
			+ "BBB = (AAA, ZZZ)\n"
			+ "ZZZ = (ZZZ, ZZZ)" );

	private static final String[] DATA = Input.linesFrom( "input_08.txt" );

	@Test
	void part1Test() {
		assertEquals( 2, Day08.part1( TEST_DATA_1 ) );
		assertEquals( 6, Day08.part1( TEST_DATA_2 ) );
	}

	@Test
	void part1() {
		assertEquals( 22357, Day08.part1( DATA ) );
	}

	@Test
	void part2Test() {
		assertEquals( 0, Day08.part2( TEST_DATA_1 ) );
		assertEquals( 0, Day08.part2( TEST_DATA_2 ) );
	}

	@Test
	void part2() {
		assertEquals( 0, Day08.part2( DATA ) );
	}

}
