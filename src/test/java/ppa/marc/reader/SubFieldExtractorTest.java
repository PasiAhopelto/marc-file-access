package ppa.marc.reader;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import ppa.marc.domain.SubField;


public class SubFieldExtractorTest {
	private static final String SPACES = " \t";
	private static final char SUBFIELD_ID = 'a';
	private static final char SUBFIELD_ID_B = 'b';
	private static final String CONTROL_VALUE = "control value";
	private static final String NORMAL_VALUE = "normal value";
	private static final String NORMAL_VALUE_B = "normal value b";
	private static final String COMBINED_FIELD = "normal value |b other value";
	SubFieldExtractor extractor = new SubFieldExtractor();
	ArrayList<SubField> expectedSubFields = new ArrayList<SubField>();

	@Test

	public void testGrantedFieldStringIsNullThenReturnsEmptyList() throws Exception {
		assertFailure(null, "Field is null.");
	}

	@Test

	public void testGrantedFieldContainsNoSubfieldsThenThrowsException() throws Exception {
		assertFailure("010.__", "Field '010.__' should have at least one subfield.");
	}

	@Test

	public void testGrantedControlFieldThenReturnsSingleSubfieldWithoutIdentity() throws Exception {
		expectedSubFields.add(new SubField(CONTROL_VALUE));
		assertSuccess("010.__ " + CONTROL_VALUE);
	}

	@Test

	public void testGrantedSubFieldWithoutValueThenReturnsSingleSubfieldWithEmptyValue() throws Exception {
		expectedSubFields.add(new SubField(SUBFIELD_ID, ""));
		assertSuccess("120.__ |" + SUBFIELD_ID);
	}

	@Test

	public void testGrantedSubFieldWithValueThenReturnsSingleSubfield() throws Exception {
		expectedSubFields.add(new SubField(SUBFIELD_ID, CONTROL_VALUE));
		assertSuccess("120.__ |" + SUBFIELD_ID + " " + CONTROL_VALUE);
	}

	@Test

	public void testGrantedSubFieldWithExtraSpacesThenSpacesAreNotTrimmed() throws Exception {
		expectedSubFields.add(new SubField(SUBFIELD_ID, SPACES + CONTROL_VALUE + SPACES));
		assertSuccess("120.__\t  |" + SUBFIELD_ID + ' ' + SPACES + CONTROL_VALUE + SPACES);
	}

	@Test

	public void testGrantedControlFieldWithoutDotUnderScoresThenReturnsSingleSubfieldWithoutIdentity() throws Exception {
		expectedSubFields.add(new SubField(CONTROL_VALUE));
		assertSuccess("010 " + CONTROL_VALUE);
	}

	@Test

	public void testGrantedControlFieldWithExtraSpacesThenSpacesAreStripped() throws Exception {
		expectedSubFields.add(new SubField(CONTROL_VALUE));
		assertSuccess("001  " + CONTROL_VALUE);
	}

	@Test

	public void testFieldWithTwoSubfieldsOnSameLineAreSeparatedAsOneSubfield() throws Exception {
		expectedSubFields.add(new SubField(SUBFIELD_ID, COMBINED_FIELD));
		assertSuccess("150.__ |" + SUBFIELD_ID + " "+ COMBINED_FIELD);
	}
	
	@Test
	
	public void testFieldWithTwoSubfieldsOnSeparateLinesAreReturnedAsTwoSubfields() throws Exception {
		expectedSubFields.add(new SubField(SUBFIELD_ID, NORMAL_VALUE));
		expectedSubFields.add(new SubField(SUBFIELD_ID_B, NORMAL_VALUE_B));
		assertSuccess("150.__ |a " + NORMAL_VALUE + "\n|b " + NORMAL_VALUE_B);
	}

	@Test

	public void testFieldWithNumericIndicatorsGetsParsed() throws Exception {
		expectedSubFields.add(new SubField(SUBFIELD_ID, NORMAL_VALUE));
		assertSuccess("050.12 |a " + NORMAL_VALUE);
	}

	@Test

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
