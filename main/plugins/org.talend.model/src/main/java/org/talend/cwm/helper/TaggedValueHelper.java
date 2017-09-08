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

import java.util.Collection;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.talend.commons.runtime.model.emf.EmfHelper;
import orgomg.cwm.objectmodel.core.CoreFactory;
import orgomg.cwm.objectmodel.core.CorePackage;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.TaggedValue;

/**
 * @author scorreia
 * 
 * This class is a helper for handling TaggedValues.
 */
public final class TaggedValueHelper {

    private static Logger log = Logger.getLogger(TaggedValueHelper.class);

    public static final String OTHER_PARAMETER = "otherParameter"; //$NON-NLS-1$

    /**
     * The tag used when setting a column content type.
     */
    public static final String DATA_CONTENT_TYPE_TAGGED_VAL = "Content Type"; //$NON-NLS-1$

    // pattern tagged values
    public static final String VALID_STATUS = "Validation_Status"; //$NON-NLS-1$

    // ~~~~~~~~~~~~~~~~~~~~~~~~~

    // metadata tagged values

    public static final String AUTHOR = "Author"; //$NON-NLS-1$

    public static final String DESCRIPTION = "Description"; //$NON-NLS-1$

    public static final String DEV_STATUS = "Status"; //$NON-NLS-1$

    public static final String PURPOSE = "Purpose"; //$NON-NLS-1$

    public static final String VERSION = "Version"; //$NON-NLS-1$

    // ~~~~~~~~~~~~~~~~~~~~~~~~~

    // TABLE/VIEW/COLUMN tagged values
    public static final String TABLE_FILTER = "Table Filter"; //$NON-NLS-1$

    public static final String VIEW_FILTER = "View Filter"; //$NON-NLS-1$

    public static final String COLUMN_FILTER = "Column Filter"; //$NON-NLS-1$

    public static final String COMMENT = "Comment"; //$NON-NLS-1$

    public static final String SYSTEMTABLENAME = "SYSTEM_TABLE_NAME"; //$NON-NLS-1$

    public static final String SYSTEMTABLESCHEMA = "SYSTEM_TABLE_SCHEMA"; //$NON-NLS-1$

    public static final String TABLESCHEMA = "TABLE_SCHEMA"; //$NON-NLS-1$

    // ~~~~~~~~~~~~~~~~~~~~~~~~~

    // data connection tagged values
    public static final String USER = "user"; //$NON-NLS-1$

    public static final String PASSWORD = "password"; //$NON-NLS-1$

    public static final String HOST = "HOST"; //$NON-NLS-1$

    public static final String PORT = "PORT"; //$NON-NLS-1$

    public static final String DBTYPE = "DBTYPE"; //$NON-NLS-1$

    public static final String DBNAME = "DBNAME"; //$NON-NLS-1$

    public static final String DB_IDENTIFIER_QUOTE_STRING = "DB IdentifierQuoteString"; //$NON-NLS-1$

    // ADD sizhaoliu TDQ-6316 add 2 tagged values to replace software system
    public static final String DB_PRODUCT_NAME = "DB ProductName";//$NON-NLS-1$

    public static final String DB_PRODUCT_VERSION = "DB ProductVersion";//$NON-NLS-1$

    public static final String UNIVERSE = "universe"; //$NON-NLS-1$

    public static final String RETRIEVE_ALL = "RETRIEVE_ALL"; //$NON-NLS-1$

    public static final String DATA_FILTER = "data filter";//$NON-NLS-1$

    // MOD klliu 2012-02-08 TDQ-4645
    public static final String PACKAGE_FILTER = "package filter";//$NON-NLS-1$

    // ~~~~~~~~~~~~~~~~~~~~~~~~~

    // report tagged values
    public static final String GEN_SINGLE_REPORT = "Single output"; //$NON-NLS-1$

    public static final String OUTPUT_TYPE_TAG = "OutputType"; //$NON-NLS-1$

    public static final String OUTPUT_FILENAME_TAG = "OutputFileName"; //$NON-NLS-1$

    public static final String OUTPUT_FOLDER_TAG = "OutputFolder"; //$NON-NLS-1$

    // report database connection info
    public static final String REP_DBINFO_DBTYPE = "REP_DBINFO_DBTYPE"; //$NON-NLS-1$

    public static final String REP_DBINFO_DBVERSION = "REP_DBINFO_DBVERSION"; //$NON-NLS-1$

    public static final String REP_DBINFO_DBNAME = "REP_DBINFO_DBNAME"; //$NON-NLS-1$

