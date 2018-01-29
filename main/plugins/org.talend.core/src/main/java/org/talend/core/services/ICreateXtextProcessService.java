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
package org.talend.core.services;

import org.talend.commons.exception.PersistenceException;
import org.talend.core.IService;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.joblet.model.JobletProcess;

/**
 * DOC nma class global comment. Detailled comment
 */
public interface ICreateXtextProcessService extends IService {

    public void createProcessItem(String jobName, String path);

    // create job's property and give parameters default values except label.
    public void addProperty(String jobName);

    // create the processType with a .job file created by metalanguage.
    public void addProcess(String path);
    
    public  ERepositoryObjectType createProcessItem();

    public String convertJobtoScript(ProcessType processType);

    public ProcessType convertDesignerEditorInput(String path, Property property) throws PersistenceException;

    public JobletProcess convertJobletDesignerEditorInput(String path, Property property) throws PersistenceException;

}
