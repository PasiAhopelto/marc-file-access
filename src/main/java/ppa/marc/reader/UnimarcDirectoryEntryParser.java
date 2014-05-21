package ppa.marc.reader;

import java.util.ArrayList;
import java.util.List;

public class UnimarcDirectoryEntryParser implements DirectoryEntryParser {

	private static final int DIRECTORY_ENTRY_LENGTH = 12;

	public List<Integer> parseEntries(String directoryEntriesAsString) {
		if((directoryEntriesAsString.length() % DIRECTORY_ENTRY_LENGTH) != 0) throw new IllegalArgumentException("Directory block's size is not multiple of 12.");
		List<Integer> entries = new ArrayList<Integer>();
		for(String directoryEntryAsString : splitToDirectoryEntryStrings(directoryEntriesAsString)) {
			entries.add(extractDirectoryEntry(directoryEntryAsString));
		}
		return entries;
	}
	
	private List<String> splitToDirectoryEntryStrings(String directoryBlock) {
		List<String> directoryEntryStrings = new ArrayList<String>();
		for(int startIndex = 0; startIndex < directoryBlock.length(); startIndex += DIRECTORY_ENTRY_LENGTH) {
			directoryEntryStrings.add(directoryBlock.substring(startIndex, startIndex + DIRECTORY_ENTRY_LENGTH));
		}
		return directoryEntryStrings;
	}

	private Integer extractDirectoryEntry(String directoryEntry) {
		return Integer.valueOf(directoryEntry.substring(0, 3));
	}

}
