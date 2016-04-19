package org.talend.core.hadoop.version.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.swt.formtools.LabelledCombo;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.hadoop.HadoopConstants;
import org.talend.core.hadoop.IHadoopDistributionService;
import org.talend.core.hadoop.IHadoopService;
import org.talend.core.model.components.ComponentCategory;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.hd.IDistributionsManager;
import org.talend.core.runtime.hd.IHDistribution;
import org.talend.core.runtime.hd.IHDistributionVersion;
import org.talend.core.runtime.i18n.Messages;

/**
 * created by ycbai on 2013-3-15 Detailled comment
 * 
 */
public class HadoopVersionDialog extends TitleAreaDialog {

    private static final int VISIBLE_DISTRIBUTION_COUNT = 5;

    private static final int VISIBLE_VERSION_COUNT = 6;

    private LabelledCombo distributionCombo;

    private LabelledCombo versionCombo;

    private Button importFromZipBtn;

    private Button importFromVersion;

    private boolean isFromExistVersion = true;;

    private boolean isFromZip;

    private String distribution;

    private String version;

    private Map<ECustomVersionGroup, String> groupsAndDispaly;

    private List<Button> existVersionCheckBoxList;

    private List<Button> fromZipCheckBoxList;

    private Text zipLocationText;

    private String zipLocation;

    private Button browseButton;

    private HadoopCustomLibrariesUtil customLibUtil;

    private ECustomVersionType[] types;

    private Map<ECustomVersionGroup, Boolean> existVersionSelectionMap = new HashMap<ECustomVersionGroup, Boolean>();

    private Map<ECustomVersionGroup, Boolean> fromZipSelectionMap = new HashMap<ECustomVersionGroup, Boolean>();

    private Map<ECustomVersionType, Map<String, Object>> typeConfigurations = new HashMap<ECustomVersionType, Map<String, Object>>();

