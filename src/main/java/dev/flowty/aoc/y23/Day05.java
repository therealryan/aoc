package dev.flowty.aoc.y23;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Day05 {

	static long part1( String[] lines ) {
		return new Pipeline( lines )
				.process( "seed" )
				.min()
				.orElseThrow( () -> new IllegalStateException( "no outputs!" ) );
	}

	static long part2( String[] lines ) {
		return new Pipeline( lines )
				.processRanges( "seed" )
				.sorted()
				.findFirst()
				.orElseThrow( () -> new IllegalStateException( "no outputs!" ) ).min;
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

				MappedRange r = MappedRange.offer( line );
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
					.flatMapToLong( Source::ids )
					.map( in -> process( inputs, in ) );
		}

		Stream<Range> processRanges( String inputs ) {
			return sources.stream()
					.filter( s -> inputs.equals( s.name ) )
					.flatMap( Source::ranges )
					.map( in -> process( inputs, in ) )
					.flatMap( Set::stream );
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

		Set<Range> process( String type, Range value ) {
			String currentType = type;
			TreeSet<Range> currentValue = Stream.of( value )
					.collect( toCollection( TreeSet::new ) );

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

		public LongStream ids() {
			return LongStream.of( ids );
		}

		public Stream<Range> ranges() {
			List<Range> ls = new ArrayList<>();
			for( int i = 0; i < ids.length; i += 2 ) {
				ls.add( new Range( ids[i], ids[i] + ids[i + 1] ) );
			}
			return ls.stream();
		}

		@Override
		public String toString() {
			return name + "[s] " + Arrays.toString( ids );
		}
	}

	private static class Mapping {
		final String from;
		final String to;
		final Set<MappedRange> ranges = new TreeSet<>( Comparator.comparing( r -> r.fromStart ) );

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
			for( MappedRange range : ranges ) {
				if( range.includes( in ) ) {
					return range.map( in );
				}
			}
			return in;
		}

		TreeSet<Range> map( TreeSet<Range> in ) {

			// split the input range so they exactly fit (or don't) the mapped ranges
			TreeSet<Range> split = new TreeSet<>( in );
			for( MappedRange mr : ranges ) {
				TreeSet<Range> s = new TreeSet<>();
				for( Range range : split ) {
					s.addAll( mr.splitOnInput( range ) );
				}
				split = s;
			}

			// now map those ranges
			TreeSet<Range> mapped = new TreeSet<>();

			for( Range range : split ) {
				Range tx = null;
				for( MappedRange mr : ranges ) {
					if( mr.includes( range ) ) {
						assert tx == null;
						tx = mr.map( range );
					}
				}

				if( tx == null ) {
					tx = range;
				}

				mapped.add( tx );
			}

			return Range.consolidate( mapped );
		}

		@Override
		public String toString() {
			return from + " -> " + to + ranges.stream()
					.map( r -> "\n  " + r )
					.collect( joining() );
		}
	}

	static class MappedRange {
		final long fromStart;
		final long toStart;
		final long size;

		MappedRange( long fromStart, long toStart, long size ) {
			this.fromStart = fromStart;
			this.toStart = toStart;
			this.size = size;
		}

		static MappedRange offer( String line ) {
			Matcher m = Pattern.compile( "(\\d+) (\\d+) (\\d+)" ).matcher( line );
			if( !m.matches() ) {
				return null;
			}
			return new MappedRange(
					Long.parseLong( m.group( 2 ) ),
					Long.parseLong( m.group( 1 ) ),
					Long.parseLong( m.group( 3 ) ) );
		}

		boolean includes( long in ) {
			return fromStart <= in && in < fromStart + size;
		}

		boolean includes( Range r ) {
			return fromStart <= r.min && r.max < fromStart + size;
		}

		long map( long in ) {
			return in - fromStart + toStart;
		}

		Range map( Range in ) {
			return new Range( map( in.min ), map( in.max ) );
		}

		Set<Range> splitOnInput( Range r ) {
			Set<Range> out = new TreeSet<>();

			if( r.max < fromStart || r.min >= fromStart + size ) {
				// no intersection
				out.add( r );
			}
			else if( fromStart <= r.min && r.max < fromStart + size ) {
				// range is encompassed
				out.add( new Range( r.min, r.max ) );
			}
			else if( r.min < fromStart && fromStart + size - 1 < r.max ) {
				// range encompasses
				out.add( new Range( r.min, fromStart - 1 ) );
				out.add( new Range( fromStart, fromStart + size - 1 ) );
				out.add( new Range( fromStart + size, r.max ) );
			}
			else if( r.min < fromStart && r.max < fromStart + size ) {
				// intersection lower
				out.add( new Range( r.min, fromStart - 1 ) );
				out.add( new Range( fromStart, r.max ) );
			}
			else if( fromStart <= r.min && fromStart + size - 1 < r.max ) {
				// intersection upper
				out.add( new Range( r.min, fromStart + size - 1 ) );
				out.add( new Range( fromStart + size, r.max ) );
			}
			else {
				throw new IllegalStateException( "unhandled! " + r + " on " + toString() );
			}

			return out;
		}

		@Override
		public String toString() {
			return fromStart + " " + toStart + " " + size;
		}
	}

	static class Range implements Comparable<Range> {
		final long min;
		final long max;

		Range( long min, long max ) {
			this.min = min;
			this.max = max;
		}

		Range encompass( Range r ) {
			return new Range( Math.min( min, r.min ), Math.max( max, r.max ) );
		}

		boolean intersects( Range r ) {
			return !(min > r.max || max < r.min);
		}

		static TreeSet<Range> consolidate( TreeSet<Range> ranges ) {
			TreeSet<Range> consolidated = new TreeSet<>();

			Iterator<Range> i = ranges.iterator();
			Range r = i.next();
			while( i.hasNext() ) {
				Range s = i.next();
				if( r.intersects( s ) || r.max == s.min - 1 || r.min == s.max + 1 ) {
					r = r.encompass( s );
				}
				else {
					consolidated.add( r );
					r = s;
				}
			}
			consolidated.add( r );

			return consolidated;
		}

		@Override
		public int compareTo( Range o ) {
			long d = min - o.min;
			if( d == 0 ) {
				d = max - o.max;
			}
			return Long.signum( d );
		}

		@Override
		public boolean equals( Object obj ) {
			if( obj instanceof Range ) {
				Range r = (Range) obj;
				return compareTo( r ) == 0;
			}
			return false;
		}

		@Override
		public String toString() {
			return "[" + min + "-" + max + "]";
		}

		@Override
		public int hashCode() {
			return toString().hashCode();
		}
	}
}
