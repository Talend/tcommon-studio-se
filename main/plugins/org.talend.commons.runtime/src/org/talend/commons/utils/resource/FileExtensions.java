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
package org.talend.commons.utils.resource;

/**
 * DOC ggu class global comment. Detailled comment
 */
public interface FileExtensions {

    String PROPERTIES_EXTENSION = "properties"; //$NON-NLS-1$

    String ITEM_EXTENSION = "item"; //$NON-NLS-1$

    String SCREENSHOT_EXTENSION = "screenshot"; //$NON-NLS-1$

    // MOD mzhao 2010-11-22, suppport TDQ item file extensions.(.ana, .rep, etc)
    String ANA_EXTENSION = "ana"; //$NON-NLS-1$

    String REP_EXTENSION = "rep"; //$NON-NLS-1$

    // MOD klliu 2010-11-26
    String DEF_EXTENSION = "definition"; //$NON-NLS-1$

    // MOD klliu 2010-11-26
    String PAT_EXTENSION = "pattern"; //$NON-NLS-1$

    // MOD klliu 2010-11-26
    String RULE_EXTENSION = "rules"; //$NON-NLS-1$

    // MOD klliu 2010-11-26
    String SQL_EXTENSION = "sql"; //$NON-NLS-1$

    // MOD klliu 2010-11-26
    String JRXML_EXTENSION = "jrxml"; //$NON-NLS-1$

    String XML_EXTENSION = "xml"; //$NON-NLS-1$

    String ZIP_EXTENSION = "zip"; //$NON-NLS-1$

    String JAR_EXTENSION = "jar"; //$NON-NLS-1$

    String CAR_EXTENSION = "car";

    /*
     * file suffix
     */
    String PROPERTIES_FILE_SUFFIX = '.' + PROPERTIES_EXTENSION;

    String ITEM_FILE_SUFFIX = '.' + ITEM_EXTENSION;

    String SCREENSHOT_FILE_SUFFIX = '.' + SCREENSHOT_EXTENSION;

    String ZIP_FILE_SUFFIX = '.' + ZIP_EXTENSION;

    String CAR_FILE_SUFFIX = '.' + CAR_EXTENSION;

    String TAR_FILE_SUFFIX = ".tar"; //$NON-NLS-1$

    String TAR_GZ_FILE_SUFFIX = ".tar.gz"; //$NON-NLS-1$

    String JAR_FILE_SUFFIX = '.' + JAR_EXTENSION;

    String WAR_FILE_SUFFIX = ".war"; //$NON-NLS-1$

    String ESB_FILE_SUFFIX = ".esb"; //$NON-NLS-1$

    String SH_FILE_SUFFIX = ".sh"; //$NON-NLS-1$

    String BAT_FILE_SUFFIX = ".bat"; //$NON-NLS-1$

    String KAR_FILE_SUFFIX = ".kar"; //$NON-NLS-1$

    String XLS_FILE_SUFFIX = ".xls"; //$NON-NLS-1$

    String WSDL_FILE_SUFFIX = ".wsdl"; //$NON-NLS-1$

    String XML_FILE_SUFFIX = '.' + XML_EXTENSION;
}
