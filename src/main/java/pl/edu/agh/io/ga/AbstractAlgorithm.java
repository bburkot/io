package pl.edu.agh.io.ga;

import org.apache.log4j.Logger;

import pl.edu.agh.io.ga.operator.ICrossover;
import pl.edu.agh.io.ga.operator.IInitializer;
import pl.edu.agh.io.ga.operator.IMutator;
import pl.edu.agh.io.ga.operator.IRate;
import pl.edu.agh.io.ga.operator.ISelector;
import pl.edu.agh.io.ga.representation.IPopulation;

public abstract class AbstractAlgorithm extends Thread {
	private static final Logger logger = Logger.getLogger(AbstractAlgorithm.class);
		
	@Override
	public void run() {
		logger.debug(String.format("Thread %s start", Thread.currentThread().getName()));
		IPopulation population = getInitilizator().generatePopulation();
		
		for (long i=0; i<getProperties().getStepsNumber(); ++i){
			getSelector().killWeakest(population);
			IPopulation childs = getCrossover().crossover(population);
			getMutator().proced(childs);
			
			population.attach(childs);
			
			if (i > 0 && (i % getProperties().getLogEveryNthLoop() == 0))
				logPopulation(population, i);
		}
		
		logPopulation(population, getProperties().getStepsNumber());
		logger.debug(String.format("Thread %s finished", Thread.currentThread().getName()));
	}
	protected void logPopulation(IPopulation population, long nthLoop){
		if (getMainNode() != null)
			getMainNode().notifyBestResults(population.getBestGenotype(), nthLoop);
	}
	
	// getters
	protected abstract IRate getRate();
	protected abstract IMutator getMutator();
	protected abstract ICrossover getCrossover();
	protected abstract ISelector getSelector();
	protected abstract IInitializer getInitilizator();
	protected abstract IMainNode getMainNode();
	protected abstract AlgorithmProperties getProperties();
}
