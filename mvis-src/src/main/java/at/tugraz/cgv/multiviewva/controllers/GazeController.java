package at.tugraz.cgv.multiviewva.controllers;

import at.tugraz.cgv.multiviewva.utility.SearchUtility;
import com.theeyetribe.clientsdk.IGazeListener;
import com.theeyetribe.clientsdk.data.GazeData;

public class GazeController implements IGazeListener {

    private GazeData gazeData;
    private GazeData previousData;
    private int notAGoodIdea = 0;
    private boolean onGazeBrushing = false;
    private boolean onGazeParcoord = false;


    public void onGazeUpdate(GazeData gazeData) {
        notAGoodIdea++;
        if (previousData == null) {
            previousData = gazeData;
        }
        this.gazeData = gazeData;
        SearchUtility.parentController.updateColorGazeCircle(gazeData.isFixated);
        SearchUtility.parentController.updateGazePos(this.gazeData.smoothedCoordinates.x, this.gazeData.smoothedCoordinates.y);

//        (Math.abs(gazeData.smoothedCoordinates.x - previousData.smoothedCoordinates.x) > 40 ||
//                Math.abs(gazeData.smoothedCoordinates.y - previousData.smoothedCoordinates.y) > 40)
        if (gazeData.isFixated && notAGoodIdea % 20 == 0) {
            if (onGazeBrushing) {
                SearchUtility.parentController.getSpMainController().updateLabelledListGaze();
            }
            this.previousData = gazeData;
        }
        //do stuff when pacoord guidance is on
        if (onGazeParcoord && gazeData.isFixated) {
            SearchUtility.parentController.getParcoordChart().onGazeFixationHandle();
        }
        //        SearchUtility.parentController.getSpMainController().selectBox.setX(this.gazeData.smoothedCoordinates.x - 100);
        //        SearchUtility.parentController.getSpMainController().selectBox.setY(this.gazeData.smoothedCoordinates.y + 100);
    }

    public com.theeyetribe.clientsdk.data.Point2D getCoordinates() {
        return gazeData.smoothedCoordinates;
    }

    public GazeData getData() {
        return gazeData;
    }

    public void setOnGazeBrushing(boolean onGazeBrushing) {
        this.onGazeBrushing = onGazeBrushing;
    }

    public void setOnGazeParcoord(boolean onGazeParcoord) {
        this.onGazeParcoord = onGazeParcoord;
    }

    public boolean isOnGazeBrushing() {
        return onGazeBrushing;
    }

    public boolean isOnGazeParcoord() {
        return onGazeParcoord;
    }
}
