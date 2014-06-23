package ppa.marc.robot;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import ppa.marc.RecordReader;
import ppa.marc.RecordWriter;
import ppa.marc.domain.Field;
import ppa.marc.domain.Record;
import ppa.marc.robot.RecordAccessLibrary;
import junit.framework.TestCase;
import static org.easymock.EasyMock.*;

public class RecordAccessLibraryTest extends TestCase {

	private ArrayList<Record> records = new ArrayList<Record>();

	private static final String FILENAME = "filename";
	
	RecordReader recordReader = createMock(RecordReader.class);
	RecordWriter recordWriter = createMock(RecordWriter.class);

	OutputStream outputStream = new ByteArrayOutputStream();
	InputStream inputStream = new ByteArrayInputStream(new byte[0]);
	
	boolean inputFileExists = true;
	boolean outputFileFails = false;
	
	RecordAccessLibrary readAndPrintRecord = new RecordAccessLibrary(recordReader, recordWriter) {
		OutputStream getOutputStream() throws FileNotFoundException {
			if(outputFileFails) throw new FileNotFoundException();
			return outputStream;
		}
		InputStream openInputFile(String filename) throws FileNotFoundException {
			if(!inputFileExists) throw new FileNotFoundException();
			assertEquals(FILENAME, filename);
			return inputStream;
		}
	};

	private Record record;
	
	protected void tearDown() throws Exception {
		verify(recordReader);
		verify(recordWriter);
	}
	
	public void testGrantedInputFileDoesNotExistThenThrowsFileNotFoundException() throws Exception {
		inputFileExists = false;
		assertReadAndPrintFails();
	}

	public void testGrantedReaderFailsThenExceptionGetsThrown() throws Exception {
		expect(recordReader.read(inputStream)).andThrow(new IOException());
		assertReadAndPrintFails();
	}

	public void testGrantedWriterFailsThenExceptionGetsThrown() throws Exception {
		expect(recordReader.read(inputStream)).andReturn(records);
		recordWriter.writeRecords(isA(OutputStream.class), eq(records));
		expectLastCall().andThrow(new IOException());
		assertReadAndPrintFails();
	}

	public void testGrantedWritingSucceedsThenListReturnedByReaderIsPassedToWriter() throws Exception {
		expect(recordReader.read(inputStream)).andReturn(records);
		recordWriter.writeRecords(isA(OutputStream.class), eq(records));
		replayMocks();
		assertEquals("", readAndPrintRecord.readRecord(FILENAME));
	}

	public void testGrantedRecordIsPrintedAsTextThenToStringOfRecordIsUsed() throws Exception {
		String recordsAsString = addRecords(1);
		expect(recordReader.read(inputStream)).andReturn(records);
		replayMocks();
		assertEquals(recordsAsString, readAndPrintRecord.printRecordWithToString(FILENAME));
	}

	public void testGrantedTwoRecordsArePrintedAsTextThenToStringOutputsAreConcatenated() throws Exception {
		String recordsAsString = addRecords(2);
		expect(recordReader.read(inputStream)).andReturn(records);
		replayMocks();
		assertEquals(recordsAsString, readAndPrintRecord.printRecordWithToString(FILENAME));
	}
	
	private void assertReadAndPrintFails() {
		replayMocks();
		try {
			readAndPrintRecord.readRecord(FILENAME);
			fail();
		} catch(IOException expected) {
		}
	}
	
	private void replayMocks() {
		replay(recordReader);
		replay(recordWriter);
	}

	private String addRecords(int numberOfRecords) {
		StringBuilder stringBuilder = new StringBuilder();
		for(int i = 0; i < numberOfRecords; ++i) {
			record = new Record("");
			record.addFields(new Field(1));
			records.add(record);
			if(stringBuilder.length() > 0) stringBuilder.append('\n');
			stringBuilder.append(record.toString());
		}
		return stringBuilder.toString();
	}

}
