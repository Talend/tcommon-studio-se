// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.metadata.managment.ui.wizard.metadata.xml.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.xsd.XSDSchema;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.datatools.xml.utils.ATreeNode;
import org.talend.datatools.xml.utils.OdaException;
import org.talend.datatools.xml.utils.SchemaPopulationUtil;
import org.talend.datatools.xml.utils.XSDPopulationUtil2;
import org.talend.datatools.xml.utils.XSDUtils;

/**
 * created by wchen on Aug 12, 2016 Detailled comment
 *
 */
public class TreeUtilTest {

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

    @Test
    public void getXSDSchemaXSDPopulationUtil2StringString() throws URISyntaxException, IOException, OdaException {
        File wsdlFile = new File(FileLocator.toFileURL(
                this.getClass().getClassLoader().getResource("resources/CardServices.wsdl")).toURI());
        XSDPopulationUtil2 populator = XSDUtils.getXsdHander(wsdlFile);
        XSDSchema xsdSchema = TreeUtil.getXSDSchema(populator, wsdlFile.getPath(), "http://tempuri.org/");
        Assert.assertNotNull(xsdSchema);
        List<ATreeNode> allRootNodes = populator.getAllRootNodes(xsdSchema);
        Assert.assertEquals(allRootNodes.size(), 270);
        String element = "tns:BlockCard_Synchronous";
        ATreeNode toTest = null;
        for (ATreeNode treeNode : allRootNodes) {
            if (element.equals(treeNode.getValue())) {
                toTest = treeNode;
                break;
            }
        }
        Assert.assertNotNull(toTest);
        ATreeNode treeNode = SchemaPopulationUtil.getSchemaTree(populator, xsdSchema, toTest);
        String expectedValues = "tns:BlockCard_Synchronous , http://schemas.datacontract.org/2004/07/GFN.Services.CardServices.Card , "
                + "http://tempuri.org/ , "
                + "http://schemas.datacontract.org/2004/07/GFN.Services.Framework.Base ,"
                + " tns:req , tns1:ColCoID , tns1:RequestRef , tns1:UserCulture , tns1:UserName , "
                + "tns2:Request , tns1:Rows , tns1:StartAtRow , tns2:CardID , tns2:Reason , tns2:ReasonID";
        String treeValues = getTreeValues(treeNode);
        Assert.assertEquals(expectedValues, treeValues);
    }

    private String getTreeValues(ATreeNode treeNode) {
        String treeValues = treeNode.toString();
        for (Object child : treeNode.getChildren()) {
            if (treeNode instanceof ATreeNode) {
                treeValues = treeValues + " , " + getTreeValues((ATreeNode) child);
            }
        }
        return treeValues;
    }
}
