package ppa.marc.common;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class UnimarcConstants {

	public static final char RECORD_TERMINATOR = '\035';
	public static final char FIELD_TERMINATOR = '\036';
	public static final char SUBFIELD_DELIMITER = '\037';
	/**
	 * Character encoding of both binary and text records. MARC 21 records in UTF-8 are
	 * marked with 'a' in leader position 9. Record, field and subfield terminators are
	 * single ASCII bytes, which never occur inside a multi-byte UTF-8 character.
	 */
	public static final Charset ENCODING = StandardCharsets.UTF_8;
	 
}
