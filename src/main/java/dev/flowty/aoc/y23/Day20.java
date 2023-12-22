package dev.flowty.aoc.y23;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

class Day20 {

	static int part1( String[] lines ) {
		Network n = new Network( lines );
		for( int i = 0; i < 1000; i++ ) {
			n.button();
		}
		return n.score();
	}

	static int part2( String[] lines ) {
		Network n = new Network( lines );

		Set<FlipFlop> flipFlops = n.modules.values().stream()
				.filter( m -> m instanceof FlipFlop )
				.map( m -> (FlipFlop) m )
				.collect( toSet() );

		flipFlops.stream().sorted( Module.NAME_ORDER ).forEach( f -> System.out.println( f ) );

		Map<FlipFlop, Level> previous = new HashMap<>();
		Map<FlipFlop, List<Integer>> flipIndices = new HashMap<>();
		flipFlops.forEach( m -> {
			previous.put( m, Level.LOW );
			flipIndices.put( m, new ArrayList<>() );
		} );

		int index = 0;
		while( flipIndices.values().stream().anyMatch( l -> l.size() < 2 ) ) {
			n.button();
			index++;
			for( FlipFlop ff : flipFlops ) {
				if( previous.get( ff ) != ff.lastOutput ) {
					flipIndices.get( ff ).add( index );
					previous.put( ff, ff.lastOutput );
				}
			}
		}

		// TODO: monitor the state all the time, not just after the button push has
		// settled

		flipIndices.forEach( ( ff, fi ) -> {
			System.out.println( ff );
			System.out.println(
					fi.get( fi.size() - 1 ) + "\t\t\t" + Integer.bitCount( fi.get( fi.size() - 1 ) ) );
		} );

		return 0;
	}

	static class Network {
		private final Map<String, Module> modules = new TreeMap<>();
		private final Deque<Pulse> pending = new ArrayDeque<>();
		private EnumMap<Level, Integer> pulseCount = new EnumMap<>( Level.class );

		Network( String[] lines ) {
			List<Function<String, Module>> builders = Arrays.asList(
					Broadcaster::offer,
					FlipFlop::offer,
					Conjunction::offer );

			for( String line : lines ) {
				Module m = builders.stream()
						.map( b -> b.apply( line ) )
						.filter( Objects::nonNull )
						.findAny()
						.orElseThrow( () -> new IllegalArgumentException( line ) );

				modules.put( m.name, m );
			}

			for( Module m : modules.values() ) {
				Set<String> inputs = new TreeSet<>();
				for( Module n : modules.values() ) {
					if( n.outputs.contains( m.name ) ) {
						inputs.add( n.name );
					}
				}
				m.registerInputs( inputs );
			}
		}

		void button() {
			pending.add( new Pulse( null, Level.LOW, "broadcaster" ) );
			while( !pending.isEmpty() ) {
				Pulse p = pending.removeFirst();
				pulseCount.compute( p.level, ( l, i ) -> i == null ? 1 : i + 1 );
				Module m = modules.get( p.destination );
				if( m != null ) {
					pending.addAll( m.process( p ) );
				}
			}
		}

		int score() {
			return pulseCount.values().stream()
					.reduce( 1, ( a, b ) -> a * b );
		}

		public Set<Module> getInputs( String name ) {
			Set<Module> inputs = new TreeSet<>( Module.NAME_ORDER );

			for( Module m : modules.values() ) {
				if( m.outputs.contains( name ) ) {
					inputs.add( m );
				}
			}

			return inputs;
		}

		@Override
		public String toString() {
			return modules.values().stream()
					.map( Module::toString )
					.collect( joining( "\n" ) );
		}
	}

	abstract static class Module {
		public static final Comparator<Module> NAME_ORDER = Comparator.comparing( m -> m.name );
		final String name;
		final List<String> outputs;
		Level lastOutput = null;

		Module( String name, List<String> outputs ) {
			this.name = name;
			this.outputs = outputs;

			assert outputs.stream().noneMatch( String::isEmpty ) : name + " " + outputs;
		}

		abstract List<Pulse> process( Pulse input );

		void registerInputs( Set<String> inputs ) {
			// default no-op
		}

		@Override
		public String toString() {
			return getClass().getSimpleName()
					+ " " + name
					+ " " + lastOutput
					+ " to " + outputs;
		}
	}

	static class Broadcaster extends Module {

		private Broadcaster( List<String> outputs ) {
			super( "broadcaster", outputs );
		}

		static Broadcaster offer( String line ) {
			Matcher m = Pattern.compile( "broadcaster -> (.*)" ).matcher( line );
			if( m.matches() ) {
				return new Broadcaster( Stream.of( m.group( 1 ).split( "[^a-z]" ) )
						.filter( s -> !s.isEmpty() )
						.toList() );
			}
			return null;
		}

		@Override
		public List<Pulse> process( Pulse input ) {
			lastOutput = input.level;
			return outputs.stream()
					.map( o -> new Pulse( name, lastOutput, o ) )
					.toList();
		}
	}

	static class FlipFlop extends Module {
		private boolean on = false;

		private FlipFlop( String name, List<String> outputs ) {
			super( name, outputs );
		}

		static FlipFlop offer( String line ) {
			Matcher m = Pattern.compile( "%([a-z]+) -> (.*)" ).matcher( line );
			if( m.matches() ) {
				return new FlipFlop(
						m.group( 1 ),
						Stream.of( m.group( 2 ).split( "[^a-z]" ) )
								.filter( s -> !s.isEmpty() )
								.toList() );
			}
			return null;
		}

		@Override
		public List<Pulse> process( Pulse input ) {
			if( input.level == Level.HIGH ) {
				return Collections.emptyList();
			}
			on = !on;
			lastOutput = on ? Level.HIGH : Level.LOW;
			return outputs.stream()
					.map( o -> new Pulse( name, lastOutput, o ) )
					.toList();
		}
	}

	static class Conjunction extends Module {
		private final Map<String, Level> inputs = new TreeMap<>();

		private Conjunction( String name, List<String> outputs ) {
			super( name, outputs );
		}

		static Conjunction offer( String line ) {
			Matcher m = Pattern.compile( "&([a-z]+) -> (.*)" ).matcher( line );
			if( m.matches() ) {
				return new Conjunction(
						m.group( 1 ),
						Stream.of( m.group( 2 ).split( "[^a-z]" ) )
								.filter( s -> !s.isEmpty() )
								.toList() );
			}
			return null;
		}

		@Override
		void registerInputs( Set<String> inputs ) {
			inputs.forEach( i -> this.inputs.put( i, Level.LOW ) );
		}

		@Override
		public List<Pulse> process( Pulse input ) {
			inputs.put( input.source, input.level );
			lastOutput = inputs.values().stream()
					.allMatch( l -> l == Level.HIGH )
							? Level.LOW
							: Level.HIGH;
			return outputs.stream()
					.map( o -> new Pulse( name, lastOutput, o ) )
					.toList();
		}
	}

	record Pulse(String source, Level level, String destination) {
	}

	enum Level {
		HIGH, LOW
	}
}
