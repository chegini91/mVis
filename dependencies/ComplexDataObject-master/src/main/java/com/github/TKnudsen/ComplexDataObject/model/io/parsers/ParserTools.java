package com.github.TKnudsen.ComplexDataObject.model.io.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

/**
 * <p>
 * Title: ParserTools
 * </p>
 * 
 * <p>
 * Description: Tools class for parsers. It handles date conversions, loads rows
 * from files, etc.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.0
 */
public abstract class ParserTools implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3729971051948506651L;

	// DateFormats
	private static SimpleDateFormat ISO0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
	private static SimpleDateFormat ISO1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat IS02 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static SimpleDateFormat IS03 = new SimpleDateFormat("yyyy-MM-dd HH");
	private static SimpleDateFormat IS04 = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat IS05 = new SimpleDateFormat("yyyy-MM");
	private static SimpleDateFormat IS06 = new SimpleDateFormat("dd.MM.yyyy");
	private static SimpleDateFormat IS06b = new SimpleDateFormat("dd_MM_yyyy");
	private static SimpleDateFormat IS07 = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	private static SimpleDateFormat IS08 = new SimpleDateFormat("MM.dd.yyyy");

	/**
	 * Parses date-oriented tokens. Checks most of the popular date formats.
	 * 
	 * @param date
	 *            token as String.
	 * @return
	 * @throws ParseException
	 * @Deprecated use DateParser
	 */
	@Deprecated
	public static synchronized Date parseDate(String token) {
		Date date = null;
		String t = token.replace("T", " ");

		// speedup:
		if (token.equals("Date/Time"))
			return date;

		// alternative
		if (t.length() == 23)
			try {
				// 2007-01-01 00:00:00:000
				synchronized (ISO0) {
					date = ISO0.parse(t);
				}
			} catch (ParseException pe0) {
			}
		else if (t.length() == 19)
			try {
				// 2007-01-01 00:00:00
				synchronized (ISO1) {
					date = ISO1.parse(t);
				}
			} catch (ParseException pe8) {
			}
		else if (t.length() == 16)
			try {
				// 2007-01-01 00:00
				synchronized (IS02) {
					date = IS02.parse(t);
				}
			} catch (ParseException pe8) {
				try {
					// 2007.01.01 00:00
					date = IS07.parse(t);
				} catch (ParseException pe7) {
				}
			}
		else if (t.length() == 13)
			try {
				// 2007-01-01 00
				synchronized (IS03) {
					date = IS03.parse(t);
				}
			} catch (ParseException pe8) {
			}
		else if (t.length() == 10)
			try {
				// 2007-01-01
				synchronized (IS04) {
					date = IS04.parse(t);
				}
			} catch (ParseException pe8) {
				try {
					// 13.01.1969
					synchronized (IS06) {
						date = IS06.parse(token);
					}
				} catch (ParseException pe) {
					try {
						// 01_13_1969
						synchronized (IS06b) {
							date = IS06b.parse(token);
						}
					} catch (ParseException pe_) {
						try {
							// 01.13.1969
							synchronized (IS08) {
								date = IS08.parse(token);
							}
						} catch (ParseException pe__) {
						}
					}
				}
			}
		else if (t.length() == 7)
			try {
				// 2007-01
				synchronized (IS05) {
					date = IS05.parse(t);
				}
			} catch (ParseException pe8) {
			}
		return date;
	}

	/**
	 * method for loading data from a file. data is returned row-wise as a List
	 * of Strings
	 * 
	 * @param dataFile
	 * @return
	 * @throws IOException
	 */
	public static List<String> loadRows(String dataFile) throws IOException {
		// DATAFILE ACCESS
		List<String> rows = new ArrayList<String>();
		System.out.println("reading " + dataFile + " ...");
		File file = new File(dataFile);
		BufferedReader reader = null;

		// file input
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException ex) {
			throw new FileNotFoundException("FileNotFoundException...");
		}
		String line = reader.readLine();
		while (line != null) {
			rows.add(line);
			line = reader.readLine();
		}

		if (reader != null)
			reader.close();

		return rows;
	}

	public static boolean hasFloatingPointValues(double[] values) {
		for (int i = 0; i < values.length; i++) {
			if (values[i] != Math.floor(values[i]))
				return true;
		}
		return false;
	}

	/**
	 * assigns an identifier and an object to an Entry.
	 * 
	 * @param identifier
	 * @param value
	 * @param missingValueIndicator
	 * @return
	 */
	public static Entry<String, ?> assignEntry(String identifier, Object value, String missingValueIndicator) {

		if (identifier == null || value == null)
			return null;

		Entry<String, ?> entry = null;

		// Integer
		if (value.equals(Integer.class))
			try {
				entry = new SimpleEntry<String, Integer>(identifier, Integer.parseInt(String.valueOf((int) value)));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		// Date (real date)
		else if (value.equals(Date.class))
			if (String.valueOf(value).equals(""))
				entry = new SimpleEntry<String, Date>(identifier, null);
			else
				entry = new SimpleEntry<String, Date>(identifier, ParserTools.parseDate(String.valueOf(value)));
		// Double //TODO check for Number?
		else if (value.equals(Double.class))
			if (String.valueOf(value).equals("") || String.valueOf(value).equals(missingValueIndicator))
				entry = new SimpleEntry<String, Double>(identifier, Double.NaN);
			else {
				try {
					entry = new SimpleEntry<String, Double>(identifier, new Double(String.valueOf(value).replace(",", ".")));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		// String
		else if (value.equals(String.class))
			entry = new SimpleEntry<String, String>(identifier, new String(String.valueOf(value)));
		// Boolean
		else if (value.equals(Boolean.class)) {
			String s = String.valueOf(value);

			switch (s) {
			case "j": {
				entry = new SimpleEntry<String, Boolean>(identifier, new Boolean(true));
				break;
			}
			case "V": {
				entry = new SimpleEntry<String, Boolean>(identifier, new Boolean(true));
				break;
			}
			case "1": {
				entry = new SimpleEntry<String, Boolean>(identifier, new Boolean(true));
				break;
			}
			case "1.0": {
				entry = new SimpleEntry<String, Boolean>(identifier, new Boolean(true));
				break;
			}
			case "Ja": {
				entry = new SimpleEntry<String, Boolean>(identifier, new Boolean(true));
				break;
			}
			case "ja": {
				entry = new SimpleEntry<String, Boolean>(identifier, new Boolean(true));
				break;
			}
			case "yes": {
				entry = new SimpleEntry<String, Boolean>(identifier, new Boolean(true));
				break;
			}
			case "0": {
				entry = new SimpleEntry<String, Boolean>(identifier, new Boolean(false));
				break;
			}
			case "0.0": {
				entry = new SimpleEntry<String, Boolean>(identifier, new Boolean(false));
				break;
			}
			case "Nein": {
				entry = new SimpleEntry<String, Boolean>(identifier, new Boolean(false));
				break;
			}
			case "nein": {
				entry = new SimpleEntry<String, Boolean>(identifier, new Boolean(false));
				break;
			}
			case "no": {
				entry = new SimpleEntry<String, Boolean>(identifier, new Boolean(false));
				break;
			}
			default:
				System.out.println("ParserTools.assignEntry: new boolean!!!: " + s);
				System.exit(-1);
				break;
			}
		}

		return entry;
	}
}
