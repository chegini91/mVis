package com.github.TKnudsen.ComplexDataObject.data.interfaces;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Title: IKeyValueStore
 * </p>
 * 
 * <p>
 * Description: interface for objects containing key-value pairs. Keys are
 * referred to attributes to avoid confusion with other types of identifiers.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2015-2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.08
 */
public interface IKeyValueProvider<V> extends IDObject {

	public void add(String attribute, V value);

	public V getAttribute(String attribute);

	public Class<?> getType(String attribute);

	public Set<String> keySet();

	public Map<String, Class<?>> getTypes();

	public V removeAttribute(String attribute);

	public int hashCode();

	@Override
	public boolean equals(Object obj);
}
