package dev.flowty.aoc.y23;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Day07 {
	static int part1( String[] lines ) {
		List<Hand> hands = Stream.of( lines )
				.map( Hand::new )
				.collect( toList() );
		Collections.sort( hands );

		int winnings = 0;
		int rank = 1;
		for( Hand hand : hands ) {
			winnings += rank * hand.bid;
			rank++;
		}

		return winnings;
	}

	static int part2( String[] lines ) {
		return 0;
	}

	enum Card {
		ACE('A'),
		KING('K'),
		QUEEN('Q'),
		JACK('J'),
		TEN('T'),
		NINE('9'),
		EIGHT('8'),
		SEVEN('7'),
		SIX('6'),
		FIVE('5'),
		FOUR('4'),
		THREE('3'),
		TWO('2'),;

		public final char character;

		Card( char character ) {
			this.character = character;
		}

		static Card of( char c ) {
			for( Card card : values() ) {
				if( card.character == c ) {
					return card;
				}
			}
			throw new IllegalArgumentException( "unrecognised : " + c );
		}
	}

	enum Type {
		FIVE_OF_A_KIND(5),
		FOUR_OF_A_KIND(4, 1),
		FULL_HOUSE(3, 2),
		THREE_OF_A_KIND(3, 1, 1),
		TWO_PAIR(2, 2, 1),
		ONE_PAIR(2, 1, 1, 1),
		HIGH_CARD(1, 1, 1, 1, 1),
		;

		final Predicate<List<Card>> matcher;

		Type( int... distribution ) {
			Arrays.sort( distribution );
			matcher = hand -> {
				Map<Card, Integer> histogram = hand.stream().collect( toMap(
						k -> k,
						v -> 1,
						( a, b ) -> a + b ) );
				return Arrays.equals(
						distribution,
						histogram.values().stream()
								.mapToInt( Integer::intValue )
								.sorted()
								.toArray() );
			};
		}

		private static Predicate<Map<Card, Integer>> dist( int... counts ) {
			Arrays.sort( counts );
			return hist -> Arrays.equals(
					counts,
					hist.values().stream()
							.mapToInt( Integer::intValue )
							.sorted()
							.toArray() );
		}
	}

	static class Hand implements Comparable<Hand> {
		private final List<Card> cards = new ArrayList<>();
		private final Type type;
		private final int bid;

		Hand( String line ) {
			String[] parts = line.split( " " );
			assert parts.length == 2 : line;

			for( int i = 0; i < parts[0].length(); i++ ) {
				cards.add( Card.of( parts[0].charAt( i ) ) );
			}
			assert cards.size() == 5 : line;
			type = Stream.of( Type.values() )
					.filter( t -> t.matcher.test( cards ) )
					.findFirst()
					.orElseThrow( () -> new IllegalArgumentException( "unrechnised type " + line ) );
			bid = Integer.parseInt( parts[1] );
		}

		@Override
		public int compareTo( Hand o ) {
			int d = o.type.ordinal() - type.ordinal();
			for( int i = 0; d == 0 && i < cards.size() && i < o.cards.size(); i++ ) {
				d = o.cards.get( i ).ordinal() - cards.get( i ).ordinal();
			}
			return d;
		}

		@Override
		public String toString() {
			return cards.stream()
					.map( c -> String.valueOf( c.character ) )
					.collect( joining() )
					+ " " + type + " " + bid;
		}
	}
}
