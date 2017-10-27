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
package org.talend.repository.viewer.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.talend.commons.ui.swt.actions.ITreeContextualAction;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.connection.BRMSConnection;
import org.talend.core.model.metadata.builder.connection.CDCConnection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.EDIFACTConnection;
import org.talend.core.model.metadata.builder.connection.EbcdicConnection;
import org.talend.core.model.metadata.builder.connection.HL7Connection;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.metadata.builder.connection.impl.MetadataTableImpl;
import org.talend.core.model.metadata.builder.connection.impl.SAPBWTableImpl;
import org.talend.core.model.metadata.builder.connection.impl.SAPTableImpl;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryContentHandler;
import org.talend.core.model.repository.RepositoryContentManager;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.core.repository.model.repositoryObject.QueryEMFRepositoryNode;
import org.talend.core.repository.model.repositoryObject.SAPFunctionRepositoryObject;
import org.talend.core.repository.model.repositoryObject.SAPIDocRepositoryObject;
import org.talend.core.repository.model.repositoryObject.SalesforceModuleRepositoryObject;
import org.talend.core.runtime.services.IGenericWizardService;
import org.talend.repository.ProjectManager;
import org.talend.repository.i18n.Messages;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 *
 * $Id: RepositoryDoubleClickAction.java 77219 2012-01-24 01:14:15Z mhirt $
 *
 */
public class RepoDoubleClickAction extends Action {

    private List<ITreeContextualAction> contextualsActions = new ArrayList<ITreeContextualAction>();

    private StructuredViewer structuredViewer;

    public RepoDoubleClickAction(StructuredViewer structuredViewer, List<ITreeContextualAction> contextualsActionsList) {
        super();
        this.structuredViewer = structuredViewer;
        for (ITreeContextualAction current : contextualsActionsList) {
            if (current.isDoubleClickAction()) {
                contextualsActions.add(current);
            }
        }
    }

    public StructuredViewer getStructuredViewer() {
        return structuredViewer;
    }

