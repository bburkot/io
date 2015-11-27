package pl.edu.agh.io.ga.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.edu.agh.io.pojo.Trip;

public class Data {

	private final List<Trip> list;
	private final long tripsIdSum;
	private final int listSize;
	
	private final long shuffleListEvery;
	private long shuffle = 0;
	
	public Data(List<Trip> list, long shuffleListEvery){		
		long tripsIdSum = 0;
		for (Trip trip : list){
			tripsIdSum += trip.getId();
		}
		
		this.list = new ArrayList<Trip>(list);
		this.listSize = list.size();
		this.tripsIdSum = tripsIdSum;
		this.shuffleListEvery = shuffleListEvery;
	}

	
	public synchronized List<Trip> shuffleAndGetList(){
		if ((shuffle--) <= 0){
			shuffle = shuffleListEvery;
			Collections.shuffle(list);
		}
		return list;
	}

	public long getTripsIdSum() {
		return tripsIdSum;
	}
	public List<Trip> getList() {
		return list;
	}
	public int getSize() {
		return listSize;
	}
	
}
