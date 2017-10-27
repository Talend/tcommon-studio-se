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
package org.talend.core.service;

import java.util.List;

import org.eclipse.ui.IEditorPart;
import org.talend.commons.ui.runtime.image.IImage;
import org.talend.core.IService;
import org.talend.core.model.components.IComponentsHandler;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.repository.IRepositoryEditorInput;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.nodes.IProjectRepositoryNode;

/**
 * DOC zwzhao class global comment. Detailled comment
 */
public interface IStormProcessService extends IService {

    boolean needStormProcess();

    IRepositoryNode getRootNode(IProjectRepositoryNode projectNode);

    public boolean collectStandardProcessNode(List<String> filteredContents, Object node);

    public boolean isStormItem(Item item);

    public boolean isStormEditor(IEditorPart editorPart);

    public List<IRepositoryViewObject> getStormProcesses(Project project);

    /**
     * This method is responsible for creating additional information which are going to be registered in the Process
     * (and then in the Item). DOC rdubois Comment method "generateSparkStreamingInfosParameter".
     * 
     * @param process the current process.
     */
    public void generateSparkStreamingInfosParameter(IProcess2 process);

    public IImage getStormWizardIcon(String framework);

    public IComponentsHandler getSparkStreamingComponentsHandler();

    public boolean isSparkStreaming(Item item);

    public IRepositoryEditorInput createTestCaseStormProcessEditorInput(ProcessItem processItem, boolean load,
            String originalJobID, List<INode> testNodes, Boolean lastVersion, Boolean readonly);

    public IProcess2 createBigdataProcess(Item item);

}