    public void setStructuredViewer(StructuredViewer structuredViewer) {
        this.structuredViewer = structuredViewer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.jdt.ui.actions.SelectionDispatchAction#run(org.eclipse.jface.viewers.IStructuredSelection)
     */
    @Override
    public void run() {
        ISelection selection = structuredViewer.getSelection();
        Object obj = ((IStructuredSelection) selection).getFirstElement();
        if (obj == null || !(obj instanceof RepositoryNode)) {
            return;
        }

        RepositoryNode node = (RepositoryNode) obj;

        if (node.getObject() instanceof MetadataColumnRepositoryObject) {
            node = node.getParent().getParent();
        }

        // if ((node.getType() == ENodeType.SIMPLE_FOLDER || node.getType() == ENodeType.STABLE_SYSTEM_FOLDER ||
        // node.getType() == ENodeType.SYSTEM_FOLDER)
        // && !isLinkCDCNode(node)) {
        // view.expand(node);
        // view.getViewer().refresh();
        // if (isSERVICES(node)) {
        // ITreeContextualAction actionToRun = getAction(node);
        // if (!(actionToRun == null)) {
        // actionToRun.init((TreeViewer) getViewPart().getViewer(), (IStructuredSelection) selection);
        // actionToRun.run();
        // // showView();
        // }
        // }
        // } else {
        ITreeContextualAction actionToRun = getAction(node);
        if (actionToRun != null) {
            actionToRun.init(null, (IStructuredSelection) selection);
            actionToRun.run();
            // showView();
            // }
        }
    }

    /**
     *
     * ggu Comment method "isLinkCDCNode".
     *
     * for cdc
     */
    private boolean isLinkCDCNode(RepositoryNode node) {
        if (node != null) {
            if (ENodeType.STABLE_SYSTEM_FOLDER.equals(node.getType())) {
                if (node.getParent() != null) {
                    RepositoryNode pNode = node.getParent().getParent();
                    if (pNode != null) {
                        ERepositoryObjectType nodeType = (ERepositoryObjectType) pNode.getProperties(EProperties.CONTENT_TYPE);
                        if (ERepositoryObjectType.METADATA_CONNECTIONS.equals(nodeType) && pNode.getObject() != null
                                && pNode.getObject().getProperty().getItem() instanceof DatabaseConnectionItem) {
                            DatabaseConnection connection = (DatabaseConnection) ((DatabaseConnectionItem) pNode.getObject()
                                    .getProperty().getItem()).getConnection();
                            if (connection != null) {
                                CDCConnection cdcConns = connection.getCdcConns();
                                return cdcConns != null;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     *
     * ggu Comment method "isEBCDICTable".
     *
     * for ebcdic
     */
    private boolean isEBCDICTable(RepositoryNode theNode) {
        ERepositoryObjectType nodeType = (ERepositoryObjectType) theNode.getProperties(EProperties.CONTENT_TYPE);
        if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE) {
            RepositoryNode node = theNode.getParent();
            nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            if (nodeType == ERepositoryObjectType.METADATA_FILE_EBCDIC) {
                return true;
            }
        } else if (nodeType == ERepositoryObjectType.METADATA_CON_COLUMN) {
            RepositoryNode node = theNode.getParent().getParent().getParent();
            nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            if (nodeType == ERepositoryObjectType.METADATA_FILE_EBCDIC) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * hwang Comment method "isMDMTable".
     *
     * for mdm
     */
    private boolean isMDMTable(RepositoryNode theNode) {
        ERepositoryObjectType nodeType = (ERepositoryObjectType) theNode.getProperties(EProperties.CONTENT_TYPE);
        if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE) {
            RepositoryNode node = theNode.getParent();
            nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            if (nodeType == ERepositoryObjectType.METADATA_MDMCONNECTION) {
                return true;
            }
        } else if (nodeType == ERepositoryObjectType.METADATA_CON_COLUMN) {
            RepositoryNode node = theNode.getParent().getParent().getParent();
            nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            if (nodeType == ERepositoryObjectType.METADATA_MDMCONNECTION) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * hwang Comment method "isSAPTable".
     *
     * for sap
     */
    private boolean isSAPTable(RepositoryNode theNode) {
        ERepositoryObjectType nodeType = (ERepositoryObjectType) theNode.getProperties(EProperties.CONTENT_TYPE);
        if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE) {
            RepositoryNode node = theNode.getParent();
            nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            if (nodeType == ERepositoryObjectType.METADATA_SAP_TABLE) {
                return true;
            }
        } else if (nodeType == ERepositoryObjectType.METADATA_CON_COLUMN) {
            RepositoryNode node = theNode.getParent().getParent().getParent();
            nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            if (nodeType == ERepositoryObjectType.METADATA_SAP_TABLE) {
                return true;
            }
        }
        return false;
    }

    private boolean isSAPBapiTable(RepositoryNode theNode) {
        ERepositoryObjectType nodeType = (ERepositoryObjectType) theNode.getProperties(EProperties.CONTENT_TYPE);
        if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE) {
            RepositoryNode node = theNode.getParent();
            nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            if (nodeType == ERepositoryObjectType.METADATA_SAP_FUNCTION) {
                return true;
            }
        } else if (nodeType == ERepositoryObjectType.METADATA_CON_COLUMN) {
            RepositoryNode node = theNode.getParent().getParent().getParent();
            nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            if (nodeType == ERepositoryObjectType.METADATA_SAP_FUNCTION) {
                return true;
            }
        }
        return false;
    }

    private boolean isSAPBWTable(RepositoryNode theNode, ITreeContextualAction action) {
        ERepositoryObjectType nodeType = (ERepositoryObjectType) theNode.getProperties(EProperties.CONTENT_TYPE);
        RepositoryNode node = null;
        if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE) {
            node = theNode.getParent();
            nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
        } else if (nodeType == ERepositoryObjectType.METADATA_CON_COLUMN) {
            node = theNode.getParent().getParent().getParent();
            nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
        }
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        boolean readOnly = false;
        if (factory.isUserReadOnlyOnCurrentProject() || !ProjectManager.getInstance().isInCurrentMainProject(node)) {
            readOnly = true;
        }
        if ((action.isEditAction() && readOnly) || (action.isReadAction() && !readOnly)) {
            return false;
        }
        if (ERepositoryObjectType.METADATA_SAP_BW_DATASOURCE != null) {
            if (nodeType == ERepositoryObjectType.METADATA_SAP_BW_DATASOURCE
                    || nodeType == ERepositoryObjectType.METADATA_SAP_BW_DATASTOREOBJECT
                    || nodeType == ERepositoryObjectType.METADATA_SAP_BW_INFOCUBE
                    || nodeType == ERepositoryObjectType.METADATA_SAP_BW_INFOOBJECT) {
                return true;
            }
        }
        return false;
    }

    private boolean isHL7Table(RepositoryNode theNode) {
        ERepositoryObjectType nodeType = (ERepositoryObjectType) theNode.getProperties(EProperties.CONTENT_TYPE);
        if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE) {
            RepositoryNode node = theNode.getParent();
            nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            if (nodeType == ERepositoryObjectType.METADATA_FILE_HL7) {
                return true;
            }
        } else if (nodeType == ERepositoryObjectType.METADATA_CON_COLUMN) {
            RepositoryNode node = theNode.getParent().getParent().getParent();
            nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            if (nodeType == ERepositoryObjectType.METADATA_FILE_HL7) {
                return true;
            }
        }
        return false;
    }

    private boolean isEDIFACTTable(RepositoryNode theNode) {
        ERepositoryObjectType nodeType = (ERepositoryObjectType) theNode.getProperties(EProperties.CONTENT_TYPE);
        if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE) {
            RepositoryNode node = theNode.getParent();
            nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            if (nodeType == ERepositoryObjectType.METADATA_EDIFACT) {
                return true;
            }
        } else if (nodeType == ERepositoryObjectType.METADATA_CON_COLUMN) {
            RepositoryNode node = theNode.getParent().getParent().getParent();
            nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            if (nodeType == ERepositoryObjectType.METADATA_EDIFACT) {
                return true;
            }
        }
        return false;
    }

    private boolean isBRMSTable(RepositoryNode theNode) {
        ERepositoryObjectType nodeType = (ERepositoryObjectType) theNode.getProperties(EProperties.CONTENT_TYPE);
        if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE) {
            RepositoryNode node = theNode.getParent();
            nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            if (nodeType == ERepositoryObjectType.METADATA_FILE_BRMS) {
                return true;
            }
        }
        return false;
    }

    private ITreeContextualAction getAction(RepositoryNode obj) {
        final boolean isCDC = isLinkCDCNode(obj);
        final ERepositoryObjectType nodeType = (ERepositoryObjectType) obj.getProperties(EProperties.CONTENT_TYPE);

        for (ITreeContextualAction current : contextualsActions) {

            if (current.getClassForDoubleClick() == null) {
                return null;
            }

            // for cdc
            if (isCDC) {
                if (current.getClassForDoubleClick().equals(CDCConnection.class)) {
                    return current;
                }
                continue;
            }
            if (nodeType != null
                    && (nodeType.equals(ERepositoryObjectType.METADATA_CON_TABLE) || (nodeType
                            .equals(ERepositoryObjectType.METADATA_CON_COLUMN)))) {
                if (current.getClassForDoubleClick().equals(IMetadataTable.class)) {
                    return current;
                }
                // for ebcdic
                if (isEBCDICTable(obj) && current.getClassForDoubleClick().equals(EbcdicConnection.class)) {
                    return current;
                }

                if (isSAPTable(obj) && current.getClassForDoubleClick().equals(SAPTableImpl.class)) {
                    return current;
                }
                if (isSAPBWTable(obj, current) && current.getClassForDoubleClick().equals(SAPBWTableImpl.class)) {
                    return current;
                }

                if (isSAPBapiTable(obj) && current.getClassForDoubleClick().equals(MetadataTableImpl.class)) {
                    return current;
                }

                if (isMDMTable(obj) && current.getClassForDoubleClick().equals(MDMConnection.class)) {
                    return current;
                }

                if (isHL7Table(obj) && current.getClassForDoubleClick().equals(HL7Connection.class)) {
                    return current;
                }

                if (isEDIFACTTable(obj) && current.getClassForDoubleClick().equals(EDIFACTConnection.class)) {
                    return current;
                }

                if (isBRMSTable(obj) && current.getClassForDoubleClick().equals(BRMSConnection.class)) {
                    return current;
                }

                for (IRepositoryContentHandler handler : RepositoryContentManager.getHandlers()) {
                    if (handler.isOwnTable(obj, current.getClassForDoubleClick())) {
                        return current;
                    }
                }
                // Added for v2.0.0
            } else if (nodeType != null && nodeType.equals(ERepositoryObjectType.METADATA_CON_QUERY)) {
                if (current.getClassForDoubleClick().equals(QueryEMFRepositoryNode.class)) {
                    return current;
                }
            } else if (nodeType != null && nodeType.equals(ERepositoryObjectType.METADATA_CON_CDC)) {
                return null; // for cdc system table
                // end
            } else if (nodeType != null && nodeType.equals(ERepositoryObjectType.METADATA_SAP_FUNCTION)) {
                if (current.getClassForDoubleClick().equals(SAPFunctionRepositoryObject.class)
                        || current.getClassForDoubleClick().equals(SAPIDocRepositoryObject.class)) {
                    return current;
                }
            } else if (nodeType != null && nodeType.equals(ERepositoryObjectType.METADATA_SAP_IDOC)) {
                if (current.getClassForDoubleClick().equals(SAPIDocRepositoryObject.class)) {
                    return current;
                }

            } else if (nodeType != null && nodeType.equals(ERepositoryObjectType.METADATA_SALESFORCE_MODULE)) {
                if (current.getClassForDoubleClick().equals(SalesforceModuleRepositoryObject.class)) {
                    return current;
                }

            } else if (nodeType != null && nodeType.equals(ERepositoryObjectType.SERVICESOPERATION)) {
                if (current.getClassForDoubleClick().getSimpleName()
                        .equals(Messages.getString("RepoDoubleClickAction.ServiceOperation"))) { //$NON-NLS-1$
                    return current;
                }

            } else if (nodeType != null && obj.getContentType() != null
                    && obj.getContentType().equals(ERepositoryObjectType.PROCESS_ROUTE) && isDITestCaseEditOrReadAction(current)) {
                // TESB-17272: Cannot open route test case editor by double click
                continue;
            } else if (obj.getObject() != null
                    && current.getClassForDoubleClick().getSimpleName()
                            .equals(obj.getObject().getProperty().getItem().eClass().getName())) {
                return current;
            }else if (nodeType != null && nodeType.equals(ERepositoryObjectType.METADATA_CONNECTIONS)
                    && isDisguiseEdit(current, obj)) {
                return current;
            }
        }

        IGenericWizardService wizardService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericWizardService.class)) {
            wizardService = (IGenericWizardService) GlobalServiceRegister.getDefault().getService(IGenericWizardService.class);
        }
        if (wizardService != null) {
            ITreeContextualAction defaultAction = wizardService.getDefaultAction(obj);
            if (defaultAction != null) {
                return defaultAction;
            }
        }

        return null;
    }
    
    private boolean isDisguiseEdit(ITreeContextualAction current, RepositoryNode obj){
        if(obj.getType() != ENodeType.REPOSITORY_ELEMENT){
            return false;
        }
        if(current.getClassForDoubleClick().getSimpleName().equals("DatabaseConnectionItem")) {//$NON-NLS-1$
            return true;
        }
        return false;
    }

    private boolean isDITestCaseEditOrReadAction(ITreeContextualAction action) {

        return "org.talend.testcontainer.core.ui.actions.EditTestContainer".equals(action.getId())
                || "org.talend.testcontainer.core.ui.actions.ReadTestContainer".equals(action.getId());
    }

    // protected ISelection getSelection() {
    // IRepositoryView view = getViewPart();
    // if (view != null) {
    // return view.getViewer().getSelection();
    // }
    // IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    // if (window != null) {
    // ISelection selection = window.getSelectionService().getSelection();
    // return selection;
    // }
    // return null;
    // }
    //
    // protected IRepositoryView getViewPart() {
    // IWorkbenchPage page = getActivePage();
    // if (page != null) {
    // IWorkbenchPart part = page.getActivePart();
    // if (part != null && part instanceof IRepositoryView) {
    // return (IRepositoryView) part;
    // }
    // }
    // return null;
    // }
    //
    // protected IWorkbenchPage getActivePage() {
    // return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    // }
}
