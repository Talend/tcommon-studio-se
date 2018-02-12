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
package org.talend.migration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.osgi.framework.FrameworkUtil;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.update.extension.UpdateManagerProviderReader;
import org.talend.core.utils.RegistryReader;

/**
 * Parses the Eclipse extension point registry to construct a Migration Task extension map using the
 * org.talend.migration.task.type.extension extension point
 * 
 */
public class MigrationTaskExtensionEPReader extends RegistryReader {

    /**
     * 
     */
    private static final String TYPE_ATTRIBUTE = "type"; //$NON-NLS-1$

    /**
     * 
     */
    private static final String EXTENDED_TYPE_ATTRIBUTE = "extended_type"; //$NON-NLS-1$

    private static final String MIGRATION_TASK_TYPE_EXTENSION_EXTENTION_POINT = "org.talend.migration.task.type.extension"; //$NON-NLS-1$

    private static final String TYPE_EXTENTION_ELEMENT_NAME = "TypeExtension"; //$NON-NLS-1$

    private Map<ERepositoryObjectType, List<ERepositoryObjectType>> migrationTaskExtensionsMap = new HashMap<ERepositoryObjectType, List<ERepositoryObjectType>>();

    /**
     * DOC sgandon MigrationTaskExtensionEPReader constructor comment.
     * 
     * @param aPluginId
     * @param anExtensionPoint
     */
    public MigrationTaskExtensionEPReader() {
        super(FrameworkUtil.getBundle(UpdateManagerProviderReader.class).getSymbolicName(),
                MIGRATION_TASK_TYPE_EXTENSION_EXTENTION_POINT);
        readRegistry();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.utils.RegistryReader#readElement(org.eclipse.core.runtime.IConfigurationElement)
     */
    @Override
    protected boolean readElement(final IConfigurationElement element) {
        if (TYPE_EXTENTION_ELEMENT_NAME.equals(element.getName())) {
            String extendedTypeStr = element.getAttribute(EXTENDED_TYPE_ATTRIBUTE);
            String typeStr = element.getAttribute(TYPE_ATTRIBUTE);
            ERepositoryObjectType extendedType = ERepositoryObjectType.getType(extendedTypeStr);
            ERepositoryObjectType type = ERepositoryObjectType.getType(typeStr);
            if (extendedType == null) {
                logError(element, "Could not find the EReositoryObjectType associated to the attribute extended_type : " //$NON-NLS-1$
                        + extendedTypeStr);
                return true;
            }
            if (type == null) {
                logError(element, "Could not find the EReositoryObjectType associated to the attribute type : " + typeStr); //$NON-NLS-1$
                return true;
            }
            List<ERepositoryObjectType> extensionList = migrationTaskExtensionsMap.get(extendedType);
            if (extensionList == null) {// no existing list so create one
                extensionList = new ArrayList<ERepositoryObjectType>();
                extensionList.add(type);
                migrationTaskExtensionsMap.put(extendedType, extensionList);
            } else {// extensions already exists
                extensionList.add(type);
            }
            return true;
        }// else return false
        return false;
    }

    /**
     * return the Map of all extensiion of all type.
     * 
     * @return
     */
    public Map<ERepositoryObjectType, List<ERepositoryObjectType>> getMigrationTaskExtensions() {
        return migrationTaskExtensionsMap;
    }

    /**
     * 
     * 
     * @return the unique set of ERepositoryObjectType that extends an existing collection of ERepositoryObjectType for
     * the migration task, may be emty but never null.
     */
    public Set<ERepositoryObjectType> getObjectTypeExtensions(Collection<ERepositoryObjectType> extendedObjects) {
        HashSet<ERepositoryObjectType> extensions = new HashSet<ERepositoryObjectType>();
        for (ERepositoryObjectType extendedObj : extendedObjects) {
            List<ERepositoryObjectType> listOfExtensions = migrationTaskExtensionsMap.get(extendedObj);
            if (listOfExtensions != null) {
                extensions.addAll(listOfExtensions);
            }
        }
        return extensions;
    }

}
