package ppa.marc.reader;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import ppa.marc.common.UnimarcConstants;
import ppa.marc.reader.UnimarcVariableDataSplitter;


public class UnimarcVariableDataSplitterTest {

	VariableDataSplitter splitter = new UnimarcVariableDataSplitter();
	List<Integer> directoryEntries = new ArrayList<Integer>();
	
	@BeforeEach
	protected void setUp() throws Exception {
		directoryEntries.add(Integer.valueOf(700));
	}
	
	@Test
	public void testReturnsEmptyCollectionIfNoDirectoryEntries() throws Exception {
		assertEquals(0, splitter.split(new ArrayList<Integer>(), "").size());
	}
	
	@Test
	public void testThrowsExceptionWithDirectoryEntryButNoData() throws Exception {
		try {
			splitter.split(directoryEntries, "");
			fail();
		}
		catch(RuntimeException ignore) {
		}
	}

	@Test
	public void testReturnsOneEntry() throws Exception {
		List<String> dataAsStrings = splitter.split(directoryEntries, "x" + UnimarcConstants.FIELD_TERMINATOR);
		assertEquals(1, dataAsStrings.size());
		assertEquals("x", dataAsStrings.get(0));
	}

	@Test
	public void testReturnsTwoEntries() throws Exception {
		directoryEntries.add(Integer.valueOf(800));
		List<String> dataAsStrings = splitter.split(directoryEntries, "x" + UnimarcConstants.FIELD_TERMINATOR + "xx" + UnimarcConstants.FIELD_TERMINATOR);
		assertEquals(2, dataAsStrings.size());
		assertEquals("x", dataAsStrings.get(0));
		assertEquals("xx", dataAsStrings.get(1));
	}
	
}
