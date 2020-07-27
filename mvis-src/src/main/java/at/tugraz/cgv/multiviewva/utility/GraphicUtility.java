/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.utility;

import java.lang.reflect.Field;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author mchegini
 */
public class GraphicUtility {

    //double or single user
    public static boolean doubleUser = false;
    
    //zooming is enabled and other things ar enot, should remove this one
    public static boolean zooming = true;
    
    //if touch is enabled
    public static boolean touch = false;
    
    //if the program is in labeling mode
    public static boolean labeling = true;
    
    //if the program in in the searching mode
    public static boolean searching = false;
    
    //if on hover should be there
    public static boolean onHoverScatterplot = true;

    //if on hover should be there
    public static boolean onHoverPacoord = true;

    //get main stage of the app
    public static Stage stage;
    
    //colors
    public static Color selectionPathColor = Color.CORNFLOWERBLUE;
    public static Color selectedBoxColor = Color.CORNFLOWERBLUE;
    public static Color regressionLineColor = Color.RED;
    public static Color regressionLineOtherColor = Color.BLUE;
    /**
     * effects in scatter plot
     */
    public static boolean spEffects = false;
    public static boolean spDynamicEffects = true;
    public static boolean eyeFish = false;

    /**
     * size
     */
    public static double regressionLineStroke = 3.0d;

    public static double regressionOtherLineStroke = 2.0d;
    
    /** 
     * change delay of tooltip
     * @param tooltip 
     */
    public static void hackTooltipStartTiming(Tooltip tooltip) {
    try {
        Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
        fieldBehavior.setAccessible(true);
        Object objBehavior = fieldBehavior.get(tooltip);

        Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
        fieldTimer.setAccessible(true);
        Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

        objTimer.getKeyFrames().clear();
        objTimer.getKeyFrames().add(new KeyFrame(new Duration(20)));
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
