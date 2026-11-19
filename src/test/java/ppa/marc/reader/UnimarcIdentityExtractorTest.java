package ppa.marc.reader;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import ppa.marc.reader.UnimarcIdentityExtractor;

public class UnimarcIdentityExtractorTest {

	IdentityExtractor extractor = new UnimarcIdentityExtractor();
	List<String> fieldsAsStrings = new ArrayList<String>();
	
	@Test
	public void testReturnsContentOfFirstField() throws Exception {
		fieldsAsStrings.add("data");
		assertEquals("data", extractor.getIdentity(fieldsAsStrings));
	}
	
	@Test
	public void testRemovesIndicatorsIfPresentInFirstField() throws Exception {
		fieldsAsStrings.add("__ data");
		assertEquals("data", extractor.getIdentity(fieldsAsStrings));
	}

	@Test
	public void testDoesNotRemoveDoubleUnderscoreThatIsPartOfValue() throws Exception {
		fieldsAsStrings.add("data __ data");
		assertEquals("data __ data", extractor.getIdentity(fieldsAsStrings));
	}

	@Test
	public void testRemoveDoubleUnderscoreThatHasTabInsteadOfSpace() throws Exception {
		fieldsAsStrings.add("__\tdata");
		assertEquals("data", extractor.getIdentity(fieldsAsStrings));
	}
	
}
