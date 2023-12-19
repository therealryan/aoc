use 5.014;
use strict;
use warnings;

my $total = 0;

while( my $line = <> ) {
  chomp $line;
  if( $line =~ m/(\d+)-(\d+),(\d+)-(\d+)/ ) {
    if( $2 < $3 || $4 < $1 ) {
    }
    else {
      $total++;
    }
  }
  else {
    say "bad line '$line'";
  }
}

say $total;
