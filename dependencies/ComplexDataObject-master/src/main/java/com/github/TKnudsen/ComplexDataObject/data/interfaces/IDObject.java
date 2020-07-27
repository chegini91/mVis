package com.github.TKnudsen.ComplexDataObject.data.interfaces;

/**
 * <p>
 * Title: IDObject
 * </p>
 * 
 * <p>
 * Description: interface for all objects having an identifier, a name and a
 * (small) textual description.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2011-2016
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.06
 */
public interface IDObject {

	public long getID();

	public int hashCode();

	public boolean equals(Object obj);
}
