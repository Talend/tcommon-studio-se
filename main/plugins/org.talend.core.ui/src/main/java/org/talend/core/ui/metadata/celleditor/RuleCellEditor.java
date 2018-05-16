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
package org.talend.core.ui.metadata.celleditor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.swt.advanced.dataeditor.AbstractDataTableEditorView;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.LinkRulesItem;
import org.talend.core.model.properties.RulesItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryObject;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.ui.rule.AbstractRlueOperationChoice;
import org.talend.core.ui.services.IRulesProviderService;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.repository.model.IMetadataService;
import org.talend.repository.model.IRepositoryService;

/**
 * DOC hywang class global comment. Detailled comment
 */
public class RuleCellEditor extends DialogCellEditor {

    private static final String DRL_EXTENSION = ".drl"; //$NON-NLS-1$

    private static final String XLS_EXTENSION = ".xls"; //$NON-NLS-1$

    private static String builtInRule = "Built-In:"; //$NON-NLS-1$

    private INode node;

    private AbstractDataTableEditorView tableEditorView;

    public RuleCellEditor(Composite parent, INode node) {
        super(parent, SWT.NONE);
        this.node = node;
    }

    public AbstractDataTableEditorView getTableEditorView() {
        return this.tableEditorView;
    }

    private TableViewer getTableViewer() {
        if (this.tableEditorView != null) {
            AbstractExtendedTableViewer extendedTableViewer = this.tableEditorView.getExtendedTableViewer();
            if (extendedTableViewer != null) {
                TableViewerCreator tableViewerCreator = extendedTableViewer.getTableViewerCreator();
                if (tableViewerCreator != null) {
                    return tableViewerCreator.getTableViewer();
                }
            }
        }
        return null;
    }

