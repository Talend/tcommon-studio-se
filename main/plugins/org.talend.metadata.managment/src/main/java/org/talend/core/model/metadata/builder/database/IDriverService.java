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
package org.talend.core.model.metadata.builder.database;

import java.sql.Driver;
import java.util.List;

import org.talend.core.IService;
import org.talend.core.model.metadata.IMetadataConnection;

/**
 * @author zshen
 * 
 */
public interface IDriverService extends IService {

    /**
     * get driver by metadataConnection information.
     * 
     * @param metadataConnection contain the information which about driver.
     * @return if can't find the driver will get a null.
     */
    abstract public Driver getDriver(IMetadataConnection metadataConnection) throws InstantiationException,
            IllegalAccessException, ClassNotFoundException;

    /**
     * This method to get all database template supported by TDQ.
     * 
     * @return
     */
    public List<String> getTDQSupportDBTemplate();

}
