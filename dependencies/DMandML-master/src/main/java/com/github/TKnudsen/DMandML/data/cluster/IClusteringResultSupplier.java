package com.github.TKnudsen.DMandML.data.cluster;

import java.util.function.Supplier;

/**
 * <p>
 * Title: IClusteringResultSupplier
 * </p>
 * 
 * <p>
 * Description: supplies a clustering result. the supplier maintains the
 * clustering result, which may update according to the supplier's
 * responsibility.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2017 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.01
 */
public interface IClusteringResultSupplier<CR extends IClusteringResult<?, ?>> extends Supplier<CR> {

}