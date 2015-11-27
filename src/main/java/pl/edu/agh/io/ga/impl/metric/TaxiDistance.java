package pl.edu.agh.io.ga.impl.metric;

import pl.edu.agh.io.pojo.Point;


public class TaxiDistance implements IMetric {
	
	public double distance(Point p1, Point p2){
		double d1 = p2.getLatitude().doubleValue() - p1.getLatitude().doubleValue();
		double d2 = p2.getLongitude().doubleValue() - p1.getLongitude().doubleValue();

		return (Math.abs(d1) + Math.abs(d2)) * 109.5;
	}
}
