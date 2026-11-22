package ppa.marc.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import ppa.marc.RecordWriter;
import ppa.marc.common.UnimarcConstants;
import ppa.marc.domain.Record;

@Component("marcFileWriter")
@RequiredArgsConstructor
public class UnimarcRecordWriter implements RecordWriter {

	static final int LEADER_LENGTH = 24;
	static final int MAXIMUM_RECORD_LENGTH = 99999;
	/**
	 * Leader used when the record has no 24 character label, for example when it was read from text.
	 * Position 9 is 'a': the record is encoded in UTF-8.
	 */
	static final String DEFAULT_LEADER = "00000nam a2200000   4500";
	private static final int RECORD_LENGTH_POSITION = 0;
	private static final int BASE_ADDRESS_POSITION = 12;

	private final DirectoryWriter directoryWriter;
	private final DataFieldsWriter dataFieldsWriter;

	public void writeRecords(OutputStream outputStream, List<Record> records) throws IOException {
		for(Record record : records) {
			ByteArrayOutputStream directory = new ByteArrayOutputStream();
			directoryWriter.writeDirectoryEntries(directory, record);
			ByteArrayOutputStream dataFields = new ByteArrayOutputStream();
			dataFieldsWriter.writeDataFields(dataFields, record);
			int baseAddress = LEADER_LENGTH + directory.size();
			int recordLength = baseAddress + dataFields.size() + 1;
			outputStream.write(createLeader(record, recordLength, baseAddress));
			directory.writeTo(outputStream);
			dataFields.writeTo(outputStream);
			outputStream.write(UnimarcConstants.RECORD_TERMINATOR);
		}
	}

	/**
	 * Creates the record leader: record's own label if it is a 24 character leader,
	 * otherwise the default leader, with record length and base address of data
	 * set to match the written record.
	 */
	byte[] createLeader(Record record, int recordLength, int baseAddress) {
		if(recordLength > MAXIMUM_RECORD_LENGTH) {
			throw new IllegalArgumentException("Record '" + record.getName() + "' is " + recordLength + " bytes, which exceeds the maximum record length of " + MAXIMUM_RECORD_LENGTH + ".");
		}
		String label = record.getLabel();
		StringBuilder leader = new StringBuilder(label != null && label.length() == LEADER_LENGTH ? label : DEFAULT_LEADER);
		leader.replace(RECORD_LENGTH_POSITION, RECORD_LENGTH_POSITION + 5, String.format("%05d", recordLength));
		leader.replace(BASE_ADDRESS_POSITION, BASE_ADDRESS_POSITION + 5, String.format("%05d", baseAddress));
		return leader.toString().getBytes(StandardCharsets.US_ASCII);
	}

}
