// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.metadata.managment.ui.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.swt.font.FontLib;
import org.talend.metadata.managment.ui.i18n.Messages;
import org.talend.metadata.managment.ui.props.PropertiesFieldModel;
import org.talend.metadata.managment.ui.props.PropertiesTableView;

/**
 * 
 * created by ycbai on 2015年1月4日 Detailled comment
 *
 */
public class PropertiesDialog extends TitleAreaDialog {

    private Composite propComposite;

    private List<Map<String, Object>> initPropertiesOfParent;

    private List<Map<String, Object>> initProperties;

    private List<Map<String, Object>> properties;

    private PropertiesTableView propertiesTableView;

    private ExpandBar propertiesBar;

    private Label statusLabel;

    private Font italicFont;

    public PropertiesDialog(Shell parentShell, List<Map<String, Object>> initProperties) {
        this(parentShell, null, initProperties);
    }

    public PropertiesDialog(Shell parentShell, List<Map<String, Object>> initPropertiesOfParent,
            List<Map<String, Object>> initProperties) {
        super(parentShell);
        setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
        this.initPropertiesOfParent = initPropertiesOfParent;
        this.initProperties = initProperties;
        properties = cloneProperties(initProperties);
    }

    public void createPropertiesFields(Composite parent) {
        propComposite = new Composite(parent, SWT.NONE);
        GridLayout propCompLayout = new GridLayout(3, false);
        propCompLayout.marginWidth = getMarginWidth();
        propCompLayout.marginHeight = getMarginHeight();
        propComposite.setLayout(propCompLayout);
        propComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Label propLabel = new Label(propComposite, SWT.NONE);
        propLabel.setText(getTitle());
        Button propertyButton = new Button(propComposite, SWT.NONE);
        final PropertiesDialog propertiesDialog = this;
        propertyButton.setImage(ImageProvider.getImage(EImage.THREE_DOTS_ICON));
        propertyButton.setLayoutData(new GridData(30, 25));
        propertyButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (propertiesDialog.open() == IDialogConstants.OK_ID) {
                    applyProperties(properties);
                    updateStatusLabel(properties);
                }
            }
        });
        statusLabel = new Label(propComposite, SWT.NONE);
        statusLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        updateStatusLabel(initProperties);
    }

    public void updatePropertiesFields(boolean isEdiable) {
        if (propComposite != null && !propComposite.isDisposed()) {
            propComposite.setEnabled(isEdiable);
        }
    }

    @Override
    protected void okPressed() {
        List<Map<String, Object>> currentProperties = getCurrentProperties();
        updateProperties(properties, currentProperties);
        super.okPressed();
    }

    protected String getTitle() {
        return Messages.getString("PropertiesDialog.title"); //$NON-NLS-1$
    }

    protected String getParentTitle() {
        return Messages.getString("PropertiesDialog.parentProperties.title"); //$NON-NLS-1$
    }

    protected String getDesc() {
        return Messages.getString("PropertiesDialog.desc"); //$NON-NLS-1$ 
    }

    protected int getMarginWidth() {
        return 5;
    }

    protected int getMarginHeight() {
        return 5;
    }

    private List<Map<String, Object>> cloneProperties(List<Map<String, Object>> originalProperties) {
        List<Map<String, Object>> newProperties = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : originalProperties) {
            newProperties.add(new HashMap<String, Object>(map));
        }
        return newProperties;
    }

    private void updateProperties(List<Map<String, Object>> originalProperties, List<Map<String, Object>> referProperties) {
        originalProperties.clear();
        for (Map<String, Object> map : referProperties) {
            originalProperties.add(new HashMap<String, Object>(map));
        }
    }

    public void updateStatusLabel(List<Map<String, Object>> hadoopProperties) {
        if (hadoopProperties == null || hadoopProperties.size() == 0) {
            statusLabel.setText(Messages.getString("PropertiesDialog.statusLabel.empty")); //$NON-NLS-1$
        } else {
            StringBuffer propsBuffer = new StringBuffer();
            propsBuffer.append("("); //$NON-NLS-1$
            for (Map<String, Object> propMap : hadoopProperties) {
                String key = String.valueOf(propMap.get("PROPERTY")); //$NON-NLS-1$
                String value = String.valueOf(propMap.get("VALUE")); //$NON-NLS-1$
                String propStr = key + "=" + value + ";"; //$NON-NLS-1$ //$NON-NLS-2$
                propsBuffer.append(propStr);
            }
            if (propsBuffer.length() > 0) {
                propsBuffer.deleteCharAt(propsBuffer.length() - 1);
            }
            if (propsBuffer.length() > 50) {
                propsBuffer = new StringBuffer(propsBuffer.subSequence(0, 50));
                propsBuffer.append("..."); //$NON-NLS-1$
            }
            propsBuffer.append(")"); //$NON-NLS-1$
            statusLabel.setText(propsBuffer.toString());
        }
        statusLabel.setFont(FontLib.ITALIC_FONT);
    }

    /**
     * <p>
     * Get the key name of property key which is "PROPERTY" by default.
     * </p>
     * DOC ycbai Comment method "getPropertiesKeyName".
     * 
     * @return
     */
    protected String getPropertiesKeyName() {
        return PropertiesTableView.DEFAULT_KEY_NAME;
    }

    /**
     * <p>
     * Get the key name of property value which is "VALUE" by default.
     * </p>
     * DOC ycbai Comment method "getPropertiesValueName".
     * 
     * @return
     */
    protected String getPropertiesValueName() {
        return PropertiesTableView.DEFAULT_VALUE_NAME;
    }

    /**
     * <p>
     * Apply the latest properties to the model.
     * </p>
     * 
     * DOC ycbai Comment method "applyProperties".
     * 
     * @param latestProperties
     */
    protected void applyProperties(List<Map<String, Object>> latestProperties) {
        // Do nothing by default.
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(getTitle());
        setHelpAvailable(false);
    }

    @Override
    public void create() {
        super.create();
        setTitle(getTitle());
        setMessage(null); // Clear the message at first.
        setMessage(getDesc());
        setTitleImage(ImageProvider.getImage(EImage.PROPERTIES_WIZ));
    }

    @Override
    protected void initializeBounds() {
        super.initializeBounds();

        Point size = getShell().getSize();
        Point location = getInitialLocation(size);
        getShell().setBounds(getConstrainedShellBounds(new Rectangle(location.x, location.y, size.x, size.y)));
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        Composite comp = new Composite(composite, SWT.NONE);
        comp.setLayoutData(new GridData(GridData.FILL_BOTH));
        GridLayout layout = new GridLayout();
        layout.marginHeight = 10;
        layout.marginWidth = 10;
        comp.setLayout(layout);

        propertiesBar = new ExpandBar(comp, SWT.V_SCROLL);
        propertiesBar.setSpacing(10);
        propertiesBar.setBackground(propertiesBar.getParent().getBackground());
        propertiesBar.setLayoutData(new GridData(GridData.FILL_BOTH));
        PropertiesTableView parentPropertiesTable = null;
        if (initPropertiesOfParent != null) {
            parentPropertiesTable = createPropertiesTable(propertiesBar, 0, getParentTitle(), initPropertiesOfParent, true, false);
        }
        int tableIndex = parentPropertiesTable == null ? 0 : 1;
        propertiesTableView = createPropertiesTable(propertiesBar, tableIndex, getTitle(), initProperties, false, true);
        updateExpandItems();

        return parent;
    }

    @Override
    protected void cancelPressed() {
        updateProperties(initProperties, properties);
        super.cancelPressed();
    }

    private PropertiesTableView createPropertiesTable(ExpandBar bar, int index, String title,
            List<Map<String, Object>> theProperties, boolean readonly, boolean expanded) {
        Composite compositeTable = new Composite(bar, SWT.NONE);
        GridLayout compositeTableLayout = new GridLayout(1, false);
        compositeTableLayout.marginWidth = 0;
        compositeTableLayout.marginHeight = 0;
        compositeTable.setLayout(compositeTableLayout);
        compositeTable.setLayoutData(new GridData(GridData.FILL_BOTH));

        PropertiesFieldModel model = new PropertiesFieldModel(theProperties, ""); //$NON-NLS-1$
        PropertiesTableView propertiesTable = new PropertiesTableView(compositeTable, model) {

            @Override
            protected String getKeyName() {
                return getPropertiesKeyName();
            }

            @Override
            protected String getValueName() {
                return getPropertiesValueName();
            }
        };
        propertiesTable.setReadOnly(readonly);
        Composite fieldTableEditorComposite = propertiesTable.getMainComposite();
        fieldTableEditorComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
        propertiesTable.getTable().addControlListener(new ControlAdapter() {

            @Override
            public void controlResized(ControlEvent e) {
                updateExpandItems();
            }
        });
        ExpandItem item = new ExpandItem(bar, SWT.NONE, index);
        item.setText(title);
        item.setHeight(compositeTable.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
        item.setControl(compositeTable);
        item.setExpanded(expanded);

        return propertiesTable;
    }

    private void updateExpandItems() {
        ExpandItem[] items = propertiesBar.getItems();
        for (ExpandItem expandItem : items) {
            expandItem.setHeight(expandItem.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
        }
    }

    private List<Map<String, Object>> getCurrentProperties() {
        return propertiesTableView.getExtendedTableModel().getBeansList();
    }

    public List<Map<String, Object>> getProperties() {
        return this.properties;
    }

    public void setInitPropertiesOfParent(List<Map<String, Object>> initPropertiesOfParent) {
        this.initPropertiesOfParent = initPropertiesOfParent;
    }

    public void setInitProperties(List<Map<String, Object>> initProperties) {
        this.initProperties = initProperties;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#close()
     */
    @Override
    public boolean close() {
        if (italicFont != null) {
            italicFont.dispose();
        }
        return super.close();
    }

}
