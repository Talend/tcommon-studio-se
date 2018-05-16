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
package org.talend.repository.ui.wizards.metadata.connection.files.excel;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jxl.read.biff.BiffException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.dialogs.ContainerCheckedTreeViewer;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.commons.ui.swt.formtools.LabelledCombo;
import org.talend.commons.ui.swt.formtools.LabelledFileField;
import org.talend.commons.ui.swt.formtools.UtilsButton;
import org.talend.commons.ui.utils.PathUtils;
import org.talend.core.model.metadata.IMetadataContextModeManager;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.repository.model.preview.ExcelSchemaBean;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.metadata.ui.wizards.form.AbstractExcelFileStepForm;

/**
 * DOC yexiaowei class global comment. Detailled comment
 */
public class ExcelFileStep1Form extends AbstractExcelFileStepForm {

    private static final int GROUP_WIDTH = 120;

    private static final int WIDTH = 60;

    private LabelledCombo serverCombo = null;

    private LabelledFileField fileField = null;

    private LabelledCombo sheetsCombo = null;

    private TableViewer viewer = null;

    private boolean readOnly;

    private UtilsButton cancelButton;

    private ExcelReader excelReader = null;

    private CheckboxTreeViewer sheetViewer = null;

    private static final int WIDTH_GRIDDATA_PIXEL = 300;

    private List<String> allsheets = new ArrayList<String>();

    private Button selectModeBtn = null;

    private LabelledCombo modeCombo = null;

    private ExcelSchemaBean bean;

    private boolean isXlsx = false;

    private String generationMode = null;

    private static final String USER_MODE = "USER_MODE";

    private static final String EVENT_MODE = "EVENT_MODE";

    private Set<String> errorSet = new HashSet<String>();

    private static final String ERRORSET_FILEFIELD = "fileField.modifyText.Exception";

    private static final String ERRORSET_EXCELPREVIEW = "viewSheet.preview.Exception";

    /**
     * DOC yexiaowei ExcelFileStep1Form constructor comment.
     * 
     * @param parent
     * @param connectionItem
     * @param existingNames
     */
    public ExcelFileStep1Form(Composite parent, ConnectionItem connectionItem, String[] existingNames,
            IMetadataContextModeManager contextModeManager) {
        super(parent, connectionItem, existingNames);
        setContextModeManager(contextModeManager);
        setupForm();
    }

