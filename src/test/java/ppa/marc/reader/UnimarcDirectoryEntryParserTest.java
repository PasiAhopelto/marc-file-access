package ppa.marc.reader;

import java.util.List;

import ppa.marc.reader.UnimarcDirectoryEntryParser;

import junit.framework.TestCase;

public class UnimarcDirectoryEntryParserTest extends TestCase {

	DirectoryEntryParser parser = new UnimarcDirectoryEntryParser();
	Integer[] expectedEntries = new Integer[] {
		new Integer(700),	
		new Integer(600)
	};
	
	public void testReturnsEmptyListWhenNothingToParse() throws Exception {
		assertEquals(0, parser.parseEntries("").size());
	}
	
	public void testThrowsExceptionWhenBlockIsTooShort() throws Exception {
		try {
			parser.parseEntries("70002000001");
			fail();
		}
		catch(IllegalArgumentException ignore) {
		}
	}
	
	public void testParsesOneEntry() throws Exception {
		List<Integer> entries = parser.parseEntries("700020000010");
		assertEquals(1, entries.size());
		assertEquals(expectedEntries[0], entries.get(0));
	}
	
	public void testParsesTwoEntries() throws Exception {
		List<Integer> entries = parser.parseEntries("700020000010600030010000");
		assertEquals(2, entries.size());
		assertEquals(expectedEntries[0], entries.get(0));
		assertEquals(expectedEntries[1], entries.get(1));
	}
	
}
