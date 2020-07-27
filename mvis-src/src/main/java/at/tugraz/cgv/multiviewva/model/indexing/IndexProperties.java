/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.model.indexing;

import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.util.Map;

/**
 * Collects a couple of constants that might be used for configuring an Indexer
 *
 * @author Robert Gregor <r.gregor@cgv.tugraz.at>
 */
public final class IndexProperties {

    /**
     * specifies what index method should be used internally, note that the
     * indexing should generally be geared towards high dimensional data
     * (dim(FT) > 20) additional methods might be supported in more advanced
     * implementations of the Indexer interface, e.g. Locality Sensitive
     * Hashing(LSH) or Approximate Nearest neighbor(ANN) Search, for low
     * dimensional features, kD- or X-Trees might be of use as well *
     */
    public static final String IDX_KEY_INDEXING_METHOD = "I_METHOD";
    public static final String IDX_VAL_INDEXING_EXACT_LINEAR = "EXACT_LINEAR";

    public static final String IDX_VAL_INDEXING_EXACT_PIVOT = "EXACT_PIVOT_TABLE";

    /**
     * the directory path where an index may store information
     */
    public static final String IDX_KEY_CACHE_DIR = "I_CACHE_DIR";
    public static final String IDX_VAL_CACHE_DIR_USER_HOME = System.getProperty("user.home") + File.separator + "Indexer" + File.separator;
    public static final String IDX_VAL_CACHE_DIR_NONE = "NONE";

    public static final String IDX_KEY_PARALLEL = "I_PARALLELISM";
    public static final String IDX_VAL_PARALLEL_AUTO = "AUTO";
    public static final String IDX_VAL_PARALLEL_NONE = "NONE";

    public static final ImmutableMap<String, String> DEFAULT_INDEXING_PROPERTIES = ImmutableMap.<String, String>of(IDX_KEY_INDEXING_METHOD, IDX_VAL_INDEXING_EXACT_LINEAR,
            IDX_KEY_CACHE_DIR, IDX_VAL_CACHE_DIR_USER_HOME, IDX_KEY_PARALLEL, IDX_VAL_PARALLEL_AUTO);

    public static ImmutableMap.Builder<String, String> indexingPropertyBuilderFromDefaults() {
        return ImmutableMap.<String, String>builder().putAll(DEFAULT_INDEXING_PROPERTIES);
    }

    public static ImmutableMap<String, String> indexingPropertiesDefaultWithOverrides(Map<String, String> props) {
        final ImmutableMap.Builder<String, String> b = ImmutableMap.<String, String>builder().putAll(props);

        DEFAULT_INDEXING_PROPERTIES.entrySet().forEach(entry -> {
            if (!props.containsKey(entry.getKey())) {
                b.put(entry.getKey(), entry.getValue());
            }
        });
        return b.build();
    }

    private IndexProperties() {
        throw new UnsupportedOperationException("THIS IS NOT GOING TO HAPPEN");
    }

}
