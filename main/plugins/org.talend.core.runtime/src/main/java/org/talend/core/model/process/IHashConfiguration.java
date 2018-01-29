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
package org.talend.core.model.process;

import java.util.List;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 */
public interface IHashConfiguration {

    public List<IHashableColumn> getHashableColumns();

    public IMatchingMode getMatchingMode();

    public boolean isPersistent();

    public String getTemporaryDataDirectory();

    public String getRowsBufferSize();

}
