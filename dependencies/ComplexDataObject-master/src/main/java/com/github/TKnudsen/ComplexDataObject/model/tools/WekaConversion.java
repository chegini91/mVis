package com.github.TKnudsen.ComplexDataObject.model.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataContainer;
import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;
import com.github.TKnudsen.ComplexDataObject.data.features.Feature;
import com.github.TKnudsen.ComplexDataObject.data.features.FeatureContainer;
import com.github.TKnudsen.ComplexDataObject.data.features.FeatureType;
import com.github.TKnudsen.ComplexDataObject.data.features.FeatureVectorContainerTools;
import com.github.TKnudsen.ComplexDataObject.data.features.mixedData.MixedDataFeatureVector;
import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.ComplexDataObject.data.interfaces.IFeatureVectorObject;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

/**
 * <p>
 * Title: WekaConversion
 * </p>
 *
 * <p>
 * Description: helper tools that ease the use of WEKA data structures, i.e., Instances and Instance objects.
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2016-2017
 * </p>
 *
 * @author Juergen Bernard
 * @version 1.06
 */
public class WekaConversion {

	public static Instances getInstances(ComplexDataContainer container) {
		if (container == null)
			return null;

		List<Attribute> attrs = new ArrayList<Attribute>();
		Map<String, Attribute> attributeMap = new HashMap<>();

		int dims = container.getAttributeNames().size();
		if (!container.getAttributeNames().contains("Name")) {
			dims++;
			Attribute a = new Attribute("Name", (List<String>) null);
			attrs.add(a);
			attributeMap.put("Name", a);
		}
		if (!container.getAttributeNames().contains("Description")) {
			dims++;
			Attribute a = new Attribute("Description", (List<String>) null);
			attrs.add(a);
			attributeMap.put("Description", a);
		}

		for (Iterator<String> iterator = container.getAttributeNames().iterator(); iterator.hasNext();) {
			String string = iterator.next();

			Attribute a = null;
			if (container.isNumeric(string))
				a = new Attribute(string);
			else if (container.isBoolean(string))
				a = new Attribute(string);
			else
				a = new Attribute(string, (List<String>) null);
			attrs.add(a);
			attributeMap.put(string, a);
		}

		Instances instances = new Instances("ComplexDataContainer " + container.toString(), (ArrayList<Attribute>) attrs, container.size());

		// create instance objects
		for (ComplexDataObject cdo : container) {
			Instance instance = new DenseInstance(dims);

			Iterator<String> attNames = cdo.iterator();
			while (attNames.hasNext()) {
				String attName = attNames.next();
				Attribute attribute = attributeMap.get(attName);

				Object value = cdo.getAttribute(attName);
				if (container.isNumeric(attName)) {
					if (value != null)
						instance.setValue(attribute, ((Number) value).doubleValue());
					else
						instance.setMissing(attribute);
				} else if (container.isBoolean(attName)) {
					if (value != null) {
						Integer i = ((Boolean) value).booleanValue() ? 1 : 0;
						instance.setValue(attribute, ((Number) i).doubleValue());
					} else
						instance.setMissing(attribute);
				} else if (value != null)
					instance.setValue(attribute, value.toString());
				else
					instance.setMissing(attribute);
			}

			Attribute nameAttribute = attributeMap.get("Name");
			instance.setValue(nameAttribute, cdo.getName());

			Attribute descripptionAttribute = attributeMap.get("Description");
			instance.setValue(descripptionAttribute, cdo.getDescription());

			instances.add(instance);
		}

		return instances;
	}

	/**
	 * 
	 * @param fvs
	 * @return
	 * @deprecated use
	 */
	public static Instances getInstances(FeatureContainer<? extends IFeatureVectorObject<?, ?>> featureContainer) {

		List<Attribute> attrs = new ArrayList<Attribute>(featureContainer.getFeatureNames().size());
		for (String featureName : featureContainer.getFeatureNames()) {
			Attribute a = null;
			if (featureContainer.isNumeric(featureName))
				a = new Attribute(featureName);
			else
				a = new Attribute(featureName, (List<String>) null);
			attrs.add(a);
		}

		Instances instances = new Instances("asdf", (ArrayList<Attribute>) attrs, featureContainer.size());
		addInstances(featureContainer, instances);
		return instances;
	}

