package dev.flowty.aoc.y15;

import java.util.stream.IntStream;
import java.util.stream.Stream;

class Day02 {

	static int part1( String... line ) {
		return Stream.of( line )
				.map( Present::new )
				.mapToInt( Present::wrapArea )
				.sum();
	}

	static int part2( String... line ) {
		return Stream.of( line )
				.map( Present::new )
				.mapToInt( Present::ribbonLength )
				.sum();
	}

	static class Present {
		final int w, h, l;

		Present( String line ) {
			String[] dims = line.split( "x" );
			assert dims.length == 3;
			w = Integer.parseInt( dims[0] );
			h = Integer.parseInt( dims[1] );
			l = Integer.parseInt( dims[2] );
		}

		int wrapArea() {
			int a = w * h;
			int b = w * l;
			int c = h * l;

			return 2 * a + 2 * b + 2 * c + IntStream.of( a, b, c ).min().getAsInt();
		}

		int ribbonLength() {
			int a = 2 * (w + h);
			int b = 2 * (w + l);
			int c = 2 * (h + l);

			int v = w * h * l;

			return v + IntStream.of( a, b, c ).min().getAsInt();
		}
	}
}
