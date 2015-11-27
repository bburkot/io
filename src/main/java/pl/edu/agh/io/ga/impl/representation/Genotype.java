package pl.edu.agh.io.ga.impl.representation;

import java.util.List;
import java.util.Map;

import pl.edu.agh.io.ga.representation.IGenotype;
import pl.edu.agh.io.pojo.Trip;

public class Genotype implements IGenotype {
	
	//			taxi number, ordered by time list of trips
	private Map<Integer, List<Trip>> map;
	private double rate;
	
	public Genotype(Map<Integer, List<Trip>> map) {
		this.map = map;
	}
	
	@Override
	public double getRate() {
		return rate;
	}
	public Map<Integer, List<Trip>> getMap() {
		return map;
	}
	public void setRate(double d) {
		rate = d;
	}

	public long getNumberOfTrips(){
		long sum = 0;
		for (List<Trip> trips : map.values())
			sum += trips.size();
		return sum;
	}
	
	@Override
	public String toString() {
		return "Genotype [rate=" + rate + ", drivers=" + map.size() 
				//+ ", super.toString()="+ super.toString() 
				+ "]";
	}
}
