package dev.flowty.aoc.y15;

import static java.util.stream.Collectors.toSet;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;

public class Day03 {

	static int part1( String line ) {
		return new Santa().moves( line ).visits.size();
	}

	static int part2( String line ) {

		Santa[] santas = { new Santa(), new Santa() };

		for( int i = 0; i < line.length(); i++ ) {
			santas[i % santas.length].move( line.charAt( i ) );
		}

		return Stream.of( santas )
				.map( Santa::visits )
				.flatMap( Set::stream )
				.collect( toSet() )
				.size();
	}

	static class Santa {
		int x = 0;
		int y = 0;

		final Map<String, Integer> visits = new TreeMap<>();

		Santa() {
			visits.compute( x + "," + y, ( k, v ) -> v == null ? 1 : v + 1 );
		}

		Santa moves( String dirs ) {
			for( int i = 0; i < dirs.length(); i++ ) {
				move( dirs.charAt( i ) );
			}
			return this;
		}

		Santa move( char dir ) {
			if( dir == '<' ) {
				x--;
			}
			else if( dir == '>' ) {
				x++;
			}
			else if( dir == '^' ) {
				y++;
			}
			else if( dir == 'v' ) {
				y--;
			}
			else {
				assert false : dir;
			}
			visits.compute( x + "," + y, ( k, v ) -> v == null ? 1 : v + 1 );
			return this;
		}

		Set<String> visits() {
			return visits.keySet();
		}

	}
}
