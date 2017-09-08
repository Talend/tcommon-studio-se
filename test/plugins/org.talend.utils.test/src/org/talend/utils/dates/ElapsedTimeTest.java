// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
/*
 * Created on 20 janv. 2005 by S. Correia
 */
package org.talend.utils.dates;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

import org.talend.utils.dates.ElapsedTime;

/**
 * @author S. Correia These tests show that the conception of a constant unit of day fails. The constant
 * ElapsedTimeTest#MILLISEC_PER_DAY cannot be viewed as the day length.
 * @see ElapsedTimeTest#runWithInvalidAssertions for testing strange behavior of Dates
 * @see bug: "GregorianCalendar changes hour field when adding one day" at
 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4958050
 * 
 * MODSCA20051125 Java 5 changed the implementation of Calendar.clear() : fields cleared are undefined but not
 * necessarily set to 0!
 */
public class ElapsedTimeTest extends TestCase {

    private static boolean verbose = false; // for big trace

    private static final long MILLISEC_PER_DAY = 86400000;// 1000 * 60 * 60 * 24;

    static final long[] FIRST_DATE = { 1341345L, 0L // fails combined with 2460 added days
            , 1L // fails combined with 2460 added days
            , 10L // fails combined with 2460 added days
            , 1000L // fails combined with 2460 added days
            , 10000L // fails combined with 2460 added days
            , 2134545L // fails combined with 2460 added days
            , 5345455L // fails combined with 2790 added days
            , 6345455L // fails combined with 2790 added days
            , 6945455L // fails combined with 2790 added days
            , 7345455L // does NOT fail combined with 2790 added days
            , 8345455L, 213454545L, 2134545455L, 123453434634L, 12355756234136L, 24143634262247L, 12303451223L, 1133823600000L // is
                                                                                                                               // bugged
                                                                                                                               // when
                                                                                                                               // long
                                                                                                                               // obtained
                                                                                                                               // from
                                                                                                                               // parsing
            , 1133737200000L // result of bugged when long obtained from parsing

    };

    static final int[] NB_DAYS = { 10, 100, 567, 765, 800, 830, 835, 840, 845, 846, 847, 848, 849, 850, 860, 945, 1000, 1253,
            1305, 1698, 1909, 2003, 2459, 2790, 3005, 3896, 4569, 30450, 100, 300, 365, 366, 1000, 1200, 3000, 4000, 10390,
            15090, 30000, 45900 };

    private static final String SEP = "/";

    private static final int[] ZERO_FIELDS_UP_TO_DATE = new int[] { Calendar.MILLISECOND, Calendar.SECOND, Calendar.MINUTE,
            Calendar.HOUR_OF_DAY // 24H format
            , Calendar.DATE };

    private static final int[] ZERO_FIELDS_UP_TO_HOUR_AND_HOUROFDAY = new int[] { Calendar.MILLISECOND, Calendar.SECOND,
            Calendar.MINUTE, Calendar.HOUR_OF_DAY // 24H format
            , Calendar.HOUR // 12H format
    };

    private static final int[] ZERO_FIELDS_UP_TO_HOUR_OF_DAY = new int[] { Calendar.MILLISECOND, Calendar.SECOND,
            Calendar.MINUTE, Calendar.HOUR_OF_DAY // 24H format
    };

    /** Jan 21 11:27:11 CET 2005 in milliseconds */
    static final long AROUND_TODAY = 1106303231906L;

    /** a number of days for which the strategy with constant MILLISEC_PER_DAY fails */
    static final int FAIL_NB_DAYS = 1172;

    /** This test shows that MILLISEC_PER_DAY cannot be used to compute the number of days */
    public void testSimpleGetDaysDateDate() {
        Date beg = new Date(AROUND_TODAY);
        final int nbDay = 1162;

        // --- check created date
        assertEquals(AROUND_TODAY, beg.getTime());

        // --- a correct interval evaluation
        System.out.println("cur " + beg.getTime() + beg);
        Date end = new Date(MILLISEC_PER_DAY * nbDay + beg.getTime());
        System.out.println("[" + beg + " -> " + end + "]");
        assertEquals("nb days between: " + beg + " and " + end, nbDay, this.getDays(beg, end));

        // --- an incorrect interval evaluation
        Date failEnd = new Date(MILLISEC_PER_DAY * FAIL_NB_DAYS + beg.getTime());
        System.out.println("[" + beg + " -> " + failEnd + "]");
        // this bug should be fixed in later version of JDK.
        assertEquals("nb days between: " + beg + " and " + failEnd, true, FAIL_NB_DAYS == this.getDays(beg, failEnd));
        // --- the same evaluation made with ElapsedTime that works
        assertEquals("nb days between: " + beg + " and " + failEnd, true, FAIL_NB_DAYS == callElapsedTimegetNbDays(beg, failEnd));
    }

