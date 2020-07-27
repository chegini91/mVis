package com.github.TKnudsen.ComplexDataObject.model.io.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IDObject;
import com.github.TKnudsen.ComplexDataObject.data.interfaces.ISelfDescription;

public abstract class AbstractIDObjectFileParser<O extends IDObject> implements IDObjectParser<O>, ISelfDescription {

	protected String tokenizerToken;
	protected String missingValueIdentifier;

	public AbstractIDObjectFileParser(String tokenizerToken, String missingValueIdentifier) {
		this.tokenizerToken = tokenizerToken;
		this.missingValueIdentifier = missingValueIdentifier;
	}

	/**
	 * reads a given file and returns a List of Strings each representing a line of
	 * the file.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	protected List<String> readLines(String file) throws IOException, FileNotFoundException {
		return readLinesOfFile(file);
	}

	public String getTokenizerToken() {
		return tokenizerToken;
	}

	public void setTokenizerToken(String tokenizerToken) {
		this.tokenizerToken = tokenizerToken;
	}

	public String getMissingValueIdentifier() {
		return missingValueIdentifier;
	}

	public void setMissingValueIdentifier(String missingValueIdentifier) {
		this.missingValueIdentifier = missingValueIdentifier;
	}

	/**
	 * reads a given file and returns a List of Strings each representing a line of
	 * the file.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static List<String> readLinesOfFile(String file) throws IOException, FileNotFoundException {
		List<String> rows = new ArrayList<String>();
		File fileObject = new File(file);
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(fileObject));
		} catch (FileNotFoundException ex) {
			throw new FileNotFoundException("AbstractIDObjectFileParser.loadRows: file not found: " + file);
		}

		String line = reader.readLine();
		while (line != null) {
			rows.add(line);
			line = reader.readLine();
		}

		reader.close();

		return rows;
	}

}
