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
package org.talend.librariesmanager.ui.dialogs;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorColumnNotModifiable;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorNotModifiable.LAYOUT_MODE;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorNotModifiable.SORT;
import org.talend.commons.ui.runtime.swt.tableviewer.behavior.IColumnImageProvider;
import org.talend.commons.ui.runtime.swt.tableviewer.sort.IColumnSortedListener;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.commons.utils.network.NetworkUtil;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;
import org.talend.core.model.general.ModuleStatusProvider;
import org.talend.core.model.general.ModuleToInstall;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.librariesmanager.ui.LibManagerUiPlugin;
import org.talend.librariesmanager.ui.actions.ImportExternalJarAction;
import org.talend.librariesmanager.ui.i18n.Messages;
import org.talend.librariesmanager.utils.DownloadModuleRunnableWithLicenseDialog;

/**
 * DOC wchen class global comment. Detailled comment
 */
public class ExternalModulesInstallDialog extends TitleAreaDialog implements IModulesListener {

    private static Logger log = Logger.getLogger(ExternalModulesInstallDialog.class);

    public static final String DO_NOT_SHOW_EXTERNALMODULESINSTALLDIALOG = "do_not_show_ExternalModulesInstallDialog"; //$NON-NLS-1$

    private Color color = new Color(null, 255, 255, 255);

    protected TableViewerCreator<ModuleToInstall> tableViewerCreator;

    protected String text;

    protected String title;

    protected Button installAllBtn;

    protected List<String> jarsInstalledSuccuss = new ArrayList<String>();

    protected List<ModuleToInstall> inputList = new ArrayList<ModuleToInstall>();

    private boolean showMessage = true;

    protected TableViewerCreatorColumn urlcolumn;

    protected TableViewerCreatorColumn installcolumn;

    private ArrayList<TableEditor> installButtonsEditors = new ArrayList<TableEditor>();

    private HashMap<ModuleToInstall, Button> manualInstallButtonMap;

    public ExternalModulesInstallDialog(Shell shell, String text, String title) {
        super(shell);
        setShellStyle(SWT.DIALOG_TRIM | SWT.MIN | SWT.MAX | SWT.RESIZE | getDefaultOrientation());
        this.text = text;
        this.title = title;
    }

    @Override
    protected void initializeBounds() {
        super.initializeBounds();
        getShell().setSize(1050, 400);
        Point location = getInitialLocation(getShell().getSize());
        getShell().setLocation(location.x, location.y);
    }

    @Override
    protected void configureShell(final Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(text);
    }

