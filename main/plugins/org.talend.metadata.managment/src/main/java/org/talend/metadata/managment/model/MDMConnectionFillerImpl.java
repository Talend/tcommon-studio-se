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
package org.talend.metadata.managment.model;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.talend.commons.utils.data.list.ListUtils;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.cwm.constants.SoftwareSystemConstants;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.softwaredeployment.SoftwaredeploymentFactory;
import org.talend.cwm.softwaredeployment.TdSoftwareSystem;
import org.talend.cwm.xml.TdXmlSchema;
import org.talend.cwm.xml.XmlFactory;
import org.talend.metadata.managment.mdm.AbsMdmConnectionHelper;
import org.talend.metadata.managment.mdm.S60MdmConnectionHelper;
import org.talend.metadata.managment.utils.DatabaseConstant;
import org.talend.metadata.managment.utils.MetadataConnectionUtils;
import org.talend.model.bridge.ReponsitoryContextBridge;
import org.talend.utils.sugars.ReturnCode;
import orgomg.cwm.foundation.softwaredeployment.Component;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * @author zshen
 * 
 */
public class MDMConnectionFillerImpl extends MetadataFillerImpl<MDMConnection> {

    private static Logger log = Logger.getLogger(MDMConnectionFillerImpl.class);

    @Override
    public MDMConnection fillUIConnParams(IMetadataConnection metadataBean, MDMConnection connection) {
        MDMConnection mdmConn = connection;
        if (mdmConn == null) {
            mdmConn = ConnectionHelper.createMDMConnection(metadataBean.getDataSourceName());
        }
        if (super.fillUIConnParams(metadataBean, mdmConn) == null) {
            return null;
        }
        ReturnCode rc = checkConnection(metadataBean);
        if (rc.isOk()) {

            // create softwareSystem
            TdSoftwareSystem softwareSystem = SoftwaredeploymentFactory.eINSTANCE.createTdSoftwareSystem();
            softwareSystem.setName(metadataBean.getDatabase());
            // system.setSubtype(databaseProductName);
            softwareSystem.setType(SoftwareSystemConstants.DBMS.toString());
            softwareSystem.setVersion(metadataBean.getVersion());
            Component component = orgomg.cwm.foundation.softwaredeployment.SoftwaredeploymentFactory.eINSTANCE.createComponent();
            softwareSystem.getOwnedElement().add(component);
            ConnectionHelper.setSoftwareSystem(connection, softwareSystem);

            // set dataprovider
            ConnectionHelper.setDataFilter(metadataBean.getDatafilter(), mdmConn);
            ConnectionHelper.setUniverse(metadataBean.getUniverse(), connection);
            // fill some base parameter
            fillMetadataParams(metadataBean, connection);
            // initialize database driver

        }
        return connection;
    }

    @Override
    public List<orgomg.cwm.objectmodel.core.Package> fillSchemas(MDMConnection mdmConn, DatabaseMetaData dbJDBCMetadata,
            List<String> schemaFilter) {
        List<TdXmlSchema> xmlDocs = new ArrayList<TdXmlSchema>();
        try {
            Object stub = MetadataConnectionUtils.getXtentisBindingStub(mdmConn);
            AbsMdmConnectionHelper connectionHelper = null;
            String regex = "";
            connectionHelper = new S60MdmConnectionHelper();
            regex = "*";
            List<String> pks = connectionHelper.getPKs(stub, "getDataModelPKs", "org.talend.mdm.webservice.WSRegexDataModelPKs",
                    regex, "getWsDataModelPKs");
            String techXSDFolderName = getTechXSDFolderName();
            for (String pk : pks) {
                if (isCreateElement(schemaFilter, pk, false)) {
                    adaptToCWMDocument(connectionHelper, xmlDocs, stub, pk, techXSDFolderName, mdmConn);
                }
            }

            // WSDataModelPK[] pks = stub.getDataModelPKs(new WSRegexDataModelPKs(StringUtils.EMPTY));
            // String techXSDFolderName = getTechXSDFolderName();
            //
            // for (WSDataModelPK pk : pks) {
            // if (isCreateElement(schemaFilter, pk.getPk())) {
            // adaptToCWMDocument(xmlDocs, stub, pk.getPk(), techXSDFolderName, mdmConn);
            // }
            // }
        } catch (Exception e) {
            log.error(e, e);
        }
        ConnectionHelper.addXMLDocuments(xmlDocs);
        return ListUtils.castList(orgomg.cwm.objectmodel.core.Package.class, xmlDocs);
    }

