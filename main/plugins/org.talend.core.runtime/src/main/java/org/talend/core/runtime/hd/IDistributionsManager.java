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
package org.talend.core.runtime.hd;

/**
 * DOC ggu class global comment. Detailled comment
 */
public interface IDistributionsManager {

    /**
     * 
     * Get all Distributions for this service manager.
     */
    IHDistribution[] getDistributions();

    String[] getDistributionsDisplay(boolean withCustom);

    /**
     * 
     * Find the name of distribution.
     */
    IHDistribution getDistribution(String name, boolean byDisplay);

    /**
     * 
     * Find the version of distribution.
     */
    IHDistributionVersion getDistributionVersion(String version, boolean byDisplay);
}
