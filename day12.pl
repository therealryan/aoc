use 5.032;
use strict;
use warnings;
use Data::Dumper;

my $scale = 5; # 1 for part 1
my $count = 0;

while ( my $line = <> ) {
  chomp $line;
  next unless $line;
  next if '// ' eq substr $line, 0, 3;
  $count += count_arrangements( $line );
}

say "Total = $count";

1;

sub count_arrangements {
  my ( $line ) = @_;
  my ( $condition, $group_text ) = split ' ', $line;
  my @groups = split ',', $group_text;

  my $exc = '';
  my @exg = ();
  for ( my $i = 0; $i < $scale; $i++ ) {
    $exc .= ( $i != 0 ? '?' : '' ) . $condition;
    @exg = ( @exg, @groups );
  }

  my $memo = {};
  my $c = count( $exc, \@exg, 0, 0, 0, $memo );
  say "$c\t$line";
  die $line if $c == 0;
  return $c;
}

sub count {
  my ( $condition, $groups, $ci, $gi, $length, $memo ) = @_;
  my $key = "$ci,$gi,$length";
  return $memo->{$key} if defined $memo->{$key};

  if( $ci == length $condition ) {
    if ( $gi == scalar @$groups - 1 && $length == $groups->[$gi] ) {
      # ended on a #
      return 1;
    }
    if ( $gi == scalar @$groups && $length == 0 ) {
      # ended on a .
      return 1;
    }
    return 0;
  }

  my $count = 0;
  my $c = substr $condition, $ci, 1;

  if ( $c eq '.' || $c eq '?' ) {
    if ( $length > 0 ) {
      # we've left a group
      if ( $gi < scalar @$groups && $length == $groups->[$gi] ) {
        # it matched! proceed!
        $count += count( $condition, $groups, $ci+1, $gi+1, 0, $memo );
      }
    }
    else {
      $count += count( $condition, $groups, $ci+1, $gi, 0, $memo );
    }
  }
  
  if ( $c eq '#' || $c eq '?' ) {
    $count += count( $condition, $groups, $ci+1, $gi, $length + 1, $memo );
  }

  $memo->{ $key } = $count;
  return $count;
}

