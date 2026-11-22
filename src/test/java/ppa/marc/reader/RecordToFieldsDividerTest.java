package ppa.marc.reader;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


public class RecordToFieldsDividerTest {
	private static final String CONTROL_FIELD = "001 value";
	private static final String DATA_FIELD = "101.0_ |a eng";
	private static final String MULTILINE_DATA_FIELD = "101.0_ |a eng\n|c ger";
	private static final String DATA_FIELD_WITHOUT_VALUE = "101.0_";
	
	RecordToFieldsDivider divider = new RecordToFieldsDivider();
	List<String> expectedFields = new ArrayList<String>();
	
	@Test
	public void testGrantedInputIsEmptyThenThrowsMarcFormatException() throws Exception {
		assertFailsWithInput("", "contains empty line.");
	}

	@Test
	public void testGrantedInputContainsOnlySpacesThenThrowsMarcFormatException() throws Exception {
		assertFailsWithInput(" \t", "could not be divided into fields.");
	}

	@Test
	public void testGrantedInputContainsControlFieldThenReturnsItAsString() throws Exception {
		expectedFields.add(CONTROL_FIELD);
		assertEquals(expectedFields, divider.divideToFields(CONTROL_FIELD));
	}

	@Test
	public void testGrantedInputContainsTwoControlFieldsThenReturnsThemAsStrings() throws Exception {
		expectedFields.add(CONTROL_FIELD);
		expectedFields.add(CONTROL_FIELD);
		assertEquals(expectedFields, divider.divideToFields(CONTROL_FIELD + "\n" + CONTROL_FIELD));
	}

	@Test
	public void testGrantedInputContainsEmptyLineThenThrowsException() throws Exception {
		assertFailsWithInput(CONTROL_FIELD + "\n\n", "contains empty line.");
	}
	
	@Test
	public void testGrantedInputContainsSingleDataFieldThenReturnsItAsString() throws Exception {
		expectedFields.add(DATA_FIELD);
		assertEquals(expectedFields, divider.divideToFields(DATA_FIELD));
	}

	@Test
	public void testGrantedInputContainsTwoDataFieldsThenReturnsThemAsStrings() throws Exception {
		expectedFields.add(DATA_FIELD);
		expectedFields.add(DATA_FIELD);
		assertEquals(expectedFields, divider.divideToFields(DATA_FIELD + "\n" + DATA_FIELD));
	}

	@Test
	public void testGrantedInputContainsMultilineDataFieldThenReturnsItAsString() throws Exception {
		expectedFields.add(MULTILINE_DATA_FIELD);
		assertEquals(expectedFields, divider.divideToFields(MULTILINE_DATA_FIELD));
	}

	@Test
	public void testGrantedInputContainsMultilineDataFieldsThenReturnsThemAsStrings() throws Exception {
		expectedFields.add(MULTILINE_DATA_FIELD);
		expectedFields.add(MULTILINE_DATA_FIELD);
		assertEquals(expectedFields, divider.divideToFields(MULTILINE_DATA_FIELD + "\n" + MULTILINE_DATA_FIELD));
	}

	@Test
	public void testGrantedInputContainsFieldWithoutValueThenReturnsStringWithFieldIdButNoValue() throws Exception {
		expectedFields.add(DATA_FIELD_WITHOUT_VALUE);
		assertEquals(expectedFields, divider.divideToFields(DATA_FIELD_WITHOUT_VALUE));
	}

	@Test
	public void testGrantedInputContainsFieldsWithoutValueThenReturnsThemAsStringsWithFieldIdsButNoValues() throws Exception {
		expectedFields.add(DATA_FIELD_WITHOUT_VALUE);
		expectedFields.add(DATA_FIELD_WITHOUT_VALUE);
		assertEquals(expectedFields, divider.divideToFields(DATA_FIELD_WITHOUT_VALUE + "\n" + DATA_FIELD_WITHOUT_VALUE));
	}

	@Test
	public void testGrantedInputContainsAllDifferentFieldTypesThenReturnsThemAsStrings() throws Exception {
		expectedFields.add(DATA_FIELD);
		expectedFields.add(CONTROL_FIELD);
		expectedFields.add(DATA_FIELD_WITHOUT_VALUE);
		assertEquals(expectedFields, divider.divideToFields(DATA_FIELD + "\n" + CONTROL_FIELD + "\n" + DATA_FIELD_WITHOUT_VALUE));
	}
	
	@Test
	public void testGrantedLineIsNotFieldOrSubfieldThenThrowsException() throws Exception {
		assertFailsWithInput(DATA_FIELD + "\nnot a field", "has line 'not a field' that is neither a field nor a subfield of the previous field (subfield codes are ASCII letters or digits).");
	}

	@Test
	public void testGrantedSubfieldCodeIsNotAsciiThenThrowsException() throws Exception {
		assertFailsWithInput(DATA_FIELD + "\n       |\u00e4 value", "has line '       |\u00e4 value' that is neither a field nor a subfield of the previous field (subfield codes are ASCII letters or digits).");
	}

	@Test
	public void testGrantedSubfieldLineHasNoPreviousFieldThenThrowsException() throws Exception {
		assertFailsWithInput("       |a value", "has line '       |a value' that is neither a field nor a subfield of the previous field (subfield codes are ASCII letters or digits).");
	}

	private void assertFailsWithInput(String input, String message) {
		try {
			divider.divideToFields(input);
			fail();
		} catch(MarcFormatException expected) {
			assertEquals("Record '" + input + "' " + message, expected.getMessage());
		}
	}
}
