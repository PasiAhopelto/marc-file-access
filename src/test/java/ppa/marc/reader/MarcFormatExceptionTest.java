package ppa.marc.reader;

import junit.framework.TestCase;

public class MarcFormatExceptionTest extends TestCase {

	private static final String MESSAGE = "message";

	public void testGrantedMessageIsPassedAsConstructorArgumentThenItIsReturnedAsMessage() throws Exception {
		assertEquals(MESSAGE, new MarcFormatException(MESSAGE).getMessage());
	}
	
}
