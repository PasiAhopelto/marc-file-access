package ppa.marc.reader;

import static org.easymock.EasyMock.*;
import ppa.marc.domain.Field;
import ppa.marc.domain.SubField;
import junit.framework.TestCase;

abstract public class AbstractFieldParserTest extends TestCase {

	SubFieldParser subFieldParser = createStrictMock(SubFieldParser.class);
	FieldParser parser = createFieldParser(subFieldParser);

	protected void tearDown() throws Exception {
		verify(subFieldParser);
	}
	
	protected abstract FieldParser createFieldParser(SubFieldParser subFieldParser);

	public void testThrowsExceptionWithEmptyInput() throws Exception {
		replay(subFieldParser);
		try {
			parser.parseField(0, "");
			fail();
		} 
		catch(IllegalArgumentException ignore) {
		}
	}
	
	public void testGivesZeroZeroSubFieldToSubFieldParser() throws Exception {
		Field expectedField = new Field(1);
		addExpectedSubField(new SubField("data"), expectedField, "data", true);
		parseReplayAndVerify(expectedField, 1, "data");
	}

	protected void addExpectedSubField(SubField expectedSubField, Field expectedField, String subFieldStringToParse, boolean isZeroZero) {
		expectedField.addSubFields(expectedSubField);
		expect(subFieldParser.parse(subFieldStringToParse, isZeroZero)).andReturn(expectedSubField);
	}

	protected void parseReplayAndVerify(Field expectedField, int id, String string) {
		replay(subFieldParser);
		assertEquals(expectedField, parser.parseField(id, string));
	}

}
