package ppa.marc.reader;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class SpecialLineCheckerTest {

	SpecialLineChecker checker = new SpecialLineChecker();
	
	@Test
	
	public void testGranterLineDoesNotStartWithHashThenItIsNotCommentLine() throws Exception {
		assertFalse(checker.isCommentLine(" #"));
	}
	
	@Test
	
	public void testGranterLineStartsWithHashThenItIsCommentLine() throws Exception {
		assertTrue(checker.isCommentLine("# "));
	}

	@Test

	public void testGrantedLineContainsOnlyWhitespacesThenItIsRecordSeparator() throws Exception {
		assertTrue(checker.isRecordSeparator(" \t "));
	}
	
	@Test
	
	public void testGrantedLineContainsNonWhitespaceThenItIsNotRecordSeparator() throws Exception {
		assertFalse(checker.isRecordSeparator(" \tx"));
	}
	
}
