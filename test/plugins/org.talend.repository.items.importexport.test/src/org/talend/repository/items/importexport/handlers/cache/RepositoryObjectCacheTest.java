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
package org.talend.repository.items.importexport.handlers.cache;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.routines.RoutinesUtil;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.designer.core.model.utils.emf.talendfile.ParametersType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.RoutinesParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;

/**
 * DOC zwxue class global comment. Detailled comment
 */
public class RepositoryObjectCacheTest {
    
    private RepositoryObjectCache roc;
    
    private ContextItem contextItem;
    
    private ProcessItem jobItem;
    
    private String contextId;
    
    private String jobId;
    
    private String contextName = "org_talend_context1";
    
    private String contextDisplayName = "org.talend.context1";
    
    private String jobName = "job1";
    
    private ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

    @Before
    public void setUp() throws Exception {
        roc = new RepositoryObjectCache();
        
        Property contextProperty = PropertiesFactory.eINSTANCE.createProperty();
        contextId = ProxyRepositoryFactory.getInstance().getNextId();
        contextProperty.setId(contextId);
        contextProperty.setLabel(contextName);
        contextProperty.setDisplayName(contextDisplayName);
        contextItem = PropertiesFactory.eINSTANCE.createContextItem();
        contextItem.setProperty(contextProperty);
        factory.create(contextItem, Path.EMPTY);

        Property jobProperty = PropertiesFactory.eINSTANCE.createProperty();
        jobId = ProxyRepositoryFactory.getInstance().getNextId();
        jobProperty.setId(jobId);
        jobProperty.setLabel(jobName);
        jobItem = PropertiesFactory.eINSTANCE.createProcessItem();
        jobItem.setProperty(jobProperty);
        
        ProcessType process = TalendFileFactory.eINSTANCE.createProcessType();
        ParametersType parameterType = TalendFileFactory.eINSTANCE.createParametersType();
        List<RoutinesParameterType> dependenciesInPreference = RoutinesUtil.createDependenciesInPreference();
        parameterType.getRoutinesParameter().addAll(dependenciesInPreference);
        process.setParameters(parameterType);
        jobItem.setProcess(process);
        
        factory.create(jobItem, Path.EMPTY);
        
    }

    @Test
    public void testAddToCache() {
        roc.addToCache(contextItem);
        validateResult(roc.getIdItemChache(), contextItem, contextId);
        validateResult(roc.getNameItemChache(), contextItem, contextName);
        
        roc.clear();
        roc.addToCache(jobItem);
        validateResult(roc.getIdItemChache(), jobItem, jobId);
        validateResult(roc.getNameItemChache(), jobItem, jobName);
    }

    @Test
    public void testInitialize() throws PersistenceException {
        roc.initialize(ERepositoryObjectType.CONTEXT);
        validateResult(roc.getIdItemChache(), contextItem, contextId);
        validateResult(roc.getNameItemChache(), contextItem, contextName);
        
        roc.clear();
        
        roc.initialize(ERepositoryObjectType.PROCESS);
        validateResult(roc.getIdItemChache(), jobItem, jobId);
        validateResult(roc.getNameItemChache(), jobItem, jobName);
    }

    private void validateResult(Map<String, List<IRepositoryViewObject>> cache, Item item, String key) {
        assertFalse(cache.isEmpty());
        List<IRepositoryViewObject> items = cache.get(key);
        assertTrue(items.size() == 1);
        IRepositoryViewObject obj = items.get(0);
        assertEquals(item, obj.getProperty().getItem());
    }
    
    private void deleteItem(Item item) throws PersistenceException {
        IRepositoryViewObject repObj = factory.getLastVersion(item.getProperty().getId());
        if (repObj != null) {
            factory.deleteObjectPhysical(repObj);
        }
    }
    

    @After
    public void tearDown() throws Exception {
        deleteItem(contextItem);
        deleteItem(jobItem);
    }

}
