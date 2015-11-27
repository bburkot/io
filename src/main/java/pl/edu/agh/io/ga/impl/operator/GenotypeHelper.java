package pl.edu.agh.io.ga.impl.operator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import pl.edu.agh.io.ga.impl.Data;
import pl.edu.agh.io.ga.impl.TaxiProblemProperties;
import pl.edu.agh.io.ga.impl.representation.Genotype;
import pl.edu.agh.io.pojo.Trip;

public class GenotypeHelper {
	private static Logger logger = Logger.getLogger(GenotypeHelper.class);
	
	
	private Rate rateFunction;
	private TaxiProblemProperties properties;
	private Random random;
	private Data data; 
	
	private double skipTryAddToList;
	
	public GenotypeHelper(Data data){
		this.properties = new TaxiProblemProperties();
		this.random = properties.getRandom();
		this.skipTryAddToList = properties.getSkipTryAddToList();
		this.rateFunction = new Rate();
		this.data = data;
	}
	
	public GenotypeHelper(TaxiProblemProperties properties, Rate rate, 
			Data data){
		this.properties = properties;
		this.random = properties.getRandom();
		this.skipTryAddToList = properties.getSkipTryAddToList();
		this.rateFunction = rate;
		this.data = data;
	}
	
	public Genotype create(){
		logger.debug("start");
		Map<Integer, List<Trip>> map = new HashMap<Integer, List<Trip>>(); 	

		for (Trip trip : data.shuffleAndGetList())
			addTripToMap(map, trip);		
		logger.debug("end");
		return create(map);
	}
	
	private Genotype create(Map<Integer, List<Trip>> map){
		Genotype genotype = new Genotype(map);
		rateFunction.rate(genotype);
		logger.debug(genotype.getRate());
		return genotype;
	}

	
	public Genotype create(Genotype parent1, Genotype parent2){
		logger.debug("start crossover ");// +parent1.getNumberOfTrips() + " " + parent2.getNumberOfTrips());
		
		if (properties.getRandom().nextDouble() < properties.getCreateNewInstedCrossover())
			return create();
		
		Set<Long> addedTripsId = new HashSet<Long>();
		
		Map<Integer, List<Trip>> map = new HashMap<Integer, List<Trip>>(); 
		
		Iterator<List<Trip>> iter1 = parent1.getMap().values().iterator();
		Iterator<List<Trip>> iter2 = parent2.getMap().values().iterator();
		
		while(iter1.hasNext() || iter2.hasNext()){
			if ( iter1.hasNext() )
				onIterHasNext(map, iter1, addedTripsId);
			if ( iter2.hasNext() )
				onIterHasNext(map, iter2, addedTripsId);		
		}
		
		logger.debug("end crossover");
		return create(map);	
	}
	
	private void onIterHasNext(Map<Integer, List<Trip>> map, 
			Iterator<List<Trip>> iter, Set<Long> addedTripsId){
		List<Trip> taxi = new ArrayList<>( iter.next() );
		int beginTaxiSize = taxi.size();
		removeTripAlreadyAdded(addedTripsId, taxi);
		if (taxi.size() > beginTaxiSize/2)
			map.put(map.size(), taxi);
		else
			for (Trip trip : taxi)
				addTripToMap(map, trip);
	}
	
	
	private void removeTripAlreadyAdded(Set<Long> addedTripsId, List<Trip> taxi) {
		Iterator<Trip> iter = taxi.iterator();
		while (iter.hasNext()){
			Trip trip = iter.next();
			if (addedTripsId.contains(trip.getId()))
				iter.remove();
			else
				addedTripsId.add(trip.getId());
		}
	}

	public void mutate(Genotype genotype){
		logger.debug("start");
		int avarangeListSize = data.getSize() / genotype.getMap().size();
		int changedListNum = random.nextInt(genotype.getMap().size());
		Integer changedListKey = new ArrayList<Integer>(genotype.getMap().keySet())
				.get(changedListNum);
		
		List<Trip> changedList = genotype.getMap().get(changedListKey);
		int changedListSize = changedList.size();
		
		if (changedListSize > avarangeListSize){ 
			int splitPoint = random.nextInt(changedListSize);
			List<Trip> trip1 = 
					new ArrayList<Trip>(changedList.subList(0, splitPoint));
			List<Trip> trip2 = 
					new ArrayList<Trip>(changedList.subList(splitPoint, changedListSize));
			
			genotype.getMap().put(changedListKey, trip1);
			genotype.getMap().put(genotype.getMap().size(), trip2);			
		} else { 
			for (Trip trip : changedList)
				for (Entry<Integer,List<Trip>> entry : genotype.getMap().entrySet()){
					if (entry.getKey() != changedListNum){
						boolean added = tryAddToList(entry.getValue(), trip);	
						if (added)
							break;
					}
				}
		}
		
		rateFunction.rate(genotype);
	}
	
	private void createNewTaxi(Map<Integer, List<Trip>> map, Trip trip) {
		List<Trip> list = new ArrayList<Trip>();
		list.add(trip);
		map.put(map.size(), list);
	}

	private void addTripToMap(Map<Integer, List<Trip>> map, Trip trip){
		boolean added = false;
		for (List<Trip> list : map.values())
			if (skipTryAddToList < random.nextDouble())
			{
				added = tryAddToList(list, trip);
				if (added)
					break;
			}
		if ( ! added )
			createNewTaxi(map, trip);	
	}
	
	private boolean tryAddToList(List<Trip> list, Trip trip) {
		for (int i=0; i<list.size()-1; ++i)
			if ( canInsertAfter(list.get(i), trip) && canInsertBefore(list.get(i+1), trip)){
				list.add(i + 1, trip);
				return true;
			}		
		if (canInsertAfter(list.get(list.size() - 1), trip)){
			list.add(trip);
			return true;
		}		
		return false;
	}
	

	private boolean canInsertBefore(Trip trip, Trip toInsert){
		return toInsert.getTimestamp().getTime() + toInsert.getDurationMills()
				< trip.getTimestamp().getTime() ;
		
	}
	private boolean canInsertAfter(Trip trip, Trip toInsert){
		return (trip.getTimestamp().getTime()  + trip.getDurationMills())
				< toInsert.getTimestamp().getTime();
	}
	
	public static void printGenotype(Genotype genotype){
		for (List<Trip> trips : genotype.getMap().values()){
			for (Trip trip : trips)
				System.out.print(trip.toStringShort());
			System.out.println();
		}
	}
}
