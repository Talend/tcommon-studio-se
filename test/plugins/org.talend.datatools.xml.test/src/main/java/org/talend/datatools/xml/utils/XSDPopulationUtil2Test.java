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
package org.talend.datatools.xml.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.xsd.XSDSchema;
import org.junit.Assert;
import org.junit.Test;

/**
 * created by wchen on May 9, 2018 Detailled comment
 *
 */
public class XSDPopulationUtil2Test {

    /**
     * 
     * DOC wchen Comment method "getSchemaTreeXSDSchemaATreeNode".
     * 
     * @throws URISyntaxException
     * @throws IOException
     * @throws OdaException
     */
    @Test
    public void testGetSchemaTreeForLoopReference() throws URISyntaxException, IOException, OdaException {
        // loop_ref.xsd has loop reference like SubGroupCollection=>SubGroup=>SubGroupCollection
        // the second SubGroupCollection will only show a root element bug no children
        URL resource = this.getClass().getClassLoader().getResource("resources/loop_ref.xsd");
        File testFile = new File(FileLocator.toFileURL(resource).toURI());

        XSDPopulationUtil2 populate = new XSDPopulationUtil2();
        XSDSchema xsdSchema = populate.getXSDSchema(testFile.getAbsolutePath());
        List<ATreeNode> allRootNodes = populate.getAllRootNodes(xsdSchema);
        Assert.assertEquals(allRootNodes.size(), 2);

        ATreeNode shipmentNode = allRootNodes.get(1);
        shipmentNode = populate.getSchemaTree(xsdSchema, shipmentNode);
        Assert.assertEquals(shipmentNode.getLabel(), "Shipment");

        Object[] children = shipmentNode.getChildren();
        Assert.assertEquals(children.length, 1);
        ATreeNode commercialInfo = ((ATreeNode) children[0]);
        Assert.assertEquals(commercialInfo.getLabel(), "CommercialInfo");

        children = commercialInfo.getChildren();
        Assert.assertEquals(children.length, 1);
        ATreeNode subGroupCollection = ((ATreeNode) children[0]);
        Assert.assertEquals(subGroupCollection.getLabel(), "SubGroupCollection");

        children = subGroupCollection.getChildren();
        Assert.assertEquals(children.length, 2);
        ATreeNode amount = ((ATreeNode) children[0]);
        Assert.assertEquals(amount.getLabel(), "Amount");
        ATreeNode subGroup = ((ATreeNode) children[1]);
        Assert.assertEquals(subGroup.getLabel(), "SubGroup");

        children = subGroup.getChildren();
        Assert.assertEquals(children.length, 1);
        ATreeNode subGroupCollection_2 = ((ATreeNode) children[0]);
        Assert.assertEquals(subGroupCollection_2.getLabel(), "SubGroupCollection");

        children = subGroupCollection_2.getChildren();
        Assert.assertEquals(children.length, 0);
    }

    /**
     * 
     * DOC wchen Comment method "getSchemaTreeXSDSchemaATreeNode".
     * 
     * @throws URISyntaxException
     * @throws IOException
     * @throws OdaException
     */
    @Test
    public void testGetSchemaTreeForMissingNodes() throws URISyntaxException, IOException, OdaException {
        // from some old version , when the node AddInfo is used in two branches, it can't get children of it in the
        // second branch
        URL resource = this.getClass().getClassLoader().getResource("resources/missing_node_test.xsd");
        File testFile = new File(FileLocator.toFileURL(resource).toURI());

        XSDPopulationUtil2 populate = new XSDPopulationUtil2();
        XSDSchema xsdSchema = populate.getXSDSchema(testFile.getAbsolutePath());
        List<ATreeNode> allRootNodes = populate.getAllRootNodes(xsdSchema);
        Assert.assertEquals(allRootNodes.size(), 5);

        ATreeNode commercialInfo = allRootNodes.get(1);
        commercialInfo = populate.getSchemaTree(xsdSchema, commercialInfo);
        Assert.assertEquals(commercialInfo.getLabel(), "CommercialInfo");

        Object[] children = commercialInfo.getChildren();
        Assert.assertEquals(children.length, 2);
        ATreeNode studentCollection = ((ATreeNode) children[0]);
        Assert.assertEquals(studentCollection.getLabel(), "StudentCollection");
        testBranches(studentCollection);

        ATreeNode teacherCollection = ((ATreeNode) children[1]);
        Assert.assertEquals(teacherCollection.getLabel(), "TeacherCollection");
        testBranches(teacherCollection);

    }

    private void testBranches(ATreeNode testBranchNode) {
        Object[] children = testBranchNode.getChildren();
        Assert.assertEquals(children.length, 1);
        ATreeNode personInfo = ((ATreeNode) children[0]);
        Assert.assertEquals(personInfo.getLabel(), "PersonInfo");

        children = personInfo.getChildren();
        Assert.assertEquals(children.length, 6);
        ATreeNode hobby = ((ATreeNode) children[2]);
        Assert.assertEquals(hobby.getLabel(), "Hobby");

        children = hobby.getChildren();
        Assert.assertEquals(children.length, 2);
        ATreeNode name = ((ATreeNode) children[0]);
        Assert.assertEquals(name.getLabel(), "name");
        ATreeNode addInfo = ((ATreeNode) children[1]);
        Assert.assertEquals(addInfo.getLabel(), "AddInfo");

        children = addInfo.getChildren();
        Assert.assertEquals(children.length, 2);
        ATreeNode key = ((ATreeNode) children[0]);
        Assert.assertEquals(key.getLabel(), "Key");
        ATreeNode value = ((ATreeNode) children[1]);
        Assert.assertEquals(value.getLabel(), "Value");
    }

}
