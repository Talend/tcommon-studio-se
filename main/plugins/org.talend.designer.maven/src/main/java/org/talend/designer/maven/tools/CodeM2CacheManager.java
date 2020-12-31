// ============================================================================
//
// Copyright (C) 2006-2020 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.maven.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.m2e.core.MavenPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.repository.item.ItemProductKeys;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.designer.maven.utils.PomIdsHelper;

public class CodeM2CacheManager {

    private static final String KEY_SEPERATOR = "|"; //$NON-NLS-1$

    private static final String EMPTY_DATE;

    private static File cacheFolder;

    static {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(0);
        EMPTY_DATE = ResourceHelper.dateFormat().format(c.getTime());
        cacheFolder = new File(MavenPlugin.getMaven().getLocalRepositoryPath()).toPath().resolve(".codecache").resolve("codes")
                .toFile();
        if (!cacheFolder.exists()) {
            cacheFolder.mkdirs();
        }
    }

    public static boolean needUpdateCodeProject(Project project, ERepositoryObjectType codeType) {
        try {
            String projectTechName = project.getTechnicalLabel();
            File cacheFile = getCacheFile(projectTechName, codeType);
            if (!cacheFile.exists()) {
                return true;
            }
            Properties cache = new Properties();
            cache.load(new FileInputStream(cacheFile));
            List<IRepositoryViewObject> allCodes = ProxyRepositoryFactory.getInstance().getAll(project, codeType, false);
            // check A/D
            if (allCodes.size() != cache.size()) {
                return true;
            }
            // check M
            for (IRepositoryViewObject codeItem : allCodes) {
                Property property = codeItem.getProperty();
                String key = getKey(projectTechName, property);
                String cacheValue = cache.getProperty(key);
                if (cacheValue != null) {
                    Date currentDate = ResourceHelper.dateFormat().parse(getModifiedDate(property));
                    Date cachedDate = ResourceHelper.dateFormat().parse(cacheValue);
                    if (currentDate.compareTo(cachedDate) != 0) {
                        return true;
                    }
                }
            }
        } catch (PersistenceException | IOException | ParseException e) {
            ExceptionHandler.process(e);
            // if any exception, still update in case breaking build job
            return true;
        }
        return false;
    }

    public static void updateCodeProjectCache(Project project, ERepositoryObjectType codeType) {
        String projectTechName = project.getTechnicalLabel();
        File cacheFile = getCacheFile(projectTechName, codeType);
        try (OutputStream out = new FileOutputStream(cacheFile)) {
            List<IRepositoryViewObject> allCodes = ProxyRepositoryFactory.getInstance().getAll(project, codeType, false);
            Properties cache = new Properties();
            for (IRepositoryViewObject codeItem : allCodes) {
                Property property = codeItem.getProperty();
                String key = getKey(projectTechName, property);
                String value = getModifiedDate(property);
                cache.put(key, value);
            }
            cache.store(out, StringUtils.EMPTY);
        } catch (PersistenceException | IOException e) {
            ExceptionHandler.process(e);
        }
    }

    public static File getCacheFile(String projectTechName, ERepositoryObjectType codeType) {
        String cacheFileName = PomIdsHelper.getProjectGroupId(projectTechName) + "." + codeType.name().toLowerCase() + "-" //$NON-NLS-1$ //$NON-NLS-2$
                + PomIdsHelper.getCodesVersion(projectTechName) + ".cache"; // $NON-NLS-1$
        return new File(cacheFolder, cacheFileName);
    }

    private static String getKey(String projectTechName, Property property) {
        return projectTechName + KEY_SEPERATOR + property.getId() + KEY_SEPERATOR + property.getVersion(); // $NON-NLS-1$
    }

    private static String getModifiedDate(Property property) {
        String modifiedDate = (String) property.getAdditionalProperties().get(ItemProductKeys.DATE.getModifiedKey());
        return StringUtils.isNotBlank(modifiedDate) ? modifiedDate : EMPTY_DATE;
    }

}
