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
package org.talend.core.hadoop.version.custom;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.hadoop.HadoopConstants;
import org.talend.core.hadoop.IHadoopService;
import org.talend.core.model.components.ComponentCategory;
import org.talend.core.model.process.INode;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.i18n.Messages;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.repository.ui.dialog.LibrariesListSelectionDialog;

/**
 * created by ycbai on 2013-3-12 Detailled comment
 * 
 * <p>
 * A dialog which can be used to define a hadoop version. You can use {@link #getLibMap()} to gain all libraries which
 * you have defined in it or use {@link #getLibList(ECustomVersionGroup)} or {@link #getLibListStr(ECustomVersionGroup)}
 * to gain the specific type(which defined in {@link ECustomVersionGroup} of libraries.
 * </p>
 * 
 */
public class HadoopCustomVersionDefineDialog extends TitleAreaDialog {

    private Map<String, Set<String>> currentLibMap;

    private Map<String, Set<LibraryFile>> libMap = new HashMap<String, Set<LibraryFile>>();

    private Set<LibraryFile> selectLibFileSet = new HashSet<LibraryFile>();

    private CustomVersionLibsManager libsManager;

    private IHadoopService hadoopService;

    private CTabFolder tabFolder;

    private TableViewer viewer;

    private Table table;

    private Button addLibBtn;

    private Button delBtn;

    private Button importLibBtn;

    private Button exportLibBtn;

    private TableViewerComparator viewerComparator;

    private HadoopCustomLibrariesUtil customLibUtil;

    private boolean readonly = false;

    private boolean needPopUpImport = true;

    private Map<ECustomVersionType, Map<String, Object>> typeConfigurations = new HashMap<ECustomVersionType, Map<String, Object>>();

    private String sparkMode;

    private String sparkStreamingMode;

    /**
     * DOC ycbai HadoopCustomVersionDefineDialog constructor comment.
     * 
     * <p>
     * Create a instance of this dialog without contents.
     * </p>
     * 
     * @param parentShell
     */
    public HadoopCustomVersionDefineDialog(Shell parentShell) {
        this(parentShell, Collections.EMPTY_MAP);
    }

    /**
     * 
     * DOC ycbai HadoopCustomVersionDefineDialog constructor comment.
     * 
     * <p>
     * Create a instance of this dialog and initialise it use {@code currentLibMap} which form is:
     * <ul>
     * <li>Key: one enumeration name of {@link ECustomVersionGroup}
     * <li>Value: set of libraries correspond to the key.</li>
     * </ul>
     * </p>
     * 
     * @param parentShell
     * @param currentLibMap
     */
    public HadoopCustomVersionDefineDialog(Shell parentShell, Map<String, Set<String>> currentLibMap) {
        super(parentShell);
        this.currentLibMap = currentLibMap;
        customLibUtil = new HadoopCustomLibrariesUtil();
        initLibMap();
    }

