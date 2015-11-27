package pl.edu.agh.io.ga.impl.representation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pl.edu.agh.io.ga.representation.IGenotype;
import pl.edu.agh.io.ga.representation.IPopulation;

public class Population implements IPopulation {
		
	private IGenotype bestGenotype;
	private List<IGenotype> genotypes;

	public Population(List<IGenotype> genotypes, Genotype best) {
		this.genotypes = genotypes;
		this.bestGenotype = best;
	}

	public Population() {
		genotypes = new ArrayList<IGenotype>();
	}

	@Override
	public IGenotype getBestGenotype() {
		return bestGenotype;
	}

	@Override
	public Collection<IGenotype> getGenotypes() {
		return genotypes;
	}

	@Override
	public void attach(IPopulation childs) {
		if (childs == null || childs.getGenotypes().isEmpty())
			return;
		
		if (bestGenotype.getRate()  > childs.getBestGenotype().getRate()){
			bestGenotype = childs.getBestGenotype();
		}
		genotypes.addAll(childs.getGenotypes());
	}
	
	
	public void addGenotype(IGenotype genotype){
		if (bestGenotype == null || bestGenotype.getRate() > genotype.getRate())
			bestGenotype = genotype;
		genotypes.add(genotype);
	}
	
}
