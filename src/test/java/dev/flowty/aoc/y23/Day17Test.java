package dev.flowty.aoc.y23;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import dev.flowty.aoc.Input;

class Day17Test {

	private static final String[] TEST_DATA = Input.linesOf( ""
			+ "2413432311323\n"
			+ "3215453535623\n"
			+ "3255245654254\n"
			+ "3446585845452\n"
			+ "4546657867536\n"
			+ "1438598798454\n"
			+ "4457876987766\n"
			+ "3637877979653\n"
			+ "4654967986887\n"
			+ "4564679986453\n"
			+ "1224686865563\n"
			+ "2546548887735\n"
			+ "4322674655533" );

	private static final String[] DATA = Input.linesFrom( "y23", "input_17.txt" );

	@Test
	void part1Test() {
		assertEquals( 102, Day17.part1( TEST_DATA ) );
	}

	@Test
	void part1() {
		assertEquals( 791, Day17.part1( DATA ) );
	}

	@Test
	void part2Test() {
		assertEquals( 4, Day17.part2( Input.linesOf( ""
				+ "11111" ) ) );

		assertEquals( 71, Day17.part2( Input.linesOf( ""
				+ "111111111111\n"
				+ "999999999991\n"
				+ "999999999991\n"
				+ "999999999991\n"
				+ "999999999991" ) ) );

		assertEquals( 94, Day17.part2( TEST_DATA ) );
	}

	@Test
	@Disabled("not correct yet!")
	void part2() {

		// simplify the recursion:
		// nodes store cost, direction and blocks since the last turn
		// *always* turn at each recursion, then recurse on the move of min to max steps
		assertEquals( 0, Day17.part2( DATA ) ); // this needs work
	}

	private static int diffCost( String city, String path ) {
		String[] cl = city.split( "\n" );
		String[] pl = path.split( "\n" );

		int cost = 0;

		for( int i = 0; i < pl.length; i++ ) {
			char[] cc = cl[i].toCharArray();
			char[] pc = pl[i].toCharArray();

			for( int j = 0; j < pc.length; j++ ) {
				if( cc[j] != pc[j] ) {
					cost += Integer.parseInt( String.valueOf( cc[j] ) );
				}
			}
		}
		return cost;
	}
}
