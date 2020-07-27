package com.github.TKnudsen.ComplexDataObject.model.io.parsers.examples;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;
import com.github.TKnudsen.ComplexDataObject.model.io.parsers.ComplexDataObjectParser;
import com.github.TKnudsen.ComplexDataObject.model.io.parsers.ParserTools;

/**
 * <p>
 * Title: TitanicParser
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2013-2016
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.13
 */
public class TitanicParser implements ComplexDataObjectParser {

	private boolean extendedDataset = false;
	private String missingValueIndicator;
	private String tokenizer = "\t";

	public TitanicParser(String missingValueIndicator, boolean extendedDataset) {
		this.missingValueIndicator = missingValueIndicator;
		this.extendedDataset = extendedDataset;
	}

	@Override
	public List<ComplexDataObject> parse(String filename) throws IOException {

		// Prolog: create return value - data structure
		List<ComplexDataObject> data = new ArrayList<ComplexDataObject>();
		Map<Integer, Entry<String, Class<?>>> metaMapping = new HashMap<Integer, Entry<String, Class<?>>>();

		// Step1: create attribute mapping. optional: for an extended data set
		// (t.b.d.)
		List<List<String>> hauptTabelle = null;
		if (!extendedDataset) {
			hauptTabelle = parse4ColumnVariant(filename);

			metaMapping.put((Integer) 0, new AbstractMap.SimpleEntry<String, Class<?>>("CLASSID", String.class));
			metaMapping.put((Integer) 1, new AbstractMap.SimpleEntry<String, Class<?>>("ADULT", String.class));
			metaMapping.put((Integer) 2, new AbstractMap.SimpleEntry<String, Class<?>>("GENDER", String.class));
			metaMapping.put((Integer) 3, new AbstractMap.SimpleEntry<String, Class<?>>("SURVIVED", Boolean.class));
		} else {
			hauptTabelle = parse13ColumnVariant(filename);

			metaMapping.put((Integer) 0, new AbstractMap.SimpleEntry<String, Class<?>>("CLASSID", String.class));
			metaMapping.put((Integer) 1, new AbstractMap.SimpleEntry<String, Class<?>>("SURVIVED", Boolean.class));
			metaMapping.put((Integer) 3, new AbstractMap.SimpleEntry<String, Class<?>>("GENDER", String.class));
			metaMapping.put((Integer) 4, new AbstractMap.SimpleEntry<String, Class<?>>("AGE", Double.class));
			// metaMapping.put((Integer) 5, new Pair<String,
			// Class>("SIBSP, Integer.class));
			// metaMapping.put((Integer) 6, new Pair<String,
			// Class>("PARCH, Integer.class));
			metaMapping.put((Integer) 7, new AbstractMap.SimpleEntry<String, Class<?>>("TICKET", String.class));
			metaMapping.put((Integer) 8, new AbstractMap.SimpleEntry<String, Class<?>>("FARE", Double.class));
			// metaMapping.put((Integer) 9, new Pair<String,
			// Class>("CABIN, String.class));
			// metaMapping.put((Integer) 10, new Pair<String,
			// Class>("EMBARKED, String.class));
			// metaMapping.put((Integer) 11, new Pair<String,
			// Class>("BOAT, Integer.class));
			// metaMapping.put((Integer) 12, new Pair<String,
			// Class>("BODY, Integer.class));
			// metaMapping.put((Integer) 13, new Pair<String,
			// Class>("HOME_DEST, String.class));
		}

		// Step2: create ComplexDataObjects
		for (int i = 0; i < hauptTabelle.size(); i++) {

			ComplexDataObject complexDataObject = new ComplexDataObject();

			// parse columns
			for (Integer spalte : metaMapping.keySet()) {
				AbstractMap.SimpleEntry<String, ?> entry = null;
				if (hauptTabelle.get(i).size() <= spalte)
					entry = new AbstractMap.SimpleEntry<String, String>(metaMapping.get(spalte).getKey(), null);
				else if (metaMapping.get(spalte).getValue().equals(Date.class))
					if (hauptTabelle.get(i).get(spalte).equals(""))
						entry = new AbstractMap.SimpleEntry<String, Date>(metaMapping.get(spalte).getKey(), null);
					else
						entry = new AbstractMap.SimpleEntry<String, Date>(metaMapping.get(spalte).getKey(),
								ParserTools.parseDate(hauptTabelle.get(i).get(spalte)));
				else if (metaMapping.get(spalte).getValue().equals(Double.class))
					if (spalte == 8 && (hauptTabelle.get(i).get(spalte).equals("0")
							|| hauptTabelle.get(i).get(spalte).equals("0,0000")))
						entry = new AbstractMap.SimpleEntry<String, Double>(metaMapping.get(spalte).getKey(),
								Double.NaN);
					else if (hauptTabelle.get(i).get(spalte).equals(""))
						entry = new AbstractMap.SimpleEntry<String, Double>(metaMapping.get(spalte).getKey(),
								Double.NaN);
					else if (hauptTabelle.get(i).get(spalte).equals(missingValueIndicator))
						entry = new AbstractMap.SimpleEntry<String, Double>(metaMapping.get(spalte).getKey(),
								Double.NaN);
					else {
						try {
							entry = new AbstractMap.SimpleEntry<String, Double>(metaMapping.get(spalte).getKey(),
									new Double(hauptTabelle.get(i).get(spalte).replace(",", ".")));
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
					}
				else if (metaMapping.get(spalte).getValue().equals(String.class))
					entry = new AbstractMap.SimpleEntry<String, String>(metaMapping.get(spalte).getKey(),
							new String(hauptTabelle.get(i).get(spalte)));
				else if (metaMapping.get(spalte).getValue().equals(Boolean.class)) {
					String s = hauptTabelle.get(i).get(spalte);
					switch (s) {
					case "j": {
						entry = new AbstractMap.SimpleEntry<String, Boolean>(metaMapping.get(spalte).getKey(),
								new Boolean(true));
						break;
					}
					case "V": {
						entry = new AbstractMap.SimpleEntry<String, Boolean>(metaMapping.get(spalte).getKey(),
								new Boolean(true));
						break;
					}
					case "1": {
						entry = new AbstractMap.SimpleEntry<String, Boolean>(metaMapping.get(spalte).getKey(),
								new Boolean(true));
						break;
					}
					case "Ja": {
						entry = new AbstractMap.SimpleEntry<String, Boolean>(metaMapping.get(spalte).getKey(),
								new Boolean(true));
						break;
					}
					case "ja": {
						entry = new AbstractMap.SimpleEntry<String, Boolean>(metaMapping.get(spalte).getKey(),
								new Boolean(true));
						break;
					}
					case "yes": {
						entry = new AbstractMap.SimpleEntry<String, Boolean>(metaMapping.get(spalte).getKey(),
								new Boolean(true));
						break;
					}
					case "0": {
						entry = new AbstractMap.SimpleEntry<String, Boolean>(metaMapping.get(spalte).getKey(),
								new Boolean(false));
						break;
					}
					case "Nein": {
						entry = new AbstractMap.SimpleEntry<String, Boolean>(metaMapping.get(spalte).getKey(),
								new Boolean(false));
						break;
					}
					case "nein": {
						entry = new AbstractMap.SimpleEntry<String, Boolean>(metaMapping.get(spalte).getKey(),
								new Boolean(false));
						break;
					}
					case "no": {
						entry = new AbstractMap.SimpleEntry<String, Boolean>(metaMapping.get(spalte).getKey(),
								new Boolean(false));
						break;
					}
					default:
						System.out.println("new boolean!!!: " + s);
						break;
					}
				}
				if (entry != null) {
					if (entry.getValue() != null && entry.getValue() instanceof String) {
						Date date = ParserTools.parseDate((String) entry.getValue());
						if (date != null)
							complexDataObject.add(entry.getKey(), date);
						else
							complexDataObject.add(entry.getKey(), entry.getValue());
					} else
						complexDataObject.add(entry.getKey(), entry.getValue());
				} else
					throw new IllegalArgumentException("null argument exception");
			}

			if (Double.isNaN((Double) complexDataObject.getAttribute("AGE")))
				continue;

			data.add(complexDataObject);
		}

		removeMetaDataEntities(data, "FARE", Double.NaN);

		removeMetaDataEntities(data, "AGE", Double.NaN);

		return data;
	}

	private void removeMetaDataEntities(List<ComplexDataObject> data, String property, Object entity) {
		for (ComplexDataObject container : data)
			if (container.getAttribute(property) != null && container.getAttribute(property) != null)
				if (entity.getClass().equals(Double.class) && Double.isNaN((double) entity)) {
					if (container.getAttribute(property).getClass().equals(Double.class)
							&& Double.isNaN((double) container.getAttribute(property)))
						container.removeAttribute(property);
				} else if (container.getAttribute(property).equals(entity))
					container.removeAttribute(property);
	}

	private List<List<String>> parse4ColumnVariant(String hauptTabellenFile) throws IOException {
		// load from file
		List<String> rows = ParserTools.loadRows(hauptTabellenFile);

		for (int i = 0; i < rows.size(); i++) {
			int count = countSubstring(rows.get(i), "\t");
			// bei 3 is alles gut!
			if (count == 3)
				continue;
			else {
				rows.remove(i);
				i--;
			}
		}

		// get data
		List<List<String>> dataTokens = new ArrayList<List<String>>();
		int coloumbsCount = 0;
		for (int i = 0; i < rows.size(); i++) {
			String row = rows.get(i);
			List<String> lineTokens = new ArrayList<String>();
			while (true) {
				if (row.contains(tokenizer)) {
					lineTokens.add(row.substring(0, row.indexOf(tokenizer)));
					row = row.substring(row.indexOf(tokenizer) + tokenizer.length(), row.length());
					// exception: last token must be added where no tokenizer is
					// left:
					if (!row.contains(tokenizer))
						lineTokens.add(row.trim());
					continue;
				}
				dataTokens.add(lineTokens);
				if (coloumbsCount < lineTokens.size())
					coloumbsCount = lineTokens.size();
				break;
			}
		}
		return dataTokens;
	}

	private List<List<String>> parse13ColumnVariant(String hauptTabellenFile) throws IOException {
		// load from file
		List<String> rows = ParserTools.loadRows(hauptTabellenFile);

		rows.remove(0);

		for (int i = 0; i < rows.size(); i++) {
			int count = countSubstring(rows.get(i), tokenizer);
			// bei 3 is alles gut!
			if (count == 13)
				continue;
			else {
				rows.remove(i);
				i--;
			}
		}

		// get data
		List<List<String>> dataTokens = new ArrayList<List<String>>();
		int coloumbsCount = 0;
		for (int i = 0; i < rows.size(); i++) {
			String row = rows.get(i);
			List<String> lineTokens = new ArrayList<String>();
			while (true) {
				if (row.contains(tokenizer)) {
					lineTokens.add(row.substring(0, row.indexOf(tokenizer)));
					row = row.substring(row.indexOf(tokenizer) + tokenizer.length(), row.length());
					// exception: last token must be added where no tokenizer is
					// left:
					if (!row.contains(tokenizer))
						lineTokens.add(row.trim());
					continue;
				}
				dataTokens.add(lineTokens);
				if (coloumbsCount < lineTokens.size())
					coloumbsCount = lineTokens.size();
				break;
			}
		}
		return dataTokens;
	}

	/**
	 * Adds secondary data with a binned attribute for the fares paid.
	 * 
	 * @param data
	 */
	private void enrichFareAttibute(List<ComplexDataObject> data) {
		for (ComplexDataObject container : data) {

			if (container.getAttribute("FARE") != null && container.getAttribute("FARE") != null) {
				double v = 0;
				try {
					v = (double) container.getAttribute("FARE");
				} catch (Exception e) {
					System.out.println(container.getAttribute("FARE"));
				}
				if (Double.isNaN(v))
					continue;
				else if (v <= 10.0)
					container.add("FARE_Bins", "[0-10]");
				else if (v < 25)
					container.add("FARE_Bins", "[20-25]");
				else if (v < 40)
					container.add("FARE_Bins", "[25-40]");
				else if (v < 80)
					container.add("FARE_Bins", "[40-80]");
				else if (v < 200)
					container.add("FARE_Bins", "[80-200]");
				else if (v < 400)
					container.add("FARE_Bins", "[200-400]");
				else
					container.add("FARE_Bins", "[400++]");
			}
		}
	}

	private int countSubstring(String string, String subString) {
		int count = 0;
		String str = string;
		while (str.indexOf(subString) > -1) {
			str = str.replaceFirst(subString, "");
			count++;
		}
		return count;
	}

	@Override
	public String getName() {
		return "TitanicDatasetParser";
	}

	@Override
	public String getDescription() {
		return getName();
	}
}
