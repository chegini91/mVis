package com.github.TKnudsen.ComplexDataObject.model.tools;

public class StringUtilsTests {

	public static void main(String[] args) {
		double value = 66.3363456;
		System.out.println(value);

		String trunc = StringUtils.truncateDouble(value, 3);
		System.out.println(trunc);
	}
}
