package org.talend.cwm.helper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.SAPBWTable;
import org.talend.core.model.metadata.builder.connection.SAPConnection;

public class SAPBWTableHelper {
    
    private static final String TYPE_DATASOURCE = "DataSource"; //$NON-NLS-1$
    private static final String TYPE_DATASTOREOBJECT = "DataStoreObject"; //$NON-NLS-1$
    private static final String TYPE_INFOCUBE = "InfoCube"; //$NON-NLS-1$
    private static final String TYPE_INFOOBJECT = "InfoObject"; //$NON-NLS-1$

    public static Set<SAPBWTable> getBWTables(Connection connection, String bwTableType) {
        Set<SAPBWTable> result = new HashSet<SAPBWTable>();
        List<SAPBWTable> bwTables;
        switch (bwTableType) {
        case TYPE_DATASOURCE:
            bwTables = ((SAPConnection) connection).getBWDataSources();
            break;
        case TYPE_DATASTOREOBJECT:
            bwTables = ((SAPConnection) connection).getBWDataStoreObjects();
            break;
        case TYPE_INFOCUBE:
            bwTables = ((SAPConnection) connection).getBWInfoCubes();
            break;
        case TYPE_INFOOBJECT:
            bwTables = ((SAPConnection) connection).getBWInfoObjects();
            break;
        default:
            bwTables = null;
        }
        result.addAll(bwTables);
        return result;
    }

    public static Set<SAPBWTable> getBWTables(Connection connection, String bwTableType, String innerIOType, boolean innerInclude) {
        if (!bwTableType.equals(TYPE_INFOOBJECT)
                || (bwTableType.equals(TYPE_INFOOBJECT) && innerIOType == null)) {
            return getBWTables(connection, bwTableType);
        }
        List<SAPBWTable> bwTables = ((SAPConnection) connection).getBWInfoObjects();
        Set<SAPBWTable> innerInfoObjects = new HashSet<SAPBWTable>();
        for (SAPBWTable bwTable : bwTables) {
            boolean flag = innerIOType.equals(bwTable.getInnerIOType());
            if(!innerInclude) {
                flag = !flag;
            }
            if (flag) {
                innerInfoObjects.add(bwTable);
            }
        }
        return innerInfoObjects;
    }
    
    public static void removeBWTable(Connection connection, String bwTableType, SAPBWTable table) {
        Set<SAPBWTable> bwTables = getBWTables(connection, bwTableType);
        if (bwTables.contains(table)) {
            bwTables.remove(table);
        }
    }
}
