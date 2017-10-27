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
package org.talend.core.hadoop.creator;

import java.util.Date;

import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.hadoop.IHadoopConnectionCreator;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * created by ycbai on 2015年6月29日 Detailled comment
 *
 */
public abstract class AbstractHadoopConnectionCreator implements IHadoopConnectionCreator {

    protected String relativeHadoopClusterId;

    protected Item relativeHadoopClusterItem;

    protected IProxyRepositoryFactory factory;

    public void init(String hadoopClusterId) {
        relativeHadoopClusterId = hadoopClusterId;
        relativeHadoopClusterItem = getHadoopClusterItem();
        factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
    }

    protected void setPropertyParameters(Property connectionProperty) {
        connectionProperty.setDisplayName(relativeHadoopClusterItem.getProperty().getLabel() + "_" + getTypeName()); //$NON-NLS-1$
        connectionProperty.setLabel(connectionProperty.getDisplayName());
        connectionProperty.setModificationDate(new Date());
        connectionProperty.setAuthor(((RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                .getProperty(Context.REPOSITORY_CONTEXT_KEY)).getUser());
        connectionProperty.setVersion(VersionUtils.DEFAULT_VERSION);
        connectionProperty.setStatusCode(""); //$NON-NLS-1$
        connectionProperty.setId(factory.getNextId());
    }

    abstract protected void initializeConnectionParameters(Connection connection);

    private Item getHadoopClusterItem() {
        IRepositoryViewObject repObj = null;
        try {
            repObj = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory().getLastVersion(relativeHadoopClusterId);
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
        if (repObj != null && repObj.getProperty() != null) {
            return repObj.getProperty().getItem();
        }
        return null;
    }

}
