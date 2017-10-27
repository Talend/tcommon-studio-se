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
package org.talend.cwm.helper;

import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.util.ConnectionSwitch;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.relational.TdView;
import org.talend.cwm.relational.util.RelationalSwitch;
import org.talend.cwm.softwaredeployment.TdSoftwareSystem;
import org.talend.cwm.softwaredeployment.util.SoftwaredeploymentSwitch;
import org.talend.cwm.xml.TdXmlElementType;
import org.talend.cwm.xml.TdXmlSchema;
import org.talend.cwm.xml.util.XmlSwitch;
import orgomg.cwm.foundation.softwaredeployment.Component;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.ForeignKey;
import orgomg.cwm.resource.relational.NamedColumnSet;
import orgomg.cwm.resource.relational.PrimaryKey;
import orgomg.cwm.resource.relational.Schema;

/**
 * @author scorreia
 * 
 * This class gives easy access to the correctly typed elements.
 */
public final class SwitchHelpers {

    private SwitchHelpers() {
    }

    public static final orgomg.cwm.resource.relational.util.RelationalSwitch<Package> PACKAGE_SWITCH = new orgomg.cwm.resource.relational.util.RelationalSwitch<Package>() {

        @Override
        public Package casePackage(Package object) {
            return object;
        }
    };

    public static final orgomg.cwm.resource.relational.util.RelationalSwitch<PrimaryKey> PRIMARY_KEY_SWITCH = new orgomg.cwm.resource.relational.util.RelationalSwitch<PrimaryKey>() {

        /*
         * (non-Javadoc)
         * 
         * @see
         * orgomg.cwm.resource.relational.util.RelationalSwitch#casePrimaryKey(orgomg.cwm.resource.relational.PrimaryKey
         * )
         */
        @Override
        public PrimaryKey casePrimaryKey(PrimaryKey object) {
            return object;
        }

    };

    public static final orgomg.cwm.resource.relational.util.RelationalSwitch<ForeignKey> FOREIGN_KEY_SWITCH = new orgomg.cwm.resource.relational.util.RelationalSwitch<ForeignKey>() {

        /*
         * (non-Javadoc)
         * 
         * @see
         * orgomg.cwm.resource.relational.util.RelationalSwitch#caseForeignKey(orgomg.cwm.resource.relational.ForeignKey
         * )
         */
        @Override
        public ForeignKey caseForeignKey(ForeignKey object) {
            return object;
        }

    };

    public static final orgomg.cwm.resource.relational.util.RelationalSwitch<Catalog> CATALOG_SWITCH = new orgomg.cwm.resource.relational.util.RelationalSwitch<Catalog>() {

        @Override
        public Catalog caseCatalog(Catalog object) {
            return object;
        }
    };

    public static final orgomg.cwm.resource.relational.util.RelationalSwitch<Schema> SCHEMA_SWITCH = new orgomg.cwm.resource.relational.util.RelationalSwitch<Schema>() {

        @Override
        public Schema caseSchema(Schema object) {
            return object;
        }

    };
    

    public static final XmlSwitch<TdXmlSchema> XMLSCHEMA_SWITCH = new XmlSwitch<TdXmlSchema>() {

        @Override
        public TdXmlSchema caseTdXmlSchema(TdXmlSchema object) {
            return object;
        }

    };

    public static final XmlSwitch<TdXmlElementType> XMLELEMENTTYPE_SWITCH = new XmlSwitch<TdXmlElementType>() {

        @Override
        public TdXmlElementType caseTdXmlElementType(TdXmlElementType object) {
            return object;
        }
    };

    public static final RelationalSwitch<TdTable> TABLE_SWITCH = new RelationalSwitch<TdTable>() {

        @Override
        public TdTable caseTdTable(TdTable object) {
            return object;
        }

    };

    public static final RelationalSwitch<TdView> VIEW_SWITCH = new RelationalSwitch<TdView>() {

        @Override
        public TdView caseTdView(TdView object) {
            return object;
        }

    };

