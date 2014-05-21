package ppa.marc.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import ppa.marc.common.UnimarcConstants;
import ppa.marc.domain.Field;
import ppa.marc.domain.Record;
import ppa.marc.domain.SubField;
import junit.framework.TestCase;

public class UnimarcDirectoryWriterTest extends TestCase {

	SubField subField = new SubField("1234");
	Field field = new Field(10);
	Field otherField = new Field(100);
	Record record = new Record("id");
	
	DirectoryWriter directoryWriter = new UnimarcDirectoryWriter();
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

	protected void setUp() throws Exception {
		record.getFields().add(field);
	}
	
	public void testGrantedRecordHasNoFieldsThenWritesFieldTerminator() throws Exception {
		record.getFields().clear();
		directoryWriter.writeDirectoryEntries(outputStream, record);
		assertEquals(String.valueOf(UnimarcConstants.FIELD_TERMINATOR), outputStream.toString());
	}

	public void testGrantedRecordHasSingleFieldThenWritesThreeDigitTagFirst() throws Exception {
		directoryWriter.writeDirectoryEntries(outputStream, record);
		assertEquals("010", getTagOfField(1));
	}
	
	public void testGrantedDataIsFourCharactersLongAndNoIndicatorsThenLengthIs0005() throws Exception {
		addSubFieldsToBothFields(1);
		assertWriteProducesDataLengthOf("0005");
	}

	public void testGrantedDataIsFourCharactersLongWithIndicatorsThenLengthIs0007() throws Exception {
		addIndicatorsToField();
		addSubFieldsToBothFields(1);
		assertWriteProducesDataLengthOf("0007");
	}

	public void testGrantedTwoSubFieldsWithFourCharacterDataWithIndicatorsThenLengthIs0011() throws Exception {
		addIndicatorsToField();
		addSubFieldsToBothFields(2);
		assertWriteProducesDataLengthOf("0011");
	}

	public void testGrantedFieldIsFirstThenItsStartingPositionIs00000() throws Exception {
		directoryWriter.writeDirectoryEntries(outputStream, record);
		assertEquals("00000", getStartingPositionOfField(1));
	}

	public void testGrantedSubFieldHasIdThenItIncreasesLengthByTwo() throws Exception {
		addSubFieldsToBothFields(1);
		subField.setId('a');
		assertWriteProducesDataLengthOf("0007");
	}

	public void testGrantedThereAreTwoFieldsThenSecondAlsoHasTagLengtAndStartingPosition() throws Exception {
		addSubFieldsToBothFields(1);
		record.getFields().add(otherField);
		directoryWriter.writeDirectoryEntries(outputStream, record);
		assertEquals("100", getTagOfField(2));
		assertEquals("0005", getLengthOfField(2));
		assertEquals("00005", getStartingPositionOfField(2));
	}

	public void testGrantedDirectoryIsWrittenThenItIsFollowedByFieldTerminator() throws Exception {
		directoryWriter.writeDirectoryEntries(outputStream, record);
		assertEquals(UnimarcConstants.FIELD_TERMINATOR, getLastCharacterOf(outputStream));
	}
	
	private char getLastCharacterOf(ByteArrayOutputStream outputStream) {
		byte[] output = outputStream.toByteArray();
		return (char) output[output.length-1];
	}

	private String getStartingPositionOfField(int field) {
		int sizeStartsAt = 12*(field-1)+7;
		return outputStream.toString().substring(sizeStartsAt, sizeStartsAt+5);
	}

	private String getLengthOfField(int field) {
		int lengthStartsAt = 12*(field-1)+3;
		return outputStream.toString().substring(lengthStartsAt, lengthStartsAt+4);
	}

	private String getTagOfField(int field) {
		int tagStartsAt = 12*(field-1);
		return outputStream.toString().substring(tagStartsAt, tagStartsAt+3);
	}
	
	private void assertWriteProducesDataLengthOf(String length) throws IOException {
		directoryWriter.writeDirectoryEntries(outputStream, record);
		assertEquals(length, getLengthOfField(1));
	}
	
	private void addIndicatorsToField() {
		field.setFirstIndicator('0');
		field.setSecondIndicator('1');
	}

	private void addSubFieldsToBothFields(int count) {
		for(int i = 0; i < count; ++i) {
			field.getSubFields().add(subField);
			otherField.getSubFields().add(subField);
		}
	}
	
}
