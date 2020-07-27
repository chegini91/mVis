package com.github.TKnudsen.ComplexDataObject.model.tools;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title: ReflectionTools
 * </p>
 *
 * <p>
 * Description: little helpers when reflection is needed.
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2017-2018
 * </p>
 *
 * @author Juergen Bernard
 * @version 1.02
 */
public class ReflectionTools {

	/**
	 * @param fields
	 * @param type
	 * @param includeSuperClassFields
	 * @return
	 */
	public static List<Field> getAllFields(List<Field> fields, Class<?> type, boolean includeSuperClassFields) {
		if (fields == null)
			fields = new ArrayList<>();

		for (Field field : type.getDeclaredFields())
			fields.add(field);

		if (includeSuperClassFields && type.getSuperclass() != null)
			fields = getAllFields(fields, type.getSuperclass(), includeSuperClassFields);

		return fields;
	}

	/**
	 * 
	 * @param fields
	 * @param instance
	 * @param targetClass
	 * @param includeSuperClassFields
	 * @param rolloutCollectionsAndMaps
	 * @return
	 */
	public static <O> List<O> getAllFieldsObjectsOfInstance(List<Field> fields, Object instance, Class<O> targetClass,
			boolean includeSuperClassFields, boolean rolloutCollectionsAndMaps) {
		List<O> targets = new ArrayList<>();

		List<Field> allFields = getAllFields(null, instance.getClass(), includeSuperClassFields);

		for (Field field : allFields) {
			if (rolloutCollectionsAndMaps) {
				Collection<?> collection = getObjectFromField(field, instance, Collection.class);
				if (collection != null)
					for (Object object : collection)
						targets.addAll(getAllFieldsObjectsOfInstance(null, object, targetClass, includeSuperClassFields,
								rolloutCollectionsAndMaps));

				Map<?, ?> map = getObjectFromField(field, instance, Map.class);
				if (map != null)
					for (Object object : map.values())
						targets.addAll(getAllFieldsObjectsOfInstance(null, object, targetClass, includeSuperClassFields,
								rolloutCollectionsAndMaps));
			}
			if (targetClass.isAssignableFrom(field.getType())) {
				try {
					field.setAccessible(true);
					O target = (O) field.get(instance);
					targets.add(target);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

		return targets;
	}

	/**
	 * 
	 * @param field
	 * @param instance
	 * @param targetClass
	 * @return
	 */
	public static <O> O getObjectFromField(Field field, Object instance, Class<O> targetClass) {
		if (!targetClass.isAssignableFrom(field.getType()))
			return null;

		try {
			field.setAccessible(true);
			O target = (O) field.get(instance);
			return target;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}
}
