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
package org.talend.repository.mdm.ui.wizard;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.swt.formtools.LabelledCombo;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.metadata.managment.mdm.AbsMdmConnectionHelper;
import org.talend.metadata.managment.mdm.S60MdmConnectionHelper;
import org.talend.metadata.managment.ui.wizard.AbstractForm;
import org.talend.repository.mdm.i18n.Messages;

/**
 * DOC hwang class global comment. Detailled comment
 */
public class UniverseForm extends AbstractForm {

    private LabelledCombo universeCombo = null;

    private LabelledCombo modelText = null;

    private LabelledCombo clusterText = null;

    // private MDMWizardPage mdmWizardPage;

    private List<String> universList = new ArrayList<String>();

    private List<String> modelList = new ArrayList<String>();

    private List<String> clusterList = new ArrayList<String>();

    private Object stub;

    private ConnectionItem connectionItem;

    private UniversePage universePage;

    private static final String DEFAULT_UNIVERS = "[HEAD]";

    private AbsMdmConnectionHelper connectionHelper;

    private String pkRegex = "";

    /**
     * DOC Administrator UniverseForm constructor comment.
     * 
     * @param parent
     * @param style
     * @param existingNames
     * @param universePage
     */
    protected UniverseForm(Composite parent, int style, String[] existingNames, ConnectionItem connectionItem,
            UniversePage universePage) {
        super(parent, style, existingNames);
        this.connectionItem = connectionItem;
        this.universePage = universePage;
        stub = ((MDMWizardPage) (universePage.getPreviousPage())).getXtentisBindingStub();
        connectionHelper = new S60MdmConnectionHelper();
        pkRegex = "*";
        initUniverse();
        setupForm(true);
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
        mdmParameterGroup.setText(Messages.getString("UniverseForm_select_version1")); //$NON-NLS-1$
        GridLayout layoutGroup = new GridLayout();
        layoutGroup.numColumns = 2;
        mdmParameterGroup.setLayout(layoutGroup);

        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        mdmParameterGroup.setLayoutData(gridData);
        // if (!MDMVersions.MDM_S60.getKey().equals(getConnection().getVersion()) && universList != null) {
        // universeCombo = new LabelledCombo(mdmParameterGroup,
        //                    Messages.getString("UniverseForm_version"), "", universList, true); //$NON-NLS-1$ //$NON-NLS-2$
        // }
        modelText = new LabelledCombo(mdmParameterGroup, Messages.getString("UniverseForm_data_model1"), "", modelList, true); //$NON-NLS-1$ //$NON-NLS-2$
        clusterText = new LabelledCombo(mdmParameterGroup,
                Messages.getString("UniverseForm_data_container"), "", clusterList, true); //$NON-NLS-1$ //$NON-NLS-2$

        universePage.setPageComplete(false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#addFieldsListeners()
     */
    @Override
    protected void addFieldsListeners() {
        if (universeCombo != null) {
            universeCombo.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(ModifyEvent e) {
                    if (stub == null) {
                        return;
                    }
                    String universeValue = universeCombo.getText();
                    if (DEFAULT_UNIVERS.equals(universeValue)) {
                        modelText.setText("");//$NON-NLS-1$
                        clusterText.setText("");//$NON-NLS-1$
                        checkFieldsValue();
                        return;
                    }
                    if (universeValue != null && universeValue.trim().length() != 0) {
                        connectionHelper.resetUniverseUser(stub, universeValue + "/");
                    }
                    try {
                        //                        WSDataModelPK[] models = stub.getDataModelPKs(new WSRegexDataModelPKs(""));//$NON-NLS-1$
                        List<String> models = connectionHelper.getPKs(stub,
                                "getDataModelPKs", "org.talend.mdm.webservice.WSRegexDataModelPKs", pkRegex, "getWsDataModelPKs");//$NON-NLS-1$
                        //                        WSDataClusterPK[] clusters = stub.getDataClusterPKs(new WSRegexDataClusterPKs(""));//$NON-NLS-1$
                        List<String> clusters = connectionHelper
                                .getPKs(stub,
                                        "getDataClusterPKs", "org.talend.mdm.webservice.WSRegexDataClusterPKs", pkRegex, "getWsDataClusterPKs");//$NON-NLS-1$
                        refreshModelCombo(models);
                        refreshClusterCombo(clusters);

                        getConnection().setUniverse(universeCombo.getText());
                        getConnection().setDatamodel(modelText.getText());
                        getConnection().setDatacluster(clusterText.getText());
                        checkFieldsValue();
                    } catch (Exception e1) {
                        modelText.setText("");//$NON-NLS-1$
                        clusterText.setText("");//$NON-NLS-1$
                        ExceptionHandler.process(e1);
                    }
                }

            });
        }
        modelText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                getConnection().setDatamodel(modelText.getText());
            }
        });

        clusterText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                getConnection().setDatacluster(clusterText.getText());
            }
        });

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
        if (universeCombo != null && (universeCombo.getText() == null || "".equals(universeCombo.getText()))) {
            updateStatus(IStatus.ERROR, "Universe can't be null"); //$NON-NLS-1$
            return false;
        }
        if (modelText.getText() == null || "".equals(modelText.getText())) {
            updateStatus(IStatus.ERROR, "Data model can't be null"); //$NON-NLS-1$
            return false;
        }

        if (clusterText.getText() == null || "".equals(clusterText.getText())) {
            updateStatus(IStatus.ERROR, "Data cluster can't be null"); //$NON-NLS-1$
            return false;

        }

        universePage.setPageComplete(true);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.ui.swt.utils.AbstractForm#initialize()
     */
    @Override
    protected void initialize() {
        String universeValue = getConnection().getUniverse();
        boolean isUniverseNull = StringUtils.trimToNull(universeValue) == null;

        if (universeCombo != null) {
            if (!isUniverseNull) {
                universeCombo.setText(getConnection().getUniverse());
            } else {
                universeCombo.setText(DEFAULT_UNIVERS);
            }
        }

        if (!isUniverseNull) {
            connectionHelper.resetUniverseUser(stub, universeValue + "/");
        }
        try {
            List<String> models = connectionHelper.getPKs(stub,
                    "getDataModelPKs", "org.talend.mdm.webservice.WSRegexDataModelPKs", pkRegex, "getWsDataModelPKs");//$NON-NLS-1$
            //            WSDataModelPK[] models = stub.getDataModelPKs(new WSRegexDataModelPKs(""));//$NON-NLS-1$
            List<String> clusters = connectionHelper.getPKs(stub,
                    "getDataClusterPKs", "org.talend.mdm.webservice.WSRegexDataClusterPKs", pkRegex, "getWsDataClusterPKs");//$NON-NLS-1$
            //            WSDataClusterPK[] clusters = stub.getDataClusterPKs(new WSRegexDataClusterPKs(""));//$NON-NLS-1$
            refreshModelCombo(models);
            refreshClusterCombo(clusters);
        } catch (Exception e1) {
            ExceptionHandler.process(e1);
        }

        if (getConnection().getDatamodel() != null) {
            modelText.setText(getConnection().getDatamodel());
        } else {
            getConnection().setDatamodel(modelText.getText());
        }
        if (getConnection().getDatacluster() != null) {
            clusterText.setText(getConnection().getDatacluster());
        } else {
            getConnection().setDatacluster(clusterText.getText());
        }

        checkFieldsValue();
    }

    private void refreshModelCombo(List<String> models) {
        String connModel = (String) modelText.getCombo().getData();
        modelText.removeAll();
        if (models.size() <= 0) {
            return;
        }
        modelText.getCombo().setItems(models.toArray(new String[models.size()]));
        if (!"".equals(connModel) && connModel != null) { //$NON-NLS-1$
            modelText.setText(connModel);// (getConnection().getDatamodel());
            return;
        }
        modelText.setText(models.get(0));

    }

    // private void refreshModelCombo(WSDataModelPK[] models) {
    // String connModel = (String) modelText.getCombo().getData();
    // modelText.removeAll();
    // if (models.length <= 0) {
    // return;
    // }
    // for (WSDataModelPK model : models) {
    // modelText.add(model.getPk());
    // }
    //        if (!"".equals(connModel) && connModel != null) { //$NON-NLS-1$
    // modelText.setText(connModel);// (getConnection().getDatamodel());
    // return;
    // }
    // modelText.setText(models[0].getPk());
    //
    // }

    private void refreshClusterCombo(List<String> clusters) {
        String connCluster = (String) clusterText.getCombo().getData();
        clusterText.removeAll();
        if (clusters.size() <= 0) {
            return;
        }
        clusterText.getCombo().setItems(clusters.toArray(new String[clusters.size()]));
        if (!"".equals(connCluster) && connCluster != null) { //$NON-NLS-1$
            clusterText.setText(connCluster);
            return;
        }
        clusterText.setText(clusters.get(0));
    }

    // private void refreshClusterCombo(WSDataClusterPK[] clusters) {
    // String connCluster = (String) clusterText.getCombo().getData();
    // clusterText.removeAll();
    // if (clusters.length <= 0) {
    // return;
    // }
    // for (WSDataClusterPK cluster : clusters) {
    // clusterText.add(cluster.getPk());
    // }
    //        if (!"".equals(connCluster) && connCluster != null) { //$NON-NLS-1$
    // clusterText.setText(connCluster);
    // return;
    // }
    // clusterText.setText(clusters[0].getPk());
    // }

    public MDMConnection getConnection() {
        return (MDMConnection) connectionItem.getConnection();
    }

    protected void initUniverse() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Control#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible && connectionHelper == null) {
            connectionHelper = new S60MdmConnectionHelper();
            pkRegex = "*";
        }
    }

}
