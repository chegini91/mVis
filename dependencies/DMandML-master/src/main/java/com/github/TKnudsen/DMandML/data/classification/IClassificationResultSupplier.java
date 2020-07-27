package com.github.TKnudsen.DMandML.data.classification;

import java.util.function.Supplier;

/**
 * <p>
 * Title: IClassificationResultSupplier
 * </p>
 * 
 * <p>
 * Description: supplies downstream applications with a probablistic
 * classisfication result. Test FVs (X) already are included in the result.
 * Example downstream applications are active learners, predictors, or visual
 * classificaiton analysis tools.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.03
 */
public interface IClassificationResultSupplier<X> extends Supplier<IClassificationResult<X>> {

}
