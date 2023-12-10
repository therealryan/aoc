package dev.flowty.aoc.y23;

import static java.util.stream.Collectors.joining;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

class Day10 {

	static int part1( String[] lines ) {
		Grid g = new Grid( lines ).link();
		Node src = g.flow( 'S' );
		return g.equidistance( src );
	}

	static int part2( String[] lines ) {
		Grid g = new Grid( lines ).link();
		g.flow( 'S' );
		return g.enclosedByLoop();
	}

	static class Grid {
		private final Node[][] nodes;

		Grid( String[] lines ) {
			nodes = new Node[lines.length][];
			for( int i = 0; i < lines.length; i++ ) {
				nodes[i] = new Node[lines[i].length()];
				for( int j = 0; j < nodes[i].length; j++ ) {
					nodes[i][j] = new Node( lines[i].charAt( j ) );
				}
			}
		}

		Grid link() {
			for( int i = 0; i < nodes.length; i++ ) {
				for( int j = 0; j < nodes[i].length; j++ ) {
					nodes[i][j].link(
							at( i - 1, j ),
							at( i, j + 1 ),
							at( i + 1, j ),
							at( i, j - 1 ) );
				}
			}
			return this;
		}

		Node flow( char src ) {
			Node n = find( Pipe.forSource( src ) );
			Deque<Flood> next = new ArrayDeque<>( n.flow( n, n, 0 ) );
			while( !next.isEmpty() ) {
				Flood f = next.removeFirst();
				next.addAll( f.to.flow( n, f.from, f.distance ) );
			}
			return n;
		}

		Node at( int row, int column ) {
			if( row < 0 || row >= nodes.length
					|| column < 0 || column >= nodes[0].length ) {
				return new Node( '.' );
			}
			return nodes[row][column];
		}

		Node find( Pipe p ) {
			for( int i = 0; i < nodes.length; i++ ) {
				for( int j = 0; j < nodes[i].length; j++ ) {
					if( nodes[i][j].pipe == p ) {
						return nodes[i][j];
					}
				}
			}
			throw new IllegalArgumentException( "No node for " + p );
		}

		int equidistance( Node src ) {
			for( int i = 0; i < nodes.length; i++ ) {
				for( int j = 0; j < nodes[i].length; j++ ) {
					int eq = nodes[i][j].equidistance( src );
					if( eq != -1 ) {
						return eq;
					}
				}
			}
			return -1;
		}

		int enclosedByLoop() {
			int count = 0;
			for( Node[] row : nodes ) {
				EnumMap<Pipe, Integer> encountered = new EnumMap<>( Pipe.class );

				for( Node node : row ) {
					if( node.isOnLoop() ) {
						encountered.compute(
								node.pipe(),
								( p, v ) -> v == null ? 1 : v + 1 );
					}
					else {
						int winding = encountered.getOrDefault( Pipe.NS, 0 )
								+ Math.min(
										encountered.getOrDefault( Pipe.NE, 0 ),
										encountered.getOrDefault( Pipe.SW, 0 ) )
								+ Math.min(
										encountered.getOrDefault( Pipe.NW, 0 ),
										encountered.getOrDefault( Pipe.SE, 0 ) );

						if( winding % 2 == 1 ) {
							node.enclosed = true;
							count++;
						}
					}
				}
			}
			return count;
		}

		@Override
		public String toString() {
			return Stream.of( nodes )
					.map( row -> Stream.of( row )
							.map( Node::toString )
							.collect( joining() ) )
					.collect( joining( "\n" ) );
		}
	}

	static class Node {
		final Pipe pipe;
		Node north;
		Node east;
		Node south;
		Node west;

		Map<Node, Map<Node, Integer>> distances = new HashMap<>();
		boolean enclosed = false;

		Node( char c ) {
			pipe = Pipe.forSource( c );
		}

		void link( Node n, Node e, Node s, Node w ) {
			if( pipe.north && n.pipe.south ) {
				north = n;
			}
			if( pipe.east && e.pipe.west ) {
				east = e;
			}
			if( pipe.south && s.pipe.north ) {
				south = s;
			}
			if( pipe.west && w.pipe.east ) {
				west = w;
			}
		}

