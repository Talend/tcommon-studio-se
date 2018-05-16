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
package org.talend.metadata.managment.ui.wizard.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.ui.context.model.table.ConectionAdaptContextVariableModel;
import org.talend.core.ui.context.model.template.ContextConstant;
import org.talend.metadata.managment.ui.i18n.Messages;

public class ContextAdaptConectionSelectPage extends WizardPage {

    public static final int CONTEXT_COLUMN_WIDTH = 200;

    private static final String EMPTY_VALUE = "";

    private TableViewer viewer;

    private List<ConectionAdaptContextVariableModel> adaptModels = new ArrayList<ConectionAdaptContextVariableModel>();;

    private IContextManager contextManager;

    private Set<String> connectionVaribles;

    public int step;

    public ContextAdaptConectionSelectPage(IContextManager contextManager, Set<String> connectionVaribles, int step) {
        super(Messages.getString("ReuseRepositoryContext.name")); //$NON-NLS-1$
        setTitle(Messages.getString("ContextAdaptConectionSelectPage.title")); //$NON-NLS-1$
        this.contextManager = contextManager;
        this.connectionVaribles = connectionVaribles;
        this.step = step;
    }

    @Override
    public void createControl(Composite parent) {

        Composite composite = new Composite(parent, SWT.NONE);
        composite.setBackground(parent.getBackground());
        composite.setFont(parent.getFont());

        GridLayout layout = new GridLayout();
        layout.verticalSpacing = 10;
        composite.setLayout(layout);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.widthHint = 400;
        composite.setLayoutData(gridData);
        GridData gd = new GridData();

        SashForm sashForm = new SashForm(composite, SWT.HORIZONTAL);
        gd = new GridData(GridData.FILL_BOTH);
        gd.widthHint = 400;
        sashForm.setLayoutData(gd);

        createDescription(sashForm);
        createTableViewer(sashForm);
        sashForm.setWeights(new int[] { 3, 5 });
        setControl(composite);
    }