    public HadoopVersionDialog(Shell parentShell, Map<ECustomVersionGroup, String> groupsAndDispaly,
            HadoopCustomLibrariesUtil customLibUtil, ECustomVersionType[] types) {
        super(parentShell);
        setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MIN | SWT.APPLICATION_MODAL);
        this.groupsAndDispaly = groupsAndDispaly;
        this.customLibUtil = customLibUtil;
        this.types = types;
    }

    private IHadoopDistributionService getHadoopDistributionService() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IHadoopDistributionService.class)) {
            return (IHadoopDistributionService) GlobalServiceRegister.getDefault().getService(IHadoopDistributionService.class);
        }
        return null;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.getString("HadoopVersionDialog.title")); //$NON-NLS-1$
        newShell.setSize(810, 450);
        setHelpAvailable(false);
    }

    @Override
    public void create() {
        super.create();
        setTitle(Messages.getString("HadoopVersionDialog.title")); //$NON-NLS-1$
        setMessage(Messages.getString("HadoopVersionDialog.msg")); //$NON-NLS-1$
        setTitleImage(ImageProvider.getImage(EImage.HADOOP_WIZ_ICON));
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

        createVersionFields(comp);
        addListener();
        init();
        updateOkState();

        return parent;
    }

    private void createVersionFields(Composite parent) {
        GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
        importFromVersion = new Button(parent, SWT.RADIO);
        importFromVersion.setText(Messages.getString("HadoopVersionDialog.importFromExistVersion"));//$NON-NLS-1$
        importFromVersion.setLayoutData(layoutData);

        Composite existVersionGroup = new Composite(parent, SWT.NONE);
        GridLayout existVersionLayout = new GridLayout();
        existVersionLayout.numColumns = 3;
        existVersionGroup.setLayout(existVersionLayout);
        existVersionGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        distributionCombo = new LabelledCombo(existVersionGroup, Messages.getString("HadoopVersionDialog.distribution"), //$NON-NLS-1$
                Messages.getString("HadoopVersionDialog.distribution.tooltip"), new String[0], 2, true); //$NON-NLS-1$
        distributionCombo.setVisibleItemCount(VISIBLE_DISTRIBUTION_COUNT);
        versionCombo = new LabelledCombo(existVersionGroup, Messages.getString("HadoopVersionDialog.version"), //$NON-NLS-1$
                Messages.getString("HadoopVersionDialog.version.tooltip"), new String[0], 2, true); //$NON-NLS-1$
        versionCombo.setVisibleItemCount(VISIBLE_VERSION_COUNT);
        // typse checkbox
        Composite checkParent = new Composite(existVersionGroup, SWT.NONE);
        layoutData = new GridData(GridData.FILL_HORIZONTAL);
        layoutData.horizontalSpan = 3;
        checkParent.setLayoutData(layoutData);
        GridLayout layout2 = new GridLayout(groupsAndDispaly.size(), false);
        checkParent.setLayout(layout2);
        layout2.marginWidth = 0;
        existVersionCheckBoxList = new ArrayList<Button>();
        for (ECustomVersionGroup group : groupsAndDispaly.keySet()) {
            final Button button = new Button(checkParent, SWT.CHECK);
            button.setData(group);
            button.setText(groupsAndDispaly.get(group));
            button.setSelection(true);
            existVersionCheckBoxList.add(button);
            existVersionSelectionMap.put(group, true);
            button.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    existVersionSelectionMap.put((ECustomVersionGroup) button.getData(), button.getSelection());
                    updateOkState();
                }
            });
        }
        // import from zip
        importFromZipBtn = new Button(parent, SWT.RADIO);
        importFromZipBtn.setText(Messages.getString("HadoopVersionDialog.importFromZip"));//$NON-NLS-1$
        layoutData = new GridData(GridData.FILL);
        importFromZipBtn.setLayoutData(layoutData);
        Composite zipGroup = new Composite(parent, SWT.NONE);
        layoutData = new GridData(GridData.FILL_HORIZONTAL);
        zipGroup.setLayoutData(layoutData);
        GridLayout zipGroupLayout = new GridLayout();
        zipGroupLayout.numColumns = 3;
        zipGroup.setLayout(zipGroupLayout);
        Label label = new Label(zipGroup, SWT.NONE);
        label.setText(Messages.getString("HadoopVersionDialog.zipLocation"));//$NON-NLS-1$
        zipLocationText = new Text(zipGroup, SWT.BORDER);
        zipLocationText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        browseButton = new Button(zipGroup, SWT.PUSH);
        browseButton.setText(Messages.getString("HadoopVersionDialog.browseBtn"));//$NON-NLS-1$

        // typse checkbox
        checkParent = new Composite(zipGroup, SWT.NONE);
        layoutData = new GridData(GridData.FILL_HORIZONTAL);
        layoutData.horizontalSpan = 3;
        checkParent.setLayoutData(layoutData);
        layout2 = new GridLayout(groupsAndDispaly.size(), false);
        checkParent.setLayout(layout2);
        layout2.marginWidth = 0;
        fromZipCheckBoxList = new ArrayList<Button>();
        for (ECustomVersionGroup group : groupsAndDispaly.keySet()) {
            final Button button = new Button(checkParent, SWT.CHECK);
            button.setData(group);
            button.setText(groupsAndDispaly.get(group));
            button.setSelection(true);
            fromZipCheckBoxList.add(button);
            fromZipSelectionMap.put(group, true);
            button.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    fromZipSelectionMap.put((ECustomVersionGroup) button.getData(), button.getSelection());
                    updateOkState();
                }
            });
        }
        importFromVersion.setSelection(true);
        enableZipGroupe(false);

    }

    private void updateOkState() {
        Button okButton = getButton(IDialogConstants.OK_ID);
        if (okButton != null) {
            okButton.setEnabled(isNotEmptySelection());
        }
    }

    private boolean isNotEmptySelection() {
        if (importFromVersion.getSelection()) {
            for (ECustomVersionGroup group : existVersionSelectionMap.keySet()) {
                if (existVersionSelectionMap.get(group)) {
                    return true;
                }
            }
        } else {
            for (ECustomVersionGroup group : fromZipSelectionMap.keySet()) {
                if (fromZipSelectionMap.get(group) && StringUtils.isNotEmpty(zipLocationText.getText())) {
                    return true;
                }
            }
        }

        return false;
    }

    private void enableExistGroup(boolean enable) {
        isFromExistVersion = enable;
        distributionCombo.setEnabled(enable);
        versionCombo.setEnabled(enable);
        for (Button button : existVersionCheckBoxList) {
            button.setEnabled(enable);
        }
    }

    private void enableZipGroupe(boolean enable) {
        isFromZip = enable;
        zipLocationText.setEnabled(enable);
        browseButton.setEnabled(enable);
        for (Button button : fromZipCheckBoxList) {
            button.setEnabled(enable);
        }
    }

    private IHDistribution getHadoopDistribution() {
        IDistributionsManager distributionManager = getDistributionsManager();
        if (distributionManager != null) {
            final IHDistribution distributionByDisplay = distributionManager.getDistribution(distributionCombo.getText(), true);
            return distributionByDisplay;
        }
        return null;
    }

    private void addListener() {
        distributionCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                final IHDistribution distributionByDisplay = getHadoopDistribution();
                if (distributionByDisplay != null) {
                    distribution = distributionByDisplay.getName();
                    updateVersionPart();
                }
            }
        });

        versionCombo.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(final ModifyEvent e) {
                final IHDistribution distributionByDisplay = getHadoopDistribution();
                String newVersionDisplayName = versionCombo.getText();

                if (distributionByDisplay != null) {
                    final IHDistributionVersion hdVersion = distributionByDisplay.getHDVersion(newVersionDisplayName, true);
                    if (hdVersion != null) {
                        version = hdVersion.getVersion();
                    }
                }
            }
        });

        // add listners
        browseButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog(getParentShell());
                dialog.setFilterExtensions(HadoopCustomLibrariesUtil.FILE__MASK);
                String path = dialog.open();
                if (path != null) {
                    zipLocationText.setText(path);
                }
            }
        });
        importFromVersion.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                enableExistGroup(true);
                enableZipGroupe(false);
                updateOkState();
            }
        });
        importFromZipBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                enableExistGroup(false);
                enableZipGroupe(true);
                updateOkState();
            }
        });
        zipLocationText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                zipLocation = zipLocationText.getText();
                updateOkState();
            }
        });
    }

    private IDistributionsManager getDistributionsManager() {
        if (types != null && types.length == 1) {
            return HadoopVersionControlUtils.getDistributionsManager(types[0]);
        }
        return null;

    }

    private void init() {
        List<String> distributionsDisplay = new ArrayList<String>();
        IDistributionsManager distributionManager = getDistributionsManager();
        if (distributionManager != null) {
            IHDistribution[] distributions = distributionManager.getDistributions();
            if (distributions != null) {
                for (IHDistribution d : distributions) {
                    if (!d.useCustom()) {// not need custom
                        distributionsDisplay.add(d.getDisplayName());
                    }
                }
            }
        }

        distributionCombo.getCombo().setItems(distributionsDisplay.toArray(new String[0]));
        distributionCombo.select(0);
    }

    private void updateVersionPart() {
        final IHDistribution hadoopDistribution = getHadoopDistribution();
        if (hadoopDistribution != null) {
            String[] versionsDisplay = hadoopDistribution.getVersionsDisplay();
            IHDistributionVersion defaultVersion = hadoopDistribution.getDefaultVersion();

            versionCombo.getCombo().setItems(versionsDisplay);
            if (defaultVersion != null) {
                versionCombo.getCombo().setText(defaultVersion.getDisplayVersion());
            } else if (versionsDisplay.length > 0) {
                versionCombo.getCombo().select(0);
            }
        }
    }

    @Override
    protected void initializeBounds() {
        super.initializeBounds();

        Point size = getShell().getSize();
        Point location = getInitialLocation(size);
        getShell().setBounds(getConstrainedShellBounds(new Rectangle(location.x, location.y, size.x, size.y)));
    }

    public String getDistribution() {
        return this.distribution;
    }

    public String getVersion() {
        return this.version;
    }

    @Override
    protected void okPressed() {
        boolean openQuestion = MessageDialog.openQuestion(getParentShell(), "Warning",
                Messages.getString("HadoopVersionDialog.confirmMsg"));//$NON-NLS-1$
        if (openQuestion) {
            super.okPressed();
        } else {
            super.cancelPressed();
        }
    }

    public Map<ECustomVersionGroup, Set<LibraryFile>> getImportLibLibraries() {
        Map<ECustomVersionGroup, Set<LibraryFile>> libMap = new HashMap<ECustomVersionGroup, Set<LibraryFile>>();
        if (isFromExistVersion) {
            IHadoopService hadoopService = null;
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IHadoopService.class)) {
                hadoopService = (IHadoopService) GlobalServiceRegister.getDefault().getService(IHadoopService.class);
            }
            if (hadoopService != null) {
                for (ECustomVersionGroup group : existVersionSelectionMap.keySet()) {

                    if (existVersionSelectionMap.get(group)) {
                        if (types != null) {
                            HashSet libInSameGroup = new HashSet<LibraryFile>();
                            boolean commonGroupCalculated = false;
                            for (ECustomVersionType type : types) {
                                if (type.getGroup() == group) {
                                    Set<String> hadoopLibraries = new HashSet<String>();
                                    if (ECustomVersionType.PIG == type || ECustomVersionType.PIG_HBASE == type
                                            || ECustomVersionType.PIG_HCATALOG == type) {
                                        hadoopLibraries = getLibrariesForPig(type);
                                    } else if (ECustomVersionType.MAP_REDUCE == type) {
                                        hadoopLibraries = getLibrariesForMapReduce(type);
                                    } else if (ECustomVersionType.SPARK == type || ECustomVersionType.SPARK_STREAMING == type) {
                                        hadoopLibraries = getLibrariesForSpark(type);
                                    } else {
                                        // fix for TDI-25676 HCATALOG and OOZIE should use the same jars as HDFS
                                        if (!commonGroupCalculated
                                                && (ECustomVersionType.HCATALOG == type || ECustomVersionType.OOZIE == type)) {
                                            type = ECustomVersionType.HDFS;
                                        }
                                        if (type == ECustomVersionType.HDFS) {
                                            commonGroupCalculated = true;
                                        }
                                        hadoopLibraries = hadoopService.getHadoopLibrariesByType(type, getDistribution(),
                                                getVersion());
                                    }
                                    Set<LibraryFile> convertToLibraryFile = customLibUtil.convertToLibraryFile(hadoopLibraries);
                                    libInSameGroup.addAll(convertToLibraryFile);
                                }
                            }

                            libMap.put(group, libInSameGroup);
                        }
                    }
                }

            }

        } else if (isFromZip) {
            Set<ECustomVersionGroup> groups = new HashSet<ECustomVersionGroup>();
            for (ECustomVersionGroup group : fromZipSelectionMap.keySet()) {
                if (fromZipSelectionMap.get(group)) {
                    groups.add(group);
                }
            }
            return customLibUtil.readZipFile(zipLocation, groups);
        }

        return libMap;
    }

    private Set<String> getLibrariesForMapReduce(ECustomVersionType type) {
        Set<String> neededLibraries = new HashSet<String>();
        INode node = CoreRuntimePlugin.getInstance().getDesignerCoreService()
                .getRefrenceNode("tMRConfiguration", ComponentCategory.CATEGORY_4_MAPREDUCE.getName());//$NON-NLS-1$

        IElementParameter elementParameter = node.getElementParameter("DISTRIBUTION");//$NON-NLS-1$
        if (elementParameter != null) {
            elementParameter.setValue(distribution);
        }

        elementParameter = node.getElementParameter("MR_VERSION");//$NON-NLS-1$
        if (elementParameter != null) {
            elementParameter.setValue(version);
        }

        List<ModuleNeeded> modulesNeeded = node.getModulesNeeded();
        for (ModuleNeeded module : modulesNeeded) {
            if (module.isRequired(node.getElementParameters())) {
                neededLibraries.add(module.getModuleName());
            }
        }
        return neededLibraries;
    }

    private Set<String> getLibrariesForSpark(ECustomVersionType type) {
        Set<String> neededLibraries = new HashSet<String>();

        String paletteType = ""; //$NON-NLS-1$
        if (ECustomVersionType.SPARK == type) {
            paletteType = ComponentCategory.CATEGORY_4_SPARK.getName();
        } else if (ECustomVersionType.SPARK_STREAMING == type) {
            paletteType = ComponentCategory.CATEGORY_4_SPARKSTREAMING.getName();
        }

        INode node = CoreRuntimePlugin.getInstance().getDesignerCoreService().getRefrenceNode("tSparkConfiguration", paletteType);//$NON-NLS-1$

        IElementParameter elementParameter = node.getElementParameter("DISTRIBUTION");//$NON-NLS-1$
        elementParameter.setValue(distribution);

        elementParameter = node.getElementParameter("SPARK_VERSION");//$NON-NLS-1$
        elementParameter.setValue(version);

        Map<String, Object> sparkConfigurations = typeConfigurations.get(type);
        String sparkMode = sparkConfigurations.get(HadoopConstants.SPARK_MODE).toString();

        elementParameter = node.getElementParameter("SPARK_MODE");//$NON-NLS-1$
        elementParameter.setValue(sparkMode);

        List<ModuleNeeded> modulesNeeded = node.getModulesNeeded();
        for (ModuleNeeded module : modulesNeeded) {
            if (module.isRequired(node.getElementParameters())) {
                neededLibraries.add(module.getModuleName());
            }
        }
        return neededLibraries;
    }

    private Set<String> getLibrariesForPig(ECustomVersionType type) {
        Set<String> neededLibraries = new HashSet<String>();
        INode node = CoreRuntimePlugin.getInstance().getDesignerCoreService().getRefrenceNode("tPigLoad");//$NON-NLS-1$

        IElementParameter elementParameter = node.getElementParameter("MAPREDUCE");//$NON-NLS-1$
        if (elementParameter != null) {
            elementParameter.setValue(true);
        }
        elementParameter = node.getElementParameter("DISTRIBUTION");//$NON-NLS-1$
        if (elementParameter != null) {
            elementParameter.setValue(distribution);
        }

        elementParameter = node.getElementParameter("PIG_VERSION");//$NON-NLS-1$
        if (elementParameter != null) {
            elementParameter.setValue(version);
        }

        elementParameter = node.getElementParameter("LOAD");//$NON-NLS-1$
        if (elementParameter != null) {
            if (ECustomVersionType.PIG_HBASE == type) {
                elementParameter.setValue("HBASESTORAGE");//$NON-NLS-1$
            } else if (ECustomVersionType.PIG_HCATALOG == type) {
                elementParameter.setValue("HCATLOADER");//$NON-NLS-1$
            }
        }

        List<ModuleNeeded> modulesNeeded = node.getModulesNeeded();
        for (ModuleNeeded module : modulesNeeded) {
            if (module.isRequired(node.getElementParameters())) {
                neededLibraries.add(module.getModuleName());
            }
        }
        return neededLibraries;
    }

    public Map<ECustomVersionType, Map<String, Object>> getTypeConfigurations() {
        return this.typeConfigurations;
    }

    public void setTypeConfigurations(Map<ECustomVersionType, Map<String, Object>> typeConfigurations) {
        this.typeConfigurations = typeConfigurations;
    }

}
