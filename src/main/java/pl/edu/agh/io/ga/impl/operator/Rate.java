package pl.edu.agh.io.ga.impl.operator;

import java.util.List;

import pl.edu.agh.io.ga.impl.TaxiProblemProperties;
import pl.edu.agh.io.ga.impl.metric.IMetric;
import pl.edu.agh.io.ga.impl.representation.Genotype;
import pl.edu.agh.io.ga.operator.IRate;
import pl.edu.agh.io.ga.representation.IGenotype;
import pl.edu.agh.io.pojo.Trip;

public class Rate implements IRate {
//	private static Logger logger = Logger.getLogger(Rate.class);

	private TaxiProblemProperties rateProperties;
	
	
	public Rate() {
		this.rateProperties = new TaxiProblemProperties();
	}
	
	public Rate(TaxiProblemProperties rateProperties) {
		this.rateProperties = rateProperties;
	}




	public void rate(IGenotype igenotype){
		Genotype genotype = (Genotype) igenotype;		
		IMetric metric = rateProperties.getMetric();
		double idleDriveCost = rateProperties.getIdleDriveCost();
		double taxiIdlenessCost = rateProperties.getTaxiIdlenessCost();
		
		double rate=0;
		rate = genotype.getMap().size() * rateProperties.getTaxiCost();
		
		for (List<Trip> list : genotype.getMap().values()){
			for (int i=0; i<list.size()-1; ++i){
				rate += metric.distance(list.get(i).getEnd(), list.get(i+1).getStart())
					* idleDriveCost;
				rate += (list.get(i).getEndTime() - list.get(i).getStartTime()) 
						* taxiIdlenessCost;
			}
		}
		
		genotype.setRate(rate);
	}
}
