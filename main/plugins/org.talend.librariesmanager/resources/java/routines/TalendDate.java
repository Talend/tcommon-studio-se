// ============================================================================
//
// %GENERATED_LICENSE%
//
// ============================================================================
package routines;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import routines.system.FastDateParser;
import routines.system.LocaleProvider;
import routines.system.TalendTimestampWithTZ;

public class TalendDate {

    /**
     * get part of date. like YEAR, MONTH, HOUR, or DAY_OF_WEEK, WEEK_OF_MONTH, WEEK_OF_YEAR, TIMEZONE and so on
     *
     * @param partName which part to get.
     * @param date the date value.
     * @return the specified part value.
     *
     * {talendTypes} Integer
     *
     * {Category} TalendDate
     *
     * {param} string("DAY_OF_WEEK") partName : which part to get
     *
     * {param} date(TalendDate.parseDate("yyyy-MM-dd", "2010-12-26")) date : the date value
     *
     * {example} getPartOfDate("DAY_OF_WEEK", TalendDate.parseDate("yyyy-MM-dd", "2010-12-26")) #
     */
    public static int getPartOfDate(String partName, Date date) {

        if (partName == null || date == null) {
            return 0;
        }
        int ret = 0;
        String[] fieldsName = { "YEAR", "MONTH", "HOUR", "MINUTE", "SECOND", "DAY_OF_WEEK", "DAY_OF_MONTH", "DAY_OF_YEAR",
                "WEEK_OF_MONTH", "DAY_OF_WEEK_IN_MONTH", "WEEK_OF_YEAR", "TIMEZONE" };
        java.util.List<String> filedsList = java.util.Arrays.asList(fieldsName);
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        switch (filedsList.indexOf(partName)) {
        case 0:
            ret = c.get(Calendar.YEAR);
            break;
        case 1:
            ret = c.get(Calendar.MONTH);
            break;
        case 2:
            ret = c.get(Calendar.HOUR);
            break;
        case 3:
            ret = c.get(Calendar.MINUTE);
            break;
        case 4:
            ret = c.get(Calendar.SECOND);
            break;
        case 5:
            ret = c.get(Calendar.DAY_OF_WEEK);
            break;
        case 6:
            ret = c.get(Calendar.DAY_OF_MONTH);
            break;
        case 7:
            ret = c.get(Calendar.DAY_OF_YEAR);
            break;
        case 8:
            // the ordinal number of current week in a month (it means a 'week' may be not contain 7 days)
            ret = c.get(Calendar.WEEK_OF_MONTH);
            break;
        case 9:
            // 1-7 correspond to 1, 8-14 correspond to 2,...
            ret = c.get(Calendar.DAY_OF_WEEK_IN_MONTH);
            break;
        case 10:
            ret = c.get(Calendar.WEEK_OF_YEAR);
            break;
        case 11:
            ret = (c.get(Calendar.ZONE_OFFSET)) / (1000 * 60 * 60);
            break;
        default:
            break;

        }
        return ret;
    }

    /**
     * Formats a Date into a date/time string.
     *
     * @param pattern the pattern to format.
     * @param date the time value to be formatted into a time string.
     * @return the formatted time string.
     *
     * {talendTypes} String
     *
     * {Category} TalendDate
     *
     * {param} string("yyyy-MM-dd HH:mm:ss") pattern : the pattern to format
     *
     * {param} date(myDate) date : the time value to be formatted into a time string
     *
     * {example} formatDate("yyyy-MM-dd", new Date()) #
     */

    public synchronized static String formatDate(String pattern, java.util.Date date) {
        DateFormat format = FastDateParser.getInstance(pattern);
        if (date instanceof TalendTimestampWithTZ) {
            format.setTimeZone(((TalendTimestampWithTZ) date).getTimeZone());
        } else {
            format.setTimeZone(TimeZone.getDefault());
        }
        return format.format(date);
    }

    /**
     * Formats a Date into a date/time string under the UTC timezone.
     *
     * @param pattern the pattern to format.
     * @param date the time value to be formatted into a time string.
     * @return the formatted time string.
     *
     * {talendTypes} String
     *
     * {Category} TalendDate
     *
     * {param} string("yyyy-MM-dd HH:mm:ss") pattern : the pattern to format
     *
     * {param} date(myDate) date : the time value to be formatted into a time string
     *
     * {example} formatDate("yyyy-MM-dd", new Date()) #
     */

    public synchronized static String formatDateInUTC(String pattern, java.util.Date date) {
        DateFormat format = FastDateParser.getInstance(pattern);
        TimeZone originalTZ = format.getTimeZone();
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateStr = format.format(date);
        format.setTimeZone(originalTZ);
        return dateStr;
    }

    /**
     * test string value as a date (with right pattern)
     *
     * @param stringDate (A <code>String</code> whose beginning should be parsed)
     * @param pattern (the pattern to format, like: "yyyy-MM-dd HH:mm:ss")
     * @return the result wheather the stringDate is a date string that with a right pattern
     *
     * {talendTypes} Boolean
     *
     * {Category} TalendDate
     *
     * {param} String(mydate) stringDate : the date to judge
     *
     * {param} String("yyyy-MM-dd HH:mm:ss") pattern : the specified pattern
     *
     * {examples}
     *
     * ->> isDate("2008-11-24 12:15:25", "yyyy-MM-dd HH:mm:ss") return true
     *
     * ->> isDate("2008-11-24 12:15:25", "yyyy-MM-dd HH:mm") return false
     *
     * ->> isDate("2008-11-32 12:15:25", "yyyy-MM-dd HH:mm:ss") return false #
     */
    public static boolean isDate(String stringDate, String pattern) {

        if (stringDate == null) {
            return false;
        }
        if (pattern == null) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(pattern);
        java.util.Date testDate = null;

        try {
            testDate = sdf.parse(stringDate);
        } catch (ParseException e) {
            return false;
        }

        if (!sdf.format(testDate).equalsIgnoreCase(stringDate)) {
            return false;
        }

        return true;
    }

