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
package org.talend.core.model.process;

import java.util.List;

/**
 * 
 * DOC amaumont AdvancedMapperComponent class global comment. Detailled comment <br/>
 * 
 */
public class HashConfiguration implements IHashConfiguration {

    private List<IHashableColumn> hashableColumns;

    private IMatchingMode matchingMode;

    private boolean persistent;

    private String temporaryDataDirectory;

    private String rowsBufferSize;

    /**
     * DOC amaumont HashConfigurationForMapper constructor comment.
     * 
     * @param hashableColumns
     * @param hashableColumnsIndices
     * @param matchingMode
     * @param b
     */
    public HashConfiguration(List<IHashableColumn> hashableColumns, IMatchingMode matchingMode, boolean persistent,
            String temporaryDataDirectory, String rowsBufferSize) {
        super();
        this.hashableColumns = hashableColumns;
        this.matchingMode = matchingMode;
        this.persistent = persistent;
        this.temporaryDataDirectory = temporaryDataDirectory;
        this.rowsBufferSize = rowsBufferSize;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IHashConfiguration#getHashableColumns(java.lang.String)
     */
    public List<IHashableColumn> getHashableColumns() {
        return this.hashableColumns;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IHashConfiguration#getMatchingMode()
     */
    public IMatchingMode getMatchingMode() {
        return this.matchingMode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.process.IHashConfiguration#isPersistent()
     */
    public boolean isPersistent() {
        return persistent;
    }

    public String getTemporaryDataDirectory() {
        return temporaryDataDirectory;
    }

    public String getRowsBufferSize() {
        return rowsBufferSize;
    }

}
