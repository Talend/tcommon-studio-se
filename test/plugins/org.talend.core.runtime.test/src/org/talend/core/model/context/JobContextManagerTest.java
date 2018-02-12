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
package org.talend.core.model.context;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.junit.Test;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextParameter;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class JobContextManagerTest {

    @Test
    public void testSaveToEmf_EmptyContextManager_EmptyEMF() {
        JobContextManager contextManager = new JobContextManager();
        assertThat("Contained only one default context group", contextManager.getListContext().size(), is(1));
        assertThat("Contained only one default context group", contextManager.getDefaultContext(), is(contextManager
                .getListContext().get(0)));
        assertThat("The name of  context is not right", contextManager.getDefaultContext().getName(), is(IContext.DEFAULT));

        contextManager.saveToEmf(null); // no error

        // test default
        EList contextTypeList = new BasicEList();
        contextManager.saveToEmf(contextTypeList);

        assertThat("Should only have one ContextType ", contextTypeList.size(), is(1));
        ContextType contextType = (ContextType) contextTypeList.get(0);
        assertThat("The name of Context is not right", contextType.getName(), is(IContext.DEFAULT));
        assertThat("No context parameter by default", contextType.getContextParameter().size(), is(0));

    }

    @Test
    public void testSaveToEmf_EmptyContextManager_SomeEMF() {
        JobContextManager contextManager = new JobContextManager();
        EList contextTypeList = new BasicEList();

        // create some exited context group
        ContextType contextType = TalendFileFactory.eINSTANCE.createContextType();
        contextType.setName("Test");
        contextTypeList.add(contextType);

        // create existed context param
        ContextParameterType contextParameterType = TalendFileFactory.eINSTANCE.createContextParameterType();
        contextParameterType.setName("new1");
        contextParameterType.setValue("123");
        contextParameterType.setType(JavaTypesManager.getDefaultJavaType().getId());
        contextType.getContextParameter().add(contextParameterType);

        contextParameterType = TalendFileFactory.eINSTANCE.createContextParameterType();
        contextParameterType.setName("new2");
        contextParameterType.setValue("/home/test");
        contextParameterType.setType(JavaTypesManager.FILE.getId());
        contextType.getContextParameter().add(contextParameterType);

        contextManager.saveToEmf(contextTypeList);

        assertThat(contextTypeList.size(), is(1));

        // default group
        contextType = (ContextType) contextTypeList.get(0);
        assertThat(contextType.getName(), is(IContext.DEFAULT));
        EList contextParameters = contextType.getContextParameter();
        assertThat(contextParameters.size(), is(0));
    }

    @Test
    public void testSaveToEmf_SomeInContextManager_EmptyEMF_OneGroup() {
        JobContextManager contextManager = new JobContextManager();
        List<IContextParameter> contextParameterList = contextManager.getDefaultContext().getContextParameterList();

        IContextParameter contextParam = new JobContextParameter();
        contextParam.setName("new1");
        contextParam.setType(JavaTypesManager.getDefaultJavaType().getId());
        contextParam.setValue("abc");
        contextParameterList.add(contextParam);

        contextParam = new JobContextParameter();
        contextParam.setName("new2");
        contextParam.setType(JavaTypesManager.getDefaultJavaType().getId());
        contextParam.setValue("xyz");
        contextParameterList.add(contextParam);

        contextParam = new JobContextParameter();
        contextParam.setName("host");
        contextParam.setType(JavaTypesManager.getDefaultJavaType().getId());
        contextParam.setValue("localhost");
        contextParameterList.add(contextParam);

        EList contextTypeList = new BasicEList();
        contextManager.saveToEmf(contextTypeList);

        assertThat("Should only have one ContextType ", contextTypeList.size(), is(1));
        ContextType contextType = (ContextType) contextTypeList.get(0);
        assertThat("The name of Context is not right", contextType.getName(), is(IContext.DEFAULT));
        EList contextParameter = contextType.getContextParameter();
        assertThat(contextParameter.size(), is(3));

        // index 1
        ContextParameterType contextParamType = (ContextParameterType) contextParameter.get(0);
        assertThat(contextParamType.getName(), is("new1"));
        assertThat(contextParamType.getValue(), is("abc"));

        // index 2
        contextParamType = (ContextParameterType) contextParameter.get(1);
        assertThat(contextParamType.getName(), is("new2"));
        assertThat(contextParamType.getValue(), is("xyz"));

        // index 2
        contextParamType = (ContextParameterType) contextParameter.get(2);
        assertThat(contextParamType.getName(), is("host"));
        assertThat(contextParamType.getValue(), is("localhost"));
    }

    @Test
    public void testSaveToEmf_SomeInContextManager_EmptyEMF_MultiGroup() {
        JobContextManager contextManager = createTestJobContextManager();

        EList contextTypeList = new BasicEList();
        contextManager.saveToEmf(contextTypeList);

        doEMFContextTest(contextTypeList);
    }

    @Test
    public void testSaveToEmf_SomeInContextManager_ContainedInEMF() {
        JobContextManager contextManager = createTestJobContextManager();

        EList contextTypeList = new BasicEList();

        // create some exited context group
        ContextType contextType = TalendFileFactory.eINSTANCE.createContextType();
        contextType.setName("Test");
        contextTypeList.add(contextType);

        // create existed context param
        ContextParameterType contextParameterType = TalendFileFactory.eINSTANCE.createContextParameterType();
        contextParameterType.setName("new1");
        contextParameterType.setValue("123");
        contextParameterType.setType(JavaTypesManager.getDefaultJavaType().getId());
        contextType.getContextParameter().add(contextParameterType);

        contextParameterType = TalendFileFactory.eINSTANCE.createContextParameterType();
        contextParameterType.setName("new2");
        contextParameterType.setValue("/home/test");
        contextParameterType.setType(JavaTypesManager.FILE.getId());
        contextType.getContextParameter().add(contextParameterType);

        contextManager.saveToEmf(contextTypeList);

        doEMFContextTest(contextTypeList);
    }

    @Test
    public void testSaveToEmf_SomeInContextManager_ContainedInEMF_DiffOrder() {
        JobContextManager contextManager = createTestJobContextManager();

        EList contextTypeList = new BasicEList();

        // create some exited context group
        ContextType contextType = TalendFileFactory.eINSTANCE.createContextType();
        contextType.setName("Test");
        contextTypeList.add(contextType);

        // create existed context param
        ContextParameterType contextParameterType = TalendFileFactory.eINSTANCE.createContextParameterType();
        contextParameterType.setName("new2");
        contextParameterType.setValue("/home/test");
        contextParameterType.setType(JavaTypesManager.FILE.getId());
        contextType.getContextParameter().add(contextParameterType);

        contextParameterType = TalendFileFactory.eINSTANCE.createContextParameterType();
        contextParameterType.setName("host");
        contextParameterType.setValue("unknown");
        contextParameterType.setType(JavaTypesManager.getDefaultJavaType().getId());
        contextType.getContextParameter().add(contextParameterType);

        contextParameterType = TalendFileFactory.eINSTANCE.createContextParameterType();
        contextParameterType.setName("new1");
        contextParameterType.setValue("123");
        contextParameterType.setType(JavaTypesManager.getDefaultJavaType().getId());
        contextType.getContextParameter().add(contextParameterType);

        contextManager.saveToEmf(contextTypeList);

        doEMFContextTest(contextTypeList);
    }

    @Test
    public void testSaveToEmf_SomeInContextManager_DiffGroupInEMF() {

        JobContextManager contextManager = createTestJobContextManager();

        EList contextTypeList = new BasicEList();

        // create some exited context group
        ContextType contextType = TalendFileFactory.eINSTANCE.createContextType();
        contextType.setName("Test");
        contextTypeList.add(contextType);

        // will be removed
        contextType = TalendFileFactory.eINSTANCE.createContextType();
        contextType.setName("Dev");
        contextTypeList.add(contextType);

        contextManager.saveToEmf(contextTypeList);

        doEMFContextTest(contextTypeList);

    }

    @Test
    public void testSaveToEmf_SomeInContextManager_DiffGroupAndParamsInEMF() {

        JobContextManager contextManager = createTestJobContextManager();

        EList contextTypeList = new BasicEList();

        // create some exited context group
        ContextType contextType = TalendFileFactory.eINSTANCE.createContextType();
        contextType.setName("Test");
        contextTypeList.add(contextType);

        // create existed context param
        ContextParameterType contextParameterType = TalendFileFactory.eINSTANCE.createContextParameterType();
        contextParameterType.setName("host");
        contextParameterType.setValue("localhost"); // will be reset
        contextParameterType.setType(JavaTypesManager.getDefaultJavaType().getId());
        contextType.getContextParameter().add(contextParameterType);

        contextParameterType = TalendFileFactory.eINSTANCE.createContextParameterType();
        contextParameterType.setName("new2");
        contextParameterType.setValue("/home/test"); // will be reset
        contextParameterType.setType(JavaTypesManager.FILE.getId()); // will be reset
        contextType.getContextParameter().add(contextParameterType);

        // will be removed
        contextParameterType = TalendFileFactory.eINSTANCE.createContextParameterType();
        contextParameterType.setName("port");
        contextParameterType.setValue("3306");
        contextParameterType.setType(JavaTypesManager.INTEGER.getId());
        contextType.getContextParameter().add(contextParameterType);

        // create another group
        contextType = TalendFileFactory.eINSTANCE.createContextType();
        contextType.setName("Dev");
        contextTypeList.add(contextType);

        // create existed context param
        contextParameterType = TalendFileFactory.eINSTANCE.createContextParameterType();
        contextParameterType.setName("host");
        contextParameterType.setValue("unknown"); // will be reset
        contextParameterType.setType(JavaTypesManager.getDefaultJavaType().getId());
        contextType.getContextParameter().add(contextParameterType);

        contextParameterType = TalendFileFactory.eINSTANCE.createContextParameterType();
        contextParameterType.setName("new2");
        contextParameterType.setValue("/home/test"); // will be reset
        contextParameterType.setType(JavaTypesManager.FILE.getId()); // will be reset
        contextType.getContextParameter().add(contextParameterType);

        // will be removed
        contextParameterType = TalendFileFactory.eINSTANCE.createContextParameterType();
        contextParameterType.setName("port");
        contextParameterType.setValue("10001");
        contextParameterType.setType(JavaTypesManager.INTEGER.getId());
        contextType.getContextParameter().add(contextParameterType);

        contextManager.saveToEmf(contextTypeList);

        doEMFContextTest(contextTypeList);
    }

    @Test
    public void testSaveToEMF_SomeInContextManager_RenameGroupAndParamName() {
        JobContextManager contextManager = new JobContextManager();
        List<IContextParameter> contextParameterList = contextManager.getDefaultContext().getContextParameterList();

        // create context parameters
        IContextParameter contextParam = new JobContextParameter();
        contextParam.setName("new");
        contextParam.setType(JavaTypesManager.getDefaultJavaType().getId());
        contextParam.setValue("abc");
        contextParameterList.add(contextParam);

        // create context group
        IContext testGroup = new JobContext("Test");
        contextManager.getListContext().add(testGroup);

        // create context parameter
        contextParam = new JobContextParameter();
        contextParam.setName("tnew");
        contextParam.setType(JavaTypesManager.getDefaultJavaType().getId());
        contextParam.setValue("tabc");
        testGroup.getContextParameterList().add(contextParam);

        EList contextTypeList = new BasicEList();
        // create some existed context group
        ContextType contextType = TalendFileFactory.eINSTANCE.createContextType();
        contextType.setName("Default");
        contextTypeList.add(contextType);

        // create existed context parameter
        ContextParameterType contextParameterType = TalendFileFactory.eINSTANCE.createContextParameterType();
        contextParameterType.setName("old");
        contextParameterType.setValue("abc");
        contextParameterType.setType(JavaTypesManager.getDefaultJavaType().getId());
        contextType.getContextParameter().add(contextParameterType);

        // create another group
        contextType = TalendFileFactory.eINSTANCE.createContextType();
        contextType.setName("Dev");
        contextTypeList.add(contextType);

        ContextParameterType contextParameterType2 = TalendFileFactory.eINSTANCE.createContextParameterType();
        contextParameterType2.setName("tnew");
        contextParameterType2.setValue("10001");
        contextParameterType2.setType(JavaTypesManager.INTEGER.getId());
        contextType.getContextParameter().add(contextParameterType2);

        contextManager.getNameMap().put("new", "old");
        contextManager.getRenameGroupContext().put(testGroup, "Dev");
        contextManager.saveToEmf(contextTypeList);
        // test rename parameter's name
        ContextType defaultContextGroup = (ContextType) contextTypeList.get(0);
        ContextParameterType newContextParameterType = (ContextParameterType) defaultContextGroup.getContextParameter().get(0);
        assertEquals("new", newContextParameterType.getName());
        assertSame(contextParameterType, newContextParameterType);
        // test rename context group's name
        ContextType newContextType = (ContextType) contextTypeList.get(1);
        assertEquals("Test", newContextType.getName());
        assertSame(contextType, newContextType);
    }

    private JobContextManager createTestJobContextManager() {
        JobContextManager contextManager = new JobContextManager();
        List<IContextParameter> contextParameterList = contextManager.getDefaultContext().getContextParameterList();

        // create context parameters
        IContextParameter contextParam = new JobContextParameter();
        contextParam.setName("new1");
        contextParam.setType(JavaTypesManager.getDefaultJavaType().getId());
        contextParam.setValue("abc");
        contextParameterList.add(contextParam);

        contextParam = new JobContextParameter();
        contextParam.setName("new2");
        contextParam.setType(JavaTypesManager.getDefaultJavaType().getId());
        contextParam.setValue("xyz");
        contextParameterList.add(contextParam);

        contextParam = new JobContextParameter();
        contextParam.setName("host");
        contextParam.setType(JavaTypesManager.getDefaultJavaType().getId());
        contextParam.setValue("localhost");
        contextParameterList.add(contextParam);

        // create context group
        IContext testGroup = new JobContext("Test");
        contextManager.getListContext().add(testGroup);

        // create context parameters
        contextParam = new JobContextParameter();
        contextParam.setName("new1");
        contextParam.setType(JavaTypesManager.getDefaultJavaType().getId());
        contextParam.setValue("tabc");
        testGroup.getContextParameterList().add(contextParam);

        contextParam = new JobContextParameter();
        contextParam.setName("new2");
        contextParam.setType(JavaTypesManager.getDefaultJavaType().getId());
        contextParam.setValue("txyz");
        testGroup.getContextParameterList().add(contextParam);

        contextParam = new JobContextParameter();
        contextParam.setName("host");
        contextParam.setType(JavaTypesManager.getDefaultJavaType().getId());
        contextParam.setValue("127.0.0.1");
        testGroup.getContextParameterList().add(contextParam);

        return contextManager;
    }

    private void doEMFContextTest(EList contextTypeList) {
        assertThat(contextTypeList.size(), is(2));

        // test default group
        ContextType contextType = (ContextType) contextTypeList.get(0);
        assertThat("The name of Context is not right", contextType.getName(), is(IContext.DEFAULT));
        EList contextParameters = contextType.getContextParameter();
        assertThat(contextParameters.size(), is(3));

        // index 1
        ContextParameterType contextParamType = (ContextParameterType) contextParameters.get(0);
        assertThat(contextParamType.getName(), is("new1"));
        assertThat(contextParamType.getValue(), is("abc"));

        // index 2
        contextParamType = (ContextParameterType) contextParameters.get(1);
        assertThat(contextParamType.getName(), is("new2"));
        assertThat(contextParamType.getValue(), is("xyz"));

        // index 2
        contextParamType = (ContextParameterType) contextParameters.get(2);
        assertThat(contextParamType.getName(), is("host"));
        assertThat(contextParamType.getValue(), is("localhost"));

        // test another group
        contextType = (ContextType) contextTypeList.get(1);
        assertThat("The name of Context is not right", contextType.getName(), is("Test"));
        contextParameters = contextType.getContextParameter();
        assertThat(contextParameters.size(), is(3));

        // index 1
        contextParamType = (ContextParameterType) contextParameters.get(0);
        assertThat(contextParamType.getName(), is("new1"));
        assertThat(contextParamType.getValue(), is("tabc"));

        // index 2
        contextParamType = (ContextParameterType) contextParameters.get(1);
        assertThat(contextParamType.getName(), is("new2"));
        assertThat(contextParamType.getValue(), is("txyz"));

        // index 2
        contextParamType = (ContextParameterType) contextParameters.get(2);
        assertThat(contextParamType.getName(), is("host"));
        assertThat(contextParamType.getValue(), is("127.0.0.1"));
    }
}
