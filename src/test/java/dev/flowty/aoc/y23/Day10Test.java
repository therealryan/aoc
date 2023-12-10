package dev.flowty.aoc.y23;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dev.flowty.aoc.Input;

class Day10Test {

	private static final String[] DATA = Input.linesFrom( "y23", "input_10.txt" );

	@Test
	void part1Test() {
		assertEquals( 4, Day10.part1( Input.linesOf( ""
				+ "-L|F7\n"
				+ "7S-7|\n"
				+ "L|7||\n"
				+ "-L-J|\n"
				+ "L|-JF" ) ) );
		assertEquals( 8, Day10.part1( Input.linesOf( ""
				+ "7-F7-\n"
				+ ".FJ|7\n"
				+ "SJLL7\n"
				+ "|F--J\n"
				+ "LJ.LJ" ) ) );
	}

	@Test
	void part1() {
		assertEquals( 6942, Day10.part1( DATA ) );
	}

	@Test
	void part2Test() {

		assertEquals( 4, Day10.part2( Input.linesOf( ""
				+ "...........\n"
				+ ".S-------7.\n"
				+ ".|F-----7|.\n"
				+ ".||.....||.\n"
				+ ".||.....||.\n"
				+ ".|L-7.F-J|.\n"
				+ ".|..|.|..|.\n"
				+ ".L--J.L--J.\n"
				+ "..........." ) ) );

		assertEquals( 4, Day10.part2( Input.linesOf( ""
				+ "..........\n"
				+ ".S------7.\n"
				+ ".|F----7|.\n"
				+ ".||....||.\n"
				+ ".||....||.\n"
				+ ".|L-7F-J|.\n"
				+ ".|..||..|.\n"
				+ ".L--JL--J.\n"
				+ ".........." ) ) );

		assertEquals( 8, Day10.part2( Input.linesOf( ""
				+ ".F----7F7F7F7F-7....\n"
				+ ".|F--7||||||||FJ....\n"
				+ ".||.FJ||||||||L7....\n"
				+ "FJL7L7LJLJ||LJ.L-7..\n"
				+ "L--J.L7...LJS7F-7L7.\n"
				+ "....F-J..F7FJ|L7L7L7\n"
				+ "....L7.F7||L7|.L7L7|\n"
				+ ".....|FJLJ|FJ|F7|.LJ\n"
				+ "....FJL-7.||.||||...\n"
				+ "....L---J.LJ.LJLJ..." ) ) );

		assertEquals( 10, Day10.part2( Input.linesOf( ""
				+ "FF7FSF7F7F7F7F7F---7\n"
				+ "L|LJ||||||||||||F--J\n"
				+ "FL-7LJLJ||||||LJL-77\n"
				+ "F--JF--7||LJLJ7F7FJ-\n"
				+ "L---JF-JLJ.||-FJLJJ7\n"
				+ "|F|F-JF---7F7-L7L|7|\n"
				+ "|FFJF7L7F-JF7|JL---7\n"
				+ "7-L-JL7||F7|L7F-7F7|\n"
				+ "L.L7LFJ|||||FJL7||LJ\n"
				+ "L7JLJL-JLJLJL--JLJ.L" ) ) );
	}

	@Test
	void part2() {
		assertEquals( 297, Day10.part2( DATA ) );
	}
}
