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
package org.talend.repository.metadata.ui.dialog;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.LinkRulesItem;
import org.talend.core.model.properties.RulesItem;
import org.talend.core.ui.metadata.celleditor.EProcessTypeForRule;
import org.talend.core.ui.rule.AbstractRlueOperationChoice;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.repository.metadata.i18n.Messages;
import org.talend.repository.model.IRepositoryService;

/**
 * DOC hywang class global comment. Detailled comment
 */
public class RuleOperationChoiceDialog extends AbstractRlueOperationChoice {

    /**
     * 
     * hywang ESelectionCategoryForRule.
     */
    public enum ESelectionCategoryForRule {
        SHOW_SCHEMA,
        BUILDIN,
        REPOSITORY;
    }

    private static final String TITLE = "Rule"; //$NON-NLS-1$

    private static final String MESSAGE = "Please choose a rule or cancel"; //$NON-NLS-1$

    private ESelectionCategoryForRule selection;

    private Button viewSchemaBtn;

    private Button repositoryBtn, createBtn, viewRulesBtn;

    private final EProcessTypeForRule processType;

    private Combo ruleCombo;

    private final INode node;

    private final RulesItem[] items;

    private final LinkRulesItem[] linkItems;

    private MetadataTable selectedTable;

    private final String ruleName;

    private final boolean readOnlyJob;

    private String[] rulesNames;

    private Label ruleLabel;

    private RulesItem currentRepositoryItem;

    private LinkRulesItem currentRepositoryLinkItem;

    // private Label versionLabel;

    // private final Map<String, List<IRepositoryObject>> allVersions;

    public RuleOperationChoiceDialog(Shell parentShell, INode node, RulesItem[] items, LinkRulesItem[] linkItems,
            EProcessTypeForRule processType, String ruleName, boolean readOnlyJob) {
        super(parentShell);
        this.node = node;
        this.items = items;
        this.linkItems = linkItems;
        this.ruleName = ruleName;
        this.readOnlyJob = readOnlyJob;
        // this.allVersions = allVersions;
        this.processType = processType;
        this.setTitle(TITLE);
        this.setMessage(MESSAGE);
        this.setHelpAvailable(false);
        try {
            rulesNames = devideRules(items, linkItems);
        } catch (BiffException e) {
            ExceptionHandler.process(e);
        } catch (IOException e) {
            ExceptionHandler.process(e);
        }
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        createMessageArea(composite);

        Label titleBarSeparator = new Label(composite, SWT.HORIZONTAL | SWT.SEPARATOR);
        titleBarSeparator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        createOptionArea(composite);

        return composite;
    }

