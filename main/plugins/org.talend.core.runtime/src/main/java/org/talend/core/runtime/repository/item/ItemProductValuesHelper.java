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

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EMap;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.Property;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.repository.ProjectManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
@SuppressWarnings({ "unchecked", "rawtypes", "nls" })
public final class ItemProductValuesHelper {

    /*
     * 2017-10-09T14:30:19.367+0800
     */
    public static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    public static String getCurDateTime() {
        return DATEFORMAT.format(new Date());
    }

    public static boolean existed(Property property) {
        if (property == null) {
            return false;
        }
        EMap additionalProperties = property.getAdditionalProperties();
        return additionalProperties.containsKey(ItemProductKeys.FULLNAME.getModifiedKey())
                || additionalProperties.containsKey(ItemProductKeys.FULLNAME.getCreatedKey());
    }

    /**
     * 
     * Set the created keys with date
     * 
     */
    public static boolean setValuesWhenCreate(Property property, Date date) {
        if (property == null) {
            return false;
        }
        if (date == null) {
            date = new Date();
        }

        if (!GlobalServiceRegister.getDefault().isServiceRegistered(IBrandingService.class)) {
            return false;
        }

        EMap additionalProperties = property.getAdditionalProperties();
        IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                IBrandingService.class);

        additionalProperties.put(ItemProductKeys.FULLNAME.getCreatedKey(), brandingService.getFullProductName());
        additionalProperties.put(ItemProductKeys.VERSION.getCreatedKey(), VersionUtils.getDisplayVersion());
        additionalProperties.put(ItemProductKeys.DATE.getCreatedKey(), DATEFORMAT.format(date));

        property.setCreationDate(null);

        return true;
    }

    /**
     * 
     * Set the modified keys with date when save .properties file
     */
    public static boolean setValuesWhenModify(Property property, Date date) {
        if (property == null) {
            return false;
        }
        if (date == null) {
            date = new Date();
        }

        if (!GlobalServiceRegister.getDefault().isServiceRegistered(IBrandingService.class)) {
            return false;
        }

        EMap additionalProperties = property.getAdditionalProperties();
        IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                IBrandingService.class);

        additionalProperties.put(ItemProductKeys.FULLNAME.getModifiedKey(), brandingService.getFullProductName());
        additionalProperties.put(ItemProductKeys.VERSION.getModifiedKey(), VersionUtils.getDisplayVersion());
        additionalProperties.put(ItemProductKeys.DATE.getModifiedKey(), DATEFORMAT.format(date));

        property.setModificationDate(null);

        return true;
    }

    /**
     * 
     * Try to migrate the created and modified date to keys.
     */
    public static boolean setValuesWhenMigrate(Property property) {
        if (property == null) {
            return false;
        }
        Project project = ProjectManager.getInstance().getProject(property);
        if (project == null) { // use current project instead
            project = ProjectManager.getInstance().getCurrentProject().getEmfProject();
        }
        return setValuesWhenMigrate(property, project);
    }

    public static boolean setValuesWhenMigrate(Property property, Project project) {
        if (property == null) {
            return false;
        }
        if (existed(property)) { // if existed, nothing to do
            return false;
        }
        if (project == null) { // use current project instead
            project = ProjectManager.getInstance().getCurrentProject().getEmfProject();
        }

        Map<String, String> productValues = getProductValues(project);
        if (productValues.isEmpty()) {
            return false;
        }
        String fullname = productValues.keySet().iterator().next();
        String version = productValues.get(fullname);

        String curDateTime = getCurDateTime();
        //
        migrateValues(property, fullname, version, curDateTime);

        return true;
    }

    private static void migrateValues(Property property, String fullname, String version, String datetime) {
        if (existed(property)) { // if existed, nothing to do
            return;
        }
        EMap additionalProperties = property.getAdditionalProperties();

        //
        Date creationDate = property.getCreationDate();
        additionalProperties.put(ItemProductKeys.FULLNAME.getCreatedKey(), fullname);
        additionalProperties.put(ItemProductKeys.VERSION.getCreatedKey(), version);
        if (creationDate != null) {
            additionalProperties.put(ItemProductKeys.DATE.getCreatedKey(), DATEFORMAT.format(creationDate));
        } else {
            additionalProperties.put(ItemProductKeys.DATE.getCreatedKey(), datetime);
        }
        property.setCreationDate(null); // move the date to additional properties

        //
        Date modificationDate = property.getModificationDate();
        additionalProperties.put(ItemProductKeys.FULLNAME.getModifiedKey(), fullname);
        additionalProperties.put(ItemProductKeys.VERSION.getModifiedKey(), version);
        if (modificationDate != null) {
            additionalProperties.put(ItemProductKeys.DATE.getModifiedKey(), DATEFORMAT.format(modificationDate));
        } else {
            additionalProperties.put(ItemProductKeys.DATE.getModifiedKey(), datetime);
        }
        property.setModificationDate(null); // move the date to additional properties

    }

    /**
     * When import, try to add import keys and add migration keys if possibly.
     * 
     */
    public static boolean setValuesWhenImport(Property property, Project project) {
        if (property == null || project == null) {
            return false;
        }

        Map<String, String> productValues = getProductValues(project);
        if (productValues.isEmpty()) {
            return false;
        }
        String fullname = productValues.keySet().iterator().next();
        String version = productValues.get(fullname);
        String curDateTime = getCurDateTime();

        return setImportValues(property, fullname, version, curDateTime);
    }

    public static boolean setImportValues(Property property, String fullname, String version, String datetime) {
        EMap additionalProperties = property.getAdditionalProperties();

        additionalProperties.put(ItemProductKeys.FULLNAME.getImportKey(), fullname);
        additionalProperties.put(ItemProductKeys.VERSION.getImportKey(), version);
        additionalProperties.put(ItemProductKeys.DATE.getImportKey(), datetime);

        // if need, migrate other keys first, because in migration task, only for current project
        migrateValues(property, fullname, version, datetime);

        return true;
    }

    public static Map<String, String> getProductValues(Project project) {
        if (project == null) {
            return Collections.emptyMap();
        }
        return parseProduct(project.getProductVersion());
    }

    static Map<String, String> parseProduct(String value) {
        if (value == null || value.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> result = new HashMap<String, String>();
        int sepIndex = value.indexOf('-');
        if (sepIndex > 0) {
            String fullname = value.substring(0, sepIndex);
            String version = value.substring(sepIndex + 1);
            if (version.length() > 0) {
                result.put(fullname, version);
            }
        }
        if (result.isEmpty()) { // if invalid, set the full name for whole string without version
            result.put(value, null);
        }
        return result;

    }
}
