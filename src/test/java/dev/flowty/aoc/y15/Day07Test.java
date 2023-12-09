package dev.flowty.aoc.y15;

import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dev.flowty.aoc.Input;
import dev.flowty.aoc.y15.Day07.Circuit;

class Day07Test {

	private static final String[] DATA = Input.linesFrom( "y15", "input_07.txt" );

	@Test
	void part1Test() {
		String[] input = Input.linesOf( ""
				+ "123 -> x\n"
				+ "456 -> y\n"
				+ "x AND y -> d\n"
				+ "x OR y -> e\n"
				+ "x LSHIFT 2 -> f\n"
				+ "y RSHIFT 2 -> g\n"
				+ "NOT x -> h\n"
				+ "NOT y -> i" );

		assertEquals( ""
				+ "d: 72\n"
				+ "e: 507\n"
				+ "f: 492\n"
				+ "g: 114\n"
				+ "h: 65412\n"
				+ "i: 65079\n"
				+ "x: 123\n"
				+ "y: 456",
				new Circuit( input )
						.signals()
						.entrySet().stream()
						.map( e -> e.getKey() + ": " + e.getValue() )
						.collect( joining( "\n" ) ) );
	}

	@Test
	void part1() {
		assertEquals( 956, Day07.part1( DATA ) );
	}

	@Test
	void part2Test() {

	}

	@Test
	void part2() {
		assertEquals( 40149, Day07.part2( DATA ) );
	}
}
