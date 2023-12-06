package dev.flowty.aoc.y23;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

class Day06 {

	static int part1( String[] lines ) {
		return new Races( lines ).waysToWin()
				.reduce( 1, ( a, b ) -> a * b );
	}

	static int part2( String[] lines ) {
		return new Races( lines )
				.combine()
				.waysToWin();
	}

	private static class Races {
		final List<Race> races = new ArrayList<>();

		Races( String[] lines ) {
			String[] times = lines[0].split( "\\D+" );
			String[] distances = lines[1].split( "\\D+" );
			assert times.length == distances.length;

			for( int i = 0; i < times.length; i++ ) {
				if( !times[i].isEmpty() && !distances[i].isEmpty() ) {
					races.add( new Race( Long.parseLong( times[i] ), Long.parseLong( distances[i] ) ) );
				}
			}
		}

		public IntStream waysToWin() {
			return races.stream()
					.mapToInt( Race::waysToWin );
		}

		public Race combine() {
			StringBuilder time = new StringBuilder();
			StringBuilder dist = new StringBuilder();
			for( Race race : races ) {
				time.append( race.time );
				dist.append( race.distance );
			}

			return new Race(
					Long.parseLong( time.toString() ),
					Long.parseLong( dist.toString() ) );
		}

		@Override
		public String toString() {
			return races.stream()
					.map( Race::toString )
					.collect( joining( "\n" ) );
		}
	}

	private static class Race {
		final long time;
		final long distance;

		Race( long time, long distance ) {
			this.time = time;
			this.distance = distance;
		}

		long[] distances() {
			long[] distances = new long[(int) time];
			for( int i = 0; i < distances.length; i++ ) {
				distances[i] = (time - i) * i;
			}
			return distances;
		}

		int waysToWin() {
			int ways = 0;
			for( long d : distances() ) {
				if( d > distance ) {
					ways++;
				}
			}
			return ways;
		}

		@Override
		public String toString() {
			return String.format( "t=%s d=%s", time, distance );
		}
	}
}
