package ppa.marc.writer;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.stereotype.Component;

import ppa.marc.common.UnimarcConstants;
import ppa.marc.domain.Field;
import ppa.marc.domain.Record;
import ppa.marc.domain.SubField;

@Component
public class UnimarcDataFieldsWriter implements DataFieldsWriter {

	public void writeDataFields(OutputStream outputStream, Record record) throws IOException {
		if(record.getFields().isEmpty()) throw new IllegalArgumentException("Record '" + record.getName() + "' does not contain any fields.");
		for(Field field : record.getFields()) {
			writeIndicators(outputStream, field);
			for(SubField subField : field.getSubFields()) {
				writeSubFieldId(outputStream, subField);
				outputStream.write(subField.getValue().getBytes(UnimarcConstants.ENCODING));
			}
			outputStream.write(UnimarcConstants.FIELD_TERMINATOR);
		}
	}

	private void writeSubFieldId(OutputStream outputStream, SubField subField) throws IOException {
		if(subField.getId() != Character.UNASSIGNED) {
			outputStream.write(UnimarcConstants.SUBFIELD_DELIMITER);
			outputStream.write(encode(subField.getId()));
		}
	}

	private void writeIndicators(OutputStream outputStream, Field field) throws IOException {
		if(!field.isControlField()) {
			outputStream.write(encode(field.getFirstIndicator()));
			outputStream.write(encode(field.getSecondIndicator()));
		}
	}

	private byte[] encode(char character) {
		return String.valueOf(character).getBytes(UnimarcConstants.ENCODING);
	}

}
