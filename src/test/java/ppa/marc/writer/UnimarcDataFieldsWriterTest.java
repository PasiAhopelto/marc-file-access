package ppa.marc.writer;

import java.io.ByteArrayOutputStream;

import ppa.marc.common.UnimarcConstants;
import ppa.marc.domain.Field;
import ppa.marc.domain.Record;
import ppa.marc.domain.SubField;

import junit.framework.TestCase;

public class UnimarcDataFieldsWriterTest extends TestCase {

	private static final char SECOND_INDICATOR = '1';
	private static final char FIRST_INDICATOR = '0';
	private static final String NORMAL_FIELD_VALUE = "normal field value";
	private static final String CONTROL_FIELD_VALUE = "control field value";
	private static final char SUBFIELD_ID = 'a';

	private static final String EXPECTED_SUBFIELD = "" + UnimarcConstants.SUBFIELD_DELIMITER + SUBFIELD_ID + NORMAL_FIELD_VALUE;
	private static final String RECORD_ID = "id";
	
	DataFieldsWriter writer = new UnimarcDataFieldsWriter();
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	Record record = new Record(RECORD_ID);
	Field controlField = new Field(1);
	Field normalField = new Field(2, FIRST_INDICATOR, SECOND_INDICATOR);
	
	protected void setUp() throws Exception {
		controlField.addSubFields(new SubField(CONTROL_FIELD_VALUE));
		normalField.addSubFields(new SubField(SUBFIELD_ID, NORMAL_FIELD_VALUE));
	}
	
	public void testGrantedThereAreNoFieldsThenThrowsIllegalArgumentException() throws Exception {
		try {
			writer.writeDataFields(outputStream, record);
			fail();
		} catch(IllegalArgumentException expected) {
			assertTrue(expected.getMessage().contains("Record '" + RECORD_ID + "' does not contain any fields."));
		}
	}

	public void testGrantedRecordHasControlFieldThenWritesItAndFieldTerminator() throws Exception {
		record.addFields(controlField);
		writer.writeDataFields(outputStream, record);
		assertEquals(CONTROL_FIELD_VALUE + UnimarcConstants.FIELD_TERMINATOR, outputStream.toString());
	}
	
	public void testGrantedRecordHasNormalFieldThenWritesItWithIndicatorsFollowedByFieldTerminator() throws Exception {
		record.addFields(normalField);
		writer.writeDataFields(outputStream, record);
		assertEquals("" + FIRST_INDICATOR + SECOND_INDICATOR + EXPECTED_SUBFIELD + UnimarcConstants.FIELD_TERMINATOR, outputStream.toString());
	}

	public void testGrantedFieldHasTwoSubfieldsThenWritesBoth() throws Exception {
		normalField.addSubFields(new SubField(SUBFIELD_ID, NORMAL_FIELD_VALUE));
		record.addFields(normalField);
		writer.writeDataFields(outputStream, record);
		assertEquals("" + FIRST_INDICATOR + SECOND_INDICATOR + EXPECTED_SUBFIELD + EXPECTED_SUBFIELD + UnimarcConstants.FIELD_TERMINATOR, outputStream.toString());
	}

	public void testGrantedRecordHasIdAndNormalFieldThenWritesBoth() throws Exception {
		record.addFields(controlField, normalField);
		writer.writeDataFields(outputStream, record);
		assertEquals(CONTROL_FIELD_VALUE + UnimarcConstants.FIELD_TERMINATOR + FIRST_INDICATOR + SECOND_INDICATOR + EXPECTED_SUBFIELD + UnimarcConstants.FIELD_TERMINATOR, outputStream.toString());
	}
	
}
