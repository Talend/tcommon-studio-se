// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.core;

import java.util.List;

import org.dom4j.Element;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorPart;
import org.talend.core.IService;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.FileItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.designer.codegen.ITalendSynchronizer;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;

/**
 * DOC guanglong.du class global comment. Detailled comment
 */
public interface ICamelDesignerCoreService extends IService {

    public IAction getCreateProcessAction(boolean isToolbar);

    public IAction getCreateBeanAction(boolean isToolbar);

    public boolean isInstanceofCamelRoutes(Item item);

    public ERepositoryObjectType getRoutes();
    
    public ERepositoryObjectType getRouteDocType();
    
    public ERepositoryObjectType getRouteDocsType();

    public ProcessType getCamelProcessType(Item item);

    public ITalendSynchronizer createCamelJavaSynchronizer();

    public ERepositoryObjectType getBeansType();

    public boolean isInstanceofCamel(Item item);

    public boolean isInstanceofCamelBeans(Item item);

    public boolean isCamelMulitPageEditor(IEditorPart editor);

	/**
	 * Synchronized Route resource
	 * 
	 * @param item
	 */
	public List<IPath> synchronizeRouteResource(Item item);
	
	public String getDeleteFolderName(ERepositoryObjectType type);
	
	public boolean isRouteBuilderNode(INode node);

	public boolean canCreateNodeOnLink(IConnection connection, INode node);
	
	public EConnectionType getTargetConnectionType(INode node);

	public void appendRouteInfo2Doc(Item item, Element jobElement);

	public FileItem newRouteDocumentationItem();

}
