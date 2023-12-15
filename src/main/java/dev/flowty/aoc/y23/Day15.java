package dev.flowty.aoc.y23;

import static java.util.stream.Collectors.joining;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day15 {
	static int part1( String[] lines ) {
		assert lines.length == 1;
		return Stream.of( lines[0].split( "," ) )
				.mapToInt( Day15::hash )
				.sum();
	}

	static long part2( String[] lines ) {
		assert lines.length == 1;
		Line line = new Line();
		Stream.of( lines[0].split( "," ) )
				.forEach( cmd -> line.process( cmd ) );
		return line.focusPower();
	}

	static int hash( String s ) {
		int v = 0;
		for( int i = 0; i < s.length(); i++ ) {
			v += s.charAt( i );
			v *= 17;
			v %= 256;
		}
		return v;
	}

	static class Line {
		final Box[] boxes = new Box[256];

		Line() {
			for( int i = 0; i < boxes.length; i++ ) {
				boxes[i] = new Box( i );
			}
		}

		static final Pattern OPERATION = Pattern.compile( "(\\w+)([-=])(\\d*)" );

		void process( String cmd ) {
			Matcher m = OPERATION.matcher( cmd );
			if( !m.matches() ) {
				throw new IllegalArgumentException( cmd );
			}

			String label = m.group( 1 );
			String op = m.group( 2 );
			String fp = m.group( 3 );

			int bi = hash( label );

			if( "-".equals( op ) ) {
				boxes[bi].remove( label );
			}
			else if( "=".equals( op ) ) {
				boxes[bi].add( label, Integer.parseInt( fp ) );
			}
		}

		public long focusPower() {
			return Stream.of( boxes )
					.mapToLong( Box::focusPower )
					.sum();
		}

		@Override
		public String toString() {
			return Stream.of( boxes )
					.filter( b -> !b.lenses.isEmpty() )
					.map( Box::toString ).collect( joining( "\n" ) );
		}
	}

	static class Box {
		final int id;
		final Deque<Lens> lenses = new ArrayDeque<>();

		Box( int id ) {
			this.id = id;
		}

		void remove( String label ) {
			lenses.removeIf( l -> l.label.equals( label ) );
		}

		void add( String label, int power ) {
			Optional<Lens> e = lenses.stream()
					.filter( l -> l.label.equals( label ) )
					.findFirst();

			if( e.isPresent() ) {
				e.get().focalPower = power;
			}
			else {
				lenses.add( new Lens( label, power ) );
			}
		}

		public long focusPower() {
			long p = 0;
			int slot = 1;
			for( Lens lens : lenses ) {
				p += (id + 1) * slot * lens.focalPower;
				slot++;
			}
			return p;
		}

		@Override
		public String toString() {
			return lenses.stream()
					.map( Lens::toString )
					.collect( joining( " ", "Box " + id + ": ", "" ) );
		}
	}

	static class Lens {
		final String label;
		int focalPower;

		Lens( String label, int focalPower ) {
			this.label = label;
			this.focalPower = focalPower;
		}

		@Override
		public String toString() {
			return "[" + label + " " + focalPower + "]";
		}
	}
}
