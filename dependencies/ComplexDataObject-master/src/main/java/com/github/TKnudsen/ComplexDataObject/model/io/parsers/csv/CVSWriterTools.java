package com.github.TKnudsen.ComplexDataObject.model.io.parsers.csv;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * <p>
 * Title: CVSWriterTools
 * </p>
 * 
 * <p>
 * Description: support functionalty to facilitate .csv output
 * 
 * Possible addon: add encapsulation of special chars and comma
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class CVSWriterTools {

	private static final char SEPARATOR = ',';

	public static void writeToCSV(List<List<? extends Object>> linesOfObjects, String file) throws IOException {

		Writer writer = createFileWriter(file);

		for (List<? extends Object> objects : linesOfObjects)
			writeLine(writer, objects);

		writer.flush();
		writer.close();
	}

	public static void writeLine(Writer writer, List<? extends Object> values) throws IOException {
		writeLine(writer, values, SEPARATOR);
	}

	public static void writeLine(Writer writer, List<? extends Object> values, char separator) throws IOException {

		boolean first = true;

		if (separator == ' ')
			separator = SEPARATOR;

		StringBuilder sb = new StringBuilder();
		for (Object value : values) {
			if (!first)
				sb.append(separator);

			sb.append(csvFormatSupport(value.toString()));

			first = false;
		}

		sb.append("\n");
		writer.append(sb.toString());
	}
	

	private static String csvFormatSupport(String value) {
		String output = value;
		return output.replace("\"", "\"\"");
	}

	public static Writer createFileWriter(String csvFile) throws IOException {
		return new FileWriter(csvFile);
	}
}