	/**
	 * 
	 * @param featureContainer
	 * @param stringToNominal
	 * @return
	 */
	public static Instances getInstances(FeatureContainer<? extends IFeatureVectorObject<?, ?>> featureContainer, boolean stringToNominal) {

		List<Attribute> attributes = createAttributes(FeatureVectorContainerTools.getObjectList(featureContainer), stringToNominal);

		// List<Attribute> attrs = new
		// ArrayList<Attribute>(featureContainer.getFeatureNames().size());
		// for (String featureName : featureContainer.getFeatureNames()) {
		// Attribute a = null;
		// if (featureContainer.isNumeric(featureName))
		// a = new Attribute(featureName);
		// else
		// a = new Attribute(featureName, (List<String>) null);
		// attrs.add(a);
		// }

		Instances instances = new Instances("asdf", (ArrayList<Attribute>) attributes, featureContainer.size());

		addInstances(featureContainer, instances);

		return instances;
	}

	/**
	 * 
	 * @param fvs
	 * @param stringToNominal
	 *            decides whether string values are represented as nominal values (with a concrete alphabet of observations)
	 * @return
	 */
	public static Instances getInstances(List<? extends IFeatureVectorObject<?, ?>> fvs, boolean stringToNominal) {
		return getInstances(fvs, stringToNominal, null);
	}

	public static Instances getInstances(List<? extends IFeatureVectorObject<?, ?>> fvs, boolean stringToNominal, Map<String, Set<String>> featureAlphabet) {
		if (fvs == null)
			return null;

		if (fvs.size() == 0)
			return null;

		List<Attribute> attrs;
		if (featureAlphabet == null)
			attrs = createAttributes(fvs, stringToNominal);
		else
			attrs = createAttributes(fvs, featureAlphabet);

		Instances data = new Instances(fvs.get(0).getClass().getName(), (ArrayList<Attribute>) attrs, fvs.size());

		addInstances(fvs, data);

		return data;
	}

	/**
	 * creates a list of WEKA attributes for a given list of FVs.
	 * 
	 * @param fvs
	 * @param stringToNominal
	 * @return
	 */
	private static List<Attribute> createAttributes(List<? extends IFeatureVectorObject<?, ?>> fvs, boolean stringToNominal) {
		if (fvs == null)
			return null;

		if (fvs.size() == 0)
			return new ArrayList<>();

		int length = fvs.get(0).sizeOfFeatures();
		List<Attribute> attributes = new ArrayList<Attribute>(length);

		for (int i = 0; i < length; i++) {
			Attribute a = null;
			if (fvs.get(0).getFeature(i).getFeatureType().equals(FeatureType.DOUBLE))
				a = new Attribute(i + 1 + "");
			else if (fvs.get(0).getFeature(i).getFeatureType().equals(FeatureType.BOOLEAN))
				a = new Attribute(i + 1 + "");
			else if (!stringToNominal)
				a = new Attribute(i + 1 + "", (List<String>) null);
			else {
				// collect alphabet
				SortedSet<String> alphabet = new TreeSet<>();
				for (IFeatureVectorObject<?, ?> fv : fvs)
					if (fv.sizeOfFeatures() == length)
						if (fv.getFeature(i) != null && fv.getFeature(i).getFeatureValue() != null)
							alphabet.add(fv.getFeature(i).getFeatureValue().toString());
						else {
							Feature<?> feature = fv.getFeature(fvs.get(0).getFeature(i).getFeatureName());
							if (feature != null && feature.getFeatureValue() != null)
								alphabet.add(feature.getFeatureValue().toString());
						}
				a = new Attribute(i + 1 + "", new ArrayList<>(alphabet));
			}
			attributes.add(a);
		}

		return attributes;
	}
	
