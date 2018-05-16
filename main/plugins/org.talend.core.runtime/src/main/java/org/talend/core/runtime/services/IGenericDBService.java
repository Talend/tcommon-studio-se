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
package org.talend.core.runtime.services;

import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.widgets.Composite;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.core.IService;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.check.IChecker;
import org.talend.daikon.properties.Properties;
import org.talend.daikon.properties.presentation.Form;

/**
 * DOC hwang  class global comment. Detailled comment
 */
public interface IGenericDBService extends IService{
    
    public Map<String, Composite> creatDBDynamicComposite(Composite composite, EComponentCategory sectionCategory, boolean isCreation,
            boolean isReadOnly,Property property, String typeName);
    
    public void dbWizardPerformFinish(ConnectionItem item, Form form, boolean creation, IPath pathToSave, List<IMetadataTable> oldMetadataTable,String contextName) throws CoreException;
    
    public Form getDynamicForm(Composite composite);
    
    public IChecker getDynamicChecker(Composite dynamicComposite);
    
    public void resetConnectionItem(Composite composite, ConnectionItem connectionItem);
    
    public List<ERepositoryObjectType> getExtraTypes();
    
    public void convertPropertiesToDBElements(Properties props,Connection connection);
    
    public String getMVNPath(String value);
    
    public IMetadataTable converTable(INode node, IMetadataTable iTable);
    
    public void setPropertyTaggedValue(ComponentProperties properties);
    
    public void initReferencedComponent(IElementParameter refPara, String newValue);
    
    public Properties getComponentProperties(String typeName, String id);
    
    public ERepositoryObjectType getExtraDBType(ERepositoryObjectType type);
    
}
