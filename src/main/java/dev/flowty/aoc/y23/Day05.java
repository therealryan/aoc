package dev.flowty.aoc.y23;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.LongStream;

public class Day05 {

	static long part1( String[] lines ) {
		return new Pipeline( lines )
				.process( "seed" )
				.min()
				.orElseThrow( () -> new IllegalStateException( "no outputs!" ) );
	}

	private static class Pipeline {
		private List<Source> sources = new ArrayList<>();
		private Map<String, Mapping> mappings = new TreeMap<>();

		public Pipeline( String[] lines ) {
			Mapping active = null;

			for( String line : lines ) {
				Source s = Source.offer( line );
				if( s != null ) {
					sources.add( s );
					continue;
				}

				Range r = Range.offer( line );
				if( r != null ) {
					active.ranges.add( r );
					continue;
				}

				active = Mapping.offer( line );
				if( active != null ) {
					mappings.put( active.from, active );
				}
			}
		}

		LongStream process( String inputs ) {
			return sources.stream()
					.filter( s -> inputs.equals( s.name ) )
					.flatMapToLong( s -> LongStream.of( s.ids ) )
					.map( in -> process( inputs, in ) );
		}

		long process( String type, long value ) {
			String currentType = type;
			long currentValue = value;

			while( mappings.containsKey( currentType ) ) {
				Mapping m = mappings.get( currentType );
				currentType = m.to;
				currentValue = m.map( currentValue );
			}

			return currentValue;
		}

		@Override
		public String toString() {
			return sources + "\n" + mappings.values().stream()
					.map( Mapping::toString )
					.collect( joining( "\n" ) );
		}
	}

	private static class Source {
		final String name;
		final long[] ids;

		private Source( String name, String[] ids ) {
			this.name = name;
			this.ids = new long[ids.length];
			for( int i = 0; i < ids.length; i++ ) {
				this.ids[i] = Long.parseLong( ids[i] );
			}
		}

		static Source offer( String line ) {
			Matcher m = Pattern.compile( "(\\w+)s: ([\\d ]+)" ).matcher( line );

			if( !m.matches() ) {
				return null;
			}
			return new Source( m.group( 1 ), m.group( 2 ).split( " " ) );
		}

		@Override
		public String toString() {
			return name + "[s] " + Arrays.toString( ids );
		}
	}

	private static class Mapping {
		final String from;
		final String to;
		final Set<Range> ranges = new TreeSet<>( Comparator.comparing( r -> r.fromStart ) );

		private Mapping( String from, String to ) {
			this.from = from;
			this.to = to;
		}

		static Mapping offer( String line ) {
			Matcher m = Pattern.compile( "(\\w+)-to-(\\w+) map:" ).matcher( line );
			if( !m.matches() ) {
				return null;
			}
			return new Mapping( m.group( 1 ), m.group( 2 ) );
		}

		long map( long in ) {
			for( Range range : ranges ) {
				if( range.includes( in ) ) {
					return range.map( in );
				}
			}
			return in;
		}

		@Override
		public String toString() {
			return from + " -> " + to + ranges.stream()
					.map( r -> "\n  " + r )
					.collect( joining() );
		}
	}

	private static class Range {
		final long fromStart;
		final long toStart;
		final long size;

		private Range( long fromStart, long toStart, long size ) {
			this.fromStart = fromStart;
			this.toStart = toStart;
			this.size = size;
		}

		static Range offer( String line ) {
			Matcher m = Pattern.compile( "(\\d+) (\\d+) (\\d+)" ).matcher( line );
			if( !m.matches() ) {
				return null;
			}
			return new Range(
					Long.parseLong( m.group( 2 ) ),
					Long.parseLong( m.group( 1 ) ),
					Long.parseLong( m.group( 3 ) ) );
		}

		boolean includes( long in ) {
			return fromStart <= in && in < fromStart + size;
		}

		long map( long in ) {
			return in - fromStart + toStart;
		}

		@Override
		public String toString() {
			return fromStart + " " + toStart + " " + size;
		}
	}
}