	private static List<Attribute> createAttributes(List<? extends IFeatureVectorObject<?, ?>> fvs, Map<String, Set<String>> featureAlphabet) {
		if (fvs == null)
			return null;

		if (fvs.size() == 0)
			return new ArrayList<>();

		int length = fvs.get(0).sizeOfFeatures();
		List<Attribute> attributes = new ArrayList<Attribute>(length);

		for (int i = 0; i < length; i++) {
			Attribute a = null;
			if (fvs.get(0).getFeature(i).getFeatureType().equals(FeatureType.DOUBLE))
				a = new Attribute(i + 1 + "");
			else if (fvs.get(0).getFeature(i).getFeatureType().equals(FeatureType.BOOLEAN))
				a = new Attribute(i + 1 + "");
			else if (!featureAlphabet.containsKey(fvs.get(0).getFeature(i).getFeatureName()))
				a = new Attribute(i + 1 + "", (List<String>) null);
			else {
				a = new Attribute(i + 1 + "", new ArrayList<>(featureAlphabet.get(fvs.get(0).getFeature(i).getFeatureName())));
			}
			attributes.add(a);
		}

		return attributes;
	}

	public static void addInstances(List<? extends IFeatureVectorObject<?, ?>> fvs, Instances data) {
		if (fvs == null || fvs.size() == 0)
			return;

		int dim = fvs.get(0).getVectorRepresentation().size();
		for (IFeatureVectorObject<?, ?> fv : fvs) {
			int length = fv.getVectorRepresentation().size();
			if (dim != length)
				throw new IllegalArgumentException("List of input FV has different features.");

			addInstance(data, fv);
		}
	}

	public static void addInstances(Iterable<? extends IFeatureVectorObject<?, ?>> featureContainer, Instances instances) {
		if (featureContainer == null)
			return;

		for (IFeatureVectorObject<?, ?> fv : featureContainer) {
			addInstance(instances, fv);
		}
	}

	/**
	 * 
	 * @param fvs
	 * @param classAttribute
	 *            attribute in the features with the class information. Note: the Weka class attribute will be 'class', though.
	 * @return
	 */
	public static Instances getLabeledInstancesNumerical(List<NumericalFeatureVector> fvs, String classAttribute) {
		List<Object> labels = new ArrayList<>();
		for (int i = 0; i < fvs.size(); i++)
			if (fvs.get(i).getAttribute(classAttribute) instanceof String)
				labels.add((String) fvs.get(i).getAttribute(classAttribute));
			else
				labels.add(fvs.get(i).getAttribute(classAttribute).toString());

		Instances insances = getInstances(fvs, false);

		return addLabelsToInstances(insances, "class", labels);
	}

	/**
	 *
	 * @param fvs
	 * @param classAttribute
	 * @return
	 */
	public static Instances getLabeledInstances(List<? extends IFeatureVectorObject<?, ?>> fvs, String classAttribute, boolean stringToNominal) {
		return getLabeledInstances(fvs, null, classAttribute, stringToNominal);
	}

	/**
	 * creates instances with weights for a given List of FVs.
	 * 
	 * @param fvs
	 * @param weights
	 * @param classAttribute
	 *            attribute in the features with the class information. Note: the Weka class attribute will be 'class', though.
	 * @return
	 */
	public static Instances getLabeledInstances(List<? extends IFeatureVectorObject<?, ?>> fvs, List<Double> weights, String classAttribute, boolean stringToNominal) {
		List<Object> labels = new ArrayList<>();
		for (int i = 0; i < fvs.size(); i++)
			if (fvs.get(i).getAttribute(classAttribute) == null)
				throw new IllegalArgumentException("WekaConverter.getLabeledInstances: classAttribute not found for given FeatureVector.");
			else if (fvs.get(i).getAttribute(classAttribute) instanceof String)
				labels.add((String) fvs.get(i).getAttribute(classAttribute));
			else
				labels.add(fvs.get(i).getAttribute(classAttribute).toString());

		Instances insances = getInstances(fvs, stringToNominal);

		if (insances != null && weights != null && weights.size() == insances.size())
			addWeightsToInstances(insances, weights);

		return addLabelsToInstances(insances, "class", labels);
	}

	/**
	 * Uses WEKAs ability to assign weights to Instances.
	 * 
	 * @param instances
	 * @param weights
	 * @return
	 */
	private static Instances addWeightsToInstances(Instances instances, List<Double> weights) {
		if (instances == null || weights == null)
			return instances;

		if (instances.size() != weights.size())
			throw new IllegalArgumentException();

		for (int i = 0; i < weights.size(); i++) {
			double w = weights.get(i);
			if (Double.isNaN(w))
				w = 0;
			instances.instance(i).setWeight(w);
		}

		return instances;

	}

