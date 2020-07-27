package com.github.TKnudsen.ComplexDataObject.data;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IKeyValueProvider;

/**
 * <p>
 * Title: DataSchema
 * </p>
 * 
 * <p>
 * Description: Contains and maintains the keys of a given set of key-value
 * attributes. Can be seen as a sort of header for tabular data sets.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2015-2016
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class DataSchema {
	private final String name;
	private final String description;

	// why sorted?!
	protected final SortedMap<String, DataSchemaEntry<?>> attributes = new TreeMap<String, DataSchemaEntry<?>>();

	public DataSchema() {
		this(null, null);
	}

	public DataSchema(String name) {
		this(name, null);
	}

	public DataSchema(String name, String description) {
		this.name = name;
		this.description = description;
	}

	/**
	 * Whether the DataSchemaEntry contains a given attribute.
	 * 
	 * @param key
	 * @return
	 */
	public boolean contains(String attribute) {
		return attributes.keySet().contains(attribute);
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
		return attributes.size();
	}

	@Override
	public String toString() {
		String output = "";
		for (String key : attributes.keySet())
			output += (key + ": " + attributes.get(key) + "\n");
		return output;
	}

	public String toStringInLine() {
		String output = "";
		for (String key : attributes.keySet())
			output += (key + attributes.get(key).toString() + "/t");
		return output;
	}

	/**
	 * @return a collection of the attributes names defined in this schema.
	 */
	public Collection<String> getAttributeNames() {
		return Collections.unmodifiableCollection(attributes.keySet());
	}

	/**
	 * @return a collection of the attributes entries contained in this schema.
	 */
	public Collection<DataSchemaEntry<?>> getAttributeEntries() {
		return Collections.unmodifiableCollection(attributes.values());
	}

	public DataSchemaEntry<?> getAttributeEntry(String attribute) {
		return attributes.get(attribute);
	}

	/**
	 * @return a map containing the types for each attribute defined in this
	 *         schema.
	 */
	public Map<String, Class<?>> getTypes() {
		return Collections.unmodifiableMap(attributes.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().getType())));
	}

	/**
	 * @return a map containing the default values for each attribute defined in
	 *         this schema.
	 */
	public Map<String, Object> getDefaultValues() {
		return Collections.unmodifiableMap(attributes.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().getDefaultValue())));
	}

	/**
	 * @param attribute
	 *            an attribute name.
	 * @return the type of the given attribute.
	 */
	public Class<?> getType(String attribute) {
		if (!attributes.containsKey(attribute)) {
			throw new IllegalArgumentException(String.format("unknown attribute name '%s'", attribute));
		}
		return attributes.get(attribute).getType();
	}

	/**
	 * @param attribute
	 *            an attribute name.
	 * @return the default value of the given attribute.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getDefaultValue(String attribute) {
		if (!attributes.containsKey(attribute)) {
			throw new IllegalArgumentException(String.format("unknown attribute name '%s'", attribute));
		}
		return (T) attributes.get(attribute).getDefaultValue();
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
	public <T> DataSchema add(String attribute, Class<T> type) {
		return add(attribute, type, null);
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
	public <T> DataSchema add(String attribute, Class<T> type, T defaultValue) {
		final DataSchemaEntry<T> entry = new DataSchemaEntry<T>(attribute, type, defaultValue);
		this.attributes.put(attribute, entry);

		return this;
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
	public <T extends IKeyValueProvider<?>> DataSchema add(String attribute, Class<T> type, DataSchema dataSchema) {
		return add(attribute, type, dataSchema, null);
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
	public <T extends IKeyValueProvider<?>> DataSchema add(String attribute, Class<T> type, DataSchema dataSchema, T defaultValue) {
		final DataSchemaEntry<T> entry = new DataSchemaEntry<T>(attribute, type, defaultValue, dataSchema);
		this.attributes.put(attribute, entry);

		return this;
	}

	/**
	 * Removes an attribute from the data schema.
	 * 
	 * @param attribute
	 *            the attribute name.
	 * @return the data schema instance for call-chaining.
	 */
	public DataSchema remove(String attribute) {
		this.attributes.remove(attribute);
		return this;
	}
}
