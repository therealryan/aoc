use 5.014;
use strict;
use warnings;
use Data::Dumper;
$Data::Dumper::Sortkeys = 1;
use POSIX qw/ceil/;

my %blueprints = ();
my %max_costs = ();

while ( my $line = <> ) {
	chomp $line;

	if ( $line =~ m/Blueprint (\d+): (.*)/ ) {
		my $bid = $1;
		my $rbts = $2;
		while ( $rbts =~ m/Each (\S*) robot costs ([^\.]+)./g ) {
			my $type = $1;
			my $costs = $2;
			while ( $costs =~ m/(\d+) (\S+)/g ) {
				$blueprints{$bid}->{$type}->{$2} = $1;
				$max_costs{$2} = max( $max_costs{$2} // 0, $1 );
			}
		}
	}
	else {
		say "bad line '$line'";
	}
}

say Dumper( \%blueprints );
say Dumper( \%max_costs );

part1();
part2();

1;

sub part1 {
	my $quality = 0;
	foreach my $id ( sort { $a <=> $b } keys %blueprints ) {
		say "assessing blueprint $id";
		my ( $g, $h ) = most_geodes( $blueprints{$id}, 24, {}, { ore => 1 }, '', { best => 0 } );
		say "blueprint $id yields $g geodes from '$h'";
		# say explain( $blueprints{$id}, $h );
		$quality += $g * $id;
	}
	say "PART 1\n---\nTotal quality level = $quality";
}

sub part2 {
	my $product = 1;
	foreach my $id ( 1 .. 3 ) {
		say "assessing blueprint $id";
		my ( $g, $h ) = most_geodes( $blueprints{$id}, 32, {}, { ore => 1 }, '', { best => 0 } );
		say "blueprint $id yields $g geodes from '$h'";
		# say explain( $blueprints{$id}, $h );
		$product *= $g;
	}
	say "PART 2\n---\nGeode product = $product";
}

sub most_geodes {
	my ( $costs, $time, $stock, $robots, $history, $best ) = @_;
	
	if ( max_geodes( $time, $stock, $robots ) < $best->{best} ) {
		# no chance of improvement on this branch, quit now.
		return ( 0, 'quit' );
	}
	
	my ( $best_geodes, $best_history ) = ( 0, '???' );
	
	foreach my $type ( qw( geode obsidian clay ore ) ) {
		if ( worthwhile( $time, $robots, $type )
		  && feasible( $costs, $robots, $type ) ) {
			my $d = delay( $costs, $stock, $robots, $type );
			if ( $d < $time ) {
				my $t = $time;
				my $h = $history;
				my $s = { %$stock };
				my $r = { %$robots };
				$h .= ( ' ' x ( $d - 1 ) ) . substr $type, 1, 1;
				while ( $d > 0 ) {
					# mine
					foreach my $type ( keys %{ $robots } ) {
						$s->{$type} += $r->{$type};
					}
					$d--;
					$t--;
				}
				build( $costs, $s, $r, $type );
				
				my ( $rg, $rh ) = most_geodes( $costs, $t, $s, $r, $h, $best );
				
				if ( $rg > $best_geodes ) {
					$best_geodes = $rg;
					$best_history = $rh;
				}
			}
		}
	}
	
	# no point building new robots, just spend the time mining
	if ( $best_history eq '???' ) {
		$best_history = $history;
		while ( $time > 0 ) {
			foreach my $type ( keys %{ $robots } ) {
				$stock->{$type} += $robots->{$type};
			}
			$best_history .= ' ';
			$time--;
		}
		$best_geodes = $stock->{geode} // 0;
	}

	if ( $best_geodes > $best->{best} ) {
		$best->{best} = $best_geodes;
	}
	
	return ( $best_geodes, $best_history );
}

# returns 1 if it will be possible to build a robot of the
# supplied type at some point in the future with current
# mining capacity
sub feasible {
	my ( $costs, $robots, $type ) = @_;
	foreach my $o ( keys %{ $costs->{$type} } ) {
		if ( ( $robots->{$o} // 0 ) == 0 ) {
			return 0;
		}
	}
	return 1;
}

# returns 1 if it's worthwhile to build a robot of the supplied
# type, given current mining capacity, in the time remaining
sub worthwhile {
	my ( $time, $robots, $type ) = @_;
	
	if ( $type eq 'geode' ) {
		# never enough geodes
	}
	elsif ( ( $robots->{$type} // 0 ) >= ( $max_costs{$type} // 0 ) ) {
		# we're no longer resource-constrained on this
		# type, no need to add more mining capacity
		return 0;
	}
	
	if ( $type eq 'geode' ) {
		# no point building a geode robot in the last
		# minute - it won't have time to crack anything
		return $time > 1;
	}
	elsif ( $type eq 'obsidian' ) {
		# the only use for obsidian is to build geode crackers
		# but there's no time to use any increased obsidian capacity
		# now - we'd not end up with any more cracked geodes
		return $time > 2;
	}
	elsif ( $type eq 'clay' ) {
		# you get the idea
		return $time > 3;
	}
	elsif ( $type eq 'ore' ) {
		# ore also goes into geode crackers though
		return $time > 2;
	}
	else {
		die $type;
	}
}

# returns the number of minutes we'll have to wait before we've
# got a new a robot of the specific type
sub delay {
	my ( $costs, $stock, $robots, $type ) = @_;
	my %short = ();
	foreach my $o ( keys %{ $costs->{$type} } ) {
		my $need = $costs->{$type}->{$o};
		my $have = $stock->{$o} // 0;
		if ( $need > $have ) {
			$short{$o} = $need - $have;
		}
	}

	my $d = 0;
	foreach my $ss ( keys %short ) {
		my $sd = ceil( $short{$ss} / $robots->{$ss} );
		if( $sd > $d ) {
			$d = $sd;
		}
	}
	
	# + 1 for the robot build time
	$d++;
	
	return $d;
}

# Builds a robot of the supplied type, adjusting stock levels
sub build {
	my ( $costs, $stock, $robots, $type ) = @_;
	foreach my $o ( keys %{ $costs->{$type} } ) {
		$stock->{$o} -= $costs->{$type}->{$o};
		die "bad build" if $stock->{$o} < 0;
	}
	$robots->{$type}++;
}

# prints a hash on one line
sub ms {
	my ( $h ) = @_;
	return join ',', map { "$_=$h->{$_}" } sort keys %$h;
}

# returns the theoretical max yield if we assume
# that all resources are unconstrained
sub max_geodes {
	my ( $time, $stock, $robots ) = @_;
	my $gc = $stock->{geode} // 0;
	my $rc = $robots->{geode} // 0;
	
	while ( $time > 0 ) {
		$time--;
		$gc += $rc;
		$rc++;
	}
	
	return $gc;
}

sub max {
	my $max = $_[0];
	foreach my $v ( @_ ) {
		$max = $max < $v ? $v : $max;
	}
	return $max;
}

sub explain {
	my ( $costs, $history ) = @_;
	my $rn = { r => 'ore', l => 'clay', b => 'obsidian', e => 'geode' };
	my $s = {};
	my $r = { ore => 1 };
	for ( my $m = 0; $m < length( $history ); $m++ ) {
		my $a = substr $history, $m, 1;
		my $desc = "== minute " . ( $m + 1 ) . " ==\n";
		
		foreach my $robot ( keys %$r ) {
			$s->{$robot} += $r->{$robot};
		}
		
		my $mining = join "\n", map { $r->{$_} 
		                 . " $_ robot collects " 
						 . $r->{$_} . " " . $_ 
						 . "; you now have " 
						 . $s->{$_} . " " . $_ } grep { defined $r->{$_} } qw ( ore clay obsidian geode );
		
		if ( $a eq ' ' ) {
			$desc .= "$mining\n";
		}
		else {
			my $robot = $rn->{$a};
			$desc .= "Spend ";
			foreach my $ot ( sort keys %{ $costs->{$robot} } ) {
				$desc .= $costs->{$robot}->{$ot} . " $ot, ";
				$s->{$ot} -= $costs->{$robot}->{$ot};
			}
			$desc .= "to build a $robot robot\n";
			$mining = join "\n", map { $r->{$_} 
		                 . " $_ robot collects " 
						 . $r->{$_} . " " . $_ 
						 . "; you now have " 
						 . $s->{$_} . " " . $_ } grep { defined $r->{$_} } qw ( ore clay obsidian geode );
			$r->{$robot}++;
			$desc .= $mining;
			$desc .= "\nThe new $robot-collecting robot is ready, you now have $r->{$robot} of them\n";
		}
		
		say $desc;
	}
}

1;