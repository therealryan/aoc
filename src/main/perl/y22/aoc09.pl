use 5.014;
use strict;
use warnings;
use Data::Dumper;

my @kx = ();
my @ky = ();
my $knots = 10;

my %visited = ();

my %xMoves = ( R => 1, L => -1, U => 0, D => 0 );
my %yMoves = ( R => 0, L => 0, U => 1, D => -1 );

while ( my $line = <> ) {
	chomp $line;
	
	if ( $line =~ m/([RLUD]) (\d+)/ ) {
		# say "\n$line";
		for ( my $i = 0; $i < $2; $i++ ) {
			move( $xMoves{$1}, $yMoves{$1} );
			$visited{ "$kx[-1],$ky[-1]" } += 1;
			# print_board();
		}
	}
	else {
		say "bad line '$line'";
	}
}T

say "Tail has been in " . scalar( keys %visited ) . " unique positions";

sub move {
	my ( $xi, $yi ) = @_;
	$kx[0] += $xi;
	$ky[0] += $yi;
	
	for ( my $idx = 1; $idx < $knots; $idx++ ) {
		follow( $idx );
	}
}

sub follow {
	my ( $idx ) = @_;
	
	my $fx = $kx[$idx-1];
	my $fy = $ky[$idx-1];
	
	$kx[$idx] //= 0;
	$ky[$idx] //= 0;
	
	my $dx = $fx - $kx[$idx];
	my $dy = $fy - $ky[$idx];
	
	if ( $dx == -2 ) {
		if ( $dy == -2 ) {
			$kx[$idx]--;
			$ky[$idx]--;
		}
		elsif ( $dy == 2 ) {
			$kx[$idx]--;
			$ky[$idx]++;
		}
	}
	elsif ( $dx == 2 ) {
		if ( $dy == -2 ) {
			$kx[$idx]++;
			$ky[$idx]--;
		}
		elsif ( $dy == 2 ) {
			$kx[$idx]++;
			$ky[$idx]++;
		}
	}
	
	$dx = $fx - $kx[$idx];
	$dy = $fy - $ky[$idx];
	
	if( $dx == -2 || $dx == 2 ) {
		$ky[$idx] = $fy;
	}
	elsif( $dy == -2 || $dy == 2 ) {
		$kx[$idx] = $fx;
	}
	
	$dx = $fx - $kx[$idx];
	$dy = $fy - $ky[$idx];
	
	if ( $dx == 0 ) {
		if ( $dy > 1 ) {
			$ky[$idx]++;
		}
		elsif ( $dy < -1 ) {
			$ky[$idx]--;
		}
	}
	elsif ( $dy == 0 ) {
		if ( $dx > 1 ) {
			$kx[$idx]++;
		}
		elsif ( $dx < -1 ) {
			$kx[$idx]--;
		}
	}
	
}

sub print_board {
	my $minx = -20;
	my $miny = -20;
	my $w = 40;
	my $h = 40;

	say "--- ($kx[0],$ky[0])-($kx[-1],$ky[-1]) ---";
	for ( my $y = $miny; $y <= $miny + $h; $y++ ) {
		for ( my $x = $minx; $x <= $minx + $w; $x++ ) {
			my $c = 0;

			for ( my $idx = 1; $idx < scalar @kx; $idx++ ) {
				if ( $x == $kx[$idx] && $y == $ky[$idx] ) {
					$c = $idx;
				}	
			}
			if ( $x == $kx[0] && $y == $ky[0] ) {
				$c = 'H';
			}
			elsif ( $x == $kx[-1] && $y == $ky[-1] ) {
				$c = 'T';
			}
			if( $x == 0 && $y == 0 ) {
				$c = 's';
			}
			
			print $c || ' ';
		}
		print "\n"
	}
	say "--- ". scalar( keys %visited ) ." ---";
	
}