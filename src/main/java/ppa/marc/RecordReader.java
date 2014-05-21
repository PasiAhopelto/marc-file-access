package ppa.marc;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import ppa.marc.domain.Record;

/**
 * Interface for reading UNIMARC files from input stream.
 * The reader is instantiated with Spring Framework after which
 * records are read from input stream, for example:
 * <pre>
 * RecordReader recordReader = (RecordReader) new ClassPathXmlApplicationContext(RecordReader.class.getName() + ".xml").getBean("marcFileReader");
 * List&lt;Record&gt; records = recordReader.read(new FileInputStream("input.marc"));
 * </pre>
 * Alternatively textual MARC record reader can be instantiated by using "testFileReader" as bean's name.
 * Note that the textual format is not from a MARC standard.
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