    private void checkNetworkStatus() {
        boolean networkValid = NetworkUtil.isNetworkValid();
        if (!networkValid) {
            setErrorMessage(Messages.getString("ExternalModulesInstallDialog.networkUnavailable.msg")); //$NON-NLS-1$
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        checkNetworkStatus();
        GridData layoutData = new GridData(GridData.FILL_BOTH);
        Composite composite = new Composite(parent, SWT.BORDER);
        GridLayout layout = new GridLayout();
        composite.setLayout(layout);
        composite.setLayoutData(layoutData);
        tableViewerCreator = new TableViewerCreator<ModuleToInstall>(composite);
        tableViewerCreator.setCheckboxInFirstColumn(false);
        tableViewerCreator.setColumnsResizableByDefault(true);
        tableViewerCreator.setLinesVisible(true);
        tableViewerCreator.setLayoutMode(LAYOUT_MODE.CONTINUOUS);

        tableViewerCreator.createTable();

        createJarNameColumn();
        createContextColumn();
        createRequiredColumn();
        createLicenseColumn();
        createMavenURIColumn();

        urlcolumn = createMoreInformationColumn();

        installcolumn = createActionColumn();

        tableViewerCreator.init(inputList);
        addInstallButtons();
        layoutData = new GridData(GridData.FILL_BOTH);
        tableViewerCreator.getTable().setLayoutData(layoutData);
        tableViewerCreator.getTable().pack();

        // TUP-17469 remove since can't provide the URL until the documentation is published.then waitting for talend
        // provide.
        // Composite footComposite = new Composite(composite, SWT.NONE);
        // layoutData = new GridData(GridData.FILL_HORIZONTAL);
        // footComposite.setLayoutData(layoutData);
        // layout = new GridLayout();
        // layout.numColumns = 2;
        // footComposite.setLayout(layout);
        //
        // final Link moreInfor = new Link(footComposite, SWT.NONE);
        // layoutData = new GridData(GridData.FILL_HORIZONTAL);
        // layoutData.widthHint = 200;
        //        moreInfor.setText(Messages.getString("ExternalModulesInstallDialog_MoreInfor")); //$NON-NLS-1$
        // moreInfor.setLayoutData(layoutData);
        // moreInfor.addSelectionListener(new SelectionAdapter() {
        //
        // @Override
        // public void widgetSelected(SelectionEvent e) {
        //                //                Program.launch(Messages.getString("download.external.dialog.help.url")); //$NON-NLS-1$
        //                openURL(Messages.getString("download.external.dialog.help.url")); //$NON-NLS-1$
        // }
        // });
        setupColumnSortListener();
        createFooter(composite);
        setTitle(title);
        return composite;
    }

    /**
     * DOC sgandon Comment method "setupColumnSortListener".
     */
    @SuppressWarnings("rawtypes")
    private void setupColumnSortListener() {
        List<TableViewerCreatorColumnNotModifiable> columns = tableViewerCreator.getColumns();
        IColumnSortedListener columnSortedListener = new IColumnSortedListener() {

            @Override
            public void handle() {
                BusyIndicator.showWhile(Display.getDefault(), new Runnable() {

                    @Override
                    public void run() {
                        addInstallButtons();
                    }
                });
            }
        };
        for (TableViewerCreatorColumnNotModifiable column : columns) {
            column.getTableColumnSelectionListener().addColumnSortedListener(columnSortedListener);
        }
    }

    /**
     * DOC sgandon Comment method "updateInstallModulesButtonState".
     */
    protected void updateInstallModulesButtonState() {
        List<ModuleToInstall> theInputList = tableViewerCreator.getInputList();
        boolean isEnable = false;
        if (!theInputList.isEmpty()) {
            for (ModuleToInstall module : theInputList) {
                if (!MavenConstants.DOWNLOAD_MANUAL.equals(module.getDistribution())) {
                    isEnable = true;
                    break;
                }
            }
        }
        installAllBtn.setEnabled(isEnable);
    }

    /**
     * DOC sgandon Comment method "createJarNameColumn".
     */
    public void createJarNameColumn() {
        TableViewerCreatorColumn<ModuleToInstall, String> column = new TableViewerCreatorColumn<ModuleToInstall, String>(
                tableViewerCreator);
        column.setTitle(Messages.getString("ExternalModulesInstallDialog_ColumnJarName")); //$NON-NLS-1$
        column.setToolTipHeader(Messages.getString("ExternalModulesInstallDialog_ColumnJarName")); //$NON-NLS-1$
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<ModuleToInstall, String>() {

            @Override
            public String get(ModuleToInstall bean) {
                return bean.getName();
            }

            @Override
            public void set(ModuleToInstall bean, String value) {
                // read only
            }
        });
        column.setSortable(true);
        tableViewerCreator.setDefaultSort(column, SORT.ASC);
        column.setWeight(5);
        column.setModifiable(false);
    }

