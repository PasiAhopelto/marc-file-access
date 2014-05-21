package ppa.marc.reader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import ppa.marc.RecordReader;
import ppa.marc.domain.Record;
import ppa.marc.reader.TextFileReader;
import junit.framework.TestCase;

import static org.easymock.classextension.EasyMock.*;

public class TextFileReaderTest extends TestCase {
	private static final String SINGLE_LINE = "record";
	private static final String TWO_LINES = SINGLE_LINE + "\n" + SINGLE_LINE;
	private static final Record RECORD = new Record("");

	SpecialLineChecker specialLineChecker = createStrictMock(SpecialLineChecker.class);
	RecordConverter recordConverter = createStrictMock(RecordConverter.class);

	RecordReader reader = new TextFileReader(recordConverter, specialLineChecker);
	
	List<Record> expectedRecords = new ArrayList<Record>();
	
	protected void tearDown() throws Exception {
		verify(recordConverter);
		verify(specialLineChecker);
	}
	
	public void testGrantedTheresNothingToReadThenReturnsEmptyCollection() throws Exception {
		InputStream inputStream = createInputStreamFrom(new String[] { "" });
		replayMocks();
		assertEquals(expectedRecords, reader.read(inputStream));
	}

	public void testGrantedThereAreNoEmptyLinesThenPassesEverythingToRecordConverterAndAddsResultToList() throws Exception {
		assertSingleRecordReadingWithInput(new String[] { SINGLE_LINE });
	}

	public void testGrantedEmptyLinePreceedsRecordThenDropsTheLineBeforePassingItToConverter() throws Exception {
		assertSingleRecordReadingWithInput(new String[] { "", SINGLE_LINE });
	}

	public void testInputHasLineThatStartsWithHashThenItIsIgnored() throws Exception {
		assertSingleRecordReadingWithInput(new String[] { "#", SINGLE_LINE });
	}
	
	public void testGrantedEmptyLineSeparatesTextBlocksThenDropsTheLineAndHandlesTheBlocksAsTwoRecords() throws Exception {
		expectSingleRecord(SINGLE_LINE);
		expectSingleRecord(SINGLE_LINE);
		InputStream inputStream = createInputStreamFrom(new String[] { SINGLE_LINE, "", SINGLE_LINE }); 
		replayMocks();
		assertEquals(expectedRecords, reader.read(inputStream));
	}

	public void testGrantedSpaceLineSeparatesTextBlocksThenDropsTheLineAndHandlesTheBlocksAsTwoRecords() throws Exception {
		expectSingleRecord(SINGLE_LINE);
		expectSingleRecord(SINGLE_LINE);
		InputStream inputStream = createInputStreamFrom(new String[] { SINGLE_LINE, " \t", SINGLE_LINE }); 
		replayMocks();
		assertEquals(expectedRecords, reader.read(inputStream));
	}

	public void testGrantedInputHasTwoNonEmptyLinesThenParsesItToOneRecord() throws Exception {
		expectSingleRecord(TWO_LINES);
		for(int i = 0; i < 2; ++i) {
			expect(specialLineChecker.isRecordSeparator(SINGLE_LINE)).andReturn(false);
			expect(specialLineChecker.isCommentLine(SINGLE_LINE)).andReturn(false);
		}
		replayMocks();
		assertEquals(expectedRecords, reader.read(new ByteArrayInputStream(TWO_LINES.getBytes())));
	}
	
	private void expectSingleRecord(String input) {
		expect(recordConverter.convert(input)).andReturn(RECORD);
		expectedRecords.add(RECORD);
	}
	
	private InputStream createInputStreamFrom(String[] lines) {
		StringBuilder input = new StringBuilder();
		for(String line : lines) {
			expect(specialLineChecker.isRecordSeparator(line)).andReturn(isSpecialLine(line));
			if(!isSpecialLine(line)) expect(specialLineChecker.isCommentLine(line)).andReturn(isCommentLine(line));
			input.append(line);
			input.append("\n");
		}
		return new ByteArrayInputStream(input.toString().getBytes());
	}

	private Boolean isSpecialLine(String line) {
		return Pattern.compile("\\s*").matcher(line).matches();
	}

	private boolean isCommentLine(String line) {
		return line.startsWith("#");
	}

	private void replayMocks() {
		replay(recordConverter);
		replay(specialLineChecker);
	}

	private void assertSingleRecordReadingWithInput(String[] input) throws IOException {
		expectSingleRecord(SINGLE_LINE);
		InputStream inputStream = createInputStreamFrom(input);
		replayMocks();
		assertEquals(expectedRecords, reader.read(inputStream));
	}
	
}
