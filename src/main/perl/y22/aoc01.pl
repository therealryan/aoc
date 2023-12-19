use 5.014;
use strict;
use warnings;

my $idx = 0;
my @calorie_totals = ();

while ( my $line = <> ) {
  chomp $line;

  if( $line eq '' ) {
    $idx++;
  }
  else {
    $calorie_totals[$idx] += $line;
  }
}

@calorie_totals = sort @calorie_totals;

say foreach @calorie_totals;


