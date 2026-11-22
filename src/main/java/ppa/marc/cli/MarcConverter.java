package ppa.marc.cli;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ppa.marc.MarcFileAccessConfiguration;
import ppa.marc.RecordReader;
import ppa.marc.RecordWriter;
import ppa.marc.domain.Record;

/**
 * Command line utility for converting records between binary UNIMARC and the
 * library's textual format. The result is written to standard output:
 * <pre>
 * java -jar marc-file-access-cli.jar to-text records.marc &gt; records.txt
 * java -jar marc-file-access-cli.jar to-marc records.txt &gt; records.marc
 * </pre>
 */
public class MarcConverter {

	static final int EXIT_OK = 0;
	static final int EXIT_FAILURE = 1;
	static final int EXIT_USAGE = 2;

	enum Direction {
		TO_TEXT("to-text", "marcFileReader", "textFileWriter", "Converts a binary UNIMARC file to text."),
		TO_MARC("to-marc", "textFileReader", "marcFileWriter", "Converts a text file to binary UNIMARC.");

		final String argument;
		final String readerBeanName;
		final String writerBeanName;
		final String description;

		Direction(String argument, String readerBeanName, String writerBeanName, String description) {
			this.argument = argument;
			this.readerBeanName = readerBeanName;
			this.writerBeanName = writerBeanName;
			this.description = description;
		}

		static Direction fromArgument(String argument) {
			for(Direction direction : values()) {
				if(direction.argument.equals(argument)) return direction;
			}
			return null;
		}
	}

	public static void main(String[] args) {
		System.exit(run(args, System.out, System.err));
	}

	static int run(String[] args, OutputStream out, PrintStream err) {
		if(args.length == 1 && (args[0].equals("-h") || args[0].equals("--help"))) {
			printUsage(new PrintStream(out, true));
			return EXIT_OK;
		}
		if(args.length != 2) {
			err.println("Expected 2 arguments, got " + args.length + ".");
			printUsage(err);
			return EXIT_USAGE;
		}
		Direction direction = Direction.fromArgument(args[0]);
		if(direction == null) {
			err.println("Unknown conversion direction '" + args[0] + "'.");
			printUsage(err);
			return EXIT_USAGE;
		}
		Path inputFile = Path.of(args[1]);
		if(Files.isDirectory(inputFile)) {
			err.println("Input '" + inputFile + "' is a directory, not a file.");
			return EXIT_FAILURE;
		}
		try {
			convert(direction, inputFile, out);
			return EXIT_OK;
		}
		catch(NoSuchFileException e) {
			err.println("Input file '" + inputFile + "' does not exist.");
		}
		catch(IOException e) {
			err.println("Could not convert '" + inputFile + "': " + e.getMessage());
		}
		catch(RuntimeException e) {
			err.println("Input file '" + inputFile + "' is not a valid " + (direction == Direction.TO_TEXT ? "UNIMARC" : "text") + " file: " + e.getMessage());
		}
		return EXIT_FAILURE;
	}

	static void convert(Direction direction, Path inputFile, OutputStream out) throws IOException {
		try(AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MarcFileAccessConfiguration.class)) {
			RecordReader reader = context.getBean(direction.readerBeanName, RecordReader.class);
			RecordWriter writer = context.getBean(direction.writerBeanName, RecordWriter.class);
			List<Record> records;
			try(InputStream input = new BufferedInputStream(Files.newInputStream(inputFile))) {
				records = reader.read(input);
			}
			if(records.isEmpty()) {
				throw new IllegalArgumentException("no records found");
			}
			OutputStream bufferedOut = new BufferedOutputStream(out);
			writer.writeRecords(bufferedOut, records);
			bufferedOut.flush();
		}
	}

	private static void printUsage(PrintStream stream) {
		stream.println("Usage: java -jar marc-file-access-cli.jar <direction> <file>");
		stream.println();
		stream.println("Directions:");
		for(Direction direction : Direction.values()) {
			stream.println(String.format("  %-9s %s", direction.argument, direction.description));
		}
		stream.println();
		stream.println("The result is written to standard output.");
	}
}
