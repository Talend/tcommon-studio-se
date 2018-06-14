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
package org.talend.core.model.repository;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.EObject;
import org.talend.core.AbstractDQModelService;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.IESBService;
import org.talend.core.PluginChecker;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.BRMSConnectionItem;
import org.talend.core.model.properties.BusinessProcessItem;
import org.talend.core.model.properties.CSVFileConnectionItem;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.DelimitedFileConnectionItem;
import org.talend.core.model.properties.DocumentationItem;
import org.talend.core.model.properties.EDIFACTConnectionItem;
import org.talend.core.model.properties.EbcdicConnectionItem;
import org.talend.core.model.properties.ExcelFileConnectionItem;
import org.talend.core.model.properties.FTPConnectionItem;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.GenericSchemaConnectionItem;
import org.talend.core.model.properties.HL7ConnectionItem;
import org.talend.core.model.properties.HeaderFooterConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobDocumentationItem;
import org.talend.core.model.properties.JobScriptItem;
import org.talend.core.model.properties.JobletDocumentationItem;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.LDAPSchemaConnectionItem;
import org.talend.core.model.properties.LdifFileConnectionItem;
import org.talend.core.model.properties.LinkDocumentationItem;
import org.talend.core.model.properties.LinkRulesItem;
import org.talend.core.model.properties.MDMConnectionItem;
import org.talend.core.model.properties.PigudfItem;
import org.talend.core.model.properties.PositionalFileConnectionItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RegExFileConnectionItem;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.properties.RulesItem;
import org.talend.core.model.properties.SAPConnectionItem;
import org.talend.core.model.properties.SQLPatternItem;
import org.talend.core.model.properties.SVGBusinessProcessItem;
import org.talend.core.model.properties.SalesforceSchemaConnectionItem;
import org.talend.core.model.properties.SnippetItem;
import org.talend.core.model.properties.SnippetVariable;
import org.talend.core.model.properties.TDQItem;
import org.talend.core.model.properties.ValidationRulesConnectionItem;
import org.talend.core.model.properties.WSDLSchemaConnectionItem;
import org.talend.core.model.properties.XmlFileConnectionItem;
import org.talend.core.model.properties.util.PropertiesSwitch;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.i18n.Messages;
import org.talend.core.utils.ProductUtils;
import org.talend.designer.core.ICamelDesignerCoreService;
import org.talend.repository.ProjectManager;

/**
 * DOC hywang class global comment. Detailled comment
 */
public class ERepositoryObjectType extends DynaEnum<ERepositoryObjectType> {

    public static final String PROD_DI = ProductUtils.PROD_DI;

    public static final String PROD_DQ = ProductUtils.PROD_DQ;

    public static final String PROD_MDM = ProductUtils.PROD_MDM;

    public final static ERepositoryObjectType GENERAL_FILE = new ERepositoryObjectType("repository.generalfile", "",
            "GENERAL_FILE", true, 1, new String[] { PROD_DI, PROD_DQ, PROD_MDM }, new String[] {}, false);
    
    public final static ERepositoryObjectType PROJECT_SETTING = new ERepositoryObjectType("repository.talend.project.setting", "",
            "PROJECT_SETTING", true, 1, new String[] { PROD_DI, PROD_DQ, PROD_MDM }, new String[] {}, false);

    public final static ERepositoryObjectType GIT_ROOT = new ERepositoryObjectType("repository.gitroot", "", "GIT_ROOT", true, 1,
            new String[] { PROD_DI, PROD_DQ }, new String[] {}, false);

    public final static ERepositoryObjectType SVN_ROOT = new ERepositoryObjectType("repository.svnroot", "", "SVN_ROOT", true, 1,
            new String[] { PROD_DI, PROD_DQ }, new String[] {}, false);

    public final static ERepositoryObjectType SVG_BUSINESS_PROCESS = new ERepositoryObjectType("repository.svgBusinessProcess",
            "businessProcessSVG", "SVG_BUSINESS_PROCESS", true, 3, new String[] { PROD_DI }, new String[] {});

    public final static ERepositoryObjectType SNIPPETS = new ERepositoryObjectType("repository.snippets", "code/snippets",
            "SNIPPETS", true, 10, new String[] { PROD_DI }, new String[] {}, false);

    // public final static ERepositoryObjectType DOCUMENTATION = new ERepositoryObjectType("repository.documentation",
    // "documentations", "DOCUMENTATION", true, 11, new String[] { PROD_DI }, new String[] {});

    public final static ERepositoryObjectType METADATA = new ERepositoryObjectType("repository.metadata", "metadata", "METADATA",
            true, 80, new String[] { PROD_DI, PROD_DQ }, new String[] {}, false);

    public final static ERepositoryObjectType METADATA_CON_TABLE = new ERepositoryObjectType("repository.metadataTable",
            "METADATA_CON_TABLE", 13, true, true, new String[] { PROD_DI }, new String[] {}, false);

    public final static ERepositoryObjectType METADATA_CON_COLUMN = new ERepositoryObjectType("repository.metadataColumn",
            "METADATA_CON_COLUMN", 14, true, true, new String[] { PROD_DI }, new String[] {}, false);

    public final static ERepositoryObjectType METADATA_CON_VIEW = new ERepositoryObjectType("repository.metadataView",
            "METADATA_CON_VIEW", 15, true, true, new String[] { PROD_DI }, new String[] {}, false);

    public final static ERepositoryObjectType METADATA_CON_CATALOG = new ERepositoryObjectType("repository.metadataCatalog",
            "METADATA_CON_CATALOG", 16, true, true, new String[] { PROD_DI }, new String[] {}, false);

    public final static ERepositoryObjectType METADATA_CON_SCHEMA = new ERepositoryObjectType("repository.metadataSchema",
            "METADATA_CON_SCHEMA", 17, true, true, new String[] { PROD_DI }, new String[] {}, false);

    public final static ERepositoryObjectType METADATA_CON_SYNONYM = new ERepositoryObjectType("repository.synonym",
            "METADATA_CON_SYNONYM", 18, true, true, new String[] { PROD_DI }, new String[] {}, false);

    public final static ERepositoryObjectType METADATA_CON_QUERY = new ERepositoryObjectType("repository.query",
            "METADATA_CON_QUERY", 19, true, true, new String[] { PROD_DI }, new String[] {}, false);

    public final static ERepositoryObjectType METADATA_CON_CDC = new ERepositoryObjectType("repository.CDC", "METADATA_CON_CDC",
            20, true, true, new String[] { PROD_DI }, new String[] {}, false);

    public final static ERepositoryObjectType METADATA_SAP_FUNCTION = new ERepositoryObjectType("repository.SAPFunction", //$NON-NLS-1$
            "METADATA_SAP_FUNCTION", 21, true, true, new String[] { PROD_DI }, new String[] {}, false);

    public final static ERepositoryObjectType METADATA_SAP_IDOC = new ERepositoryObjectType("repository.SAPIDoc",
            "METADATA_SAP_IDOC", 22, true, true, new String[] { PROD_DI }, new String[] {}, false);

    public final static ERepositoryObjectType MDM_CONCEPT = new ERepositoryObjectType("repository.concept", "MDM_CONCEPT", 23, //$NON-NLS-1$
            true, true, new String[] { PROD_DI }, new String[] {}, false);

    public final static ERepositoryObjectType MDM_SCHEMA = new ERepositoryObjectType("repository.xmlSchema", "MDM_SCHEMA", 24, //$NON-NLS-1$
            true, true, new String[] { PROD_DI }, new String[] {}, false);

    public final static ERepositoryObjectType MDM_ELEMENT_TYPE = new ERepositoryObjectType("repository.xmlElementType", //$NON-NLS-1$
            "MDM_ELEMENT_TYPE", 25, true, true, new String[] { PROD_DI }, new String[] {}, false);

    public final static ERepositoryObjectType RECYCLE_BIN = new ERepositoryObjectType("repository.recyclebin", "", "RECYCLE_BIN",
            true, 900, new String[] { PROD_DI }, new String[] {}, false);

    public final static ERepositoryObjectType METADATA_COLUMN = new ERepositoryObjectType("repository.column", "",
            "METADATA_COLUMN", true, 27, new String[] { PROD_DI }, new String[] {}, false);

    // feature 0006484 add
    // public final static ERepositoryObjectType METADATA_FILE_RULES = new
    // ERepositoryObjectType("repository.metadataFileRules", //$NON-NLS-1$
    // "metadata/rules", "METADATA_FILE_RULES", 28, true, "repository.metadataFileRules.alias", new String[] { PROD_DI
    // }, //$NON-NLS-1$
    // new String[] {});

