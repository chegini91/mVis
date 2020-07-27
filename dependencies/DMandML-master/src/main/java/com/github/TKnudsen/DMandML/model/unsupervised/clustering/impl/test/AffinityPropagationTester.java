package com.github.TKnudsen.DMandML.model.unsupervised.clustering.impl.test;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;
import com.github.TKnudsen.ComplexDataObject.data.dataFactory.DataFactory;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.model.processors.features.numericalData.MinMaxNormalization;
import com.github.TKnudsen.ComplexDataObject.model.processors.features.numericalData.MissingValueRemover;
import com.github.TKnudsen.ComplexDataObject.model.transformations.descriptors.numericalFeatures.NumericalFeatureVectorDescriptor;
import com.github.TKnudsen.DMandML.data.cluster.featureVector.numerical.NumericalFeatureVectorClusterResult;
import com.github.TKnudsen.DMandML.model.unsupervised.clustering.impl.AffinityPropagation;

public class AffinityPropagationTester {

	public static void main(String[] args) {
		List<ComplexDataObject> titanicData = DataFactory.createTitanicDataSet();

		NumericalFeatureVectorDescriptor descriptor = new NumericalFeatureVectorDescriptor();
		List<NumericalFeatureVector> fvs = descriptor.transform(titanicData);

		MissingValueRemover missingValueRemover = new MissingValueRemover();
		missingValueRemover.process(fvs);

		MinMaxNormalization normalization = new MinMaxNormalization();
		normalization.process(fvs);

		AffinityPropagation ap = new AffinityPropagation();
		ap.setFeatureVectors(fvs);
		ap.calculateClustering();
		NumericalFeatureVectorClusterResult clusteringResult = ap.getClusteringResult();

		System.out.println(clusteringResult);
	}

}
