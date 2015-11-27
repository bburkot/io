package pl.edu.agh.io.ga.impl.operator;

import java.util.Iterator;
import java.util.Random;

import org.apache.log4j.Logger;

import pl.edu.agh.io.ga.AlgorithmProperties;
import pl.edu.agh.io.ga.operator.ISelector;
import pl.edu.agh.io.ga.representation.IGenotype;
import pl.edu.agh.io.ga.representation.IPopulation;

public class Selector implements ISelector {
	private static Logger logger = Logger.getLogger(Selector.class);
	
	private AlgorithmProperties properties;
	private final long MAX_SIZE, MIN_SIZE, AVG_SIZE;
	
	public Selector(AlgorithmProperties properties) {
		this.properties = properties;
		this.MIN_SIZE = properties.getThreadPopulationMinSize();
		this.MAX_SIZE = properties.getThreadPopulationMaxSize();
		this.AVG_SIZE = (MIN_SIZE + MAX_SIZE) / 2;
	}
	
	@Override
	public void killWeakest(IPopulation population) {
		logger.debug("start");
		double killRate = getKillRate(population);
		if (killRate <= 0)
			return;
		long minSize = properties.getThreadPopulationMinSize();
		long size = population.getGenotypes().size();
		Random random = properties.getRandom();
		IGenotype best = population.getBestGenotype();
		
		Iterator<IGenotype> iter = population.getGenotypes().iterator();
		while (iter.hasNext() && size > minSize){
			IGenotype genotype = iter.next();
			if (!best.equals(genotype) && random.nextDouble() < killRate){
				iter.remove();
				size--;
			}
		}
		if (size > properties.getThreadPopulationMaxSize())
			killWeakest(population);
		logger.debug("end");
	}

	private double getKillRate(IPopulation parents){
		int population_size = parents.getGenotypes().size();
		if (population_size >= AVG_SIZE)
			return properties.getKillRate();
		
		if (population_size <= MIN_SIZE)
			return 0;
		
		return properties.getKillRate() 
				* (AVG_SIZE - population_size) / MAX_SIZE;
	}
}