    protected Control createOptionArea(Composite composite) {
        Composite inner = new Composite(composite, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginHeight = 0;
        inner.setLayout(gridLayout);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.minimumWidth = 250;
        inner.setLayoutData(gridData);
        //
        Group group = new Group(inner, SWT.NONE);
        group.setText(Messages.getString("SchemaOperationChoiceDialog.Option")); //$NON-NLS-1$
        group.setLayout(new GridLayout());
        group.setLayoutData(new GridData(GridData.FILL_BOTH));

        // default
        switch (processType) {
        case CREATE:
            setSelection(ESelectionCategoryForRule.BUILDIN);
            createCreationComposite(group);
            break;
        case BUILTIN:
            setSelection(ESelectionCategoryForRule.SHOW_SCHEMA);
            createBuiltInComposite(group);
            break;
        case XLS_REPOSITORY:
            setSelection(ESelectionCategoryForRule.SHOW_SCHEMA);
            createRepositoryXLSComposite(group);
            break;
        default:
        }

        configControlStatus();
        return composite;
    }

    private void createCreationComposite(Group group) {
        createViewRules(group, Messages.getString("RuleOperationChoiceDialog.editRules")); //$NON-NLS-N$ //$NON-NLS-1$
        createCreateRuleButton(group, "Create a rule with guide"); //$NON-NLS-1$
        createSelectFromRepositoryButton(group, "Select a rule from repository"); //$NON-NLS-1$
        createRuleCombo(group);
        // createVersionCombo(group);
        // repositoryBtn.setSelection(true);
    }

    private void createBuiltInComposite(Group group) {
        //        createViewSchemaButton(group, Messages.getString("SchemaOperationChoiceDialog.EditSchemaMessage")); //$NON-NLS-1$
        createSelectFromRepositoryButton(group, Messages.getString("RuleOperationChoiceDialog.selectRulesFile")); //$NON-NLS-1$
        createRuleCombo(group);

    }

    private void createRepositoryXLSComposite(Group group) {
        createViewRules(group, Messages.getString("RuleOperationChoiceDialog.viewRules")); //$NON-NLS-N$ //$NON-NLS-1$
        createSelectFromRepositoryButton(group, "Select a rule from repository"); //$NON-NLS-1$
        createRuleCombo(group);
    }

    // private void createViewSchemaButton(Composite composite, String message) {
    // viewSchemaBtn = new Button(composite, SWT.RADIO);
    // viewSchemaBtn.setSelection(getSelctionType() == ESelectionCategoryForRule.SHOW_SCHEMA);
    // viewSchemaBtn.setText(message);
    // viewSchemaBtn.addSelectionListener(new SelectionAdapter() {
    //
    // @Override
    // public void widgetSelected(SelectionEvent e) {
    // if (viewSchemaBtn.getSelection()) {
    // setSelection(ESelectionCategoryForRule.SHOW_SCHEMA);
    // if (ruleCombo != null) {
    // ruleCombo.setVisible(false);
    // }
    // }
    // setButtonAndStatus(true);
    // }
    // });
    //
    // }

    // private void createLabel(Composite composite) {
    // Label mainLabel = new Label(composite, SWT.NONE);
    //        mainLabel.setText("Select a rule from repository:"); //$NON-NLS-N$
    // }

    private void createCreateRuleButton(Composite composite, String message) {
        createBtn = new Button(composite, SWT.RADIO);
        createBtn.setText(message);
        // createBtn.setVisible(showBtn);
        createBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                isRepositoryBtnChecked = false;
                if (createBtn.getSelection()) {
                    setSelection(ESelectionCategoryForRule.BUILDIN);
                    if (ruleCombo != null) {
                        ruleCombo.setVisible(false);
                    }
                    if (ruleLabel != null) {
                        ruleLabel.setVisible(false);
                    }
                    // if (versionLabel != null) {
                    // versionLabel.setVisible(false);
                    // }
                }
            }

        });

    }

    private void createSelectFromRepositoryButton(Composite composite, String message) {
        repositoryBtn = new Button(composite, SWT.RADIO);
        repositoryBtn.setText(message);
        repositoryBtn.setVisible(true);
        repositoryBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (repositoryBtn.getSelection()) {
                    isRepositoryBtnChecked = true;
                    setSelection(ESelectionCategoryForRule.REPOSITORY);

                    if (ruleCombo != null) {
                        ruleCombo.setVisible(true);
                    }
                    if (ruleLabel != null) {
                        ruleLabel.setVisible(true);
                    }
                    // if (versionLabel != null) {
                    // versionLabel.setVisible(true);
                    // }
                }

            }

        });

    }

    private void createViewRules(Composite composite, String message) {
        viewRulesBtn = new Button(composite, SWT.RADIO);
        viewRulesBtn.setText(message);
        viewRulesBtn.setVisible(true);
        viewRulesBtn.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                ruleCombo.setVisible(false);
                ruleLabel.setVisible(false);
            }

        });

    }

    private void createRuleCombo(Composite composite) {
        ruleLabel = new Label(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
        ruleLabel.setText(Messages.getString("RuleOperationChoiceDialog.rule")); //$NON-NLS-N$ //$NON-NLS-1$
        ruleLabel.setVisible(true);
        ruleCombo = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
        ruleCombo.setVisible(true);
        GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
        ruleCombo.setLayoutData(layoutData);
        // for (RulesItem item : items) {
        // ruleCombo.add(item.getProperty().getLabel());
        // }
        if (this.rulesNames != null) {
            for (int i = 0; i < this.rulesNames.length; i++) {
                ruleCombo.add(rulesNames[i]);
            }
            if (ruleCombo.getItemCount() > 0) {
                ruleCombo.select(0);
            }
        }

    }

    // private void createVersionCombo(Composite composite) {
    // versionLabel = new Label(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
    //        versionLabel.setText("Version:"); //$NON-NLS-N$
    // versionLabel.setVisible(false);
    // GridData layoutData = new GridData();
    // layoutData.widthHint = 40;
    // if (allVersions != null || !allVersions.isEmpty()) {
    // // add every versions for the current chosen rule
    // String currentRuleName = ruleCombo.getItem(ruleCombo.getSelectionIndex());
    //
    // }
    //
    // }

    private void configControlStatus() {
        if (readOnlyJob) {
            // if (repositoryBtn != null && !repositoryBtn.isDisposed()) {
            // repositoryBtn.setEnabled(false);
            // }
            if (viewSchemaBtn != null && !viewSchemaBtn.isDisposed()) {
                viewSchemaBtn.setEnabled(false);
            }
            if (ruleCombo != null && !ruleCombo.isDisposed()) {
                ruleCombo.setEnabled(true);
            }
        }
    }

    @Override
    protected void okPressed() {
        this.selectedTable = null;
        String convertName = null;
        // if (getSelctionType() == ESelectionCategoryForRule.REPOSITORY) {

        // for (RulesItem item : items) {
        // if (item.getProperty().getLabel().equals(this.ruleCombo.getItem(ruleCombo.getSelectionIndex()))) {
        if (ruleCombo.getSelectionIndex() >= 0) {
            convertName = this.ruleCombo.getItem(ruleCombo.getSelectionIndex());
        } else {
            convertName = ""; //$NON-NLS-N$ //$NON-NLS-1$
        }
        if (viewRulesBtn != null) {
            if (viewRulesBtn.getSelection()) {
                isCheckViewRules = true;
            }
        }
        // }
        // }
        this.selectedRuleFileName = convertName;
        // }
        super.okPressed();

    }

    @Override
    protected void cancelPressed() {
        // TODO Auto-generated method stub
        this.isCancel = true;
        super.cancelPressed();
    }

    private void setSelection(ESelectionCategoryForRule selection) {
        this.selection = selection;
    }

    public ESelectionCategoryForRule getSelctionType() {

        return this.selection;
    }

    public org.talend.core.model.metadata.builder.connection.MetadataTable getSelectedMetadataTable() {
        return this.selectedTable;
    }

    private String[] devideRules(RulesItem[] rulesItems, LinkRulesItem[] linkItems) throws BiffException, IOException {
        String currentRulesContent = null;
        String currentNodeRepositoryRuleId = null;
        String[] ruleNames = null;
        if (node.getElementParameter("PROPERTY_TYPE") != null && ("REPOSITORY").equals(node.getElementParameter("PROPERTY_TYPE").getValue())) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            for (IElementParameter ep : node.getElementParameters()) {
                if (ep.getName().equals("PROPERTY")) { //$NON-NLS-N$ //$NON-NLS-1$
                    if (ep.getChildParameters() != null) {
                        currentNodeRepositoryRuleId = ep.getChildParameters().get("REPOSITORY_PROPERTY_TYPE").getValue() //$NON-NLS-N$ //$NON-NLS-1$
                                .toString();
                        break;
                    }
                }
            }

            if (currentNodeRepositoryRuleId != null) {
                for (RulesItem item : rulesItems) {
                    if (item.getProperty().getId().equals(currentNodeRepositoryRuleId)) {
                        this.currentRepositoryItem = item;
                        break;
                    }
                }
                for (LinkRulesItem item : linkItems) {
                    if (item.getProperty().getId().equals(currentNodeRepositoryRuleId)) {
                        this.currentRepositoryLinkItem = item;
                        break;
                    }
                }

                if (this.currentRepositoryItem != null) { //$NON-NLS-N$  for all RulesItems
                    if (this.currentRepositoryItem.getExtension().equals(".drl")) { //$NON-NLS-N$ //$NON-NLS-1$
                        // rulesItem drl
                        currentRulesContent = new String(this.currentRepositoryItem.getContent().getInnerContent());
                        ruleNames = devideRules2SingleRuleFromRepositoryDrl(currentRulesContent);
                    } else if (this.currentRepositoryItem.getExtension().equals(".xls")) { //$NON-NLS-N$ //$NON-NLS-1$
                        // rulesItem xls
                        IRepositoryService repoService = (IRepositoryService) GlobalServiceRegister.getDefault().getService(
                                IRepositoryService.class);
                        if (repoService != null) {
                            String path = repoService.getRulesProviderPath(currentRepositoryItem);
                            if (path != null) {
                                ruleNames = readExc(path);
                            }
                        }
                    }
                }
                if (this.currentRepositoryLinkItem != null) { //$NON-NLS-N$  for all LinkItems
                    if (this.currentRepositoryLinkItem.getExtension().equals(".drl")) { //$NON-NLS-N$   //$NON-NLS-1$
                        // linkItem drl
                        currentRulesContent = readFileByLines(currentRepositoryLinkItem.getLink().getURI());
                        ruleNames = devideRules2SingleRuleFromRepositoryDrl(currentRulesContent);
                    } else if (this.currentRepositoryLinkItem.getExtension().equals(".xls")) { //$NON-NLS-N$ //$NON-NLS-1$
                        // linkItem xls
                        ruleNames = readExc(currentRepositoryLinkItem.getLink().getURI());
                    }
                }
            }
        }
        if (node.getElementParameter("PROPERTY_TYPE") != null //$NON-NLS-N$ //$NON-NLS-1$
                && ("BUILT_IN").equals(node.getElementParameter("PROPERTY_TYPE").getValue()) //$NON-NLS-1$ //$NON-NLS-2$
                && node.getElementParameter("SELECTED_FILE").getValue() != null) { //$NON-NLS-N$ //$NON-NLS-1$
            // get rules names from built-in drl or xls
            String fileFullPath = node.getElementParameter("SELECTED_FILE").getValue().toString().replaceAll("\"", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            if (fileFullPath.endsWith(".drl")) { //$NON-NLS-1$
                currentRulesContent = readFileByLines(fileFullPath);
                ruleNames = devideRules2SingleRuleFromRepositoryDrl(currentRulesContent);
            }
            if (fileFullPath.endsWith(".xls")) { //$NON-NLS-1$
                ruleNames = readExc(fileFullPath);
            }

        }

        return ruleNames;
    }

    private String[] devideRules2SingleRuleFromRepositoryDrl(String content) {
        List<String> names = new ArrayList<String>();
        String[] ruleNames = null;
        Pattern regex = Pattern.compile("\\s*rule\\s+(.*)", //$NON-NLS-N$ //$NON-NLS-1$
                Pattern.CANON_EQ);
        InputStreamReader isr = null;
        BufferedReader reader = null;
        if (content != null && !content.equals("")) { //$NON-NLS-1$
            try {
                isr = new InputStreamReader(new ByteArrayInputStream(content.getBytes()));
                reader = new BufferedReader(isr);
                String tempString = null;
                int line = 1;
                while ((tempString = reader.readLine()) != null) {
                    tempString = tempString.trim();
                    if (!tempString.startsWith("#")) { //$NON-NLS-N$ //$NON-NLS-1$
                        Matcher regexMatcher = regex.matcher(tempString);
                        while (regexMatcher.find()) {
                            names.add(regexMatcher.group(1));
                        }
                        if (names.isEmpty()) {
                            regex = Pattern.compile("\\s*rule\\s+\"(.*)\"", //$NON-NLS-N$ //$NON-NLS-1$
                                    Pattern.CANON_EQ);
                            regexMatcher = regex.matcher(tempString);
                            while (regexMatcher.find()) {
                                names.add(TalendQuoteUtils.removeQuotes(regexMatcher.group(1)));
                            }
                        }
                        line++;
                    }
                }
                if (names != null && !names.isEmpty()) {
                    Object[] objs = names.toArray();
                    ruleNames = new String[objs.length];
                    for (int i = 0; i < objs.length; i++) {
                        if (objs[i] instanceof String) {
                            ruleNames[i] = (String) objs[i];
                        }
                    }
                }
            } catch (Exception e) {
                // Syntax error in the regular expression
                ExceptionHandler.process(e);
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                    if (isr != null) {
                        isr.close();
                    }
                } catch (IOException e1) {
                    ExceptionHandler.process(e1);
                }
            }
        }

        return ruleNames;
    }

    public static String readFileByLines(String fileName) {
        String fileContent = ""; //$NON-NLS-1$
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
                sb.append("\n"); //$NON-NLS-1$
                line++;
            }
            fileContent = sb.toString();
            reader.close();
        } catch (IOException e) {
            ExceptionHandler.process(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return fileContent;
    }

    public static String[] readExc(String filename) throws BiffException, IOException {
        File file = new File(filename);
        Workbook wb = Workbook.getWorkbook(file);
        Sheet s = wb.getSheet(0);
        Cell c = null;
        int col = s.getColumns();
        int row = s.getRows();
        String[] ruleNames = null;
        List<String> names = new ArrayList<String>();
        Pattern regex = Pattern.compile("(.+\\s)rules$", //$NON-NLS-N$ //$NON-NLS-1$
                Pattern.CANON_EQ);
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                c = s.getCell(i, j);
                Matcher regexMatcher = regex.matcher(c.getContents());
                if (regexMatcher.find()) {
                    try {
                        for (int k = j + 1; k < row; k++) {
                            if (s.getCell(i, k).getContents() != null
                                    && !"".equals(s.getCell(i, k).getContents()) //$NON-NLS-N$ //$NON-NLS-1$
                                    && !regex.matcher(s.getCell(i, k).getContents()).find()
                                    && !names.contains(s.getCell(i, k).getContents())) {
                                names.add(s.getCell(i, k).getContents());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        ruleNames = new String[names.size()];
        for (int x = 0; x < names.size(); x++) {
            if (names.get(x) != null && names.get(x) instanceof String) {
                ruleNames[x] = (String) names.get(x);
            }
        }

        return ruleNames;
    }
}
