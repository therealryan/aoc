package dev.flowty.aoc.y23;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day08 {

	static int part1( String[] lines ) {
		return new Maps( lines ).stepsBetween(
				n -> "AAA".equals( n.name ),
				n -> "ZZZ".equals( n.name ) );
	}

	static long part2( String[] lines ) {
		return new Maps( lines )
				.periods(
						n -> n.name.endsWith( "A" ),
						n -> n.name.endsWith( "Z" ) ).stream()
				.map( p -> primeFactors( p ).keySet() )
				.flatMap( Set::stream )
				.collect( toSet() ).stream()
				.mapToLong( i -> i )
				.reduce( 1, ( a, b ) -> a * b );
	}

	public static Map<Integer, Integer> primeFactors( int number ) {
		int remainder = number;

		Map<Integer, Integer> factors = new TreeMap<>();

		for( int factor = 2; factor <= remainder; factor++ ) {
			while( remainder % factor == 0 ) {
				factors.compute( factor, ( k, v ) -> v == null ? 1 : v + 1 );
				remainder /= factor;
			}
		}

		assert number == factors.entrySet().stream()
				.mapToInt( e -> e.getKey() * e.getValue() )
				.reduce( 1, ( a, b ) -> a * b ) : number + " != " + factors;

		return factors;
	}

	static class Maps {
		private final String directions;
		private final Map<String, Node> nodes = new TreeMap<>();

		Maps( String[] lines ) {
			directions = lines[0];

			Pattern p = Pattern.compile( "(.{3}) = \\((.{3}). (.{3})\\)" );
			for( int i = 2; i < lines.length; i++ ) {
				Matcher m = p.matcher( lines[i] );
				if( !m.matches() ) {
					throw new IllegalArgumentException( lines[i] );
				}

				nodes.put( m.group( 1 ), new Node( m.group( 1 ) ) );
			}

			for( int i = 2; i < lines.length; i++ ) {
				Matcher m = p.matcher( lines[i] );
				if( !m.matches() ) {
					throw new IllegalArgumentException( lines[i] );
				}
				Node n = nodes.get( m.group( 1 ) );
				Node l = nodes.get( m.group( 2 ) );
				Node r = nodes.get( m.group( 3 ) );

				assert n != null : lines[0];
				assert l != null : lines[0];
				assert r != null : lines[0];

				n.set( l, r );
			}
		}

		int stepsBetween( Predicate<Node> start, Predicate<Node> end ) {
			int steps = 0;
			int dirIndex = 0;

			Set<Node> current = nodes.values().stream()
					.filter( start )
					.collect( toSet() );

			while( !current.stream().allMatch( end ) ) {
				char move = directions.charAt( dirIndex );
				current = current.stream()
						.map( n -> n.move( move ) )
						.collect( toSet() );
				steps++;
				dirIndex = (dirIndex + 1) % directions.length();
			}

			return steps;
		}

		Set<Integer> periods( Predicate<Node> start, Predicate<Node> end ) {
			Set<Integer> p = new TreeSet<>();
			nodes.values().stream()
					.filter( start )
					.forEach( node -> {
						String startName = node.name;
						Set<Integer> periods = new TreeSet<>();
						int samples = 100;
						int dirIndex = 0;
						int steps = 0;
						int previous = 0;
						while( samples > 0 ) {
							char move = directions.charAt( dirIndex );
							node = node.move( move );
							steps++;
							dirIndex = (dirIndex + 1) % directions.length();

							if( end.test( node ) ) {
								periods.add( steps - previous );
								previous = steps;
								samples--;
							}
						}
						assert periods.size() == 1 : startName + " " + periods;
						p.add( periods.iterator().next() );
					} );
			return p;
		}

		@Override
		public String toString() {
			return directions
					+ "\n\n"
					+ nodes.values().stream()
							.map( Node::toString )
							.collect( joining( "\n" ) );
		}
	}

	static class Node {
		private final String name;
		private Node left;
		private Node right;

		Node( String name ) {
			this.name = name;
		}

		void set( Node l, Node r ) {
			left = l;
			right = r;
		}

		Node move( char dir ) {
			if( 'L' == dir ) {
				return left;
			}
			if( 'R' == dir ) {
				return right;
			}
			throw new IllegalArgumentException( "Bad direction " + dir );
		}

		@Override
		public String toString() {
			return name + " = ( " + left.name + ", " + right.name + " )";
		}
	}
}
