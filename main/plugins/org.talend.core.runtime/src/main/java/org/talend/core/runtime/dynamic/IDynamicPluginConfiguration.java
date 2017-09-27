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
package org.talend.core.runtime.dynamic;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public interface IDynamicPluginConfiguration extends IDynamicAttribute {

    public static final String TAG_NAME = "dynamicPluginConfiguration"; //$NON-NLS-1$

    public static final String ATTR_ID = "id"; //$NON-NLS-1$

    public static final String ATTR_NAME = "name"; //$NON-NLS-1$

    public static final String ATTR_DESCRIPTION = "description"; //$NON-NLS-1$

    /**
     * get Id / Version
     * 
     * @return
     */
    public String getId();

    /**
     * set Id / Version
     * 
     * @param id
     */
    public void setId(String id);

    /**
     * get display name
     * 
     * @return
     */
    public String getName();

    /**
     * set display name
     * 
     * @param name
     */
    public void setName(String name);

    public String getDescription();

    public void setDescription(String description);

    public String toXmlString() throws Exception;

}
