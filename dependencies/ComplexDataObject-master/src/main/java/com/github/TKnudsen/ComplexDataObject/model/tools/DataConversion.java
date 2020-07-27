package com.github.TKnudsen.ComplexDataObject.model.tools;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;

/**
 * <p>
 * Title: DataConversion
 * </p>
 *
 * <p>
 * Description: Provides little helpers for the conversion of general data
 * structures
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 *
 * @author Juergen Bernard
 * @version 1.05
 */
public class DataConversion {

	/**
	 * converts a list of Doubles to an array of double primitives. May make
	 * List<Double> become obsolete.
	 * 
	 * @param values
	 * @return
	 */
	public static double[] toPrimitives(Collection<Double> values) {
		if (values == null)
			return null;

		return values.stream().mapToDouble(Double::doubleValue).toArray();
	}

	/**
	 * converts a list of Doubles to an array of double primitives.
	 * 
	 * @param values
	 * @return
	 */
	public static double[] toPrimitives(List<Double> values) {
		if (values == null)
			return null;

		return values.stream().mapToDouble(Double::doubleValue).toArray();
	}

	/**
	 * converts a array of Doubles to an array of double primitives.
	 * 
	 * @param values
	 * @return
	 */
	public static double[] toPrimitives(Double[] values) {
		if (values == null)
			return null;

		return ArrayUtils.toPrimitive(values);
	}

	/**
	 * converts a list of Integers to an array of int primitives.
	 * 
	 * @param values
	 * @return
	 */
	public static int[] toIntPrimitives(Collection<Integer> values) {
		if (values == null)
			return null;

		return values.stream().mapToInt(Integer::intValue).toArray();
	}

	/**
	 * converts a list of Integers to an array of int primitives.
	 * 
	 * @param values
	 * @return
	 */
	public static int[] toIntPrimitives(List<Integer> values) {
		if (values == null)
			return null;

		return values.stream().mapToInt(Integer::intValue).toArray();
	}

	/**
	 * Converts an array of double primitives to an array of Double objects.
	 * 
	 * @param values
	 * @return
	 */
	public static Double[] doublePrimitivesToArray(double[] values) {
		if (values == null)
			return null;
		Double[] array = new Double[values.length];
		for (int i = 0; i < values.length; i++)
			array[i] = values[i];
		return array;
	}

	/**
	 * Converts an array of double primitives to an array of Double objects.
	 * 
	 * @param values
	 * @return
	 */
	public static List<Double> doublePrimitivesToList(double[] values) {
		return Arrays.asList(DataConversion.doublePrimitivesToArray(values));
	}

	/**
	 * Converts an array to a List.
	 * 
	 * @param values
	 * @return
	 */
	public static <T> List<T> arrayToList(T[] values) {
		return new ArrayList<T>(Arrays.asList(values));
	}

	/**
	 * Converts a given Collection to an array.
	 * 
	 * @param collection
	 * @param classType
	 * @return
	 */
	public static <T> T[] collectionToArray(Collection<T> collection, Class<T> classType) {
		List<T> list = collectionToList(collection);
		return listToArray(list, classType);
	}

	/**
	 * converts a given Collection into a List.
	 * 
	 * @param collection
	 * @return
	 */
	public static <T> List<T> collectionToList(Collection<T> collection) {
		List<T> list;
		if (collection instanceof List)
			list = (List<T>) collection;
		else
			list = new ArrayList<T>(collection);
		return list;
	}

	/**
	 * Converts a Set into a List.
	 * 
	 * @param set
	 * @return
	 */
	public static <T> List<T> setToList(Set<T> set) {
		List<T> list = new ArrayList<>();
		list.addAll(set);
		return list;
	}

	/**
	 * abstracts a List of concrete Doubles to Numbers
	 * 
	 * @param values
	 * @return
	 */
	public static List<Number> numberListFromDoubleList(List<Double> values) {
		List<Number> numbers = new ArrayList<>();
		for (Double d : values)
			numbers.add(d);
		return numbers;
	}

	/**
	 * Converts a given List to an array.
	 * 
	 * @param list
	 * @param classType
	 * @return
	 */
	public static <T> T[] listToArray(List<T> list, Class<T> classType) {
		if (list == null)
			return null;
		if (classType == null)
			throw new IllegalArgumentException("DataConversion.listToArray: class information missing.");

		@SuppressWarnings("unchecked")
		T[] array = (T[]) Array.newInstance(classType, list.size());

		for (int i = 0; i < list.size(); ++i) {
			array[i] = list.get(i);
		}

		return array;
	}
}
