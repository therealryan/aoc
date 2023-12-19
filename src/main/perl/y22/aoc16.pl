use 5.014;
use strict;
use warnings;
use Data::Dumper;
$Data::Dumper::Sortkeys = 1;

my $valves = {};
my @worthwhile_valves = ();
my $time = 26;

while ( my $line = <> ) {
	chomp $line;
	if ( $line =~ m/Valve ([A-Z]{2}) has flow rate=(\d+); tunnels? leads? to valves? (.*)/ ) {
		my @tunnels = ();
		my $exits = $3;
		while ( $exits =~ m/([A-Z]{2})/g ) {
			push @tunnels, $1;
		}
		$valves->{$1} = { fr => $2, tn => \@tunnels };
		push @worthwhile_valves, $1 if $2;
	}
	else {
		say "bad line '$line'";
	}
}

# map from source to destination to the time taken to open the destination valve
my $activation_time = {};

foreach my $src ( ( 'AA', @worthwhile_valves ) ) {
	foreach my $dst ( @worthwhile_valves ) {
		if ( $src ne $dst and not defined $activation_time->{$src}->{$dst} ) {
			my $t = travel_time( $src, $dst, {} ) + 1;
			$activation_time->{$src}->{$dst} = $t;
		}
	}
}

my @included = map { 0 } @worthwhile_valves;
my %set_relief = ();
my $best_pair_score = 0;
my $best_pair_name = '???';
my $complete = 0;

while ( not $complete ) {
	my $i = 0;
	while ( $i < scalar @included ) {
		if ( not $included[$i] ) {
			$included[$i] = 1;
			last;
		}
		$included[$i] = 0;
		$i++;
	}
	$complete = $i == scalar( @included ) - 1;
	
	my @in_set = ();
	my @out_set = ();
	for ( $i = 0; $i < scalar( @included ); $i++ ) {
		if ( $included[$i] ) {
			push @in_set, $worthwhile_valves[$i];
		}
		else {
			push @out_set, $worthwhile_valves[$i];
		}
	}
	
	my ( $in_score, $in_name ) = evaluate_set( \%set_relief, @in_set );
	my ( $out_score, $out_name ) = evaluate_set( \%set_relief, @out_set );
	
	my $pair_name = join '   ', sort( $in_name, $out_name );
	my $pair_score = $in_score + $out_score;
	
	if( $pair_score > $best_pair_score ) {
		$best_pair_score = $pair_score;
		$best_pair_name = $pair_name;
	}
	
	say "$pair_name = $pair_score   | best is $best_pair_score from $best_pair_name";
}

sub evaluate_set {
	my ( $results, @set )  = @_;
	my $name = join ',', sort @set;
	
	unless ( defined $results->{$name} ) {
		my ( $r ) = best_release( @set );
		$results->{$name} = $r;
	}
	
	return ( $results->{$name}, $name );
}

sub best_release {
	my @valves = @_;
	
	my @orders = ();
	all_orders( 'AA', { map { $_ => 1 } @valves }, [  ], $time, \@orders );

	my $best_release = 0;
	my $best_sequence;

	foreach my $seq ( @orders ) {
		my $r = release_value( $time, @$seq );
		if( $r > $best_release ) {
			$best_release = $r;
			$best_sequence = $seq;
		}
	}

	return ( $best_release, join ',', @$best_sequence );
}

sub release_value {
	my ( $time, @seq ) = @_;
	
	my $r = 0;
	my $t = 0;
	my $current = 'AA';
	while ( $t < $time && scalar( @seq ) ) {
		my $next = shift @seq;
		my $ac = $activation_time->{$current}->{$next};
		$t += $ac;
		my $relief = ( $time - $t ) * $valves->{$next}->{fr};
		$r += $relief;
		
		$current = $next;
	}
	
	return $r;
}

sub all_orders {
	my ( $start, $dests, $visited, $limit, $routes ) = @_;
	
	if( scalar( keys %$dests ) == 0 ) {
		push @$routes, $visited;
	}
	else {	
		foreach my $dst ( keys %$dests ) {
			my $cost = $activation_time->{$start}->{$dst};
			if ( $cost < $limit ) {
				my %d = %$dests;
				delete $d{$dst};
				my @v = @$visited;
				push @v, $dst;
				all_orders( $dst, \%d, \@v, $limit - $cost, $routes );
			}
			else {
				push @$routes, $visited;
			}
		}
	}
}

sub travel_time {
	my ( $src, $dst, $visited ) = @_;

	if ( $src eq $dst ) {
		return 0;
	}
	
	my $best = 2 * scalar keys %$valves;
	
	my %v = %$visited;
	$v{$src} = 1;
	
	foreach my $neighbour ( @{ $valves->{$src}->{tn} } ) {
		unless ( $v{$neighbour} ) {
			my $t = travel_time( $neighbour, $dst, \%v ) + 1;
			if ( $t < $best ) {
				$best = $t;
			}
		}
	}

	return $best;
}

1;