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
package org.talend.repository.mdm.ui.wizard.concept;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.swt.formtools.LabelledCombo;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.core.model.metadata.builder.connection.Concept;
import org.talend.core.model.metadata.builder.connection.XMLFileNode;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.datatools.xml.utils.ATreeNode;
import org.talend.datatools.xml.utils.OdaException;
import org.talend.datatools.xml.utils.SchemaPopulationUtil;
import org.talend.repository.mdm.i18n.Messages;

/**
 * DOC hwang class global comment. Detailled comment
 */
public class SetNameForm extends AbstractMDMFileStepForm {

    private LabelledCombo entityCombo;

    private LabelledText nameText;

    private String conceptName;

    private String[] existingNames;

    private String selectedEntity;

    private ATreeNode schemaTree;

    private Concept concept;

    private boolean creation;

    private boolean firstTime = true;

    /**
     * DOC Administrator SetNameForm constructor comment.
     * 
     * @param parent
     * @param style
     * @param existingNames
     */
    protected SetNameForm(Composite parent, ConnectionItem connectionItem, Concept concept, String[] existingNames,
            boolean creation) {
        super(parent, SWT.NONE, existingNames);
        this.connectionItem = connectionItem;
        this.concept = concept;
        this.existingNames = existingNames;
        this.creation = creation;
        setupForm(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#adaptFormToReadOnly()
     */
    @Override
    protected void adaptFormToReadOnly() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFields()
     */
    @Override
    protected void addFields() {
        Group mdmParameterGroup = new Group(this, SWT.NULL);
        GridLayout layoutGroup = new GridLayout();
        layoutGroup.numColumns = 2;
        mdmParameterGroup.setLayout(layoutGroup);

        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        mdmParameterGroup.setLayoutData(gridData);

        entityCombo = new LabelledCombo(
                mdmParameterGroup,
                Messages.getString("SetNameForm_entities"), Messages.getString("SetNameForm_select_entity"), new ArrayList<String>(), true); //$NON-NLS-1$ //$NON-NLS-2$

        nameText = new LabelledText(mdmParameterGroup, Messages.getString("SetNameForm_name"), true); //$NON-NLS-1$
        checkFieldsValue();
        nameText.forceFocus();
        if (!creation) {
            entityCombo.setReadOnly(true);
            nameText.setReadOnly(true);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFieldsListeners()
     */
    @Override
    protected void addFieldsListeners() {
        nameText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (xsdFilePath == null) {
                    return;
                }
                String textValue = nameText.getText();

                boolean canContinue = true;
                String pattern = "^[a-zA-Z]+[a-zA-Z0-9\\_]*(\\{CURRENT_TABLE\\})[a-zA-Z0-9\\_]*$";//$NON-NLS-1$
                String pattern1 = "^[a-zA-Z]+[a-zA-Z0-9\\_]*$";//$NON-NLS-1$     
                if (!Pattern.matches(pattern, textValue) && !Pattern.matches(pattern1, textValue)) {
                    canContinue = false;
                    updateStatus(IStatus.ERROR, Messages.getString("SetNameForm_entity_illegal")); //$NON-NLS-1$
                }
                if (canContinue) {
                    if (existingNames != null) {
                        for (int i = 0; i < existingNames.length; i++) {
                            if (nameText.getText().equals(existingNames[i])) {
                                updateStatus(IStatus.ERROR, Messages.getString("SetNameForm_entity_exist")); //$NON-NLS-1$
                                canContinue = false;
                                break;
                            }
                        }
                    }
                }
                concept.setLabel(textValue);
                if (canContinue) {
                    checkFieldsValue();
                }
            }

        });

        entityCombo.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                selectedEntity = entityCombo.getText();
                String type = ""; //$NON-NLS-1$
                switch (concept.getConceptType()) {
                case INPUT:
                    type = "In"; //$NON-NLS-1$
                    break;
                case OUTPUT:
                    type = "Out"; //$NON-NLS-1$
                    break;
                case RECEIVE:
                    type = "Receive"; //$NON-NLS-1$
                    break;
                }

                selectedEntity = selectedEntity.trim();

                // if entity name has special char
                String regex = "[^a-zA-Z&&[^0-9]&&[^\\_]]"; //$NON-NLS-1$
                selectedEntity = selectedEntity.replaceAll(regex, "_"); //$NON-NLS-1$

                // if entity name don't start with alphabet
                final char charAt = selectedEntity.charAt(0);
                if (charAt < 'A' || charAt > 'z' || charAt > 'Z' && charAt < 'a') {
                    selectedEntity = "a" + selectedEntity; //$NON-NLS-1$
                }

                String name = selectedEntity + type;
                int counter = 0;
                boolean exists = true;
                while (exists) {
                    exists = !isValidName(name);
                    if (!exists) {
                        break;
                    }
                    counter++;
                    name = name + counter;
                }

                if (creation || firstTime != true) {
                    nameText.setText(name);
                    concept.setLabel(name);
                }
                firstTime = false;
                checkFieldsValue();
            }
        });
    }

    private boolean isValidName(String name) {
        if (existingNames == null) {
            return true;
        }
        for (int i = 0; i < existingNames.length; i++) {
            if (name.equals(existingNames[i])) {
                return false;
            }
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addUtilsButtonListeners()
     */
    @Override
    protected void addUtilsButtonListeners() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#checkFieldsValue()
     */
    @Override
    protected boolean checkFieldsValue() {
        if (nameText == null || nameText.getCharCount() == 0) {
            updateStatus(IStatus.ERROR, Messages.getString("SetNameForm_meta_null")); //$NON-NLS-1$
            return false;
        }
        updateStatus(IStatus.OK, null);
        conceptName = nameText.getText();
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#initialize()
     */
    @Override
    protected void initialize() {
        super.initialize();
    }

    protected String getName() {
        return conceptName;
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            try {
                if (xsdFilePath != null) {
                    if (schemaTree == null) {
                        schemaTree = SchemaPopulationUtil.getSchemaTree(xsdFilePath, true, 0);
                        if (entityCombo != null && schemaTree != null && schemaTree.getChildren() != null) {
                            Object[] children = schemaTree.getChildren();
                            for (int i = 0; i < children.length; i++) {
                                if (children[i] instanceof ATreeNode) {
                                    if (((ATreeNode) children[i]).getValue() != null) {
                                        entityCombo.add(((ATreeNode) children[i]).getValue().toString());
                                    }
                                }
                            }
                        }
                        if (entityCombo.getItemCount() > 0) {
                            if (creation) {
                                entityCombo.select(0);
                            } else {
                                int index = getIndex();
                                entityCombo.select(index);
                            }
                            selectedEntity = entityCombo.getText();
                        }
                    } else if (selectedEntity != null && !"".equals(selectedEntity)) {
                        entityCombo.setText(selectedEntity);
                    } else if (entityCombo.getItemCount() > 0) {
                        if (creation) {
                            entityCombo.select(0);
                        } else {
                            int index = getIndex();
                            entityCombo.select(index);
                        }

                    }
                    if (!creation) {
                        nameText.setText(concept.getLabel());
                    }
                    checkFieldsValue();
                } else {
                    updateStatus(IStatus.ERROR, Messages.getString("SetNameForm_get_entity_fail")); //$NON-NLS-1$
                }

            } catch (OdaException e) {
                ExceptionHandler.process(e);
            } catch (URISyntaxException e) {
                ExceptionHandler.process(e);
            } catch (IOException e) {
                ExceptionHandler.process(e);
            }

        }
    }

    public String getSelectedEntity() {
        return this.selectedEntity;
    }

    private int getIndex() {
        String expression = concept.getLoopExpression();

        if (expression == null) {
            EList<XMLFileNode> loops = concept.getLoop();
            if (loops != null && loops.size() > 0) {
                expression = loops.get(0).getXMLPath();
            }
        }

        if (expression != null) {
            if (expression.contains("/")) {
                String entity = expression.split("/")[1];
                String[] entitys = entityCombo.getCombo().getItems();
                for (int i = 0; i < entitys.length; i++) {
                    if (entitys[i].equals(entity)) {
                        return i;
                    }
                }
            }
        }

        return 0;
    }

}
