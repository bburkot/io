package pl.edu.agh.io.ga.operator;

import pl.edu.agh.io.ga.representation.IPopulation;

public interface ICrossover {

	IPopulation crossover(IPopulation parents);

}
