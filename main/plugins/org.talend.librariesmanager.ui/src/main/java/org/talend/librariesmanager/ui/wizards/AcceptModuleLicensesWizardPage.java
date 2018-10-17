package org.talend.librariesmanager.ui.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.model.general.ModuleToInstall;
import org.talend.core.model.utils.TalendPropertiesUtil;
import org.talend.librariesmanager.ui.LibManagerUiPlugin;
import org.talend.librariesmanager.ui.i18n.Messages;
import org.talend.librariesmanager.utils.RemoteModulesHelper;

/**
 * 
 * created by ycbai on 2013-10-16 Detailled comment
 * 
 */
public class AcceptModuleLicensesWizardPage extends WizardPage {

    private static final String UNKNOWN_LICENSE = "Unknown license"; //$NON-NLS-1$

    private static final String DIALOG_SETTINGS_SECTION = "LicensessPage"; //$NON-NLS-1$

    private static final String LIST_WEIGHT = "ListSashWeight"; //$NON-NLS-1$

    private static final String LICENSE_WEIGHT = "LicenseSashWeight"; //$NON-NLS-1$

    private static final String LICENSE_SEP = ","; //$NON-NLS-1$

    private TreeViewer licenseTypeViewer;

    private Browser licenseTextBox;

    private Text licenseText;

    private Button acceptButton;

    private Button declineButton;

    private SashForm sashForm;

    private List<ModuleToInstall> modulesToInstall;

    private Map<String, Boolean> licenseTypeToStatus = new HashMap<String, Boolean>();

