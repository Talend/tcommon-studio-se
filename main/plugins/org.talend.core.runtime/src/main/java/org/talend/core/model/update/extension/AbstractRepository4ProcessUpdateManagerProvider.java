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
package org.talend.core.model.update.extension;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.relationship.Relation;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.update.IUpdateItemType;
import org.talend.core.model.update.RepositoryUpdateManagerHelper;
import org.talend.core.model.update.UpdateResult;

/**
 * created by ggu on Mar 28, 2014 Detailled comment
 * 
 * If refactor RepositoryUpdateManager totally later, need change this class, because there are some dupliates.
 * 
 */
public abstract class AbstractRepository4ProcessUpdateManagerProvider extends AbstractRepositoryNodeUpdateManagerProvider {

    @Override
    protected List<UpdateResult> retrieveUpdateResults(IProgressMonitor monitor, final IRepositoryViewObject object) {
        if (object == null) {
            return Collections.emptyList();
        }

        RepositoryUpdateManagerHelper helper = new RepositoryUpdateManagerHelper() {

            @Override
            protected boolean enableCheckItem() {
                return AbstractRepository4ProcessUpdateManagerProvider.this.enableCheckItem(object);
            }

            @Override
            protected List<Relation> getRelations() {
                return AbstractRepository4ProcessUpdateManagerProvider.this.getRelations(object);
            }

            @Override
            protected List<UpdateResult> getOtherUpdateResults(IProgressMonitor monitor, List<IProcess2> openedProcessList,
                    final Set<IUpdateItemType> types) {
                return AbstractRepository4ProcessUpdateManagerProvider.this.getOtherUpdateResults(monitor, object,
                        openedProcessList, types);
            }

            @Override
            protected void checkAndSetParameters(IProcess2 process2) {
                AbstractRepository4ProcessUpdateManagerProvider.this.checkAndSetParameters(object, process2);
            }

        };
        return helper.checkJobItemsForUpdate(monitor, getTypes());
    }

    protected List<Relation> getRelations(final IRepositoryViewObject object) {
        // get the relation of item with other items
        return null;
    }

    protected List<UpdateResult> getOtherUpdateResults(IProgressMonitor monitor, final IRepositoryViewObject object,
            List<IProcess2> openedProcessList, final Set<IUpdateItemType> types) {
        return Collections.emptyList(); // no, default
    }

    protected void checkAndSetParameters(final IRepositoryViewObject object, IProcess2 process2) {
        // nothing to do
    }
}
