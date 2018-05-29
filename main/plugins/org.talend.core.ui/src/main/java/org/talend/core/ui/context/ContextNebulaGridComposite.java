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
package org.talend.core.ui.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.utils.threading.ExecutionLimiter;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.context.ContextUtils;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.context.JobContextParameter;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.types.ContextParameterJavaTypeManager;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.Item;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.ui.context.ContextTreeTable.ContextTreeNode;
import org.talend.core.ui.context.model.ContextTabChildModel;
import org.talend.core.ui.context.model.table.ContextTableTabChildModel;
import org.talend.core.ui.context.model.table.ContextTableTabParentModel;
import org.talend.core.ui.context.nattableTree.ContextNatTableUtils;
import org.talend.core.ui.i18n.Messages;
import org.talend.core.utils.TalendQuoteUtils;

/**
 * created by ldong on Jul 8, 2014 Detailled comment
 * 
 */
public class ContextNebulaGridComposite extends AbstractContextTabEditComposite {

    public static final int CONTEXT_COLUMN_WIDTH = 200;

    public static final String NEW_PARAM_NAME = "new"; //$NON-NLS-1$

    private TreeViewer viewer;

    private IContextModelManager modelManager = null;

    private ConfigureContextAction configContext;

    private Combo contextsCombo;

    private Button contextConfigButton;

    private ContextManagerHelper helper;

    private List<Button> buttonList;

    private Button addButton;

    private Button removeButton;

    private Button moveUpButton;

    private Button moveDownButton;

    private Button selectContextVariablesButton;

    private Composite contextTableComp;

    private Composite availableLabelComp;

    private Composite messageComp;

    private Composite contextsSelectComp;

    private Composite buttonsComp;

    private ContextTreeTable treeTable;

    /**
     * Constructor.
     * 
     * @param parent
     * @param style
     */
    public ContextNebulaGridComposite(Composite parent, IContextModelManager manager) {
        super(parent, SWT.NONE);
        modelManager = manager;
        buttonList = new ArrayList<Button>();
        this.helper = new ContextManagerHelper(manager.getContextManager());
        this.setLayout(GridLayoutFactory.swtDefaults().spacing(0, 0).create());
        initializeUI();
        // for bug TDI-32674 to set different bgColor of ContextView and RepositoryContextComposite. 
        if(modelManager instanceof ContextComposite
                && ((ContextComposite) modelManager).isRepositoryContext()) {
        	this.setBackground(parent.getBackground());
        } else {
        	// CSS
        	CoreUIPlugin.setCSSClass(this, this.getClass().getSimpleName());
        }
    }

    @Override
    public IContextModelManager getContextModelManager() {
        return this.modelManager;
    }

    public IContextManager getContextManager() {
        return modelManager.getContextManager();
    }

    /**
     * zwang Comment method "initializeUI".
     * 
     * @param viewer
     */
    private void initializeUI() {
        if (getContextManager() == null) {
            createContextNotAvailableGroup(this);
        } else {
            if (!ContextNatTableUtils.checkIsInstallExternalJar()) {
                createMessageGroup(this);
            } else {
                createNatTableGroup(this);

                createNatTable();

                createButtonsGroup(this);

                addListener(SWT.Resize, resizeListener);
            }
        }
    }

    private void createContextNotAvailableGroup(Composite parentComposite) {
        availableLabelComp = new Composite(parentComposite, SWT.NULL);
        availableLabelComp.setLayout(new GridLayout());
        availableLabelComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        Label contextUnAvailableLabel = new Label(availableLabelComp, SWT.NULL);
        contextUnAvailableLabel.setText(Messages.getString("ContextNebulaComposite.ContextsUnAvailable")); //$NON-NLS-1$
        availableLabelComp.getParent().layout();
        availableLabelComp.layout();
    }

    private void createMessageGroup(Composite parentComposite) {
        messageComp = new ContextMissSettingComposite(parentComposite, SWT.NULL);
        messageComp.setLayout(new GridLayout(3, false));
        messageComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        messageComp.getParent().layout();
        messageComp.layout();
    }

    private void createNatTableGroup(Composite parentComposite) {
        contextTableComp = new Composite(parentComposite, SWT.NULL);
        GridLayout dataTableLayout = new GridLayout(2, Boolean.FALSE);
        contextTableComp.setLayout(dataTableLayout);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        contextTableComp.setLayoutData(gridData);
        treeTable = new ContextTreeTable(modelManager);
    }

