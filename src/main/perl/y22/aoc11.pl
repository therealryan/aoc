use 5.014;
use strict;
use warnings;
use Data::Dumper;
$Data::Dumper::Sortkeys = 1;

my $monkeys = {};

my $current_monkey;

my $modulo = 1;

while ( my $line = <> ) {
	chomp $line;
	
	if( $line =~ m/Monkey (\d+)/ ) {
		$current_monkey = $1;
	}
	elsif ( $line =~ m/Starting items: (.*)/ ) {
		$monkeys->{$current_monkey}->{items} = [ split( m/\D+/, $1 ) ];
	}
	elsif ( $line =~ m/Operation: new = (.+)/ ) {
		$monkeys->{$current_monkey}->{operation} = $1;
	}
	elsif ( $line =~ m/Test: divisible by (\d+)/ ) {
		$monkeys->{$current_monkey}->{divisor} = $1;
		$modulo *= $1;
	}
	elsif ( $line =~ m/If true: throw to monkey (\d+)/ ) {
		$monkeys->{$current_monkey}->{true_target} = $1;
	}
	elsif ( $line =~ m/If false: throw to monkey (\d+)/ ) {
		$monkeys->{$current_monkey}->{false_target} = $1;
	}
	elsif ( $line eq '' ) {
		# this is fine
	}
	else {
		say "bad line '$line'";
	}
}
say "initial state";
say Dumper $monkeys;

say "modulo $modulo";

foreach my $round ( 1 .. 10000 ) {
	foreach my $monkey ( sort keys %$monkeys ) {
		while ( scalar @{ $monkeys->{$monkey}->{items} } ) {
			my $item = shift @{ $monkeys->{$monkey}->{items} };
			
			my $op = $monkeys->{$monkey}->{operation};
			
			$op =~ s/old/$item/g;
			
			my $result = eval $op;
			
			# $result = int( $result / 3 );
			$result %= $modulo;
			
			my $divisible = ( $result % $monkeys->{$monkey}->{divisor} ) == 0;
			
			my $target = $divisible
				? $monkeys->{$monkey}->{true_target}
				: $monkeys->{$monkey}->{false_target};
			
			push @{ $monkeys->{$target}->{items} }, $result;
			
			$monkeys->{$monkey}->{inspection_count}++;
		}
	}
}

say "final state";
say Dumper $monkeys;

my @inspections = reverse sort { $a <=> $b } map { $monkeys->{$_}->{inspection_count} } keys %$monkeys;

say "monkey business = ", $inspections[0] * $inspections[1];