package dev.flowty.aoc.y23;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

class Day21 {

	static long part1( int distance, String[] lines ) {
		Garden g = new Garden( lines );
		g.link();
		g.flood( distance );
		return g.countMatching( p -> p.distancetoStart <= distance
				&& p.distancetoStart % 2 == distance % 2 );
	}

	static int part2( String[] lines ) {
		int distance = 26501365;
		System.out.println( distance / 130.0 );
		System.out.println( distance % 130 );
		Garden g = new Garden( lines );
		g.link();
		g.flood( distance );
		long total = 0;

		long full = g.countMatching( p -> p.distancetoStart % 2 == distance % 2 );

		// The garden can be traversed in any direction for the same cost - the walked
		// distance will completely cover many iterations of the garden, we just need to
		// worry about the fringes of the manhattan-distance diamond

		return 0;
	}

	static class Garden {
		final Plot start;
		final Plot[][] plots;

		Garden( String[] lines ) {
			Plot s = null;
			plots = new Plot[lines.length][];
			for( int i = 0; i < lines.length; i++ ) {
				plots[i] = new Plot[lines[i].length()];
				for( int j = 0; j < plots[i].length; j++ ) {
					if( lines[i].charAt( j ) != '#' ) {
						plots[i][j] = new Plot( i, j );
						if( lines[i].charAt( j ) == 'S' ) {
							s = plots[i][j];
						}
					}
				}
			}
			if( s == null ) {
				throw new IllegalArgumentException( "no start plot!" );
			}
			start = s;
		}

		public void link() {
			for( int i = 0; i < plots.length; i++ ) {
				for( int j = 0; j < plots[i].length; j++ ) {
					Plot p = get( i, j );
					if( p != null ) {
						p.link(
								get( i - 1, j ),
								get( i, j + 1 ),
								get( i + 1, j ),
								get( i, j - 1 ) );
					}
				}
			}
		}

		void flood( int max ) {
			Deque<Flood> pending = new ArrayDeque<>();
			pending.add( new Flood( 0, start ) );

			while( !pending.isEmpty() ) {
				Flood f = pending.removeFirst();
				f.to.offerDistance( f.distance )
						.stream()
						.map( n -> new Flood( f.distance + 1, n ) )
						.forEach( pending::add );
			}
		}

		void clear() {
			for( int i = 0; i < plots.length; i++ ) {
				for( int j = 0; j < plots[i].length; j++ ) {
					if( plots[i][j] != null ) {
						plots[i][j].distancetoStart = Integer.MAX_VALUE;
					}
				}
			}
		}

		long countMatching( Predicate<Plot> condition ) {
			return Stream.of( plots )
					.flatMap( Stream::of )
					.filter( Objects::nonNull )
					.filter( condition )
					.count();
		}

		private Plot get( int row, int column ) {
			if( row < 0
					|| row >= plots.length
					|| column < 0
					|| column >= plots[row].length ) {
				return null;
			}
			return plots[row][column];
		}

		private Plot getWrapped( int row, int column ) {
			int r = (row + plots.length) % plots.length;
			int c = (column + plots[r].length) % plots[r].length;
			return plots[r][c];
		}
	}

	static class Plot {
		final Location location;
		final Set<Plot> neighbours = new HashSet<>();
		int distancetoStart = Integer.MAX_VALUE;

		Plot( int row, int column ) {
			location = new Location( row, column );
		}

		void link( Plot... neighbours ) {
			Stream.of( neighbours )
					.filter( Objects::nonNull )
					.forEach( this.neighbours::add );
		}

		Set<Plot> offerDistance( int distance ) {
			if( distance < distancetoStart ) {
				distancetoStart = distance;
				return neighbours;
			}
			return Collections.emptySet();
		}
	}

	record Flood(int distance, Plot to) {

	}

	record Location(int row, int column) {
	}
}
