package pl.edu.agh.io.ga.impl.operator;

import java.util.Random;

import org.apache.log4j.Logger;

import pl.edu.agh.io.ga.AlgorithmProperties;
import pl.edu.agh.io.ga.impl.representation.Genotype;
import pl.edu.agh.io.ga.operator.IMutator;
import pl.edu.agh.io.ga.representation.IGenotype;
import pl.edu.agh.io.ga.representation.IPopulation;

public class Mutator implements IMutator {
	private static Logger logger = Logger.getLogger(Mutator.class);
	
	private AlgorithmProperties properties;
	private GenotypeHelper helper;
	
	public Mutator(AlgorithmProperties properties, GenotypeHelper helper) {
		this.properties = properties;
		this.helper = helper;
	}
	
	@Override
	public void proced(IPopulation childs) {
		logger.debug("start");
		Random random = properties.getRandom();
		double mutationRate = properties.getMutationRate();
		
		for (IGenotype genotype : childs.getGenotypes())
			if (random.nextDouble() < mutationRate)
				mutate(genotype);
		logger.debug("end");
	}

	@Override
	public void mutate(IGenotype igenotype) {
		logger.debug("start");
		Genotype genotype = (Genotype) igenotype;
		helper.mutate(genotype);	
		logger.debug("end");
	}

}
