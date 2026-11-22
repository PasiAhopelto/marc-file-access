package ppa.marc.writer;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import ppa.marc.RecordWriter;
import ppa.marc.common.UnimarcConstants;
import ppa.marc.domain.Record;

public class UnimarcRecordWriterTest {

	private static final String DIRECTORY = "001000800000" + UnimarcConstants.FIELD_TERMINATOR;
	private static final String DATA_FIELDS = "1234567" + UnimarcConstants.FIELD_TERMINATOR;
	private static final int BASE_ADDRESS = 24 + DIRECTORY.length();
	private static final int RECORD_LENGTH = BASE_ADDRESS + DATA_FIELDS.length() + 1;
	private static final String RECORD_BODY = DIRECTORY + DATA_FIELDS + UnimarcConstants.RECORD_TERMINATOR;

	DirectoryWriter directoryWriter = createStrictMock(DirectoryWriter.class);
	DataFieldsWriter dataFieldsWriter = createStrictMock(DataFieldsWriter.class);

	RecordWriter writer = new UnimarcRecordWriter(directoryWriter, dataFieldsWriter);

	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	Record record = new Record("");

	List<Record> records = new ArrayList<Record>();

	@AfterEach
	protected void tearDown() throws Exception {
		verify(directoryWriter);
		verify(dataFieldsWriter);
	}

	@Test
	public void testGrantedRecordHasNoLabelThenWritesDefaultLeaderWithLengthsAndDirectoryAndDataFieldsAndRecordTerminator() throws Exception {
		expectRecords(1);
		writer.writeRecords(outputStream, records);
		assertEquals(defaultLeader() + RECORD_BODY, outputStream.toString());
	}

	@Test
	public void testGrantedStreamWorksThenWritesTwoRecords() throws Exception {
		expectRecords(2);
		writer.writeRecords(outputStream, records);
		assertEquals(defaultLeader() + RECORD_BODY + defaultLeader() + RECORD_BODY, outputStream.toString());
	}

	@Test
	public void testGrantedRecordHasLeaderAsLabelThenItIsUsedWithLengthsUpdated() throws Exception {
		record.setLabel("99999cam a2299999 a 4500");
		expectRecords(1);
		writer.writeRecords(outputStream, records);
		assertEquals(String.format("%05dcam a22%05d a 4500", RECORD_LENGTH, BASE_ADDRESS) + RECORD_BODY, outputStream.toString());
	}

	@Test
	public void testGrantedRecordLabelIsNotTwentyFourCharactersThenDefaultLeaderIsUsed() throws Exception {
		record.setLabel("3073077");
		expectRecords(1);
		writer.writeRecords(outputStream, records);
		assertEquals(defaultLeader() + RECORD_BODY, outputStream.toString());
	}

	@Test
	public void testGrantedRecordIsLongerThanMaximumThenThrowsException() throws Exception {
		replayMocks();
		assertThrows(IllegalArgumentException.class, () -> ((UnimarcRecordWriter) writer).createLeader(record, 100000, 24));
	}

	private String defaultLeader() {
		return String.format("%05dnam  22%05d   4500", RECORD_LENGTH, BASE_ADDRESS);
	}

	private void replayMocks() {
		replay(dataFieldsWriter);
		replay(directoryWriter);
	}

	private void expectRecords(int numberOfRecords) throws Exception {
		for(int i = 0; i < numberOfRecords; ++i) {
			records.add(record);
			directoryWriter.writeDirectoryEntries(anyObject(OutputStream.class), same(record));
			expectLastCall().andAnswer(() -> writeToStreamArgument(DIRECTORY));
			dataFieldsWriter.writeDataFields(anyObject(OutputStream.class), same(record));
			expectLastCall().andAnswer(() -> writeToStreamArgument(DATA_FIELDS));
		}
		replayMocks();
	}

	private Object writeToStreamArgument(String content) throws Exception {
		((OutputStream) getCurrentArgument(0)).write(content.getBytes());
		return null;
	}
}
