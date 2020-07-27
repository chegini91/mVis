package com.github.TKnudsen.ComplexDataObject.data.ranking;

import java.util.LinkedList;

/**
 * <p>
 * Title: RankingLinkedList
 * </p>
 * 
 * <p>
 * Description: structures objects in sorted manner. Based on a
 * {@link LinkedList}.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2011-2015
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.03
 */
public class RankingLinkedList<T extends Comparable<T>> extends LinkedList<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3625638957983883603L;

	public boolean add(T t) {
		for (int i = 0; i < this.size(); i++) {
			if (t.compareTo(get(i)) < 0) {
				this.add(i, t);
				return true;
			}
		}
		super.add(t);
		return true;
	}
}
