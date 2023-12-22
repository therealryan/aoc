package dev.flowty.aoc.y23;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toSet;

import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

class Day22 {

	static int part1( String[] lines ) {
		return new BrickFall( lines )
				.settle()
				.countDisintegratable();
	}

	static int part2( String[] lines ) {
		return new BrickFall( lines )
				.settle()
				.countChainReactionSum();
	}

	static class BrickFall {
		private static final Brick GROUND = new Brick( "0,0,0~9,9,0" );
		private final Brick[] bricks;
		private final int minZ;
		private final int maxZ;

		private final Map<Integer, Map<Integer, SortedSet<Block>>> xyBlocks = new TreeMap<>();

		BrickFall( String[] lines ) {
			bricks = new Brick[lines.length];

			for( int i = 0; i < lines.length; i++ ) {
				bricks[i] = new Brick( lines[i] );
			}

			IntSummaryStatistics ss = Stream.of( bricks ).flatMap( b -> b.blocks.stream() )
					.mapToInt( b -> b.z )
					.summaryStatistics();
			minZ = ss.getMin();
			maxZ = ss.getMax();

			all().forEach( brick -> brick.blocks.stream()
					.forEach( block -> xyBlocks
							.computeIfAbsent( block.x, k -> new TreeMap<>() )
							.computeIfAbsent( block.y, k -> new TreeSet<>() )
							.add( block ) ) );
		}

		BrickFall settle() {

			// iterate in the z-axis
			for( int z = minZ; z <= maxZ; z++ ) {
				final int zPlane = z;

				// find the set of loose bricks that intersect with our z-plane
				Set<Brick> loose = Stream.of( bricks )
						.filter( b -> !b.isSupported() )
						.filter( b -> b.existsAt( zPlane ) )
						.collect( toSet() );

				for( Brick brick : loose ) {
					// Look at the the blocks on the bottom of the brick, find the one that will hit
					// something first as we move it downwards
					int minImpactDistance = Integer.MAX_VALUE;
					for( Block block : brick.lowFace ) {
						SortedSet<Block> column = xyBlocks.get( block.x ).get( block.y );
						Block below = column.headSet( block ).last();
						assert below.brick != brick;
						int id = block.z - below.z;
						if( id < minImpactDistance ) {
							minImpactDistance = id;
						}
					}
					// move every block in the brick down
					int drop = minImpactDistance - 1;
					for( Block block : brick.blocks ) {
						block.z -= drop;
					}

					// look for supports
					for( Block block : brick.lowFace ) {
						SortedSet<Block> column = xyBlocks.get( block.x ).get( block.y );
						Block below = column.headSet( block ).last();
						assert below.brick != brick;
						if( below.z == block.z - 1 ) {
							brick.supportedBy.add( below.brick );
							below.brick.supporting.add( brick );
						}
					}
				}
			}

			return this;
		}

		private Stream<Brick> all() {
			return Stream.concat( Stream.of( GROUND ), Stream.of( bricks ) );
		}

		int countDisintegratable() {
			return (int) Stream.of( bricks )
					.filter( Brick::canBeDisintegrated )
					.count();
		}

		int countChainReactionSum() {
			return Stream.of( bricks )
					.mapToInt( Brick::chainReaction )
					.sum();
		}
	}

	static class Brick {
		final String line;
		final Set<Block> blocks = new TreeSet<>();
		final Set<Brick> supporting = new HashSet<>();
		final Set<Brick> supportedBy = new HashSet<>();
		final Set<Block> lowFace;

		Brick( String line ) {
			this.line = line;
			Matcher m = Pattern.compile( "(\\d+),(\\d+),(\\d+)~(\\d+),(\\d+),(\\d+)" ).matcher( line );
			if( !m.matches() ) {
				throw new IllegalArgumentException( line );
			}

			for( int x = Integer.parseInt( m.group( 1 ) );
					x <= Integer.parseInt( m.group( 4 ) );
					x++ ) {
				for( int y = Integer.parseInt( m.group( 2 ) );
						y <= Integer.parseInt( m.group( 5 ) );
						y++ ) {
					for( int z = Integer.parseInt( m.group( 3 ) );
							z <= Integer.parseInt( m.group( 6 ) );
							z++ ) {
						blocks.add( new Block( this, x, y, z ) );
					}
				}
			}
			Map<Integer, Map<Integer, SortedSet<Block>>> xy = new TreeMap<>();
			blocks.forEach( block -> xy
					.computeIfAbsent( block.x, k -> new TreeMap<>() )
					.computeIfAbsent( block.y, k -> new TreeSet<>() )
					.add( block ) );
			lowFace = xy.values().stream()
					.flatMap( y -> y.values().stream() )
					.map( SortedSet::first )
					.collect( toCollection( TreeSet::new ) );
		}

		boolean existsAt( int z ) {
			return blocks.stream().anyMatch( b -> b.z == z );
		}

		boolean isSupported() {
			return !supportedBy.isEmpty();
		}

		boolean canBeDisintegrated() {
			assert isSupported();
			// everything we support is also supported by something else
			return supporting.stream().allMatch( s -> s.supportedBy.size() > 1 );
		}

		Set<Brick> transitiveSupport( Set<Brick> base ) {
			for( Brick brick : supporting ) {
				if( base.containsAll( brick.supportedBy ) ) {
					base.add( brick );
					brick.transitiveSupport( base );
				}
			}
			return base;
		}

		int chainReaction() {
			Set<Brick> base = new HashSet<>();
			base.add( this );
			return transitiveSupport( base ).size() - 1;
		}
	}

	static class Block implements Comparable<Block> {
		final Brick brick;
		final int x;
		final int y;
		int z;

		Block( Brick brick, int x, int y, int z ) {
			this.brick = brick;
			this.x = x;
			this.y = y;
			this.z = z;
		}

		@Override
		public int compareTo( Block o ) {
			int d = x - o.x;
			if( d == 0 ) {
				d = y - o.y;
			}
			if( d == 0 ) {
				d = z - o.z;
			}
			return d;
		}
	}
}
