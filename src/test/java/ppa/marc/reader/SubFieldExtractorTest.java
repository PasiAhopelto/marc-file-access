package ppa.marc.reader;

import java.util.ArrayList;

import ppa.marc.domain.SubField;

import junit.framework.TestCase;

public class SubFieldExtractorTest extends TestCase {
	private static final String SPACES = " \t";
	private static final char SUBFIELD_ID = 'a';
	private static final char SUBFIELD_ID_B = 'b';
	private static final String CONTROL_VALUE = "control value";
	private static final String NORMAL_VALUE = "normal value";
	private static final String NORMAL_VALUE_B = "normal value b";
	private static final String COMBINED_FIELD = "normal value |b other value";
	SubFieldExtractor extractor = new SubFieldExtractor();
	ArrayList<SubField> expectedSubFields = new ArrayList<SubField>();

	public void testGrantedFieldStringIsNullThenReturnsEmptyList() throws Exception {
		assertFailure(null, "Field is null.");
	}

	public void testGrantedFieldContainsNoSubfieldsThenThrowsException() throws Exception {
		assertFailure("010.__", "Field '010.__' should have at least one subfield.");
	}

	public void testGrantedControlFieldThenReturnsSingleSubfieldWithoutIdentity() throws Exception {
		expectedSubFields.add(new SubField(CONTROL_VALUE));
		assertSuccess("010.__ " + CONTROL_VALUE);
	}

	public void testGrantedSubFieldWithoutValueThenReturnsSingleSubfieldWithEmptyValue() throws Exception {
		expectedSubFields.add(new SubField(SUBFIELD_ID, ""));
		assertSuccess("120.__ |" + SUBFIELD_ID);
	}

	public void testGrantedSubFieldWithValueThenReturnsSingleSubfield() throws Exception {
		expectedSubFields.add(new SubField(SUBFIELD_ID, CONTROL_VALUE));
		assertSuccess("120.__ |" + SUBFIELD_ID + " " + CONTROL_VALUE);
	}

	public void testGrantedSubFieldWithExtraSpacesThenSpacesAreNotTrimmed() throws Exception {
		expectedSubFields.add(new SubField(SUBFIELD_ID, SPACES + CONTROL_VALUE + SPACES));
		assertSuccess("120.__\t  |" + SUBFIELD_ID + ' ' + SPACES + CONTROL_VALUE + SPACES);
	}

	public void testGrantedControlFieldWithoutDotUnderScoresThenReturnsSingleSubfieldWithoutIdentity() throws Exception {
		expectedSubFields.add(new SubField(CONTROL_VALUE));
		assertSuccess("010 " + CONTROL_VALUE);
	}

	public void testGrantedControlFieldWithExtraSpacesThenSpacesAreStripped() throws Exception {
		expectedSubFields.add(new SubField(CONTROL_VALUE));
		assertSuccess("001  " + CONTROL_VALUE);
	}

	public void testFieldWithTwoSubfieldsOnSameLineAreSeparatedAsOneSubfield() throws Exception {
		expectedSubFields.add(new SubField(SUBFIELD_ID, COMBINED_FIELD));
		assertSuccess("150.__ |" + SUBFIELD_ID + " "+ COMBINED_FIELD);
	}
	
	public void testFieldWithTwoSubfieldsOnSeparateLinesAreReturnedAsTwoSubfields() throws Exception {
		expectedSubFields.add(new SubField(SUBFIELD_ID, NORMAL_VALUE));
		expectedSubFields.add(new SubField(SUBFIELD_ID_B, NORMAL_VALUE_B));
		assertSuccess("150.__ |a " + NORMAL_VALUE + "\n|b " + NORMAL_VALUE_B);
	}

	public void testFieldWithNumericIndicatorsGetsParsed() throws Exception {
		expectedSubFields.add(new SubField(SUBFIELD_ID, NORMAL_VALUE));
		assertSuccess("050.12 |a " + NORMAL_VALUE);
	}

	public void testFieldWithIndentedSecondSubfieldGetsParsed() throws Exception {
		expectedSubFields.add(new SubField(SUBFIELD_ID, NORMAL_VALUE));
		expectedSubFields.add(new SubField(SUBFIELD_ID_B, NORMAL_VALUE_B));
		assertSuccess("150.__ |a " + NORMAL_VALUE + "\n |b " + NORMAL_VALUE_B);
	}
	
	private void assertSuccess(String fieldAsString) throws MarcFormatException {
		assertEquals(expectedSubFields, extractor.parseSubFields(fieldAsString));
	}

	private void assertFailure(String field, String message) {
		try { 
			assertEquals(expectedSubFields, extractor.parseSubFields(field));
			fail();
		} catch(MarcFormatException expected) {
			assertEquals(message, expected.getMessage());
		}
	}
}
