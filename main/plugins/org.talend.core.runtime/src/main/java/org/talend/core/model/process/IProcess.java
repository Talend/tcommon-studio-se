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
package org.talend.core.model.process;

import java.util.List;
import java.util.Set;

import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.metadata.IMetadataTable;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: IProcess.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
/**
 * DOC nrousseau class global comment. Detailled comment
 */
public interface IProcess extends IElement {

    public static final String NEED_UPDATE_JOB = "NEED_UPDATE_JOB"; //$NON-NLS-1$

    public static final String TABLE_ACTION = "TABLE_ACTION"; //$NON-NLS-1$

    public static final String DEFAULT_ROW_CONNECTION_NAME = "row"; //$NON-NLS-1$

    public static final String DEFAULT_TABLE_CONNECTION_NAME = "table"; //$NON-NLS-1$

    public static final String DEFAULT_ITERATE_CONNECTION_NAME = "iterate"; //$NON-NLS-1$

    public static final String DEFAULT_ROUTE_CONNECTION_NAME = "route"; //$NON-NLS-1$

    public static final String DEFAULT_WHEN_CONNECTION_NAME = "when"; //$NON-NLS-1$

    public static final String DEFAULT_CATCH_CONNECTION_NAME = "catch"; //$NON-NLS-1$

    public static final String DEFAULT_OTHER_CONNECTION_NAME = "otherwise"; //$NON-NLS-1$

    public static final String DEFAULT_TRY_CONNECTION_NAME = "try"; //$NON-NLS-1$

    public static final String DEFAULT_FINALLY_CONNECTION_NAME = "finally"; //$NON-NLS-1$

    public static final String DEFAULT_ENDBLOCK_CONNECTION_NAME = "route"; //$NON-NLS-1$

    public static final String SCREEN_OFFSET_X = "SCREEN_OFFSET_X"; //$NON-NLS-1$

    public static final String SCREEN_OFFSET_Y = "SCREEN_OFFSET_Y"; //$NON-NLS-1$

    public String getName();

    public String getId();

    public String getVersion();

    // list of nodes that are in the designer
    public List<? extends INode> getGraphicalNodes();

    // list of nodes that will be used to generated the code
    // this list is slightly different from the designer nodes
    public List<? extends INode> getGeneratingNodes();

    public String generateUniqueConnectionName(String baseName);

    public void addUniqueConnectionName(String uniqueConnectionName);

    public void removeUniqueConnectionName(String uniqueConnectionName);

    public boolean checkValidConnectionName(String connectionName);

    public boolean checkValidConnectionName(String connectionName, boolean checkExists);

    public IContextManager getContextManager();

    public List<? extends INode> getNodesOfType(String componentName);

    /**
     * Comment method "getAllConnections".
     * 
     * @param filter only return the filter matched connections
     * @return
     */
    public IConnection[] getAllConnections(String filter);

    public Set<String> getNeededLibraries(boolean withChildrens);

    public Set<ModuleNeeded> getNeededModules(boolean withChildrens);

    public Set<String> getNeededRoutines();

    public Set<String> getNeededPigudf();

    public int getMergelinkOrder(final INode node);

    public boolean isThereLinkWithHash(final INode node);

    public List<INode> getNodesWithImport();

    /**
     * if need to regenerate the code for the current process. For jobs without GUI, always true.
     * 
     * @return
     */
    public boolean isNeedRegenerateCode();

    public void setNeedRegenerateCode(boolean regenerateCode);

    public IMetadataTable getOutputMetadataTable();

    public boolean isDuplicate();

    /**
     * Duplicate == true means copy of process used for code generation.
     * 
     * @param duplicate
     */
    public void setDuplicate(boolean duplicate);

    public void setActivate(boolean activate);

    public void setContextManager(IContextManager contextManager);

    public void checkStartNodes();

    public String getComponentsType();
}
