use 5.032;
use strict;
use warnings;
use Data::Dumper;
use List::Util qw ( min );

my $diff_target = 1; # 0 for part 1
my $score = 0;
my @field_lines = ();

while ( my $line = <> ) {
  chomp $line;
  if ( length $line ) {
    push @field_lines, $line;
  }
  else {
    my $s = score_field( $diff_target, @field_lines );
    @field_lines = ();
    $score += $s;
  }
}

$score += score_field( $diff_target, @field_lines ) if scalar @field_lines;

say "\nTotal = $score";

1;

sub score_field {
  my ( $diff_target, @lines ) = @_;

  my @rows = @lines;
  my @columns = ();

  foreach my $row ( @rows ) {
    for ( my $c = 0; $c < length $rows[0]; $c++ ) {
      $columns[$c] = ( $columns[$c] // '' ) . substr $row, $c, 1;
    }
  }
  my $hr = reflection_index( $diff_target, @rows );
  my $vr = reflection_index( $diff_target, @columns );

  die "multireflect!" if( $hr != 0 && $vr != 0 );

  say join "\n", @lines, "hr = $hr, vr = $vr\n";

  return $vr + 100 * $hr 
}

sub reflection_index {
  my ( $diff_target, @lines ) = @_;

  for ( my $i = 1; $i < scalar @lines; $i++ ) {
    my $lim = min( $i, scalar @lines - $i );
    my $diff = 0;

    for ( my $j = 0; $j < $lim; $j++ ) {
      $diff += diff( $lines[ $i - $j - 1 ], $lines[ $i + $j ] );
    }

    if ( $diff == $diff_target ) {
      return $i;
    }
  }
  return 0;
}

sub diff {
  my ( $a, $b ) = @_;
  my $diff = 0;

  for ( my $i = 0; $i < length $a; $i++ ) {
    if ( substr( $a, $i, 1 ) ne substr( $b, $i, 1 ) ) {
      $diff++;
    }
  }

  return $diff;
}
