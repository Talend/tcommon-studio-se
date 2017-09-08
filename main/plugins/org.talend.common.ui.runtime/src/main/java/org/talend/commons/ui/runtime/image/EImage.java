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
package org.talend.commons.ui.runtime.image;

/**
 * 
 * DOC smallet ImageProvider class global comment. Detailled comment <br/>
 * 
 * $Id: ImageProvider.java 418 2006-11-13 16:01:26 +0000 (lun., 13 nov. 2006) cantoine $
 * 
 */
public enum EImage implements IImage {

    DEFAULT_IMAGE,
    EMPTY("/icons/empty.gif"), //$NON-NLS-1$

    SAVE_ICON("/icons/save.png"), //$NON-NLS-1$
    COPY_ICON("/icons/copy.gif"), //$NON-NLS-1$
    PASTE_ICON("/icons/paste.gif"), //$NON-NLS-1$
    DUPLICATE_ICON("/icons/duplicate.png"), //$NON-NLS-1$
    CUT_ICON("/icons/cut.png"), //$NON-NLS-1$
    DELETE_ICON("/icons/delete.gif"), //$NON-NLS-1$
    RESTORE_ICON("/icons/add.gif"), //$NON-NLS-1$
    ADD_ICON("/icons/add.gif"), //$NON-NLS-1$
    ADD_ALL_ICON("/icons/addall.gif"), //$NON-NLS-1$
    MINUS_ICON("/icons/delete.gif"), //$NON-NLS-1$
    REFRESH_WITH_BGCOLOR_ICON("/icons/refresh_with_bgcolor.gif"), //$NON-NLS-1$
    REFRESH_ICON("/icons/refresh.gif"), //$NON-NLS-1$
    EDIT_ICON("/icons/write_obj.gif"), //$NON-NLS-1$
    READ_ICON("/icons/read_obj.gif"), //$NON-NLS-1$

    CREATE_CONNECTION_ICON("/icons/connection.gif"), //$NON-NLS-1$

    RESET_DBTYPES_ICON("/icons/reset_dbtypes.jpg"), //$NON-NLS-1$

    IMPORT_ICON("/icons/import.gif"), //$NON-NLS-1$
    EXPORT_ICON("/icons/export.gif"), //$NON-NLS-1$
    EXPORT_WIZ_ICON("/icons/export_wiz.png"), //$NON-NLS-1$
    EXPORT_ZIP_ICON("/icons/exportzip_wiz.png"), //$NON-NLS-1$
    EXPORT_JOB_ICON("/icons/export_job.png"), //$NON-NLS-1$
    EXPORT_HTML_ICON("/icons/export_html.png"), //$NON-NLS-1$

    TREE_EXPAND("/icons/tree_expand.gif"), //$NON-NLS-1$
    TREE_COLLAPSE("/icons/tree_collapse.gif"), //$NON-NLS-1$

    CHECKED_ICON("/icons/checked.gif"), //$NON-NLS-1$
    UNCHECKED_ICON("/icons/unchecked.gif"), //$NON-NLS-1$

    ERRORSIMPLEMESS_ICON("/icons/errorSimpleMess.gif"), //$NON-NLS-1$
    RIGHTPRESS_ICON("/icons/rightpressedarrow.gif"), //$NON-NLS-1$
    DOWNPRESS_ICON("/icons/downpressedarrow.gif"), //$NON-NLS-1$

    ERROR_ICON("/icons/error.gif"), //$NON-NLS-1$
    WARNING_ICON("/icons/warning.gif"), //$NON-NLS-1$
    INFORMATION_ICON("/icons/info.png"), //$NON-NLS-1$
    ERROR_SMALL("/icons/error_small.png"), //$NON-NLS-1$
    WARNING_SMALL("/icons/warning_small.png"), //$NON-NLS-1$
    INFORMATION_SMALL("/icons/info_ovr.gif"), //$NON-NLS-1$
    Error_Mark("/icons/error_mark.gif"), //$NON-NLS-1$
    OK("/icons/ok.png"), //$NON-NLS-1$
    UNKNOWN_ICON("/icons/unknown.gif"), //$NON-NLS-1$

    TRUNK_ICON("/icons/trunk.gif"), //$NON-NLS-1$
    BRANCHES_ICON("/icons/branches.gif"), //$NON-NLS-1$
    TAGS_ICON("/icons/tags.gif"), //$NON-NLS-1$
    BRANCH_FOLDER_ICON("/icons/newfolder.gif"), //$NON-NLS-1$

    ARROWBRANCH_ICON("/icons/arrow_branch.png"),
    ARROWDIVIDE_ICON("/icons/arrow_divide.png"),
    ARROWRIGHT_ICON("/icons/arrow_right.png"),

