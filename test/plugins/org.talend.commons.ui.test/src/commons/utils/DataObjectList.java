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
package commons.utils;

import java.util.ArrayList;

/**
 * @version $Revision: 7038 $ ($Author: plegall $)
 */
public class DataObjectList extends ArrayList {

    /**
     * 
     */
    private static final long serialVersionUID = 7020347198188725677L;

    /**
     * 
     */
    private static final int A_100 = 100;

    /**
     */
    @SuppressWarnings("unchecked")
    public DataObjectList() {
        super();

        for (int j = 0; j < A_100; j++) {
            add(new DataObject());
        }
    }

}
