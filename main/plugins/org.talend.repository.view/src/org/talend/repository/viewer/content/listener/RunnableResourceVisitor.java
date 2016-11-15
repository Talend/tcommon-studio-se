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
package org.talend.repository.viewer.content.listener;

import java.util.Collection;

import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

public abstract class RunnableResourceVisitor implements IResourceDeltaVisitor {

    private Collection<Runnable> runnables;

    public void setRunnables(Collection<Runnable> runnableCollections) {
        this.runnables = runnableCollections;
    }

    @Override
    public boolean visit(IResourceDelta delta) throws CoreException {
        return visit(delta, runnables);
    }

    abstract protected boolean visit(IResourceDelta delta, Collection<Runnable> runnableCollections);

}
