package com.github.TKnudsen.ComplexDataObject.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IDObject;
import com.github.TKnudsen.ComplexDataObject.data.interfaces.IKeyValueProvider;
import com.github.TKnudsen.ComplexDataObject.data.interfaces.IMasterProvider;
import com.github.TKnudsen.ComplexDataObject.data.interfaces.ISelfDescription;

/**
 * <p>
 * Title: DataStore
 * </p>
 * 
 * <p>
 * Description: stores collections of IDObjects.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2012-2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class DataStore<T extends IDObject> implements IDObject, ISelfDescription, Iterable<T> {

	private long ID;
	private String name;
	protected int hash = -1;

	/**
	 * Map<Long, T> dataMap: quick access by objectID
	 */
	private transient Map<Long, T> dataMap = new HashMap<Long, T>();

	/**
	 * List<T> dataList: access the data via an List. Consistent dataMap
	 * representation
	 */
	private transient List<T> dataList = new ArrayList<>();

	/**
	 * 
	 */
	private Set<String> attributes = new TreeSet<>();

	public DataStore(List<T> data) {
		this.ID = getRandomLong();
		if (data == null || data.size() == 0)
			return;

		// initialize data
		for (T dataObject : data) {
			if (dataObject == null)
				continue;

			if (name == null)
				name = "Data Store for Objects of Type: " + data.getClass();

			dataList.add(dataObject);
			dataMap.put(dataObject.getID(), dataObject);
		}

		initializeAttributes();
	}

	public DataStore(Map<Long, T> data) {
		this.ID = getRandomLong();
		if (data == null || data.size() == 0)
			return;

		// initialize data
		for (Long l : data.keySet()) {
			if (data.get(l) == null)
				continue;

			if (name == null)
				name = "Data Store for Objects of Type: " + data.get(l).getClass();

			dataMap.put(l, data.get(l));
			dataList.add(data.get(l));
		}

		initializeAttributes();
	}

	/**
	 * Initializes the attribute keys. Use addAttributes(T dataObject) when
	 * adding new 0bjects.
	 */
	private void initializeAttributes() {
		if (attributes == null)
			attributes = new TreeSet<>();
		for (T dataObject : dataList)
			if (dataObject instanceof IKeyValueProvider) {
				IKeyValueProvider<?> keyValueStore = (IKeyValueProvider<?>) dataObject;
				Map<String, Class<?>> types = keyValueStore.getTypes();
				if (types != null)
					for (String s : types.keySet())
						attributes.add(s);
			}
	}

	/**
	 * Adds new attributes of a given object.
	 */
	private void addAttributes(T dataObject) {
		if (attributes == null)
			initializeAttributes();

		if (dataObject instanceof IKeyValueProvider) {
			IKeyValueProvider<?> keyValueStore = (IKeyValueProvider<?>) dataObject;
			Map<String, Class<?>> types = keyValueStore.getTypes();
			if(types!=null)
			for (String s : types.keySet())
				attributes.add(s);
		}
	}

	/**
	 * Adds a List of objects to the data store
	 * 
	 * @param objects
	 */
	public void add(List<T> objects) {
		for (T t : objects)
			add(t);
	}

	/**
	 * Adds an object to the data store.
	 * 
	 * @param dobj
	 * @return
	 */
	public boolean add(T dataObject) {
		if (dataMap.containsKey(dataObject.getID()))
			return false;

		dataMap.put(dataObject.getID(), dataObject);
		if (!dataList.contains(dataObject))
			dataList.add(dataObject);

		addAttributes(dataObject);

		return true;
	}

	/**
	 * Get all data objects that have an equal name. Compares the name to the
	 * getName method when an object of the store is an instance of
	 * ITextDescription, else the toString value.
	 * 
	 * @param name
	 * @return
	 */
	public List<T> getDataByName(String name) {
		List<T> ret = new ArrayList<T>();

		for (T dataObject : dataList) {
			if (dataObject instanceof ISelfDescription)
				if (((ISelfDescription) dataObject).getName().equals(name))
					ret.add(dataObject);
				else if (dataObject.toString().equals(name))
					ret.add(dataObject);
		}

		return ret;
	}

	@Override
	public long getID() {
		return ID;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<Long, T> getDataMap() {
		return dataMap;
	}

	public List<T> getDataList() {
		return dataList;
	}

	/**
	 * 
	 * @param master
	 *            some IDObject which is probably the master of one of the
	 *            elements of the data store. Master is specific to the data
	 *            modeling context, e.g., a real-world object and it's feature
	 *            vector.
	 * @return the element(s) of the DataStore with a master object equals the
	 *         given master object.
	 */
	public List<T> getDataByMaster(IDObject master) {
		List<T> data = new ArrayList<>();
		for (T e : dataList)
			if (e instanceof IMasterProvider)
				if (((IMasterProvider) e).getMaster() != null)
					if (((IMasterProvider) e).getMaster().equals(master))
						data.add(e);
		return data;
	}

	public T getDataByID(long ID) {
		return dataMap.get(ID);
	}

	public Set<String> getAttributes() {
		return attributes;
	}

	public boolean contains(T t) {
		if (dataMap.containsKey(t.getID()))
			return true;
		return false;
	}

	@Override
	public int hashCode() {
		if (hash == -1) {

			hash = 19;

			for (Long i : dataMap.keySet())
				hash += 31 * hash + (int) dataMap.get(i).hashCode();
		}
		return hash;
	}

	public int size() {
		return dataMap.size();
	}

	@Override
	public String getDescription() {
		return getName();
	}

	@Override
	public Iterator<T> iterator() {
		return dataMap.values().iterator();
	}

	/**
	 * Little helper for the generation of a unique identifier.
	 * 
	 * @return unique ID
	 */
	private long getRandomLong() {
		return UUID.randomUUID().getMostSignificantBits();
	}
}
