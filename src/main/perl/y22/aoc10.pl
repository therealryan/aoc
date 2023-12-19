use 5.014;
use strict;
use warnings;
use Data::Dumper;

my $x = 1;
my $cycle = 0;
my $signal_strength_sum = 0;

my $row = 0;
my $column = 0;

my $pixels = [];

while ( my $line = <> ) {
	chomp $line;
	
	if( $line =~ m/addx (-?\d+)/ ) {
		cycle();
		cycle();
		$x += $1;
	}
	elsif ( $line eq 'noop' ) {
		cycle();
	}
	else {
		say "bad line '$line'";
	}
}

say "signal strength sum = $signal_strength_sum";

say @$_ foreach @$pixels;

sub cycle {
	$cycle++;
	
	if( ( ( $cycle - 20 ) % 40 ) == 0 ) {
		$signal_strength_sum += $cycle * $x;
	}
	
	$pixels->[$row]->[$column] = abs( $x - $column ) <= 1 ? '#' : '.';
	
	$column++;
	if( $column >= 40 ) {
		$row++;
		$column = 0
	}
}