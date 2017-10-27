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
package org.talend.core.model.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ERepositoryObjectTypeTest {

    /**
     * 
     * Copy from TAC, if there are some updated items, need update this enum
     */
    public enum ItemType implements java.io.Serializable {

        // DI
        SERVICES("Services"),
        ROUTES("Routes"),
        RESOURCES("Resources"),
        HDFS("HDFS"),
        HCATALOG("HCatalog"),
        REPOSITORYMETADATAEDIFACT("repositorymetadataEDIFact"),
        CONTEXT("repository.context"),
        PROCESS("repository.process"),
        JOBSCRIPT("repository.jobscript"),
        ROUTINES("repository.routines"),
        DOCUMENTATION("repository.documentation"),
        BUSINESSPROCESS("repository.businessProcess"),
        METADATASQLPATTERNS("repository.metadataSQLPatterns"),
        METADATACONNECTIONS("repository.metadataConnections"),
        METADATAFILEDELIMITED("repository.metadataFileDelimited"),
        METADATAFILEEDCDIC("repository.metadataFileEDCDIC"),
        METADATAFILEEXCEL("repository.metadataFileExcel"),
        METADATAFILELDIF("repository.metadataFileLdif"),
        METADATAFILEPOSITIONAL("repository.metadataFilePositional"),
        METADATAFILEREGEXP("repository.metadataFileRegexp"),
        METADATAFILERULES("repository.metadataFileRules"),
        METADATAFILEXML("repository.metadataFileXml"),
        METADATAGENERICSCHEMA("repository.metadataGenericSchema"),
        METADATALDAPSCHEMA("repository.metadataLDAPSchema"),
        METADATAMDMCONNECTIONS("repository.metadataMDMConnections"),
        METADATASAPCONNECTIONS("repository.metadataSAPConnections"),
        METADATAWSDLSCHEMA("repository.metadataWSDLSchema"),
        METADATASALESFORCESCHEMA("repository.metadataSalesforceSchema"),
        METADATAVALIDATIONRULES("repository.metadataValidationRules"),
        METADATAFILEFTP("repository.metadataFileFTP"),
        METADATAFILEHL7("repository.metadataFileHL7"),

        PROCESS_MR("repository.mapReduceProcess"),
        PIG_UDF("repository.pigUdf"),
        JOB_DOC("repository.jobdoc"),
        METADATA_FILE_BRMS("repository.metadataFileBRMS"),
        METADATA_FILE_LINKRULES("repository.metadataLinkFileRules"),
        METADATA_HEADER_FOOTER("METADATA_HEADER_FOOTER"),
        OOZIE("OOZIE"),
        SURVIVORSHIP_FILE_ITEM("SURVIVORSHIP_FILE_ITEM"),

        // DQ
        TDQ_ANALYSIS_ELEMENT("repository.tdqelement.analysis"),
        TDQ_REPORT_ELEMENT("repository.tdqelement.report"),
        TDQ_INDICATOR_ELEMENT("repository.tdqelement.indicator"),
        TDQ_PATTERN_ELEMENT("repository.tdqelement.pattern"),
        TDQ_SOURCE_FILE_ELEMENT("repository.tdqelement.sourceFile"),
        TDQ_JRAXML_ELEMENT("repository.tdqelement.jrxml"),
        TDQ_EXCHANGE("repository.tdqExchange"),
        TDQ_RULES("repository.rules"),
        TDQ_RULESPARSER("repository.rulesparser"),
        TDQ_RULESSQL("repository.rulessql"),

        // MDM
        MDM_JOB("Job"),
        MDM_RECYCLEBIN("Recycle bin"),
        MDM_CUSTOMFORM("MDM.CustomForm"), // Custom Layout
        MDM_DATACLUSTER("MDM.DataCluster"), // Data Container
        MDM_DATAMODEL("MDM.DataModel"), // Data Model
        MDM_EVENTMANAGER("MDM.EventManager"), // Event Management
        MDM_MENU("MDM.Menu"), // Menu
        MDM_RESOURCE("MDM.Resource"), // Resource
        MDM_ROLE("MDM.Role"), // Role
        MDM_ROUTINGRULE("MDM.RoutingRule"), // Trigger
        MDM_SERVERDEF("MDM.ServerDef"),
        MDM_SERVICECONFIGURATION("MDM.ServiceConfiguration"),
        MDM_STOREDPROCEDURE("MDM.StoredProcedure"), // Stored Procedure
        MDM_SYNCHRONIZATIONPLAN("MDM.SynchronizationPlan"), // Synchronization Plan
        MDM_TRANSFORMERV2("MDM.TransformerV2"), // process
        MDM_UNIVERSE("MDM.Universe"), // Version
        MDM_VIEW("MDM.View"), // View
        MDM_WORKFLOW("MDM.Workflow"), // Workflow

        DATABASES("DATABASES"),
        JOBLET_DOC("repository.jobletdoc"),
        MAPS("MAPS"),
        NAMESPACES("NAMESPACES"),
        REPORTS("REPORTS"),
        SAMPLE_DATA("SAMPLE_DATA"),
        STRUCTURES("STRUCTURES"),

        BEANS("Bean"),
        HADOOPCLUSTER("repository.metadata.hadoopCluster"),
        JOBLET("repository.standardjoblet"),
        JSON("repository.metadataJSON"),

        // issue TMC-9433 created for the items bellow.
        datastewardship("datastewardship"),
        METADATA_EDIFACT("repository.metadataEDIFact"),
        METADATA_NOSQL_CONNECTIONS("repository.metadataNoSQLConnections"),
        PROCESS_STORM("repository.stormProcess"),
        ROUTE("Route"),
        ROUTE_DOC("ROUTE_DOC"),
        ROUTE_RESOURCES("Resource"),
        ROUTELET("Routelet"),
        salesforce("salesforce"),
        SPARK_JOBLET("repository.sparkjoblet"),
        SPARK_STREAMING_JOBLET("repository.sparkstreamingjoblet"),
        TEST_CONTAINER("repository.testContainer"), ;

        private String i18nKey;

        private ItemType(String i18nKey) {
            this.i18nKey = i18nKey;
        }

        /**
         * Getter for i18nKey.
         * 
         * @return the i18nKey
         */
        public String getI18nKey() {
            return i18nKey;
        }

        public static ItemType get(String status) {
            for (ItemType taskStatus : values()) {
                if (taskStatus.toString().equals(status)) {
                    return taskStatus;
                }
            }
            return null;
        }

        public static ItemType getByKey(String key) {
            for (ItemType type : values()) {
                if (type.getI18nKey().equals(key)) {
                    return type;
                }
            }
            return null;
        }
        // /**
        // * return the message value according to Messages.properties
        // *
        // * @return
        // */
        // public String getMessage() {
        // if (GWT.isClient()) {
        // return I18nUtils.getString(getI18nKey());
        // }
        // return null;
        // }

    }

    static abstract class ObjectTypeMatcher {

        private ERepositoryObjectType[] testTypes;

        private List<ERepositoryObjectType> unusedTypes;

        public ObjectTypeMatcher(ERepositoryObjectType[] testTypes) {
            super();
            this.testTypes = testTypes;
        }

        public void setUnusedTypes(List<ERepositoryObjectType> unusedTypes) {
            this.unusedTypes = unusedTypes;
        }

        String match() {
            // Please check with TAC Team for Class ItemType, after add/delete some types for DI.
            StringBuffer missTypes = new StringBuffer(100);
            Map<ERepositoryObjectType, ItemType> notMatchedTypes = new LinkedHashMap<ERepositoryObjectType, ItemType>(10);

            for (ERepositoryObjectType type : this.testTypes) {
                if (this.unusedTypes != null && this.unusedTypes.contains(type)) {
                    continue;
                }
                if (valid(type)) {
                    // FIXME, TUP-1190 don't check the name, only match the key.

                    String key = type.getKey();
                    ItemType itemType = ItemType.getByKey(key);
                    // if (itemType == null) {
                    // typeName = type.getType().replaceAll("_", "");
                    // itemType = ItemType.get(typeName);
                    // }
                    if (itemType == null) { // not found
                        missTypes.append(type.getType() + "(\"" + type.getKey() + "\"),");
                        missTypes.append('\n');
                    } else if (!itemType.name().equals(type.getType())) {
                        // FIXME, TUP-1190 don't check the name, only match the key.
                        // notMatchedTypes.put(type, itemType);
                    }
                    // System.out.println(type.getType() + "(\"" + type.getKey() + "\"),");
                }
            }
            StringBuffer sb = new StringBuffer(200);

            if (missTypes.length() > 0) {
                sb.append("The following should be added in class ItemType for TAC:\n");
                sb.append(missTypes);
                sb.append("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
            }
            if (!notMatchedTypes.isEmpty()) {
                sb.append("The following are not matched for TAC and Studio:\n");
                sb.append("Expected   <====>   Actual\n");
                for (ERepositoryObjectType type : notMatchedTypes.keySet()) {
                    sb.append(type.getType() + "(\"" + type.getKey() + "\")");
                    sb.append("   <====>   ");
                    ItemType itemType = notMatchedTypes.get(type);
                    sb.append(itemType.name() + "(\"" + itemType.getI18nKey() + "\")");
                    sb.append('\n');
                }
                sb.append("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
            }
            if (sb.length() > 0) {
                sb.insert(0, "\n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
            }
            return sb.toString();
        }

        boolean valid(ERepositoryObjectType type) {
            return type.isResouce() && Arrays.asList(type.getProducts()).contains(getCurrentProductName());
        }

        abstract String getCurrentProductName();

    }

    private ERepositoryObjectType[] allTypes;

    private List<ERepositoryObjectType> unusedTypes;

    @Before
    public void setUp() {
        allTypes = ERepositoryObjectType.values(ERepositoryObjectType.class);
        Arrays.sort(allTypes, new Comparator<ERepositoryObjectType>() {

            @Override
            public int compare(ERepositoryObjectType o1, ERepositoryObjectType o2) {
                return o1.getType().compareToIgnoreCase(o2.getType());
            }

        });

        //
        unusedTypes = new ArrayList<ERepositoryObjectType>();
        unusedTypes.add(ERepositoryObjectType.SVG_BUSINESS_PROCESS);
        unusedTypes.add(ERepositoryObjectType.SNIPPETS);

        // for example tests
        ERepositoryObjectType t = ERepositoryObjectType.getType("CODE_EXT");
        if (t != null) {
            unusedTypes.add(t);
        }
        t = ERepositoryObjectType.getType("JOB_DOC_EXT");
        if (t != null) {
            unusedTypes.add(t);
        }
        t = ERepositoryObjectType.getType("JOB_EXT");
        if (t != null) {
            unusedTypes.add(t);
        }
        t = ERepositoryObjectType.getType("JOB_EXT2");
        if (t != null) {
            unusedTypes.add(t);
        }
    }

    @After
    public void tearDown() throws Exception {
        allTypes = null;
        unusedTypes.clear();
        unusedTypes = null;
    }

    @Test
    public void testTacMatchedDITypes() {
        ObjectTypeMatcher matcher = new ObjectTypeMatcher(allTypes) {

            @Override
            boolean valid(ERepositoryObjectType type) {
                /*
                 * don't test the generic one, because it's dynamic
                 */
                return type.isResouce() && type.isDIItemType() && !type.isGenericType();
            }

            @Override
            String getCurrentProductName() {
                return ERepositoryObjectType.PROD_DI;
            }
        };
        matcher.setUnusedTypes(unusedTypes);

        String result = matcher.match();
        Assert.assertTrue(result.toString(), result.length() == 0);

    }

    @Test
    public void testTacMatchedDQTypes() {
        ObjectTypeMatcher matcher = new ObjectTypeMatcher(allTypes) {

            @Override
            boolean valid(ERepositoryObjectType type) {
                return type.isResouce() && type.isDQItemType();
            }

            @Override
            String getCurrentProductName() {
                return ERepositoryObjectType.PROD_DQ;
            }

        };
        matcher.setUnusedTypes(unusedTypes);

        String result = matcher.match();
        Assert.assertTrue(result.toString(), result.length() == 0);
    }

    @Test
    public void testTacMatchedMDMTypes() {
        ObjectTypeMatcher matcher = new ObjectTypeMatcher(allTypes) {

            @Override
            String getCurrentProductName() {
                return ERepositoryObjectType.PROD_MDM;
            }

        };
        matcher.setUnusedTypes(unusedTypes);

        String result = matcher.match();
        Assert.assertTrue(result.toString(), result.length() == 0);

    }

    @Test
    @Ignore
    public void testTacUnknownTypes() {
        StringBuffer unknownKeyTypes = new StringBuffer(200);
        for (ItemType itemType : ItemType.values()) {
            ERepositoryObjectType objType = null;
            for (ERepositoryObjectType t : this.allTypes) {
                if (itemType.getI18nKey().equals(t.getKey())) {
                    objType = t;
                    break;
                }
            }
            if (objType == null) { // don't find the same key
                unknownKeyTypes.append(itemType.name() + "(\"" + itemType.getI18nKey() + "\"),");
                unknownKeyTypes.append('\n');
            }
        }
        if (unknownKeyTypes.length() > 0) {
            unknownKeyTypes.insert(0, "There are no ERepositoryObjectType(s) to match with the following ItemType(s):\n");
            unknownKeyTypes.insert(0, "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
            unknownKeyTypes.append("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
        }
        Assert.assertTrue(unknownKeyTypes.toString(), unknownKeyTypes.length() == 0);
    }

}
