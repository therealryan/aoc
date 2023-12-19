use 5.014;
use strict;
use warnings;
use Data::Dumper;
$Data::Dumper::Sortkeys = 1;

my %monkeys = ();
my @satisfied = ();

while ( my $line = <> ) {
	chomp $line;
	
	if ( $line =~ m/^([a-z]{4}): (\d+)$/ ) {
		$monkeys{$1}->{value} = $2;
		push @satisfied, $1;
	}
	elsif ( $line =~ m|^([a-z]{4}): ([a-z]{4}) ([+-/*]) ([a-z]{4})$| ) {
		$monkeys{$1}->{left}= $2;
		$monkeys{$1}->{op} = $3;
		$monkeys{$1}->{right} = $4;
		push @{ $monkeys{$2}->{supplies} }, $1;
		push @{ $monkeys{$4}->{supplies} }, $1;
	}
	else {
		say "bad line '$line'";
	}
}

#part1();

part2();

sub part2 {
	$monkeys{root}->{op} = '==';
	delete $monkeys{humn}->{value};
	
	# find one side of the equality
	while ( scalar @satisfied ) {
		
		my @newly_satisfied = ();
		
		foreach my $sm ( @satisfied ) {
			my $src = $monkeys{$sm};
			foreach my $dst ( @{ $src->{supplies} } ) {
				my $ln = $monkeys{$dst}->{left};
				my $op = $monkeys{$dst}->{op};
				my $rn = $monkeys{$dst}->{right};
				my $lv = $monkeys{$ln}->{value};
				my $rv = $monkeys{$rn}->{value};
				
				if ( defined $lv and defined $rv ) {
					$monkeys{$dst}->{value} = evaluate( $lv, $op, $rv );
					push @newly_satisfied, $dst;
				}
			}
		}
		
		@satisfied = @newly_satisfied;
	}
	
	say "root op is ",
		$monkeys{root}->{left}, "(",
		$monkeys{ $monkeys{root}->{left} }->{value} // '???',
		") == ",
		$monkeys{root}->{right}, "(",
		$monkeys{ $monkeys{root}->{right} }->{value} // '???',
		")";
	
	# copy to the other side
	@satisfied = ();
	if (  defined $monkeys{ $monkeys{root}->{left} }->{value} ) {
		$monkeys{ $monkeys{root}->{right} }->{value} = $monkeys{ $monkeys{root}->{left} }->{value};
		push @satisfied, $monkeys{root}->{right};
	}
	else {
		$monkeys{ $monkeys{root}->{left} }->{value} = $monkeys{ $monkeys{root}->{right} }->{value};
		push @satisfied, $monkeys{root}->{left};
	}

	# now work backwards
	while ( not defined $monkeys{humn}->{value} ) {
		my @newly_satisfied = ();
		
		foreach my $sm ( @satisfied ) {
			my $src = $monkeys{$sm};
			
			my $result = $src->{value};
			my $left_value = $monkeys{ $src->{left} }->{value};
			my $right_value = $monkeys{ $src->{right} }->{value};
			
			if ( defined $left_value and not defined $right_value ) {
				$right_value = inverse_for_right( $src->{op}, $left_value, $result );
				$monkeys{ $src->{right} }->{value} = $right_value;
				push @newly_satisfied, $src->{right};
			}
			elsif ( not defined $left_value and defined $right_value ) {
				$left_value = inverse_for_left( $src->{op}, $right_value, $result );
				$monkeys{ $src->{left} }->{value} = $left_value;
				push @newly_satisfied, $src->{left};
			}
			else {
				die "???";
			}
		}
		
		@satisfied = @newly_satisfied;
	}
}

my $humn_value = $monkeys{humn}->{value};

reset_values();
$monkeys{humn}->{value} = $humn_value;

say "possible human value $humn_value";
say "CHECK";
part1();

sub evaluate {
	my ( $left, $op, $right ) = @_;
	
	my $result = eval "$left $op $right";
	
	if ( $op ne '==' ) {
		my $inv_left = inverse_for_left( $op, $right, $result );
		die "inverse left fail! $left != $inv_left for $left $op $right = $result" if $left != $inv_left;	

		my $inv_right = inverse_for_right( $op, $left, $result );
		die "inverse right fail! $right != $inv_right for $left $op $right = $result" if $right != $inv_right;
	}
	
	return $result;
}

sub inverse_for_left {
	my ( $op, $right, $result ) = @_;
	if ( $op eq '+' ) {
		# result = left + right
		return $result - $right;
	}
	elsif ( $op eq '-' ) {
		# result = left - $right
		return $result + $right;
	}
	elsif ( $op eq '*' ) {
		# result = left * right;
		return $result / $right;
	}
	elsif ( $op eq '/' ) {
		# result = left / right;
		return $result * $right;
	}
	else {
		die "bad op $op for left inverse";
	}
}

sub inverse_for_right {
	my ( $op, $left, $result ) = @_;
	if ( $op eq '+' ) {
		# result = left + right
		return $result - $left;
	}
	elsif ( $op eq '-' ) {
		# result = left - $right
		return $left - $result;
	}
	elsif ( $op eq '*' ) {
		# result = left * right;
		return $result / $left;
	}
	elsif ( $op eq '/' ) {
		# result = left / right;
		return $left / $result;
	}
	else {
		die "bad op $op for left inverse";
	}
}

sub part1 {	
	while ( not defined $monkeys{root}->{value} ) {
		die Dumper \%monkeys unless scalar @satisfied;
		
		my @newly_satisfied = ();
		
		foreach my $sm ( @satisfied ) {
			my $src = $monkeys{$sm};
			foreach my $dst ( @{ $src->{supplies} } ) {
				my $ln = $monkeys{$dst}->{left};
				my $op = $monkeys{$dst}->{op};
				my $rn = $monkeys{$dst}->{right};
				my $lv = $monkeys{$ln}->{value};
				my $rv = $monkeys{$rn}->{value};
				
				if ( defined $lv and defined $rv ) {
					$monkeys{$dst}->{value} = evaluate( $lv, $op, $rv );
					push @newly_satisfied, $dst;
				}
			}
		}
		
		@satisfied = @newly_satisfied;
	}

	say "root monkey has value $monkeys{root}->{value}";
}

sub reset_values {
	foreach my $m ( keys %monkeys ) {
		delete $monkeys{$m}->{value} if defined $monkeys{m}->{op};
	}
}