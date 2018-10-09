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
package org.talend.updates.runtime.ui.feature.form.listener;

import org.eclipse.jface.operation.IRunnableWithProgress;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public interface ICheckListener {

    public void showMessage(String message, int level);

    public String getMessage();

    public void updateButtons();

    public void run(boolean fork, boolean cancelable, IRunnableWithProgress runnable) throws Exception;

}
