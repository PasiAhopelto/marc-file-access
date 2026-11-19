package ppa.marc;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import ppa.marc.domain.Record;

/**
 * Interface for reading UNIMARC files from input stream.
 * The reader is instantiated with Spring Framework (see {@link MarcFileAccessConfiguration}) after which
 * records are read from input stream, for example:
 * <pre>
 * RecordReader recordReader = new AnnotationConfigApplicationContext(MarcFileAccessConfiguration.class).getBean("marcFileReader", RecordReader.class);
 * List&lt;Record&gt; records = recordReader.read(new FileInputStream("input.marc"));
 * </pre>
 * Alternatively textual MARC record reader can be instantiated by using "textFileReader" as bean's name.
 * Note that the textual format is not from a MARC standard.
 * The XML context file ppa.marc.RecordReader.xml is still provided for
 * XML-based application contexts.
 * @see ppa.marc.domain
 */
public interface RecordReader {

	/**
	 * Reads UNIMARC records from stream, and returns them as a List of Record objects.
	 * @param inputStream InputStream from which to read the records.
	 * @return The records as List of Record objects.
	 * @throws IOException in case of read error.
	 */
	List<Record> read(InputStream inputStream) throws IOException;

}
