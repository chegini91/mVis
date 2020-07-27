package com.github.TKnudsen.ComplexDataObject.model.io.writers;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataContainer;
import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;
import com.github.TKnudsen.ComplexDataObject.model.tools.WekaConversion;

import weka.core.Instances;
import weka.core.converters.ArffSaver;

/**
 * <p>
 * Title: WekaWriterTools
 * </p>
 * 
 * <p>
 * Description: Tools easing the output of Weka-associated data structures.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2016
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.0
 */
public class WekaWriterTools {

	/**
	 * simple Weka writer routine. Inspired by Weka's tutorial:
	 * https://weka.wikispaces.com/Save+Instances+to+an+ARFF+File
	 * 
	 * @param instances
	 * @param fileName
	 * @throws IOException
	 */
	public static void writeToFile(ComplexDataObject object, String fileName) throws IOException {
		ComplexDataContainer container = new ComplexDataContainer(Arrays.asList(object));
		Instances instances = WekaConversion.getInstances(container);
		writeToFile(instances, fileName);
	}

	/**
	 * simple Weka writer routine. Inspired by Weka's tutorial:
	 * https://weka.wikispaces.com/Save+Instances+to+an+ARFF+File
	 * 
	 * @param instances
	 * @param fileName
	 * @throws IOException
	 */
	public static void writeToFile(List<ComplexDataObject> objects, String fileName) throws IOException {
		ComplexDataContainer container = new ComplexDataContainer(objects);
		Instances instances = WekaConversion.getInstances(container);
		writeToFile(instances, fileName);
	}

	/**
	 * simple Weka writer routine. Inspired by Weka's tutorial:
	 * https://weka.wikispaces.com/Save+Instances+to+an+ARFF+File
	 * 
	 * @param container
	 * @param fileName
	 * @throws IOException
	 */
	public static void writeToFile(ComplexDataContainer container, String fileName) throws IOException {
		Instances instances = WekaConversion.getInstances(container);
		writeToFile(instances, fileName);
	}

	/**
	 * simple Weka writer routine. Inspired by Weka's tutorial:
	 * https://weka.wikispaces.com/Save+Instances+to+an+ARFF+File
	 * 
	 * @param instances
	 * @param fileName
	 * @throws IOException
	 */
	public static void writeToFile(Instances instances, String fileName) throws IOException {
		if (fileName == null)
			throw new IllegalArgumentException("WekaWriterTools.writeToFile: file was null.");

		File file = new File(fileName);

		ArffSaver saver = new ArffSaver();
		saver.setInstances(instances);
		saver.setFile(file);
		saver.writeBatch();
	}
}
