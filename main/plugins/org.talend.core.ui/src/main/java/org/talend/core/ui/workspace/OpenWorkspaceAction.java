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
package org.talend.core.ui.workspace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.talend.core.model.general.ConnectionBean;
import org.talend.core.ui.i18n.Messages;
import org.talend.repository.ui.login.connections.ConnectionUserPerReader;

/**
 * Implements the open workspace action. Opens a dialog prompting for a directory and then restarts the IDE on that
 * workspace.
 * 
 * @since 3.0
 */
public class OpenWorkspaceAction extends Action implements ActionFactory.IWorkbenchAction {

    /**
     * Action responsible for opening the "Other..." dialog (ie: the workspace chooser).
     * 
     * @since 3.3
     * 
     */
    class OpenDialogAction extends Action {

        OpenDialogAction() {
            super(Messages.getString("WorkspaceMnu.choose.text"));//$NON-NLS-1$
            setToolTipText(Messages.getString("WorkspaceMnu.choose.tooltip"));//$NON-NLS-1$
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.action.Action#run()
         */
        @Override
        public void run() {
            OpenWorkspaceAction.this.run();
        }
    }

    /**
     * Action responsible for opening a specific workspace location
     * 
     * @since 3.3
     */
    class WorkspaceMRUAction extends Action {

        private ChooseWorkspaceData data;

        private String location;

        WorkspaceMRUAction(String location, ChooseWorkspaceData data) {
            this.location = location; // preserve the location directly -
            // setText mucks with accelerators so we
            // can't necessarily use it safely for
            // manipulating the location later.
            setText(location);
            setToolTipText(location);
            this.data = data;
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.action.Action#run()
         */
        @Override
        public void run() {
            data.workspaceSelected(location);
            data.writePersistedData();
            setCorrespondingConnectionAsDefault(location);
            restart(location);
        }
    }

    private static final String PROP_VM = "eclipse.vm"; //$NON-NLS-1$

    private static final String PROP_VMARGS = "eclipse.vmargs"; //$NON-NLS-1$

    private static final String PROP_COMMANDS = "eclipse.commands"; //$NON-NLS-1$

    private static final String PROP_EXIT_CODE = "eclipse.exitcode"; //$NON-NLS-1$

    private static final String PROP_EXIT_DATA = "eclipse.exitdata"; //$NON-NLS-1$

    private static final String CMD_DATA = "-data"; //$NON-NLS-1$

    private static final String CMD_VMARGS = "-vmargs"; //$NON-NLS-1$

    private static final String NEW_LINE = "\n"; //$NON-NLS-1$

    private IWorkbenchWindow window;

