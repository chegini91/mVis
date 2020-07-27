package com.github.TKnudsen.DMandML.model.supervised.classifier.use;

import java.util.List;
import java.util.function.Function;

import com.github.TKnudsen.DMandML.data.classification.IClassificationResult;

/**
 * <p>
 * Title: IClassificationApplicationFunction
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.03
 */
public interface IClassificationApplicationFunction<X> extends Function<List<? extends X>, IClassificationResult<X>> {

}
