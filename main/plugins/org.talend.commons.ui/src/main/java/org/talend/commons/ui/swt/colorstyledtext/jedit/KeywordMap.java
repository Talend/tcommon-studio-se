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
package org.talend.commons.ui.swt.colorstyledtext.jedit;

import java.util.HashMap;
import java.util.Map;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: KeywordMap.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class KeywordMap {

    protected Map keywords;

    protected boolean ignoreCase;

    public KeywordMap(boolean ignoreCase) {
        super();
        keywords = new HashMap();
        this.ignoreCase = ignoreCase;
    }

    @SuppressWarnings("unchecked")
    public void put(Object key, Object value) {
        keywords.put(key, value);
    }

    public boolean ignoreCase() {
        return ignoreCase;
    }

    public String[] get(Object key) {
        return (String[]) keywords.get(key);
    }

}