    private String getTechXSDFolderName() {
        String techXSDFolderName = MetadataConnectionUtils.createTechnicalName(DatabaseConstant.XSD_SUFIX
                + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss")); //$NON-NLS-1$
        IFolder xsdFolder = ReponsitoryContextBridge.getRootProject().getFolder(new Path("metadata/MDMconnections")); //$NON-NLS-1$
        // ResourceManager.getMDMConnectionFolder().getFolder(DatabaseConstant.XSD_SUFIX);
        if (xsdFolder.getFolder(techXSDFolderName).exists()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getTechXSDFolderName();
        }
        return techXSDFolderName;
    }

    /**
     * DOC xqliu Comment method "adaptToCWMDocument".
     * 
     * @param xmlDocCollection
     * @param stub
     * @param resName
     * @param providerTechName
     * @param dataProvider
     * @throws RemoteException
     * @throws CoreException
     */
    private void adaptToCWMDocument(AbsMdmConnectionHelper connectionHelper, List<TdXmlSchema> xmlDocCollection, Object stub,
            String resName, String providerTechName, Connection dataProvider) throws RemoteException, CoreException {
        // MOD xqliu 2010-10-18 bug 16161
        String resXSD = null;
        try {
            resXSD = connectionHelper.getXsdSchema(stub, resName);
            // resXSD = stub.getDataModel(new WSGetDataModel(new WSDataModelPK(resName))).getXsdSchema();
        } catch (Exception e1) {
            log.warn(e1, e1);
        }
        if (resXSD == null || StringUtils.EMPTY.equals(resXSD.trim())) {
            log.warn("XSD not exist for \"" + resName + "\""); //$NON-NLS-1$ //$NON-NLS-2$
            return;
        }
        // ~ 16161
        // Save xsd file to local disk.
        // TODO Specify unique xsd file name.
        IFolder xsdFolder = ReponsitoryContextBridge.getRootProject().getFolder(new Path("metadata/MDMconnections")) //$NON-NLS-1$
                .getFolder(DatabaseConstant.XSD_SUFIX);
        if (!xsdFolder.exists()) {
            xsdFolder.create(true, true, new NullProgressMonitor());
        }
        xsdFolder = xsdFolder.getFolder(providerTechName);
        if (!xsdFolder.exists()) {
            xsdFolder.create(true, true, new NullProgressMonitor());
        }
        IFile file = xsdFolder.getFile(resName + DatabaseConstant.XSD_SUFIX);
        // zshen bug 14089: unfolder MDM node get exception.because of the encoding of stream
        try {
            file.create(new ByteArrayInputStream(resXSD.getBytes("UTF-8")), true, new NullProgressMonitor()); //$NON-NLS-1$
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // ~14089
        TdXmlSchema tdXmlDoc = XmlFactory.eINSTANCE.createTdXmlSchema();
        tdXmlDoc.setName(resName);
        // TODO Specify unique xsd file name.
        tdXmlDoc.setXsdFilePath(DatabaseConstant.XSD_SUFIX + File.separator + xsdFolder.getName() + File.separator
                + file.getName());
        tdXmlDoc.getDataManager().add(dataProvider);

        xmlDocCollection.add(tdXmlDoc);
    }

    @Override
    public List<Catalog> fillCatalogs(MDMConnection dbConn, DatabaseMetaData dbJDBCMetadata, List<String> catalogFilter) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Catalog> fillCatalogs(MDMConnection dbConn, DatabaseMetaData dbJDBCMetadata, IMetadataConnection metaConnection,
            List<String> catalogFilter) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<TdTable> fillTables(Package pack, DatabaseMetaData dbJDBCMetadata, List<String> tableFilter, String tablePattern,
            String[] tableType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Schema> fillSchemaToCatalog(MDMConnection dbConn, DatabaseMetaData dbJDBCMetadata, Catalog catalog,
            List<String> schemaFilter) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<MetadataTable> fillAll(Package pack, DatabaseMetaData dbJDBCMetadata, List<String> tableFilter,
            String tablePattern, String[] tableType) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.IMetadataFiller#fillAll(orgomg.cwm.objectmodel.core.Package,
     * java.sql.DatabaseMetaData, org.talend.core.model.metadata.IMetadataConnection, java.util.List, java.lang.String,
     * java.lang.String[])
     */
    @Override
    public List<MetadataTable> fillAll(Package pack, DatabaseMetaData dbJDBCMetadata, IMetadataConnection metaConnection,
            List<String> tableFilter, String tablePattern, String[] tableType) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.metadata.IMetadataFiller#fillSchemas(org.talend.core.model.metadata.builder.connection.
     * Connection, java.sql.DatabaseMetaData, org.talend.core.model.metadata.IMetadataConnection, java.util.List)
     */
    @Override
    public List<Package> fillSchemas(MDMConnection dbConn, DatabaseMetaData dbJDBCMetadata, IMetadataConnection metaConnection,
            List<String> Filter) {
        // TODO Auto-generated method stub
        return null;
    }

}
