package ppa.marc.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.springframework.stereotype.Component;

import ppa.marc.RecordWriter;
import ppa.marc.common.UnimarcConstants;
import ppa.marc.domain.Record;

@Component("textFileWriter")
public class TextRecordWriter implements RecordWriter {

	public void writeRecords(OutputStream outputStream, List<Record> records) throws IOException {
		boolean isFirstRecord = true;
		for(Record record : records) {
			if(!isFirstRecord) outputStream.write('\n');
			isFirstRecord = false;
			outputStream.write(record.toString().getBytes(UnimarcConstants.ENCODING));
		}
	}

}
