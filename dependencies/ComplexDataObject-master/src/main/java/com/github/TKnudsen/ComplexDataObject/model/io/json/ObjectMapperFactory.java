package com.github.TKnudsen.ComplexDataObject.model.io.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class ObjectMapperFactory {

	private static ObjectMapper complexDataObjectObjectMapper;

	public static ObjectMapper getComplexDataObjectObjectMapper() {
		if (complexDataObjectObjectMapper == null)
			initComplexDataObjectObjectMapper();

		return complexDataObjectObjectMapper;
	}

	private static void initComplexDataObjectObjectMapper() {
		complexDataObjectObjectMapper = new ObjectMapper();
		complexDataObjectObjectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		complexDataObjectObjectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, true);
		complexDataObjectObjectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		complexDataObjectObjectMapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
		complexDataObjectObjectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		
	}
}
