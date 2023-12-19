use 5.014;
use strict;
use warnings;
use Data::Dumper;

my $p = 1;
my %priorities = map { $_ => $p++ } ( 'a'..'z', 'A'..'Z' ) ;

my $total = 0;
my $gc = 0;
my %intersection = ();
while ( my $line = <> ) {
  chomp $line;
  my %a = characters( $line );

  if( $gc == 0 ) {
    %intersection = %a;
  }
  else {
    %intersection = map { $_ => 1 } grep { exists $intersection{$_} } keys %a;
  }

  $gc++;

  if( $gc == 3 ) {
    $total += $_ foreach map { $priorities{$_} } keys %intersection;
    $gc = 0;
  }
}

say $total;

sub characters {
  my ( $str ) = @_;
  my %chars = ();
  for( my $i = 0; $i < length $str; $i++ ) {
    $chars{ substr $str, $i, 1 } = 1;
  }

  return %chars;
}
