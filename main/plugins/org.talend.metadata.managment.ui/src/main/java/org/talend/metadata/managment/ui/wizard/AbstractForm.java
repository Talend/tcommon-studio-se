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
package org.talend.metadata.managment.ui.wizard;

import java.util.Arrays;
import java.util.Collections;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.commons.utils.platform.PluginChecker;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.connection.FileConnection;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.repository.model.provider.IDBMetadataProvider;
import org.talend.core.ui.context.model.table.ConectionAdaptContextVariableModel;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.metadata.managment.ui.MetadataManagmentUiPlugin;
import org.talend.metadata.managment.ui.i18n.Messages;
import org.talend.metadata.managment.ui.model.IConnParamName;
import org.talend.metadata.managment.ui.preview.ProcessDescription;
import org.talend.metadata.managment.ui.utils.ConnectionContextHelper;
import org.talend.metadata.managment.ui.utils.ShadowProcessHelper;

/**
 * DOC tguiu class global comment. Detailled comment <br/>
 * 
 * $Id: AbstractForm.java 45884 2010-07-23 09:18:06Z hwang $
 * 
 */
public abstract class AbstractForm extends Composite {

    protected static final String PID = MetadataManagmentUiPlugin.PLUGIN_ID;

    /**
     * Common Forms Settings.
     */

    protected static final int VERTICAL_SPACING_FORM = 0;

    protected static final int WIDTH_BUTTON_PIXEL = 100;

    protected static final int HEIGHT_BUTTON_PIXEL = 30;

    /**
     * Main Forms Settings.
     */
    private boolean readOnly = false;

    private boolean isInWizard = true;

    /**
     * Use to manage the status (error, warning, info messages).
     */
    private int statusLevel = IStatus.OK;

    private String status;

    protected Label statusLabel;

    protected ICheckListener listener;

    /**
     * Use to validate the unicity of label use to the metadata.
     */
    protected List<String> existingNames;

    protected static final String DEFAULT_LABEL = "Column"; //$NON-NLS-1$

    /**
     * Use to export field as context. (feature 2449)
     */
    private boolean hasContextBtn = false;

    protected UtilsButton exportContextBtn;

    protected UtilsButton revertContextBtn;

    protected ConnectionItem connectionItem;

    protected Set<IConnParamName> contextParamSet = new HashSet<IConnParamName>();

    protected IDBMetadataProvider provider = null;

    protected EDatabaseTypeName typeName = null;

    protected IMetadataConnection metadataconnection;

    protected IContextManager contextManager;

    private boolean revertingContext;

    /**
     * DOC ocarbone AbstractForm constructor comment.
     * 
     * @param parent
     * @param style
     */
    protected AbstractForm(Composite parent, int style) {
        super(parent, style);
        this.existingNames = Collections.EMPTY_LIST;

    }

    /**
     * DOC ocarbone AbstractForm constructor comment.
     * 
     * @param parent
     * @param style
     * @param existingNames
     */
    protected AbstractForm(Composite parent, int style, String[] existingNames) {
        super(parent, style);
        this.existingNames = existingNames == null ? Collections.EMPTY_LIST : Arrays.asList(existingNames);
    }

    /**
     * DOC tguiu AbstractDelimitedFileStepForm class global comment. Detailled comment <br/>
     * 
     * $Id: AbstractForm.java 45884 2010-07-23 09:18:06Z hwang $
     * 
     */
    public static interface ICheckListener extends EventListener {

        void checkPerformed(AbstractForm source);
    }

    /**
     * checks control values DOC ocarbone Comment method "checkFieldsValue".
     */
    protected abstract boolean checkFieldsValue();

    protected void setupForm() {
        setupForm(false);
    }

