package com.github.TKnudsen.DMandML.model.evaluation.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.github.TKnudsen.DMandML.model.evaluation.IModelEvaluation;
import com.github.TKnudsen.DMandML.model.supervised.ILearningModel;

/**
 * <p>
 * Title: CSVModelEvaluation
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Christian Ritter
 * @version 1.03
 * 
 */
public class CSVModelEvaluationIO<X, Y, L extends ILearningModel<X, Y>> extends AbstractModelEvaluationIO<X, Y, L> {

	public CSVModelEvaluationIO() {
		super();
	}

	public CSVModelEvaluationIO(IModelEvaluation<X, Y, L> modelEvaluation) {
		super(modelEvaluation);
	}

	@Override
	public void exportResult(String directory) {
		// simple header containing all meta data
		StringBuilder sb = new StringBuilder();
		sb.append("metadata");
		for (String k : getAllMetaData().keySet()) {
			sb.append(",");
			sb.append(k);
			sb.append("=");
			sb.append(getAllMetaData().get(k));
		}
		sb.append(System.lineSeparator());

		setOrderedPerformanceMeasureList(new ArrayList<>());
		// column names
		sb.append("index");
		for (String pm : getPerformanceValues().keySet()) {
			sb.append(",");
			sb.append(pm);
			getOrderedPerformanceMeasureList().add(pm);
		}
		sb.append(System.lineSeparator());

		// cumulated values
		sb.append("cumulated");
		for (String pm : getOrderedPerformanceMeasureList()) {
			sb.append(",");
			sb.append(String.valueOf(getCumulatedPerformanceValue(pm)));
		}
		sb.append(System.lineSeparator());

		// single values
		if (getOrderedPerformanceMeasureList().size() > 0) {
			int n = getPerformanceValue(getOrderedPerformanceMeasureList().get(0)).size();
			for (int i = 0; i < n; i++) {
				sb.append(i);
				for (String pm : getOrderedPerformanceMeasureList()) {
					sb.append(",");
					sb.append(getPerformanceValue(pm).get(i));
				}
				sb.append(System.lineSeparator());
			}
		}

		// write output to file
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(directory)));
			bw.write(sb.toString());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getDescription() {
		return "Exports / imports evaluation results from / to CSV-files.";
	}

	@Override
	public String getName() {
		return "CSV Model Evaluation IO";
	}

	@Override
	public void importResult(String directory) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(directory)));
			String line = br.readLine();

			if (line == null) {
				br.close();
				throw new IOException("File is empty.");
			}

			// read meta data
			String[] vals = line.split(",");
			if (vals.length == 0 || !vals[0].equals("metadata")) {
				br.close();
				throw new IOException("File is malformed.");
			}

			for (int i = 1; i < vals.length; i++) {
				String[] kv = vals[i].split("=");
				getAllMetaData().put(kv[0], kv[1]);
			}

			line = br.readLine();

			// read performance measure names
			setOrderedPerformanceMeasureList(new ArrayList<>());
			vals = line.split(",");
			if (vals.length == 0 || !vals[0].equals("index")) {
				br.close();
				throw new IOException("File is malformed.");
			}

			for (int i = 1; i < vals.length; i++) {
				getOrderedPerformanceMeasureList().add(vals[i]);
			}

			line = br.readLine();

			// read cumulated values
			vals = line.split(",");
			if (vals.length == 0 || !vals[0].equals("cumulated")) {
				br.close();
				throw new IOException("File is malformed.");
			}

			for (int i = 1; i < vals.length; i++) {
				getCumulatedPerformanceValues().put(getOrderedPerformanceMeasureList().get(i - 1),
						Double.valueOf(vals[i]));
			}

			line = br.readLine();

			// read remaining values
			while (line != null) {
				vals = line.split(",");

				for (int i = 1; i < vals.length; i++) {
					if (getPerformanceValues().get(getOrderedPerformanceMeasureList().get(i - 1)) == null)
						getPerformanceValues().put(getOrderedPerformanceMeasureList().get(i - 1), new ArrayList<>());
					getPerformanceValues().get(getOrderedPerformanceMeasureList().get(i - 1))
							.add(Double.valueOf(vals[i]));
				}
				line = br.readLine();
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
