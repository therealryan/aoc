package dev.flowty.aoc.y23;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class Day14 {

	static int part1( String[] lines ) {
		return new Platform( lines )
				.tiltNorth()
				.northLoad();
	}

	static int part2( String[] lines ) {
		return new Platform( lines )
				.loadAfter( 1000000000 );
	}

	static class Platform {
		private final char[][] grid;

		Platform( String[] lines ) {
			grid = new char[lines.length][];
			for( int i = 0; i < lines.length; i++ ) {
				grid[i] = lines[i].toCharArray();
			}
		}

		Integer loadAfter( int spins ) {
			// settle state
			spinCycle( 500 );
			// record loads
			List<Integer> loads = spinCycle( 1000 );
			// find period
			Integer period = findPeriod( loads );
			// predict
			return loads.get( (spins - 500 - 1) % period );
		}

		List<Integer> spinCycle( int reps ) {
			List<Integer> loads = new ArrayList<>();
			for( int i = 0; i < reps; i++ ) {
				tiltNorth();
				tiltWest();
				tiltSouth();
				tiltEast();

				loads.add( northLoad() );
			}
			return loads;
		}

		Integer findPeriod( List<Integer> values ) {
			for( int i = 1; i < values.size() / 2; i++ ) {
				if( isPeriodic( values, i ) ) {
					return i;
				}
			}
			return null;
		}

		boolean isPeriodic( List<Integer> values, int period ) {
			for( int i = 0; i < values.size(); i++ ) {
				if( !values.get( i ).equals( values.get( i % period ) ) ) {
					return false;
				}
			}
			return true;
		}

		Platform tiltNorth() {
			for( int r = 0; r < grid.length; r++ ) {
				for( int c = 0; c < grid[r].length; c++ ) {
					if( grid[r][c] == '.' ) {
						// scan down for a round rock
						for( int s = r + 1; s < grid.length; s++ ) {
							if( grid[s][c] == '#' ) {
								break;
							}
							if( grid[s][c] == 'O' ) {
								grid[r][c] = grid[s][c];
								grid[s][c] = '.';
								break;
							}
						}
					}
				}
			}
			return this;
		}

		void tiltWest() {
			for( char[] row : grid ) {
				for( int c = 0; c < row.length; c++ ) {
					if( row[c] == '.' ) {
						for( int s = c + 1; s < row.length; s++ ) {
							if( row[s] == '#' ) {
								break;
							}
							if( row[s] == 'O' ) {
								row[c] = row[s];
								row[s] = '.';
								break;
							}
						}
					}
				}
			}
		}

		void tiltSouth() {
			for( int r = grid.length - 1; r >= 0; r-- ) {
				for( int c = 0; c < grid[r].length; c++ ) {
					if( grid[r][c] == '.' ) {
						// scan up for a round rock
						for( int s = r - 1; s >= 0; s-- ) {
							if( grid[s][c] == '#' ) {
								break;
							}
							if( grid[s][c] == 'O' ) {
								grid[r][c] = grid[s][c];
								grid[s][c] = '.';
								break;
							}
						}
					}
				}
			}
		}

		void tiltEast() {
			for( char[] row : grid ) {
				for( int c = row.length - 1; c >= 0; c-- ) {
					if( row[c] == '.' ) {
						for( int s = c - 1; s >= 0; s-- ) {
							if( row[s] == '#' ) {
								break;
							}
							if( row[s] == 'O' ) {
								row[c] = row[s];
								row[s] = '.';
								break;
							}
						}
					}
				}
			}
		}

		int northLoad() {
			int load = 0;
			for( int r = 0; r < grid.length; r++ ) {
				for( int c = 0; c < grid[r].length; c++ ) {
					if( grid[r][c] == 'O' ) {
						load += grid.length - r;
					}
				}
			}
			return load;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for( int i = 0; i < grid.length; i++ ) {
				for( int j = 0; j < grid[i].length; j++ ) {
					sb.append( Optional.ofNullable( grid[i][j] ).orElse( ' ' ) );
				}
				sb.append( "\n" );
			}
			return sb.toString();
		}
	}

}