    /**
     * DOC ocarbone Comment method "setupForm".
     */
    protected void setupForm(boolean hasContextBtn) {
        this.hasContextBtn = hasContextBtn;
        setDisposeListener();
        addComponents();
        // statusLabel is only use to SWT form / not use to Wizard
        if (!isInWizard) {
            statusLabel = new Label(this, SWT.LEFT);
            statusLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
            statusLabel.setForeground(getDisplay().getSystemColor(SWT.COLOR_RED));
        }
        addFields();
        addExportContextButton();
        addHelpInfoFields();
        initialize();
        addUtilsButtonListeners();
        addFieldsListeners();
        if (this instanceof IRefreshable) {
            addAllKeyListener(getShell(), (IRefreshable) this);
        }
    }

    /**
     * adds addComponents to the form DOC ocarbone Comment method "addComponents". add a swt Form width a statusLabel
     * who is a default element of wizard. create an instance of GridLayout and addField()
     */
    protected void addComponents() {
        // Main Layout
        GridLayout gridLayout = new GridLayout();
        gridLayout.verticalSpacing = VERTICAL_SPACING_FORM;
        this.setLayout(gridLayout);
    }

    /**
     * init the UI and the values.
     * 
     * @param String
     */
    protected abstract void initialize();

    /**
     * adds listeners to controls the utils buttons disposed on the form DOC ocarbone Comment method
     * "addUtilsButtonControls".
     */
    protected abstract void addUtilsButtonListeners();

    /**
     * adds controls to parent composite DOC ocarbone Comment method "addFields".
     * 
     * @param form
     */
    protected abstract void addFields();

    /**
     * adds listeners to controls DOC ocarbone Comment method "addControls".
     */
    protected abstract void addFieldsListeners();

    /**
     * Getter for readOnly. DOC ocarbone Comment method.
     * 
     * @return the readOnly
     */
    protected boolean isReadOnly() {
        return this.readOnly;
    }

    /**
     * Sets readOnly, adapt the Form to Read Only Mode or edition and execute checkFieldsValue. DOC ocarbone Comment
     * method setReadOnly.
     * 
     * @param readOnly the readOnly to set
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        // adapt all the fields to enabled
        adaptFormToReadOnly();

        if (exportContextBtn != null) {
            exportContextBtn.setEnabled(!readOnly);
        }
        if (!readOnly) {
            // adapt the field to the context
            checkFieldsValue();
        }
    }

    /**
     * DOC ycbai Comment method "updateSpecialFieldsState".
     */
    public void updateSpecialFieldsState() {
        // do nothing by default.
    }

    /**
     * DOC ocarbone Comment method "adaptFormToReadOnly".
     */
    protected abstract void adaptFormToReadOnly();

    /**
     * Sets the listener.
     * 
     * @param listener the listener to set
     */
    public void setListener(ICheckListener listener) {
        this.listener = listener;
    }

    /**
     * update Status of the Wizard OR of the label Status.
     * 
     * @param String
     */
    protected void updateStatus(final int status, final String statusLabelText) {
        this.status = statusLabelText;
        if (!isInWizard) {
            if (statusLabelText != null) {
                statusLabel.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
                statusLabel.setText(statusLabelText);
            } else {
                statusLabel.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
                statusLabel.setText(""); //$NON-NLS-1$
            }
        }
        this.statusLevel = status;
        if (listener != null) {
            listener.checkPerformed(this);
        }
    }

    /**
     * Getter for statusOnError.
     * 
     * @return the statusOnError
     */
    public boolean isStatusOnError() {
        return this.statusLevel == IStatus.ERROR;
    }

    /**
     * Getter for statusOnValid.
     * 
     * @return the statusOnValid
     */
    public boolean isStatusOnValid() {
        return this.statusLevel == IStatus.OK;
    }

    public String getStatus() {
        return status;
    }

    public int getStatusLevel() {
        return statusLevel;
    }

    protected boolean isNameAllowed(String name) {
        return existingNames.contains(name);
    }

    /**
     * Getter for isInWizard.
     * 
     * @return the isInWizard
     */
    protected boolean isInWizard() {
        return this.isInWizard;
    }

    /**
     * Sets the isInWizard.
     * 
     * @param isInWizard the isInWizard to set
     */
    protected void setInWizard(boolean isInWizard) {
        this.isInWizard = isInWizard;
    }

