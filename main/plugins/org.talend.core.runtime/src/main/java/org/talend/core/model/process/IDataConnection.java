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

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public interface IDataConnection extends IConnection {

    /**
     * Only filled if the original link is a from lookup (to tell which is tMap linked for example).
     * 
     * @return the linkedNodeForHash
     */
    public INode getLinkNodeForHash();

    /**
     * Only filled if the original link is a from lookup (to tell which is tMap linked for example).
     * 
     * @param linkedNodeForHash the linkedNodeForHash to set
     */
    public void setLinkNodeForHash(INode linkedNodeForHash);
}
