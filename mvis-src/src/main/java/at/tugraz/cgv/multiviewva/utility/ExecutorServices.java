/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.utility;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class with only static members intended to provide a global instance of an
 * JavaSE ExecutorService. Intended to be used to perform any asynchronous
 * background computation tasks if multiple tasks are passed to the parExecutor,
 * they will be executed in parallel on multiple threads see
 * model.indexing.ScatterPlotIndex to get an idea how to use this pool from your
 * component as well.
 *
 * In the web, there are some nice examples of how to use ThreadPools /
 * ExecutorServices as well
 *
 * @author Robert Gregor
 */
public final class ExecutorServices {

    public static final Logger LOG = LoggerFactory.getLogger(ExecutorServices.class);
    /**
     * an executor service that is backed by the systems global Work Stealing
     * Pool (e.g. also used by parallel stream processing methods of the JVM)
     * it's maximum number of threads is limited to the number of processors on
     * your system. Use this for CPU intensive tasks that should be executed in
     * parallel for higher throughput or if you implement your workload as
     * ForkJoinTasks
     *
     * special note: avoid ForkJoin if unsure whether your problem should be
     * used by a ForkJoinTask-based approach
     *
     * DO NOT use this for tasks that wait or block for a large part of their
     * lifecycle unless you implement them via the ManagedBlocker interface.
     */
    public static final ForkJoinPool COMPUTE_EXEC_SVC;
    /**
     * an executor service that is backed by an unbound, cached thread pool. Use
     * this pool for tasks that do need to block or wait for IO and/or other
     * threads for a large part of their lifecycle
     */
    public static final ExecutorService CONTROL_EXEC_SVC;
    /**
     * an executor service that directly executes it's task in the calling
     * thread. Use this one for an API compatible drop-in replacement of the
     * parallel pools if you want e.g. to dynamically disable parallel execution
     * without changing the remainder of your implementation.
     */
    public static final ExecutorService DIRECT_EXEC_SVC = MoreExecutors.newDirectExecutorService();

    /**
     * a decorator on top of COMPUTE_EXEC_SVC that produces Guava's
     * ListenableFuture instead of the standard Java SDK Future
     */
    public static final ListeningExecutorService LISTEN_COMPUTE_EXEC_SVC;
    /**
     * a decorator on top of CONTROL_EXEC_SVC that produces Guava's
     * ListenableFuture instead of the standard Java SDK Future
     */
    public static final ListeningExecutorService LISTEN_CONTROL_EXEC_SVC;
    /**
     * a decorator on top of DIRECT_EXEC_SVC that produces Guava's
     * ListenableFuture instead of the standard Java SDK Future
     */
    public static final ListeningExecutorService LISTEN_DIRECT_EXEC_SVC = MoreExecutors.listeningDecorator(DIRECT_EXEC_SVC);

    static {
        //this property needs to be set before the first invocation of ForkJoinPool.commonPool()
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", Integer.toString(systemConcurrencyLevel()));
        COMPUTE_EXEC_SVC = ForkJoinPool.commonPool();
        LISTEN_COMPUTE_EXEC_SVC = MoreExecutors.listeningDecorator(COMPUTE_EXEC_SVC);

        ThreadPoolExecutor tpe = new ThreadPoolExecutor(1, systemConcurrencyLevel() * 4, 10000, TimeUnit.MILLISECONDS, new SynchronousQueue<>(), new ThreadPoolExecutor.AbortPolicy()) {
            @Override
            protected void beforeExecute(Thread t, Runnable r) {
                if (LOG.isDebugEnabled()) {
//                    LOG.debug("Starting new Job in CONTROL_EXEC_SVC: " + r);
                }
                super.beforeExecute(t, r);
            }

            @Override
            protected void afterExecute(Runnable r,
                    Throwable t) {
                if (LOG.isDebugEnabled()) {
//                    LOG.debug("Finsihed Job in CONTROL_EXEC_SVC: " + r);

                }
                if (t != null) {
//                    LOG.error(r + "execution failed: ", t);
                }
                super.afterExecute(r, t);
            }
        };
        tpe.allowCoreThreadTimeOut(false);
        tpe.prestartCoreThread();
        CONTROL_EXEC_SVC = MoreExecutors.getExitingExecutorService(tpe, 10000L, TimeUnit.MILLISECONDS);
        LISTEN_CONTROL_EXEC_SVC = MoreExecutors.listeningDecorator(CONTROL_EXEC_SVC);
    }

    public static int systemConcurrencyLevel() {
        return Runtime.getRuntime().availableProcessors();
    }

    private ExecutorServices() {
        throw new UnsupportedOperationException("this is not going to happen");
    }

}
