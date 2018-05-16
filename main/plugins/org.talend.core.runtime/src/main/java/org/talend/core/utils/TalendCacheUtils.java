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
package org.talend.core.utils;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.utils.system.EclipseCommandLine;
import org.talend.core.BrandingChecker;
import org.talend.core.model.utils.TalendPropertiesUtil;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public final class TalendCacheUtils {

    private static final String CLEAN_COMPONENT_CACHE = "--clean_component_cache"; //$NON-NLS-1$

    public static boolean isSetCleanComponentCache() {
        return ArrayUtils.contains(Platform.getApplicationArgs(), CLEAN_COMPONENT_CACHE);
    }

    public static boolean isSetClean() {
        return ArrayUtils.contains(Platform.getApplicationArgs(), EclipseCommandLine.CLEAN);
    }

    public static boolean cleanComponentCache() {
        return TalendPropertiesUtil.isCleanCache() || isSetCleanComponentCache() || isSetClean()
                || BrandingChecker.isBrandingChanged();
    }
}
