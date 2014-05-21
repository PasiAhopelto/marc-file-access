package ppa.marc.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import ppa.marc.RecordWriter;
import ppa.marc.common.UnimarcConstants;
import ppa.marc.domain.Record;

@Named("marcFileWriter")
@Singleton
public class UnimarcRecordWriter implements RecordWriter {

	static final byte[] DEFAULT_RECORD_LABEL = { 
		0, 0, 0, 0, 0, 0, 0, 0, 
		0, 0, 0, 0, 0, 0, 0, 0, 
		0, 0, 0, 0, 0, 0, 0, 0
	};

	private final DirectoryWriter directoryWriter;
	private final DataFieldsWriter dataFieldsWriter;

	@Inject
	public UnimarcRecordWriter(DirectoryWriter directoryWriter, DataFieldsWriter dataFieldsWriter) {
		this.directoryWriter = directoryWriter;
		this.dataFieldsWriter = dataFieldsWriter;
	}

	public void writeRecords(OutputStream outputStream, List<Record> records) throws IOException {
		for(Record record : records) {
			outputStream.write(resolveRecordLabel(record));
			directoryWriter.writeDirectoryEntries(outputStream, record);
			dataFieldsWriter.writeDataFields(outputStream, record);
			outputStream.write(UnimarcConstants.RECORD_TERMINATOR);
		}
	}

	protected byte[] resolveRecordLabel(Record record) {
		if(record.getLabel() == null) return DEFAULT_RECORD_LABEL;
		return record.getLabel().getBytes();
	}

}
