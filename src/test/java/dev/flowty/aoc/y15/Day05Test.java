package dev.flowty.aoc.y15;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dev.flowty.aoc.Input;

class Day05Test {

	private static final String[] DATA = Input.linesFrom( "y15", "input_05.txt" );

	@Test
	void part1Test() {
		assertEquals( true, Day05.isNiceV1( "ugknbfddgicrmopn" ) );
		assertEquals( true, Day05.isNiceV1( "aaa" ) );
		assertEquals( false, Day05.isNiceV1( "jchzalrnumimnmhp" ) );
		assertEquals( false, Day05.isNiceV1( "haegwjzuvuyypxyu" ) );
		assertEquals( false, Day05.isNiceV1( "dvszwmarrgswjxmb" ) );
	}

	@Test
	void part1() {
		assertEquals( 236, Day05.part1( DATA ) );
	}

	@Test
	void part2Test() {
		assertEquals( true, Day05.isNiceV2( "qjhvhtzxzqqjkmpb" ) );
		assertEquals( true, Day05.isNiceV2( "xxyxx" ) );
		assertEquals( false, Day05.isNiceV2( "uurcxstgmygtbstg" ) );
		assertEquals( false, Day05.isNiceV2( "ieodomkazucvgmuy" ) );
	}

	@Test
	void part2() {
		assertEquals( 51, Day05.part2( DATA ) );
	}
}
