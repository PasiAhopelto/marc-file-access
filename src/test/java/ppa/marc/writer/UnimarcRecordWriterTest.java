package ppa.marc.writer;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ppa.marc.RecordWriter;
import ppa.marc.common.UnimarcConstants;
import ppa.marc.domain.Record;
import static org.easymock.EasyMock.*;

public class UnimarcRecordWriterTest {

	private static final String LABEL = "label";
	DirectoryWriter directoryWriter = createStrictMock(DirectoryWriter.class);
	DataFieldsWriter dataFieldsWriter = createStrictMock(DataFieldsWriter.class);
	
	RecordWriter writer = new UnimarcRecordWriter(directoryWriter, dataFieldsWriter);

	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	Record record = new Record("");
	
	byte[] expectedRecordLabel = UnimarcRecordWriter.DEFAULT_RECORD_LABEL;
	
	List<Record> records = new ArrayList<Record>();

	@AfterEach

	protected void tearDown() throws Exception {
		verify(directoryWriter);
		verify(dataFieldsWriter);
	}

	@Test

	public void testGrantedStreamWorksThenWritesTwentyFourBytesAndDirectoryAndDataFieldsAndRecordTerminator() throws Exception {
		expectRecords(1);
		replayMocks();
		writer.writeRecords(outputStream, records);
		assertOutputStreamHasRecordLabelsAndTerminator(1);
	}

	@Test

	public void testGrantedStreamWorksThenWritesTwoRecords() throws Exception {
		expectRecords(2);
		replayMocks();
		writer.writeRecords(outputStream, records);
		assertOutputStreamHasRecordLabelsAndTerminator(2);
	}
	
	@Test
	
	public void testGrantedRecordHasLabelThenItIsUsedInsteadOfDefault() throws Exception {
		record.setLabel(LABEL);
		expectedRecordLabel = LABEL.getBytes();
		expectRecords(1);
		replayMocks();
		writer.writeRecords(outputStream, records);
		assertOutputStreamHasRecordLabelsAndTerminator(1);
	}
	
	private void assertOutputStreamHasRecordLabelsAndTerminator(int numberOfRecordLabels) throws IOException {
		ByteArrayOutputStream expectedOutputStream = new ByteArrayOutputStream();
		for(int i = 0; i < numberOfRecordLabels; ++i) {
			expectedOutputStream.write(expectedRecordLabel);
			expectedOutputStream.write(UnimarcConstants.RECORD_TERMINATOR);
		}
		assertEquals(expectedOutputStream.toString(), outputStream.toString());
	}

	private void replayMocks() {
		replay(dataFieldsWriter);
		replay(directoryWriter);
	}

	private void expectRecords(int numberOfRecords) throws Exception {
		for(int i = 0; i < numberOfRecords; ++i) {
			records.add(record);
			directoryWriter.writeDirectoryEntries(outputStream, record);
			dataFieldsWriter.writeDataFields(outputStream, record);
		}
	}
	
}
