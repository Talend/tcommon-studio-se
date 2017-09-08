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
package org.talend.migration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.talend.core.model.repository.ERepositoryObjectType;

/**
 * created by sgandon on 7 avr. 2014 Detailled comment
 * 
 */
public class MigrationTaskExtensionEPReaderTest {

    /**
     * Test method for {@link org.talend.migration.MigrationTaskExtensionEPReader#getMigrationTaskExtension()}.
     * 
     * @throws IOException
     */
    @Test
    public void testGetMigrationTaskExtensions() throws IOException {
        MigrationTaskExtensionEPReader migrationTaskExtensionEPReader = new MigrationTaskExtensionEPReader();
        Map<ERepositoryObjectType, List<ERepositoryObjectType>> migrationTaskExtensions = migrationTaskExtensionEPReader
                .getMigrationTaskExtensions();
        assertFalse(migrationTaskExtensions.isEmpty());
        List<ERepositoryObjectType> listOfExtensions = migrationTaskExtensions.get(ERepositoryObjectType.JOB_DOC);
        assertNotNull(listOfExtensions);
        assertThat(listOfExtensions.size(), equalTo(3)); //$NON-NLS-1$
        assertThat(listOfExtensions, hasItem(ERepositoryObjectType.getType("JOB_EXT")));
        assertThat(listOfExtensions, hasItem(ERepositoryObjectType.getType("JOB_EXT2")));
        assertThat(listOfExtensions, hasItem(ERepositoryObjectType.getType("JOB_DOC_EXT")));
    }

    @Test
    public void testGetMigrationTaskExtensionsWithNoExtension() throws IOException {
        MigrationTaskExtensionEPReader migrationTaskExtensionEPReader = new MigrationTaskExtensionEPReader() {

            /*
             * (non-Javadoc)
             * 
             * @see org.talend.core.utils.RegistryReader#readRegistry()
             */
            @Override
            public void readRegistry() {// do nothing to simulate that no extension is foundd.
            }
        };
        Map<ERepositoryObjectType, List<ERepositoryObjectType>> migrationTaskExtensions = migrationTaskExtensionEPReader
                .getMigrationTaskExtensions();
        assertTrue(migrationTaskExtensions.isEmpty());
    }

    @Test
    public void testGetObjectTypeExtensionsOneType() {
        MigrationTaskExtensionEPReader migrationTaskExtensionEPReader = new MigrationTaskExtensionEPReader();
        Set<ERepositoryObjectType> objectTypeExtensions = migrationTaskExtensionEPReader.getObjectTypeExtensions(Collections
                .singleton(ERepositoryObjectType.JOB_DOC));
        assertEquals(3, objectTypeExtensions.size());
        assertThat(objectTypeExtensions, hasItem(ERepositoryObjectType.getType("JOB_EXT")));
        assertThat(objectTypeExtensions, hasItem(ERepositoryObjectType.getType("JOB_EXT2")));
        assertThat(objectTypeExtensions, hasItem(ERepositoryObjectType.getType("JOB_DOC_EXT")));
        objectTypeExtensions = migrationTaskExtensionEPReader.getObjectTypeExtensions(Collections
                .singleton(ERepositoryObjectType.BUSINESS_PROCESS));
        assertTrue(objectTypeExtensions.isEmpty());
    }

    @Test
    public void testGetObjectTypeExtensions2TypesType() {
        MigrationTaskExtensionEPReader migrationTaskExtensionEPReader = new MigrationTaskExtensionEPReader();
        Set<ERepositoryObjectType> objectTypeExtensions = migrationTaskExtensionEPReader.getObjectTypeExtensions(Arrays.asList(
                ERepositoryObjectType.JOB_DOC, ERepositoryObjectType.CODE));
        assertEquals(4, objectTypeExtensions.size());
        assertThat(objectTypeExtensions, hasItem(ERepositoryObjectType.getType("JOB_EXT")));
        assertThat(objectTypeExtensions, hasItem(ERepositoryObjectType.getType("JOB_EXT2")));
        assertThat(objectTypeExtensions, hasItem(ERepositoryObjectType.getType("JOB_DOC_EXT")));
        assertThat(objectTypeExtensions, hasItem(ERepositoryObjectType.getType("CODE_EXT")));
        objectTypeExtensions = migrationTaskExtensionEPReader.getObjectTypeExtensions(Collections
                .singleton(ERepositoryObjectType.BUSINESS_PROCESS));
        assertTrue(objectTypeExtensions.isEmpty());
    }

}
