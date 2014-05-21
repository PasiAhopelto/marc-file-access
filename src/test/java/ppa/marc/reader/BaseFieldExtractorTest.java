package ppa.marc.reader;

import ppa.marc.domain.Field;
import junit.framework.TestCase;

public class BaseFieldExtractorTest extends TestCase {

	private static final String FIELD_WITH_INDICATORS_AS_STRING = "123._1";
	private static final String ID_ONLY_FIELD_AS_STRING = "123";
	private static final String ID_AND_VALUE_AS_STRING = "123 value";
	private static final String INVALID_INDICATORS = "123.000 value";
	private static final String FOUR_DIGIT_ID = "1234";

	private static final Field ID_ONLY_FIELD = new Field(123);
	private static final Object FIELD_WITH_INDICATORS = new Field(123, '_', '1');
	
	BaseFieldExtractor extractor = new BaseFieldExtractor();
	
	public void testGrantedInputIsNullThenThrowsMarcFormatException() throws Exception {
		assertThrowsMarcFormatException(null, "Field is null.");
	}

	public void testGrantedInputIsEmptyThenThrowsMarcFormatException() throws Exception {
		assertThrowsMarcFormatException("", "Field is empty.");
	}
	
	public void testGrantedFieldDoesNotHaveThreeDigitsFirstThenMarcFormatExceptionIsThrown() throws Exception {
		assertThrowsMarcFormatException(FOUR_DIGIT_ID, "Field '" + FOUR_DIGIT_ID + "' is not valid.");
	}

	public void testGrantedFieldHasThreeDigitsThenReturnsFieldWithTheDigitsAsId() throws Exception {
		assertEquals(ID_ONLY_FIELD, extractor.parseBaseField(ID_ONLY_FIELD_AS_STRING));
	}

	public void testGrantedFieldIsFollowedBySpaceThenReturnsFieldWithTheDigitsAsId() throws Exception {
		assertEquals(ID_ONLY_FIELD, extractor.parseBaseField(ID_AND_VALUE_AS_STRING));
	}

	public void testGrantedFieldHasIdFollowedBySpaceAndInvalidIndicatorsThenMarcFormatExceptionIsThrown() throws Exception {
		assertThrowsMarcFormatException(INVALID_INDICATORS, "Field '" + INVALID_INDICATORS + "' is not valid.");
	}

	public void testGrantedFieldIsFollowedByDotAndIndicatorsThenReturnsFieldWithIdAndIndicators() throws Exception {
		assertEquals(FIELD_WITH_INDICATORS, extractor.parseBaseField(FIELD_WITH_INDICATORS_AS_STRING));
	}

	private void assertThrowsMarcFormatException(String field, String message) {
		try {
			extractor.parseBaseField(field);
			fail();
		} catch(MarcFormatException expected) {
			assertEquals(message, expected.getMessage());
		}
	}
	
}
