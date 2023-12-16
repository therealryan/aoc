package dev.flowty.aoc.y23;

import static java.util.stream.Collectors.joining;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

class Day16 {

	static int part1( String[] lines ) {
		Grid g = new Grid( lines );
		return g.energised( 0, 0, Direction.E );
	}

	static int part2( String[] lines ) {
		Grid g = new Grid( lines );
		return g.maxEnergised();
	}

	private static class Grid {
		private final Device[][] grid;

		Grid( String[] lines ) {
			grid = new Device[lines.length][];
			for( int i = 0; i < lines.length; i++ ) {
				grid[i] = new Device[lines[i].length()];
				for( int j = 0; j < lines[i].length(); j++ ) {
					grid[i][j] = Device.of( lines[i].charAt( j ) );
				}
			}
		}

		int maxEnergised() {
			int max = 0;

			for( int i = 0; i < grid.length; i++ ) {
				max = Math.max( max, energised( i, 0, Direction.E ) );
				max = Math.max( max, energised( i, grid[i].length - 1, Direction.W ) );
			}
			for( int i = 0; i < grid[0].length; i++ ) {
				max = Math.max( max, energised( 0, i, Direction.S ) );
				max = Math.max( max, energised( grid.length - 1, i, Direction.N ) );
			}

			return max;
		}

		int energised( int row, int colum, Direction dir ) {
			Deque<Segment> pending = new ArrayDeque<>();
			pending.add( new Segment( row, colum, dir ) );

			Set<Segment> complete = new TreeSet<>();

			while( !pending.isEmpty() ) {
				Segment b = pending.removeFirst();
				List<Segment> children = trace( b );
				complete.add( b );
				children.stream()
						.filter( s -> !complete.contains( s ) )
						.forEach( pending::add );
			}

			boolean[][] energised = new boolean[grid.length][];
			for( int i = 0; i < grid.length; i++ ) {
				energised[i] = new boolean[grid[i].length];
				Arrays.fill( energised[i], false );
			}

			complete.stream()
					.forEach( seg -> energised[seg.row][seg.column] = true );

			int ec = 0;
			for( int i = 0; i < energised.length; i++ ) {
				for( int j = 0; j < energised[i].length; j++ ) {
					if( energised[i][j] ) {
						ec++;
					}
				}
			}

			return ec;
		}

		Device get( int row, int col ) {
			if( row < 0 || grid.length <= row
					|| col < 0 || grid[row].length <= col ) {
				return null;
			}
			return grid[row][col];
		}

		List<Segment> trace( Segment seg ) {
			ArrayList<Segment> children = new ArrayList<>();
			Device d = get( seg.row, seg.column );

			Direction[] out = d.outputs( seg.direction );

			for( Direction dir : out ) {
				Segment n = new Segment( seg.row + dir.ri, seg.column + dir.ci, dir );
				if( get( n.row, n.column ) != null ) {
					children.add( n );
				}
			}

			return children;
		}

		public String draw( Collection<Segment> segs ) {
			String[] rows = toString().split( "\n" );
			char[][] canvas = new char[rows.length][];
			for( int i = 0; i < rows.length; i++ ) {
				canvas[i] = rows[i].toCharArray();
			}

			segs.forEach( s -> canvas[s.row][s.column] = s.direction.display );

			return Stream.of( canvas )
					.map( String::new )
					.collect( joining( "\n" ) );
		}

		@Override
		public String toString() {
			return Stream.of( grid )
					.map( r -> Stream.of( r )
							.map( d -> String.valueOf( d.display ) )
							.collect( joining() ) )
					.collect( joining( "\n" ) );
		}
	}

	private static class Segment implements Comparable<Segment> {
		final int row;
		final int column;
		final Direction direction;

		Segment( int row, int column, Direction direction ) {
			this.row = row;
			this.column = column;
			this.direction = direction;
		}

		@Override
		public int compareTo( Segment o ) {
			int d = row - o.row;
			if( d == 0 ) {
				d = column - o.column;
				if( d == 0 ) {
					d = direction.ordinal() - o.direction.ordinal();
				}
			}
			return d;
		}

		@Override
		public boolean equals( Object obj ) {
			if( obj instanceof Segment ) {
				Segment s = (Segment) obj;
				return compareTo( s ) == 0;
			}
			return false;
		}

		@Override
		public int hashCode() {
			return toString().hashCode();
		}

		@Override
		public String toString() {
			return direction.display + "[" + row + "," + column + "]";
		}
	}

	private enum Device {
		EMPTY('.', '░', new Direction[][] {
				{ Direction.N },
				{ Direction.E },
				{ Direction.S },
				{ Direction.W } }),
		MIRROR_DOWN('\\', '╲', new Direction[][] {
				{ Direction.W },
				{ Direction.S },
				{ Direction.E },
				{ Direction.N } }),
		MIRROR_UP('/', '╱', new Direction[][] {
				{ Direction.E },
				{ Direction.N },
				{ Direction.W },
				{ Direction.S } }),
		SPLITTER_VERT('|', '│', new Direction[][] {
				{ Direction.N },
				{ Direction.N, Direction.S },
				{ Direction.S },
				{ Direction.N, Direction.S } }),
		SPLITTER_HORZ('-', '─', new Direction[][] {
				{ Direction.E, Direction.W },
				{ Direction.E },
				{ Direction.E, Direction.W },
				{ Direction.W } });

		final char input;
		final char display;
		final Direction[][] effect;

		Device( char input, char display, Direction[][] effect ) {
			this.input = input;
			this.display = display;
			this.effect = effect;
		}

		Direction[] outputs( Direction in ) {
			return effect[in.ordinal()];
		}

		static Device of( char c ) {
			for( Device d : values() ) {
				if( d.input == c ) {
					return d;
				}
			}
			throw new IllegalArgumentException( "what is " + c + "?!" );
		}
	}

	private enum Direction {
		N('↑', -1, 0),
		E('→', 0, 1),
		S('↓', 1, 0),
		W('←', 0, -1);

		final char display;
		final int ri;
		final int ci;

		Direction( char display, int ri, int ci ) {
			this.display = display;
			this.ri = ri;
			this.ci = ci;
		}
	}
}
