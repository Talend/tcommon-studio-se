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
package org.talend.commons.ui.swt.extended.table;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.swt.advanced.dataeditor.AbstractExtendedToolbar;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class AbstractExtendedControlViewer {

    private AbstractExtendedControlModel extendedControlModel;

    private Composite parentComposite;

    private CommandStack commandStack;

    private AbstractExtendedToolbar bindingToolbar = null;

    private boolean readOnly;

    /**
     * 
     * Event type. <br/>
     * 
     * $Id$
     * 
     */
    public enum EVENT_TYPE implements IExtendedControlEventType {
        MODEL_CHANGED,
        READ_ONLY_CHANGED,
    };

    /*
     * The list of listeners who wish to be notified when something significant happens.
     */
    private ListenerList listeners = new ListenerList();

    /**
     * DOC amaumont AbstractExtendedControlViewer constructor comment.
     */
    public AbstractExtendedControlViewer(Composite parentComposite) {
        super();
        this.parentComposite = parentComposite;
    }

    /**
     * DOC amaumont AbstractExtendedControlViewer constructor comment.
     * 
     * @param extendedControl, can be null
     * @param parentComposite
     */
    public AbstractExtendedControlViewer(AbstractExtendedControlModel extendedControl, Composite parentComposite) {
        super();
        this.extendedControlModel = extendedControl;
        this.parentComposite = parentComposite;
    }

    /**
     * DOC amaumont AbstractExtendedControlViewer constructor comment.
     * 
     * @param extendedControl, can be null
     * @param parentComposite
     * @param readOnly
     */
    public AbstractExtendedControlViewer(AbstractExtendedControlModel extendedControl, Composite parentComposite, boolean readOnly) {
        super();
        this.extendedControlModel = extendedControl;
        this.parentComposite = parentComposite;
        this.readOnly = readOnly;
    }

    /**
     * DOC amaumont Comment method "executeCommand".
     * 
     * @param command
     */
    public void executeCommand(Command command) {
        if (this.commandStack != null) {
            this.commandStack.execute(command);
        } else {
            command.execute();
        }

    }

    /**
     * Getter for extendedControl.
     * 
     * @return the extendedControl
     */
    public AbstractExtendedControlModel getExtendedControlModel() {
        return this.extendedControlModel;
    }

    /**
     * Sets the extendedControl.
     * 
     * @param model the extendedControl to set
     */
    public void setExtendedControlModel(AbstractExtendedControlModel model) {
        AbstractExtendedControlModel previousModel = this.extendedControlModel;
        this.extendedControlModel = model;
        if (previousModel != this.extendedControlModel) {
            modelChanged(previousModel, model);
            fireEvent(new ExtendedControlEvent(EVENT_TYPE.MODEL_CHANGED));
        }
    }

    /**
     * DOC amaumont Comment method "modelChanged".
     */
    protected abstract void modelChanged(AbstractExtendedControlModel previousModel, AbstractExtendedControlModel newModel);

    /**
     * Getter for parentComposite.
     * 
     * @return the parentComposite
     */
    public Composite getParentComposite() {
        return this.parentComposite;
    }

    /**
     * Getter for commandStackAdapter.
     * 
     * @return the commandStackAdapter
     */
    public CommandStack getCommandStack() {
        return this.commandStack;
    }

    /**
     * Sets the commandStackAdapter.
     * 
     * @param commandStack the commandStackAdapter to set
     */
    public void setCommandStack(CommandStack commandStack) {
        this.commandStack = commandStack;
    }

    public void addListener(IExtendedControlListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(IExtendedControlListener listener) {
        this.listeners.remove(listener);
    }

    /**
     * DOC amaumont Comment method "fireEvent".
     * 
     * @param event
     */
    protected void fireEvent(ExtendedControlEvent event) {
        final Object[] listenerArray = listeners.getListeners();
        for (int i = 0; i < listenerArray.length; i++) {
            ((IExtendedControlListener) listenerArray[i]).handleEvent(event);
        }

    }

    /**
     * Getter for readOnly.
     * 
     * @return the readOnly
     */
    public boolean isReadOnly() {
        return this.readOnly;
    }

    /**
     * Sets the readOnly.
     * 
     * @param readOnly the readOnly to set
     */
    public void setReadOnly(boolean readOnly) {
        if (readOnly != this.readOnly) {
            this.readOnly = readOnly;
            fireEvent(new ExtendedControlEvent(EVENT_TYPE.READ_ONLY_CHANGED));
        }
    }

    /**
     * Getter for bindingToolbar.
     * 
     * @return the bindingToolbar
     */
    public AbstractExtendedToolbar getBindingToolbar() {
        return this.bindingToolbar;
    }

    /**
     * Sets the bindingToolbar.
     * 
     * @param bindingToolbar the bindingToolbar to set
     */
    public void setBindingToolbar(AbstractExtendedToolbar bindingToolbar) {
        this.bindingToolbar = bindingToolbar;
    }

}
