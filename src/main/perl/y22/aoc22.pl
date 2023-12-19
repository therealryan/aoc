use 5.014;
use strict;
use warnings;
use Data::Dumper;
$Data::Dumper::Sortkeys = 1;
use Term::Cap;
use Time::HiRes qw(usleep);

my $terminal = Term::Cap->Tgetent( { OSPEED => 9600 } );
my $clear_screen = $terminal->Tputs('cl');

my $layout = {};
$layout->{1}->{test} = { 
	a => {
		x => 2,
		y => 0,
		U => { dst => 'e', transform => \&top_to_bottom },
		D => { dst => 'd', transform => \&bottom_to_top },
		L => { dst => 'a', transform => \&left_to_right },
		R => { dst => 'a', transform => \&right_to_left },
	},
	b => {
		x => 0,
		y => 1,
		U => { dst => 'b', transform => \&top_to_bottom },
		D => { dst => 'b', transform => \&bottom_to_top },
		L => { dst => 'd', transform => \&left_to_right },
		R => { dst => 'c', transform => \&right_to_left },
	},
	c => {
		x => 1,
		y => 1,
		U => { dst => 'c', transform => \&top_to_bottom },
		D => { dst => 'c', transform => \&bottom_to_top },
		L => { dst => 'b', transform => \&left_to_right },
		R => { dst => 'd', transform => \&right_to_left },
	},
	d => {
		x => 2,
		y => 1,
		U => { dst => 'a', transform => \&top_to_bottom },
		D => { dst => 'e', transform => \&bottom_to_top },
		L => { dst => 'c', transform => \&left_to_right },
		R => { dst => 'b', transform => \&right_to_left },
	},
	e => {
		x => 2,
		y => 2,
		U => { dst => 'd', transform => \&top_to_bottom },
		D => { dst => 'a', transform => \&bottom_to_top },
		L => { dst => 'f', transform => \&left_to_right },
		R => { dst => 'f', transform => \&right_to_left },
	},
	f => {
		x => 3,
		y => 2,
		U => { dst => 'f', transform => \&top_to_bottom },
		D => { dst => 'f', transform => \&bottom_to_top },
		L => { dst => 'e', transform => \&left_to_right },
		R => { dst => 'e', transform => \&right_to_left },
	},
};
$layout->{1}->{actual} = { 
	a => {
		x => 1,	y => 0,
		U => { dst => 'e', transform => \&top_to_bottom },
		D => { dst => 'c', transform => \&bottom_to_top },
		L => { dst => 'b', transform => \&left_to_right },
		R => { dst => 'b', transform => \&right_to_left },
	},
	b => {
		x => 2,	y => 0,
		U => { dst => 'b', transform => \&top_to_bottom },
		D => { dst => 'b', transform => \&bottom_to_top },
		L => { dst => 'a', transform => \&left_to_right },
		R => { dst => 'a', transform => \&right_to_left },
	},
	c => {
		x => 1,	y => 1,
		U => { dst => 'a', transform => \&top_to_bottom },
		D => { dst => 'e', transform => \&bottom_to_top },
		L => { dst => 'c', transform => \&left_to_right },
		R => { dst => 'c', transform => \&right_to_left },
	},
	d => {
		x => 0,	y => 2,
		U => { dst => 'f', transform => \&top_to_bottom },
		D => { dst => 'f', transform => \&bottom_to_top },
		L => { dst => 'e', transform => \&left_to_right },
		R => { dst => 'e', transform => \&right_to_left },
	},
	e => {
		x => 1,	y => 2,
		U => { dst => 'c', transform => \&top_to_bottom },
		D => { dst => 'a', transform => \&bottom_to_top },
		L => { dst => 'd', transform => \&left_to_right },
		R => { dst => 'd', transform => \&right_to_left },
	},
	f => {
		x => 0,	y => 3,
		U => { dst => 'd', transform => \&top_to_bottom },
		D => { dst => 'd', transform => \&bottom_to_top },
		L => { dst => 'f', transform => \&left_to_right },
		R => { dst => 'f', transform => \&right_to_left },
	},
};
$layout->{2}->{test} = { 
	a => {
		x => 2, y => 0,
		U => { dst => 'b', transform => \&top_to_top },
		D => { dst => 'd', transform => \&bottom_to_top },
		L => { dst => 'c', transform => \&left_to_top },
		R => { dst => 'f', transform => \&right_to_right },
	},
	b => {
		x => 0,	y => 1,
		U => { dst => 'a', transform => \&top_to_top },
		D => { dst => 'c', transform => \&bottom_to_right },
		L => { dst => 'f', transform => \&left_to_bottom },
		R => { dst => 'c', transform => \&right_to_left },
	},
	c => {
		x => 1,	y => 1,
		U => { dst => 'a', transform => \&top_to_left },
		D => { dst => 'e', transform => \&bottom_to_right },
		L => { dst => 'b', transform => \&left_to_right },
		R => { dst => 'd', transform => \&right_to_bottom },
	},
	d => {
		x => 2,	y => 1,
		U => { dst => 'a', transform => \&top_to_bottom },
		D => { dst => 'e', transform => \&bottom_to_top },
		L => { dst => 'c', transform => \&left_to_right },
		R => { dst => 'f', transform => \&right_to_top },
	},
	e => {
		x => 2,	y => 2,
		U => { dst => 'd', transform => \&top_to_bottom },
		D => { dst => 'b', transform => \&bottom_to_bottom },
		L => { dst => 'c', transform => \&left_to_bottom },
		R => { dst => 'f', transform => \&right_to_left },
	},
	f => {
		x => 3,	y => 2,
		U => { dst => 'd', transform => \&top_to_right },
		D => { dst => 'b', transform => \&bottom_to_right },
		L => { dst => 'e', transform => \&left_to_right },
		R => { dst => 'a', transform => \&right_to_right },
	},
};
$layout->{2}->{actual} = { 
	a => {
		x => 1,	y => 0,
		U => { dst => 'f', transform => \&top_to_left },
		D => { dst => 'c', transform => \&bottom_to_top },
		L => { dst => 'd', transform => \&left_to_left },
		R => { dst => 'b', transform => \&right_to_left },
	},
	b => {
		x => 2,	y => 0,
		U => { dst => 'f', transform => \&top_to_bottom },
		D => { dst => 'c', transform => \&bottom_to_right },
		L => { dst => 'a', transform => \&left_to_right },
		R => { dst => 'e', transform => \&right_to_right },
	},
	c => {
		x => 1,	y => 1,
		U => { dst => 'a', transform => \&top_to_bottom },
		D => { dst => 'e', transform => \&bottom_to_top },
		L => { dst => 'd', transform => \&left_to_top },
		R => { dst => 'b', transform => \&right_to_bottom },
	},
	d => {
		x => 0,	y => 2,
		U => { dst => 'c', transform => \&top_to_left },
		D => { dst => 'f', transform => \&bottom_to_top },
		L => { dst => 'a', transform => \&left_to_left },
		R => { dst => 'e', transform => \&right_to_left },
	},
	e => {
		x => 1,	y => 2,
		U => { dst => 'c', transform => \&top_to_bottom },
		D => { dst => 'f', transform => \&bottom_to_right },
		L => { dst => 'd', transform => \&left_to_right },
		R => { dst => 'b', transform => \&right_to_right },
	},
	f => {
		x => 0,	y => 3,
		U => { dst => 'd', transform => \&top_to_bottom },
		D => { dst => 'b', transform => \&bottom_to_top },
		L => { dst => 'a', transform => \&left_to_top },
		R => { dst => 'e', transform => \&right_to_bottom },
	},
};


