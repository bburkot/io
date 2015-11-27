package pl.edu.agh.io.ga.impl.metric;

import pl.edu.agh.io.pojo.Point;

public interface IMetric {
	double distance(Point p1, Point p2);
}
