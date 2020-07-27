package com.github.TKnudsen.ComplexDataObject.model.io.json;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;

/**
 * <p>
 * Title: JSONLoader
 * </p>
 * 
 * <p>
 * Description: loads a ComplexDataObject from JSON
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.0
 */
public class JSONLoader {
	
	private static ObjectMapper mapper = ObjectMapperFactory.getComplexDataObjectObjectMapper();

	public static ComplexDataObject loadFromString(String json) {
//		ObjectMapper mapper = ObjectMapperFactory.getComplexDataObjectObjectMapper();

		ComplexDataObject complexDataObject;
		try {
			complexDataObject = mapper.readValue(json, ComplexDataObject.class);
			return complexDataObject;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static ComplexDataObject loadFromFile(String file) {
//		ObjectMapper mapper = ObjectMapperFactory.getComplexDataObjectObjectMapper();

		ComplexDataObject complexDataObject;
		try {
			complexDataObject = mapper.readValue(new File(file), ComplexDataObject.class);
			return complexDataObject;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static NumericalFeatureVector loadNumericalFeatureVectorFromString(String json) {
//		ObjectMapper mapper = ObjectMapperFactory.getComplexDataObjectObjectMapper();

		NumericalFeatureVector numericalFeatureVector;
		try {
			numericalFeatureVector = mapper.readValue(json, NumericalFeatureVector.class);
			return numericalFeatureVector;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static NumericalFeatureVector loadNumericalFeatureVectorFromFile(String file) {
//		ObjectMapper mapper = ObjectMapperFactory.getComplexDataObjectObjectMapper();

		NumericalFeatureVector numericalFeatureVector;
		try {
			numericalFeatureVector = mapper.readValue(new File(file), NumericalFeatureVector.class);
			return numericalFeatureVector;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
