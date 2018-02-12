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
package org.talend.core.runtime.dynamic;

import java.util.Map;

import us.monoid.json.JSONObject;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public interface IDynamicAttribute {

    public void setAttribute(String key, Object value);

    public Object getAttribute(String key);

    public Map<String, Object> getAttributes();

    public Object removeAttribute(String key);

    public int getChildIndex(IDynamicAttribute child);

    public String getTagName();

    public JSONObject toXmlJson() throws Exception;
}
