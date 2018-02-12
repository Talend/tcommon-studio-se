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
package org.talend.commons.ui.swt.extended.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HadoopPropertiesFieldModel extends ExtendedTableModel<Map<String, Object>> {

    public HadoopPropertiesFieldModel(String name) {
        super(name);
        setProperties(new ArrayList<Map<String, Object>>());
    }

    public HadoopPropertiesFieldModel(List<Map<String, Object>> propertiesTypeList, String name) {
        super(name);
        setProperties(propertiesTypeList);
    }

    public void setProperties(List<Map<String, Object>> properties) {
        registerDataList(properties);
    }

    public Map<String, Object> createHadoopPropertiesType() {
        return new HashMap<String, Object>();
    }
}
