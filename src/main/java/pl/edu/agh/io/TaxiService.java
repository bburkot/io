package pl.edu.agh.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import pl.edu.agh.io.pojo.Route;
import pl.edu.agh.io.pojo.Trip;


@SuppressWarnings("unused")
public class TaxiService {
	private static final Logger logger = Logger.getLogger(TaxiService.class);
	private static final String FILENAME = "src/main/resources/data.csv";
//	private static final String FILENAME = "src/main/resources/train.csv";
	private static final AtomicInteger threadWorking = new AtomicInteger(0);
	
	private static final int MAX_THREAD_NUM = 20;
	private static final int MAX_NUMBER_OBJECTS_TO_SAVE = 1000;
	
    public static void main( String[] args ) {
    	HibernateUtil.getSessionFactory();
    	System.out.println(new Date());
    	loadData(FILENAME);
    	System.out.println(new Date());
    	HibernateUtil.getSessionFactory().close();
    }
    
    
    public static void loadData(String filename){    
    	Date startDate = new Date();
    	try {
    		ExecutorService es = Executors.newFixedThreadPool(MAX_THREAD_NUM);
    		
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			
			br.readLine(); // skip header
			
			List<Trip> trips = new ArrayList<Trip>();
	    	long tripsSize = 0;
			
			int lines = 0;
			String line;
			while ( (line = br.readLine()) != null){	
				if (tripsSize > MAX_NUMBER_OBJECTS_TO_SAVE){					
					es.execute(new SaveHelper(trips));					
					trips = new ArrayList<Trip>();
					tripsSize = 0;
				}				
				lines += 1;
				tripsSize += 1;
				
				String splited[] = line.replace("\"", "").split(",", 9);
				
				String trip_id = splited[0];
				char call_type = splited[1].charAt(0);
				
				Integer origin_call = splited[2].equals("") ? null : Integer.parseInt(splited[2]);				
				Integer origin_stand = splited[3].equals("") ? null : Integer.parseInt(splited[3]);
							
				Integer taxi_id = Integer.parseInt(splited[4]);
				
				Date timestamp = getDate(splited[5]);
				
				char day_type = splited[6].charAt(0);
				boolean missing_data = Boolean.parseBoolean(splited[7]);
				String route = splited[8];
				
				BigDecimal startLat = null;
				BigDecimal startLong = null;
				BigDecimal endLat = null;
				BigDecimal endLong = null;
				
				List<Point> list = splitRoute(route);							
				if (list.size() > 0){						
					Point start = list.get(0);
					Point end = list.get(list.size() - 1);
					
					startLat = start.getLatitude();
					startLong = start.getLongitude();
					endLat = end.getLatitude();
					endLong = end.getLongitude();				
				}
				
				Trip trip = new Trip(trip_id, CallType.fromChar(call_type), origin_call, 
						origin_stand, taxi_id, timestamp, DayType.fromChar(day_type), 
						missing_data, new Route(route), startLat, startLong, endLat, endLong);
	
				trips.add(trip);
			}
			fr.close();
			
			if (trips.size() > 0){
				es.execute(new SaveHelper(trips));
			}
	    	System.out.println("Samples " + lines);
			
			while( threadWorking.get() > 0){
				System.out.println("Some threads are working ");	
				try {
				    Thread.sleep(1000);
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
			}
			es.shutdown();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	Date end = new Date();
    	System.out.println("Load time: " + (end.getTime() - startDate.getTime()) + " [ms]");
    }
    
    private static Date getDate(String unixTimestamp){
    	long secs = Long.parseLong(unixTimestamp);
    	Date date = new Date();
    	date.setTime(secs * 1000);
    	return date;
    }
    
    public static List<Point> splitRoute(String route){
    	List<Point> list = new ArrayList<Point>();
    	Pattern pattern = Pattern.compile("(\\d+\\.\\d+, \\d+\\.\\d+)");
    	Matcher matcher = pattern.matcher(route);	
    	while(matcher.find()){
    		String s[] = matcher.group().split(",");
    		list.add(new Point(
    				new BigDecimal(s[0].trim()), 
    				new BigDecimal(s[1].trim())  ));
    	}
    	return list;
    }

    
    public static class SaveHelper implements Runnable {
    	private List<Trip> trips;
    	
    	public SaveHelper(List<Trip> trips) {
			this.trips = trips;
		}
    	
		@Override
		public void run() {
			threadWorking.incrementAndGet();
			Session session = HibernateUtil.getSessionFactory().openSession();
	    	session.beginTransaction();
	    	
	    	for (Trip trip : trips)
	    		session.save(trip);
	    	
	    	session.getTransaction().commit();
	    	session.close();	
	    	trips.clear();
	    	trips = null;
	    	threadWorking.decrementAndGet();
	    }    	
    }
}
