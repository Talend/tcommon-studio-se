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
package org.talend.core.model.process;

import java.util.List;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class UniqueNodeNameGenerator {

    public static String generateUniqueNodeName(String baseName, List<String> uniqueNodeNameList) {
        if (baseName == null) {
            throw new IllegalArgumentException("Component name can't be null"); //$NON-NLS-1$
        }
        String uniqueName = baseName + "_" + 1; //$NON-NLS-1$

        int counter = 1;
        boolean exists = true;
        while (exists) {
            exists = uniqueNodeNameList.contains(uniqueName);
            if (!exists) {
                break;
            }
            uniqueName = baseName + "_" + counter++; //$NON-NLS-1$
        }
        return uniqueName;
    }

}
