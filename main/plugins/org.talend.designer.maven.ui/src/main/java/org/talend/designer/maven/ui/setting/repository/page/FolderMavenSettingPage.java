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
package org.talend.designer.maven.ui.setting.repository.page;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferencePageContainer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.projectsetting.AbstractProjectSettingPage;
import org.talend.core.runtime.projectsetting.IProjectSettingContainer;
import org.talend.core.runtime.projectsetting.ProjectPreferenceManager;
import org.talend.designer.maven.ui.i18n.Messages;
import org.talend.designer.maven.ui.setting.repository.RepositoryMavenSettingDialog;
import org.talend.designer.maven.ui.utils.DesignerMavenUiHelper;
import org.talend.repository.ProjectManager;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment
 */
@SuppressWarnings("rawtypes")
public abstract class FolderMavenSettingPage extends AbstractProjectSettingPage {

    private RepositoryNode node;

    protected static final String ID_MAVEN_PROJECT_SETTING = "projectsetting.Maven"; //$NON-NLS-1$

    private Link messageLink, noteLink;

    protected Button createBtn, deleteBtn;

    private Text pathTxt;

    private Composite pathComposite, noteComposite;

    private boolean readonly = false;

    private SelectionAdapter linkListener = new SelectionAdapter() {

        @Override
        public void widgetSelected(SelectionEvent e) {
            processLinkId(e);
        }
    };

    public FolderMavenSettingPage(RepositoryNode node) {
        super();
        this.node = node;

        IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
        if (factory.isUserReadOnlyOnCurrentProject() || !ProjectManager.getInstance().isInCurrentMainProject(this.getNode())) {
            readonly = true;
        }
        noDefaultAndApplyButton();
    }

    protected RepositoryNode getNode() {
        return node;
    }

    @Override
    protected IPreferenceStore doGetPreferenceStore() {
        // because no filename to set, so won't save and load it.
        return new PreferenceStore() {

            @Override
            public void load() throws IOException {
                // super.load();
            }

            @Override
            public void save() throws IOException {
                // super.save();
            }

        };
    }

