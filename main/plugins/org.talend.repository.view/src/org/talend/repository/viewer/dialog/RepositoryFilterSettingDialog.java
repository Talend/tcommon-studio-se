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
package org.talend.repository.viewer.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.INavigatorContentExtension;
import org.eclipse.ui.navigator.INavigatorContentService;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.Status;
import org.talend.core.model.properties.User;
import org.talend.core.model.repository.IRepositoryPrefConstants;
import org.talend.core.service.IMRProcessService;
import org.talend.repository.ProjectManager;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.nodes.IProjectRepositoryNode;
import org.talend.repository.viewer.filter.RepositoryNodeFilterHelper;
import org.talend.repository.viewer.ui.provider.RepoCommonViewerProvider;
import org.talend.repository.viewer.ui.viewer.CheckboxRepoCommonViewer;

/**
 * DOC ggu class global comment. Detailled comment
 */
@SuppressWarnings({ "rawtypes" })
public class RepositoryFilterSettingDialog extends Dialog {

    private static final int WIDTH = 600;

    private static final int HEIGHT = 500;

    private final ICommonActionExtensionSite actionSite;

    private final boolean activedFilter;

    RepoCommonViewerProvider repoProvider;

    private CheckboxRepoCommonViewer checkboxCommonViewer;

    private final boolean isPerspectiveFilter;

    private Table statusTable;

    private Table userTable;

    private Text userFilterPattern;

    private Label patternInfo;

    private Button enableUserPatternBtn;

    private Button allUsersBtn;

    private Set<String> uncheckedStatus = new HashSet<String>();

    private Set<String> uncheckedUser = new HashSet<String>();

    public RepositoryFilterSettingDialog(final ICommonActionExtensionSite actionSite, final boolean activedFilter,
            boolean isPerspectiveFilter) {
        super(actionSite.getViewSite().getShell());
        setShellStyle(getShellStyle() | SWT.MAX | SWT.MIN | SWT.RESIZE | SWT.APPLICATION_MODAL);
        this.actionSite = actionSite;
        this.activedFilter = activedFilter;
        this.isPerspectiveFilter = isPerspectiveFilter;
    }

    protected boolean isActivedFilter() {
        return activedFilter;
    }

    protected boolean isPerspectiveFilter() {
        return isPerspectiveFilter;
    }

    protected INavigatorContentService getNavigatorContentService() {
        return this.actionSite.getContentService();
    }

    protected CommonViewer getCommonViewer() {
        return (CommonViewer) this.actionSite.getStructuredViewer();
    }

    protected CommonNavigator getCommonNavigator() {
        return getCommonViewer().getCommonNavigator();
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.getString("RepositoryFilterSettingDialog.FilterSetting")); //$NON-NLS-1$
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        SashForm sash = new SashForm(parent, SWT.HORIZONTAL | SWT.SMOOTH);
        sash.setLayoutData(new GridData(GridData.FILL_BOTH));
        GridLayout layout = new GridLayout();
        layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
        layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
        layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
        layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
        // layout.numColumns = 2;
        sash.setLayout(layout);
        GridData layoutData = new GridData(GridData.FILL_BOTH);
        layoutData.widthHint = WIDTH;
        layoutData.heightHint = HEIGHT;
        sash.setLayoutData(layoutData);
        applyDialogFont(sash);

        Composite leftPart = new Composite(sash, SWT.NONE);
        leftPart.setLayout(new GridLayout());
        leftPart.setLayoutData(new GridData(GridData.FILL_BOTH));
        applyDialogFont(leftPart);
        createLeftContent(leftPart);

