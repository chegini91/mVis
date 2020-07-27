package com.github.TKnudsen.ComplexDataObject.data.features;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * <p>
 * Title: FeatureSchema
 * </p>
 * 
 * <p>
 * Description: Contains and maintains the keys/attributes of a given set of
 * features. Can be seen as a sort of header for features tables.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2016
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.00
 */
public class FeatureSchema {
	private final String name;
	private final String description;

	// why sorted?!
	protected final SortedMap<String, FeatureSchemaEntry<?>> featureAttributes = new TreeMap<String, FeatureSchemaEntry<?>>();

	public FeatureSchema() {
		this(null, null);
	}

	public FeatureSchema(String name) {
		this(name, null);
	}

	public FeatureSchema(String name, String description) {
		this.name = name;
		this.description = description;
	}

	/**
	 * Whether the FeatureSchemaEntry contains a given attribute.
	 * 
	 * @param key
	 * @return
	 */
	public boolean contains(String attribute) {
		return featureAttributes.keySet().contains(attribute);
	}

	/**
	 * @return a string containing the name of the data schema.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return a string containing the description of the data schema.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the number of attributes defined in this schema.
	 */
	public int size() {
		return featureAttributes.size();
	}

	@Override
	public String toString() {
		String output = "";
		for (String key : featureAttributes.keySet())
			output += (key + ": " + featureAttributes.get(key) + "\n");
		return output;
	}

	public String toStringInLine() {
		String output = "";
		for (String key : featureAttributes.keySet())
			output += (key + featureAttributes.get(key).toString() + "/t");
		return output;
	}

	/**
	 * @return a collection of the attributes names defined in this schema.
	 */
	public Collection<String> getFeatureNames() {
		return Collections.unmodifiableCollection(featureAttributes.keySet());
	}

	/**
	 * @return a collection of the attributes entries contained in this schema.
	 */
	public Collection<FeatureSchemaEntry<?>> getFeatureEntries() {
		return Collections.unmodifiableCollection(featureAttributes.values());
	}

	public FeatureSchemaEntry<?> getAttributeEntry(String attribute) {
		return featureAttributes.get(attribute);
	}

	/**
	 * @return a map containing the types for each attribute defined in this
	 *         schema.
	 */
	public Map<String, Class<?>> getTypes() {
		return Collections.unmodifiableMap(featureAttributes.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().getType())));
	}

	/**
	 * @param attribute
	 *            an attribute name.
	 * @return the type of the given attribute.
	 */
	public Class<?> getType(String attribute) {
		if (!featureAttributes.containsKey(attribute)) {
			throw new IllegalArgumentException(String.format("unknown attribute name '%s'", attribute));
		}
		return featureAttributes.get(attribute).getType();
	}

	/**
	 * Introduces or updates a new attribute to the data schema with 'null' as
	 * default value.
	 * 
	 * @param attribute
	 *            the attribute name
	 * @param defaultValue
	 *            the default value in case the
	 * @return the data schema instance for call-chaining.
	 */
	public <T> FeatureSchema add(String attribute, Class<T> type, FeatureType featureType) {
		return add(attribute, type, featureType, null);
	}

	/**
	 * Introduces or updates a new attribute to the data schema.
	 * 
	 * @param attribute
	 *            the attribute name
	 * @param type
	 *            the expected data type.
	 * @param defaultValue
	 *            the default value in case the attribute is missing from a data
	 *            object.
	 * @return the data schema instance for call-chaining.
	 */
	public <T> FeatureSchema add(String attribute, Class<T> type, FeatureType featureType, FeatureSchema featureSchema) {
		final FeatureSchemaEntry<T> entry = new FeatureSchemaEntry<T>(attribute, type, featureType, featureSchema);
		this.featureAttributes.put(attribute, entry);

		return this;
	}

	/**
	 * Removes an attribute from the data schema.
	 * 
	 * @param attribute
	 *            the attribute name.
	 * @return the data schema instance for call-chaining.
	 */
	public FeatureSchema remove(String attribute) {
		this.featureAttributes.remove(attribute);
		return this;
	}
}
