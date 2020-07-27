package com.github.TKnudsen.DMandML.model.semiSupervised.activeLearning.densityWeighting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.github.TKnudsen.ComplexDataObject.data.entry.EntryWithComparableKey;
import com.github.TKnudsen.ComplexDataObject.data.features.FeatureType;
import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;
import com.github.TKnudsen.ComplexDataObject.data.ranking.Ranking;
import com.github.TKnudsen.DMandML.data.classification.IClassificationResult;
import com.github.TKnudsen.DMandML.model.semiSupervised.activeLearning.AbstractActiveLearningModel;
import com.github.TKnudsen.DMandML.model.supervised.classifier.use.IClassificationApplicationFunction;

/**
 * For more information see: An Analysis of Active Learning Strategies for
 * Sequence Labeling Tasks by Burr Settles and Mark Craven section 3.4
 *
 * @author Christian Ritter, Jürgen Bernard
 */
public class InformationDensityActiveLearning<FV extends IFeatureVectorObject<?, ?>>
		extends AbstractActiveLearningModel<FV> {

	private AbstractActiveLearningModel<FV> baseModel;
	// keeping the density map can save time later

	private Map<FV, Double> density;
	// private double beta = 1.0; unused

	protected InformationDensityActiveLearning() {

	}

	public InformationDensityActiveLearning(
			Function<List<? extends FV>, IClassificationResult<FV>> classificationApplicationFunction,
			AbstractActiveLearningModel<FV> baseModel) {
		super(classificationApplicationFunction);

		setBaseModel(baseModel);
	}

	public InformationDensityActiveLearning(IClassificationApplicationFunction<FV> classificationApplicationFunction,
			AbstractActiveLearningModel<FV> baseModel) {
		super(classificationApplicationFunction);

		setBaseModel(baseModel);
	}

	public void setBaseModel(AbstractActiveLearningModel<FV> baseModel) {
		this.baseModel = baseModel;
	}

	public void setCandidates(List<? extends FV> featureVectors) {
		super.setCandidates(featureVectors);
		density = null;
	}

	@Override
	protected void calculateRanking() {
		ranking = new Ranking<>();
		remainingUncertainty = 0.0;

		if (candidates.size() < 1)
			return;

		int U = candidates.size();
		if (density == null) {
			density = new HashMap<>();
			for (int i = 0; i < U; i++) {
				double sim = 0.0;
				for (int j = 0; j < U; j++) {
					if (i != j) {
						sim += cosineSimilarity(candidates.get(i), candidates.get(j));
					}
				}
				sim /= U;
				density.put(candidates.get(i), sim);
			}
		}
		// baseModel.setTrainingData(this.trainingFeatureVectors);
		baseModel.setCandidates(this.candidates);
		baseModel.suggestCandidates(U);

		// the ranking has to contain EntryWithComparableKey where the keys are
		// th2e informativeness of the feature vector
		Ranking<EntryWithComparableKey<Double, FV>> r = baseModel.getRanking();
		for (int i = 0; i < U; i++) {
			FV fv = r.get(i).getValue();
			double w = density.get(fv);
			double p = r.get(i).getKey();
			double uninterestingness = 1 - (1 - p) * w; // as high interest
			// needs to have low
			// values in Ranking
			ranking.add(new EntryWithComparableKey<>(uninterestingness, fv));
			remainingUncertainty += 1 - uninterestingness;
		}

		remainingUncertainty /= U;
		System.out.println("InformationDensityActiveLearning: remaining uncertainty = " + remainingUncertainty);
	}

	private double cosineSimilarity(FV fv1, FV fv2) {
		if (fv1.sizeOfFeatures() != fv2.sizeOfFeatures())
			return 0.0;
		double a = 0, b = 0, c = 0;
		for (int i = 0; i < fv1.sizeOfFeatures(); i++) {
			if (fv1.getFeature(i).getFeatureType() == FeatureType.DOUBLE
					&& fv2.getFeature(i).getFeatureType() == FeatureType.DOUBLE) {
				double v1 = (Double) fv1.getFeature(i).getFeatureValue();
				double v2 = (Double) fv2.getFeature(i).getFeatureValue();
				a += v1 * v2;
				b += v1 * v1;
				c += v2 * v2;
			}
		}
		return a / Math.sqrt(b) / Math.sqrt(c);
	}

	@Override
	public String getName() {
		return "InformationDensityActiveLearning";
	}

	@Override
	public String getDescription() {
		return "InformationDensityActiveLearning";
	}
}