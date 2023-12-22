package dev.flowty.aoc.y23;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

class Day17 {

	static int part1( String[] lines ) {
		City c = new City( lines );
		return c.minLoss( 1, 3 );
	}

	static int part2( String[] lines ) {
		City c = new City( lines );
		return c.minLoss( 4, 10 );
	}

	record Loc(int row, int col) {
	}

	private static class City {
		private final Block[][] blocks;
		private final Block end;

		City( String[] lines ) {
			blocks = new Block[lines.length][];
			for( int i = 0; i < lines.length; i++ ) {
				blocks[i] = new Block[lines[i].length()];
				for( int j = 0; j < blocks[i].length; j++ ) {
					blocks[i][j] = new Block(
							new Loc( i, j ),
							Integer.parseInt( lines[i].substring( j, j + 1 ) ) );
				}
			}
			end = blocks[blocks.length - 1][blocks[blocks.length - 1].length - 1];
		}

		int minLoss( int minRun, int maxRun ) {
			PriorityQueue<Step> pending = new PriorityQueue<>(
					comparing( s -> s.cumulativeLoss ) );
			pending.add( new Step( null, null, blocks[0][0], 0 ) );

			while( !pending.isEmpty() ) {
				Step s = pending.remove();
				Set<Vector> next = s.current.offer( minRun, maxRun, s );
				for( Vector vec : next ) {

					Loc loc = new Loc(
							s.current.location.row + vec.direction.ri * vec.steps,
							s.current.location.col + vec.direction.ci * vec.steps );
					Step ns = s;
					if( inBounds( loc ) ) {
						for( int i = 1; i <= vec.steps; i++ ) {
							loc = new Loc(
									s.current.location.row + vec.direction.ri * i,
									s.current.location.col + vec.direction.ci * i );
							Block n = blocks[loc.row][loc.col];
							ns = new Step( ns, vec, n, ns.cumulativeLoss + n.loss );
						}
						pending.add( ns );
					}
				}
			}

			Step s = end.minLossPath();
			return s.cumulativeLoss;
		}

		private boolean inBounds( Loc loc ) {
			return 0 <= loc.row && 0 <= loc.col
					&& loc.row < blocks.length
					&& loc.col < blocks[loc.row].length;
		}

		@Override
		public String toString() {
			return Stream.of( blocks )
					.map( r -> Stream.of( r )
							.map( String::valueOf )
							.collect( joining() ) )
					.collect( joining( "\n" ) );
		}

		public String draw( Step step ) {
			String[] lines = toString().split( "\n" );
			char[][] canvas = new char[lines.length][];
			for( int i = 0; i < canvas.length; i++ ) {
				canvas[i] = lines[i].toCharArray();
			}

			Step s = step;
			while( s != null ) {
				if( s.vector != null ) {
					canvas[s.current.location.row][s.current.location.col] = s.vector.direction.display;
				}
				s = s.previous;
			}

			return Stream.of( canvas )
					.map( String::new )
					.collect( joining( "\n" ) );
		}
	}

	private static class Block {
		final Loc location;
		final int loss;

		// maps from a direction/distance to the lowest-cost route to here that took
		// arrived via that vector
		final Map<Vector, Step> minimal = new HashMap<>();

		Block( Loc location, int loss ) {
			this.location = location;
			this.loss = loss;
		}

		Set<Vector> offer( int minRun, int maxRun, Step step ) {

			Step min = minimal.compute(
					step.vector,
					( k, e ) -> e == null || step.cumulativeLoss < e.cumulativeLoss
							? step
							: e );

			if( min != step ) {
				return Collections.emptySet();
			}

			Set<Direction> next = step.vector != null
					? step.vector.direction.next()
					: EnumSet.allOf( Direction.class );

			Set<Vector> toExplore = new TreeSet<>();
			for( Direction dir : next ) {
				for( int i = minRun; i <= maxRun; i++ ) {
					toExplore.add( new Vector( dir, i ) );
				}
			}

			return toExplore;
		}

		public Step minLossPath() {
			return minimal.entrySet().stream()
					.map( Entry::getValue )
					.min( Comparator.comparing( s -> s.cumulativeLoss ) )
					.orElseThrow();
		}

		@Override
		public String toString() {
			return String.valueOf( loss );
		}
	}

	record Step(Step previous, Vector vector, Block current, int cumulativeLoss) {
	}

	record Vector(Direction direction, int steps) implements Comparable<Vector> {
		@Override
		public int compareTo( Vector o ) {
			int d = direction.ordinal() - o.direction.ordinal();
			if( d == 0 ) {
				d = steps - o.steps;
			}
			return d;
		}
	}

	private enum Direction {
		N('↑', -1, 0) {
			@Override
			Set<Direction> next() {
				return EnumSet.of( E, W );
			}
		},
		E('→', 0, 1) {
			@Override
			Set<Direction> next() {
				return EnumSet.of( N, S );
			}
		},
		S('↓', 1, 0) {
			@Override
			Set<Direction> next() {
				return EnumSet.of( E, W );
			}
		},
		W('←', 0, -1) {
			@Override
			Set<Direction> next() {
				return EnumSet.of( N, S );
			}
		};

		public final char display;
		public final int ri;
		public final int ci;

		Direction( char display, int ri, int ci ) {
			this.display = display;
			this.ri = ri;
			this.ci = ci;
		}

		abstract Set<Direction> next();
	}
}