    /** Class under test for int getDays(GregorianCalendar, GregorianCalendar) */
    public void testGetDaysGregorianCalendarGregorianCalendar() {
        // --- loop on beginning date
        for (int i = 0; i < FIRST_DATE.length; i++) {
            long timeInMillis = FIRST_DATE[i];
            if (verbose) {
                System.out.println("input: " + timeInMillis + " -> " + new Date(timeInMillis));
            }

            // --- loop on the number of days
            for (int n = 0; n < NB_DAYS.length; n++) {
                int nbDay = NB_DAYS[n];
                // --- test with positive nb days and date after 1970
                doTest(nbDay, timeInMillis);
            }
        }
    }

    /** test adding n times 1 day vs adding directly n days */
    public void testAddNvsNtimesAdd() {
        // --- loop on beginning date
        for (int i = 0; i < FIRST_DATE.length; i++) {
            // --- loop on the number of days
            for (int n = 0; n < NB_DAYS.length; n++) {
                addNvsNtimesAdd(i, n);
            }
        }
    }

    /**
     * @param idxDebut the index of the first date
     * @param nb the number of days to check test adding n times 1 day vs adding directly n days
     */
    private void addNvsNtimesAdd(int idxDebut, int nb) {
        GregorianCalendar debut = new GregorianCalendar();
        debut.setTimeInMillis(FIRST_DATE[idxDebut]);

        // --- clone the date
        // date to which n days will be added
        GregorianCalendar addNCal = (GregorianCalendar) debut.clone();
        addNCal.add(Calendar.DATE, nb); // add nb days

        // date to which n times 1 day will be added
        GregorianCalendar nAddCal = (GregorianCalendar) debut.clone();
        for (int i = 0; i < nb; i++) { // add n times 1 day
            nAddCal.add(Calendar.DATE, 1);
        }

        // --- check that the 2 dates are identical
        assertEquals(addNCal.getTime(), nAddCal.getTime());

        // --- check the difference of the 2 dates with respect to the debut date
        int addNDuration = this.getDays(debut.getTime(), addNCal.getTime());
        int nAddDuration = this.getDays(debut.getTime(), nAddCal.getTime());
        assertEquals(addNDuration, nAddDuration);

        // --- check the difference of the dates with respect to the debut date (with other implementation)
        long correcAddNDuration = callElapsedTimegetNbDays(debut.getTime(), addNCal.getTime());
        long correcNAddDuration = callElapsedTimegetNbDays(debut.getTime(), nAddCal.getTime());
        assertEquals(correcAddNDuration, correcNAddDuration);

        // --- now check that the two implementation give the same result
        assertEquals(correcAddNDuration, addNDuration);

        // --- check that the result corresponds to the input value
        assertEquals(nb, correcAddNDuration);
    }

