package dev.flowty.aoc.y23;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Day11 {

	static long part1( String[] lines ) {
		return new Space( lines, 2 )
				.distances()
				.sum();
	}

	static long part2( String[] lines ) {
		return new Space( lines, 1000000 )
				.distances()
				.sum();
	}

	static class Space {
		final List<Galaxy> gl = new ArrayList<>();
		final Galaxy[][] galaxies;
		final int[][] hcost;
		final int[][] vcost;

		Space( String[] lines, int expansion ) {

			galaxies = new Galaxy[lines.length][];
			hcost = new int[lines.length][];
			vcost = new int[lines.length][];

			Set<Integer> emptyColumns = IntStream.range( 0, lines[0].length() )
					.mapToObj( Integer::valueOf )
					.collect( toCollection( TreeSet::new ) );

			int galaxyId = 1;
			for( int i = 0; i < lines.length; i++ ) {
				galaxies[i] = new Galaxy[lines[0].length()];
				hcost[i] = new int[galaxies[i].length];
				vcost[i] = new int[galaxies[i].length];

				Arrays.fill( hcost[i], lines[i].chars()
						.allMatch( c -> c == '.' )
								? expansion
								: 1 );
				Arrays.fill( vcost[i], 1 );

				for( int j = 0; j < lines[i].length(); j++ ) {
					if( lines[i].charAt( j ) == '#' ) {
						galaxies[i][j] = new Galaxy( galaxyId++, i, j );
						gl.add( galaxies[i][j] );
						emptyColumns.remove( j );
					}
				}
			}

			emptyColumns.forEach( c -> {
				for( int[] row : vcost ) {
					row[c] = expansion;
				}
			} );
		}

		LongStream distances() {
			List<Long> d = new ArrayList<>();
			for( int i = 0; i < gl.size(); i++ ) {
				for( int j = i + 1; j < gl.size(); j++ ) {
					d.add( distance(
							gl.get( i ),
							gl.get( j ) ) );
				}
			}
			return d.stream()
					.mapToLong( Long::longValue );
		}

		long distance( Galaxy a, Galaxy b ) {
			long d = 0;
			int x = a.x;
			int y = a.y;

			while( x != b.x ) {
				if( x > b.x ) {
					x--;
					d += hcost[x][y];
				}
				if( x < b.x ) {
					x++;
					d += hcost[x][y];
				}
			}
			while( y != b.y ) {
				if( y > b.y ) {
					y--;
					d += vcost[x][y];
				}
				if( y < b.y ) {
					y++;
					d += vcost[x][y];
				}
			}
			return d;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for( int i = 0; i < galaxies.length; i++ ) {
				for( int j = 0; j < galaxies[i].length; j++ ) {
					if( galaxies[i][j] != null ) {
						sb.append( "*" );
					}
					else {
						sb.append( '.' );
					}
				}
				sb.append( "\n" );
			}
			sb.append( gl.stream().map( Galaxy::toString )
					.collect( joining( "\n" ) ) );
			return sb.toString();
		}
	}

	static class Galaxy {
		final int id;
		final int x;
		final int y;

		Galaxy( int id, int x, int y ) {
			this.id = id;
			this.x = x;
			this.y = y;
		}

		@Override
		public String toString() {
			return String.format( "%s ( %s, %s )", id, x, y );
		}
	}
}
