package dev.flowty.aoc.y23;

import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Day09 {

	static long part1( String[] lines ) {
		return Stream.of( lines )
				.map( Sequence::new )
				.mapToLong( Sequence::next )
				.sum();
	}

	static long part2( String[] lines ) {
		return Stream.of( lines )
				.map( Sequence::new )
				.mapToLong( Sequence::previous )
				.sum();
	}

	static class Sequence {
		final long[] values;

		Sequence( String line ) {
			this( Stream.of( line.split( " " ) )
					.mapToLong( Long::parseLong )
					.toArray() );
		}

		private Sequence( long[] values ) {
			this.values = values;
		}

		boolean isAllZero() {
			return LongStream.of( values )
					.allMatch( l -> l == 0 );
		}

		long next() {
			if( isAllZero() ) {
				return 0;
			}
			return values[values.length - 1] + diffs().next();
		}

		long previous() {
			if( isAllZero() ) {
				return 0;
			}
			return values[0] - diffs().previous();
		}

		Sequence diffs() {
			long[] diffs = new long[values.length - 1];
			for( int i = 0; i < diffs.length; i++ ) {
				diffs[i] = values[i + 1] - values[i];
			}
			return new Sequence( diffs );
		}
	}
}
