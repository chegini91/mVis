package com.github.TKnudsen.ComplexDataObject.model.io.json;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;

/**
 * <p>
 * Title: JSONWriter
 * </p>
 * 
 * <p>
 * Description: writes a ComplexDataObject as JSON
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.0
 */
public class JSONWriter {

	public static String writeToString(ComplexDataObject complexDataObject) {
		ObjectMapper mapper = ObjectMapperFactory.getComplexDataObjectObjectMapper();

		String stringRepresentation;
		try {
			stringRepresentation = mapper.writeValueAsString(complexDataObject);
			return stringRepresentation;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void writeToFile(ComplexDataObject complexDataObject, String file) {
		ObjectMapper mapper = ObjectMapperFactory.getComplexDataObjectObjectMapper();

		try {
			mapper.writeValue(new File(file), complexDataObject);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return;
	}

	public static String writeToString(NumericalFeatureVector fv) {
		ObjectMapper mapper = ObjectMapperFactory.getComplexDataObjectObjectMapper();

		String stringRepresentation;
		try {
			stringRepresentation = mapper.writeValueAsString(fv);
			return stringRepresentation;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void writeToFile(NumericalFeatureVector fv, String file) {
		ObjectMapper mapper = ObjectMapperFactory.getComplexDataObjectObjectMapper();

		try {
			mapper.writeValue(new File(file), fv);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return;
	}
}
