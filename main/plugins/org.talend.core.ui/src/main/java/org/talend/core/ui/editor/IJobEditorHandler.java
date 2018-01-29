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
package org.talend.core.ui.editor;

import org.eclipse.ui.PartInitException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.Item;

/**
 * This interface is used to handle the job editor like creating job editor input by item, opening a job editor and etc.
 * Created by Marvin Wang on Apr 17, 2013.
 */
public interface IJobEditorHandler {

    /**
     * Sub-classes override this method to create a job editor input by the given item. Added by Marvin Wang on Apr 18,
     * 2013.
     * 
     * @param item
     * @param load that is indicate if the job editor input need to load.
     * @return
     * @throws PersistenceException
     */
    JobEditorInput createJobEditorInput(Item item, boolean load) throws PersistenceException;

    /**
     * Sub-classes override this method to open a job editor by the given input, and
     * {@link #createJobEditorInput(Item, boolean)} can be used to provide a job editor input. Added by Marvin Wang on
     * Apr 18, 2013.
     * 
     * @param jobEditorInput
     * @throws PersistenceException
     * @throws PartInitException
     */
    void openJobEditor(JobEditorInput jobEditorInput) throws PersistenceException, PartInitException;

}
