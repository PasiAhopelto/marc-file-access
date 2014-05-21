package ppa.marc.writer;

import java.io.IOException;
import java.io.OutputStream;

import ppa.marc.domain.Record;

public interface DirectoryWriter {

	void writeDirectoryEntries(OutputStream stream, Record record) throws IOException;
	
}
