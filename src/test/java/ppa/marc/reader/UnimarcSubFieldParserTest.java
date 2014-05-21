package ppa.marc.reader;

import ppa.marc.domain.SubField;
import ppa.marc.reader.UnimarcSubFieldParser;
import junit.framework.TestCase;

public class UnimarcSubFieldParserTest extends TestCase {

	SubFieldParser parser = new UnimarcSubFieldParser();
	
	public void testThrowsExceptionIfEmptyField() throws Exception {
		try {
			parser.parse("", false);
			fail();
		}
		catch(RuntimeException ignore) {
		}
	}
	
	public void testExtractsDataOfZeroZeroField() throws Exception {
		SubField expectedSubField = new SubField("data");
		assertEquals(expectedSubField, parser.parse("data", true));
	}

	public void testExtractsDataAndId() throws Exception {
		SubField expectedSubField = new SubField('a', "data");
		assertEquals(expectedSubField, parser.parse("adata", false));
	}
	
}
