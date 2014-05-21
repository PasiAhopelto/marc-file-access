package ppa.marc.reader;

import ppa.marc.domain.Field;
import ppa.marc.domain.Record;

public class TextRecordConverter implements RecordConverter {
	private final RecordToFieldsDivider recordToFieldsDivider;
	private final BaseFieldExtractor fieldIdAndIndicatorExtractor;
	private final SubFieldExtractor subFieldExtractor;
	private final FromFieldIdentityExtractor recordNameExtractor;
	private final FromFieldIdentityExtractor recordLabelExtractor;

	public TextRecordConverter(RecordToFieldsDivider recordToFieldsDivider, BaseFieldExtractor fieldIdAndIndicatorExtractor, SubFieldExtractor subFieldExtractor, FromFieldIdentityExtractor recordNameExtractor, FromFieldIdentityExtractor recordLabelExtractor) {
		this.recordToFieldsDivider = recordToFieldsDivider;
		this.fieldIdAndIndicatorExtractor = fieldIdAndIndicatorExtractor;
		this.subFieldExtractor = subFieldExtractor;
		this.recordNameExtractor = recordNameExtractor;
		this.recordLabelExtractor = recordLabelExtractor;
	}

	public Record convert(String input) {
		Record record = new Record(null);
		if(input == null || input.length() == 0) throwRuntimeException(input, "is empty.");
		try {
			for(String fieldAsString : recordToFieldsDivider.divideToFields(input)) {
				record.getFields().add(convertField(input, fieldAsString));
			}
			if(record.getFields().isEmpty()) throwRuntimeException(input, "has no fields.");
			setRecordIdentity(input, record);
		} catch (MarcFormatException e) {
			throwRuntimeException(input, "is invalid: " + e.getMessage(), e);
		}
		return record;
	}

	private Record setRecordIdentity(String input, Record record) throws MarcFormatException {
		record.setName(recordNameExtractor.extractIdentity(record.getFields()));
		record.setLabel(recordLabelExtractor.extractIdentity(record.getFields()));
		return record;
	}

	private Field convertField(String input, String fieldAsString) throws MarcFormatException {
		Field field = fieldIdAndIndicatorExtractor.parseBaseField(fieldAsString);
		field.getSubFields().addAll(subFieldExtractor.parseSubFields(fieldAsString));
		if(field.getSubFields().isEmpty()) throwRuntimeException(input, "has field '" + fieldAsString + "' without values.");
		return field;
	}

	private void throwRuntimeException(String input, String message) {
		throwRuntimeException(input, message, null);
	}
	
	private void throwRuntimeException(String input, String message, Throwable throwable) {
		RuntimeException toThrow = new RuntimeException("The source record '" + input + "' " + message);
		if(throwable != null) toThrow.setStackTrace(throwable.getStackTrace());
		throw toThrow;
	}
}
