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
package org.talend.core.model.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.talend.core.model.metadata.types.DBTypeUtil;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.prefs.ITalendCorePrefConstants;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class MappingTypeRetriever {

    private static Logger log = Logger.getLogger(MappingTypeRetriever.class);

    private Dbms dbms;

    private MultiMap mapDbToTalendTypes = new MultiValueMap();

    private MultiMap mapTalendToDbTypes = new MultiValueMap();

    private Map<MappingType, MappingType> defaultMappings = new HashMap<MappingType, MappingType>();

    private MappingType mappingTypeKey = new MappingType();

    /**
     * DOC amaumont MappingTypeHelper constructor comment.
     * 
     * @param dbms
     */
    public MappingTypeRetriever(Dbms dbms) {
        super();
        this.dbms = dbms;
        init(dbms);
    }

    /**
     * DOC amaumont Comment method "init".
     */
    private void init(Dbms dbms) {
        Set<MappingType> mappingTypes = dbms.getTalendToDbTypes();
        defaultMappings.clear();
        mapTalendToDbTypes.clear();
        for (MappingType mappingType : mappingTypes) {

            MappingType mapTalendToDbKey = new MappingType();
            mapTalendToDbKey.setTalendType(mappingType.getTalendType());
            mapTalendToDbTypes.put(mapTalendToDbKey, mappingType);
            if (mappingType.getDefaultSelected()) {
                mapTalendToDbKey = new MappingType();
                mapTalendToDbKey.setTalendType(mappingType.getTalendType());
                mapTalendToDbKey.setDefaultSelected(mappingType.getDefaultSelected());
                defaultMappings.put(mapTalendToDbKey, mappingType);
            }

        }
        mappingTypes = dbms.getDbToTalendTypes();
        mapDbToTalendTypes.clear();
        for (MappingType mappingType : mappingTypes) {
            MappingType mapDbToTalendKey = new MappingType();
            mapDbToTalendKey.setDbType(mappingType.getDbType());
            mapDbToTalendTypes.put(mapDbToTalendKey, mappingType);
            if (mappingType.getDefaultSelected()) {
                mapDbToTalendKey = new MappingType();
                mapDbToTalendKey.setDbType(mappingType.getDbType().toUpperCase());
                mapDbToTalendKey.setDefaultSelected(mappingType.getDefaultSelected());
                defaultMappings.put(mapDbToTalendKey, mappingType);
            }
        }
    }

    /**
     * Returns adviced mappings for a given db type.
     * 
     * @param dbType
     * @return a list of MappingType or null if not found
     */
    public List<MappingType> getAdvicedDbToTalendTypes(String dbType) {
        mappingTypeKey.setDbType(dbType.toUpperCase());
        mappingTypeKey.setTalendType(null);
        mappingTypeKey.setDefaultSelected(null);
        mapDbToTalendTypes.get(mappingTypeKey);
        return (List<MappingType>) mapDbToTalendTypes.get(mappingTypeKey);
    }

    /**
     * Returns adviced mappings for a given Talend type.
     * 
     * @param talendType
     * @return a list of MappingType or null if not found
     */
    public List<MappingType> getAdvicedTalendToDbTypes(String talendType) {
        mappingTypeKey.setDbType(null);
        mappingTypeKey.setTalendType(talendType);
        mappingTypeKey.setDefaultSelected(null);
        mapTalendToDbTypes.get(mappingTypeKey);
        return (List<MappingType>) mapTalendToDbTypes.get(mappingTypeKey);
    }

    public boolean isAdvicedTalendToDbType(String talendType, String dbType) {
        List<MappingType> advicedTalendToDbTypes = getAdvicedTalendToDbTypes(talendType);
        if (advicedTalendToDbTypes == null) {
            return false;
        }
        int advicedTalendToDbTypesListSize = advicedTalendToDbTypes.size();
        for (int i = 0; i < advicedTalendToDbTypesListSize; i++) {
            MappingType type = advicedTalendToDbTypes.get(i);
            if (type.getDbType().equals(dbType)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAdvicedDBToTalendType(String dbType, String talendType) {
        List<MappingType> advicedDbToTalendTypes = getAdvicedDbToTalendTypes(dbType);
        if (advicedDbToTalendTypes == null) {
            return false;
        }
        int advicedDbToTalendTypesListSize = advicedDbToTalendTypes.size();
        for (int i = 0; i < advicedDbToTalendTypesListSize; i++) {
            MappingType type = advicedDbToTalendTypes.get(i);
            if (type.getDbType().equals(dbType)) {
                return true;
            }
        }
        return false;
    }

    private boolean isExtensionLengthIgnored(String dbmsId, String dbType) {
        Map<String, Map<String, List<DBTypeUtil>>> javaTypeMappingFromExtension = JavaTypesManager
                .getJavaTypeMappingFromExtension();
        for (String id : javaTypeMappingFromExtension.keySet()) {
            for (String mappingId : javaTypeMappingFromExtension.get(id).keySet()) {
                if (dbmsId.equals(mappingId)) {
                    for (DBTypeUtil dbTypeUtil : javaTypeMappingFromExtension.get(id).get(mappingId)) {
                        if (dbTypeUtil.getDbTypeName().equals(dbType)) {
                            return dbTypeUtil.isIgnoreLength();
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isLengthIgnored(String dbmsId, String dbType) {
        if (isExtensionLengthIgnored(dbmsId, dbType)) {
            return true;
        }
        Dbms dbms = MetadataTalendType.getDbms(dbmsId);
        List ignoreLP = dbms.getIgnoreLengthPrecision();
        String ignore = new String(""); //$NON-NLS-1$
        for (int i = 0; i < ignoreLP.size(); i++) {
            DbIgnoreLengthAndPrecision dbIgnore = (DbIgnoreLengthAndPrecision) ignoreLP.get(i);
            if (dbIgnore.getDbType().equalsIgnoreCase(dbType)) {
                ignore = dbIgnore.getIgnoreLength();
                if (ignore == null) {
                    return false;
                } else if (ignore.equals("true")) { //$NON-NLS-1$
                    return true;
                } else {
                    return false;
                }
            }// end if
        }// end for
        return false;
    }

    private boolean isExtensionPrecisionIgnored(String dbmsId, String dbType) {
        Map<String, Map<String, List<DBTypeUtil>>> javaTypeMappingFromExtension = JavaTypesManager
                .getJavaTypeMappingFromExtension();
        for (String id : javaTypeMappingFromExtension.keySet()) {
            for (String mappingId : javaTypeMappingFromExtension.get(id).keySet()) {
                if (dbmsId.equals(mappingId)) {
                    for (DBTypeUtil dbTypeUtil : javaTypeMappingFromExtension.get(id).get(mappingId)) {
                        if (dbTypeUtil.getDbTypeName().equals(dbType)) {
                            return dbTypeUtil.isIgnorePrecision();
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isPrecisionIgnored(String dbmsId, String dbType) {
        if (isExtensionPrecisionIgnored(dbmsId, dbType)) {
            return true;
        }

        Dbms dbms = MetadataTalendType.getDbms(dbmsId);
        List ignoreLP = dbms.getIgnoreLengthPrecision();
        String ignore = new String(""); //$NON-NLS-1$
        for (int i = 0; i < ignoreLP.size(); i++) {
            DbIgnoreLengthAndPrecision dbIgnore = (DbIgnoreLengthAndPrecision) ignoreLP.get(i);
            if (dbIgnore.getDbType().equalsIgnoreCase(dbType)) {
                ignore = dbIgnore.getIgnorePrecision();
                if (ignore == null) {
                    return false;
                } else if (ignore.equals("true")) { //$NON-NLS-1$
                    return true;
                } else {
                    return false;
                }
            }// end if
        }// end for
        return false;
    }

    public boolean isPreBeforeLength(String dbmsId, String dbType) {
        Dbms dbms = MetadataTalendType.getDbms(dbmsId);
        List preBeforeLen = dbms.getPrebeforelength();
        String before = new String(""); //$NON-NLS-1$
        for (int i = 0; i < preBeforeLen.size(); i++) {
            DbPreBeforeLength dbPBL = (DbPreBeforeLength) preBeforeLen.get(i);
            if (dbPBL.getDbType().equals(dbType)) {
                before = dbPBL.getPreBeforeLen();
                if (before == null) {
                    return false;
                }
                if (before.equals("true")) { //$NON-NLS-1$
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 
     * Search and return the Db type which matches with the given parameters. If the Db type is not found, a new search
     * is done with inverse of given <code>nullable</code>
     * 
     * @param dbmsType
     * @param nullable
     * @return
     */
    public String getDefaultSelectedTalendType(String dbmsType) {
        if (dbmsType == null) {
            return MetadataTalendType.getDefaultTalendType();
        }
        mappingTypeKey.setDbType(dbmsType.toUpperCase());
        mappingTypeKey.setTalendType(null);
        mappingTypeKey.setDefaultSelected(Boolean.TRUE);
        MappingType mappingType = defaultMappings.get(mappingTypeKey);
        if (mappingType == null) {
            mappingTypeKey.setDefaultSelected(Boolean.FALSE);
            mappingType = defaultMappings.get(mappingTypeKey);
            if (mappingType == null) {
                return MetadataTalendType.getDefaultTalendType();
            }
        }
        return mappingType.getTalendType();
    }

    public String getDefaultSelectedTalendType(String dbmsType, int length, int precison) {
        if (dbmsType == null) {
            return MetadataTalendType.getDefaultTalendType();
        }
        mappingTypeKey.setDbType(dbmsType.toUpperCase());
        mappingTypeKey.setTalendType(null);
        List<MappingType> listMappingtype = new ArrayList<MappingType>();
        listMappingtype = (List<MappingType>) mapDbToTalendTypes.get(mappingTypeKey);
        mappingTypeKey.setDefaultSelected(Boolean.TRUE);
        MappingType mappingTypeOrigin = defaultMappings.get(mappingTypeKey);
        if (mappingTypeOrigin == null) {
            mappingTypeKey.setDefaultSelected(Boolean.FALSE);
            mappingTypeOrigin = defaultMappings.get(mappingTypeKey);
            if (mappingTypeOrigin == null) {
                return MetadataTalendType.getDefaultTalendType();
            }
        }
        IEclipsePreferences corePluginNode = new InstanceScope().getNode(ITalendCorePrefConstants.CoreUIPlugin_ID);
        if (!corePluginNode.getBoolean(ITalendCorePrefConstants.FORBIDDEN_MAPPING_LENGTH_PREC_LOGIC, false)) {
            TalendTypePreLenRetriever talendTypePre = new TalendTypePreLenRetriever(mappingTypeOrigin, length, precison);
            String mappingType = talendTypePre.getMappingType();
            if (listMappingtype != null && listMappingtype.size() != 0) {
                for (MappingType type : listMappingtype) {
                    if (type.getTalendType().equals(mappingType)) {
                        return type.getTalendType();
                    }
                }
            }
        }

        return mappingTypeOrigin.getTalendType();

    }

    /**
     * 
     * Search and return the Db type which matches with the given parameters. If the Db type is not found, a new search
     * is done with inverse of given <code>nullable</code>
     * 
     * @param talendType
     * @param nullable
     * @return
     */
    public String getDefaultSelectedDbType(String talendType) {
        mappingTypeKey.setDbType(null);
        mappingTypeKey.setTalendType(talendType);
        mappingTypeKey.setDefaultSelected(Boolean.TRUE);
        MappingType mappingType = defaultMappings.get(mappingTypeKey);
        if (mappingType == null) {
            return dbms.getDefaultDbType();
        }
        return mappingType.getDbType();

    }

    /**
     * Getter for the current loaded dbms.
     * 
     * @return the dbms
     */
    public Dbms getDbms() {
        return this.dbms;
    }

    /**
     * Sets the dbms.
     * 
     * @param dbms the dbms to set
     */
    public void setDbms(Dbms dbms) {
        this.dbms = dbms;
        init(dbms);
    }

}
