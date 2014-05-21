package ppa.marc.reader;

import junit.framework.TestCase;

public class SpecialLineCheckerTest extends TestCase {

	SpecialLineChecker checker = new SpecialLineChecker();
	
	public void testGranterLineDoesNotStartWithHashThenItIsNotCommentLine() throws Exception {
		assertFalse(checker.isCommentLine(" #"));
	}
	
	public void testGranterLineStartsWithHashThenItIsCommentLine() throws Exception {
		assertTrue(checker.isCommentLine("# "));
	}

	public void testGrantedLineContainsOnlyWhitespacesThenItIsRecordSeparator() throws Exception {
		assertTrue(checker.isRecordSeparator(" \t "));
	}
	
	public void testGrantedLineContainsNonWhitespaceThenItIsNotRecordSeparator() throws Exception {
		assertFalse(checker.isRecordSeparator(" \tx"));
	}
	
}
