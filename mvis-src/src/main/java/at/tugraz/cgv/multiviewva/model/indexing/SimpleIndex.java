/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.model.indexing;

import com.google.common.base.Charsets;
import com.google.common.base.Converter;
import com.google.common.base.Strings;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import com.google.common.util.concurrent.AtomicDouble;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.SoftReference;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import static java.util.Objects.requireNonNull;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.BaseStream;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import at.tugraz.cgv.multiviewva.model.indexing.IndexSearch.Result;
import at.tugraz.cgv.multiviewva.model.indexing.metrics.L1;
import org.ejml.data.DMatrixRMaj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import at.tugraz.cgv.multiviewva.utility.ExecutorServices;
import at.tugraz.cgv.multiviewva.utility.HashingUtil;

/**
 * Abstract base implementations to be used by implementations of the Index
 * Interface
 *
 * @author Robert Gregor
 * @param <OT> The object type to be indexed
 * @param <FET> The feature extractor type
 */
public class SimpleIndex<OT, FET extends FeatureExtractor<OT>> implements Index<OT, FET> {

    protected final class SimpleContainer<OT2> implements IndexedObjectContainer<OT2> {

        protected SoftReference<OT2> obj;
        protected final String name;
        protected final DMatrixRMaj desc;

        @SuppressWarnings("unchecked")
        protected SimpleContainer(OT2 object, DMatrixRMaj descriptor) {
            assert object != null && descriptor != null;
            obj = new SoftReference<>(object);
            desc = descriptor;
            name = SimpleIndex.this.objectNameMapper.convert((OT) object);
            assert !Strings.isNullOrEmpty(name);
        }

        protected SimpleContainer(String name, DMatrixRMaj descriptor) {
            assert !Strings.isNullOrEmpty(name) && descriptor != null;
            this.name = name;
            desc = descriptor;
            obj = new SoftReference<>(null);
            assert !Strings.isNullOrEmpty(name);
        }

        @Override
        public DMatrixRMaj getDescriptor() {
            return desc;
        }

        @SuppressWarnings("unchecked")
        @Override
        public FeatureExtractor<OT2> getFeatureExtractor() {
            return (FeatureExtractor<OT2>) SimpleIndex.this.extractor;
        }

