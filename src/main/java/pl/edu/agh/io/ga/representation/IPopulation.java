package pl.edu.agh.io.ga.representation;

import java.util.Collection;


public interface IPopulation {

	IGenotype getBestGenotype();

	Collection<IGenotype> getGenotypes();

	void attach(IPopulation childs);

}