		List<Flood> flow( Node source, Node from, int srcDistance ) {

			boolean proceed;
			if( distances.containsKey( source ) ) {
				Map<Node, Integer> nd = distances.get( source );
				if( nd.containsKey( from ) ) {
					proceed = false;
				}
				else {
					nd.put( from, srcDistance );
					proceed = true;
				}
			}
			else {
				distances.computeIfAbsent( source, s -> new HashMap<>() )
						.put( from, srcDistance );
				proceed = true;
			}

			if( proceed ) {
				List<Flood> next = new ArrayList<>();
				if( north != null && north != from ) {
					next.add( new Flood( this, north, srcDistance + 1 ) );
				}
				if( east != null && east != from ) {
					next.add( new Flood( this, east, srcDistance + 1 ) );
				}
				if( south != null && south != from ) {
					next.add( new Flood( this, south, srcDistance + 1 ) );
				}
				if( west != null && west != from ) {
					next.add( new Flood( this, west, srcDistance + 1 ) );
				}
				return next;
			}
			return Collections.emptyList();
		}

		boolean hasDistance( Node src ) {
			return distances.containsKey( src );
		}

		int equidistance( Node src ) {
			Map<Node, Integer> nd = distances.get( src );
			if( nd != null && nd.size() == 2 ) {
				Set<Integer> d = new TreeSet<>( nd.values() );
				if( d.size() == 1 ) {
					return d.iterator().next();
				}
			}
			return -1;
		}

		boolean isOnLoop() {
			return distances.values().stream()
					.anyMatch( m -> m.size() > 1 );
		}

		Pipe pipe() {
			if( pipe == Pipe.S ) {
				if( north != null && north.isOnLoop() ) {
					if( east != null && east.isOnLoop() ) {
						return Pipe.NE;
					}
					if( south != null && south.isOnLoop() ) {
						return Pipe.NS;
					}
					if( west != null && west.isOnLoop() ) {
						return Pipe.NW;
					}
				}
				if( east != null && east.isOnLoop() ) {
					if( south.isOnLoop() ) {
						return Pipe.SE;
					}
					if( west != null && west.isOnLoop() ) {
						return Pipe.EW;
					}
				}
				if( south != null && south.isOnLoop() ) {
					if( west != null && west.isOnLoop() ) {
						return Pipe.SW;
					}
				}
				throw new IllegalStateException( "???" );
			}
			return pipe;
		}

		@Override
		public String toString() {
			if( isOnLoop() ) {
				return String.valueOf( pipe.looped );
			}
			if( enclosed ) {
				return "▓";
			}
			return String.valueOf( pipe.connected );
		}
	}

	static class Flood {
		Flood( Node from, Node to, int distance ) {
			this.from = from;
			this.to = to;
			this.distance = distance;
		}

		final Node from;
		final Node to;
		final int distance;
	}

	enum Pipe {
		NS('|', '│', '║', true, false, true, false),
		EW('-', '─', '═', false, true, false, true),
		NE('L', '└', '╚', true, true, false, false),
		NW('J', '┘', '╝', true, false, false, true),
		SW('7', '┐', '╗', false, false, true, true),
		SE('F', '┌', '╔', false, true, true, false),
		G('.', '░', '░', false, false, false, false),
		S('S', 'S', 'S', true, true, true, true);

		public final char source;
		public final char connected;
		public final char looped;

		public final boolean north;
		public final boolean east;
		public final boolean south;
		public final boolean west;

		Pipe( char source, char connected, char looped,
				boolean north, boolean east, boolean south, boolean west ) {
			this.source = source;
			this.connected = connected;
			this.looped = looped;
			this.north = north;
			this.east = east;
			this.south = south;
			this.west = west;
		}

		static Pipe forSource( char c ) {
			for( Pipe p : values() ) {
				if( c == p.source ) {
					return p;
				}
			}
			throw new IllegalArgumentException( "Bad pipe input " + c );
		}
	}
}
