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
package org.talend.repository.items.importexport.wizard.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.Path;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.ui.ITestContainerProviderService;
import org.talend.repository.items.importexport.handlers.model.ImportItem;

/**
 * created by cmeng on Jan 18, 2016
 * Detailled comment
 *
 */
@SuppressWarnings("nls")
@RunWith(PowerMockRunner.class)
@PrepareForTest({ GlobalServiceRegister.class })
public class ImportNodesBuilderTest {

    /**
     * Test case for TDI-35088
     */
    @Test
    public void testImportWithoutTestContainerLoaded() {
        // 1. mock GlobalServiceRegister
        PowerMockito.mockStatic(GlobalServiceRegister.class);
        GlobalServiceRegister globalServiceRegister = PowerMockito.mock(GlobalServiceRegister.class);
        PowerMockito.when(GlobalServiceRegister.getDefault()).thenReturn(globalServiceRegister);
        PowerMockito.when(globalServiceRegister.isServiceRegistered(ITestContainerProviderService.class)).thenReturn(false);
        PowerMockito.when(globalServiceRegister.getService(ITestContainerProviderService.class)).thenReturn(null);

        // 2. prepare data
        ImportNodesBuilder importNodesBuilder = new ImportNodesBuilder();
        ProcessItem processItem = PropertiesFactory.eINSTANCE.createProcessItem();
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        property.setLabel("testJob");
        processItem.setProperty(property);
        ImportItem importItem = new ImportItem(new Path("dummy"));
        importItem.setProperty(processItem.getProperty());
        List<ImportItem> importItems = new ArrayList<ImportItem>();
        importItems.add(importItem);

        // 3. test
        importNodesBuilder.addItems(importItems);
        List<ImportItem> importItemRecords = Arrays.asList(importNodesBuilder.getAllImportItemRecords());
        Assert.assertTrue(importItemRecords.contains(importItem));
    }
}