    public static final String REP_DBINFO_USER = "REP_DBINFO_USER"; //$NON-NLS-1$

    public static final String REP_DBINFO_PASSWORD = "REP_DBINFO_PASSWORD"; //$NON-NLS-1$

    public static final String REP_DBINFO_DRIVER = "REP_DBINFO_DRIVER"; //$NON-NLS-1$

    public static final String REP_DBINFO_DIALECT = "REP_DBINFO_DIALECT"; //$NON-NLS-1$

    public static final String REP_DBINFO_URL = "REP_DBINFO_URL"; //$NON-NLS-1$

    public static final String REP_DBINFO_HOST = "REP_DBINFO_HOST"; //$NON-NLS-1$

    public static final String REP_DBINFO_PORT = "REP_DBINFO_PORT"; //$NON-NLS-1$

    public static final String REP_DBINFO_SCHEMA = "REP_DBINFO_SCHEMA"; //$NON-NLS-1$

    public static final String REP_LAST_RUN_CONTEXT = "REP_LAST_RUN_CONTEXT"; //$NON-NLS-1$

    public static final String REP_PASSWORD_CONTEXT = "REP_PASSWORD_CONTEXT"; //$NON-NLS-1$

    /**
     * the analysis last run context tag value key.
     */
    public static final String ANA_LAST_RUN_CONTEXT = "ANA_LAST_RUN_CONTEXT"; //$NON-NLS-1$

    // ~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * The tag used for setting a technical name.
     */
    public static final String TECH_NAME_TAGGED_VAL = "Technical Name"; //$NON-NLS-1$

    // ~~~~~~~~~~~~~~~~~~~~~~~~~

    // Property tagged values.
    // MOD mzhao feature 7488
    public static final String PROPERTY_FILE = "Property File"; //$NON-NLS-1$

    public static final String TDQ_ELEMENT_FILE = "TDQ Element File"; //$NON-NLS-1$

    // ~~~~~~~~~~~~~~~~~~~~~~~~~

    // overview analysis tagged values
    public static final String RELOAD_DATABASES = "Reload Databases"; //$NON-NLS-1$

    // ~~~~~~~~~~~~~~~~~~~~~~~~~

    // UDI tagged values
    public static final String CLASS_NAME_TEXT = "CLASS_NAME_TEXT";//$NON-NLS-1$

    public static final String JAR_FILE_PATH = "JAR_FILE_PATH";//$NON-NLS-1$

    // Added yyin 20121203 TDQ-6497 use "IS_DB_NEED_RELOAD" to replace "USING_URL"
    public static final String IS_CONN_NEED_RELOAD = "Is Conn Need Reload";//$NON-NLS-1$

    public static final String REP_GENERATE_FILE = "REP_GENERATE_FILE"; //$NON-NLS-1$

    public static final String CONCEPT_NAME = "CONCEPT_NAME"; //$NON-NLS-1$

    public static final String SEMANTIC_NAME = "SEMANTIC_NAME"; //$NON-NLS-1$

    public static final String CONTENT_TYPE = "Content Type"; //$NON-NLS-1$

    public static final String PREVIEW_ROW_NUMBER = "PREVIEW_ROW_NUMBER"; //$NON-NLS-1$

    // Added zshen TDQ-12581

    public static final String IS_USE_SAMPLE_DATA = "Is Use Sample Data"; //$NON-NLS-1$

    public static final String IS_SQL_ENGIN_BEFORE_CHECK = "false"; //$NON-NLS-1$

    private TaggedValueHelper() {
    }

    /**
     * Method "createTaggedValue".
     * 
     * @param tag the tag of the tagged value to create
     * @param value the value of the tagged value to create
     * @return the created tagged value
     */
    public static TaggedValue createTaggedValue(String tag, String value) {
        TaggedValue taggedValue = CoreFactory.eINSTANCE.createTaggedValue();
        taggedValue.setTag(tag);
        taggedValue.setValue(value);
        return taggedValue;
    }

    /**
     * Method "getTaggedValue" retrieves the tagged value corresponding to the first matching tag.
     * 
     * @param tag the tag to match
     * @param taggedValues the tagged values in which to search for the given tag
     * @return the tagged value (if found) or null
     */
    public static TaggedValue getTaggedValue(String tag, Collection<TaggedValue> taggedValues) {
        if (tag == null || taggedValues == null) {
            return null;
        }
        TaggedValue value = null;
        for (TaggedValue taggedValue : taggedValues) {
            if (taggedValue == null) {
                continue;
            }
            if (tag.compareTo(taggedValue.getTag()) == 0) {
                value = taggedValue;
            }
        }
        return value;
    }