    private void createDescription(Composite parent) {
        Text descriptionText = new Text(parent, SWT.BORDER | SWT.WRAP);
        descriptionText.setText(Messages.getString("ContextAdaptConectionSelectPage.selectOrCustomVariableNew")); //$NON-NLS-1$
        descriptionText.setEditable(false);
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.widthHint = 100;
        descriptionText.setLayoutData(gd);
        descriptionText.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseDown(MouseEvent e) {
                validateField();
            }
        });
    }

    private void createTableViewer(Composite parent) {
        viewer = new TableViewer(parent, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        Table table = viewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.heightHint = 120;
        gridData.widthHint = 200;
        table.setLayoutData(gridData);

        createColumnsAndCellEditors(table);

        // set the content provider
        viewer.setContentProvider(new IStructuredContentProvider() {

            @Override
            public void dispose() {
            }

            @Override
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }

            @Override
            public Object[] getElements(Object inputElement) {
                return adaptModels.toArray();
            }
        });

        // set the label provider
        viewer.setLabelProvider(new ContextResueTableLabelProvider());
        viewer.setInput(adaptModels);
        viewer.getTable().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseDown(MouseEvent e) {
                validateField();
            }
        });
    }

    class ContextResueTableLabelProvider implements ITableLabelProvider, ITableColorProvider {

        @Override
        public boolean isLabelProperty(Object element, String property) {
            return false;
        }

        @Override
        public void dispose() {
        }

        @Override
        public void addListener(ILabelProviderListener listener) {
        }

        @Override
        public void removeListener(ILabelProviderListener listener) {
        }

        @Override
        public String getColumnText(Object element, int columnIndex) {
            String variable = "";
            ConectionAdaptContextVariableModel currentModel = (ConectionAdaptContextVariableModel) element;
            switch (columnIndex) {
            case 0:
                variable = currentModel.getName();
                break;
            case 1:
                variable = currentModel.getValue();
                break;
            }
            return variable;
        }

        @Override
        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        @Override
        public Color getForeground(Object element, int columnIndex) {
            return null;
        }

        @Override
        public Color getBackground(Object element, int columnIndex) {
            if (element instanceof ConectionAdaptContextVariableModel) {
                ConectionAdaptContextVariableModel currentModel = (ConectionAdaptContextVariableModel) element;
                boolean defaultColour = true;
                switch (columnIndex) {
                case 0:
                    defaultColour = true;
                    break;

                case 1:
                    defaultColour = true;
                    break;

                case 2:
                    if (currentModel.getValue().equals(ContextConstant.NEW_VARIABLE)) {
                        defaultColour = true;
                    } else {
                        defaultColour = false;
                    }
                    break;
                }
                return Display.getDefault().getSystemColor(defaultColour ? SWT.COLOR_WHITE : SWT.COLOR_GRAY);
            }
            return null;
        }
    }

    private void validateField() {
        boolean hasError = false;
        String errorMessage = EMPTY_VALUE;
        int contextVariableNumber = adaptModels.size();
        int connectionFieldNumber = connectionVaribles.size();

        if (adaptModels.size() < connectionVaribles.size()) {
            errorMessage = Messages.getString("ContextAdaptConectionSelectPage.lackOfVariablesMsg", contextVariableNumber,
                    connectionFieldNumber);
            hasError = true;
        } else {
            for (ConectionAdaptContextVariableModel model : adaptModels) {
                if (model.getValue().equals(EMPTY_VALUE)) {
                    contextVariableNumber -= 1;
                }
            }
            if (contextVariableNumber < connectionFieldNumber) {
                errorMessage = Messages.getString("ContextAdaptConectionSelectPage.errorMsgNew"); //$NON-NLS-1$
                hasError = true;
            } else {
                out: for (int i = 0; i < adaptModels.size(); i++) {
                    String value = adaptModels.get(i).getValue();
                    if (value != null && !value.equals(EMPTY_VALUE)) {
                        for (int j = adaptModels.size() - 1; j > i; j--) {
                            if (value.equals(adaptModels.get(j).getValue())) {
                                errorMessage = Messages.getString("ContextAdaptConectionSelectPage.duplicateErrorMsg", value); //$NON-NLS-1$
                                hasError = true;
                                break out;
                            }
                        }
                    }
                }
            }
        }

        if (!hasError) {
            setPageComplete(true);
            setErrorMessage(null);
        } else {
            setPageComplete(false);
            setErrorMessage(errorMessage);
        }
    }

    private List<IContextParameter> getContextVariableList() {
        List<IContext> contextVariableList = contextManager.getListContext();
        List<IContextParameter> contextVariables = contextVariableList.get(0).getContextParameterList();
        return contextVariables;
    }

    /**
     * ldong Comment method "createColumnsAndCellEditors".
     */
    private void createColumnsAndCellEditors(final Table table) {
        TableColumn column = new TableColumn(table, SWT.NONE);
        column.setText(ContextConstant.VARIABLE_COLUMN_NAME);
        column.setWidth(200);

        column = new TableColumn(table, SWT.NONE);
        column.setText(ContextConstant.CONNECTION_FIELD_NAME);
        column.setWidth(200);

        List<String> connnectionFields = new ArrayList<String>();
        for (String cp : connectionVaribles) {
            connnectionFields.add(cp);
        }
        String[] values = (String[]) ArrayUtils.clone(connectionVaribles.toArray(new String[0]));

        CellEditor[] cellEditor = new CellEditor[3];
        cellEditor[0] = new TextCellEditor(table, SWT.READ_ONLY);
        cellEditor[1] = new ComboBoxCellEditor(table, values, SWT.NONE);
        cellEditor[2] = new TextCellEditor(table, SWT.NONE);
        ((CCombo) cellEditor[1].getControl()).setEditable(false);
        // set column properties
        viewer.setColumnProperties(new String[] { ContextConstant.VARIABLE_COLUMN_NAME, ContextConstant.CONNECTION_FIELD_NAME });
        // Set cell editors
        viewer.setCellEditors(cellEditor);
        // set the Cell Modifier
        viewer.setCellModifier(new ContextVariableSelectCellModifier(viewer, values));
    }

    public List<ConectionAdaptContextVariableModel> getVariableModels() {
        return adaptModels;
    }

    public void setVariableModels(List<ConectionAdaptContextVariableModel> variableModels) {
        this.adaptModels = variableModels;
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            initTable();
        }
    }

    private void initTable() {
        ContextModeWizard currentWizard = null;
        if (getWizard() instanceof ContextModeWizard) {
            currentWizard = (ContextModeWizard) getWizard();
        }
        if (currentWizard != null) {
            this.contextManager = currentWizard.getResueContextManager();
            // in case click back and chose another context ,and next again,we need to consider to claer the models
            if (!isNeedClearModels(adaptModels)) {
                adaptModels.clear();
            }
            if (adaptModels.size() == 0) {
                for (IContextParameter contextVariable : getContextVariableList()) {
                    adaptModels.add(new ConectionAdaptContextVariableModel(contextVariable.getName(), "", ""));
                }
            }
            currentWizard.setAdaptModels(this.adaptModels);
        }
        refresh();
        validateField();
    }

    private boolean isNeedClearModels(List<ConectionAdaptContextVariableModel> adaptModels) {
        boolean isSame = true;
        List<String> contextVariableNames = new ArrayList<String>();
        List<IContext> contextVariableList = this.contextManager.getListContext();
        List<IContextParameter> contextVariables = contextVariableList.get(0).getContextParameterList();
        for (IContextParameter para : contextVariables) {
            contextVariableNames.add(para.getName());
        }

        if (adaptModels.size() == contextVariableNames.size()) {
            for (ConectionAdaptContextVariableModel model : adaptModels) {
                String modelName = model.getName();
                if (!contextVariableNames.contains(modelName)) {
                    isSame = false;
                    break;
                }
            }
        } else {
            isSame = false;
        }

        return isSame;
    }

    private void refresh() {
        final Table table = viewer.getTable();
        TableColumn[] columns = table.getColumns();
        for (TableColumn tableColumn : columns) {
            tableColumn.dispose();
        }

        createColumnsAndCellEditors(table);

        viewer.setInput(adaptModels);
    }
}
