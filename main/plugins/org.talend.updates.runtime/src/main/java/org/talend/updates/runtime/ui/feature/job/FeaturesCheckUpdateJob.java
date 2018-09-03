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
package org.talend.updates.runtime.ui.feature.job;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.talend.updates.runtime.feature.FeaturesManager;
import org.talend.updates.runtime.feature.FeaturesManager.SearchOption;
import org.talend.updates.runtime.feature.FeaturesManager.SearchResult;
import org.talend.updates.runtime.feature.model.Category;
import org.talend.updates.runtime.feature.model.Type;
import org.talend.updates.runtime.i18n.Messages;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class FeaturesCheckUpdateJob extends Job {

    private FeaturesManager featuresManager;

    private SearchOption option;

    private Exception exception;

    private SearchResult searchResult;

    private volatile boolean finished;

    public FeaturesCheckUpdateJob(FeaturesManager featuresManager) {
        super(Messages.getString("ComponentsManager.job.checkUpdate")); //$NON-NLS-1$
        this.featuresManager = featuresManager;
        this.option = new SearchOption(Type.ALL, Category.ALL, ""); //$NON-NLS-1$
        this.finished = true;
        this.setUser(false);
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        try {
            finished = false;
            searchResult = featuresManager.searchUpdates(monitor, getSearchOption());
        } catch (Exception e) {
            exception = e;
        } finally {
            finished = true;
        }
        return Status.OK_STATUS;
    }

    private SearchOption getSearchOption() {
        return this.option;
    }

    public SearchResult getSearchResult() {
        return this.searchResult;
    }

    public Exception getException() {
        return this.exception;
    }

    public boolean isFinished() {
        return finished;
    }
}
