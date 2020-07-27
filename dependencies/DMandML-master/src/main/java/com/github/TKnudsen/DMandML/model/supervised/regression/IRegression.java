package com.github.TKnudsen.DMandML.model.supervised.regression;

import com.github.TKnudsen.DMandML.model.supervised.ILearningModel;

/**
 * <p>
 * Title: IRegression
 * </p>
 * 
 * <p>
 * Description: basic algorithmic model that learns numerical label information.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.04
 * 
 */
public interface IRegression<X> extends ILearningModel<X, Double> {
}
