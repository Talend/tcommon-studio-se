// ============================================================================
//
// Copyright (C) 2006-2020 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.commons.utils.time;

import static org.junit.Assert.assertEquals;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.junit.Test;

/**
 * DOC sbliu  class global comment. Detailled comment
 */
public class PerformanceStatisticUtilTest {

    @Test
    public void testNumberFormat() {
        Locale defaultLocale = Locale.getDefault();
        double dvalue = 123456123456.789123456789;
        String pattern = "#,###.##";
        try {
            Locale.setDefault(new Locale("en","US"));
            DecimalFormat df = new DecimalFormat(pattern);
            assertEquals("123,456,123,456.79",df.format(dvalue));
            assertEquals("123456123456.79",PerformanceStatisticUtil.format(dvalue));
            
            Locale.setDefault(new Locale("fr","FR"));
            // DecimalFormat need to create after defaultLocale settle to apply
            df = new DecimalFormat(pattern);
            assertEquals("123 456 123 456,79",df.format(dvalue));
            assertEquals("123456123456.79",PerformanceStatisticUtil.format(dvalue));
            
            Locale.setDefault(new Locale("de","DE"));
            df = new DecimalFormat(pattern);
            assertEquals("123.456.123.456,79",df.format(dvalue));
            assertEquals("123456123456.79",PerformanceStatisticUtil.format(dvalue));
            
            
            DecimalFormat dformat = (DecimalFormat)NumberFormat.getNumberInstance(new Locale("en","US"));
            assertEquals("123,456,123,456.789", dformat.format(dvalue));
            assertEquals("123456123456.79", PerformanceStatisticUtil.format(dvalue));
            
            dformat = (DecimalFormat)NumberFormat.getNumberInstance(new Locale("fr","FR"));
            assertEquals("123 456 123 456,789", dformat.format(dvalue));
            assertEquals("123456123456.79", PerformanceStatisticUtil.format(dvalue));
            
            dformat = (DecimalFormat)NumberFormat.getNumberInstance(new Locale("de","DE"));
            assertEquals("123.456.123.456,789", dformat.format(dvalue));
            assertEquals("123456123456.79", PerformanceStatisticUtil.format(dvalue));
        } finally {
            Locale.setDefault(defaultLocale);
        }
    }
}
