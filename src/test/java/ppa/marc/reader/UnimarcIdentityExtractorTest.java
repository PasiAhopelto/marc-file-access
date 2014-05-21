package ppa.marc.reader;

import java.util.ArrayList;
import java.util.List;

import ppa.marc.reader.UnimarcIdentityExtractor;
import junit.framework.TestCase;

public class UnimarcIdentityExtractorTest extends TestCase {

	IdentityExtractor extractor = new UnimarcIdentityExtractor();
	List<String> fieldsAsStrings = new ArrayList<String>();
	
	public void testReturnsContentOfFirstField() throws Exception {
		fieldsAsStrings.add("data");
		assertEquals("data", extractor.getIdentity(fieldsAsStrings));
	}
	
	public void testRemovesIndicatorsIfPresentInFirstField() throws Exception {
		fieldsAsStrings.add("__ data");
		assertEquals("data", extractor.getIdentity(fieldsAsStrings));
	}

	public void testDoesNotRemoveDoubleUnderscoreThatIsPartOfValue() throws Exception {
		fieldsAsStrings.add("data __ data");
		assertEquals("data __ data", extractor.getIdentity(fieldsAsStrings));
	}

	public void testRemoveDoubleUnderscoreThatHasTabInsteadOfSpace() throws Exception {
		fieldsAsStrings.add("__\tdata");
		assertEquals("data", extractor.getIdentity(fieldsAsStrings));
	}
	
}
