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
package org.talend.updates.runtime.ui.feature.form.item;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.IFormColors;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.swt.composites.GradientCanvas;
import org.talend.updates.runtime.feature.FeaturesManager.SearchResult;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.ui.feature.model.IFeatureNavigator;
import org.talend.updates.runtime.ui.feature.model.runtime.FeaturesManagerRuntimeData;
import org.talend.updates.runtime.ui.util.UIUtils;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class FeatureListNavigator extends AbstractControlListItem<IFeatureNavigator> {

    private Label verticalLine;

    private Label horizonLine;

    private Composite prevNextPanel;

    private Composite skipPanel;

    private Button previousPageButton;

    private Button nextPageButton;

    private Button skipButton;

    private Text currentPageText;

    private Label pagesLabel;

    private Label curPageLabel;

    private StyledText totalText;

    public FeatureListNavigator(Composite parent, int style, FeaturesManagerRuntimeData runtimeData, IFeatureNavigator element) {
        super(parent, style, runtimeData, element);
    }

    @Override
    protected Composite createPanel() {
        FormColors formColors = UIUtils.getFormColors();
        GradientCanvas panel = new GradientCanvas(this, SWT.NONE);
        panel.setSeparatorVisible(true);
        panel.setSeparatorAlignment(SWT.TOP);
        panel.setBackgroundGradient(new Color[] { formColors.getColor(IFormColors.H_GRADIENT_END),
                formColors.getColor(IFormColors.H_GRADIENT_START) }, new int[] { 100 }, true);
        panel.putColor(GradientCanvas.H_BOTTOM_KEYLINE1, formColors.getColor(IFormColors.H_GRADIENT_END));
        panel.putColor(GradientCanvas.H_BOTTOM_KEYLINE2, formColors.getColor(IFormColors.H_GRADIENT_START));

        FormLayout layout = new FormLayout();
        layout.marginWidth = 10;
        panel.setLayout(layout);

        FormData formData = new FormData();
        formData.height = 50;
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(100, 0);
        panel.setLayoutData(formData);
        return panel;
    }

    @Override
    protected void initControl(Composite panel) {
        super.initControl(panel);

        verticalLine = new Label(panel, SWT.NONE);
        horizonLine = new Label(panel, SWT.NONE);

        totalText = new StyledText(panel, SWT.SINGLE);

        prevNextPanel = new Composite(panel, SWT.NONE);
        previousPageButton = new Button(prevNextPanel, SWT.NONE);
        previousPageButton.setText(Messages.getString("ComponentsManager.form.navigator.previousPage")); //$NON-NLS-1$
        previousPageButton.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        curPageLabel = new Label(prevNextPanel, SWT.NONE);
        nextPageButton = new Button(prevNextPanel, SWT.NONE);
        nextPageButton.setText(Messages.getString("ComponentsManager.form.navigator.next")); //$NON-NLS-1$
        nextPageButton.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        skipPanel = new Composite(panel, SWT.NONE);
        currentPageText = new Text(skipPanel, SWT.BORDER | SWT.RIGHT | SWT.V_SCROLL);
        currentPageText.getVerticalBar().setVisible(false);
        pagesLabel = new Label(skipPanel, SWT.NONE);
        skipButton = new Button(skipPanel, SWT.NONE);
        skipButton.setText(Messages.getString("ComponentsManager.form.navigator.skip")); //$NON-NLS-1$
        skipButton.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
    }

    @Override
    protected void layoutControl() {
        super.layoutControl();
        int horizonAlignWidth = getHorizonAlignWidth();

        FormData formData = null;

        // base lines
        formData = new FormData();
        formData.top = new FormAttachment(0, 0);
        formData.bottom = new FormAttachment(100, 0);
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(0, 0);
        verticalLine.setLayoutData(formData);

        formData = new FormData();
        formData.top = new FormAttachment(0, 0);
        formData.bottom = new FormAttachment(0, 0);
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(100, 0);
        horizonLine.setLayoutData(formData);

        // total
        formData = new FormData();
        formData.left = new FormAttachment(0, 0);
        formData.top = new FormAttachment(verticalLine, 0, SWT.CENTER);
        totalText.setLayoutData(formData);

        // previous/next button
        FormLayout layout = new FormLayout();
        prevNextPanel.setLayout(layout);
        formData = new FormData();
        formData.top = new FormAttachment(verticalLine, 0, SWT.CENTER);
        formData.left = new FormAttachment(horizonLine, 0, SWT.CENTER);
        prevNextPanel.setLayoutData(formData);
        int preNexBtnWidth = 0;
        previousPageButton.pack();
        nextPageButton.pack();
        Point previousPageBtnSize = previousPageButton.getSize();
        Point nextPageBtnSize = nextPageButton.getSize();
        if (previousPageBtnSize.x < nextPageBtnSize.x) {
            preNexBtnWidth = nextPageBtnSize.x;
        } else {
            preNexBtnWidth = previousPageBtnSize.x;
        }
        formData = new FormData();
        formData.left = new FormAttachment(0, 0);
        formData.width = preNexBtnWidth;
        previousPageButton.setLayoutData(formData);
        formData = new FormData();
        formData.left = new FormAttachment(previousPageButton, horizonAlignWidth, SWT.RIGHT);
        formData.top = new FormAttachment(previousPageButton, 0, SWT.CENTER);
        curPageLabel.setLayoutData(formData);
        formData = new FormData();
        formData.left = new FormAttachment(curPageLabel, horizonAlignWidth, SWT.RIGHT);
        formData.top = new FormAttachment(curPageLabel, 0, SWT.CENTER);
        formData.width = preNexBtnWidth;
        formData.right = new FormAttachment(100, 0);
        nextPageButton.setLayoutData(formData);

        // skips
        layout = new FormLayout();
        skipPanel.setLayout(layout);
        formData = new FormData();
        formData.right = new FormAttachment(100, 0);
        formData.top = new FormAttachment(verticalLine, 0, SWT.CENTER);
        skipPanel.setLayoutData(formData);

        formData = new FormData();
        formData.right = new FormAttachment(100, 0);
        skipButton.setLayoutData(formData);

        formData = new FormData();
        formData.right = new FormAttachment(skipButton, -2 * horizonAlignWidth, SWT.LEFT);
        formData.top = new FormAttachment(skipButton, 0, SWT.CENTER);
        pagesLabel.setLayoutData(formData);

        formData = new FormData();
        formData.right = new FormAttachment(pagesLabel, -1 * horizonAlignWidth, SWT.LEFT);
        formData.top = new FormAttachment(skipButton, 0, SWT.CENTER);
        formData.width = 18;
        currentPageText.setLayoutData(formData);
    }

    @Override
    protected void addListeners() {
        super.addListeners();
        previousPageButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                onPreviousPageButtonClicked(e);
            }
        });

        nextPageButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                onNextPageButtonClicked(e);
            }
        });

        skipButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                onSkipPageButtonClicked(e);
            }
        });

        currentPageText.addVerifyListener(new VerifyListener() {

            @Override
            public void verifyText(VerifyEvent e) {
                onSkipPageTextVerify(e);
            }
        });

        currentPageText.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                onSkipPageTextModified(e);
            }
        });

        currentPageText.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                onSkipPageFocus(e);
            }

            @Override
            public void focusLost(FocusEvent e) {
                onSkipPageFocusLost(e);
            }
        });

        currentPageText.addTraverseListener(new TraverseListener() {

            @Override
            public void keyTraversed(TraverseEvent e) {
                onSkipPageTextTraversed(e);
            }
        });

        currentPageText.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseScrolled(MouseEvent e) {
                onSkipPageMouseWheel(e);
            }
        });
    }

    private void onSkipPageTextTraversed(TraverseEvent e) {
        if (e != null) {
            switch (e.detail) {
            case SWT.TRAVERSE_RETURN:
                onSkipPageButtonClicked(null);
                break;
            default:
                // nothing to do
                break;
            }
        }
    }

    private void onPreviousPageButtonClicked(SelectionEvent e) {
        getData().getNavigatorCallBack().showPrevousPage();
    }

    private void onNextPageButtonClicked(SelectionEvent e) {
        getData().getNavigatorCallBack().showNextPage();
    }

    private void onSkipPageButtonClicked(SelectionEvent e) {
        getData().getNavigatorCallBack().skipPage(getSkipPage());
    }

    private void updatePrevNextButton() {
        int curPage = getDefaultPage();
        if (curPage <= 1) {
            previousPageButton.setEnabled(false);
        } else {
            previousPageButton
                    .setToolTipText(Messages.getString("ComponentsManager.form.navigator.toolTip.previousPage", curPage - 1)); //$NON-NLS-1$
        }
        if (getData().getSearchResult().getTotalPageSize() <= curPage) {
            nextPageButton.setEnabled(false);
        } else {
            nextPageButton.setToolTipText(Messages.getString("ComponentsManager.form.navigator.toolTip.nextPage", curPage + 1)); //$NON-NLS-1$
        }
    }

    private void onSkipPageTextVerify(VerifyEvent e) {
        if (!checkInteger(e)) {
            return;
        }
        String text = currentPageText.getText();
        String newText = text.substring(0, e.start) + e.text;
        if (e.end < text.length()) {
            newText = newText + text.substring(e.end);
        }

        try {
            int i = Integer.valueOf(newText);
            if (i < 1 || getData().getSearchResult().getTotalPageSize() < i) {
                e.doit = false;
                if (StringUtils.isEmpty(text)) {
                    onSkipPageTextModified(null);
                }
            }
        } catch (Exception ex) {
            skipButton.setEnabled(false);
        }
    }

    private void onSkipPageTextModified(ModifyEvent e) {
        String text = currentPageText.getText();

        clearError();
        skipButton.setEnabled(true);
        currentPageText.setBackground(getBackground());
        currentPageText.setToolTipText(
                Messages.getString("ComponentsManager.form.navigator.toolTip.currentPage", text, skipButton.getText(), //$NON-NLS-1$
                        getDefaultPage()));

        if (StringUtils.isBlank(text)) {
            String errorMsg = Messages.getString("ComponentsManager.form.navigator.exception.currentPageBlank"); //$NON-NLS-1$
            showError(errorMsg);

            currentPageText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
            currentPageText.setToolTipText(errorMsg);
            skipButton.setEnabled(false);
            return;
        }

    }

    private void onSkipPageFocus(FocusEvent e) {
        currentPageText.selectAll();
    }

    private void onSkipPageFocusLost(FocusEvent e) {
        // currentPageText.setText("" + getDefaultPage()); //$NON-NLS-1$
    }

    private void onSkipPageMouseWheel(MouseEvent e) {
        if (e == null) {
            return;
        }
        int count = e.count;
        int curPage = getDefaultPage();
        try {
            curPage = Integer.valueOf(currentPageText.getText());
        } catch (Exception ex) {
            ExceptionHandler.process(ex);
        }
        if (0 < count) {
            currentPageText.setText(Integer.toString(curPage - 1));
        } else {
            currentPageText.setText(Integer.toString(curPage + 1));
        }
    }

    private boolean checkInteger(VerifyEvent e) {
        try {
            if (StringUtils.isNotBlank(e.text)) {
                Integer i = Integer.valueOf(e.text);
                if (i <= 0) {
                    e.doit = false;
                }
            }
        } catch (Exception ex) {
            e.doit = false;
        } finally {
            if (!e.doit) {
                return e.doit;
            }
        }
        if (e.character != 0 && e.keyCode != SWT.BS && e.keyCode != SWT.DEL && !Character.isDigit(e.character)) {
            e.doit = false;
        } else {
            if (e.character == '0' && e.start == 0) {
                e.doit = false;
            } else {
                e.doit = true;
            }
        }
        return e.doit;
    }

    private int getSkipPage() {
        String curPageText = currentPageText.getText();
        int curPage = 1;
        if (StringUtils.isNotBlank(curPageText)) {
            curPage = Integer.valueOf(curPageText);
        }
        return curPage - 1;
    }

    @Override
    protected void initData() {
        super.initData();
        loadData();
    }

    private void loadData() {
        IFeatureNavigator navigator = getData();

        SearchResult searchResult = navigator.getSearchResult();

        int totalSize = searchResult.getTotalSize();
        String totalLabel = Messages.getString("ComponentsManager.form.navigator.label.totalSize"); //$NON-NLS-1$
        StyleRange totalStyleRange = new StyleRange(0, totalLabel.length(), null, null, SWT.BOLD);
        totalText.setText(totalLabel + " " + totalSize); //$NON-NLS-1$
        totalText.setStyleRange(totalStyleRange);

        curPageLabel.setText("<" + getDefaultPage() + ">"); //$NON-NLS-1$ //$NON-NLS-2$
        currentPageText.setText("" + getDefaultPage()); //$NON-NLS-1$

        pagesLabel.setText("/" + searchResult.getTotalPageSize()); //$NON-NLS-1$

        updatePrevNextButton();

        getPanel().layout();
    }

    private int getDefaultPage() {
        return getData().getSearchResult().getCurrentPage() + 1;
    }

    @Override
    protected void showError(String msg) {
        super.showError(msg);
    }
}
