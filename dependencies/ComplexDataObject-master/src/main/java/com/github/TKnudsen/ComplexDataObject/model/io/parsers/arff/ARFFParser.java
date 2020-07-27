package com.github.TKnudsen.ComplexDataObject.model.io.parsers.arff;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.exception.NullArgumentException;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;
import com.github.TKnudsen.ComplexDataObject.model.io.parsers.ComplexDataObjectParser;
import com.github.TKnudsen.ComplexDataObject.model.io.parsers.ParserTools;

import weka.core.Instance;
import weka.core.Instances;

/**
 * <p>
 * Title: ARFFParser
 * </p>
 * 
 * <p>
 * Description: Parses ComplexDataObjects from an ARFF file. Note: this parser
 * is not part of the persistence layer. In fact, it gathers new
 * ComplexDataObjects from a given file.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.0
 */
public class ARFFParser implements ComplexDataObjectParser {

	private String missingValueIndicator = "?";

	@Override
	public List<ComplexDataObject> parse(String filename) throws IOException {
		Instances instances = parseARFF(filename);
		if (instances == null)
			return null;

		List<ComplexDataObject> data = new ArrayList<>();

		// Step1: create metaMapping
		Map<Integer, Entry<String, Class<?>>> metaMapping = WekaTools.getAttributeSchema(instances);

		// Step2: create ComplexDataObjects
		for (int zeile = 0; zeile < instances.numInstances(); zeile++) {

			Instance instance = instances.instance(zeile);

			ComplexDataObject complexDataObject = new ComplexDataObject();

			// parse columns
			for (Integer spalte = 0; spalte < instances.numAttributes(); spalte++) {

				Entry<String, ?> entry = null;
				try {
					entry = WekaTools.assignEntry(metaMapping, instance, spalte, missingValueIndicator);
				} catch (Exception e) {

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
					throw new NullArgumentException();

				if (complexDataObject.getAttribute("Name") != null)
					complexDataObject.setName(complexDataObject.getAttribute("Name").toString());

				if (complexDataObject.getAttribute("Description") != null)
					complexDataObject.setDescription(complexDataObject.getAttribute("Description").toString());

			}
			data.add(complexDataObject);
		}
		return data;
	}

	public static Instances parseARFF(String arffFile) {
		if (arffFile == null)
			return null;

		BufferedReader reader;
		try {
			FileReader fileReader = new FileReader(arffFile);
			reader = new BufferedReader(fileReader);
			Instances instances = new Instances(reader);
			reader.close();
			return instances;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getMissingValueIndicator() {
		return missingValueIndicator;
	}

	@Override
	public String getName() {
		return "ARFF Parser";
	}

	@Override
	public String getDescription() {
		return getName();
	}
}
