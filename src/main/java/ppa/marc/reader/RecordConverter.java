package ppa.marc.reader;

import ppa.marc.domain.Record;

public interface RecordConverter {

	Record convert(String input);
	
}