    @Override
    protected void createFieldEditors() {

        Composite fieldEditorParent = getFieldEditorParent();
        fieldEditorParent.setLayout(new GridLayout());
        messageLink = new Link(fieldEditorParent, SWT.WRAP);
        messageLink.addSelectionListener(linkListener);

        pathComposite = createPathComposite(fieldEditorParent);
        // noteComposite = createNoteComposite(fieldEditorParent);

        createBtn = new Button(fieldEditorParent, SWT.PUSH);
        createBtn.setLayoutData(new GridData());
        createBtn.setText(Messages.getString("FolderMavenSettingPage_CreateButtonText"));//$NON-NLS-1$
        createBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                createMavenFiles();
            }

        });

        deleteBtn = new Button(fieldEditorParent, SWT.PUSH);
        deleteBtn.setLayoutData(new GridData());
        deleteBtn.setText(Messages.getString("FolderMavenSettingPage_DeleteButtonText"));//$NON-NLS-1$
        deleteBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                deleteMavenFiles();
            }

        });

        updateFields();
    }

    private Composite createPathComposite(Composite parent) {
        Composite composite = createLabelComposite(parent, Messages.getString("FolderMavenSettingPage_CreatingMavenSettingPath")); //$NON-NLS-1$

        pathTxt = new Text(composite, SWT.READ_ONLY);
        pathTxt.setBackground(composite.getBackground());
        pathTxt.setFont(parent.getFont());
        pathTxt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        return composite;
    }

    private Composite createNoteComposite(Composite parent) {
        Composite composite = createLabelComposite(parent, Messages.getString("FolderMavenSettingPage_CreatingMavenSettingNote")); //$NON-NLS-1$

        noteLink = new Link(composite, SWT.WRAP);
        noteLink.setFont(parent.getFont());
        noteLink.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        noteLink.addSelectionListener(linkListener);
        return composite;
    }

    protected void updateFields() {
        IFolder nodeFolder = DesignerMavenUiHelper.getNodeFolder(node);
        boolean created = checkMavenScriptsExisted(nodeFolder);

        messageLink.setText(createMessages(created));

        // path
        IPath projectRelativePath = nodeFolder.getProjectRelativePath();
        pathTxt.setText("<Project>/" + projectRelativePath.toString());//$NON-NLS-1$
        pathTxt.setToolTipText(projectRelativePath.toString());
        GridData pathTxtGridData = (GridData) pathTxt.getLayoutData();
        Point size = messageLink.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        pathTxtGridData.widthHint = size.x;

        // GridData pathCompGridData = (GridData) pathComposite.getLayoutData();
        // pathCompGridData.exclude = true; // hide path first.
        // pathComposite.setLayoutData(pathCompGridData);

        // // note
        //        String mvnProjectSettingLinkStr = "<a href=\"" + ID_MAVEN_PROJECT_SETTING + "\">Maven</a>";//$NON-NLS-1$ //$NON-NLS-2$
        //        noteLink.setText(Messages.getString("FolderMavenSettingPage_CreatingMavenSettingNoteMessage", //$NON-NLS-1$
        // mvnProjectSettingLinkStr));
        //
        // GridData noteCompGridData = (GridData) noteComposite.getLayoutData();
        // noteCompGridData.exclude = created;
        // noteComposite.setLayoutData(noteCompGridData);

        // FIXME, there are some problem for display, so don't hide it first.
        // createBtn.setEnabled(!readonly);
        // GridData createBtnGridData = (GridData) createBtn.getLayoutData();
        // createBtnGridData.exclude = created;
        //
        // deleteBtn.setEnabled(!readonly);
        // GridData deleteBtnGridData = (GridData) deleteBtn.getLayoutData();
        // deleteBtnGridData.exclude = !created;
        //
        // messageLink.getParent().layout();

        createBtn.setEnabled(!created && !readonly);
        deleteBtn.setEnabled(created && !readonly);

        deleteBtn.getParent().layout(true);
    }

    protected boolean checkMavenScriptsExisted(IFolder nodeFolder) {
        return DesignerMavenUiHelper.existMavenSetting(nodeFolder, getProcessFileNames());
    }

    protected abstract String createMessages(boolean created);

    protected String[] buildLinks(String... fileNames) {
        List<String> list = new ArrayList<String>();
        if (fileNames != null && fileNames.length > 0) {
            for (String name : fileNames) {
                list.add(buildLink(name));
            }
        }
        return list.toArray(new String[0]);
    }

    protected String buildLink(String fileWithExtension) {
        String name = fileWithExtension;
        String displayName = fileWithExtension;

        if (!DesignerMavenUiHelper.idWithExtension) {
            name = fileWithExtension.substring(0, fileWithExtension.lastIndexOf('.'));
        }

        if (!DesignerMavenUiHelper.displayWithExtension) {
            displayName = fileWithExtension.substring(0, fileWithExtension.lastIndexOf('.'));
        }
        return "<a href=\"" + name + '\"' + '>' //$NON-NLS-1$ 
                + displayName + "</a>";//$NON-NLS-1$ 
    }

    protected void processLinkId(SelectionEvent e) {
        String id = e.text;
        if (getDefaultProjectSettingId().equals(id)) {
            openProjectSettingDialog(id);
        }
        processLinkIdForFileNames(id, getProcessFileNames());
    }

    protected String getDefaultProjectSettingId() {
        return ID_MAVEN_PROJECT_SETTING;
    }

    protected void processLinkIdForFileNames(String id, String... fileNames) {
        if (fileNames != null) {
            for (String name : fileNames) {
                if (name.equals(id) || !DesignerMavenUiHelper.idWithExtension
                        && name.substring(0, name.lastIndexOf('.')).equals(id)) {
                    String childId = DesignerMavenUiHelper.buildRepositoryPreferenceNodeId(getPrefNodeId(), id);
                    openChildPage(childId);
                }
            }
        }
    }

    protected void openChildPage(String childId) {
        IPreferencePageContainer container = getContainer();
        if (container instanceof IWorkbenchPreferenceContainer) {
            ((IWorkbenchPreferenceContainer) container).openPage(childId, getNode());
        } else if (container instanceof IProjectSettingContainer) {
            ((IProjectSettingContainer) container).openPage(childId, getNode());
        }
    }

    protected void openProjectSettingDialog(String projectSettingId) {
        if (this.getContainer() instanceof IProjectSettingContainer
                && !(this.getContainer() instanceof RepositoryMavenSettingDialog)) {
            ((IProjectSettingContainer) this.getContainer()).openPage(projectSettingId, null);
        } else if (GlobalServiceRegister.getDefault().isServiceRegistered(IRepositoryService.class)) {
            IRepositoryService repositoryService = (IRepositoryService) GlobalServiceRegister.getDefault().getService(
                    IRepositoryService.class);
            repositoryService.openProjectSettingDialog(projectSettingId);
        }
    }

    protected abstract ProjectPreferenceManager getProjectSettingManager();

    protected Map<String, String> getProjectSettingKeyWithFileNamesMap() {
        return Collections.emptyMap();
    }

    protected String[] getProcessFileNames() {
        Map<String, String> projectSettingKeyWithFileNamesMap = getProjectSettingKeyWithFileNamesMap();
        return projectSettingKeyWithFileNamesMap.values().toArray(new String[0]);
    }

    protected Map<String, IFile> buildProjectSettingKeyWithFilesMap(IFolder nodeFolder) {
        Map<String, IFile> projectSettingKeyWithFilesMap = new LinkedHashMap<String, IFile>();

        Map<String, String> projectSettingKeyWithFileNamesMap = getProjectSettingKeyWithFileNamesMap();

        for (String projectSettingKey : projectSettingKeyWithFileNamesMap.keySet()) {
            String fileName = projectSettingKeyWithFileNamesMap.get(projectSettingKey);
            if (fileName != null) {
                IFile mavenFile = nodeFolder.getFile(fileName);
                projectSettingKeyWithFilesMap.put(projectSettingKey, mavenFile);
            }
        }
        return projectSettingKeyWithFilesMap;
    }

    protected void createMavenFiles() {
        processFiles(new RepositoryWorkUnit(createBtn.getText()) {

            @Override
            protected void run() throws LoginException, PersistenceException {
                final IFolder nodeFolder = DesignerMavenUiHelper.getNodeFolder(getNode());

                Map<String, IFile> buildProjectSettingKeyWithFilesMap = buildProjectSettingKeyWithFilesMap(nodeFolder);

                boolean success = true;
                for (String projectSettingKey : buildProjectSettingKeyWithFilesMap.keySet()) {
                    IFile mavenFile = buildProjectSettingKeyWithFilesMap.get(projectSettingKey);
                    boolean created = createFileFromProjectSetting(mavenFile, projectSettingKey);
                    if (!created) {
                        success = false;
                        break;
                    }
                }

                if (success) {
                    // update the tree view to add the new nodes
                    final Shell shell = FolderMavenSettingPage.this.getShell();
                    shell.getDisplay().syncExec(new Runnable() {

                        @Override
                        public void run() {
                            // have created,no need check again.
                            List<IPreferenceNode> childrenNodes = createMavenChildrenNodes(nodeFolder);

                            IPreferencePageContainer container = getContainer();
                            if (container instanceof IProjectSettingContainer) {
                                ((IProjectSettingContainer) container).addChildrenPreferenceNodes(getPrefNodeId(), childrenNodes);
                            }
                        }
                    });

                } else { // if failed, clean the created files.
                    for (String projectSettingKey : buildProjectSettingKeyWithFilesMap.keySet()) {
                        IFile mavenFile = buildProjectSettingKeyWithFilesMap.get(projectSettingKey);
                        File mFile = mavenFile.getLocation().toFile();
                        if (mFile.exists()) {
                            mFile.delete();
                        }
                    }
                    //
                    showErrorDialog(nodeFolder.getProjectRelativePath().toString());
                }

                // refresh
                try {
                    nodeFolder.refreshLocal(IResource.DEPTH_ONE, null);
                } catch (CoreException e) {
                    ExceptionHandler.process(e);
                }
            }

        });

    }

    protected boolean createFileFromProjectSetting(IFile targetFile, String projectSettingKey) {
        boolean ok = false;
        try {
            String pomContent = getProjectSettingManager().getValue(projectSettingKey);
            if (pomContent == null) { // use project setting always
                // pomContent =
                // MavenTemplateManager.getTemplateContent(MavenTemplateConstants.POM_JOB_TEMPLATE_FILE_NAME);
            }
            if (pomContent != null && pomContent.length() > 0) {
                writeContent(targetFile.getLocation().toFile(), pomContent);
                ok = true;
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return ok;
    }

    protected void writeContent(File file, String contents) throws IOException {
        FileWriter pomWriter = new FileWriter(file);
        pomWriter.write(contents);
        pomWriter.close();
    }

    /**
     * 
     * Will create the maven scripts nodes, it same as the RepositoryMavenSetting to create.
     */
    protected abstract List<IPreferenceNode> createMavenChildrenNodes(IFolder nodeFolder);

    protected void deleteMavenFiles() {
        processFiles(new RepositoryWorkUnit(deleteBtn.getText()) {

            @Override
            protected void run() throws LoginException, PersistenceException {
                //
                try {
                    final IFolder nodeFolder = DesignerMavenUiHelper.getNodeFolder(getNode());
                    final Map<String, IFile> buildProjectSettingKeyWithFilesMap = buildProjectSettingKeyWithFilesMap(nodeFolder);
                    // update the tree view to add the new nodes
                    final Shell shell = FolderMavenSettingPage.this.getShell();
                    shell.getDisplay().syncExec(new Runnable() {

                        @Override
                        public void run() {
                            List<String> childIds = new ArrayList<String>();

                            for (String projectSettingKey : buildProjectSettingKeyWithFilesMap.keySet()) {
                                IFile mavenFile = buildProjectSettingKeyWithFilesMap.get(projectSettingKey);
                                String nodeId = DesignerMavenUiHelper.buildRepositoryPreferenceNodeId(getPrefNodeId(), mavenFile);
                                childIds.add(nodeId);
                            }

                            IPreferencePageContainer container = getContainer();
                            if (container instanceof IProjectSettingContainer) {
                                ((IProjectSettingContainer) container).removeChildrenPreferenceNodes(getPrefNodeId(), childIds);
                            }
                        }
                    });

                    for (String projectSettingKey : buildProjectSettingKeyWithFilesMap.keySet()) {
                        IFile mavenFile = buildProjectSettingKeyWithFilesMap.get(projectSettingKey);
                        if (mavenFile.exists()) {
                            mavenFile.delete(true, null);
                        }
                    }

                    nodeFolder.refreshLocal(IResource.DEPTH_ONE, null);
                } catch (CoreException e) {
                    ExceptionHandler.process(e);
                }
            }

        });
    }

    protected void processFiles(final RepositoryWorkUnit rwu) {

        final IWorkspaceRunnable op = new IWorkspaceRunnable() {

            @Override
            public void run(IProgressMonitor monitor) throws CoreException {
                final Shell shell = FolderMavenSettingPage.this.getShell();
                shell.getDisplay().syncExec(new Runnable() {

                    @Override
                    public void run() {
                        // don't commit it yet for remote
                        // CoreRuntimePlugin.getInstance().getProxyRepositoryFactory().executeRepositoryWorkUnit(rwu);
                        rwu.executeRun();
                        updateFields();
                    }
                });
            };
        };
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        try {
            workspace.run(op, workspace.getRoot(), IWorkspace.AVOID_UPDATE, null);
        } catch (CoreException e) {
            ExceptionHandler.process(e);
        }
    }

    protected void showErrorDialog(final String addition) {
        final Shell shell = this.getShell();
        shell.getDisplay().syncExec(new Runnable() {

            @Override
            public void run() {
                MessageDialog.openError(shell, Messages.getString("FolderMavenSettingPage_CreatingMavenSettingErrorTitle"), //$NON-NLS-1$
                        Messages.getString("FolderMavenSettingPage_CreatingMavenSettingErrorMessage") //$NON-NLS-1$
                                + '\n' + addition);

            }
        });
    }
}
