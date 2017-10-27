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
package org.talend.core.ui;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IEditorPart;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.IService;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.process.Element;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.repository.IRepositoryEditorInput;

/**
 * DOC hwang  class global comment. Detailled comment
 */
public interface ISparkStreamingJobletProviderService extends IService{
	
	public boolean isSparkStreamingJobletEditor(IEditorPart activeEditor);
    
    public boolean isSparkStreamingJobletItem(Item item);

    public boolean isSparkStreamingJobletComponent(INode node);
    
    public IComponent instanceSparkStreamingJobletComponent(Object execObj);
    
    public void clearSparkStreamingJobletComponent();
    
    public IComponent createSparkStreamingJobletComponent();
    
    public IComponent createSparkJobletInputOutputComponent(Object jobletNodeType);
    
    public Element createSparkStreamingJobletContainer(INode node);
    
    public IAdaptable createEditorPart(Object model);
    
    public IRepositoryEditorInput createJobletEditor(JobletProcessItem processItem, Boolean load, Boolean lastVersion, Boolean readonly,Boolean openedInJob) throws PersistenceException;
    
}
