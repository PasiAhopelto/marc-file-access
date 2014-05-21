package ppa.marc;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import ppa.marc.domain.Record;

/**
 * Interface for writing UNIMARC records.  The writer is instantiated with
 * Spring Framework, after which records can be written to a file.  For
 * example binary MARC record writer:
 * <pre>
 * RecordWriter recordWriter = (RecordWriter) new ClassPathXmlApplicationContext(RecordWriter.class.getName() + ".xml").getBean("marcFileWriter");
 * recordWriter.write(new FileOutputStream("output.marc"), records);
 * </pre>
 * Alternatively textual MARC record writer can be instantiated by using "testFileWriter" as bean's name.
 * Note that the textual format is not from a MARC standard.
 * @see ppa.marc.domain
 */
public interface RecordWriter {

	/**
	 * Writes records to output stream.
	 * @param outputStream Target stream.
	 * @param records List of records to be written.
	 * @throws IOException In case of an write error.
	 */
	void writeRecords(OutputStream outputStream, List<Record> records) throws IOException;

}
