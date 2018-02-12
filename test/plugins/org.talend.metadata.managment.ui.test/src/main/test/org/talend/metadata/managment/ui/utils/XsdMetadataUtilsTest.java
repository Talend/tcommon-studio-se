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
package org.talend.metadata.managment.ui.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.core.runtime.FileLocator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.metadata.ISAPConstant;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.XmlFileConnectionItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.datatools.xml.utils.XSDPopulationUtil2;
import org.talend.datatools.xml.utils.XSDUtils;

/**
 * created by wchen on Aug 30, 2016 Detailled comment
 *
 */
public class XsdMetadataUtilsTest {

    /**
     * DOC wchen Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * DOC wchen Comment method "tearDown".
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for
     * {@link org.talend.metadata.managment.ui.utils.XsdMetadataUtils#createMetadataFromXSD(javax.xml.namespace.QName, java.lang.String, java.lang.String, java.lang.String, java.util.Collection, java.io.File, org.talend.datatools.xml.utils.XSDPopulationUtil2)}
     * .
     * 
     * @throws IOException
     * @throws URISyntaxException
     * @throws PersistenceException
     * @throws BusinessException
     */
    @Test
    public void testCreateMetadataFromXSDQNameStringStringStringCollectionOfXmlFileConnectionItemFileXSDPopulationUtil2()
            throws URISyntaxException, IOException, PersistenceException, BusinessException {
        File xsdFile = new File(FileLocator.toFileURL(
                this.getClass().getClassLoader().getResource("resources/RFC_READ_TABLE.xsd")).toURI());
        File wsdlFile = new File(FileLocator.toFileURL(
                this.getClass().getClassLoader().getResource("resources/CardServices.wsdl")).toURI());
        ProxyRepositoryFactory repFactory = ProxyRepositoryFactory.getInstance();
        // create xml connection from xsd
        deleteAllXmlMetadataObject(repFactory);
        Assert.assertEquals("No xml connection before create from .xsd", 0,
                repFactory.getAll(ERepositoryObjectType.METADATA_FILE_XML).size());
        QName inputQName = new QName(ISAPConstant.NAMESAPCE_RUI, "RFC_READ_TABLE");
        XSDPopulationUtil2 populate = XSDUtils.getXsdHander(xsdFile);
        XsdMetadataUtils.createMetadataFromXSD(inputQName, "label", "sap_conn_label", "function_name", Collections.EMPTY_LIST,
                xsdFile, populate);
        Assert.assertEquals("One xml connection created from RFC_READ_TABLE.xsd", 1,
                repFactory.getAll(ERepositoryObjectType.METADATA_FILE_XML).size());

        // create xml connection from wsdl
        deleteAllXmlMetadataObject(repFactory);
        Assert.assertEquals("No xml connection before create from .wsdl", 0,
                repFactory.getAll(ERepositoryObjectType.METADATA_FILE_XML).size());
        inputQName = new QName("http://tempuri.org/", "GetCardLimits");
        populate = XSDUtils.getXsdHander(wsdlFile);
        XsdMetadataUtils.createMetadataFromXSD(inputQName, "GetCardLimits", "ICardServices", "GetCardLimits",
                Collections.EMPTY_LIST, wsdlFile, populate);
        Assert.assertEquals("One xml connection created from RFC_READ_TABLE.xsd", 1,
                repFactory.getAll(ERepositoryObjectType.METADATA_FILE_XML).size());

        deleteAllXmlMetadataObject(repFactory);

    }

    @Test
    public void testCreateMetadataFromXSDQNameStringStringStringFile() throws URISyntaxException, IOException,
            PersistenceException, BusinessException {
        File xsdFile = new File(FileLocator.toFileURL(
                this.getClass().getClassLoader().getResource("resources/RFC_READ_TABLE_Input.xsd")).toURI());
        QName inputQName = new QName(ISAPConstant.NAMESAPCE_RUI, "RFC_READ_TABLE");
        XsdMetadataUtils.createMetadataFromXSD(inputQName, "Input_Schema", "talend_sap", "RFC_READ_TABLE", xsdFile);
        ProxyRepositoryFactory repFactory = ProxyRepositoryFactory.getInstance();

        List<IRepositoryViewObject> all = repFactory.getAll(ERepositoryObjectType.METADATA_FILE_XML);
        for (IRepositoryViewObject xmlObj : all) {
            if ("Input_Schema".equals(xmlObj.getLabel())) {
                XmlFileConnectionItem item = (XmlFileConnectionItem) xmlObj.getProperty().getItem();
                MetadataTable metadataTable = ConnectionHelper.getTables(item.getConnection()).toArray(new MetadataTable[0])[0];
                Assert.assertEquals(metadataTable.getColumns().get(0).getTalendType(), "id_String");
                Assert.assertEquals(metadataTable.getColumns().get(1).getTalendType(), "id_String");
                Assert.assertEquals(metadataTable.getColumns().get(2).getTalendType(), "id_String");
                Assert.assertEquals(metadataTable.getColumns().get(3).getTalendType(), "id_Integer");
                Assert.assertEquals(metadataTable.getColumns().get(4).getTalendType(), "id_Integer");
                Assert.assertEquals(metadataTable.getColumns().get(5).getTalendType(), "id_String");
                Assert.assertEquals(metadataTable.getColumns().get(6).getTalendType(), "id_String");
                Assert.assertEquals(metadataTable.getColumns().get(7).getTalendType(), "id_Long");
                Assert.assertEquals(metadataTable.getColumns().get(8).getTalendType(), "id_Long");
                Assert.assertEquals(metadataTable.getColumns().get(9).getTalendType(), "id_String");
                Assert.assertEquals(metadataTable.getColumns().get(9).getTalendType(), "id_String");
            }
        }
        deleteAllXmlMetadataObject(repFactory);
    }

    private void deleteAllXmlMetadataObject(ProxyRepositoryFactory repFactory) throws PersistenceException, BusinessException {
        List<IRepositoryViewObject> all = repFactory.getAll(ERepositoryObjectType.METADATA_FILE_XML);
        for (IRepositoryViewObject obj : all) {
            repFactory.deleteObjectLogical(obj);
            repFactory.deleteObjectPhysical(obj);
        }
    }
}