    public void testGetJulianDays() {
        final int[][] date = { { 1900, 12, 24 }, { 1950, 2, 4 }, { 1350, 1, 31 }, { 1550, 5, 5 }, { 1650, 7, 18 },
                { 1950, 9, 27 }, { 1999, 12, 31 }, { 2000, 1, 1 }, { 2005, 1, 20 }, { 2005, 1, 30 }, { 2053, 05, 15 },
                { 1582, 10, 15 } // date from this one are gregorian dates
                , { 1582, 10, 4 } // gap between this date and the previous one (10 missing days)
        };

        final long[] julianDaysFromWeb = { // from http://aa.usno.navy.mil/data/docs/JulianDate.html
        2415378 // 1900 , 12, 24
                , 2433317 // 1950 , 2, 4
                , 2214176 // 1350 , 1, 31
                , 2287320 // 1550 , 5, 5
                , 2323909 // 1650 , 7, 18
                , 2433552 // 1950 , 9, 27
                , 2451544 // 1999 , 12, 31
                , 2451545 // 2000 , 1, 1
                , 2453391 // 2005, 1, 20
                , 2453401 // 2005, 1, 30
                , 2471038 // 2053, 05, 15
                , 2299161 // 1582, 10, 15
                , 2299160 // 1582, 10, 4
        };

        assertEquals(date.length, julianDaysFromWeb.length);
        int year = 0;
        int month = 0;
        int day = 0;
        for (int d = 0; d < date.length; d++) {
            try {
                year = date[d][0];
                month = date[d][1];
                day = date[d][2];

                long jd = ElapsedTime.getJulianDays(year, month, day);
                System.out.println("D: " + year + SEP + month + SEP + day + " -> " + jd);
                long fromWeb = julianDaysFromWeb[d];
                assertEquals("Bad julian day for " + d + "th date:" + year + SEP + month + SEP + day, fromWeb, jd);
            } catch (IllegalArgumentException e) {
                assertEquals("Date:" + year + SEP + month + SEP + day, true, (year < ElapsedTime.GREGORIAN_CAL_YEAR)
                        || ((year == ElapsedTime.GREGORIAN_CAL_YEAR) && (month < ElapsedTime.GREGORIAN_CAL_MONTH))
                        || ((year == ElapsedTime.GREGORIAN_CAL_YEAR) && (month == ElapsedTime.GREGORIAN_CAL_MONTH))
                        && (day < ElapsedTime.GREGORIAN_CAL_DAY));

                System.out.println(e.getMessage());
            }

        }

        // --- check that the diff between
        // 24/01/2005 23:23
        // and
        // 25/01/2005 3:52
        // is 1 day
        Calendar c1 = Calendar.getInstance();
        c1.set(2005, 01, 24, 23, 23);

        Calendar c2 = Calendar.getInstance();
        c2.set(2005, 01, 25, 3, 52);

        assertEquals(1, callElapsedTimegetNbDays(c1.getTime(), c2.getTime()));
    }

    public void testFieldDifference() {
        final int maxNbDays = 7600;
        for (int i = 0; i < maxNbDays; i += 117) {
            fieldDaysDifference(i);
        }

        // --- loop on several input amounts
        for (int i = 0; i < INPUT_EXPECTED.length; i++) {
            fieldDifference(i);
        }
    }

    /**
     * input {months, days, hours} -> expected {months, days, hours}
     */
    private static final int[][][] INPUT_EXPECTED = { { { 3, 5, 2 }, { 3, 5, 2 } },
            { { 3, 5, 20 }, { 3, 5, 20 } },
            { { 3, 12, 2 }, { 3, 12, 2 } },
            { { 3, 1, 2 }, { 3, 1, 2 } },
            { { 1, 5, 2 }, { 1, 5, 2 } },
            { { 0, 0, 200 }, { 0, 8, 8 } } // e.g. input 200 hours -> expected 8 days, 8 hours
            , { { 0, 50, 2 }, { 1, 19, 2 } }, { { 34, 4, 45 }, { 34, 5, 21 } }, { { 124, 0, 0 }, { 124, 0, 0 } },
            { { 0, 1235, 0 }, { 40, 19, 0 } }, { { 0, 4445, 0 }, { 146, 3, 0 } }, { { 149, 4445, 0 }, { 295, 1, 0 } },
            { { 23456, 4445, 5653 }, { 23609, 23, 14 } }, { { 3, 4445, 56 }, { 149, 3, 8 } },
            { { 0, 0, 12454546 }, { 17049, 22, 10 } } };

    private void fieldDaysDifference(int days) {
        // --- loop on several input dates
        for (int i = 0; i < FIRST_DATE.length; i++) {
            fieldDiffWithGetNbDays(new Date(FIRST_DATE[i]), days);
        }
    }

