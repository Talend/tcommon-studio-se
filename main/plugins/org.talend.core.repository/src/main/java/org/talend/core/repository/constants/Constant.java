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

/**
 * Constants related to repostiory <br/>
 * 
 */
public final class Constant {

    /**
     * constant used for OSGI event bus notification for repository Items
     */
    public static final String REPOSITORY_ITEM_EVENT_PREFIX = "org/talend/repository/item/"; //$NON-NLS-1$

    /**
     * suffix used when issuing an event on the OSGI event bus when an lock is done
     */
    public static final String ITEM_LOCK_EVENT_SUFFIX = "lock"; //$NON-NLS-1$

    /**
     * suffix used when issuing an event on the OSGI event bus when an unlock is done
     */
    public static final String ITEM_UNLOCK_EVENT_SUFFIX = "unlock"; //$NON-NLS-1$

    /**
     * key used to get/set the property of an event related to an item (REPOSITORY_ITEM_EVENT_PREFIX). The value is the
     * Item instance.
     */
    public static final String ITEM_EVENT_PROPERTY_KEY = "item"; //$NON-NLS-1$

    /**
     * key used to get/set the property of an event related to a list of files modified in the repository
     * (REPOSITORY_ITEM_EVENT_PREFIX). The value is the Collection of String (list of all files modified).
     */
    public static final String FILE_LIST_EVENT_SUFFIX = "fileList"; //$NON-NLS-1$

    /**
     * key used to get/set the property of an event related to a list of files modified in the repository
     * (REPOSITORY_ITEM_EVENT_PREFIX). The value is the Collection of String (list of all files modified).
     */
    public static final String FILE_LIST_EVENT_PROPERTY_KEY = "fileList"; //$NON-NLS-1$

    /**
     * suffix used when issuing an event on the OSGI event bus when the main project is reloaded.
     */
    public static final String PROJECT_RELOAD_EVENT_SUFFIX = "project"; //$NON-NLS-1$


    /**
     * key used to get/set the property of an event related to a list of files modified in the repository
     * (REPOSITORY_ITEM_EVENT_PREFIX). The value is the Collection of String (list of all files modified).
     */
    public static final String PROJECT_RELOAD_PROPERTY_KEY = "project"; //$NON-NLS-1$
}
