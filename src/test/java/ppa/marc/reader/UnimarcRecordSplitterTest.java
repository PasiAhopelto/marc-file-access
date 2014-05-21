package ppa.marc.reader;

import ppa.marc.common.UnimarcConstants;
import ppa.marc.reader.UnimarcRecordSplitter;
import junit.framework.TestCase;

public class UnimarcRecordSplitterTest extends TestCase {

	private static final String DATA = "data" + UnimarcConstants.FIELD_TERMINATOR + "data" + UnimarcConstants.FIELD_TERMINATOR;
	private static final String RECORD_LABEL = "rrrrrrrrrrrrrrrrrrrrrrrr";
	private static final String DIRECTORY = "dir";
	
	RecordSplitter splitter = new UnimarcRecordSplitter();
	
	public void testReturnsDirectoryFieldsAsString() throws Exception {
		assertEquals(DIRECTORY, splitter.extractDirectoryBlock(RECORD_LABEL + DIRECTORY + UnimarcConstants.FIELD_TERMINATOR + DATA + UnimarcConstants.FIELD_TERMINATOR + UnimarcConstants.RECORD_TERMINATOR));
	}

	public void testReturnsDataFieldsAsString() throws Exception {
		assertEquals(DATA, splitter.extractFieldBlock(RECORD_LABEL + DIRECTORY + UnimarcConstants.FIELD_TERMINATOR + DATA + UnimarcConstants.FIELD_TERMINATOR + UnimarcConstants.RECORD_TERMINATOR));
	}
	
	public void testGrantedRecordIsNotEmptyThenReturnsFirstTwentyFourCharactersAsRecordLabel() throws Exception {
		assertEquals(RECORD_LABEL, splitter.extractRecordLabel(RECORD_LABEL + DIRECTORY + UnimarcConstants.FIELD_TERMINATOR + DATA + UnimarcConstants.FIELD_TERMINATOR + UnimarcConstants.RECORD_TERMINATOR));
	}

}