# map from dir to index increments
my $dirs = { 
	U => { ri => -1, ci => 0 },
	D => { ri => 1, ci => 0 },
	L => { ri => 0, ci => -1 },
	R => { ri => 0, ci => 1 },
};

my $dir_scores = {
	U => 3,
	D => 1,
	L => 2,
	R => 0,
};

# map from dir to turn to new dir
my $turns = { 
	U => { L => 'L', R => 'R' },
	D => { L => 'R', R => 'L' },
	L => { L => 'D', R => 'U' },
	R => { L => 'U', R => 'D' },
};

my $grid = [];
my @moves = ();
my $trail = {};

while ( my $line = <> ) {
	chomp $line;
	
	if ( $line =~ m/[ .#]+/ ) {
		push @{ $grid }, [ split '', $line ];
	}
	elsif ( $line eq '' ) {
		# the div line
	}
	elsif ( $line =~ m/[0-9RL]+/ ) {
		while ( $line =~ m/([RL])?(\d+)([RL])?/g ) {
			push @moves, grep { defined } ( $1, $2, $3 );
		}
	}
	else {
		say "bad line '$line'";
	}
}

my $part = 2;
my $input = scalar( @$grid ) < 50 ? 'test' : 'actual';
my $edge = $input eq 'test' ? 4 : 50;
my $structure = $layout->{$part}->{$input};
my $face = 'a';
my $row = 0;
my $col = 0;
my $dir = 'R';

visited( $row, $col, $face );

draw();
say "initial";

my $i = 0;
foreach my $m ( @moves ) {
	if ( $m =~ m/[LR]/ ) {
		$dir = $turns->{$dir}->{$m};
	}
	elsif ( $m =~ m/\d/ ) {
		( $row, $col, $dir, $face ) = move( $row, $col, $dir, $face, $m );
	}
	else {
		say "bad move '$m'";
	}
	#draw();
	# say "After $m\n---\n";
}

draw();
say "final";

my ( $gr, $gc ) = to_global( $row, $col, $face );
my $score = 1000 * ( $gr + 1 ) + 4 * ( $gc + 1 ) + $dir_scores->{$dir};

say "part $part score = $score";

sub move {
	my ( $r, $c, $dir, $face, $distance ) = @_;
	
	while ( $distance > 0 && next_content( $r, $c, $dir, $face ) ne '#' ) {
		( $r, $c, $dir, $face ) = next_coords( $r, $c, $dir, $face );
		visited( $r, $c, $face );
		$distance--;
	}
	
	return ( $r, $c, $dir, $face );
}

# find the row and column of the space in front of us
sub next_coords {
	my ( $r, $c, $d, $f ) = @_;
	my ( $gc, $gr );

	my $nr = $r + $dirs->{$d}->{ri};
	my $nc = $c + $dirs->{$d}->{ci};
	my $nd = $d;
	my $nf = $f;
	
	my $transform;
	my $dst;
	if ( $nr < 0 ) {
		$transform = $structure->{$nf}->{U}->{transform};
		$dst = $structure->{$nf}->{U}->{dst};
	}
	elsif ( $nr >= $edge ) {
		$transform = $structure->{$nf}->{D}->{transform};
		$dst = $structure->{$nf}->{D}->{dst};
	}
	elsif ( $nc < 0 ) {
		$transform = $structure->{$nf}->{L}->{transform};
		$dst = $structure->{$nf}->{L}->{dst};
	}
	elsif ( $nc >= $edge ) {
		$transform = $structure->{$nf}->{R}->{transform};
		$dst = $structure->{$nf}->{R}->{dst};
	}
	
	if ( defined $transform ) {
		( $nr, $nc, $nd ) = $transform->( $nr, $nc, $nd, $edge );
		$nf = $dst;
	}

	return ( $nr, $nc, $nd, $nf );
}

# finds the value of the space in front of us
sub next_content {
	my ( $r, $c, $d, $f ) = @_;
	my ( $nr, $nc, $nd, $nf ) = next_coords( $r, $c, $d, $f );
	return sample( $nr, $nc, $nf );
}

sub sample {
	my ( $r, $c, $f ) = @_;
	my ( $gr, $gc ) = to_global( $r, $c, $f );
	my $v = $grid->[$gr]->[$gc];
	die 'bad coords' unless $v eq '#' or $v eq '.';
	return $v;
}

sub visited {
	my ( $r, $c, $f ) = @_;
	my ( $gr, $gc ) = to_global( $r, $c, $f );
	$trail->{$gr}->{$gc} = '+';
}

# turns face-local coordinates to global
sub to_global {
	my ( $r, $c, $f ) = @_;
	return (
		$structure->{$f}->{y} * $edge + $r,
		$structure->{$f}->{x} * $edge + $c );
}

sub rev {
	my ( $dir ) = @_;
	return left( left ( $dir ) );
}

sub left {
	my ( $dir ) = @_;
	return $turns->{$dir}->{L};
}
sub right {
	my ( $dir ) = @_;
	return $turns->{$dir}->{R};
}

sub bottom_to_bottom {
	my ( $r, $c, $d, $e ) = @_;
	return ( $r - 1, $e - 1 - $c, rev( $d ) );
}
sub bottom_to_right {
	my ( $r, $c, $d, $e ) = @_;
	return ( $c, $r - 1, right( $d ) );
}
sub bottom_to_top {
	my ( $r, $c, $d, $e ) = @_;
	return ( $r - $e, $c, $d );
}

sub left_to_bottom {
	my ( $r, $c, $d, $e ) = @_;
	return ( $e - 2 - $c, $e - 1 - $r, right( $d ) );
}
sub left_to_left {
	my ( $r, $c, $d, $e ) = @_;
	return ( $e - 1 - $r, $c + 1, rev( $d ) );
}
sub left_to_right {
	my ( $r, $c, $d, $e ) = @_;
	return ( $r, $c + $e, $d );
}
sub left_to_top {
	my ( $r, $c, $d, $e ) = @_;
	return ( $c + 1, $r, left( $d ) );
}

sub top_to_bottom {
	my ( $r, $c, $d, $e ) = @_;
	return ( $r + $e, $c, $d );
}
sub top_to_left {
	my ( $r, $c, $d, $e ) = @_;
	return ( $c, $r+1, right( $d ) );
}
sub top_to_right {
	my ( $r, $c, $d, $e ) = @_;
	return ( $e - 1 - $c, $e + $r, right( $d ) );
}
sub top_to_top {
	my ( $r, $c, $d, $e ) = @_;
    return ( $r + 1, $e - 1 - $c, rev( $d ) );
}

sub right_to_bottom {
	my ( $r, $c, $d, $e ) = @_;
	return ( $c - 1, $r, left( $d ) );
}
sub right_to_left {
	my ( $r, $c, $d, $e ) = @_;
	return ( $r, $c - $e, $d );
}
sub right_to_right {
	my ( $r, $c, $d, $e ) = @_;
	return ( $e - 1 - $r, $c - 1, rev( $d ) );
}
sub right_to_top {
	my ( $r, $c, $d, $e ) = @_;
	return ( $e - $c, $e -1 - $r, right( $d ) );
}


sub draw {
	my $screen = '';#$clear_screen;
	my $extent = 2000;
	my $minr = $row - $extent;
	my $maxr = $row + $extent;
	$minr = $minr < 0 ? 0 : $minr;
	$maxr = $maxr > scalar( @$grid ) ? scalar( @$grid ) : $maxr;
	
	my ( $my_r, $my_c ) = to_global( $row, $col, $face );
	
	for ( my $r = $minr; $r < $maxr; $r++ ) {
		my $minc = $col - $extent;
		my $maxc = $col + $extent;
		$minc = $minc < 0 ? 0 : $minc;
		$maxc = $maxc > scalar( @{ $grid->[$r] } ) ? scalar( @{ $grid->[$r] } ) : $maxc;
		for ( my $c = $minc; $c < $maxc; $c++ ) {
			my $v = $trail->{$r}->{$c} // $grid->[$r]->[$c] // ' ';
			
			if ( $r == $my_r && $c == $my_c ) {
				#$v = $dir;
			}
			
			$screen .= $v;
		}
		$screen .= "\n";
	}
	
	usleep 10000;
	say $screen;
}

