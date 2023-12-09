package dev.flowty.aoc.y15;

public class Day01 {

	static int part1( String line ) {
		int floor = 0;

		for( int i = 0; i < line.length(); i++ ) {
			if( line.charAt( i ) == '(' ) {
				floor++;
			}
			else if( line.charAt( i ) == ')' ) {
				floor--;
			}
			else {
				assert false : i + " = " + line.charAt( i );
			}
		}

		return floor;
	}

	static int part2( String line ) {
		int floor = 0;

		for( int i = 0; i < line.length(); i++ ) {
			if( line.charAt( i ) == '(' ) {
				floor++;
			}
			else if( line.charAt( i ) == ')' ) {
				floor--;
			}
			else {
				assert false : i + " = " + line.charAt( i );
			}
			if( floor < 0 ) {
				return i + 1;
			}
		}

		throw new IllegalArgumentException( "no basement entry!" );
	}
}
