package dev.flowty.aoc.y23;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dev.flowty.aoc.Input;

class Day22Test {

	private static final String[] TEST_DATA = Input.linesOf( ""
			+ "1,0,1~1,2,1\n"
			+ "0,0,2~2,0,2\n"
			+ "0,2,3~2,2,3\n"
			+ "0,0,4~0,2,4\n"
			+ "2,0,5~2,2,5\n"
			+ "0,1,6~2,1,6\n"
			+ "1,1,8~1,1,9" );

	private static final String[] DATA = Input.linesFrom( "y23", "input_22.txt" );

	@Test
	void part1Test() {
		assertEquals( 5, Day22.part1( TEST_DATA ) );
	}

	@Test
	void part1() {
		assertEquals( 534, Day22.part1( DATA ) );
	}

	@Test
	void part2Test() {
		assertEquals( 7, Day22.part2( TEST_DATA ) );
	}

	@Test
	void part2() {
		assertEquals( 88156, Day22.part2( DATA ) );
	}

}
