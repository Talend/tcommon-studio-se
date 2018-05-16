// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.utils.dates;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author S. Correia Class This class has methods for counting days and months.
 * 
 * The definition of time unit change is relatively simple: If you are counting days, you simply count the number of
 * times the date has changed. For example, if something starts on the 15th and ends on the 17th, 2 days have passed.
 * (The date changed first to the 16th, then to the 17th.) Similarly, if a process starts at 3:25 in the afternoon and
 * finishes at 4:10 p.m., 1 hour has passed because the hour has changed once (from 3 to 4).
 * 
 * Libraries often calculate time in this manner. For example, if I borrow a book from my library, I don't need to have
 * the book in my possession for a minimum of 24 hours for the library to consider it borrowed for one day. Instead, the
 * day that I borrow the book is recorded on my account. As soon as the date switches to the next day, I have borrowed
 * the book for one day, even though the amount of time is often less than 24 hours.
 */
public final class ElapsedTime {

    private ElapsedTime() {
    }

    public static final int GREGORIAN_CAL_YEAR = 1582;

    public static final int GREGORIAN_CAL_MONTH = 10;

    public static final int GREGORIAN_CAL_DAY = 15;

    public static final double TROPICAL_YEAR_LENGTH = 365.25;

    /** temporary calendar reserved in getNbDays(). */
    private static final Calendar TMP_GREG_CAL_1 = new GregorianCalendar(); // SCA why Gregorian ?

    /** temporary calendar reserved in getNbDays(). */
    private static final Calendar TMP_GREG_CAL_2 = new GregorianCalendar();

    private static final int NB_MILLIS_PER_MINUTE = 60000;

    private static final int NB_MILLIS_PER_SECOND = 1000;

    /**
     * @param first the first date
     * @param last the last date
     * @return the number of day changes in the interval [first, last] = last - first. This number is positive if last
     * is after first, else negative.
     */
    public static long getNbDays(Date first, Date last) {
        // code from http://forum.java.sun.com/thread.jspa?forumID=54&threadID=285551
        Calendar c1 = TMP_GREG_CAL_1;
        Calendar c2 = TMP_GREG_CAL_2;

        c1.setTime(last);
        c2.setTime(first);

        long j1 = getJulianDays(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH) + 1, c1.get(Calendar.DATE));
        long j2 = getJulianDays(c2.get(Calendar.YEAR), c2.get(Calendar.MONTH) + 1, c2.get(Calendar.DATE));

        long daysElapsed = j1 - j2;
        return daysElapsed;
    }

    /**
     * @param first the first date
     * @param last the last date
     * @return the number of minutes between first and last ( last - first ). This number is positive if last is after
     * first, else negative.
     */
    public static long getNbMinutes(Date first, Date last) {
        long min = (last.getTime() - first.getTime()) / NB_MILLIS_PER_MINUTE;
        return min;
    }

    /**
     * @param first the first date
     * @param last the last date
     * @return the number of seconds between first and last ( last - first ). This number is positive if last is after
     * first, else negative.
     */
    public static long getNbSeconds(Date first, Date last) {
        long min = (last.getTime() - first.getTime()) / NB_MILLIS_PER_SECOND;
        return min;
    }

    /**
     * See http://scienceworld.wolfram.com/astronomy/JulianDate.html for some formulae, but I did not see the one used
     * here. Since 15 Oct 1582 the input dates represent Gregorian calendar Input date must be after this first date of
     * the Gregorian calendar, else the julian day is incorrect (10 days should be added) for dates on or before 4
     * October 1582, the Julian calendar is to be used, not the Gregorian calendar.
     * 
     * @param year
     * @param month
     * @param day
     * @return the julian day
     */
    public static long getJulianDays(int year, int month, int day) throws IllegalArgumentException {
        if ((year < GREGORIAN_CAL_YEAR) || ((year == GREGORIAN_CAL_YEAR) && (month < GREGORIAN_CAL_MONTH))
                || ((year == GREGORIAN_CAL_YEAR) && (month == GREGORIAN_CAL_MONTH)) && (day < GREGORIAN_CAL_DAY)) {
            throw new IllegalArgumentException("Dates before 15 Oct 1582 are not valid Gregorian dates " + year + "/" + month
                    + "/" + day);
        }

        // TODO understand this formula, and find reference
        int y = year;
        int m = month;
        if (m == 1 || m == 2) {
            y--;
            m += 12;
        }

        int a = y / 100;
        int b = a / 4;
        int c = 2 - a + b;

        double e = TROPICAL_YEAR_LENGTH * (y + 4716);
        double e1 = Math.floor(e);
        long e2 = Math.round(e1);

        double f = 30.6001 * (m + 1);
        double f1 = Math.floor(f);
        long f2 = Math.round(f1);

        // [2 - (y / 100) + (a/4)] + [day] + [INT(365.25 * (y + 4716) )] + [INT(30.6001 * (m+1) )]
        // where INT (d) = upper int nearest to d
        long julianDay = c + day + e2 + f2 - 1524;

        return julianDay;
    }

    /**
     * Finds the largest value of field that can be added to start that ends up with a date that is less than or equal
     * to end. Modifies the input calendar to be the result of adding that number to the field. Ought to be method on
     * calendar, with just the final 2 params
     * 
     * @param start the start date (is modified)
     * @param endTime the end date
     * @param field the Calendar's field unit
     * @return the difference between start and endTime in Calendar's field unit Ex: int months =
     * ElapsedTime.fieldDifference(cal2, y, Calendar.MONTH); int days = ElapsedTime.fieldDifference(cal2, y,
     * Calendar.DAY_OF_MONTH); int hours = ElapsedTime.fieldDifference(cal2, y, Calendar.HOUR);
     * 
     * Ex: Adding 124 months, 0 days and 0 hours to date Thu Jan 01 02:29:05 CET 1970 gives Thu May 01 02:29:05 CEST
     * 1980 and the difference between the two is: 123 months, 29 days, 23 hours.
     * 
     * See "Need API to find intervals between dates, in locale-independent fashion" at
     * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4114914
     */
    public static int fieldDifference(Calendar start, Date endTime, int field) {
        // hack for now. Can be done more efficiently inside precise Calendar
        Date startTime = start.getTime();
        if (!startTime.before(endTime)) {
            return 0;
        }
        for (int i = 1;; ++i) {
            start.add(field, 1);
            if (start.getTime().after(endTime)) {
                start.add(field, -1); // backup
                return (i - 1); // return difference in field
            }
        }
    }
}
