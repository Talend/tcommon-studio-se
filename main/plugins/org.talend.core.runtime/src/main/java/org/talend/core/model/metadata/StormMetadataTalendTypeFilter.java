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
package org.talend.core.model.metadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StormMetadataTalendTypeFilter extends SparkMetadataTalendTypeFilter {

    private final static List<String> STORM_UNSUPPORTED_TYPES = Arrays.asList(new String[] { "Document", "Dynamic", "Vector" }); //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * @param componentName, the current component name the filter applies on.
     */
    public StormMetadataTalendTypeFilter(String componentName) {
        super(componentName);
    }

    @Override
    protected List<String> getUnsupportedTypes() {
        List<String> output = new ArrayList<>();
        output.addAll(super.getUnsupportedTypes());
        output.addAll(STORM_UNSUPPORTED_TYPES);
        return output;
    }

}
