// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.ui.context.cmd;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.commands.CommandStack;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.image.ImageUtils;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.components.ComponentCategory;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.context.JobContext;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.context.JobContextParameter;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryObject;
import org.talend.core.model.routines.RoutinesUtil;
import org.talend.core.repository.ui.view.RepositoryLabelProvider;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.ui.component.ComponentsFactoryProvider;
import org.talend.core.ui.context.ContextManagerHelper;
import org.talend.core.ui.context.IContextModelManager;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.ParametersType;
import org.talend.designer.core.model.utils.emf.talendfile.RoutinesParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;
import org.talend.designer.joblet.model.JobletFactory;
import org.talend.designer.joblet.model.JobletProcess;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * created by wchen on Aug 1, 2018 Detailled comment
 *
 */
public class AddRepositoryContextGroupCommandTest {

    IProxyRepositoryFactory factory = CoreUIPlugin.getDefault().getProxyRepositoryFactory();

    IRepositoryViewObject jobletForTest;

    IRepositoryViewObject contextObject;

    @Before
    public void setup() throws PersistenceException {
        List<IRepositoryViewObject> all = factory.getAll(ERepositoryObjectType.CONTEXT);
        for (IRepositoryViewObject obj : all) {
            if ("AddRepositoryContextGroupCommandTest_joblet".equals(obj.getLabel())) {
                factory.deleteObjectPhysical(obj);
            }
        }

    }

    @After
    public void tearDown() throws Exception {
        if (jobletForTest != null) {
            factory.deleteObjectPhysical(jobletForTest);
            IComponentsFactory components = ComponentsFactoryProvider.getInstance();
            IComponent jobletComponent = components.get(jobletForTest.getLabel(), ComponentCategory.CATEGORY_4_DI.getName());
            components.getComponents().remove(jobletComponent);
            factory.deleteObjectPhysical(contextObject);
        }
    }

    @Test
    public void testCommand() throws PersistenceException {
        jobletForTest = createRepositoryObject("AddRepositoryContextGroupCommandTest_joblet", factory.getNextId(),
                VersionUtils.DEFAULT_VERSION);
        String jobletId = jobletForTest.getId();

        ContextManager manager = new ContextManager();
        IContextManager contextManager = manager.getContextManager();
        // add a build-in context
        JobContextParameter buildInParam = new JobContextParameter();
        buildInParam.setName("buildIn");
        List<IContextParameter> contextParameterList = contextManager.getDefaultContext().getContextParameterList();
        contextParameterList.add(buildInParam);
        // add a joblet context
        JobContextParameter jobletParam = new JobContextParameter();
        jobletParam.setName("joblet");
        jobletParam.setSource(jobletId);
        contextParameterList.add(jobletParam);

        ContextManagerHelper helper = new ContextManagerHelper(contextManager);

        // add a repository context by command should not affact the existing joblet and build context param
        contextObject = createContextObject("AddRepositoryContextGroupCommandTest_context", factory.getNextId(),
                VersionUtils.DEFAULT_VERSION);
        ContextItem contextItem = (ContextItem) contextObject.getProperty().getItem();
        List<ContextItem> selectedItems = new ArrayList<ContextItem>();
        selectedItems.add(contextItem);
        Set<String> nameSet = new HashSet<String>();
        List<ContextParameterType> parameterList = new ArrayList<ContextParameterType>();
        parameterList.addAll(((ContextType) contextItem.getContext().get(0)).getContextParameter());
        AddRepositoryContextGroupCommand command = new AddRepositoryContextGroupCommand(null, manager, selectedItems, nameSet,
                helper, parameterList);
        command.execute();

        contextParameterList = contextManager.getDefaultContext().getContextParameterList();
        Assert.assertEquals(contextParameterList.size(), 3);
        Assert.assertEquals(contextParameterList.get(0).getName(), "buildIn");
        Assert.assertEquals(contextParameterList.get(1).getName(), "joblet");
        Assert.assertEquals(contextParameterList.get(2).getName(), "repository_new1");

    }

