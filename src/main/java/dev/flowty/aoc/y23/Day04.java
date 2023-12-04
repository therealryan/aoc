package dev.flowty.aoc.y23;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

class Day04 {

	static int part1( String[] lines ) {
		return Stream.of( lines )
				.map( Card::new )
				.mapToInt( Card::points )
				.sum();
	}

	static int part2( String[] lines ) {
		List<Card> cards = Stream.of( lines )
				.map( Card::new )
				.collect( toList() );

		for( int i = 0; i < cards.size(); i++ ) {
			Card c = cards.get( i );
			int m = c.matches();
			for( int j = 1; j <= m; j++ ) {
				cards.get( i + j ).instances += c.instances;
			}
		}

		return cards.stream()
				.mapToInt( c -> c.instances )
				.sum();
	}

	private static class Card {
		final int id;
		final Set<Integer> winning;
		final Set<Integer> have;
		int instances = 1;

		Card( String line ) {
			Matcher m = Pattern.compile( "Card\\s+(\\d+): ([0-9 ]+) \\| ([0-9 ]+)" ).matcher( line );

			if( !m.matches() ) {
				throw new IllegalArgumentException( line );
			}

			id = Integer.parseInt( m.group( 1 ) );
			winning = Stream.of( m.group( 2 ).split( "\\D+" ) )
					.filter( s -> !s.isEmpty() )
					.map( Integer::parseInt )
					.collect( toCollection( TreeSet::new ) );
			have = Stream.of( m.group( 3 ).split( "\\D+" ) )
					.filter( s -> !s.isEmpty() )
					.map( Integer::parseInt )
					.collect( toCollection( TreeSet::new ) );
		}

		int matches() {
			Set<Integer> intersection = new TreeSet<>( have );
			intersection.retainAll( winning );
			return intersection.size();
		}

		int points() {
			int matches = matches();
			if( matches == 0 ) {
				return 0;
			}
			int p = 1;
			for( int i = 0; i < matches - 1; i++ ) {
				p *= 2;
			}
			return p;
		}

		@Override
		public String toString() {
			return String.format( "%02d: %s | %s = %s : %s",
					id, winning, have, points(), instances );
		}
	}
}
