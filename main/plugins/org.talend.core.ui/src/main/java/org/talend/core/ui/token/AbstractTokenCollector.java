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
public abstract class AbstractTokenCollector implements ITokenCollector {

    protected static final TokenKey PROJECTS_REPOSITORY = new TokenKey("projects.repository"); //$NON-NLS-1$

    public AbstractTokenCollector() {
        //
    }

    public void priorCollect() throws Exception {
        // default, nothing to do
    }

    public JSONObject collect() throws Exception {
        JSONObject result = new JSONObject();
        // default is empty

        return result;
    }
    
    @Deprecated
    protected void collectProperties(JSONObject propertiesObject) throws Exception {
    }
}
