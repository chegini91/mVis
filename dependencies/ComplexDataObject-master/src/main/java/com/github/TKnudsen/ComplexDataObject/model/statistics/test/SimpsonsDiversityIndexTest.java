package com.github.TKnudsen.ComplexDataObject.model.statistics.test;

import com.github.TKnudsen.ComplexDataObject.model.statistics.SimpsonsIndex;

/**
 * <p>
 * Title: SimpsonsDiversityIndexTest
 * </p>
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2016-2017
 * </p>
 *
 * @author Juergen Bernard
 * @version 1.01
 */
public class SimpsonsDiversityIndexTest {

	public static void main(String args[]) {

		// you should use integers. iff the index is use for double values this
		// is how the index can be addressed.
		double[] probability = new double[5];
		probability[0] = 0.3;
		probability[1] = 0.2;
		probability[2] = 0.2;
		probability[3] = 0.2;
		probability[4] = 0.1;

		int[] distribution = SimpsonsIndex.transformToIntDistribution(probability);
		
		distribution[0] = 1;
		distribution[1] = 2;
		distribution[2] = 1;
		distribution[3] = 1;
		distribution[4] = 1;

		System.out.println("Simpsons Diversity Index: " + SimpsonsIndex.calculateSimpsonsIndexOfDiversity(distribution));
	}

}
