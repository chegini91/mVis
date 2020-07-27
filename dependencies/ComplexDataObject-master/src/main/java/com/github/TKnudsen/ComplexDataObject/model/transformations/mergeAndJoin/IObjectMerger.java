package com.github.TKnudsen.ComplexDataObject.model.transformations.mergeAndJoin;

/**
 * <p>
 * Title: IObjectMerger
 * </p>
 * 
 * <p>
 * Description: Accepts two objects of a given type T and returns one NEW object
 * of a given type T
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public interface IObjectMerger<O> {

	public O merge(O object1, O object2);
}
