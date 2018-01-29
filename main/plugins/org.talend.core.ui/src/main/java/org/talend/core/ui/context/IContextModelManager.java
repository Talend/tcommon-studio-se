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
package org.talend.core.ui.context;

import java.util.Set;

import org.eclipse.gef.commands.CommandStack;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IProcess2;

/**
 * DOC bqian class global comment. Detailled comment <br/>
 * 
 */
public interface IContextModelManager {

    public IContextManager getContextManager();

    public IProcess2 getProcess();

    /**
     * refresh the contextComposite.
     */
    public void refresh();

    public CommandStack getCommandStack();

    public void onContextChangeDefault(IContextManager contextManager, IContext newDefault);

    public void onContextRenameParameter(IContextManager contextManager, String oldName, String newName);

    /**
     * Added by Marvin Wang on Mar.8, 2012 for re-naming the variables of context with the given parameters, the one who
     * implements this method needs to iterate the contexts from {@link IContextManager#getListContext()} to rename the
     * variable name.
     * 
     * @param contextManager
     * @param sourceId
     * @param oldName
     * @param newName
     */
    public void onContextRenameParameter(IContextManager contextManager, String sourceId, String oldName, String newName);

    public void onContextModify(IContextManager contextManager, IContextParameter parameter);

    public void onContextAddParameter(IContextManager contextManager, IContextParameter parameter);

    public void onContextRemoveParameter(IContextManager contextManager, String paramName);

    /**
     * Added by Marvin Wang on Mar.7, 2012 for removing the <code>IContextParameter</code> with the specified
     * {@link IContextParameter#getName()} and {@link IContextParameter#getSource()}, the one who implements this method
     * needs to iterate the contexts from {@link IContextManager#getListContext()} to remove the context parameter.
     * 
     * @param contextManager
     * @param paramName
     * @param sourceId
     */
    public void onContextRemoveParameter(IContextManager contextManager, String paramName, String sourceId);

    /**
     * Should use paraName and sourceId to identify which context parameter can be removed.
     * 
     * @param contextManager
     * @param paramNames
     * @param sourceId
     */
    public void onContextRemoveParameter(IContextManager contextManager, Set<String> paramNames, String sourceId);

    public void onContextRemoveParameter(IContextManager contextManager, Set<String> paramNames);

    public boolean isReadOnly();

    public boolean isRepositoryContext();
}
