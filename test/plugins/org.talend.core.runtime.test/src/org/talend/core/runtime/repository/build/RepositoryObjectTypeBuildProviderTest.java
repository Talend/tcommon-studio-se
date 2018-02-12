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
package org.talend.core.runtime.repository.build;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.ui.IEditorPart;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.process.ISubjobContainer;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.User;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.update.IUpdateManager;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.repository.model.IRepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class RepositoryObjectTypeBuildProviderTest {

    class RepositoryObjectTypeBuildProviderTestClass extends RepositoryObjectTypeBuildProvider {

        @Override
        protected ERepositoryObjectType getObjectType() {
            return ERepositoryObjectType.PROCESS;
        }
    }

    class TestProcess implements IProcess2 {

        @Override
        public void setId(String id) {
        }

        @Override
        public void setLabel(String label) {
        }

        @Override
        public void setVersion(String version) {
        }

        @Override
        public void setAuthor(User author) {
        }

        @Override
        public void setStatusCode(String statusCode) {
        }

        @Override
        public void setCreationDate(Date value) {
        }

        @Override
        public void setDescription(String value) {
        }

        @Override
        public void setModificationDate(Date value) {
        }

        @Override
        public void setPurpose(String value) {
        }

        @Override
        public Property getProperty() {
            final Property p = PropertiesFactory.eINSTANCE.createProperty();
            p.setId("12345");
            final ProcessItem item = PropertiesFactory.eINSTANCE.createProcessItem();
            p.setItem(item);
            item.setProperty(p);
            return p;
        }

        @Override
        public void setProperty(Property property) {

        }

        @Override
        public String getId() {
            return null;
        }

        @Override
        public String getLabel() {
            return null;
        }

        @Override
        public String getVersion() {
            return null;
        }

        @Override
        public User getAuthor() {
            return null;
        }

        @Override
        public String getStatusCode() {
            return null;
        }

        @Override
        public Date getCreationDate() {
            return null;
        }

        @Override
        public String getDescription() {
            return null;
        }

        @Override
        public Date getModificationDate() {
            return null;
        }

        @Override
        public String getPurpose() {
            return null;
        }

        @Override
        public ERepositoryObjectType getRepositoryObjectType() {
            return null;
        }

        @Override
        public List<IRepositoryViewObject> getChildren() {
            return null;
        }

        @Override
        public void setRepositoryNode(IRepositoryNode node) {
        }

        @Override
        public IRepositoryNode getRepositoryNode() {
            return null;
        }

        @Override
        public boolean isDeleted() {
            return false;
        }

        @Override
        public String getProjectLabel() {
            return null;
        }

        @Override
        public String getPath() {
            return null;
        }

        @Override
        public ERepositoryStatus getRepositoryStatus() {
            return null;
        }

        @Override
        public ERepositoryStatus getInformationStatus() {
            return null;
        }

        @Override
        public boolean isModified() {
            return false;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public List<? extends INode> getGraphicalNodes() {
            return null;
        }

        @Override
        public List<? extends INode> getGeneratingNodes() {
            return null;
        }

        @Override
        public String generateUniqueConnectionName(String baseName) {
            return null;
        }

        @Override
        public void addUniqueConnectionName(String uniqueConnectionName) {

        }

        @Override
        public void removeUniqueConnectionName(String uniqueConnectionName) {
        }

        @Override
        public boolean checkValidConnectionName(String connectionName) {
            return false;
        }

        @Override
        public boolean checkValidConnectionName(String connectionName, boolean checkExists) {
            return false;
        }

        @Override
        public IContextManager getContextManager() {
            return null;
        }

        @Override
        public List<? extends INode> getNodesOfType(String componentName) {
            return null;
        }

        @Override
        public IConnection[] getAllConnections(String filter) {
            return null;
        }

        @Override
        public Set<String> getNeededLibraries(boolean withChildrens) {
            return null;
        }

        @Override
        public Set<ModuleNeeded> getNeededModules(boolean withChildrens) {
            return null;
        }

        @Override
        public Set<String> getNeededRoutines() {
            return null;
        }

        @Override
        public Set<String> getNeededPigudf() {
            return null;
        }

        @Override
        public int getMergelinkOrder(INode node) {
            return 0;
        }

        @Override
        public boolean isThereLinkWithHash(INode node) {
            return false;
        }

        @Override
        public List<INode> getNodesWithImport() {
            return null;
        }

        @Override
        public boolean isNeedRegenerateCode() {
            return false;
        }

        @Override
        public void setNeedRegenerateCode(boolean regenerateCode) {

        }

        @Override
        public IMetadataTable getOutputMetadataTable() {
            return null;
        }

        @Override
        public boolean isDuplicate() {
            return false;
        }

        @Override
        public void setDuplicate(boolean duplicate) {
        }

        @Override
        public void setContextManager(IContextManager contextManager) {
        }

        @Override
        public String getComponentsType() {
            return null;
        }

        @Override
        public IElementParameter getElementParameter(String name) {
            return null;
        }

        @Override
        public List<? extends IElementParameter> getElementParameters() {
            return null;
        }

        @Override
        public List<? extends IElementParameter> getElementParametersWithChildrens() {
            return null;
        }

        @Override
        public void setElementParameters(List<? extends IElementParameter> elementsParameters) {
        }

        @Override
        public boolean isReadOnly() {
            return false;
        }

        @Override
        public void setReadOnly(boolean readOnly) {
        }

        @Override
        public boolean isForceReadOnly() {
            return false;
        }

        @Override
        public void setForceReadOnly(boolean readOnly) {
        }

        @Override
        public String getElementName() {
            return null;
        }

        @Override
        public Object getPropertyValue(String name) {
            return null;
        }

        @Override
        public IElementParameter getElementParameterFromField(EParameterFieldType dbtable) {
            return null;
        }

        @Override
        public IElementParameter getElementParameterFromField(EParameterFieldType propertyType, EComponentCategory category) {
            return null;
        }

        @Override
        public Object getPropertyValue(String name, String paramName) {
            return null;
        }

        @Override
        public boolean checkReadOnly() {
            return false;
        }

        @Override
        public ProcessType saveXmlFile() throws IOException {
            return null;
        }

        @Override
        public ProcessType saveXmlFile(boolean checkJoblet) throws IOException {
            return null;
        }

        @Override
        public void setPropertyValue(String id, Object value) {
        }

        @Override
        public void updateProperties() {
        }

        @Override
        public void loadXmlFile() {
        }

        @Override
        public void loadXmlFile(boolean loadScreenshots) {
        }

        @Override
        public void checkLoadNodes() throws PersistenceException {
        }

        @Override
        public void setXmlStream(InputStream contents) {
        }

        @Override
        public List getElements() {
            return null;
        }

        @Override
        public void setActivate(boolean b) {
        }

        @Override
        public void checkStartNodes() {
        }

        @Override
        public void checkProcess() {
        }

        @Override
        public void setProcessModified(boolean processModified) {
        }

        @Override
        public boolean isProcessModified() {
            return false;
        }

        @Override
        public List<? extends ISubjobContainer> getSubjobContainers() {

            return null;
        }

        @Override
        public IUpdateManager getUpdateManager() {

            return null;
        }

        @Override
        public Map<String, byte[]> getScreenshots() {

            return null;
        }

        @Override
        public void dispose() {

        }

        @Override
        public List<NodeType> getUnloadedNode() {

            return null;
        }

        @Override
        public void checkTableParameters() {

        }

        @Override
        public String generateUniqueConnectionName(String baseName, String tableName) {

            return null;
        }

        @Override
        public boolean disableRunJobView() {

            return false;
        }

        @Override
        public IEditorPart getEditor() {

            return null;
        }

        @Override
        public void removeUniqueNodeName(String uniqueName) {

        }

        @Override
        public void addUniqueNodeName(String uniqueName) {

        }

        @Override
        public boolean isGridEnabled() {

            return false;
        }

        @Override
        public void updateSubjobContainers() {

        }

        @Override
        public IContext getLastRunContext() {

            return null;
        }

        @Override
        public void setLastRunContext(IContext context) {

        }

        @Override
        public Map<Object, Object> getAdditionalProperties() {

            return null;
        }

        @Override
        public boolean isSubjobEnabled() {

            return false;
        }

        @Override
        public void removeProblems4ProcessDeleted() {

        }

        @Override
        public void setMRData() {

        }

        @Override
        public boolean isNeedLoadmodules() {

            return false;
        }

        @Override
        public void setNeedLoadmodules(boolean isNeedLoadmodules) {

        }

        @Override
        public void refreshProcess() {

        }

    }

    @Before
    @After
    public void clean() {
        ERepositoryObjectType.getTypeCacheById().clear();
    }

    @Test
    public void test_valid_emptyParameters() {
        RepositoryObjectTypeBuildProviderTestClass proivder = new RepositoryObjectTypeBuildProviderTestClass();
        Assert.assertFalse(proivder.valid(null));
        Assert.assertFalse(proivder.valid(Collections.emptyMap()));
    }

    @Test
    public void test_valid_nonProcess() {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(IBuildParametes.PROCESS, new Object());

        RepositoryObjectTypeBuildProviderTestClass proivder = new RepositoryObjectTypeBuildProviderTestClass();
        Assert.assertFalse(proivder.valid(parameters));
    }

    @Test
    public void test_valid_process() {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(IBuildParametes.PROCESS, new TestProcess());

        RepositoryObjectTypeBuildProviderTestClass proivder = new RepositoryObjectTypeBuildProviderTestClass();
        Assert.assertTrue(proivder.valid(parameters));
    }

    @Test
    public void test_valid_nonItem() {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(IBuildParametes.ITEM, new Object());

        RepositoryObjectTypeBuildProviderTestClass proivder = new RepositoryObjectTypeBuildProviderTestClass();
        Assert.assertFalse(proivder.valid(parameters));
    }

    @Test
    public void test_valid_item() {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(IBuildParametes.ITEM, new TestProcess().getProperty().getItem());

        RepositoryObjectTypeBuildProviderTestClass proivder = new RepositoryObjectTypeBuildProviderTestClass();
        Assert.assertTrue(proivder.valid(parameters));
    }

    @Test
    public void test_valid_nonRepositoryViewObject() {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(IBuildParametes.REPOSITORY_OBJECT, new Object());

        RepositoryObjectTypeBuildProviderTestClass proivder = new RepositoryObjectTypeBuildProviderTestClass();
        Assert.assertFalse(proivder.valid(parameters));
    }

    @Test
    public void test_valid_repositoryViewObject() {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(IBuildParametes.REPOSITORY_OBJECT, new IRepositoryViewObject() {

            @Override
            public String getId() {
                return null;
            }

            @Override
            public String getLabel() {
                return null;
            }

            @Override
            public String getVersion() {
                return null;
            }

            @Override
            public User getAuthor() {
                return null;
            }

            @Override
            public String getStatusCode() {
                return null;
            }

            @Override
            public Date getCreationDate() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public Date getModificationDate() {
                return null;
            }

            @Override
            public String getPurpose() {
                return null;
            }

            @Override
            public ERepositoryObjectType getRepositoryObjectType() {
                return ERepositoryObjectType.PROCESS;
            }

            @Override
            public List<IRepositoryViewObject> getChildren() {
                return null;
            }

            @Override
            public void setRepositoryNode(IRepositoryNode node) {

            }

            @Override
            public IRepositoryNode getRepositoryNode() {
                return null;
            }

            @Override
            public Property getProperty() {
                return null;
            }

            @Override
            public boolean isDeleted() {
                return false;
            }

            @Override
            public String getProjectLabel() {
                return null;
            }

            @Override
            public String getPath() {
                return null;
            }

            @Override
            public ERepositoryStatus getRepositoryStatus() {
                return null;
            }

            @Override
            public ERepositoryStatus getInformationStatus() {
                return null;
            }

            @Override
            public boolean isModified() {
                return false;
            }
        });

        RepositoryObjectTypeBuildProviderTestClass proivder = new RepositoryObjectTypeBuildProviderTestClass();
        Assert.assertTrue(proivder.valid(parameters));
    }
}