    public final static ERepositoryObjectType METADATA_FILE_LINKRULES = new ERepositoryObjectType(
            "repository.metadataLinkFileRules", "metadata/rules", "METADATA_FILE_LINKRULES", 29, true, //$NON-NLS-1$ //$NON-NLS-2$
            "repository.metadataLinkFileRules.alias", new String[] { PROD_DI }, new String[] {});

    // public final static ERepositoryObjectType METADATA_RULES_MANAGEMENT = new ERepositoryObjectType(
    // "repository.metadataRulesManagement", "", "METADATA_RULES_MANAGEMENT", 30, true, //$NON-NLS-1$ //$NON-NLS-2$
    // "repository.metadataRulesManagement.alias", new String[] { PROD_DI }, new String[] {}, false);

    // 0015169 added
    public final static ERepositoryObjectType METADATA_FILE_BRMS = new ERepositoryObjectType("repository.metadataFileBRMS", //$NON-NLS-1$
            "metadata/BRMSconnections", "METADATA_FILE_BRMS", 37, true, "repository.metadataFileBRMS.alias", //$NON-NLS-1$
            new String[] { PROD_DI }, new String[] {});

    public final static ERepositoryObjectType METADATA_VALIDATIONS_RULES_FOLDER = new ERepositoryObjectType(
            "repository.metadataValidationRulesFolder", "", "METADATA_VALIDATIONS_RULES_FOLDER", 49, true, //$NON-NLS-1$ //$NON-NLS-2$
            "repository.metadataValidationRulesFolder.alias", new String[] { PROD_DI }, new String[] {}, false);

    public final static ERepositoryObjectType FOLDER = new ERepositoryObjectType("repository.folder", "", "FOLDER", true, 50, //$NON-NLS-1$
            new String[] { PROD_DI }, new String[] {}, false);

    public final static ERepositoryObjectType REFERENCED_PROJECTS = new ERepositoryObjectType("repository.referencedProjects", "", //$NON-NLS-1$ //$NON-NLS-2$
            "REFERENCED_PROJECTS", 51, true, "repository.referencedProjects.alias", new String[] { PROD_DI, PROD_DQ },
            new String[] {}, false);

    public final static ERepositoryObjectType COMPONENTS = new ERepositoryObjectType("repository.components", "components", //$NON-NLS-1$
            "COMPONENTS", true, 61, new String[] { PROD_DI }, new String[] {}, false);

    // MOD mzhao feature 9207
    public final static ERepositoryObjectType TDQ_ELEMENT = new ERepositoryObjectType("repository.tdqelement", "", "TDQ_ELEMENT", //$NON-NLS-1$ //$NON-NLS-2$
            62, true, "repository.tdqelement.alias", new String[] { PROD_DQ }, new String[] {}, false);

    // MOD mzhao feature 13114, 2010-05-19
    public final static ERepositoryObjectType TDQ_ANALYSIS_ELEMENT = new ERepositoryObjectType("repository.tdqelement.analysis", //$NON-NLS-1$
            "TDQ_Data Profiling/Analyses", "TDQ_ANALYSIS_ELEMENT", 63, true, "repository.tdqelement.analysis.alias", //$NON-NLS-1$
            new String[] { PROD_DQ }, new String[] {});

    public final static ERepositoryObjectType TDQ_REPORT_ELEMENT = new ERepositoryObjectType("repository.tdqelement.report", //$NON-NLS-1$
            "TDQ_Data Profiling/Reports", "TDQ_REPORT_ELEMENT", 64, true, "repository.tdqelement.report.alias", //$NON-NLS-1$
            new String[] { PROD_DQ }, new String[] {});

    public final static ERepositoryObjectType TDQ_INDICATOR_ELEMENT = new ERepositoryObjectType("repository.tdqelement.indicator", //$NON-NLS-1$
            "TDQ_Libraries/Indicators", "TDQ_INDICATOR_ELEMENT", 66, true, "repository.tdqelement.indicator.alias", //$NON-NLS-1$
            new String[] { PROD_DQ }, new String[] {});

    public final static ERepositoryObjectType TDQ_PATTERN_ELEMENT = new ERepositoryObjectType("repository.tdqelement.pattern", //$NON-NLS-1$
            "TDQ_Libraries/Patterns", "TDQ_PATTERN_ELEMENT", 67, true, "repository.tdqelement.pattern.alias", //$NON-NLS-1$
            new String[] { PROD_DQ }, new String[] {});

    public final static ERepositoryObjectType TDQ_SOURCE_FILE_ELEMENT = new ERepositoryObjectType(
            "repository.tdqelement.sourceFile", "TDQ_Libraries/Source Files", "TDQ_SOURCE_FILE_ELEMENT", 68, true, //$NON-NLS-1$ //$NON-NLS-2$
            "repository.tdqelement.sourceFile.alias", new String[] { PROD_DQ }, new String[] {});

    // MOD zqin feature 14507
    public final static ERepositoryObjectType TDQ_JRAXML_ELEMENT = new ERepositoryObjectType("repository.tdqelement.jrxml", //$NON-NLS-1$
            "TDQ_Libraries/JRXML Template", "TDQ_JRAXML_ELEMENT", 69, true, "repository.tdqelement.jrxml.alias", //$NON-NLS-1$
            new String[] { PROD_DQ }, new String[] {});

    public final static ERepositoryObjectType TDQ_FOLDER_NODE = new ERepositoryObjectType("repository.tdqelement.folderNode", "", //$NON-NLS-1$ //$NON-NLS-2$
            "TDQ_FOLDER_NODE", 70, true, "repository.tdqelement.folderNode.alias", new String[] { PROD_DQ }, new String[] {},
            false);

    // MOD klliu feature 15750
    public final static ERepositoryObjectType TDQ_DATA_PROFILING = new ERepositoryObjectType("repository.dataprofiling", //$NON-NLS-1$
            "TDQ_Data Profiling", "TDQ_DATA_PROFILING", 71, true, "repository.dataprofiling.alias", new String[] { PROD_DQ }, //$NON-NLS-1$
            new String[] {}, false);

    public final static ERepositoryObjectType TDQ_LIBRARIES = new ERepositoryObjectType("repository.libraries", "TDQ_Libraries", //$NON-NLS-1$ //$NON-NLS-2$
            "TDQ_LIBRARIES", 74, true, "repository.libraries.alias", new String[] { PROD_DQ }, new String[] {}, false);

    public final static ERepositoryObjectType TDQ_PATTERN_REGEX = new ERepositoryObjectType("repository.patternRegex", //$NON-NLS-1$
            "TDQ_Libraries/Patterns/Regex", "TDQ_PATTERN_REGEX", 76, true, "repository.patternRegex.alias", //$NON-NLS-1$
            new String[] { PROD_DQ }, new String[] {}, false);

    public final static ERepositoryObjectType TDQ_PATTERN_SQL = new ERepositoryObjectType("repository.patternSql", //$NON-NLS-1$
            "TDQ_Libraries/Patterns/SQL", "TDQ_PATTERN_SQL", 77, true, "repository.patternSql.alias", new String[] { PROD_DQ }, //$NON-NLS-1$
            new String[] {}, false);

    public final static ERepositoryObjectType TDQ_RULES = new ERepositoryObjectType("repository.rules", "TDQ_Libraries/Rules", //$NON-NLS-1$ //$NON-NLS-2$
            "TDQ_RULES", 79, true, "repository.rules.alias", new String[] { PROD_DQ }, new String[] {});

    public final static ERepositoryObjectType TDQ_RULES_SQL = new ERepositoryObjectType("repository.rulesSql", //$NON-NLS-1$
            "TDQ_Libraries/Rules/SQL", "TDQ_RULES_SQL", 80, true, "repository.rulesSql.alias", new String[] { PROD_DQ }, //$NON-NLS-1$
            new String[] {}, false);

    // MOD klliu feature 23109

    public final static ERepositoryObjectType TDQ_RULES_PARSER = new ERepositoryObjectType("repository.rulesParser", //$NON-NLS-1$
            "TDQ_Libraries/Rules/Parser", "TDQ_RULES_PARSER", 81, true, "repository.rulesParser.alias", new String[] { PROD_DQ }, //$NON-NLS-1$
            new String[] {}, false);

    public final static ERepositoryObjectType TDQ_RULES_MATCHER = new ERepositoryObjectType("repository.rulesMatcher", //$NON-NLS-1$
            "TDQ_Libraries/Rules/Match", "TDQ_RULES_MATCHER", 82, true, "repository.rulesMatcher.alias", new String[] { PROD_DQ }, //$NON-NLS-1$
            new String[] {}, false);

    // ~
    // MOD klliu 2010-11-26 definition type

