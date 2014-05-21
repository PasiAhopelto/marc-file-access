package ppa.marc.reader;

import java.util.List;

public interface VariableDataSplitter {

	// TODO throw better exception
	List<String> split(List<Integer> directoryEntries, String dataFieldsWithoutEndOfRecordIndicator);
	
}