    public static final RelationalSwitch<TdColumn> COLUMN_SWITCH = new RelationalSwitch<TdColumn>() {

        @Override
        public TdColumn caseTdColumn(TdColumn object) {
            return object;
        }

    };

    public static final RelationalSwitch<ColumnSet> COLUMN_SET_SWITCH = new RelationalSwitch<ColumnSet>() {

        @Override
        public ColumnSet caseColumnSet(ColumnSet object) {
            return object;
        }

    };

    public static final RelationalSwitch<NamedColumnSet> NAMED_COLUMN_SET_SWITCH = new RelationalSwitch<NamedColumnSet>() {

        @Override
        public NamedColumnSet caseNamedColumnSet(NamedColumnSet object) {
            return object;
        }

    };

    public static final SoftwaredeploymentSwitch<TdSoftwareSystem> TDSOFTWARE_SYSTEM_SWITCH = new SoftwaredeploymentSwitch<TdSoftwareSystem>() {

        /*
         * (non-Javadoc)
         * 
         * @seeorg.talend.cwm.softwaredeployment.util.SoftwaredeploymentSwitch#caseTdSoftwareSystem(org.talend.cwm.
         * softwaredeployment.TdSoftwareSystem)
         */
        @Override
        public TdSoftwareSystem caseTdSoftwareSystem(TdSoftwareSystem object) {
            return object;
        }

    };

    public static final orgomg.cwm.foundation.softwaredeployment.util.SoftwaredeploymentSwitch<Component> COMPONENT_SWITCH = new orgomg.cwm.foundation.softwaredeployment.util.SoftwaredeploymentSwitch<Component>() {

        /*
         * (non-Javadoc)
         * 
         * @see
         * orgomg.cwm.foundation.softwaredeployment.util.SoftwaredeploymentSwitch#caseComponent(orgomg.cwm.foundation
         * .softwaredeployment.Component)
         */
        @Override
        public Component caseComponent(Component object) {
            return object;
        }

    };

    public static final ConnectionSwitch<MetadataTable> METADATA_TABLE_SWITCH = new ConnectionSwitch<MetadataTable>() {

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.talend.core.model.metadata.builder.connection.util.ConnectionSwitch#caseMetadataTable(org.talend.core
         * .model.metadata.builder.connection.MetadataTable)
         */
        @Override
        public MetadataTable caseMetadataTable(MetadataTable object) {
            return object;
        }
    };

    public static final ConnectionSwitch<Connection> CONNECTION_SWITCH = new ConnectionSwitch<Connection>() {

        public Connection caseConnection(Connection object) {
            return object;
        }

        public Connection caseDatabaseConnection(DatabaseConnection object) {
            return object;
        };

        public Connection caseMDMConnection(MDMConnection object) {
            return object;
        };
    };

    public static final ConnectionSwitch<DatabaseConnection> DATABASECONNECTION_SWITCH = new ConnectionSwitch<DatabaseConnection>() {

        @Override
        public DatabaseConnection caseDatabaseConnection(DatabaseConnection object) {
            return object;
        }

    };

    public static final ConnectionSwitch<MDMConnection> MDMCONNECTION_SWITCH = new ConnectionSwitch<MDMConnection>() {

        @Override
        public MDMConnection caseMDMConnection(MDMConnection object) {
            return object;
        }

    };

    public static final ConnectionSwitch<MetadataColumn> METADATA_COLUMN_SWITCH = new ConnectionSwitch<MetadataColumn>() {

        @Override
        public MetadataColumn caseMetadataColumn(MetadataColumn object) {
            return object;
        }
    };

    public static final ConnectionSwitch<DelimitedFileConnection> DELIMITEDFILECONNECTION_SWITCH = new ConnectionSwitch<DelimitedFileConnection>() {

        @Override
        public DelimitedFileConnection caseDelimitedFileConnection(DelimitedFileConnection object) {
            return object;
        }

    };
}