    public final static ERepositoryObjectType TDQ_SYSTEM_INDICATORS = new ERepositoryObjectType("repository.systemIndicators", //$NON-NLS-1$
            "TDQ_Libraries/Indicators/System Indicators", "TDQ_SYSTEM_INDICATORS", 83, true, "repository.systemIndicators.alias", //$NON-NLS-1$
            new String[] { PROD_DQ }, new String[] {}, false);

    public final static ERepositoryObjectType TDQ_USERDEFINE_INDICATORS = new ERepositoryObjectType(
            "repository.userDefineIndicators", "TDQ_Libraries/Indicators/User Defined Indicators", "TDQ_USERDEFINE_INDICATORS", //$NON-NLS-1$ //$NON-NLS-2$
            84, true, "repository.userDefineIndicators.alias", new String[] { PROD_DQ }, new String[] {}, false);

    public final static ERepositoryObjectType TDQ_USERDEFINE_INDICATORS_LIB = new ERepositoryObjectType(
            "repository.userDefineIndicators.lib", "", "TDQ_USERDEFINE_INDICATORS_LIB", 85, true, //$NON-NLS-1$ //$NON-NLS-2$
            "repository.userDefineIndicators.lib.alias", new String[] { PROD_DQ }, new String[] {}, false);

    public final static ERepositoryObjectType SYSTEM_INDICATORS_ADVANCED_STATISTICS = new ERepositoryObjectType(
            "repository.systemIndicators.advancedStatistics", "TDQ_Libraries/Indicators/System Indicators/Advanced Statistics", //$NON-NLS-1$ //$NON-NLS-2$
            "SYSTEM_INDICATORS_ADVANCED_STATISTICS", 86, true, "repository.systemIndicators.advancedStatistics.alias",
            new String[] { PROD_DQ }, new String[] {}, false);

    public final static ERepositoryObjectType SYSTEM_INDICATORS_BUSINESS_RULES = new ERepositoryObjectType(
            "repository.systemIndicators.businessRules", "TDQ_Libraries/Indicators/System Indicators/Business Rules", //$NON-NLS-1$ //$NON-NLS-2$
            "SYSTEM_INDICATORS_BUSINESS_RULES", 87, true, "repository.systemIndicators.businessRules.alias",
            new String[] { PROD_DQ }, new String[] {}, false);

    public final static ERepositoryObjectType SYSTEM_INDICATORS_CORRELATION = new ERepositoryObjectType(
            "repository.systemIndicators.correlation", "TDQ_Libraries/Indicators/System Indicators/Correlation", //$NON-NLS-1$ //$NON-NLS-2$
            "SYSTEM_INDICATORS_CORRELATION", 88, true, "repository.systemIndicators.correlation.alias", new String[] { PROD_DQ },
            new String[] {}, false);

    public final static ERepositoryObjectType SYSTEM_INDICATORS_FUNCTIONAL_DEPENDENCY = new ERepositoryObjectType(
            "repository.systemIndicators.functionalDependency", //$NON-NLS-1$
            "TDQ_Libraries/Indicators/System Indicators/Functional Dependency", "SYSTEM_INDICATORS_FUNCTIONAL_DEPENDENCY", 89, //$NON-NLS-1$
            true, "repository.systemIndicators.functionalDependency.alias", new String[] { PROD_DQ }, new String[] {}, false);

    public final static ERepositoryObjectType SYSTEM_INDICATORS_OVERVIEW = new ERepositoryObjectType(
            "repository.systemIndicators.overview", "TDQ_Libraries/Indicators/System Indicators/Overview", //$NON-NLS-1$ //$NON-NLS-2$
            "SYSTEM_INDICATORS_OVERVIEW", 90, true, "repository.systemIndicators.overview.alias", new String[] { PROD_DQ }, //$NON-NLS-1$ //$NON-NLS-2$
            new String[] {}, false);

    public final static ERepositoryObjectType SYSTEM_INDICATORS_PATTERN_FREQUENCY_STATISTICS = new ERepositoryObjectType(
            "repository.systemIndicators.patternFinder", //$NON-NLS-1$
            "TDQ_Libraries/Indicators/System Indicators/Pattern Frequency Statistics", "SYSTEM_INDICATORS_PATTERN_FINDER", 91, //$NON-NLS-1$
            true, "repository.systemIndicators.patternFinder.alias", new String[] { PROD_DQ }, new String[] {}, false);

    public final static ERepositoryObjectType SYSTEM_INDICATORS_PATTERN_MATCHING = new ERepositoryObjectType(
            "repository.systemIndicators.patternMatching", "TDQ_Libraries/Indicators/System Indicators/Pattern Matching", //$NON-NLS-1$ //$NON-NLS-2$
            "SYSTEM_INDICATORS_PATTERN_MATCHING", 92, true, "repository.systemIndicators.patternMatching.alias",
            new String[] { PROD_DQ }, new String[] {}, false);

    public final static ERepositoryObjectType SYSTEM_INDICATORS_ROW_COMPARISON = new ERepositoryObjectType(
            "repository.systemIndicators.rowComparison", "TDQ_Libraries/Indicators/System Indicators/Row Comparison", //$NON-NLS-1$ //$NON-NLS-2$
            "SYSTEM_INDICATORS_ROW_COMPARISON", 93, true, "repository.systemIndicators.rowComparison.alias",
            new String[] { PROD_DQ }, new String[] {}, false);

    public final static ERepositoryObjectType SYSTEM_INDICATORS_SIMPLE_STATISTICS = new ERepositoryObjectType(
            "repository.systemIndicators.simpleStatistics", "TDQ_Libraries/Indicators/System Indicators/Simple Statistics", //$NON-NLS-1$ //$NON-NLS-2$
            "SYSTEM_INDICATORS_SIMPLE_STATISTICS", 94, true, "repository.systemIndicators.simpleStatistics.alias",
            new String[] { PROD_DQ }, new String[] {}, false);

    public final static ERepositoryObjectType SYSTEM_INDICATORS_SOUNDEX = new ERepositoryObjectType(
            "repository.systemIndicators.soundex", "TDQ_Libraries/Indicators/System Indicators/Soundex", //$NON-NLS-1$ //$NON-NLS-2$
            "SYSTEM_INDICATORS_SOUNDEX", 95, true, "repository.systemIndicators.soundex.alias", new String[] { PROD_DQ },
            new String[] {}, false);

    public final static ERepositoryObjectType SYSTEM_INDICATORS_SUMMARY_STATISTICS = new ERepositoryObjectType(
            "repository.systemIndicators.summaryStatistics", "TDQ_Libraries/Indicators/System Indicators/Summary Statistics", //$NON-NLS-1$ //$NON-NLS-2$
            "SYSTEM_INDICATORS_SUMMARY_STATISTICS", 96, true, "repository.systemIndicators.summaryStatistics.alias",
            new String[] { PROD_DQ }, new String[] {}, false);

    public final static ERepositoryObjectType SYSTEM_INDICATORS_TEXT_STATISTICS = new ERepositoryObjectType(
            "repository.systemIndicators.textStatistics", "TDQ_Libraries/Indicators/System Indicators/Text Statistics", //$NON-NLS-1$ //$NON-NLS-2$
            "SYSTEM_INDICATORS_TEXT_STATISTICS", 97, true, "repository.systemIndicators.textStatistics.alias",
            new String[] { PROD_DQ }, new String[] {}, false);

    public final static ERepositoryObjectType TDQ_EXCHANGE = new ERepositoryObjectType("repository.tdqExchange", //$NON-NLS-1$
            "TDQ_Libraries/Exchange", "TDQ_EXCHANGE", 98, true, "repository.tdqExchange.alias", new String[] { PROD_DQ }, //$NON-NLS-1$
            new String[] {});

    public final static ERepositoryObjectType METADATA_SALESFORCE_MODULE = new ERepositoryObjectType(
            "repository.metadataSalesforceModule", "METADATA_SALESFORCE_MODULE", 99, true, true, new String[] { PROD_DI }, //$NON-NLS-1$ //$NON-NLS-2$
            new String[] {}, false);

    public final static ERepositoryObjectType SYSTEM_INDICATORS_PHONENUMBER_STATISTICS = new ERepositoryObjectType(
            "repository.systemIndicators.phoneNumberStatistics", //$NON-NLS-1$
            "TDQ_Libraries/Indicators/System Indicators/Phone Number Statistics", "SYSTEM_INDICATORS_PHONENUMBER_STATISTICS", 100, //$NON-NLS-1$
            true, "repository.systemIndicators.phoneNumberStatistics.alias", new String[] { PROD_DQ }, new String[] {}, false);

    public final static ERepositoryObjectType SERVICESOPERATION = new ERepositoryObjectType("repository.servicesOperation", //$NON-NLS-1$
            "SERVICESOPERATION", 101, true, true, new String[] { PROD_DI }, new String[] {}, false); //$NON-NLS-1$

