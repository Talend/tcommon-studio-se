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
package org.talend.core;

import java.util.Collection;

import org.talend.core.model.process.INode;
import org.talend.core.model.properties.Item;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;

/**
 * Interface of TDQSurvivorshipService
 */
public interface ITDQSurvivorshipService extends IService {

    /**
     * Create rule file items in repository folder named "metadata/survivorship" according to the configuration of
     * tRuleSurvivorship Component.
     * 
     * @param node
     */
    public void createSurvivorshipItems(INode node);

    /**
     * 
     * get all tRuleSurvirship Nodes from a job and all dependencies(job/joblet).
     * 
     * @param item
     * @return
     */
    public Collection<NodeType> getSurvivorshipNodesOfProcess(Item item);
}
