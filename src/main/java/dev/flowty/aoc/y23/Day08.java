package dev.flowty.aoc.y23;

import static java.util.stream.Collectors.joining;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day08 {

	static int part1( String[] lines ) {
		return new Maps( lines ).stepsBetween( "AAA", "ZZZ" );
	}

	static int part2( String[] lines ) {
		return 0;
	}

	static class Maps {
		private final String directions;
		private final Map<String, Node> nodes = new TreeMap<>();

		Maps( String[] lines ) {
			directions = lines[0];

			Pattern p = Pattern.compile( "([A-Z]{3}) = \\(([A-Z]{3}). ([A-Z]{3})\\)" );
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

		int stepsBetween( String start, String end ) {
			int steps = 0;
			int dirIndex = 0;

			Node current = nodes.get( start );
			Node dest = nodes.get( end );

			while( current != dest ) {
				current = current.move( directions.charAt( dirIndex ) );
				steps++;
				dirIndex = (dirIndex + 1) % directions.length();
			}

			return steps;
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