    public final static ERepositoryObjectType SERVICESPORT = new ERepositoryObjectType("repository.servicesPort", "SERVICESPORT", //$NON-NLS-1$ //$NON-NLS-2$
            102, true, true, new String[] { PROD_DI }, new String[] {}, false);

    public final static ERepositoryObjectType SYSTEM_INDICATORS_FRAUDDETECTION = new ERepositoryObjectType(
            "repository.systemIndicators.fraudDetection", "TDQ_Libraries/Indicators/System Indicators/Fraud Detection", //$NON-NLS-1$ //$NON-NLS-2$
            "SYSTEM_INDICATORS_FRAUDDETECTION", 103, true, "repository.systemIndicators.fraudDetectionStatistics.alias",
            new String[] { PROD_DQ }, new String[] {}, false);

    public final static ERepositoryObjectType METADATA_SAP_TABLE = new ERepositoryObjectType("repository.SAPTable", //$NON-NLS-1$
            "METADATA_SAP_TABLE", 104, true, true, new String[] { PROD_DI }, new String[] {}, false);

    public final static ERepositoryObjectType METADATA_SAP_CONTENT_EXTRACTOR = new ERepositoryObjectType("repository.SAPTable", //$NON-NLS-1$
            "METADATA_SAP_CONTENT_EXTRACTOR", 105, true, true, new String[] { PROD_DI }, new String[] {}, false);

    public final static ERepositoryObjectType METADATA_CON_CALCULATION_VIEW = new ERepositoryObjectType(
            "repository.metadataCalculationView", "METADATA_CON_CALCULATION_VIEW", 106, true, true, new String[] { PROD_DI },
            new String[] {}, false);

    private String label;

    private String alias;

    private String folder = "";

    private String[] products;

    private boolean subItem;

    private boolean isResouce = true;

    private String[] userRight;

    private String namePattern = null;

    /*
     * this used to indicate the multiple same name items are allowed to created or not, In generic, those resources or
     * items should be in different folder.
     */
    private boolean isAllowMultiName = false;

    /*
     * This attribute is used to indicate folder name using ResourcesPlugin.getWorkspace().validateName(label,
     * IResource.FOLDER) to validate but not talend default folder pattern when the value is true.
     */
    private boolean isAllowPlainFolder = false;

    /*
     * This attribute is used to mark the type which come from generic way.
     */
    private boolean isFromGeneric = false;

    private List<ERepositoryObjectType> parentTypes = new ArrayList<ERepositoryObjectType>();

    private List<ERepositoryObjectType> childrenTypes = new ArrayList<ERepositoryObjectType>();

    static {
        // init
        RepositoryNodeProviderRegistryReader.getInstance().init();
    }

    public final static ERepositoryObjectType BUSINESS_PROCESS = ERepositoryObjectType.valueOf("BUSINESS_PROCESS"); //$NON-NLS-1$

    public final static ERepositoryObjectType PROCESS = ERepositoryObjectType.valueOf("PROCESS"); //$NON-NLS-1$

    /**
     * <font color="red">This value may be <b>null</b> in TOS, <b>should add NPE check</b></font>
     */
    public final static ERepositoryObjectType JOBLET = ERepositoryObjectType.valueOf("JOBLET"); //$NON-NLS-1$

    public final static ERepositoryObjectType JOBLET_DESIGNS = ERepositoryObjectType.valueOf("JOBLET_DESIGNS"); //$NON-NLS-1$

    public final static ERepositoryObjectType SPARK_JOBLET = ERepositoryObjectType.valueOf("SPARK_JOBLET"); //$NON-NLS-1$

    public final static ERepositoryObjectType SPARK_STREAMING_JOBLET = ERepositoryObjectType.valueOf("SPARK_STREAMING_JOBLET"); //$NON-NLS-1$

    public final static ERepositoryObjectType TEST_CONTAINER = ERepositoryObjectType.valueOf("TEST_CONTAINER"); //$NON-NLS-1$

    public final static ERepositoryObjectType CODE = ERepositoryObjectType.valueOf("CODE"); //$NON-NLS-1$

    public final static ERepositoryObjectType ROUTINES = ERepositoryObjectType.valueOf("ROUTINES"); //$NON-NLS-1$

    public final static ERepositoryObjectType METADATA_HEADER_FOOTER = ERepositoryObjectType.valueOf("METADATA_HEADER_FOOTER"); //$NON-NLS-1$

    public final static ERepositoryObjectType JOB_SCRIPT = ERepositoryObjectType.valueOf("JOB_SCRIPT"); //$NON-NLS-1$

    public final static ERepositoryObjectType PIG_UDF = ERepositoryObjectType.valueOf("PIG_UDF"); //$NON-NLS-1$

    public final static ERepositoryObjectType CONTEXT = ERepositoryObjectType.valueOf("CONTEXT"); //$NON-NLS-1$

    public final static ERepositoryObjectType SQLPATTERNS = ERepositoryObjectType.valueOf("SQLPATTERNS"); //$NON-NLS-1$

    public final static ERepositoryObjectType ROUTE_RESOURCES = ERepositoryObjectType.valueOf("ROUTE_RESOURCES"); //$NON-NLS-1$

    /**
     * Docs
     * 
     * <font color="red">This value may be <b>null</b> in some licenses, <b>should add NPE check</b></font>
     */
    public final static ERepositoryObjectType DOCUMENTATION = ERepositoryObjectType.valueOf("DOCUMENTATION"); //$NON-NLS-1$

    public final static ERepositoryObjectType GENERATED = ERepositoryObjectType.valueOf("GENERATED");//$NON-NLS-1$

    public final static ERepositoryObjectType JOB_DOC = ERepositoryObjectType.valueOf("JOB_DOC");//$NON-NLS-1$

    public final static ERepositoryObjectType JOBLET_DOC = ERepositoryObjectType.valueOf("JOBLET_DOC");//$NON-NLS-1$

    /**
     * Metadatas
     */
    public final static ERepositoryObjectType METADATA_FILE_POSITIONAL = ERepositoryObjectType
            .valueOf("METADATA_FILE_POSITIONAL"); //$NON-NLS-1$

    public final static ERepositoryObjectType METADATA_FILE_REGEXP = ERepositoryObjectType.valueOf("METADATA_FILE_REGEXP"); //$NON-NLS-1$

    public final static ERepositoryObjectType METADATA_FILE_XML = ERepositoryObjectType.valueOf("METADATA_FILE_XML"); //$NON-NLS-1$

    public final static ERepositoryObjectType METADATA_FILE_LDIF = ERepositoryObjectType.valueOf("METADATA_FILE_LDIF"); //$NON-NLS-1$

    public final static ERepositoryObjectType METADATA_FILE_EXCEL = ERepositoryObjectType.valueOf("METADATA_FILE_EXCEL"); //$NON-NLS-1$

    public final static ERepositoryObjectType METADATA_SALESFORCE_SCHEMA = ERepositoryObjectType
            .valueOf("METADATA_SALESFORCE_SCHEMA"); //$NON-NLS-1$

    public final static ERepositoryObjectType METADATA_LDAP_SCHEMA = ERepositoryObjectType.valueOf("METADATA_LDAP_SCHEMA"); //$NON-NLS-1$

    public final static ERepositoryObjectType METADATA_WSDL_SCHEMA = ERepositoryObjectType.valueOf("METADATA_WSDL_SCHEMA"); //$NON-NLS-1$

    public final static ERepositoryObjectType METADATA_GENERIC_SCHEMA = ERepositoryObjectType.valueOf("METADATA_GENERIC_SCHEMA"); //$NON-NLS-1$

    public final static ERepositoryObjectType METADATA_MDMCONNECTION = ERepositoryObjectType.valueOf("METADATA_MDMCONNECTION"); //$NON-NLS-1$

    public final static ERepositoryObjectType METADATA_CONNECTIONS = ERepositoryObjectType.valueOf("METADATA_CONNECTIONS");

    public final static ERepositoryObjectType METADATA_FILE_DELIMITED = ERepositoryObjectType.valueOf("METADATA_FILE_DELIMITED");

    public final static ERepositoryObjectType METADATA_FILE_FTP = ERepositoryObjectType.valueOf("METADATA_FILE_FTP");

    public final static ERepositoryObjectType METADATA_SAPCONNECTIONS = ERepositoryObjectType.valueOf("METADATA_SAPCONNECTIONS");

    public final static ERepositoryObjectType METADATA_FILE_EBCDIC = ERepositoryObjectType.valueOf("METADATA_FILE_EBCDIC");

    public final static ERepositoryObjectType METADATA_VALIDATION_RULES = ERepositoryObjectType
            .valueOf("METADATA_VALIDATION_RULES");

