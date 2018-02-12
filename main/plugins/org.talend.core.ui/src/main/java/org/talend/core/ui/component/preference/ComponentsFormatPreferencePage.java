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
package org.talend.core.ui.component.preference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ui.component.ComponentPaletteUtilities;
import org.talend.core.ui.i18n.Messages;
import org.talend.core.ui.services.IComponentsLocalProviderService;
import org.talend.core.utils.KeywordsValidator;
import org.talend.designer.core.IDesignerCoreService;

/**
 * yzhang class global comment. Detail led comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class ComponentsFormatPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

    private Button buttonAdd, buttonRemove;

    private Text textHint, textLabel, textConnection;

    private TableViewer viewer;

    private IPreferenceStore preferenceStore;

    private Map<String, String> preferenceCach;

    private PaletteRoot paletteRoot;

    private static String DEFAULT_HINT;

    private static String DEFAULT_LABEL;

    private static String DEFAULT_CONNECTION;

    static {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerCoreService.class)) {
            IDesignerCoreService service = (IDesignerCoreService) GlobalServiceRegister.getDefault().getService(
                    IDesignerCoreService.class);
            if (service != null) {
                DEFAULT_HINT = service.getPreferenceStore("defaultHint"); //$NON-NLS-1$
                DEFAULT_LABEL = service.getPreferenceStore("defaultLabel"); //$NON-NLS-1$
                DEFAULT_CONNECTION = service.getPreferenceStore("defaultConnection"); //$NON-NLS-1$
            }
        }

    }

    private int textHintId = 1;

    private int textLabelId = 2;

    private int textConnectionId = 3;

    private String configTextConnectionValue;

    /**
     * yzhang Comment method "getPaletteRoot".
     */
    public PaletteRoot getPaletteRoot() {

        paletteRoot = ComponentPaletteUtilities.getPaletteRoot();

        // TODO:need to remove the tool group.

        // if (paletteRoot == null || ComponentUtilities.isComponentPaletteNeedRefresh) {
        // paletteRoot =
        // CorePlugin.getDefault().getDesignerCoreService().createPalette(ComponentsFactoryProvider.getInstance());
        // PaletteEntry entry = null;
        // for (Object element : paletteRoot.getChildren()) {
        // if (((PaletteContainer) element).getLabel().equals("Tools")) {
        // entry = (PaletteEntry) element;
        // break;
        // }
        // }
        // if (entry != null) {
        // paletteRoot.remove(entry);
        // }
        // }
        return paletteRoot;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createContents(Composite parent) {

        Composite panel = new Composite(parent, SWT.NONE);
        panel.setLayout(new GridLayout(2, false));
        panel.setLayoutData(new GridData(GridData.FILL_BOTH));

        // viewer = new TableViewer(panel);
        // GridData viewerGridData = new GridData(GridData.FILL_BOTH);
        // viewer.getControl().setLayoutData(viewerGridData);
        // configViewer(viewer);

        // Composite buttonPanel = new Composite(panel, SWT.NONE);
        // buttonPanel.setLayoutData(new GridData(GridData.FILL_VERTICAL));
        // buttonPanel.setLayout(new GridLayout());
        //
        // buttonAdd = new Button(buttonPanel, SWT.NONE);
        //        buttonAdd.setText(Messages.getString("ComponentsFormatPreferencePage.add")); //$NON-NLS-1$
        // buttonAdd.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        //
        // buttonRemove = new Button(buttonPanel, SWT.NONE);
        //        buttonRemove.setText(Messages.getString("ComponentsFormatPreferencePage.remove")); //$NON-NLS-1$
        // buttonRemove.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Composite footerPanel = new Composite(panel, SWT.NONE);
        footerPanel.setLayout(new GridLayout(2, false));
        GridData footerPanelGD = new GridData(GridData.FILL_BOTH);
        footerPanelGD.horizontalSpan = 2;
        footerPanel.setLayoutData(footerPanelGD);

        Label labelHint = new Label(footerPanel, SWT.NONE);
        labelHint.setText(Messages.getString("ComponentsFormatPreferencePage.formatHint")); //$NON-NLS-1$
        textHint = new Text(footerPanel, SWT.BORDER);
        textHint.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        textHint.setData(textHintId);

        configTextModifyListener(textHint, IComponentsLocalProviderService.PREFERENCE_TYPE_HINT);

        Label labelLabel = new Label(footerPanel, SWT.NONE);
        labelLabel.setText(Messages.getString("ComponentsFormatPreferencePage.formatLable")); //$NON-NLS-1$
        textLabel = new Text(footerPanel, SWT.BORDER);
        textLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        textLabel.setData(textLabelId);
        configTextModifyListener(textLabel, IComponentsLocalProviderService.PREFERENCE_TYPE_LABEL);

        Label labelConnection = new Label(footerPanel, SWT.NONE);
        labelConnection.setText(Messages.getString("ComponentsFormatPreferencePage.formatConnection")); //$NON-NLS-1$
        textConnection = new Text(footerPanel, SWT.BORDER);
        textConnection.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        textConnection.setData(textConnectionId);
        configTextModifyListener(textConnection, IComponentsLocalProviderService.PREFERENCE_TYPE_CONNECTION);

        addButtonListeners();
        initViewerContent();

        return panel;
    }

    /**
     * yzhang Comment method "configTexts".
     * 
     * @param text
     * @param preferenceType
     */
    private void configTextModifyListener(final Text text, final String preferenceType) {
        text.addKeyListener(new KeyAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.KeyAdapter#keyReleased(org.eclipse.swt.events.KeyEvent)
             */
            @Override
            public void keyReleased(KeyEvent e) {
                // qli modified to fix the bug 7074
                String content = text.getText();
                if (text.getData().equals(textConnectionId) && !canRename(content)) {
                    String message = Messages.getString("ComponentsFormatPreferencePage.errorCreateConnectionName", content); //$NON-NLS-1$
                    MessageDialog.openError(getShell(), Messages.getString("ComponentsFormatPreferencePage.ErrorTitle"), message); //$NON-NLS-1$
                    setText(textConnection, configTextConnectionValue, true);
                }
                // Object[] objs = ((IStructuredSelection) viewer.getSelection()).toArray();
                // for (Object o : objs) {
                // preferenceCach.put(preferenceType, content);
                //
                // }
                preferenceCach.put(preferenceType, content);

            }
        });

    }

    /**
     * yzhang Comment method "configListViewer".
     * 
     * @param viewer
     */
    private void configViewer(TableViewer v) {

        v.setContentProvider(new IStructuredContentProvider() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
             */
            @Override
            public Object[] getElements(Object inputElement) {
                return ((List) inputElement).toArray();
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.IContentProvider#dispose()
             */
            @Override
            public void dispose() {

            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
             * java.lang.Object, java.lang.Object)
             */
            @Override
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }

        });

        v.setLabelProvider(new ILabelProvider() {

            @Override
            public Image getImage(Object element) {
                PaletteEntry entry = (PaletteEntry) element;
                ImageDescriptor descriptor = entry.getSmallIcon();
                if (descriptor == null) {
                    if (entry instanceof PaletteContainer) {
                        // descriptor = InternalImages.DESC_FOLDER_OPEN;
                        descriptor = PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_FOLDER);
                    } else if (entry instanceof PaletteSeparator) {// icons/separator.gif
                        // descriptor = InternalImages.DESC_SEPARATOR;
                        descriptor = ImageProvider.getImageDesc(EImage.SEPARATOR_ICON);
                    } else {
                        return null;
                    }
                }
                return ImageProvider.getImage(descriptor);
            }

            @Override
            public String getText(Object element) {
                String label = null;
                if (element instanceof PaletteContainer) {
                    label = formContainerName((PaletteContainer) element);
                } else {
                    label = ((PaletteEntry) element).getLabel();
                }
                return label;
            }

            @Override
            public void addListener(ILabelProviderListener listener) {

            }

            @Override
            public void dispose() {

            }

            @Override
            public boolean isLabelProperty(Object element, String property) {
                return false;
            }

            @Override
            public void removeListener(ILabelProviderListener listener) {

            }

        });

        v.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                refreshTextContent();
            }

        });

    }

    /**
     * yzhang Comment method "refreshTextContent".
     * 
     * @param selection
     */
    private void refreshTextContent() {

        // Object[] objs = selection.toArray();
        List<DefaultFormat> formats = new ArrayList<DefaultFormat>();
        //
        // for (Object o : objs) {
        //
        // DefaultFormat format = getDefaultPreference();
        //
        // formats.add(format);
        // }

        DefaultFormat format = getDefaultPreference();

        formats.add(format);

        if (formats.size() > 1) {
            checkFormatsEquals(formats);
        }

        setTextsContents(formats);
    }

    /**
     * yzhang Comment method "getLabelWithoutType".
     * 
     * @param o
     * @return
     */
    private String getIdWithoutPreferenceType(Object o) {
        if (o instanceof ToolEntry) {
            return ((ToolEntry) o).getLabel() + IComponentsLocalProviderService.PALETTE_ENTRY_TYPE;
        } else if (o instanceof PaletteContainer) {
            String name = formContainerName((PaletteContainer) o);
            return name + IComponentsLocalProviderService.PALETTE_CONTAINER_TYPE;
        }
        return null;
    }

    private String formContainerName(PaletteContainer container) {
        String family = null;

        if (container.getParent() != null) {
            family = formContainerName(container.getParent());
        }

        return family != null ? family + "/" + container.getLabel() : container.getLabel(); //$NON-NLS-1$

    }

    /**
     * yzhang Comment method "setTextsContents".
     * 
     * @param formats
     */
    private void setTextsContents(List<DefaultFormat> formats) {
        if (formats.size() != 0) {
            DefaultFormat format = formats.get(0);

            setText(textConnection, format.getConnection(), connection);
            setText(textLabel, format.getLabel(), label);
            setText(textHint, format.getHint(), hint);
        } else {
            setText(textConnection, "", connection); //$NON-NLS-1$
            setText(textLabel, "", label); //$NON-NLS-1$
            setText(textHint, "", hint); //$NON-NLS-1$
        }

        connection = true;
        label = true;
        hint = true;

    }

    /**
     * yzhang Comment method "setText".
     * 
     * @param text
     * @param content
     * @param flag
     */
    private void setText(Text text, String content, boolean flag) {
        // qli modified to fix the bug 7074.
        if (flag) {
            text.setText(content);
            if (text.getData().equals(textConnectionId)) {
                configTextConnectionValue = content;
            }
        } else {
            text.setText(""); //$NON-NLS-1$
        }
    }

    /**
     * qli Comment method "canRename".
     * 
     * @param content
     * 
     * @return canRename
     * 
     */
    private boolean canRename(String content) {
        boolean canRename = true;
        Perl5Matcher matcher = new Perl5Matcher();
        Perl5Compiler compiler = new Perl5Compiler();
        Pattern pattern;
        try {
            pattern = compiler.compile("^[A-Za-z_][A-Za-z0-9_]*$"); //$NON-NLS-1$
            if (!matcher.matches(content, pattern)) {
                canRename = false;
            }
        } catch (MalformedPatternException e) {
            throw new RuntimeException(e);
        }
        if (KeywordsValidator.isKeyword(content) || KeywordsValidator.isSqlKeyword(content)) {
            canRename = false;
        }
        return canRename;
    }

    private void checkFormatsEquals(List<DefaultFormat> formats) {
        DefaultFormat oldFormat = null;
        for (DefaultFormat format : formats) {
            if (oldFormat == null) {
                oldFormat = format;
                continue;
            }
            int status = oldFormat.equals(format);
            if (status != DefaultFormat.EQUAL) {
                switch (status) {
                case DefaultFormat.HINT:
                    hint = false;
                    break;
                case DefaultFormat.CONNECTION:
                    connection = false;
                    break;
                case DefaultFormat.LABEL:
                    label = false;
                    break;
                }
            }
        }

    }

    /**
     * DOC yzhang Comment method "initPreferenceCach".
     */
    private void initPreferenceCach() {
        // List viewerContent = (List) viewer.getInput();
        //
        // for (Object o : viewerContent) {
        //
        // String cachId = getIdWithoutPreferenceType(o);
        //
        // setDefaultValueToPreferenceCach(cachId, IComponentsLocalProviderService.PREFERENCE_TYPE_CONNECTION,
        // DEFAULT_CONNECTION, false);
        // setDefaultValueToPreferenceCach(cachId, IComponentsLocalProviderService.PREFERENCE_TYPE_HINT, DEFAULT_HINT,
        // false);
        // setDefaultValueToPreferenceCach(cachId, IComponentsLocalProviderService.PREFERENCE_TYPE_LABEL, DEFAULT_LABEL,
        // false);
        //
        // }
        String connection = preferenceStore.getString(IComponentsLocalProviderService.PREFERENCE_TYPE_CONNECTION);
        String hint = preferenceStore.getString(IComponentsLocalProviderService.PREFERENCE_TYPE_HINT);
        String label = preferenceStore.getString(IComponentsLocalProviderService.PREFERENCE_TYPE_LABEL);

        setDefaultValueToPreferenceCach("", IComponentsLocalProviderService.PREFERENCE_TYPE_CONNECTION, connection, false);
        setDefaultValueToPreferenceCach("", IComponentsLocalProviderService.PREFERENCE_TYPE_HINT, hint, false);
        setDefaultValueToPreferenceCach("", IComponentsLocalProviderService.PREFERENCE_TYPE_LABEL, label, false);
        refreshTextContent();
    }

    /**
     * yzhang Comment method "setDefaultValueToPreferenceCach".
     * 
     * @param id
     * @param type
     * @param defaultValue
     */
    private void setDefaultValueToPreferenceCach(String id, String type, String defaultValue, boolean forceDefault) {
        if (forceDefault || "".equals(preferenceStore.getString(type))) { //$NON-NLS-1$
            preferenceCach.put(type, defaultValue);
        } else {
            preferenceCach.put(type, preferenceStore.getString(type));
        }
    }

    /**
     * yzhang Comment method "getDefaultPreference".
     * 
     * @param labelID
     * @return
     */
    private DefaultFormat getDefaultPreference() {
        String h, l, c;

        h = preferenceCach.get(IComponentsLocalProviderService.PREFERENCE_TYPE_HINT);

        l = preferenceCach.get(IComponentsLocalProviderService.PREFERENCE_TYPE_LABEL);

        c = preferenceCach.get(IComponentsLocalProviderService.PREFERENCE_TYPE_CONNECTION);

        return new DefaultFormat(h, l, c);

    }

    private boolean hint = true;

    private boolean label = true;

    private boolean connection = true;

    private Set<String> idSet;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    @Override
    public void init(IWorkbench workbench) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IComponentsLocalProviderService.class)) {
            IComponentsLocalProviderService service = (IComponentsLocalProviderService) GlobalServiceRegister.getDefault()
                    .getService(IComponentsLocalProviderService.class);
            if (service != null) {
                preferenceStore = service.getPreferenceStore();
            }
        }
        preferenceCach = new HashMap<String, String>();

        String ids = preferenceStore.getString(IComponentsLocalProviderService.FORMAT_IDS);
        if (!"".equals(ids)) { //$NON-NLS-1$
            String[] idArray = ids.split(";"); //$NON-NLS-1$
            idSet = new HashSet<String>();
            for (String id : idArray) {
                String[] items = id.split(":"); //$NON-NLS-1$
                idSet.add(items[0] + ":" + items[1]); //$NON-NLS-1$
            }
            iteratePaletteEntriesToInitViewerInput(getPaletteRoot());
        }

    }

    private final List initViewerInput = new ArrayList();

    private void insertInitViewerInputList(PaletteEntry entry) {
        for (Object obj : initViewerInput) {
            if (entry.getLabel().equals(((PaletteEntry) obj).getLabel())) {
                return;
            }
        }
        initViewerInput.add(entry);
    }

    /**
     * yzhang Comment method "iteratePaletteEntriesToInitViewerInput".
     * 
     * @param entry
     */
    private void iteratePaletteEntriesToInitViewerInput(PaletteEntry entry) {

        if (entry instanceof PaletteContainer) {
            for (Object obj : ((PaletteContainer) entry).getChildren()) {
                iteratePaletteEntriesToInitViewerInput((PaletteEntry) obj);
                if (isWithinIds(((PaletteContainer) entry).getLabel() + IComponentsLocalProviderService.PALETTE_CONTAINER_TYPE)) {
                    insertInitViewerInputList(entry);
                }
            }
        } else if (entry instanceof ToolEntry) {
            if (isWithinIds(((ToolEntry) entry).getLabel() + IComponentsLocalProviderService.PALETTE_ENTRY_TYPE)) {
                insertInitViewerInputList(entry);
            }
        }
    }

    private boolean isWithinIds(String target) {
        for (String id : idSet) {
            if (id.contains(target)) {
                return true;
            }
        }
        return false;
    }

    /**
     * yzhang Comment method "addButtonListeners".
     */
    private void addButtonListeners() {
        // buttonAdd.addMouseListener(new MouseAdapter() {
        //
        // /*
        // * (non-Javadoc)
        // *
        // * @see org.eclipse.swt.events.MouseAdapter#mouseUp(org.eclipse.swt.events.MouseEvent)
        // */
        // @Override
        // public void mouseUp(MouseEvent e) {
        // PaletteContentDialog dialog = new PaletteContentDialog(getShell(), ComponentsFormatPreferencePage.this);
        // dialog.open();
        // }
        // });
        //
        // buttonRemove.addMouseListener(new MouseAdapter() {
        //
        // /*
        // * (non-Javadoc)
        // *
        // * @see org.eclipse.swt.events.MouseAdapter#mouseUp(org.eclipse.swt.events.MouseEvent)
        // */
        // @Override
        // public void mouseUp(MouseEvent e) {
        // StructuredSelection selection = (StructuredSelection) viewer.getSelection();
        // for (Object obj : selection.toArray()) {
        // ((List) viewer.getInput()).remove(obj);
        //
        // String id = getIdWithoutPreferenceType(obj);
        // preferenceCach.remove(id + IComponentsLocalProviderService.PREFERENCE_TYPE_HINT);
        // preferenceCach.remove(id + IComponentsLocalProviderService.PREFERENCE_TYPE_LABEL);
        // preferenceCach.remove(id + IComponentsLocalProviderService.PREFERENCE_TYPE_CONNECTION);
        //
        // }
        // viewer.refresh();
        //
        // }
        // });

    }

    /**
     * yzhang Comment method "initViewer".
     */
    private void initViewerContent() {
        // viewer.setInput(initViewerInput);
        initPreferenceCach();
    }

    /**
     * yzhang Comment method "setListViewerInput".
     * 
     * @param selections
     */
    public void setViewerInput(ISelection selections) {
        // List<PaletteEntry> entryList = new ArrayList<PaletteEntry>();
        //
        // List selectionsList = ((TreeSelection) selections).toList();
        //
        // if (viewer.getInput() != null) {
        // List s = (List) viewer.getInput();
        // entryList.addAll(s);
        // }
        //
        // for (Object paletteEntry : selectionsList) {
        // boolean contained = false;
        // for (PaletteEntry entry : entryList) {
        // if ((((PaletteEntry) paletteEntry)).getLabel().equals(entry.getLabel())) {
        // contained = true;
        // break;
        // }
        // }
        // if (!contained) {
        // entryList.add((PaletteEntry) paletteEntry);
        // }
        //
        // }
        //
        // viewer.setInput(entryList);

        initPreferenceCach();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    @Override
    public boolean performOk() {
        StringBuffer key = new StringBuffer();
        for (String id : preferenceCach.keySet()) {
            preferenceStore.putValue(id, preferenceCach.get(id));
            key.append(id + ";"); //$NON-NLS-1$
        }
        preferenceStore.putValue(IComponentsLocalProviderService.FORMAT_IDS, key.toString());
        return super.performOk();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
     */
    @Override
    protected void performDefaults() {
        // StructuredSelection selections = (StructuredSelection) viewer.getSelection();
        // for (Object obj : selections.toArray()) {
        // String id = getIdWithoutPreferenceType(obj);
        // setDefaultValueToPreferenceCach(id, IComponentsLocalProviderService.PREFERENCE_TYPE_CONNECTION,
        // DEFAULT_CONNECTION,
        // true);
        // setDefaultValueToPreferenceCach(id, IComponentsLocalProviderService.PREFERENCE_TYPE_HINT, DEFAULT_HINT,
        // true);
        // setDefaultValueToPreferenceCach(id, IComponentsLocalProviderService.PREFERENCE_TYPE_LABEL, DEFAULT_LABEL,
        // true);
        // }
        setDefaultValueToPreferenceCach("", IComponentsLocalProviderService.PREFERENCE_TYPE_CONNECTION, DEFAULT_CONNECTION, true);
        setDefaultValueToPreferenceCach("", IComponentsLocalProviderService.PREFERENCE_TYPE_HINT, DEFAULT_HINT, true);
        setDefaultValueToPreferenceCach("", IComponentsLocalProviderService.PREFERENCE_TYPE_LABEL, DEFAULT_LABEL, true);
        refreshTextContent();

        super.performDefaults();
    }

    /**
     * yzhang ComponentsFormatPreferencePage class global comment. Detailled comment <br/>
     * 
     * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
     * 
     */
    private class DefaultFormat {

        private String hint, label, connection;

        public static final int EQUAL = 0;

        public static final int HINT = 1;

        public static final int LABEL = 2;

        public static final int CONNECTION = 3;

        /**
         * yzhang Comment method "equals".
         * 
         * @param obj
         * @return
         */
        public int equals(DefaultFormat obj) {

            if (!obj.getConnection().equals(connection)) {
                return CONNECTION;
            } else if (!obj.getHint().equals(hint)) {
                return HINT;
            } else if (!obj.getLabel().equals(label)) {
                return LABEL;
            } else {
                return EQUAL;
            }
        }

        /**
         * yzhang ComponentsFormatPreferencePage.DefaultFormat constructor comment.
         */
        public DefaultFormat(String h, String l, String c) {
            hint = h;
            label = l;
            connection = c;
        }

        /**
         * Getter for hint.
         * 
         * @return the hint
         */
        public String getHint() {
            return this.hint;
        }

        /**
         * Sets the hint.
         * 
         * @param hint the hint to set
         */
        public void setHint(String hint) {
            this.hint = hint;
        }

        /**
         * Getter for label.
         * 
         * @return the label
         */
        public String getLabel() {
            return this.label;
        }

        /**
         * Sets the label.
         * 
         * @param label the label to set
         */
        public void setLabel(String label) {
            this.label = label;
        }

        /**
         * Getter for connection.
         * 
         * @return the connection
         */
        public String getConnection() {
            return this.connection;
        }

        /**
         * Sets the connection.
         * 
         * @param connection the connection to set
         */
        public void setConnection(String connection) {
            this.connection = connection;
        }

    }

}
