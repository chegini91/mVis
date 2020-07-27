package com.github.TKnudsen.DMandML.model.supervised.regression;

import java.util.ArrayList;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;

/**
 * <p>
 * Title: DecisionStump
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * @author Christian Ritter
 * @version 1.03
 * 
 */
public class BasicDecisionStump<FV extends IFeatureVectorObject<?, ?>> extends WekaRegressionWrapper<FV> {

	private boolean debug = false;

	@Override
	public String getDescription() {
		return "WEKA's implementation of the DecisionStump regression algorithm.";
	}

	@Override
	public String getName() {
		return "DecisionStump";
	}

	/**
	 * @return the doDebug
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * @param doDebug
	 *            the doDebug to set
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	@Override
	protected void initializeRegression() {
		wekaRegressionModel = new weka.classifiers.trees.DecisionStump();

		List<String> optionsList = new ArrayList<String>();

		if (isDebug())
			optionsList.add("-D"); // Enables debug mode

		String[] options = optionsList.toArray(new String[optionsList.size()]);

		try {
			wekaRegressionModel.setOptions(options);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
