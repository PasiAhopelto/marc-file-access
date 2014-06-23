package ppa.marc.reader;

import java.util.ArrayList;
import java.util.List;

import ppa.marc.domain.Field;
import ppa.marc.domain.Record;
import ppa.marc.domain.SubField;

import junit.framework.TestCase;

import static org.easymock.classextension.EasyMock.*;

public class TextRecordConverterTest extends TestCase {

	private static final String MESSAGE = "message";
	private static final String FIELD_AS_STRING = "field";
	private static final String INPUT_WITHOUT_NEWLINE = "input without newline";
	private static final Field FIELD = new Field(1);
	private static final SubField SUBFIELD = new SubField("value");
	private static final String LABEL = "label";
	private static final String NAME = "name";

	private List<String> fieldsAsStrings = new ArrayList<String>();
	private List<SubField> subFields = new ArrayList<SubField>();
	
	RecordToFieldsDivider recordToFieldsDivider = createStrictMock(RecordToFieldsDivider.class);
	BaseFieldExtractor fieldExtractor = createStrictMock(BaseFieldExtractor.class);
	SubFieldExtractor subFieldExtractor = createStrictMock(SubFieldExtractor.class);
	FromFieldIdentityExtractor recordNameExtractor = createStrictMock(FromFieldIdentityExtractor.class);
	FromFieldIdentityExtractor recordLabelExtractor = createStrictMock(FromFieldIdentityExtractor.class);
	
	RecordConverter converter = new TextRecordConverter(recordToFieldsDivider, fieldExtractor, subFieldExtractor, recordNameExtractor, recordLabelExtractor);
	
	protected void tearDown() throws Exception {
		verify(recordToFieldsDivider);
		verify(fieldExtractor);
		verify(subFieldExtractor);
		verify(recordLabelExtractor);
		verify(recordNameExtractor);
	}
	
	public void testGrantedInputIsEmptyThenThrowsRuntimeException() throws Exception {
		assertConversionThrowsRuntimeExceptionWith("", "is empty.");
	}

	public void testGrantedInputIsNullThenThrowsRuntimeException() throws Exception {
		assertConversionThrowsRuntimeExceptionWith(null, "is empty.");
	}

	public void testGrantedFieldDividerThrowsExceptionThenItsMessageIsAppendedToNewRuntimeException() throws Exception {
		expect(recordToFieldsDivider.divideToFields(INPUT_WITHOUT_NEWLINE)).andThrow(new MarcFormatException(MESSAGE));
		assertConversionThrowsRuntimeExceptionWith(INPUT_WITHOUT_NEWLINE, "is invalid: " + MESSAGE);
	}

	public void testGrantedFieldDividerReturnsEmptyCollectionThenThrowsExceptionWithMessageNoFields() throws Exception {
		expect(recordToFieldsDivider.divideToFields(INPUT_WITHOUT_NEWLINE)).andReturn(fieldsAsStrings);
		assertConversionThrowsRuntimeExceptionWith(INPUT_WITHOUT_NEWLINE, "has no fields.");
	}
	
	public void testGrantedFieldIdExtractorThrowsExceptionThenItsMessageIsAppendedToNewRuntimeException() throws Exception {
	    fieldsAsStrings.add(FIELD_AS_STRING);
		expect(recordToFieldsDivider.divideToFields(INPUT_WITHOUT_NEWLINE)).andReturn(fieldsAsStrings);
		expect(fieldExtractor.parseBaseField(FIELD_AS_STRING)).andThrow(new MarcFormatException(MESSAGE));
		assertConversionThrowsRuntimeExceptionWith(INPUT_WITHOUT_NEWLINE, "is invalid: " + MESSAGE);
	}

	public void testGrantedSubFieldExtractorThrowsExceptionThenItsMessageIsAppendedToNewRuntimeException() throws Exception {
	    expectFieldParsing();
		expect(subFieldExtractor.parseSubFields(FIELD_AS_STRING)).andThrow(new MarcFormatException(MESSAGE));
		assertConversionThrowsRuntimeExceptionWith(INPUT_WITHOUT_NEWLINE, "is invalid: " + MESSAGE);
	}

	public void testGrantedSubFieldExtractorReturnsNothingThenRuntimeExceptionIsThrown() throws Exception {
	    expectFieldParsing();
		expect(subFieldExtractor.parseSubFields(FIELD_AS_STRING)).andReturn(subFields);
		assertConversionThrowsRuntimeExceptionWith(INPUT_WITHOUT_NEWLINE, "has field '" + FIELD_AS_STRING + "' without values.");
	}

	public void testGrantedSubFieldsExistThenTheyAreAddedToFieldAndFieldIsAddedToRecord() throws Exception {
	    expectFieldParsing();
	    subFields.add(SUBFIELD);
		expect(subFieldExtractor.parseSubFields(FIELD_AS_STRING)).andReturn(subFields);
		expect(recordLabelExtractor.extractIdentity(createExpectedFields())).andReturn(LABEL);
		expect(recordNameExtractor.extractIdentity(createExpectedFields())).andReturn(NAME);
		replayMocks();
		assertEquals(createExpectedRecord(), converter.convert(INPUT_WITHOUT_NEWLINE));
	}

	private Record createExpectedRecord() {
		Record record = new Record(NAME);
		record.setLabel(LABEL);
		record.addFields(createExpectedFields().toArray(new Field[0]));
		return record;
	}
	
	private List<Field> createExpectedFields() {
		List<Field> expectedFields = new ArrayList<Field>();
		Field expectedField = new Field(FIELD.getId());
		expectedField.addSubFields(subFields.toArray(new SubField[0]));
		expectedFields.add(expectedField);
		return expectedFields;
	}
	
	private void expectFieldParsing() throws MarcFormatException {
		fieldsAsStrings.add(FIELD_AS_STRING);
		expect(recordToFieldsDivider.divideToFields(INPUT_WITHOUT_NEWLINE)).andReturn(fieldsAsStrings);
		expect(fieldExtractor.parseBaseField(FIELD_AS_STRING)).andReturn(FIELD);
	}
	
	private void assertConversionThrowsRuntimeExceptionWith(String input, String reason) {
		replayMocks();
		try {
			converter.convert(input);
			fail();
		} catch(RuntimeException expected) {
			assertEquals("The source record '" + input + "' " + reason, expected.getMessage());
		}
	}

	private void replayMocks() {
		replay(recordToFieldsDivider);
		replay(fieldExtractor);
		replay(subFieldExtractor);
		replay(recordLabelExtractor);	
		replay(recordNameExtractor);
	}
	
}
