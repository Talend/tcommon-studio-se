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
package org.talend.core.ui.metadata.celleditor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView;
import org.talend.commons.ui.swt.dialogs.IConfigModuleDialog;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerUIService;
import org.talend.core.model.process.IElement;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess2;
import org.talend.core.runtime.services.IGenericDBService;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.ui.process.IGEFProcess;
import org.talend.core.ui.services.IDesignerCoreUIService;

/**
 * ggu class global comment. Detailled comment
 */
public class ModuleListCellEditor extends DialogCellEditor {

    private final IElementParameter param, tableParam;

    private AbstractDataTableEditorView<?> tableEditorView;

    private Text defaultLabel;

    private String oldValue;

    public ModuleListCellEditor(Composite parent, IElementParameter param, IElementParameter tableParam) {
        super(parent, SWT.NONE);
        this.param = param;
        this.tableParam = tableParam;
    }

    @Override
    protected Control createContents(Composite cell) {
        defaultLabel = new Text(cell, SWT.LEFT);
        defaultLabel.setFont(cell.getFont());
        defaultLabel.setBackground(cell.getBackground());
        defaultLabel.addFocusListener(new FocusAdapter() {

            private String memo = "";

            @Override
            public void focusGained(FocusEvent e) {
                memo = defaultLabel.getText();
                super.focusGained(e);
            }

            @Override
            public void focusLost(FocusEvent e) {

                String newValue = defaultLabel.getText();
                if (newValue == null || newValue.trim().equals("")) { //$NON-NLS-1$
                    defaultLabel.setText(oldValue);
                    return;
                }
                if (newValue.equals(oldValue)) {
                    return;
                }
                boolean newValidState = isCorrect(newValue);
                if (newValidState && !(newValue.equals(memo))) {
                    doSetValue(newValue);
                    setModuleValue(newValue);
                }
            }
        });
        return defaultLabel;
    }

    @Override
    protected void updateContents(Object value) {
        if (defaultLabel == null) {
            return;
        }

        String text = "";//$NON-NLS-1$
        if (value != null) {
            text = value.toString();
        }
        oldValue = text;
        defaultLabel.setText(text);
    }

    @Override
    protected void doSetFocus() {
        if (defaultLabel != null && !defaultLabel.isDisposed()) {
            defaultLabel.setFocus();
        }
    }

    private void executeCommand(Command cmd) {
        IProcess2 process = null;
        if (tableParam != null) {
            if (tableParam.getElement() instanceof IProcess2) {
                process = (IProcess2) tableParam.getElement();
            } else if (tableParam.getElement() instanceof INode) {
                if (((INode) tableParam.getElement()).getProcess() instanceof IProcess2) {
                    process = (IProcess2) ((INode) tableParam.getElement()).getProcess();
                }
            }
        }

        boolean executed = false;
        if (process != null && process instanceof IGEFProcess) {
            IDesignerCoreUIService designerCoreUIService = CoreUIPlugin.getDefault().getDesignerCoreUIService();
            if (designerCoreUIService != null) {
                executed = designerCoreUIService.executeCommand((IGEFProcess) process, cmd);
            }
        }
        if (!executed) {
            cmd.execute();
        }
    }

    public AbstractDataTableEditorView<?> getTableEditorView() {
        return this.tableEditorView;
    }

    private TableViewer getTableViewer() {
        if (this.tableEditorView != null) {
            AbstractExtendedTableViewer<?> extendedTableViewer = this.tableEditorView.getExtendedTableViewer();
            if (extendedTableViewer != null) {
                TableViewerCreator<?> tableViewerCreator = extendedTableViewer.getTableViewerCreator();
                if (tableViewerCreator != null) {
                    return tableViewerCreator.getTableViewer();
                }
            }
        }
        return null;
    }

    public void setTableEditorView(AbstractDataTableEditorView<?> tableEditorView) {
        this.tableEditorView = tableEditorView;
    }

