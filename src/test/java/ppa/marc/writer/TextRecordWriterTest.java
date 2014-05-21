package ppa.marc.writer;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import ppa.marc.RecordWriter;
import ppa.marc.domain.Record;
import junit.framework.TestCase;

public class TextRecordWriterTest extends TestCase {

	private static final String RECORD_AS_TEXT = "first record";

	RecordWriter writer = new TextRecordWriter();
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	List<Record> records = new ArrayList<Record>();

	Record record = new Record(RECORD_AS_TEXT) {
		public String toString() {
			return getName();
		}
	};
	
	public void testGrantedThereAreNoRecordThenNothingIsWritten() throws Exception {
		writer.writeRecords(outputStream, records);
		assertEquals(0, outputStream.size());
	}
	
	public void testGrantedThereIsRecordThenItsTostringIsWrittenToOutputStream() throws Exception {
		records.add(record);
		writer.writeRecords(outputStream, records);
		assertEquals(RECORD_AS_TEXT, outputStream.toString());
	}

	public void testGrantedThereAreTwoRecordsThenTheyAreSeparatedByNewline() throws Exception {
		records.add(record);
		records.add(record);
		writer.writeRecords(outputStream, records);
		assertEquals(RECORD_AS_TEXT + "\n" + RECORD_AS_TEXT, outputStream.toString());
	}
	
}
