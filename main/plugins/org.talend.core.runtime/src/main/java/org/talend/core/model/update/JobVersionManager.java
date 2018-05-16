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

import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.process.IProcess;
import org.talend.core.model.repository.IRepositoryViewObject;

/**
 * DOC gldu class global comment. Detailled comment
 */
public class JobVersionManager {

    private static JobVersionManager instance = null;

    private IRepositoryViewObject node;

    private List<IProcess> jobVersionResults = new ArrayList<IProcess>();

    public static JobVersionManager getInstance() {
        if (instance == null) {
            instance = new JobVersionManager();
        }
        return instance;
    }

    public IRepositoryViewObject getNode() {
        return this.node;
    }

    public void setNode(IRepositoryViewObject node) {
        this.node = node;
    }

}
