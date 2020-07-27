package at.tugraz.cgv.multiviewva.utility.evaluation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.netlib.arpack.Dnaupd;

import java.util.Random;

/**
 * It's a temp class for evaluation paper (FITEE), probably should be deleted afterward
 * and most likely I will forget deleting it
 */

public class LabellingUtility {

    /**
     * keeping label values of records
     */
    public static ObservableList<String> labels = FXCollections.observableArrayList();

    /**
     * randomly create label values for records
     * @param alphabetSize
     * @param modelSize
     */
    public static void initLablesRandomly(int alphabetSize, int modelSize) {
        for (int i = 0; i < modelSize; i++) {
            labels.add(String.valueOf(new Random().nextInt(alphabetSize) + 1));
        }
    }

    /**
     * create random labels based on digits (from 1 to 9)
     * @param modelSize
     */
    public static void initLabelsRandomlyNumbers(int modelSize){
        for (int i = 0; i < modelSize; i++) {
            labels.add(String.valueOf(new Random().nextInt(9) + 1));
        }
    }
}
