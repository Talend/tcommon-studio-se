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
package org.talend.core.service;

import org.talend.core.IService;

/**
 * 
 * DOC hcyi class global comment. Detailled comment
 */
public interface IExchangeService extends IService {

    /*
     * Returns selected file
     */
    public String openExchangeDialog();

    public void openExchangeEditor();

    public String checkUserAndPass(String username, String password);
}
