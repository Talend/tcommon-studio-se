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
package org.talend.metadata.managment.ui.wizard;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.commons.ui.gmf.util.DisplayUtils;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.swt.dialogs.RepositoryFolderSelectionDialog;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.LockInfo;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.designer.core.convert.IProcessConvertService;
import org.talend.metadata.managment.ui.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IProxyRepositoryService;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.ui.properties.StatusHelper;

/**
 * Wizard page contains common properties fields.<br/>
 * 
 * $Id: PropertiesWizardPage.java 914 2006-12-08 08:28:53 +0000 (������, 08 ʮ���� 2006) bqian $
 * 
 */
public abstract class PropertiesWizardPage extends AbstractNamedWizardPage {

    /** Name text. */
    protected Text nameText;

    public Text getNameText() {
        return this.nameText;
    }

    /** Purpose text. */
    protected Text purposeText;

    /** Comment text. */
    protected Text descriptionText;

    /** Author text. */
    protected Text authorText;

    /** Locker text. */
    protected Text lockerText;

    /** Version text. */
    protected Text versionText;

    /** Status text. */
    // protected Text statusText;
    protected CCombo statusText;

    /** Version upgrade major button. */
    protected Button versionMajorBtn;

    /** Version upgrade minor button. */
    protected Button versionMinorBtn;

    protected Text pathText;

    protected IStatus purposeStatus;

    protected IStatus commentStatus;

    private boolean update = false;

    protected Property property;

    protected IPath destinationPath;

    private String path;

    protected boolean readOnly;

    protected StatusHelper statusHelper = null;

    protected boolean editPath = true;

    protected static final boolean NEED_CANCEL_BUTTON = true;

    private String lastVersionFound;

    // for save as: because there need check name and version
    private String orignalName = null;

    private String orignalVersion = null;

    private boolean isSaveAs = false;

    protected boolean allowVerchange = true;

    private Item item;

    private HashMap<String, Boolean> folderItems;

    protected IProcessConvertService converter;// Just for the page which would like to convert self to another process.

    // private Button convertBtn;// For convertation between M/R job and common job

    // private IProcessConverter processConverter;

    public void initializeSaveAs(String orignalName, String orignalVersion, boolean isSaveAs) {
        this.orignalName = orignalName;
        this.orignalVersion = orignalVersion;
        this.isSaveAs = isSaveAs;
    }

    protected PropertiesWizardPage(String pageName, Property property, IPath destinationPath) {
        this(pageName, property, destinationPath, false, true, null);
    }

    protected PropertiesWizardPage(String pageName, final Property property, IPath destinationPath, boolean readOnly,
            boolean editPath) {
        this(pageName, property, destinationPath, readOnly, editPath, null);
    }

