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
package org.talend.commons.ui.swt.extended.table;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.swt.advanced.dataeditor.AbstractExtendedToolbar;

public abstract class AbstractExtendedControlViewer {

    private AbstractExtendedControlModel extendedControlModel;

    private Composite parentComposite;

    private CommandStack commandStack;

    private AbstractExtendedToolbar bindingToolbar = null;

    private boolean readOnly;

    /**
     * Event type.
     */
    public enum EVENT_TYPE implements IExtendedControlEventType {
        MODEL_CHANGED,
        READ_ONLY_CHANGED,
    };

    /*
     * The list of listeners who wish to be notified when something significant happens.
     */
    private ListenerList listeners = new ListenerList();

    public AbstractExtendedControlViewer(Composite parentComposite) {
        super();
        this.parentComposite = parentComposite;
    }

    /**
     * @param extendedControl, can be null
     * @param parentComposite
     */
    public AbstractExtendedControlViewer(AbstractExtendedControlModel extendedControl, Composite parentComposite) {
        super();
        this.extendedControlModel = extendedControl;
        this.parentComposite = parentComposite;
    }

    /**
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

    protected abstract void modelChanged(AbstractExtendedControlModel previousModel, AbstractExtendedControlModel newModel);

    public Composite getParentComposite() {
        return this.parentComposite;
    }

    public CommandStack getCommandStack() {
        return this.commandStack;
    }

    public void setCommandStack(CommandStack commandStack) {
        this.commandStack = commandStack;
    }

    public void addListener(IExtendedControlListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(IExtendedControlListener listener) {
        this.listeners.remove(listener);
    }

    protected void fireEvent(ExtendedControlEvent event) {
        final Object[] listenerArray = listeners.getListeners();
        for (int i = 0; i < listenerArray.length; i++) {
            ((IExtendedControlListener) listenerArray[i]).handleEvent(event);
        }

    }

    public boolean isReadOnly() {
        return this.readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        if (readOnly != this.readOnly) {
            this.readOnly = readOnly;
            fireEvent(new ExtendedControlEvent(EVENT_TYPE.READ_ONLY_CHANGED));
        }
    }

    public AbstractExtendedToolbar getBindingToolbar() {
        return this.bindingToolbar;
    }

    public void setBindingToolbar(AbstractExtendedToolbar bindingToolbar) {
        this.bindingToolbar = bindingToolbar;
    }

}
