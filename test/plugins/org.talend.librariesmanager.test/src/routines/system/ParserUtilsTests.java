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

package routines.system;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ParserUtilsTests {

    private Calendar calendar;

    @Before
    public void setUp() {
        calendar = Calendar.getInstance(Locale.ROOT);
    }

    @Test
    public void testParseDate() {
        Date date = ParserUtils.parseTo_Date("14-03-2018", "dd-MM-yyyy");
        calendar.setTime(date);
        assertEquals(2018, calendar.get(Calendar.YEAR));
        assertEquals(2, calendar.get(Calendar.MONTH));
        assertEquals(14, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testParseEpoch() {
        testParseEpoch(calendar, ParserUtils.parseTo_Date(1535623651L), 2018, 7, 30, 12, 07, 31);
        testParseEpoch(calendar, ParserUtils.parseTo_Date("1535623651"), 2018, 7, 30, 12, 07, 31);
        testParseEpoch(calendar, ParserUtils.parseTo_Date("1535623651.0"), 2018, 7, 30, 12, 07, 31);
        try {
            testParseEpoch(calendar, ParserUtils.parseTo_Date("1535623651.3"));
        } catch (NumberFormatException e) {
            assertTrue(e instanceof NumberFormatException);
        }
    }
    
    private void testParseEpoch(Calendar calendar, Date date, int... args) {
        calendar.setTime(date);
        int[] fields = new int[] { Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH,
                                   Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND };
        for (int i = 0; i < args.length; i++) {
            assertEquals(args[i], calendar.get(fields[i]));
        }
    }
}
