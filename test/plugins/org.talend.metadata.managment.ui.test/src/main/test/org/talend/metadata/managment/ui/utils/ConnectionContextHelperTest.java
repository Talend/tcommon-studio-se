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
package org.talend.metadata.managment.ui.utils;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.junit.Test;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.context.JobContext;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.context.JobContextParameter;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;

/**
 * created by ycbai on 2016年9月2日
 * Detailled comment
 *
 */
public class ConnectionContextHelperTest {

    @Test
    public void testIsContextMode() {
        String testValue = "value1"; //$NON-NLS-1$
        String testContextValue = "context.value1"; //$NON-NLS-1$

        Connection connection = ConnectionFactory.eINSTANCE.createDatabaseConnection();
        connection.setContextMode(false);
        assertFalse(ConnectionContextHelper.isContextMode(connection, testValue));
        assertFalse(ConnectionContextHelper.isContextMode(connection, testContextValue));

        connection.setContextMode(true);
        assertFalse(ConnectionContextHelper.isContextMode(connection, testValue));
        assertTrue(ConnectionContextHelper.isContextMode(connection, testContextValue));
    }
    
    @Test
    public void testCheckAndAddContextVariables(){
        JobContextManager contextManager = new JobContextManager();
        List<IContextParameter> contextParameterList = contextManager.getDefaultContext().getContextParameterList();
        // create context parameters
        IContextParameter contextParam = new JobContextParameter();
        contextParam.setName("new1");
        contextParam.setType(JavaTypesManager.getDefaultJavaType().getId());
        contextParam.setValue("abc");
        contextParameterList.add(contextParam);
        EList contextTypeList = new BasicEList();
        // create some existed context group
        ContextType contextType = TalendFileFactory.eINSTANCE.createContextType();
        contextType.setName("Default");
        contextTypeList.add(contextType);

        // create existed context parameter
        ContextParameterType contextParameterType = TalendFileFactory.eINSTANCE.createContextParameterType();
        contextParameterType.setName("new1");
        contextParameterType.setValue("abc");
        contextParameterType.setType(JavaTypesManager.getDefaultJavaType().getId());
        contextType.getContextParameter().add(contextParameterType);
        
        contextParameterType = TalendFileFactory.eINSTANCE.createContextParameterType();
        contextParameterType.setName("new2");
        contextParameterType.setValue("abc");
        contextParameterType.setType(JavaTypesManager.getDefaultJavaType().getId());
        contextType.getContextParameter().add(contextParameterType);
        
        contextParameterType = TalendFileFactory.eINSTANCE.createContextParameterType();
        contextParameterType.setName("new3");
        contextParameterType.setValue("abc");
        contextParameterType.setType(JavaTypesManager.getDefaultJavaType().getId());
        contextType.getContextParameter().add(contextParameterType);
        
        ContextItem contextItem = PropertiesFactory.eINSTANCE.createContextItem();
        contextItem.getContext().add(contextType);
        Property myContextProperty = PropertiesFactory.eINSTANCE.createProperty();
        myContextProperty.setId("_DHiJ0KPlEeGSwOgmctA-XA");
        myContextProperty.setLabel("testContext");
        myContextProperty.setVersion("0.1");
        contextItem.setProperty(myContextProperty);
        contextItem.setDefaultContext("Default");
        
        Set<String> set = ConnectionContextHelper.checkAndAddContextVariables(contextItem, contextManager, false);
        assertTrue(set.size() == 2);
    }

}
