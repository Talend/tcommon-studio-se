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

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

/**
 * created by rdubois on 30 juil. 2015 Detailled comment
 *
 */
public class MetadataTalendTypeFilterTest {

    private static final String INTEGER = "Integer"; //$NON-NLS-1$

    private static final String DOCUMENT = "Document"; //$NON-NLS-1$

    private static final String STRING = "String"; //$NON-NLS-1$

    private static final String OBJECT = "Object"; //$NON-NLS-1$

    private static final String LIST = "List"; //$NON-NLS-1$

    private static final String DOUBLE = "Double"; //$NON-NLS-1$

    private static final String SHORT = "Short"; //$NON-NLS-1$

    private static final String DYNAMIC = "Dynamic"; //$NON-NLS-1$

    private static final String VECTOR = "Vector"; //$NON-NLS-1$

    @Test
    public void filterStringTest() {
        String[] types;
        MetadataTalendTypeFilter dummyfilter = new DummyMetadataTalendTypeFilter();
        MetadataTalendTypeFilter mrfilter = new MrMetadataTalendTypeFilter();
        MetadataTalendTypeFilter sparkfilter = new SparkMetadataTalendTypeFilter(""); //$NON-NLS-1$
        MetadataTalendTypeFilter sparkWithtRowGeneratorfilter = new SparkMetadataTalendTypeFilter("tRowGenerator"); //$NON-NLS-1$
        MetadataTalendTypeFilter sparkWithtFileInputParquetfilter = new SparkMetadataTalendTypeFilter("tFileInputParquet"); //$NON-NLS-1$
        MetadataTalendTypeFilter stormfilter = new StormMetadataTalendTypeFilter(""); //$NON-NLS-1$

        types = new String[] { INTEGER, DOCUMENT, STRING, OBJECT, LIST, DOUBLE, SHORT, DYNAMIC, VECTOR };
        assertEquals(Arrays.asList(dummyfilter.filter(types)),
                Arrays.asList(new String[] { INTEGER, DOCUMENT, STRING, OBJECT, LIST, DOUBLE, SHORT, DYNAMIC }));
        assertEquals(Arrays.asList(mrfilter.filter(types)), Arrays.asList(new String[] { INTEGER, STRING, DOUBLE, SHORT }));
        assertEquals(Arrays.asList(sparkfilter.filter(types)),
                Arrays.asList(new String[] { INTEGER, STRING, OBJECT, LIST, DOUBLE, SHORT, VECTOR }));
        assertEquals(Arrays.asList(sparkWithtRowGeneratorfilter.filter(types)),
                Arrays.asList(new String[] { INTEGER, STRING, LIST, DOUBLE, SHORT, VECTOR }));
        assertEquals(Arrays.asList(sparkWithtFileInputParquetfilter.filter(types)),
                Arrays.asList(new String[] { INTEGER, STRING, DOUBLE, SHORT }));
        assertEquals(Arrays.asList(stormfilter.filter(types)),
                Arrays.asList(new String[] { INTEGER, STRING, OBJECT, LIST, DOUBLE, SHORT }));

        types = new String[] {};
        assertEquals(Arrays.asList(dummyfilter.filter(types)), Arrays.asList(new String[] {}));
        assertEquals(Arrays.asList(mrfilter.filter(types)), Arrays.asList(new String[] {}));
        assertEquals(Arrays.asList(sparkfilter.filter(types)), Arrays.asList(new String[] {}));
        assertEquals(Arrays.asList(sparkWithtRowGeneratorfilter.filter(types)), Arrays.asList(new String[] {}));
        assertEquals(Arrays.asList(sparkWithtFileInputParquetfilter.filter(types)), Arrays.asList(new String[] {}));
        assertEquals(Arrays.asList(stormfilter.filter(types)), Arrays.asList(new String[] {}));

        types = new String[] { INTEGER, STRING, DOUBLE, SHORT };
        assertEquals(Arrays.asList(dummyfilter.filter(types)), Arrays.asList(new String[] { INTEGER, STRING, DOUBLE, SHORT }));
        assertEquals(Arrays.asList(mrfilter.filter(types)), Arrays.asList(new String[] { INTEGER, STRING, DOUBLE, SHORT }));
        assertEquals(Arrays.asList(sparkfilter.filter(types)), Arrays.asList(new String[] { INTEGER, STRING, DOUBLE, SHORT }));
        assertEquals(Arrays.asList(sparkWithtRowGeneratorfilter.filter(types)),
                Arrays.asList(new String[] { INTEGER, STRING, DOUBLE, SHORT }));
        assertEquals(Arrays.asList(sparkWithtFileInputParquetfilter.filter(types)),
                Arrays.asList(new String[] { INTEGER, STRING, DOUBLE, SHORT }));
        assertEquals(Arrays.asList(stormfilter.filter(types)), Arrays.asList(new String[] { INTEGER, STRING, DOUBLE, SHORT }));

        types = new String[] { DOCUMENT, OBJECT, LIST, DYNAMIC, VECTOR };
        assertEquals(Arrays.asList(dummyfilter.filter(types)), Arrays.asList(new String[] { DOCUMENT, OBJECT, LIST, DYNAMIC }));
        assertEquals(Arrays.asList(mrfilter.filter(types)), Arrays.asList(new String[] {}));
        assertEquals(Arrays.asList(sparkfilter.filter(types)), Arrays.asList(new String[] { OBJECT, LIST, VECTOR }));
        assertEquals(Arrays.asList(sparkWithtRowGeneratorfilter.filter(types)), Arrays.asList(new String[] { LIST, VECTOR }));
        assertEquals(Arrays.asList(sparkWithtFileInputParquetfilter.filter(types)), Arrays.asList(new String[] {}));
        assertEquals(Arrays.asList(stormfilter.filter(types)), Arrays.asList(new String[] { OBJECT, LIST }));

    }

}
