package dev.flowty.aoc.y23;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Day01 {

	static int part1( String[] lines ) {
		int sum = 0;

		for( String line : lines ) {
			List<Integer> digits = new ArrayList<>();
			for( int i = 0; i < line.length(); i++ ) {
				if( Character.isDigit( line.charAt( i ) ) ) {
					digits.add( Character.digit( line.charAt( i ), 10 ) );
				}
			}
			int value = digits.get( 0 ) * 10 + digits.get( digits.size() - 1 );
			sum += value;
		}

		return sum;
	}

	private static final Map<String, Integer> VALUES = new HashMap<>();
	static {
		String[] nums = {
				"zero", "one", "two", "three", "four",
				"five", "six", "seven", "eight", "nine" };
		for( int i = 0; i < nums.length; i++ ) {
			VALUES.put( String.valueOf( i ), i );
			VALUES.put( nums[i], i );
		}
	}

	static int part2( String[] lines ) {
		int sum = 0;

		for( String line : lines ) {
			int value = 10 * first( line ) + last( line );
			sum += value;
		}

		return sum;
	}

	private static int first( String line ) {
		int idx = line.length() + 1;
		String first = null;

		for( String value : VALUES.keySet() ) {
			int i = line.indexOf( value );
			if( i >= 0 && i < idx ) {
				idx = i;
				first = value;
			}
		}

		return VALUES.get( first );
	}

	private static int last( String line ) {
		int idx = -1;
		String last = null;

		for( String value : VALUES.keySet() ) {
			int i = line.lastIndexOf( value );
			if( i >= 0 && i > idx ) {
				idx = i;
				last = value;
			}
		}

		return VALUES.get( last );
	}
}