    public final static ERepositoryObjectType METADATA_FILE_HL7 = ERepositoryObjectType.valueOf("METADATA_FILE_HL7");

    public final static ERepositoryObjectType METADATA_EDIFACT = ERepositoryObjectType.valueOf("METADATA_EDIFACT");

    /**
     * Rules
     */
    public final static ERepositoryObjectType METADATA_RULES_MANAGEMENT = ERepositoryObjectType
            .valueOf("METADATA_RULES_MANAGEMENT"); //$NON-NLS-1$

    public final static ERepositoryObjectType METADATA_FILE_RULES = ERepositoryObjectType.valueOf("METADATA_FILE_RULES"); //$NON-NLS-1$

    /**
     * <font color="red">This value may be <b>null</b> in some licenses, <b>should add NPE check</b></font>
     */
    public final static ERepositoryObjectType PROCESS_MR = ERepositoryObjectType.valueOf("PROCESS_MR"); //$NON-NLS-1$

    /**
     * <font color="red">This value may be <b>null</b> in some licenses, <b>should add NPE check</b></font>
     */
    public final static ERepositoryObjectType PROCESS_STORM = ERepositoryObjectType.valueOf("PROCESS_STORM");

    /**
     * <font color="red">This value may be <b>null</b> in some licenses, <b>should add NPE check</b></font>
     */
    public final static ERepositoryObjectType PROCESS_ROUTE = ERepositoryObjectType.valueOf("ROUTE"); //$NON-NLS-1$

    /**
     * <font color="red">This value may be <b>null</b> in some licenses, <b>should add NPE check</b></font>
     */
    public final static ERepositoryObjectType PROCESS_ROUTE_MICROSERVICE = ERepositoryObjectType.valueOf("ROUTE_MICROSERVICE"); //$NON-NLS-1$
    
    /**
     * <font color="red">This value may be <b>null</b> in some licenses, <b>should add NPE check</b></font>
     */
    public final static ERepositoryObjectType PROCESS_ROUTELET = ERepositoryObjectType.valueOf("ROUTELET"); //$NON-NLS-1$

    /**
     * <b>Attention!!!</b><br/>
     * 1. This enum is <b>always null</b> since it is never configure the extention point
     * "org.talend.core.repository.repository_node_provider" in the plugin.xml used in
     * RepositoryNodeProviderRegistryReader.readElement(...) <br/>
     * 2. both frameworks (mapreduce / spark) should use PROCESS_MAPREDUCE in current code I think <br/>
     */
    public final static ERepositoryObjectType PROCESS_SPARK = ERepositoryObjectType.valueOf("PROCESS_SPARK");

    /**
     * <b>Attention!!!</b><br/>
     * 1. This enum is <b>always null</b> since it is never configure the extention point
     * "org.talend.core.repository.repository_node_provider" in the plugin.xml used in
     * RepositoryNodeProviderRegistryReader.readElement(...) <br/>
     * 2. both frameworks (storm / sparkstreaming) should use PROCESS_STORM in current code I think <br/>
     */
    public final static ERepositoryObjectType PROCESS_SPARKSTREAMING = ERepositoryObjectType.valueOf("PROCESS_SPARKSTREAMING");

    public final static ERepositoryObjectType METADATA_SAP_BW_ADVANCEDDATASTOREOBJECT = ERepositoryObjectType
            .valueOf("METADATA_SAP_BW_ADVANCEDDATASTOREOBJECT"); //$NON-NLS-1$

    public final static ERepositoryObjectType METADATA_SAP_BW_DATASOURCE = ERepositoryObjectType
            .valueOf("METADATA_SAP_BW_DATASOURCE"); //$NON-NLS-1$

    public final static ERepositoryObjectType METADATA_SAP_BW_DATASTOREOBJECT = ERepositoryObjectType
            .valueOf("METADATA_SAP_BW_DATASTOREOBJECT"); //$NON-NLS-1$

    public final static ERepositoryObjectType METADATA_SAP_BW_INFOCUBE = ERepositoryObjectType
            .valueOf("METADATA_SAP_BW_INFOCUBE"); //$NON-NLS-1$

    public final static ERepositoryObjectType METADATA_SAP_BW_INFOOBJECT = ERepositoryObjectType
            .valueOf("METADATA_SAP_BW_INFOOBJECT"); //$NON-NLS-1$

    private static Map<String, ERepositoryObjectType> typeCacheById = new HashMap<String, ERepositoryObjectType>();

    ERepositoryObjectType(String key, String folder, String type, boolean isStaticNode, int ordinal, String[] products,
            String[] userRight, boolean... isResouce) {
        super(key, type, isStaticNode, ordinal);
        this.folder = folder;
        this.products = products;
        this.userRight = userRight;
        if (isResouce != null && isResouce.length == 1) {
            this.isResouce = isResouce[0];
        }
    }

    ERepositoryObjectType(String key, String folder, String type, boolean isStaticNode, int ordinal, String[] products,
            boolean isAllowMultiName, String[] userRight, boolean... isResouce) {
        this(key, folder, type, isStaticNode, ordinal, products, userRight, isResouce);
        this.isAllowMultiName = isAllowMultiName;
    }

    ERepositoryObjectType(String key, String folder, String type, boolean isStaticNode, int ordinal, String[] products,
            String[] userRight, boolean isAllowMultiName, String namePattern, boolean... isResouce) {
        this(key, folder, type, isStaticNode, ordinal, products, isAllowMultiName, userRight, isResouce);
        this.namePattern = namePattern;
    }

    ERepositoryObjectType(String key, String type, int ordinal, boolean isStaticNode, boolean subItem, String[] products,
            String[] userRight, boolean... isResouce) {
        super(key, type, isStaticNode, ordinal);
        this.subItem = subItem;
        this.products = products;
        this.userRight = userRight;
        if (isResouce != null && isResouce.length == 1) {
            this.isResouce = isResouce[0];
        }
    }

    ERepositoryObjectType(String key, String type, int ordinal, boolean isStaticNode, boolean subItem, String[] products,
            boolean isAllowMultiName, String[] userRight, boolean... isResouce) {
        this(key, type, ordinal, isStaticNode, subItem, products, userRight, isResouce);
        this.isAllowMultiName = isAllowMultiName;
    }

    ERepositoryObjectType(String key, String type, int ordinal, boolean isStaticNode, boolean subItem, String[] products,
            String[] userRight, boolean isAllowMultiName, String namePattern, boolean... isResouce) {
        this(key, type, ordinal, isStaticNode, subItem, products, isAllowMultiName, userRight, isResouce);
        this.namePattern = namePattern;
    }

    ERepositoryObjectType(String key, String folder, String type, int ordinal, boolean isStaticNode, String alias,
            String[] products, String[] userRight, boolean... isResouce) {
        this(key, folder, type, isStaticNode, ordinal, products, userRight, isResouce);
        this.alias = alias;
    }

    ERepositoryObjectType(String key, String folder, String type, int ordinal, boolean isStaticNode, String alias,
            String[] products, boolean isAllowMultiName, String[] userRight, boolean... isResouce) {
        this(key, folder, type, ordinal, isStaticNode, alias, products, userRight, isResouce);
        this.isAllowMultiName = isAllowMultiName;
    }

    ERepositoryObjectType(String key, String folder, String type, int ordinal, boolean isStaticNode, String alias,
            String[] products, String[] userRight, boolean isAllowMultiName, String namePattern, boolean... isResouce) {
        this(key, folder, type, ordinal, isStaticNode, alias, products, isAllowMultiName, userRight, isResouce);
        this.namePattern = namePattern;
    }

    ERepositoryObjectType(String key, String label, String folder, String type, int ordinal, boolean isStaticNode, String alias,
            String[] products, String[] userRight, boolean... isResouce) {
        this(key, folder, type, ordinal, isStaticNode, alias, products, userRight, isResouce);
        this.label = label;
    }

    ERepositoryObjectType(String key, String label, String folder, String type, int ordinal, boolean isStaticNode, String alias,
            String[] products, boolean isAllowMultiName, String[] userRight, boolean... isResouce) {
        this(key, label, folder, type, ordinal, isStaticNode, alias, products, userRight, isResouce);
        this.isAllowMultiName = isAllowMultiName;
    }

    ERepositoryObjectType(String key, String label, String folder, String type, int ordinal, boolean isStaticNode, String alias,
            String[] products, String[] userRight, boolean isAllowMultiName, String namePattern, boolean... isResouce) {
        this(key, folder, type, ordinal, isStaticNode, alias, products, isAllowMultiName, userRight, isResouce);
        this.namePattern = namePattern;
        this.label = label;
    }

