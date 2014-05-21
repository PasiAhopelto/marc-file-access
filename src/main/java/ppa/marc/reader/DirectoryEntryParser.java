package ppa.marc.reader;

import java.util.List;

public interface DirectoryEntryParser {

	List<Integer> parseEntries(String directoryBlock);
	
}