    protected PropertiesWizardPage(String pageName, final Property property, IPath destinationPath, boolean readOnly,
            boolean editPath, String lastVersionFound) {
        super(pageName);
        statusHelper = new StatusHelper(CoreRuntimePlugin.getInstance().getProxyRepositoryFactory());
        this.destinationPath = destinationPath;
        this.readOnly = readOnly;
        this.editPath = editPath;

        purposeStatus = createOkStatus();
        commentStatus = createOkStatus();

        this.lastVersionFound = lastVersionFound;
        // Changed by Marvin Wang on Mar. 6, 2012 for TDI-24943.
        this.property = property;
        Job job = new Job("") { //$NON-NLS-1$

            @Override
            protected IStatus run(IProgressMonitor monitor) {
                try {
                    listExistingObjects = loadRepositoryViewObjectList();
                } catch (PersistenceException e) {
                    return new org.eclipse.core.runtime.Status(IStatus.ERROR, "org.talend.metadata.management.ui", 1, "", e); //$NON-NLS-1$ //$NON-NLS-2$
                }
                retrieveNameFinished = true;
                // force the refresh of the text field, no matter successfull retrieve of not.
                Display d = DisplayUtils.getDisplay();
                if (d != null) {
                    d.syncExec(new Runnable() {

                        @Override
                        public void run() {
                            evaluateTextField();
                        }

                    });
                }
                return Status.OK_STATUS;
            }
        };
        job.setUser(false);
        job.setPriority(Job.BUILD);
        job.schedule(); // start as soon as possible

        IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                IBrandingService.class);
        allowVerchange = brandingService.getBrandingConfiguration().isAllowChengeVersion();
    }

    /**
     * Loads the repository view objects that are used to check if the name of job(Opened in the current properties
     * wizard dialog) can be found. Added by Marvin Wang on Feb 22, 2013.
     * 
     * @return a list includes the instance of <code>IRepositoryViewObject</code>, which are used to check if a given
     * job name is present in the list.
     * @throws PersistenceException
     */
    protected List<IRepositoryViewObject> loadRepositoryViewObjectList() throws PersistenceException {
        List<IRepositoryViewObject> list = new ArrayList<IRepositoryViewObject>();

        List<IRepositoryViewObject> repViewObjectWithSameType = loadRepViewObjectWithSameType();

        if (repViewObjectWithSameType != null && repViewObjectWithSameType.size() > 0) {
            list.addAll(repViewObjectWithSameType);
        }

        List<IRepositoryViewObject> others = loadRepViewObjectWithOtherTypes();

        // Loads other repository view objects with the different repository type.
        if (others != null && others.size() > 0) {
            list.addAll(others);
        }
        return list;
    }

    /**
     * 
     * get all available process objects except current object(only If this object is a proecss object)
     * 
     * @return
     * @throws PersistenceException
     */
    protected List<IRepositoryViewObject> getAllProcessTypeObjectsWithoutCurrentType() throws PersistenceException {
        List<IRepositoryViewObject> list = new ArrayList<IRepositoryViewObject>();
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IProxyRepositoryService.class)) {
            final ERepositoryObjectType currentType = ERepositoryObjectType.getItemType(property.getItem());
            IProxyRepositoryService service = (IProxyRepositoryService) GlobalServiceRegister.getDefault().getService(
                    IProxyRepositoryService.class);

            List<ERepositoryObjectType> processTypeList = ERepositoryObjectType.getAllTypesOfProcess();
            if (processTypeList == null) {
                return list;
            }
            boolean currentTypeIsProcessType = processTypeList.contains(currentType);
            IProxyRepositoryFactory proxyRepositoryFactory = service.getProxyRepositoryFactory();
            for (ERepositoryObjectType processType : processTypeList) {
                if (currentTypeIsProcessType && currentType == processType) {
                    continue;
                }
                List<IRepositoryViewObject> processList = proxyRepositoryFactory.getAll(processType, true, false);
                if (processList != null && !processList.isEmpty()) {
                    list.addAll(processList);
                }
            }
        }
        return list;
    }

    /**
     * Loads the repository view objects, which have the same repository type as the current job that are opened in
     * properties wizard dialog. Added by Marvin Wang on Feb 22, 2013.
     * 
     * @return
     * @throws PersistenceException
     */
    private List<IRepositoryViewObject> loadRepViewObjectWithSameType() throws PersistenceException {
        List<IRepositoryViewObject> list = new ArrayList<IRepositoryViewObject>();
        ERepositoryObjectType type = ERepositoryObjectType.getItemType(property.getItem());
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IProxyRepositoryService.class)) {
            IProxyRepositoryService service = (IProxyRepositoryService) GlobalServiceRegister.getDefault().getService(
                    IProxyRepositoryService.class);

            list = service.getProxyRepositoryFactory().getAll(type, true, false);
        }
        return list;
    }

    /**
     * Loads other repository view objects, which have the different repository type as the current job that are opened
     * in properties wizard dialog. If the given job named is required to check with other repository view object with
     * different repository type, then implement this method to return others repository veiw object. Added by Marvin
     * Wang on Feb 22, 2013.
     * 
     * @return
     * @throws PersistenceException
     */
    protected List<IRepositoryViewObject> loadRepViewObjectWithOtherTypes() throws PersistenceException {
        return new ArrayList<IRepositoryViewObject>();
    }

    protected boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Getter for editPath.
     * 
     * @return the editPath
     */
    public boolean isEditPath() {
        return this.editPath;
    }

    /**
     * Sets the editPath.
     * 
     * @param editPath the editPath to set
     */
    public void setEditPath(boolean editPath) {
        this.editPath = editPath;
    }

    /**
     * Getter for isUpdate.
     * 
     * @return the isUpdate
     */
    public boolean isUpdate() {
        return this.update;
    }

    /**
     * Sets the isUpdate.
     * 
     * @param isUpdate the isUpdate to set
     */
    public void setUpdate(boolean update) {
        this.update = update;
    }

    /**
     * Getter for nameModifiedByUser.
     * 
     * @return the nameModifiedByUser
     */
    public boolean isNameModifiedByUser() {
        return this.nameModifiedByUser;
    }

    /**
     * Sets the nameModifiedByUser.
     * 
     * @param nameModifiedByUser the nameModifiedByUser to set
     */
    public void setNameModifiedByUser(boolean nameModifiedByUser) {
        this.nameModifiedByUser = nameModifiedByUser;
    }

    /**
     * Getter for destinationPath.
     * 
     * @return the destinationPath
     */
    public IPath getDestinationPath() {
        String pathStr = ""; //$NON-NLS-1$
        if (pathText != null) {
            pathStr = pathText.getText();
        }
        if (pathStr.contains("(default)")) {//$NON-NLS-1$
            int index = pathStr.indexOf(")");//$NON-NLS-1$
            if (pathStr.length() > index + 1) {
                pathStr = pathStr.substring(pathStr.indexOf(")") + 1);//$NON-NLS-1$
            }
        }
        return new Path(pathStr);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createControl(Composite parent) {
        GridData data;

        // Name
        Label nameLab = new Label(parent, SWT.NONE);
        nameLab.setText(Messages.getString("PropertiesWizardPage.Name")); //$NON-NLS-1$

        nameText = new Text(parent, SWT.BORDER);
        nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        nameText.setEditable(!readOnly);

        createFrameworkPart(parent);

        // Purpose
        Label purposeLab = new Label(parent, SWT.NONE);
        purposeLab.setText(Messages.getString("PropertiesWizardPage.Purpose")); //$NON-NLS-1$

        purposeText = new Text(parent, SWT.BORDER);
        purposeText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        purposeText.setEditable(!readOnly);

        // Description
        Label descriptionLab = new Label(parent, SWT.NONE);
        descriptionLab.setText(Messages.getString("PropertiesWizardPage.Description")); //$NON-NLS-1$
        descriptionLab.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

        descriptionText = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.heightHint = 60;
        descriptionText.setLayoutData(data);
        descriptionText.setEditable(!readOnly);

        // Author
        Label authorLab = new Label(parent, SWT.NONE);
        authorLab.setText(Messages.getString("PropertiesWizardPage.Author")); //$NON-NLS-1$

        authorText = new Text(parent, SWT.BORDER);
        authorText.setEnabled(false);
        authorText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // Locker
        Label lockerLab = new Label(parent, SWT.NONE);
        lockerLab.setText(Messages.getString("PropertiesWizardPage.Locker")); //$NON-NLS-1$

        lockerText = new Text(parent, SWT.BORDER);
        lockerText.setEnabled(false);
        lockerText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // Version
        IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                IBrandingService.class);
        allowVerchange = brandingService.getBrandingConfiguration().isAllowChengeVersion();
        if (allowVerchange) {
            Label versionLab = new Label(parent, SWT.NONE);
            versionLab.setText(Messages.getString("PropertiesWizardPage.Version")); //$NON-NLS-1$

            Composite versionContainer = new Composite(parent, SWT.NONE);
            versionContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            GridLayout versionLayout = new GridLayout(3, false);
            versionLayout.marginHeight = 0;
            versionLayout.marginWidth = 0;
            versionLayout.horizontalSpacing = 0;
            versionContainer.setLayout(versionLayout);

            versionText = new Text(versionContainer, SWT.BORDER);
            versionText.setEnabled(false);
            versionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

            versionMajorBtn = new Button(versionContainer, SWT.PUSH);
            versionMajorBtn.setText(Messages.getString("PropertiesWizardPage.Version.Major")); //$NON-NLS-1$
            versionMajorBtn.setEnabled(!readOnly && allowVerchange);

            versionMinorBtn = new Button(versionContainer, SWT.PUSH);
            versionMinorBtn.setText(Messages.getString("PropertiesWizardPage.Version.Minor")); //$NON-NLS-1$
            versionMinorBtn.setEnabled(!readOnly && allowVerchange);
        }

        // Status
        Label statusLab = new Label(parent, SWT.NONE);
        statusLab.setText(Messages.getString("PropertiesWizardPage.Status")); //$NON-NLS-1$

        statusText = new CCombo(parent, SWT.BORDER);
        statusText.setEditable(false);
        List<org.talend.core.model.properties.Status> statusList;
        try {
            if (property != null) {
                statusList = statusHelper.getStatusList(property);
                statusText.setItems(toArray(statusList));
                statusText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
                // statusText.setEditable(!readOnly);
                statusText.setEnabled(!readOnly);
            }
        } catch (PersistenceException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            ExceptionHandler.process(e);
        }

        // Path:
        Label pathLab = new Label(parent, SWT.NONE);
        pathLab.setText(Messages.getString("PropertiesWizardPage.Path")); //$NON-NLS-1$

        Composite pathContainer = new Composite(parent, SWT.NONE);
        pathContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        GridLayout pathLayout = new GridLayout(2, false);
        pathLayout.marginHeight = 0;
        pathLayout.marginWidth = 0;
        pathLayout.horizontalSpacing = 0;
        pathContainer.setLayout(pathLayout);

        pathText = new Text(pathContainer, SWT.BORDER);
        pathText.setEnabled(false);
        pathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        if (editPath) {
            Button button = new Button(pathContainer, SWT.PUSH);
            button.setText(Messages.getString("PropertiesWizardPage.Select")); //$NON-NLS-1$

            button.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    openFolderSelectionDialog(NEED_CANCEL_BUTTON);
                }
            });

            if (destinationPath == null) {
                openFolderSelectionDialog(!NEED_CANCEL_BUTTON);
            }

        }

        // Added by Marvin Wang on Jan. 29, 2013.
        createBottomPart(parent);
    }

    /**
     * This method is used to create the Job type and Framework part for any properties wizard page which extends this
     * page. Added by hwang on Aug. 8, 2016.
     * 
     * @param parent
     */
    protected void createFrameworkPart(Composite parent) {
    }

    /**
     * This method is used to create the bottom part for any properties wizard page which extends this page. Added by
     * Marvin Wang on Jan. 29, 2013.
     * 
     * @param parent
     */
    protected void createBottomPart(Composite parent) {
    }

    /**
     * yzhang PropertiesWizardPage class global comment. Detailled comment <br/>
     * 
     * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
     * 
     */
    private class Folder {

        public static final String ROOT_FOLDER = FoldersContentProvider.DEFAULT;

        private String name;

        private final List<Folder> children;

        private Folder parent;

        public Folder(String name) {
            this.name = name;
            children = new ArrayList<Folder>();
        }

        /**
         * Getter for parent.
         * 
         * @return the parent
         */
        public Folder getParent() {
            return this.parent;
        }

        /**
         * Sets the parent.
         * 
         * @param parent the parent to set
         */
        public void setParent(Folder parent) {
            this.parent = parent;
        }

        /**
         * Getter for name.
         * 
         * @return the name
         */
        public String getName() {
            return this.name;
        }

        /**
         * Getter for path.
         * 
         * @return the path
         */
        public String getPath() {
            if (parent == null) {
                return null;
            }
            return parent.getName() + "/" + name; //$NON-NLS-1$
        }

        public String getFullPath() {
            if (getParent() == null) {
                return name;
            } else {
                return getParent().getFullPath() + "/" + name;
            }
        }

        /**
         * Sets the name.
         * 
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * yzhang Comment method "addChildFolder".
         * 
         * @param folder
         */
        public void addChildFolder(Folder folder) {
            folder.setParent(this);
            this.children.add(folder);
        }

        /**
         * yzhang Comment method "getChildren".
         * 
         * @return
         */
        public List<Folder> getChildren() {
            return this.children;
        }

    }

    /**
     * yzhang PropertiesWizardPage class global comment. Detailled comment <br/>
     * 
     * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
     * 
     */
    private class FoldersLabelProvider extends LabelProvider {

        private final RepositoryFolderSelectionDialog dialog;

        /**
         * yzhang PropertiesWizardPage.FoldersLabelProvider constructor comment.
         */
        public FoldersLabelProvider(RepositoryFolderSelectionDialog dialog) {
            this.dialog = dialog;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
         */
        @Override
        public Image getImage(Object element) {
            ECoreImage image = (dialog.getExpandedState(element) ? ECoreImage.FOLDER_OPEN_ICON : ECoreImage.FOLDER_CLOSE_ICON);
            return ImageProvider.getImage(image);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
         */
        @Override
        public String getText(Object element) {
            return ((Folder) element).getName();
        }

    }

    private Folder formdFolderTree(List<String> paths) {
        Folder root = new Folder(Folder.ROOT_FOLDER);
        for (String path : paths) {
            String[] splitedPaths = path.split("/"); //$NON-NLS-1$
            Folder parentFolder = root;
            for (int i = 0; i < splitedPaths.length; i++) {
                String parentLabel = root.getName();
                String folderLabel = splitedPaths[i];
                if (i > 0) {
                    parentLabel = splitedPaths[i - 1];
                    // find parent folder
                    Folder findParent = findParentFolder(root, parentLabel, i);
                    if (findParent != null) {
                        parentFolder = findParent;
                    }
                }
                boolean exist = false;
                for (Folder children : parentFolder.getChildren()) {
                    if (children.getName().equals(folderLabel)) {
                        exist = true;
                    }
                }
                if (!exist) {
                    // create one if not exist
                    parentFolder.addChildFolder(new Folder(folderLabel));
                }

            }
        }

        return root;
    }

    /**
     * yzhang Comment method "findFolder".
     * 
     * @param folder
     * @param name
     * @return
     */
    private Folder findParentFolder(Folder folder, String parentName, int parentDepth) {
        Folder toRreturn = null;
        String fullPath = folder.getFullPath();
        int depth = fullPath.split("/").length - 1;
        if (depth == parentDepth && folder.getName().equals(parentName)) {
            return folder;
        }
        if (depth <= parentDepth) {
            for (Folder f : folder.getChildren()) {
                toRreturn = findParentFolder(f, parentName, parentDepth);
            }
        }
        return toRreturn;
    }

    /**
     * Provides all user folders for a given type.<br/>
     * 
     * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
     * 
     */
    private class FoldersContentProvider implements ITreeContentProvider {

        private static final String DEFAULT = "(default)"; //$NON-NLS-1$

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
         */
        @Override
        public Object[] getElements(Object inputElement) {
            ERepositoryObjectType type = (ERepositoryObjectType) inputElement;
            IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
            try {
                List<String> folders = factory.getFolders(type);
                Collections.sort(folders);
                Folder root = formdFolderTree(folders);

                return new Folder[] { root };
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
                return new String[0];
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IContentProvider#dispose()
         */
        @Override
        public void dispose() {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
         * java.lang.Object, java.lang.Object)
         */
        @Override
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
         */
        @Override
        public Object[] getChildren(Object parentElement) {
            return ((Folder) parentElement).getChildren().toArray();
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
         */
        @Override
        public Object getParent(Object element) {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
         */
        @Override
        public boolean hasChildren(Object element) {
            return ((Folder) element).getChildren().size() > 0;
        }

    }

    protected void openFolderSelectionDialog(boolean needCancelButton) {

        // ListDialog dlg = new ListDialog(getShell());
        // dlg.setInput(getRepositoryObjectType());
        // dlg.setContentProvider(new FoldersContentProvider());
        // dlg.setLabelProvider(new LabelProvider());
        //        dlg.setTitle(Messages.getString("PropertiesWizardPage.SelectfolderTitle")); //$NON-NLS-1$
        //        dlg.setMessage(Messages.getString("PropertiesWizardPage.SelectfolderMessage")); //$NON-NLS-1$
        // if (!needCancelButton) {
        // dlg.setAddCancelButton(false);
        // }
        //        String defaultValue = (pathText.getText().equals("") ? FoldersContentProvider.DEFAULT : pathText.getText()); //$NON-NLS-1$
        // dlg.setInitialSelections(new String[] { defaultValue });
        //
        // if (dlg.open() == Window.OK) {
        // String string = (String) dlg.getResult()[0];
        // if (string.equals(FoldersContentProvider.DEFAULT)) {
        //                pathText.setText(""); //$NON-NLS-1$
        // } else {
        // pathText.setText(string);
        // this.path = string;
        // }
        // }

        RepositoryFolderSelectionDialog dialog = new RepositoryFolderSelectionDialog(getShell()) {

            @Override
            protected ViewerFilter[] getFilter() {
                return (new ViewerFilter[] { new ViewerFilter() {

                    @Override
                    public boolean select(Viewer viewer, Object parentElement, Object element) {
                        if (parentElement instanceof ERepositoryObjectType) {
                            ERepositoryObjectType type = (ERepositoryObjectType) parentElement;
                            IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
                            try {
                                folderItems = factory.getFolderItems(type);
                            } catch (PersistenceException e) {
                                e.printStackTrace();
                            }
                        }
                        if (element instanceof Folder) {
                            Folder folderElement = (Folder) element;
                            String folderFullPath = folderElement.getFullPath();
                            String defaultFolder = "(default)/";
                            if ("(default)".equals(folderFullPath)) {
                                return true;
                            }
                            if (folderFullPath.startsWith(defaultFolder)) {
                                folderFullPath = folderFullPath.substring(defaultFolder.length()).trim();
                            }
                            if (folderItems.containsKey(folderFullPath)) {
                                return !folderItems.get(folderFullPath);
                            }
                        }
                        return true;
                    }
                } });
            };
        };
        dialog.setInput(getRepositoryObjectType());
        dialog.setContentProvider(new FoldersContentProvider());
        dialog.setLabelProvider(new FoldersLabelProvider(dialog));
        dialog.setTreeListener(new ITreeViewerListener() {

            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
                setItemImage(event, true);
            }

            @Override
            public void treeExpanded(TreeExpansionEvent event) {
                setItemImage(event, false);
            }

            /**
             * yzhang Comment method "setItemImage".
             * 
             * @param event
             */
            private void setItemImage(TreeExpansionEvent event, boolean isCollpased) {
                Tree tree = ((TreeViewer) event.getSource()).getTree();
                Object element = event.getElement();
                TreeItem item = getTreeObject(tree, element);
                if (isCollpased) {
                    item.setImage(ImageProvider.getImage(ECoreImage.FOLDER_CLOSE_ICON));
                } else {
                    item.setImage(ImageProvider.getImage(ECoreImage.FOLDER_OPEN_ICON));
                }
            }
        });
        dialog.setTitle(Messages.getString("PropertiesWizardPage.SelectfolderTitle")); //$NON-NLS-1$
        dialog.setMessage(Messages.getString("PropertiesWizardPage.SelectfolderMessage")); //$NON-NLS-1$
        if (dialog.open() == Window.OK) {
            if (dialog.getResult().length > 0) {
                Folder folder = (Folder) dialog.getResult()[0];
                if (folder.getPath() == null) {
                    pathText.setText(""); //$NON-NLS-1$
                } else {
                    String pathString = ""; //$NON-NLS-1$
                    if (folder != null) {
                        pathString = getFolderPath(getParentsFolder(folder));// folder.getPath();
                    }
                    pathText.setText(pathString);
                    this.path = pathString;
                }
            }
        }
    }

    private List<Folder> getParentsFolder(Folder folder) {
        List<Folder> list = new ArrayList<Folder>();
        list.add(folder);
        if (folder.getParent() != null) {
            list.addAll(getParentsFolder(folder.getParent()));
        }
        return list;
    }

    private String getFolderPath(List<Folder> list) {
        String path = ""; //$NON-NLS-1$
        for (Folder folder : list) {
            path = File.separator + folder.getName() + path;
        }
        return path;
    }

    /**
     * yzhang Comment method "getTreeObject".
     * 
     * @param item
     * @param element
     * @return
     */
    private TreeItem getTreeObject(TreeItem item, Object element) {
        if (element.equals(item.getData())) {
            return item;
        }
        for (TreeItem treeItem : item.getItems()) {
            TreeItem toReturn = getTreeObject(treeItem, element);
            if (toReturn != null) {
                return toReturn;
            }
        }
        return null;
    }

    /**
     * yzhang Comment method "getTreeObject".
     * 
     * @param tree
     * @param objectToFind
     * @return
     */
    private TreeItem getTreeObject(Tree tree, Object objectToFind) {
        for (TreeItem item : tree.getItems()) {
            TreeItem toReturn = getTreeObject(item, objectToFind);
            if (toReturn != null) {
                return toReturn;
            }
        }
        return null;
    }

    public String[] toArray(List<org.talend.core.model.properties.Status> status) {
        String[] res = new String[status.size()];
        int i = 0;
        for (org.talend.core.model.properties.Status s : status) {
            res[i++] = s.getLabel();
        }
        return res;
    }

    protected void updateContent() {
        if (property != null) {
            nameText.setText(StringUtils.trimToEmpty(property.getDisplayName()));
            purposeText.setText(StringUtils.trimToEmpty(property.getPurpose()));
            descriptionText.setText(StringUtils.trimToEmpty(property.getDescription()));
            authorText.setText(StringUtils.trimToEmpty(property.getAuthor().getLogin()));
            lockerText.setText(""); //$NON-NLS-1$
            ProxyRepositoryFactory instance = ProxyRepositoryFactory.getInstance();
            Item item = property.getItem();
            if (instance.getStatus(item) == ERepositoryStatus.LOCK_BY_USER
                    || (instance.getStatus(item) == ERepositoryStatus.LOCK_BY_OTHER)) {
                LockInfo lockInfo = instance.getLockInfo(item);
                if (lockInfo != null) {
                    String locker = lockInfo.getUser();
                    if (locker != null) {
                        lockerText.setText(locker);
                    }
                }
            }
            if (allowVerchange) {
                versionText.setText(property.getVersion());
            }
            statusText.setText(statusHelper.getStatusLabel(property.getStatusCode()));
            if (destinationPath != null) {
                pathText.setText(destinationPath.toString());
            }
        }

        evaluateFields();
    }

    protected void addListeners() {
        nameText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!isSaveAs) {// it means create a new one and keep the old logic
                    // if (!update) {
                    if (nameText.getText().length() == 0) {
                        nameModifiedByUser = false;
                    } else {
                        nameModifiedByUser = true;
                    }
                    // }
                    evaluateTextField();
                } else {// means to go the SaveAS logic
                    if (nameText.getText().equalsIgnoreCase(orignalName)) {
                        nameStatus = createOkStatus(); // clean the last status
                        updatePageStatus();

                        if (versionText.getText().equalsIgnoreCase(orignalVersion)) {
                            setPageComplete(false); // can't save as
                        } else {
                            setPageComplete(true); // can save as
                        }

                        // assign value directly(because the old job name is valid).
                        if (property != null && nameStatus.getSeverity() == IStatus.OK) {
                            property.setDisplayName(StringUtils.trimToNull(nameText.getText()));
                            property.setLabel(getPropertyLabel(StringUtils.trimToNull(nameText.getText())));
                        }

                    } else {
                        // there won't plan to process this case:
                        // exist (Job AAA 1.0 and Job BBB 2.0), to save Job AAA 1.0 as Job BBB 3.0
                        nameModifiedByUser = true;
                        evaluateTextField();
                    }

                }
                if (property != null && nameStatus.getSeverity() == IStatus.OK && purposeText.getText().length() == 0) {
                    purposeStatus = createStatus(IStatus.WARNING, Messages.getString("PropertiesWizardPage.EmptyPurposeWarning")); //$NON-NLS-1$
                    updatePageStatus();
                }
            }
        });

        purposeText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (purposeText.getText().length() == 0) {
                    purposeStatus = createStatus(IStatus.WARNING, Messages.getString("PropertiesWizardPage.EmptyPurposeWarning")); //$NON-NLS-1$
                } else {
                    purposeStatus = createOkStatus();
                }
                if (property != null && purposeStatus.getSeverity() == IStatus.OK && descriptionText.getText().length() == 0) {
                    commentStatus = createStatus(IStatus.WARNING, Messages.getString("PropertiesWizardPage.EmptyDescWarning")); //$NON-NLS-1$
                }
                property.setPurpose(StringUtils.trimToNull(purposeText.getText()));
                updatePageStatus();
            }
        });

        descriptionText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (descriptionText.getText().length() == 0) {
                    commentStatus = createStatus(IStatus.WARNING, Messages.getString("PropertiesWizardPage.EmptyDescWarning")); //$NON-NLS-1$
                } else {
                    commentStatus = createOkStatus();
                }
                property.setDescription(StringUtils.trimToNull(descriptionText.getText()));
                updatePageStatus();
            }
        });

        if (allowVerchange) {
            versionMajorBtn.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    String version = property.getVersion();
                    if (lastVersionFound != null && VersionUtils.compareTo(lastVersionFound, version) > 0) {
                        version = lastVersionFound;
                    }
                    version = VersionUtils.upMajor(version);
                    versionText.setText(version);
                    property.setVersion(version);
                    updatePageStatus();
                }
            });

            versionMinorBtn.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    String version = property.getVersion();
                    if (lastVersionFound != null && VersionUtils.compareTo(lastVersionFound, version) > 0) {
                        version = lastVersionFound;
                    }
                    version = VersionUtils.upMinor(version);
                    versionText.setText(version);
                    property.setVersion(version);
                    updatePageStatus();
                }
            });
        }

        statusText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                property.setStatusCode(statusHelper.getStatusCode(statusText.getText()));
                updatePageStatus();
            }

        });

        // regConvertBtnListener();
        regListeners();
    }

    /**
     * Registers all listeners for wizard page which extends this page, child could override this method to add the
     * listener for widget. For example, it assumes that there is a button in child page, you need to add a listener for
     * this button. You just need to add what you want in this method. This method is invoked by
     * {@link PropertiesWizardPage#addListeners()} . Added by Marvin Wang on Jan 29, 2013.
     */
    protected void regListeners() {

    }

    protected void evaluateFields() {
        evaluateTextField();
    }

    protected void evaluateTextField() {
        if (readOnly) {
            return;
        }
        if (nameText == null || nameText.isDisposed()) {
            return;
        }
        evaluateName(nameText.getText());
        updatePageStatus();
    }

    @Override
    protected IStatus[] getStatuses() {
        return new IStatus[] { nameStatus, purposeStatus, commentStatus };
    }

    public IPath getPathForSaveAsGenericSchema() {
        if (this.path != null && path.length() > 0) {
            if (path.contains("(default)")) {//$NON-NLS-1$
                int index = path.indexOf(")");//$NON-NLS-1$
                if (path.length() > index + 1) {
                    path = path.substring(path.indexOf(")") + 1);//$NON-NLS-1$
                }
            }
            return new Path(path);
        }
        return null;

    }

    @Override
    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    /**
     * ftang Comment method "evaluateNameInRoutine".
     */
    protected void evaluateNameInRoutine() {
        if (nameText == null) {
            return;
        }
        String jobName = nameText.getText().trim();
        boolean isValid = isNameValidInRountine(jobName);

        if (!isValid) {
            nameStatus = createStatus(IStatus.ERROR, Messages.getString("PropertiesWizardPage.ItemExistsInRoutineError")); //$NON-NLS-1$
            updatePageStatus();
        }
    }

    /**
     * ftang Comment method "isNameExistingInRountine".
     * 
     * @param jobName
     */
    private boolean isNameValidInRountine(String jobName) {
        Property property = PropertiesFactory.eINSTANCE.createProperty();

        IProxyRepositoryFactory repositoryFactory = ProxyRepositoryFactory.getInstance();
        property.setId(repositoryFactory.getNextId());
        RoutineItem routineItem = PropertiesFactory.eINSTANCE.createRoutineItem();
        routineItem.setProperty(property);
        boolean isValid = false;
        try {
            isValid = repositoryFactory.isNameAvailable(property.getItem(), jobName);
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
            return false;
        }
        return isValid;
    }

    /**
     * ftang Comment method "evaluateNameInJob".
     */
    protected void evaluateNameInJob() {
        String jobName = nameText.getText().trim();
        boolean isValid = isNameValidInJob(jobName);

        if (!isValid) {
            nameStatus = createStatus(IStatus.ERROR, Messages.getString("PropertiesWizardPage.ItemExistsInJobError")); //$NON-NLS-1$
            updatePageStatus();
        }
    }

    /**
     * ftang Comment method "isNameExistingInJob".
     * 
     * @param jobName
     */
    private boolean isNameValidInJob(String jobName) {
        Property property = PropertiesFactory.eINSTANCE.createProperty();

        IRepositoryService repositoryService = (IRepositoryService) GlobalServiceRegister.getDefault().getService(
                IRepositoryService.class);
        IProxyRepositoryFactory repositoryFactory = repositoryService.getProxyRepositoryFactory();
        property.setId(repositoryFactory.getNextId());
        ProcessItem processItem = PropertiesFactory.eINSTANCE.createProcessItem();
        processItem.setProperty(property);
        boolean isValid = false;
        try {
            isValid = repositoryFactory.isNameAvailable(property.getItem(), jobName);
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
            return false;
        }
        return isValid;
    }

    public List<IRepositoryViewObject> getListExistingObjects() {
        return listExistingObjects;
    }

    /**
     * Getter for item.
     * 
     * @return the item
     */
    public Item getItem() {
        return this.item;
    }

    /**
     * Sets the item.
     * 
     * @param item the item to set
     */
    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * Getter for converter.
     * 
     * @return the converter
     */
    public IProcessConvertService getConverter() {
        return this.converter;
    }

    /**
     * Sets the converter.
     * 
     * @param converter the converter to set
     */
    public void setConverter(IProcessConvertService converter) {
        this.converter = converter;
    }

    protected List<IRepositoryViewObject> loadRepViewObjectWithOtherTypes(ERepositoryObjectType type) throws PersistenceException {
        List<IRepositoryViewObject> list = new ArrayList<IRepositoryViewObject>();
        if (type != null) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IProxyRepositoryService.class)) {
                IProxyRepositoryService service = (IProxyRepositoryService) GlobalServiceRegister.getDefault().getService(
                        IProxyRepositoryService.class);

                List<IRepositoryViewObject> mrList = service.getProxyRepositoryFactory().getAll(type, true, false);
                if (mrList != null && mrList.size() > 0) {
                    list.addAll(mrList);
                }
            }
        }
        return list;

    }

}
