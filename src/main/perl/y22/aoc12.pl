use 5.014;
use strict;
use warnings;
use Data::Dumper;

my $grid = [];

my ( $sx, $sy, $ex, $ey ) = ( -1, -1, -1, -1 );

my @distances = ( 0..9, 'a'..'z', 'A'..'Z' );

my $y = 0;
while ( my $line = <> ) {
	chomp $line;
	for ( my $x = 0; $x < length $line; $x++ ) {
		my $h = substr $line, $x, 1;
		if( $h eq 'S' ) {
			$sx = $x;
			$sy = $y;
			$h = 'a';
		}
		elsif ( $h eq 'E' ) {
			$ex = $x;
			$ey = $y;
			$h = 'z';
		}
		$grid->[$y]->[$x] = { h => $h };
	}
	$y++;
}

my $width = scalar @{ $grid->[0] };
my $height = scalar @{ $grid };

say "w = $width h = $height";

say "heights";
print_grid( 'h' );

my @starts = ();
for ( my $y = 0; $y < $height; $y++ ) {
	for ( my $x = 0; $x < $width; $x++ ) {
		if ( $grid->[$y]->[$x]->{h} eq 'a' ) {
			push @starts, { x => $x, y => $y };
		}
	}
}

foreach my $start ( @starts ) {
	reset();
	my $queue = [ { x => $start->{x}, y => $start->{y} } ];
	$grid->[$start->{y}]->[$start->{x}]->{c} = 0;
	
	while ( scalar @$queue ) {
		flood( $queue );
	}

	$start->{steps} = $grid->[$ey]->[$ex]->{c};
}

say Dumper [ ( sort { $a->{steps} <=> $b->{steps} } @starts )[0] ];

sub flood {
	my ( $queue ) = @_;
	
	my $p = shift @$queue;
	my $cost = $grid->[$p->{y}]->[$p->{x}]->{c};
	my $height = ord $grid->[$p->{y}]->[$p->{x}]->{h};
	flood_dir( $queue, $p->{x} + 1, $p->{y}, $height, $cost+1, '>' );
	flood_dir( $queue, $p->{x} - 1, $p->{y}, $height, $cost+1, '<' );
	flood_dir( $queue, $p->{x}, $p->{y} + 1, $height, $cost+1, 'v' );
	flood_dir( $queue, $p->{x}, $p->{y} - 1, $height, $cost+1, '^' );
}

sub flood_dir {
	my ( $queue, $fx, $fy, $src_height, $new_cost, $dir ) = @_;
	if( $fx < 0 || $fx >= $width || $fy < 0 || $fy >= $height ) {
		return 0; # out of bounds
	}
	my $dst_height = ord $grid->[$fy]->[$fx]->{h};
	my $delta = $dst_height - $src_height;
	if( $delta <= 1 ) {	
		my $old_cost = $grid->[$fy]->[$fx]->{c};
		if ( not defined $old_cost or $old_cost > $new_cost ) {
			$grid->[$fy]->[$fx]->{c} = $new_cost;
			$grid->[$fy]->[$fx]->{d} = $dir;
			push @$queue, { x => $fx, y => $fy };
			return 1;
		}
	}
}

sub print_grid {
	my ( $f ) = @_;
	for ( my $y = 0; $y < $height; $y++ ) {
		for ( my $x = 0; $x < $width; $x++ ) {
			my $v = $grid->[$y]->[$x]->{$f};
			if( defined $v && $f eq 'c' ) {
				$v = $distances[ $v % scalar( @distances ) ];
			}
			print $v // ' ';
		}
		print "\n";
	}
}
sub reset {
	my ( $f ) = @_;
	for ( my $y = 0; $y < $height; $y++ ) {
		for ( my $x = 0; $x < $width; $x++ ) {
			undef $grid->[$y]->[$x]->{c};
			undef $grid->[$y]->[$x]->{d};
		}
	}
}
