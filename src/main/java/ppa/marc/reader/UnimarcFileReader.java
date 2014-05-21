package ppa.marc.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ppa.marc.RecordReader;
import ppa.marc.common.UnimarcConstants;
import ppa.marc.domain.Record;

public class UnimarcFileReader implements RecordReader {

	private final RecordConverter recordConverter;

	public UnimarcFileReader(RecordConverter recordConverter) {
		this.recordConverter = recordConverter;
	}

	public List<Record> read(InputStream input) throws IOException {
		List<Record> records = new ArrayList<Record>();
		StringBuffer content = new StringBuffer();
		int character;
		while((character = input.read()) > -1) {
			content.append((char) character);
			if(character == UnimarcConstants.RECORD_TERMINATOR) {
				records.add(recordConverter.convert(content.toString()));
				content = new StringBuffer();
			}
		}
		return records;
	}

}
