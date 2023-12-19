use 5.014;
use strict;
use warnings;
use Data::Dumper;
$Data::Dumper::Sortkeys = 1;

my @numbers = ();

my $i = 0;
while ( my $line = <> ) {
	chomp $line;
	if( $line =~ m/^-?\d+$/ ) {
		push @numbers, { i => $i, v => int $line };
	}
	else {
		say "bad line '$line'";
	}
	$i++;
}

say "initial";
print_values();

for ( my $original_index = 0; $original_index < scalar @numbers; $original_index++ ) {
	my $r = move( $original_index );
	say "\n$r moves";
	print_values();
}

say "\nfinal";
print_values();

my $zero_index = find_value( 0 );
say "zero at index $zero_index";
my $a_idx = ( $zero_index + 1000 ) % scalar @numbers;
my $b_idx = ( $zero_index + 2000 ) % scalar @numbers;
my $c_idx = ( $zero_index + 3000 ) % scalar @numbers;

my $a = $numbers[ $a_idx ]->{v};
my $b = $numbers[ $b_idx ]->{v};
my $c = $numbers[ $c_idx ]->{v};

say "$a + $b + $c = " . ( $a + $b + $c );

1;

sub move {
	my ( $og_index ) = @_;
	my $src = find_index( $og_index );
	my $moved = $numbers[$src]->{v};
	my $count = scalar @numbers;
	
	my $dst = $src + $moved;
	
	if ( $moved < 0 ) {
		$dst --;
	}
	
	say "source $src value $moved";
	say "initial dest $dst";
	while( $dst < 0 ) {
		$dst += $count;
	}
	say "after minus $dst";
	
	while( $dst >= $count ) {
		$dst -= $count-1;
	}
	say "after mod $dst";
	
	
	my @removed = splice @numbers, $src, 1;
	splice @numbers, $dst, 0, @removed;
	
	return $moved;
}

sub find_index {
	my ( $index ) = @_;
	for ( my $i = 0; $i < scalar @numbers; $i++ ) {
		if( $numbers[$i]->{i} == $index ) {
			return $i;
		}
	}
	
	die "failed to find og index $index";
}

sub find_value {
	my ( $value ) = @_;
	for ( my $i = 0; $i < scalar @numbers; $i++ ) {
		if( $numbers[$i]->{v} == $value ) {
			return $i;
		}
	}
	
	die "failed to find value $value";
}

sub print_values {
	say join ', ', map { $_->{v} } @numbers;
}