	public static Instances getLabeledInstances(List<? extends IFeatureVectorObject<?, ?>> fvs, List<?> labels) {
		Instances insances = getInstances(fvs, false);

		return addLabelsToInstances(insances, "class", labels);
	}

	public static Instances getRegressionValueInstances(List<? extends IFeatureVectorObject<?, ?>> fvs, List<Double> values) {
		Instances insances = getInstances(fvs, false);

		return addRegressionValuesToInstances(insances, "class", values);
	}

	public static Instances getRegressionValueInstances(List<? extends IFeatureVectorObject<?, ?>> fvs, List<Double> values, Map<String, Set<String>> featureAlphabet) {
		Instances insances = getInstances(fvs, true, featureAlphabet);

		return addRegressionValuesToInstances(insances, "class", values);
	}

	/**
	 * what is this good for?!
	 * 
	 * @param mfvs
	 * @param numLabels
	 * @param stringToNominal
	 * @return
	 */
	public static Instances getNumericLabeledMixInstances(List<MixedDataFeatureVector> mfvs, List<Double> numLabels, boolean stringToNominal) {
		Instances insances = getInstances(mfvs, stringToNominal);

		return addNumericLabelsToInstances(insances, numLabels);
	}

	/**
	 * what is this good for?!
	 * 
	 * @param insances
	 * @param numLabels
	 * @return
	 */
	private static Instances addNumericLabelsToInstances(Instances insances, List<Double> numLabels) {
		Attribute classAtt = new Attribute("num");

		insances.insertAttributeAt(classAtt, insances.numAttributes());
		insances.setClassIndex(insances.numAttributes() - 1);

		for (int i = 0; i < numLabels.size(); i++)
			insances.instance(i).setValue(insances.numAttributes() - 1, numLabels.get(i));

		return insances;

	}

	/**
	 * adds a label/class attribute to an instances object. if the list of given objects does not have the behavior of valid (categorical) class labels the class attribute is generate without any further characterization.
	 * 
	 * @param instances
	 * @param attributeName
	 * @param labels
	 * @return
	 */
	public static Instances addLabelAttributeToInstance(Instances instances, String attributeName, List<?> labels) {

		Attribute classAtt;

		if (labels != null && labels.size() > 0)
			if (labels.get(0) instanceof String) {
				Set<String> set = new LinkedHashSet<>();
				for (Object o : labels)
					set.add(o.toString());
				List<String> distinctLabels = new ArrayList<>(set);

				classAtt = new Attribute(attributeName, distinctLabels);

			} else {
				classAtt = new Attribute(attributeName);
			}
		else {
			classAtt = new Attribute(attributeName);
		}

		instances.insertAttributeAt(classAtt, instances.numAttributes());
		instances.setClass(classAtt);
		instances.setClassIndex(instances.numAttributes() - 1);

		return instances;
	}

	public static Instances addRegressionValueAttributeToInstance(Instances instances, String attributeName) {

		Attribute classAtt;
		classAtt = new Attribute(attributeName);

		instances.insertAttributeAt(classAtt, instances.numAttributes());
		instances.setClass(classAtt);
		instances.setClassIndex(instances.numAttributes() - 1);

		return instances;
	}

	public static Instances addNumericLabelAttributeToInstance(Instances insances) {
		Attribute classAtt = new Attribute("num");

		insances.insertAttributeAt(classAtt, insances.numAttributes());
		insances.setClass(classAtt);
		insances.setClassIndex(insances.numAttributes() - 1);

		return insances;

	}

	private static Instances addLabelsToInstances(Instances instances, String attributeName, List<?> labels) {
		if (instances == null)
			return null;

		Instances inst2 = addLabelAttributeToInstance(instances, attributeName, labels);

		for (int i = 0; i < labels.size(); i++)
			inst2.instance(i).setClassValue(labels.get(i).toString());

		return inst2;
	}

