package dev.flowty.aoc.y15;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class Day04 {

	static int part1( String input ) {
		return findSuffix( input, "00000" );
	}

	static int part2( String input ) {
		return findSuffix( input, "000000" );
	}

	private static int findSuffix( String input, String prefix ) {
		AtomicInteger result = new AtomicInteger( -1 );
		List<Thread> threads = new ArrayList<>();
		int count = 16;
		for( int i = 0; i < count; i++ ) {
			int start = i;
			threads.add( new Thread( () -> search( input, prefix, start, count, result ) ) );
		}
		threads.forEach( Thread::start );
		threads.forEach( t -> {
			try {
				t.join();
			}
			catch( InterruptedException e ) {
				Thread.currentThread().interrupt();
				throw new IllegalStateException( e );
			}
		} );
		return result.get();
	}

	private static void search( String input, String prefix, int start, int increment,
			AtomicInteger result ) {
		try {
			MessageDigest digest = MessageDigest.getInstance( "MD5" );
			int i = start;

			while( result.get() == -1 || result.get() > i ) {
				String hex = bytesToHex( digest.digest( (input + i).getBytes( UTF_8 ) ) );
				if( hex.startsWith( prefix ) && (result.get() == -1 || i < result.get()) ) {
					result.set( i );
				}
				i += increment;
			}
		}
		catch( Exception e ) {
			throw new IllegalStateException( e );
		}
	}

	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex( byte[] bytes ) {
		char[] hexChars = new char[bytes.length * 2];
		for( int j = 0; j < bytes.length; j++ ) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}
		return new String( hexChars );
	}
}
