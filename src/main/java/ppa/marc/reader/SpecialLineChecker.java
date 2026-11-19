package ppa.marc.reader;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class SpecialLineChecker {

	public boolean isCommentLine(String line) {
		return line.startsWith("#");
	}

	public boolean isRecordSeparator(String line) {
		return Pattern.matches("\\s*", line);
	}

}
