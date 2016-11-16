// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TalendTimestampWithTZ extends Date {

    Timestamp ts;

    TimeZone tz;

    public TalendTimestampWithTZ(Timestamp ts, TimeZone tz) {
        super(ts.getTime());
        this.ts = ts;
        this.tz = tz;
    }

    public TimeZone getTimeZone() {
        return tz;
    }

    public Timestamp getTimestamp() {
        return ts;
    }

    public Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(ts.getTime());
        calendar.setTimeZone(tz);
        return calendar;
    }
}
