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
package org.talend.cwm.indicator;

/**
 * created by talend on Aug 22, 2014 Detailled comment
 * 
 */
public interface DataValidation {

    /**
     * 
     * When MapDB is used some different indicator maybe used same data file.then the indicator need to implements it let me know
     * what data is for it
     * 
     * @param inputData
     * @return
     */
    boolean isValid(Object inputData);

    /**
     * 
     * MapDB is will keep key and value this method is uesed to judge which one should be check
     * 
     * 
     * @param inputData
     * @return
     */
    boolean isCheckKey();

    /**
     * 
     * MapDB is will keep key and value this method is uesed to setting which one should be check
     * 
     * 
     * @param inputData
     * @return
     */
    void setCheckKey(boolean checkKey);

}
