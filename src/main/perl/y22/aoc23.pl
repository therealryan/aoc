use 5.014;
use strict;
use warnings;
use Data::Dumper;
$Data::Dumper::Sortkeys = 1;
use Term::Cap;
use Time::HiRes qw(usleep);

my $terminal = Term::Cap->Tgetent( { OSPEED => 9600 } );
my $clear_screen = $terminal->Tputs('cl');

my %neighbours = (
	NW => { x => -1, y => -1 },
	N  => { x =>  0, y => -1 },
	NE => { x =>  1, y => -1 },
	E  => { x =>  1, y =>  0 },
	SE => { x =>  1, y =>  1 },
	S  => { x =>  0, y =>  1 },
	SW => { x => -1, y =>  1 },
	W  => { x => -1, y =>  0 },
);

my %dir_checks = (
	N => [ map { $neighbours{$_} } qw( NW N NE ) ],
	S => [ map { $neighbours{$_} } qw( SW S SE ) ],
	W => [ map { $neighbours{$_} } qw( NW W SW ) ],
	E => [ map { $neighbours{$_} } qw( NE E SE ) ],
);

my @dir_order = qw( N S W E );
my $dir_index = 0;
my @elves = ();

my $y = 0;
while ( my $line = <> ) {
	chomp $line;
	for ( my $x = 0; $x < length $line; $x ++ ) {
		if ( '#' eq substr $line, $x, 1  ) {
			push @elves, { x => $x, y => $y };
		}
	}
	$y++;
}

print_field("at outset");

my $round = 1;
my $moved = 0;

do {
	$moved = round();
	print_field("at round $round") if $round == 10;
	$round++;
}
while ( $moved );
$round--;
print_field("after completion on round $round");

1;

sub round {
	my $moved = 0;
	# collect current positions
	my %ep = ();
	foreach my $elf ( @elves ) {
		$ep{"$elf->{x},$elf->{y}"} = 1;
	}
	
	# propose moves
	my %proposals = ();
	foreach my $elf ( @elves ) {
		if ( has_neighbour( $elf, \%ep, values %neighbours ) ) {
			for ( my $di = 0; $di < scalar @dir_order; $di++ ) {
				my $dir = $dir_order[ ( $di + $dir_index ) % scalar @dir_order ];
				unless ( has_neighbour( $elf, \%ep, @{ $dir_checks{$dir} } ) ) {
					my $prp = {
						x => $elf->{x} + $neighbours{$dir}->{x},
						y => $elf->{y} + $neighbours{$dir}->{y},
					};
					$elf->{prp} = $prp;
					$proposals{"$prp->{x},$prp->{y}"}++;
					last;
				}
			}
		}
	}
	
	# perform moves
	foreach my $elf ( @elves ) {
		my $prp = $elf->{prp};
		if ( defined $prp && $proposals{"$prp->{x},$prp->{y}"} == 1 ) {
			$elf->{x} = $prp->{x};
			$elf->{y} = $prp->{y};
			$moved = 1;
		}
		delete $elf->{prp};
	}
	
	$dir_index = ( $dir_index + 1 ) % scalar( @dir_order );
	
	return $moved;
}

sub has_neighbour {
	my ( $elf, $ep, @increments ) = @_;
	
	foreach my $ni ( @increments ) {
		my $cp = {
			x => $elf->{x} + $ni->{x},
			y => $elf->{y} + $ni->{y},
		};
		return 1 if $ep->{"$cp->{x},$cp->{y}"};
	}
	
	return 0;
}

sub bounds {
	my $minx = $elves[0]->{x};
	my $miny = $elves[0]->{y};
	my $maxx = $elves[0]->{x};
	my $maxy = $elves[0]->{y};
	
	foreach my $elf ( @elves ) {
		$minx = $minx > $elf->{x} ? $elf->{x} : $minx;
		$miny = $miny > $elf->{y} ? $elf->{y} : $miny;
		$maxx = $maxx < $elf->{x} ? $elf->{x} : $maxx;
		$maxy = $maxy < $elf->{y} ? $elf->{y} : $maxy;
	}
	
	return { min => { x => $minx, y => $miny }, max => { x => $maxx, y => $maxy } };
}

sub print_field {
	my ( $name ) = @_;
	my $b = bounds();
	my $grid = [];
	
	foreach my $elf ( @elves ) {
		$grid->[$elf->{y} - $b->{min}->{y}]->[$elf->{x} - $b->{min}->{x}] = '#';
	}
	
	my $field = '';
	my $empty_count = 0;
	for ( my $y = 0; $y <= $b->{max}->{y} - $b->{min}->{y}; $y++ ) {
		for ( my $x = 0; $x <= $b->{max}->{x} - $b->{min}->{x}; $x++ ) {
			$field .= $grid->[$y]->[$x] // '.';
			$empty_count++ unless defined ( $grid->[$y]->[$x] );
		}
		$field .= "\n";
	}
	
	chomp $field;
	say $field;
	say "$empty_count empty tiles $name";
}

1;