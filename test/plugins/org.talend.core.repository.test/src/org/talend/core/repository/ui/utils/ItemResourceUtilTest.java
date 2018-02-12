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
package org.talend.core.repository.ui.utils;

import static org.junit.Assert.*;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.RepositoryObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.utils.ItemResourceUtil;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;

/**
 * DOC zwxue class global comment. Detailled comment
 */
public class ItemResourceUtilTest {

    private ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

    private Property property;

    String relativePath = "folder1/folder2";

    @Before
    public void setUp() throws Exception {
        property = PropertiesFactory.eINSTANCE.createProperty();
        property.setId(factory.getNextId());
        property.setLabel("test1");
        property.setVersion(VersionUtils.DEFAULT_VERSION);

        ProcessItem item = PropertiesFactory.eINSTANCE.createProcessItem();
        ProcessType process = TalendFileFactory.eINSTANCE.createProcessType();
        item.setProcess(process);
        item.setProperty(property);

        factory.create(item, new Path(relativePath));
    }

    @Test
    public void testGetItemRelativePath() throws PersistenceException {
        IPath path = ItemResourceUtil.getItemRelativePath(property);
        assertEquals(relativePath, path.toPortableString());
    }

    @After
    public void tearDown() throws Exception {
        factory.deleteObjectPhysical(new RepositoryObject(property));
    }
}
