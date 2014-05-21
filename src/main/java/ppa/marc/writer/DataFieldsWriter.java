package ppa.marc.writer;

import java.io.IOException;
import java.io.OutputStream;

import ppa.marc.domain.Record;

public interface DataFieldsWriter {

	// TODO write better exception in case of an error (not RTE)
	void writeDataFields(OutputStream outputStream, Record record) throws IOException;
	
}
