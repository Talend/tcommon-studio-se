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
package org.talend.login;

import java.util.Date;
import java.util.GregorianCalendar;

public abstract class AbstractLoginTask implements ILoginTask {

    @Override
    public boolean isCommandlineTask() {
        return false;
    }

    @Override
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2015, 1, 1, 12, 0, 0);
        return gc.getTime();
    }

}
