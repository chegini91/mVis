/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.utility;

import at.tugraz.cgv.multiviewva.model.enums.clusteringType;
import at.tugraz.cgv.multiviewva.model.enums.dimReductionType;

/**
 *
 * @author chegini
 */
public class MLUtility {
    /**
     * number of cluster, unique
     */
    public static int clusterCount = 0;
    
    public static int activeLearningNumber = 20;
    
    public static dimReductionType dimeRedType = dimReductionType.TSNE;
    
    public static clusteringType clusterType = clusteringType.KMEANS;
}
