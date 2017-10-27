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
package org.talend.commons.utils.threading.locker.operators;

import org.talend.commons.utils.threading.Locker;
import org.talend.commons.utils.threading.threadsafetester.IThreadSafetyOperator;


public abstract class AbstractLockerOperator implements IThreadSafetyOperator {

    protected Locker locker;

    protected int nOperationsByOperator;

    protected ResultContainer resultContainer;

    /**
     * Sets the locker.
     * 
     * @param locker the locker to set
     */
    public void setLocker(Locker locker) {
        this.locker = locker;
    }

    /**
     * Sets the nOperationsByOperator.
     * 
     * @param nOperationsByOperator the nOperationsByOperator to set
     */
    public void setnOperationsByOperator(int nOperationsByOperator) {
        this.nOperationsByOperator = nOperationsByOperator;
    }

    /**
     * Sets the resultContainer.
     * 
     * @param resultContainer the resultContainer to set
     */
    public void setResultContainer(ResultContainer resultContainer) {
        this.resultContainer = resultContainer;
    }

}
