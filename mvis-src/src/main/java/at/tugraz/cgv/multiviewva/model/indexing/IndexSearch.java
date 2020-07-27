/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.model.indexing;

import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author Robert Gregor
 */
public interface IndexSearch<OT> {

    public static interface Result<OT> {

        /**
         * @return an IndexedObjectContainer that contains the result Object
         */
        IndexedObjectContainer<OT> get();
        
        /**
         * @return the distance of this result to the query
         */
        double getDistance();
    }

    /**
     * Returns the query object and it's descriptor
     * @return 
     */
    IndexedObjectContainer<OT> getQuery();
    

    /**
     * @return the radius that was passed to Index::findNeighbors() or null if the Search was started by Index::findKNN() 
     */
    double getRadius();

    /**
     * @return the k that was passed to Index::findNeighbors() or null if the Search was started by Index::findInRadius() 
     */
    int getK();
    
    void addCallbacks(Consumer<List<Result<OT>>> onSuccessCallback, Consumer<Throwable> onErrorCallback, boolean waitForResults);
    
    void cancel();
    
    boolean isDone();
}