    /**
     * Method "setTaggedValue".
     * 
     * @param element the CWM model element to which a tagged value will be attached (if not already set)
     * @param tag the tag
     * @param value the value to set
     * @return true if the value was not set before.
     */
    public static boolean setTaggedValue(ModelElement element, String tag, String value) {
        boolean create = false;
        EList<TaggedValue> taggedValues = element.getTaggedValue();
        TaggedValue currentValue = TaggedValueHelper.getTaggedValue(tag, taggedValues);
        if (currentValue == null) {
            taggedValues.add(TaggedValueHelper.createTaggedValue(tag, value));
            create = true;
        } else {
            currentValue.setValue(value);
        }
        return create;
    }

    /**
     * Method "setValidStatus" sets the status on the given element.
     * 
     * @param status the status to set
     * @param pattern the element
     * @return true if the value was not set before.
     */
    public static boolean setValidStatus(Boolean status, ModelElement element) {
        String statusStr = String.valueOf(status);
        return TaggedValueHelper.setTaggedValue(element, TaggedValueHelper.VALID_STATUS, statusStr);
    }

    /**
     * Method "getValidStatus".
     * 
     * @param element
     * @return the validation status of the element
     */
    public static Boolean getValidStatus(ModelElement element) {
        return getValueBoolean(TaggedValueHelper.VALID_STATUS, element);
    }

    /**
     * get the Boolean value according to the tag name, if the TaggedValue is null, return false.
     * 
     * @param tag
     * @param element
     * @return
     */
    public static Boolean getValueBoolean(String tag, ModelElement element) {
        TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(tag, element.getTaggedValue());
        return taggedValue == null ? false : Boolean.valueOf(taggedValue.getValue());
    }

    /**
     * get the CLASS_NAME_TEXT value.
     * 
     * @param element
     * @return
     */
    public static String getClassNameText(ModelElement element) {
        return getValueString(TaggedValueHelper.CLASS_NAME_TEXT, element);
    }

    /**
     * set the CLASS_NAME_TEXT value.
     * 
     * @param classNameText
     * @param element
     * @return
     */
    public static boolean setClassNameText(String classNameText, ModelElement element) {
        return TaggedValueHelper.setTaggedValue(element, TaggedValueHelper.CLASS_NAME_TEXT, classNameText);
    }

    /**
     * get the JAR_FILE_PATH value.
     * 
     * @param element
     * @return
     */
    public static String getJarFilePath(ModelElement element) {
        return getValueString(TaggedValueHelper.JAR_FILE_PATH, element);
    }

    /**
     * set the JAR_FILE_PATH value.
     * 
     * @param jarFilePath
     * @param element
     * @return
     */
    public static boolean setJarFilePath(String jarFilePath, ModelElement element) {
        return TaggedValueHelper.setTaggedValue(element, TaggedValueHelper.JAR_FILE_PATH, jarFilePath);
    }

    /**
     * get the String value according to the tag name, if the TaggedValue is null, return empty string.
     * 
     * @param tag
     * @param element
     * @return
     */
    public static String getValueString(String tag, ModelElement element) {
        TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(tag, element.getTaggedValue());
        return taggedValue == null ? "" : taggedValue.getValue(); //$NON-NLS-1$
    }

    /**
     * return the String size limit for the given ecore attribute. This looks for an annotation url :
     * htttp://talend.org/UiConstraints and search for the key string.max.size
     * 
     * @param tag the tag value to get the size limit from.
     * @param defaultValue the default value returned if limit not found in feature
     * @return the string limit found or the default value ADDED sgandon 16/03/2010 bug 11760
     */
    public static int getStringMaxSize(String tag, int defaultValue) {
        Assert.isNotNull(tag);
        int result = defaultValue;
        EAnnotation guiAnnotation = CorePackage.Literals.MODEL_ELEMENT__TAGGED_VALUE
                .getEAnnotation(EmfHelper.UI_CONSTRAINTS_ANNOTATION_URL);
        if (guiAnnotation != null) {
            String docuValue = guiAnnotation.getDetails().get(EmfHelper.STRING_MAX_SIZE_ANNOTATION_KEY + "." + tag); //$NON-NLS-1$
            try {
                result = Integer.parseInt(docuValue);
            } catch (Exception e) { // if conversion fail return default value
                // MOD sgandon 1/04/2010 bug 11760 : change error to warning cause there is a fallback
                log.warn("Could not get max size for tag " + tag, e); //$NON-NLS-1$
            }
        } // else return default value
        return result;
    }
}
