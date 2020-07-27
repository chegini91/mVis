package com.github.TKnudsen.ComplexDataObject.model.processors.features.mixedData.featureSelection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.TKnudsen.ComplexDataObject.data.entry.EntryWithComparableKey;
import com.github.TKnudsen.ComplexDataObject.data.features.FeatureType;
import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeatureContainer;
import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeatureVector;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DataProcessingCategory;
import com.github.TKnudsen.ComplexDataObject.model.processors.features.mixedData.IMixedDataFeatureVectorProcessor;

/**
 * @author Christian Ritter
 *
 */
public class MutualInformationFeatureSelection implements IMixedDataFeatureVectorProcessor {

	private int discretization = 100;

	private List<MixedDataFeatureVector> labeledFVs = null;

	private List<String> labels = null;

	private int nrFeatures;

	public MutualInformationFeatureSelection(List<MixedDataFeatureVector> labeledFVs, List<String> labels, int nrOfFeaturesToSelect) {
		if (labeledFVs == null || labels == null || labeledFVs.size() == 0) {
			throw new IllegalArgumentException("List must not be null or of size 0");
		}
		if (labeledFVs.size() != labels.size()) {
			throw new IllegalArgumentException("List have to be of the same size");
		}
		this.labeledFVs = new ArrayList<>(labeledFVs);
		this.labels = labels;
		this.nrFeatures = nrOfFeaturesToSelect;
	}

	public MutualInformationFeatureSelection(List<MixedDataFeatureVector> labeledFVs, String labelAttributeName, int nrOfFeatureToSelect) {
		if (labeledFVs == null || labeledFVs.size() == 0) {
			throw new IllegalArgumentException("List must not be null or of size 0");
		}
		labels = new ArrayList<>();
		for (MixedDataFeatureVector fv : labeledFVs) {
			labels.add(fv.getAttribute(labelAttributeName).toString());
		}
		this.labeledFVs = new ArrayList<>(labeledFVs);
		this.nrFeatures = nrOfFeatureToSelect;
	}

	private double[] calculateLabelMI() {
		MixedDataFeatureVector fv0 = labeledFVs.get(0);
		double[] mutualInformation = new double[fv0.sizeOfFeatures()];
		// map containing a list of the feature values of each vector for every feature
		Map<Integer, List<Double>> featureMap = new HashMap<>();
		for (int i = 0; i < fv0.sizeOfFeatures(); i++) {
			if (fv0.getFeature(i).getFeatureType() == FeatureType.DOUBLE) {
				List<Double> lst = new ArrayList<>();
				for (MixedDataFeatureVector fv : labeledFVs) {
					lst.add((Double) fv.getFeature(i).getFeatureValue());
				}
				featureMap.put(i, discretize(lst));
			} else if (fv0.getFeature(i).getFeatureType() == FeatureType.STRING) {
				List<String> lst = new ArrayList<>();
				for (MixedDataFeatureVector fv : labeledFVs) {
					lst.add((String) fv.getFeature(i).getFeatureValue());
				}
				featureMap.put(i, numerify(lst));
			} else if (fv0.getFeature(i).getFeatureType() == FeatureType.BOOLEAN) {
				List<Boolean> lst = new ArrayList<>();
				for (MixedDataFeatureVector fv : labeledFVs) {
					lst.add((Boolean) fv.getFeature(i).getFeatureValue());
				}
				featureMap.put(i, numerifyBools(lst));
			}
		}

		// calculate probability distribution for each feature
		Map<Integer, Map<Double, Double>> probabilities = new HashMap<>();
		double v = 1.0 / labeledFVs.size();
		for (int i : featureMap.keySet()) {
			Map<Double, Double> map = new HashMap<>();
			for (double d : featureMap.get(i)) {
				if (map.get(d) == null)
					map.put(d, v);
				else
					map.put(d, map.get(d) + v);
			}
			probabilities.put(i, map);
		}

		Map<Double, Double> labelProbs = new HashMap<>();
		List<Double> numLabels = numerify(labels);
		for (double d : numLabels) {
			if (labelProbs.get(d) == null)
				labelProbs.put(d, v);
			else
				labelProbs.put(d, labelProbs.get(d) + v);
		}
		for (int i = 0; i < probabilities.size(); i++) {
			Map<Double, Double> pi = probabilities.get(i);
			List<Double> fi = featureMap.get(i);
			Map<EntryWithComparableKey<Double, Double>, Double> prob = new HashMap<>();
			for (int k = 0; k < fi.size(); k++) {
				EntryWithComparableKey<Double, Double> p = new EntryWithComparableKey<>(fi.get(k), numLabels.get(k));
				if (prob.get(p) == null) {
					prob.put(p, v);
				} else {
					prob.put(p, prob.get(p) + v);
				}
			}
			double mi = 0;
			for (double y : labelProbs.keySet()) {
				for (double x : pi.keySet()) {
					Double p = prob.get(new EntryWithComparableKey<>(x, y));
					if (p != null)
						mi += p * Math.log(p / pi.get(x) / labelProbs.get(y));
				}
			}
			mutualInformation[i] = mi;
		}
		return mutualInformation;
	}

