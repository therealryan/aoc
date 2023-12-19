use 5.014;
use strict;
use warnings;
use Data::Dumper;

my @stack = ();
my %dirs = ();

while ( my $line = <> ) {
	chomp $line;
	if ( $line =~ m/\$ cd (\S+)/ ) {
		if ( $1 eq '/' ) {
			@stack = ();
		}
		elsif ( $1 eq '..' ) {
			pop @stack;
		}
		else {
			push @stack, $1;
		}
	}
	elsif ( $line eq '$ ls' ) {
		# ???
	}
	elsif ( $line =~ m/dir (\S+)/ ){
		# ???
	}
	elsif ( $line =~ m/(\d+) (\S+)/ ) {
		add_size( $1, @stack );
	}
	else {
		say "bad line '$line'";
	}
}

my $total = 0;
$total += $_ foreach grep { $_ <= 100000 } values %dirs;

say "Sum of dirs smaller than 100000 is $total";

my $root = $dirs{''};
my $available = 70000000 - $root;
my $required = 30000000 - $available;

say( "Smallest dir to delete to make sufficent space has size " . ( sort { $a <=> $b } grep { $_ > $required } values %dirs )[0] );

1;

sub add_size {
	my ( $size, @stack ) = @_;
	
	$dirs{ join('/', @stack) } += $size;
	if ( scalar @stack ) {
		pop @stack;
		add_size( $size, @stack );
	}
}
