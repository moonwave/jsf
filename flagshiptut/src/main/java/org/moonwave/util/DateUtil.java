package org.moonwave.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static String DEFAULT_DISPLAY_FORMAT = "MM/dd/yyyy HH:mm:ss";
    public static String STORED_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String FILENAME_FORMAT1 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static String FILENAME_FORMAT2 = "yyyy-MM-dd-HH:mm:ss.SSSZ";
    public static String FILENAME_FORMAT3 = "yyyyMMdd_HHmmss_SSS_Z";
    public static String FILENAME_FORMAT4 = "yyyyMMdd_HHmmss_SSS";
    public static String UNIVERSAL_FORMAT = "yyyy-MM-dd HH:mm:ss a";
    public static String HHMM_AMPM_FORMAT = "hh:mm a";
    public static String US_DATE_FORMAT = "MM/dd/yyyy";
    public static String INTL_DATE_FORMAT = "yyyy-MM-dd";
    public static String MONTH_YEAR = "MMM yyyy";
    public static String MONTH_YEAR_SHORT = "MMM yy";
    public static SimpleDateFormat usDateFormatShort = new SimpleDateFormat(US_DATE_FORMAT);
    public static SimpleDateFormat usDateFormatLong = new SimpleDateFormat(DEFAULT_DISPLAY_FORMAT);

    public static java.sql.Timestamp getSqlTimestamp() {
        return new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
    }

    public static java.sql.Timestamp dateToSqlTimestamp(java.util.Date date) {
        return new java.sql.Timestamp(date.getTime());
    }

    public static java.util.Date sqlTimestampToDate(java.sql.Timestamp timestamp) {
        Date date = new Date(timestamp.getTime());
        return date;
    }

    public static java.util.Calendar dateToCalendar(java.util.Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static java.util.Date calendarToDate(java.util.Calendar calendar) {
        return calendar.getTime();
    }

    /**
     *  usDateFormatShort handles "11/08/2011" or "5/4/2011"
     *  usDateFormatLong  handles "05/04/2011 13:24:55" 
     *
     * @param dateString
     * @return
     */
    public static Timestamp toTimestamp(String dateString) {
        try 
        {
          Date date = null;
          if (dateString.length() <= 10)
              date = ((SimpleDateFormat)usDateFormatShort.clone()).parse(dateString);
          else
              date = ((SimpleDateFormat)usDateFormatLong.clone()).parse(dateString);
          return new Timestamp(date.getTime()); 
        } catch (java.text.ParseException e) {
          // error while converting a string to a timestamp
        }
        return null;
    }

    /**
     * Converts a <code>java.util.Date</code> to MM/dd/yyyy string.
     *
     * @param date date to convert.
     * @return converted string on success; empty string otherwise.
     */
    public static String toMMDDYYYY(Date date) {
        String sRet = "";
        if (date != null) {
            try {
                sRet = new SimpleDateFormat(US_DATE_FORMAT).format(date);
            } catch (Exception e) {
                // ignore
            }
        }
        return sRet;
    }

    /**
     * Convert to 'May 09' format.
     *
     * @param date
     * @return
     */
    public static String toMMMYY(Date date) {
        if(date != null) {
            return new SimpleDateFormat(MONTH_YEAR_SHORT).format(date);
        }
        return "N/A";
    }

    /**
     * Convert to 'May 2009' format.
     *
     * @param date
     * @return
     */
    public static String toMMMYYYY(Date date) {
        if(date != null) {
            return new SimpleDateFormat(MONTH_YEAR).format(date);
        }
        return "N/A";
    }

    /**
     * Converts a <code>java.util.Date</code> to yyyy-MM-dd string.
     *
     * @param date date to convert.
     * @return converted string on success; empty string otherwise.
     */
    public static String toYYYYMMDD(Date date) {
        String sRet = "";
        if (date != null) {
            try {
                sRet = new SimpleDateFormat(INTL_DATE_FORMAT).format(date);
            } catch (Exception e) {
                // ignore
            }
        }
        return sRet;
    }

    /**
     * Converts a <code>java.util.Date</code> to hh:mm AP|PM string.
     * For example, a date of 1920-01-01 13:00:00 will be converted to
     * 01:00 PM, not that it is not 1:00 PM
     *
     * @param date date to convert.
     * @return converted string on success; empty string otherwise.
     */
    public static String toHHMM_AMPM(Date date) {
        String sRet = "";
        if (date != null) {
            try {
                sRet = new SimpleDateFormat(HHMM_AMPM_FORMAT).format(date);
            } catch (Exception e) {
                // ignore;
            }
        }
        return sRet;
    }

    /**
     * Converts a <code>java.util.Date</code> to stored format.
     *
     * @param date date  to convert.
     * @return date string in stored format on success; empty string otherwise.
     */
    public static String toStoredFormat(Date date) {
        String sRet = "";
        if (date != null) {
            try {
                sRet = new SimpleDateFormat(STORED_FORMAT).format(date);
            } catch (Exception e) {
                // ignore
            }
        }
        return sRet;
    }

    /**
     * Converts a <code>java.util.Date</code> to stored format.
     *
     * @param date date  to convert.
     * @return date string in stored format on success; empty string otherwise.
     */
    public static String toFilenameFormat(Date date) {
        String sRet = "";
        if (date != null) {
            try {
                sRet = new SimpleDateFormat(FILENAME_FORMAT1).format(date);
            } catch (Exception e) {
                // ignore
            }
        }
        return sRet;
    }
    public static String toFilenameFormat2(Date date) {
        String sRet = "";
        if (date != null) {
            try {
                sRet = new SimpleDateFormat(FILENAME_FORMAT2).format(date);
            } catch (Exception e) {
                // ignore
            }
        }
        return sRet;
    }

    public static String toFilenameFormat3(Date date) {
        String sRet = "";
        if (date != null) {
            try {
                sRet = new SimpleDateFormat(FILENAME_FORMAT3).format(date);
            } catch (Exception e) {
                // ignore
            }
        }
        return sRet;
    }

    public static String toFilenameFormat4(Date date) {
        String sRet = "";
        if (date != null) {
            try {
                sRet = new SimpleDateFormat(FILENAME_FORMAT4).format(date);
            } catch (Exception e) {
                // ignore
            }
        }
        return sRet;
    }

    /**
     * Converts a <code>java.util.Date</code> to stored format.
     *
     * @param date date  to convert.
     * @return date string in stored format on success; empty string otherwise.
     */
    public static String toYYYYMMDD_HHMMSS_AMPM(Date date) {
        String sRet = "";
        if (date != null) {
            try {
                sRet = new SimpleDateFormat(UNIVERSAL_FORMAT).format(date);
            } catch (Exception e) {
                // ignore
            }
        }
        return sRet;
    }

    /**
     * Converts a date string to stored format.
     *
     * @param dateString date string to convert.
     * @return date string in stored format on success; empty string otherwise.
     */
    public static String toStoredFormat(String dateString) {
        if (dateString != null) {
            Date date = getDate(dateString);
            return toStoredFormat(date);
        }
        return "";
    }

    public static Date getToday() {
        Calendar calendar = Calendar.getInstance();
        clearHHMMSSmm(calendar);
        return calendar.getTime();
    }
    
    public static Calendar getTODAY() {
        return Calendar.getInstance();
    }

    public static Date getTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static Date toDayStart(Date date) {
        Calendar calendar = DateUtil.toCalendar(date);
        clearHHMMSSmm(calendar);
        return calendar.getTime();
    }

    public static Date toDayEnd(Date date) {
        Calendar calendar = DateUtil.toCalendar(date);
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date setHour(Date date, int hour) {
        Calendar calendar = DateUtil.toCalendar(date);
        calendar.set(Calendar.HOUR, hour);
        return calendar.getTime();
    }

    /**
     * Adds days or minus days from specified date.
     * 
     * @param date the date to be operated on
     * @param numOfDays number of days add, can be negative
     * @return
     */
    public static Date addDays(Date date, int numOfDays) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, numOfDays);
        return cal.getTime();
    }

    /**
     * Adds months or minus months from specified date.
     * 
     * @param date the date to be operated on
     * @param numOfMonths number of months to add, can be negative
     * @return
     */
    public static Date addMonths(Date date, int numOfMonths){
        Date dateRet = null;
        if (date != null) {
            Calendar c = toCalendar(date);
            c.add(Calendar.MONTH, numOfMonths);
            dateRet = c.getTime();
        }
        return dateRet;
    }

    public static Calendar toCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Date clearHHMMSSmm(Date date) {
        Calendar calendar = DateUtil.toCalendar(date);

        clearHHMMSSmm(calendar);

        return calendar.getTime();
    }

    public static Calendar clearHHMMSSmm(Calendar calendar) {
        calendar.clear(Calendar.HOUR);
        calendar.clear(Calendar.HOUR_OF_DAY);
        calendar.clear(Calendar.AM_PM);

        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        return calendar;
    }

    /**
     * Converts a <code>java.util.Date</code> to display format.
     *
     * @param date date  to convert.
     * @return date string in display format on success; empty string otherwise.
     */
    public static String toDisplayFormat(Date date) {
        if(date != null) {
            return new SimpleDateFormat(DEFAULT_DISPLAY_FORMAT).format(date);
        }
        return "";
    }

    /**
     * Converts a date string to display format.
     *
     * @param dateString date string to convert.
     * @return date string in display format on success; empty string otherwise.
     */
    public static String toDisplayFormat(String dateString) {
        if(dateString != null) {
            Date date = getDate(dateString);
            return toDisplayFormat(date);
        }
        return "";
    }

    /**
     * Converts a date string in stored format or default display format to a date.
     *
     * @param dateString date string in stored format: yyyy-MM-dd HH:mm:ss.
     * @return Converted date on success; null otherwise.
     */
    public static Date getDate(String dateString) {
        SimpleDateFormat fmt = null;
        Date date = null;
        if ((dateString != null) && (dateString.length() > 0)) {
            try {
                if (dateString.indexOf("/") >=0 )
                    fmt = new SimpleDateFormat(DateUtil.DEFAULT_DISPLAY_FORMAT);
                else
                    fmt = new SimpleDateFormat(DateUtil.STORED_FORMAT);
                date = fmt.parse(dateString);
            } catch (Exception e) {
            }
        }
        return date;
    }

    /**
     * Creates a date object based on specified year, month, day values.
     *
     * @param year 4-digits year
     * @param month 1-indexed month, for example, 7 for July
     * @param day day of the month
     * @return date created
     *
     * examples: createDate(2008, 5, 11) returns a date of May 11th, 2008
     *           createDate(2003, 7, 6)  returns a date of July 6th, 2003
     */
    public static Date createDate(int year, int month, int day){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        DateUtil.clearHHMMSSmm(cal);
        return cal.getTime();
    }

    /**
     * Changes an existing date object to a new date by specified year, month, and
     * day values. There are no changes for the hour, minute, and second values
     * for the argument date object.
     *
     * @param date date object to change
     * @param year 4-digits year
     * @param month 1-indexed month, for example, 7 for July
     * @param day day of the month
     * @return date object with a new date
     */
    public static Date changeDate(Date date, int year, int month, int day) {
        Calendar cal = toCalendar(date);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    /**
     * Gets the time from a time string. Valid time must be in HH:MM (AM|PM) format.
     * For example, 11:00 AM, 1:00 PM, 3:20 PM are valid time string
     *
     * The resulting date part is pre-set as 1970-01-01
     *
     * @param timeString time string to be parsed
     * @return
     */
    public static Date getTime(String timeString) {
        Date time;
        try {
            time = new SimpleDateFormat(HHMM_AMPM_FORMAT).parse(timeString);
        } catch (ParseException e) {
            throw new RuntimeException("Invalid format for time string '" + timeString +
                                       "', a valid time must be in HH:MM (AM|PM) format");
        }
//      time = changeDate(time, 2008, 1, 1);
        return time;
    }

    /**
     * Gets the time from a time string, or from the default time string if the
     * first one fails to convert. Valid time must be in HH:MM (AM|PM) format.
     * For example, 11:00 AM, 1:00 PM, 3:20 PM are valid time string
     *
     * The resulting date part is pre-set as 1970-01-01
     *
     * @param timeString time string to be parsed
     * @defaultTime default time string to be parsed
     * @return
     */
    public static Date getTime(String timeString, String defaultTime) {
        Date time;
        try {
            time = getTime(timeString);
        } catch (Exception e) {
            try {
                time = getTime(defaultTime);
            } catch (Exception e1) {
                throw new RuntimeException("Invalid format for time string '" + timeString +
                                       "', a valid time must be in HH:MM (AM|PM) format");
            }
        }
        return time;
    }

    /*
     * Replaces the time of the first date with the time from a second date
     *
     * @param date1
     * @param date2
     * @return
     */
    public static Date replaceTime(Date date1, Date date2) {
        Calendar cDate1 = Calendar.getInstance();
        Calendar cDate2 = Calendar.getInstance();

        cDate1.setTime(date1);
        cDate2.setTime(date2);

        cDate1.set(Calendar.HOUR,        cDate2.get(Calendar.HOUR));
        cDate1.set(Calendar.MINUTE,      cDate2.get(Calendar.MINUTE));
        cDate1.set(Calendar.SECOND,      cDate2.get(Calendar.SECOND));
        cDate1.set(Calendar.MILLISECOND, cDate2.get(Calendar.MILLISECOND));
        cDate1.set(Calendar.AM_PM,       cDate2.get(Calendar.AM_PM));

        return cDate1.getTime();
    }

    /**
     * Get the difference in days for day 2 - day 1
     * 
     * @param day1
     * @param day2
     * @return 
     */
    public static long dayDifference(Date day1, Date day2) {
        long day1MilliSec = day1.getTime();
        long day2MilliSec = day2.getTime();

        long diffDays = (day2MilliSec - day1MilliSec) / (24 * 60 * 60 * 1000);
        return diffDays;
    }
        
    public static void main(String[] vargs) {
        Timestamp ts = DateUtil.toTimestamp("07/21/2011");
        Date date = new Date();
        String ret = DateUtil.toFilenameFormat(date);
        String ret2 = DateUtil.toFilenameFormat2(date);
        String ret3 = DateUtil.toFilenameFormat3(date);
        String ret4 = DateUtil.toFilenameFormat4(date);
        
        ts = DateUtil.toTimestamp("03/02/2011");
        ts = DateUtil.toTimestamp("5/4/2011");
        ts = DateUtil.toTimestamp("05/04/2011 13:24:55");
        ts = DateUtil.toTimestamp("07/21/2011 11:02:31");
    }
}
