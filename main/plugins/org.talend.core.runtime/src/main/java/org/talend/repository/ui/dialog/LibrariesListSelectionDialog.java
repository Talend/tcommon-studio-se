package org.talend.repository.ui.dialog;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.AbstractElementListSelectionDialog;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.ISVNProviderServiceInCoreRuntime;
import org.talend.core.PluginChecker;
import org.talend.core.runtime.i18n.Messages;

/**
 * 
 * created by ycbai on 2013-3-19 Detailled comment
 * 
 * <p>
 * You can use this dialog to browse or select all internal and external libraries.<br>
 * You can use {@link #getResult()} method to get the result.
 * </p>
 * 
 */
public class LibrariesListSelectionDialog extends AbstractElementListSelectionDialog {

    private Button internalBtn, externalBtn;

    private Composite internalPage;

    private Composite externalPage;

    private Object[] fElements;

    private boolean multiSelection = false;

    private boolean deployNonexistentLibs = true;

    private String[] libExtensions;

    private ILibraryManagerService librairesService;

    private Set<String> libsInCache = new HashSet<String>();

    private Set<URI> libsNeedDeploy = new HashSet<URI>();

    public LibrariesListSelectionDialog(Shell parent) {
        this(parent, new LabelProvider());
    }

    public LibrariesListSelectionDialog(Shell parent, ILabelProvider renderer) {
        super(parent, renderer);
        if (libExtensions == null) {
            libExtensions = FilesUtils.getAcceptJARFilesSuffix();
        }
        librairesService = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(ILibraryManagerService.class);
        libsInCache = librairesService.list(new NullProgressMonitor());
        if (fElements == null) {
            setElements(libsInCache.toArray());
        }
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.getString("LibrariesListSelectionDialog.title")); //$NON-NLS-1$
        newShell.setSize(450, 500);
    }

    @Override
    protected void initializeBounds() {
        super.initializeBounds();

        Point size = getShell().getSize();
        Point location = getInitialLocation(size);
        getShell().setBounds(getConstrainedShellBounds(new Rectangle(location.x, location.y, size.x, size.y)));
    }

    /*
     * @see SelectionStatusDialog#computeResult()
     */
    @Override
    protected void computeResult() {
        if (internalBtn.getSelection()) {
            setResult(Arrays.asList(getSelectedElements()));
        }
    }

    /*
     * @see Dialog#createDialogArea(Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite contents = (Composite) super.createDialogArea(parent);

        createHeadContents(contents);
        createInternalContents(contents);
        createExternalContents(contents);

        updateContents();
        addListener();

        return contents;
    }

    private void createHeadContents(Composite parent) {
        Group btnGroup = new Group(parent, SWT.NONE);
        btnGroup.setLayout(new GridLayout(2, false));
        btnGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        internalBtn = new Button(btnGroup, SWT.RADIO);
        internalBtn.setText(Messages.getString("LibrariesListSelectionDialog.button.internal")); //$NON-NLS-1$
        internalBtn.setSelection(true);
        externalBtn = new Button(btnGroup, SWT.RADIO);
        externalBtn.setText(Messages.getString("LibrariesListSelectionDialog.button.external")); //$NON-NLS-1$
    }

    private void createInternalContents(Composite parent) {
        internalPage = new Composite(parent, SWT.NONE);
        internalPage.setLayoutData(new GridData(GridData.FILL_BOTH));
        internalPage.setLayout(new GridLayout());
        createFilterText(internalPage);
        createFilteredList(internalPage);
        setListElements(fElements);
        setSelection(getInitialElementSelections().toArray());
    }

    private void createExternalContents(Composite parent) {
        externalPage = new Composite(parent, SWT.NONE);
        externalPage.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        externalPage.setLayout(new GridLayout(3, false));

        final LabelledText libText = new LabelledText(externalPage,
                Messages.getString("LibrariesListSelectionDialog.selectLibFromOS"), 1); //$NON-NLS-1$
        libText.getTextControl().setEditable(false);

        Button browseBtn = new Button(externalPage, SWT.PUSH);
        browseBtn.setText(Messages.getString("LibrariesListSelectionDialog.browseButton.text")); //$NON-NLS-1$

        browseBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                int flags = SWT.OPEN | (multiSelection ? SWT.MULTI : SWT.SINGLE);
                FileDialog fileDialog = new FileDialog(getShell(), flags);
                fileDialog.setText(Messages.getString("LibrariesListSelectionDialog.selectLibFromOS")); //$NON-NLS-1$
                fileDialog.setFilterExtensions(libExtensions);
                String res = fileDialog.open();
                if (res != null) {
                    StringBuffer displayBuffer = new StringBuffer();
                    String parentPath = fileDialog.getFilterPath();
                    String[] jars = fileDialog.getFileNames();
                    for (String jar : jars) {
                        if (!libsInCache.contains(jar)) {
                            File jarFile = new File(parentPath + File.separatorChar + jar);
                            libsNeedDeploy.add(jarFile.toURI());
                        }
                        displayBuffer.append(jar).append(";"); //$NON-NLS-1$
                    }
                    setResult(Arrays.asList(jars));
                    libText.setText(displayBuffer.toString());
                    updateOkState();
                }
            }

        });

    }

    private void addListener() {
        internalBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                updateContents();
            }

        });
        externalBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                updateContents();
            }

        });
    }

    private void updateContents() {
        hideControl(internalPage, externalBtn.getSelection());
        hideControl(externalPage, internalBtn.getSelection());
    }

    @Override
    protected void updateOkState() {
        Button okButton = getOkButton();
        if (okButton != null) {
            okButton.setEnabled(getSelectedElements().length != 0 || getResult() != null);
        }
    }

    private void hideControl(Control control, boolean hide) {
        GridData dataBtn = (GridData) control.getLayoutData();
        dataBtn.exclude = hide;
        control.setLayoutData(dataBtn);
        control.setVisible(!hide);
        if (control.getParent() != null) {
            control.getParent().layout();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.dialogs.AbstractElementListSelectionDialog#setMultipleSelection(boolean)
     */
    @Override
    public void setMultipleSelection(boolean multipleSelection) {
        super.setMultipleSelection(multipleSelection);
        this.multiSelection = multipleSelection;
    }

    /**
     * DOC ycbai Comment method "setDeployNonexistentLibs".
     * 
     * <p>
     * Specifies whether or not deploy the nonexistent libraries.
     * </p>
     * 
     * @param deployNonexistentLibs
     */
    public void setDeployNonexistentLibs(boolean deployNonexistentLibs) {
        this.deployNonexistentLibs = deployNonexistentLibs;
    }

    /**
     * DOC ycbai Comment method "setLibExtensions".
     * 
     * <p>
     * Specifies supported extensions of libraries. "*.jar;*.properties;*.zip;*.dll;*.so;*.bar" are supported by
     * default.
     * </p>
     * 
     * @param libExtensions
     */
    public void setLibExtensions(String[] libExtensions) {
        this.libExtensions = libExtensions;
    }

    /**
     * DOC ycbai Comment method "setElements".
     * 
     * <p>
     * Sets the internal libraries elements list.
     * </p>
     * 
     * @param elements
     */
    public void setElements(Object[] elements) {
        fElements = elements;
    }

    private void doDeployLibs() {
        if (libsNeedDeploy.size() == 0) {
            return;
        }
        IRunnableWithProgress runnableWithProgress = new IRunnableWithProgress() {

            @Override
            public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                monitor.beginTask(Messages.getString("LibrariesListSelectionDialog.deployLibs"), libsNeedDeploy.size()); //$NON-NLS-1$
                try {
                    for (URI jar : libsNeedDeploy) {
                        monitor.subTask(jar.toURL().getPath());
                        librairesService.deploy(jar, monitor);
                        monitor.worked(1);
                    }
                } catch (MalformedURLException e) {
                    // dont care...
                } finally {
                    monitor.done();
                }
            }
        };
        ProgressMonitorDialog dialog = new ProgressMonitorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
        try {
            dialog.run(true, true, runnableWithProgress);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.dialogs.SelectionStatusDialog#okPressed()
     */
    @Override
    protected void okPressed() {
        super.okPressed();
        if (deployNonexistentLibs) {
            doDeployLibs();
        }
    }

}