    /**
     * test string value as a date with right pattern. </br>examples: </br>TimeZone:+0100 </br>2011/03/27 01:00:00 begin
     * to carry out the daylight saving time. So parse dateString "20110327 021711" with TimeZone is wrong </br>
     * <code>isDate("20110327 021711", "yyyyMMdd HHmmss",false)</code> return <code>false</code>
     *
     * </br> <code>isDate("20110327 021711", "yyyyMMdd HHmmss",true)</code> return <code>true</code>
     *
     * </br> <code>isDate("2008-11-32 12:15:25", "yyyy-MM-dd HH:mm:ss",true)</code> return <code>false</code>
     *
     * </br> <code>isDate("2008-11-32 12:15:25", "yyyy-MM-dd HH:mm:ss",false)</code> return <code>false</code>
     *
     * @param stringDate (A <code>String</code> whose beginning should be parsed)
     * @param pattern (the pattern to format, like: "yyyy-MM-dd HH:mm:ss")
     * @param ignoreTimeZone (if true ignore TimeZone when pare date with pattern)
     * @return the result whether the stringDate is a date string that with a right pattern
     *
     * {talendTypes} Boolean
     *
     * {Category} TalendDate
     *
     * {param} String(mydate) stringDate : the date to judge
     *
     * {param} String("yyyy-MM-dd HH:mm:ss") pattern : the specified pattern
     *
     * {param} boolean(true) ignoreTimeZone : ignore the time zone
     */
    public static boolean isDate(String stringDate, String pattern, boolean ignoreTimeZone) {
        TimeZone tz = TimeZone.getDefault();
        if (ignoreTimeZone) {
            tz = TimeZone.getTimeZone("UTC");
        }

        if (stringDate == null) {
            return false;
        }
        if (pattern == null) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(pattern);
        sdf.setTimeZone(tz);
        sdf.setLenient(false);

        java.util.Date testDate = null;

        try {
            testDate = sdf.parse(stringDate);
        } catch (ParseException e) {
            return false;
        }

        String formatDate = sdf.format(testDate);
        if (formatDate.equalsIgnoreCase(stringDate) || formatDate.length() == stringDate.length()) {
            return true;
        }

        return false;
    }

    /**
     * compare two date
     *
     * @param date1 (first date)
     * @param date2 (second date)
     * @param pattern (compare specified part, example: "yyyy-MM-dd")
     * @return the result wheather two date is the same, if first one less than second one return number -1, equlas
     * return number 0, bigger than return number 1. (can compare partly)
     *
     * {talendTypes} Integer
     *
     * {Category} TalendDate
     *
     * {param} date(myDate) date1 : the first date to compare
     *
     * {param} date(myDate2) date2 : the second date to compare
     *
     * {param} String("yyyy-MM-dd") pattern : compare specified part
     *
     * {examples}
     *
     * ->> compareDate(2008/11/24 12:15:25, 2008/11/24 16:10:35) return -1
     *
     * ->> compareDate(2008/11/24 16:10:35, 2008/11/24 12:15:25) return 1
     *
     * ->> compareDate(2008/11/24 12:15:25, 2008/11/24 16:10:35,"yyyy/MM/dd") return 0 #
     */
    public static int compareDate(Date date1, Date date2, String pattern) {
        if (date1 == null && date2 == null) {
            return 0;
        } else if (date1 != null && date2 == null) {
            return 1;
        } else if (date1 == null && date2 != null) {
            return -1;
        }

        if (pattern != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            String part1 = sdf.format(date1), part2 = sdf.format(date2);
            return (part1.compareTo(part2) >= 1 ? 1 : (part1.compareTo(part2) <= -1 ? -1 : 0));
        } else {
            long time1 = date1.getTime(), time2 = date2.getTime();
            return (time1 < time2 ? -1 : (time1 == time2 ? 0 : 1));
        }
    }

    /**
     * compare two date
     *
     * @param date1 (first date)
     * @param date2 (second date)
     * @return the result wheather two date is the same, if first one less than second one return number -1, equlas
     * return number 0, bigger than return number 1. (can compare partly)
     *
     * {talendTypes} Integer
     *
     * {Category} TalendDate
     *
     * {param} date(myDate) date1 : the first date to compare
     *
     * {param} date(myDate2) date2 : the second date to compare
     *
     * {example} compareDate(2008/11/24 12:15:25, 2008/11/24 16:10:35) return -1 #
     *
     */
    public static int compareDate(Date date1, Date date2) {
        return compareDate(date1, date2, null);
    }

    /**
     * add number of day, month ... to a date (with Java date type !)
     *
     * @param date (a <code>Date</code> type value)
     * @param nb (the value to add)
     * @param dateType (date pattern = ("yyyy","MM","dd","HH","mm","ss","SSS" ))
     * @return a new date
     *
     * {talendTypes} Date
     *
     * {Category} TalendDate
     *
     * {param} date(myDate) date : the date to update
     *
     * {param} int(addValue) nb : the added value
     *
     * {param} date("MM") dateType : the part to add
     *
     * {examples}
     *
     * ->> addDate(dateVariable), 5,"dd") return a date with 2008/11/29 12:15:25 (with dateVariable is a date with
     * 2008/11/24 12:15:25) #
     *
     * ->> addDate(2008/11/24 12:15:25, 5,"ss") return 2008/11/24 12:15:30 #
     *
     */
    public static Date addDate(Date date, int nb, String dateType) {
        if (date == null || dateType == null) {
            return null;
        }

        Calendar c1 = Calendar.getInstance();
        c1.setTime(date);

        if (dateType.equalsIgnoreCase("yyyy")) { //$NON-NLS-1$
            c1.add(Calendar.YEAR, nb);
        } else if (dateType.equals("MM")) { //$NON-NLS-1$
            c1.add(Calendar.MONTH, nb);
        } else if (dateType.equalsIgnoreCase("dd")) { //$NON-NLS-1$
            c1.add(Calendar.DAY_OF_MONTH, nb);
        } else if (dateType.equals("HH")) { //$NON-NLS-1$
            c1.add(Calendar.HOUR, nb);
        } else if (dateType.equals("mm")) { //$NON-NLS-1$
            c1.add(Calendar.MINUTE, nb);
        } else if (dateType.equalsIgnoreCase("ss")) { //$NON-NLS-1$
            c1.add(Calendar.SECOND, nb);
        } else if (dateType.equalsIgnoreCase("SSS")) { //$NON-NLS-1$
            c1.add(Calendar.MILLISECOND, nb);
        } else {
            throw new RuntimeException("Can't support the dateType: " + dateType);
        }

        return c1.getTime();
    }