        @SuppressWarnings("unchecked")
        @Override
        public final Index<OT2, FeatureExtractor<OT2>> getIndex() {
            return (Index<OT2, FeatureExtractor<OT2>>) SimpleIndex.this;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof IndexedObjectContainer)) {
                return false;
            }
            final IndexedObjectContainer o = (IndexedObjectContainer) other;
            return name.equals(o.getIndexedName())
                    && 0 == L1.dist(desc, o.getDescriptor());
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 83 * hash + Objects.hashCode(this.name);
            hash = 83 * hash + Objects.hashCode(this.desc);
            return hash;
        }

        @SuppressWarnings("unchecked")
        @Override
        public final OT2 get() {
            OT2 o = obj.get();
            if (o == null) {
                o = (OT2) SimpleIndex.this.objectNameMapper.reverse().convert(name);
            }
            obj = new SoftReference<>(o);
            return o;
        }

        @Override
        public String getIndexedName() {
            return name;
        }
    }

    protected class SimpleSearch<OT2> implements IndexSearch<OT2> {

        protected final int k;
        protected final double r;
        protected final ListenableFuture<IndexedObjectContainer<OT2>> queryContainerFuture;
        protected final ListenableFuture<List<Result<OT2>>> future;

        public SimpleSearch(ListenableFuture<IndexedObjectContainer<OT2>> qf, int k, double r, ListenableFuture<List<Result<OT2>>> f) {
            assert qf != null && f != null;
            this.queryContainerFuture = qf;
            this.k = k;
            this.r = r;
            this.future = f;
        }

        @Override
        public IndexedObjectContainer<OT2> getQuery() {
            try {
                return queryContainerFuture.get();
            } catch (Throwable t) {
                throw new RuntimeException("Could not extract descriptor from query object", t);
            }
        }

        @Override
        public double getRadius() {
            return r;
        }

        @Override
        public int getK() {
            return k;
        }

        /**
         * To obtain the results of the search (or the errors that occurred) add
         * you listeners here, they are guaranteed to be invoked exactly once no
         * matter whether the current state of the search (i.e. if you add
 another listener after the search is finished already, they will be
 invoked again).

 Think about threading when using this function... e.g. if you are
 interfacing with UI widgets in the aftermath of the IndexSearch you
 might need to synchronize what happens in the callbacks and your main
 UI thread. If unsure, try setting synchronous to true... however, if
 done so from the UI thread, your UI may freeze until results are
 available. However if the results are ready (and synchronous == true)
 , the callbacks will be invoked immediately within the same thread
         *
         *
         * @param onSuccessCallback to be called when the search was successful
         * @param onErrorCallback to be called when there is an error
         * @param synchronous if true, this method will block until a result is
         * available, the callbacks will be invoked directly in the caller's
         * thread, if false, the callbacks will be invoked asynchronously from a
         * background thread
         */
        @Override
        public void addCallbacks(Consumer<List<Result<OT2>>> onSuccessCallback, Consumer<Throwable> onErrorCallback, boolean synchronous) {
            final Runnable forwardResults = () -> {
                try {
                    List<Result<OT2>> result = future.get();//thiw will block the invoking thread until future.isDOne() == true
                    onSuccessCallback.accept(result);
                } catch (Throwable t) {
                    onErrorCallback.accept(t);
                }
            };

            if (synchronous) {
                forwardResults.run();
            } else {
                future.addListener(forwardResults, ExecutorServices.LISTEN_CONTROL_EXEC_SVC);//this will submit the runnable to the provided executor, exactly once as soon as future.isDone() == true, fires immeadeatly if this is already true at the time of adding the listener
            }
        }

        @Override
        public void cancel() {
            future.cancel(true);
        }

        @Override
        public boolean isDone() {
            return future.isDone();
        }

    }

    protected class SimpleResult<OT2> implements IndexSearch.Result<OT2> {

        final int descIdx;
        final double dist;
        SoftReference<IndexedObjectContainer<OT2>> containerReference;

        protected SimpleResult(int descriptorIndex, double distance) {
            assert descriptorIndex >= 0 && descriptors.size() > descriptorIndex;
            assert distance >= 0.0;
            descIdx = descriptorIndex;
            dist = distance;
            containerReference = new SoftReference<>(null);
        }

        @Override
        public double getDistance() {
            return dist;
        }

        @Override
        public IndexedObjectContainer<OT2> get() {
            IndexedObjectContainer<OT2> c = containerReference.get();
            if (c == null) {
                c = new SimpleContainer<>(indicesByName.inverse().get(descIdx), descriptors.get(descIdx));
                containerReference = new SoftReference<>(c);
            }
            return c;
        }

    }

    protected static int DISK_CACHE_VERSION = 0; //increment on change of disk cache format 

    private static final Logger log = LoggerFactory.getLogger(SimpleIndex.class);

    protected final HashBiMap<String, Integer> indicesByName;
    protected final ArrayList<DMatrixRMaj> descriptors;

    protected FET extractor;
    protected Converter<OT, String> objectNameMapper;
    protected SimilarityMeasure metric;
    protected ImmutableMap<String, String> properties;

    private Path cacheFilePath = null;

    protected final AtomicInteger currentOpCount = new AtomicInteger(Integer.MIN_VALUE);

    public SimpleIndex() {
        this(4096);
    }
    
    public SimpleIndex(int expectedCapacity) {
        if(expectedCapacity < 0) {
            throw new IllegalArgumentException("expectedCapacity should at least be zero");
        }
        indicesByName = HashBiMap.create(expectedCapacity);//ConcurrentHashMap<>(0,0.75f,Runtime.getRuntime().availableProcessors());
        descriptors = new ArrayList<>(expectedCapacity);
    }

    /**
     * creates and initializes a new Simple Index in one step, this method is
     * just convenience over calling the no args ctor and the init method
     * separately
     *
     * @param featureExtractor
     * @param objectToNameConverter see SimpleIndex#init()
     * @param similarityMeasure
     * @param indexerProps
     */
    @SuppressWarnings("OverridableMethodCallDuringObjectConstruction")
    public SimpleIndex(FET featureExtractor, Converter<OT, String> objectToNameConverter, SimilarityMeasure similarityMeasure, ImmutableMap<String, String> indexerProps) {
        this();
        init(featureExtractor, objectToNameConverter, similarityMeasure, indexerProps);
    }

    @Override
    public ImmutableMap<String, String> getIndexerProperties() {
        return properties;
    }

    @Override
    public SimilarityMeasure getSimilarityMeasure() {
        return metric;
    }

    @Override
    public FET getFeatureExtractor() {
        return (FET) extractor;
    }

    /**
     * causes the calling thread to sleep until all index operation (running in
     * the background) are finished
     *
     * @param sleepIntervalMillis the amount of ms to sleep between polling the
     * current number of operations
     * @param timeoutMillis the maximum amount to wait, if the amount is
     * exceeded
     */
    public void waitForOpFinish(int sleepIntervalMillis, long timeoutMillis) {
        assert sleepIntervalMillis > 0 && timeoutMillis > 0;
        if (currentOpCount.get() == Integer.MIN_VALUE) {
            throw new IllegalStateException("Can not wait before call to init");
        }
        timeoutMillis += System.currentTimeMillis();
        while (currentOpCount.get() != 0) {
            try {
                Thread.sleep(sleepIntervalMillis);
            } catch (Throwable t) {
                log.debug("interrupted while waiting", t);/*NOP*/
            }

//            if (log.isTraceEnabled()) {
//                log.trace("waiting, currentOpsRunning=" + currentOperationsRunning());
//            }

            if (System.currentTimeMillis() > timeoutMillis) {
                throw new RuntimeException("Timeout exceeded while waiting for index ops to finish");
            }
        }
        log.debug("finished waiting, no ops remaining");
    }

    @Override
    public void setSimilarityMeasure(SimilarityMeasure sm, Consumer<Index> onSuccessCallback, Consumer<Throwable> onErrorCallback) {
        synchronized (currentOpCount) {
            requireInitialized();
            if (currentOpCount.get() != 0) {
                throw new IllegalStateException("can only change metric when no other searches are in progress for now!");
            }

            //if distances are cached or if something different than a linear search is performed... some more things will need to be done in here
            //it is highly likely, that this would also involve additional locking
            this.metric = sm;
        } //synchronized
        onSuccessCallback.accept(this);
    }

    /**
     * @return the number of index operations currently running in the
     * background, note this only if the index is used from multiple client
     * threads, the returned value might be out of date again immediately after
     * this method has returned.
     *
     * this will return Integer.MIN_VALUE if uninitialized
     */
    public int currentOperationsRunning() {
        return Math.abs(currentOpCount.get());
    }

    /**
     * returns true if this SimpleIndex has been initialized successfully
     *
     * @return
     */
    @Override
    public boolean isInitialized() {
        return currentOpCount.get() >= 0;
    }

    protected void requireInitialized() throws RuntimeException {
        if (!isInitialized()) {
            throw new IllegalStateException("The Index must be initialized and all other operations are required to have finished before it can be used, check: " + SimpleIndex.class.getSimpleName() + "#isReady()");
        }
    }

    @Override
    public void init(FET featureExtractor, Converter<OT, String> objectToNameConverter, SimilarityMeasure similarityMeasure, ImmutableMap<String, String> indexerProps) throws RuntimeException {
        synchronized (currentOpCount) {

            final int previous = currentOpCount.getAndSet(-1);
            if (previous != Integer.MIN_VALUE) {
                throw new IllegalStateException("The " + SimpleIndex.class.getSimpleName() + " is designed to be initialized exactly once, if you need to change the config, create a new instance, you may change the metric via setMetric() though");
            }

            extractor = requireNonNull(featureExtractor);
            objectNameMapper = requireNonNull(objectToNameConverter);
            metric = requireNonNull(similarityMeasure);
            properties = requireNonNull(indexerProps);

            ExecutorServices.CONTROL_EXEC_SVC.execute(() -> {
                synchronized (currentOpCount) {
                    final String cacheDirName = properties.get(IndexProperties.IDX_KEY_CACHE_DIR);
                    if (cacheDirName == null) {
                        cacheFilePath = null;
                    }

                    cacheFilePath = Paths.get(
                            cacheDirName,
                            this.getClass().getName() + "_" + HashingUtil.hasherFromExcept(properties,
                            IndexProperties.IDX_KEY_CACHE_DIR,
                            IndexProperties.IDX_KEY_PARALLEL)
                            .putString(getClass().toGenericString(), Charsets.UTF_8).hash() + ".cache");
                    attemptReadFromDisk();
                    if (!currentOpCount.compareAndSet(-1, 0)) {
                        throw new IllegalStateException("Index in unexpected state, must be a bug in the implementation");
                    }
                    log.debug("init finished");
                }
            });

        }
    }

    @Override
    public int getNumberOfIndexedObjects() {
        return descriptors.size();
    }

    protected Path getCacheFilePath() {
        if (cacheFilePath != null) {
            return cacheFilePath;
        }

        return cacheFilePath;
    }

    @SuppressWarnings("unchecked")
    protected void attemptReadFromDisk() {
        if (cacheFilePath == null) {
            log.info("Index disk caching disabled");
            return;
        }
        try (final ObjectInputStream ois = new ObjectInputStream(new FileInputStream(cacheFilePath.toFile()))) {
            assert ois != null;

            log.debug("Looking for cache file to read: " + cacheFilePath);

            //Simple Index version
            if (ois.readInt() != SimpleIndex.DISK_CACHE_VERSION) {
                log.warn("disk cache does not match expected Version, should be " + SimpleIndex.DISK_CACHE_VERSION);
                return;
            }

            //Feature Extractor & Naming Class Fingerprint
            if (!ois.readUTF().equals(HashingUtil.hash(extractor.getClass().getName(), extractor.getClass().toGenericString(), objectNameMapper.getClass().getName(), objectNameMapper.getClass().toGenericString()).toString())) {
                log.warn("the feature extractor or the naming strategy that were used for the previous disk cache differs from current setup, the previous disk cache is discarded and the corresponding file will be overridden");
                return;
            }

            //Properties hash
            if (!ois.readUTF().equals(HashingUtil.hashExcept(properties, IndexProperties.IDX_KEY_CACHE_DIR, IndexProperties.IDX_KEY_PARALLEL).toString())) {
                log.warn("properties of previous disk cache differ from current setup, file will be overridden");
                return;
            }

            //indices by name
            assert indicesByName != null && indicesByName.isEmpty();
            indicesByName.putAll((Map<String, Integer>) ois.readObject());
            if (indicesByName.isEmpty()) {
                log.warn("empty cache file found");
                return;
            }

            //actual descriptor list
            assert descriptors != null && descriptors.isEmpty();
            descriptors.addAll((List<DMatrixRMaj>) ois.readObject());
            if (descriptors.size() != indicesByName.size()) {
                descriptors.clear();
                indicesByName.clear();
                log.error("Number of names in cache file do not match the number of descriptors! Previous cache from disc is discarded!");
            }

            log.info("Read cache file from " + getCacheFilePath() + " with " + descriptors.size() + " entries");

        } catch (FileNotFoundException e) {
            log.info("cache file " + getCacheFilePath() + " not found, it will be created once objects are added to the index");
        } catch (Throwable e) {
            log.warn("disk cache inaccessible or malformed, content is ignored... there will be an attempt to (re-)create and overwrite the file after objects have been added to this index", e);
        }

    }

    protected void attemptWriteToDisk() {
        if (cacheFilePath == null) {
            log.info("Index disk caching disabled");
            return;
        }

        File file = cacheFilePath.toFile();
        if (!file.exists()) {
            try {
                Files.createParentDirs(file);
                Files.touch(file);
            } catch (Throwable e) {
                log.error("Error accessing the cache file, check whether you have the correct permissions to access the file at: " + cacheFilePath);
                return;
            }
        }

        try (final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file, false))) {
            assert oos != null;

            if (descriptors.size() != indicesByName.size()) {
                throw new IllegalStateException("number of names in cache file do not math the number of descriptors!");
            }

            //DISK CACHE VERSION
            oos.writeInt(DISK_CACHE_VERSION);
            //EXTRACTOR AND NAMING FINGERPRINT
            oos.writeUTF(HashingUtil.hash(extractor.getClass().getName(), extractor.getClass().toGenericString(), objectNameMapper.getClass().getName(), objectNameMapper.getClass().toGenericString()).toString());
            //INDEXER PROPERTIES HASH
            oos.writeUTF(HashingUtil.hashExcept(properties, IndexProperties.IDX_KEY_CACHE_DIR, IndexProperties.IDX_KEY_PARALLEL).toString());

            //NAMING MAP
            oos.writeObject(indicesByName);
            //DESCRIPTORS
            oos.writeObject(descriptors);

        } catch (Throwable e) {
            log.error("Could not write Index cache to disk: ", e);
         }

        log.info("wrote disk cache to " + file);

    }

    @Override
    public boolean contains(OT object) throws RuntimeException {
        requireInitialized();
        return indicesByName.containsKey(objectNameMapper.convert(object));
    }

    @Override
    public boolean containsName(String uniqueObjectName) throws RuntimeException {
        requireInitialized();
        return indicesByName.containsKey(uniqueObjectName);
    }

    protected final <E, S extends BaseStream<E, S>> S parallelIfEnabled(S stream) {
        switch (properties.get(IndexProperties.IDX_KEY_PARALLEL)) {
            case IndexProperties.IDX_VAL_PARALLEL_AUTO:
                return stream.parallel();
            case IndexProperties.IDX_VAL_PARALLEL_NONE:
                return stream.sequential();
            default:
                throw new IllegalStateException("Indexing Property for parallelism mode not set, try to add the following to the indexing properties passed to init() if unsure: props.put(IndexingProperties.IDX_KEY_PARALLEL,IndexingProperties.IDX_VAL_PARALLEL_AUTO");
        }
    }

    @Override
    public void add(
            final List<OT> objects,
            final ImmutableMap<String, String> featureExtractorProps,
            final Consumer<List<IndexedObjectContainer<OT>>> onSuccessCallback,
            final Consumer<Throwable> onErrorCallback) {

        synchronized (currentOpCount) {
            requireInitialized();
            final int previous = currentOpCount.getAndIncrement();
            assert previous >= 0;
            if (previous > 0) {
                log.warn("There are concurrent index operations running in the background, if those are findNeighbor operations, their results may vary depending on the progress of this parallel add operation!");
            }
            ExecutorServices.CONTROL_EXEC_SVC.submit(() -> {
                try {

                    //extraction might be done in parallel
                    final List<IndexedObjectContainer<OT>> containers = parallelIfEnabled(objects.stream())
                            .filter(obj -> !this.contains(obj)) //don't add if something with the same name is already in the index
                            .map(obj -> new SimpleContainer<>(obj, SimpleIndex.this.extractor.apply(obj, featureExtractorProps)))
                            .collect(Collectors.toList());

                    //the ops below must not be executed in parallel!
                    synchronized (currentOpCount) {
                        final int prev1 = currentOpCount.get();
                        assert prev1 > 0; //should at least be one due to the add operation itself
                        int baseOffset = descriptors.size();
                        descriptors.ensureCapacity(baseOffset + containers.size());
                        for (int i = 0; i < containers.size(); ++i) {
                            final IndexedObjectContainer<OT> c = containers.get(i);
                            descriptors.add(c.getDescriptor());
                            indicesByName.put(c.getIndexedName(), baseOffset + i);
                        }
                        attemptWriteToDisk();
                        final int prev2 = currentOpCount.getAndDecrement();
                        assert prev2 > 0 && prev1 == prev2; // see above (double assert placed here to check for wrong use of synchronized block here or in concurrent operations
                    }
                    onSuccessCallback.accept(containers);//placed outside of sync to allow for addition of new ops within callback

                } catch (Throwable t) {
                    final int prev2 = currentOpCount.getAndDecrement();
                    assert prev2 > 0; // see above
                    log.error("error adding features to index: ", t);
                    onErrorCallback.accept(t);
                }
            });
        }//synchronized
    }

    protected final List<Result<OT>> findLinear(final IndexedObjectContainer<OT> c, final int k, final double r) {
        assert isInitialized() && currentOpCount.get() >= 0; //the caller of this method is responsible for handling proper state checking/setting and synchronization
        assert k > 0;
        assert r >= 0.0;

        if (descriptors.isEmpty()) // no descriptors -> no work, no need to pollute the executor service, don't throw this out... or have fun with divisions by zero ;-)
        {
            return Collections.EMPTY_LIST;
        }

        //we findLinear splits of the total number of descriptors w.r.t. to the number of processors available
        final int taskSize = descriptors.size() / ExecutorServices.systemConcurrencyLevel() + 1; //integer division is intended here
        final int numTasks = (int) Math.ceil((double) descriptors.size() / (double) taskSize);
        final int maxResultsNum = Math.min(k, taskSize);
        final AtomicDouble maxDistShared = new AtomicDouble(r);

        //the actual tasks are invoked via mapToObj on the int stream
        final List<ArrayDeque<SimpleResult<OT>>> resultCandidates = parallelIfEnabled(IntStream.range(0, numTasks))
                .mapToObj(taskNumber -> {
                    final int startIdx = taskNumber * taskSize;
                    final int endIdx = Math.min(startIdx + taskSize, descriptors.size()) - 1;

                    final PriorityQueue<SimpleResult<OT>> taskHits = new PriorityQueue<>(Math.min(maxResultsNum, endIdx - startIdx + 1),
                            (r1, r2) -> Double.compare(r2.dist, r1.dist)); //should place the result with largest distance at the head

                    double maxDist = maxDistShared.get(); //this is intentionally not placed in the loop below 

                    for (int i = startIdx; i <= endIdx; ++i) {
                        final double curDist = metric.apply(c.getDescriptor(), descriptors.get(i));
                        if (curDist > maxDist) {
                            continue;
                        }
                        final SimpleResult<OT> curResultCandidate = new SimpleResult<>(i, curDist);
                        if (taskHits.size() == maxResultsNum && taskHits.peek().dist >= curResultCandidate.dist) {
                            taskHits.poll();
                            taskHits.add(curResultCandidate);
                            //for lower k and/or large r, this should provide a substantial speedup (if the metric isn't very expensive)
                            maxDist = taskHits.peek().dist;
                            final double globMaxDist = maxDistShared.get();
                            if (globMaxDist > maxDist) {
                                maxDistShared.lazySet(maxDist);
                                continue;
                            } else { // globMaxDist <= maxDist
                                maxDist = globMaxDist;
                            }
                            assert maxDist <= r;
                        } else {
                            taskHits.add(curResultCandidate);
                        }
                    }
                    maxDist = maxDistShared.get();
                    while (!taskHits.isEmpty() && taskHits.peek().dist > maxDist) {
                        taskHits.poll();
                    }
                    if (log.isDebugEnabled()) {
                        log.debug("found " + taskHits.size() + " result candidates in subtask no: " + taskNumber + "descriptor index range= " + startIdx + ".." + endIdx);
                    }
                    ArrayDeque<SimpleResult<OT>> reversed = new ArrayDeque<>(taskHits.size());
                    while (!taskHits.isEmpty()) {
                        reversed.offer(taskHits.poll());//sorts all results for the subtask descending by distance 
                    }
                    return reversed;
                })
                .collect(Collectors.toCollection(LinkedList::new));

        //we now merge the (pre-sorted) deques provided by the individual subtasks within a single thread to the final result list
        int numResults = 0;
        List<Result<OT>> results = new ArrayList<>(maxResultsNum);
        ArrayDeque<SimpleResult<OT>> selectedDeque = null;
        do {
            //find the queue containing the result with the smalles distance, store its reference in selectedDeque
            double curMinDist = Double.MAX_VALUE;
            final Iterator<ArrayDeque<SimpleResult<OT>>> it = resultCandidates.iterator();
            while (it.hasNext()) {
                final ArrayDeque<SimpleResult<OT>> curDeque = it.next();

                final SimpleResult<OT> curResult = curDeque.peekLast();
                //if a deque is empty, we remove it from the resultCandidatesList entirely
                if (curResult == null) {
                    it.remove();
                    continue; //continue with next deque returned by iterator
                }
                //if the current deque provides a result better than the current best result
                if (curResult.dist < curMinDist) {
                    //...we store its reference
                    selectedDeque = curDeque;
                    //...and remember it as best result
                    curMinDist = curResult.dist;
                }
            }
            //the last run found no suitable result, we are finished.
            if (curMinDist == Double.MAX_VALUE) {
                break;
            }
            //otherwise now try to add the deque item which is known to provide the smalles distance to the final result list
            assert selectedDeque != null && !selectedDeque.isEmpty() && selectedDeque.peekLast().dist == curMinDist;
            results.add(selectedDeque.pollLast());
        } while (results.size() < k); // we only do this again if results.size() hasn't reached its maximum possible value

        if (log.isInfoEnabled()) {
            log.info("found " + results.size() + " total results");
        }

        return results;
    }

    @Override
    public IndexSearch<OT> findNeighbors(final IndexedObjectContainer<OT> query, final int k, final double r) throws RuntimeException {
        synchronized (currentOpCount) {
            requireInitialized();
            final int previous = currentOpCount.getAndIncrement();
            assert previous >= 0;
            if (previous > 0) {
                log.warn("There are concurrent index operations running in the background, your results may vary if one of these is an add operation!");
            }

            return new SimpleSearch<>(Futures.immediateFuture(query), k, r, ExecutorServices.LISTEN_CONTROL_EXEC_SVC.submit(() -> {
                try {
                    return findLinear(query, k, r);
                } finally {
                    currentOpCount.getAndDecrement();
                }
            }));
        }
    }

    @Override
    public IndexSearch<OT> findNeighbors(final OT queryObject, final ImmutableMap<String, String> featureExtractorProps,
            final int k, final double r) throws RuntimeException {

        synchronized (currentOpCount) {
            requireInitialized();
            final int previous = currentOpCount.getAndIncrement();
            assert previous >= 0;
            if (previous > 0) {
                log.warn("There are concurrent index operations running in the background, your results may vary if one of these is an add operation!");
            }

            ListenableFuture<IndexedObjectContainer<OT>> qcFuture = ExecutorServices.LISTEN_COMPUTE_EXEC_SVC.submit(() -> {
                return new SimpleContainer<>(queryObject, extractor.apply(queryObject, featureExtractorProps));
            });

            return new SimpleSearch<>(qcFuture,
                    k,
                    r,
                    ExecutorServices.LISTEN_CONTROL_EXEC_SVC.submit(() -> {
                        try {
                            return findLinear(qcFuture.get(), k, r);
                        } finally {
                            final int prev2 = currentOpCount.getAndDecrement();
                            assert prev2 > 0;
                        }
                    }));
        }
    }

}
