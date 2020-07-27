package com.github.TKnudsen.ComplexDataObject.data;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IKeyValueProvider;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description: Describes individual attributes of key-value data structures.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2016
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.0
 */

public class DataSchemaEntry<T> {

	protected final String name;
	protected final Class<T> type;
	protected final T defaultValue;
	protected final DataSchema typeSchema;

	public DataSchemaEntry(String name, Class<T> type, T defaultValue) {
		this(name, type, defaultValue, null);
	}

	public DataSchemaEntry(String name, Class<T> type, T defaultValue, DataSchema typeSchema) {
		if (!IKeyValueProvider.class.isAssignableFrom(type) && typeSchema != null) {
			throw new IllegalArgumentException("types with a defined typeSchema must inherit IDataObject");
		}

		this.name = name;
		this.type = type;
		this.typeSchema = typeSchema;
		this.defaultValue = defaultValue;
	}

	public String getName() {
		return name;
	}

	public Class<T> getType() {
		return type;
	}

	public T getDefaultValue() {
		return defaultValue;
	}

	public DataSchema getTypeSchema() {
		return typeSchema;
	}

	public boolean isComplexType() {
		return typeSchema != null;
	}

	@Override
	public String toString() {
		String output = "";
		output += ("Name: " + name + "\t" + "Type: " + type.getSimpleName() + "\t" + "Default Value: " + defaultValue);
		return output;
	}
}