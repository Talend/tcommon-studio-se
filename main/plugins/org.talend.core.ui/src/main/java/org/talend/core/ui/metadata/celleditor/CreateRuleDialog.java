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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.RulesItem;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.ui.i18n.Messages;
import org.talend.core.ui.services.IRulesProviderService;
import org.talend.core.utils.TalendQuoteUtils;

/**
 * DOC hywang class global comment. Detailled comment
 */
public class CreateRuleDialog extends SelectionDialog {

    private INode node;

    private Text nameText, conditionText;

    private String name;

    private String selectedSchemaName;

    private Label status;

    public CreateRuleDialog(Shell parent, INode node, String selectedSchemaName) {
        super(parent);
        this.node = node;
        this.selectedSchemaName = selectedSchemaName;
        this.setTitle(Messages.getString("CreateRuleDialog.title")); //$NON-NLS-1$
        this.setHelpAvailable(false);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        status = new Label(composite, SWT.NONE);
        status.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        status.setText("Rule name already exists"); //$NON-NLS-1$
        status.setForeground(new Color(null, 255, 0, 0));
        status.setVisible(false);
        createMessageArea(composite);

        Label titleBarSeparator = new Label(composite, SWT.HORIZONTAL | SWT.SEPARATOR);
        titleBarSeparator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        createCreateRuleArea(composite);
        return composite;
    }

    @Override
    protected Label createMessageArea(Composite composite) {
        Label label = new Label(composite, SWT.NONE);
        label.setText(Messages.getString("CreateRuleDialog.messageLabel")); //$NON-NLS-1$ 
        label.setFont(composite.getFont());
        return label;
    }

    protected Control createCreateRuleArea(Composite composite) {
        Composite inner = new Composite(composite, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginHeight = 0;
        inner.setLayout(gridLayout);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.minimumWidth = 250;
        inner.setLayoutData(gridData);
        //
        Group group = new Group(inner, SWT.NONE);
        group.setText(Messages.getString("CreateRuleDialog.groupLabel")); //$NON-NLS-1$
        group.setLayout(new GridLayout());
        group.setLayoutData(new GridData(GridData.FILL_BOTH));
        createName(group);
        createCondition(group);
        return composite;
    }

    private void createName(Composite composite) {
        Label nameLabel = new Label(composite, SWT.NONE);
        nameLabel.setText(Messages.getString("CreateRuleDialog.nameLabel")); //$NON-NLS-1$
        nameText = new Text(composite, SWT.BORDER);
        nameText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                setButtonAndStatus(checkRule());
            }
        });
        nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        nameText.setText(selectedSchemaName);
        nameText.setEditable(true);
    }

    private void createCondition(Composite composite) {
        Label conditonLabel = new Label(composite, SWT.NONE);
        conditonLabel.setText(Messages.getString("CreateRuleDialog.conditionLabel")); //$NON-NLS-1$
        conditionText = new Text(composite, SWT.BORDER);
        conditionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        conditionText.setEditable(true);
    }

    @Override
    protected void okPressed() {
        name = nameText.getText();
        String itemId = null;
        String index = null;
        IRepositoryViewObject obj = null;
        Item item = null;
        if ((Messages.getString("CreateRuleDialog.ruleComponentName")).equals(node.getComponent().getName())) { //$NON-NLS-1$
            if (node.getElementParameter("REPOSITORY_PROPERTY_TYPE") != null) { //$NON-NLS-1$
                itemId = node.getElementParameter("REPOSITORY_PROPERTY_TYPE").getValue().toString(); //$NON-NLS-1$
                try {
                    obj = CoreUIPlugin.getDefault().getProxyRepositoryFactory().getLastVersion(itemId);
                    IRulesProviderService rulesService = (IRulesProviderService) GlobalServiceRegister.getDefault().getService(
                            IRulesProviderService.class);
                    if (obj != null) {
                        item = obj.getProperty().getItem();
                        if (item instanceof RulesItem) {
                            RulesItem rulesItem = (RulesItem) item;
                            String content = new String(rulesItem.getContent().getInnerContent());
                            String[] rules = devideRules2SingleRuleFromRepositoryDrl(content);
                            if (rules != null) {
                                index = Integer.toString(rules.length);
                            } else {
                                index = Integer.toString(0);
                            }
                        }
                        rulesService.addRule(item, nameText.getText(), conditionText.getText(), index);
                    }
                } catch (PersistenceException e) {
                    ExceptionHandler.process(e);
                }
            }
        }
        super.okPressed();
    }

    private String[] devideRules2SingleRuleFromRepositoryDrl(String content) { // copy from RuleOperationChoiceDialog
        List<String> names = new ArrayList<String>();
        String[] ruleNames = null;
        if (content != null && !content.equals("")) { //$NON-NLS-1$
            try {
                Pattern regex = Pattern.compile("\\s*rule\\s+\"(.*)\"", //$NON-NLS-1$ 
                        Pattern.CANON_EQ);
                Matcher regexMatcher = regex.matcher(content);
                while (regexMatcher.find()) {
                    names.add(regexMatcher.group(1));

                }
                if (names.isEmpty()) {
                    regex = Pattern.compile("\\s*rule\\x20(.*)", //$NON-NLS-1$ 
                            Pattern.CANON_EQ);
                    regexMatcher = regex.matcher(content);
                    while (regexMatcher.find()) {
                        names.add(regexMatcher.group(1));

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
            } catch (PatternSyntaxException ex) {
                // Syntax error in the regular expression
            }
        }
        return ruleNames;
    }

    private boolean checkRule() {
        boolean valid = true;
        String selectedRuleName = null;
        IElementParameter param = node.getElementParameter("SCHEMAS"); //$NON-NLS-1$
        if (param != null) {
            if (param.getValue() instanceof List) {
                List list = (List) param.getValue();
                for (int index = 0; index < list.size(); index++) {
                    if (list.get(index) != null && list.get(index) instanceof Map) {
                        Map map = (Map) list.get(index);
                        selectedRuleName = (String) map.get("RULE"); //$NON-NLS-1$
                        if (selectedRuleName != null && !"".equals(selectedRuleName)) {
                            selectedRuleName = TalendQuoteUtils.removeQuotes(selectedRuleName);
                            if (nameText.getText().equals(selectedRuleName)) {
                                valid = false;
                                break;
                            }
                        }
                    }
                }
            }
        }

        return valid;
    }

    private void setButtonAndStatus(boolean enable) {
        Button okBtn = getButton(IDialogConstants.OK_ID);
        if (okBtn != null) {
            okBtn.setEnabled(enable);
            status.setVisible(!enable);
        }
    }

    public String getName() {
        return this.name;
    }
}
