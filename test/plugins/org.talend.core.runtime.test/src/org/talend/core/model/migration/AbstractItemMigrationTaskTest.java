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
package org.talend.core.model.migration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.repository.ERepositoryObjectType;

/**
 * created by sgandon on 7 avr. 2014 Detailled comment
 * 
 */
public class AbstractItemMigrationTaskTest {

    /**
     * created by sgandon on 23 avr. 2014 Detailled comment
     * 
     */
    public class JobAndCodeItemMigrationTask extends AbstractItemMigrationTask {

        @Override
        public Date getOrder() {
            return null;
        }

        @Override
        public ExecutionResult execute(Item item) {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.migration.AbstractItemMigrationTask#getTypes()
         */
        @Override
        public List<ERepositoryObjectType> getTypes() {
            return Arrays.asList(ERepositoryObjectType.JOB_DOC, ERepositoryObjectType.CODE);
        }
    }

    public class JobDocItemMigrationTask extends AbstractItemMigrationTask {

        @Override
        public Date getOrder() {
            return null;
        }

        @Override
        public ExecutionResult execute(Item item) {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.migration.AbstractItemMigrationTask#getTypes()
         */
        @Override
        public List<ERepositoryObjectType> getTypes() {
            return Arrays.asList(ERepositoryObjectType.JOB_DOC);
        }
    }

    final JobAndCodeItemMigrationTask jobAndCodeItemMigrationTask = new JobAndCodeItemMigrationTask();

    final AbstractItemMigrationTask noTypeItemMigrationTask = new AbstractItemMigrationTask() {

        @Override
        public Date getOrder() {
            return null;
        }

        @Override
        public ExecutionResult execute(Item item) {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.talend.core.model.migration.AbstractItemMigrationTask#getTypes()
         */
        @Override
        public List<ERepositoryObjectType> getTypes() {
            return Collections.EMPTY_LIST;
        }
    };

    @Test
    public void testGetAllTypes() {
        List<ERepositoryObjectType> allTypes = jobAndCodeItemMigrationTask.getAllTypes();
        assertEquals(6, allTypes.size());// 2 original types and 4 extended types

        List<ERepositoryObjectType> allDocTypes = new JobDocItemMigrationTask().getAllTypes();
        assertEquals(4, allDocTypes.size());// 1 original type and 3 extended types
}

    @Test
    public void testGetAllTypesEmptyType() {
        List<ERepositoryObjectType> allTypes = noTypeItemMigrationTask.getAllTypes();
        assertTrue(allTypes.isEmpty());
    }

    @Test
    public void testExecuteProjectItem() {
        // we use a context item to simulate an JOB_DOC_EXT item type but I do not want to create a new type so I have
        // declared an ContextItem2JobDocExtContentHandler
        // to return the type JOB_DOC_EXT for this item to simulate an item of JOB_DOC_EXT type.
        ContextItem jobDocExtItem = PropertiesFactory.eINSTANCE.createContextItem();
        // we set the property to avoid an NPE with Mokito framework
        jobDocExtItem.setProperty(PropertiesFactory.eINSTANCE.createProperty());
        // we use a migration task that is migrate JOB_DOC only and we will check that it will migrate our jobDocExtItem
        // to because we have extended the JOB_DOC type
        JobDocItemMigrationTask jobDocItemMigrationTaskSpy = spy(new JobDocItemMigrationTask());
        jobDocItemMigrationTaskSpy.execute(jobDocExtItem);
        verify(jobDocItemMigrationTaskSpy).execute(jobDocExtItem);
    }
}
