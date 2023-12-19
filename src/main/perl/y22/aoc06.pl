use 5.014;
use strict;
use warnings;

my $input = <<EOI;
pnnfhnhshrhmhwwmwzmznmnwmwfmfhfjfcjjtgtbggpdgdjjbjrjsjpjrrmddmgmpmddrhddnfnfzfpfvpfpprhhlffmtffqhhdtdcdsswsdwswmmfvvpdprrnnhhhtffnfbbznbznnvdnnbffjrfrbfrbrgbrrntnggrqqwtqwwgjgsswgwqwtwwsvwvbwvwrwlrlppzfzwfzzpmzzhqqzqlzlglzzmrmwrmwwvmwvvnppjfjttlffhjjjsccbggnffqgfgjjnccmdmzmllvnlnznttlvlttvnvgnvvqvmvqqzrqqcgglzzwtztwwmjmzjjnddsffqrqlrrvsvdvldvvlgvlvccdzczcqcpphggtnthhhtbhtttcjtjcjgcjcbbrhbbfrffgjgdgzddcttczzsccbpcpddcpcggmjgjddtcccthccfrccmdmhmddnwddfldffntnptnpttcptcptpfphhmfmwfwmmlblgbgvbvlltqltldttfcfcclgcllmplmlbbjnjzjnzzttnvvgddshddsqddggsqgqddsggdhghjgjhhgchhdmdjmjddgdhghrrphrrpnnqhhjwwqrrcmmslmmszzpgzpzzrmzmznzllnjnnlnbbdvdsdffbpffcmmnznqqcbbzvvjnjvvwqwgqqpnnzwnzwnzwwwlpwpzzfqzfqqwnqnbnfnqnbqqbggqnqdndrrzzlffbbgqgfgrfrqqsddnqnqjjgssqwqwcqcpcrrqppwpjjfnfpffhphwwmcwwznwznnplptlplnlsnlsldslslsffwtwftwtbtdbbjsjcczwwfllwtlwtlwlvwlwrwppsggvcvrrqcrqqvmqvmmbrrsbsfspspjpnnmpmqmcczgzffqmfmtmpppwzppzrpzrzsrrpqrpqpmpvmpvpttbqtqmtmjttqdqgggcppclcjcpcsppctpplpdpcppcmmdzmzddvhvhnhrrldllcwwbnwnssshlhrhthggtmggbjbjwwbvbttjllvrrfggngvngnmmvzzrrmddmcddztztctfccqpcpcqqvqppqcqdcdhhvhssgfgzgwwzmmnssvwwbqbhbnhnphpqqjcqcddfwfttqjtqtlttglljgjbgjgnnsqqvrvffqvqfqbffljjpffssqdsqstqqqldqqmhmsmsqqwtqwqdqgdgjjfbjffgbbrhhqghgppqgpgmpmmfzfhhfrhffgmfggpzgpzzhtzhhlbhhqbbzvvnvqnntptmmbhbdhdwwmjjcnnmsscqcbbtjtvjvwjvvmsmjmtmpmgghttcztzggpddbfbfgbbdsdrsrrqfqjjqfjjhzhtzzmdzzcgzgdzzmvmmfmjmgjmggmppbdppmzpppvfppzhhfsfwfhhpjpmmrjjpssdccjpjpwjppvdpphcpcjcfjfwjfwfjwfjjqcqcwqqqsmmmbbgdgwwpcwwdfdlflrltlgtthfhfjhhlthtddlgdggsjsrrdpdcppgttphpgpwppmpzmmrjmmvjvgvgfglfgllbqlqhllszzlwwhzzdfdcdtctptwtztfzzmjzjtjtrjrcrnrjjmwmnnbddgvgtgsstjsszmpqdmzgqflrbrspjmtzjcrmlzltmhgblghnwqvwwqwzbpnfrpdpblpjgshfccfbjfsnwvvhnjftsdnsgtzzjtzpmtfdvzrhtqpblhwgmqtgpbfvbdmsnrrrvvbstpsznvbbwgjfqjrhdvwvgptpglpfddhddmtglmjlpwlvfpbtbmgbplbzrlpdlvqzcwhbscpszgfstjpfdvfpmljlngrbgrdnnblzqrfpzsdvblpwbtnhdjclldvwvbwcwzfzbdspgwpfqjfbdbrqcshtlvcrdstnzggbwqnzbrfzbpnrtmvpbvdhcvdsdshgtvhfgdzljflppqbwclnvbhbczvrscjhlbgbfvwdjhnjsgmvwhpfgwbbmnndpnglfrmtfdzvqgfjdqfhgrhvpbqndmqnqccgwswwdsqjnbjtjbjdbqgjnmfbdvlnfwbnrdqgvgzzhmmbbdzfdvvpwhpbwbnzdcdpchrwlhfsjnhhjggvplmqggwjdsvjtpnpnqgldjjdcscrdltssjdrpcrfbgbcjfplhzgwbprfcslhpcngtszrghmwhzdqscbfrhzdwcffzvmjrmcjcstfvhplvrsglgsjnjtrpddsdfqjsndjnfmvdhfgdbzzflqhsrrwmrnlpqzmcddqbqvvzgtlztpgjnddtcnbmqsjlhmcszrmcjvwzpptlfqsmpvgnzvrjdwzpdwqgbmdgdtvjlmfczthjbcgfhbqpnmlbmrwwhfptzlbmfdhssznjcvjbmnjtnvzjhzczlrrdnttmmcbnzhqpplzqwgttwrnwfvmnptgqlfrnzvqpjfgrzwmlcwvtptvcvrlsrdwdgqfvffspmdbnnrqjttpqvhvdpbcrvzptwnhhfsqzchmncvttcdgdnlppcfzpmjpvbvqhlvplwvrmmbbggbwttwmvsqjlllsftprsmtmnzjcqfzblrllzgshfljchrjwjlpvhpbrtrsschzltrblgjnbgdnmwdggjhqggntblnhsvfgsbcblhmctbqzqwmhqnjhpzjfqpjdgwpzhczcftfcpdhvzhzccmwmrfrbqshzmtpqgpbbvfqqbjbmvnlnlwjtzrpmhdlffccrqcfgsjfszbrzrfztntchtmgmbhjgmlsqzcbtqqjzzlghtzzqmlnnvsgsvbbjfgqsqbqmqrdzwpwdgbggpdvhvnlzshhntprjdwhnwfvdjzpqgflwrvwgtmfdmfdztcbtfnjdrvgdwwczdgphnvdgrbdchprqldfjrvcsflcmlcmzqvqgsgnzcgmrhccgcmptcdzhbcdgdtppwztfstzqqzqrdzlnzthggjmpcflmbcmdrrjnnpbpqfmjbzqbtsjjgdlmgncbmgspqqvbrvzrdjscpzjsdtcdvsdwqlmwrngttswnrsbqctvhgfnnwblpcqzdmzpfchplslspmghvgcqntmlrfhgpcbpspvfhnvqvglsqzsnsdzddqpbsjhlclslngbwvvgjhwfcncqsmqwbptzvpzlzslsjjjldjpwpfrdlfbjphqcjtsgqdsdfdjhqgdhcppndwmhmmldvvmblcqcqfqhltbcbvrnghjfmtgqwtwljtczvqlnmgscjhqdhnzwhzvzzqnlsrhqvljqpgpwghfqlhjjrrhvnmnnrbnlhdcjctwtlhmhhmhjvcgzdrzmdjrvqzgnsttjdwglgwlcmbcdnjprgfsbbdzzngbqdrvwwwhbtlnnmzqdjttsrrpvlfdqnfhhtdtvmpcjgdwtbnqmwmtszdqfmbhjsjpqqddzfggwjhbtlnqfgcwbjzdtcpcpzgnrmnvwlpgmwfjlpgppdfrfvvjwsfcdqdnpcpjbqsvhttssgptqjghctrbgntlfjzdrfjccsprsjlrrwrzsmnjsqslmpdtrvhlqbnmgpjthpqdqmnvrtzlhhzzfzbrcclpmpcszhbttgrtcpgcpjwpdbfpfvgspsgtvglwthqcmcvmrfmclwlvjlsptfgmtlrnsvjrnfwzhdcsmgztpzfcvzwdztpppvqpvqfpdrsfnlhrbqwrsqjtwjmhnpwmqmpdgdhbtbpfwnmswffdqffdggrdrpmngvpzplmmwlddnhcvjjzqqfsbbtfmzdwnpvbjrshmllczhgvwwcbcbtfrfnplqjwmjlvpwwgfrtffwddwppsgtnlmpvfnhfzcsgjbqbjmbvpnqppsrvwnlzvcmjqgtbzrdsnrgwbfmrvnflgccrssfvcwgllqqbbcthzmbtnsmbzbcczhtzcvmthttpltrtdmgspctvtpvqbhmnnpnjwmhpqclmjsdrbjwvjbtzcjlqbjsvbgdwqzflnwzcfjwtrhjgfshfmwbjfwpnhjsmtpgbpwlfjjnmdlrhchmnfmgmgcrftmwbzshdwbhndgwtjbrrvbwprqppfmgfmfllpcjgrwdmtzddthsjlgjljv
EOI

#$input = 'mjqjpqmgbljsphdztnvjfqwrcgsmlb';
#$input = 'bvwbjplbgvbhsrlpgdmjqwftvncz';
#$input = 'nppdvjthqldpwncqszvftbrmjlhg';
#$input = 'nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg';
#$input = 'zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw';

say "start-of-packet at  " . find_uniq_sequence( $input, 4 );
say "start-of-message at " . find_uniq_sequence( $input, 14 );

sub find_uniq_sequence {
  my ( $input, $length ) = @_;

  my @buffer = ();

  for( my $idx = 0; $idx < length $input; $idx++ ) {
    push @buffer, substr( $input, $idx, 1 );
    while( $length < scalar @buffer ){
      shift @buffer;
    }

    my %uniq = map { $_ => 1 } @buffer;

    if ( scalar( %uniq ) == $length ) {
      my $answer = $idx + 1;
      return "$answer with @buffer";
    }
  }

  return "Nothing found!";
}
