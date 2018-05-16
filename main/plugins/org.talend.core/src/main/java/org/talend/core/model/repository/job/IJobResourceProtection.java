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
package org.talend.core.model.repository.job;

/**
 * Add protection on the resource for prescribed job.
 * 
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: IResourceProtection.java 下午03:39:51 2007-7-6 +0000 (2007-7-6) yzhang $
 * 
 */
public interface IJobResourceProtection {

    /**
     * Calculate the id of the resources need to be protected. The id of resource need add protection.
     * 
     * yzhang Comment method "getProtectionIds".
     * 
     * @return
     */
    public String[] calculateProtectedIds();

    /**
     * Return the ids of protected resources. The protected id need to be removed from protection.
     * 
     * yzhang Comment method "getProjectedIds".
     * 
     * @return
     */
    public String[] getProtectedIds();

    /**
     * The the job resource under the specific id.
     * 
     * yzhang Comment method "getJobResource".
     * 
     * @return
     */
    public JobResource getJobResource(String id);
}
