use 5.014;
use strict;
use warnings;
use Data::Dumper;
$Data::Dumper::Sortkeys = 1;

my $grid = [];

my $maxx = 0;
my $maxy = 0;
my $maxz = 0;

while ( my $line = <> ) {
	chomp $line;
	
	if( $line =~ m/(\d+),(\d+),(\d+)/ ) {
		my $x = $1+1;
		my $y = $2+1;
		my $z = $3+1;
		$grid->[$x]->[$y]->[$z] = 'l';
		
		$maxx = $x > $maxx ? $x : $maxx;
		$maxy = $y > $maxy ? $y : $maxy;
		$maxz = $z > $maxz ? $z : $maxz;
	}
	else {
		say "bad line '$line'";
	}
}

encase_with_air( $grid, $maxx+1, $maxy+1, $maxz+1 );
flood_air( $grid );

say count_faces( $grid ), " faces exposed";

sub count_faces {
	my ( $grid ) = @_;
	
	my $faces = 0;
	for ( my $x = 0; $x < scalar( @{ $grid } ); $x++ ) {
		for ( my $y = 0; $y < scalar( @{ $grid->[$x] // [] } ); $y++ ) {
			for ( my $z = 0; $z < scalar( @{ $grid->[$x]->[$y] // [] } ); $z++ ) {
				if ( 'l' eq get_point( $grid, $x, $y, $z ) ) {
					$faces++ if get_point( $grid, $x+1, $y, $z ) eq 'a';
					$faces++ if get_point( $grid, $x-1, $y, $z ) eq 'a';
					$faces++ if get_point( $grid, $x, $y+1, $z ) eq 'a';
					$faces++ if get_point( $grid, $x, $y-1, $z ) eq 'a';
					$faces++ if get_point( $grid, $x, $y, $z+1 ) eq 'a';
					$faces++ if get_point( $grid, $x, $y, $z-1 ) eq 'a';
				}
			}
		}
	}
	
	return $faces;
}

sub get_point {
	my ( $grid, $x, $y, $z ) = @_;
	if ( $x < 0 || $y < 0 || $z < 0 ) {
		return 'a';
	}
	return $grid->[$x]->[$y]->[$z] // 'v';
}

sub encase_with_air {
	my ( $grid, $maxx, $maxy, $maxz ) = @_;
	
	
	for ( my $x = 0; $x <= $maxx; $x++ ) {
		for ( my $y = 0; $y <= $maxy; $y++ ) {
			$grid->[$x]->[$y]->[0] = 'a';
			$grid->[$x]->[$y]->[$maxz] = 'a';
		}
	}
	for ( my $x = 0; $x <= $maxx; $x++ ) {
		for ( my $z = 0; $z <= $maxz; $z++ ) {
			$grid->[$x]->[0]->[$z] = 'a';
			$grid->[$x]->[$maxy]->[$z] = 'a';
		}
	}
	for ( my $z = 0; $z <= $maxz; $z++ ) {
		for ( my $y = 0; $y <= $maxy; $y++ ) {
			$grid->[0]->[$y]->[$z] = 'a';
			$grid->[$maxx]->[$y]->[$z] = 'a';
		}
	}
}

sub flood_air {
	my ( $grid ) = @_;
	
	my @pending = ();
	
	for ( my $x = 0; $x < scalar( @{ $grid } ); $x++ ) {
		for ( my $y = 0; $y < scalar( @{ $grid->[$x] // [] } ); $y++ ) {
			for ( my $z = 0; $z < scalar( @{ $grid->[$x]->[$y] // [] } ); $z++ ) {
				push @pending, { x=> $x, y => $y, z => $z } if get_point( $grid, $x, $y, $z ) eq 'a';
			}
		}
	}
	
	while ( scalar @pending ) {
		my $p = shift @pending;
		push @pending, flood_air_into( $grid, $p,  1, 0, 0 );
		push @pending, flood_air_into( $grid, $p, -1, 0, 0 );
		push @pending, flood_air_into( $grid, $p, 0,  1, 0 );
		push @pending, flood_air_into( $grid, $p, 0, -1, 0 );
		push @pending, flood_air_into( $grid, $p, 0, 0,  1 );
		push @pending, flood_air_into( $grid, $p, 0, 0, -1 );
	}
}

sub flood_air_into {
	my ( $grid, $p, $xd, $yd, $zd ) = @_;
	
	my $x = $p->{x} + $xd;
	my $y = $p->{y} + $yd;
	my $z = $p->{z} + $zd;
	
	if ( $x < 0 || $x >= $maxx +1) {
		return ();
	}
	if ( $y < 0 || $y >= $maxy +1) {
		return ();
	}
	if ( $z < 0 || $z >= $maxz +1) {
		return ();
	}
	if ( 'v' eq get_point( $grid, $x, $y, $z ) ) {
		$grid->[$x]->[$y]->[$z] = 'a';
		return ( { x => $x, y => $y, z => $z } );
	}
	
	return ();
}