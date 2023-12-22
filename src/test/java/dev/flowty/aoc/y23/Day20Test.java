package dev.flowty.aoc.y23;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dev.flowty.aoc.Input;

class Day20Test {

	private static final String[] TEST_DATA_1 = Input.linesOf( ""
			+ "broadcaster -> a, b, c\n"
			+ "%a -> b\n"
			+ "%b -> c\n"
			+ "%c -> inv\n"
			+ "&inv -> a" );
	private static final String[] TEST_DATA_2 = Input.linesOf( ""
			+ "broadcaster -> a\n"
			+ "%a -> inv, con\n"
			+ "&inv -> b\n"
			+ "%b -> con\n"
			+ "&con -> output" );

	private static final String[] DATA = Input.linesFrom( "y23", "input_20.txt" );

	@Test
	void part1Test() {
		assertEquals( 32000000, Day20.part1( TEST_DATA_1 ) );
		assertEquals( 11687500, Day20.part1( TEST_DATA_2 ) );
	}

	@Test
	void part1() {
		assertEquals( 896998430, Day20.part1( DATA ) );
	}

	@Test
	void part2() {
		assertEquals( 0, Day20.part2( DATA ) );
	}

}
