package pl.edu.agh.io.ga.impl;


import pl.edu.agh.io.ga.AbstractAlgorithm;
import pl.edu.agh.io.ga.AlgorithmProperties;
import pl.edu.agh.io.ga.IMainNode;
import pl.edu.agh.io.ga.impl.operator.Crossover;
import pl.edu.agh.io.ga.impl.operator.GenotypeHelper;
import pl.edu.agh.io.ga.impl.operator.Initializer;
import pl.edu.agh.io.ga.impl.operator.Mutator;
import pl.edu.agh.io.ga.impl.operator.Rate;
import pl.edu.agh.io.ga.impl.operator.Selector;

public class TaxiProblemAlgorithmWorker extends AbstractAlgorithm {

	private TaxiProblemProperties properties;
	
	private Rate rate;
	private Mutator mutator;
	private Crossover crossover;
	private Selector selector;
	private Initializer initializer;
	
	private GenotypeHelper helper; 
	
	public TaxiProblemAlgorithmWorker(TaxiProblemProperties properties, Data data) {
		
		this.properties = properties;
		
		this.rate = new Rate(properties);		
		this.helper = new GenotypeHelper(properties, rate, data);
		this.selector = new Selector(properties);
		this.mutator = new Mutator(properties, helper);
		this.crossover = new Crossover(properties, helper);
		this.initializer = new Initializer(properties, helper);	
	}
	
	// getters
	protected Rate getRate() {
		return rate;
	}
	protected Mutator getMutator() {
		return mutator;
	}
	protected Crossover getCrossover() {
		return crossover;
	}
	protected Selector getSelector() {
		return selector;
	}
	protected Initializer getInitilizator() {
		return initializer;
	}
	protected AlgorithmProperties getProperties() {
		return properties;
	}
	protected IMainNode getMainNode() {
		return properties.getMainNode();
	}
}
