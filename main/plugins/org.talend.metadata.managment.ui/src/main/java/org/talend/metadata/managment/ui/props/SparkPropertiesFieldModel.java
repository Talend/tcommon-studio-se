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
package org.talend.metadata.managment.ui.props;

import java.util.List;
import java.util.Map;

/**
 * created by hcyi on Feb 23, 2017 Detailled comment
 *
 */
public class SparkPropertiesFieldModel extends PropertiesFieldModel {

    public SparkPropertiesFieldModel(List<Map<String, Object>> propertiesTypeList, String name) {
        super(propertiesTypeList, name);
    }

    @Override
    public void addAll(final Integer index, List<Map<String, Object>> beans, boolean fireBefore, boolean fireAfter) {
        super.superAddAll(index, beans, fireBefore, fireAfter);
    }
}