    /**
     * Add key listener on each control within the wizard page.
     * 
     * yzhang Comment method "configKeyListener".
     */
    private void addAllKeyListener(Control control, final IRefreshable refresh) {

        control.addKeyListener(new KeyAdapter() {

            private final IRefreshable refreshableComposite = refresh;

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.KeyAdapter#keyReleased(org.eclipse.swt.events.KeyEvent)
             */
            @Override
            public void keyReleased(KeyEvent e) {
                if ((e.keyCode == SWT.F5)) {
                    refreshableComposite.refresh();
                }
            }
        });

        if (control instanceof Composite) {
            Composite composite = (Composite) control;
            Control[] children = composite.getChildren();
            for (Control ctrl : children) {
                addAllKeyListener(ctrl, refresh);
            }
        }

    }

    /**
     * 
     * ggu Comment method "addExportContextButton".
     * 
     * export as context button
     */
    private void addExportContextButton() {
        if (hasContextBtn() && connectionItem != null && !isTOPStandaloneMode()) {
            final int height = 45;
            Composite group = Form.startNewGridLayout(this, 1, true, SWT.CENTER, SWT.CENTER);
            GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
            gridData.minimumHeight = height;
            gridData.heightHint = height;
            group.setLayoutData(gridData);

            Composite exportComposite = Form.startNewGridLayout(group, 2, true, SWT.CENTER, SWT.CENTER);
            GC gc = new GC(exportComposite);

            String displayStr = Messages.getString("AbstractForm.ExportAsContext"); //$NON-NLS-1$
            Point buttonSize = gc.stringExtent(displayStr);
            exportContextBtn = new UtilsButton(exportComposite, displayStr, buttonSize.x + 12, HEIGHT_BUTTON_PIXEL);
            exportContextBtn.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    exportAsContext();
                }
            });
            displayStr = Messages.getString("AbstractForm.RevertContext"); //$NON-NLS-1$
            buttonSize = gc.stringExtent(displayStr);
            revertContextBtn = new UtilsButton(exportComposite, displayStr, buttonSize.x + 12, HEIGHT_BUTTON_PIXEL);
            gc.dispose();

            revertContextBtn.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    revertContext();
                }
            });

            RowLayout layout = (RowLayout) exportComposite.getLayout();
            layout.spacing = 20;
            exportComposite.setLayout(layout);

            refreshContextBtn();
        }
    }

    /**
     * DOC ycbai Comment method "addHelpInfoFields".
     * 
     * <p>
     * Create some fields which display help infos.
     * </p>
     * 
     */
    protected void addHelpInfoFields() {
        // Add nothing by default.
    }

    /**
     * 
     * ggu Comment method "hasContextBtn".
     * 
     * Use to export field as context.(feature 2449)
     */
    public boolean hasContextBtn() {
        return this.hasContextBtn;
    }

    public void setConnectionItem(ConnectionItem connectionItem) {
        ConnectionContextHelper.checkContextMode(connectionItem);
        this.connectionItem = connectionItem;
    }

    protected final boolean isContextMode() {
        if (connectionItem != null) {
            return connectionItem.getConnection().isContextMode();
        }
        return false;
    }

    protected final boolean isTOPStandaloneMode() {
        return PluginChecker.isOnlyTopLoaded();
    }

    protected void exportAsContext() {
        if (hasContextBtn() && connectionItem != null) {
            if (isContextMode()) {
                ConnectionContextHelper.openInConetxtModeDialog();
            } else {
                String defaultContextName = ConnectionContextHelper.convertContextLabel(connectionItem.getProperty().getLabel());
                Map<ContextItem, List<ConectionAdaptContextVariableModel>> variableModels = ConnectionContextHelper
                        .exportAsContext(defaultContextName, connectionItem, getConetxtParams());
                contextManager = ConnectionContextHelper.contextManager;

                if (variableModels != null) {
                    Iterator<ContextItem> contextItor = variableModels.keySet().iterator();
                    while (contextItor.hasNext()) {
                        ContextItem contextItem = contextItor.next();
                        List<ConectionAdaptContextVariableModel> apaptModels = variableModels.get(contextItem);
                        if (contextItem != null && apaptModels.size() == 0) { // create
                            if (contextManager instanceof JobContextManager) {
                                Map<String, String> map = ((JobContextManager) contextManager).getNameMap();
                                // set properties for context mode
                                ConnectionContextHelper.setPropertiesForContextMode(defaultContextName, connectionItem,
                                        contextItem, getConetxtParams(), map);
                            }
                        } else {
                            // set properties for exist context
                            ConnectionContextHelper.setPropertiesForExistContextMode(connectionItem, getConetxtParams(),
                                    variableModels);
                        }
                        // refresh current UI.
                        initialize();
                        adaptFormToEditable();
                    }
                }
            }
        }
    }

    protected void revertContext() {
        if (hasContextBtn() && connectionItem != null) {
            if (isContextMode()) {
                ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(getShell(),
                        connectionItem.getConnection(), true);
                if (contextType != null) { // choose
                    revertingContext = true;
                    ConnectionContextHelper.revertPropertiesForContextMode(connectionItem, contextType);
                    adaptContextModeToReversion();
                    revertingContext = false;
                }
            } else {
                ConnectionContextHelper.openOutConetxtModeDialog();
            }
        }
    }

    private void refreshContextBtn() {
        if (exportContextBtn != null) {
            exportContextBtn.setEnabled(!isContextMode());
        }
        if (revertContextBtn != null) {
            revertContextBtn.setEnabled(isContextMode());
        }
    }

    protected boolean isNeedFillDefaults() {
        return !isContextMode() && revertingContext == false;
    }

    protected void adaptFormToEditable() {
        refreshContextBtn();
    }

    protected void adaptContextModeToReversion() {
        initialize();
        adaptFormToEditable();
        checkFieldsValue();
    }

    protected void clearContextParams() {
        contextParamSet.clear();
    }

    protected Set<IConnParamName> getConetxtParams() {
        return contextParamSet; // for db and file connection
    }

    protected void addContextParams(IConnParamName param, boolean added) {
        if (added) {
            contextParamSet.add(param);
        } else {
            if (contextParamSet.contains(param)) {
                contextParamSet.remove(param);
            }
        }
    }

    /**
     * DOC YeXiaowei Comment method "headerRowForSchemaRowNames".
     * 
     * @param originalValueConnection
     * @param processDescription
     */
    protected void headerRowForSchemaRowNames(FileConnection originalValueConnection, ProcessDescription processDescription) {
        // Adapt Header width firstRowIsCaption to preview the first line on caption or not
        if (originalValueConnection.isFirstLineCaption()) {
            int i = ConnectionContextHelper.convertValue(originalValueConnection.getHeaderValue());
            processDescription.setHeaderRow(i - 1);
        }
    }

    /**
     * 
     * cli Comment method "setDisposeListener".
     * 
     * (bug 6976)
     */
    private void setDisposeListener() {
        this.addDisposeListener(new DisposeListener() {

            @Override
            public void widgetDisposed(DisposeEvent event) {
                try {
                    processWhenDispose();
                    ShadowProcessHelper.forceStopPreview();
                    //
                    ExtractMetaDataUtils.getInstance().closeConnection(true);
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
        });
    }

    protected boolean useProvider() {
        return typeName != null && typeName.isUseProvider() && provider != null;
    }

    /**
     * 
     * cli Comment method "processWhenDispose".
     * 
     * (bug 6976)
     */
    protected void processWhenDispose() {
        //
    }

    public void refreshLinks() {

    }

    protected void hideControl(Control control, boolean hide) {
        Object layoutData = control.getLayoutData();
        if (layoutData instanceof GridData) {
            GridData data = (GridData) layoutData;
            data.exclude = hide;
            control.setLayoutData(data);
            control.setVisible(!hide);
            if (control.getParent() != null) {
                control.getParent().layout();
            }
        }
    }
}
