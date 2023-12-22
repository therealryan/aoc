package dev.flowty.aoc.y23;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Day18 {

	static long part1( String[] lines ) {
		return new Lagoon( lines, false )
				.enclosedArea();
	}

	static long part2( String[] lines ) {
		return new Lagoon( lines, true )
				.enclosedArea();
	}

	record Loc(int row, int column) {
	}

	static class Lagoon {
		final List<Trench> trenches = new ArrayList<>();
		final List<Point> leftCorners = new ArrayList<>();
		final List<Point> rightCorners = new ArrayList<>();

		Lagoon( String[] lines, boolean hexDecode ) {
			Point dig = new Point( 0.5f, 0.5f );
			for( String line : lines ) {
				Trench s = new Trench( dig, line, hexDecode );
				trenches.add( s );
				dig = dig.add( s.direction.vector.scale( s.length ) );
			}

			Trench previous = trenches.get( trenches.size() - 1 );
			for( Trench t : trenches ) {
				leftCorners.add( corner( previous, t, d -> d.left ) );
				rightCorners.add( corner( previous, t, d -> d.right ) );
				previous = t;
			}
		}

		long enclosedArea() {
			return Math.max(
					area( leftCorners ),
					area( rightCorners ) );
		}

		@Override
		public String toString() {
			return String.format( "%s\nleft\n  %s\nright\n  %s",
					trenches.stream()
							.map( Trench::toString )
							.collect( joining( "\n" ) ),
					leftCorners.stream()
							.map( Point::toString )
							.collect( joining( "\n  " ) ),
					rightCorners.stream()
							.map( Point::toString )
							.collect( joining( "\n  " ) ) );
		}
	}

	static class Trench {
		final Point origin;
		final Direction direction;
		final int length;
		final String colour;
		static final Pattern LP = Pattern.compile(
				"([UDLR]) (\\d+) \\(#([0-9a-f]+)\\)" );

		Trench( Point origin, String line, boolean hexDecode ) {
			this.origin = origin;
			Matcher m = LP.matcher( line );
			if( !m.matches() ) {
				throw new IllegalArgumentException( line );
			}

			if( hexDecode ) {
				direction = Direction.values()[Integer.parseInt( m.group( 3 ).substring( 5 ) )];
				length = Integer.parseInt( m.group( 3 ).substring( 0, 5 ), 16 );
				colour = m.group( 2 );
			}
			else {
				direction = Direction.valueOf( m.group( 1 ) );
				length = Integer.parseInt( m.group( 2 ) );
				colour = m.group( 3 );
			}
		}

		@Override
		public String toString() {
			return String.format( "%s to %s %s (#%s)",
					origin,
					origin.add( direction.vector.scale( length ) ),
					length,
					colour );
		}
	}

	static Point corner( Trench a, Trench b,
			Function<Direction, Point> side ) {
		assert !a.direction.parallel( b.direction );

		Point pa = a.origin.add( side.apply( a.direction ) );
		Point pb = b.origin.add( side.apply( b.direction ) );

		return new Point(
				a.direction == Direction.U || a.direction == Direction.D
						? pa.x
						: pb.x,
				a.direction == Direction.L || a.direction == Direction.R
						? pa.y
						: pb.y );
	}

	static long area( List<Point> shape ) {
		assert shape.size() > 1 : shape;
		Iterator<Point> points = shape.iterator();
		Point o = points.next();
		Point p = points.next();
		double area = 0;
		while( points.hasNext() ) {
			Point c = points.next();
			area += o.side( p, c ) * area( o, p, c );
			p = c;
		}
		return Math.abs( Math.round( area ) );
	}

	static double area( Point a, Point b, Point c ) {
		double ab = a.distance( b );
		double bc = b.distance( c );
		double ca = c.distance( a );
		double s = (ab + bc + ca) / 2;
		return Math.sqrt( s * (s - ab) * (s - bc) * (s - ca) );
	}

	record Point(double x, double y) {
		Point add( Point p ) {
			return new Point( x + p.x, y + p.y );
		}

		Point scale( int s ) {
			return new Point( x * s, y * s );
		}

		double distance( Point p ) {
			double dx = x - p.x;
			double dy = y - p.y;
			return Math.sqrt( dx * dx + dy * dy );
		}

		int side( Point a, Point b ) {
			return (int) Math.signum(
					(b.x - a.x) * (y - a.y)
							- (b.y - a.y) * (x - a.x) );
		}
	}

	enum Direction {
		R(new Point( 1, 0 ),
				new Point( 0, 0.5f ),
				new Point( 0, -0.5f )),
		D(new Point( 0, -1 ),
				new Point( 0.5f, 0 ),
				new Point( -0.5f, 0 )),
		L(new Point( -1, 0 ),
				new Point( 0, -0.5f ),
				new Point( 0, 0.5f )),
		U(new Point( 0, 1 ),
				new Point( -0.5f, 0 ),
				new Point( 0.5f, 0 ));

		final Point vector;
		final Point left;
		final Point right;

		Direction( Point vector, Point left, Point right ) {
			this.vector = vector;
			this.left = left;
			this.right = right;
		}

		boolean parallel( Direction d ) {
			return vector.x == 0 && d.vector.x == 0
					|| vector.y == 0 && d.vector.y == 0;
		}
	}
}
