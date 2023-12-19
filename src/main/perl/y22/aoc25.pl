use 5.014;
use strict;
use warnings;
use Data::Dumper;
$Data::Dumper::Sortkeys = 1;
use Term::Cap;
use Time::HiRes qw(usleep);

my $terminal = Term::Cap->Tgetent( { OSPEED => 9600 } );
my $clear_screen = $terminal->Tputs('cl');

my $value_map = {
	'2' => 2,
	'1' => 1,
	'0' => 0,
	'-' => -1,
	'=' => -2,
};

my $sum = 0;
while ( my $line = <> ) {
	chomp $line;
	
	my $snafu = $line;
	my $decimal = decode( $snafu );
	$sum += $decimal;
	say "$snafu\t$decimal";
}

say "decimal sum is $sum";
my $snafu = encode( $sum );
say "snafu sum is " . $snafu;

sub decode {
	my ( $snafu ) = @_;
	
	my $value = 0;
	my $ord = 1;
	foreach my $c ( split '', reverse $snafu ) {
		my $v = $value_map->{$c};
		die "bad char $c" unless defined $v;
		$value += $ord * $v;
		$ord *= 5;
	}
	return $value;
}

sub encode {
	my ( $decimal ) = @_;
	
	my @digits = qw( 0 1 2 = - );
	my $snafu = '';
	while( $decimal > 0 ) {		
		my $rem = $decimal % 5;
		my $digit = $digits[$rem];
		$decimal = int( ( $decimal + 2 ) / 5 );
		$snafu = $digit . $snafu;
	}
	return $snafu;

}

1;