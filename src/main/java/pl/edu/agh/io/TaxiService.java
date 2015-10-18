package pl.edu.agh.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import pl.edu.agh.io.pojo.CallType;
import pl.edu.agh.io.pojo.DayType;
import pl.edu.agh.io.pojo.Point;
import pl.edu.agh.io.pojo.Route;
import pl.edu.agh.io.pojo.Trip;


@SuppressWarnings("unused")
public class TaxiService {
	private static final Logger logger = Logger.getLogger(TaxiService.class);
	private static final String FILENAME = "src/main/resources/data.csv";
	
    public static void main( String[] args ) {
    	HibernateUtil.getSessionFactory();
    	
    	loadData(FILENAME);
    	HibernateUtil.getSessionFactory().close();
//    	splitRoute(null);
//    	logger.info("HELLO");
//       	HibernateUtil.getSessionFactory().close();
    }
    
    
    public static void loadData(String filename){    
    	Date startDate = new Date();
    	try {
    		Session session = HibernateUtil.getSessionFactory().openSession();
    		session.beginTransaction();
    		
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			
			br.readLine(); // skip header
			
			int lines = 0;
			String line;
			while ( (line = br.readLine()) != null){			
				lines += 1;
				
				String splited[] = line.split(",", 9);
				
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
	
				session.save(trip);
			}
			System.out.println("Samples: " + lines);
			fr.close();
			session.getTransaction().commit();
			session.close();
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
}
