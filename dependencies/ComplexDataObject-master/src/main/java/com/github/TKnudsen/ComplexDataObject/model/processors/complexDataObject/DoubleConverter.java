package com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataContainer;
import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;

/**
 * <p>
 * Title: CollectionToBooleanAttributesConverter
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2016
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public class DoubleConverter implements IComplexDataObjectProcessor {

	String attribute;
	String targetAttribute;

	private List<String> missingValueIndicators = null;

	NumberFormat format = null;

	DecimalFormat decimalFormat = null;

	public DoubleConverter(String attribute, String targetAttribute) {
		this.attribute = attribute;
		this.targetAttribute = targetAttribute;
	}

	public DoubleConverter(String attribute, String targetAttribute, NumberFormat format) {
		this.attribute = attribute;
		this.targetAttribute = targetAttribute;

		this.format = format;
	}

	public DoubleConverter(String attribute, String targetAttribute, Locale locale) {
		this.attribute = attribute;
		this.targetAttribute = targetAttribute;

		this.format = NumberFormat.getInstance(locale);
	}

	public DoubleConverter(String attribute, String targetAttribute, Character decimalSeparator, Character thousandsSeparator) {
		this.attribute = attribute;
		this.targetAttribute = targetAttribute;

		decimalFormat = new DecimalFormat();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		symbols.setGroupingSeparator(' ');
		decimalFormat.setDecimalFormatSymbols(symbols);
	}

	public DoubleConverter(String attribute, String targetAttribute, Character decimalSeparator) {
		this.attribute = attribute;
		this.targetAttribute = targetAttribute;

		decimalFormat = new DecimalFormat();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		decimalFormat.setDecimalFormatSymbols(symbols);
	}

	@Override
	public void process(ComplexDataContainer container) {

		// add new attribute to schema
		container.addAttribute(targetAttribute, Double.class, Double.NaN);

		// parse
		for (ComplexDataObject complexDataObject : container) {
			Double d = Double.NaN;

			if (complexDataObject.getAttribute(attribute) == null)
				d = Double.NaN;
			else if (missingValueIndicators != null && missingValueIndicators.contains(complexDataObject.getAttribute(attribute).toString()))
				d = Double.NaN;
			else if (format != null)
				try {
					Number number = format.parse(complexDataObject.getAttribute(attribute).toString());
					d = number.doubleValue();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			else if (decimalFormat != null) {
				try {
					Number number = decimalFormat.parse(complexDataObject.getAttribute(attribute).toString());
					d = number.doubleValue();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else
				try {
					d = Double.parseDouble(complexDataObject.getAttribute(attribute).toString());
					complexDataObject.add(attribute, d);
				} catch (Exception e) {
					e.printStackTrace();
				}

			complexDataObject.add(targetAttribute, d);
		}
	}
	
	@Override
	public void process(List<ComplexDataObject> data) {
		ComplexDataContainer container = new ComplexDataContainer(data);
		process(container);
	}

	public List<String> getMissingValueIndicators() {
		return missingValueIndicators;
	}

	public void setMissingValueIndicators(List<String> missingValueIndicators) {
		this.missingValueIndicators = missingValueIndicators;
	}

	@Override
	public DataProcessingCategory getPreprocessingCategory() {
		return DataProcessingCategory.SECONDARY_DATA_PROVIDER;
	}
}
