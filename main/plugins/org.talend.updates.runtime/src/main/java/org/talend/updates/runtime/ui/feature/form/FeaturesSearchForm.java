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
package org.talend.updates.runtime.ui.feature.form;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.updates.runtime.EUpdatesImage;
import org.talend.updates.runtime.feature.FeaturesManager;
import org.talend.updates.runtime.feature.FeaturesManager.SearchOption;
import org.talend.updates.runtime.feature.FeaturesManager.SearchResult;
import org.talend.updates.runtime.feature.model.Category;
import org.talend.updates.runtime.feature.model.Type;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.ui.ImageFactory;
import org.talend.updates.runtime.ui.feature.form.item.FeatureListViewer;
import org.talend.updates.runtime.ui.feature.model.IFeatureItem;
import org.talend.updates.runtime.ui.feature.model.IFeatureNavigator;
import org.talend.updates.runtime.ui.feature.model.IFeatureNavigator.INavigatorCallBack;
import org.talend.updates.runtime.ui.feature.model.impl.FeatureNavigator;
import org.talend.updates.runtime.ui.feature.model.impl.FeatureProgress;
import org.talend.updates.runtime.ui.feature.model.impl.FeatureTitle;
import org.talend.updates.runtime.ui.feature.model.impl.ModelAdapter;
import org.talend.updates.runtime.ui.feature.model.runtime.FeaturesManagerRuntimeData;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class FeaturesSearchForm extends AbstractFeatureForm {

    private ComboViewer marketsComboViewer;

    private ComboViewer categoriesComboViewer;

    private Text searchText;

    private Label findLabel;

    private Button searchButton;

    private FeatureListViewer featureListViewer;

    public FeaturesSearchForm(Composite parent, int style, FeaturesManagerRuntimeData runtimeData) {
        super(parent, style, runtimeData);
    }

    @Override
    protected void initControl(Composite parent) {
        marketsComboViewer = new ComboViewer(parent, SWT.READ_ONLY);
        categoriesComboViewer = new ComboViewer(parent, SWT.READ_ONLY);
        searchText = new Text(parent, SWT.BORDER);

        findLabel = new Label(parent, SWT.NONE);
        findLabel.setText(Messages.getString("ComponentsManager.form.install.label.find")); //$NON-NLS-1$
        findLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        searchButton = new Button(parent, SWT.NONE);
        searchButton.setImage(ImageProvider.getImage(EUpdatesImage.FIND_16));

        featureListViewer = new FeatureListViewer(parent, getRuntimeData(), SWT.BORDER);
        featureListViewer.setContentProvider(ArrayContentProvider.getInstance());
    }

    @Override
    protected void initLayout() {
        FormData formData = null;

        final int comboWidth = getComboWidth();
        final int horizonAlignWidth = getHorizonAlignWidth();
        final int versionAlignWidth = getVersionAlignWidth();

        formData = new FormData();
        formData.top = new FormAttachment(0, versionAlignWidth);
        formData.left = new FormAttachment(0, 0);
        findLabel.setLayoutData(formData);

        formData = new FormData();
        formData.top = new FormAttachment(findLabel, 0, SWT.CENTER);
        // formData.top = new FormAttachment(searchButton, 0, SWT.TOP);
        // formData.bottom = new FormAttachment(searchButton, 0, SWT.BOTTOM);
        formData.left = new FormAttachment(findLabel, horizonAlignWidth, SWT.RIGHT);
        formData.width = comboWidth;
        marketsComboViewer.getControl().setLayoutData(formData);
        marketsComboViewer.setContentProvider(ArrayContentProvider.getInstance());
        marketsComboViewer.setLabelProvider(new TypeLabelProvider());

        formData = new FormData();
        formData.top = new FormAttachment(marketsComboViewer.getControl(), 0, SWT.CENTER);
        // formData.top = new FormAttachment(searchButton, 0, SWT.TOP);
        // formData.bottom = new FormAttachment(searchButton, 0, SWT.BOTTOM);
        formData.left = new FormAttachment(marketsComboViewer.getControl(), horizonAlignWidth, SWT.RIGHT);
        formData.width = 100;
        categoriesComboViewer.getControl().setLayoutData(formData);
        categoriesComboViewer.setContentProvider(ArrayContentProvider.getInstance());
        categoriesComboViewer.setLabelProvider(new CategoryLabelProvider());

        formData = new FormData();
        formData.top = new FormAttachment(categoriesComboViewer.getControl(), 0, SWT.CENTER);
        // formData.top = new FormAttachment(searchButton, 0, SWT.TOP);
        // formData.bottom = new FormAttachment(searchButton, 0, SWT.BOTTOM);
        formData.left = new FormAttachment(categoriesComboViewer.getControl(), horizonAlignWidth, SWT.RIGHT);
        formData.right = new FormAttachment(searchButton, -1 * horizonAlignWidth, SWT.LEFT);
        searchText.setLayoutData(formData);

        formData = new FormData();
        formData.top = new FormAttachment(searchText, 0, SWT.CENTER);
        formData.right = new FormAttachment(100, 0);
        searchButton.setLayoutData(formData);

        formData = new FormData();
        formData.top = new FormAttachment(searchButton, versionAlignWidth, SWT.BOTTOM);
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(100, 0);
        formData.bottom = new FormAttachment(100, 0);
        featureListViewer.getControl().setLayoutData(formData);
    }

    @Override
    protected void initData() {
        updateSearchTooltip(null);
        featureListViewer.setCheckListener(getCheckListener());

        List<Type> types = new ArrayList<>();
        types.add(Type.ALL);
        marketsComboViewer.setInput(types);
        marketsComboViewer.setSelection(new StructuredSelection(Type.ALL));

        List<Category> categories = new ArrayList<>();
        categories.add(Category.ALL);
        categoriesComboViewer.setInput(categories);
        categoriesComboViewer.setSelection(new StructuredSelection(Category.ALL));

        doSearch();

    }

    @Override
    protected void addListeners() {
        searchButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                onSearchButtonSelected(e);
            }
        });
        searchText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                updateSearchTooltip(e);
            }
        });
        searchText.addTraverseListener(new TraverseListener() {

            @Override
            public void keyTraversed(TraverseEvent e) {
                onSearchTextTraversed(e);
            }
        });
    }

    private void onSearchTextTraversed(TraverseEvent e) {
        if (e != null) {
            switch (e.detail) {
            case SWT.TRAVERSE_RETURN:
                onSearchButtonSelected(null);
                break;
            default:
                // nothing to do
                break;
            }
        }
    }

    private void onSearchButtonSelected(SelectionEvent e) {
        doSearch();
    }

    private void doSearch() {
        final String keyword = searchText.getText();
        final Type type = getSelectedType();
        final Category category = getSelectedCategory();

        SearchOption searchOption = new SearchOption(type, category, keyword);
        searchOption.setPage(0);
        searchOption.setPageSize(6);
        doSearch(searchOption);
    }

    private void doSearch(SearchOption searchOption) {
        final FeatureProgress progress = showProgress();
        clearThreadPool();
        execute(new Runnable() {

            @Override
            public void run() {
                try {

                    ModalContext.run(new IRunnableWithProgress() {

                        @Override
                        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                            doSearch(monitor, searchOption);
                        }
                    }, true, progress.getProgressMonitor(), getDisplay());
                } catch (Exception e1) {
                    ExceptionHandler.process(e1);
                }
            }
        });
    }

    private void doSearch(IProgressMonitor monitor, SearchOption searchOption) {
        monitor.beginTask(Messages.getString("ComponentsManager.form.install.label.progress.begin"), IProgressMonitor.UNKNOWN); //$NON-NLS-1$
        List<IFeatureItem> features = new ArrayList<>();
        Collection<IFeatureItem> featureItems = Collections.EMPTY_SET;
        SearchResult searchResult = null;
        FeaturesManager componentsManager = getRuntimeData().getComponentsManager();
        try {
            searchResult = componentsManager.searchFeatures(monitor, searchOption);
            Collection<ExtraFeature> allComponentFeatures = searchResult.getCurrentPageResult();
            featureItems = ModelAdapter.convert(allComponentFeatures);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }

        if (featureItems == null) {
            featureItems = Collections.EMPTY_SET;
        }

        boolean showTitleBar = true;

        String titleMsg = ""; //$NON-NLS-1$
        if (featureItems.isEmpty()) {
            titleMsg = Messages.getString("ComponentsManager.form.install.label.head.searchResult.empty"); //$NON-NLS-1$
        } else {
            if (StringUtils.isBlank(searchOption.getKeyword())) {
                titleMsg = Messages.getString("ComponentsManager.form.install.label.head.featured"); //$NON-NLS-1$
            } else {
                showTitleBar = false;
            }
        }

        if (showTitleBar) {
            FeatureTitle title = new FeatureTitle();
            title.setTitle(titleMsg);
            features.add(title);
        }

        features.addAll(featureItems);

        if (searchResult != null && searchResult.hasMultiplePages()) {
            IFeatureNavigator navigator = createFeatureNavigator(searchResult);
            features.add(navigator);
        }

        if (monitor.isCanceled()) {
            return;
        }
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                if (monitor.isCanceled()) {
                    return;
                }
                featureListViewer.setInput(features);
            }
        });
    }

    private IFeatureNavigator createFeatureNavigator(SearchResult searchResult) {
        final FeatureNavigator navigator = new FeatureNavigator(searchResult);
        navigator.setNavigatorCallBack(new INavigatorCallBack() {

            @Override
            public void skipPage(int page) {
                SearchOption searchOption = navigator.getSearchResult().getSearchOption();
                searchOption.setPage(page);
                doSearch(searchOption);
            }

            @Override
            public void showPrevousPage() {
                SearchResult result = navigator.getSearchResult();
                SearchOption option = result.getSearchOption();
                option.setPage(result.getCurrentPage() - 1);
                doSearch(option);
            }

            @Override
            public void showNextPage() {
                SearchResult result = navigator.getSearchResult();
                SearchOption option = result.getSearchOption();
                option.setPage(result.getCurrentPage() + 1);
                doSearch(option);
            }
        });
        return navigator;
    }

    private void updateSearchTooltip(ModifyEvent e) {
        searchButton
                .setToolTipText(Messages.getString("ComponentsManager.form.install.label.toolTip.search", searchText.getText())); //$NON-NLS-1$
    }

    private Type getSelectedType() {
        ISelection selection = marketsComboViewer.getSelection();
        return (Type) ((StructuredSelection) selection).getFirstElement();
    }

    private Category getSelectedCategory() {
        ISelection selection = categoriesComboViewer.getSelection();
        return (Category) ((StructuredSelection) selection).getFirstElement();
    }

    private void clearThreadPool() {
        ImageFactory.getInstance().clearThreadPool();
    }

    private void execute(Runnable runnable) {
        ImageFactory.getInstance().getThreadPoolExecutor().execute(runnable);
    }

    private FeatureProgress showProgress() {
        List<IFeatureItem> progressList = new ArrayList<>();
        final FeatureProgress progress = new FeatureProgress();
        progressList.add(progress);
        featureListViewer.setInput(progressList);
        return progress;
    }

    private class CategoryLabelProvider extends LabelProvider {

        @Override
        public String getText(Object element) {
            if (element instanceof Category) {
                return ((Category) element).getLabel();
            } else {
                return super.getText(element);
            }
        }
    }

    private class TypeLabelProvider extends LabelProvider {

        @Override
        public String getText(Object element) {
            if (element instanceof Type) {
                return ((Type) element).getLabel();
            } else {
                return super.getText(element);
            }
        }
    }

}
