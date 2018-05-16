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
package org.talend.designer.runprocess;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 */
public interface IProcessMessage {

    /**
     * Getter for content.
     * 
     * @return the content
     */
    public String getContent();

    /**
     * Getter for type.
     * 
     * @return the type
     */
    public IMsgType getType();

}
