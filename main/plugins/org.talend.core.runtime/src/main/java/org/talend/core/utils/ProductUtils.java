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
package org.talend.core.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * created by ggu on Apr 3, 2014 Detailled comment
 * 
 */
public final class ProductUtils {

    /*
     * perspectives.
     */
    public static final String PERSPECTIVE_DI_ID = "org.talend.rcp.perspective"; //$NON-NLS-1$

    public static final String PERSPECTIVE_DQ_ID = "org.talend.dataprofiler.DataProfilingPerspective"; //$NON-NLS-1$

    public static final String PERSPECTIVE_MDM_ID = "org.talend.mdm.perspective"; //$NON-NLS-1$

    public static final String PERSPECTIVE_MAPPING_ID = "com.oaklandsw.transform.perspective"; //$NON-NLS-1$

    /*
     * products.
     */
    public static final String PROD_DI = "DI"; //$NON-NLS-1$

    public static final String PROD_CAMEL = "CAMEL"; //$NON-NLS-1$

    public static final String PROD_DQ = "DQ"; //$NON-NLS-1$

    public static final String PROD_MDM = "MDM"; //$NON-NLS-1$

    public static final String PROD_MAPPING = "MAPPING"; //$NON-NLS-1$

    /*
     * mapping.
     */
    private static Map<String, String> productPerspectiveMap = new HashMap<String, String>();
    static {
        productPerspectiveMap.put(PROD_DI, PERSPECTIVE_DI_ID);
        productPerspectiveMap.put(PROD_DQ, PERSPECTIVE_DQ_ID);
        productPerspectiveMap.put(PROD_MDM, PERSPECTIVE_MDM_ID);
        productPerspectiveMap.put(PROD_MAPPING, PERSPECTIVE_MAPPING_ID);
    }

    /*
     * 
     */
    public static String getPerspectiveId(String product) {
        if (product != null) {
            return productPerspectiveMap.get(product);
        }
        return null;
    }
}