        Composite rightPart = new Composite(sash, SWT.NONE);
        rightPart.setLayout(new GridLayout());
        rightPart.setLayoutData(new GridData(GridData.FILL_BOTH));
        applyDialogFont(rightPart);
        createRightContent(rightPart);
        addListeners();
        sash.setWeights(new int[] { 1, 1 });
        return sash;
    }

    private void createLeftContent(Composite parent) {
        Label extensionsInstructionLabel = new Label(parent, SWT.BOLD | SWT.WRAP);
        extensionsInstructionLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL
                | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
        extensionsInstructionLabel.setFont(parent.getFont());
        extensionsInstructionLabel.setText("");

        repoProvider = RepoCommonViewerProvider.CHECKBOX;
        if (actionSite != null && actionSite.getViewSite() != null) {
            String baseViewId = actionSite.getViewSite().getId();
            repoProvider.setViewId(baseViewId);
        }

        checkboxCommonViewer = (CheckboxRepoCommonViewer) repoProvider.createViewer(parent);
        checkboxCommonViewer.addFilter(new ViewerFilter() {

            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                if (element instanceof RepositoryNode) {
                    RepositoryNode parent2 = ((RepositoryNode) element).getParent();
                    if (parent2 instanceof IProjectRepositoryNode) { // only root type of node, need improve
                        return true;
                    }
                }
                return false;
            }
        });
        checkboxCommonViewer.expandAll();

        restoreTreeItems();
    }

    private void createRightContent(Composite parent) {
        final IPreferenceStore preferenceStore = RepositoryNodeFilterHelper.getPreferenceStore();
        Project currentProject = ProjectManager.getInstance().getCurrentProject();
        org.talend.core.model.properties.Project emfProject = currentProject.getEmfProject();
        Resource resource = emfProject.eResource();
        Collection<User> users = new ArrayList<User>();
        if (resource != null) {
            users = EcoreUtil.getObjectsByType(resource.getContents(), PropertiesPackage.eINSTANCE.getUser());
        }
        // if resource is null, add current user
        if (users.size() == 0) {
            users.add(emfProject.getAuthor());
        }
        // filter by Name
        Composite userPatternComp = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        userPatternComp.setLayout(layout);
        enableUserPatternBtn = new Button(userPatternComp, SWT.CHECK);
        enableUserPatternBtn.setText(Messages.getString("RepositoryFilterSettingDialog.FilterByName")); //$NON-NLS-1$

        boolean enabled = preferenceStore.getBoolean(IRepositoryPrefConstants.TAG_USER_DEFINED_PATTERNS_ENABLED);
        enableUserPatternBtn.setSelection(enabled);

        String patterns = preferenceStore.getString(IRepositoryPrefConstants.FILTER_BY_NAME);
        userFilterPattern = new Text(parent, SWT.BORDER);
        userFilterPattern.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        userFilterPattern.setEnabled(enabled);
        if (patterns != null) {
            userFilterPattern.setText(patterns);
        }

        patternInfo = new Label(parent, SWT.NONE);
        patternInfo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        patternInfo.setText(Messages.getString("RepositoryFilterSettingDialog.PatternInfo")); //$NON-NLS-1$
        patternInfo.setEnabled(enabled);

        // filter by user
        Composite userTop = new Composite(parent, SWT.NONE);
        userTop.setLayout(new GridLayout(2, false));
        userTop.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        Label filterByUser = new Label(userTop, SWT.NONE);
        filterByUser.setText(Messages.getString("RepositoryFilterSettingDialog.FilterByUser")); //$NON-NLS-1$

        allUsersBtn = new Button(userTop, SWT.CHECK | SWT.LEFT);
        allUsersBtn.setText(Messages.getString("RepositoryFilterSettingDialog.AllUsers")); //$NON-NLS-1$
        boolean userTableEnable = preferenceStore.getBoolean(IRepositoryPrefConstants.USER_FILTER_TABLE_ENABLED);
        allUsersBtn.setSelection(!userTableEnable);

        userTable = new Table(parent, SWT.BORDER | SWT.CHECK);
        userTable.setLayoutData(new GridData(GridData.FILL_BOTH));
        userTable.setHeaderVisible(true);
        userTable.setLinesVisible(true);
        userTable.setEnabled(!allUsersBtn.getSelection());

        TableColumn login = new TableColumn(userTable, SWT.NONE);
        login.setWidth(100);
        login.setText(Messages.getString("RepositoryFilterSettingDialog.Login")); //$NON-NLS-1$

        TableColumn firstName = new TableColumn(userTable, SWT.NONE);
        firstName.setWidth(100);
        firstName.setText(Messages.getString("RepositoryFilterSettingDialog.FirstName")); //$NON-NLS-1$

        TableColumn lastName = new TableColumn(userTable, SWT.NONE);
        lastName.setWidth(100);
        lastName.setText(Messages.getString("RepositoryFilterSettingDialog.LastName")); //$NON-NLS-1$

        String[] filtersByPreferenceKey = RepositoryNodeFilterHelper
                .getFiltersByPreferenceKey(IRepositoryPrefConstants.FILTER_BY_USER);
        if (filtersByPreferenceKey != null && filtersByPreferenceKey.length > 0) {
            uncheckedUser.addAll(Arrays.asList(filtersByPreferenceKey));
        }
        for (User user : users) {
            TableItem item = new TableItem(userTable, SWT.NONE);
            item.setText(0, user.getLogin());
            item.setText(1, user.getFirstName() == null ? "" : user.getFirstName()); //$NON-NLS-1$
            item.setText(2, user.getLastName() == null ? "" : user.getLastName()); //$NON-NLS-1$
            if (!uncheckedUser.contains(user.getLogin())) {
                item.setChecked(true);
            }
        }

        // filter by status
        Label filterByStatus = new Label(parent, SWT.NONE);
        filterByStatus.setText(Messages.getString("RepositoryFilterSettingDialog.FilterByStatus")); //$NON-NLS-1$
        EList technicalStatus = emfProject.getTechnicalStatus();
        statusTable = new Table(parent, SWT.BORDER | SWT.CHECK);
        statusTable.setLayoutData(new GridData(GridData.FILL_BOTH));
        statusTable.setHeaderVisible(true);
        statusTable.setLinesVisible(true);

        TableColumn code = new TableColumn(statusTable, SWT.NONE);
        code.setWidth(100);
        code.setText(Messages.getString("RepositoryFilterSettingDialog.Code")); //$NON-NLS-1$

        TableColumn lable = new TableColumn(statusTable, SWT.NONE);
        lable.setWidth(100);
        lable.setText(Messages.getString("RepositoryFilterSettingDialog.Label")); //$NON-NLS-1$
        String[] filters = RepositoryNodeFilterHelper.getFiltersByPreferenceKey(IRepositoryPrefConstants.FILTER_BY_STATUS);
        if (filters != null && filters.length > 0) {
            uncheckedStatus.addAll(Arrays.asList(filters));
        }
        for (Object o : technicalStatus) {
            Status status = (Status) o;
            TableItem item = new TableItem(statusTable, SWT.NONE);
            item.setText(0, status.getCode());
            item.setText(1, status.getLabel());
            if (!uncheckedStatus.contains(status.getCode())) {
                item.setChecked(true);
            }

        }
        TableItem item = new TableItem(statusTable, SWT.NONE);
        item.setText(1, Messages.getString("RepositoryFilterSettingDialog.Item")); //$NON-NLS-1$
        item.setData(RepositoryConstants.NOT_SET_STATUS, RepositoryConstants.NOT_SET_STATUS);
        if (!uncheckedStatus.contains(RepositoryConstants.NOT_SET_STATUS)) {
            item.setChecked(true);
        }
    }

    private void addListeners() {
        statusTable.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (e.detail == SWT.CHECK) {
                    TableItem item = (TableItem) e.item;
                    String text = item.getText(0);
                    if (text == null || "".equals(text)) { //$NON-NLS-1$
                        Object data = item.getData(RepositoryConstants.NOT_SET_STATUS);
                        if (data != null && RepositoryConstants.NOT_SET_STATUS.equals(data)) {
                            text = data.toString();
                        }
                    }
                    if (!item.getChecked()) {
                        uncheckedStatus.add(text);
                    } else {
                        uncheckedStatus.remove(text);
                    }
                }
            }

        });

        userTable.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (e.detail == SWT.CHECK) {
                    TableItem item = (TableItem) e.item;
                    if (!item.getChecked()) {
                        uncheckedUser.add(item.getText(0));
                    } else {
                        uncheckedUser.remove(item.getText(0));
                    }
                }
            }
        });
        enableUserPatternBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {

                boolean selection = enableUserPatternBtn.getSelection();
                patternInfo.setEnabled(selection);
                userFilterPattern.setEnabled(selection);
                if (selection) {
                    userFilterPattern.setFocus();
                }
            }

        });

        allUsersBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                userTable.setEnabled(!allUsersBtn.getSelection());
                if (allUsersBtn.getSelection()) {
                    uncheckedUser.clear();
                }
            }

        });
    }

    private void restoreTreeItems() {
        String[] filteredContents = RepositoryNodeFilterHelper.getFilterByNodeValues();
        List<String> filteredList = new ArrayList<String>();
        if (filteredContents != null) {
            filteredList = Arrays.asList(filteredContents);
        }

        INavigatorContentService navigatorContentService = checkboxCommonViewer.getNavigatorContentService();
        restoreContents(filteredList, checkboxCommonViewer.getTree().getItems(), navigatorContentService);
    }

    private void restoreContents(List<String> filteredContents, TreeItem[] items, INavigatorContentService navigatorContentService) {
        if (items != null) {
            for (TreeItem item : items) {
                Object data = item.getData();

                boolean found = false;
                Set contentExtensions = navigatorContentService.findRootContentExtensions(data);
                for (Iterator itr = contentExtensions.iterator(); itr.hasNext();) {
                    if (filteredContents.contains(((INavigatorContentExtension) itr.next()).getId())) {
                        found = true;
                        break;
                    }
                }
                // for Standard Jobs
                if (!found && filteredContents.contains(data.getClass().getName())) {
                    found = true;
                }

                item.setChecked(!found);
                restoreContents(filteredContents, item.getItems(), navigatorContentService);
            }
        }
    }

    private List<String> getFilteredContents() {
        List<String> filteredContents = new ArrayList<String>();
        INavigatorContentService navigatorContentService = checkboxCommonViewer.getNavigatorContentService();
        collectContents(filteredContents, checkboxCommonViewer.getTree().getItems(), navigatorContentService);
        return filteredContents;
    }

    private void collectContents(List<String> filteredContents, TreeItem[] items, INavigatorContentService navigatorContentService) {
        if (items != null) {
            for (TreeItem item : items) {
                Object data = item.getData();
                if (!item.getChecked()) {
                    // for Standard Jobs
                    boolean isStandardJobsNode = false;
                    if (GlobalServiceRegister.getDefault().isServiceRegistered(IMRProcessService.class)) {
                        IMRProcessService service = (IMRProcessService) GlobalServiceRegister.getDefault().getService(
                                IMRProcessService.class);
                        isStandardJobsNode = service.collectStandardProcessNode(filteredContents, data);

                    }
                    if (!isStandardJobsNode) {
                        Set contentExtensions = navigatorContentService.findRootContentExtensions(data);
                        for (Iterator itr = contentExtensions.iterator(); itr.hasNext();) {
                            filteredContents.add(((INavigatorContentExtension) itr.next()).getId());
                        }
                    }
                }
                collectContents(filteredContents, item.getItems(), navigatorContentService);

            }
        }
    }

    @Override
    protected void okPressed() {
        IPreferenceStore preferenceStore = RepositoryNodeFilterHelper.getPreferenceStore();
        final List<String> filteredContents = getFilteredContents();
        String relust = convertToString(filteredContents, RepositoryNodeFilterHelper.ITEM_SEPARATOR);

        final String filterByNodeKey = RepositoryNodeFilterHelper.getFilterByNodeKey(
                RepositoryNodeFilterHelper.getPerspectiveId(), isPerspectiveFilter());
        preferenceStore.setValue(filterByNodeKey, relust.length() > 2 ? relust.substring(0, relust.length() - 2) : relust);

        String status = convertToString(uncheckedStatus, RepositoryNodeFilterHelper.ITEM_SEPARATOR);
        preferenceStore.setValue(IRepositoryPrefConstants.FILTER_BY_STATUS,
                status.length() > 2 ? status.substring(0, status.length() - 2) : status);

        String users = convertToString(uncheckedUser, RepositoryNodeFilterHelper.ITEM_SEPARATOR);
        preferenceStore.setValue(IRepositoryPrefConstants.FILTER_BY_USER,
                users.length() > 2 ? users.substring(0, users.length() - 2) : users);

        boolean canUserFilterEnable = this.userFilterPattern.getText() != null && !"".equals(this.userFilterPattern.getText()); //$NON-NLS-1$
        preferenceStore.setValue(IRepositoryPrefConstants.FILTER_BY_NAME, this.userFilterPattern.getText());
        preferenceStore.setValue(IRepositoryPrefConstants.TAG_USER_DEFINED_PATTERNS_ENABLED,
                this.enableUserPatternBtn.getSelection() && canUserFilterEnable);

        preferenceStore.setValue(IRepositoryPrefConstants.USER_FILTER_TABLE_ENABLED, !allUsersBtn.getSelection());
        super.okPressed();

    }

    private String convertToString(Collection<String> connection, String separator) {
        StringBuffer buffer = new StringBuffer();
        for (String item : connection) {
            buffer.append(item);
            buffer.append(separator);
        }
        return buffer.toString();
    }

}