    private void fieldDifference(int idx) {
        // --- input date: "Tue Jan 25 10:44 CET 2005"
        Calendar cal = Calendar.getInstance();
        cal.set(2005, Calendar.JANUARY, 25, 10, 44);
        Date x = cal.getTime();
        if (verbose) {
            System.out.println(x);
        }

        int month = INPUT_EXPECTED[idx][0][0];
        int days = INPUT_EXPECTED[idx][0][1];
        int hours = INPUT_EXPECTED[idx][0][2];
        // --- add given amounts
        if (verbose) {
            System.out.println("Adding " + month + " months, " + days + " days and " + hours + " hours to date.");
        }
        cal.add(Calendar.MONTH, month);
        cal.add(Calendar.DAY_OF_MONTH, days);
        cal.add(Calendar.HOUR, hours);
        Date y = cal.getTime();
        if (verbose) {
            System.out.println(y);
        }

        // --- get the difference between the 2 dates
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(x);

        // However, there is no method that will tell you what the interval
        // is between two dates. Here is an example of usage:

        int m = ElapsedTime.fieldDifference(cal2, y, Calendar.MONTH);

        int d = ElapsedTime.fieldDifference(cal2, y, Calendar.DAY_OF_MONTH);

        int h = ElapsedTime.fieldDifference(cal2, y, Calendar.HOUR);

        if (verbose) {
            System.out.println(m + " months");
        }
        if (verbose) {
            System.out.println(d + " days");
        }
        if (verbose) {
            System.out.println(h + " hours");
        }

        assertEquals(INPUT_EXPECTED[idx][1][0], m);
        assertEquals(INPUT_EXPECTED[idx][1][1], d);
        assertEquals(INPUT_EXPECTED[idx][1][2], h);
    }

