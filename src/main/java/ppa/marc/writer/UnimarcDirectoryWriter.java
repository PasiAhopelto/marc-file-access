package ppa.marc.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.inject.Named;
import javax.inject.Singleton;

import ppa.marc.common.UnimarcConstants;
import ppa.marc.domain.Field;
import ppa.marc.domain.Record;
import ppa.marc.domain.SubField;

@Named("directoryWriter")
@Singleton
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

	private int calculateDataLength(Field field) {
		int length = 1;  // Field terminator is included to length
		if(field.getFirstIndicator() != Character.UNASSIGNED) ++length;
		if(field.getSecondIndicator() != Character.UNASSIGNED) ++length;
		for(SubField subField : field.getSubFields()) {
			if(subField.getId() != Character.UNASSIGNED) length += 2;
			length += subField.getValue().length();
		}
		return length;
	}
}