    public ExcelFileStep1Form(Composite parent, ConnectionItem connectionItem, String[] existingNames,
            IMetadataContextModeManager contextModeManager, ExcelSchemaBean bean) {
        super(parent, connectionItem, existingNames);
        setContextModeManager(contextModeManager);
        setupForm();
        this.bean = bean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#adaptFormToReadOnly()
     */
    @Override
    protected void adaptFormToReadOnly() {
        readOnly = isReadOnly();
        fileField.setReadOnly(isReadOnly());
        updateStatus(IStatus.OK, ""); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFields()
     */
    @Override
    protected void addFields() {
        Group group = Form.createGroup(this, 1, Messages.getString("FileStep2.groupDelimitedFileSettings"), GROUP_WIDTH); //$NON-NLS-1$
        Composite compositeFileLocation = Form.startNewDimensionnedGridLayout(group, 3, WIDTH_GRIDDATA_PIXEL, 120);

        String[] serverLocation = { "Localhost 127.0.0.1" }; //$NON-NLS-1$
        serverCombo = new LabelledCombo(compositeFileLocation, Messages.getString("FileStep1.server"), Messages //$NON-NLS-1$
                .getString("FileStep1.serverTip"), serverLocation, 2, true, SWT.NONE); //$NON-NLS-1$

        String[] extensions = { "*.xls;*.xlsx;*.*" }; //$NON-NLS-1$ //  hywang add "*.xlsx"
        fileField = new LabelledFileField(compositeFileLocation, Messages.getString("FileStep1.filepath"), extensions); //$NON-NLS-1$

        selectModeBtn = new Button(compositeFileLocation, SWT.CHECK);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 3;
        selectModeBtn.setLayoutData(gridData);
        selectModeBtn.setText(Messages.getString("FileStep1.modeButText")); //$NON-NLS-1$

        String[] modeItem = { "Memory-consuming(User mode)", "Less memory consumed for large excel(Event mode)" };//$NON-NLS-1$
        modeCombo = new LabelledCombo(compositeFileLocation,
                Messages.getString("FileStep1.readMode"), "", modeItem, 2, true, SWT.NONE); //$NON-NLS-1$
        modeCombo.setReadOnly(true);
        modeCombo.select(0);// default select index 1

        int numColumnForViewer = 4;

        viewerGroup = Form.createGroup(this, numColumnForViewer, Messages.getString("ExcelFileStep1Form.fileSetting"), 150); //$NON-NLS-1$

        createSheetsSelectViewer(viewerGroup);

        Composite compositeExcelViewer = Form.startNewDimensionnedGridLayout(viewerGroup, 2, WIDTH_GRIDDATA_PIXEL, 150);

        sheetsCombo = new LabelledCombo(compositeExcelViewer, Messages.getString("ExcelFileStep1Form.sheet.choice"), Messages //$NON-NLS-1$
                .getString("ExcelFileStep1Form.sheet.tip"), new String[] { "Sheet1" }, 1, false, SWT.NONE); //$NON-NLS-1$ //$NON-NLS-2$

        createTableViewer(compositeExcelViewer);

        if (!isInWizard()) {
            Composite compositeBottomButton = Form.startNewGridLayout(this, 2, false, SWT.CENTER, SWT.CENTER);
            cancelButton = new UtilsButton(compositeBottomButton, Messages.getString("CommonWizard.cancel"), WIDTH_BUTTON_PIXEL, //$NON-NLS-1$
                    HEIGHT_BUTTON_PIXEL);
        }

        makeViewerGroupAvailable(false);
        addUtilsButtonListeners();
    }

    /**
     * DOC YeXiaowei Comment method "createSheetsSelectViewer".
     * 
     * @param group
     */
    private void createSheetsSelectViewer(Group group) {
        sheetsViewerComposite = Form.startNewDimensionnedGridLayout(group, 2, 80, 150);
        Label label = new Label(sheetsViewerComposite, SWT.NONE);
        label.setText(Messages.getString("ExcelFileStep1Form.sheetPara")); //$NON-NLS-1$

        Combo place = new Combo(sheetsViewerComposite, SWT.NONE);
        place.setVisible(false);

        createPerlSheetsViewer(sheetsViewerComposite);
    }

    private void makeViewerGroupAvailable(boolean available) {
        if (viewerGroup != null) {
            viewerGroup.setEnabled(available);
        }
    }

    private void createPerlSheetsViewer(Composite parent) {
        sheetViewer = new ContainerCheckedTreeViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE | SWT.BORDER);

        sheetViewer.setContentProvider(new ITreeContentProvider() {

            @Override
            public Object[] getChildren(Object parentElement) {
                if (parentElement instanceof SheetNode) {
                    return ((SheetNode) parentElement).getChildren().toArray();
                }
                return null;
            }

            @Override
            public Object getParent(Object element) {

                return null;
            }

            @Override
            public boolean hasChildren(Object element) {
                if (element instanceof SheetNode) {
                    return ((SheetNode) element).getChildren() != null && ((SheetNode) element).getChildren().size() > 0;
                }
                return false;
            }

            @Override
            public Object[] getElements(Object inputElement) {
                if (inputElement instanceof List) {
                    return ((List) inputElement).toArray();
                }
                return null;
            }

            @Override
            public void dispose() {

            }

            @Override
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

            }
        });

        sheetViewer.setLabelProvider(new ILabelProvider() {

            @Override
            public Image getImage(Object element) {
                return null;
            }

            @Override
            public String getText(Object element) {
                if (element instanceof SheetNode) {
                    return ((SheetNode) element).getLabel();
                }
                return null;
            }

            @Override
            public void addListener(ILabelProviderListener listener) {

            }

            @Override
            public void dispose() {

            }

            @Override
            public boolean isLabelProperty(Object element, String property) {

                return false;
            }

            @Override
            public void removeListener(ILabelProviderListener listener) {

            }

        });
        initSheetViewer();
        sheetViewer.expandAll();

        GridData data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 2;
        sheetViewer.getTree().setLayoutData(data);

        addTreeListener();
    }

