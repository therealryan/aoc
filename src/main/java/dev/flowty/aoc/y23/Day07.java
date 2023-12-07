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
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day07 {

	static int part1( String[] lines ) {
		return winnings( lines, StdDeck.values(), StdType.values() );
	}

	static int part2( String[] lines ) {
		return winnings( lines, LowJokerDeck.values(), JokerWildType.values() );
	}

	private static int winnings( String[] lines, Card[] deck, Type[] types ) {
		List<Hand> hands = Stream.of( lines )
				.map( line -> new Hand( line, deck, types ) )
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

	interface Card {
		char character();

		int ordinal();

		static Card of( char c, Card[] deck ) {
			for( Card card : deck ) {
				if( card.character() == c ) {
					return card;
				}
			}
			throw new IllegalArgumentException( "unrecognised : " + c );
		}
	}

	enum StdDeck implements Card {
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

		private final char character;

		StdDeck( char character ) {
			this.character = character;
		}

		@Override
		public char character() {
			return character;
		}
	}

	enum LowJokerDeck implements Card {
		ACE('A'),
		KING('K'),
		QUEEN('Q'),
		TEN('T'),
		NINE('9'),
		EIGHT('8'),
		SEVEN('7'),
		SIX('6'),
		FIVE('5'),
		FOUR('4'),
		THREE('3'),
		TWO('2'),
		JOKER('J'),;

		private final char character;

		LowJokerDeck( char character ) {
			this.character = character;
		}

		@Override
		public char character() {
			return character;
		}
	}

	interface Type {
		boolean matches( List<Card> hand );

		int ordinal();
	}

	enum StdType implements Type {
		FIVE_OF_A_KIND(5),
		FOUR_OF_A_KIND(4, 1),
		FULL_HOUSE(3, 2),
		THREE_OF_A_KIND(3, 1, 1),
		TWO_PAIR(2, 2, 1),
		ONE_PAIR(2, 1, 1, 1),
		HIGH_CARD(1, 1, 1, 1, 1),
		;

		final Predicate<List<Card>> matcher;

		StdType( int... distribution ) {
			Arrays.sort( distribution );
			matcher = hand -> {
				Map<Character, Integer> histogram = hand.stream().collect( toMap(
						Card::character,
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

		@Override
		public boolean matches( List<Card> hand ) {
			return matcher.test( hand );
		}
	}

	enum JokerWildType implements Type {
		FIVE_OF_A_KIND,
		FOUR_OF_A_KIND,
		FULL_HOUSE,
		THREE_OF_A_KIND,
		TWO_PAIR,
		ONE_PAIR,
		HIGH_CARD,
		;

		private final StdType peer = StdType.valueOf( name() );

		@Override
		public boolean matches( List<Card> hand ) {
			if( peer.matches( hand ) ) {
				return true; // natural match!
			}

			List<Card> nonJokers = hand.stream()
					.filter( c -> c.character() != 'J' )
					.collect( toList() );
			int jokerCount = hand.size() - nonJokers.size();
			if( jokerCount > 0 ) {
				// we have jokers!
				for( Card candidate : nonJokers ) {
					// what if they were the same as one of our other cards?
					List<Card> prospect = new ArrayList<>( nonJokers );
					IntStream.range( 0, jokerCount ).forEach( i -> prospect.add( new Card() {

						@Override
						public int ordinal() {
							throw new UnsupportedOperationException();
						}

						@Override
						public char character() {
							return candidate.character();
						}
					} ) );

					if( peer.matches( prospect ) ) {
						return true;
					}
				}
			}

			return false;
		}
	}

	static class Hand implements Comparable<Hand> {
		private final List<Card> cards = new ArrayList<>();
		private final Type type;
		private final int bid;

		Hand( String line, Card[] deck, Type[] types ) {
			String[] parts = line.split( " " );
			assert parts.length == 2 : line;

			for( int i = 0; i < parts[0].length(); i++ ) {
				cards.add( Card.of( parts[0].charAt( i ), deck ) );
			}
			assert cards.size() == 5 : line;
			type = Stream.of( types )
					.filter( t -> t.matches( cards ) )
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
					.map( c -> String.valueOf( c.character() ) )
					.collect( joining() )
					+ " " + type + " " + bid;
		}
	}
}
