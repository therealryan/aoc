package dev.flowty.aoc.y23;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import dev.flowty.aoc.y23.Day07.Hand;

class Day07Test {

	private static final String[] TEST_DATA = Input.linesOf( ""
			+ "32T3K 765\n"
			+ "T55J5 684\n"
			+ "KK677 28\n"
			+ "KTJJT 220\n"
			+ "QQQJA 483" );

	private static final String[] DATA = Input.linesFrom( "input_07.txt" );

	@Test
	void part1Test() {
		assertEquals( 6440, Day07.part1( TEST_DATA ) );
	}

	@Test
	void part1() {
		assertEquals( 251216224, Day07.part1( DATA ) );
	}

	@Test
	void part2Test() {
		assertEquals( 5905, Day07.part2( TEST_DATA ) );
	}

	@Test
	void part2() {
		assertEquals( 0, Day07.part2( DATA ) );
	}

	@Test
	void order() {
		BiConsumer<String, String> test = ( in, out ) -> {
			List<Hand> hands = Stream.of( in.split( "\n" ) ).map( Hand::new ).collect( toList() );
			Collections.sort( hands );
			assertEquals( out, hands.stream().map( Hand::toString ).collect( joining( "\n" ) ),
					"for " + in );
		};

		test.accept( "T55J5 684\nQQQJA 483", ""
				+ "T55J5 THREE_OF_A_KIND 684\n"
				+ "QQQJA THREE_OF_A_KIND 483" );

		test.accept( "K2T3K 765\nT55J5 684", ""
				+ "K2T3K ONE_PAIR 765\n"
				+ "T55J5 THREE_OF_A_KIND 684" );
	}
}
