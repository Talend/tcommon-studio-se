// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.service;

import java.util.List;

import org.talend.core.IService;
import org.talend.core.model.components.IComponentsHandler;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.Item;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.nodes.IProjectRepositoryNode;

/**
 * DOC zwzhao class global comment. Detailled comment
 */
public interface IStormProcessService extends IService {

    IRepositoryNode getRootNode(IProjectRepositoryNode projectNode);

    public boolean collectStandardProcessNode(List<String> filteredContents, Object node);

    /**
     * This method is responsible for creating additional information which are going to be registered in the Process
     * (and then in the Item). DOC rdubois Comment method "generateSparkStreamingInfosParameter".
     *
     * @param process the current process.
     */
    public void generateSparkStreamingInfosParameter(IProcess2 process);

    public IComponentsHandler getSparkStreamingComponentsHandler();

    public boolean isSparkStreaming(Item item);

    public IProcess2 createBigdataProcess(Item item);

}
