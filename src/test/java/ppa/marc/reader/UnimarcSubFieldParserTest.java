package ppa.marc.reader;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import ppa.marc.domain.SubField;
import ppa.marc.reader.UnimarcSubFieldParser;

public class UnimarcSubFieldParserTest {

	SubFieldParser parser = new UnimarcSubFieldParser();
	
	@Test
	
	public void testThrowsExceptionIfEmptyField() throws Exception {
		try {
			parser.parse("", false);
			fail();
		}
		catch(RuntimeException ignore) {
		}
	}
	
	@Test
	
	public void testExtractsDataOfZeroZeroField() throws Exception {
		SubField expectedSubField = new SubField("data");
		assertEquals(expectedSubField, parser.parse("data", true));
	}

	@Test

	public void testExtractsDataAndId() throws Exception {
		SubField expectedSubField = new SubField('a', "data");
		assertEquals(expectedSubField, parser.parse("adata", false));
	}
	
}
