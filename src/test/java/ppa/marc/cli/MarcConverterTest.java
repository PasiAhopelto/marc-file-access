package ppa.marc.cli;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import ppa.marc.common.UnimarcConstants;

public class MarcConverterTest {

	private static final String TEST_DATA = "test/data/";

	ByteArrayOutputStream out = new ByteArrayOutputStream();
	ByteArrayOutputStream errBytes = new ByteArrayOutputStream();
	PrintStream err = new PrintStream(errBytes, true);

	@Test
	public void testConvertsMarcFileToText() throws Exception {
		assertEquals(MarcConverter.EXIT_OK, run("to-text", TEST_DATA + "two_records.marc"));
		assertEquals(Files.readString(Path.of(TEST_DATA + "two_records.marc.toString")), out.toString());
		assertEquals("", errBytes.toString());
	}

	@Test
	public void testConvertsTextFileToMarcThatConvertsBackToSameText(@TempDir Path tempDir) throws Exception {
		assertEquals(MarcConverter.EXIT_OK, run("to-marc", TEST_DATA + "two_records.marc.toString"));
		assertEquals("", errBytes.toString());
		Path marcFile = Files.write(tempDir.resolve("converted.marc"), out.toByteArray());
		out.reset();
		assertEquals(MarcConverter.EXIT_OK, run("to-text", marcFile.toString()));
		assertEquals(Files.readString(Path.of(TEST_DATA + "two_records.marc.toString")), out.toString());
	}

	@Test
	public void testConvertsTextFileToMarcWithValidLeader() throws Exception {
		assertEquals(MarcConverter.EXIT_OK, run("to-marc", TEST_DATA + "record.marc.toString"));
		String marc = out.toString();
		assertEquals(marc.length(), Integer.parseInt(marc.substring(0, 5)));
		int baseAddress = Integer.parseInt(marc.substring(12, 17));
		assertEquals(UnimarcConstants.FIELD_TERMINATOR, marc.charAt(baseAddress - 1));
		assertEquals(UnimarcConstants.RECORD_TERMINATOR, marc.charAt(marc.length() - 1));
	}

	@Test
	public void testConvertingMarcToTextAndBackKeepsMarcFileIdentical(@TempDir Path tempDir) throws Exception {
		assertEquals(MarcConverter.EXIT_OK, run("to-text", TEST_DATA + "record.marc"));
		Path textFile = Files.write(tempDir.resolve("converted.txt"), out.toByteArray());
		out.reset();
		assertEquals(MarcConverter.EXIT_OK, run("to-marc", textFile.toString()));
		byte[] original = Files.readAllBytes(Path.of(TEST_DATA + "record.marc"));
		// The text format has no leader, so only the leader's record-specific codes may differ.
		assertArrayEquals(Arrays.copyOfRange(original, 24, original.length), Arrays.copyOfRange(out.toByteArray(), 24, out.size()));
	}

	@Test
	public void testConvertsUtf8MarcFileToText() throws Exception {
		assertEquals(MarcConverter.EXIT_OK, run("to-text", TEST_DATA + "utf8_record.marc"));
		assertArrayEquals(Files.readAllBytes(Path.of(TEST_DATA + "utf8_record.marc.toString")), out.toByteArray());
	}

	@Test
	public void testConvertsUtf8TextFileToMarc() throws Exception {
		assertEquals(MarcConverter.EXIT_OK, run("to-marc", TEST_DATA + "utf8_record.marc.toString"));
		assertArrayEquals(Files.readAllBytes(Path.of(TEST_DATA + "utf8_record.marc")), out.toByteArray());
	}

	@Test
	public void testConvertsMarcFileWithLineBreaksBetweenRecordsToText() throws Exception {
		assertEquals(MarcConverter.EXIT_OK, run("to-text", TEST_DATA + "two_records_line_breaks.marc"));
		assertEquals(Files.readString(Path.of(TEST_DATA + "two_records.marc.toString")), out.toString());
	}

	@Test
	public void testGrantedMarcFileWithTruncatedLastRecordThenFails() throws Exception {
		assertEquals(MarcConverter.EXIT_FAILURE, run("to-text", TEST_DATA + "two_records_truncated.marc"));
		assertTrue(errBytes.toString().contains("is not a valid UNIMARC file: Record 2 is not terminated"));
		assertEquals(0, out.size());
	}

	@Test
	public void testPrintsUsageToStandardOutputWhenHelpIsRequested() throws Exception {
		assertEquals(MarcConverter.EXIT_OK, run("--help"));
		assertTrue(out.toString().startsWith("Usage:"));
	}

	@Test
	public void testGrantedWrongNumberOfArgumentsThenPrintsUsageAndFails() throws Exception {
		assertEquals(MarcConverter.EXIT_USAGE, run("to-text"));
		assertTrue(errBytes.toString().contains("Usage:"));
		assertEquals(0, out.size());
	}

	@Test
	public void testGrantedUnknownDirectionThenPrintsUsageAndFails() throws Exception {
		assertEquals(MarcConverter.EXIT_USAGE, run("sideways", TEST_DATA + "record.marc"));
		assertTrue(errBytes.toString().contains("Unknown conversion direction 'sideways'"));
		assertEquals(0, out.size());
	}

	@Test
	public void testGrantedMissingFileThenFails() throws Exception {
		assertEquals(MarcConverter.EXIT_FAILURE, run("to-text", TEST_DATA + "missing.marc"));
		assertTrue(errBytes.toString().contains("does not exist"));
		assertEquals(0, out.size());
	}

	@Test
	public void testGrantedDirectoryThenFails() throws Exception {
		assertEquals(MarcConverter.EXIT_FAILURE, run("to-text", TEST_DATA));
		assertTrue(errBytes.toString().contains("is a directory"));
	}

	@Test
	public void testGrantedInvalidTextFileThenFails(@TempDir Path tempDir) throws Exception {
		Path invalid = Files.writeString(tempDir.resolve("invalid.txt"), "not a record\n");
		assertEquals(MarcConverter.EXIT_FAILURE, run("to-marc", invalid.toString()));
		assertTrue(errBytes.toString().contains("is not a valid text file"));
		assertEquals(0, out.size());
	}

	@Test
	public void testGrantedTextFileForMarcConversionThenFailsWithUnterminatedRecord() throws Exception {
		assertEquals(MarcConverter.EXIT_FAILURE, run("to-text", TEST_DATA + "record.marc.toString"));
		assertTrue(errBytes.toString().contains("is not a valid UNIMARC file: Record 1 is not terminated"));
		assertEquals(0, out.size());
	}

	@Test
	public void testGrantedEmptyFileThenFailsWithNoRecordsFound(@TempDir Path tempDir) throws Exception {
		Path empty = Files.createFile(tempDir.resolve("empty.txt"));
		assertEquals(MarcConverter.EXIT_FAILURE, run("to-marc", empty.toString()));
		assertTrue(errBytes.toString().contains("no records found"));
	}

	private int run(String... args) {
		return MarcConverter.run(args, out, err);
	}
}