    /**
     * DOC sgandon Comment method "createModuleNameColumn".
     */
    public void createMavenURIColumn() {
        TableViewerCreatorColumn<ModuleToInstall, String> column = new TableViewerCreatorColumn<ModuleToInstall, String>(
                tableViewerCreator);
        column.setTitle(Messages.getString("ExternalModulesInstallDialog.mvnuri_columnModuleName")); //$NON-NLS-1$
        column.setToolTipHeader(Messages.getString("ExternalModulesInstallDialog.mvnuri_columnModuleName")); //$NON-NLS-1$
        column.setSortable(true);
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<ModuleToInstall, String>() {

            @Override
            public String get(ModuleToInstall bean) {
                return bean.toString();
            }

            @Override
            public void set(ModuleToInstall bean, String value) {
                // read only
            }
        });
        column.setWeight(6);
        column.setModifiable(false);
    }

    /**
     * DOC sgandon Comment method "createContextColumn".
     */
    public void createContextColumn() {
        TableViewerCreatorColumn<ModuleToInstall, String> column = new TableViewerCreatorColumn<ModuleToInstall, String>(
                tableViewerCreator);
        column.setSortable(true);
        column.setTitle(Messages.getString("ExternalModulesInstallDialog_ColumnRequiredBy")); //$NON-NLS-1$
        column.setToolTipHeader(Messages.getString("ExternalModulesInstallDialog_ColumnRequiredBy")); //$NON-NLS-1$
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<ModuleToInstall, String>() {

            @Override
            public String get(ModuleToInstall bean) {
                return bean.getContext();
            }

            @Override
            public void set(ModuleToInstall bean, String value) {
                // read only column
            }
        });

        column.setModifiable(false);
        column.setWeight(4);
    }

    /**
     * DOC sgandon Comment method "createREquiredColumn".
     */
    public void createRequiredColumn() {
        TableViewerCreatorColumn<ModuleToInstall, Boolean> column = new TableViewerCreatorColumn<ModuleToInstall, Boolean>(
                tableViewerCreator);
        column.setTitle(Messages.getString("ExternalModulesInstallDialog_ColumnRequired")); //$NON-NLS-1$
        column.setToolTipHeader(Messages.getString("ExternalModulesInstallDialog_ColumnRequired")); //$NON-NLS-1$
        column.setDisplayedValue(""); //$NON-NLS-1$
        column.setSortable(true);
        column.setImageProvider(new IColumnImageProvider<ModuleToInstall>() {

            @Override
            public Image getImage(ModuleToInstall bean) {
                if (bean.isRequired()) {
                    return ImageProvider.getImage(EImage.CHECKED_ICON);
                } else {
                    return ImageProvider.getImage(EImage.UNCHECKED_ICON);
                }
            }
        });
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<ModuleToInstall, Boolean>() {

            @Override
            public Boolean get(ModuleToInstall bean) {
                return bean.isRequired();
            }

            @Override
            public void set(ModuleToInstall bean, Boolean value) {
                // read only
            }
        });

        column.setModifiable(false);
        column.setWeight(2);
    }

    /**
     * DOC sgandon Comment method "createLicenseColumn".
     */
    public void createLicenseColumn() {
        TableViewerCreatorColumn<ModuleToInstall, String> column = new TableViewerCreatorColumn<ModuleToInstall, String>(
                tableViewerCreator);
        column.setTitle(Messages.getString("ExternalModulesInstallDialog_ColumnLicense")); //$NON-NLS-1$
        column.setToolTipHeader(Messages.getString("ExternalModulesInstallDialog_ColumnLicense")); //$NON-NLS-1$
        column.setSortable(true);
        // set bean property accessor to allow sort by license name
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<ModuleToInstall, String>() {

            @Override
            public String get(ModuleToInstall bean) {
                return bean.getLicenseType();
            }

            @Override
            public void set(ModuleToInstall bean, String value) {
                // read only
            }
        });

        column.setModifiable(false);
        column.setWeight(3);
    }

    /**
     * DOC sgandon Comment method "createMoreInformationColumn".
     * 
     * @param composite
     * 
     * @return
     */
    public TableViewerCreatorColumn<ModuleToInstall, String> createMoreInformationColumn() {
        TableViewerCreatorColumn<ModuleToInstall, String> column = new TableViewerCreatorColumn<ModuleToInstall, String>(
                tableViewerCreator);
        column.setTitle(Messages.getString("ExternalModulesInstallDialog_ColumnUrl")); //$NON-NLS-1$
        column.setToolTipHeader(Messages.getString("ExternalModulesInstallDialog_ColumnUrl")); //$NON-NLS-1$
        column.setModifiable(false);
        column.setSortable(true);
        column.setWeight(4);
        // set bean property accessor to allow sort by url name
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<ModuleToInstall, String>() {

            @Override
            public String get(ModuleToInstall bean) {
                return bean.getUrl_description() != null ? bean.getUrl_description() : ""; //$NON-NLS-1$
            }

            @Override
            public void set(ModuleToInstall bean, String value) {
                // read only
            }
        });
        return column;
    }

    /**
     * DOC sgandon Comment method "createActionColumn".
     * 
     * @return
     */
    public TableViewerCreatorColumn<ModuleToInstall, String> createActionColumn() {
        TableViewerCreatorColumn<ModuleToInstall, String> column = new TableViewerCreatorColumn<ModuleToInstall, String>(
                tableViewerCreator);
        column.setTitle(Messages.getString("ExternalModulesInstallDialog_AvailableOnTalendForge")); //$NON-NLS-1$
        column.setToolTipHeader(Messages.getString("ExternalModulesInstallDialog_AvailableOnTalendForge")); //$NON-NLS-1$
        column.setModifiable(false);
        column.setSortable(true);
        column.setWeight(5);
        // set bean property accessor to allow sort by download type (automatic or manual)
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<ModuleToInstall, String>() {

            @Override
            public String get(ModuleToInstall bean) {
                // use 2 invisible different values to allow sorting and have no visual impact when the button is
                // selected
                return bean.getUrl_download() == null ? "" : " "; //$NON-NLS-1$//$NON-NLS-2$
            }

            @Override
            public void set(ModuleToInstall bean, String value) {
                // read only
            }
        });
        return column;
    }

    @Override
    protected Control createHelpControl(Composite parent) {
        Image helpImage = JFaceResources.getImage(DLG_IMG_HELP);
        if (helpImage != null) {
            return createHelpImageButton(parent, helpImage);
        }
        return createHelpLink(parent);
    }

    private Link createHelpLink(Composite parent) {
        Link link = new Link(parent, SWT.WRAP | SWT.NO_FOCUS);
        ((GridLayout) parent.getLayout()).numColumns++;
        link.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
        link.setText("<a>" + IDialogConstants.HELP_LABEL + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$
        link.setToolTipText(IDialogConstants.HELP_LABEL);
        link.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                helpPressed();
            }
        });
        return link;
    }

    private void helpPressed() {
        //        Program.launch(Messages.getString("download.external.dialog.help.url")); //$NON-NLS-1$
        openURL(Messages.getString("download.external.dialog.help.url")); //$NON-NLS-1$
    }

    private ToolBar createHelpImageButton(Composite parent, Image image) {
        ToolBar toolBar = new ToolBar(parent, SWT.FLAT | SWT.NO_FOCUS);
        ((GridLayout) parent.getLayout()).numColumns++;
        toolBar.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
        final Cursor cursor = new Cursor(parent.getDisplay(), SWT.CURSOR_HAND);
        toolBar.setCursor(cursor);
        toolBar.addDisposeListener(new DisposeListener() {

            @Override
            public void widgetDisposed(DisposeEvent e) {
                cursor.dispose();
            }
        });
        ToolItem item = new ToolItem(toolBar, SWT.NONE);
        item.setImage(image);
        item.setToolTipText(JFaceResources.getString("helpToolTip")); //$NON-NLS-1$
        item.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                helpPressed();
            }
        });
        return toolBar;
    }

    // private void emptyLibs() {
    // if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibrariesService.class)) {
    // ILibrariesService libService = (ILibrariesService) GlobalServiceRegister.getDefault().getService(
    // ILibrariesService.class);
    // if (libService != null) {
    // libService.cleanLibs();
    // }
    // }
    // }

    protected void addListeners() {

        installAllBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                List<ModuleToInstall> toInstall = getModulesToBeInstalled();

                final DownloadModuleJob job = new DownloadModuleJob(toInstall);

                job.addJobChangeListener(new JobChangeAdapter() {

                    @Override
                    public void done(final IJobChangeEvent event) {

                        Display.getDefault().asyncExec(new Runnable() {

                            @Override
                            public void run() {
                                if (event.getJob() instanceof DownloadModuleJob) {
                                    DownloadModuleJob theJob = (DownloadModuleJob) event.getJob();
                                    Set<String> downloadFialed = theJob.getDownloadFailed();
                                    Set<String> installedModule = theJob.getInstalledModule();
                                    jarsInstalledSuccuss.addAll(installedModule);
                                    int installedModules = installedModule.size();
                                    String success = installedModules
                                            + Messages.getString("ExternalModulesInstallDialog_DownloadSuccess"); //$NON-NLS-1$
                                    String message = success;
                                    if (!downloadFialed.isEmpty()) {
                                        String fail = Messages.getString("ExternalModulesInstallDialog_DownloadFailed"); //$NON-NLS-1$
                                        String names = ""; //$NON-NLS-1$
                                        for (String name : downloadFialed) {
                                            if (names.length() > 0) {
                                                names += " , " + name; //$NON-NLS-1$
                                            } else {
                                                names += name;
                                            }
                                        }
                                        message = message + fail + names;
                                    }
                                    MessageDialog.openInformation(getShell(),
                                            Messages.getString("ExternalModulesInstallDialog.MessageDialog.Infor"), message); //$NON-NLS-1$
                                    // refreshUI();
                                    if (installedModules > 0) {
                                        // emptyLibs(); //never empty all jars, only clean the existed one
                                        ImportExternalJarAction.cleanupLib(installedModule);
                                    }
                                    // need to force a refresh after install jars of component
                                    if (GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerCoreService.class)) {
                                        IDesignerCoreService service = (IDesignerCoreService) GlobalServiceRegister.getDefault()
                                                .getService(IDesignerCoreService.class);
                                        if (service != null) {
                                            service.refreshComponentView();
                                        }
                                    }
                                    LibManagerUiPlugin.getDefault().getLibrariesService().checkLibraries();
                                }
                            }
                        });
                    }
                });
                job.setUser(true);
                job.setPriority(Job.INTERACTIVE);
                job.schedule();

                close();
            }

        });

    }

    protected void createFooter(Composite parent) {
        // to be overriden
    }

    // TODO the implementation of this method is horrible and creating too many widgets
    // table/column renderer/editor should be used instead should be used instead
    protected void addInstallButtons() {
        final AtomicInteger enabledButtonCount = new AtomicInteger(0);
        tableViewerCreator.getTableViewer().getControl().setRedraw(false);
        final Table table = tableViewerCreator.getTable();
        manualInstallButtonMap = new HashMap<ModuleToInstall, Button>();
        ILibrariesService librariesService = LibManagerUiPlugin.getDefault().getLibrariesService();

        disposePreviousEditors();
        for (final TableItem item : table.getItems()) {
            TableEditor editor = new TableEditor(table);
            installButtonsEditors.add(editor);
            Control control = null;
            Object obj = item.getData();
            if (obj instanceof ModuleToInstall) {
                final ModuleToInstall data = (ModuleToInstall) obj;
                boolean isInstalled = false;
                isInstalled = librariesService.getLibraryStatus(data.getName(), data.getMavenUri()) == ELibraryInstallStatus.INSTALLED;
                boolean hasDownloadUrl = data.getUrl_description() != null;
                if (!MavenConstants.DOWNLOAD_MANUAL.equals(data.getDistribution())) {// add the button to download
                    final Button button = new Button(table, SWT.FLAT);
                    control = button;
                    enabledButtonCount.incrementAndGet();
                    button.setText(Messages.getString("ExternalModulesInstallDialog_Download")); //$NON-NLS-1$
                    button.setData(item);
                    button.addSelectionListener(new SelectionAdapter() {

                        @Override
                        public void widgetSelected(SelectionEvent e) {
                            table.select(table.indexOf(item));
                            launchIndividualDownload(enabledButtonCount, data, button);
                        }

                    });
                    button.setEnabled(!isInstalled);
                    button.setToolTipText(data.toString());
                } else {// add the link for manual download
                    Composite composite = new Composite(table, SWT.NONE);
                    composite.setBackground(color);
                    control = composite;
                    GridLayout layout = new GridLayout(hasDownloadUrl ? 2 : 1, false);
                    layout.marginHeight = 0;
                    layout.verticalSpacing = 1;
                    composite.setLayout(layout);
                    if (hasDownloadUrl) {
                        Link openLink = new Link(composite, SWT.NONE);
                        GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.CENTER).applyTo(openLink);
                        openLink.setBackground(color);
                        // openLink.setLayoutData(gData);
                        openLink.setText("<a href=\"\">" + Messages.getString("ExternalModulesInstallDialog.openInBrowser") + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        openLink.addSelectionListener(new SelectionAdapter() {

                            @Override
                            public void widgetSelected(final SelectionEvent e) {
                                // Program.launch(data.getUrl_description());
                                openURL(data.getUrl_description());
                            }
                        });
                    }// else no download URL so just add the install buttonb
                    enabledButtonCount.incrementAndGet();
                    Button importButton = new Button(composite, SWT.FLAT);
                    importButton.setImage(ImageProvider.getImage(ECoreImage.IMPORT_JAR));
                    importButton.setToolTipText(Messages.getString("ImportExternalJarAction.title")); //$NON-NLS-1$
                    importButton.addSelectionListener(new ImportButtonSelectionListener(enabledButtonCount, item));
                    manualInstallButtonMap.put(data, importButton);
                    GridDataFactory.fillDefaults().align(SWT.RIGHT, SWT.CENTER).grab(true, false).applyTo(importButton);
                    importButton.setEnabled(!isInstalled);
                    importButton.setToolTipText(data.toString());
                }
                editor.grabHorizontal = true;
                editor.setEditor(control, item, tableViewerCreator.getColumns().indexOf(installcolumn));
                editor.layout();
                // url
                editor = new TableEditor(table);
                installButtonsEditors.add(editor);
                Composite composite = new Composite(table, SWT.NONE);
                composite.setBackground(color);
                // GridLayout layout = new GridLayout();
                FormLayout layout = new FormLayout();
                layout.marginHeight = 0;
                layout.marginWidth = 0;
                composite.setLayout(layout);
                FormData gData = new FormData();
                gData.left = new FormAttachment(0);
                gData.right = new FormAttachment(100);
                gData.top = new FormAttachment(composite, 0, SWT.CENTER);
                final Link openLink = new Link(composite, SWT.NONE);
                openLink.setLayoutData(gData);
                openLink.setBackground(color);
                gData.height = new GC(composite).stringExtent(" ").y; //$NON-NLS-1$
                openLink.setText("<a href=\"\">" + (hasDownloadUrl ? data.getUrl_description() : "") + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$
                openLink.addSelectionListener(new SelectionAdapter() {

                    @Override
                    public void widgetSelected(final SelectionEvent e) {
                        // Program.launch(data.getUrl_description());
                        openURL(data.getUrl_description());
                    }
                });
                editor.grabHorizontal = true;
                // editor.minimumHeight = 20;
                editor.setEditor(composite, item, tableViewerCreator.getColumns().indexOf(urlcolumn));
                editor.layout();
            }
        }
        tableViewerCreator.getTableViewer().getTable().layout();
        tableViewerCreator.getTableViewer().refresh(true);
        tableViewerCreator.getTableViewer().getControl().setRedraw(true);
    }

    private static void openURL(String strURL) {
        if (Program.launch(strURL)) {
            return;
        }
        URL openURL = null;
        try {
            openURL = new URL(strURL);
            PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(openURL);
        } catch (PartInitException e) {
            // if no default browser (like on linux), try to open directly with firefox.
            try {
                Runtime.getRuntime().exec("firefox " + openURL.toString()); //$NON-NLS-1$
            } catch (IOException e2) {
                if (PlatformUI.getWorkbench().getBrowserSupport().isInternalWebBrowserAvailable()) {
                    IWebBrowser browser;
                    try {
                        browser = PlatformUI.getWorkbench().getBrowserSupport().createBrowser("registrationId"); //$NON-NLS-1$
                        browser.openURL(openURL);
                    } catch (PartInitException e1) {
                        ExceptionHandler.process(e);
                    }
                } else {
                    ExceptionHandler.process(e);
                }
            }
        } catch (MalformedURLException e) {
            ExceptionHandler.process(e);
        }
    }

    /**
     * dispose of previously set editors and their associated controls before creating new ones
     */
    private void disposePreviousEditors() {
        for (TableEditor te : installButtonsEditors) {
            if (te.getEditor() != null) {
                te.getEditor().dispose();
            }
            te.dispose();
        }
        installButtonsEditors.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.librariesmanager.ui.dialogs.ExternalModulesInstallDialog#createButtonsForButtonBar(org.eclipse.swt
     * .widgets.Composite)
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        installAllBtn = createInstallButton(parent);
        updateInstallModulesButtonState();
        addListeners();// bad name but I accanont change it since it may have been overriden.

    }

    /**
     * Creates the Install button for this wizard dialog. Creates a standard (<code>SWT.PUSH</code>) button and
     * registers for its selection events. Note that the number of columns in the button bar composite is incremented.
     * 
     * @param parent the parent button bar
     * @return the new Install button
     */
    private Button createInstallButton(Composite parent) {
        // increment the number of columns in the button bar
        ((GridLayout) parent.getLayout()).numColumns++;
        Button button = new Button(parent, SWT.PUSH);
        button.setText(Messages.getString("ExternalModulesInstallDialog_InstallAll") + "  "); //$NON-NLS-1$ //$NON-NLS-2$//a space is added cause the last letter is missing on my MAC
        setButtonLayoutData(button);
        button.setFont(parent.getFont());

        return button;
    }

    @Override
    protected void okPressed() {
        super.okPressed();
        color.dispose();
    }

    /**
     * created by sgandon on 30 sept. 2013 Detailled comment
     * 
     */
    private final class ImportButtonSelectionListener implements SelectionListener {

        private final TableItem item;

        private AtomicInteger enabledButtonCount;

        /**
         * DOC sgandon ImportButtonSelectionListener constructor comment.
         * 
         * @param item
         */
        public ImportButtonSelectionListener(AtomicInteger enabledButtonCount, TableItem item) {
            this.item = item;
            this.enabledButtonCount = enabledButtonCount;
        }

        @Override
        public void widgetSelected(SelectionEvent e) {
            showImportJarDialog(enabledButtonCount, item);
        }

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
            widgetSelected(e);
        }
    }

    /**
     * I (SG) do not want to refadctor every thing so I delagate to a IRunnableWithProgress
     **/
    class DownloadModuleJob extends Job {

        private DownloadModuleRunnableWithLicenseDialog downloadModuleRunnable;

        public DownloadModuleJob(List<ModuleToInstall> toDownload) {
            super(Messages.getString("ExternalModulesInstallDialog.downloading1")); //$NON-NLS-1$
            downloadModuleRunnable = new DownloadModuleRunnableWithLicenseDialog(toDownload, getShell());
        }

        @Override
        protected IStatus run(final IProgressMonitor monitor) {
            downloadModuleRunnable.run(monitor);
            return Status.OK_STATUS;
        }

        Set<String> getInstalledModule() {
            return downloadModuleRunnable.getInstalledModules();
        }

        Set<String> getDownloadFailed() {
            return downloadModuleRunnable.getDownloadFailed();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.librariesmanager.ui.dialogs.IJobDownListener#downloadProgress()
     */
    @Override
    public void listModulesDone() {
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                if (inputList.size() > 0) {
                    open();
                } else if (showMessage) {
                    MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                            Messages.getString("ExternalModulesInstallDialog.MessageDialog.Infor"), //$NON-NLS-1$
                            Messages.getString("ExternalModulesInstallDialog_NoExternalModules")); //$NON-NLS-1$
                }
            }
        });

    }

    /**
     * DOC sgandon Comment method "showImportJarDialog".
     * 
     * @param item
     */
    public void showImportJarDialog(AtomicInteger enabledButtonCount, TableItem item) {
        if (item.getData() instanceof ModuleToInstall) {
            ModuleToInstall module = (ModuleToInstall) item.getData();
            String[] importedJars = new ImportExternalJarAction().handleImportJarDialog(getShell(), module);
            updateManualImportedJars(enabledButtonCount, importedJars);
            if (importedJars.length > 1) {
                String message = "";
                for (int i = 0; i < importedJars.length; i++) {
                    String jarName = importedJars[i];
                    message = message + jarName;
                    if (i < importedJars.length - 1) {
                        message = message + " , ";
                    }
                }
                MessageDialog.openInformation(getShell(), Messages.getString("ExternalModulesInstallDialog.imported.dialog"),
                        message);
            }
        }
    }

    /**
     * DOC sgandon Comment method "updateManualImportedJars".
     * 
     * @param importedJars
     */
    private void updateManualImportedJars(AtomicInteger enabledButtonCount, String[] importedJars) {
        for (Entry<ModuleToInstall, Button> moduleAndButton : manualInstallButtonMap.entrySet()) {
            String jarName = moduleAndButton.getKey().getName();
            for (String importedJar : importedJars) {
                if (importedJar.equals(jarName)) {// disable the button
                    moduleAndButton.getValue().setEnabled(false);
                    enabledButtonCount.decrementAndGet();
                }// else leave the button as it is
            }
        }
        if (enabledButtonCount.get() == 0) {
            close();
            // refresh
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerCoreService.class)) {
                IDesignerCoreService service = (IDesignerCoreService) GlobalServiceRegister.getDefault().getService(
                        IDesignerCoreService.class);
                if (service != null) {
                    service.refreshComponentView();
                }
            }
            LibManagerUiPlugin.getDefault().getLibrariesService().checkLibraries();
        }
    }

    /**
     * DOC sgandon Comment method "getModulesToBeInstalled".
     * 
     * @return
     */
    protected List<ModuleToInstall> getModulesToBeInstalled() {
        List<ModuleToInstall> theInputList = tableViewerCreator.getInputList();

        List<ModuleToInstall> toInstall = new ArrayList<ModuleToInstall>();
        for (ModuleToInstall module : theInputList) {
            if (!MavenConstants.DOWNLOAD_MANUAL.equals(module.getDistribution())
                    && !jarsInstalledSuccuss.contains(module.getName())
                    && ELibraryInstallStatus.INSTALLED != ModuleStatusProvider.getStatus(module.getMavenUri())) {
                toInstall.add(module);
            }
        }
        return toInstall;
    }

    /**
     * called when the user clicked on a indivual download button.
     * 
     * @param button, to make it disabled or enabled
     * 
     * @param enabledButtonCount, if 0 means that the last button has been clicked. This needs to maintained when
     * enabeling or disabeling the button
     * @param data, the data to install
     */
    protected void launchIndividualDownload(final AtomicInteger enabledButtonCount, final ModuleToInstall data,
            final Button button) {
        button.setEnabled(false);
        enabledButtonCount.decrementAndGet();
        final DownloadModuleJob job = new DownloadModuleJob(Collections.singletonList(data));
        job.addJobChangeListener(new JobChangeAdapter() {

            @Override
            public void done(IJobChangeEvent event) {
                Display.getDefault().asyncExec(new Runnable() {

                    @Override
                    public void run() {
                        String message = ""; //$NON-NLS-1$
                        Set<String> installedModule = job.getInstalledModule();
                        if (!installedModule.isEmpty()) {
                            message = Messages.getString("ExternalModulesInstallDialog_Download_Ok", data.getName()); //$NON-NLS-1$
                            // emptyLibs(); //never empty all jars, only clean the existed one
                            ImportExternalJarAction.cleanupLib(installedModule);
                        } else {
                            message = Messages.getString("ExternalModulesInstallDialog_Download_Fialed", data.getName());; //$NON-NLS-1$
                            // set enable to let user be able to try download again
                            button.setEnabled(true);
                        }
                        MessageDialog.openInformation(getShell(),
                                Messages.getString("ExternalModulesInstallDialog.MessageDialog.Infor"), message); //$NON-NLS-1$
                        // need to force a refresh after install jars of component
                        if (enabledButtonCount.get() == 0) {
                            if (GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerCoreService.class)) {
                                IDesignerCoreService service = (IDesignerCoreService) GlobalServiceRegister.getDefault()
                                        .getService(IDesignerCoreService.class);
                                if (service != null) {
                                    service.refreshComponentView();
                                }
                            }
                        }
                    }
                });
            }
        });
        job.setUser(true);
        job.setPriority(Job.INTERACTIVE);
        job.schedule();
        if (enabledButtonCount.get() == 0) {
            close();
        }
    }

}
