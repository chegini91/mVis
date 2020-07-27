package com.github.TKnudsen.ComplexDataObject.model.processors;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.github.TKnudsen.ComplexDataObject.model.tools.MathFunctions;

public class ParameterSupportTools {

	public static List<Double> getAlternativeDoubles(double baseline, int count) {

		SortedSet<Double> set = new TreeSet<>();

		int lower = count / 2;
		double div = 1 / (lower + 1.0);
		for (int i = 1; i < lower + 1; i++) {
			double newValue = baseline * (i * div);
			if (newValue != baseline)
				set.add(newValue);
		}

		int upper = count - lower;
		for (int i = 1; i < upper + 1; i++) {
			double pow = Math.pow(2, i);
			double newValue = baseline * pow;
			if (newValue != baseline)
				set.add(newValue);
		}

		return new ArrayList<>(set);
	}

	public static List<Integer> getAlternativeIntegers(int baseline, int count) {

		SortedSet<Integer> set = new TreeSet<>();

		int lower = count / 2;
		double div = 1 / (lower + 1.0);
		for (int i = 1; i < lower + 1; i++) {
			int newValue = (int) (MathFunctions.round(baseline * (i * div), 0));
			if (newValue != baseline)
				set.add(newValue);
		}

		int upper = count - lower;
		for (int i = 1; i < upper + 1; i++) {
			double pow = Math.pow(2, i);
			int newValue = (int) (baseline * pow);
			if (newValue != baseline)
				set.add(newValue);
		}

		return new ArrayList<>(set);
	}

	public static List<Long> getAlternativeLongs(long baseline, int count) {

		SortedSet<Long> set = new TreeSet<>();

		int lower = count / 2;
		double div = 1 / (lower + 1.0);
		for (int i = 1; i < lower + 1; i++) {
			long newValue = (long) (MathFunctions.round(baseline * (i * div), 0));
			if (newValue != baseline)
				set.add(newValue);
		}

		int upper = count - lower;
		for (int i = 1; i < upper + 1; i++) {
			double pow = Math.pow(2, i);
			long newValue = (long) (baseline * pow);
			if (newValue != baseline)
				set.add(newValue);
		}

		return new ArrayList<>(set);
	}
}