    private IRepositoryViewObject createContextObject(String label, String id, String version) throws PersistenceException {
        ContextItem contextItem = PropertiesFactory.eINSTANCE.createContextItem();
        Property contextProperty = PropertiesFactory.eINSTANCE.createProperty();
        contextProperty.setAuthor(
                ((RepositoryContext) CoreRuntimePlugin.getInstance().getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                        .getUser());
        contextProperty.setVersion(VersionUtils.DEFAULT_VERSION);
        contextProperty.setStatusCode(""); //$NON-NLS-1$
        contextProperty.setId(id);
        contextProperty.setLabel(label);
        contextProperty.setDisplayName(contextProperty.getLabel());
        contextItem.setProperty(contextProperty);
        contextItem.setDefaultContext("Default");

        ContextType contextType = TalendFileFactory.eINSTANCE.createContextType();
        contextItem.getContext().add(contextType);
        contextType.setName("Default");
        EList contextTypeParamList = contextType.getContextParameter();
        ContextParameterType contextParamType = TalendFileFactory.eINSTANCE.createContextParameterType();
        contextParamType.setName("repository_new1");
        contextParamType.setType("id_String");
        contextTypeParamList.add(contextParamType);
        factory.create(contextItem, new Path(""));
        return new RepositoryObject(contextProperty);

    }

    private IRepositoryViewObject createRepositoryObject(String label, String id, String version) throws PersistenceException {
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        property.setAuthor(
                ((RepositoryContext) CoreRuntimePlugin.getInstance().getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                        .getUser());
        property.setVersion(version);
        property.setStatusCode(""); //$NON-NLS-1$

        JobletProcessItem processItem = PropertiesFactory.eINSTANCE.createJobletProcessItem();
        ByteArray ba = PropertiesFactory.eINSTANCE.createByteArray();
        processItem.setIcon(ba);
        processItem.getIcon()
                .setInnerContent(ImageUtils.saveImageToData(RepositoryLabelProvider.getDefaultJobletImage(processItem)));

        processItem.setProperty(property);
        property.setId(id);
        property.setLabel(label);
        property.setDisplayName(property.getLabel());
        ParametersType parameterType = TalendFileFactory.eINSTANCE.createParametersType();
        // add depended routines.
        List<RoutinesParameterType> dependenciesInPreference;
        dependenciesInPreference = RoutinesUtil.createDependenciesInPreference();

        parameterType.getRoutinesParameter().addAll(dependenciesInPreference);
        JobletProcess process = JobletFactory.eINSTANCE.createJobletProcess();
        process.setParameters(parameterType);
        processItem.setJobletProcess(process);
        factory.create(processItem, new Path(""));
        return new RepositoryObject(property);
    }

    class ContextManager implements IContextModelManager {

        /*
         * (non-Javadoc)
         *
         * @see org.talend.core.ui.context.IContextModelManager#getContextManager()
         */
        @Override
        public IContextManager getContextManager() {
            JobContextManager jobContextManager = new JobContextManager();
            JobContext context = new JobContext("Default");
            jobContextManager.getListContext().add(context);
            return jobContextManager;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.talend.core.ui.context.IContextModelManager#getProcess()
         */
        @Override
        public IProcess2 getProcess() {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.talend.core.ui.context.IContextModelManager#refresh()
         */
        @Override
        public void refresh() {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         *
         * @see org.talend.core.ui.context.IContextModelManager#getCommandStack()
         */
        @Override
        public CommandStack getCommandStack() {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.talend.core.ui.context.IContextModelManager#onContextChangeDefault(org.talend.core.model.process.
         * IContextManager, org.talend.core.model.process.IContext)
         */
        @Override
        public void onContextChangeDefault(IContextManager contextManager, IContext newDefault) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         *
         * @see org.talend.core.ui.context.IContextModelManager#onContextRenameParameter(org.talend.core.model.process.
         * IContextManager, java.lang.String, java.lang.String)
         */
        @Override
        public void onContextRenameParameter(IContextManager contextManager, String oldName, String newName) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         *
         * @see org.talend.core.ui.context.IContextModelManager#onContextRenameParameter(org.talend.core.model.process.
         * IContextManager, java.lang.String, java.lang.String, java.lang.String)
         */
        @Override
        public void onContextRenameParameter(IContextManager contextManager, String sourceId, String oldName, String newName) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         *
         * @see org.talend.core.ui.context.IContextModelManager#onContextModify(org.talend.core.model.process.
         * IContextManager, org.talend.core.model.process.IContextParameter)
         */
        @Override
        public void onContextModify(IContextManager contextManager, IContextParameter parameter) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         *
         * @see org.talend.core.ui.context.IContextModelManager#onContextAddParameter(org.talend.core.model.process.
         * IContextManager, org.talend.core.model.process.IContextParameter)
         */
        @Override
        public void onContextAddParameter(IContextManager contextManager, IContextParameter parameter) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         *
         * @see org.talend.core.ui.context.IContextModelManager#onContextRemoveParameter(org.talend.core.model.process.
         * IContextManager, java.lang.String)
         */
        @Override
        public void onContextRemoveParameter(IContextManager contextManager, String paramName) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         *
         * @see org.talend.core.ui.context.IContextModelManager#onContextRemoveParameter(org.talend.core.model.process.
         * IContextManager, java.lang.String, java.lang.String)
         */
        @Override
        public void onContextRemoveParameter(IContextManager contextManager, String paramName, String sourceId) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         *
         * @see org.talend.core.ui.context.IContextModelManager#onContextRemoveParameter(org.talend.core.model.process.
         * IContextManager, java.util.Set, java.lang.String)
         */
        @Override
        public void onContextRemoveParameter(IContextManager contextManager, Set<String> paramNames, String sourceId) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         *
         * @see org.talend.core.ui.context.IContextModelManager#onContextRemoveParameter(org.talend.core.model.process.
         * IContextManager, java.util.Set)
         */
        @Override
        public void onContextRemoveParameter(IContextManager contextManager, Set<String> paramNames) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         *
         * @see org.talend.core.ui.context.IContextModelManager#isReadOnly()
         */
        @Override
        public boolean isReadOnly() {
            // TODO Auto-generated method stub
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.talend.core.ui.context.IContextModelManager#isRepositoryContext()
         */
        @Override
        public boolean isRepositoryContext() {
            // TODO Auto-generated method stub
            return false;
        }

    }

}
