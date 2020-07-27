package com.github.TKnudsen.DMandML.model.supervised.classifier;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;
import com.github.TKnudsen.ComplexDataObject.data.interfaces.IKeyValueProvider;
import com.github.TKnudsen.ComplexDataObject.data.keyValueObject.KeyValueProviders;

/**
 * <p>
 * Title: Classifiers
 * </p>
 * 
 * <p>
 * Description: provides additional functionality in the context of
 * classification tasks. Little helpers that should not be contained in the
 * classifier functionality in itself.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2017-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 * 
 */
public class Classifiers {
	public static <V, T extends IKeyValueProvider<V>> void setAttribute(String attributeName, List<? extends T> objects,
			List<? extends V> attributeValues) {

		KeyValueProviders.setAttribute(attributeName, objects, attributeValues);
	}

	public static <FV extends IFeatureVectorObject<?, ?>> Classifier<FV> createParameterizedCopy(
			WekaClassifierWrapper<FV> classifier) throws Exception {
		WekaClassifierWrapper<FV> classifierNew = classifier.getClass().asSubclass(WekaClassifierWrapper.class)
				.newInstance();

		classifierNew.setWekaClassifier((weka.classifiers.AbstractClassifier) weka.classifiers.AbstractClassifier
				.makeCopy(classifierNew.getWekaClassifier()));
		return classifierNew;
	}
}
