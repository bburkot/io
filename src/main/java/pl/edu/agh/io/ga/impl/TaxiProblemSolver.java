package pl.edu.agh.io.ga.impl;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.print.attribute.standard.Chromaticity;

import org.apache.log4j.Logger;

import pl.edu.agh.io.ga.IMainNode;
import pl.edu.agh.io.ga.impl.representation.Genotype;
import pl.edu.agh.io.ga.representation.IGenotype;
import pl.edu.agh.io.pojo.Trip;
import pl.edu.agh.io.util.DataUtil;
import pl.edu.agh.io.util.HibernateUtil;

public class TaxiProblemSolver implements IMainNode {
	private static Logger logger = Logger.getLogger(TaxiProblemSolver.class);
	
	// nth loop , best rate
	private TreeMap<Long, Double> bestResults; 
	private Genotype bestGenotype;
		
	private static FileWriter fw;
	private static StringBuilder generalInfo = createGeneralInfoSB();
	
	public static void main(String[] args) throws IOException{	
		fw = new FileWriter("output_" + System.currentTimeMillis() + ".txt" );		
		logger.info("start main");
		
		Data data = getData();
		TaxiProblemProperties tpp = new TaxiProblemProperties();
		
//		testPopulationSize(data, tpp);
//		testGAStandardParams(data, tpp);
//		testGAIndividualParams(data, tpp);
		
		generalTest(data, tpp);
			
		generalInfo.append("--- GENERAL INFO ----");
		logger.warn(generalInfo.toString());
		fw.write(generalInfo.toString());
		
		fw.close();
	}

	public static void testPopulationSize(Data data, TaxiProblemProperties tpp){
		List<Long> threadPopulationMinSizes = Arrays.asList(10L, 100L, 500L, 1000L);
		List<Long> threadPopulationMaxSizes = Arrays.asList(30L, 300L, 1500L, 3000L);
		
		for (int i=0; i<threadPopulationMinSizes.size(); ++i){
			tpp.setPopulationSize(threadPopulationMinSizes.get(i),
					threadPopulationMaxSizes.get(i) );
						
			singleRun(tpp, data);			
		}
	}	
	public static void testGAStandardParams(Data data, TaxiProblemProperties tpp){
		List<Double> mutationRates = Arrays.asList(0.05, 0.1, 0.2, 0.4);
		List<Double> crossoverRates = Arrays.asList(0.5, 0.6, 0.7, 0.8);
		List<Double> killRates = Arrays.asList(0.1, 0.2, 0.3, 0.4);
		
		for (Double mutationRate : mutationRates)
			for (Double crossoverRate : crossoverRates)
				for (Double killRate : killRates){
					tpp.setGAStandardParams(mutationRate, crossoverRate, killRate);
					singleRun(tpp, data);
				}
	}	
	public static void testGAIndividualParams(Data data, TaxiProblemProperties tpp){
		List<Double> skipTryAddToLists = Arrays.asList(0.0, 0.05, 0.1, 0.15, 0.2, 0.4);
		List<Double> createNewInstedCrossovers = Arrays.asList(0.0, 0.05, 0.1, 0.15, 0.2, 0.4);
		
		for (Double skipTryAddToList : skipTryAddToLists)
			for (Double createNewInstedCrossover : createNewInstedCrossovers){
				tpp.setGAIndividualParams(skipTryAddToList, createNewInstedCrossover);
				singleRun(tpp, data);
			}
	}
	
	public static void generalTest(Data data, TaxiProblemProperties tpp){
		singleRun(tpp, data);
	}
	
	private static StringBuilder createGeneralInfoSB(){
		generalInfo = new StringBuilder();
		generalInfo.append("\n--- GENERAL INFO ----\n");
		generalInfo.append("data.size ; best genotype rate ; runtime ; ");
		generalInfo.append(TaxiProblemProperties.genealInfoHeader());
		return generalInfo;
	}
	
	private static Data getData(){
		// full data  min (2013-07-01 02:00:53), max (2014-07-01 01:59:56) // size 1710670
		// small data min (2013-07-01 02:00:53), max (2013-07-02 18:17:26) // size 7733
//		Date from = DataUtil.createDate("2013-07-01 07:00:53"); // size 933
		
		Date from = null;//DataUtil.createDate("2013-07-01 09:10:53");
		Date to = null;//DataUtil.createDate("2013-07-01 05:17:26");		
		
		List<Trip> list = HibernateUtil.getTrips(from, to);
		HibernateUtil.getSessionFactory().close();
		logger.info("list size " + list.size());
		
		return new Data(list, 10);
	}
	
	public static void singleRun(TaxiProblemProperties tpp, Data data){
		Long startTime = System.currentTimeMillis();
		
		TaxiProblemSolver tps = new TaxiProblemSolver();
		tps.bestResults = new TreeMap<Long, Double>();
		tpp.setMainNode(tps);
		tps.bestGenotype = null;
		
		TreeMap<Long, Double> results = tps.runAlgorithm(tpp, data);
		
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("-- SINGLE RUN STATS: data.size = %d, threads.number = %d\nnth loop ; best genotype.rank()", 
				data.getSize(), tpp.getThreadsNumber()));
		for (Entry<Long, Double> entry : results.entrySet()){
			sb.append(String.format("\n%d ; %f", entry.getKey(), entry.getValue()));
		}
		
		sb.append("\n");
		sb.append(tps.bestGenotype);		
		sb.append("\n------- SINGLE RUN STATS ----------\n\n");
		try {
			fw.write(sb.toString());
			logger.info(sb.toString());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		
		generalInfo.append(String.format("%d ; %f ; %d ; %s\n", data.getSize(), 
			results.lastEntry().getValue(), System.currentTimeMillis() - startTime, 
			tpp.generalInfo()));
	}
	
	
	public TreeMap<Long, Double> runAlgorithm(TaxiProblemProperties tpp, Data data){
		logger.debug("start solver");
					
		List<TaxiProblemAlgorithmWorker> workers = new ArrayList<>();
		
		for (int i=0; i<tpp.getThreadsNumber(); ++i){
			TaxiProblemAlgorithmWorker worker = new TaxiProblemAlgorithmWorker(tpp, data);
			worker.start();
			workers.add(worker);
		}
		logger.info("-- All workers started");
		
		for (TaxiProblemAlgorithmWorker worker : workers){
			try {
				worker.join();
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
		}
		
		return bestResults;
	}

	@Override
	public synchronized void notifyBestResults(IGenotype bestChromosome, long nthLoop) {
		if (	!bestResults.containsKey(nthLoop) 
				|| bestResults.get(nthLoop) < bestChromosome.getRate())
			bestResults.put(nthLoop, bestChromosome.getRate());
		
		if (bestGenotype == null || bestGenotype.getRate() < bestChromosome.getRate())
			bestGenotype = (Genotype) bestChromosome;
		
//		logger.info("loop " + nthLoop 
//				+ ", size " + ((Genotype)bestChromosome).getNumberOfTrips() 
//				+ ", "+ bestChromosome);
	}
}