    /**
     * Set definition for this action and text so that it will be used for File -&gt; Open Workspace in the argument
     * window.
     * 
     * @param window the window in which this action should appear
     */
    public OpenWorkspaceAction(IWorkbenchWindow window) {
        super(Messages.getString("WorkspaceMnu.switch.text"), IAction.AS_DROP_DOWN_MENU); //$NON-NLS-1$

        if (window == null) {
            throw new IllegalArgumentException();
        }

        // TODO help?

        this.window = window;
        setToolTipText(Messages.getString("WorkspaceMnu.switch.tooltip")); //$NON-NLS-1$
        setActionDefinitionId("org.eclipse.ui.file.openWorkspace"); //$NON-NLS-1$
        setMenuCreator(new IMenuCreator() {

            private MenuManager dropDownMenuMgr;

            /**
             * Creates the menu manager for the drop-down.
             */
            private void createDropDownMenuMgr() {
                if (dropDownMenuMgr == null) {
                    dropDownMenuMgr = new MenuManager();
                    final ChooseWorkspaceData data = new ChooseWorkspaceData(Platform.getInstanceLocation().getURL());
                    data.readPersistedData();
                    filterUsefulWorkspaces(data);
                    String current = data.getInitialDefault();
                    String[] workspaces = data.getRecentWorkspaces();
                    for (int i = 0; i < workspaces.length; i++) {
                        if (workspaces[i] != null && !workspaces[i].equals(current)) {
                            dropDownMenuMgr.add(new WorkspaceMRUAction(workspaces[i], data));
                        }
                    }
                    if (!dropDownMenuMgr.isEmpty()) {
                        dropDownMenuMgr.add(new Separator());
                    }
                    dropDownMenuMgr.add(new OpenDialogAction());
                }
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.action.IMenuCreator#getMenu(org.eclipse.swt.widgets.Control)
             */
            @Override
            public Menu getMenu(Control parent) {
                createDropDownMenuMgr();
                return dropDownMenuMgr.createContextMenu(parent);
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.action.IMenuCreator#getMenu(org.eclipse.swt.widgets.Menu)
             */
            @Override
            public Menu getMenu(Menu parent) {
                createDropDownMenuMgr();
                Menu menu = new Menu(parent);
                IContributionItem[] items = dropDownMenuMgr.getItems();
                for (IContributionItem item : items) {
                    IContributionItem newItem = item;
                    if (item instanceof ActionContributionItem) {
                        newItem = new ActionContributionItem(((ActionContributionItem) item).getAction());
                    }
                    newItem.fill(menu, -1);
                }
                return menu;
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.action.IMenuCreator#dispose()
             */
            @Override
            public void dispose() {
                if (dropDownMenuMgr != null) {
                    dropDownMenuMgr.dispose();
                    dropDownMenuMgr = null;
                }
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        String path = promptForWorkspace();
        if (path == null) {
            return;
        }

        restart(path);
    }

    /**
     * Restart the workbench using the specified path as the workspace location.
     * 
     * @param path the location
     * @since 3.3
     */
    private void restart(String path) {
        String command_line = buildCommandLine(path);
        if (command_line == null) {
            return;
        }

        System.setProperty(PROP_EXIT_CODE, Integer.toString(24));
        System.setProperty(PROP_EXIT_DATA, command_line);
        window.getWorkbench().restart();
    }

    /**
     * Use the ChooseWorkspaceDialog to get the new workspace from the user.
     * 
     * @return a string naming the new workspace and null if cancel was selected
     */
    private String promptForWorkspace() {
        // get the current workspace as the default
        ChooseWorkspaceData data = new ChooseWorkspaceData(Platform.getInstanceLocation().getURL());
        ChooseWorkspaceDialog dialog = new ChooseWorkspaceDialog(window.getShell(), data, true, false);
        dialog.prompt(true);

        // return null if the user changed their mind
        String selection = data.getSelection();
        if (selection == null) {
            return null;
        }

        // otherwise store the new selection and return the selection
        data.writePersistedData();
        return selection;
    }

    /**
     * Create and return a string with command line options for eclipse.exe that will launch a new workbench that is the
     * same as the currently running one, but using the argument directory as its workspace.
     * 
     * @param workspace the directory to use as the new workspace
     * @return a string of command line options or null on error
     */
    private String buildCommandLine(String workspace) {
        String property = System.getProperty(PROP_VM);
        if (property == null) {
            MessageDialog.openError(window.getShell(), Messages.getString("WorkspaceMnu.restart.error.title"), //$NON-NLS-1$
                    Messages.getString("WorkspaceMnu.restart.error.message", PROP_VM)); //$NON-NLS-1$
            return null;
        }

        StringBuffer result = new StringBuffer(512);
        result.append(property);
        result.append(NEW_LINE);

        // append the vmargs and commands. Assume that these already end in \n
        String vmargs = System.getProperty(PROP_VMARGS);
        if (vmargs != null) {
            result.append(vmargs);
        }

        // append the rest of the args, replacing or adding -data as required
        property = System.getProperty(PROP_COMMANDS);
        if (property == null) {
            result.append(CMD_DATA);
            result.append(NEW_LINE);
            result.append(workspace);
            result.append(NEW_LINE);
        } else {
            // find the index of the arg to replace its value
            int cmd_data_pos = property.lastIndexOf(CMD_DATA);
            if (cmd_data_pos != -1) {
                cmd_data_pos += CMD_DATA.length() + 1;
                result.append(property.substring(0, cmd_data_pos));
                result.append(workspace);
                result.append(property.substring(property.indexOf('\n', cmd_data_pos)));
            } else {
                result.append(CMD_DATA);
                result.append(NEW_LINE);
                result.append(workspace);
                result.append(NEW_LINE);
                result.append(property);
            }
        }

        // put the vmargs back at the very end (the eclipse.commands property
        // already contains the -vm arg)
        if (vmargs != null) {
            result.append(CMD_VMARGS);
            result.append(NEW_LINE);
            result.append(vmargs);
        }

        return result.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#dispose()
     */
    @Override
    public void dispose() {
        window = null;
    }

    protected void setCorrespondingConnectionAsDefault(String selectedWorkspace) {
        if (selectedWorkspace == null || selectedWorkspace.isEmpty()) {
            return;
        }
        ConnectionUserPerReader connectionReader = ConnectionUserPerReader.getInstance();
        List<ConnectionBean> connections = connectionReader.readConnections();
        if (connections == null || connections.isEmpty()) {
            return;
        }
        Map<String, ConnectionBean> usefullWorkspaces = new HashMap<String, ConnectionBean>();
        for (ConnectionBean connBean : connections) {
            String workSpace = connBean.getWorkSpace();
            if (workSpace == null || workSpace.trim().isEmpty()) {
                continue;
            }
            usefullWorkspaces.put(workSpace, connBean);
        }
        if (usefullWorkspaces.isEmpty()) {
            return;
        }

        ConnectionBean connBean = usefullWorkspaces.get(selectedWorkspace);
        if (connBean == null) {
            return;
        }
        connectionReader.saveLastConnectionBean(connBean);
    }

    protected void filterUsefulWorkspaces(ChooseWorkspaceData data) {
        String[] recentWorkspaces = null;
        if (data == null || (recentWorkspaces = data.getRecentWorkspaces()) == null || recentWorkspaces.length == 0) {
            return;
        }
        List<ConnectionBean> connections = ConnectionUserPerReader.getInstance().readConnections();
        if (connections == null || connections.isEmpty()) {
            return;
        }
        Set<String> workspaceSet = new HashSet<String>();
        for (ConnectionBean connBean : connections) {
            String workspace = connBean.getWorkSpace();
            if (workspace == null || workspace.trim().isEmpty()) {
                continue;
            }
            workspaceSet.add(workspace);
        }
        if (workspaceSet.isEmpty()) {
            return;
        }

        List<String> recentWorkspacesList = new ArrayList<String>(Arrays.asList(recentWorkspaces));
        Iterator<String> iter = recentWorkspacesList.iterator();
        while (iter.hasNext()) {
            if (!workspaceSet.contains(iter.next())) {
                iter.remove();
            }
        }

        data.setRecentWorkspaces(recentWorkspacesList.toArray(new String[0]));
        data.writePersistedData();
    }
}