    private void fieldDiffWithGetNbDays(Date x, int days) {
        if (verbose) {
            System.out.println(x);
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(x);

        // --- add given amounts
        if (verbose) {
            System.out.println("Adding " + days + " days to date " + x);
        }
        cal.add(Calendar.DAY_OF_MONTH, days);
        Date y = cal.getTime();
        if (verbose) {
            System.out.println(y);
        }

        // --- compare with getNbDays() when possible
        Calendar cal3 = Calendar.getInstance();
        cal3.setTime(x);

        // However, there is no method that will tell you what the interval
        // is between two dates. Here is an example of usage:

        int d = ElapsedTime.fieldDifference(cal3, y, Calendar.DAY_OF_MONTH);
        if (verbose) {
            System.out.println(d + " days");
        }

        assertEquals(ElapsedTime.getNbDays(x, y), d);
    }

    public void testGetNbMinutes() {
        final int[] minutes = { 3600, 4546, 9, 123345, 653573, 245676, 5425676 };
        for (int m = 0; m < minutes.length; m++) {
            getDurationInMinutes(minutes[m]);
        }

    }

    void getDurationInMinutes(int minutes) {
        Date in = new Date();
        Date end = createEndDate(in, minutes, Calendar.MINUTE);
        int duration = (int) ElapsedTime.getNbMinutes(in, end); // ATTENTION cast long -> int
        assertEquals(in.toString() + " " + minutes + " min -> " + end.toString(), minutes, duration);

        // verify result with generic method
        Calendar cal = Calendar.getInstance();
        cal.setTime((Date) in.clone());
        decompose(minutes, in, end); // for trace only
        int otherComputed = ElapsedTime.fieldDifference(cal, (Date) end.clone(), Calendar.MINUTE);
        assertEquals(duration, otherComputed);
    }

    /**
     * @param in
     * @param minutes
     * @param field
     * @return
     */
    private Date createEndDate(Date in, int minutes, int field) {

        // --- create the Calendar with the start date
        GregorianCalendar gregCal = new GregorianCalendar();
        gregCal.setTime((Date) in.clone()); // clone start date to get an end date

        gregCal.add(field, minutes);
        return gregCal.getTime();
    }

    private void decompose(int minutes, Date start, Date end) {
        Calendar cal = Calendar.getInstance();
        cal.setTime((Date) start.clone());
        int y = ElapsedTime.fieldDifference(cal, end, Calendar.YEAR);
        int m = ElapsedTime.fieldDifference(cal, end, Calendar.MONTH);
        int d = ElapsedTime.fieldDifference(cal, end, Calendar.DATE);
        int h = ElapsedTime.fieldDifference(cal, end, Calendar.HOUR);
        int mm = ElapsedTime.fieldDifference(cal, end, Calendar.MINUTE);
        System.out.println("Diff " + start + " -> " + end + " nb min: " + minutes + "\n" + y + " years " + m + " month " + d
                + " days " + h + " hours " + mm + " min ");
    }

    // ----------------------------------
    // --- herafter some tests that fail

    /**
     * ATTENTION by running these tests the assertions will fail. => do not include this test in a test suite, since it
     * would corrupt the suite's result.
     */
    boolean runWithInvalidAssertions = false; // ATTENTION when true, assertions will fail and test will be broken

    public void testPositiveNbDaysDateBeforeEpoch() {
        if (runWithInvalidAssertions == false) {
            return; // PATCH this test is removed
        }

        // --- loop on beginning date
        for (int i = 0; i < FIRST_DATE.length; i++) {
            long timeInMillis = FIRST_DATE[i];
            System.out.println("input: " + timeInMillis + " -> " + new Date(timeInMillis));

            // --- loop on the number of days
            for (int n = 0; n < NB_DAYS.length; n++) {
                int nbDay = NB_DAYS[n];
                // --- test with positive nb days and date before 1970
                doTest(nbDay, -timeInMillis); // pb with 10 missing days in year 1582
            }
        }
    }

    public void testNegativeNbDaysDateBeforeEpoch() {
        if (runWithInvalidAssertions == false) {
            return; // PATCH this test is removed
        }

        // --- loop on beginning date
        for (int i = 0; i < FIRST_DATE.length; i++) {
            long timeInMillis = FIRST_DATE[i];
            System.out.println("input: " + timeInMillis + " -> " + new Date(timeInMillis));

            // --- loop on the number of days
            for (int n = 0; n < NB_DAYS.length; n++) {
                int nbDay = NB_DAYS[n];
                // --- test with negative nb days and date before 1970
                doTest(-nbDay, -timeInMillis);
            }
        }
    }

    public void testNegativeNbDaysDateAfterEpoch() {
        if (runWithInvalidAssertions == false) {
            return; // PATCH this test is removed
        }

        // --- loop on beginning date
        for (int i = 0; i < FIRST_DATE.length; i++) {
            long timeInMillis = FIRST_DATE[i];
            System.out.println("input: " + timeInMillis + " -> " + new Date(timeInMillis));

            // --- loop on the number of days
            for (int n = 0; n < NB_DAYS.length; n++) {
                int nbDay = NB_DAYS[n];
                // --- test with negative nb days and date after 1970
                doTest(-nbDay, timeInMillis);
            }
        }
    }

    /**
     * Class under test for int getDays(Date, Date)
     */
    public void testGetDaysDateDate() {
        // --- test bug from PCR:
        String date1 = "01/11/2004 00:00";
        String date2 = "05/11/2004 07:00";
        final int expectedNb = 4;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Date d1 = dateFormatter.parse(date1, new ParsePosition(0));
        Date d2 = dateFormatter.parse(date2, new ParsePosition(0));
        int nbDaysElapsedTime = (int) ElapsedTime.getNbDays(d1, d2);
        assertEquals("Nb days with ElapseTime: " + nbDaysElapsedTime + " instead of " + expectedNb, expectedNb, nbDaysElapsedTime);

        if (runWithInvalidAssertions == false) {
            return; // PATCH this test is removed
        }

        // --- loop on beginning date
        for (int i = 0; i < FIRST_DATE.length; i++) {
            // --- loop on the number of days
            for (int n = 0; n < NB_DAYS.length; n++) {
                int nbDay = NB_DAYS[n]; // test fail for 850 (and others)
                Date beg = new Date(FIRST_DATE[i]);
                Date end = new Date(MILLISEC_PER_DAY * nbDay + FIRST_DATE[i]);
                assertEquals("nb days between: " + beg + " and " + end, nbDay, this.getDays(beg, end));
            }
        }
    }

    public void testGregorianCalendar() {
        if (runWithInvalidAssertions == false) {
            return; // PATCH this test is removed
        }

        // --- start date
        GregorianCalendar beg = new GregorianCalendar();
        beg.setTimeInMillis(AROUND_TODAY);

        // --- end date, assuming the day length is constant
        GregorianCalendar badEnd = new GregorianCalendar();
        badEnd.setTimeInMillis(AROUND_TODAY // start date
                + MILLISEC_PER_DAY * FAIL_NB_DAYS // + nb day in millisec
        );

        // --- try to add the number of days (The correct way to add a number of days)
        GregorianCalendar correctEnd = (GregorianCalendar) beg.clone();
        correctEnd.add(Calendar.DATE, FAIL_NB_DAYS);

        // end and endBis are not always identical: day length is not a constant
        assertEquals(badEnd.getTimeInMillis(), correctEnd.getTimeInMillis());
    }

    /**
     * Tests 2 ways for computing the difference between 2 GregorianCalendar Dates
     * 
     * @param inputNbDay
     * @param startTimeInMillis
     */
    void doTest(int inputNbDay, long startTimeInMillis) {
        // --- start date
        GregorianCalendar beg = new GregorianCalendar();
        beg.setTimeInMillis(startTimeInMillis);

        // --- try to add the number of days (The correct way to add a number of days)
        GregorianCalendar correctEnd = (GregorianCalendar) beg.clone();
        correctEnd.add(Calendar.DATE, inputNbDay);

        // --- test different ways of computing the number of days between 2 dates
        int buggedNbDays = this.getDays(beg, correctEnd);
        long nbDaysWithDateDiff = callElapsedTimegetNbDays(beg.getTime(), correctEnd.getTime());
        if (runWithInvalidAssertions) {
            assertEquals(
                    // FAILS (e.g. for Thu Jan 01 01:35:34 CET 1970 and 2460 days)
                    "The 2 ways of computing the number of days should give the same result and should give " + inputNbDay,
                    nbDaysWithDateDiff, buggedNbDays);
        }

        // --- print and test what happens when there is an offset
        if (correctEnd.get(Calendar.DST_OFFSET) != 0) {
            if (inputNbDay != buggedNbDays) {
                // I thought there is a diffence in the number of days only when the DST offset is not null, but is not
                // true
                if (verbose) {
                    print(correctEnd);
                }
                assertEquals(inputNbDay, callElapsedTimegetNbDays(beg.getTime(), correctEnd.getTime()));
                assertEquals(false, inputNbDay == this.getDays(beg, correctEnd)); // in principle, should be equals but
                                                                                  // fails !!
            }
        } else { // only check assertion when DST offset is null (no daylight saving)
            if (runWithInvalidAssertions) {
                assertEquals(inputNbDay, buggedNbDays);
            }
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // The following methods fail to give the correct number of days
    // in some cases.
    // These methods were first in class ElapsedTime but have been put here.
    // cut & paste from
    // http://www.javaworld.com/javaworld/jw-03-2001/jw-0330-time_p.html
    // /////////////////////////////////////////////////////////////////////////

    /**
     * @param d1 the first date
     * @param d2 the last date
     * @return the number of months changes in the interval This number is positive if d2 is after d1, else negative
     */
    int getMonths(Date d1, Date d2) {
        GregorianCalendar g1 = convert(d1);
        GregorianCalendar g2 = convert(d2);
        return getMonths(g1, g2);
    }

    /**
     * @param g1 the first date
     * @param g2 the last date
     * @return the number of day changes in the interval This number is positive if d2 is after d1, else negative
     */
    int getDays(GregorianCalendar g1, GregorianCalendar g2) {
        int elapsed = 0;
        GregorianCalendar gc1, gc2;

        if (g2.after(g1)) {
            gc2 = (GregorianCalendar) g2.clone();
            gc1 = (GregorianCalendar) g1.clone();
        } else {
            gc2 = (GregorianCalendar) g1.clone();
            gc1 = (GregorianCalendar) g2.clone();
        }

        clearFields(ZERO_FIELDS_UP_TO_HOUR_OF_DAY, gc1);
        clearFields(ZERO_FIELDS_UP_TO_HOUR_OF_DAY, gc2);

        while (gc1.before(gc2)) {
            gc1.add(Calendar.DATE, 1);
            elapsed++;
        }
        return (g2.after(g1)) ? elapsed : -elapsed;

        // return elapsed;
    }

    /**
     * @param g1 the first date
     * @param g2 the last date
     * @return the number of months changes in the interval This number is positive if d2 is after d1, else negative
     */
    int getMonths(GregorianCalendar g1, GregorianCalendar g2) {
        int elapsed = 0;
        GregorianCalendar gc1, gc2;

        if (g2.after(g1)) {
            gc2 = (GregorianCalendar) g2.clone();
            gc1 = (GregorianCalendar) g1.clone();
        } else {
            gc2 = (GregorianCalendar) g1.clone();
            gc1 = (GregorianCalendar) g2.clone();
        }

        clearFields(ZERO_FIELDS_UP_TO_DATE, gc1);
        clearFields(ZERO_FIELDS_UP_TO_DATE, gc2);

        while (gc1.before(gc2)) {
            gc1.add(Calendar.MONTH, 1);
            elapsed++;
        }
        return elapsed;
    }

    private static void clearFields(final int[] fieldsToReset, Calendar calendar) {
        for (int i = 0; i < fieldsToReset.length; i++) {
            // calendar.set(fieldsToReset[i], 0); // FAILS in java 1.2 and java 5
            if (false) {
                calendar.clear(fieldsToReset[i]); // ok works in java 1.2 and also in java 5 but it is not needed to
                // clear fields
            }
        }
    }

    private static GregorianCalendar convert(Date date) {
        GregorianCalendar g1 = new GregorianCalendar();
        g1.setTime(date);
        return g1;
    }

    // ///////////////////////////////////////////////////////////////////
    // / End of methods extracted from ElapsedTime
    // ///////////////////////////////////////////////////////////////////

    /**
     * @param first first date
     * @param last last date
     * @return last - first
     */
    private long callElapsedTimegetNbDays(Date first, Date last) {
        // return callElapsedTimegetNbDaysJer(first, last);
        return callElapsedTimegetNbDaysSeb(first, last);
    }

    private long callElapsedTimegetNbDaysSeb(Date first, Date last) {
        long res = ElapsedTime.getNbDays(first, last);
        long otherImpl = myGetDaysWithHourCleared(first, last);
        if (runWithInvalidAssertions) {
            assertEquals("Dates= " + first + " " + last, res, otherImpl);
        }
        return res;
    }

    /**
     * @param d1 the first date
     * @param d2 the last date
     * @return the number of day changes in the interval This number is positive if d2 is after d1, else negative
     */
    int getDays(Date d1, Date d2) {
        GregorianCalendar g1 = convert(d1);
        GregorianCalendar g2 = convert(d2);

        int nDay = getDays(g1, g2);
        int myNDay = myGetDays(g1, g2);
        int nbWithHoursCleared = myGetDaysWithHourCleared(g1, g2);

        if (runWithInvalidAssertions) {// PATCH this test is removed
            assertEquals("Dates: " + d1 + " " + d2, nDay, nbWithHoursCleared); // fails => myGetDaysWithHourCleared() is
                                                                               // the correct impl
        }
        assertEquals("Dates: " + d1 + " " + d2, nDay, myNDay);
        return nDay;
    }

    int myGetDays(GregorianCalendar g1, GregorianCalendar g2) {
        int elapsed = 0;
        GregorianCalendar gc1, gc2;

        if (g2.after(g1)) {
            gc2 = (GregorianCalendar) g2.clone();
            gc1 = (GregorianCalendar) g1.clone();
        } else {
            gc2 = (GregorianCalendar) g1.clone();
            gc1 = (GregorianCalendar) g2.clone();
        }

        // SCA why clear?
        // gc1.clear(Calendar.MILLISECOND);
        // gc1.clear(Calendar.SECOND);
        // gc1.clear(Calendar.MINUTE);
        // gc1.clear(Calendar.HOUR_OF_DAY);
        //
        // gc2.clear(Calendar.MILLISECOND);
        // gc2.clear(Calendar.SECOND);
        // gc2.clear(Calendar.MINUTE);
        // gc2.clear(Calendar.HOUR_OF_DAY);

        while (gc1.before(gc2)) {
            gc1.add(Calendar.DATE, 1);
            elapsed++;
        }
        return (g2.after(g1)) ? elapsed : -elapsed;

        // return elapsed;
    }

    /**
     * should return the number of days between the 2 given dates But since Java 5, it seems the returned number is
     * incorrect?
     */
    int myGetDaysWithHourCleared(Date d1, Date d2) {
        GregorianCalendar g1 = convert(d1);
        GregorianCalendar g2 = convert(d2);
        int nbWithHoursCleared = myGetDaysWithHourCleared(g1, g2);

        return nbWithHoursCleared;
    }

    /**
     * Same implementation as ElapsedTimeTest#getDays(Date, Date) except that I ADDED clear also Hour:
     * gc1.clear(Calendar.HOUR);
     * 
     * @see ElapsedTimeTest#getDays(GregorianCalendar, GregorianCalendar)
     * @return positive or negative values
     */
    int myGetDaysWithHourCleared(GregorianCalendar g1, GregorianCalendar g2) {
        int elapsed = 0;
        GregorianCalendar gc1, gc2;

        if (g2.after(g1)) {
            gc2 = (GregorianCalendar) g2.clone();
            gc1 = (GregorianCalendar) g1.clone();
        } else {
            gc2 = (GregorianCalendar) g1.clone();
            gc1 = (GregorianCalendar) g2.clone();
        }

        clearFields(ZERO_FIELDS_UP_TO_HOUR_AND_HOUROFDAY, gc1);
        clearFields(ZERO_FIELDS_UP_TO_HOUR_AND_HOUROFDAY, gc2);

        while (gc1.before(gc2)) {
            gc1.add(Calendar.DATE, 1);
            elapsed++;
        }
        return (g2.after(g1)) ? elapsed : -elapsed;
        // return elapsed;
    }

    void print(Calendar calendar) {
        // print out a bunch of interesting things
        System.out.println("ERA: " + calendar.get(Calendar.ERA));
        System.out.println("YEAR: " + calendar.get(Calendar.YEAR));
        System.out.println("MONTH: " + calendar.get(Calendar.MONTH));
        System.out.println("WEEK_OF_YEAR: " + calendar.get(Calendar.WEEK_OF_YEAR));
        System.out.println("WEEK_OF_MONTH: " + calendar.get(Calendar.WEEK_OF_MONTH));
        System.out.println("DATE: " + calendar.get(Calendar.DATE));
        System.out.println("DAY_OF_MONTH: " + calendar.get(Calendar.DAY_OF_MONTH));
        System.out.println("DAY_OF_YEAR: " + calendar.get(Calendar.DAY_OF_YEAR));
        System.out.println("DAY_OF_WEEK: " + calendar.get(Calendar.DAY_OF_WEEK));
        System.out.println("DAY_OF_WEEK_IN_MONTH: " + calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH));
        System.out.println("AM_PM: " + calendar.get(Calendar.AM_PM));
        System.out.println("HOUR: " + calendar.get(Calendar.HOUR));
        System.out.println("HOUR_OF_DAY: " + calendar.get(Calendar.HOUR_OF_DAY));
        System.out.println("MINUTE: " + calendar.get(Calendar.MINUTE));
        System.out.println("SECOND: " + calendar.get(Calendar.SECOND));
        System.out.println("MILLISECOND: " + calendar.get(Calendar.MILLISECOND));
        System.out.println("ZONE_OFFSET: " + (calendar.get(Calendar.ZONE_OFFSET) / (60 * 60 * 1000))); // in hours
        System.out.println("DST_OFFSET: " + (calendar.get(Calendar.DST_OFFSET) / (60 * 60 * 1000))); // in hours
        System.out.println();
    }

    // /** Yet another algo from http://forum.java.sun.com/thread.jspa?forumID=54&threadID=558752 */
    // public static class DateInterval {
    // public static final long MILLIS_PER_DAY = 1000 * 60 * 60 * 24;
    //	 
    // public static int daysBetween(Calendar fromCal, Calendar toCal) {
    // //make copies because we're going to rollback
    // fromCal = (Calendar) fromCal.clone();
    // toCal = (Calendar) toCal.clone();
    //	 
    // rollBackToMidnight(fromCal);
    // rollBackToMidnight(toCal);
    //	 
    // long fromMillis = fromCal.getTimeInMillis();
    // long toMillis = toCal.getTimeInMillis();
    // long diffMillis = toMillis-fromMillis;
    // long diffDays = diffMillis / MILLIS_PER_DAY;
    // long remainder = diffMillis % MILLIS_PER_DAY;
    // if (Math.abs(remainder) > MILLIS_PER_DAY/2) {
    // if (diffDays > 0)
    // ++diffDays;
    // else
    // --diffDays;
    // }
    // return (int) diffDays;
    // }
    //	 
    // public static void rollBackToMidnight(Calendar cal) {
    // cal.set(Calendar.HOUR_OF_DAY, 0);
    // cal.set(Calendar.MINUTE, 0);
    // cal.set(Calendar.SECOND, 0);
    // cal.set(Calendar.MILLISECOND, 0);
    // }
    // }

}
