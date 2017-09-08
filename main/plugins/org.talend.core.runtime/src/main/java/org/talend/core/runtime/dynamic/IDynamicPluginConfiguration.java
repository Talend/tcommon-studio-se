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

import java.util.List;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public interface IDynamicPluginConfiguration extends IDynamicAttribute {

    public static final String TAG_NAME = "dynamicPluginConfiguration"; //$NON-NLS-1$

    /**
     * Unique id which should be stored in items so that dynamic service can be got by this id
     */
    public static final String ATTR_ID = "id"; //$NON-NLS-1$

    public static final String ATTR_NAME = "name"; //$NON-NLS-1$

    /**
     * Only a property to let us know which version it is
     */
    public static final String ATTR_VERSION = "version"; //$NON-NLS-1$

    public static final String ATTR_DESCRIPTION = "description"; //$NON-NLS-1$

    public static final String ATTR_DISTRIBUTION = "distribution"; //$NON-NLS-1$

    public static final String ATTR_REPOSITORY = "repository"; //$NON-NLS-1$

    public static final String ATTR_SERVICES = "services"; //$NON-NLS-1$

    public String getId();

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

    public String getVersion();

    public void setVersion(String version);

    public String getDescription();

    public void setDescription(String description);

    public String getDistribution();

    public void setDistribution(String destribution);

    public String getRepository();

    public void setRepository(String repository);

    public List<String> getServices();

    public void setServices(List<String> services);

    public String toXmlString() throws Exception;

}
