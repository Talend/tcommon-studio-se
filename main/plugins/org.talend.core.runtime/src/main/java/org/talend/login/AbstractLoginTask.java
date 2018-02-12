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
package org.talend.login;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * created by wchen on 2015-5-15 Detailled comment
 *
 */
public abstract class AbstractLoginTask implements ILoginTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.login.ILoginTask#isCommandlineTask()
     */
    @Override
    public boolean isCommandlineTask() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.login.ILoginTask#getOrder()
     */
    @Override
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2015, 1, 1, 12, 0, 0);
        return gc.getTime();
    }

}
