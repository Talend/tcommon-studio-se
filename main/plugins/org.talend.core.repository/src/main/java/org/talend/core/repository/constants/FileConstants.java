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
package org.talend.core.repository.constants;

import org.talend.commons.utils.resource.FileExtensions;
import org.talend.repository.model.RepositoryConstants;

/**
 */
public interface FileConstants {

    String OLD_TALEND_PROJECT_FILENAME = "talendProject"; //$NON-NLS-1$

    String LOCAL_PROJECT_FILENAME = "talend.project"; //$NON-NLS-1$
    
    String SETTINGS_FOLDER_NAME = ".settings"; //$NON-NLS-1$

    String PROJECTSETTING_FILE_NAME = "project.settings"; //$NON-NLS-1$

    String RELATIONSHIP_FILE_NAME = "relationship.index"; //$NON-NLS-1$
    
    String COMPONENT_FILE_NAME = "component.index"; //$NON-NLS-1$

    String TALEND_RECYCLE_BIN_INDEX = "recycle_bin.index"; //$NON-NLS-1$
    
    String MIGRATION_TASK_FILE_NAME = "migration_task.index"; //$NON-NLS-1$

    String TDM_PROPS_FOLDER = RepositoryConstants.SETTING_DIRECTORY;
    
    String TDM_PROPS = "com.oaklandsw.base.projectProps"; //$NON-NLS-1$

    String TDM_PROPS_PATH = TDM_PROPS_FOLDER + "/" + TDM_PROPS; //$NON-NLS-1$

    String PROPERTIES_EXTENSION = FileExtensions.PROPERTIES_EXTENSION;

    String ITEM_EXTENSION = FileExtensions.ITEM_EXTENSION;

    String SCREENSHOT_EXTENSION = FileExtensions.SCREENSHOT_EXTENSION;

    String XML_EXTENSION = FileExtensions.XML_EXTENSION;

    // MOD mzhao 2010-11-22, suppport TDQ item file extensions.(.ana, .rep, etc)
    String ANA_EXTENSION = FileExtensions.ANA_EXTENSION;

    String REP_EXTENSION = FileExtensions.REP_EXTENSION;

    // MOD klliu 2010-11-26
    String DEF_EXTENSION = FileExtensions.DEF_EXTENSION;

    // MOD klliu 2010-11-26
    String PAT_EXTENSION = FileExtensions.PAT_EXTENSION;

    // MOD klliu 2010-11-26
    String RULE_EXTENSION = FileExtensions.RULE_EXTENSION;

    // MOD klliu 2010-11-26
    String SQL_EXTENSION = FileExtensions.SQL_EXTENSION;

    // MOD klliu 2010-11-26
    String JRXML_EXTENSION = FileExtensions.JRXML_EXTENSION;

    /*
     * file suffix
     */
    String PROPERTIES_FILE_SUFFIX = '.' + PROPERTIES_EXTENSION;

    String ITEM_FILE_SUFFIX = '.' + ITEM_EXTENSION;

    String SCREENSHOT_FILE_SUFFIX = '.' + SCREENSHOT_EXTENSION;

    String ZIP_FILE_SUFFIX = FileExtensions.ZIP_FILE_SUFFIX;

    String TAR_FILE_SUFFIX = FileExtensions.TAR_FILE_SUFFIX;

    String TAR_GZ_FILE_SUFFIX = FileExtensions.TAR_GZ_FILE_SUFFIX;

    String JAR_FILE_SUFFIX = FileExtensions.JAR_FILE_SUFFIX;

    String WAR_FILE_SUFFIX = FileExtensions.WAR_FILE_SUFFIX;

    String ESB_FILE_SUFFIX = FileExtensions.ESB_FILE_SUFFIX;

    String SH_FILE_SUFFIX = FileExtensions.SH_FILE_SUFFIX;

    String BAT_FILE_SUFFIX = FileExtensions.BAT_FILE_SUFFIX;

    String KAR_FILE_SUFFIX = FileExtensions.KAR_FILE_SUFFIX;

    String XLS_FILE_SUFFIX = FileExtensions.XLS_FILE_SUFFIX;

    String WSDL_FILE_SUFFIX = FileExtensions.WSDL_FILE_SUFFIX;

    /*
     * file names
     */
    String MANIFEST_MF_FILE_NAME = "MANIFEST.MF"; //$NON-NLS-1$

    String META_INF_FOLDER_NAME = "META-INF"; //$NON-NLS-1$

    String BLUEPRINT_FOLDER_NAME = "OSGI-INF/blueprint"; //$NON-NLS-1$

    String SPRING_FOLDER_NAME = "META-INF/spring"; //$NON-NLS-1$

}
