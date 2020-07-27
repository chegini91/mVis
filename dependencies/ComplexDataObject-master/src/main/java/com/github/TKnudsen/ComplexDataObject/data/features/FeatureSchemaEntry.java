package com.github.TKnudsen.ComplexDataObject.data.features;

/**
 * <p>
 * Title: FeatureSchemaEntry
 * </p>
 * 
 * <p>
 * Description: Describes individual features within a FeatureSchema.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2016
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */

public class FeatureSchemaEntry<T> {

	protected final String name;
	protected final Class<T> type;
	private final FeatureType featureType;

	protected final FeatureSchema typeSchema;
	
	private Double weight = 1.0;

	public FeatureSchemaEntry(String name, Class<T> type, FeatureType featureType) {
		this(name, type, featureType, null);
	}

	public FeatureSchemaEntry(String name, Class<T> type, FeatureType featureType, FeatureSchema typeSchema) {
		this.name = name;
		this.type = type;
		this.featureType = featureType;
		this.typeSchema = typeSchema;
	}

	public String getName() {
		return name;
	}

	public Class<T> getType() {
		return type;
	}

	public FeatureType getFeatureType() {
		return featureType;
	}

	public FeatureSchema getTypeSchema() {
		return typeSchema;
	}

	@Override
	public String toString() {
		String output = "";
		output += ("Name: " + name + "\t" + "Type: " + type.getSimpleName() + "\t" + "FeatureType: " + featureType);
		return output;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}
}