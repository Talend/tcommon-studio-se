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
package org.talend.core.ui.token;

import us.monoid.json.JSONObject;

/**
 * ggu class global comment. Detailled comment
 */
public interface ITokenCollector {

    /**
     * 
     * ggu Comment method "priorCollect".
     * 
     * main for special collect, after logon project.
     */
    public void priorCollect() throws Exception;

    /**
     * 
     * ggu Comment method "collect".
     * 
     * collect the values from different provider.
     */
    public JSONObject collect() throws Exception;
}
