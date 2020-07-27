package com.github.TKnudsen.DMandML.model.associations;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataContainer;
import com.github.TKnudsen.ComplexDataObject.model.tools.WekaConversion;

import weka.associations.AbstractAssociator;
import weka.core.Instances;

/**
 * <p>
 * Title: WekaAssociator
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2017 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public abstract class WekaAssociator implements IAssociator {

	protected AbstractAssociator associator;

	protected Instances data;

	protected abstract void initializeAssociator();

	public void initializeData(ComplexDataContainer dataContainer) {
		data = WekaConversion.getInstances(dataContainer);
	}

	public void buildAssociations() throws Exception {
		if (data == null)
			throw new IllegalArgumentException("Associator: no data instances defined.");

		associator.buildAssociations(data);
	}

	public AbstractAssociator getAssociator() {
		return associator;
	}

	public void printResult() {
		System.out.println(associator);
	}

}
