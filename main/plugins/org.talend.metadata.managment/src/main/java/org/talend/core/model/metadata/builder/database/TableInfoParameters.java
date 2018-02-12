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
package org.talend.core.model.metadata.builder.database;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase.ETableTypes;
import org.talend.core.model.metadata.builder.database.manager.dbs.OracleExtractManager;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 */
public class TableInfoParameters {

    public static final String DEFAULT_FILTER = "%"; //$NON-NLS-1$

    public static final String ORACLE_10G_RECBIN_SQL = OracleExtractManager.ORACLE_10G_RECBIN_SQL;

    private boolean usedName = true;

    private final Set<String> nameFiters;

    /**
     * DOC qzhang TableInfoParameters constructor comment.
     */
    public TableInfoParameters() {
        types = new ArrayList<ETableTypes>();
        nameFiters = new HashSet<String>();
        types.add(ETableTypes.TABLETYPE_TABLE);
        types.add(ETableTypes.TABLETYPE_VIEW);
        types.add(ETableTypes.TABLETYPE_SYNONYM);
    }

    private final List<ETableTypes> types;

    private String sqlFiter;

    /**
     * Getter for types.
     * 
     * @return the types
     */
    public List<ETableTypes> getTypes() {
        return this.types;
    }

    /**
     * Sets the types.
     * 
     * @param types the types to set
     */
    private void addTypes(ETableTypes type) {
        if (!types.contains(type)) {
            types.add(type);
        }
    }

    private void removeTypes(ETableTypes type) {
        types.remove(type);
    }

    public void changeType(ETableTypes type, boolean add) {
        if (add) {
            addTypes(type);
        } else {
            removeTypes(type);
        }
    }

    /**
     * Getter for nameFiter.
     * 
     * @return the nameFiter
     */
    public Set<String> getNameFilters() {
        return this.nameFiters;
    }

    public void setNameFilters(String[] nameFilters) {
        nameFiters.clear();
        for (String string : nameFilters) {
            addNameFiter(string);
        }
    }

    /**
     * Sets the nameFiter.
     * 
     * @param nameFiter the nameFiter to set
     */
    private void addNameFiter(String nameFiter) {
        if (nameFiters.contains(DEFAULT_FILTER)) {
            return;
        }
        if (DEFAULT_FILTER.equals(nameFiter)) {
            nameFiters.clear();
        }
        nameFiters.add(nameFiter);
    }

    /**
     * Getter for sqlFiter.
     * 
     * @return the sqlFiter
     */
    public String getSqlFiter() {
        return this.sqlFiter;
    }

    /**
     * Sets the sqlFiter.
     * 
     * @param sqlFiter the sqlFiter to set
     */
    public void setSqlFiter(String sqlFiter) {
        this.sqlFiter = sqlFiter;
    }

    /**
     * Getter for usedName.
     * 
     * @return the usedName
     */
    public boolean isUsedName() {
        return this.usedName;
    }

    /**
     * Sets the usedName.
     * 
     * @param usedName the usedName to set
     */
    public void setUsedName(boolean usedName) {
        this.usedName = usedName;
    }

}