    @Override
    public void create() {
        super.create();
        setTitle(Messages.getString("HadoopCustomVersionDialog.title")); //$NON-NLS-1$
        setMessage(Messages.getString("HadoopCustomVersionDialog.msg")); //$NON-NLS-1$
        setTitleImage(ImageProvider.getImage(EImage.HADOOP_WIZ_ICON));
        if (getShell() != null) {
            getShell().getDisplay().asyncExec(new Runnable() {

                @Override
                public void run() {
                    if (needPopUpImport && currentLibMap.isEmpty()) {
                        doImportLibs();
                        needPopUpImport = false;
                    }
                }
            });
        }

    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.getString("HadoopCustomVersionDialog.topTitle")); //$NON-NLS-1$
        newShell.setSize(650, 600);
        setHelpAvailable(false);
    }

    @Override
    protected void initializeBounds() {
        super.initializeBounds();
        Point size = getShell().getSize();
        Point location = getInitialLocation(size);
        getShell().setBounds(getConstrainedShellBounds(new Rectangle(location.x, location.y, size.x, size.y)));
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        Composite comp = new Composite(composite, SWT.NONE);
        comp.setLayoutData(new GridData(GridData.FILL_BOTH));
        GridLayout layout = new GridLayout();
        layout.marginHeight = 10;
        layout.marginWidth = 10;
        comp.setLayout(layout);

        tabFolder = new CTabFolder(comp, SWT.BORDER | SWT.FLAT);
        tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
        tabFolder.setLayout(new GridLayout());

        Composite tableComposite = createTable(tabFolder);
        createTabItems(tableComposite);
        createBtns(comp);

        addListener();
        init();

        return parent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        Button createButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton.setEnabled(!readonly);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    private Composite createTable(Composite parent) {
        Composite tableComposite = new Composite(parent, SWT.NONE);
        tableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
        TableColumnLayout tableColumnLayout = new TableColumnLayout();
        tableComposite.setLayout(tableColumnLayout);

        viewer = new TableViewer(tableComposite, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);
        viewer.setContentProvider(ArrayContentProvider.getInstance());
        viewerComparator = new TableViewerComparator();
        viewer.setComparator(viewerComparator);
        table = viewer.getTable();
        table.setLayoutData(new GridData(GridData.FILL_BOTH));
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        TableViewerColumn descColumn = createTableViewerColumn(
                Messages.getString("HadoopCustomVersionDialog.table.descColumn"), 0); //$NON-NLS-1$
        descColumn.setLabelProvider(new ColumnLabelProvider() {

            @Override
            public String getText(Object element) {
                LibraryFile libraryFile = (LibraryFile) element;
                return libraryFile.getDesc();
            }
        });
        tableColumnLayout.setColumnData(descColumn.getColumn(), new ColumnWeightData(45, 80, true));
        TableViewerColumn libNameColumn = createTableViewerColumn(
                Messages.getString("HadoopCustomVersionDialog.table.libNameColumn"), 1); //$NON-NLS-1$
        libNameColumn.setEditingSupport(new LibNameEditingSupport(viewer));
        libNameColumn.setLabelProvider(new ColumnLabelProvider() {

            @Override
            public String getText(Object element) {
                LibraryFile libraryFile = (LibraryFile) element;
                return libraryFile.getName();
            }
        });
        tableColumnLayout.setColumnData(libNameColumn.getColumn(), new ColumnWeightData(55, 100, true));

        return tableComposite;
    }

    private TableViewerColumn createTableViewerColumn(String title, final int colNumber) {
        final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
        final TableColumn column = viewerColumn.getColumn();
        column.setText(title);
        column.setResizable(true);
        column.setMoveable(true);
        column.addSelectionListener(getSelectionAdapter(column, colNumber));
        return viewerColumn;
    }

    private SelectionAdapter getSelectionAdapter(final TableColumn column, final int index) {
        SelectionAdapter selectionAdapter = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                viewerComparator.setColumn(index);
                int dir = viewerComparator.getDirection();
                viewer.getTable().setSortDirection(dir);
                viewer.getTable().setSortColumn(column);
                viewer.refresh();
            }
        };
        return selectionAdapter;
    }

    private void createTabItems(Control control) {
        for (ECustomVersionType type : getTypes()) {
            createTabItem(type, control);
        }
    }

    public ECustomVersionType[] getTypes() {
        ECustomVersionType[] types = getDisplayTypes();
        if (types == null) {
            return null;
        }

        if (ArrayUtils.contains(types, ECustomVersionType.ALL)) {
            types = ECustomVersionType.values();
            types = (ECustomVersionType[]) ArrayUtils.remove(types, ArrayUtils.indexOf(types, ECustomVersionType.ALL));
        }
        return types;
    }

    private CTabItem createTabItem(ECustomVersionType type, Control ctl) {
        CTabItem ti = null;
        String displayName = type.getDisplayName();
        CTabItem[] items = tabFolder.getItems();
        for (CTabItem item : items) {
            Object data = item.getData();
            if (data instanceof ECustomVersionType) {
                ECustomVersionType dataType = (ECustomVersionType) data;
                if (!dataType.equals(type) && dataType.isSameGroup(type)) {
                    displayName = item.getText() + "/" + displayName; //$NON-NLS-1$
                    ti = item;
                }
            }
        }
        if (ti == null) {
            ti = createTabItem(SWT.NONE, type, null, ctl);
        }
        ti.setText(displayName);

        return ti;
    }

    private CTabItem createTabItem(int style, ECustomVersionType type, Image icon, Control ctl) {
        CTabItem ti = new CTabItem(tabFolder, style);
        if (type != null) {
            ti.setData(type);
            ti.setText(type.getDisplayName());
        }
        if (icon != null) {
            ti.setImage(icon);
        }
        if (ctl != null) {
            ti.setControl(ctl);
        }
        return ti;
    }

    private void createBtns(Composite parent) {
        Composite btnComposite = new Composite(parent, SWT.NONE);
        btnComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        btnComposite.setLayout(new GridLayout(5, true));

        addLibBtn = new Button(btnComposite, SWT.NONE);
        addLibBtn.setImage(ImageProvider.getImage(EImage.ADD_ICON));
        addLibBtn.setToolTipText(Messages.getString("HadoopCustomVersionDialog.btn.addLibBtn.tooltip")); //$NON-NLS-1$
        delBtn = new Button(btnComposite, SWT.NONE);
        delBtn.setImage(ImageProvider.getImage(EImage.DELETE_ICON));
        if (isSupportHadoop()) {
            importLibBtn = new Button(btnComposite, SWT.NONE);
            importLibBtn.setImage(ImageProvider.getImage(EImage.IMPORT_ICON));
            importLibBtn.setToolTipText(Messages.getString("HadoopCustomVersionDialog.btn.importLibBtn.tooltip")); //$NON-NLS-1$

            exportLibBtn = new Button(btnComposite, SWT.NONE);
            exportLibBtn.setImage(ImageProvider.getImage(EImage.EXPORT_ICON));
            exportLibBtn.setToolTipText(Messages.getString("HadoopCustomVersionDialog.btn.exportLibBtn.tooltip")); //$NON-NLS-1$
        }
    }

    private void addListener() {
        tabFolder.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                doChangeViewerContent();
            }
        });
        addLibBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                doAddLibs();
            }
        });
        delBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                doDelLibs();
            }
        });
        if (importLibBtn != null) {
            importLibBtn.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    doImportLibs();
                }
            });
        }
        if (exportLibBtn != null) {
            exportLibBtn.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    doExportLibs();
                }
            });
        }
    }

    private void doExportLibs() {
        FileDialog dialog = new FileDialog(getParentShell(), SWT.SAVE);
        dialog.setFilterExtensions(HadoopCustomLibrariesUtil.FILE__MASK);
        dialog.setText("Export to Archive File"); //$NON-NLS-1$

        final String selectedArchive = dialog.open();
        if (selectedArchive != null) {
            IRunnableWithProgress iRunnableWithProgress = new IRunnableWithProgress() {

                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {

                        @Override
                        public void run() {
                            customLibUtil.exportLibZipFile(selectedArchive, libMap);
                        }
                    });
                }
            };
            try {
                new ProgressMonitorDialog(getShell()).run(true, true, iRunnableWithProgress);
            } catch (InvocationTargetException e) {
                ExceptionHandler.process(e);
            } catch (InterruptedException e) {
                ExceptionHandler.process(e);
            }
        }
    }

    private void init() {
        libsManager = CustomVersionLibsManager.getInstance();

        // initLibMap();// move it to constructor
        ECustomVersionGroup selectedType = getSelectedType();
        if (selectedType != null) {
            selectLibFileSet = libMap.get(selectedType.getName());
            viewer.setInput(selectLibFileSet);
        }
    }

    /**
     * Initialize the libMap.<br>
     * <b>NOTE:</b><br>
     * 1. Can call this method when currentLibMap is changed;<br>
     * 2. This method will be called automatically in the constructor.
     */
    public void initLibMap() {
        if (currentLibMap != null) {
            ECustomVersionGroup[] groups = ECustomVersionGroup.values();
            for (ECustomVersionGroup group : groups) {
                Set<String> set = currentLibMap.get(group.getName());
                if (set == null) {
                    set = new HashSet<String>();
                }
                libMap.put(group.getName(), customLibUtil.convertToLibraryFile(set));
            }
        }
    }

    private boolean isSupportHadoop() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IHadoopService.class)) {
            hadoopService = (IHadoopService) GlobalServiceRegister.getDefault().getService(IHadoopService.class);
        }

        return hadoopService != null;
    }

    /**
     * DOC ycbai Comment method "getDisplayTypes".
     * 
     * @return Types({@link ECustomVersionType}) array which you want to display in this dialog.
     */
    protected ECustomVersionType[] getDisplayTypes() {
        ECustomVersionType[] values = ECustomVersionType.values();
        values = filterTypes(values);
        return values;
    }

    private ECustomVersionType[] filterTypes(Object[] types) {
        Object[] filteredTypes = ArrayUtils.removeElement(types, ECustomVersionType.ALL);
        IDesignerCoreService designerCoreService = CoreRuntimePlugin.getInstance().getDesignerCoreService();
        INode node = designerCoreService.getRefrenceNode("tPigLoad"); //$NON-NLS-1$
        if (node == null) {
            filteredTypes = ArrayUtils.removeElement(filteredTypes, ECustomVersionType.PIG);
        }
        node = designerCoreService.getRefrenceNode("tMRConfiguration", ComponentCategory.CATEGORY_4_MAPREDUCE.getName());//$NON-NLS-1$
        if (node == null) {
            filteredTypes = ArrayUtils.removeElement(filteredTypes, ECustomVersionType.MAP_REDUCE);
        }
        node = designerCoreService.getRefrenceNode("tSparkConfiguration", ComponentCategory.CATEGORY_4_SPARK.getName());//$NON-NLS-1$
        if (node == null) {
            filteredTypes = ArrayUtils.removeElement(filteredTypes, ECustomVersionType.SPARK);
        }
        node = designerCoreService.getRefrenceNode("tSparkConfiguration", ComponentCategory.CATEGORY_4_SPARKSTREAMING.getName());//$NON-NLS-1$
        if (node == null) {
            filteredTypes = ArrayUtils.removeElement(filteredTypes, ECustomVersionType.SPARK_STREAMING);
        }
        return (ECustomVersionType[]) filteredTypes;
    }

    private void doChangeViewerContent() {
        selectLibFileSet = getSelectedTypeLibList();
        viewer.setInput(selectLibFileSet);
    }

    private void doAddLibs() {
        LibrariesListSelectionDialog selectDialog = new LibrariesListSelectionDialog(getShell());
        selectDialog.setMultipleSelection(true);
        if (selectDialog.open() == Window.OK) {
            Object[] result = selectDialog.getResult();
            selectLibFileSet.addAll(customLibUtil.convertToLibraryFile(result));
            viewer.refresh();
        }
    }

    private void doDelLibs() {
        ISelection selection = viewer.getSelection();
        if (selection != null && selection instanceof IStructuredSelection) {
            IStructuredSelection sel = (IStructuredSelection) selection;
            for (Iterator<LibraryFile> iterator = sel.iterator(); iterator.hasNext();) {
                LibraryFile lib = iterator.next();
                selectLibFileSet.remove(lib);
            }
            viewer.refresh();
        }
    }

    private void doImportLibs() {
        // keep the same sequnce
        Comparator comparator = new Comparator<ECustomVersionGroup>() {

            @Override
            public int compare(ECustomVersionGroup o1, ECustomVersionGroup o2) {
                List itemGroups = new ArrayList();
                CTabItem[] items = tabFolder.getItems();
                for (CTabItem item : items) {
                    ECustomVersionGroup customVersionGroup = getCustomVersionGroup(item);
                    itemGroups.add(customVersionGroup);
                }
                return itemGroups.indexOf(o1) - itemGroups.indexOf(o2);
            }

        };
        Map<ECustomVersionGroup, String> groupsAndDispaly = new TreeMap<ECustomVersionGroup, String>(comparator);

        CTabItem[] items = tabFolder.getItems();
        for (CTabItem item : items) {
            ECustomVersionGroup customVersionGroup = getCustomVersionGroup(item);
            if (customVersionGroup != null) {
                groupsAndDispaly.put(customVersionGroup, item.getText());
            }
        }
        final HadoopVersionDialog versionDialog = new HadoopVersionDialog(getShell(), groupsAndDispaly, customLibUtil, getTypes());
        versionDialog.setTypeConfigurations(getTypeConfigurations());
        if (versionDialog.open() == Window.OK) {
            final IRunnableWithProgress runnableWithProgress = new IRunnableWithProgress() {

                @Override
                public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    monitor.beginTask(Messages.getString("HadoopCustomVersionDialog.importLibs"), IProgressMonitor.UNKNOWN); //$NON-NLS-1$
                    try {
                        PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {

                            @Override
                            public void run() {
                                Map<ECustomVersionGroup, Set<LibraryFile>> importLibLibraries = versionDialog
                                        .getImportLibLibraries();
                                if (importLibLibraries == null) {
                                    return;
                                }
                                for (ECustomVersionGroup group : importLibLibraries.keySet()) {
                                    Set<LibraryFile> set = libMap.get(group.getName());
                                    if (set != null) {
                                        set.clear();
                                        set.addAll(importLibLibraries.get(group));
                                    }
                                }
                            }
                        });
                        PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

                            @Override
                            public void run() {
                                viewer.refresh();
                            }
                        });
                    } finally {
                        monitor.done();
                    }
                }
            };

            ProgressMonitorDialog dialog = new ProgressMonitorDialog(getShell());
            try {
                dialog.run(true, true, runnableWithProgress);
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
    }

    private ECustomVersionGroup getSelectedType() {
        CTabItem selectItem = tabFolder.getSelection();
        return getCustomVersionGroup(selectItem);
    }

    private ECustomVersionGroup getCustomVersionGroup(CTabItem selectItem) {
        if (selectItem != null) {
            Object data = selectItem.getData();
            if (data != null && data instanceof ECustomVersionType) {
                return ((ECustomVersionType) data).getGroup();
            }
        }

        return null;
    }

    private Set<LibraryFile> getSelectedTypeLibList() {
        ECustomVersionGroup selectedType = getSelectedType();
        if (selectedType != null) {
            return libMap.get(selectedType.getName());
        }

        return Collections.EMPTY_SET;
    }

    private boolean isExistingName(String libName) {
        for (LibraryFile lf : selectLibFileSet) {
            if (lf.getName().equals(libName)) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected boolean isResizable() {
        return true;
    }

    /**
     * DOC ycbai Comment method "getLibMap".
     * 
     * <p>
     * Get libraries map which defined in the dialog. The form of the returned map is:
     * <ul>
     * <li>Key: one enumeration name of {@link ECustomVersionGroup}
     * <li>Value: set of libraries correspond to the key.</li>
     * </ul>
     * </p>
     * 
     * @return Libraries map which used by the hadoop version.
     */
    public Map<String, Set<String>> getLibMap() {
        Map<String, Set<String>> map = new HashMap<String, Set<String>>();
        Iterator<Entry<String, Set<LibraryFile>>> iter = libMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Set<LibraryFile>> entry = iter.next();
            String groupName = entry.getKey();
            Set<String> libraries = customLibUtil.convertToLibName(entry.getValue());
            map.put(groupName, libraries);
        }

        return map;
    }

    /**
     * 
     * DOC ycbai Comment method "getLibList".
     * 
     * <p>
     * Get libraries which <code>definitionType</code> need.
     * </p>
     * 
     * @param definitionType
     * @return Libraries set which needed by the <code>definitionType</code>.
     */
    public Set<String> getLibList(ECustomVersionGroup definitionType) {
        return getLibMap().get(definitionType.getName());
    }

    /**
     * DOC ycbai Comment method "getLibListStr".
     * 
     * <p>
     * Get libraries which <code>definitionType</code> need.
     * </p>
     * 
     * @param definitionType
     * @return Libraries string(separated by ';') which needed by the <code>definitionType</code>.
     */
    public String getLibListStr(ECustomVersionGroup definitionType) {
        StringBuffer libStr = new StringBuffer();
        Set<String> libList = getLibList(definitionType);
        if (libList != null) {
            for (String lib : libList) {
                if (StringUtils.isNotEmpty(lib)) {
                    libStr.append(lib).append(";"); //$NON-NLS-1$
                }
            }
            if (libStr.length() > 0) {
                libStr.deleteCharAt(libStr.length() - 1);
            }
        }

        return libStr.toString();
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    /**
     * 
     * created by ycbai on 2013-3-19 Detailled comment
     * 
     */
    class TableViewerComparator extends ViewerComparator {

        private int propertyIndex;

        private static final int DESCENDING = 1;

        private int direction = DESCENDING;

        public TableViewerComparator() {
            this.propertyIndex = 0;
            direction = DESCENDING;
        }

        public int getDirection() {
            return direction == 1 ? SWT.DOWN : SWT.UP;
        }

        public void setColumn(int column) {
            if (column == this.propertyIndex) {
                // Same column as last sort; toggle the direction
                direction = 1 - direction;
            } else {
                // New column; do an ascending sort
                this.propertyIndex = column;
                direction = DESCENDING;
            }
        }

        @Override
        public int compare(Viewer v, Object e1, Object e2) {
            LibraryFile jf1 = (LibraryFile) e1;
            LibraryFile jf2 = (LibraryFile) e2;
            int rc = 0;
            switch (propertyIndex) {
            case 0:
                rc = jf1.getDesc().compareTo(jf2.getDesc());
                break;
            case 1:
                rc = jf1.getName().compareTo(jf2.getName());
                break;
            default:
                rc = 0;
            }
            // If descending order, flip the direction
            if (direction == DESCENDING) {
                rc = -rc;
            }
            return rc;
        }

    }

    /**
     * created by ycbai on 2013-3-19 Detailled comment
     * 
     */
    class LibNameDialogCellEditor extends DialogCellEditor {

        /**
         * DOC ycbai LibNameDialogCellEditor constructor comment.
         * 
         * @param composite
         */
        public LibNameDialogCellEditor(Composite composite) {
            super(composite);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.swt.widgets.Control)
         */
        @Override
        protected Object openDialogBox(Control cellEditorWindow) {
            LibrariesListSelectionDialog selectDialog = new LibrariesListSelectionDialog(getShell());
            if (selectDialog.open() == Window.OK) {
                Object[] result = selectDialog.getResult();
                if (result.length > 0) {
                    return result[0].toString();
                }
            }

            return null;
        }

    }

    @Override
    public int open() {
        ECustomVersionType[] displayTypes = getDisplayTypes();
        Map<ECustomVersionType, Map<String, Object>> typeConfigurations = getTypeConfigurations();
        Map<String, Object> sparkConfigurations = typeConfigurations.get(ECustomVersionType.SPARK);
        if (sparkConfigurations == null) {
            sparkConfigurations = new HashMap<String, Object>();
            typeConfigurations.put(ECustomVersionType.SPARK, sparkConfigurations);
        }

        String strSparkMode = getSparkMode();
        if (strSparkMode == null || strSparkMode.isEmpty()) {
            // the mode is set following the configurations of tSparkConfiguration_java.xml
            if (displayTypes != null && displayTypes.length == 1 && ECustomVersionType.SPARK == displayTypes[0]) {
                strSparkMode = HadoopConstants.SPARK_MODE_CLUSTER;
            } else {
                strSparkMode = HadoopConstants.SPARK_MODE_YARN_CLIENT;
            }
        }
        sparkConfigurations.put(HadoopConstants.SPARK_MODE, strSparkMode);

        Map<String, Object> sparkStreamingConfigurations = typeConfigurations.get(ECustomVersionType.SPARK_STREAMING);
        if (sparkStreamingConfigurations == null) {
            sparkStreamingConfigurations = new HashMap<String, Object>();
            typeConfigurations.put(ECustomVersionType.SPARK_STREAMING, sparkStreamingConfigurations);
        }
        String strSparkStreamingMode = getSparkStreamingMode();
        if (strSparkStreamingMode == null || strSparkStreamingMode.isEmpty()) {
            // the mode is set following the configurations of tSparkConfiguration_java.xml
            if (displayTypes != null && displayTypes.length == 1 && ECustomVersionType.SPARK_STREAMING == displayTypes[0]) {
                strSparkStreamingMode = HadoopConstants.SPARK_MODE_CLUSTER;
            } else {
                strSparkStreamingMode = HadoopConstants.SPARK_MODE_YARN_CLIENT;
            }
        }
        sparkStreamingConfigurations.put(HadoopConstants.SPARK_MODE, strSparkStreamingMode);
        return super.open();
    }

    /**
     * created by ycbai on 2013-3-19 Detailled comment
     * 
     */
    class LibNameEditingSupport extends EditingSupport {

        private TableViewer tv;

        public LibNameEditingSupport(TableViewer viewer) {
            super(viewer);
            tv = viewer;
        }

        @Override
        protected boolean canEdit(Object element) {
            return true;
        }

        @Override
        protected CellEditor getCellEditor(Object element) {
            return new LibNameDialogCellEditor(tv.getTable());
        }

        @Override
        protected Object getValue(Object element) {
            LibraryFile libraryFile = (LibraryFile) element;
            return libraryFile.getName();
        }

        @Override
        protected void setValue(Object element, Object value) {
            String newLibName = value.toString();
            if (isExistingName(newLibName)) {
                return;
            }
            LibraryFile libraryFile = (LibraryFile) element;
            if (newLibName != null && !newLibName.equals(libraryFile.getName())) {
                libraryFile.setName(newLibName);
                libraryFile.setDesc(libsManager.getLibDescription(newLibName));
                getViewer().update(element, null);
            }
        }

    }

    public Map<ECustomVersionType, Map<String, Object>> getTypeConfigurations() {
        return this.typeConfigurations;
    }

    public void setTypeConfigurations(Map<ECustomVersionType, Map<String, Object>> typeConfigurations) {
        this.typeConfigurations = typeConfigurations;
    }

    public String getSparkMode() {
        return this.sparkMode;
    }

    public void setSparkMode(String sparkMode) {
        this.sparkMode = sparkMode;
    }

    public String getSparkStreamingMode() {
        return this.sparkStreamingMode;
    }

    public void setSparkStreamingMode(String sparkStreamingMode) {
        this.sparkStreamingMode = sparkStreamingMode;
    }
}
