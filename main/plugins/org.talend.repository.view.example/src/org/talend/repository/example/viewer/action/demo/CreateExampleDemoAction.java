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
package org.talend.repository.example.viewer.action.demo;

import java.util.List;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.MessageBoxExceptionHandler;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.runtime.image.OverlayImageProvider;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.ui.actions.metadata.AbstractCreateAction;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.repository.ProjectManager;
import org.talend.repository.example.image.EExampleDemoImage;
import org.talend.repository.example.model.demo.DemoFactory;
import org.talend.repository.example.model.demo.ExampleDemoConnection;
import org.talend.repository.example.model.demo.ExampleDemoConnectionItem;
import org.talend.repository.example.viewer.node.ExampleDemoRepositoryNodeType;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 * NOTE: this class is not finished, because need add some wizards or wizard pages also.
 */
@SuppressWarnings("nls")
public class CreateExampleDemoAction extends AbstractCreateAction {

    protected String EDIT_LABEL = "Edit Example Demo";

    protected String CREATE_LABEL = "Create Example Demo";

    protected String OPEN_LABEL = "Open Example Demo";

    protected static ImageDescriptor defaultImage = ImageProvider.getImageDesc(EExampleDemoImage.DEMO_ICON);

    protected static ImageDescriptor createImage = OverlayImageProvider.getImageWithNew(ImageProvider
            .getImage(EExampleDemoImage.DEMO_ICON));

    protected boolean creation = false;

