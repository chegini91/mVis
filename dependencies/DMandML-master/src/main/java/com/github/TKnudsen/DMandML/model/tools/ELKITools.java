package com.github.TKnudsen.DMandML.model.tools;

import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.features.numericalData.NumericalFeatureVector;

import de.lmu.ifi.dbs.elki.database.Database;
import de.lmu.ifi.dbs.elki.database.StaticArrayDatabase;
import de.lmu.ifi.dbs.elki.datasource.ArrayAdapterDatabaseConnection;
import de.lmu.ifi.dbs.elki.datasource.DatabaseConnection;

public class ELKITools {

	/**
	 * creates an ELKI database instance from a list of given feature vectors.
	 * 
	 * The database will be initialized as well.
	 * 
	 * @param featureVectors
	 * @return
	 */
	public static Database createAndInitializeELKIDatabase(List<? extends NumericalFeatureVector> featureVectors) {
		int count = featureVectors.size();
		int dimensions = featureVectors.get(0).getSize();

		double[][] data = new double[count][dimensions];
		for (int i = 0; i < count; i++) {
			data[i] = featureVectors.get(i).getVector();
		}

		DatabaseConnection dbc = new ArrayAdapterDatabaseConnection(data);
		Database db = new StaticArrayDatabase(dbc, null);

		// initialize
		db.initialize();

		return db;
	}
}
