package pl.edu.agh.io.ga;

import pl.edu.agh.io.ga.representation.IGenotype;

public interface IMainNode {

	void notifyBestResults(IGenotype bestChromosome, long nthLoop);
	
}
