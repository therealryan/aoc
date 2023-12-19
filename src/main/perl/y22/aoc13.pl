use 5.014;
use strict;
use warnings;
use Data::Dumper;
$Data::Dumper::Sortkeys = 1;

my $pairs = [];
my $lidx = 0;
my $pidx = 0;

while ( my $line = <> ) {
	chomp $line;
	
	if( $line eq '' ) {
		$pidx++;
	}
	else {
		$pairs->[$pidx]->{ chr( ord('a') + ( $lidx % 2 ) ) } = eval $line;
		$lidx++;
	}
}

my $hi = 1;
my $sum = 0;
foreach my $pair ( @$pairs ) {
	if ( is_in_correct_order( $pair->{a}, $pair->{b} ) ) {
		$sum += $hi;
	}
	$hi++;
}

say "index sum = $sum";

my @packets = ( [[2]], [[6]] );
push( @packets, $_->{a}, $_->{b} ) foreach @$pairs;
my @sorted = sort { compare( $a, $b ) } @packets;

my $decoder_key = 1;
$hi = 1;
foreach my $packet ( @sorted ) {
	if( ref( $packet ) eq 'ARRAY'
	 && scalar( @{ $packet } ) == 1
	 && ref( $packet->[0] ) eq 'ARRAY'
	 && scalar( @{ $packet->[0] } ) == 1
	 && ( $packet->[0]->[0] == 2 || $packet->[0]->[0] == 6 ) ) {
		 say "found ". Dumper( $packet ) . " at $hi";
		 $decoder_key *= $hi;
	}
	$hi++;
}

say "decoder key = $decoder_key";

sub is_in_correct_order {
	my ( $left, $right ) = @_;

	return compare( $left, $right ) == -1;
}

sub compare {
	my ( $l, $r ) = @_;
	if( ref( $l ) eq '' && ref( $r ) eq '' ) {
		if ( $l < $r ) {
			return -1;
		}
		elsif ( $l > $r ) {
			return 1;
		}
		return 0;
	}
	elsif ( ref( $l ) eq 'ARRAY' && ref( $r ) eq 'ARRAY' ) {
		
		my $ll = scalar( @{$l} );
		my $rl = scalar( @{$r} );
		
		for ( my $i = 0; $i < $ll && $i < $rl; $i++ ) {
			my $c = compare( $l->[$i], $r->[$i] );
			return $c unless $c == 0;
		}
		return compare( $ll, $rl );
	}
	else {
		if( ref ( $l ) eq '' ) {
			return compare( [ $l ], $r );
		}
		else {
			return compare( $l, [ $r ] );
		}
	}
}