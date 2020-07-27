package com.github.TKnudsen.ComplexDataObject.preprocessing;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataContainer;
import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;
import com.github.TKnudsen.ComplexDataObject.model.processors.complexDataObject.DoubleConverter;
import com.github.TKnudsen.ComplexDataObject.model.tools.ComplexDataObjectFactory;

public class DoubleConverterTest {

	@Test
	public void shouldProcessDataObjectContainers() {
		final List<ComplexDataObject> objects = Arrays.asList(
				ComplexDataObjectFactory.createObject("A", new Double(2.0), "B", "1,2"),
				ComplexDataObjectFactory.createObject("A", new Double(3.0), "B", "3,3")
		);

		final ComplexDataContainer complexDataContainer = new ComplexDataContainer(objects);

		DoubleConverter doubleConverter = new DoubleConverter("B", "B'", ',', '.');
		doubleConverter.process(complexDataContainer);

		complexDataContainer.forEach(dataObject -> assertThat(dataObject.getAttribute("B'")).isOfAnyClassIn(Double.class));
	}
}