    /**
     * add number of day, month ... to a date (with Date given in String with a pattern)
     *
     * @param date (a Date given in string)
     * @param pattern (the pattern for the related date)
     * @param nb (the value to add)
     * @param dateType (date pattern = ("yyyy","MM","dd","HH","mm","ss","SSS" ))
     * @return a new date
     *
     * {talendTypes} String
     *
     * {Category} TalendDate
     *
     * {param} String("") string : date represent in string
     *
     * {param} String("yyyy-MM-dd") pattern : date pattern
     *
     * {param} int(addValue) nb : the added value
     *
     * {param} date("MM") dateType : the part to add
     *
     * {examples}
     *
     * ->> addDate("2008/11/24 12:15:25", "yyyy-MM-dd HH:mm:ss", 5,"dd") return "2008/11/29 12:15:25"
     *
     * ->> addDate("2008/11/24 12:15:25", "yyyy/MM/DD HH:MM:SS", 5,"ss") return "2008/11/24 12:15:30" #
     *
     */
    public static String addDate(String string, String pattern, int nb, String dateType) {
        if (string == null || dateType == null) {
            return null;
        }
        java.util.Date date = null;

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(pattern);
        try {
            date = sdf.parse(string);
        } catch (ParseException e) {
            throw new RuntimeException(pattern + " can't support the date!"); //$NON-NLS-1$
        }
        String dateString = sdf.format(addDate(date, nb, dateType));

        return dateString;
    }

    /**
     * return difference between two dates
     *
     * @param Date1 ( first date )
     * @param Date1 ( second date )
     * @param dateType value=("yyyy","MM","dd","HH","mm","ss","SSS") for type of return
     * @return a number of years, months, days ... date1 - date2
     *
     * {talendTypes} Long
     *
     * {Category} TalendDate
     *
     * {param} date(myDate) date1 : the first date to compare
     *
     * {param} date(myDate2) date2 : the second date to compare
     *
     * {param} String("MM") dateType : the difference on the specified part
     *
     * {examples}
     *
     * ->> diffDate(2008/11/24 12:15:25, 2008/10/14 16:10:35, "yyyy") : return 0
     *
     * ->> diffDate(2008/11/24 12:15:25, 2008/10/14 16:10:35, "MM") : return 1
     *
     * ->> diffDate(2008/11/24 12:15:25, 2008/10/14 16:10:35, "dd") : return 41 #
     */
    public static long diffDate(Date date1, Date date2, String dateType) {
        return diffDate(date1, date2, dateType, false);
    }

    /**
     * return difference between two dates
     *
     * @param Date1 ( first date )
     * @param Date1 ( second date )
     * @param dateType value=("yyyy","MM","dd","HH","mm","ss","SSS") for type of return
     * @param ignoreDST
     * @return a number of years, months, days ... date1 - date2
     *
     * {talendTypes} Long
     *
     * {Category} TalendDate
     *
     * {param} date(myDate) date1 : the first date to compare
     *
     * {param} date(myDate2) date2 : the second date to compare
     *
     * {param} String("MM") dateType : the difference on the specified part
     *
     * {param} boolean(true) ignoreDST : ignore daylight saving time or not.
     *
     * {examples}
     *
     * ->> diffDate(2012/03/26 00:00:00, 2012/03/24 00:00:00, "dd", true) : return 2 not 1 in GMT+1#
     *
     * ->> diffDate(2012/03/26 00:00:00, 2012/03/24 00:00:00, "dd", false) : return 1 not 2 in GMT+1#
     */
    public static long diffDate(Date date1, Date date2, String dateType, boolean ignoreDST) {

        if (date1 == null) {
            date1 = new Date(0);
        }
        if (date2 == null) {
            date2 = new Date(0);
        }

        if (dateType == null) {
            dateType = "SSS";
        }

        // ignore DST
        int addDSTSavings = 0;
        if (ignoreDST) {
            boolean d1In = TimeZone.getDefault().inDaylightTime(date1);
            boolean d2In = TimeZone.getDefault().inDaylightTime(date2);
            if (d1In != d2In) {
                if (d1In) {
                    addDSTSavings = TimeZone.getDefault().getDSTSavings();
                } else if (d2In) {
                    addDSTSavings = -TimeZone.getDefault().getDSTSavings();
                }
            }
        }

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);

