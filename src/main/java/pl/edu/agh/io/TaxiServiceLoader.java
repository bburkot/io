package pl.edu.agh.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import pl.edu.agh.io.pojo.CallType;
import pl.edu.agh.io.pojo.DayType;
import pl.edu.agh.io.pojo.Point;
import pl.edu.agh.io.pojo.Trip;


@SuppressWarnings("unused")
public class TaxiServiceLoader {
	private static final Logger logger = Logger.getLogger(TaxiServiceLoader.class);
//	private static final String FILENAME = "src/main/resources/data.csv";
	private static final String FILENAME = "src/main/resources/train.csv";
	private static final AtomicInteger threadWorking = new AtomicInteger(0);
	
	private static final int MAX_THREAD_NUM = 20;
	private static final int MAX_NUMBER_OBJECTS_TO_SAVE = 1000;
	
	private static Pattern POINT_PATTERN = Pattern.compile("(\\d+\\.\\d+,\\s?\\d+\\.\\d+)");
	
	private static ExecutorService es;

    public static void main( String[] args ) throws Exception {
    	HibernateUtil.getSessionFactory();
    	
    	logger.info("start");
    	Date startDate = new Date();
    	
    	es = Executors.newFixedThreadPool(MAX_THREAD_NUM);
    	
    	Map<Integer, Point> points = loadPoints();
    	waitTillThreadFinished();
    	System.out.println("Points " + points.size());
    	
    	loadData(points);
    	waitTillThreadFinished();
    	
    	logger.info("Load time: " + (new Date().getTime() - startDate.getTime()) + " [ms]");		
		
    	
    	es.shutdown(); 	  	
		HibernateUtil.getSessionFactory().close();   	
		 
    	System.exit(0);

    }
    
    private static void waitTillThreadFinished(){
    	logger.info("TaxiServiceLoader.waitTillThreadFinished() START");
    	while( threadWorking.get() > 0){
			try {
			    Thread.sleep(1000);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
		}
    	logger.info("TaxiServiceLoader.waitTillThreadFinished() END" );
    }
    
    public static Map<Integer, Point> loadPoints() throws Exception{
    	Map<Integer, Point> points = new HashMap<Integer, Point>();
		ExecutorService es = Executors.newFixedThreadPool(MAX_THREAD_NUM);
	
		FileReader fr = new FileReader(FILENAME);
		BufferedReader br = new BufferedReader(fr);
		
		br.readLine(); // skip header
		String line;
		List<Point> list = new ArrayList<Point>();
		while ( (line = br.readLine()) != null){							
			String splited[] = line.replace("\"", "").split(",", 9);
			
	    	Matcher matcher = POINT_PATTERN.matcher(splited[8]);	
	    	while(matcher.find()){
	    		String s[] = matcher.group().split(",");
	    		Point point = new Point(new BigDecimal(s[0].trim()), 
	    				new BigDecimal(s[1].trim()));  	
	    		if ( !points.containsKey(point.hashCode())){
		    		points.put(point.hashCode(), point);
		    		list.add(point);
	    		}
	    	}
	    	if (list.size() > MAX_NUMBER_OBJECTS_TO_SAVE){
				es.execute(new SaveHelper<Point>(list));
				list = new ArrayList<Point>();
			}
		}
		fr.close();
		if (list.size() > 0)
			es.execute(new SaveHelper<Point>(list));
    	return points;
    }
    
    public static void loadData(Map<Integer, Point> points) throws Exception{       	
		FileReader fr = new FileReader(FILENAME);
		BufferedReader br = new BufferedReader(fr);
		
		br.readLine(); // skip header
		
		List<Trip> trips = new ArrayList<Trip>();
		
		int lines = 0;
		String line;
		while ( (line = br.readLine()) != null) {				
			lines += 1;
			
			String splited[] = line.replace("\"", "").split(",", 9);
			
			long trip_id = Long.parseLong(splited[0]);
			char call_type = splited[1].charAt(0);
			
			Integer origin_call = splited[2].equals("") ? null : Integer.parseInt(splited[2]);				
			Integer origin_stand = splited[3].equals("") ? null : Integer.parseInt(splited[3]);
						
			Integer taxi_id = Integer.parseInt(splited[4]);
			
			Date timestamp = getDate(splited[5]);
			
			char day_type = splited[6].charAt(0);
			boolean missing_data = Boolean.parseBoolean(splited[7]);
							
			List<Point> route = splitRoute(splited[8], points);	
			Point start = null, end = null;
			
			if (route.size() > 0){						
				start = route.get(0);
				end = route.get(route.size() - 1);								
			}
			
			Trip trip = new Trip(trip_id, CallType.fromChar(call_type), origin_call, 
					origin_stand, taxi_id, timestamp, DayType.fromChar(day_type), 
					missing_data, route, start, end);

			trips.add(trip); 
			if (trips.size() > MAX_NUMBER_OBJECTS_TO_SAVE){					
				es.execute(new SaveHelper<Trip>(trips));	
				trips = new ArrayList<Trip>();
			}	
		}
		fr.close();
		if (trips.size() > 0)
			es.execute(new SaveHelper<Trip>(trips));	
    	System.out.println("Samples " + lines);
    }
    
    private static Date getDate(String unixTimestamp){
    	long secs = Long.parseLong(unixTimestamp);
    	Date date = new Date();
    	date.setTime(secs * 1000);
    	return date;
    }
    
    public static List<Point> splitRoute(String route, Map<Integer, Point> points){
    	List<Point> list = new ArrayList<Point>();
    	Matcher matcher = POINT_PATTERN.matcher(route);	
    	while(matcher.find()){
    		String s[] = matcher.group().split(",");
    		Point point = new Point(new BigDecimal(s[0].trim()), 
    				new BigDecimal(s[1].trim()));  		
    		if ( points.containsKey(point.hashCode())){
    			point = points.get(point.hashCode());
    			if (point.getId() <=0)
    				System.out.println("splitRoute() " + point);
    		} else {
    			points.put(point.hashCode(), point);
    			HibernateUtil.saveOrUpdate(point);
    			System.err.println("TaxiServiceLoader.splitRoute() DON'T FIND POINT IN MAP");
    		}
    		list.add(point);
    	}
    	return list;
    }
    
    
    public static class SaveHelper<T> implements Runnable {
    	private List<T> objs;
    	
    	public SaveHelper(List<T> trips) {
			this.objs = trips;
		}
    	
		@Override
		public void run() {
			threadWorking.incrementAndGet();
					
			Session session = HibernateUtil.getSessionFactory().openSession();
	    	session.beginTransaction();
	    	for (T obj : objs)
	    		session.save(obj);
	    	session.getTransaction().commit();
	    	session.close();	
	    	
	    	objs.clear();
	    	objs = null;
	    	threadWorking.decrementAndGet();
	    }    	
    }
}
