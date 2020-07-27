package com.github.TKnudsen.ComplexDataObject.model.io.parsers;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IDObject;

/**
 * <p>
 * Title: IDObjectParser
 * </p>
 * 
 * <p>
 * Description: Interface for parsers to parse files towards IDObjects.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2015-2016
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.0
 */
public interface IDObjectParser<O extends IDObject> {

	/**
	 * Parses a data file and returns an List of KeyValueObjects.
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public abstract List<O> parse(String filename) throws IOException, ParseException;
}
