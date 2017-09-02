package br.com.browseframework.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Converts between Date x String / String x Date
 * @author Eduardo
 *
 */
public class DateFormatterUtil {
	
	// Stores the instance given format
	private static final Map<String, SimpleDateFormat> mapSDF = new HashMap<String, SimpleDateFormat>();
	
	/**
	 * Converts the date with the given format.
	 * @param date
	 * @param format
	 * @return
	 */
	public static String convertDateToString(Date date, String format) {
		String result = null;
		if (date != null) {
			final SimpleDateFormat sdf = getSimpleDateFormat(format);
			// converts
			result = sdf.format(date);
		}
		return result;
	}

	/**
	 * 
	 * @param d
	 * @param formato
	 * @return
	 * @throws ParseException
	 */
	public static Date convertStringToDate(String d, String formato) throws ParseException {
		Date retorno = null;
		if (d != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(formato);
			retorno = sdf.parse(d);
		}
		return retorno;
	}
	
	/**
	 * Tries to obtain from the map stored SimpleDateFormat, if the given format doesn't exists creates one.
	 * @param format
	 * @return
	 */
	private static SimpleDateFormat getSimpleDateFormat(String format) {
		// Try to obtain from the map
		SimpleDateFormat sdf = getMapsdf().get(format);
		// If it doesn't exists creates a new and stores in the map
		if (sdf == null){
			sdf = new SimpleDateFormat(format);
			getMapsdf().put(format, sdf);
		}
		return sdf;
	}
	 
	// GETTERS && SETTERS

	private static Map<String, SimpleDateFormat> getMapsdf() {
		return mapSDF;
	}
	
	
}
