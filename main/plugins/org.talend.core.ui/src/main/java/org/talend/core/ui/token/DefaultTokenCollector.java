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
package org.talend.core.ui.token;

import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.daikon.security.CryptoHelper;
import org.talend.daikon.token.TokenGenerator;

import us.monoid.json.JSONObject;

/**
 * ggu class global comment. Detailled comment
 */
public class DefaultTokenCollector extends AbstractTokenCollector {

    private static final TokenKey VERSION = new TokenKey("version"); //$NON-NLS-1$

    private static final TokenKey UNIQUE_ID = new TokenKey("uniqueId"); //$NON-NLS-1$

    private static final TokenKey TYPE_STUDIO = new TokenKey("studio.type"); //$NON-NLS-1$

    private static final TokenKey STOP_COLLECTOR = new TokenKey("stop.collection"); //$NON-NLS-1$
    
    private static final TokenKey SYNC_NB = new TokenKey("sync.nb"); //$NON-NLS-1$

    private static final TokenKey OS = new TokenKey("os"); //$NON-NLS-1$

    public static final String COLLECTOR_SYNC_NB = "COLLECTOR_SYNC_NB"; //$NON-NLS-1$

    public DefaultTokenCollector() {
        super();
    }

    public static String calcUniqueId() {
        return TokenGenerator.generateMachineToken(new CryptoHelper(CryptoHelper.PASSPHRASE));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.token.AbstractTokenCollector#collect()
     */
    @Override
    public JSONObject collect() throws Exception {
        JSONObject tokenStudioObject = new JSONObject();
        // version
        tokenStudioObject.put(VERSION.getKey(), VersionUtils.getInternalVersion());
        // uniqueId
        tokenStudioObject.put(UNIQUE_ID.getKey(), calcUniqueId());

        // typeStudio
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IBrandingService.class)) {
            IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                    IBrandingService.class);
            tokenStudioObject.put(TYPE_STUDIO.getKey(), brandingService.getAcronym());
            // tokenStudioObject.put(TYPE_STUDIO.getKey(), brandingService.getShortProductName());
        }
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("os.name", System.getProperty("os.name"));
        jsonObject.put("os.arch", System.getProperty("os.arch"));
        jsonObject.put("os.version", System.getProperty("os.version"));
        tokenStudioObject.put(OS.getKey(), jsonObject);

        final IPreferenceStore preferenceStore = CoreUIPlugin.getDefault().getPreferenceStore();
        long syncNb = preferenceStore.getLong(COLLECTOR_SYNC_NB);
        tokenStudioObject.put(SYNC_NB.getKey(), syncNb);

        if (!preferenceStore.getBoolean(ITalendCorePrefConstants.DATA_COLLECTOR_ENABLED)) {
            tokenStudioObject.put(STOP_COLLECTOR.getKey(), "1"); //$NON-NLS-1$
        } else {
            tokenStudioObject.put(STOP_COLLECTOR.getKey(), "0"); //$NON-NLS-1$
        }
        return tokenStudioObject;
    }
}
