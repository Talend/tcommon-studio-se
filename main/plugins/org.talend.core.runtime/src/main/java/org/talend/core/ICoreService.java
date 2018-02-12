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
package org.talend.core;

import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.SystemException;
import org.talend.core.model.general.LibraryInfo;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.ColumnNameChanged;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;

/**
 * DOC hywang class global comment. Detailled comment
 */
public interface ICoreService extends IService {

    public List<ColumnNameChanged> getColumnNameChanged(IMetadataTable oldTable, IMetadataTable newTable);

    public List<ColumnNameChanged> getNewMetadataColumns(IMetadataTable oldTable, IMetadataTable newTable);

    public List<ColumnNameChanged> getRemoveMetadataColumns(IMetadataTable oldTable, IMetadataTable newTable);

    public void initializeForTalendStartupJob();

    public String getLanTypeString();

    public Image getImageWithDocExt(String extension);

    public ImageDescriptor getImageWithSpecial(Image source);

    public boolean isContainContextParam(String code);

    public void setFlagForQueryUtils(boolean flag);

    public boolean getContextFlagFromQueryUtils();

    public Map<String, List<LibraryInfo>> getRoutineAndJars();

    public String getTemplateString();

    public String getParameterUNIQUENAME(NodeType node);

    public boolean isAlreadyBuilt(Project project);

    public void removeItemRelations(Item item);

    public String getJavaProjectFolderName(Item item);

    public String getJavaJobFolderName(String jobName, String version);

    public IResource getSpecificResourceInJavaProject(IPath path) throws CoreException;

    public String getRootProjectNameForPerl(Item item);

    public String getContextFileNameForPerl(String projectName, String jobName, String version, String context);

    public IResource getSpecificResourceInPerlProject(IPath path) throws CoreException;

    public void syncLibraries(IProgressMonitor... monitorWrap);

    public void syncLog4jSettings(ITalendProcessJavaProject talendJavaProject);

    public void removeJobLaunch(IRepositoryViewObject objToDelete);

    public void deleteRoutinefile(IRepositoryViewObject objToDelete);

    public void deleteBeanfile(IRepositoryViewObject objToDelete);

    boolean checkJob(String name) throws BusinessException;

    public void syncAllRoutines() throws SystemException;

    public void syncAllBeans() throws SystemException;

    public Job initializeTemplates();

    public void createStatsLogAndImplicitParamter(Project project);

    public void deleteAllJobs(boolean fromPluginModel);

    public List<String> readWorkspaceTasksDone();

    public String getLastUser();

    public void addWorkspaceTaskDone(String task);

    public boolean isKeyword(String word);

    public String filterSpecialChar(String input);

    public String validateValueForDBType(String columnName);

    public void synchronizeMapptingXML(ITalendProcessJavaProject talendJavaProject);

    public IPreferenceStore getPreferenceStore();

    public boolean isOpenedItemInEditor(IRepositoryViewObject object);

    public IMetadataTable convert(MetadataTable originalTable);
    
    public void syncMappingsFileFromSystemToProject();

}
