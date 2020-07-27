package com.github.TKnudsen.ComplexDataObject.model.processors.attributes.Double;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataContainer;
import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;
import com.github.TKnudsen.ComplexDataObject.model.io.parsers.objects.DoubleParser;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataProcessingCategory;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.IComplexDataObjectProcessor;

public class AttributeNumerificationProcessor implements IComplexDataObjectProcessor {

	private String attribute;

	private final DoubleParser doubleParser = new DoubleParser();

	public AttributeNumerificationProcessor() {
		this.attribute = null;
	}

	public AttributeNumerificationProcessor(String attribute) {
		this.attribute = attribute;
	}

	public void process(ComplexDataContainer container) {
		if (attribute == null)
			throw new IllegalArgumentException("AttributeNumerificationProcessor requires attribute definition first.");

		// strategy: parse to double and buffer the values
		// then remove attribute and re-create attribute in the container
		Map<ComplexDataObject, Double> values = new HashMap<>();
		for (ComplexDataObject cdo : container) {
			if (cdo.getAttribute(attribute) != null) {
				Double d = doubleParser.apply(cdo.getAttribute(attribute));
				values.put(cdo, d);
			} else
				values.put(cdo, Double.NaN);
		}

		container.remove(attribute);
		container.addAttribute(attribute, Double.class, Double.NaN);
		for (ComplexDataObject cdo : container)
			cdo.add(attribute, values.get(cdo));
	}

	@Override
	public void process(List<ComplexDataObject> data) {
		if (attribute == null)
			throw new IllegalArgumentException("AttributeNumerificationProcessor requires attribute definition first.");

		for (ComplexDataObject cdo : data) {
			if (cdo.getAttribute(attribute) != null) {
				Double d = doubleParser.apply(cdo.getAttribute(attribute));
				cdo.add(attribute, d);
			} else
				cdo.add(attribute, Double.NaN);
		}
	}

	@Override
	public DataProcessingCategory getPreprocessingCategory() {
		return DataProcessingCategory.DATA_CLEANING;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
}