    private void createNatTable() {
        ContextTreeTable.TControl tControl = treeTable.createTable(contextTableComp);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(tControl.getControl());

        configContext = new ConfigureContextAction(modelManager, this.getShell());
        contextConfigButton = new Button(contextTableComp, SWT.NULL);
        GridData addContextGridData = new GridData();
        addContextGridData.verticalAlignment = SWT.TOP;
        contextConfigButton.setLayoutData(addContextGridData);
        contextConfigButton.setImage(ImageProvider.getImage(EImage.ADD_ICON));
        contextConfigButton.setToolTipText(configContext.getText());
        contextConfigButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                configContext.run();
            }
        });
    }

    private void createButtonsGroup(Composite parentComposite) {
        buttonsComp = new Composite(parentComposite, SWT.NULL);
        buttonsComp.setLayout(GridLayoutFactory.swtDefaults().spacing(0, 0).margins(0, 0).numColumns(7).create());
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.DOWN).grab(true, false).applyTo(buttonsComp);
        buttonList.clear();
        addButton = createAddPushButton(buttonsComp);
        buttonList.add(addButton);
        removeButton = createRemovePushButton(buttonsComp);
        buttonList.add(removeButton);

        boolean isRepositoryContext = (modelManager instanceof ContextComposite)
                && ((ContextComposite) modelManager).isRepositoryContext();
        if (!isRepositoryContext) {// for bug 7393
            moveUpButton = createMoveUpPushButton(buttonsComp);
            buttonList.add(moveUpButton);
            moveDownButton = createMoveDownPushButton(buttonsComp);
            buttonList.add(moveDownButton);
        }

        if ((modelManager instanceof ContextComposite) && !((ContextComposite) modelManager).isRepositoryContext()) {
            selectContextVariablesButton = createSelectContextVariablesPushButton(buttonsComp);
            buttonList.add(selectContextVariablesButton);
        }
        // move the context group from the top to the bottom
        Composite layoutComposite = new Composite(buttonsComp, SWT.NULL);
        layoutComposite.setLayout(GridLayoutFactory.swtDefaults().spacing(0, 0).numColumns(1).create());
        GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.DOWN).grab(true, false).applyTo(layoutComposite);

        createContextsGroup(layoutComposite);
    }

    private void createContextsGroup(Composite parentComposite) {
        contextsSelectComp = new Composite(parentComposite, SWT.NULL);
        contextsSelectComp.setLayout(GridLayoutFactory.swtDefaults().spacing(10, 0).margins(0, 0).numColumns(2).create());
        GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.DOWN).grab(true, false).applyTo(contextsSelectComp);
        GridLayout layout2 = (GridLayout) contextsSelectComp.getLayout();
        layout2.marginHeight = 0;
        layout2.marginTop = 0;
        layout2.marginBottom = 0;

        Label contextSeletLabel = new Label(contextsSelectComp, SWT.NULL);
        contextSeletLabel.setText(Messages.getString("ContextNebulaComposite.ContextGroupLabel")); //$NON-NLS-1$
        contextsCombo = new Combo(contextsSelectComp, SWT.READ_ONLY);
        contextsCombo.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Object obj = e.getSource();
                String selectContext = ((Combo) obj).getText();
                IContext defaultContext = modelManager.getContextManager().getDefaultContext();
                if (selectContext.equals(defaultContext.getName())) {
                } else {
                    IContext newSelContext = null;
                    for (IContext enviroContext : modelManager.getContextManager().getListContext()) {
                        if (selectContext.equals(enviroContext.getName())) {
                            newSelContext = enviroContext;
                        }
                    }
                    modelManager.onContextChangeDefault(modelManager.getContextManager(), newSelContext);
                    refresh();
                }

            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {

            }

        });
    }

    private Button createAddPushButton(final Composite parent) {
        Button addPushButton = new Button(parent, SWT.PUSH);
        addPushButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                IContextParameter parameter = (IContextParameter) createNewEntry();
                if (parameter != null) {
                    // set the source to built-in
                    parameter.setSource(IContextParameter.BUILT_IN);
                    modelManager.onContextAddParameter(getContextManager(), parameter);

                    // see feature 4661: Add an option to propagate when add or remove a variable in a repository
                    // context to jobs/joblets.
                    if (ContextUtils.isPropagateContextVariable() && getContextManager() != null) {
                        IContextManager manager = getContextManager();
                        if (manager != null && manager instanceof JobContextManager) {
                            JobContextManager jobManger = (JobContextManager) manager;
                            // set updated flag.
                            jobManger.setModified(true);
                            jobManger.addNewParameters(parameter.getName());
                        }
                    }

                }
            }

        });
        Image image = ImageProvider.getImage(EImage.ADD_ICON);
        addPushButton.setImage(image);
        return addPushButton;
    }

    private Button createRemovePushButton(final Composite parent) {
        Button removePushButton = new Button(parent, SWT.PUSH);
        removePushButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {

                IStructuredSelection sel = treeTable.getSelection();

                if (treeTable.getSelection() != null) {

                    Object[] obj = new Object[sel.toList().size()];

                    int i = 0;
                    for (Object node : sel.toList().toArray()) {
                        if (node instanceof ContextTreeNode) {
                            obj[i++] = ((ContextTreeNode) node).getTreeData();
                        }

                    }

                    for (Object object : obj) { // multi delete
                        if (object == null) {
                            return;
                        }
                        if (object instanceof ContextTableTabParentModel) {
                            ContextTableTabParentModel parentModel = (ContextTableTabParentModel) object;
                            removeParentModelInGroupBySource(parentModel);
                        } else if (object instanceof ContextTableTabChildModel) {
                            ContextTableTabChildModel childModel = (ContextTableTabChildModel) object;
                            removeChildModelInGroupBySource(childModel);
                        }
                    }
                    modelManager.refresh();
                    setButtonEnableState();
                    
                    if (!treeTable.getSelection().isEmpty()) {
                        treeTable.clearSelection();
                    }
                }
            }
        });

        Image image = ImageProvider.getImage(EImage.DELETE_ICON);
        removePushButton.setImage(image);
        return removePushButton;
    }

    private Button createMoveUpPushButton(final Composite parent) {
        Button moveUpPushButton = new Button(parent, SWT.PUSH);
        Image image = ImageProvider.getImage(EImage.UP_ICON);
        moveUpPushButton.setImage(image);
        moveUpPushButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                IStructuredSelection sel = treeTable.getSelection();
                if (ContextManagerHelper.changeContextOrder(sel, modelManager, true)) {
                    setButtonEnableState();
                }
            }
        });
        return moveUpPushButton;
    }

    private Button createMoveDownPushButton(final Composite parent) {
        Button moveDownPushButton = new Button(parent, SWT.PUSH);
        Image image = ImageProvider.getImage(EImage.DOWN_ICON);
        moveDownPushButton.setImage(image);
        moveDownPushButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                IStructuredSelection sel = treeTable.getSelection();
                if (ContextManagerHelper.changeContextOrder(sel, modelManager, false)) {
                    setButtonEnableState();
                }
            }

        });
        return moveDownPushButton;
    }

    private Button createSelectContextVariablesPushButton(final Composite parent) {
        Button selectContextVariablesPushButton = new Button(parent, SWT.PUSH);
        Image image = ImageProvider.getImage(ECoreImage.CONTEXT_ICON);
        selectContextVariablesPushButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                SelectRepositoryContextDialog dialog = new SelectRepositoryContextDialog(getContextModelManager(), parent
                        .getShell(), helper);
                if (dialog.open() == Dialog.OK) {
                    // ADD msjian TDQ-9629: if the current perspective is dataprofiling, change all to builtin context
                    IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                    if (activePage != null) {
                        if ("org.talend.dataprofiler.DataProfilingPerspective".equals(activePage.getPerspective().getId())) { //$NON-NLS-1$
                            IContextManager contextManager = getContextModelManager().getContextManager();
                            if (contextManager instanceof JobContextManager) {
                                JobContextManager jobContextManager = (JobContextManager) contextManager;
                                jobContextManager.setModified(true);
                            }

                            contextManager.fireContextsChangedEvent();
                        }
                    }
                    // TDQ-9629~

                    refresh();
                }
            }

        });
        selectContextVariablesPushButton.setImage(image);
        return selectContextVariablesPushButton;
    }

    private void setButtonEnableState() {
        boolean enableState = !modelManager.isReadOnly();
        if (this.addButton != null) {
            this.addButton.setEnabled(enableState);
        }
        if (this.removeButton != null) {
            this.removeButton.setEnabled(enableState);
        }
        if (this.moveUpButton != null) {
            this.moveUpButton.setEnabled(enableState);
        }
        if (this.moveDownButton != null) {
            this.moveDownButton.setEnabled(enableState);
        }
        if (this.selectContextVariablesButton != null) {
            this.selectContextVariablesButton.setEnabled(enableState);
        }
        if (contextConfigButton != null) {
            this.contextConfigButton.setEnabled(enableState);
        }
        if (contextsCombo != null) {
            this.contextsCombo.setEnabled(enableState);
        }
    }

    public Object createNewEntry() {
        List<IContextParameter> listParams = getContextManager().getDefaultContext().getContextParameterList();
        Integer numParam = new Integer(1);
        boolean paramNameFound;
        String paramName = null;
        do { // look for a new name
            paramNameFound = true;
            paramName = NEW_PARAM_NAME + numParam;
            for (int i = 0; i < listParams.size(); i++) {
                if (paramName.equals(listParams.get(i).getName())) {
                    paramNameFound = false;
                }
            }
            if (!paramNameFound) {
                numParam++;
            }
        } while (!paramNameFound);

        JobContextParameter contextParam = new JobContextParameter();
        contextParam.setName(paramName);
        ECodeLanguage curLanguage = LanguageManager.getCurrentLanguage();
        if (curLanguage == ECodeLanguage.JAVA) {
            contextParam.setType(ContextParameterJavaTypeManager.getDefaultJavaType().getId());
        } else {
            contextParam.setType(MetadataTalendType.getDefaultTalendType());
        }
        contextParam.setPrompt(paramName + "?"); //$NON-NLS-1$
        String defaultValue;
        if (curLanguage == ECodeLanguage.JAVA) {
            defaultValue = ContextParameterJavaTypeManager.getDefaultValueFromJavaIdType(ContextParameterJavaTypeManager
                    .getDefaultJavaType().getId(), false);
        } else {
            defaultValue = TalendQuoteUtils.addQuotes(""); //$NON-NLS-1$
        }
        contextParam.setValue(defaultValue);
        contextParam.setComment(""); //$NON-NLS-1$
        contextParam.setSource(""); //$NON-NLS-1$
        return contextParam;
    }

    private void removeChildModelInGroupBySource(ContextTableTabChildModel child) {
        IContextParameter contextPara = child.getContextParameter();
        String sourceId = contextPara.getSource();
        String contextName = contextPara.getName();
        modelManager.onContextRemoveParameter(getContextManager(), contextName, sourceId);
    }

    private void removeParentModelInGroupBySource(ContextTableTabParentModel parentModel) {
        Set<String> paraNames = new HashSet<String>();
        String sourceId = parentModel.getSourceId();
        if (IContextParameter.BUILT_IN.equals(sourceId)) {
            String paraName = parentModel.getContextParameter().getName();
            paraNames.add(paraName);
        } else {
            List<ContextTabChildModel> children = parentModel.getChildren();
            if (children != null && children.size() > 0) {
                for (ContextTabChildModel child : children) {
                    IContextParameter contextPara = child.getContextParameter();
                    String paraName = contextPara.getName();
                    paraNames.add(paraName);
                }
            }
        }
        modelManager.onContextRemoveParameter(getContextManager(), paraNames, sourceId);
    }

    @Override
    public boolean isGroupBySource() {
        boolean isRepositoryContext = false;
        if (modelManager != null) {
            isRepositoryContext = (modelManager instanceof ContextComposite)
                    && ((ContextComposite) modelManager).isRepositoryContext();
        }
        boolean value = getPreferenceStore().getBoolean(ITalendCorePrefConstants.CONTEXT_GROUP_BY_SOURCE);
        return value && !isRepositoryContext;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (configContext != null) {
            configContext.setEnabled(enabled);
        }
        if (contextTableComp != null && !contextTableComp.isDisposed()) {
            contextTableComp.setEnabled(enabled);
        }
        if (messageComp != null) {
            this.getParent().setEnabled(true);
        }
    }

    @Override
    public TreeViewer getViewer() {
        return this.viewer;
    }

    /**
     * need force refresh here after install the external jar
     */
    private void reInitializeUI() {
        disposeInstallMessageComp();
        disposeUnAvailableContextComp();
        if (contextTableComp == null || (contextTableComp != null && contextTableComp.isDisposed())) {
            initializeUI();
            contextTableComp.getParent().layout();
            contextTableComp.layout();
        }
    }

    @Override
    public void refresh() {
        if (getContextManager() == null) {
            disposeInstallMessageComp();
            disposeNatTableComp();
            if (availableLabelComp == null || (availableLabelComp != null && availableLabelComp.isDisposed())) {
                createContextNotAvailableGroup(this);
            }
        } else {
            if (!ContextNatTableUtils.checkIsInstallExternalJar()) {
                disposeUnAvailableContextComp();
                if (messageComp == null || (messageComp != null && messageComp.isDisposed())) {
                    createMessageGroup(this);
                }
            } else {
                reInitializeUI();

                initializeContextCombo();

                checkContextGroupSource();

                // dispose the data table composite
                disposeDataTable();
                // create the data table composite
                createNatTable();

                contextTableComp.getParent().layout();
                contextTableComp.layout();
                treeTable.refresh();

                setButtonEnableState();
            }
        }
    }

    private void initializeContextCombo() {
        IContextManager contextManager = getContextManager();
        if (contextsCombo == null) {
            return;
        }
        if (contextsCombo.getItems().length > 0) {
            contextsCombo.removeAll();
        }
        if (contextManager != null) {
            List<IContext> contexts = contextManager.getListContext();
            for (IContext context : contexts) {
                if (!Arrays.asList(contextsCombo.getItems()).contains(context.getName())) {
                    contextsCombo.add(context.getName());
                }
            }

            for (int i = 0; i < contextsCombo.getItemCount(); i++) {
                IContext defaultContext = contextManager.getDefaultContext();
                if (defaultContext.getName().equals(contextsCombo.getItem(i))) {
                    contextsCombo.select(i);
                    break;
                }
            }
        }

        int visibleItemCount = contextsCombo.getItemCount();
        if (visibleItemCount > 20) {
            visibleItemCount = 20;
        }
        contextsCombo.setVisibleItemCount(visibleItemCount);
    }

    private void checkContextGroupSource() {
        IContextManager contextManager = getContextManager();
        if (helper == null) {
            return;
        }
        if (contextManager != null) {
            helper.initHelper(contextManager);
            Map<String, Item> items = new HashMap<String, Item>();
            boolean needRefresh = false;
            for (IContextParameter param : contextManager.getDefaultContext().getContextParameterList()) {
                if (!param.isBuiltIn()) {
                    String source = param.getSource();
                    Item sourceItem = items.get(source);
                    if (sourceItem == null) {
                        sourceItem = ContextUtils.getRepositoryContextItemById(source);
                    }
                    if (sourceItem == null) { // source not found
                        needRefresh = true;
                        param.setSource(IContextParameter.BUILT_IN);
                        propagateType(contextManager, param);
                    } else {
                        items.put(source, sourceItem);
                    }
                }
            }
            if (needRefresh) {
                setModifiedFlag(contextManager);
                modelManager.refresh();
            }
        }
    }

    private void setModifiedFlag(IContextManager contextManager) {
        if (contextManager != null && contextManager instanceof JobContextManager) {
            JobContextManager manager = (JobContextManager) contextManager;
            manager.setModified(true);
        }
    }

    private void propagateType(IContextManager contextManager, IContextParameter param) {
        for (IContext context : contextManager.getListContext()) {
            IContextParameter paramToModify = context.getContextParameter(param.getName());
            paramToModify.setType(param.getType());
            paramToModify.setComment(param.getComment());
            paramToModify.setSource(param.getSource());
        }
    }

    private void disposeDataTable() {
        if (contextTableComp != null && !contextTableComp.isDisposed()) {
            for (Control control : contextTableComp.getChildren()) {
                control.dispose();
            }
        }
    }

    private void disposeUnAvailableContextComp() {
        if (availableLabelComp != null && !availableLabelComp.isDisposed()) {
            availableLabelComp.dispose();
        }
    }

    private void disposeInstallMessageComp() {
        if (messageComp != null && !messageComp.isDisposed()) {
            messageComp.dispose();
        }
    }

    private void disposeNatTableComp() {
        if (contextsSelectComp != null && !contextsSelectComp.isDisposed()) {
            contextsSelectComp.dispose();
        }
        if (contextTableComp != null && !contextTableComp.isDisposed()) {
            contextTableComp.dispose();
        }
        if (buttonsComp != null && !buttonsComp.isDisposed()) {
            buttonsComp.dispose();
        }
    }

    private final Listener resizeListener = new Listener() {

        @Override
        public void handleEvent(Event event) {
            resizeLimiter.resetTimer();
            resizeLimiter.startIfExecutable(true, null);
        }
    };

    private final ExecutionLimiter resizeLimiter = new ExecutionLimiter(250, true) {

        @Override
        public void execute(final boolean isFinalExecution, Object data) {
            if (!isDisposed()) {
                getDisplay().asyncExec(new Runnable() {

                    @Override
                    public void run() {
                        if (!isDisposed() && !getParent().isDisposed()) {
                            refresh();
                        }
                    }
                });
            }
        }
    };

}
