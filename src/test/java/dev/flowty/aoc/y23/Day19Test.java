package dev.flowty.aoc.y23;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dev.flowty.aoc.Input;

class Day19Test {

	private static final String[] TEST_DATA = Input.linesOf( ""
			+ "px{a<2006:qkq,m>2090:A,rfg}\n"
			+ "pv{a>1716:R,A}\n"
			+ "lnx{m>1548:A,A}\n"
			+ "rfg{s<537:gd,x>2440:R,A}\n"
			+ "qs{s>3448:A,lnx}\n"
			+ "qkq{x<1416:A,crn}\n"
			+ "crn{x>2662:A,R}\n"
			+ "in{s<1351:px,qqz}\n"
			+ "qqz{s>2770:qs,m<1801:hdj,R}\n"
			+ "gd{a>3333:R,R}\n"
			+ "hdj{m>838:A,pv}\n"
			+ "\n"
			+ "{x=787,m=2655,a=1222,s=2876}\n"
			+ "{x=1679,m=44,a=2067,s=496}\n"
			+ "{x=2036,m=264,a=79,s=2244}\n"
			+ "{x=2461,m=1339,a=466,s=291}\n"
			+ "{x=2127,m=1623,a=2188,s=1013}" );

	private static final String[] DATA = Input.linesFrom( "y23", "input_19.txt" );

	@Test
	void part1Test() {
		assertEquals( 19114, Day19.part1( TEST_DATA ) );
	}

	@Test
	void part1() {
		assertEquals( 332145, Day19.part1( DATA ) );
	}

	@Test
	void part2Test() {
		assertEquals( 1, Day19.part2( Input.linesOf( ""
				+ "in{x>1:R,m>1:R,a>1:R,s>1:R,A}\n"
				+ "\n"
				+ "{x=1,m=1,a=1,s=1}\n"
				+ "{x=4000,m=4000,a=4000,s=4000}" ) ) );
		assertEquals( 1, Day19.part2( Input.linesOf( ""
				+ "in{x<4000:R,m<4000:R,a<4000:R,s<4000:R,A}\n"
				+ "\n"
				+ "{x=1,m=1,a=1,s=1}\n"
				+ "{x=4000,m=4000,a=4000,s=4000}" ) ) );
		assertEquals( 1, Day19.part2( Input.linesOf( ""
				+ "in{x<4000:R,m<4000:R,a<4000:R,s>1:R,A}\n"
				+ "\n"
				+ "{x=1,m=1,a=1,s=1}\n"
				+ "{x=4000,m=4000,a=4000,s=1}" ) ) );

		assertEquals( 167409079868000L, Day19.part2( TEST_DATA ) );
	}

	@Test
	void part2() {
		assertEquals( 136661579897555L, Day19.part2( DATA ) );
	}
}
