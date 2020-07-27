package com.github.TKnudsen.DMandML.model.supervised.classifier;

import java.util.ArrayList;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;

import weka.classifiers.functions.LibSVM;
import weka.core.SelectedTag;

/**
 * <p>
 * Title: LibSVMClassifier
 * </p>
 * 
 * <p>
 * Description: LibSVM classifier wrapped through the WEKA library.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public abstract class LibSVMClassifier extends WekaClassifierWrapper<NumericalFeatureVector> {

	/**
	 * the type of kernel used for the SVM.
	 * 
	 * -t kernel_type : set type of kernel function (default 2)
	 * 
	 * 0 -- linear: u'*v //nice
	 * 
	 * 1 -- polynomial: (gamma*u'*v + coef0)^degree //nice, but sometimes produces
	 * WARNING: reaching max number of iterations
	 * 
	 * 2 -- radial basis function:* exp(-gamma*|u-v|^2) //appears to behave strange
	 * 
	 * 3 -- sigmoid: tanh(gamma*u'*v + coef0) //appears to behave strange
	 * 
	 */
	private SelectedTag kernelType = new SelectedTag(1, LibSVM.TAGS_KERNELTYPE);

	/**
	 * the type of SVM used.
	 * 
	 * -s svm_type : set type of SVM (default 0)
	 * 
	 * 0 -- C-SVC
	 * 
	 * 1 -- nu-SVC //no difference to 0 identified yet
	 * 
	 * 2 -- one-class SVM // Cannot handle multi-valued nominal class!
	 * 
	 * 3 -- epsilon-SVR // Cannot handle multi-valued nominal class!
	 * 
	 * 4 -- nu-SVR // Cannot handle multi-valued nominal class!
	 */
	private SelectedTag svmType = new SelectedTag(0, LibSVM.TAGS_SVMTYPE);

	/**
	 * set degree in kernel function (default 3)
	 */
	private int degree = 3;

	/**
	 * set gamma in kernel function (default 1/num_features)
	 */
	// private double gamma = ?

	/**
	 * set coef0 in kernel function (default 0)
	 */
	private double coeffInKernel = 0.0;

	/**
	 * set the parameter C of C-SVC, epsilon-SVR, and nu-SVR (default 1)
	 */
	// private int cost = 1;

	/**
	 * set cache memory size in MB (default 100)
	 */
	private int cacheSize = 100;

	public LibSVMClassifier() {

	}

	@Override
	protected void initializeClassifier() {
		setWekaClassifier(new LibSVM());

		List<String> optionsList = new ArrayList<String>();

		optionsList.add("-D");
		optionsList.add(degree + "");

		optionsList.add("-R");
		optionsList.add(coeffInKernel + "");

		optionsList.add("-M");
		optionsList.add(cacheSize + "");

		String[] options = optionsList.toArray(new String[optionsList.size()]);

		// String options = ("-S 0 -K 0 -D 3 -G 0.0 -R 0.0 -N 0.5 -M 40.0 -C 1.0 -E
		// 0.001 -P 0.1");
		// String[] optionsArray = options.split(" ");

		try {
			((LibSVM) getWekaClassifier()).setOptions(options);
			((LibSVM) getWekaClassifier()).setProbabilityEstimates(true);
			((LibSVM) getWekaClassifier()).setKernelType(kernelType);
			((LibSVM) getWekaClassifier()).setSVMType(svmType);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public SelectedTag getKernelType() {
		return kernelType;
	}

	public void setKernelType(SelectedTag kernelType) {
		this.kernelType = kernelType;

		initializeClassifier();
	}

	public SelectedTag getSvmType() {
		return svmType;
	}

	public void setSvmType(SelectedTag svmType) {
		this.svmType = svmType;

		initializeClassifier();
	}

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;

		initializeClassifier();
	}

	public int getCacheSize() {
		return cacheSize;
	}

	public void setCacheSize(int cacheSize) {
		this.cacheSize = cacheSize;

		initializeClassifier();
	}

	public double getCoeffInKernel() {
		return coeffInKernel;
	}

	public void setCoeffInKernel(double coeffInKernel) {
		this.coeffInKernel = coeffInKernel;

		initializeClassifier();
	}
}
