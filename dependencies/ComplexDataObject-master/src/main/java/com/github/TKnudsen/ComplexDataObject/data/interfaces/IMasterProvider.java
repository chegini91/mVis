package com.github.TKnudsen.ComplexDataObject.data.interfaces;

/**
 * <p>
 * Title: IMasterProvider
 * </p>
 * 
 * <p>
 * Description: interface for all objects having a master object. Allows
 * modeling object hierarchies.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2011-2016
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public interface IMasterProvider extends IDObject {
	public IDObject getMaster();
}
