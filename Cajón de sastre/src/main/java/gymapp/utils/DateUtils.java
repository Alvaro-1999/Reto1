package gymapp.utils;

import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;

public class DateUtils {

    private static final DateFormat FORMATTER = 
        new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);

    // Convierte un String (ej. "2000-01-01") a Date
    public static Date stringToDate(String stringDate) throws ParseException {
        if (stringDate == null || stringDate.isEmpty()) return null;
        return FORMATTER.parse(stringDate);
    }

    // Convierte un Date a String (ej. "2000-01-01")
    public static String dateToString(Date date) {
        if (date == null) return null;
        return FORMATTER.format(date);
    }

    // Convierte un Date a epoch millis (long)
    public static Long dateToLong(Date date) {
        return (date == null) ? null : date.getTime();
    }

    // Convierte epoch millis (long) a Date
    public static Date longToDate(Long millis) {
        return (millis == null) ? null : new Date(millis);
    }

    // Convierte epoch millis (long) a String con el formato definido
    public static String longToString(Long millis) {
        if (millis == null) return null;
        return FORMATTER.format(new Date(millis));
    }

    // Convierte un String de epoch millis a Date
    public static Date stringMillisToDate(String millisString) {
        if (millisString == null || millisString.isEmpty()) return null;
        try {
            long millis = Long.parseLong(millisString);
            return new Date(millis);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
