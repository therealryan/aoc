package dev.flowty.aoc.y23;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dev.flowty.aoc.Input;

class Day15Test {

	private static final String[] TEST_DATA = Input.linesOf( ""
			+ "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7" );

	private static final String[] DATA = Input.linesFrom( "y23", "input_15.txt" );

	@Test
	void part1Test() {
		assertEquals( 1320, Day15.part1( TEST_DATA ) );
	}

	@Test
	void part1() {
		assertEquals( 522547, Day15.part1( DATA ) );
	}

	@Test
	void part2Test() {
		assertEquals( 145, Day15.part2( TEST_DATA ) );
	}

	@Test
	void part2() {
		assertEquals( 229271, Day15.part2( DATA ) );
	}
}
