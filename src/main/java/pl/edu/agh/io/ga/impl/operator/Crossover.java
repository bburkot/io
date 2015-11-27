package pl.edu.agh.io.ga.impl.operator;

import java.util.Iterator;
import java.util.Random;

import org.apache.log4j.Logger;

import pl.edu.agh.io.ga.AlgorithmProperties;
import pl.edu.agh.io.ga.impl.representation.Genotype;
import pl.edu.agh.io.ga.impl.representation.Population;
import pl.edu.agh.io.ga.operator.ICrossover;
import pl.edu.agh.io.ga.representation.IGenotype;
import pl.edu.agh.io.ga.representation.IPopulation;

public class Crossover implements ICrossover {
	private static Logger logger = Logger.getLogger(Crossover.class);
	
	private AlgorithmProperties properties;
	private GenotypeHelper helper;
	private Random random;
	private final long MAX_SIZE, MIN_SIZE, AVG_SIZE;
	
	public Crossover(AlgorithmProperties properties, GenotypeHelper helper) {
		this.properties = properties;
		this.helper = helper;
		this.random = properties.getRandom();
		this.MIN_SIZE = properties.getThreadPopulationMinSize();
		this.MAX_SIZE = properties.getThreadPopulationMaxSize();
		this.AVG_SIZE = (MIN_SIZE + MAX_SIZE) / 2;
	}
	
	@Override
	public IPopulation crossover(IPopulation parents) {	
		logger.debug("start");
		Population childs = new Population();
		double crossoverRate = getCrossoverRate(parents);
		if (crossoverRate <= 0)
			return childs;
		
		Iterator<IGenotype> iter = parents.getGenotypes().iterator();		
		while(iter.hasNext()){
			Genotype parent1 = (Genotype) iter.next();
			if (!iter.hasNext())
				continue;
			Genotype parent2 = (Genotype) iter.next();
			
			if (random.nextDouble() < crossoverRate)
				childs.addGenotype(helper.create(parent1, parent2));
		}
		logger.debug("end");
		return childs;
	}

	private double getCrossoverRate(IPopulation parents){
		int population_size = parents.getGenotypes().size();
		if (population_size <= AVG_SIZE)
			return properties.getCrossoverRate();
		
		if (population_size >= MAX_SIZE)
			return 0;
		
		return properties.getCrossoverRate() 
				* (MAX_SIZE - population_size) / MAX_SIZE;
	}
}
