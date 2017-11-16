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
    static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    private static String getCurDateTime() {
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

        EMap additionalProperties = property.getAdditionalProperties();

        String productStr = project.getProductVersion();
        Map<String, String> productValues = parseProduct(productStr);
        if (productValues.isEmpty()) {
            return false;
        }
        String fullname = productValues.keySet().iterator().next();
        String version = productValues.get(fullname);

        String curDateTime = getCurDateTime();
        //
        Date creationDate = property.getCreationDate();
        additionalProperties.put(ItemProductKeys.FULLNAME.getCreatedKey(), fullname);
        additionalProperties.put(ItemProductKeys.VERSION.getCreatedKey(), version);
        if (creationDate != null) {
            additionalProperties.put(ItemProductKeys.DATE.getCreatedKey(), DATEFORMAT.format(creationDate));
        } else {
            additionalProperties.put(ItemProductKeys.DATE.getCreatedKey(), curDateTime);
        }
        property.setCreationDate(null); // move the date to additional properties

        //
        Date modificationDate = property.getModificationDate();
        additionalProperties.put(ItemProductKeys.FULLNAME.getModifiedKey(), fullname);
        additionalProperties.put(ItemProductKeys.VERSION.getModifiedKey(), version);
        if (modificationDate != null) {
            additionalProperties.put(ItemProductKeys.DATE.getModifiedKey(), DATEFORMAT.format(modificationDate));
        } else {
            additionalProperties.put(ItemProductKeys.DATE.getModifiedKey(), curDateTime);
        }
        property.setModificationDate(null); // move the date to additional properties

        return true;
    }

    public static boolean setValuesWhenImport(Property property, Project project) {
        if (property == null || project == null) {
            return false;
        }
        EMap additionalProperties = property.getAdditionalProperties();
        String productVersion = project.getProductVersion();
        Map<String, String> productValues = parseProduct(productVersion);
        if (productValues.isEmpty()) {
            return false;
        }
        String fullname = productValues.keySet().iterator().next();
        String version = productValues.get(fullname);

        additionalProperties.put(ItemProductKeys.FULLNAME.getImportKey(), fullname);
        additionalProperties.put(ItemProductKeys.VERSION.getImportKey(), version);
        additionalProperties.put(ItemProductKeys.DATE.getImportKey(), getCurDateTime());

        // if need, migrate other keys first, because in migration task, only for current project
        setValuesWhenMigrate(property, project);

        return true;
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
