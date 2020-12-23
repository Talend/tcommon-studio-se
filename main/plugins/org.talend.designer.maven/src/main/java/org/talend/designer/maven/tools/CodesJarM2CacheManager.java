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
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.m2e.core.MavenPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutinesJarItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.repository.item.ItemProductKeys;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.designer.core.model.utils.emf.component.IMPORTType;
import org.talend.designer.maven.utils.PomIdsHelper;

public class CodesJarM2CacheManager {

    private static final String KEY_MODIFIED_DATE = "MODIFIED_DATE"; //$NON-NLS-1$

    private static final String KEY_DEPENDENCY_LIST = "DEPENDENCY_LIST"; //$NON-NLS-1$

    private static final String KEY_INNERCODE_PREFIX = "INNERCODE"; //$NON-NLS-1$

    private static final String KEY_SEPERATOR = "|"; //$NON-NLS-1$

    private static final String DEP_SEPERATOR = ","; //$NON-NLS-1$

    private static final String EMPTY_DATE;

    static {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(0);
        EMPTY_DATE = ResourceHelper.dateFormat().format(c.getTime());
    }

    @SuppressWarnings("unchecked")
    public static boolean needUpdateCodesJarProject(Project project, Property property) {
        try {
            ERepositoryObjectType codeType = ERepositoryObjectType.getItemType(property.getItem());
            String projectTechName = project.getTechnicalLabel();
            File cacheFile = getCacheFile(projectTechName, property);
            if (!cacheFile.exists()) {
                return true;
            }
            DateFormat format = ResourceHelper.dateFormat();
            Properties cache = new Properties();
            cache.load(new FileInputStream(cacheFile));
            String currentTime = getModifiedDate(property);
            String cachedTime = cache.getProperty(KEY_MODIFIED_DATE);
            // check codesjar modified date
            if (cachedTime == null) {
                return true;
            }
            if (format.parse(currentTime).compareTo(format.parse(cachedTime)) != 0) {
                return true;
            }

            // check dependency list
            String dependencies = cache.getProperty(KEY_DEPENDENCY_LIST);
            List<String> cachedDepList;
            if (dependencies == null) {
                cachedDepList = Collections.emptyList();
            } else {
                cachedDepList = Arrays.asList(dependencies.split(DEP_SEPERATOR));
            }
            EList<IMPORTType> imports = ((RoutinesJarItem) property.getItem()).getRoutinesJarType().getImports();
            List<String> currentDepList = imports.stream().map(IMPORTType::getMVN).collect(Collectors.toList());
            if (cachedDepList.size() != currentDepList.size()) {
                return true;
            }
            if (!cachedDepList.isEmpty() && !cachedDepList.stream().allMatch(s -> currentDepList.contains(s))) {
                return true;
            }

            // check inner codes
            List<IRepositoryViewObject> currentInnerCodes = ProxyRepositoryFactory.getInstance().getAllInnerCodes(project,
                    codeType, property);
            Map<Object, Object> cachedInnerCodes = cache.entrySet().stream()
                    .filter(e -> e.getKey().toString().startsWith(KEY_INNERCODE_PREFIX))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            // check A/D
            if (currentInnerCodes.size() != cachedInnerCodes.size()) {
                return true;
            }
            // check M
            for (IRepositoryViewObject codeItem : currentInnerCodes) {
                Property innerCodeProperty = codeItem.getProperty();
                String key = getInnerCodeKey(projectTechName, innerCodeProperty);
                String cacheValue = (String) cachedInnerCodes.get(key);
                if (cacheValue != null) {
                    Date currentDate = ResourceHelper.dateFormat().parse(getModifiedDate(innerCodeProperty));
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

    @SuppressWarnings("unchecked")
    public static void updateCodesJarProjectCache(Project project, Property property) {
        ERepositoryObjectType codeType = ERepositoryObjectType.getItemType(property.getItem());
        String projectTechName = project.getTechnicalLabel();
        Properties cache = new Properties();
        File cacheFile = getCacheFile(projectTechName, property);
        // update codesjar modified date
        cache.setProperty(KEY_MODIFIED_DATE, getModifiedDate(property));
        // update dependencies
        EList<IMPORTType> imports = ((RoutinesJarItem) property.getItem()).getRoutinesJarType().getImports();
        StringBuilder builder = new StringBuilder();
        if (!imports.isEmpty()) {
            imports.forEach(i -> builder.append(i.getMVN()).append(DEP_SEPERATOR));
            cache.setProperty(KEY_DEPENDENCY_LIST, StringUtils.stripEnd(builder.toString(), DEP_SEPERATOR));
        }
        // update inner codes
        try (OutputStream out = new FileOutputStream(cacheFile)) {
            List<IRepositoryViewObject> allInnerCodes = ProxyRepositoryFactory.getInstance().getAll(project, codeType, false);
            for (IRepositoryViewObject codeItem : allInnerCodes) {
                Property innerCodeProperty = codeItem.getProperty();
                String key = getInnerCodeKey(projectTechName, innerCodeProperty);
                String value = getModifiedDate(innerCodeProperty);
                cache.put(key, value);
            }
            cache.store(out, StringUtils.EMPTY);
        } catch (PersistenceException | IOException e) {
            ExceptionHandler.process(e);
        }
    }

    public static File getCacheFile(String projectTechName, Property property) {
        Path cacheRootPath = new File(MavenPlugin.getMaven().getLocalRepositoryPath()).toPath().resolve(".codecache")
                .resolve("codesjar");
        String cacheFileName = PomIdsHelper.getCodesJarGroupId(projectTechName, property.getItem()) + "." //$NON-NLS-1$
                + property.getLabel().toLowerCase() + "-" //$NON-NLS-1$
                + PomIdsHelper.getCodesVersion(projectTechName) + ".cache"; // $NON-NLS-1$

        return cacheRootPath.resolve(cacheFileName).toFile();
    }

    private static String getInnerCodeKey(String projectTechName, Property property) {
        return KEY_INNERCODE_PREFIX + KEY_SEPERATOR + property.getId() + KEY_SEPERATOR + property.getVersion();
    }

    private static String getModifiedDate(Property property) {
        String modifiedDate = (String) property.getAdditionalProperties().get(ItemProductKeys.DATE.getModifiedKey());
        return StringUtils.isNotBlank(modifiedDate) ? modifiedDate : EMPTY_DATE;
    }

}
