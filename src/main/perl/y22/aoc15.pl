use 5.014;
use strict;
use warnings;
use Data::Dumper;
$Data::Dumper::Sortkeys = 1;

my @pairs = ();
my $range;

while ( my $line = <> ) {
	chomp $line;
	if( $line =~ m/Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)/ ) {
		my $pair = { s => { x => $1, y => $2 }, b => { x => $3, y => $4 } };
		$range = encompass( $range, $pair->{s} );
		$range = encompass( $range, $pair->{b} );
		push @pairs, $pair;
	}
	else {
		say "bad line '$line'";
	}
}

say Dumper $range;

#say part2( 0, 0, 20, 20 );
say "the answer is " . part2( 0, 0, 4000000, 4000000 );

sub part2 {
	my ( $minx, $miny, $maxx, $maxy ) = @_;
	
	my $c = 0;
	for( my $y = $miny; $y <= $maxy; $y++ ) {
		say "y=$y" if( ( $y % 10000 ) == 0 );
		for( my $x = $minx; $x <= $maxx; $x++ ) {
			$c++;
			my ( $v, $pair ) = whats_at( { x => $x, y => $y } );
			if ( $v eq '.' ) {
				say "checked $c";
				return 4000000 * $x + $y;
			}
			else {
				my $r = dist( $pair->{b}, $pair->{s} );
				my $yd = abs( $y - $pair->{s}->{y} );
				my $xr = $r - $yd;
				if( $xr < 0 ){
					die "???";
				}
				
				my $nx = $pair->{s}->{x} + $xr;
				#say "skipping from $x to $nx";
				$x = $nx;
			}
		}
	}
	return "???";
}

sub part1 {
	my $y = 2000000;
	my %covered = ();
	for my $pair ( @pairs ) {
		say Dumper( $pair );
		my $r = dist( $pair->{s}, $pair->{b} );
		my $yd = abs( $y - $pair->{s}->{y} );
		
		if( $yd <= $r ) {
			my $ex = $r - $yd;
			for ( my $x = $pair->{s}->{x} - $ex; $x <= $pair->{s}->{x} + $ex; $x++ ) {
				$covered{$x} = 1;
			}
		}
	}

	for my $pair ( @pairs ) {
		if ( $pair->{b}->{y} == $y ) {
			delete %covered{$pair->{b}->{x}};
		}
		if ( $pair->{s}->{y} == $y ) {
			delete %covered{$pair->{s}->{x}};
		}
	}

	return scalar keys %covered;
}

sub whats_at {
	my ( $p )  = @_;

	foreach my $pair ( @pairs ) {
		my $bd = dist( $p, $pair->{b} );
		my $sd = dist( $p, $pair->{s} );
		if( $bd == 0 ) {
			return ( 'B', $pair );
		}
		if( $sd == 0 ) {
			return ( 'S', $pair );
		}
		my $r = dist( $pair->{s}, $pair->{b} );
		if( $r >= $sd ) {
			return ( '#', $pair );
		}
	}
	return ( '.' );
}

sub build_grid {
	my $grid = [];
	foreach my $pair ( @pairs ) {
		build_point( $grid, $pair->{b}, 'B' );
		build_point( $grid, $pair->{s}, 'S' );
	}
	foreach my $pair ( @pairs ) {
		fill_dist( $grid, $pair->{s}, dist( $pair->{s}, $pair->{b} ) );		
	}
	return $grid;
}

sub fill_dist {
	my ( $grid, $c, $d ) = @_;
	for( my $y = $c->{y} - $d; $y <= $c->{y} + $d; $y++ ) {
		for( my $x = $c->{x} - $d; $x <= $c->{x} + $d; $x++ ) {
			my $p = { x => $x, y => $y };
			if( dist( $c, $p ) <= $d ) {
				build_point( $grid, $p, '#' );
			}
		}
	}
}

sub dist {
	my ( $p, $q ) = @_;
	return abs( $p->{x} - $q->{x} ) + abs( $p->{y} - $q->{y} ); 
}

sub build_point {
	my ( $grid, $p, $c ) = @_;
	my $x = $p->{x} - $range->{minx};
	my $y = $p->{y} - $range->{miny};
	if ( $y >= 0 
	  && $y <= ( $range->{maxy} - $range->{miny} )
	  && $x >= 0 
	  && $x <= ( $range->{maxx} - $range->{minx} ) ) {
		  if( defined $c ) {
			$grid->[$y]->[$x] //= $c;
		  }
		  else {
			$grid->[$y]->[$x]++;
		  }
	}
}

sub draw_grid {
	my ( $grid ) = @_;
	$grid = build_grid() unless defined $grid;
	
	my $screen = "";
	for( my $y = 0; $y <= $range->{maxy} - $range->{miny}; $y++ ) {
		for( my $x = 0; $x <= $range->{maxx} - $range->{minx}; $x++ ) {
			$screen .= $grid->[$y]->[$x] // '.';
		}
		$screen .= "\n";
	}

	say $screen;	
}


sub encompass {
	my ( $range, $p )  = @_;
	if ( not defined $range ) {
		return { minx => $p->{x},
		         miny => $p->{y},
				 maxx => $p->{x},
				 maxy => $p->{maxy} };
	}
	else {
		return { minx => min( $range->{minx}, $p->{x} ),
		         miny => min( $range->{miny}, $p->{y} ),
				 maxx => max( $range->{maxx}, $p->{x} ),
				 maxy => max( $range->{maxy}, $p->{y} )
			 };
	}
}

sub min {
	my ( $a, $b ) = @_;
	return $a <= $b ? $a : $b;
}
sub max {
	my ( $a, $b ) = @_;
	return $a >= $b ? $a : $b;
}