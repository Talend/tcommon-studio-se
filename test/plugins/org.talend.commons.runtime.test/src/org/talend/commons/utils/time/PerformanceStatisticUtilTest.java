// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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
        try {
            Locale.setDefault(new Locale("en","US"));
            assertEquals(new Locale("en","US"), Locale.getDefault());
            assertEquals("123456123456.79",PerformanceStatisticUtil.format(dvalue));
            
            Locale.setDefault(new Locale("fr","FR"));
            assertEquals(new Locale("fr","FR"), Locale.getDefault());
            assertEquals("123456123456.79",PerformanceStatisticUtil.format(dvalue));
            
            Locale.setDefault(new Locale("de","DE"));
            assertEquals(new Locale("de","DE"), Locale.getDefault());
            assertEquals("123456123456.79",PerformanceStatisticUtil.format(dvalue));
        } finally {
            Locale.setDefault(defaultLocale);
        }
    }
}
