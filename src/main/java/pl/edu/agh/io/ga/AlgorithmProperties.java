package pl.edu.agh.io.ga;

import java.util.Random;


public class AlgorithmProperties {

	private long stepsNumber = 100; 
	private int logEveryNthLoop = 10;
	
	private int threadsNumber = 10;
	private long threadPopulationMinSize = 100;
	private long threadPopulationMaxSize = 300;
		
	private double mutationRate = 0.05;	 	
	private double crossoverRate = 0.7;
	private double killRate = 0.3; 
		
	
	public Random getRandom() {
		return new Random();
	}
	
	public long getStepsNumber() {
		return stepsNumber;
	}
	public int getThreadsNumber() {
		return threadsNumber;
	}
	public long getThreadPopulationInitSize() {
		return (threadPopulationMinSize + threadPopulationMaxSize)/2;
	}
	public int getLogEveryNthLoop() {
		return logEveryNthLoop;
	}
	public double getMutationRate() {
		return mutationRate;
	}
	public double getCrossoverRate() {
		return crossoverRate;
	}
	public double getKillRate() {
		return killRate;
	}
	public long getThreadPopulationMinSize() {
		return threadPopulationMinSize;
	}
	public long getThreadPopulationMaxSize() {
		return threadPopulationMaxSize;
	}
	
	
	public void setStepsNumber(long stepsNumber) {
		this.stepsNumber = stepsNumber;
	}
	public void setThreadsNumber(int threadsNumber) {
		this.threadsNumber = threadsNumber;
	}
	public void setLogEveryNthLoop(int logBestGenotypeEveryNthLoop) {
		this.logEveryNthLoop = logBestGenotypeEveryNthLoop;
	}
	public void setMutationRate(double mutationRate) {
		this.mutationRate = mutationRate;
	}
	public void setCrossoverRate(double crossoverRate) {
		this.crossoverRate = crossoverRate;
	}
	public void setKillRate(double killRate) {
		this.killRate = killRate;
	}
	public void setThreadPopulationMinSize(long threadPopulationMinSize) {
		this.threadPopulationMinSize = threadPopulationMinSize;
	}
	public void setThreadPopulationMaxSize(long threadPopulationMaxSize) {
		this.threadPopulationMaxSize = threadPopulationMaxSize;
	}
}
