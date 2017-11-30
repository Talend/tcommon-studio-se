package org.talend.cwm.helper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.SAPBWTable;
import org.talend.core.model.metadata.builder.connection.SAPConnection;

public class SAPBWTableHelper {
    
    // bw object type
    public static final String TYPE_ADVANCEDDATASTOREOBJECT = "AdvancedDataStoreObject"; //$NON-NLS-1$

    public static final String TYPE_DATASOURCE = "DataSource"; //$NON-NLS-1$

    public static final String TYPE_DATASTOREOBJECT = "DataStoreObject"; //$NON-NLS-1$

    public static final String TYPE_INFOCUBE = "InfoCube"; //$NON-NLS-1$

    public static final String TYPE_INFOOBJECT = "InfoObject"; //$NON-NLS-1$

    // InfoObject inner type
    public static final String IO_INNERTYPE_BASIC = "BASIC"; //$NON-NLS-1$

    public static final String IO_INNERTYPE_ATTRIBUTE = "ATTRIBUTE"; //$NON-NLS-1$

    public static final String IO_INNERTYPE_TEXTS = "TEXT"; //$NON-NLS-1$

    public static final String IO_INNERTYPE_HIERARCHY = "HIERARCHY"; //$NON-NLS-1$
    
    public static final String SAP_INFOOBJECT_INNER_TYPE = "innerIOType";  //$NON-NLS-1$

    public static final String SAP_DATASOURCE_SOURCESYSNAME = "SOURCE_SYSNAME";  //$NON-NLS-1$
    
    private static final List<String> INFOOBJECT_INNERTYPE = new ArrayList<String>();
    
    static {
        INFOOBJECT_INNERTYPE.add(IO_INNERTYPE_ATTRIBUTE);
        INFOOBJECT_INNERTYPE.add(IO_INNERTYPE_TEXTS);
        INFOOBJECT_INNERTYPE.add(IO_INNERTYPE_HIERARCHY);
    }


    public static Set<MetadataTable> getBWTables(Connection connection, String bwTableType) {
        Set<MetadataTable> result = new HashSet<MetadataTable>();
        List<SAPBWTable> list = getBWTableList(connection, bwTableType);
        result.addAll(list);
        return result;
    }
    
    public static List<SAPBWTable> getBWTableList(Connection connection, String bwTableType) {
        List<SAPBWTable> bwTables;
        switch (bwTableType) {
        case TYPE_ADVANCEDDATASTOREOBJECT:
            bwTables = ((SAPConnection) connection).getBWAdvancedDataStoreObjects();
            break;
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
        return bwTables;
    }

    public static Set<MetadataTable> getBWTables(Connection connection, String bwTableType, String innerIOType, boolean innerInclude) {
        if (!bwTableType.equals(TYPE_INFOOBJECT)
                || (bwTableType.equals(TYPE_INFOOBJECT) && innerIOType == null)) {
            return getBWTables(connection, bwTableType);
        }
        List<SAPBWTable> bwTables = ((SAPConnection) connection).getBWInfoObjects();
        Set<MetadataTable> innerInfoObjects = new HashSet<MetadataTable>();
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
        List<SAPBWTable> bwTables = getBWTableList(connection, bwTableType);
        if (bwTables.contains(table)) {
            bwTables.remove(table);
        }
    }
    
    public static boolean isInnerIOType(String innerIOType) {
        return INFOOBJECT_INNERTYPE.contains(innerIOType);
    }
}
