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
package org.talend.core.ui.context;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ICoreService;

/**
 * DOC YeXiaowei class global comment. Detailled comment <br/>
 * 
 */
public abstract class AbstractContextTabEditComposite extends Composite {

    /**
     * DOC YeXiaowei AbstractContextTabEditComposite constructor comment.
     * 
     * @param parent
     * @param style
     */
    public AbstractContextTabEditComposite(Composite parent, int style) {
        super(parent, style);
    }

    /**
     * 
     * cli Comment method "getPreferenceStore".
     * 
     */
    public IPreferenceStore getPreferenceStore() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ICoreService.class)) {
            ICoreService service = (ICoreService) GlobalServiceRegister.getDefault().getService(ICoreService.class);
            return service.getPreferenceStore();
        }
        return null;
    }

    private boolean needRefresh = true;

    public boolean isNeedRefresh() {
        return this.needRefresh;
    }

    public void setNeedRefresh(boolean refresh) {
        this.needRefresh = refresh;
    }

    public abstract IContextModelManager getContextModelManager();

    /**
     * Added by Marvin Wang on Mar.19, 2012 for verifying the current status.
     * 
     * @return
     */
    public abstract boolean isGroupBySource();

    public abstract TreeViewer getViewer();

    public abstract void refresh();

    public void runCommand(Command command) {
        if (getContextModelManager().getCommandStack() == null) {
            command.execute();
        } else {
            getContextModelManager().getCommandStack().execute(command);
        }
    }
}
