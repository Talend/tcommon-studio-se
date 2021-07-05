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
package org.talend.core.model.metadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SparkMetadataTalendTypeFilter extends MetadataTalendTypeFilter {

    // Temporary activate Dynamic type for all components - TODO To be removed later
    private final static List<String> UNSUPPORTED_TYPES = Arrays.asList(new String[] { "Document" }); //$NON-NLS-1$

    private final static String ROWGENERATOR_COMPONENT_NAME = "tRowGenerator"; //$NON-NLS-1$

    protected final static String INPUTPARQUET_COMPONENT_NAME = "tFileInputParquet"; //$NON-NLS-1$

    protected final static String INPUTSTREAMPARQUET_COMPONENT_NAME = "tFileStreamInputParquet"; //$NON-NLS-1$

    protected final static String OUTPUTPARQUET_COMPONENT_NAME = "tFileOutputParquet"; // ; //$NON-NLS-1$

    private final static String INPUTCASSANDRA_COMPONENT_NAME = "tCassandraInput"; //$NON-NLS-1$

    private final static String LOOKUPINPUTCASSANDRA_COMPONENT_NAME = "tCassandraLookupInput"; //$NON-NLS-1$

    private final static String OUTPUTCASSANDRA_COMPONENT_NAME = "tCassandraOutput"; //$NON-NLS-1$

    private final static String INPUTKUDU_COMPONENT_NAME = "tKuduInput"; //$NON-NLS-1$

    private final static String OUTPUTKUDU_COMPONENT_NAME = "tKuduOutput"; //$NON-NLS-1$

    private final static String INPUTJDBC_COMPONENT_NAME = "tJDBCInput"; //$NON-NLS-1$

    private final static String OUTPUTJDBC_COMPONENT_NAME = "tJDBCOutput"; //$NON-NLS-1$

    protected final static Map<String, List<String>> COMPONENT_UNSUPPORTED_TYPES = new HashMap<>();

    // Contains types only supported for specific components
    protected final static Map<String, List<String>> COMPONENT_SUPPORTED_TYPES = new HashMap<>();

    protected final String mComponentName;

    static {
        COMPONENT_UNSUPPORTED_TYPES.put(ROWGENERATOR_COMPONENT_NAME, Arrays.asList(new String[] { "Object" })); //$NON-NLS-1$
        COMPONENT_UNSUPPORTED_TYPES.put(INPUTPARQUET_COMPONENT_NAME, Arrays.asList(new String[] { "Object", "List", "Vector" })); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        COMPONENT_UNSUPPORTED_TYPES.put(OUTPUTPARQUET_COMPONENT_NAME, Arrays.asList(new String[] { "Object", "List", "Vector" })); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        COMPONENT_UNSUPPORTED_TYPES.put(INPUTSTREAMPARQUET_COMPONENT_NAME,
                Arrays.asList(new String[] { "Object", "List", "Vector" })); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        COMPONENT_UNSUPPORTED_TYPES.put(INPUTCASSANDRA_COMPONENT_NAME,
                Arrays.asList(new String[] { "Object", "List", "Vector" })); //$NON-NLS-1$ //$NON-NLS-2$
        COMPONENT_UNSUPPORTED_TYPES.put(LOOKUPINPUTCASSANDRA_COMPONENT_NAME,
                Arrays.asList(new String[] { "Object", "List", "Vector" })); //$NON-NLS-1$ //$NON-NLS-2$
        COMPONENT_UNSUPPORTED_TYPES.put(OUTPUTCASSANDRA_COMPONENT_NAME,
                Arrays.asList(new String[] { "Object", "List", "Vector" })); //$NON-NLS-1$ //$NON-NLS-2$
        COMPONENT_UNSUPPORTED_TYPES.put(INPUTKUDU_COMPONENT_NAME,
                Arrays.asList(new String[] { "Object", "List", "Vector", "byte[]", "BigDecimal" })); //$NON-NLS-1$ //$NON-NLS-2$
        COMPONENT_UNSUPPORTED_TYPES.put(OUTPUTKUDU_COMPONENT_NAME,
                Arrays.asList(new String[] { "Object", "List", "Vector", "byte[]", "BigDecimal" })); //$NON-NLS-1$ //$NON-NLS-2$

        COMPONENT_SUPPORTED_TYPES.put(INPUTJDBC_COMPONENT_NAME, Arrays.asList("Dynamic")); //$NON-NLS-1$
        COMPONENT_SUPPORTED_TYPES.put(OUTPUTJDBC_COMPONENT_NAME, Arrays.asList("Dynamic")); //$NON-NLS-1$
    }

    public SparkMetadataTalendTypeFilter(String componentName) {
        this.mComponentName = componentName;
    }

    @Override
    protected List<String> getUnsupportedTypes() {
        List<String> unsupportedTypes = new ArrayList<String>(UNSUPPORTED_TYPES);

        // Add component specific unsupported types
        List<String> currentComponentUnsupportedType = COMPONENT_UNSUPPORTED_TYPES.get(this.mComponentName);
        if (currentComponentUnsupportedType != null) {
            unsupportedTypes.addAll(currentComponentUnsupportedType);
        }

        // Remove component specific supported types
        List<String> currentComponentSupportedType = COMPONENT_SUPPORTED_TYPES.get(this.mComponentName);
        if (currentComponentSupportedType != null) {
            unsupportedTypes.removeIf(item -> currentComponentSupportedType.contains(item));
        }

        return unsupportedTypes;
    }

}
