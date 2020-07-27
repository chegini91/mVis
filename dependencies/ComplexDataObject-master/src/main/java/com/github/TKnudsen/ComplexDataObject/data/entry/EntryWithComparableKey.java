package com.github.TKnudsen.ComplexDataObject.data.entry;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * Title: EntryWithComparableKey
 * </p>
 * 
 * <p>
 * Description: Key-Value pair implementation of {@link Map.Entry}, with a key
 * that extends {@link Comparable}.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2015-2016
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.03
 */
public class EntryWithComparableKey<K extends Comparable<K>, V> implements Comparable<EntryWithComparableKey<K, V>>, Map.Entry<K, V>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1835568089597747740L;

	private K key;

	private V value;

	public EntryWithComparableKey(K key, V value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	@Override
	public V getValue() {
		return value;
	}

	@Override
	public V setValue(V value) {
		this.value = value;
		return this.value;
	}

	@Override
	public int compareTo(EntryWithComparableKey<K, V> o) {
		return key.compareTo(o.getKey());
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof EntryWithComparableKey) {
			try {
				@SuppressWarnings("unchecked")
				EntryWithComparableKey<K, V> p = (EntryWithComparableKey<K, V>) o;
				return p.getValue().equals(getValue()) && p.getKey().equals(getKey());
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}

	public String toString() {
		return "[Entry with comparable key. Key: " + key + ", Value: " + value + "]";
	}

	public String getName() {
		return toString();
	}

	@Override
	public int hashCode() {
		int result = 19;

		if (key != null)
			result = 29 * result + key.hashCode();

		if (value != null)
			result = 29 * result + value.hashCode();

		return result;
	}

	public String getDescription() {
		return toString();
	}
}
