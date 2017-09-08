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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.context.JobContext;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.context.JobContextParameter;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.ui.i18n.Messages;
import org.talend.repository.model.RepositoryConstants;

/**
 * A dialog that config the context value sets.
 * 
 */
public class ContextSetConfigurationDialog extends ObjectSelectionDialog<IContext> {

    private static String defaultMesage = "Configure Contexts for Job. Click to Set Default Context.         "; //$NON-NLS-1$

    IContextModelManager manager = null;

    /**
     * DOC bqian ContextSetConfigurationDialog class global comment. Detailled comment <br/>
     * 
     */
    public class ContextLabelProvider extends LabelProvider {

        @Override
        public Image getImage(Object object) {
            return ImageProvider.getImage(ECoreImage.CONTEXT_ICON);
        }

        @Override
        public String getText(Object object) {
            IContext context = (IContext) object;
            IContext defaultContext = manager.getContextManager().getDefaultContext();
            if (context.equals(defaultContext)) {
                return context.getName() + " (Default)"; //$NON-NLS-1$
            } else {
                return context.getName();
            }
        }
    }

    @SuppressWarnings("restriction")
    public ContextSetConfigurationDialog(Shell parentShell, IContextModelManager manager) {
        super(parentShell, "Configure Contexts", defaultMesage, null); //$NON-NLS-1$
        this.manager = manager;
        setLabelProvider(getLabelProvider());
        List<IContext> list = new ArrayList<IContext>(manager.getContextManager().getListContext());
        setData(list);
        setShellStyle(getShellStyle() | SWT.RESIZE);
    }

    @Override
    protected void initTableInput() {
        super.initTableInput();
        ((CheckboxTableViewer) fTableViewer).setCheckedElements(new Object[] { manager.getContextManager().getDefaultContext() });
        fTableViewer.refresh();
    }

    @Override
    protected void createTableViewer(Composite parent) {
        fTableViewer = CheckboxTableViewer.newCheckList(parent, SWT.NONE);
        initTableViewer(parent);
        ((CheckboxTableViewer) fTableViewer).setCheckedElements(new Object[] { manager.getContextManager().getDefaultContext() });
        fTableViewer.refresh();
        ((CheckboxTableViewer) fTableViewer).addCheckStateListener(new ICheckStateListener() {

            public void checkStateChanged(CheckStateChangedEvent event) {
                Object obj = event.getElement();
                IContext defaultContext = manager.getContextManager().getDefaultContext();
                boolean checked = event.getChecked();
                if (obj.equals(defaultContext)) {
                    // keep check status
                    ((CheckboxTableViewer) fTableViewer).setChecked(obj, true);
                } else {
                    ((CheckboxTableViewer) fTableViewer).setChecked(defaultContext, false);
                    manager.onContextChangeDefault(manager.getContextManager(), (IContext) obj);
                    fTableViewer.refresh(true);
                }
                updateButtonAvailability();
            }
        });
    }

    @Override
    protected void createOrderButtons(Composite parent) {
        super.createOrderButtons(parent);
        fSelectAll.setVisible(false);
        fDeselectAll.setVisible(false);
    }

    public List<IContext> getResultContexts() {
        return getData();
    }

    LabelProvider getLabelProvider() {
        return new ContextLabelProvider();
    }

    private String toValid(String newText) {
        if (newText.equals("") || !newText.matches(RepositoryConstants.CODE_ITEM_PATTERN)) { //$NON-NLS-1$
            return Messages.getString("ContextSetConfigurationDialog.nameNotValid"); //$NON-NLS-1$
        }
        for (IContext context : getAllContexts()) {
            if (context.getName().equalsIgnoreCase(newText)) {
                return Messages.getString("ContextProcessSection.30"); //$NON-NLS-1$
            }
        }
        return null;
    }

    @Override
    public void createElement() {

        IInputValidator validator = new IInputValidator() {

            public String isValid(String newText) {
                return toValid(newText);
            }
        };

        InputDialog inputDial = new InputDialog(getShell(), Messages.getString("ContextProcessSection.6"), //$NON-NLS-1$
                Messages.getString("ContextProcessSection.7"), "", validator); //$NON-NLS-1$ //$NON-NLS-2$

        inputDial.open();
        String returnValue = inputDial.getValue();
        if (returnValue == null || "".equals(returnValue)) {
            return;
        }
        createContext(returnValue);

        refreshViewer();
    }

    private boolean validateContextName(String name) {
        if (name.equals("") || !name.matches(RepositoryConstants.CODE_ITEM_PATTERN)) { //$NON-NLS-1$
            MessageDialog.openWarning(new Shell(), Messages.getString(Messages.getString("ContextProcessSection.50")), Messages //$NON-NLS-1$
                    .getString(Messages.getString("ContextProcessSection.51"))); //$NON-NLS-1$
            return false;
        }

        return !isContextExisting(name);
    }

    public List<IContext> getAllContexts() {
        return getData();
    }

    private boolean isContextExisting(String name) {
        for (IContext context : getAllContexts()) {
            if (context.getName().equalsIgnoreCase(name)) {
                MessageBox mBox = new MessageBox(this.getShell(), SWT.ICON_ERROR);
                mBox.setText(Messages.getString("ContextProcessSection.29")); //$NON-NLS-1$
                mBox.setMessage(Messages.getString("ContextProcessSection.30")); //$NON-NLS-1$
                mBox.open();
                return true;
            }
        }
        return false;
    }

