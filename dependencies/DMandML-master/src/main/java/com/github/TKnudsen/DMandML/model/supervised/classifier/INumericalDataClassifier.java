package com.github.TKnudsen.DMandML.model.supervised.classifier;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;

/**
 * <p>
 * Title: INumericalDataClassifier
 * </p>
 * 
 * <p>
 * Description: basic algorithmic model that learns label information for
 * numerical features. The labels are strings, thus, classifier-like models are
 * trained.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.03
 */
public interface INumericalDataClassifier extends IClassifier<NumericalFeatureVector> {

}
