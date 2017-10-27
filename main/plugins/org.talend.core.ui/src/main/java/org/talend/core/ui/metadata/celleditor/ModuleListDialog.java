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
package org.talend.core.ui.metadata.celleditor;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.swt.formtools.LabelledFileField;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.ui.i18n.Messages;

/**
 * created by hwang on Dec 24, 2014 Detailled comment copied from ModuleListCellEditor/ModuleListDialog
 */
public class ModuleListDialog extends Dialog {

    private String selecteModule;

    private String selectedJarPath,selectedJarVersion;

    private Button innerBtn, extBtn, addBtn, delBtn;;

    private ListViewer jarsViewer;

    private LabelledFileField selectField;

    private boolean isInner, selectChanged;

    private boolean isJDBCCreate = false;

    private IElementParameter param;

    private String[] selecteModuleArray = null;

    private String[] moduleNameArray = null;

    private List<String> jarsList = new ArrayList<String>();

    private LabelledText versionLabel;

    public ModuleListDialog(Shell parentShell, String selecteModule, IElementParameter param, boolean isJDBCCreate) {
        super(parentShell);
        this.selecteModule = selecteModule;
        this.param = param;
        this.isJDBCCreate = isJDBCCreate;
        this.setShellStyle(getShellStyle() | SWT.RESIZE);
        initModuleArray();
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.getString("ModuleListCellEditor.title")); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#create()
     */
    @Override
    public void create() {
        super.create();
        checkField(true); // init
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);

        Group group = new Group(composite, SWT.NONE);
        group.setLayout(new GridLayout(2, false));
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        innerBtn = new Button(group, SWT.RADIO);
        innerBtn.setText(Messages.getString("ModuleListCellEditor.innerLabel")); //$NON-NLS-1$
        extBtn = new Button(group, SWT.RADIO);
        extBtn.setText(Messages.getString("ModuleListCellEditor.externalLabel")); //$NON-NLS-1$

        Composite comp = new Composite(composite, SWT.NONE);
        comp.setLayout(new GridLayout());
        GridData layoutData = new GridData(GridData.FILL_BOTH);
        layoutData.minimumHeight = 150;
        layoutData.minimumWidth = 450;
        layoutData.heightHint = 150;
        layoutData.widthHint = 450;
        comp.setLayoutData(layoutData);

        int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL;
        if (isJDBCCreate) {
            style = SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL;
        }
        jarsViewer = new ListViewer(comp, style);
        jarsViewer.setContentProvider(new ArrayContentProvider());
        jarsViewer.setLabelProvider(new LabelProvider() {

            @Override
            public Image getImage(Object element) {
                String text = getText(element);
                if (text != null && !text.equals(selecteModule)) {
                    return ImageProvider.getImage(EImage.LOCK_ICON);
                }
                return super.getImage(element);
            }

        });

        jarsViewer.getList().setLayoutData(new GridData(GridData.FILL_BOTH));

        Composite buttnComp = new Composite(comp, SWT.NONE);
        buttnComp.setLayout(new GridLayout(2, false));
        addBtn = new Button(buttnComp, SWT.PUSH);
        addBtn.setText(Messages.getString("ModuleListCellEditor.add")); //$NON-NLS-1$
        delBtn = new Button(buttnComp, SWT.PUSH);
        delBtn.setText(Messages.getString("ModuleListCellEditor.delete")); //$NON-NLS-1$

        Composite c = new Composite(comp, SWT.NONE);
        c.setLayout(new GridLayout(3, false));
        c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        selectField = new LabelledFileField(c,
                Messages.getString("ModuleListCellEditor.selectLabel"), FilesUtils.getAcceptJARFilesSuffix()); //$NON-NLS-1$

        if(param != null){
            
            IElementParameter componentName = param.getElement().getElementParameter("COMPONENT_NAME");
            
            if (componentName!=null && "cConfig".equals(componentName.getValue())) {
                versionLabel = new LabelledText(c, "Version");
                IElementParameter param = this.param.getElement().getElementParameter("DRIVER_JAR");
                List jars = (List) param.getValue();
                for (int i = 0; i < jars.size(); i++) {
                    Map<String, String> jar = (Map) jars.get(i);
                    if (selecteModule.equals(jar.get("JAR_NAME"))) {
                        selectField.setText(selecteModule.equals("\"newLine\"") ? "" : selecteModule);
                        versionLabel.setText(String.valueOf(jar.get("JAR_NEXUS_VERSION")));
                        break;
                    }
                }
            }
        }
        