    public void setTableEditorView(AbstractDataTableEditorView tableEditorView) {
        this.tableEditorView = tableEditorView;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(org.eclipse.swt.widgets.Control)
     */
    @Override
    protected Object openDialogBox(Control cellEditorWindow) {
        String ruleToEdit = (String) this.getValue();
        String oldValue = ruleToEdit;
        // for Rule(feature 6484),hywang add
        Object[] allRules = null;
        RulesItem[] repositoryRuleItems = null;
        LinkRulesItem[] linkRuleItems = null;
        List<RulesItem> listRulesItems = new ArrayList<RulesItem>();
        List<LinkRulesItem> listLinkItems = new ArrayList<LinkRulesItem>();
        // Map<String, List<IRepositoryObject>> allVersions = new HashMap<String, List<IRepositoryObject>>();
        IRepositoryService service = (IRepositoryService) GlobalServiceRegister.getDefault().getService(IRepositoryService.class);
        IRulesProviderService ruleService = (IRulesProviderService) GlobalServiceRegister.getDefault().getService(
                IRulesProviderService.class);
        final IMetadataService metadataService = CoreUIPlugin.getDefault().getMetadataService();
        // IProxyRepositoryFactory repositoryFactory =
        // CorePlugin.getDefault().getRepositoryService().getProxyRepositoryFactory();
        try {
            allRules = service.getProxyRepositoryFactory().getAll(ERepositoryObjectType.METADATA_FILE_RULES).toArray();
            if (allRules != null) {
                int rulesItemsNo = 0;
                int linkItemsNo = 0;
                for (Object allRule : allRules) {
                    IRepositoryObject obj = ((IRepositoryObject) allRule);
                    if (obj.getProperty().getItem() instanceof RulesItem) {
                        RulesItem tempRuleItem = (RulesItem) obj.getProperty().getItem();
                        listRulesItems.add(tempRuleItem);
                        rulesItemsNo++;
                    }
                    if (obj.getProperty().getItem() instanceof LinkRulesItem) {
                        LinkRulesItem tempLinkItem = (LinkRulesItem) obj.getProperty().getItem();
                        listLinkItems.add(tempLinkItem);
                        linkItemsNo++;
                    }
                }
                repositoryRuleItems = new RulesItem[rulesItemsNo];
                linkRuleItems = new LinkRulesItem[linkItemsNo];
                for (int i = 0; i < rulesItemsNo; i++) {
                    repositoryRuleItems[i] = listRulesItems.get(i);
                }
                for (int i = 0; i < linkItemsNo; i++) {
                    linkRuleItems[i] = listLinkItems.get(i);
                }
            }

            // see whether the current RulesItem exsit
            boolean findRepositoryItem = false;
            String itemId;
            RulesItem rulesItem = null;
            AbstractRlueOperationChoice ruleChoiceDialog = null;
            if (node.getElementParameter("REPOSITORY_PROPERTY_TYPE") != null //$NON-NLS-1$ 
                    && node.getElementParameter("PROPERTY_TYPE").getValue().toString().equals("REPOSITORY")) { //$NON-NLS-1$ //$NON-NLS-2$ 
                itemId = node.getElementParameter("REPOSITORY_PROPERTY_TYPE").getValue().toString(); //$NON-NLS-1$
                IRepositoryViewObject obj = CoreUIPlugin.getDefault().getProxyRepositoryFactory().getLastVersion(itemId);
                if (obj.getProperty().getItem() != null && obj.getProperty().getItem() instanceof RulesItem) {
                    rulesItem = (RulesItem) obj.getProperty().getItem();
                    findRepositoryItem = true;
                }
            }
            if (findRepositoryItem && rulesItem.getExtension().equals(DRL_EXTENSION)) { // open dialog for repository
                boolean readonly = node.getProcess().isReadOnly();
                if (node.getJobletNode() != null) {
                    readonly = node.isReadOnly();
                }
                if (metadataService != null) {
                    ruleChoiceDialog = metadataService.getOperationChoice(cellEditorWindow.getShell(), node, repositoryRuleItems,
                            linkRuleItems, EProcessTypeForRule.CREATE, ruleToEdit, readonly);
                }

                if (ruleChoiceDialog != null && ruleChoiceDialog.open() == Window.OK && ruleChoiceDialog.isRepositoryBtnChecked()) {
                    return TalendQuoteUtils.addQuotes(ruleChoiceDialog.getSelectedRuleFileName());
                } else if (ruleChoiceDialog != null && !ruleChoiceDialog.isCancel()) {
                    if (!ruleChoiceDialog.isCheckViewRules()) {
                        // create a rule
                        String selectedSchemaName = ""; //$NON-NLS-1$
                        int index = getTableViewer().getTable().getSelectionIndex();
                        IElementParameter param = node.getElementParameter("SCHEMAS"); //$NON-NLS-1$
                        if (param != null) {
                            if (param.getValue() instanceof List) {
                                List list = (List) param.getValue();
                                if (list.get(index) != null && list.get(index) instanceof Map) {
                                    Map map = (Map) list.get(index);
                                    selectedSchemaName = (String) map.get("SCHEMA"); //$NON-NLS-1$
                                }
                            }
                        }
                        CreateRuleDialog createDialog = new CreateRuleDialog(ruleChoiceDialog.getShell(), node,
                                selectedSchemaName);
                        createDialog.create();
                        if (createDialog.open() == Window.OK) {
                            if (createDialog.getName() != null && !createDialog.getName().equals("")) { //$NON-NLS-1$
                                return TalendQuoteUtils.addQuotes(createDialog.getName());
                            } else {
                                return ""; //$NON-NLS-1$
                            }
                        } else {
                            this.setValue(oldValue);
                            return oldValue;
                        }
                    } else {
                        // view rules
                        if (rulesItem != null) {
                            if (rulesItem.getExtension().equals(DRL_EXTENSION)) {
                                // open drl editor
                                ruleService.openRulesEditor(rulesItem);
                                // repositoryFactory.lock(current);
                                this.setValue(oldValue);
                                return oldValue;
                            }
                            if (rulesItem.getExtension().equals(XLS_EXTENSION)) {
                                // open xls editor
                                IFile file = ruleService.getRuleFile(rulesItem, rulesItem.getExtension());
                                // URL url = file.getLocationURI().toURL();
                                ruleService.openedByBrowser(rulesItem, file);
                                // repositoryFactory.lock(current);
                                this.setValue(oldValue);
                                return oldValue;
                            }
                        }
                    }
                } else {
                    this.setValue(oldValue);
                    return oldValue;
                }
            } else if (findRepositoryItem && rulesItem != null) {
                if (rulesItem.getExtension().equals(XLS_EXTENSION)) {
                    if (metadataService != null) {
                        ruleChoiceDialog = metadataService.getOperationChoice(cellEditorWindow.getShell(), node,
                                repositoryRuleItems, linkRuleItems, EProcessTypeForRule.XLS_REPOSITORY, ruleToEdit, node
                                        .getProcess().isReadOnly());
                    }
                    if (ruleChoiceDialog != null && ruleChoiceDialog.open() == Window.OK) {
                        if (ruleChoiceDialog.isCheckViewRules()) {
                            // view rules

                            // open xls editor
                            IFile file = ruleService.getRuleFile(rulesItem, rulesItem.getExtension());
                            // URL url = file.getLocationURI().toURL();
                            ruleService.openedByBrowser(rulesItem, file);
                            // repositoryFactory.lock(current);
                            this.setValue(oldValue);
                            return oldValue;
                        }
                        return TalendQuoteUtils.addQuotes(ruleChoiceDialog.getSelectedRuleFileName());
                    } else {
                        this.setValue(oldValue);
                        return oldValue;
                    }
                }
            } else { // open dialog for built,either xls or drl just to show rule combo
                if (metadataService != null) {
                    ruleChoiceDialog = metadataService.getOperationChoice(cellEditorWindow.getShell(), node, repositoryRuleItems,
                            linkRuleItems, EProcessTypeForRule.BUILTIN, ruleToEdit, node.getProcess().isReadOnly());
                }
                if (ruleChoiceDialog != null && ruleChoiceDialog.open() == Window.OK) {
                    return TalendQuoteUtils.addQuotes(ruleChoiceDialog.getSelectedRuleFileName());
                } else {
                    this.setValue(oldValue);
                    return oldValue;
                }
            }

        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return ""; //$NON-NLS-1$
    }
}
