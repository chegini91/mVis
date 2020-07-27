package com.github.TKnudsen.ComplexDataObject.model.processors.attributes.String;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataContainer;
import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataProcessingCategory;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.IComplexDataObjectProcessor;

public class URLAttributeRemover implements IComplexDataObjectProcessor {

	double removalThreshold = 0.8;

	private String attribute;

	public URLAttributeRemover(double removalThreshold) {
		this.removalThreshold = removalThreshold;
		this.attribute = null;
	}

	public URLAttributeRemover(double removalThreshold, String attribute) {
		this.removalThreshold = removalThreshold;
		this.attribute = attribute;
	}

	public void process(ComplexDataContainer container) {
		if (attribute == null)
			throw new IllegalArgumentException("URLAttributeRemover requires attribute definition first.");

		double urls = 0;
		double values = 0;

		for (ComplexDataObject o : container)
			if (o.getAttribute(attribute) != null)
				if (isURL(o.getAttribute(attribute).toString())) {
					urls++;
					values++;
				} else
					values++;

		urls /= values;

		if (urls >= removalThreshold)
			container.remove(attribute);
	}

	@Override
	public void process(List<ComplexDataObject> data) {
		if (attribute == null)
			throw new IllegalArgumentException("URLAttributeRemover requires attribute definition first.");

		double urls = 0;
		for (ComplexDataObject o : data)
			if (o.getAttribute(attribute) != null)
				if (isURL(o.getAttribute(attribute).toString()))
					urls++;

		urls /= (double) data.size();

		if (urls >= removalThreshold)
			for (ComplexDataObject o : data)
				o.removeAttribute(attribute);
	}

	@Override
	public DataProcessingCategory getPreprocessingCategory() {
		return DataProcessingCategory.DATA_REDUCTION;
	}

	private boolean isURL(String string) {
		try {
			URL url = new URL(string);
			return true;
		} catch (MalformedURLException e) {
			return false;
		}
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
}
