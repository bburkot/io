package pl.edu.agh.io.ga.operator;

import pl.edu.agh.io.ga.representation.IGenotype;
import pl.edu.agh.io.ga.representation.IPopulation;

public interface IMutator {

	void proced(IPopulation childs);
	void mutate(IGenotype genotype);
}
