package dev.flowty.aoc.y23;

import static java.util.stream.Collectors.toCollection;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import dev.flowty.aoc.Input;
import dev.flowty.aoc.y23.Day05.MappedRange;
import dev.flowty.aoc.y23.Day05.Range;

class Day05Test {

	private static final String[] TEST_DATA = Input.linesOf( ""
			+ "seeds: 79 14 55 13\n"
			+ "\n"
			+ "seed-to-soil map:\n"
			+ "50 98 2\n"
			+ "52 50 48\n"
			+ "\n"
			+ "soil-to-fertilizer map:\n"
			+ "0 15 37\n"
			+ "37 52 2\n"
			+ "39 0 15\n"
			+ "\n"
			+ "fertilizer-to-water map:\n"
			+ "49 53 8\n"
			+ "0 11 42\n"
			+ "42 0 7\n"
			+ "57 7 4\n"
			+ "\n"
			+ "water-to-light map:\n"
			+ "88 18 7\n"
			+ "18 25 70\n"
			+ "\n"
			+ "light-to-temperature map:\n"
			+ "45 77 23\n"
			+ "81 45 19\n"
			+ "68 64 13\n"
			+ "\n"
			+ "temperature-to-humidity map:\n"
			+ "0 69 1\n"
			+ "1 0 69\n"
			+ "\n"
			+ "humidity-to-location map:\n"
			+ "60 56 37\n"
			+ "56 93 4" );

	private static final String[] DATA = Input.linesFrom( "y23", "input_05.txt" );

	@Test
	void part1Test() {
		assertEquals( 35, Day05.part1( TEST_DATA ) );
	}

	@Test
	void part1() {
		assertEquals( 107430936, Day05.part1( DATA ) );
	}

	@Test
	void part2Test() {
		assertEquals( 46, Day05.part2( TEST_DATA ) );
	}

	@Test
	void part2() {
		assertEquals( 23738616, Day05.part2( DATA ) );
	}

	@Test
	void split() {
		MappedRange mr = new MappedRange( 3, 0, 4 );

		BiConsumer<Range, String> test = ( in, out ) -> assertEquals(
				out,
				mr.splitOnInput( in ).toString(),
				"for " + in );

		// below
		test.accept( new Range( 0, 1 ), "[[0-1]]" );
		test.accept( new Range( 0, 2 ), "[[0-2]]" );

		// above
		test.accept( new Range( 7, 9 ), "[[7-9]]" );

		// inside
		test.accept( new Range( 3, 6 ), "[[3-6]]" );
		test.accept( new Range( 6, 6 ), "[[6-6]]" );

		// around
		test.accept( new Range( 0, 9 ), "[[0-2], [3-6], [7-9]]" );
		test.accept( new Range( 0, 7 ), "[[0-2], [3-6], [7-7]]" );
		test.accept( new Range( 2, 7 ), "[[2-2], [3-6], [7-7]]" );

		// lower
		test.accept( new Range( 0, 5 ), "[[0-2], [3-5]]" );
		test.accept( new Range( 0, 6 ), "[[0-2], [3-6]]" );

		// upper
		test.accept( new Range( 6, 9 ), "[[6-6], [7-9]]" );
		test.accept( new Range( 3, 9 ), "[[3-6], [7-9]]" );
		test.accept( new Range( 3, 7 ), "[[3-6], [7-7]]" );

		// exhaustive check for unhandled cases
		for( int i = 0; i < 10; i++ ) {
			for( int j = i + 1; j < 10; j++ ) {
				Range r = new Range( i, j );
				mr.splitOnInput( r );
			}
		}
	}

	@Test
	void consolidate() {
		Pattern rp = Pattern.compile( "(\\d+)-(\\d+)" );
		BiConsumer<String, String> test = ( in, out ) -> {
			TreeSet<Range> r = Stream.of( in.split( "," ) )
					.map( s -> rp.matcher( s ) )
					.filter( Matcher::matches )
					.map( m -> new Range( Long.parseLong( m.group( 1 ) ), Long.parseLong( m.group( 2 ) ) ) )
					.collect( toCollection( TreeSet::new ) );

			assertEquals( out, Range.consolidate( r ).toString(), "for " + in );
		};

		// unit
		test.accept( "0-1", "[[0-1]]" );

		// separate
		test.accept( "0-1,3-4", "[[0-1], [3-4]]" );

		// encompassed
		test.accept( "0-5,2-4", "[[0-5]]" );
		test.accept( "2-4,0-5", "[[0-5]]" );

		// butted. lol.
		test.accept( "0-1,2-3", "[[0-3]]" );

		// overlap
		test.accept( "0-4,3-6", "[[0-6]]" );
		test.accept( "3-6,0-4", "[[0-6]]" );
	}
}
