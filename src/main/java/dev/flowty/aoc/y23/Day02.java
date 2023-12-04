package dev.flowty.aoc.y23;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

class Day02 {

	static int part1( String[] lines ) {
		return Stream.of( lines )
				.map( Game::new )
				.filter( g -> g.possible( 12, 13, 14 ) )
				.mapToInt( g -> g.id )
				.sum();
	}

	static int part2( String[] lines ) {
		return Stream.of( lines )
				.map( Game::new )
				.mapToInt( Game::maxPower )
				.sum();
	}

	private static class Game {
		final int id;
		final List<Set> sets;

		Game( String line ) {
			Matcher m = Pattern.compile( "Game (\\d+): (.+)" ).matcher( line );

			if( !m.matches() ) {
				throw new IllegalArgumentException( "bad line " + line );
			}

			id = Integer.parseInt( m.group( 1 ) );
			sets = Stream.of( m.group( 2 ).split( ";" ) )
					.map( Set::new )
					.collect( toList() );
		}

		boolean possible( int r, int g, int b ) {
			return sets.stream().allMatch( s -> s.possible( r, g, b ) );
		}

		int maxPower() {
			Set max = new Set( "" );
			sets.forEach( s -> s.maximal( max ) );
			return max.power();
		}

		@Override
		public String toString() {
			return id + " " + sets;
		}
	}

	private static class Set {
		int red;
		int green;
		int blue;

		Set( String def ) {
			Function<String, Integer> extract = colour -> {
				Matcher m = Pattern.compile( "(\\d+) " + colour ).matcher( def );
				if( m.find() ) {
					return Integer.parseInt( m.group( 1 ) );
				}
				return 0;
			};
			red = extract.apply( "red" );
			green = extract.apply( "green" );
			blue = extract.apply( "blue" );
		}

		boolean possible( int r, int g, int b ) {
			return r >= red && g >= green && b >= blue;
		}

		void maximal( Set max ) {
			if( red > max.red ) {
				max.red = red;
			}
			if( green > max.green ) {
				max.green = green;
			}
			if( blue > max.blue ) {
				max.blue = blue;
			}
		}

		int power() {
			return red * green * blue;
		}

		@Override
		public String toString() {
			return "r" + red + " g" + green + " b" + blue;
		}
	}
}
