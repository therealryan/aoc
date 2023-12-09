package dev.flowty.aoc.y15;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntUnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Day06 {

	static int part1( String[] lines ) {
		Map<String, IntUnaryOperator> operations = new HashMap<>();
		operations.put( "toggle", b -> b == 1 ? 0 : 1 );
		operations.put( "turn on", b -> 1 );
		operations.put( "turn off", b -> 0 );
		return brightnessAfter( lines, operations );
	}

	static int part2( String[] lines ) {
		Map<String, IntUnaryOperator> operations = new HashMap<>();
		operations.put( "toggle", b -> b + 2 );
		operations.put( "turn on", b -> b + 1 );
		operations.put( "turn off", b -> Math.max( 0, b - 1 ) );
		return brightnessAfter( lines, operations );
	}

	static int brightnessAfter( String[] lines, Map<String, IntUnaryOperator> operations ) {
		Lights l = new Lights();
		for( String i : lines ) {
			l.with( i, operations );
		}
		return l.brightness();
	}

	static class Lights {
		final int[][] grid = new int[1000][1000];
		static final Pattern INSTRUCTION = Pattern.compile(
				"(.*) (\\d+),(\\d+) through (\\d+),(\\d+)" );

		Lights with( String instruction, Map<String, IntUnaryOperator> operations ) {
			Matcher m = INSTRUCTION.matcher( instruction );
			if( !m.matches() ) {
				throw new IllegalArgumentException( instruction );
			}

			IntUnaryOperator bf = operations.get( m.group( 1 ) );

			int xs = Integer.parseInt( m.group( 2 ) );
			int xe = Integer.parseInt( m.group( 4 ) );
			int ys = Integer.parseInt( m.group( 3 ) );
			int ye = Integer.parseInt( m.group( 5 ) );

			for( int i = xs; i <= xe; i++ ) {
				for( int j = ys; j <= ye; j++ ) {
					grid[i][j] = bf.applyAsInt( grid[i][j] );
				}
			}

			return this;
		}

		int brightness() {
			int b = 0;
			for( int i = 0; i < grid.length; i++ ) {
				for( int j = 0; j < grid[i].length; j++ ) {
					b += grid[i][j];
				}
			}
			return b;
		}
	}
}
