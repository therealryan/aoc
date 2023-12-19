use 5.014;
use strict;
use warnings;
use Data::Dumper;
$Data::Dumper::Sortkeys = 1;
use Term::Cap;
use Time::HiRes qw(usleep);

my $terminal = Term::Cap->Tgetent( { OSPEED => 9600 } );
my $clear_screen = $terminal->Tputs('cl');

my @walls = ();
my $miny = 0;
my $maxy = 0;
my $minx = 500;
my $maxx = 500;

my $srcx = 500;
my $srcy = 0;

my $ii = 0;
while ( my $line = <> ) {
	chomp $line;
	while ( $line =~ m/(\d+),(\d+)/g ) {
		$minx = $1 < $minx ? $1 : $minx;
		$miny = $2 < $miny ? $2 : $miny;
		$maxx = $1 > $maxx ? $1 : $maxx;
		$maxy = $2 > $maxy ? $2 : $maxy;
		push @{ $walls[$ii] }, { x => $1, y => $2 };
	}
	$ii++;
}

my $grid = [];
foreach my $wall ( @walls ) {
	build_wall( @$wall );
}

$maxy += 2;
for ( my $x = 0; $x < 1000; $x++ ) {
	$grid->[$maxy]->[$x] = '#';
}

draw_grid();

while( add_sand() ) {
}

draw_grid();

my $count = 0;
for ( my $y = 0; $y <= scalar( @{ $grid } ); $y++ ) {
	if( defined $grid->[$y] ) {
		for ( my $x = 0; $x <= scalar( @{ $grid->[$y] } ); $x++ ) {
			if( ( $grid->[$y]->[$x] // '' )  eq 'o' ) {
				$count++;
			}
		}
	}
}

say "Holds $count sands";

sub add_sand {
	my $s = { x => $srcx, y => $srcy };
	my $at_rest = 0;
	
	while ( !$at_rest ) {
		if ( not defined $grid->[$s->{y}+1]->[$s->{x}] ) {
			$s->{y}++;
		}
		elsif ( not defined $grid->[$s->{y}+1]->[$s->{x}-1] ) {
			$s->{y}++;
			$s->{x}--;
		}
		elsif ( not defined $grid->[$s->{y}+1]->[$s->{x}+1] ) {
			$s->{y}++;
			$s->{x}++;
		}
		else {
			$at_rest = 1;
			$grid->[$s->{y}]->[$s->{x}] = 'o'
		}
		
		draw_grid( $s );
		
		if ( $s->{x} < $minx || $s->{x} > $maxx || $s->{y} < $miny || $s->{y} > $maxy ) {
			# return 0;
		}
		if( $s->{x} == $srcx && $s->{y} == $srcy ) {
			return 0;
		}
	}
	
	return 1;
}

sub build_wall {
	my ( @points ) = @_;
	my $p = $points[0];

	for( my $i = 1; $i < scalar @points; $i++ ) {
		my $t = $points[$i];
		while( $p->{x} != $t->{x} || $p->{y} != $t->{y} ) {
			$grid->[ $p->{y} ]->[ $p->{x} ] = '#';
			
			if( $p->{x} < $t->{x} ) {
				$p->{x}++;
			}
			elsif( $p->{x} > $t->{x} ) {
				$p->{x}--;
			}
			if( $p->{y} < $t->{y} ) {
				$p->{y}++;
			}
			elsif( $p->{y} > $t->{y} ) {
				$p->{y}--;
			}
		}
		$grid->[ $p->{y} ]->[ $p->{x} ] = '#';
	}
}

sub draw_grid {
	my ( $s ) = @_;
	my $screen = $clear_screen;
	my $draw_min_y = $miny;
	my $draw_max_y = $maxy;
	my $draw_min_x = $minx;
	my $draw_max_x = $maxx;
	
	if( defined $s ) {
		my $extent = 25;
		$draw_min_y = max( 0, $s->{y} - $extent );
		$draw_max_y = $s->{y} + $extent;
	
		$draw_min_x = max( 0, $s->{x} - $extent );
		$draw_max_x = $s->{x} + $extent;
	}
	
	for ( my $y = $draw_min_y; $y <= $draw_max_y; $y++ ) {
		for ( my $x = $draw_min_x; $x <= $draw_max_x; $x++ ) {
			my $char = $grid->[$y]->[$x] // '.';
			
			if ( $x == $srcx && $y == $srcy ) {
				$char = '+';
			}
			
			if( defined $s && $s->{x} == $x && $s->{y} == $y ) {
				$char = 'O';
			}
			
			$screen .= $char;
		}
		$screen .= "\n";
	}
	say $screen;
	usleep 1000;
}

sub min {
	my ( $a, $b ) = @_;
	return $a <= $b ? $a : $b;
}
sub max {
	my ( $a, $b ) = @_;
	return $a >= $b ? $a : $b;
}