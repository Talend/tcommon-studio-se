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
package org.talend.commons.ui.swt.advanced.dataeditor.control;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedControlModel;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedControlViewer;
import org.talend.commons.ui.swt.extended.table.ExtendedButtonEvent;
import org.talend.commons.ui.swt.extended.table.ExtendedControlEvent;
import org.talend.commons.ui.swt.extended.table.IExtendedButtonListener;
import org.talend.commons.ui.swt.extended.table.IExtendedControlListener;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class ExtendedPushButton implements IExtendedPushButton {

    Button button;

    protected AbstractExtendedControlViewer extendedControlViewer;

    private Command commandToExecute;

    /*
     * The list of listeners who wish to be notified when something significant happens.
     */
    private ListenerList beforeCommandListeners;

    private ListenerList afterCommandListeners;

    /**
     * DOC amaumont ExtendedTableButton constructor comment.
     * 
     * @param extendedViewer
     */
    public ExtendedPushButton(Composite parent, AbstractExtendedControlViewer extendedViewer, String tooltip, Image image) {
        super();
        this.extendedControlViewer = extendedViewer;
        init(parent, tooltip, image);
    }

    /**
     * DOC amaumont Comment method "init".
     * 
     * @param image
     * @param tooltip
     * @param parent2
     */
    private void init(Composite parent, String tooltip, Image image) {
        button = new Button(parent, SWT.PUSH);
        button.setToolTipText(tooltip);
        button.setImage(image);

        button.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                handleSelectionEvent(event);
            }

        });

        if (extendedControlViewer.getExtendedControlModel() == null) {
            button.setEnabled(false);
        }

        this.extendedControlViewer.addListener(new IExtendedControlListener() {

            public void handleEvent(ExtendedControlEvent event) {
                if (event.getType() == AbstractExtendedControlViewer.EVENT_TYPE.MODEL_CHANGED) {
                    handleModelChange();
                }
            }

        });

    }

    protected void handleModelChange() {
        button.setEnabled(getEnabledState());
    }

    /**
     * Getter for button.
     * 
     * @return the button
     */
    public Button getButton() {
        return this.button;
    }

    /**
     * Getter for extendedTableViewer.
     * 
     * @return the extendedTableViewer
     */
    public AbstractExtendedControlViewer getExtendedControlViewer() {
        return this.extendedControlViewer;
    }

    /**
     * DOC amaumont Comment method "executeCommand".
     * 
     * @param command
     */
    public void executeCommand(Command command) {
        extendedControlViewer.executeCommand(command);
    }

    /**
     * 
     * This method is not intended to be overriden.
     * 
     * @param event
     */
    protected void handleSelectionEvent(Event event) {
        beforeCommandExecution();
        this.commandToExecute = getCommandToExecute();
        if (this.commandToExecute != null) {
            executeCommand(this.commandToExecute);
            afterCommandExecution(this.commandToExecute);
        }
    }

    /**
     * 
     * DOC YeXiaowei Comment method "callSelectionAction".
     */
    public void callSelectionAction() {
        button.notifyListeners(SWT.Selection, null);
    }

    /**
     * This method is called before getCommandToExecute() to prepare data for command instanciation if needed.
     * 
     */
    protected void beforeCommandExecution() {
        fireEvent(new ExtendedButtonEvent(true));

        // override it if needed
    }

    /**
     * Return the <code>Command</code> to execute.
     * 
     * @return the command to execute, null if no command to execute
     */
    protected abstract Command getCommandToExecute();

    /**
     * This method is called after getCommandToExecute() to get data or errors after command execution.
     * 
     * @param executedCommand
     */
    protected void afterCommandExecution(Command executedCommand) {
        fireEvent(new ExtendedButtonEvent(false));
        // override it if needed
    }

    public void addListener(IExtendedButtonListener listener, boolean before) {
        if (before) {
            if (beforeCommandListeners == null) {
                beforeCommandListeners = new ListenerList();
            }
            this.beforeCommandListeners.add(listener);
        } else {
            if (afterCommandListeners == null) {
                afterCommandListeners = new ListenerList();
            }
            this.afterCommandListeners.add(listener);
        }
    }

    public void removeListener(IExtendedButtonListener listener, boolean before) {
        if (before && beforeCommandListeners != null) {
            this.beforeCommandListeners.remove(listener);
        } else if (!before && afterCommandListeners != null) {
            this.afterCommandListeners.remove(listener);
        }
    }

    /**
     * DOC amaumont Comment method "fireEvent".
     * 
     * @param event
     */
    protected void fireEvent(ExtendedButtonEvent event) {

        ListenerList listeners = null;
        if (event.isBefore()) {
            listeners = beforeCommandListeners;
        } else {
            listeners = afterCommandListeners;
        }
        if (listeners != null) {
            final Object[] listenerArray = listeners.getListeners();
            for (int i = 0; i < listenerArray.length; i++) {
                ((IExtendedButtonListener) listenerArray[i]).handleEvent(event);
            }
        }

    }

    public boolean getEnabledState() {
        AbstractExtendedControlModel extendedControlModel = extendedControlViewer.getExtendedControlModel();
        if (extendedControlModel == null) {
            return false;
        } else {
            return true;
        }
    }

}