    public AcceptModuleLicensesWizardPage(List<ModuleToInstall> modulesToInstall) {
        super("AcceptLicenses"); //$NON-NLS-1$
        setTitle(Messages.getString("AcceptModuleLicensesWizardPage.title")); //$NON-NLS-1$
        setMessage(Messages.getString("AcceptModuleLicensesWizardPage.desc")); //$NON-NLS-1$
        this.modulesToInstall = modulesToInstall;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createControl(Composite parent) {
        initializeDialogUnits(parent);
        if (modulesToInstall == null || modulesToInstall.size() == 0) {
            Label label = new Label(parent, SWT.NONE);
            setControl(label);
        } else {
            sashForm = new SashForm(parent, SWT.HORIZONTAL);
            sashForm.setLayout(new GridLayout());
            GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
            sashForm.setLayoutData(gd);

            createLicenseListSection(sashForm);
            createLicenseContentSection(sashForm);
            sashForm.setWeights(getSashWeights());
            setControl(sashForm);
        }
        Dialog.applyDialogFont(getControl());
    }

    private void createLicenseListSection(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        composite.setLayout(layout);
        GridData gd = new GridData(GridData.FILL_BOTH);
        composite.setLayoutData(gd);

        Label label = new Label(composite, SWT.NONE);
        label.setText(Messages.getString("AcceptModuleLicensesWizardPage.licensesList.label")); //$NON-NLS-1$
        licenseTypeViewer = new TreeViewer(composite, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        licenseTypeViewer.setContentProvider(new LicenseContentProvider());
        licenseTypeViewer.setLabelProvider(new LicenseLabelProvider());
        licenseTypeViewer.setComparator(new ViewerComparator());

        licenseTypeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                handleSelectionChanged();
            }

        });
        gd = new GridData(GridData.FILL_BOTH);
        licenseTypeViewer.getControl().setLayoutData(gd);
    }

    private void createLicenseAcceptSection(Composite parent) {
        // Buttons for accepting licenses
        Composite buttonContainer = new Composite(parent, SWT.NULL);
        GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        buttonContainer.setLayout(new GridLayout());
        buttonContainer.setLayoutData(gd);

        acceptButton = new Button(buttonContainer, SWT.RADIO);
        acceptButton.setText(Messages.getString("AcceptModuleLicensesWizardPage.licenseContent.button.accept")); //$NON-NLS-1$

        acceptButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                licenseTypeToStatus.put(getSelectedLicenseType(), true);
            }
        });
        declineButton = new Button(buttonContainer, SWT.RADIO);
        declineButton.setText(Messages.getString("AcceptModuleLicensesWizardPage.licenseContent.button.decline")); //$NON-NLS-1$
        declineButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                licenseTypeToStatus.put(getSelectedLicenseType(), false);
            }
        });

        acceptButton.setSelection(false);
        declineButton.setSelection(true);
    }

    private void createLicenseContentSection(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        composite.setLayout(layout);
        GridData gd = new GridData(GridData.FILL_BOTH);
        composite.setLayoutData(gd);

        Label label = new Label(composite, SWT.NONE);
        label.setText(Messages.getString("AcceptModuleLicensesWizardPage.licenseContent.label")); //$NON-NLS-1$
        if (TalendPropertiesUtil.isEnabledUseBrowser()) {
            licenseTextBox = new Browser(composite, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.READ_ONLY);
            licenseTextBox.setBackground(licenseTextBox.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

            initializeDialogUnits(licenseTextBox);
            gd = new GridData(SWT.FILL, SWT.FILL, true, true);
            licenseTextBox.setLayoutData(gd);
        } else {
            licenseText = new Text(composite, SWT.MULTI | SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL);
            initializeDialogUnits(licenseText);
            licenseText.setLayoutData(new GridData(GridData.FILL_BOTH));
        }
        createLicenseAcceptSection(composite);

        setControl(composite);
    }

    private String getSelectedLicenseType() {
        Element selected = getSelectedElement();
        if (selected != null) {
            if (selected instanceof License) {
                License license = (License) selected;
                return license.getType();
            } else if (selected instanceof Module) {
                Module module = (Module) selected;
                Element parent = module.getParent();
                if (parent instanceof License) {
                    return ((License) module.getParent()).getType();
                }
            }
        }

        return UNKNOWN_LICENSE;
    }

    private Element getSelectedElement() {
        IStructuredSelection selection = (IStructuredSelection) licenseTypeViewer.getSelection();
        if (!selection.isEmpty()) {
            Object selected = selection.getFirstElement();
            if (selected instanceof Element) {
                return (Element) selected;
            }
        }

        return null;
    }

    private void handleSelectionChanged() {
        Element selected = getSelectedElement();
        if (selected != null) {
            License license = null;
            if (selected instanceof License) {
                license = (License) selected;
            } else if (selected instanceof Module) {
                Module module = (Module) selected;
                Element parent = module.getParent();
                if (parent instanceof License) {
                    license = (License) module.getParent();
                }
            }
            if (license != null) {
                String type = license.getType();
                Boolean licenseStatus = licenseTypeToStatus.get(type);
                boolean isLicenseAccepted = licenseStatus != null && licenseStatus;
                acceptButton.setSelection(isLicenseAccepted);
                declineButton.setSelection(!isLicenseAccepted);
                String url = license.getUrl();

                if (TalendPropertiesUtil.isEnabledUseBrowser() && licenseTextBox != null) {
                    if (url != null) {
                        licenseTextBox.setUrl(url);
                    } else {
                        licenseTextBox.setText(Messages.getString("AcceptModuleLicensesWizardPage.licenseContent.defaultDesc")); //$NON-NLS-1$
                    }
                } else if (licenseText != null) {
                    String licenseContent = RemoteModulesHelper.getInstance().getLicenseContentByUrl(url);
                    if (licenseContent != null) {
                        licenseText.setText(licenseContent);
                    } else {
                        licenseText.setText(Messages.getString("AcceptModuleLicensesWizardPage.licenseContent.defaultDesc")); //$NON-NLS-1$
                    }
                }
            }
        }
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean visible) {
        retrieveLicensesModel();
        super.setVisible(visible);
    }

    private void retrieveLicensesModel() {
        final Element licenseRoot = new Element();
        IRunnableWithProgress runnableWithProgress = new IRunnableWithProgress() {

            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                monitor.beginTask(Messages.getString("AcceptModuleLicensesWizardPage.retrieveLicenses"), IProgressMonitor.UNKNOWN); //$NON-NLS-1$
                try {
                    List<ModuleToInstall> mti = modulesToInstall;
                    if (mti == null) {
                        mti = new ArrayList<ModuleToInstall>();
                    }
                    Map<String, License> typeToLicense = new HashMap<String, License>();
                    for (ModuleToInstall moduleToInstall : mti) {
                        // no need accept license if it is from custom nexus
                        if (moduleToInstall.isFromCustomNexus()) {
                            continue;
                        }
                        String licenseType = moduleToInstall.getLicenseType();
                        if (licenseType == null) {
                            licenseType = UNKNOWN_LICENSE;
                        }
                        boolean isLicenseAccepted = LibManagerUiPlugin.getDefault().getPreferenceStore().getBoolean(licenseType);
                        if (isLicenseAccepted) {
                            continue;
                        }
                        License license = typeToLicense.get(licenseType);
                        if (license == null) {
                            license = new License();
                            String licenseUrl = moduleToInstall.getLicenseUrl();
                            if (licenseUrl == null) {
                                licenseUrl = RemoteModulesHelper.getInstance().getLicenseUrl(licenseType);
                            }
                            license.setName(licenseType);
                            license.setType(licenseType);
                            license.setUrl(licenseUrl);
                            license.setParent(licenseRoot);
                            licenseRoot.addChild(license);
                            typeToLicense.put(licenseType, license);
                            licenseTypeToStatus.put(licenseType, false);
                        }
                        Module module = new Module();
                        module.setName(moduleToInstall.getName());
                        module.setParent(license);
                        license.addChild(module);
                    }
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
        licenseTypeViewer.setInput(licenseRoot);
        // select first item
        if (licenseTypeViewer.getTree().getItems().length > 0) {
            TreeItem item = licenseTypeViewer.getTree().getItem(0);
            if (item != null) {
                licenseTypeViewer.setSelection(new StructuredSelection(item.getData()), true);
            }// else nothing to select
        }// else no item
    }

    /**
     * The wizard is finishing. Perform any necessary processing.
     * 
     * @return <code>true</code> if the finish can proceed, <code>false</code> if it should not.
     */
    public boolean performFinish() {
        if (!confirmRejectLicenses()) {
            return false;
        }

        rememberAcceptedLicenses();
        saveBoundsRelatedSettings();
        return true;
    }

    private boolean confirmRejectLicenses() {
        List<String> rejectLicensesList = new ArrayList<String>();
        Iterator<Entry<String, Boolean>> iter = licenseTypeToStatus.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Boolean> entry = iter.next();
            String licenseType = entry.getKey();
            if (!entry.getValue()) {
                rejectLicensesList.add(licenseType);
            }
        }
        StringBuffer rejectLicenses = new StringBuffer();
        Collections.sort(rejectLicensesList);
        for (String lt : rejectLicensesList) {
            rejectLicenses.append(lt).append(LICENSE_SEP);
        }
        boolean hasRejectLicenses = false;
        if (rejectLicenses.length() > 0) {
            rejectLicenses.deleteCharAt(rejectLicenses.length() - 1);
            hasRejectLicenses = true;
        }
        if (hasRejectLicenses) {
            MessageBox messagebox = new MessageBox(getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
            messagebox.setText(Messages.getString("AcceptModuleLicensesWizardPage.rejectLicensesConfirmation.title")); //$NON-NLS-1$
            messagebox.setMessage(Messages.getString(
                    "AcceptModuleLicensesWizardPage.rejectLicensesConfirmation.msg", rejectLicenses.toString())); //$NON-NLS-1$
            if (messagebox.open() == SWT.YES) {
                return true;
            } else {
                return false;
            }
        }

        return true;
    }

    private void rememberAcceptedLicenses() {
        Iterator<Entry<String, Boolean>> iter = licenseTypeToStatus.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Boolean> entry = iter.next();
            String licenseType = entry.getKey();
            boolean status = entry.getValue();
            LibManagerUiPlugin.getDefault().getPreferenceStore().setValue(licenseType, status);
        }
    }

    private String getDialogSettingsName() {
        return getWizard().getClass().getName() + "." + DIALOG_SETTINGS_SECTION; //$NON-NLS-1$
    }

    /**
     * Save any settings related to the current size and location of the wizard page.
     */
    private void saveBoundsRelatedSettings() {
        if (licenseTypeViewer == null || licenseTypeViewer.getTree().isDisposed()) {
            return;
        }
        IDialogSettings settings = LibManagerUiPlugin.getDefault().getDialogSettings();
        IDialogSettings section = settings.getSection(getDialogSettingsName());
        if (section == null) {
            section = settings.addNewSection(getDialogSettingsName());
        }
        if (sashForm == null || sashForm.isDisposed()) {
            return;
        }
        int[] weights = sashForm.getWeights();
        section.put(LIST_WEIGHT, weights[0]);
        section.put(LICENSE_WEIGHT, weights[1]);
    }

    private int[] getSashWeights() {
        IDialogSettings settings = LibManagerUiPlugin.getDefault().getDialogSettings();
        IDialogSettings section = settings.getSection(getDialogSettingsName());
        if (section != null) {
            try {
                int[] weights = new int[2];
                if (section.get(LIST_WEIGHT) != null) {
                    weights[0] = section.getInt(LIST_WEIGHT);
                    if (section.get(LICENSE_WEIGHT) != null) {
                        weights[1] = section.getInt(LICENSE_WEIGHT);
                        return weights;
                    }
                }
            } catch (NumberFormatException e) {
                // Ignore if there actually was a value that didn't parse.
            }
        }
        return new int[] { 30, 70 };
    }

    class Element {

        private String name;

        private Element parent;

        private List<Element> children = new ArrayList<Element>();;

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Element getParent() {
            return this.parent;
        }

        public void setParent(Element parent) {
            this.parent = parent;
        }

        public List<? extends Element> getChildren() {
            return this.children;
        }

        public void addChild(Element child) {
            if (!children.contains(child)) {
                children.add(child);
            }
        }
    }

    class Module extends Element {
        //
    }

    class License extends Element {

        private String type;

        private String url;

        public String getType() {
            return this.type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }

    class LicenseContentProvider implements ITreeContentProvider {

        @Override
        public Object[] getChildren(Object parentElement) {
            if (parentElement instanceof Element) {
                Element element = (Element) parentElement;
                return element.getChildren().toArray();
            }

            return null;
        }

        @Override
        public Object getParent(Object element) {
            if (element instanceof Element) {
                return ((Element) element).getParent();
            }

            return null;
        }

        @Override
        public boolean hasChildren(Object element) {
            if (element instanceof Element) {
                return ((Element) element).getChildren().size() > 0;
            }

            return false;
        }

        @Override
        public Object[] getElements(Object inputElement) {
            return getChildren(inputElement);
        }

        @Override
        public void dispose() {
            // Nothing to do
        }

        @Override
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            // Nothing to do
        }
    }

    class LicenseLabelProvider extends LabelProvider {

        @Override
        public Image getImage(Object element) {
            return null;
        }

        @Override
        public String getText(Object element) {
            if (element instanceof Element) {
                return ((Element) element).getName();
            }

            return ""; //$NON-NLS-1$
        }

    }
}
