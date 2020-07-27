package test.com.github.TKnudsen.DMandML.model.transformations.dimensionalityReduction;

import java.util.ArrayList;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVectorFactory;
import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.featureVector.EuclideanDistanceMeasure;
import com.github.TKnudsen.DMandML.model.transformations.dimensionalityReduction.MDS;

/**
 * <p>
 * Title: MultiDimensionalScalingTester
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.0
 */

public class MDSTester {
	public static void main(String[] args) {

		List<NumericalFeatureVector> fvs = new ArrayList<>();

		double[] vect1 = new double[] { 2.0, 1.0, 0.0 };
		double[] vect2 = new double[] { 2.0, 0.98, 0.0 };
		double[] vect3 = new double[] { 0.0, 1.0, 1.0 };
		double[] vect4 = new double[] { 0.0, 1.0, 0.98 };
		double[] vect5 = new double[] { 0.0, 1.0, 0.0 };
		fvs.add(NumericalFeatureVectorFactory.createNumericalFeatureVector(vect1));
		fvs.add(NumericalFeatureVectorFactory.createNumericalFeatureVector(vect2));
		fvs.add(NumericalFeatureVectorFactory.createNumericalFeatureVector(vect3));
		fvs.add(NumericalFeatureVectorFactory.createNumericalFeatureVector(vect4));
		fvs.add(NumericalFeatureVectorFactory.createNumericalFeatureVector(vect5));

		for (NumericalFeatureVector fv : fvs) {
			for (int i = 0; i < fv.getDimensions(); i++)
				System.out.print(fv.getVector()[i] + " ");
			System.out.println();
		}

		// V1: with features in the constructor
		MDS<NumericalFeatureVector> mds = new MDS<>(fvs, new EuclideanDistanceMeasure(), 2);

		// // V2 with a distance matrix in the constructor
		// EuclideanDistanceMeasure dm = new EuclideanDistanceMeasure();
		// double[][] distanceMatrix = new double[fvs.size()][fvs.size()];
		// for (int i = 0; i < fvs.size(); i++)
		// for (int j = 0; j < fvs.size(); j++)
		// distanceMatrix[i][j] = dm.getDistance(fvs.get(i), fvs.get(j));
		// MultiDimensionalScaling mds = new
		// MultiDimensionalScaling(distanceMatrix, 1);

		// ...and the story goes on...
		mds.setMaxIterations(50);
		mds.setPrintProgress(true);
		mds.calculateDimensionalityReduction();
		List<NumericalFeatureVector> transformed = mds.transform(fvs);

		for (NumericalFeatureVector fv : transformed) {
			for (int i = 0; i < fv.getDimensions(); i++)
				System.out.print(fv.getVector()[i] + " ");
			System.out.println();
		}
	}
}
