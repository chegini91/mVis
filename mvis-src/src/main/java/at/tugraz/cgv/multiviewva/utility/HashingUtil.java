/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.cgv.multiviewva.utility;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Robert Gregor <r.gregor@cgv.tugraz.at>
 */
public final class HashingUtil {

    private static final HashFunction hashing = Hashing.murmur3_128();

    public static final Hasher newHasher() {
        return hashing.newHasher();
    }

    public static HashCode hash(String... strs) {
        return newHasher().putString(
                String.join("\n", Arrays.asList(strs)), Charsets.UTF_8
        ).hash();
    }
    
    public static HashCode hash(Map<String, String> props) {
        return hashExcept(props);
    }

    public static HashCode hashExcept(Map<String, String> props, String... excludedKeys) {
        return hasherFromExcept(props, excludedKeys).hash();
    }

    public static Hasher hasherFromExcept(Map<String, String> props, String... excludedKeys) {
        return newHasher().putString(
                props.entrySet().stream()
                        .filter(entry -> !Arrays.asList(excludedKeys).contains(entry.getKey()))
                        .map(entry -> entry.getKey() + '=' + entry.getValue())
                        .sorted()
                        .collect(Collectors.joining("\n")),
                Charsets.UTF_8);
    }

    private HashingUtil() {
        throw new UnsupportedOperationException("this is not going to happen");
    }
}
