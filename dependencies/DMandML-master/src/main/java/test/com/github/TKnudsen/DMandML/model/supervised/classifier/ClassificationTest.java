package test.com.github.TKnudsen.DMandML.model.supervised.classifier;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataContainer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeature;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.model.io.parsers.arff.ARFFParser;
import com.github.TKnudsen.ComplexDataObject.model.io.parsers.objects.DoubleParser;
import com.github.TKnudsen.ComplexDataObject.model.transformations.descriptors.numericalFeatures.NumericalFeatureVectorDescriptor;
import com.github.TKnudsen.DMandML.data.classification.IClassificationResult;
import com.github.TKnudsen.DMandML.model.supervised.classifier.IClassifier;
import com.github.TKnudsen.DMandML.model.supervised.classifier.impl.numericalFeatures.RandomForest;
import java.util.Iterator;

/**
 * <p>
 * Title: ClassificationTest
 * </p>
 *
 * <p>
 * Description: Simple test/example of a classifier applied on
 * NumericalFeatureVectors extracted from the Titanic dataset.
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 *
 * @author Juergen Bernard
 * @version 1.01
 */
public class ClassificationTest {

    private static String classAttribute = "class";

    public static void main(String[] args) {

        // Some data set
        List<ComplexDataObject> dataSet = null;

        try {
            System.out.println("Working Directory = " + System.getProperty("user.dir"));
            ARFFParser arffParser = new ARFFParser();
            dataSet = arffParser.parse("data/cars.arff");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Descriptor: transforms ComplexDataObjects into NumericalFeatureVectors
        NumericalFeatureVectorDescriptor descriptor = new NumericalFeatureVectorDescriptor();
        List<NumericalFeatureVector> featureVectors = descriptor.transform(dataSet);

        // add target variable: the "CLASSID" (1-3) the passengers traveled with.
        // by design, the class attribute (target variable) is not part of the content
        // of the NumericalFeatureVector, but is an additional attribute stored
        // in an embodied key-value pair metadata structure.
        for (int i = 0; i < dataSet.size(); i++) {
            featureVectors.get(i).add(classAttribute, dataSet.get(i).getAttribute("num-of-cylinders").toString());
        }

        // split into training and testing data
        List<NumericalFeatureVector> trainingVectors = new ArrayList<>();
        List<NumericalFeatureVector> testingVectors = new ArrayList<>();

        for (int i = 0; i < featureVectors.size(); i++) {
            if (i % 2 == 0) {
                trainingVectors.add(featureVectors.get(i));
            } else {
                testingVectors.add(featureVectors.get(i));
            }
        }

        //access the data in dataset 
        /**/
        ComplexDataContainer container = new ComplexDataContainer(dataSet);
        Iterator<ComplexDataObject> iterator = container.iterator();
        while (iterator.hasNext()) {
            ComplexDataObject cdo = iterator.next();

            for (String attribute : container.getAttributeNames()) {
                System.out.print(attribute + ": ");
                System.out.print(cdo.getAttribute(attribute)+ "   ");
            }
            System.out.println("");
        }
        //*/

        // Classifier - target variable is fixed to "class"
        // IClassifier<NumericalFeatureVector> classifier = new BayesNet();
        // IClassifier<NumericalFeatureVector> classifier = new DecorateClassifier();
        // IClassifier<NumericalFeatureVector> classifier = new GaussianProcesses();
        // IClassifier<NumericalFeatureVector> classifier = new NaiveBayesMultinomial();
        IClassifier<NumericalFeatureVector> classifier = new RandomForest();
		// IClassifier<NumericalFeatureVector> classifier = new SMOSVN();
        // IClassifier<NumericalFeatureVector> classifier = new SVMLinearClassifier();
        // IClassifier<NumericalFeatureVector> classifier = new
        // SVMPolynomialClassifier();

        // (1) train classification model
        classifier.train(trainingVectors);

        // (2) test the model
        IClassificationResult<NumericalFeatureVector> classificationResult = classifier
                .createClassificationResult(testingVectors);

        for (NumericalFeatureVector fv
                : classificationResult.getFeatureVectors()) {
            System.out.println(fv.getName() + ", predicted CLASS = " + classificationResult.getClass(fv)
                    + ", true CLASS = " + fv.getAttribute("class") + ", Probabilities: "
                    + classificationResult.getLabelDistribution(fv));
        }

        System.out.println(
                "EXAMPLE FINISHED");
    }

}
