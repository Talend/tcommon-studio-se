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
package org.talend.core.repository.svn;

import org.talend.repository.ui.actions.AContextualAction;

/**
 * created by wchen on 2014-4-1 Detailled comment
 * 
 */
public abstract class SvnCommitHook extends AContextualAction {

    private static boolean activeAutoCommit = false;

    public static boolean isActiveAutoCommit() {
        return activeAutoCommit;
    }

    public static void activeAutoCommit() {
        activeAutoCommit = true;
    }

    public static void desactAutoCommit() {
        activeAutoCommit = false;
    }
}
