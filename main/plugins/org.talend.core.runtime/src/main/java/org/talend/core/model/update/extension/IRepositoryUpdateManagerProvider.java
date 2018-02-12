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
package org.talend.core.model.update.extension;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.update.UpdateResult;

/**
 * DOC ggu class global comment. Detailled comment
 */
public interface IRepositoryUpdateManagerProvider extends IUpdateManagerProvider {

    /**
     * 
     * DOC ggu Comment method "validateAction".
     * 
     * Mostly work for the method init(TreeViewer,IStructuredSelection) of class DetecteViewImpactAction.
     * 
     * @param viewer
     * @param selection
     * @return
     */
    boolean validateAction(TreeViewer viewer, IStructuredSelection selection);

    /**
     * 
     * DOC ggu Comment method "needPropagate".
     * 
     * @param selection
     * @return
     */
    boolean needPropagate(IStructuredSelection selection);

    /**
     * 
     * DOC ggu Comment method "needForcePropagation".
     * 
     * @param selection
     * @return if true, will force propagate to update manager.
     */
    boolean needForcePropagation(IStructuredSelection selection);

    /**
     * 
     * DOC ggu Comment method "updateForRepository".
     * 
     * Work for DetecteViewImpactAction.
     * 
     * @param selection
     * @return true, update successfully
     */
    boolean updateForRepository(IStructuredSelection selection);

    /**
     * @return false, will only check the opened job.
     */
    boolean enableCheckItem(final IRepositoryViewObject object);

    /**
     * 
     * @param checkItem if true , force to check closed job
     */
    void setEnableCheckItem(boolean checkItem);
    
    /**
     * @param selection
     * @return Retrieve the update result
     */
    public List<UpdateResult> retrieveUpdateResults(final IStructuredSelection selection);
    
    /**
     * @param updateResults
     * @return Valid update result
     */
    public List<UpdateResult> validResults(List<UpdateResult> updateResults);

}
