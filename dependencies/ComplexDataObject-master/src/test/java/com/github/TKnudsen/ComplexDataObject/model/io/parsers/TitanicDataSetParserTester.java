package com.github.TKnudsen.ComplexDataObject.model.io.parsers;

import java.io.IOException;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;
import com.github.TKnudsen.ComplexDataObject.model.io.parsers.examples.TitanicParser;

public class TitanicDataSetParserTester {
	public static void main(String[] args) {
		System.out.println(parseTitanicDataSet());
	}

	public static List<ComplexDataObject> parseTitanicDataSet() {
		List<ComplexDataObject> titanicData = null;

		TitanicParser p = new TitanicParser("", true);
		try {
			System.out.println("Working Directory = " + System.getProperty("user.dir"));
			titanicData = p.parse("data/titanic_extended.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return titanicData;
	}
}
