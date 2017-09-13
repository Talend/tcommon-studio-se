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
package org.talend.metadata.managment.ui.wizard.metadata.xml.utils;

import static org.junit.Assert.*;

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
import org.talend.commons.runtime.xml.XmlUtil;
import org.talend.datatools.xml.utils.ATreeNode;
import org.talend.datatools.xml.utils.NodeCreationObserver;
import org.talend.datatools.xml.utils.OdaException;
import org.talend.datatools.xml.utils.SchemaPopulationUtil;
import org.talend.datatools.xml.utils.XSDPopulationUtil2;
import org.talend.datatools.xml.utils.XSDUtils;
import org.talend.metadata.managment.ui.wizard.metadata.xml.node.FOXTreeNode;

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

    @Test
    public void testCloneATreeNode() throws Exception {
        String xsdFile = FileLocator.toFileURL(
                this.getClass().getClassLoader().getResource("resources/CCTS_CCT_SchemaModule-2.1.xsd")).toURI().getPath();
        ATreeNode treeNode = SchemaPopulationUtil.getSchemaTree(xsdFile, true, 0);
        FOXTreeNode root = TreeUtil.cloneATreeNode(treeNode, XmlUtil.isXSDFile(xsdFile));
        assertNotNull(root);
        List<FOXTreeNode> list = root.getChildren();
        assertTrue(list != null && list.size() == 10);
        assertEquals("AmountType", list.get(0).getLabel());
        assertEquals("IndicatorType", list.get(1).getLabel());
        assertEquals("NumericType", list.get(2).getLabel());
        assertEquals("CodeType", list.get(3).getLabel());
        assertEquals("MeasureType", list.get(4).getLabel());
        assertEquals("DateTimeType", list.get(5).getLabel());
        assertEquals("QuantityType", list.get(6).getLabel());
        assertEquals("TextType", list.get(7).getLabel());
        assertEquals("BinaryObjectType", list.get(8).getLabel());
        assertEquals("IdentifierType", list.get(9).getLabel());
    }

    @Test
    public void testCloneATreeNode2() throws Exception {
        NodeCreationObserver.start();
        ATreeNode parent = new ATreeNode() {
        };
        parent.setValue("parent"); //$NON-NLS-1$
        ATreeNode child1 = new ATreeNode() {
        };
        child1.setValue("child1"); //$NON-NLS-1$
        parent.addChild(child1);
        ATreeNode subchild1 = new ATreeNode() {
        };
        subchild1.setValue("subchild1"); //$NON-NLS-1$
        child1.addChild(subchild1);
        ATreeNode child2 = new ATreeNode() {
        };
        child2.setValue("child2"); //$NON-NLS-1$
        parent.addChild(child2);
        
        // add again the same child1/subchild1 to the tree on child2.
        child2.addChild(child1);
        NodeCreationObserver.stop();
        
        FOXTreeNode root = TreeUtil.cloneATreeNode(parent, true);
        assertEquals(2, root.getChildren().size());
        assertEquals("child1", root.getChildren().get(0).getLabel());
        assertEquals("child2", root.getChildren().get(1).getLabel());
        assertEquals(1, root.getChildren().get(0).getChildren().size());
        assertEquals("subchild1", root.getChildren().get(0).getChildren().get(0).getLabel());
        
        assertEquals(1, root.getChildren().get(1).getChildren().size());
        assertEquals("child1", root.getChildren().get(1).getChildren().get(0).getLabel());

        assertEquals(1, root.getChildren().get(1).getChildren().get(0).getChildren().size());
        assertEquals("subchild1", root.getChildren().get(1).getChildren().get(0).getChildren().get(0).getLabel());
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

    @Test
    public void getXSDPopulationUtil2AttributeType() throws URISyntaxException, IOException, OdaException {
        File file = new File(FileLocator
                .toFileURL(this.getClass().getClassLoader().getResource("resources/test_xsdAttribute_type.xsd")).toURI());//$NON-NLS-1$
        XSDPopulationUtil2 populator = XSDUtils.getXsdHander(file);
        XSDSchema xsdSchema = TreeUtil.getXSDSchema(populator, file.getPath(), "http://www.domain.com/");//$NON-NLS-1$
        Assert.assertNotNull(xsdSchema);
        List<ATreeNode> allRootNodes = populator.getAllRootNodes(xsdSchema);
        for (ATreeNode rootTreeNode : allRootNodes) {
            ATreeNode treeNode = SchemaPopulationUtil.getSchemaTree(populator, xsdSchema, rootTreeNode);
            FOXTreeNode root = TreeUtil.cloneATreeNode(treeNode, true);
            assertNotNull(root);
            List<FOXTreeNode> list = root.getChildren();
            assertTrue(list != null && list.size() == 3);
            assertEquals("id_Integer", list.get(0).getDataType()); //$NON-NLS-1$
            assertEquals("id_Long", list.get(1).getDataType());//$NON-NLS-1$
            assertEquals("id_String", list.get(2).getDataType());//$NON-NLS-1$
        }
    }
}