	private static Instances addRegressionValuesToInstances(Instances instances, String attributeName, List<Double> values) {
		if (instances == null)
			return null;

		Instances inst2 = addRegressionValueAttributeToInstance(instances, attributeName);

		for (int i = 0; i < values.size(); i++)
			inst2.instance(i).setClassValue(values.get(i));

		return inst2;
	}

	/**
	 * creates the alphabet of String elements.
	 * 
	 * @param list
	 * @return
	 */
	public static List<String> distinctListCreator(List<String> list) {
		return new ArrayList<>(new LinkedHashSet<>(list));
	}

	public static Instances replaceLabelAttributeInInstances(Instances instances, List<String> labels) {
		List<String> distinctLabels = new ArrayList<>(new LinkedHashSet<>(labels));

		Attribute classAtt = new Attribute("class", distinctLabels);

		instances.replaceAttributeAt(classAtt, instances.numAttributes() - 1);
		instances.setClass(classAtt);
		instances.setClassIndex(instances.numAttributes() - 1);

		return instances;
	}
	
	/**
	 * Add a weka instance to the given data set, that corresponds to the 
	 * given feature vector
	 * 
	 * @param instances The instances
	 * @param fv The feature vector
	 */
	private static void addInstance(Instances instances, IFeatureVectorObject<?, ?> fv) {

		if (hasOnlyValidDoubleFeatureValues(fv)) {
			double values[] = getDoubleFeatureValues(fv);
			Instance instance = new DenseInstance(1.0, values);
			instances.add(instance);
		} else {
			instances.add(new DenseInstance(fv.sizeOfFeatures()));
			Instance ins = instances.get(instances.size() - 1);
			fillInstanceByIndex(ins, fv, fv.sizeOfFeatures());
		}
	}	

	private static void fillInstanceByIndex(Instance instance, IFeatureVectorObject<?, ?> fv, int targetLength) {
		if (fv == null)
			return;

		if (fv.sizeOfFeatures() != targetLength)
			throw new IllegalArgumentException("WekaConversion: length of given featurevector does not match target instance feature length");

		List<? extends Feature<?>> features = fv.getVectorRepresentation();
		
		for (int i = 0; i < features.size(); i++) {
			Feature<?> f = features.get(i);
			if (f == null || f.getFeatureValue() == null) {
				instance.setMissing(i);
				continue;
			}
			if (f.getFeatureType() == FeatureType.DOUBLE)
				instance.setValue(i, (Double) f.getFeatureValue());
			else if (f.getFeatureType() == FeatureType.STRING) {
				Object o = f.getFeatureValue();
				instance.setValue(i, o.toString());
			} else if (f.getFeatureType() == FeatureType.BOOLEAN) {
				Boolean b = (Boolean) f.getFeatureValue();
				Integer integer = b.booleanValue() ? 1 : 0;
				instance.setValue(i, ((Number) integer).doubleValue());
			} else
				throw new IllegalArgumentException("WekaConversion: unsupported feature type");
		}
	}
	
	/**
	 * Returns the values of all features of the given vector, assuming that
	 * {@link #hasOnlyValidDoubleFeatureValues(IFeatureVectorObject)} returns
	 * <code>true</code> for the given vector.
	 * 
	 * @param fv The feature vector
	 * @return The double values
	 */
	private static double[] getDoubleFeatureValues(IFeatureVectorObject<?, ?> fv) {
		int n = fv.sizeOfFeatures();
		double featureValues[] = new double[n];
		for (int i = 0; i < n; i++) {
			Feature<?> f = fv.getFeature(i);
			Object v = f.getFeatureValue();
			featureValues[i] = (Double)v;
		}
		return featureValues;
	}
	
	/**
	 * Returns whether all features of the given feature vector are not <code>null</code>
	 * and have the type "Double"
	 *  
	 * @param fv The feature vector
	 * @return Whether the vector contains only valid double values
	 */
	private static boolean hasOnlyValidDoubleFeatureValues(IFeatureVectorObject<?, ?> fv) {
		int n = fv.sizeOfFeatures();
		for (int i = 0; i < n; i++) {
			Feature<?> f = fv.getFeature(i);
			if (f == null || f.getFeatureValue() == null) {
				return false;
			}
			if (f.getFeatureType() != FeatureType.DOUBLE) {
				return false;
			}
		}
		return true;
	}
}