    PARALLEL_EXECUTION("/icons/parallelize.png"), //$NON-NLS-1$
    WINDOW("/icons/window.png"), //$NON-NLS-1$

    UP_ICON("/icons/up.gif"), //$NON-NLS-1$
    DOWN_ICON("/icons/down.gif"), //$NON-NLS-1$
    LEFT_ICON("/icons/left.gif"), //$NON-NLS-1$
    LEFTX_ICON("/icons/leftx.png"), //$NON-NLS-1$
    RIGHT_ICON("/icons/right.gif"), //$NON-NLS-1$
    RIGHTX_ICON("/icons/rightx.png"), //$NON-NLS-1$

    KEY_ICON("/icons/key.gif"), //$NON-NLS-1$
    HIERARCHY_ICON("/icons/hierarchicalLayout.gif"), //$NON-NLS-1$

    THREE_DOTS_ICON("/icons/dots_button.gif"), //$NON-NLS-1$

    PROPERTIES_WIZ("/icons/editpref_wiz.gif"), //$NON-NLS-1$

    TRACES_EXPAND("/icons/traces_expand.png"), //$NON-NLS-1$
    TRACES_COLLAPSE("/icons/traces_collapse.png"), //$NON-NLS-1$

    SUBJOB_EXPAND("/icons/subjob_expand.png"), //$NON-NLS-1$
    SUBJOB_COLLAPSE("/icons/subjob_collapse.png"), //$NON-NLS-1$

    COMPACT_VIEW("/icons/compact.png"), //$NON-NLS-1$
    NO_COMPACT_VIEW("/icons/noCompact.png"), //$NON-NLS-1$
    TABLE_VIEW("/icons/array_hot.png"), //$NON-NLS-1$
    NO_TABLE_VIEW("/icons/array.png"), //$NON-NLS-1$
    COMPOSITE_BACKGROUND("/icons/compositeBackground.jpg"), //$NON-NLS-1$

    LOCK_ICON("/icons/lock.gif"), //$NON-NLS-1$
    UNLOCK_ICON("/icons/unlock.gif"), //$NON-NLS-1$
    FIND_ICON("/icons/find.gif"), //$NON-NLS-1$  
    JOB_ICON("/icons/process_icon.png"), //$NON-NLS-1$
    JOBLET_ICON("/icons/joblet_icon.png"), //$NON-NLS-1$

    MERGE_ICON("/icons/merge.png"), //$NON-NLS-1$

    BOLD_ICON("/icons/bold.gif"), //$NON-NLS-1$
    ITALIC_ICON("/icons/italic.gif"), //$NON-NLS-1$
    LINE_COLOR_ICON("/icons/line_color.gif"), //$NON-NLS-1$
    FONT_COLOR_ICON("/icons/font_color.gif"), //$NON-NLS-1$
    FILL_COLOR_ICON("/icons/fill_color.gif"), //$NON-NLS-1$

    OPEN_IN_BROWSER("/icons/open.gif"), //$NON-NLS-1$
    DOWNLOAD_MODULE("/icons/download.gif"), //$NON-NLS-1$

    COMPONENT_MISSING("/icons/component_missing.gif"), //$NON-NLS-1$

    FILTER_DEACTIVED_ICON("/icons/filter_deactivated.png"), //$NON-NLS-1$
    FILTER_ACTIVED_ICON("/icons/filter_activated.png"), //$NON-NLS-1$

    PARTITIONER_ICON("/icons/partioner.png"), //$NON-NLS-1$
    DEPARTITIONER_ICON("/icons/departitioner.png"), //$NON-NLS-1$
    RECOLLECTOR_ICON("/icons/recollector.png"), //$NON-NLS-1$
    COLLECTOR_ICON("/icons/collector.png"), //$NON-NLS-1$
    REPARTITION_ICON("/icons/repartition.png"), //$NON-NLS-1$

    HADOOP_WIZ_ICON("/icons/hadoop-logo-wiz.png"), //$NON-NLS-1$

    SEPARATOR_ICON("/icons/separator.gif"), //$NON-NLS-1$

    HIGHTLIGHT_ICON("/icons/hightLight.png"), //$NON-NLS-1$

    OPEN_DATA_PREP("/icons/favicon.png"),

    CHESS_GRAY("/icons/gray.gif"); //$NON-NLS-1$

    private String path;

    EImage() {
        this.path = "/icons/unknown.gif"; //$NON-NLS-1$
    }

    EImage(String path) {
        this.path = path;
    }

    /**
     * Getter for path.
     * 
     * @return the path
     */
    @Override
    public String getPath() {
        return this.path;
    }

    /**
     * Getter for clazz.
     * 
     * @return the clazz
     */
    @Override
    public Class getLocation() {
        return EImage.class;
    }

}
