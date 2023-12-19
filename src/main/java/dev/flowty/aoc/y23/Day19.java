package dev.flowty.aoc.y23;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

class Day19 {

	static int part1( String[] lines ) {
		PartSystem ps = new PartSystem();
		boolean flows = true;

		Map<String, List<Part>> output = new TreeMap<>();

		for( String line : lines ) {
			if( line.isEmpty() ) {
				flows = false;
			}
			else if( flows ) {
				ps.withFlow( line );
			}
			else {
				Part part = new Part( line );
				String result = ps.evaluatePart( part, "in" );
				output.computeIfAbsent( result, k -> new ArrayList<>() ).add( part );
			}
		}

		assert output.size() == 2
				&& output.containsKey( "A" )
				&& output.containsKey( "R" ) : output.keySet().toString();

		return output.get( "A" ).stream()
				.mapToInt( Part::score )
				.sum();
	}

	static long part2( String[] lines ) {
		PartSystem ps = new PartSystem();
		boolean flows = true;

		Map<String, List<Part>> output = new TreeMap<>();

		for( String line : lines ) {
			if( line.isEmpty() ) {
				flows = false;
			}
			else if( flows ) {
				ps.withFlow( line );
			}
			else {
				Part part = new Part( line );
				String result = ps.evaluatePart( part, "in" );
				output.computeIfAbsent( result, k -> new ArrayList<>() ).add( part );
			}
		}

		List<PartRange> accepted = ps.evaluateRange( new PartRange(), "in" );

		assert output.size() == 2
				&& output.containsKey( "A" )
				&& output.containsKey( "R" ) : output.keySet().toString();

		for( Part part : output.get( "A" ) ) {
			accepted.stream()
					.filter( r -> r.contains( part ) )
					.findFirst()
					.orElseThrow( () -> new IllegalStateException(
							"Failed to find range for " + part + " in " + accepted ) );
		}

		return accepted.stream()
				.mapToLong( PartRange::combinations )
				.sum();
	}

	private static class PartSystem {
		Map<String, Workflow> flows = new TreeMap<>();

		PartSystem withFlow( String line ) {
			Workflow wf = new Workflow( line );
			flows.put( wf.name, wf );
			return this;
		}

		String evaluatePart( Part part, String input ) {
			String pipe = input;

			while( flows.containsKey( pipe ) ) {
				pipe = flows.get( pipe ).evaluate( part );
			}

			return pipe;
		}

		List<PartRange> evaluateRange( PartRange pr, String input ) {
			List<PartRange> accepted = new ArrayList<>();

			SortedMap<String, List<PartRange>> pipes = new TreeMap<>();
			pipes.computeIfAbsent( input, k -> new ArrayList<>() ).add( pr );

			while( !pipes.isEmpty() ) {
				System.out.println( pipes );
				Workflow flow = flows.get( pipes.firstKey() );
				List<PartRange> ranges = pipes.remove( pipes.firstKey() );
				for( PartRange range : ranges ) {
					Map<String, List<PartRange>> fo = flow.ranges( range );
					Optional.ofNullable( fo.remove( "A" ) )
							.ifPresent( accepted::addAll );
					fo.remove( "R" );
					fo.forEach( ( k, v ) -> pipes.computeIfAbsent( k, i -> new ArrayList<>() ).addAll( v ) );
				}
			}

			return accepted;
		}
	}

	private static class Workflow {
		private static final Pattern PARSE = Pattern.compile( "(\\w+)\\{(.*),(\\w+?)\\}" );

		private final String name;
		private final List<Rule> rules;
		private final String sink;

		Workflow( String line ) {
			Matcher m = PARSE.matcher( line );
			if( !m.matches() ) {
				throw new IllegalArgumentException( line );
			}
			name = m.group( 1 );
			rules = Stream.of( m.group( 2 ).split( "," ) )
					.map( Rule::new )
					.collect( toList() );
			sink = m.group( 3 );
		}

		String evaluate( Part p ) {
			return rules.stream()
					.map( r -> r.evaluate( p ) )
					.filter( Objects::nonNull )
					.findFirst()
					.orElse( sink );
		}

