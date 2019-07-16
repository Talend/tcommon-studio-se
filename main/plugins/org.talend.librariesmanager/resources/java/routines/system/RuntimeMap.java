// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package routines.system;

import java.util.HashMap;

/**
 * created by hwang on 2014-4-15 Detailled comment
 *
 */
public class RuntimeMap {

    public static java.util.Map<String, Object> globalTDMMap = null;

    private static RuntimeMap runtimeMap;

    public static synchronized RuntimeMap getInstance() {
        if (runtimeMap == null) {
            runtimeMap = new RuntimeMap();
        }
        return runtimeMap;
    }

    public java.util.Map<String, Object> getRuntimeMap() {
        if (globalTDMMap == null) {
            globalTDMMap = new HashMap<String, Object>();
        }
        return globalTDMMap;
    }

}
