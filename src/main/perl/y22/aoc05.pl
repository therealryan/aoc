use 5.014;
use strict;
use warnings;
use Data::Dumper;

my $stacks = undef;

my @state_lines = ();

while ( my $line = <> ) {
  chomp $line;

  if( not defined $stacks ) {
    if( $line =~ m/^\s+(\d+\s+)*(\d+)\s+$/ ) {
		say $2;
		say Dumper \@state_lines;
		$stacks = [];
		parse_row( $stacks, $2, $_ ) foreach @state_lines;
		say Dumper $stacks;
	}
	else {
		unshift @state_lines, $line;
	}
  }
  else {
	  if( $line =~ m/move (\d+) from (\d+) to (\d+)/ ) {
	    move( $stacks, $1, $2-1, $3-1 );
	  }
	  else {
		say "bad line '$line'";
	  }
  }
}

say Dumper( $stacks );

my $result = '';
foreach my $stack ( @$stacks ) {
	$result .= pop @$stack;
}

say $result;

sub parse_row {
	my ( $stacks, $columns, $line ) = @_;
	
	for( my $i = 0; $i < $columns; $i++ ) {
		my $t = substr $line, $i * 4, 4;
		if( $t =~ m/\[(.)\]/ ) {
			push @{$stacks->[$i]}, $1;
		}
	}
}

sub move {
	my ( $stacks, $count, $from, $to ) = @_;
	my @moved = ();
	for( my $i = 0; $i < $count; $i++ ) {
		unshift @moved, pop @{$stacks->[$from]};
	}
	push( @{$stacks->[$to]}, $_ ) foreach @moved;
}