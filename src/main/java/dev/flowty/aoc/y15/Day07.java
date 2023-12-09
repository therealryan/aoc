package dev.flowty.aoc.y15;

import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Day07 {

	static int part1( String[] lines ) {
		return new Circuit( lines )
				.signals()
				.get( "a" );
	}

	static int part2( String[] lines ) {
		Circuit c = new Circuit( lines );
		int a = c.signals().get( "a" );
		c.jackAndReset( "b", a );
		return c.signals().get( "a" );
	}

	static class Circuit {
		final Map<String, Wire> wires = new TreeMap<>();
		final List<Component> components = new ArrayList<>();

		public Circuit( String[] lines ) {
			Pattern wireName = Pattern.compile( "-> (\\w+)$" );
			for( String line : lines ) {
				Matcher m = wireName.matcher( line );
				if( !m.find() ) {
					throw new IllegalArgumentException( line );
				}
				wires.put( m.group( 1 ), new Wire( m.group( 1 ) ) );
			}

			List<BiFunction<String, Function<String, Wire>, Component>> builders = Arrays.asList(
					Supply::offer,
					And::offer, Or::offer,
					Not::offer,
					RightShift::offer, LeftShift::offer,
					Connection::offer );

			for( String line : lines ) {
				components.add( builders.stream()
						.map( b -> b.apply( line, this::wire ) )
						.filter( Objects::nonNull )
						.findAny()
						.orElseThrow( () -> new IllegalArgumentException( line ) ) );
			}
		}

		public Circuit jackAndReset( String wire, int signal ) {
			wires.values().forEach( w -> w.signal = null );
			wire( wire ).signal = signal;
			return this;
		}

		Wire wire( String name ) {
			if( wires.containsKey( name ) ) {
				return wires.get( name );
			}
			if( name.matches( "\\d+" ) ) {
				Wire w = new Wire( name );
				Supply s = new Supply( Integer.parseInt( name ), w );
				components.add( s );
				wires.put( name, w );
				return w;
			}
			throw new IllegalArgumentException( name );
		}

		Map<String, Integer> signals() {
			while( wires.values().stream().anyMatch( w -> w.signal == null ) ) {
				components.forEach( Component::process );
			}
			return wires.values().stream().collect( toMap(
					w -> w.name,
					Wire::get,
					( a, b ) -> b,
					TreeMap::new ) );
		}
	}

	static class Wire {
		final String name;
		Integer signal = null;

		Wire( String name ) {
			this.name = name;
		}

		void set( int value ) {
			signal = value & 0xFFFF;
		}

		Integer get() {
			return signal;
		}
	}

	abstract static class Component {
		final Wire output;

		protected Component( Wire output ) {
			this.output = output;
			assert output != null;
		}

		public abstract void process();
	}

	static class Supply extends Component {
		private static final Pattern PATTERN = Pattern.compile( "(\\d+) -> (\\w+)" );
		private int value;

		private Supply( int value, Wire output ) {
			super( output );
			this.value = value;
		}

		static Supply offer( String line, Function<String, Wire> wires ) {
			Matcher m = PATTERN.matcher( line );
			if( m.matches() ) {
				return new Supply( Short.parseShort( m.group( 1 ) ),
						wires.apply( m.group( 2 ) ) );
			}
			return null;
		}

		@Override
		public void process() {
			if( output.get() == null ) {
				output.set( value );
			}
		}
	}

	static class And extends Component {
		private static final Pattern PATTERN = Pattern.compile( "(\\w+) AND (\\w+) -> (\\w+)" );
		private final Wire left, right;

		private And( Wire left, Wire right, Wire output ) {
			super( output );
			this.left = left;
			this.right = right;
		}

		static And offer( String line, Function<String, Wire> wires ) {
			Matcher m = PATTERN.matcher( line );
			if( m.matches() ) {
				return new And(
						wires.apply( m.group( 1 ) ),
						wires.apply( m.group( 2 ) ),
						wires.apply( m.group( 3 ) ) );
			}
			return null;
		}

		@Override
		public void process() {
			if( left.get() != null && right.get() != null && output.get() == null ) {
				output.set( left.get() & right.get() );
			}
		}
	}

	static class Or extends Component {
		private static final Pattern PATTERN = Pattern.compile( "(\\w+) OR (\\w+) -> (\\w+)" );
		private final Wire left, right;

		private Or( Wire left, Wire right, Wire output ) {
			super( output );
			this.left = left;
			this.right = right;
		}

		static Or offer( String line, Function<String, Wire> wires ) {
			Matcher m = PATTERN.matcher( line );
			if( m.matches() ) {
				return new Or(
						wires.apply( m.group( 1 ) ),
						wires.apply( m.group( 2 ) ),
						wires.apply( m.group( 3 ) ) );
			}
			return null;
		}

		@Override
		public void process() {
			if( left.get() != null && right.get() != null && output.get() == null ) {
				output.set( left.get() | right.get() );
			}
		}
	}

	static class Not extends Component {
		private static final Pattern PATTERN = Pattern.compile( "NOT (\\w+) -> (\\w+)" );
		private final Wire input;

		private Not( Wire input, Wire output ) {
			super( output );
			this.input = input;
		}

		static Not offer( String line, Function<String, Wire> wires ) {
			Matcher m = PATTERN.matcher( line );
			if( m.matches() ) {
				return new Not(
						wires.apply( m.group( 1 ) ),
						wires.apply( m.group( 2 ) ) );
			}
			return null;
		}

		@Override
		public void process() {
			if( input.get() != null ) {
				output.set( ~input.get() );
			}
		}
	}

	static class RightShift extends Component {
		private static final Pattern PATTERN = Pattern.compile( "(\\w+) RSHIFT (\\d+) -> (\\w+)" );
		private final Wire input;
		private final int shift;

		private RightShift( Wire input, int shift, Wire output ) {
			super( output );
			this.input = input;
			this.shift = shift;
		}

		static RightShift offer( String line, Function<String, Wire> wires ) {
			Matcher m = PATTERN.matcher( line );
			if( m.matches() ) {
				return new RightShift(
						wires.apply( m.group( 1 ) ),
						Integer.parseInt( m.group( 2 ) ),
						wires.apply( m.group( 3 ) ) );
			}
			return null;
		}

		@Override
		public void process() {
			if( input.get() != null && output.get() == null ) {
				output.set( input.get() >>> shift );
			}
		}
	}

	static class LeftShift extends Component {
		private static final Pattern PATTERN = Pattern.compile( "(\\w+) LSHIFT (\\d+) -> (\\w+)" );
		private final Wire input;
		private final int shift;

		private LeftShift( Wire input, int shift, Wire output ) {
			super( output );
			this.input = input;
			this.shift = shift;
		}

		static LeftShift offer( String line, Function<String, Wire> wires ) {
			Matcher m = PATTERN.matcher( line );
			if( m.matches() ) {
				return new LeftShift(
						wires.apply( m.group( 1 ) ),
						Integer.parseInt( m.group( 2 ) ),
						wires.apply( m.group( 3 ) ) );
			}
			return null;
		}

		@Override
		public void process() {
			if( input.get() != null && output.get() == null ) {
				output.set( input.get() << shift );
			}
		}
	}

	static class Connection extends Component {
		private static final Pattern PATTERN = Pattern.compile( "(\\w+) -> (\\w+)" );
		private final Wire input;

		private Connection( Wire input, Wire output ) {
			super( output );
			this.input = input;
		}

		static Connection offer( String line, Function<String, Wire> wires ) {
			Matcher m = PATTERN.matcher( line );
			if( m.matches() ) {
				return new Connection(
						wires.apply( m.group( 1 ) ),
						wires.apply( m.group( 2 ) ) );
			}
			return null;
		}

		@Override
		public void process() {
			if( input.get() != null && output.get() == null ) {
				output.set( input.get() );
			}
		}
	}
}
