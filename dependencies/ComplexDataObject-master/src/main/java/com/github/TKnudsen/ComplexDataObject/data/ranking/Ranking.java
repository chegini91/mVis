package com.github.TKnudsen.ComplexDataObject.data.ranking;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.collections4.list.TreeList;

/**
 * <p>
 * Title: Ranking
 * </p>
 * 
 * <p>
 * Description: structures objects in sorted manner. Based on Apache Common's
 * {@link TreeList} it provides fast insertion and removal, even in the middle
 * of the List. The earlier implementation extended a {@link LinkedList} - if
 * doesn't perform well it is possible to return to {@link LinkedList}.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2015-2016
 * </p>
 * 
 * @author Juergen Bernard
 * @version 2.01
 */
public class Ranking<T extends Comparable<T>> extends TreeList<T> implements Collection<T> {

	public Ranking() {
	}

	public Ranking(Iterable<T> values) {
		for (T t : values) {
			add(t);
		}
	}

	@Override
	public boolean add(T t) {
		for (int i = 0; i < this.size(); i++) {
			if (t.compareTo(get(i)) < 0) {
				// using super avoids endless loops
				super.add(i, t);
				return true;
			}
		}
		super.add(t);
		return true;
	}

	/**
	 * Fast way to add at the end of the Ranking. Tests if the sorting criterion
	 * is fulfilled, otherwise the value is added at the correct position.
	 * 
	 * @return
	 */
	public boolean addLast(T t) {
		if (size() == 0)
			return add(t);
		else
			add(size() - 1, t);
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		for (T t : c) {
			add(t);
		}
		return true;
	}

	@Override
	/**
	 * To preserve the sorting criterion addAll(int index, Collection<? extends
	 * T> c) is mounted to addAll(Collection<? extends T> c).
	 */
	public boolean addAll(int index, Collection<? extends T> c) {
		return addAll(c);
	}

	@Override
	/**
	 * Adds a value at a given index IF the sorting criterion is preserved. Else
	 * the value is added that the correct index.
	 */
	public void add(int index, T t) {
		if (size() < index)
			add(t);

		if (index == 0)
			if (size() == 0)
				super.add(index, t);
			else if (t.compareTo(get(index)) <= 0)
				super.add(index, t);
			else
				add(t);
		else if (t.compareTo(get(index - 1)) >= 0)
			if (index >= size())
				super.add(index, t);
			else if (t.compareTo(get(index)) <= 0)
				super.add(index, t);
			else
				add(t);
		else
			add(t);
		return;
	}

	@Override
	/**
	 * superclass is not visible
	 */
	public T get(int index) {
		if (size() < index)
			return null;
		return super.get(index);
	}

	public T getFirst() {
		if (size() == 0)
			return null;
		return super.get(0);
	}

	public T getLast() {
		if (size() == 0)
			return null;
		return super.get(size() - 1);
	}

	public T remove(int index) {
		return super.remove(index);
	}

	@Override
	public Iterator<T> iterator() {
		return super.iterator();
	}

	public T remove(T t) {
		T removed = null;
		for (int i = 0; i < size(); i++)
			if (t.equals(get(i))) {
				removed = super.remove(i);
				i--;
			}
		return removed;
	}

	public T removeFirst() {
		if (size() == 0)
			return null;
		return super.remove(0);
	}

	public T poll() {
		if (size() == 0)
			return null;
		return super.remove(0);
	}

	public T removeLast() {
		if (size() == 0)
			return null;
		return super.remove(size() - 1);
	}

	@Override
	/**
	 * size() was not visible
	 */
	public int size() {
		return super.size();
	}

	public boolean contains(T t) {
		return super.contains(t);
	}
}