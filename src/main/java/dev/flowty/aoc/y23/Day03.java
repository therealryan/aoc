package dev.flowty.aoc.y23;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Day03 {

	static int part1( String[] lines ) {
		return new Grid( lines ).numbers.numbers.stream()
				.mapToInt( Number::partNumber )
				.sum();
	}

	static int part2( String[] lines ) {
		return new Grid( lines ).parts.parts.stream()
				.mapToInt( Part::gearRatio )
				.sum();

	}

	private static class Grid {
		final NumberGrid numbers;
		final PartGrid parts;

		Grid( String[] lines ) {
			numbers = new NumberGrid( lines );
			parts = new PartGrid( lines );

			for( Part part : parts.parts ) {
				for( int i = -1; i < 2; i++ ) {
					for( int j = -1; j < 2; j++ ) {
						Number n = numbers.get( part.rowIndex + i, part.columnIndex + j );
						if( n != null ) {
							n.adjacent.add( part );
							part.adjacent.add( n );
						}
					}
				}
			}
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for( int i = 0; i < numbers.grid.length; i++ ) {
				for( int j = 0; j < numbers.grid[i].length; j++ ) {
					Number n = numbers.get( i, j );
					Part p = parts.get( i, j );
					if( n != null ) {
						sb.append( n.get( i, j ) );
					}
					else if( p != null ) {
						sb.append( p.get( i, j ) );
					}
					else {
						sb.append( '.' );
					}
				}
				sb.append( "\n" );
			}
			sb.append( "Numbers" );
			numbers.numbers.forEach( n -> sb.append( "\n  " ).append( n ) );
			sb.append( "\nParts" );
			parts.parts.forEach( p -> sb.append( "\n  " ).append( p ) );
			return sb.toString();
		}
	}

	private static class NumberGrid {
		final List<Number> numbers = new ArrayList<>();
		final Number[][] grid;

		NumberGrid( String[] lines ) {
			grid = new Number[lines.length][lines[0].length()];
			for( int i = 0; i < lines.length; i++ ) {
				for( int j = 0; j < lines[i].length(); j++ ) {
					char c = lines[i].charAt( j );
					if( Character.isDigit( c ) ) {
						StringBuilder num = new StringBuilder().append( c );
						while( j < lines[i].length() - 1 && Character.isDigit( lines[i].charAt( j + 1 ) ) ) {
							num.append( lines[i].charAt( j + 1 ) );
							j++;
						}
						Number n = new Number( Integer.parseInt( num.toString() ),
								i, j - num.length() + 1, num.length() );
						numbers.add( n );
						for( int k = 0; k < n.length; k++ ) {
							grid[i][j - k] = n;
						}
					}
				}
			}
		}

		public Number get( int row, int column ) {
			if( row < 0 || row > grid.length - 1
					|| column < 0 || column > grid[row].length - 1 ) {
				return null;
			}
			return grid[row][column];
		}
	}

	private static class PartGrid {
		final List<Part> parts = new ArrayList<>();
		final Part[][] grid;

		public PartGrid( String[] lines ) {
			grid = new Part[lines.length][lines[0].length()];
			for( int i = 0; i < lines.length; i++ ) {
				for( int j = 0; j < lines[i].length(); j++ ) {
					char c = lines[i].charAt( j );
					if( c != '.' && !Character.isDigit( c ) ) {
						grid[i][j] = new Part( c, i, j );
						parts.add( grid[i][j] );
					}
				}
			}
		}

		public Part get( int row, int column ) {
			if( row < 0 || row > grid.length - 1
					|| column < 0 || column > grid[row].length - 1 ) {
				return null;
			}
			return grid[row][column];
		}
	}

	private static class Number {
		final int value;
		final int rowIndex;
		final int columnIndex;
		final int length;
		final Set<Part> adjacent = new HashSet<>();

		Number( int value, int rowIndex, int columnIndex, int length ) {
			this.value = value;
			this.rowIndex = rowIndex;
			this.columnIndex = columnIndex;
			this.length = length;
		}

		Character get( int row, int column ) {
			if( row == rowIndex && columnIndex <= column && columnIndex < column + length ) {
				return String.valueOf( value ).charAt( column - columnIndex );
			}
			return null;
		}

		int partNumber() {
			if( !adjacent.isEmpty() ) {
				return value;
			}
			return 0;
		}

		@Override
		public String toString() {
			return value + "[" + rowIndex + "," + columnIndex + "] "
					+ adjacent.stream()
							.map( a -> String.valueOf( a.symbol ) )
							.collect( joining( "," ) );
		}
	}

	private static class Part {
		final char symbol;
		final int rowIndex;
		final int columnIndex;
		final Set<Number> adjacent = new HashSet<>();

		Part( char symbol, int rowIndex, int columnIndex ) {
			this.symbol = symbol;
			this.rowIndex = rowIndex;
			this.columnIndex = columnIndex;
		}

		Character get( int row, int column ) {
			if( row == rowIndex && column == columnIndex ) {
				return symbol;
			}
			return null;
		}

		int gearRatio() {
			if( symbol == '*' && adjacent.size() == 2 ) {
				int r = 1;
				for( Number number : adjacent ) {
					r *= number.value;
				}
				return r;
			}
			return 0;
		}

		@Override
		public String toString() {
			return symbol + "[" + rowIndex + "," + columnIndex + "] "
					+ adjacent.stream()
							.map( a -> String.valueOf( a.value ) )
							.collect( joining( "," ) );
		}
	}
}
