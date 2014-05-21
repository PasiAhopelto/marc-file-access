package ppa.marc.reader;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class RecordToFieldsDividerTest extends TestCase {
	private static final String CONTROL_FIELD = "001 value";
	private static final String DATA_FIELD = "101.0_ |a eng";
	private static final String MULTILINE_DATA_FIELD = "101.0_ |a eng\n|c ger";
	private static final String DATA_FIELD_WITHOUT_VALUE = "101.0_";
	
	RecordToFieldsDivider divider = new RecordToFieldsDivider();
	List<String> expectedFields = new ArrayList<String>();
	
	public void testGrantedInputIsEmptyThenThrowsMarcFormatException() throws Exception {
		assertFailsWithInput("", "contains empty line.");
	}

	public void testGrantedInputContainsOnlySpacesThenThrowsMarcFormatException() throws Exception {
		assertFailsWithInput(" \t", "could not be divided into fields.");
	}

	public void testGrantedInputContainsControlFieldThenReturnsItAsString() throws Exception {
		expectedFields.add(CONTROL_FIELD);
		assertEquals(expectedFields, divider.divideToFields(CONTROL_FIELD));
	}

	public void testGrantedInputContainsTwoControlFieldsThenReturnsThemAsStrings() throws Exception {
		expectedFields.add(CONTROL_FIELD);
		expectedFields.add(CONTROL_FIELD);
		assertEquals(expectedFields, divider.divideToFields(CONTROL_FIELD + "\n" + CONTROL_FIELD));
	}

	public void testGrantedInputContainsEmptyLineThenThrowsException() throws Exception {
		assertFailsWithInput(CONTROL_FIELD + "\n\n", "contains empty line.");
	}
	
	public void testGrantedInputContainsSingleDataFieldThenReturnsItAsString() throws Exception {
		expectedFields.add(DATA_FIELD);
		assertEquals(expectedFields, divider.divideToFields(DATA_FIELD));
	}

	public void testGrantedInputContainsTwoDataFieldsThenReturnsThemAsStrings() throws Exception {
		expectedFields.add(DATA_FIELD);
		expectedFields.add(DATA_FIELD);
		assertEquals(expectedFields, divider.divideToFields(DATA_FIELD + "\n" + DATA_FIELD));
	}

	public void testGrantedInputContainsMultilineDataFieldThenReturnsItAsString() throws Exception {
		expectedFields.add(MULTILINE_DATA_FIELD);
		assertEquals(expectedFields, divider.divideToFields(MULTILINE_DATA_FIELD));
	}

	public void testGrantedInputContainsMultilineDataFieldsThenReturnsThemAsStrings() throws Exception {
		expectedFields.add(MULTILINE_DATA_FIELD);
		expectedFields.add(MULTILINE_DATA_FIELD);
		assertEquals(expectedFields, divider.divideToFields(MULTILINE_DATA_FIELD + "\n" + MULTILINE_DATA_FIELD));
	}

	public void testGrantedInputContainsFieldWithoutValueThenReturnsStringWithFieldIdButNoValue() throws Exception {
		expectedFields.add(DATA_FIELD_WITHOUT_VALUE);
		assertEquals(expectedFields, divider.divideToFields(DATA_FIELD_WITHOUT_VALUE));
	}

	public void testGrantedInputContainsFieldsWithoutValueThenReturnsThemAsStringsWithFieldIdsButNoValues() throws Exception {
		expectedFields.add(DATA_FIELD_WITHOUT_VALUE);
		expectedFields.add(DATA_FIELD_WITHOUT_VALUE);
		assertEquals(expectedFields, divider.divideToFields(DATA_FIELD_WITHOUT_VALUE + "\n" + DATA_FIELD_WITHOUT_VALUE));
	}

	public void testGrantedInputContainsAllDifferentFieldTypesThenReturnsThemAsStrings() throws Exception {
		expectedFields.add(DATA_FIELD);
		expectedFields.add(CONTROL_FIELD);
		expectedFields.add(DATA_FIELD_WITHOUT_VALUE);
		assertEquals(expectedFields, divider.divideToFields(DATA_FIELD + "\n" + CONTROL_FIELD + "\n" + DATA_FIELD_WITHOUT_VALUE));
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
