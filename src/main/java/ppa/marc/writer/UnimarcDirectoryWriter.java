package ppa.marc.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.springframework.stereotype.Component;

import ppa.marc.common.UnimarcConstants;
import ppa.marc.domain.Field;
import ppa.marc.domain.Record;
import ppa.marc.domain.SubField;

@Component
public class UnimarcDirectoryWriter implements DirectoryWriter {

	public void writeDirectoryEntries(OutputStream outputStream, Record record) throws IOException {
		int startingPosition = 0;
		PrintStream writer = new PrintStream(outputStream);
		for(Field field : record.getFields()) {
			int length = calculateDataLength(field);
			writer.printf("%03d%04d%05d", field.getId(), length, startingPosition);
			startingPosition += length;
		}
		outputStream.write(new byte[] { UnimarcConstants.FIELD_TERMINATOR });
	}

	/** Length in bytes, as written by {@link UnimarcDataFieldsWriter}. */
	private int calculateDataLength(Field field) {
		int length = 1;  // Field terminator is included to length
		if(field.getFirstIndicator() != Character.UNASSIGNED) length += encodedLength(field.getFirstIndicator());
		if(field.getSecondIndicator() != Character.UNASSIGNED) length += encodedLength(field.getSecondIndicator());
		for(SubField subField : field.getSubFields()) {
			if(subField.getId() != Character.UNASSIGNED) length += 1 + encodedLength(subField.getId());
			length += subField.getValue().getBytes(UnimarcConstants.ENCODING).length;
		}
		return length;
	}

	private int encodedLength(char character) {
		return String.valueOf(character).getBytes(UnimarcConstants.ENCODING).length;
	}
}
