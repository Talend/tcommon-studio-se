// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

public class HashConfiguration implements IHashConfiguration {

    private List<IHashableColumn> hashableColumns;

    private IMatchingMode matchingMode;

    private boolean persistent;

    private String temporaryDataDirectory;

    private String rowsBufferSize;

    public HashConfiguration(List<IHashableColumn> hashableColumns, IMatchingMode matchingMode, boolean persistent,
            String temporaryDataDirectory, String rowsBufferSize) {
        super();
        this.hashableColumns = hashableColumns;
        this.matchingMode = matchingMode;
        this.persistent = persistent;
        this.temporaryDataDirectory = temporaryDataDirectory;
        this.rowsBufferSize = rowsBufferSize;
    }

    public List<IHashableColumn> getHashableColumns() {
        return this.hashableColumns;
    }

    public IMatchingMode getMatchingMode() {
        return this.matchingMode;
    }

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
