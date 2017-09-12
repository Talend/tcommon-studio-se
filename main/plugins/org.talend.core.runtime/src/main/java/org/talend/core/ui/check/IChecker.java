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
package org.talend.core.ui.check;

/**
 * 
 * created by ycbai on 2015年10月9日 Detailled comment
 *
 */
public interface IChecker {

    public void setListener(ICheckListener listener);

    public void updateStatus(int level, final String statusLabelText);

    public boolean isStatusOnError();

    public String getStatus();

    public int getStatusLevel();

}