    private void createContext(final String name) {
        IContextManager contextManager = manager.getContextManager();
        IContext context = contextManager.getDefaultContext();

        JobContext newContext = new JobContext(name);

        List<IContextParameter> newParamList = new ArrayList<IContextParameter>();
        newContext.setContextParameterList(newParamList);
        JobContextParameter param;
        for (int i = 0; i < context.getContextParameterList().size(); i++) {
            param = (JobContextParameter) context.getContextParameterList().get(i).clone();
            param.setContext(newContext);
            newParamList.add(param);
        }
        // add for bug 9119
        if (manager.isRepositoryContext()) {
            if (contextManager instanceof JobContextManager) {
                JobContextManager jobContextManager = (JobContextManager) contextManager;

                List<IContext> addGroupContext = jobContextManager.getAddGroupContext();
                addGroupContext.add(newContext);
                jobContextManager.setModified(true);
            }
        }

        getAllContexts().add(newContext);

        Collections.sort(getAllContexts(), new ContextCompare());
    }

    /**
     * DOC hshen ContextCompare class global comment. Detailled comment
     */
    private class ContextCompare implements java.util.Comparator<IContext> {

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(IContext o1, IContext o2) {
            String name1 = o1.getName().toUpperCase();
            String name2 = o2.getName().toUpperCase();
            return name1.compareTo(name2);
        }
    }

    protected void removeSelectedContexts() {
        super.removeSelectedContexts();
        IContextManager contextManager = manager.getContextManager();
        JobContextManager jobContextManager = (JobContextManager) contextManager;
        jobContextManager.setModified(true);
        List<IContext> removeGroupContext = jobContextManager.getRemoveGroupContext();
        List dataList = getRemoveData();
        if (dataList == null) {
            return;
        }
        for (Object obj : dataList) {
            if ((obj instanceof IContext) && !removeGroupContext.contains(obj)) {
                removeGroupContext.add((IContext) obj);
            }
        }
    }

    @Override
    protected void editSelectedElement() {

        IInputValidator validator = new IInputValidator() {

            public String isValid(String newText) {
                return toValid(newText);
            }
        };

        IContext selectedContext = (IContext) (getSelection()).getFirstElement();
        String contextName = selectedContext.getName();
        InputDialog inputDial = new InputDialog(getShell(), Messages.getString("ContextProcessSection.12"), //$NON-NLS-1$
                Messages.getString("ContextProcessSection.13", contextName), "", validator); //$NON-NLS-1$ //$NON-NLS-2$
        inputDial.open();
        String returnValue = inputDial.getValue();
        if (returnValue == null || "".equals(returnValue)) {
            return;
        }
        if (manager.getProcess() != null && manager.getProcess().getLastRunContext() != null
                && manager.getProcess().getLastRunContext().sameAs(selectedContext)) {
            manager.getProcess().setLastRunContext(selectedContext);
        }
        renameContext(selectedContext, returnValue);
        refreshViewer();
    }

    private void renameContext(IContext context, String newName) {
        List listBefor = new ArrayList(getData());
        String oldName = context.getName();
        IContextManager contextManager = manager.getContextManager();
        JobContextManager jobContextManager = (JobContextManager) contextManager;
        Map<IContext, String> renameGroupContext = jobContextManager.getRenameGroupContext();
        boolean haveFound = false;
        if (listBefor.contains(context)) {
            haveFound = true;
        }

        context.setName(newName);
        if (haveFound) {
            renameGroupContext.put(context, oldName);
        }
        jobContextManager.setModified(true);
    }

    /**
     * Updates the modify buttons' enabled state based on the current seleciton.
     */
    @Override
    protected void updateButtonAvailability() {
        super.updateButtonAvailability();

        boolean selectDefaultContext = false;
        for (Iterator iterator = getSelection().iterator(); iterator.hasNext();) {
            IContext context = (IContext) iterator.next();
            if (context == manager.getContextManager().getDefaultContext()) {
                selectDefaultContext = true;
                break;
            }

        }
        if (selectDefaultContext) {
            fRemoveButton.setEnabled(false);
            String contextNname = manager.getContextManager().getDefaultContext().getName();
            setDisplayMessage(Messages.getString("ContextProcessSection.RemoveInformation", contextNname)); //$NON-NLS-1$
        } else {
            setDisplayMessage(defaultMesage);
        }

    }

    // /**
    // *
    // * DOC chuang ContextSetConfigurationDialog class global comment. Detailled comment
    // */
    // class MyCheckboxTableViewer extends CheckboxTableViewer {
    //
    // MyCheckboxTableViewer(Table table) {
    // super(table);
    // }
    //
    // /*
    // * (non-Javadoc) Method declared on StructuredViewer.
    // */
    // @Override
    // public void handleSelect(SelectionEvent event) {
    // super.handleSelect(event);
    // // if (event.detail == SWT.CHECK) {
    // // TableItem item = (TableItem) event.item;
    // // if (item.getChecked()) {
    // // return;
    // // } else {
    // // Table table = getTable();
    // // table.setRedraw(false);
    // // for (TableItem it : table.getItems()) {
    // // it.setChecked(false);
    // // }
    // // item.setChecked(true);
    // // for (IContext context : manager.getContextManager().getListContext()) {
    // // if (context.equals(item.getData())) {
    // // manager.onContextChangeDefault(manager.getContextManager(), context);
    // // }
    // // }
    // // // manager.getContextManager().setDefaultContext(null);
    // // table.setRedraw(true);
    // // refresh();
    // // }
    // // } else {
    // // super.handleSelect(event);
    // // }
    // }
    //
    // }
}
