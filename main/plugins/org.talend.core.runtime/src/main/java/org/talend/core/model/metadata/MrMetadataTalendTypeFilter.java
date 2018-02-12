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
package org.talend.core.model.metadata;

import java.util.Arrays;
import java.util.List;

public class MrMetadataTalendTypeFilter extends MetadataTalendTypeFilter {

    private final static List<String> UNSUPPORTED_TYPES = Arrays.asList(new String[] {
            "Document", "Dynamic", "List", "Object", "Vector" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

    @Override
    protected List<String> getUnsupportedTypes() {
        return UNSUPPORTED_TYPES;
    }

}
