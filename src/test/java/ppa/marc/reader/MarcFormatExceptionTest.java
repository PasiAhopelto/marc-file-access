package ppa.marc.reader;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class MarcFormatExceptionTest {

	private static final String MESSAGE = "message";

	@Test
	public void testGrantedMessageIsPassedAsConstructorArgumentThenItIsReturnedAsMessage() throws Exception {
		assertEquals(MESSAGE, new MarcFormatException(MESSAGE).getMessage());
	}
	
}
