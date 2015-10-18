package pl.edu.agh.io;

import org.apache.log4j.Logger;


public class TaxiService {
	private static final Logger logger = Logger.getLogger(TaxiService.class);
	
    public static void main( String[] args ) {
    	logger.info("HELLO");
       	HibernateUtil.getSessionFactory().close();
    }
}