    /**
     * 
     * DOC ggu ERepositoryObjectType constructor comment.
     * 
     * Used for GenericWizardInternalService to create Generic Types
     */
    ERepositoryObjectType(String type, String label, String alias, String folder, int ordinal, String[] products) {
        this(type, label, folder, type, ordinal, false, alias, products, false, new String[0], true);
        isFromGeneric = true;
    }

    public static <E> DynaEnum<? extends DynaEnum<?>>[] values() {
        Project currentProject = ProjectManager.getInstance().getCurrentProject();
        String projectType = null;
        if (currentProject != null) {
            projectType = currentProject.getEmfProject().getType();
        }
        if (PROD_DI.equals(projectType)) {
            // limit with DI items only.
            List<ERepositoryObjectType> toReturn = new ArrayList<ERepositoryObjectType>();
            for (ERepositoryObjectType currentType : values(ERepositoryObjectType.class)) {
                if (ArrayUtils.contains(currentType.getProducts(), PROD_DI)) {
                    toReturn.add(currentType);
                }
            }
            return toReturn.toArray(new ERepositoryObjectType[] {});

        }
        // Bad code here to fix bug TDI-23178. If DQ repository type implemented by extention point like what MDM does
        // will avoid this bug.
        if (!PluginChecker.isPluginLoaded("org.talend.dataprofiler.core")) { //$NON-NLS-1$
            List<ERepositoryObjectType> toReturn = new ArrayList<ERepositoryObjectType>();
            for (ERepositoryObjectType currentType : values(ERepositoryObjectType.class)) {
                if (!(currentType.getProducts().length == 1 && ArrayUtils.contains(currentType.getProducts(), PROD_DQ))) {
                    toReturn.add(currentType);
                }
            }
            return toReturn.toArray(new ERepositoryObjectType[0]);
        }

        return values(ERepositoryObjectType.class);
    }

