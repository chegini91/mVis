package com.github.TKnudsen.DMandML.model.retrieval;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.github.TKnudsen.ComplexDataObject.data.entry.EntryWithComparableKey;
import com.github.TKnudsen.ComplexDataObject.data.ranking.Ranking;
import com.github.TKnudsen.ComplexDataObject.model.distanceMeasure.IDistanceMeasure;

/**
 * <p>
 * Title: KNN
 * </p>
 * 
 * <p>
 * Description: k nearest neighbor operation, applied on a collection of
 * pre-given instancess.
 * </p>
 * 
 * <p>
 * Copyright: (c) 2016-2018 Juergen Bernard, https://github.com/TKnudsen/DMandML
 * </p>
 * 
 * @author Juergen Bernard
 * @version 1.02
 */
public class KNN<FV> {

	private int kNN;

	private IDistanceMeasure<? super FV> distanceMeasure;

	private final Collection<FV> elements;

	public KNN(int kNN, IDistanceMeasure<? super FV> distanceMeasure, Collection<? extends FV> elements) {
		setKNN(kNN);

		this.distanceMeasure = distanceMeasure;
		this.elements = Collections.unmodifiableCollection(elements);
	}

	public Ranking<EntryWithComparableKey<Double, FV>> getNearestNeighborsWithScores(FV element) {
		Ranking<EntryWithComparableKey<Double, FV>> ranking = new Ranking<>();

		for (FV o : elements) {
			if (o.equals(element))
				continue;

			double distance = distanceMeasure.getDistance(element, o);
			if (ranking.size() >= kNN && ranking.get(kNN - 1).getKey() < distance)
				continue;

			ranking.add(new EntryWithComparableKey<Double, FV>(distance, o));

			if (ranking.size() > kNN)
				ranking.removeLast();
		}

		return ranking;
	}

	public List<FV> getNearestNeighbors(FV element) {
		Ranking<EntryWithComparableKey<Double, FV>> nearestNeighborsWithScores = getNearestNeighborsWithScores(element);

		List<FV> returnElements = new ArrayList<>();
		for (int i = 0; i < nearestNeighborsWithScores.size(); i++)
			returnElements.add(nearestNeighborsWithScores.get(i).getValue());

		return returnElements;
	}

	public int getKNN() {
		return kNN;
	}

	public void setKNN(int kNN) {
		if (kNN < 1)
			throw new IllegalArgumentException("KNN: illegal parameter value for kNN: " + kNN + "must be >0");

		this.kNN = kNN;
	}

}
