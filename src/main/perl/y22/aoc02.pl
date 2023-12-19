use 5.014;
use strict;
use warnings;

my $choice_scores = {
  X => 1,
  Y => 2,
  Z => 3,
};

my $comparison_scores = {
  A => { X => 3, Y => 6, Z => 0 },
  B => { X => 0, Y => 3, Z => 6 },
  C => { X => 6, Y => 0, Z => 3 },
};

my $strategy = {
  A => { X => 'Z', Y => 'X', Z => 'Y' },
  B => { X => 'X', Y => 'Y', Z => 'Z' },
  C => { X => 'Y', Y => 'Z', Z => 'X' },
};


my $score = 0;

while ( my $line = <> ) {
  if( $line =~ m/([ABC]) ([XYZ])/ ) {
    my $them = $1;
    my $me = $strategy->{$1}->{$2};
    $score += $choice_scores->{$me} + $comparison_scores->{$them}->{$me};
  }
  else {
    say "bad line '$line'";
  }
}

say $score;

