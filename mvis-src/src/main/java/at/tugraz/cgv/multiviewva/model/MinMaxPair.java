package at.tugraz.cgv.multiviewva.model;

/**
 * This class is a simple Pair of double values representing the minimum and maximum of a dimension.
 */
public class MinMaxPair {
	private double minimum;
	private double maximum;
	
	public MinMaxPair(double minimum, double maximum) {
		this.minimum = minimum;
		this.maximum = maximum;
	}
	
	public double getMinimum() {
		return minimum;
	}
	public void setMinimum(double minimum) {
		this.minimum = minimum;
	}
	public double getMaximum() {
		return maximum;
	}
	public void setMaximum(double maximum) {
		this.maximum = maximum;
	}
}
