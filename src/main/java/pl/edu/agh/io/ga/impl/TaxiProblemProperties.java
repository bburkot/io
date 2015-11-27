package pl.edu.agh.io.ga.impl;

import pl.edu.agh.io.ga.AlgorithmProperties;
import pl.edu.agh.io.ga.IMainNode;
import pl.edu.agh.io.ga.impl.metric.IMetric;
import pl.edu.agh.io.ga.impl.metric.TaxiDistance;

public class TaxiProblemProperties extends AlgorithmProperties {
	
	
	private double taxiCost = 1000;
	private double idleDriveCost = 3; //  money / KM		\\ only between clients 
	private double taxiIdlenessCost = 0;   // money / s    
	
	private double taxiAvarangeSpeed = 35; //  KM / h

	private double skipTryAddToList = 0.0;
	private double createNewInstedCrossover = 0.15;
	
	private IMainNode mainNode;	
	private IMetric metric;
	
		
	public TaxiProblemProperties(){
		super();
		metric = new TaxiDistance();
	}
	
	public void setPopulationSize(Long min, Long max) {
		super.setThreadPopulationMinSize(min);
		super.setThreadPopulationMaxSize(max);
	}

	public void setGAStandardParams(Double mutationRate, Double crossoverRate,
			Double killRate) {
		super.setMutationRate(mutationRate);
		super.setCrossoverRate(crossoverRate);
		super.setKillRate(killRate);
	}
	
	public void setGAIndividualParams(Double skipTryAddToList,
			Double createNewInstedCrossover) {
		this.skipTryAddToList = skipTryAddToList;
		this.createNewInstedCrossover = createNewInstedCrossover;
	}
	
	
	public static String genealInfoHeader(){
		return "stepsNumber ; threadsNumber ; threadPopulationMinSize"
				+ " ; threadPopulationMaxSize ; mutationRate"
				+ " ; crossoverRate ; killRate ; skipTryAddToList"
				+ " ; createNewInstedCrossover\n";
	}
	
	public String generalInfo(){
		return getStepsNumber() 
				+ " ; " + getThreadsNumber()
				+ " ; " + getThreadPopulationMinSize()
				+ " ; " + getThreadPopulationMaxSize()
				+ " ; " + getMutationRate() 
				+ " ; " + getCrossoverRate() 
				+ " ; " + getKillRate()
				+ " ; " + skipTryAddToList
				+ " ; " + createNewInstedCrossover;
	}
	
	// getters and setters
	public double getTaxiCost() {
		return taxiCost;
	}
	public void setTaxiCost(double taxiCost) {
		this.taxiCost = taxiCost;
	}
	public double getIdleDriveCost() {
		return idleDriveCost;
	}
	public void setIdleDriveCost(double driveToClientCost) {
		this.idleDriveCost = driveToClientCost;
	}
	public double getTaxiIdlenessCost() {
		return taxiIdlenessCost;
	}
	public void setTaxiIdlenessCost(double taxiIdlenessCost) {
		this.taxiIdlenessCost = taxiIdlenessCost;
	}
	public double getTaxiAvarangeSpeed() {
		return taxiAvarangeSpeed;
	}
	public void setTaxiAvarangeSpeed(double taxiAvarangeSpeed) {
		this.taxiAvarangeSpeed = taxiAvarangeSpeed;
	}
	public IMetric getMetric() {
		return metric;
	}
	public void setMetric(IMetric metric) {
		this.metric = metric;
	}
	public double getSkipTryAddToList() {
		return skipTryAddToList;
	}
	public void setSkipTryAddToList(double skipTryAddToList) {
		this.skipTryAddToList = skipTryAddToList;
	}
	public double getCreateNewInstedCrossover() {
		return createNewInstedCrossover;
	}
	public void setCreateNewInstedCrossover(double createNewInstedCrossover) {
		this.createNewInstedCrossover = createNewInstedCrossover;
	}
	public IMainNode getMainNode(){
		return mainNode;
	}
	public void setMainNode(IMainNode tps){
		this.mainNode = tps;
	}
}
