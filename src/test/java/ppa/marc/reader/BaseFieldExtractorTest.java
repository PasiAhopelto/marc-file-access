package ppa.marc.reader;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import ppa.marc.domain.Field;

public class BaseFieldExtractorTest {

	private static final String FIELD_WITH_INDICATORS_AS_STRING = "123._1";
	private static final String ID_ONLY_FIELD_AS_STRING = "123";
	private static final String ID_AND_VALUE_AS_STRING = "123 value";
	private static final String INVALID_INDICATORS = "123.000 value";
	private static final String FOUR_DIGIT_ID = "1234";

	private static final Field ID_ONLY_FIELD = new Field(123);
	private static final Object FIELD_WITH_INDICATORS = new Field(123, ' ', '1');
	
	BaseFieldExtractor extractor = new BaseFieldExtractor();
	
	@Test
	public void testGrantedInputIsNullThenThrowsMarcFormatException() throws Exception {
		assertThrowsMarcFormatException(null, "Field is null.");
	}

	@Test
	public void testGrantedInputIsEmptyThenThrowsMarcFormatException() throws Exception {
		assertThrowsMarcFormatException("", "Field is empty.");
	}
	
	@Test
	public void testGrantedFieldDoesNotHaveThreeDigitsFirstThenMarcFormatExceptionIsThrown() throws Exception {
		assertThrowsMarcFormatException(FOUR_DIGIT_ID, "Field '" + FOUR_DIGIT_ID + "' is not valid.");
	}

	@Test
	public void testGrantedFieldHasThreeDigitsThenReturnsFieldWithTheDigitsAsId() throws Exception {
		assertEquals(ID_ONLY_FIELD, extractor.parseBaseField(ID_ONLY_FIELD_AS_STRING));
	}

	@Test
	public void testGrantedFieldIsFollowedBySpaceThenReturnsFieldWithTheDigitsAsId() throws Exception {
		assertEquals(ID_ONLY_FIELD, extractor.parseBaseField(ID_AND_VALUE_AS_STRING));
	}

	@Test
	public void testGrantedFieldHasIdFollowedBySpaceAndInvalidIndicatorsThenMarcFormatExceptionIsThrown() throws Exception {
		assertThrowsMarcFormatException(INVALID_INDICATORS, "Field '" + INVALID_INDICATORS + "' is not valid.");
	}

	@Test
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