    /**
     * DOC YeXiaowei Comment method "resetSheetViewer".
     */
    private void initSheetViewer() {
        if (sheetViewer == null) {
            return;
        }
        List<SheetNode> sheetChildren = new ArrayList<SheetNode>();
        for (String s : allsheets) {
            SheetNode current = new SheetNode(rootNode, s);
            sheetChildren.add(current);
        }
        rootNode.setChildren(sheetChildren);

        List<SheetNode> rootList = new ArrayList<SheetNode>();
        rootList.add(rootNode);
        sheetViewer.setInput(rootList);
        sheetViewer.expandAll();
    }

    private void addTreeListener() {

        sheetViewer.addCheckStateListener(new ICheckStateListener() {

            @Override
            public void checkStateChanged(CheckStateChangedEvent event) {
                fillSheetList();
                checkFieldsValue();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void fillSheetList() {
        Object[] x = sheetViewer.getCheckedElements();
        ArrayList sl = getConnection().getSheetList();
        if (sl == null) {
            sl = new ArrayList();
            getConnection().setSheetList(sl);
        }
        sl.clear();
        boolean allSheets = false;
        for (Object o : x) {
            if (o instanceof SheetNode) {
                if (o.equals(rootNode)) {
                    if (!sheetViewer.getGrayed(o)) {
                        allSheets = true;
                    }
                } else {
                    sl.add(((SheetNode) o).getLabel());
                }
            }
        }
        getConnection().setSelectAllSheets(allSheets);
    }

    /**
     * DOC yexiaowei Comment method "createTableViewer".
     * 
     * @param compositeExcelViewer
     */
    private void createTableViewer(Composite compositeExcelViewer) {

        viewer = new TableViewer(compositeExcelViewer, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);

        Table table = viewer.getTable();

        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        GridData layData = new GridData(GridData.FILL_BOTH);
        layData.horizontalSpan = 2;

        table.setLayoutData(layData);

        viewer.setContentProvider(new IStructuredContentProvider() {

            @Override
            public Object[] getElements(Object inputElement) {
                if (inputElement instanceof List) {
                    return ((List) inputElement).toArray();
                }
                return null;
            }

            @Override
            public void dispose() {

            }

            @Override
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

            }

        });

        viewer.setLabelProvider(new ITableLabelProvider() {

            @Override
            public Image getColumnImage(Object element, int columnIndex) {
                return null;
            }

            @Override
            public String getColumnText(Object element, int columnIndex) {
                if (element instanceof String[]) {
                    try {
                        return ((String[]) element)[columnIndex];
                    } catch (Exception e) {
                        return ""; //$NON-NLS-1$
                    }
                }
                return ""; //$NON-NLS-1$
            }

            @Override
            public void addListener(ILabelProviderListener listener) {

            }

            @Override
            public void dispose() {

            }

            @Override
            public boolean isLabelProperty(Object element, String property) {
                return true;
            }

            @Override
            public void removeListener(ILabelProviderListener listener) {

            }

        });

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFieldsListeners()
     */
    @Override
    protected void addFieldsListeners() {

        serverCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                getConnection().setServer(serverCombo.getText());
                checkFieldsValue();
            }
        });

        fileField.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                if (!isContextMode()) {
                    if (isVisible()) {
                        errorSet.remove(ERRORSET_FILEFIELD);
                        try {
                            viewExcelFile();
                            restoreSelectedSheets();
                            checkFieldsValue();
                        } catch (BiffException e1) {
                            getConnection().setFilePath(null);
                            errorSet.add(ERRORSET_FILEFIELD);
                            updateErrorStatus(e1.getMessage());
                            makeViewerGroupAvailable(false);
                        } catch (IOException e1) {
                            getConnection().setFilePath(null);
                            errorSet.add(ERRORSET_FILEFIELD);
                            updateErrorStatus(e1.getMessage());
                            makeViewerGroupAvailable(false);
                        }
                        if (isXlsx) {
                            selectModeBtn.setSelection(true);
                            modeCombo.setReadOnly(false);
                        } else {
                            selectModeBtn.setSelection(false);
                            modeCombo.setReadOnly(true);
                        }
                        if ((EVENT_MODE).equals(generationMode)) {
                            modeCombo.select(1);
                        } else {
                            modeCombo.select(0);
                        }
                        updateStatus();
                    }
                }
            }

        });

        sheetsCombo.getCombo().addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (isVisible()) {
                    String sheet = sheetsCombo.getText().trim();
                    viewSheet(sheet);
                    checkFieldsValue();
                }
            }
        });

        selectModeBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (selectModeBtn.getSelection()) {
                    modeCombo.setReadOnly(false);
                } else {
                    modeCombo.setReadOnly(true);
                    generationMode = USER_MODE;
                }
                updateStatus();
            }
        });

        modeCombo.getCombo().addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                int index = modeCombo.getSelectionIndex();
                if (index == 0) {
                    generationMode = USER_MODE;
                } else {
                    generationMode = EVENT_MODE;
                }
                updateStatus();
            }
        });

        modeCombo.getCombo().addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                checkFieldsValue();
            }
        });
    }

    private void updateStatus() {
        if (isXlsx) {
            bean.setGenerationMode(generationMode);
            getConnection().setGenerationMode(generationMode);
        } else {
            bean.setGenerationMode(null);
            getConnection().setGenerationMode(null);
        }
    }

    /**
     * DOC YeXiaowei Comment method "reserverSelectedSheets".
     */
    private void restoreSelectedSheets() {
        if (!getConnection().isSelectAllSheets()) {
            ArrayList<?> sheetList = getConnection().getSheetList();
            List<String> removed = new ArrayList<String>();
            if (sheetList != null && !sheetList.isEmpty()) {
                for (int i = 0; i < sheetList.size(); i++) {
                    String curSheet = (String) sheetList.get(i);
                    boolean needRemove = true;
                    for (String sheet : allsheets) {
                        if (curSheet.equals(sheet)) {
                            needRemove = false;
                            break;
                        }
                    }
                    if (needRemove) {
                        removed.add(curSheet);
                    }
                }
                if (!removed.isEmpty()) {
                    sheetList.removeAll(removed);
                }
            }
        }
    }

    /**
     * DOC yexiaowei Comment method "readAndViewExcelFile".
     */
    private void viewExcelFile() throws IOException, BiffException {

        String fileStr = fileField.getText();
        String filePath = null;

        if (isContextMode() && getContextModeManager() != null) {
            fileStr = getContextModeManager().getOriginalValue(getConnection().getFilePath());
            fileStr = TalendQuoteUtils.removeQuotes(fileStr);
            filePath = PathUtils.getPortablePath(fileStr);
        } else {
            filePath = PathUtils.getPortablePath(fileStr);
        }

        if (filePath.toLowerCase().endsWith(".xlsx")) {
            isXlsx = true;
        } else if (filePath.toLowerCase().endsWith(".xls")) {
            isXlsx = false;
        }

        String genMode = getConnection().getGenerationMode();
        if (isXlsx && genMode != null && !("").equals(genMode)) {
            generationMode = getConnection().getGenerationMode();
        }

        if (isXlsx) {
            excelReader = new ExcelReader(filePath, isXlsx, generationMode);
        } else {
            excelReader = new ExcelReader(filePath);
        }

        String[] sheets = excelReader.getSheetNames();

        allsheets.clear();

        for (String s : sheets) {
            allsheets.add(s);
        }

        initSheetViewer();
        initSheetsCombo(sheets);
        viewSheet(sheetsCombo.getText());

        if (!isContextMode()) {
            getConnection().setFilePath(filePath);
        }
        makeViewerGroupAvailable(true);
    }

    private void viewSheet(final String sheetName) {

        ProgressMonitorDialog dialog = new ProgressMonitorDialog(viewer.getTable().getShell());
        errorSet.remove(ERRORSET_EXCELPREVIEW);

        try {
            dialog.run(true, false, new IRunnableWithProgress() {

                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

                    monitor.beginTask("Excel Preview", IProgressMonitor.UNKNOWN); //$NON-NLS-1$

                    getConnection().setSheetName(sheetName);

                    Display.getDefault().syncExec(new Runnable() {

                        @Override
                        @SuppressWarnings("unchecked")
                        public void run() {

                            disposeExistColumns();

                            final List<String[]> input = excelReader.readSheet(sheetName);
                            if (input == null) {
                                viewer.setInput(getEmptyInput());
                                return;
                            }

                            int maxLength = 0;
                            for (int i = 0, z = input.size(); i < z; i++) {
                                int x = input.get(i).length;
                                if (x > maxLength) {
                                    maxLength = x;
                                }
                            }

                            String[] names = ExcelReader.getColumnsTitle(maxLength);
                            List columns = getConnection().getSheetColumns();
                            columns.clear();

                            for (String name : names) {
                                columns.add(name);
                                TableColumn tableColumn = new TableColumn(viewer.getTable(), SWT.NONE);
                                tableColumn.setText(name);
                                tableColumn.setWidth(WIDTH);
                            }
                            viewer.setInput(input);
                        }
                    });

                    monitor.done();

                }

            });
        } catch (InvocationTargetException e) {
            errorSet.add(ERRORSET_EXCELPREVIEW);
            updateErrorStatus(e.getMessage());
        } catch (InterruptedException e) {
            errorSet.add(ERRORSET_EXCELPREVIEW);
            updateErrorStatus(e.getMessage());
        }

    }

    private List<String[]> getEmptyInput() {
        List<String[]> res = new ArrayList<String[]>();
        res.add(new String[0]);
        return res;
    }

    /**
     * DOC yexiaowei Comment method "disposeExistColumns".
     */
    private void disposeExistColumns() {
        Table table = viewer.getTable();
        TableColumn[] cols = table.getColumns();
        if (cols != null && cols.length > 0) {
            for (TableColumn col : cols) {
                col.dispose();
            }
        }
    }

    private void updateErrorStatus(String msg) {
        updateStatus(IStatus.ERROR, msg);
    }

    /**
     * DOC yexiaowei Comment method "initSheetsCombo".
     * 
     * @param sheets
     */
    private void initSheetsCombo(String[] sheets) {

        String sheetOrigin = getConnection().getSheetName();

        sheetsCombo.removeAll();

        for (String sheet : sheets) {
            sheetsCombo.add(sheet);
        }

        if (sheetOrigin == null || sheetOrigin.equals("") || !containsSheet(sheets, sheetOrigin)) { //$NON-NLS-1$
            sheetsCombo.select(0);
        } else {
            sheetsCombo.setText(sheetOrigin);
        }
    }

    private boolean containsSheet(String[] sheets, String sheet) {
        for (String s : sheets) {
            if (s.equals(sheet)) {
                return true;
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addUtilsButtonListeners()
     */
    @Override
    protected void addUtilsButtonListeners() {
        if (!isInWizard()) {
            cancelButton.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    getShell().close();
                }
            });
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#checkFieldsValue()
     */
    @Override
    protected boolean checkFieldsValue() {
        if (!isContextMode()) {
            fileField.setEditable(true);
        }

        if (!errorSet.isEmpty()) {
            return false;
        }

        if (fileField.getText() == "") { //$NON-NLS-1$
            updateStatus(IStatus.ERROR, Messages.getString("FileStep1.filepathAlert")); //$NON-NLS-1$
            return false;
        }

        String modeText = modeCombo.getText();
        if (!(modeText.equals("Memory-consuming(User mode)") || modeText
                .equals("Less memory consumed for large excel(Event mode)"))) {
            updateStatus(IStatus.ERROR, Messages.getString("FileStep1.modeComboAlert")); //$NON-NLS-1$
            return false;
        }

        if (!isContextMode()) {
            Object elements[] = sheetViewer.getCheckedElements();
            if (elements == null || elements.length <= 0) {
                updateStatus(IStatus.ERROR, "At lease one sheet should be selected"); //$NON-NLS-1$
                return false;
            }
        }

        updateStatus(IStatus.OK, null);
        return true;
    }

    /**
     * DOC YeXiaowei Comment method "initAllParameters".
     */
    private void initAllParameters() {
        if (getConnection().getServer() == null) {
            serverCombo.select(0);
            getConnection().setServer(serverCombo.getText());
        } else {
            serverCombo.setText(getConnection().getServer());
        }
        serverCombo.clearSelection();

        // Just mask it.
        serverCombo.setReadOnly(true);

        if (getConnection().getFilePath() != null) {
            if (isContextMode()) {
                fileField.setText(getConnection().getFilePath());
            } else {
                fileField.setText(getConnection().getFilePath().replace("\\\\", "\\")); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }

        String sheetName = getConnection().getSheetName();
        if (sheetName != null && !sheetName.equals("")) { //$NON-NLS-1$
            sheetName = sheetName.replace("\\\\", "\\"); //$NON-NLS-1$ //$NON-NLS-2$
            for (int i = 0; i < sheetsCombo.getCombo().getItems().length; i++) {
                if (sheetName.equals(sheetsCombo.getCombo().getItems()[i])) {
                    sheetsCombo.select(i);
                    break;
                }
            }
        }
    }

    /**
     * DOC YeXiaowei Comment method "initTreeSelectStates".
     */
    private void initTreeSelectStates() {
        if (rootNode == null || getConnection().getSheetList() == null) {
            return;
        }
        List<SheetNode> ss = rootNode.getChildren();
        if (ss == null || ss.size() <= 0) {
            return;
        }
        for (SheetNode node : ss) {
            if (getConnection().getSheetList().contains(node.getLabel())) {
                sheetViewer.setChecked(node, true);
            }
        }
        sheetViewer.expandAll();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (isReadOnly() != readOnly) {
            adaptFormToReadOnly();
        }
        if (visible) {
            initAllParameters();
            initTreeSelectStates();
            checkFieldsValue();
        }
    }

    @Override
    protected void adaptFormToEditable() {
        super.adaptFormToEditable();
        fileField.setEditable(!isContextMode());
    }

    private final SheetNode rootNode = new SheetNode(null, "All sheets/DSelect sheet"); //$NON-NLS-1$

    private Composite sheetsViewerComposite;

    private Group viewerGroup;

    /**
     * 
     * DOC YeXiaowei ExcelFileStep1Form class global comment. Detailled comment <br/>
     * 
     */
    public static class SheetNode {

        private final String label;

        private final SheetNode parent;

        private List<SheetNode> children = null;

        public SheetNode(SheetNode parent, String label) {
            this.label = label;
            this.parent = parent;
        }

        /**
         * Getter for label.
         * 
         * @return the label
         */
        public String getLabel() {
            return this.label;
        }

        /**
         * Getter for parent.
         * 
         * @return the parent
         */
        public SheetNode getParent() {
            return this.parent;
        }

        /**
         * Getter for children.
         * 
         * @return the children
         */
        public List<SheetNode> getChildren() {
            return this.children;
        }

        /**
         * Sets the children.
         * 
         * @param children the children to set
         */
        public void setChildren(List<SheetNode> children) {
            this.children = children;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#initialize()
     */
    @Override
    protected void initialize() {
    }
}
