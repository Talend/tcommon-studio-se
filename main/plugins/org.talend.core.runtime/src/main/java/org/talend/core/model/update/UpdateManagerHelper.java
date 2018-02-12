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
package org.talend.core.model.update;

import java.util.Collection;

import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.runtime.i18n.Messages;

/**
 * DOC ggu class global comment. Detailled comment
 */
public final class UpdateManagerHelper {

    // define in extension point
    public static final String TYPE_JOBLET_CONTEXT = "JOBLET_CONTEXT"; //$NON-NLS-1$

    public static final String TYPE_JOB_CONTEXT = "JOB_CONTEXT"; //$NON-NLS-1$

    public static void setConfigrationForReadOnlyJob(IProcess process, UpdateResult result) {
        if (process != null && process.isReadOnly()) {
            result.setChecked(false);
            result.setRemark(Messages.getString("UpdateManagerHelper.ReadOnlyProcessUpdateWarningMessages")); //$NON-NLS-1$
            result.setReadOnlyProcess(true);
        }
    }

    public static String getResultTaskInfor(UpdateResult result) {
        if (result == null) {
            return UpdatesConstants.EMPTY;
        }
        StringBuffer infor = new StringBuffer();
        infor.append(result.getName());
        infor.append(UpdatesConstants.LEFT_BRACKETS);
        infor.append(result.getCategory());
        infor.append(UpdatesConstants.SEGMENT);
        infor.append(result.getJobInfor());
        infor.append(UpdatesConstants.RIGHT_BRACKETS);
        return infor.toString();
    }

    public static String getRenamedDisplay(final String oldName, final String newName) {
        if (oldName == null || newName == null) {
            return null;
        }
        return oldName + UpdatesConstants.RENAME_SIGN + newName;
    }

    public static String getCollectionsDisplay(Object object, boolean all) {
        if (object == null) {
            return null;
        }
        String displayName = UpdatesConstants.EMPTY;
        if (object instanceof Collection) {
            for (Object obj : (Collection<?>) object) {
                String tmp = null;
                if (obj instanceof String) {
                    tmp = (String) obj;
                } else if (obj instanceof INode) {
                    INode node = (INode) obj;
                    if (all && !node.getLabel().equals(node.getUniqueName())) {
                        tmp = node.getLabel() + addBrackets(node.getUniqueName());
                    } else {
                        tmp = node.getLabel();
                    }
                }
                if (tmp != null) {
                    displayName = displayName + UpdatesConstants.SEGMENT + tmp;
                }
            }
            if (displayName.startsWith(UpdatesConstants.SEGMENT)) {
                displayName = displayName.substring(1);
            }
        }
        return displayName;
    }

    public static String addBrackets(String value) {
        if (value == null || UpdatesConstants.EMPTY.equals(value.trim())) {
            return UpdatesConstants.EMPTY;
        }
        return UpdatesConstants.SPACE + UpdatesConstants.LEFT_BRACKETS + value + UpdatesConstants.RIGHT_BRACKETS;
    }

}