        addListeners();
        // checkField(true); // init
        jarsViewer.getList().setSelection(new String[] { selecteModule });
        return composite;
    }

    private void addListeners() {
        innerBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                checkField(true);
            }

        });
        extBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                checkField(false);
            }

        });
        jarsViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                if (jarsViewer.getList().getSelection().length <= 0) {
                    getOKButton().setEnabled(false);
                } else {
                    getOKButton().setEnabled(true);
                }
                selecteModuleArray = jarsViewer.getList().getSelection();
            }
        });
        selectField.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (selectField.getText().trim().length() <= 0) {
                    getOKButton().setEnabled(false);
                } else {
                    String version = "0.0.1-SNAPSHOT";
                    
                    try {
                        
                        Manifest manifest = new JarFile(selectField.getText()).getManifest();
                        Attributes mainAttribs = manifest.getMainAttributes();
                        
                        if(StringUtils.isNotBlank(mainAttribs.getValue("Bundle-Version"))){
                            version = mainAttribs.getValue("Bundle-Version");
                        }else if(StringUtils.isNotBlank(mainAttribs.getValue("Implementation-Version"))){
                            version = mainAttribs.getValue("Implementation-Version");
                        }
                    } catch (Exception eee) {
                        
                    }
                    
                    if(versionLabel !=null ){
                        versionLabel.setText(version);
                    }
                    
                    getOKButton().setEnabled(true);
                }
            }
        });

        addBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog(getShell());
                dialog.setFilterExtensions(new String[] { "*.jar", "*.zip", "*.*", "*" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                String userDir = System.getProperty("user.dir"); //$NON-NLS-1$
                String pathSeparator = System.getProperty("file.separator"); //$NON-NLS-1$
                dialog.setFilterPath(userDir + pathSeparator + "lib" + pathSeparator + "java"); //$NON-NLS-1$ //$NON-NLS-2$
                String path = dialog.open();
                if (path == null) {
                    return;
                }
                if (!jarsList.contains(path)) {
                    jarsList.add(path);
                    jarsViewer.setInput(jarsList);
                } else {
                    MessageDialog.openWarning(
                            getShell(),
                            Messages.getString("ModuleListCellEditor.warningTitle"), Messages.getString("ModuleListCellEditor.warningMessage")); //$NON-NLS-1$ //$NON-NLS-2$
                }
                if (jarsList.size() > 0) {
                    getOKButton().setEnabled(true);
                } else {
                    getOKButton().setEnabled(false);
                }
            }

        });

        delBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                for (Object o : ((StructuredSelection) jarsViewer.getSelection()).toList()) {
                    jarsList.remove(o);
                }
                jarsViewer.setInput(jarsList);

                if (jarsList.size() > 0) {
                    getOKButton().setEnabled(true);
                } else {
                    getOKButton().setEnabled(false);
                }
            }
        });
    }

    private void checkField(boolean inner) {
        innerBtn.setSelection(inner);
        extBtn.setSelection(!inner);
        jarsViewer.getList().setVisible(inner || isJDBCCreate);
        ((GridData) jarsViewer.getList().getLayoutData()).exclude = !inner && !isJDBCCreate;
        selectField.getTextControl().getParent().setVisible(!inner && !isJDBCCreate);
        ((GridData) selectField.getTextControl().getParent().getLayoutData()).exclude = inner || isJDBCCreate;
        addBtn.getParent().setVisible(!inner && isJDBCCreate);
        ((GridData) addBtn.getParent().getLayoutData()).exclude = inner || !isJDBCCreate;
        jarsViewer.getList().getParent().layout();

        jarsViewer.getList().removeAll();
        if (inner) {
            jarsViewer.setInput(this.moduleNameArray);
            if (selecteModuleArray != null) {
                jarsViewer.getList().setSelection(selecteModuleArray);
            } else if (selecteModule != null) {
                if (selecteModule.contains(";")) {
                    String[] names = selecteModule.trim().split(";");
                    jarsViewer.getList().setSelection(names);
                } else {
                    jarsViewer.getList().setSelection(new String[] { selecteModule });
                }
            }
        } else {
            jarsViewer.setInput(jarsList);
        }

        boolean canFinish = true;
        if (innerBtn.getSelection()) {
            if (jarsViewer.getList().getSelection().length <= 0) {
                canFinish = false;
            }

        } else {
            if (isJDBCCreate) {
                if (jarsList.size() <= 0) {
                    canFinish = false;
                }
            } else {
                String fileName = selectField.getText().trim();
                if (fileName.length() <= 0) {
                    canFinish = false;
                }
            }

        }
        if (getOKButton() != null) {
            getOKButton().setEnabled(canFinish);
        }
    }

    public String getSelecteModule() {
        return this.selecteModule;
    }

    public void setSelecteModule(String val) {
        selectChanged = true;
        this.selecteModule = val;
    }

    public boolean isSelectChanged() {
        return this.selectChanged;
    }

    public String getSelectedJarPath() {
        return this.selectedJarPath;
    }
    
    public String getSelectedJarVersion() {
        return this.selectedJarVersion;
    }

    public boolean isInner() {
        return isInner;
    }

    @Override
    protected void okPressed() {
        isInner = innerBtn.getSelection();
        if (isInner) {
            if (isJDBCCreate) {
                selecteModuleArray = jarsViewer.getList().getSelection();
            } else {
                selecteModuleArray = null;
                setSelecteModule(jarsViewer.getList().getSelection()[0]);
            }
        } else {
            List<String> pathList = new ArrayList<String>();
            if (isJDBCCreate) {
                pathList.addAll(jarsList);
            } else {
                selecteModuleArray = null;
                pathList.add(selectField.getText());
            }
            String[] jarNames = new String[pathList.size()];
            for (int i = 0; i < pathList.size(); i++) {
                String jarPath = pathList.get(i);
                IPath path = Path.fromOSString(jarPath);
                File source = path.toFile();
                if (!source.exists()) {
                    MessageDialog.openWarning(getParentShell(), "File Not Found", path
                            + " is not Found,Please make sure the file is exist!");
                    return;
                }
                String lastSegment = path.lastSegment();
                try {
                    if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibrariesService.class)) {
                        ILibrariesService service = (ILibrariesService) GlobalServiceRegister.getDefault().getService(
                                ILibrariesService.class);
                        
                        if (param != null) {
                            
                            IElementParameter componentName = param.getElement().getElementParameter("COMPONENT_NAME");
                            
                            if(componentName !=null && "cConfig".equals(componentName.getValue())){
                                selectedJarPath = jarPath;
                                selectedJarVersion = versionLabel.getText();
                            }else{
                                service.deployLibrary(path.toFile().toURI().toURL());
                            }
                        }else{
                            service.deployLibrary(path.toFile().toURI().toURL());
                        }
                        
                    }

                } catch (Exception ee) {
                    ExceptionHandler.process(ee);
                }

                setSelecteModule(lastSegment);
                jarNames[i] = lastSegment;
            }
            if (isJDBCCreate) {
                selecteModuleArray = jarNames;
            }
        }
        super.okPressed();
    }

    private void initModuleArray() {
        if (param != null) {
            this.moduleNameArray = param.getListItemsDisplayName();
            return;
        }

        ILibraryManagerService libManager = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                ILibraryManagerService.class);
        final Set<String> existingJars = libManager.list(new NullProgressMonitor());
        List<String> jarList = new ArrayList<String>();
        jarList.addAll(existingJars);
        Comparator comparator = new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {

                return o1.compareTo(o2);
            }
        };
        Collections.sort(jarList, comparator);
        this.moduleNameArray = jarList.toArray(new String[0]);
        if (this.moduleNameArray == null) {
            this.moduleNameArray = new String[0];
        }
    }

    public String[] getSelecteModuleArray() {
        return this.selecteModuleArray;
    }
}
