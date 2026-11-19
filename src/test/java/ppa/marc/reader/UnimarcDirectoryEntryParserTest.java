package ppa.marc.reader;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import ppa.marc.reader.UnimarcDirectoryEntryParser;


public class UnimarcDirectoryEntryParserTest {

	DirectoryEntryParser parser = new UnimarcDirectoryEntryParser();
	Integer[] expectedEntries = new Integer[] {
		Integer.valueOf(700),	
		Integer.valueOf(600)
	};
	
	@Test
	public void testReturnsEmptyListWhenNothingToParse() throws Exception {
		assertEquals(0, parser.parseEntries("").size());
	}
	
	@Test
	public void testThrowsExceptionWhenBlockIsTooShort() throws Exception {
		try {
			parser.parseEntries("70002000001");
			fail();
		}
		catch(IllegalArgumentException ignore) {
		}
	}
	
	@Test
	public void testParsesOneEntry() throws Exception {
		List<Integer> entries = parser.parseEntries("700020000010");
		assertEquals(1, entries.size());
		assertEquals(expectedEntries[0], entries.get(0));
	}
	
	@Test
	public void testParsesTwoEntries() throws Exception {
		List<Integer> entries = parser.parseEntries("700020000010600030010000");
		assertEquals(2, entries.size());
		assertEquals(expectedEntries[0], entries.get(0));
		assertEquals(expectedEntries[1], entries.get(1));
	}
	
}
