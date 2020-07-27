/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.utility;

import com.theeyetribe.clientsdk.data.GazeData;

/**
 *
 * @author chegini
 */
public class gazeLensUtility {
    
    /**
     * if lens is visible
     */
    public static boolean lensIsVisible = false;
    public static double lensSize = 50;
    public static GazeData previousData = new GazeData();
    public static boolean gazeLabellingDemo = false;
    
}
