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
package org.talend.core.model.repository;

import java.util.Date;

import org.eclipse.core.runtime.IPath;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.impl.ItemImpl;
import org.talend.core.model.properties.impl.PropertyImpl;
import org.talend.core.runtime.CoreRuntimePlugin;

/**
 * created by wchen on 2014-3-11 Detailled comment
 * 
 */
public class FakePropertyImpl extends PropertyImpl {

    private IPath itemPath;

    /**
     * DOC FakePropertyImpl constructor comment.
     * 
     * @param id
     */
    public FakePropertyImpl() {
        Context ctx = CoreRuntimePlugin.getInstance().getContext();
        RepositoryContext repoContext = (RepositoryContext) ctx.getProperty(Context.REPOSITORY_CONTEXT_KEY);
        this.setId("1");
        this.setAuthor(repoContext.getUser());
        setVersion(""); //$NON-NLS-1$
        setCreationDate(new Date());

    }

    @Override
    public void setItem(Item newItem) {
        super.setItem(newItem);
        ItemState itemState = PropertiesFactory.eINSTANCE.createItemState();
        getItem().setState(itemState);
        getItem().setProperty(this);
    }

    public class FakeItemImpl extends ItemImpl {

        /**
         * DOC TransformFakeItemImpl constructor comment.
         */
        public FakeItemImpl() {
            super();
        }

    }

    /**
     * Sets the itemPath.
     * 
     * @param itemPath the itemPath to set
     */
    public void setItemPath(IPath itemPath) {
        this.itemPath = itemPath;
    }

    /**
     * Getter for itemPath.
     * 
     * @return the itemPath
     */
    public IPath getItemPath() {
        return this.itemPath;
    }

}
