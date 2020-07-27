/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.model.indexing;

import com.google.common.base.Converter;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.function.Consumer;


/**
 * Basic Interface for a Content Based Indexing module
 *
 * @author Robert Gregor
 * @param <OT> Type of objects that are to be indexed
 * @param <FET> Type of the Feature Extractor to be used
 */
public interface Index<OT,FET extends FeatureExtractor<OT>> {

   
    /**
     * @param featureExtractor
     * @param objectConverter should provide methods to map an object to an unique string (and vice versa) ... the most simple implementation could be realized as a facade for a BiMap<String,OT> from all objects to their names
     * @param metric
     * @param indexerProps
     * @throws RuntimeException 
     */
    public void init(FET featureExtractor, Converter<OT,String> objectToNameMapper, SimilarityMeasure metric, ImmutableMap<String,String> indexerProps) throws RuntimeException;
    
    /**
     * @return true if a call to init was successful 
     */
    public boolean isInitialized();

    /**
     * Asynchronously adds objects to an index
     * 
     * Note that the two callbacks (Consumers) may very well be invoked in another thread. If you intend to toggle UI events from within the callback, check the docs of your UI toolkit of how to do that from another thread
 Depending on the implementation, the index may throw an IllegalStateException if you try to invoke another operation on the index while the add operation is still running in the background.
 
 It is safe, however, to start another operation once the onSuccessCallback has been invoked
 
 The computed feature vectors can be accessed from within the onSuccessCallback via IndexedObjectContainer::getDescriptor()
     * 
     * 
     * @param objects the objects that should be added to the index
     * @param featureExtractorProps properties that steer the feature extraction, check what properties are supported by the feature extractor that is used by the indexer
     * @param onSuccessCallback invoked by the index when all objects have been added. A list of indexed objects containers will be passed that wraps the input objects 
     * @param onErrorCallback invoked whenever something goes wrong
     * @throws RuntimeException 
     */
    public void add(List<OT> objects, ImmutableMap<String,String> featureExtractorProps, Consumer<List<IndexedObjectContainer<OT>>> onSuccessCallback, Consumer<Throwable> onErrorCallback) throws RuntimeException;
    
    /**
     * @return the feature extractor used by this index
     */
    public FeatureExtractor<OT> getFeatureExtractor();
    
    /**
     * Can be used to get the Similarity Measure
     * @param sm 
     */
    public SimilarityMeasure getSimilarityMeasure();
    
    public ImmutableMap<String,String> getIndexerProperties();
    
    /**
     * Can be used to change the Similarity Measure, depending on the internal implementation, the index may need to rebuild itself after that
     * @param sm 
     * @param onSuccessCallback 
     * @param onErrorCallback 
     */
    public void setSimilarityMeasure(SimilarityMeasure sm,Consumer<Index> onSuccessCallback,Consumer<Throwable> onErrorCallback) throws RuntimeException;
    
    /**
     * 
     * @return the number of objects that have been added to the index
     */
    int getNumberOfIndexedObjects();
    
    
    /**
     * Starts a search for the nearest neighbors of the query on the index. The number of results can be limited by k and r simultaneously.
     * If k is set to Integer.MAX_VALUE and r is set to Double.MAX_VALUE, all objects in the database will be returned, ordered ascending by distance to the query 
     * 
     * The query object itself will not be added to the index, however, it will be returned as first result if it has already been added before
     * 
     * @param query
     * @param k maximum number of results  
     * @param r maximum distance of the results
     * @return
     * @throws RuntimeException
     */

    public IndexSearch<OT> findNeighbors(IndexedObjectContainer<OT> query, int k, double r) throws RuntimeException;
    
    /**
     * Starts a search for the nearest neighbors of the query on the index. The number of results can be limited by k and r simultaneously.
 If k is set to Integer.MAX_VALUE and r is set to Double.MAX_VALUE, all objects in the database will be returned, ordered ascending by distance to the query 
 
 As the queryObject that is passed in does not provide features, features will be extracted on the fly. The features can be accessed via IndexSearch::getQuery().getDescriptor()
 
 The query object itself will not be added to the index, however, it will be returned as first result if it has already been added before
     * 
     * 
     * @param queryObject
     * @param k
     * @param r
     * @return
     * @throws RuntimeException 
     */
    public IndexSearch<OT> findNeighbors(OT queryObject, ImmutableMap<String,String> featureExtractorProps, int k, double r) throws RuntimeException;
    
    
    /**
     * checks whether an object has been added to the index...
     * Note: the Behavior may rely on the provided objectNameMapper (passed to Index#init())
     * 
     * @param object
     * @return true if the passed object was previously added to this index
     * @throws RuntimeException if this method is used before Index::init() was invoked or if the objectNameMapper#convert throws an exception
     */
    public boolean contains(OT object) throws RuntimeException;
    
    /**
     * checks whether an object was previously added to the index that beared the same name as uniqueObjectName.
     * Note: the Behavior may rely on the provided objectNameMapper (passed to Index#init())
     * 
     * @param uniqueObjectName
     * @return true if the passed names mathes the name of an object that was previously added to this index
     * @throws RuntimeException if this method is used before Index::init() was invoked
     */
    public boolean containsName(String uniqueObjectName) throws RuntimeException;
    
    
}
