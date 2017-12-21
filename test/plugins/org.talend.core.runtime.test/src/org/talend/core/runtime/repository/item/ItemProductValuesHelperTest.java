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
package org.talend.core.runtime.repository.item;

import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.Map;

import org.eclipse.emf.common.util.EMap;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.repository.ProjectManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ItemProductValuesHelperTest {

    static IBrandingService brandingService = null;

    private Property prop;

    @BeforeClass
    public static void setup() {
        brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(IBrandingService.class);
        assertNotNull(brandingService);
    }

    @Before
    public void init() {
        prop = PropertiesFactory.eINSTANCE.createProperty();

        Date createdDate = new Date();
        try {
            Thread.sleep(100);// make sure the date is different
        } catch (InterruptedException e) {
            //
        }
        Date modifiedDate = new Date();

        prop.setCreationDate(createdDate);
        prop.setModificationDate(modifiedDate);
    }

    @Test
    public void test_existed_null() {
        assertFalse(ItemProductValuesHelper.existed(null));
    }

    @Test
    public void test_existed_empty() {
        Property prop = PropertiesFactory.eINSTANCE.createProperty();
        assertFalse(ItemProductValuesHelper.existed(prop));
    }

    @Test
    public void test_existed_others() {
        EMap additionalProp = prop.getAdditionalProperties();

        additionalProp.put("ABC", "XYZ");

        assertFalse(ItemProductValuesHelper.existed(prop));
    }

    @Test
    public void test_existed_modifiedKey() {
        EMap additionalProp = prop.getAdditionalProperties();

        additionalProp.put(ItemProductKeys.FULLNAME.getModifiedKey(), "TOS");

        assertTrue("Don't exist modified or created product full name", ItemProductValuesHelper.existed(prop));
    }

    @Test
    public void test_existed_createdKey() {
        EMap additionalProp = prop.getAdditionalProperties();

        additionalProp.put(ItemProductKeys.FULLNAME.getCreatedKey(), "TOS");

        assertTrue("Don't exist modified or created product full name", ItemProductValuesHelper.existed(prop));
    }

    @Test
    public void test_existed_importKey() {
        EMap additionalProp = prop.getAdditionalProperties();

        additionalProp.put(ItemProductKeys.FULLNAME.getImportKey(), "TOS");

        assertFalse("Only import product full name is invalid", ItemProductValuesHelper.existed(prop));
    }

    @Test
    public void test_existed_all() {
        EMap additionalProp = prop.getAdditionalProperties();

        additionalProp.put(ItemProductKeys.FULLNAME.getModifiedKey(), "TP");
        additionalProp.put(ItemProductKeys.FULLNAME.getCreatedKey(), "TOP");
        additionalProp.put(ItemProductKeys.FULLNAME.getImportKey(), "TP");

        assertTrue("Don't exist modified or created product full name", ItemProductValuesHelper.existed(prop));
    }

    @Test
    public void test_setValuesWhenCreate() {
        doTestSetValuesWhenCreate(new Date());
    }

    @Test
    public void test_setValuesWhenCreate_nullDate() {
        doTestSetValuesWhenCreate(null);
    }

    private void doTestSetValuesWhenCreate(Date date) {
        EMap additionalProp = prop.getAdditionalProperties();
        boolean set = ItemProductValuesHelper.setValuesWhenCreate(prop, date);
        assertTrue(set);

        assertThat(additionalProp.get(ItemProductKeys.FULLNAME.getCreatedKey()), equalTo(brandingService.getFullProductName()));
        assertThat(additionalProp.get(ItemProductKeys.VERSION.getCreatedKey()), equalTo(VersionUtils.getDisplayVersion()));
        assertThat(additionalProp.get(ItemProductKeys.DATE.getCreatedKey()), anything());

    }

    @Test
    public void test_setValuesWhenModify() {
        doTestSetValuesWhenModify(new Date());
    }

    @Test
    public void test_setValuesWhenModify_nullDate() {
        doTestSetValuesWhenModify(null);
    }

    private void doTestSetValuesWhenModify(Date date) {
        boolean set = ItemProductValuesHelper.setValuesWhenModify(prop, date);
        assertTrue(set);

        EMap additionalProp = prop.getAdditionalProperties();
        assertThat(additionalProp.get(ItemProductKeys.FULLNAME.getModifiedKey()), equalTo(brandingService.getFullProductName()));
        assertThat(additionalProp.get(ItemProductKeys.VERSION.getModifiedKey()), equalTo(VersionUtils.getDisplayVersion()));
        assertThat(additionalProp.get(ItemProductKeys.DATE.getModifiedKey()), anything());
    }

    @Test
    public void test_setValuesWhenMigrate_exist() {
        prop.getAdditionalProperties().put(ItemProductKeys.FULLNAME.getModifiedKey(), "xxxx");

        boolean set = ItemProductValuesHelper.setValuesWhenMigrate(prop, null);
        assertFalse(set);
    }

    @Test
    public void test_setValuesWhenMigrate_nullDate() {
        String productFullname = "Talend Open Studio for Big Data";
        String productVersion = "6.5.1.20171110_1941";
        Project project = PropertiesFactory.eINSTANCE.createProject();
        project.setProductVersion("Talend Open Studio for Big Data-6.5.1.20171110_1941");

        boolean set = ItemProductValuesHelper.setValuesWhenMigrate(prop, project);
        assertTrue(set);
        EMap additionalProp = prop.getAdditionalProperties();

        assertThat(additionalProp.get(ItemProductKeys.FULLNAME.getCreatedKey()), equalTo(productFullname));
        assertThat(additionalProp.get(ItemProductKeys.VERSION.getCreatedKey()), equalTo(productVersion));
        assertThat(additionalProp.get(ItemProductKeys.DATE.getCreatedKey()), anything());
        assertThat(prop.getCreationDate(), notNullValue());

        assertThat(additionalProp.get(ItemProductKeys.FULLNAME.getModifiedKey()), equalTo(productFullname));
        assertThat(additionalProp.get(ItemProductKeys.VERSION.getModifiedKey()), equalTo(productVersion));
        assertThat(additionalProp.get(ItemProductKeys.DATE.getModifiedKey()), anything());
        assertThat(prop.getModificationDate(), notNullValue());
    }

    @Test
    public void test_setValuesWhenMigrate_nullProject() {
        org.talend.core.model.general.Project currentProject = ProjectManager.getInstance().getCurrentProject();
        assertNotNull(currentProject);
        Project project = currentProject.getEmfProject();
        Map<String, String> productValues = ItemProductValuesHelper.parseProduct(project.getProductVersion());
        assertFalse(productValues.isEmpty());
        String productFullname = productValues.keySet().iterator().next();
        String productVersion = productValues.get(productFullname);

        //
        boolean set = ItemProductValuesHelper.setValuesWhenMigrate(prop, null);
        assertTrue(set);

        EMap additionalProp = prop.getAdditionalProperties();
        assertThat(additionalProp.get(ItemProductKeys.FULLNAME.getCreatedKey()), equalTo(productFullname));
        assertThat(additionalProp.get(ItemProductKeys.VERSION.getCreatedKey()), equalTo(productVersion));
        assertThat(additionalProp.get(ItemProductKeys.DATE.getCreatedKey()), anything());
        assertThat(prop.getCreationDate(), notNullValue());

        assertThat(additionalProp.get(ItemProductKeys.FULLNAME.getModifiedKey()), equalTo(productFullname));
        assertThat(additionalProp.get(ItemProductKeys.VERSION.getModifiedKey()), equalTo(productVersion));
        assertThat(additionalProp.get(ItemProductKeys.DATE.getModifiedKey()), anything());
        assertThat(prop.getModificationDate(), notNullValue());

    }

    @Test
    public void test_setValuesWhenMigrate() {
        String productFullname = "Talend Open Studio for Big Data";
        String productVersion = "6.5.1.20171110_1941";
        Project project = PropertiesFactory.eINSTANCE.createProject();
        project.setProductVersion("Talend Open Studio for Big Data-6.5.1.20171110_1941");

        Date createdDate = prop.getCreationDate();
        Date modifiedDate = prop.getModificationDate();
        boolean set = ItemProductValuesHelper.setValuesWhenMigrate(prop, project);
        assertTrue(set);

        EMap additionalProp = prop.getAdditionalProperties();
        assertThat(additionalProp.get(ItemProductKeys.FULLNAME.getCreatedKey()), equalTo(productFullname));
        assertThat(additionalProp.get(ItemProductKeys.VERSION.getCreatedKey()), equalTo(productVersion));
        assertThat(additionalProp.get(ItemProductKeys.DATE.getCreatedKey()),
                equalTo(ItemProductValuesHelper.DATEFORMAT.format(createdDate)));
        assertThat(prop.getCreationDate(), equalTo(createdDate));

        assertThat(additionalProp.get(ItemProductKeys.FULLNAME.getModifiedKey()), equalTo(productFullname));
        assertThat(additionalProp.get(ItemProductKeys.VERSION.getModifiedKey()), equalTo(productVersion));
        assertThat(additionalProp.get(ItemProductKeys.DATE.getModifiedKey()),
                equalTo(ItemProductValuesHelper.DATEFORMAT.format(modifiedDate)));
        assertThat(prop.getModificationDate(), equalTo(modifiedDate));
    }

    @Test
    public void test_setValuesWhenImport_null() {
        boolean set = ItemProductValuesHelper.setValuesWhenImport(null, null);
        assertFalse(set);

        Property prop = PropertiesFactory.eINSTANCE.createProperty();
        set = ItemProductValuesHelper.setValuesWhenImport(prop, null);
        assertFalse(set);

        Project project = PropertiesFactory.eINSTANCE.createProject();
        set = ItemProductValuesHelper.setValuesWhenImport(null, project);
        assertFalse(set);
    }

    @Test
    public void test_setValuesWhenImport_empty() {
        Project project = PropertiesFactory.eINSTANCE.createProject();
        project.setProductVersion("");

        boolean set = ItemProductValuesHelper.setValuesWhenImport(prop, project);
        assertFalse(set);
    }

    // @Test
    public void test_setValuesWhenImport_tosdq() {
        // not productversion for TOS DQ
        fail("There is no product version value");
        // doTest_SetValuesForImport("","","");
    }

    @Test
    public void test_setValuesWhenImport_withMigrating() {
        String productFullname = "Talend Open Studio for Big Data";
        String productVersion = "6.5.1.20171110_1941";
        Project project = PropertiesFactory.eINSTANCE.createProject();
        project.setProductVersion("Talend Open Studio for Big Data-6.5.1.20171110_1941");

        boolean set = ItemProductValuesHelper.setValuesWhenImport(prop, project);
        assertTrue(set);

        EMap additionalProp = prop.getAdditionalProperties();
        assertThat(additionalProp.get(ItemProductKeys.FULLNAME.getImportKey()), equalTo(productFullname));
        assertThat(additionalProp.get(ItemProductKeys.VERSION.getImportKey()), equalTo(productVersion));
        assertThat(additionalProp.get(ItemProductKeys.DATE.getImportKey()), anything());
        assertThat(prop.getCreationDate(), notNullValue());

        assertThat(additionalProp.get(ItemProductKeys.FULLNAME.getCreatedKey()), equalTo(productFullname));
        assertThat(additionalProp.get(ItemProductKeys.VERSION.getCreatedKey()), equalTo(productVersion));
        assertThat(additionalProp.get(ItemProductKeys.DATE.getCreatedKey()), anything());
        assertThat(prop.getCreationDate(), notNullValue());

        assertThat(additionalProp.get(ItemProductKeys.FULLNAME.getModifiedKey()), equalTo(productFullname));
        assertThat(additionalProp.get(ItemProductKeys.VERSION.getModifiedKey()), equalTo(productVersion));
        assertThat(additionalProp.get(ItemProductKeys.DATE.getModifiedKey()), anything());
        assertThat(prop.getModificationDate(), notNullValue());
    }

    @Test
    public void test_setValuesWhenImport_withoutMigrating() {
        String productFullname = "Talend Open Studio for Big Data";
        String productVersion = "6.5.1.20171110_1941";
        Project project = PropertiesFactory.eINSTANCE.createProject();
        project.setProductVersion("Talend Open Studio for Big Data-6.5.1.20171110_1941");

        EMap additionalProp = prop.getAdditionalProperties();
        additionalProp.put(ItemProductKeys.FULLNAME.getModifiedKey(), "XXXX"); // set flag to avoid migrating

        boolean set = ItemProductValuesHelper.setValuesWhenImport(prop, project);
        assertTrue(set);

        assertThat(additionalProp.get(ItemProductKeys.FULLNAME.getImportKey()), equalTo(productFullname));
        assertThat(additionalProp.get(ItemProductKeys.VERSION.getImportKey()), equalTo(productVersion));
        assertThat(additionalProp.get(ItemProductKeys.DATE.getImportKey()), anything());

        assertThat(additionalProp.get(ItemProductKeys.FULLNAME.getCreatedKey()), nullValue());
        assertThat(additionalProp.get(ItemProductKeys.VERSION.getCreatedKey()), nullValue());
        assertThat(additionalProp.get(ItemProductKeys.DATE.getCreatedKey()), nullValue());
        assertThat(prop.getCreationDate(), notNullValue());

        assertThat(additionalProp.get(ItemProductKeys.FULLNAME.getModifiedKey()), equalTo("XXXX"));
        assertThat(additionalProp.get(ItemProductKeys.VERSION.getModifiedKey()), nullValue());
        assertThat(additionalProp.get(ItemProductKeys.DATE.getModifiedKey()), anything());
        assertThat(prop.getModificationDate(), notNullValue());
    }

    @Test
    public void test_parseProduct_empty() {
        Map<String, String> values = ItemProductValuesHelper.parseProduct(null);
        assertTrue(values.isEmpty());

        values = ItemProductValuesHelper.parseProduct("");
        assertTrue(values.isEmpty());
    }

    @Test
    public void test_parseProduct_invalid() {
        doTestParseProduct("ABCD", "ABCD", null);

        doTestParseProduct("Talend Open Studio for Big Data+6.5.1.20171110_1941",
                "Talend Open Studio for Big Data+6.5.1.20171110_1941", null);
        doTestParseProduct("Talend Open Studio for Big Data_6.5.1.20171110_1941",
                "Talend Open Studio for Big Data_6.5.1.20171110_1941", null);
    }

    @Test
    public void test_parseProduct_valid() {
        doTestParseProduct("ABCD-XYZ", "ABCD", "XYZ");

        doTestParseProduct("ABCD-XYZ-123", "ABCD", "XYZ-123");

        doTestParseProduct("Talend Open Studio for Big Data-6.5.1.20171110_1941", "Talend Open Studio for Big Data",
                "6.5.1.20171110_1941");
        doTestParseProduct("Talend Open Studio for Big Data-6.5.1.20171110-1941", "Talend Open Studio for Big Data",
                "6.5.1.20171110-1941");

        doTestParseProduct("Talend Open Studio-3.2.3.r35442", "Talend Open Studio", "3.2.3.r35442");
        doTestParseProduct("Talend Open Studio for Data Integration-5.3.2.r113626", "Talend Open Studio for Data Integration",
                "5.3.2.r113626");
    }

    @Test
    public void test_parseProduct_patch() {
        doTestParseProduct("Talend Data Fabric-6.3.1.20171030_0901-patch", "Talend Data Fabric", "6.3.1.20171030_0901-patch");
    }

    @Test
    public void test_parseProduct_snapshot() {
        doTestParseProduct("Talend Open Studio for Big Data-6.5.1.20171110_1941-SNAPSHOT", "Talend Open Studio for Big Data",
                "6.5.1.20171110_1941-SNAPSHOT");
    }

    private void doTestParseProduct(String value, String productFullname, String productVersion) {
        Map<String, String> values = ItemProductValuesHelper.parseProduct(value);
        assertFalse(values.isEmpty());
        assertEquals(1, values.size());

        String actualProductFullname = values.keySet().iterator().next();
        String actualProductVersion = values.get(actualProductFullname);

        assertEquals(productFullname, actualProductFullname);

        if (productVersion != null) {
            assertEquals(productVersion, actualProductVersion);
        } else {
            assertNull(actualProductVersion);
        }
    }
}
