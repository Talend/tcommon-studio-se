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
import java.util.Collection;

import org.junit.Test;
import org.talend.core.model.utils.SQLFormatUtil;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class TestSQLFormatUtil {

    /**
     * Test method for {@link org.talend.core.utils.SQLFormatUtil#separate(java.lang.String, java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void testSeparate() throws Exception {
        SQLFormatUtil util = new SQLFormatUtil();
        Collection<String> res = util.separate("select * from table", "sel|table"); //$NON-NLS-1$ //$NON-NLS-2$
        System.out.println(res);
    }

}
