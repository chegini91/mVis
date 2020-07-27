package com.github.TKnudsen.ComplexDataObject.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataContainer;
import com.github.TKnudsen.ComplexDataObject.data.complexDataObject.ComplexDataObject;
import com.github.TKnudsen.ComplexDataObject.model.tools.ComplexDataObjectFactory;

public class ComplexDataContainerTest {

	@Test
	public void shouldConstructWithObjects() {
		final List<ComplexDataObject> objects = Arrays.asList(
				ComplexDataObjectFactory.createObject("Att A", new Double(2.0), "Att B", "asdf"),
				ComplexDataObjectFactory.createObject("Att A", new Double(3.0), "Att B", "jkl�")
		);

		final ComplexDataContainer complexDataContainer = new ComplexDataContainer(objects);

		assertThat(complexDataContainer).hasSameElementsAs(objects);
	}
}
