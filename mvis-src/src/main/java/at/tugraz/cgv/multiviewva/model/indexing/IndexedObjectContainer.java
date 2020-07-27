/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.model.indexing;

import org.ejml.data.DMatrixRMaj;

/**
* @author Robert Gregor
 * @param <OT> The ObjectType
 */
public interface IndexedObjectContainer<OT extends Object> {
    
    /**
     * A method to access the raw descriptor.
     * There must be no attempt to modify the returned List, otherwise the implementation may throw a RuntimeException
     * 
     * DANGER: please do not attempt to modify the descriptor! (create a copy if you intend to do so, unless you know exactly what you are doing (including background threads that work that compute searches etc.)
     * @return a List of Doubles representing the raw descriptor values as computed by the Index.
     * 
     */
    DMatrixRMaj getDescriptor();
    
    /**
     * @return the feature extractor instance which has been used to compute the descriptor for this object
     */
    FeatureExtractor<OT> getFeatureExtractor();
            
    /**
     * This method may be used to access the index to which this container has been added to
     * @return the index to which the IndexedObjectContainer has been added to, if this IndexedObjectContainer has not been added to an Index, the returned value will be null
     */
    Index<OT,FeatureExtractor<OT>> getIndex();
    
    /**
     * Accessor to the underlying object (which is to be indexed)
     * 
     * ATTENTION: changing the object after creation of the container will not modify the descriptor... you should create a new instance instead
     * @return the object, must never be null
     */
    OT get();
    
    /**
     * Implementing this method is important for unique identification of an object across program sessions. An index may use this value to uniquely identify the object (this includes across program session if the index is persisted)
     * 
     * @return a unique name for the object.  
     */
    String getIndexedName();
    
    
    
}

