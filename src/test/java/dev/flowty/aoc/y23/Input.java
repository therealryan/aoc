package dev.flowty.aoc.y23;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Input {

	public static String[] linesOf( String data ) {
		return data.split( "\n" );
	}

	public static String[] linesFrom( String name ) {
		try {
			return linesOf( new String(
					Files.readAllBytes( Paths.get( "src/test/resources/y23", name ) ), UTF_8 ) );
		}
		catch( IOException e ) {
			throw new UncheckedIOException( e );
		}
	}
}
