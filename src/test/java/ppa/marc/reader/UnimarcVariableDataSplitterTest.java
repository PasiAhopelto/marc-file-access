package ppa.marc.reader;

import java.util.ArrayList;
import java.util.List;

import ppa.marc.common.UnimarcConstants;
import ppa.marc.reader.UnimarcVariableDataSplitter;

import junit.framework.TestCase;

public class UnimarcVariableDataSplitterTest extends TestCase {

	VariableDataSplitter splitter = new UnimarcVariableDataSplitter();
	List<Integer> directoryEntries = new ArrayList<Integer>();
	
	protected void setUp() throws Exception {
		directoryEntries.add(new Integer(700));
	}
	
	public void testReturnsEmptyCollectionIfNoDirectoryEntries() throws Exception {
		assertEquals(0, splitter.split(new ArrayList<Integer>(), "").size());
	}
	
	public void testThrowsExceptionWithDirectoryEntryButNoData() throws Exception {
		try {
			splitter.split(directoryEntries, "");
			fail();
		}
		catch(RuntimeException ignore) {
		}
	}

	public void testReturnsOneEntry() throws Exception {
		List<String> dataAsStrings = splitter.split(directoryEntries, "x" + UnimarcConstants.FIELD_TERMINATOR);
		assertEquals(1, dataAsStrings.size());
		assertEquals("x", dataAsStrings.get(0));
	}

	public void testReturnsTwoEntries() throws Exception {
		directoryEntries.add(new Integer(800));
		List<String> dataAsStrings = splitter.split(directoryEntries, "x" + UnimarcConstants.FIELD_TERMINATOR + "xx" + UnimarcConstants.FIELD_TERMINATOR);
		assertEquals(2, dataAsStrings.size());
		assertEquals("x", dataAsStrings.get(0));
		assertEquals("xx", dataAsStrings.get(1));
	}
	
}
