package dev.flowty.aoc.y23;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

	static int part2( String[] lines ) {
		return 0;
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
}
