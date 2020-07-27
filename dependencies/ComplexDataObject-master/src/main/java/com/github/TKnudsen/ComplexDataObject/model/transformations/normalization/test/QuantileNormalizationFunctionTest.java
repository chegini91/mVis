package com.github.TKnudsen.ComplexDataObject.model.transformations.normalization.test;

import java.util.ArrayList;
import java.util.Collection;

import com.github.TKnudsen.ComplexDataObject.model.transformations.normalization.QuantileNormalizationFunction;

public class QuantileNormalizationFunctionTest {

	public static void main(String[] args) {

		Collection<Number> values = new ArrayList<>();

		values.add(1.0);
		values.add(1.0);
		values.add(1.1);
		values.add(1.2);
		values.add(1.3);
		values.add(1.3);
		values.add(1.3);
		values.add(1.3);
		values.add(1.4);
		values.add(1.4);
		values.add(2.0);

		QuantileNormalizationFunction quantileNormalizationFunction = new QuantileNormalizationFunction(values);

		System.out.println("Testing the QuantilesNormalizationFunction with value 0.0. Expected output: 0.0, obseved: "
				+ quantileNormalizationFunction.apply(0.0));

		System.out.println("Testing the QuantilesNormalizationFunction with value 1.0. Expected output: 0.0, obseved: "
				+ quantileNormalizationFunction.apply(1.0));

		System.out.println("Testing the QuantilesNormalizationFunction with value 1.3. Expected output: 0.55, obseved: "
				+ quantileNormalizationFunction.apply(1.3));

		System.out
				.println("Testing the QuantilesNormalizationFunction with value 1.33. Expected output: 0.75, obseved: "
						+ quantileNormalizationFunction.apply(1.33));

		System.out.println("Testing the QuantilesNormalizationFunction with value 2.0. Expected output: 1.0, obseved: "
				+ quantileNormalizationFunction.apply(2.0));

		System.out.println("Testing the QuantilesNormalizationFunction with value 3.0. Expected output: 1.0, obseved: "
				+ quantileNormalizationFunction.apply(3.0));

	}

}
