package org.talend.repository;

import java.io.IOException;
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
import java.util.List;

import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.ProjectReference;

public interface IReferenceProjectProvider {

    public static final String CONFIGURATION_FOLDER_NAME = ".settings"; //$NON-NLS-1$

    public static final String CONFIGURATION_FILE_NAME = "reference_projects.settings"; //$NON-NLS-1$

    public List<ProjectReference> getProjectReference() throws PersistenceException;

    public void setProjectReference(List<ProjectReference> projectReferenceList);

    public void loadSettings() throws PersistenceException, IOException;

    public void saveSettings() throws PersistenceException, IOException;

    public void initSettings() throws BusinessException, PersistenceException;

    public boolean isHasConfigurationFile();
}
