package com.github.TKnudsen.DMandML.model.supervised.classifier.impl.numericalFeatures;

import com.github.TKnudsen.DMandML.model.supervised.classifier.LibSVMClassifier;

import weka.classifiers.functions.LibSVM;
import weka.core.SelectedTag;

/**
 * <p>
 * Title: SVMPolynomialClassifier
 * </p>
 * 
 * <p>
 * Description: SVM classifier with a polynomial kernel.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class SVMPolynomialClassifier extends LibSVMClassifier {

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
	private static int kernelType = 1;

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

	public SVMPolynomialClassifier() {
		super();

		setKernelType(new SelectedTag(kernelType, LibSVM.TAGS_KERNELTYPE));
	}

}
