package ppa.marc.writer;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import ppa.marc.RecordWriter;
import ppa.marc.domain.Record;

public class TextRecordWriterTest {

	private static final String RECORD_AS_TEXT = "first record";

	RecordWriter writer = new TextRecordWriter();
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	List<Record> records = new ArrayList<Record>();

	Record record = new Record(RECORD_AS_TEXT) {
		public String toString() {
			return getName();
		}
	};
	
	@Test
	
	public void testGrantedThereAreNoRecordThenNothingIsWritten() throws Exception {
		writer.writeRecords(outputStream, records);
		assertEquals(0, outputStream.size());
	}
	
	@Test
	
	public void testGrantedThereIsRecordThenItsTostringIsWrittenToOutputStream() throws Exception {
		records.add(record);
		writer.writeRecords(outputStream, records);
		assertEquals(RECORD_AS_TEXT, outputStream.toString());
	}

	@Test

	public void testGrantedThereAreTwoRecordsThenTheyAreSeparatedByNewline() throws Exception {
		records.add(record);
		records.add(record);
		writer.writeRecords(outputStream, records);
		assertEquals(RECORD_AS_TEXT + "\n" + RECORD_AS_TEXT, outputStream.toString());
	}
	
}
