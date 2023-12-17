package dev.flowty.aoc.y23;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Stream;

public class Day17 {

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
				Set<Direction> next = s.current.offer( minRun, maxRun, s );
				for( Direction dir : next ) {
					int minMove = 1;
					if( dir != s.direction ) {
						minMove = minRun;
					}
					Loc loc = new Loc(
							s.current.location.row + dir.ri * minMove,
							s.current.location.col + dir.ci * minMove );
					Step ns = s;
					if( inBounds( loc ) ) {
						for( int i = 1; i <= minMove; i++ ) {
							loc = new Loc(
									s.current.location.row + dir.ri * i,
									s.current.location.col + dir.ci * i );
							Block n = blocks[loc.row][loc.col];
							ns = new Step( ns, dir, n, ns.cumulativeLoss + n.loss );
						}
						pending.add( ns );
					}
				}
			}

			Step s = end.minLossPath();
//			System.out.println( "\n routes for\n" + toString() );
//			System.out.println( "\n" + draw( s ) + " cost " + s.cumulativeLoss );
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
				if( s.direction != null ) {
					canvas[s.current.location.row][s.current.location.col] = s.direction.display;
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

		// maps from a direction to the lowest-cost route to here that can continue in
		// that direction
		final Map<Vector, Step> minimal = new HashMap<>();

		Block( Loc location, int loss ) {
			this.location = location;
			this.loss = loss;
		}

		Set<Direction> offer( int minRun, int maxRun, Step step ) {
			Set<Direction> next = step.direction != null
					? step.direction.next( minRun, maxRun, step )
					: EnumSet.allOf( Direction.class );
			Set<Direction> toExplore = EnumSet.noneOf( Direction.class );

			for( Direction dir : next ) {
				for( Vector vec : step.possibles( dir, maxRun ) ) {
					if( minimal.get( vec ) == null
							|| minimal.get( vec ).cumulativeLoss > step.cumulativeLoss ) {
						minimal.put( vec, step );
						toExplore.add( dir );
					}
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

	record Step(Step previous, Direction direction, Block current, int cumulativeLoss) {
		int tailLength( Direction dir ) {
			int sameCount = 0;
			Step s = this;
			while( s != null && s.direction == dir ) {
				sameCount++;
				s = s.previous;
			}
			return sameCount;
		}

		Set<Vector> possibles( Direction dir, int limit ) {
			int seq = tailLength( dir );
			Set<Vector> v = new HashSet<>();
			for( int i = 1; i <= limit - seq; i++ ) {
				v.add( new Vector( dir, i ) );
			}
			return v;
		}
	}

	record Vector(Direction direction, int steps) {
	}

	private enum Direction {
		N('↑', -1, 0) {
			@Override
			Set<Direction> next( int min, int max, Step last ) {
				Set<Direction> p = EnumSet.noneOf( Direction.class );
				int seq = last.tailLength( this );

				if( seq >= min ) {
					p.add( E );
					p.add( W );
				}
				if( seq < max ) {
					p.add( this );
				}

				return p;
			}
		},
		E('→', 0, 1) {
			@Override
			Set<Direction> next( int min, int max, Step last ) {
				Set<Direction> p = EnumSet.noneOf( Direction.class );
				int seq = last.tailLength( this );

				if( seq >= min ) {
					p.add( N );
					p.add( S );
				}
				if( seq < max ) {
					p.add( this );
				}

				return p;
			}
		},
		S('↓', 1, 0) {
			@Override
			Set<Direction> next( int min, int max, Step last ) {
				Set<Direction> p = EnumSet.noneOf( Direction.class );
				int seq = last.tailLength( this );

				if( seq >= min ) {
					p.add( E );
					p.add( W );
				}
				if( seq < max ) {
					p.add( this );
				}

				return p;
			}
		},
		W('←', 0, -1) {
			@Override
			Set<Direction> next( int min, int max, Step last ) {
				Set<Direction> p = EnumSet.noneOf( Direction.class );
				int seq = last.tailLength( this );

				if( seq >= min ) {
					p.add( N );
					p.add( S );
				}
				if( seq < max ) {
					p.add( this );
				}

				return p;
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

		abstract Set<Direction> next( int min, int max, Step last );
	}
}
