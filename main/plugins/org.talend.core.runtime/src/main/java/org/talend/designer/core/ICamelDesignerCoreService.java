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
package org.talend.designer.core;

import org.dom4j.Element;
import org.talend.core.IService;
import org.talend.core.model.process.EConnectionType;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.FileItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.ERepositoryObjectType;

/**
 * DOC guanglong.du class global comment. Detailled comment<br/>
 * <b>!!!NOTE!!!</b> ICamelDesignerCoreService should also be usable for commandline, should not add UI related codes;
 * for UI related codes, please add them into ICamelDesignerCoreUIService
 */
public interface ICamelDesignerCoreService extends IService {

    public boolean isInstanceofCamelRoutes(Item item);

    public ERepositoryObjectType getRouteDocType();

    public ERepositoryObjectType getRouteDocsType();

    public ERepositoryObjectType getBeansType();

    public boolean isInstanceofCamel(Item item);

    public boolean isInstanceofCamelBeans(Item item);

    /**
     * Synchronized Route resource
     * 
     * @param item
     */
    public void synchronizeRouteResource(ProcessItem item);

    public String getDeleteFolderName(ERepositoryObjectType type);

    public boolean isRouteBuilderNode(INode node);

    public boolean canCreateNodeOnLink(IConnection connection, INode node);

    public EConnectionType getTargetConnectionType(INode node);

    public void appendRouteInfo2Doc(Item item, Element jobElement);

    public FileItem newRouteDocumentationItem();

}
