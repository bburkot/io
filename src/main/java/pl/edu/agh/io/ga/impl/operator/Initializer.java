package pl.edu.agh.io.ga.impl.operator;

import org.apache.log4j.Logger;

import pl.edu.agh.io.ga.AlgorithmProperties;
import pl.edu.agh.io.ga.impl.representation.Genotype;
import pl.edu.agh.io.ga.impl.representation.Population;
import pl.edu.agh.io.ga.operator.IInitializer;
import pl.edu.agh.io.ga.representation.IPopulation;

public class Initializer implements IInitializer {
	private static Logger logger = Logger.getLogger(Initializer.class);
	
	private AlgorithmProperties properties;	
	private GenotypeHelper helper;
	
	
	public Initializer(AlgorithmProperties properties, GenotypeHelper helper){
		this.properties = properties;
		this.helper = helper;
	}
	
	@Override
	public IPopulation generatePopulation() {
		logger.debug("start");
		Population population = new Population();
		
		for (long i=0; i<properties.getThreadPopulationInitSize(); ++i){
			Genotype g = helper.create();
			population.addGenotype(g);
		}

		logger.debug("end");
		return population;
	}
}
