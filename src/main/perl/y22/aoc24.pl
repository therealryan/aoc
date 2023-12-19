use 5.014;
use strict;
use warnings;
use Data::Dumper;
$Data::Dumper::Sortkeys = 1;
use Term::Cap;
use Time::HiRes qw(usleep);

my $terminal = Term::Cap->Tgetent( { OSPEED => 9600 } );
my $clear_screen = $terminal->Tputs('cl');

my $blizzard = {};
my $width = 0;
my $height = 0;

my $grid = [];

my $start = {};
my $end = {};
my $time = 0;

while ( my $line = <> ) {
	chomp $line;
	$height++;
	$width = length $line;
	
	if ( $line =~ m/^#\.#+$/ ) {
		# start
		for ( my $c = 0; $c < length $line; $c++ ) {
			my $v = substr $line, $c, 1;
			$grid->[$height]->[$c] = $v;
			if ( $v eq '.' ) {
				$start = { r => $height, c => $c };
			}
		}
	}
	elsif ( $line =~ m/^#+\.#$/ ) {
		# end
		for ( my $c = 0; $c < length $line; $c++ ) {
			my $v = substr $line, $c, 1;
			$grid->[$height]->[$c] = $v;
			if ( $v eq '.' ) {
				$end = { r => $height, c => $c };
			}
		}
	}
	elsif ( $line =~ m/^#([<>^v.]+)#$/ ) {
		# body
		for ( my $c = 0; $c < length $line; $c++ ) {
			my $v = substr $line, $c, 1;
			$grid->[$height]->[$c] = $v;
			if ( $v eq '#' ) {
				$grid->[$height]->[$c] = $v;
			}
			else {
				if ( $v eq '<' ) {
					push @{ $blizzard->{h}->{$height} }, { char => $v, c => $c, r => $height, ci => -1, ri => 0 }
				}
				elsif ( $v eq '>' ) {
					push @{ $blizzard->{h}->{$height} }, { char => $v, c => $c, r => $height, ci => 1, ri => 0 }
				}
				elsif ( $v eq '^' ) {
					push @{ $blizzard->{v}->{$c} }, { char => $v, c => $c, r => $height, ci => 0, ri => -1 }
				}
				elsif ( $v eq 'v' ) {
					push @{ $blizzard->{v}->{$c} }, { char => $v, c => $c, r => $height, ci => 0, ri => 1 }
				}
				$grid->[$height]->[$c] = '.';
			}
		}
	}
	else {
		say "bad line '$line'";
	}
}

$grid->[0] = [ split '', '#' x $width ];
push @$grid, [ split '', '#' x $width ];

find_path( $start, $end );
find_path( $end, $start );
find_path( $start, $end );

1;

sub find_path {
	my ( $start, $end ) = @_;
	
	my @positions = ( {%$start} );
	
	while ( 1 ) {
		my %next_positions = ();
		$time++;
		foreach my $p ( @positions ) {
			if ( $p->{r} == $end->{r} && $p->{c} == $end->{c} ) {
				say "found exit at time " . ( $time - 1 );
				return;
			}
			foreach my $m ( { ri => 0, ci => 1 }, { ri => 0, ci => -1 }, { ri => 1, ci => 0 }, { ri => -1, ci => 0 }, { ri => 0, ci => 0 } ) {
				my $r = $p->{r} + $m->{ri};
				my $c = $p->{c} + $m->{ci};
				if ( sample( $r, $c ) eq '0' && $grid->[$r]->[$c] eq '.' ) {
					$next_positions{"$r,$c"} = 1;
				}
			}
		}
		
		@positions = ();
		foreach my $n ( keys %next_positions ) {
			die "bad coord $n" unless $n =~ m/(\d+),(\d+)/;
			push @positions, { r => $1, c => $2 };
		}
		
		say "Exploring " . scalar( @positions ) . " points at time $time";
	}
}

sub sample {
	my ( $r, $c ) = @_;
	
	my @poss = ( @{ $blizzard->{h}->{$r} // [] }, @{ $blizzard->{v}->{$c} // [] } );
	my @bl = ();
	foreach my $b ( @poss ) {
		my $p = blizzard_pos( $b );
		if ( $p->{r} == $r && $p->{c} == $c ) {
			push @bl, $b;
		}
	}
	
	if ( scalar @bl == 1 ) {
		return $bl[0]->{char};
	}

	return scalar @bl;
}

sub blizzard_pos {
	my ( $b ) = @_;
	my $p = { 
		r => ( ( $b->{r} - 2 + $time * $b->{ri} ) % ( $height - 2 ) ) + 2, 
		c => ( ( $b->{c} - 1 + $time * $b->{ci} ) % ( $width - 2 ) ) + 1 };
	# say "blizzard_pos " . Dumper { b => $b, t => $time, p => $p, h => $height, w => $width };
	return $p;
}

sub draw_valley {
	my $screen = '';
	
	for ( my $r = 0; $r < scalar @{ $grid }; $r++ ) {

		for ( my $c = 0; $c < scalar @{ $grid->[$r] }; $c++ ) {
			my $v = $grid->[$r]->[$c];
			
			if ( $r == $start->{r} && $c == $start->{c} ) {
				$v = 's';
			}
			elsif ( $r == $end->{r} && $c == $end->{c} ) {
				$v = 'e';
			}
			
			$screen .= sample( $r, $c ) || $v;
		}
		$screen .= "\n";
	}
	$screen .= "At time $time";
	say $screen;
}