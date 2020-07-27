package com.github.TKnudsen.DMandML.model.associations;

import weka.core.Utils;

/**
 * <p>
 * Title: Apriori
 * </p>
 * 
 * <p>
 * Description: Apriori Associator from Weka.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2017 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @inproceedings{Agrawal1994, author = {R. Agrawal and R. Srikant}, booktitle =
 *                             {20th International Conference on Very Large Data
 *                             Bases}, pages = {478-499}, publisher = {Morgan
 *                             Kaufmann, Los Altos, CA}, title = {Fast
 *                             Algorithms for Mining Association Rules in Large
 *                             Databases}, year = {1994} }
 * @inproceedings{Liu1998, author = {Bing Liu and Wynne Hsu and Yiming Ma},
 *                         booktitle = {Fourth International Conference on
 *                         Knowledge Discovery and Data Mining}, pages =
 *                         {80-86}, publisher = {AAAI Press}, title =
 *                         {Integrating Classification and Association Rule
 *                         Mining}, year = {1998} }
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class Apriori extends WekaAssociator {

	@Override
	protected void initializeAssociator() {
		associator = new weka.associations.Apriori();

		String[] options = null;
		try {
			options = Utils.splitOptions("-N 500 -T 1 -C 0.5 -D 0.05 -U 1.0 -M 0.1 -V yes");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		try {
			associator.setOptions(options);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
