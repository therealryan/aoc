use 5.014;
use strict;
use warnings;
use Data::Dumper;

my $heights = [];

my $row = 0;
while ( my $line = <> ) {
	chomp $line;
	for ( my $col = 0; $col < length $line; $col++ ) {
		$heights->[$row]->[$col] = substr $line, $col, 1;
	}
	$row++;
}

my $count = 0;
my $maxScore = 0;
my $mr;
my $mc;
for ( my $r = 0; $r < scalar @{ $heights }; $r++ ) {
	for ( my $c = 0; $c < scalar @{ $heights->[$r] }; $c++ ) {
		$count += is_visible( $heights, $r, $c );
		my $score = scenic_score( $heights, $r, $c );
		
		if( $score > $maxScore ) {
			$maxScore = $score;
			$mr = $r;
			$mc = $c;
		}
	}
}

say "$count trees visible from outside";
say "Optimal scenic score is $maxScore from $mr,$mc";

# returns 1 if the tree at ($r,$c) is visible from the outside, otherwise 0
sub is_visible {
	my ( $heights, $r, $c ) = @_;
	
	return ( vis_check( $heights, $r, $c, -1,  0 )
	      || vis_check( $heights, $r, $c,  1,  0 )
	      || vis_check( $heights, $r, $c,  0, -1 )
	      || vis_check( $heights, $r, $c,  0,  1 ) ) ? 1 : 0
}

# returns 1 if you can step from ($r,$c) to the limits of the forest with
# increments (ri,ci) without finding tree of equal or greater height
sub vis_check {
	my ( $heights, $r, $c, $ri, $ci ) = @_;
	my $h = $heights->[$r]->[$c];
	my $cr = $r + $ri;
	my $cc = $c + $ci;
	
	while( $cr >= 0 && $cr < scalar @{ $heights }
	    && $cc >= 0 && $cc < scalar @{ $heights->[$cr] } ) {
		my $th = $heights->[$cr]->[$cc];
		if ( $th >= $h ) {
			return 0;
		}
		$cr += $ri;
		$cc += $ci;
	}
	return 1;
}

# returns the scenic score of the tree at (r,c)
sub scenic_score {
	my ( $heights, $r, $c ) = @_;
	
	my $up = score( $heights, $r, $c,  -1,  0 );
	my $down = score( $heights, $r, $c,   1,  0 );
	my $left = score( $heights, $r, $c,   0, -1 );
	my $right = score( $heights, $r, $c,   0,  1 );
	
	my $total = $left * $right * $up * $down;
	return $total;
}

# Returns the number of (ri,ci) steps you can take from (r,c) before either
# hitting the edge of the forest or a tree of equal or greater height
sub score {
	my ( $heights, $r, $c, $ri, $ci ) = @_;
	my $h = $heights->[$r]->[$c];
	my $cr = $r + $ri;
	my $cc = $c + $ci;
	
	my $count = 0;
	while( $cr >= 0 && $cr < scalar @{ $heights }
	    && $cc >= 0 && $cc < scalar @{ $heights->[$cr] } ) {
		$count++;
		my $th = $heights->[$cr]->[$cc];
		if ( $th >= $h ) {
			return $count;
		}
		$cr += $ri;
		$cc += $ci;
	}
	return $count;
}