        if (dateType.equalsIgnoreCase("yyyy")) { //$NON-NLS-1$
            return c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
        } else if (dateType.equals("MM")) { //$NON-NLS-1$
            return (c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR)) * 12 + (c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH));
        } else {
            long diffTime = date1.getTime() - date2.getTime() + addDSTSavings;

            if (dateType.equalsIgnoreCase("HH")) { //$NON-NLS-1$
                return diffTime / (1000 * 60 * 60);
            } else if (dateType.equals("mm")) { //$NON-NLS-1$
                return diffTime / (1000 * 60);
            } else if (dateType.equalsIgnoreCase("ss")) { //$NON-NLS-1$
                return diffTime / 1000;
            } else if (dateType.equalsIgnoreCase("SSS")) { //$NON-NLS-1$
                return diffTime;
            } else if (dateType.equalsIgnoreCase("dd")) {
                return diffTime / (1000 * 60 * 60 * 24);
            } else {
                throw new RuntimeException("Can't support the dateType: " + dateType);
            }
        }
    }

    /**
     * return difference between two dates ignore DST
     *
     * @param Date1 ( first date )
     * @param Date1 ( second date )
     * @param dateType value=("yyyy","MM","dd","HH","mm","ss","SSS") for type of return
     * @return a number of years, months, days ... date1 - date2
     *
     * {talendTypes} Long
     *
     * {Category} TalendDate
     *
     * {param} date(myDate) date1 : the first date to compare
     *
     * {param} date(myDate2) date2 : the second date to compare
     *
     * {param} String("MM") dateType : the difference on the specified part
     *
     * {examples}
     *
     * ->> diffDate(2012/03/26 00:00:00, 2012/03/24 00:00:00, "dd") : return 2 not 1 in GMT+1#
     */
    public static long diffDateIgnoreDST(Date date1, Date date2, String dateType) {
        return diffDate(date1, date2, dateType, true);
    }

    /**
     * return difference between two dates ignore DST
     *
     * @param Date1 ( first date )
     * @param Date1 ( second date )
     * @return a number of years, months, days ... date1 - date2
     *
     * {talendTypes} Long
     *
     * {Category} TalendDate
     *
     * {param} date(myDate) date1 : the first date to compare
     *
     * {param} date(myDate2) date2 : the second date to compare
     *
     * {examples}
     *
     * ->> diffDate(2012/03/26 00:00:00, 2012/03/24 00:00:00) : return 2 not 1 in GMT+1#
     */
    public static long diffDateIgnoreDST(Date date1, Date date2) {
        return diffDateIgnoreDST(date1, date2, "dd");
    }

    /**
     * return difference between two dates by floor
     *
     * @param Date1 ( first date )
     * @param Date1 ( second date )
     * @param dateType value=("yyyy","MM") for type of return
     * @return a number of years, months (date1 - date2)
     *
     * {talendTypes} Integer
     *
     * {Category} TalendDate
     *
     * {param} date(myDate) date1 : the first date to compare
     *
     * {param} date(myDate2) date2 : the second date to compare
     *
     * {param} String("MM") dateType : the difference on the specified part
     *
     * {examples}
     *
     * ->> diffDate(2009/05/10, 2008/10/15, "yyyy") : return 0
     *
     * ->> diffDate(2009/05/10, 2008/10/15, "MM") : return 6
     */
    public static int diffDateFloor(Date date1, Date date2, String dateType) {
        if (date1 == null) {
            date1 = new Date(0);
        }
        if (date2 == null) {
            date2 = new Date(0);
        }

        if (dateType == null) {
            dateType = "yyyy";
        }

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);

        int result = 0;
        Calendar tmp = null;
        boolean flag = false;
        if (c1.compareTo(c2) < 0) {
            flag = true;
            tmp = c1;
            c1 = c2;
            c2 = tmp;
        }
        result = (c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR)) * 12 + (c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH));
        c2.add(Calendar.MONTH, result);
        result += c2.after(c1) ? -1 : 0;
        if (flag) {
            result = -result;
        }

        if (dateType.equalsIgnoreCase("yyyy")) {
            return result / 12;
        } else if (dateType.equals("MM")) {
            return result;
        } else {
            throw new RuntimeException("Can't support the dateType: " + dateType + " ,please try \"yyyy\" or \"MM\"");
        }
    }

    /**
     * return difference between two dates
     *
     * @param Date1 ( first date )
     * @param Date1 ( second date )
     * @return a number of years, months, days ... date1 - date2
     *
     * {talendTypes} Long
     *
     * {Category} TalendDate
     *
     * {param} date(myDate) date1 : the first date to compare
     *
     * {param} date(myDate) date2 : the second date to compare
     *
     * {examples} diffDate(2008/11/24 12:15:25, 2008/10/14 16:10:35) : return 41 #
     */

    public static long diffDate(Date date1, Date date2) {
        return diffDate(date1, date2, "dd");
    }

    /**
     * get first day of the month
     *
     * @param date (a date value)
     * @return a new date (the date has been changed to the first day)
     *
     * {talendTypes} Date
     *
     * {Category} TalendDate
     *
     * {param} date(mydate) date : the date to get first date of current month
     *
     * {example} getFirstDayMonth(2008/02/24 12:15:25) return 2008/02/01 12:15:25 #
     */
    public static Date getFirstDayOfMonth(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DATE, 1);
        return c.getTime();
    }

    /**
     * get last day of the month
     *
     * @param date (a date value)
     * @return a new date (the date has been changed to the last day)
     *
     * {talendTypes} Date
     *
     * {Category} TalendDate
     *
     * {param} date(mydate) date : the date to get last date of current month
     *
     * {example} getFirstDayMonth(2008/02/24 12:15:25) return 2008/02/28 12:15:25
     */
    public static Date getLastDayOfMonth(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        c.set(Calendar.DATE, lastDay);
        return c.getTime();
    }

    /**
     *
     * set a date new value partly
     *
     * @param date (a date value)
     * @param nb (new number)
     * @param dateType (the part)
     * @return a new date
     *
     * {talendTypes} Date
     *
     * {Category} TalendDate
     *
     * {param} date(mydate) date : the date to set
     *
     * {param} Integer(newValue) nb : the new value
     *
     * {param} String("MM") dateType : the part to set
     *
     * {examples}
     *
     * ->> setDate(2008/11/24 12:15:25, 2010, "yyyy") return 2010/11/24 12:15:25
     *
     * ->> setDate(2008/11/24 12:15:25, 01, "MM") return 2008/01/24 12:15:25
     *
     * ->> setDate(2008/11/24 12:15:25, 15, "dd") return 2008/11/15 12:15:25 #
     */
    public static Date setDate(Date date, int nb, String dateType) {
        if (date == null || dateType == null) {
            return null;
        }

        // if (nb < 0) {
        // return date;
        // }

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        if (dateType.equalsIgnoreCase("yyyy")) { //$NON-NLS-1$
            c.set(Calendar.YEAR, nb);
        } else if (dateType.equals("MM")) { //$NON-NLS-1$
            c.set(Calendar.MONTH, nb - 1);
        } else if (dateType.equalsIgnoreCase("dd")) { //$NON-NLS-1$
            c.set(Calendar.DATE, nb);
        } else if (dateType.equalsIgnoreCase("HH")) { //$NON-NLS-1$
            c.set(Calendar.HOUR_OF_DAY, nb);
        } else if (dateType.equals("mm")) { //$NON-NLS-1$
            c.set(Calendar.MINUTE, nb);
        } else {
            throw new RuntimeException("Can't support the dateType: " + dateType);
        }
        return c.getTime();
    }

    /**
     * Formats a Date into a date/time string using the given pattern and the default date format symbols for the given
     * locale.
     *
     * @param pattern the pattern to format.
     * @param date the time value to be formatted into a time string.
     * @param locale the locale whose date format symbols should be used.
     * @return the formatted time string.
     *
     * {talendTypes} String
     *
     * {Category} TalendDate
     *
     * {param} string("yyyy-MM-dd HH:mm:ss") pattern : the pattern to format
     *
     * {param} date(myDate) date : the time value to be formatted into a time string
     *
     * {param} string("EN") languageOrCountyCode : the language or country whose date format symbols should be used, in
     * lower or upper case
     *
     * {example} formatDateLocale("yyyy-MM-dd", new Date(), "en") #
     */
    public synchronized static String formatDateLocale(String pattern, java.util.Date date, String languageOrCountyCode) {
        return FastDateParser.getInstance(pattern, LocaleProvider.getLocale(languageOrCountyCode)).format(date);
    }

    /**
     * Parses text from the beginning of the given string to produce a date using the given pattern and the default date
     * format symbols for the given locale. The method may not use the entire text of the given string.
     * <p>
     *
     * @param pattern the pattern to parse.
     * @param stringDate A <code>String</code> whose beginning should be parsed.
     * @return A <code>Date</code> parsed from the string.
     * @throws ParseException
     * @exception ParseException if the beginning of the specified string cannot be parsed.
     *
     * {talendTypes} Date
     *
     * {Category} TalendDate
     *
     * {param} string("yyyy-MM-dd HH:mm:ss") pattern : the pattern to parse
     *
     * {param} string("") stringDate : A <code>String</code> whose beginning should be parsed
     *
     * {example} parseDate("yyyy-MMM-dd HH:mm:ss", "23-Mar-1979 23:59:59") #
     */
    public synchronized static Date parseDate(String pattern, String stringDate) {
        return parseDate(pattern, stringDate, true);
    }

    /**
     * Parses text from the beginning of the given string to produce a date using the given pattern and the default date
     * format symbols for the given locale. The method may not use the entire text of the given string.
     * <p>
     *
     * @param pattern the pattern to parse.
     * @param stringDate A <code>String</code> whose beginning should be parsed.
     * @param isLenient A <code>boolean</code>judge DateFormat parse the date Lenient or not.
     * @return A <code>Date</code> parsed from the string.
     * @throws ParseException
     * @exception ParseException if the beginning of the specified string cannot be parsed.
     *
     * {talendTypes} Date
     *
     * {Category} TalendDate
     *
     * {param} string("yyyy-MM-dd HH:mm:ss") pattern : the pattern to parse
     *
     * {param} string("") stringDate : A <code>String</code> whose beginning should be parsed
     *
     * {param} boolean(true) isLenient : Judge DateFormat parse the date Lenient or not.
     *
     * {example} parseDate("yyyy-MM-dd HH:mm:ss", "29-02-1979 23:59:59",false) #
     */
    public synchronized static Date parseDate(String pattern, String stringDate, boolean isLenient) {
        try {
            boolean hasZone = false;
            boolean inQuote = false;
            char[] ps = pattern.toCharArray();
            for (char p : ps) {
                if (p == '\'') {
                    inQuote = !inQuote;
                } else if (!inQuote && (p == 'Z' || p == 'z')) {
                    hasZone = true;
                    break;
                }
            }
            DateFormat df = FastDateParser.getInstance(pattern);
            df.setLenient(isLenient);
            Date d = df.parse(stringDate);
            if (hasZone) {
                int offset = df.getCalendar().get(Calendar.ZONE_OFFSET);
                char sign = offset >= 0 ? '+' : '-';
                int hour = Math.abs(offset) / 1000 / 60 / 60;
                int min = Math.abs(offset) / 1000 / 60 % 60;
                String minStr = min < 10 ? "0" + min : min + "";
                TalendTimestampWithTZ tstz = new TalendTimestampWithTZ(new java.sql.Timestamp(d.getTime()),
                        TimeZone.getTimeZone("GMT" + sign + hour + ":" + minStr));
                return tstz;
            } else {
                return d;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parses text from the beginning of the given string to produce a date using the given pattern and the default date
     * format symbols for UTC. The method may not use the entire text of the given string.
     * <p>
     *
     * @param pattern the pattern to parse.
     * @param stringDate A <code>String</code> whose beginning should be parsed.
     * @return A <code>Date</code> parsed from the string.
     * @throws ParseException
     * @exception ParseException if the beginning of the specified string cannot be parsed.
     *
     * {talendTypes} Date
     *
     * {Category} TalendDate
     *
     * {param} string("yyyy-MM-dd HH:mm:ss") pattern : the pattern to parse
     *
     * {param} string("") stringDate : A <code>String</code> whose beginning should be parsed
     *
     * {example} parseDate("yyyy-MMM-dd HH:mm:ss", "23-Mar-1979 23:59:59") #
     */
    public synchronized static Date parseDateInUTC(String pattern, String stringDate) {
        return parseDateInUTC(pattern, stringDate, true);
    }

    /**
     * Parses text from the beginning of the given string to produce a date in UTC using the given pattern and the
     * default date format symbols for the UTC. The method may not use the entire text of the given string.
     * <p>
     *
     * @param pattern the pattern to parse.
     * @param stringDate A <code>String</code> whose beginning should be parsed.
     * @param isLenient A <code>boolean</code>judge DateFormat parse the date Lenient or not.
     * @return A <code>Date</code> parsed from the string.
     * @throws ParseException
     * @exception ParseException if the beginning of the specified string cannot be parsed.
     *
     * {talendTypes} Date
     *
     * {Category} TalendDate
     *
     * {param} string("yyyy-MM-dd HH:mm:ss") pattern : the pattern to parse
     *
     * {param} string("") stringDate : A <code>String</code> whose beginning should be parsed
     *
     * {param} boolean(true) isLenient : Judge DateFormat parse the date Lenient or not.
     *
     * {example} parseDate("yyyy-MM-dd HH:mm:ss", "29-02-1979 23:59:59",false) #
     */
    public synchronized static Date parseDateInUTC(String pattern, String stringDate, boolean isLenient) {
        try {
            boolean hasZone = false;
            boolean inQuote = false;
            char[] ps = pattern.toCharArray();
            for (char p : ps) {
                if (p == '\'') {
                    inQuote = !inQuote;
                } else if (!inQuote && (p == 'Z' || p == 'z')) {
                    hasZone = true;
                    break;
                }
            }
            DateFormat df = FastDateParser.getInstance(pattern);
            TimeZone originalTZ = df.getTimeZone();
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            df.setLenient(isLenient);
            Date d = df.parse(stringDate);
            df.setTimeZone(originalTZ);
            if (hasZone) {
                int offset = df.getCalendar().get(Calendar.ZONE_OFFSET);
                char sign = offset >= 0 ? '+' : '-';
                int hour = Math.abs(offset) / 1000 / 60 / 60;
                int min = Math.abs(offset) / 1000 / 60 % 60;
                String minStr = min < 10 ? "0" + min : min + "";
                TalendTimestampWithTZ tstz = new TalendTimestampWithTZ(new java.sql.Timestamp(d.getTime()),
                        TimeZone.getTimeZone("GMT" + sign + hour + ":" + minStr));
                return tstz;
            } else {
                return d;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parses text from the beginning of the given string to produce a date. The method may not use the entire text of
     * the given string.
     * <p>
     *
     * @param pattern the pattern to parse.
     * @param stringDate A <code>String</code> whose beginning should be parsed.
     * @param locale the locale whose date format symbols should be used.
     * @return A <code>Date</code> parsed from the string.
     * @throws ParseException
     * @exception ParseException if the beginning of the specified string cannot be parsed.
     *
     * {talendTypes} Date
     *
     * {Category} TalendDate
     *
     * {param} string("yyyy-MM-dd HH:mm:ss") pattern : the pattern to parse
     *
     * {param} string("") stringDate : A <code>String</code> whose beginning should be parsed
     *
     * {param} string("EN") languageOrCountyCode : the language or country whose date format symbols should be used, in
     * lower or upper case
     *
     * {example} parseDateLocale("yyyy-MMM-dd", "23-Mar-1979", "en") #
     */
    public synchronized static Date parseDateLocale(String pattern, String stringDate, String languageOrCountyCode) {
        try {
            return FastDateParser.getInstance(pattern, LocaleProvider.getLocale(languageOrCountyCode)).parse(stringDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * getDate : return the current datetime with the given display format format : (optional) string representing the
     * wished format of the date. This string contains fixed strings and variables related to the date. By default, the
     * format string is DD/MM/CCYY. Here is the list of date variables:
     *
     *
     * {talendTypes} String
     *
     * {Category} TalendDate
     *
     * {param} string("CCYY-MM-DD hh:mm:ss") pattern : date pattern + CC for century + YY for year + MM for month + DD
     * for day + hh for hour + mm for minute + ss for second
     *
     * {example} getDate("CCYY-MM-DD hh:mm:ss") #
     */
    public static String getDate(String pattern) {
        if (pattern == null) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }

        StringBuffer result = new StringBuffer();

        pattern = pattern.replace("CC", "yy"); //$NON-NLS-1$ //$NON-NLS-2$
        pattern = pattern.replace("YY", "yy"); //$NON-NLS-1$ //$NON-NLS-2$
        pattern = pattern.replace("MM", "MM"); //$NON-NLS-1$ //$NON-NLS-2$
        pattern = pattern.replace("DD", "dd"); //$NON-NLS-1$ //$NON-NLS-2$
        pattern = pattern.replace("hh", "HH"); //$NON-NLS-1$ //$NON-NLS-2$

        // not needed
        // pattern.replace("mm", "mm");
        // pattern.replace("ss", "ss");

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.format(Calendar.getInstance().getTime(), result, new FieldPosition(0));
        return result.toString();
    }

    /**
     * getDate : return the current date
     *
     *
     *
     * {talendTypes} Date
     *
     * {Category} TalendDate
     *
     * {example} getCurrentDate()
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * return an ISO formatted random date
     *
     *
     * {talendTypes} Date
     *
     * {Category} TalendDate
     *
     * {param} string("2007-01-01") min : minimum date
     *
     * {param} string("2008-12-31") max : maximum date (superior to min)
     *
     * {example} getRandomDate("1981-01-18", "2005-07-24") {example} getRandomDate("1980-12-08", "2007-02-26")
     */
    public static Date getRandomDate(String minDate, String maxDate) {
        if (minDate == null) {
            minDate = "1970-01-01";
        }

        if (maxDate == null) {
            maxDate = "2099-12-31";
        }

        if (!minDate.matches("\\d{4}-\\d{2}-\\d{2}") || !minDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("The parameter should be \"yyy-MM-dd\"");
        }

        int minYear = Integer.parseInt(minDate.substring(0, 4));
        int minMonth = Integer.parseInt(minDate.substring(5, 7));
        int minDay = Integer.parseInt(minDate.substring(8, 10));

        int maxYear = Integer.parseInt(maxDate.substring(0, 4));
        int maxMonth = Integer.parseInt(maxDate.substring(5, 7));
        int maxDay = Integer.parseInt(maxDate.substring(8, 10));

        Calendar minCal = Calendar.getInstance();
        minCal.set(Calendar.YEAR, minYear);
        minCal.set(Calendar.MONTH, minMonth - 1);
        minCal.set(Calendar.DAY_OF_MONTH, minDay);

        Calendar maxCal = Calendar.getInstance();
        maxCal.set(Calendar.YEAR, maxYear);
        maxCal.set(Calendar.MONTH, maxMonth - 1);
        maxCal.set(Calendar.DAY_OF_MONTH, maxDay);

        long random = minCal.getTimeInMillis()
                + (long) ((maxCal.getTimeInMillis() - minCal.getTimeInMillis() + 1) * Math.random());
        return new Date(random);
    }

    /**
     *
     * Testcase:
     * <p>
     * getRandomDate(String minDate, String maxDate)
     * </p>
     */
    public static void test_getRandomDate() {
        System.out
                .println("getRandomDate: " + TalendDate.formatDate("yyyy-MM-dd HH:mm:ss", TalendDate.getRandomDate(null, null))); //$NON-NLS-1$
    }

    /**
     *
     * Testcase:
     * <p>
     * compareDate(Date date1, Date date2)
     * </p>
     */
    public static void test_compareDate() {
        System.out
                .println("compareDate: " + Boolean.toString(TalendDate.compareDate(new Date(), new Date(System.currentTimeMillis() - 10000)) == 1)); //$NON-NLS-1$
    }

    /**
     *
     * Testcase:
     * <p>
     * isDate(String stringDate, String pattern)
     * </p>
     */
    public static void test_isDate() {
        System.out.println("isDate: " + Boolean.toString(TalendDate.isDate("2008-11-35 12:15:25", "yyyy-MM-dd HH:mm") == false)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    /**
     * format date to mssql 2008 type datetimeoffset ISO 8601 string with local time zone format string : yyyy-MM-dd
     * HH:mm:ss.SSSXXX(JDK7 support it)
     */
    public static String formatDatetimeoffset(Date date) {
        String dateString = formatDate("yyyy-MM-dd HH:mm:ss.SSSZ", date);// keep the max precision in java
        StringBuilder sb = new StringBuilder(30);
        sb.append(dateString);
        sb.insert(dateString.length() - 2, ':');
        return sb.toString();
    }

    /**
     *
     * Testcase:
     * <p>
     * formatDate(String pattern, java.util.Date date)
     * </p>
     * <p>
     * formatDateLocale(String pattern, java.util.Date date, String languageOrCountyCode)
     * </p>
     */
    public static void test_formatDate() {
        final int LOOPS = 100000;
        final String dateTimeRef_Test1 = "1979-03-23 mars 12:30";
        Thread test1 = new Thread() {

            @Override
            public void run() {
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.set(1979, 2, 23, 12, 30, 40);
                Date dateCalendar = calendar.getTime();
                for (int i = 0; i < LOOPS; i++) {
                    String date = TalendDate.formatDate("yyyy-MM-dd MMM HH:mm", dateCalendar);
                    // System.out.println("Test1:" + date + " # " + dateTimeRef_Test1);
                    if (!dateTimeRef_Test1.equals(date)) {
                        throw new IllegalStateException("Test1: Date ref : '" + dateTimeRef_Test1 + "' is different of '" + date
                                + "'");
                    }
                }
                System.out.println("test1 ok");
            }
        };
        final String dateTimeRef_Test2 = "1980-03-23 mars 12:30";
        Thread test2 = new Thread() {

            @Override
            public void run() {
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.set(1980, 2, 23, 12, 30, 40);
                Date dateCalendar = calendar.getTime();
                for (int i = 0; i < LOOPS; i++) {
                    String date = TalendDate.formatDate("yyyy-MM-dd MMM HH:mm", dateCalendar);
                    // System.out.println("Test2:" + date + " # " + dateTimeRef_Test2);
                    if (!dateTimeRef_Test2.equals(date)) {
                        throw new IllegalStateException("Test2: Date ref : '" + dateTimeRef_Test2 + "' is different of '" + date
                                + "'");
                    }
                }
                System.out.println("test2 ok");
            }
        };

        final String dateTimeRef_Test3 = "1979-03-23 mars 12:30";
        Thread test3 = new Thread() {

            @Override
            public void run() {
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.set(1979, 2, 23, 12, 30, 40);
                Date dateCalendar = calendar.getTime();
                for (int i = 0; i < LOOPS; i++) {
                    String date = TalendDate.formatDateLocale("yyyy-MM-dd MMM HH:mm", dateCalendar, "FR");
                    // System.out.println("Test3:" + date + " # " + dateTimeRef_Test3);
                    if (!dateTimeRef_Test3.equals(date)) {
                        throw new IllegalStateException("Test3: Date ref : '" + dateTimeRef_Test3 + "' is different of '" + date
                                + "'");
                    }
                }
                System.out.println("test3 ok");
            }
        };
        final String dateTimeRef_Test4 = "1980-03-23 Mar 12:30";
        Thread test4 = new Thread() {

            @Override
            public void run() {
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.set(1980, 2, 23, 12, 30, 40);
                Date dateCalendar = calendar.getTime();
                for (int i = 0; i < LOOPS; i++) {
                    String date = TalendDate.formatDateLocale("yyyy-MM-dd MMM HH:mm", dateCalendar, "EN");
                    // System.out.println("Test4:" + date + " # " + dateTimeRef_Test4);
                    if (!dateTimeRef_Test4.equals(date)) {
                        throw new IllegalStateException("Test4: Date ref : '" + dateTimeRef_Test4 + "' is different of '" + date
                                + "'");
                    }
                }
                System.out.println("test4 ok");
            }
        };

        final String dateTimeRef_Test5 = "1979-03-23";
        Thread test5 = new Thread() {

            @Override
            public void run() {
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.set(1979, 2, 23, 12, 30, 40);
                Date dateCalendar = calendar.getTime();
                for (int i = 0; i < LOOPS; i++) {
                    String date = TalendDate.formatDate("yyyy-MM-dd", dateCalendar);
                    // System.out.println("Test5:" + date + " # " + dateTimeRef_Test5);
                    if (!dateTimeRef_Test5.equals(date)) {
                        throw new IllegalStateException("Test5: Date ref : '" + dateTimeRef_Test5 + "' is different of '" + date
                                + "'");
                    }

                }
                System.out.println("test5 ok");
            }
        };

        test1.start();
        test2.start();
        test3.start();
        test4.start();
        test5.start();
    }
    
    /**
     * 
     * @param string Must be a string datatype. Passes the values that you want to convert.
     * @param format Enter a valid TO_DATE format string. The format string must match the parts of the string argument
     * 		  default formate is "MM/DD/yyyy HH:mm:ss.sss" if not specified.
     * @return Date 
     * @throws ParseException
     * {example} TO_DATE("1464576463231", "J") #Mon May 30 10:47:43 CST 2016
     * {example} TO_DATE("2015-11-21 13:23:45","yyyy-MM-dd HH:mm:ss") #Sat Nov 21 13:23:45 CST 2015
     * 
     */
    public static Date TO_DATE(String string, String format) throws ParseException {
		String defaultFormat = "MM/dd/yyyy HH:mm:ss.SSS";
		if (StringHandling.isVacant(string)) {
			return null;
		}
		if (!StringHandling.isVacant(format)) {
			if (format.equals("J")) {
				return new Date(Long.parseLong(string));
			}
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormatConvert(format));
			return sdf.parse(string);
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat(defaultFormat);
			return sdf.parse(string);
		}

	}

	public static Date TO_DATE(String string) throws ParseException {
		return TO_DATE(string, null);
	}

	private static String dateFormatConvert(String format) {
		/**
		 * we do not support the type list below: 
		 * D : Day of week (1-7), where Sunday equals 1. 
		 * NS: Nanoseconds (0-999999999). SSSSS: Seconds since midnight (00000 - 86399). 
		 * US: Microseconds (0-999999). 
		 * Q: Quarter of year (1-4), where January to March equals 1.
		 */
		format = format.replaceAll("Y", "y");
		format = format.replaceAll("RR", "yy");
		format = format.replaceAll("MONTH", "MMMM");
		format = format.replaceAll("MON", "MMM");
		format = format.replaceAll("WW", "w");// Week of year (01-53)
		format = format.replaceAll("W", "F");// Week of month (1-5)
		format = format.replaceAll("(AM|A.M.|PM|P.M.)", "a");
		format = format.replaceAll("DY", "E");// Abbreviated three-character
												// name for a day (for example,
												// Wed).
		format = format.replaceAll("DDD", "D");// Day of year (001-366,
												// including leap years).
		format = format.replaceAll("DD", "d");// Day of month (01-31).
		format = format.replaceAll("HH24", "zx@i#o%l!");//protect HH24 from HH
		format = format.replaceAll("(HH|HH12)", "hh");
		format = format.replaceAll("zx@i#o%l!", "HH");
		format = format.replaceAll("MS", "sss");
		format = format.replaceAll("MI", "mm");
		format = format.replaceAll("SS", "ss");

		return format;
	}
	
	/**
	 * 
	 * @param date  Passes the values you want to change
	 * @param format A format string specifying the portion of the date value you want to change.For example, 'mm'.
	 * @param amount An integer value specifying the amount of years, months, days, hours, 
	 * 				and so on by which you want to change the date value.
	 * @return Date  NULL if a null value is passed as an argument to the function.
	 * @throws ParseException
	 * {example} ADD_TO_DATE(new Date(1464576463231l), "HH",2) #Mon May 30 12:47:43 CST 2016
	 */
	public static Date ADD_TO_DATE(Date date, String format, int amount) throws ParseException{
		if (date == null || StringHandling.isVacant(format)) {
			return null;
		}
		if (format != null) {
			format = dateFormatADD_TO_DATE(format);
		}
		Long time = date.getTime();
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		switch (format) {
		case "Y":
			calender.add(Calendar.YEAR, amount);
			time = calender.getTimeInMillis();
			break;
		case "MONTH":
			calender.add(Calendar.MONTH, amount);
			time = calender.getTimeInMillis();
			break;
        case "DAY":
            time += (long)amount * (long)86400000;
            break;
        case "HH":
            time += (long)amount * (long)3600000;
            break;
        case "MI":
            time += (long)amount * (long)60000;
            break;
        case "SS":
            time += (long)amount * (long)1000;
            break;
		case "MS":
			time += amount;
			break;
		case "US":
			time += amount / 1000;
			break;
		case "NS":
			time += amount / 1000000;
			break;
		default:
			throw new ParseException("Please enter a vaild format.", 0);
		}
		return new Date(time);

	}

	private static String dateFormatADD_TO_DATE(String format) {
		if (format.equals("Y") || format.equals("YY") || format.equals("YYY") || format.equals("YYYY")) {
			return "Y";
		}
		if (format.equals("MONTH") || format.equals("MM") || format.equals("MON")) {
			return "MONTH";
		}
		if (format.equals("D") || format.equals("DD") || format.equals("DDD") || format.equals("DAY")
				|| format.equals("DY")) {
			return "DAY";
		}
		if (format.equals("HH") || format.equals("HH12") || format.equals("HH24")) {
			return "HH";
		}
		return format;

	}
	
    /**
     * 
     * @param date  Date/Time datatype. Passes the date values you want to convert to character strings.
     * @param format   Enter a valid TO_CHAR format string. The format string defines the format of the return value,
     * @return  String.   NULL if a value passed to the function is NULL.
     */
    
    public static String TO_CHAR(Date date, String format) {
        if (date == null) {
            return null;
        }
        if(format==null||format.equals("")){
            format="MM/DD/YYYY HH24:MI:SS";
        }
        if("J".equals(format)){
            return Long.toString(date.getTime());
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormatConvert(format));
        return sdf.format(date);
    }
}