    @SuppressWarnings("unchecked")
    private static <E> Constructor<E> getConstructor(Class<E> clazz, Class<?>[] argTypes) {
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            try {
                return (Constructor<E>) c.getDeclaredConstructor(argTypes);
            } catch (Exception e) {
                continue;
            }
        }
        return null;
    }

    public boolean isGenericType() {
        return isFromGeneric;
    }

    public String getLabel() {
        if (this.label == null) {
            if (isStaticNode()) {
                return super.toString();
            } else {
                return getType();
            }
        }
        return this.label;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.DynaEnum#toString()
     */
    @Override
    public String toString() {
        return getLabel();
    }

    public String getAlias() {
        if (alias == null) {
            return null;
        }
        if (isStaticNode()) {
            return Messages.getString(alias);
        }
        return alias;
    }

    public static ERepositoryObjectType getTypeFromKey(String key) {
        for (ERepositoryObjectType type : (ERepositoryObjectType[]) values()) {
            if (type.getKey().equals(key)) {
                return type;
            }
        }
        return null;
    }

    public static ERepositoryObjectType getType(String typeName) {
        for (ERepositoryObjectType type : (ERepositoryObjectType[]) values()) {
            if (type.getType().equals(typeName)) {
                return type;
            }
        }
        return null;
    }

    public static ERepositoryObjectType getType(Property property) {
        String itemId = property.getId();
        ERepositoryObjectType type = typeCacheById.get(itemId);
        if (type == null) {
            Item item = property.getItem();
            type = getItemType(item);
            if (type != null) {
                typeCacheById.put(itemId, type);
            }
        }
        return type;
    }

    /**
     * Getter for typeCacheById.
     * 
     * @return the typeCacheById
     */
    public static Map<String, ERepositoryObjectType> getTypeCacheById() {
        return typeCacheById;
    }

    public boolean hasFolder() {
        try {
            String folderName = getFolderName(this);
            if (folderName != null && !"".equals(folderName)) { //$NON-NLS-1$
                return true;
            }
        } catch (IllegalArgumentException e) { // not found
            // nothing to do
        }
        return false;
    }

    public static String getFolderName(ERepositoryObjectType type) {

        if (type == GENERATED || type == JOB_DOC) {
            if ((PluginChecker.isDocumentationPluginLoaded())) {
                return type.getFolder();
            }
        }
        if (type == JOBLET_DOC) {
            if (PluginChecker.isJobLetPluginLoaded()) {
                return type.getFolder();
            }
        } else {

            return type.getFolder();
        }
        throw new IllegalArgumentException(Messages.getString("ERepositoryObjectType.FolderNotFound", type)); //$NON-NLS-1$
    }

    public static String getDeleteFolderName(ERepositoryObjectType type) {
        if (type == BUSINESS_PROCESS) {
            return "businessProcess"; //$NON-NLS-1$
        } else if (type == SVG_BUSINESS_PROCESS) {
            return "businessProcessSVG"; //$NON-NLS-1$
        } else if (type == PROCESS) {
            return "job"; //$NON-NLS-1$
        } else if (type == JOBLET) {
            return "joblet";
        } else if (type == CONTEXT) {
            return "context"; //$NON-NLS-1$
        } else if (type == ROUTINES) {
            return "routine"; //$NON-NLS-1$
        } else if (type == JOB_SCRIPT) {
            return "jobscript"; //$NON-NLS-1$
        } else if (type == SNIPPETS) {
            return "snippet"; //$NON-NLS-1$
        } else if (type == DOCUMENTATION) {
            return "documentation"; //$NON-NLS-1$
        } else if (type == SQLPATTERNS) {
            return "sqlPattern"; //$NON-NLS-1$
        } else if (type == METADATA) {
            return "metadata"; //$NON-NLS-1$
        } else if (type == METADATA_CONNECTIONS) {
            return "DB connection"; //$NON-NLS-1$
        } else if (type == METADATA_SAPCONNECTIONS) {
            return "SAPconnection"; //$NON-NLS-1$
        } else if (type == METADATA_FILE_EBCDIC) {
            return "fileEBCDIC"; //$NON-NLS-1$
        } else if (type == METADATA_FILE_HL7) {
            return "fileHL7"; //$NON-NLS-1$
        } else if (type == METADATA_FILE_FTP) {
            return "FTPconnection"; //$NON-NLS-1$
        } else if (type == METADATA_FILE_BRMS) {
            return "BRMSconnection"; //$NON-NLS-1$
        } else if (type == METADATA_MDMCONNECTION) {
            return "MDMconnection"; //$NON-NLS-1$
        } else if (type == METADATA_FILE_DELIMITED) {
            return "fileDelimited"; //$NON-NLS-1$
        } else if (type == METADATA_FILE_POSITIONAL) {
            return "filePositional"; //$NON-NLS-1$
        } else if (type == METADATA_FILE_REGEXP) {
            return "fileRegex"; //$NON-NLS-1$
        } else if (type == METADATA_FILE_XML) {
            return "fileXml"; //$NON-NLS-1$
        } else if (type == METADATA_FILE_EXCEL) {
            return "fileExcel"; //$NON-NLS-1$
        } else if (type == METADATA_FILE_LDIF) {
            return "fileLdif"; //$NON-NLS-1$
        } else if (type == METADATA_LDAP_SCHEMA) {
            return "LDAPSchema"; //$NON-NLS-1$
        } else if (type == METADATA_GENERIC_SCHEMA) {
            return "genericSchema"; //$NON-NLS-1$
        } else if (type == METADATA_WSDL_SCHEMA) {
            return "WSDLSchema"; //$NON-NLS-1$
        } else if (type == METADATA_SALESFORCE_SCHEMA) {
            return "SalesforceSchema"; //$NON-NLS-1$
        } else if (type == METADATA_FILE_RULES) {
            return "rules"; //$NON-NLS-1$
        } else if (type == METADATA_FILE_LINKRULES) {
            return "rules"; //$NON-NLS-1$
        } else if (type == METADATA_HEADER_FOOTER) {
            return "header_footer";
        } else if (type == TDQ_ANALYSIS_ELEMENT) {
            return "Analyses"; //$NON-NLS-1$
        } else if (type == TDQ_RULES_SQL) {
            return "Rules"; //$NON-NLS-1$
        } else if (type == TDQ_INDICATOR_ELEMENT) {
            return "Indicators"; //$NON-NLS-1$
        } else if (type == TDQ_PATTERN_ELEMENT) {
            return "Patterns"; //$NON-NLS-1$
        } else if (type == TDQ_JRAXML_ELEMENT) {
            return "JRXML Template";
        } else if (type == TDQ_REPORT_ELEMENT) {
            return "Reports"; //$NON-NLS-1$
        } else if (type == TDQ_ELEMENT) {
            return "";//$NON-NLS-1$
        } else if (type == COMPONENTS) {
            return "components";
        } else if (type.getType().equals("SERVICES")) {
            return "Services";
        } else if (type.getType().equals("TEST_CONTAINER")) {
            return "test case";
        } else if (GlobalServiceRegister.getDefault().isServiceRegistered(ICamelDesignerCoreService.class)) {
            ICamelDesignerCoreService camelService = (ICamelDesignerCoreService) GlobalServiceRegister.getDefault()
                    .getService(ICamelDesignerCoreService.class);
            String deleteFolderName = camelService.getDeleteFolderName(type);
            if (deleteFolderName != null) {
                return deleteFolderName;
            }
        }

        return "job";
    }

    /**
     * Please use instead the method : public static ERepositoryObjectType getType(Property property)<br>
     * 
     * This method might change to private in the future, since many calls will have low performance.
     * 
     * @param item
     * @return
     */
    public static ERepositoryObjectType getItemType(Item item) {

        ERepositoryObjectType repObjType = getTDQRepObjType(item);
        if (repObjType != null) {
            return repObjType;
        }
        repObjType = getRepositoryObjectType(item);
        if (repObjType != null) {
            return repObjType;
        }
        return (ERepositoryObjectType) new PropertiesSwitch() {

            @Override
            public Object caseFolderItem(FolderItem object) {
                return FOLDER;
            }

            @Override
            public Object caseDocumentationItem(DocumentationItem object) {
                return DOCUMENTATION;
            }

            @Override
            public Object caseLinkDocumentationItem(LinkDocumentationItem object) {
                return DOCUMENTATION;
            }

            @Override
            public Object caseRulesItem(RulesItem object) {
                return METADATA_FILE_RULES;
            }

            @Override
            public Object caseLinkRulesItem(LinkRulesItem object) {
                return METADATA_FILE_LINKRULES;
            }

            /*
             * (non-Javadoc)
             * 
             * @seeorg.talend.core.model.properties.util.PropertiesSwitch# caseJobDocumentationItem
             * (org.talend.core.model.properties.JobDocumentationItem)
             */
            @Override
            public Object caseJobDocumentationItem(JobDocumentationItem object) {
                return JOB_DOC;
            }

            /*
             * (non-Javadoc)
             * 
             * @seeorg.talend.core.model.properties.util.PropertiesSwitch# caseJobletDocumentationItem
             * (org.talend.core.model.properties.JobletDocumentationItem)
             */
            @Override
            public Object caseJobletDocumentationItem(JobletDocumentationItem object) {
                return JOBLET_DOC;
            }

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.talend.core.model.properties.util.PropertiesSwitch#casePigudfItem(org.talend.core.model.properties
             * .PigudfItem)
             */
            @Override
            public Object casePigudfItem(PigudfItem object) {
                return PIG_UDF;
            }

            @Override
            public Object caseRoutineItem(RoutineItem object) {
                return ROUTINES;
            }

            // public Object caseBeanItem(BeanItem object) {
            // return BEANS;
            // }

            @Override
            public Object caseJobScriptItem(JobScriptItem object) {
                return JOB_SCRIPT;
            }

            /*
             * (non-Javadoc)
             * 
             * @seeorg.talend.core.model.properties.util.PropertiesSwitch# caseSQLPatternItem
             * (org.talend.core.model.properties.SQLPatternItem)
             */
            @Override
            public Object caseSQLPatternItem(SQLPatternItem object) {
                return SQLPATTERNS;
            }

            @Override
            public Object caseProcessItem(ProcessItem object) {

                return PROCESS;
            }

            /*
             * (non-Javadoc)
             * 
             * @seeorg.talend.core.model.properties.util.PropertiesSwitch# caseJobletProcessItem
             * (org.talend.core.model.properties.JobletProcessItem)
             */
            @Override
            public Object caseJobletProcessItem(JobletProcessItem object) {
                return JOBLET;
            }

            @Override
            public Object caseContextItem(ContextItem object) {
                return CONTEXT;
            }

            @Override
            public Object caseSnippetItem(SnippetItem object) {
                return SNIPPETS;
            }

            @Override
            public Object caseSnippetVariable(SnippetVariable object) {
                return SNIPPETS;
            }

            @Override
            public Object caseBusinessProcessItem(BusinessProcessItem object) {
                return BUSINESS_PROCESS;
            }

            @Override
            public Object caseCSVFileConnectionItem(CSVFileConnectionItem object) {
                throw new IllegalStateException(Messages.getString("ERepositoryObjectType.NotImplemented")); //$NON-NLS-1$
            }

            @Override
            public Object caseDatabaseConnectionItem(DatabaseConnectionItem object) {
                return METADATA_CONNECTIONS;
            }

            @Override
            public Object caseSAPConnectionItem(SAPConnectionItem object) {
                return METADATA_SAPCONNECTIONS;
            }

            @Override
            public Object caseDelimitedFileConnectionItem(DelimitedFileConnectionItem object) {
                return METADATA_FILE_DELIMITED;
            }

            @Override
            public Object casePositionalFileConnectionItem(PositionalFileConnectionItem object) {
                return METADATA_FILE_POSITIONAL;
            }

            @Override
            public Object caseRegExFileConnectionItem(RegExFileConnectionItem object) {
                return METADATA_FILE_REGEXP;
            }

            @Override
            public Object caseXmlFileConnectionItem(XmlFileConnectionItem object) {
                return METADATA_FILE_XML;
            }

            @Override
            public Object caseExcelFileConnectionItem(ExcelFileConnectionItem object) {
                return METADATA_FILE_EXCEL;
            }

            @Override
            public Object caseLdifFileConnectionItem(LdifFileConnectionItem object) {
                return METADATA_FILE_LDIF;
            }

            @Override
            public Object caseLDAPSchemaConnectionItem(LDAPSchemaConnectionItem object) {
                return METADATA_LDAP_SCHEMA;
            }

            @Override
            public Object caseGenericSchemaConnectionItem(GenericSchemaConnectionItem object) {
                return METADATA_GENERIC_SCHEMA;
            }

            @Override
            public Object caseSalesforceSchemaConnectionItem(SalesforceSchemaConnectionItem object) {
                return METADATA_SALESFORCE_SCHEMA;
            }

            @Override
            public Object caseWSDLSchemaConnectionItem(WSDLSchemaConnectionItem object) {
                return METADATA_WSDL_SCHEMA;
            }

            @Override
            public Object caseEDIFACTConnectionItem(EDIFACTConnectionItem object) {
                return METADATA_EDIFACT;
            }

            @Override
            public Object caseEbcdicConnectionItem(EbcdicConnectionItem object) {
                return METADATA_FILE_EBCDIC;
            }

            @Override
            public Object caseHL7ConnectionItem(HL7ConnectionItem object) {
                return METADATA_FILE_HL7;
            }

            @Override
            public Object caseFTPConnectionItem(FTPConnectionItem object) {
                return METADATA_FILE_FTP;
            }

            @Override
            public Object caseBRMSConnectionItem(BRMSConnectionItem object) {
                return METADATA_FILE_BRMS;
            }

            @Override
            public Object caseMDMConnectionItem(MDMConnectionItem object) {
                return METADATA_MDMCONNECTION;
            }

            @Override
            public Object caseSVGBusinessProcessItem(SVGBusinessProcessItem object) {
                return SVG_BUSINESS_PROCESS;
            }

            @Override
            public Object caseHeaderFooterConnectionItem(HeaderFooterConnectionItem object) {
                return METADATA_HEADER_FOOTER;
            }

            // MOD mzhao feature 9207
            @Override
            public Object caseTDQItem(TDQItem object) {
                return TDQ_ELEMENT;
            }

            @Override
            public Object caseValidationRulesConnectionItem(ValidationRulesConnectionItem object) {
                return METADATA_VALIDATION_RULES;
            }

            @Override
            public Object defaultCase(EObject object) {
                // throw new IllegalStateException();
                return null; // fixed for TUP-575
            }
        }.doSwitch(item);

    }

    private static ERepositoryObjectType getTDQRepObjType(Item item) {
        AbstractDQModelService dqModelService = CoreRuntimePlugin.getInstance().getDQModelService();
        if (dqModelService != null) {
            return dqModelService.getTDQRepObjType(item);
        }
        return null;
    }

    private static ERepositoryObjectType getRepositoryObjectType(Item item) {
        ERepositoryObjectType type = null;
        for (IRepositoryContentHandler handler : RepositoryContentManager.getHandlers()) {
            type = handler.getRepositoryObjectType(item);
            if (type != null) {
                return type;
            }
        }
        if(item instanceof ConnectionItem){
            type = ERepositoryObjectType.getType(((ConnectionItem)item).getTypeName());
        }
        return type;
    }

    public boolean isSubItem() {
        return this.subItem;
    }

    public boolean isResourceItem() {
        return this.isResouce();
    }

    /**
     * DOC bZhou Comment method "isDQItemType".
     * 
     * This method is to estimat a type is a TDQ item or not.
     * 
     * @return
     */
    public boolean isDQItemType() {
        return Arrays.asList(this.getProducts()).contains(PROD_DQ);
    }

    /**
     * DOC bZhou Comment method "isDIType".
     * 
     * This method is to estimat a type belongs to Data Intergration.
     * 
     * @param type
     * @return
     */
    public boolean isDIItemType() {
        return Arrays.asList(this.getProducts()).contains(PROD_DI);
    }

    /**
     * DOC bZhou Comment method "isSharedType".
     * 
     * This method is to estimat a type belongs to both DQ and DI product.
     * 
     * @param type
     * @return
     */
    public boolean isSharedType() {
        return isDQItemType() && isDIItemType();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.repository.DynaEnum#name()
     */
    @Override
    public String name() {
        if (isStaticNode()) {
            Field[] allFields = ERepositoryObjectType.class.getDeclaredFields();
            for (Field f : allFields) {
                try {
                    Object object = f.get(null);
                    if (object == this) {
                        return f.getName();
                    }
                } catch (IllegalArgumentException e) {
                    //
                } catch (IllegalAccessException e) {
                    //
                }
            }
        }
        return super.name();
    }

    public String getFolder() {
        return this.folder;
    }

    public String[] getProducts() {
        return this.products;
    }

    public boolean isResouce() {
        return this.isResouce;
    }

    public String[] getUserRight() {
        return userRight;
    }

    public String getNamePattern() {
        return namePattern;
    }

    public boolean isAllowMultiName() {
        return isAllowMultiName;
    }

    public boolean isAllowPlainFolder() {
        return isAllowPlainFolder;
    }

    public void setAllowPlainFolder(boolean isAllowPlainFolder) {
        this.isAllowPlainFolder = isAllowPlainFolder;
    }

    public boolean addExtraProducts(String[] productsArray) {
        if (productsArray != null && productsArray.length > 0) {
            if (this.products == null && this.products.length == 0) {
                this.products = productsArray;
            } else {
                String[] tmp = new String[this.products.length + productsArray.length];
                System.arraycopy(this.products, 0, tmp, 0, this.products.length);
                System.arraycopy(productsArray, 0, tmp, this.products.length, productsArray.length);
                this.products = tmp;
            }
            return true;
        }
        return false;
    }

    protected List<ERepositoryObjectType> getParentTypes() {
        return this.parentTypes;
    }

    protected List<ERepositoryObjectType> getChildrenTypes() {
        return this.childrenTypes;
    }

    public ERepositoryObjectType[] getParentTypesArray() {
        return this.parentTypes.toArray(new ERepositoryObjectType[0]);
    }

    public ERepositoryObjectType[] getChildrenTypesArray() {
        return this.childrenTypes.toArray(new ERepositoryObjectType[0]);
    }

    public void setAParent(ERepositoryObjectType parentType) {
        if (parentType != null) {
            getParentTypes().add(parentType);
            parentType.getChildrenTypes().add(this);
        }
    }

    public boolean isChildTypeOf(ERepositoryObjectType parentType) {
        if (parentType != null && hasParentType()) {
            return getParentTypes().contains(parentType);
        }
        return false;
    }

    public boolean isParentTypeOf(ERepositoryObjectType childType) {
        if (childType != null && hasChildrenType()) {
            return getChildrenTypes().contains(childType);
        }
        return false;
    }

    public boolean hasChildrenType() {
        return !getChildrenTypes().isEmpty();
    }

    public boolean hasParentType() {
        return !getParentTypes().isEmpty();
    }

    /**
     * 
     * DOC ggu Comment method "valueOf".
     * 
     * in order to adapte old codes. and enable to init this class at same times.
     * 
     * @param clazz
     * @param name
     * @return
     */
    public static ERepositoryObjectType valueOf(Class<ERepositoryObjectType> clazz, String name) {
        return DynaEnum.valueOfEnum(clazz, name);
    }

    public static ERepositoryObjectType valueOf(String name) {
        return valueOf(ERepositoryObjectType.class, name);
    }

    public static ERepositoryObjectType findParentType(ERepositoryObjectType curtype) {
        if (curtype == null) {
            return null;
        }
        ERepositoryObjectType parentType = null;
        ERepositoryObjectType[] parentTypesArray = curtype.getParentTypesArray();
        if (parentTypesArray != null && parentTypesArray.length > 0) {
            parentType = parentTypesArray[0]; // only process first parent.
        }
        // maybe find by path again
        if (parentType == null) {
            String folder = curtype.getFolder();
            if (StringUtils.isNotEmpty(folder)) {
                IPath folderPath = new Path(folder);
                parentType = findParentTypeByPath(folderPath);
            }
        }
        return parentType;
    }

    private static ERepositoryObjectType findParentTypeByPath(IPath folderPath) {
        if (folderPath != null && folderPath.segmentCount() > 1) { // if only 1, should be no parent
            IPath parentPath = folderPath.removeLastSegments(1);
            for (ERepositoryObjectType type : (ERepositoryObjectType[]) ERepositoryObjectType.values()) {
                if (StringUtils.isNotEmpty(type.getFolder()) && parentPath.equals(new Path(type.getFolder()))) {
                    return type;
                }
            }
            return findParentTypeByPath(parentPath);
        }
        return null;
    }

    public static List<ERepositoryObjectType> getAllTypesOfProcess() {
        List<ERepositoryObjectType> allTypes = new ArrayList<ERepositoryObjectType>();

        if (ERepositoryObjectType.PROCESS != null) {
            allTypes.add(ERepositoryObjectType.PROCESS);
        }
        if (ERepositoryObjectType.PROCESS_MR != null) {
            allTypes.add(ERepositoryObjectType.PROCESS_MR);
        }
        if (ERepositoryObjectType.PROCESS_STORM != null) {
            allTypes.add(ERepositoryObjectType.PROCESS_STORM);
        }
        if (ERepositoryObjectType.PROCESS_SPARK != null) {
            allTypes.add(ERepositoryObjectType.PROCESS_SPARK);
        }
        if (ERepositoryObjectType.PROCESS_SPARKSTREAMING != null) {
            allTypes.add(ERepositoryObjectType.PROCESS_SPARKSTREAMING);
        }
        if (ERepositoryObjectType.PROCESS_ROUTE != null) {
            allTypes.add(ERepositoryObjectType.PROCESS_ROUTE);
        }
        // FIXME move to getAllTypesOfProcess2()
        if (ERepositoryObjectType.PROCESS_ROUTELET != null) {
            allTypes.add(ERepositoryObjectType.PROCESS_ROUTELET);
        }

        return allTypes;
    }

    /**
     * DOC zwxue Comment method "getAllTypesOfProcess2".
     * for types have pom.
     * @return
     */
    public static List<ERepositoryObjectType> getAllTypesOfProcess2() {
        List<ERepositoryObjectType> allTypes = getAllTypesOfProcess();
        if (ERepositoryObjectType.JOBLET != null) {
            allTypes.add(ERepositoryObjectType.JOBLET);
        }
        if (ERepositoryObjectType.SPARK_JOBLET != null) {
            allTypes.add(ERepositoryObjectType.SPARK_JOBLET);
        }
        if (ERepositoryObjectType.SPARK_STREAMING_JOBLET != null) {
            allTypes.add(ERepositoryObjectType.SPARK_STREAMING_JOBLET);
        }
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IESBService.class)) {
            IESBService esbService = (IESBService) GlobalServiceRegister.getDefault().getService(IESBService.class);
            ERepositoryObjectType serviceType = esbService.getServicesType();
            if (serviceType != null) {
                allTypes.add(serviceType);
            }
        }
        return allTypes;
    }

    public static List<ERepositoryObjectType> getAllTypesOfJoblet() {
        List<ERepositoryObjectType> allTypes = new ArrayList<ERepositoryObjectType>();

        if (ERepositoryObjectType.JOBLET != null) {
            allTypes.add(ERepositoryObjectType.JOBLET);
        }
        if (ERepositoryObjectType.SPARK_JOBLET != null) {
            allTypes.add(ERepositoryObjectType.SPARK_JOBLET);
        }
        if (ERepositoryObjectType.SPARK_STREAMING_JOBLET != null) {
            allTypes.add(ERepositoryObjectType.SPARK_STREAMING_JOBLET);
        }
        return allTypes;
    }

    public static List<ERepositoryObjectType> getAllTypesOfTestContainer() {
        List<ERepositoryObjectType> allTypes = new ArrayList<ERepositoryObjectType>();

        if (ERepositoryObjectType.TEST_CONTAINER != null) {
            allTypes.add(ERepositoryObjectType.TEST_CONTAINER);
        }
        return allTypes;
    }
}
