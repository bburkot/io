package pl.edu.agh.io.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;


public class DataUtil {
	private static final Logger logger = Logger.getLogger(DataUtil.class);
	private static SimpleDateFormat sdf = 
			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
	
	public static Date createDate(String date) {
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			logger.warn(e.getMessage(), e);
			return null;
		}
	}
}
