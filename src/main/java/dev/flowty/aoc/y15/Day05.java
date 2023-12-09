package dev.flowty.aoc.y15;

import static java.util.stream.Collectors.toSet;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

class Day05 {

	static long part1( String[] lines ) {
		return Stream.of( lines )
				.filter( Day05::isNiceV1 )
				.count();
	}

	static long part2( String[] lines ) {
		return Stream.of( lines )
				.filter( Day05::isNiceV2 )
				.count();
	}

	private static final Set<String> bad = Stream.of(
			"ab", "cd", "pq", "xy" )
			.collect( toSet() );

	static boolean isNiceV1( String line ) {

		if( bad.stream().anyMatch( line::contains ) ) {
			return false;
		}

		boolean hasDouble = false;
		int vowels = 0;

		for( int i = 0; i < line.length(); i++ ) {
			char c = line.charAt( i );
			if( i > 0 ) {
				Character p = line.charAt( i - 1 );
				hasDouble |= p == c;
			}

			if( c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' ) {
				vowels++;
			}

			if( hasDouble && vowels >= 3 ) {
				return true;
			}
		}

		return false;
	}

	static boolean isNiceV2( String line ) {

		boolean hasSpacedPair = false;
		Set<String> pairs = new TreeSet<>();

		for( int i = 0; i < line.length(); i++ ) {
			char c = line.charAt( i );
			if( i > 0 ) {
				pairs.add( line.substring( i - 1, i + 1 ) );
			}
			if( i > 1 ) {
				Character p = line.charAt( i - 2 );
				hasSpacedPair |= p == c;
			}
		}

		if( !hasSpacedPair ) {
			return false;
		}

		for( String pair : pairs ) {
			if( line.lastIndexOf( pair ) - line.indexOf( pair ) > 1 ) {
				return true;
			}
		}

		return false;
	}
}
