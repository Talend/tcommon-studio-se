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
package org.talend.commons.ui.utils.io.archive;

import java.io.IOException;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 */
public abstract class AbstractUnarchiver {

    private long totalEntries = -1;

    private long currentEntryIndex = -1;

    public abstract void unarchive() throws IOException;

    public abstract long countEntries() throws IOException;

    public int getPercentUnarchived() throws IOException {
        if (totalEntries == -1) {
            totalEntries = countEntries();
        }
        if (this.currentEntryIndex == -1) {
            return 0;
        } else if (totalEntries < 0) {
            return 100;
        } else {
            int p = (int) ((this.currentEntryIndex + 1) * 100 / totalEntries);
            return p;
        }
    }

    protected void setCurrentEntryIndex(long currentEntryIndex) {
        this.currentEntryIndex = currentEntryIndex;
    }

}
