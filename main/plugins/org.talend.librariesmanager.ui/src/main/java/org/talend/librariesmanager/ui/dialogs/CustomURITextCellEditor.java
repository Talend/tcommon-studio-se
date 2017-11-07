// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.librariesmanager.ui.dialogs;

import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.runtime.swt.tableviewer.celleditor.CellEditorDialogBehavior;
import org.talend.commons.ui.runtime.swt.tableviewer.celleditor.ExtendedTextCellEditor;
import org.talend.core.model.general.ModuleNeeded;

/**
 * created by wchen on Aug 24, 2017 Detailled comment
 *
 */
public class CustomURITextCellEditor extends ExtendedTextCellEditor {

    private ModuleNeeded module;

    /**
     * DOC wchen CustomURITextCellEditor constructor comment.
     * 
     * @param parent
     * @param cellEditorBehavior
     */
    public CustomURITextCellEditor(Composite parent, CellEditorDialogBehavior cellEditorBehavior) {
        super(parent, cellEditorBehavior);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.CellEditor#fireApplyEditorValue()
     */
    @Override
    public void fireApplyEditorValue() {
        super.fireApplyEditorValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.CellEditor#activate()
     */
    @Override
    public void activate() {
        super.activate();
        getTextControl().setEditable(false);
    }

    /**
     * Getter for module.
     * 
     * @return the module
     */
    public ModuleNeeded getModule() {
        return this.module;
    }

    /**
     * Sets the module.
     * 
     * @param module the module to set
     */
    public void setModule(ModuleNeeded module) {
        this.module = module;
    }
}
