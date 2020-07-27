package com.github.TKnudsen.ComplexDataObject.data.uncertainty;

import java.util.Map;

/**
 * <p>
 * Title: IUncertaintyQualitative
 * </p>
 * 
 * <p>
 * Description: Tnterface for uncertainty information for qualitative data.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2015-2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.0
 */
public interface IUncertaintyQualitative<T> extends IUncertainty<T> {

	public Map<T, Double> getValueDistribution();

}
