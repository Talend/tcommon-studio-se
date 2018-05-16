// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.core.model.repository.helper;

import java.util.ArrayList;
import java.util.List;

import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.repository.ERepositoryObjectType;

/**
 * DOC bZhou class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z bzhou $
 * 
 */
public final class RepositoryObjectTypeHelper {

    private static ERepositoryObjectType[] typeValues = (ERepositoryObjectType[]) ERepositoryObjectType.values();

    /**
     * DOC bZhou Comment method "getSharedTypeList".
     * 
     * This mehtod is to get all shared types.
     * 
     * @return
     */
    public static List<ERepositoryObjectType> getSharedTypeList() {
        List<ERepositoryObjectType> result = new ArrayList<ERepositoryObjectType>();

        for (ERepositoryObjectType type : typeValues) {
            if (type.isSharedType()) {
                result.add(type);
            }
        }

        return result;
    }

    /**
     * DOC bZhou Comment method "getDQTypeList".
     * 
     * This method is to get all types used in Data Quality, it inclused the Shared types and the DP types.
     * 
     * @return
     */
    public static List<ERepositoryObjectType> getDQTypeList() {
        List<ERepositoryObjectType> result = new ArrayList<ERepositoryObjectType>();

        for (ERepositoryObjectType type : typeValues) {
            if (type.isDQItemType()) {
                result.add(type);
            }
        }

        return result;
    }

    /**
     * DOC bZhou Comment method "getDPTypeList".
     * 
     * This method is to get Data Profile object types. These types is just only used in the Data Profile.
     * 
     * @return
     */
    public static List<ERepositoryObjectType> getDPTypeList() {
        List<ERepositoryObjectType> result = new ArrayList<ERepositoryObjectType>();

        for (ERepositoryObjectType type : typeValues) {
            String[] products = type.getProducts();
            if (products != null && products.length == 1 && type.isDQItemType()) {
                result.add(type);
            }
        }

        return result;
    }

    /**
     * DOC bZhou Comment method "getDQResourceTypeList".
     * 
     * This method is to get all types used in Data Quality, it inclused the Shared types and the DP types.
     * 
     * @return
     */
    public static List<ERepositoryObjectType> getDQResourceTypeList() {
        List<ERepositoryObjectType> result = new ArrayList<ERepositoryObjectType>();

        for (ERepositoryObjectType type : typeValues) {
            if (type.isDQItemType() && type.isResouce()) {
                result.add(type);
            }
        }

        return result;
    }

    /**
     * 
     * create Connection instance by connType
     * 
     * @param connType
     * @return Connection which be Created, null if connType is null.
     * @exception throw UnsupportedOperationException if connType is not be use
     */
    public static Connection CreateConnectionFromType(ERepositoryObjectType connType) {
        if (connType == null) {
            return null;
        }
        if (ERepositoryObjectType.METADATA_CONNECTIONS.equals(connType)) {
            return ConnectionFactory.eINSTANCE.createDatabaseConnection();
        } else if (ERepositoryObjectType.METADATA_FILE_DELIMITED.equals(connType)) {
            return ConnectionFactory.eINSTANCE.createDelimitedFileConnection();
        } else if (ERepositoryObjectType.METADATA_MDMCONNECTION.equals(connType)) {
            return ConnectionFactory.eINSTANCE.createMDMConnection();
        } else {
            throw new UnsupportedOperationException();
        }

    }
}