	private List<Double> discretize(List<Double> features) {
		double max = features.stream().max(Comparator.<Double> naturalOrder()).get();
		double min = features.stream().min(Comparator.<Double> naturalOrder()).get();
		double d = max - min;
		List<Double> res = new ArrayList<>();
		int m = discretization - 1;
		for (double v : features) {
			v -= min;
			int i = (int) (v * m);
			res.add(d * i / m);
		}
		return res;
	}

	public int getDiscretization() {
		return discretization;
	}

	@Override
	public DataProcessingCategory getPreprocessingCategory() {
		return DataProcessingCategory.DATA_REDUCTION;
	}

	private List<Double> numerify(List<String> features) {
		List<String> featureVals = new ArrayList<>();
		List<Double> res = new ArrayList<>();
		for (String s : features) {
			if (featureVals.contains(s)) {
				res.add((double) featureVals.indexOf(s));
			} else {
				featureVals.add(s);
				res.add((double) featureVals.indexOf(s));
			}
		}
		return res;
	}

	private List<Double> numerifyBools(List<Boolean> features) {
		List<Double> res = new ArrayList<>();
		for (Boolean b : features) {
			if (b)
				res.add(1.0);
			else
				res.add(0.0);
		}
		return res;
	}

	@Override
	public void process(List<MixedDataFeatureVector> data) {
		process(new MixedDataFeatureContainer(data));
	}

	@Override
	public void process(MixedDataFeatureContainer container) {
		double[] labelMI = null;
		labelMI = calculateLabelMI();

		List<Integer> selectedIndices = new ArrayList<>();
		int[] sorted = weka.core.Utils.stableSort(labelMI);

		for (int i = 0; i < sorted.length - nrFeatures; i++) {
			selectedIndices.add(sorted[i]);
		}
		IndexFeatureSelection ifs = new IndexFeatureSelection(selectedIndices);
		ifs.process(container);
	}

	public void setDiscretization(int discretization) {
		this.discretization = discretization;
	}

	public List<String> getSortedFeatureNames() {
		double[] labelMI = null;
		labelMI = calculateLabelMI();

		int[] sorted = weka.core.Utils.stableSort(labelMI);
		List<Integer> sortedList = new ArrayList<>();
		List<String> sortedFs = new ArrayList<>();
		for (int i : sorted) {
			sortedList.add(i);
		}
		Collections.reverse(sortedList);
		
		MixedDataFeatureVector sample = labeledFVs.get(0);

		for (int i = 0; i < sortedList.size(); i++) {
			sortedFs.add(sample.getFeature(sortedList.indexOf(i)).getFeatureName());
		}
		return sortedFs;
	}

}
