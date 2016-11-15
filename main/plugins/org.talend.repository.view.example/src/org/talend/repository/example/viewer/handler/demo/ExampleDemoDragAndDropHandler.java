// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.example.viewer.handler.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsService;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.process.IElement;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.utils.AbstractDragAndDropServiceHandler;
import org.talend.core.model.utils.IComponentName;
import org.talend.core.repository.RepositoryComponentSetting;
import org.talend.repository.example.model.demo.ExampleDemoConnection;
import org.talend.repository.example.model.demo.ExampleDemoConnectionItem;
import org.talend.repository.example.viewer.node.ExampleDemoRepositoryNodeType;
import org.talend.repository.model.RepositoryNode;

/**
 * NOTE: this class is not finished, because need related some components.
 */
public class ExampleDemoDragAndDropHandler extends AbstractDragAndDropServiceHandler {

    public ExampleDemoDragAndDropHandler() {
    }

    @Override
    public boolean canHandle(Connection connection) {
        return connection instanceof ExampleDemoConnection;
    }

    @Override
    public Object getComponentValue(Connection connection, String value, IMetadataTable table) {
        if (value != null && canHandle(connection)) {
            ExampleDemoConnection demoConn = (ExampleDemoConnection) connection;
            if ("USE_FILE_AMBIGUOUS".equals(value)) { //$NON-NLS-1$
                return Boolean.TRUE;
            } else if ("FILE_AMBIGUOUS".equals(value)) { //$NON-NLS-1$
                return "foo/bar";
            }
        }
        return null;
    }

    @Override
    public List<IComponent> filterNeededComponents(Item item, RepositoryNode seletetedNode, ERepositoryObjectType type) {
        List<IComponent> neededComponents = new ArrayList<IComponent>();
        if (!(item instanceof ExampleDemoConnectionItem)) {
            return neededComponents;
        }
        IComponentsService service = (IComponentsService) GlobalServiceRegister.getDefault().getService(IComponentsService.class);
        Set<IComponent> components = service.getComponentsFactory().getComponents();
        for (IComponent component : components) {
            if ("tExampleComponent".equals(component.getName())) {
                neededComponents.add(component);
            }
        }

        return neededComponents;
    }

    @Override
    public IComponentName getCorrespondingComponentName(Item item, ERepositoryObjectType type) {
        RepositoryComponentSetting setting = null;
        if (item instanceof ExampleDemoConnectionItem) {
            setting = new RepositoryComponentSetting();
            setting.setName("my.metadata");
            setting.setRepositoryType("my.metadata");
            // setting.setWithSchema(true);
            // setting.setInputComponent(INPUT);
            // setting.setOutputComponent(OUTPUT);
            List<Class<Item>> list = new ArrayList<Class<Item>>();
            Class clazz = null;
            try {
                clazz = getClass().forName(ExampleDemoConnectionItem.class.getName());
            } catch (ClassNotFoundException e) {
                ExceptionHandler.process(e);
            }
            list.add(clazz);
            setting.setClasses(list.toArray(new Class[0]));
        }

        return setting;
    }

    @Override
    public void setComponentValue(Connection connection, INode node, IElementParameter param) {
        if (node != null && canHandle(connection)) {
            ExampleDemoConnection demoConn = (ExampleDemoConnection) connection;
            // PTODO get the values from node, and set to the matched attributes(repositoryValue) of connection
        }
    }

    @Override
    public ERepositoryObjectType getType(String repositoryType) {
        // PTODO accordding to the checked type to return real object type.
        if ("EXAMPLE_DEMO".equals(repositoryType)) {
            return ExampleDemoRepositoryNodeType.repositoryExampleDemoType;
        }
        if ("EXTENDED_EXAMPLE_DEMO".equals(repositoryType)) {
            return ExampleDemoRepositoryNodeType.repositoryExtendedExampleDemoType;
        }
        return null;
    }

    @Override
    public Object getComponentValue(Connection connection, String value, IMetadataTable table, String targetComponent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void handleTableRelevantParameters(Connection connection, IElement ele, IMetadataTable metadataTable) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean isValidForDataViewer(Connection connection, IMetadataTable metadataTable) {
        if (!canHandle(connection)) {
            return false;
        }
        return true;
    }

}
