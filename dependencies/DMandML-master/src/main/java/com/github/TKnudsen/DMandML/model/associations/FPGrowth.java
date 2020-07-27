package com.github.TKnudsen.DMandML.model.associations;

/**
 * <p>
 * Title: FPGrowth
 * </p>
 * 
 * <p>
 * Description: FPGrowth Associator from Weka.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2017 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @inproceedings{Han2000, author = {J. Han and J.Pei and Y. Yin}, booktitle =
 *                         {Proceedings of the 2000 ACM-SIGMID International
 *                         Conference on Management of Data}, pages = {1-12},
 *                         title = {Mining frequent patterns without candidate
 *                         generation}, year = {2000} }
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public class FPGrowth extends WekaAssociator {

	@Override
	protected void initializeAssociator() {
		associator = new weka.associations.FPGrowth();
		
		// Valid options are:
		//
		// -P <attribute index of positive value>
		// Set the index of the attribute value to consider as 'positive'
		// for binary attributes in normal dense instances. Index 2 is always
		// used for sparse instances. (default = 2)
		//
		// -I <max items>
		// The maximum number of items to include in large items sets (and
		// rules). (default = -1, i.e. no limit.)
		//
		// -N <require number of rules>
		// The required number of rules. (default = 10)
		//
		// -T <0=confidence | 1=lift | 2=leverage | 3=Conviction>
		// The metric by which to rank rules. (default = confidence)
		//
		// -C <minimum metric score of a rule>
		// The minimum metric score of a rule. (default = 0.9)
		//
		// -U <upper bound for minimum support>
		// Upper bound for minimum support. (default = 1.0)
		//
		// -M <lower bound for minimum support>
		// The lower bound for the minimum support. (default = 0.1)
		//
		// -D <delta for minimum support>
		// The delta by which the minimum support is decreased in
		// each iteration. (default = 0.05)
		//
		// -S
		// Find all rules that meet the lower bound on
		// minimum support and the minimum metric constraint.
		// Turning this mode on will disable the iterative support reduction
		// procedure to find the specified number of rules.
		//
		// -transactions <comma separated list of attribute names>
		// Only consider transactions that contain these items (default = no
		// restriction)
		//
		// -rules <comma separated list of attribute names>
		// Only print rules that contain these items. (default = no restriction)
		//
		// -use-or
		// Use OR instead of AND for must contain list(s). Use in conjunction
		// with -transactions and/or -rules

//		String[] options = null;
//		try {
//			options = Utils.splitOptions(" to be defined");
//		} catch (Exception e) {
//			e.printStackTrace();
//			return;
//		}

		// try {
		// associator.setOptions(options);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

}
