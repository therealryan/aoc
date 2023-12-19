use 5.014;
use strict;
use warnings;
use Data::Dumper;
$Data::Dumper::Sortkeys = 1;

my $first;
my $prev;

my $key = 811589153;
my $rounds = 10;

my $i = 0;
while ( my $line = <> ) {
	chomp $line;
	if( $line =~ m/^-?\d+$/ ) {
		my $node = { 
			i => $i, 
			v => $key * int $line,
			p => $prev };
		$prev->{n} = $node;
		$first //= $node;
		$prev = $node;
	}
	else {
		say "bad line '$line'";
	}
	$i++;
}

my $count = $i;
$first->{p} = $prev;
$prev->{n} = $first;

for ( my $r = 0; $r < $rounds; $r++ ) {
	say "round $r";
	for ( my $original_index = 0; $original_index < $count; $original_index++ ) {
		my $r = move( $original_index );
	}
}

my $zero_node = find_value( 0 );

my $a = advance( $zero_node, 1000 % scalar $count );
my $b = advance( $zero_node, 2000 % scalar $count );
my $c = advance( $zero_node, 3000 % scalar $count );

say "$a->{v} + $b->{v} + $c->{v} = " . ( $a->{v} + $b->{v} + $c->{v} );

1;

sub move {
	my ( $og_index ) = @_;
	
	my $node = find_index( $og_index );
	my $shift = $node->{v};
	
	if ( $shift < 0 || ( $count - 1 ) < $shift  ) {
		$shift %= ( $count - 1 );
	}
	
	if ( $shift > 0 ) {
		for ( my $i = 0; $i < $shift; $i++ ) {
			move_right( $node );
		}
	}
	if ( $shift < 0 ) {
		for ( my $i = 0; $i > $shift; $i-- ) {
			move_left( $node );
		}
	}
	
	return $node->{v};
}

sub find_index {
	my ( $index ) = @_;
	
	my $node = $first;
	
	while ( $node->{i} != $index ) {
		$node = $node->{n};
	}
	
	die "failed to find og index $index" if $node->{i} != $index;
	
	return $node;
}

sub move_right {
	my ( $node ) = @_;
	
	my $prev = $node->{p};
	my $next = $node->{n};
	my $next_next = $next->{n};
	
	$prev->{n} = $next;
	$next->{n} = $node;
	$node->{n} = $next_next;
	
	$next_next->{p} = $node;	
	$node->{p} = $next;
	$next->{p} = $prev;
}

sub move_left {
	my ( $node ) = @_;
	move_right( $node->{p} );
}

sub find_value {
	my ( $value ) = @_;
	my $node = $first;

	while ( $node->{v} != $value ) {
		$node = $node->{n};
	}
	
	die "failed to find value $value" if $node->{v} != $value;
	
	return $node;
}

sub advance {
	my ( $node, $shift ) = @_;
	for ( my $i = 0; $i < $shift; $i++ ) {
		$node = $node->{n};
	}
	return $node;
}

sub print_values {
	my $s = "$first->{v}, ";
	my $node = $first->{n};
	while ( $node != $first ) {
		$s .= "$node->{v}, ";
		$node = $node->{n};
	}
	say $s;
}
