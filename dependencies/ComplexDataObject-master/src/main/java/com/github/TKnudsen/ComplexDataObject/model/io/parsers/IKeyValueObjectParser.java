package com.github.TKnudsen.ComplexDataObject.model.io.parsers;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.keyValueObject.KeyValueObject;

/**
 * <p>
 * Title: IKeyValueObjectParser
 * </p>
 * 
 * <p>
 * Description: Interface for parsers to parse files towards KeyValueObjects.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2015-2016
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.0
 */
public interface IKeyValueObjectParser<O, KV extends KeyValueObject<O>> {

	/**
	 * Parses a data file and returns an List of KeyValueObjects.
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public abstract List<KV> parse(String filename) throws IOException, ParseException;
}