		Map<String, List<PartRange>> ranges( PartRange in ) {
			System.out.println( "Day19.Workflow.ranges() " + in );
			Map<String, List<PartRange>> out = new TreeMap<>();

			PartRange rem = in;
			for( Rule rule : rules ) {
				out.computeIfAbsent( rule.exit, k -> new ArrayList<>() ).add( rule.pass( rem ) );
				rem = rule.fail( rem );
			}

			out.computeIfAbsent( sink, k -> new ArrayList<>() ).add( rem );

			return out;
		}
	}

	private static class Rule {
		private static final Pattern PARSE = Pattern.compile( "([xmas])([<>])(\\d+):(\\w+)" );

		private final String property;
		private final String comparator;
		private final int value;
		private final String exit;

		Rule( String s ) {
			Matcher m = PARSE.matcher( s );
			if( !m.matches() ) {
				throw new IllegalArgumentException( s );
			}
			property = m.group( 1 );
			comparator = m.group( 2 );
			value = Integer.parseInt( m.group( 3 ) );
			exit = m.group( 4 );
		}

		String evaluate( Part p ) {
			int pv = p.values.get( property );

			if( "<".equals( comparator ) && pv < value ) {
				return exit;
			}

			if( ">".equals( comparator ) && pv > value ) {
				return exit;
			}

			return null;
		}

		PartRange pass( PartRange in ) {
			PartRange pr = new PartRange( in );

			if( "<".equals( comparator ) ) {
				pr.max.values.put( property, value - 1 );
			}

			if( ">".equals( comparator ) ) {
				pr.min.values.put( property, value + 1 );
			}

			System.out.println( "  " + toString() + " pass()\n     in " + in + "\n    out " + pr );
			return pr;
		}

		PartRange fail( PartRange in ) {
			PartRange pr = new PartRange( in );

			if( "<".equals( comparator ) ) {
				pr.min.values.put( property, value );
			}

			if( ">".equals( comparator ) ) {
				pr.max.values.put( property, value );
			}

			System.out.println( "  " + toString() + " fail()\n     in " + in + "\n    out " + pr );
			return pr;
		}

		@Override
		public String toString() {
			return String.format( "%s %s %s : %s", property, comparator, value, exit );
		}
	}

	private static class Part {
		private static final Pattern VALUE = Pattern.compile( "([xmas])=(\\d+)" );
		private final Map<String, Integer> values = new TreeMap<>();

		Part( String line ) {
			Matcher m = VALUE.matcher( line );
			int matched = 0;
			while( m.find() ) {
				values.put( m.group( 1 ), Integer.parseInt( m.group( 2 ) ) );
				matched += m.group().length();
			}

			assert line.length() == matched + 2 + values.size() - 1 : line + " -> " + values;
		}

		Part( Part toCopy ) {
			values.putAll( toCopy.values );
		}

		public int score() {
			return values.values().stream()
					.mapToInt( Integer::intValue )
					.sum();
		}

		@Override
		public String toString() {
			return values.toString();
		}
	}

	private static class PartRange {
		private final Part min;
		private final Part max;

		PartRange() {
			min = new Part( "{x=1,m=1,a=1,s=1}" );
			max = new Part( "{x=4000,m=4000,a=4000,s=4000}" );
		}

		PartRange( PartRange toCopy ) {
			min = new Part( toCopy.min );
			max = new Part( toCopy.max );
		}

		long combinations() {
			return Stream.of( "x", "m", "a", "s" )
					.mapToLong( k -> (max.values.get( k ) - min.values.get( k ) + 1) )
					.reduce( 1, ( a, b ) -> a * b );
		}

		boolean contains( Part p ) {
			return Stream.of( "x", "m", "a", "s" )
					.map( k -> min.values.get( k ) <= p.values.get( k )
							&& p.values.get( k ) <= max.values.get( k ) )
					.reduce( true, ( a, b ) -> a && b );
		}

		@Override
		public String toString() {
			return Stream.of( "x", "m", "a", "s" )
					.map( k -> k + "[" + min.values.get( k ) + "-" + max.values.get( k ) + "]" )
					.collect( joining( "," ) );
		}
	}
}
