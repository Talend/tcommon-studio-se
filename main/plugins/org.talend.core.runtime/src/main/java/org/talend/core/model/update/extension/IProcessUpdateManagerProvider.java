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
package org.talend.core.model.update.extension;

import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.update.IUpdateItemType;
import org.talend.core.model.update.UpdateResult;

public interface IProcessUpdateManagerProvider extends IUpdateManagerProvider {

    /**
     * check the type need current provider to do or not.
     * 
     * @param type
     * @return
     */
    boolean validate(IUpdateItemType type);

    /**
     * @param process
     * @return
     */
    List<UpdateResult> retrieveUpdateResults(IProcess process);

    /**
     * mostly work for label provider in update manager dialog.
     * 
     * @return
     */
    Object getDisplayImage(Object element, int columnIndex);

    /**
     * mostly work for label provider in update manager dialog.
     * 
     * @return
     */
    String getDisplayText(Object element, int columnIndex);

    /**
     * mostly work for label provider in update manager dialog.
     * 
     * @param result
     * @return
     */
    String getDisplayCategory(UpdateResult result);

    /**
     * the label of result.
     * 
     * @param result
     * @return
     */
    String getResultName(UpdateResult result);

    /**
     * Will do the real update by result via GEF command, if it's opened job/joblet to be dirty by the command.
     * 
     * @param result
     * @param monitor
     * @return
     */
    boolean doUpdate(IProgressMonitor monitor, UpdateResult result);

    /**
     * most like needRefreshRelatedViews, after udpate, need something to do.
     * 
     * @param results
     */
    void postUpdate(List<UpdateResult> results);

    /**
     * @param results all result to check the related views
     * @return will refresh the view ids.
     */
    Set<String> needRefreshRelatedViews(List<UpdateResult> results);

}
