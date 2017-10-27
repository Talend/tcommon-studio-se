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
package org.talend.librariesmanager.ui.views;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorNotModifiable.LAYOUT_MODE;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorNotModifiable.SORT;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.librariesmanager.model.ModulesNeededProvider;
import org.talend.librariesmanager.ui.i18n.Messages;

/**
 * This is the composite filled in the ModulesView. So it implemented the inferface IModulesViewComposite. Know more see
 * interface IModulesViewComposite.
 * 
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: PerlModulesViewComposite.java PerlModulesViewComposite 2007-1-26 下�?�1�7:53:04 +0000 (下�?�1�7:53:04, 2007-1-26
 * 2007) yzhang $
 * 
 */
public class ModulesViewComposite extends Composite {

    protected static final String ID_STATUS = "status"; //$NON-NLS-1$

    private static TableViewerCreator tableViewerCreator;

    private static Logger log = Logger.getLogger(ModulesView.class);

    private IContextActivation ca;

    /**
     * Construct a new Perl modules view composite.
     * 
     * yzhang PerlModulesViewComposite constructor comment.
     * 
     * @param parent
     * @param style
     */
    public ModulesViewComposite(Composite parent) {
        super(parent, SWT.NONE);

        this.setLayout(new FormLayout());
        FormData formData = new FormData();

        Composite rightPartComposite = new Composite(this, SWT.NONE);
        formData = new FormData();
        formData.top = new FormAttachment(0);
        formData.left = new FormAttachment(0);
        formData.right = new FormAttachment(100);
        formData.bottom = new FormAttachment(100);
        rightPartComposite.setLayoutData(formData);
        rightPartComposite.setLayout(new FillLayout());

        tableViewerCreator = new TableViewerCreator(rightPartComposite);
        tableViewerCreator.setCheckboxInFirstColumn(false);
        tableViewerCreator.setColumnsResizableByDefault(true);
        tableViewerCreator.setLayoutMode(LAYOUT_MODE.FILL_HORIZONTAL);
        tableViewerCreator.createTable();

        TableViewerCreatorColumn column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(Messages.getString("ModulesViewComposite.Status.TitleText")); //$NON-NLS-1$
        column.setId(ID_STATUS);
        column.setSortable(true);
        column.setImageProvider(new StatusImageProvider());
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<ModuleNeeded, String>() {

            @Override
            public String get(ModuleNeeded bean) {
                String str = null;
                switch (bean.getStatus()) {
                case INSTALLED:
                    str = Messages.getString("ModulesViewComposite.hint.installed"); //$NON-NLS-1$
                    break;
                case NOT_INSTALLED:
                    str = Messages.getString("ModulesViewComposite.hint.notInstalled"); //$NON-NLS-1$
                    break;
                case UNUSED:
                    str = "Unused"; //$NON-NLS-1$
                    break;
                default:
                    str = Messages.getString("ModulesViewComposite.hint.unknown"); //$NON-NLS-1$
                }
                return str;
            }

            @Override
            public void set(ModuleNeeded bean, String value) {
            }
        });

        column.setWeight(3);
        column.setModifiable(false);

        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(Messages.getString("ModulesViewComposite.Component.TitleText")); //$NON-NLS-1$
        column.setSortable(true);
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<ModuleNeeded, String>() {

            @Override
            public String get(ModuleNeeded bean) {
                return bean.getContext();
            }

            @Override
            public void set(ModuleNeeded bean, String value) {
            }
        });

        column.setModifiable(false);
        column.setWeight(4);

        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(Messages.getString("ModulesViewComposite.Module.TitleText")); //$NON-NLS-1$
        column.setSortable(true);
        tableViewerCreator.setDefaultSort(column, SORT.ASC);
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<ModuleNeeded, String>() {

            @Override
            public String get(ModuleNeeded bean) {
                return bean.getModuleName();
            }

            @Override
            public void set(ModuleNeeded bean, String value) {
            }
        });

        column.setModifiable(false);
        column.setWeight(6);

        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(Messages.getString("ModulesViewComposite.Required.TitleText")); //$NON-NLS-1$

        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<ModuleNeeded, String>() {

            @Override
            public String get(ModuleNeeded bean) {
                return bean.getInformationMsg();
            }

            @Override
            public void set(ModuleNeeded bean, String value) {
            }
        });

        column.setModifiable(true);
        column.setWeight(10);

        column = new TableViewerCreatorColumn(tableViewerCreator);
        column.setTitle(Messages.getString("ModulesViewComposite.Required.Title")); //$NON-NLS-1$
        column.setImageProvider(new RequiredImageProvider());
        column.setSortable(true);
        column.setDisplayedValue(""); //$NON-NLS-1$
        column.setBeanPropertyAccessors(new IBeanPropertyAccessors<ModuleNeeded, String>() {

            @Override
            public String get(ModuleNeeded bean) {
                return String.valueOf(bean.isRequired());
            }

            @Override
            public void set(ModuleNeeded bean, String value) {
            }
        });

        column.setModifiable(false);
        column.setWeight(2);
        // need check it's ok to remove, or caused bug
        // removed by TUP-833
        // IComponentsFactory compFac = ComponentsFactoryProvider.getInstance();
        // compFac.getComponents();
        List<ModuleNeeded> modules = ModulesNeededProvider.getModulesNeeded();

        tableViewerCreator.init(filterHidenModule(modules));

        FocusListener fl = new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                log.trace("Modules gain focus"); //$NON-NLS-1$
                IContextService contextService = (IContextService) PlatformUI.getWorkbench().getAdapter(IContextService.class);
                ca = contextService.activateContext("talend.modules"); //$NON-NLS-1$
            }

            @Override
            public void focusLost(FocusEvent e) {
                log.trace("Modules lost focus"); //$NON-NLS-1$
                if (ca != null) {
                    IContextService contextService = (IContextService) PlatformUI.getWorkbench()
                            .getAdapter(IContextService.class);
                    contextService.deactivateContext(ca);
                }
            }
        };

        parent.addFocusListener(fl);
        rightPartComposite.addFocusListener(fl);
        tableViewerCreator.getTableViewer().getTable().addFocusListener(fl);
    }

    /**
     * DOC bqian Comment method "filterHidenModule".
     * 
     * @param modules
     * @return
     */
    private List filterHidenModule(List<ModuleNeeded> modules) {
        List<ModuleNeeded> list = new ArrayList<ModuleNeeded>();
        for (ModuleNeeded module : modules) {
            if (module.isShow()) {
                list.add(module);
            }
        }
        return list;
    }

    /*
     * Be called when the set focus of modules view was called.
     * 
     * @see org.eclipse.swt.widgets.Composite#setFocus()
     */
    @Override
    public boolean setFocus() {
        return tableViewerCreator.getTableViewer().getTable().setFocus();
    }

    /*
     * Be called when the refresh of modules view was called.
     * 
     * @see org.talend.designer.codegen.perlmodule.ui.views.IModulesViewComposite#refresh()
     */
    public void refresh() {
        List<ModuleNeeded> modulesNeeded = new ArrayList<ModuleNeeded>();
        modulesNeeded.addAll(ModulesNeededProvider.getModulesNeeded());
        modulesNeeded.addAll(ModulesNeededProvider.getUnUsedModules());
        ModulesViewComposite.getTableViewerCreator().init(modulesNeeded);
        tableViewerCreator.getTableViewer().refresh();
    }

    /**
     * Getter for tableViewerCreator.
     * 
     * @return the tableViewerCreator
     */
    public static TableViewerCreator getTableViewerCreator() {
        return tableViewerCreator;
    }

}
