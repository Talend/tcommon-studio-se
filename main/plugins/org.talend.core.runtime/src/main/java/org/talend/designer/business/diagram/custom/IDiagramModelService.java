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
package org.talend.designer.business.diagram.custom;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;
import org.talend.core.IService;
import org.talend.core.model.business.BusinessAlignment;
import org.talend.core.model.business.BusinessType;
import org.talend.core.model.properties.BusinessProcessItem;
import org.talend.core.model.repository.IRepositoryEditorInput;
import org.talend.core.model.repository.IRepositoryObject;

/**
 * DOC qian class global comment. An implementation of the IRunProcessService. <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (星期�? 29 九月 2006) nrousseau $
 * 
 */

public interface IDiagramModelService extends IService {

    public IAction getCreateDiagramAction(boolean isToolbar);

    public void refreshBusinessModel(IEditorReference editors);

    public boolean isBusinessDiagramEditor(IEditorPart part);

    public IRepositoryEditorInput getBusinessDiagramEditorInput(IEditorPart editor);

    public Object getBusinessEditorProcess();

    public BusinessType getBusinessModelType(Object part);

    public EObject getEObject(ISelection selection);

    public ISelection getBusinessEditorSelection(IEditorPart editor);

    public void setBusinessItemAlignment(BusinessAlignment alignment, BusinessAlignment alignmentGroup, Object part);

    public void setBusinessItemsAlignment(BusinessAlignment alignment, BusinessAlignment alignmentGroup, Object part);

    public String getBusinessItemAlignment(Object part, BusinessAlignment alignmentGroup);

    public void handleNewEditorAction(IWorkbenchPart editor);

    public void openBusinessDiagramEditor(IWorkbenchPage page, IEditorInput input) throws PartInitException;

    public IFile getDiagramFileAndUpdateResource(IWorkbenchPage page, BusinessProcessItem businessProcessItem);

    public void addDeleteAssignmentAction(IMenuManager mgr, ISelection selection);
    
    public boolean isInstanceOfCompartmentEditPart(Object o);
    
    public Object getBusinessAppearanceComposite(Composite parent, int style, TabbedPropertySheetWidgetFactory widgetFactory,
            ISelection selection);
    public Object getBusinessRulersAndGridComposite(Composite parent, int style, TabbedPropertySheetWidgetFactory widgetFactory,
            IRepositoryObject obj);
    public Object getBusinessAssignmentComposite(Composite parent, int style, TabbedPropertySheetWidgetFactory widgetFactory,
            ISelection selection);

    public boolean isInstanceOfBusinessAssignmentComposite(Object o);
}
