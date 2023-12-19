package dev.flowty.aoc.y23;

import static java.util.stream.Collectors.joining;

import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

class Day18 {

	static int part1( String[] lines ) {
		Lagoon l = new Lagoon();
		for( String inst : lines ) {
			l.dig( inst );
		}
		System.out.println( "trench" );
		System.out.println( l );
		l.excavate();
		System.out.println( "lagoon" );
		System.out.println( l );
		return l.volume();
	}

	static int part2( String[] lines ) {
		return 0;
	}

	record Loc(int row, int column) {
	}

	static class Lagoon {
		private final SortedMap<Integer, SortedMap<Integer, String>> rowColColour = new TreeMap<>();
		private Loc dig = new Loc( 0, 0 );

		private static final Pattern INST = Pattern.compile( "([RLUD]) (\\d+) \\(#([a-f0-9]+)\\)" );

		Lagoon() {
			rowColColour
					.computeIfAbsent( dig.row, k -> new TreeMap<>() )
					.put( dig.column, "???" );
		}

		void dig( String inst ) {
			Matcher m = INST.matcher( inst );
			if( !m.matches() ) {
				throw new IllegalArgumentException( inst );
			}

			int ri = 0;
			int ci = 0;
			switch( m.group( 1 ) ) {
				case "R":
					ri = 0;
					ci = 1;
					break;
				case "L":
					ri = 0;
					ci = -1;
					break;
				case "D":
					ri = 1;
					ci = 0;
					break;
				case "U":
					ri = -1;
					ci = 0;
					break;
				default:
					throw new IllegalArgumentException( "Unexpected value: " + m.group( 1 ) );
			}

			int l = Integer.parseInt( m.group( 2 ) );
			for( int i = 0; i < l; i++ ) {
				dig = new Loc( dig.row + ri, dig.column + ci );
				rowColColour
						.computeIfAbsent( dig.row, k -> new TreeMap<>() )
						.put( dig.column, m.group( 3 ) );
			}
		}

		void excavate() {
			int minRow = rowColColour.firstKey();
			int maxRow = rowColColour.lastKey();
			int minCol = rowColColour.values().stream()
					.mapToInt( SortedMap::firstKey )
					.min()
					.orElseThrow();
			int maxCol = rowColColour.values().stream()
					.mapToInt( SortedMap::lastKey )
					.max()
					.orElseThrow();

		}

		int volume() {
			return rowColColour.values().stream()
					.mapToInt( SortedMap::size )
					.sum();
		}

		@Override
		public String toString() {
			int minRow = rowColColour.firstKey();
			int maxRow = rowColColour.lastKey();
			int minCol = rowColColour.values().stream()
					.mapToInt( SortedMap::firstKey )
					.min()
					.orElseThrow();
			int maxCol = rowColColour.values().stream()
					.mapToInt( SortedMap::lastKey )
					.max()
					.orElseThrow();

			char[][] canvas = new char[maxRow - minRow + 1][maxCol - minCol + 1];
			for( int r = 0; r < canvas.length; r++ ) {
				for( int c = 0; c < canvas[r].length; c++ ) {
					int row = r;
					int col = c;
					canvas[r][c] = Optional.of( rowColColour )
							.map( m -> m.get( row + minRow ) )
							.map( m -> m.get( col + minCol ) )
							.map( h -> '#' )
							.orElse( '.' );
				}
			}
			return Stream.of( canvas )
					.map( String::new )
					.collect( joining( "\n" ) );
		}
	}
}
