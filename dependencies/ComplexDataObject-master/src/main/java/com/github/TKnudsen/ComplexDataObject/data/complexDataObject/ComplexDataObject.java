package com.github.TKnudsen.ComplexDataObject.data.complexDataObject;

import java.util.ArrayList;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.ISelfDescription;
import com.github.TKnudsen.ComplexDataObject.data.keyValueObject.KeyValueObject;

/**
 * <p>
 * Title: ComplexDataObject
 * </p>
 * 
 * <p>
 * Description: ComplexDataObject is a key-value store that can be used to model
 * complex real-world objects.
 * 
 * For the use of ComplexDataObject in combination with DB solutions some
 * constructors allow the definition of the ID from an external competence.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2015-2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.05
 */
public class ComplexDataObject extends KeyValueObject<Object> implements ISelfDescription {

	protected String name;
	protected String description;

	public ComplexDataObject() {
		super();
	}

	public ComplexDataObject(long ID) {
		super(ID);
	}

	public ComplexDataObject(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	public ComplexDataObject(Long ID, String name, String description) {
		super(ID);
		this.name = name;
		this.description = description;
	}

	public String toStringInLine() {
		String output = "";
		for (String key : attributes.keySet())
			output += (key + attributes.get(key).toString() + "/t");
		return output;
	}

	@Override
	public String getName() {
		if (name != null)
			return name;

		if (getAttribute("Name") != null)
			return getAttribute("Name").toString();

		return "no name available";
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDescription() {
		if (description != null)
			return description;

		return "no description";
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * retrieves all attributes for objects matching a given class type.
	 * 
	 * @param classType
	 * @return List of attributes
	 */
	public List<String> getAttributes(Class<?> classType) {
		List<String> properties = new ArrayList<>();
		for (String property : attributes.keySet())
			if (getAttribute(property) != null && getAttribute(property) != null && getAttribute(property).getClass().equals(classType))
				if (!properties.contains(property))
					properties.add(property);
		return properties;
	}

}
