package dev.flowty.aoc.y23;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import dev.flowty.aoc.Input;
import dev.flowty.aoc.y23.Day11.Space;

class Day11Test {
	private static final String[] DATA = Input.linesFrom( "y23", "input_11.txt" );

	@Test
	void part1Test() {
		Function<String, Long> manualExpansion = in -> new Space( Input.linesOf( in ), 1 )
				.distances().sum();

		assertEquals( 1, Day11.part1( Input.linesOf( ""
				+ "##" ) ) );
		assertEquals( 1, Day11.part1( Input.linesOf( ""
				+ "#\n"
				+ "#" ) ) );
		assertEquals( 2, Day11.part1( Input.linesOf( ""
				+ "#.\n"
				+ ".#" ) ) );

		assertEquals( 4, Day11.part1( Input.linesOf( ""
				+ "#..\n"
				+ "..#" ) ) );
		assertEquals( 4, manualExpansion.apply( ""
				+ "#...\n"
				+ "...#" ) );

		assertEquals( 6, manualExpansion.apply( ""
				+ "#...\n"
				+ "....\n"
				+ "....\n"
				+ "...#" ) );
		assertEquals( 6, Day11.part1( Input.linesOf( ""
				+ "#..\n"
				+ "...\n"
				+ "..#" ) ) );

		assertEquals( 374, manualExpansion.apply( ""
				+ "....#........\n"
				+ ".........#...\n"
				+ "#............\n"
				+ ".............\n"
				+ ".............\n"
				+ "........#....\n"
				+ ".#...........\n"
				+ "............#\n"
				+ ".............\n"
				+ ".............\n"
				+ ".........#...\n"
				+ "#....#......." ) );

		assertEquals( 374, Day11.part1( Input.linesOf( ""
				+ "...#......\n"
				+ ".......#..\n"
				+ "#.........\n"
				+ "..........\n"
				+ "......#...\n"
				+ ".#........\n"
				+ ".........#\n"
				+ "..........\n"
				+ ".......#..\n"
				+ "#...#....." ) ) );
	}

	@Test
	void part1() {
		assertEquals( 9403026, Day11.part1( DATA ) );
	}

	@Test
	void part2Test() {
		BiFunction<String, Integer, Long> manual = ( p, e ) -> new Space(
				Input.linesOf( p ), e )
						.distances().sum();

		assertEquals( 1, manual.apply( ""
				+ "##", 1000000 ) );
		assertEquals( 1, manual.apply( ""
				+ "#\n"
				+ "#", 1000000 ) );
		assertEquals( 2, manual.apply( ""
				+ "#.\n"
				+ ".#", 1000000 ) );

		assertEquals( 1000001, manual.apply( ""
				+ "#.#", 1000000 ) );
		assertEquals( 2000001, manual.apply( ""
				+ "#..#", 1000000 ) );

		assertEquals( 1000001, manual.apply( ""
				+ "#\n"
				+ ".\n"
				+ "#", 1000000 ) );
		assertEquals( 2000001, manual.apply( ""
				+ "#\n"
				+ ".\n"
				+ ".\n"
				+ "#", 1000000 ) );

		assertEquals( 2000002, manual.apply( ""
				+ "#..\n"
				+ "...\n"
				+ "..#", 1000000 ) );
		assertEquals( 4000002, manual.apply( ""
				+ "#...\n"
				+ "....\n"
				+ "....\n"
				+ "...#", 1000000 ) );

		Function<Integer, Long> example = in -> manual.apply( ""
				+ "...#......\n"
				+ ".......#..\n"
				+ "#.........\n"
				+ "..........\n"
				+ "......#...\n"
				+ ".#........\n"
				+ ".........#\n"
				+ "..........\n"
				+ ".......#..\n"
				+ "#...#.....",
				in );

		assertEquals( 1030, example.apply( 10 ) );
		assertEquals( 8410, example.apply( 100 ) );
	}

	@Test
	void part2() {
		assertEquals( 543018317006L, Day11.part2( DATA ) );
	}
}
