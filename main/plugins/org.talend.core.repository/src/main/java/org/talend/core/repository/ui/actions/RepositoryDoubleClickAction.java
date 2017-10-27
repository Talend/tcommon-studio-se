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
package org.talend.core.repository.ui.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.swt.actions.ITreeContextualAction;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.connection.BRMSConnection;
import org.talend.core.model.metadata.builder.connection.CDCConnection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.EDIFACTConnection;
import org.talend.core.model.metadata.builder.connection.EbcdicConnection;
import org.talend.core.model.metadata.builder.connection.HL7Connection;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.metadata.builder.connection.SAPConnection;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.core.repository.model.repositoryObject.QueryEMFRepositoryNode;
import org.talend.core.repository.model.repositoryObject.SAPFunctionRepositoryObject;
import org.talend.core.repository.model.repositoryObject.SAPIDocRepositoryObject;
import org.talend.core.repository.model.repositoryObject.SalesforceModuleRepositoryObject;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class RepositoryDoubleClickAction extends Action {

    private List<ITreeContextualAction> contextualsActions = new ArrayList<ITreeContextualAction>();

    private IRepositoryView view;

    public RepositoryDoubleClickAction(IRepositoryView view, List<ITreeContextualAction> contextualsActionsList) {
        super();
        this.view = view;
        for (ITreeContextualAction current : contextualsActionsList) {
            if (current.isDoubleClickAction()) {
                contextualsActions.add(current);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        ISelection selection = getSelection();
        if (selection == null || !(selection instanceof IStructuredSelection)) {
            return;
        }

        Object obj = ((IStructuredSelection) selection).getFirstElement();
        if (obj == null || !(obj instanceof RepositoryNode)) {
            return;
        }

        RepositoryNode node = (RepositoryNode) obj;

        if (node.getObject() instanceof MetadataColumnRepositoryObject) {
            node = node.getParent().getParent();
        }

        if ((node.getType() == ENodeType.SIMPLE_FOLDER || node.getType() == ENodeType.STABLE_SYSTEM_FOLDER || node.getType() == ENodeType.SYSTEM_FOLDER)
                && !isLinkCDCNode(node)) {
            view.expand(node);
            view.getViewer().refresh();
            if (isSERVICES(node)) {
                ITreeContextualAction actionToRun = getAction(node);
                if (!(actionToRun == null)) {
                    actionToRun.init((TreeViewer) getViewPart().getViewer(), (IStructuredSelection) selection);
                    actionToRun.run();
                    // showView();
                }
            }
        } else {
            ITreeContextualAction actionToRun = getAction(node);
            if (!(actionToRun == null)) {
                actionToRun.init((TreeViewer) getViewPart().getViewer(), (IStructuredSelection) selection);
                actionToRun.run();
                // showView();
            }
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
                        if (nodeType.equals(ERepositoryObjectType.METADATA_CONNECTIONS) && pNode.getObject() != null
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
    private boolean isEBCDICTable(RepositoryNode node) {
        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
        if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE) {
            node = node.getParent();
            nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            if (nodeType == ERepositoryObjectType.METADATA_FILE_EBCDIC) {
                return true;
            }
        } else if (nodeType == ERepositoryObjectType.METADATA_CON_COLUMN) {
            node = node.getParent().getParent().getParent();
            nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            if (nodeType == ERepositoryObjectType.METADATA_FILE_EBCDIC) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * hwang Comment method "isSERVICES".
     * 
     * for services
     */
    private boolean isSERVICES(RepositoryNode node) {
        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
        if (nodeType == ERepositoryObjectType.SERVICESOPERATION) {
            return true;
        }
        return false;
    }

    /**
     * 
     * hwang Comment method "isMDMTable".
     * 
     * for mdm
     */
    private boolean isMDMTable(RepositoryNode node) {
        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
        if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE) {
            node = node.getParent();
            nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            if (nodeType == ERepositoryObjectType.METADATA_MDMCONNECTION) {
                return true;
            }
        } else if (nodeType == ERepositoryObjectType.METADATA_CON_COLUMN) {
            node = node.getParent().getParent().getParent();
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
    private boolean isSAPTable(RepositoryNode node) {
        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
        if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE) {
            node = node.getParent();
            nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            if (nodeType == ERepositoryObjectType.METADATA_SAP_FUNCTION || nodeType == ERepositoryObjectType.METADATA_SAP_IDOC) {
                return true;
            }
        } else if (nodeType == ERepositoryObjectType.METADATA_CON_COLUMN) {
            node = node.getParent().getParent().getParent();
            nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            if (nodeType == ERepositoryObjectType.METADATA_SAP_FUNCTION || nodeType == ERepositoryObjectType.METADATA_SAP_IDOC) {
                return true;
            }
        }
        return false;
    }

    private boolean isHL7Table(RepositoryNode node) {
        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
        if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE) {
            node = node.getParent();
            nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            if (nodeType == ERepositoryObjectType.METADATA_FILE_HL7) {
                return true;
            }
        } else if (nodeType == ERepositoryObjectType.METADATA_CON_COLUMN) {
            node = node.getParent().getParent().getParent();
            nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            if (nodeType == ERepositoryObjectType.METADATA_FILE_HL7) {
                return true;
            }
        }
        return false;
    }

    private boolean isEDIFACTTable(RepositoryNode node) {
        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
        if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE) {
            node = node.getParent();
            nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            if (nodeType == ERepositoryObjectType.METADATA_EDIFACT) {
                return true;
            }
        } else if (nodeType == ERepositoryObjectType.METADATA_CON_COLUMN) {
            node = node.getParent().getParent().getParent();
            nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
            if (nodeType == ERepositoryObjectType.METADATA_EDIFACT) {
                return true;
            }
        }
        return false;
    }

    private boolean isBRMSTable(RepositoryNode node) {
        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
        if (nodeType == ERepositoryObjectType.METADATA_CON_TABLE) {
            node = node.getParent();
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

                if (isSAPTable(obj) && current.getClassForDoubleClick().equals(SAPConnection.class)) {
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
                if (current.getClassForDoubleClick().getSimpleName().equals("ServiceOperation")) {
                    return current;
                }

            } else if (obj.getObject() != null
                    && current.getClassForDoubleClick().getSimpleName()
                            .equals(obj.getObject().getProperty().getItem().eClass().getName())) {
                return current;
            }
        }
        return null;
    }

    protected ISelection getSelection() {
        IRepositoryView view = getViewPart();
        if (view != null) {
            return view.getViewer().getSelection();
        }
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (window != null) {
            ISelection selection = window.getSelectionService().getSelection();
            return selection;
        }
        return null;
    }

    protected IRepositoryView getViewPart() {
        IWorkbenchPage page = getActivePage();
        if (page != null) {
            IWorkbenchPart part = page.getActivePart();
            if (part != null && part instanceof IRepositoryView) {
                return (IRepositoryView) part;
            }
        }
        return null;
    }

    protected IWorkbenchPage getActivePage() {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    }
}
