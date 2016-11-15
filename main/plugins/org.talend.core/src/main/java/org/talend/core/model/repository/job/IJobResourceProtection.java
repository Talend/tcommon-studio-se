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
package org.talend.core.model.repository.job;

/**
 * Add protection on the resource for prescribed job.
 */
public interface IJobResourceProtection {

    /**
     * Calculate the id of the resources need to be protected. The id of resource need add protection.
     */
    public String[] calculateProtectedIds();

    /**
     * Return the ids of protected resources. The protected id need to be removed from protection.
     */
    public String[] getProtectedIds();

    /**
     * The the job resource under the specific id.
     */
    public JobResource getJobResource(String id);
}
