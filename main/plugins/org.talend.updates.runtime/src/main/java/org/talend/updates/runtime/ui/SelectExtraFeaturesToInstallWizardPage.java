// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.updates.runtime.ui;

import java.util.Set;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.ISetChangeListener;
import org.eclipse.core.databinding.observable.set.SetChangeEvent;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.equinox.internal.p2.metadata.InstallableUnit;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableSetTreeContentProvider;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.databinding.wizard.WizardPageSupport;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.model.IuP2ExtraFeature;

/**
 * created by sgandon on 25 févr. 2013 Detailled comment
 * 
 */
public class SelectExtraFeaturesToInstallWizardPage extends WizardPage {

    private Tree tree;

    private final UpdateWizardModel updateWizardModel;

    private CheckboxTreeViewer checkboxTreeViewer;

    private StyledText featureDescriptionText;

    public DataBindingContext dbc;

    /**
     * Create the wizard.
     * 
     * @param updateWizardModel
     * @wbp.parser.constructor
     */
    public SelectExtraFeaturesToInstallWizardPage(UpdateWizardModel updateWizardModel) {
        super("wizardPage"); //$NON-NLS-1$
        this.updateWizardModel = updateWizardModel;
        setTitle(Messages.getString("SelectExtraFeaturesToInstallWizardPage.wizard.page.title")); //$NON-NLS-1$
        setDescription(Messages.getString("SelectExtraFeaturesToInstallWizardPage.wizard.page.description")); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
     */
    @Override
    public boolean canFlipToNextPage() {
        return super.canFlipToNextPage() && !updateWizardModel.selectedExtraFeatures.isEmpty()
                && updateWizardModel.canConfigureUpdateSiteLocation();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
     */
    @Override
    public boolean isPageComplete() {
        return updateWizardModel.hasDoNotShowThisAgainChanged ? true : super.isPageComplete();
    }

    /**
     * Create contents of the wizard.
     * 
     * @param parent
     */
    @SuppressWarnings("unchecked")
    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);

        setControl(container);
        container.setLayout(new GridLayout(2, false));

        Label lblNewLabel = new Label(container, SWT.NONE);
        lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
        lblNewLabel.setText(Messages.getString("SelectExtraFeaturesToInstallWizardPage.feature.list.label")); //$NON-NLS-1$

        Composite featureComposite = new Composite(container, SWT.NONE);
        featureComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        TreeColumnLayout friendsColumnLayout = new TreeColumnLayout();
        featureComposite.setLayout(friendsColumnLayout);

        checkboxTreeViewer = new CheckboxTreeViewer(featureComposite, SWT.BORDER | SWT.FULL_SELECTION);
        checkboxTreeViewer.setSorter(new ViewerSorter() {// regroupt by class type and then by name

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.ViewerComparator#category(java.lang.Object)
             */
            @Override
            public int category(Object element) {
                return element.getClass().hashCode();
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.viewers.Viewer,
             * java.lang.Object, java.lang.Object)
             */
            @Override
            public int compare(Viewer viewer, Object e1, Object e2) {
                if ((e2 instanceof ExtraFeature) && !((ExtraFeature) e2).mustBeInstalled()) {
                    return -1;
                }
                return 1;
            }
        });
        tree = checkboxTreeViewer.getTree();
        tree.setSize(400, 155);
        tree.setHeaderVisible(true);
        tree.setLinesVisible(true);
        final Realm realm = SWTObservables.getRealm(checkboxTreeViewer.getControl().getDisplay());

        (updateWizardModel.availableExtraFeatures).addChangeListener(new IChangeListener() {
            
            @Override
            public void handleChange(ChangeEvent event) {
                System.out.println();
            }
        });
        final IObservableFactory setFactory = new IObservableFactory() {
            public IObservable createObservable(final Object target) {
                if (target instanceof WritableSet) {
                    return (IObservableSet) target;
                }
                InstallableUnit iu = new InstallableUnit();
                iu.setProperty(IInstallableUnit.PROP_NAME, "test");
                IuP2ExtraFeature p2e = new IuP2ExtraFeature(iu,"abc");
                
//                Set<ExtraFeature> features = new HashSet<>();
//                features.add(p2e);
                WritableSet set = new WritableSet(realm);
                set.add(p2e);
                return set;
//                if (target instanceof Object[]) {
//                    return Observables.staticObservableList(realm, Arrays.asList((Object[]) target));
//                }
//                Object value;
//                if (target instanceof FakeRoot) {
//                    value = ((FakeRoot) target).getRoot();
//                    if (value == null) {
//                        return new ObservableList(Collections.EMPTY_LIST, treeElementClass) {
//                            // empty list
//                        };
//                    }
//                } else {
//                    value = target;
//                }
//                if (AbstractSWTWidgetRidget.isBean(treeElementClass)) {
//                    return BeansObservables.observeList(realm, value, childrenAccessor, treeElementClass);
//                } else {
//                    return PojoObservables.observeList(realm, value, childrenAccessor, treeElementClass);
//                }
            }
        };

        // Label provider for the tree
        IBaseLabelProvider labelProvider = new CellLabelProvider() {

            @Override
            public void update(ViewerCell cell) {
                if (cell.getColumnIndex() == 0) {
                    cell.setText(((ExtraFeature)cell.getElement()).getName());
                } else {
                    cell.setText(((ExtraFeature)cell.getElement()).getVersion());
                }
            }
            
        };
        
     // UpdatableTreeContentProvider converts an ITreeProvider into a
        // standard JFace content provider
        ObservableSetTreeContentProvider contentProvider = new ObservableSetTreeContentProvider(
                setFactory, null);

        checkboxTreeViewer.setContentProvider(contentProvider);
        checkboxTreeViewer.setLabelProvider(labelProvider);

        // For the ITreeProvider above, it doesn't matter what we select as the
        // input.
        checkboxTreeViewer.setInput(updateWizardModel.availableExtraFeatures);
        
        TreeColumn featureNameColumn = new TreeColumn(tree, SWT.NONE);
        featureNameColumn.setText(Messages.getString("SelectExtraFeaturesToInstallWizardPage.feature.column.name.name")); //$NON-NLS-1$
        friendsColumnLayout.setColumnData(featureNameColumn, new ColumnWeightData(3, true));

        TreeColumn tblclmnVersion = new TreeColumn(tree, SWT.NONE);
        friendsColumnLayout.setColumnData(tblclmnVersion, new ColumnWeightData(1, true));
        tblclmnVersion.setText(Messages.getString("SelectExtraFeaturesToInstallWizardPage.features.column.version.name")); //$NON-NLS-1$

        Label lblDescription = new Label(container, SWT.NONE);
        lblDescription.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
        lblDescription.setText(Messages.getString("SelectExtraFeaturesToInstallWizardPage.description.label")); //$NON-NLS-1$

        featureDescriptionText = new StyledText(container, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
        GridData gd_featureDescriptionText = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        gd_featureDescriptionText.heightHint = 61;
        featureDescriptionText.setLayoutData(gd_featureDescriptionText);
        initDataBindings();
        updateSelectedState(updateWizardModel.availableExtraFeatures);
        updatePageStatus();
    }

    protected DataBindingContext initDataBindings() {
        dbc = new DataBindingContext();
//        IListProperty childrenProp = new DelegatingListProperty() {
//
//            protected IListProperty doGetDelegate(Object source) {
//                if (source instanceof ExtraFeature) {
//                    return null;
//                }
//                IListProperty f = BeanProperties.list(RootFeature.class, "children");
//                return categories;
//            }
//
//        };
//        // bind the table elements with the updateWizardModel availableExtraFeatures and the columns with the
//        // extrafeatures values
//        ViewerSupport.bind(checkboxTreeViewer, updateWizardModel.availableExtraFeatures, childrenProp,
//                new IValueProperty[] { PojoProperties.value(ExtraFeature.class, "name"), //$NON-NLS-1$
//                        PojoProperties.value(ExtraFeature.class, "version") }); //$NON-NLS-1$
        // bind selecting of the check boxes to the selected extra features set in the model
        dbc.bindSet(ViewersObservables.observeCheckedElements(checkboxTreeViewer, ExtraFeature.class),
                updateWizardModel.selectedExtraFeatures);
        // bind the table selection desctiption to the text field
        IObservableValue selectedFeature = ViewersObservables.observeSingleSelection(checkboxTreeViewer);
        dbc.bindValue(SWTObservables.observeText(featureDescriptionText),
                PojoObservables.observeDetailValue(selectedFeature, "description", String.class)); //$NON-NLS-1$
        // add a validator for feature selection because SetObservable does not provide any validator.
        dbc.addValidationStatusProvider(updateWizardModel.new FeatureSelectionValidator());
        WizardPageSupport.create(this, dbc);
        // add a listener to update the description and enabled state when avaialble features are added and also
        // add them to the selected list if the must be installed
        updateWizardModel.availableExtraFeatures.addSetChangeListener(new ISetChangeListener() {

            @Override
            public void handleSetChange(SetChangeEvent arg0) {
                updatePageStatus();
                updateSelectedState(arg0.diff.getAdditions());
            }
        });
        return dbc;

    }

    /**
     * when an element is added to the feature list this get called so that the selected state of the item gets
     * automatically set according to it's mustBeInstalled value
     * 
     * @param arg0
     */
    protected void updateSelectedState(Set<ExtraFeature> features) {
        for (ExtraFeature ef : features) {
            if (ef.mustBeInstalled()) {
                updateWizardModel.selectedExtraFeatures.add(ef);
            } // else do not select caus must not be installed
        }
    }

    /**
     * DOC sgandon Comment method "updateEnabledStatus".
     */
    protected void updatePageStatus() {
        // tell the user when no feature is to be installed.
        if (updateWizardModel.availableExtraFeatures.isEmpty()) {
            featureDescriptionText.setText(Messages.getString("SelectExtraFeaturesToInstallWizardPage.no.feauture.to.install")); //$NON-NLS-1$
            // table.setEnabled(false);
        } else {
            featureDescriptionText.setText(""); //$NON-NLS-1$
        }
    }
}
