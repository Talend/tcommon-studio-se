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
package org.talend.commons.ui.swt.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Defines contextual actions used on a TreeViewer.
 * 
 * $Id: ITreeContextualAction.java 7196 2007-11-26 04:19:19Z bqian $
 * 
 */
public interface ITreeContextualAction extends IAction, Cloneable {

    /**
     * Initialise the action on a given tree with the actual selection.<br/>
     * 
     * Use the <code>setEnabled</code> method to anable/disable this action for the current selection.
     * 
     * @param viewer - The tree.
     * @param selection - Actual selection in the tree.
     */
    void init(TreeViewer viewer, IStructuredSelection selection);

    /**
     * Specifies if this action can be launch by double-click.
     * 
     * @return <code>true</code> if this action can be launch by double-click.
     */
    public boolean isDoubleClickAction();

    public void setWorkbenchPart(IWorkbenchPart workbenchPart);

    /**
     * Defines the Class on wich this action is launch by double-click. Means that when a double-click occurs in the
     * TreeViewer, the first registred IContextuelAction matching the selection Class is launch.
     * 
     * @return the class correspondinf to this action
     */
    public Class getClassForDoubleClick();

    public boolean isVisible();

    public int getLevel();

    public void setLevel(int level);

    public boolean isEditAction();

    public void setEditAction(boolean editAction);

    public boolean isReadAction();

    public void setReadAction(boolean readAction);

    public boolean isPropertiesAction();

    public void setPropertiesAction(boolean propertiesAction);

    public void setSpecialSelection(ISelectionProvider selectionProvider);

    public void setGroupId(String groupId);

    public String getGroupId();

    public ITreeContextualAction clone() throws CloneNotSupportedException;
}
