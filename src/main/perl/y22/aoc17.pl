use 5.014;
use strict;
use warnings;
use Data::Dumper;
$Data::Dumper::Sortkeys = 1;

my @rocks = (
	{
		name => 'flat',
		coords => [
			{x => 0, y => 0},
			{x => 1, y => 0},
			{x => 2, y => 0},
			{x => 3, y => 0},
		],
	},
	{
		name => 'cross',
		coords => [
			{x => 0, y => 1},
			{x => 1, y => 0},
			{x => 1, y => 1},
			{x => 1, y => 2},
			{x => 2, y => 1},
		],
	},
	{
		name => 'corner',
		coords => [
			{x => 0, y => 0},
			{x => 1, y => 0},
			{x => 2, y => 0},
			{x => 2, y => 1},
			{x => 2, y => 2},
		],
	},
	{
		name => 'tall',
		coords => [
			{x => 0, y => 0},
			{x => 0, y => 1},
			{x => 0, y => 2},
			{x => 0, y => 3},
		],
	},
	{
		name => 'block',
		coords => [
			{x => 0, y => 0},
			{x => 0, y => 1},
			{x => 1, y => 0},
			{x => 1, y => 1},
		],
	},
);

my @jets = ();
my $grid = [];
my $top = 0;
my $trimmed = 0;
my $jet_index = 0;
my @top_deltas = ();

my $active_rows_bottom = 0;

while ( my $line = <> ) {
	chomp $line;
	push @jets, $_ foreach split '', $line;
}

my $sample = 1 * scalar( @rocks ) * scalar( @jets );
my $target = 1000000000000;

say "generating $sample height deltas";

for ( my $ri = 0; $ri < $sample; $ri++ ) {
	my $rock = @rocks[$ri % scalar( @rocks ) ];
	my $settled = 0;
	
	$active_rows_bottom = $top + 3;
	for my $p ( @{ $rock->{coords} } ) {
		$grid->[ $top + 3 + $p->{y} ]->[ 2 + $p->{x} ] = '@';
	}
	
	while ( not $settled ) {
		my $jet = $jets[$jet_index];
		$jet_index = ( $jet_index + 1 ) % scalar( @jets );
		if ( $jet eq '<' ) {
			move_left();
		}
		else {
			move_right();
		}
		
		$settled = fall();
		$active_rows_bottom--;
	}
	
	my $old = $top;
	if( $top < $settled ) {
		$top = $settled;
	}
	push @top_deltas, $top - $old;
	
	trim();
}

my $i = 0;
my $screen = '';
foreach my $d ( reverse @top_deltas ) {
	$screen .= $d;
}
say $screen;

sub trim {
	if ( scalar( @$grid ) > 2000 ) {
		$trimmed += 1000;
		$top -= 1000;
		splice @$grid, 0, 1000;
	}
}

sub move_left {
	my $blocked = 0;
	for ( my $r = $active_rows_bottom - 4; $r < $active_rows_bottom + 4; $r++ ) {
		for ( my $c = 0; $c < 7; $c++ ) {
			if ( ( $grid->[$r]->[$c] // '' ) eq '@' ) {
				if ( $c == 0 ) {
					$blocked = 1;
				}
				else {
					if ( ( $grid->[$r]->[$c - 1] // '' ) eq '#' ) {
						$blocked = 1;
					}
				}
			}
		}
	}
	
	unless ( $blocked ) {
		for ( my $r = $active_rows_bottom - 4; $r < $active_rows_bottom + 4; $r++ ) {
			for ( my $c = 1; $c < 7; $c++ ) {
				if ( ( $grid->[$r]->[$c] // '' ) eq '@' ) {
					$grid->[$r]->[$c-1] = '@';
					$grid->[$r]->[$c] = undef;
				}
			}
		}
	}
}

sub move_right {
	my $blocked = 0;
	for ( my $r = $active_rows_bottom - 4; $r < $active_rows_bottom + 4; $r++ ) {
		for ( my $c = 6; $c >= 0; $c-- ) {
			if ( ( $grid->[$r]->[$c] // '' ) eq '@' ) {
				if ( $c == 6 ) {
					$blocked = 1;
				}
				else {
					if ( ( $grid->[$r]->[$c + 1] // '' ) eq '#' ) {
						$blocked = 1;
					}
				}
			}
		}
	}
	
	unless ( $blocked ) {
		for ( my $r = $active_rows_bottom - 4; $r < $active_rows_bottom + 4; $r++ ) {
			for ( my $c = 5; $c >= 0; $c-- ) {
				if ( ( $grid->[$r]->[$c] // '' ) eq '@' ) {
					$grid->[$r]->[$c+1] = '@';
					$grid->[$r]->[$c] = undef;
				}
			}
		}
	}
}

sub fall {
	my $blocked = 0;
	
	for ( my $r = $active_rows_bottom - 4; $r < $active_rows_bottom + 4; $r++ ) {
		for ( my $c = 0; $c < 7; $c++ ) {
			if ( ( $grid->[$r]->[$c] // '' ) eq '@' ) {
				if ( $r == 0 ) {
					$blocked = 1;
				}
				else {
					if ( ( $grid->[$r-1]->[$c] // '' ) eq '#' ) {
						$blocked = 1;
					}
				}
			}
		}
	}
	
	my $new_top = 0;
	if ( $blocked ) {
		for ( my $r = $active_rows_bottom - 4; $r < $active_rows_bottom + 4; $r++ ) {
			for ( my $c = 0; $c < 7; $c++ ) {
				if ( ( $grid->[$r]->[$c] // '' ) eq '@' ) {
					$grid->[$r]->[$c] = '#';
					if ( $r > $new_top ) {
						$new_top = $r;
					}
				}
			}
		}
		$new_top += 1;
	}
	else {
		for ( my $r = $active_rows_bottom - 4; $r < $active_rows_bottom + 4; $r++ ) {
			for ( my $c = 0; $c < 7; $c++ ) {
				if ( ( $grid->[$r]->[$c] // '' ) eq '@' ) {
					$grid->[$r-1]->[$c] = '@';
					$grid->[$r]->[$c] = undef;
				}
			}
		}
	}
	
	return $new_top;
}

sub draw {
	my @lines = ( '+-------+' );
	foreach my $row ( @$grid ) {
		my $line = '|';
		$row //= [];
		for ( my $c = 0; $c < 7; $c++ ) {
			$line .= $row->[$c] // '.';
		}
		$line .= '|';
		push @lines, $line;
	}
	
	my $screen = '';
	$screen .= "$_\n" foreach reverse @lines;
	say $screen;
	say "Height = $top";
}

1;