    @Override
    protected Object openDialogBox(Control cellEditorWindow) {
        String value = (String) getValue();
        // TODO for cConfig , keep the same dialog as before ,need esb team help to check latter
        IElementParameter componentName = param.getElement().getElementParameter("COMPONENT_NAME");
        if (componentName != null && "cConfig".equals(componentName.getValue())) {
            ModuleListDialog dialog = new ModuleListDialog(cellEditorWindow.getShell(), value, this.param, false);
            if (dialog.open() == Window.OK) {
                String selecteModule = dialog.getSelecteModule();
                if (selecteModule != null && (value == null || !value.equals(selecteModule) || dialog.isSelectChanged())) {
                    setModuleValue(selecteModule, dialog.getSelectedJarPath(), dialog.getSelectedJarVersion());
                    return selecteModule;
                }
            }
        } else {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerUIService.class)) {
                ILibraryManagerUIService libUiService = (ILibraryManagerUIService) GlobalServiceRegister.getDefault().getService(
                        ILibraryManagerUIService.class);
                IConfigModuleDialog dialog = libUiService.getConfigModuleDialog(cellEditorWindow.getShell(), value);
                if (dialog.open() == IDialogConstants.OK_ID) {
                    String selecteModule = dialog.getModuleName();
                    if (selecteModule != null && (value == null || !value.equals(selecteModule))) {
                        setModuleValue(selecteModule, null, null);
                        return selecteModule;
                    }
                }
            }
        }
        return null;
    }

    private void setModuleValue(String newValue, String newVal, String nexusVersion) {
        int index = 0;
        if (getTableViewer() != null) {
            if (getTableViewer().getTable() != null && !getTableViewer().getTable().isDisposed()) {
                index = getTableViewer().getTable().getSelectionIndex();
            }
        } else {
            return;
        }
        // enable to refresh component setting after change modules.
        IElement element = this.tableParam.getElement();
        boolean isNotCConfig = element.getElementParameter("COMPONENT_NAME") == null ? 
                true : !"cConfig".equals(element.getElementParameter("COMPONENT_NAME").getValue());
        if (element != null && isNotCConfig) {
            IElementParameter updateComponentsParam = element.getElementParameter("UPDATE_COMPONENTS"); //$NON-NLS-1$
            if (updateComponentsParam != null) {
                updateComponentsParam.setValue(Boolean.TRUE);
            }
        }
        //
        executeCommand(new ModelChangeCommand(tableParam, param.getName(), newValue, index));

        if (newVal != null) {
            executeCommand(new ModelChangeCommand(tableParam, "JAR_PATH", newVal, index));
        }
        if (nexusVersion != null) {
            executeCommand(new ModelChangeCommand(tableParam, "JAR_NEXUS_VERSION", nexusVersion, index));
        }

        oldValue = newValue;
        if (getTableViewer() != null) {
            getTableViewer().refresh(true);
        }
    }

    private void setModuleValue(String newValue) {
        setModuleValue(newValue, null, null);
    }

    /**
     * 
     * ggu ModelChangeCommand class global comment. Detailled comment
     */
    class ModelChangeCommand extends Command {

        private final IElementParameter param;

        private final String columnName;

        private final String value;

        private final int index;

        public ModelChangeCommand(IElementParameter param, String columnName, String value, int index) {
            super();
            this.param = param;
            this.columnName = columnName;
            this.value = value;
            this.index = index;
        }

        @Override
        public void execute() {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> values = (List<Map<String, Object>>) param.getValue();
            if (values == null) {
                values = new ArrayList<Map<String, Object>>();
                param.setValue(values);
            }
            if (values.size() > index) {
                Map<String, Object> line = values.get(index);
                if (line != null) {
                    line.put(columnName, value);
                }
            }
            param.setValue(values);
            // CorePlugin.getDefault().getLibrariesService().resetModulesNeeded();
        }

        @Override
        public void undo() {
            super.undo();
        }

    }

}