    /**
     * DOC ggu CreateDemoAction constructor comment.
     */
    public CreateExampleDemoAction() {
        this.setText(CREATE_LABEL);
        this.setToolTipText(CREATE_LABEL);
        this.setImageDescriptor(defaultImage);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.repository.ui.actions.metadata.AbstractCreateAction#init(org.talend.repository.model.RepositoryNode
     * )
     */
    @Override
    protected void init(RepositoryNode node) {
        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
        if (!getRepoObjectType().equals(nodeType)) {
            return;
        }

        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        switch (node.getType()) {
        case SIMPLE_FOLDER:
            if (node.getObject() != null && node.getObject().getProperty().getItem().getState().isDeleted()) {
                setEnabled(false);
                return;
            }
            break;
        case SYSTEM_FOLDER:
            if (factory.isUserReadOnlyOnCurrentProject() || !ProjectManager.getInstance().isInCurrentMainProject(node)) {
                setEnabled(false);
                return;
            }
            this.setText(CREATE_LABEL);
            collectChildNames(node);
            this.setImageDescriptor(createImage);
            creation = true;
            break;
        case REPOSITORY_ELEMENT:
            if (factory.isPotentiallyEditable(node.getObject())) {
                this.setText(EDIT_LABEL);
                this.setImageDescriptor(defaultImage);
                collectSiblingNames(node);
            } else {
                this.setText(OPEN_LABEL);
                this.setImageDescriptor(defaultImage);
            }
            collectSiblingNames(node);
            creation = false;
            break;
        default:
            return;
        }
        setEnabled(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.AContextualAction#doRun()
     */
    @Override
    protected void doRun() {
        if (repositoryNode == null) {
            repositoryNode = getCurrentRepositoryNode();
        }
        if (repositoryNode == null) {
            return;
        }
        /*
         * Should open the ExampleDemoWizard here.
         * 
         * just do simple to test.
         */

        if (creation) {
            IWorkspaceRunnable runnable = new IWorkspaceRunnable() {

                @Override
                public void run(final IProgressMonitor monitor) throws CoreException {
                    IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
                    int index = 0;
                    try {
                        List<IRepositoryViewObject> demoObjects = factory.getAll(getRepoObjectType(), true);
                        for (IRepositoryViewObject object : demoObjects) {
                            Item item = object.getProperty().getItem();
                            if (item instanceof ExampleDemoConnectionItem) {
                                String type = ((ExampleDemoConnection) ((ExampleDemoConnectionItem) item).getConnection())
                                        .getType();
                                try {
                                    int num = Integer.parseInt(type);
                                    if (num > index) {
                                        index = num;
                                    }
                                } catch (NumberFormatException e) {
                                    //
                                }
                            }
                        }
                    } catch (PersistenceException e) {
                        ExceptionHandler.process(e);
                    }
                    index++;

                    // find the path
                    IPath pathToSave = new Path("");//$NON-NLS-1$
                    switch (repositoryNode.getType()) {
                    case SIMPLE_FOLDER:
                    case REPOSITORY_ELEMENT:
                        pathToSave = RepositoryNodeUtilities.getPath(repositoryNode);
                        break;
                    case SYSTEM_FOLDER:
                        pathToSave = new Path(""); //$NON-NLS-1$
                        break;
                    }

                    // create the items
                    Property connectionProperty = PropertiesFactory.eINSTANCE.createProperty();
                    connectionProperty.setLabel(getBaseLabel() + index);
                    connectionProperty.setAuthor(((RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                            .getProperty(Context.REPOSITORY_CONTEXT_KEY)).getUser());
                    connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
                    connectionProperty.setId(factory.getNextId());

                    ExampleDemoConnectionItem demoItem = createDemoItem();
                    demoItem.setProperty(connectionProperty);

                    ExampleDemoConnection demoConn = DemoFactory.eINSTANCE.createExampleDemoConnection();
                    demoItem.setConnection(demoConn);

                    demoConn.setName(getRepoObjectType().getType());
                    demoConn.setType(Integer.toString(index));
                    demoConn.setValid(true);

                    // set the label same as displayName
                    demoItem.getProperty().setLabel(demoItem.getProperty().getDisplayName());

                    // create item
                    try {
                        factory.create(demoItem, pathToSave);
                        displayDetails(demoItem, "Create successfully");
                    } catch (PersistenceException e) {
                        ExceptionHandler.process(e);
                    }
                }
            };
            IWorkspace workspace = ResourcesPlugin.getWorkspace();
            try {
                workspace.run(runnable, workspace.getRoot(), IWorkspace.AVOID_UPDATE, null);
            } catch (CoreException e) {
                MessageBoxExceptionHandler.process(e.getCause());
            }
        } else {
            if (repositoryNode.getType() == ENodeType.REPOSITORY_ELEMENT) {
                IRepositoryViewObject object = repositoryNode.getObject();
                if (object != null) {
                    Item item = object.getProperty().getItem();
                    if (item instanceof ExampleDemoConnectionItem) {
                        displayDetails((ExampleDemoConnectionItem) item, "Demo Item details");
                    }
                }
            }
        }

    }

    private void displayDetails(final ExampleDemoConnectionItem demoItem, final String title) {
        this.getWorkbench().getDisplay().syncExec(new Runnable() {

            @Override
            public void run() {
                ExampleDemoConnection demoConn = (ExampleDemoConnection) demoItem.getConnection();
                MessageDialog.openInformation(getWorkbench().getDisplay().getActiveShell(), title, "Name: "
                        + demoItem.getProperty().getLabel() + "\nDisplayName: " + demoItem.getProperty().getDisplayName()
                        + "\nType: " + demoConn.getType() + "\nValid: " + demoConn.isValid());
            }

        });

    }

    protected ERepositoryObjectType getRepoObjectType() {
        return ExampleDemoRepositoryNodeType.repositoryExampleDemoType;
    }

    protected ExampleDemoConnectionItem createDemoItem() {
        return DemoFactory.eINSTANCE.createExampleDemoConnectionItem();
    }

    protected String getBaseLabel() {
        return "DemoItem";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.actions.AContextualAction#getClassForDoubleClick()
     */
    @Override
    public Class getClassForDoubleClick() {
        return ExampleDemoConnectionItem.class;
    }

}
