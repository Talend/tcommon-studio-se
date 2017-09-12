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
package org.talend.core.runtime.services;

import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.IService;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.ui.check.IChecker;
import org.talend.daikon.properties.presentation.Form;

/**
 * DOC hwang  class global comment. Detailled comment
 */
public interface IGenericDBService extends IService{
    
    public Map<String, Composite> creatDBDynamicComposite(Composite composite, EComponentCategory sectionCategory, boolean isCreation,
            boolean isReadOnly,Property property, String typeName);
    
    public Connection createGenericConnection();
    
    public ConnectionItem createGenericConnectionItem();
    
    public String getGenericConnectionType(Item item);
    
    public void setGenericConnectionType(String type, Item item);
    
    public void dbWizardPerformFinish(ConnectionItem item, Form form, boolean creation, IPath pathToSave, List<IMetadataTable> oldMetadataTable) throws CoreException;
    
    public Form getDynamicForm(Composite composite);
    
    public IChecker getDynamicChecker(Composite dynamicComposite);
    
    public void resetConnectionItem(Composite composite, ConnectionItem connectionItem);
    
    public List<ERepositoryObjectType> getExtraTypes();
    
}
