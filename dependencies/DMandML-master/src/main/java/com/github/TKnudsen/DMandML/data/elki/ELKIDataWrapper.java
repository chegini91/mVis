package com.github.TKnudsen.DMandML.data.elki;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;
import com.github.TKnudsen.DMandML.model.tools.ELKITools;

import de.lmu.ifi.dbs.elki.data.NumberVector;
import de.lmu.ifi.dbs.elki.data.type.TypeUtil;
import de.lmu.ifi.dbs.elki.database.Database;
import de.lmu.ifi.dbs.elki.database.ids.DBIDIter;
import de.lmu.ifi.dbs.elki.database.relation.Relation;

/**
 * <p>
 * Title: ELKIDataWrapper
 * </p>
 * 
 * <p>
 * Description: eases the work with ELKI's data strucutes (NumberVector,
 * Database, etc.)
 * </p>
 * 
 * <p>
 * Copyright: (c) 2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class ELKIDataWrapper {

	/**
	 * redundant data structure to store the input FVs for the clustering
	 */
	private List<NumericalFeatureVector> featureVectors;

	/**
	 * allows linking back to given feature vectors.
	 */
	private Map<NumberVector, NumericalFeatureVector> lookupTable;

	/**
	 * this is how ELKI handles feature vectors.
	 */
	private Database db;

	public ELKIDataWrapper(List<? extends NumericalFeatureVector> featureVectors) {
		setFeatureVectors(featureVectors);
	}

	public NumericalFeatureVector getFeatureVector(NumberVector numberVector) {
		NumericalFeatureVector fv = lookupTable.get(numberVector);

		if (fv == null)
			System.err.println("ELKIDataWrapper: NumberVector (elki) lookup to NumericalFeatureVector failed.");

		return fv;
	}

	public List<NumericalFeatureVector> getFeatureVectors() {
		return Collections.unmodifiableList(featureVectors);
	}

	public void setFeatureVectors(List<? extends NumericalFeatureVector> featureVectors) {
		this.featureVectors = new ArrayList<>(featureVectors);

		db = null;
		if (featureVectors == null)
			return;

		db = ELKITools.createAndInitializeELKIDatabase(featureVectors);

		// create lookup table
		lookupTable = new HashMap<>();

		Relation<NumberVector> rel = db.getRelation(TypeUtil.NUMBER_VECTOR_FIELD);

		int i = 0;
		for (DBIDIter it = rel.getDBIDs().iter(); it.valid(); it.advance()) {
			NumberVector v = rel.get(it);

			// TODO this is dirty! it assumes that the order is always preserved.
			lookupTable.put(v, featureVectors.get(i++));
		}
	}

	public Database getDB() {
		return db;
	}

	public Relation<NumberVector> getRelation() {
		return db.getRelation(TypeUtil.NUMBER_VECTOR_FIELD);
	}
}
