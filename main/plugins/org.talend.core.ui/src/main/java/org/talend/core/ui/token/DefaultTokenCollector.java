// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
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

import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.daikon.token.TokenGenerator;
import org.talend.utils.security.StudioEncryption;

import oshi.SystemInfo;
import oshi.hardware.Baseboard;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.ProcessorIdentifier;
import oshi.hardware.ComputerSystem;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
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
        return TokenGenerator.generateMachineToken((src) -> StudioEncryption.getStudioEncryption(StudioEncryption.EncryptionKeyName.SYSTEM).encrypt(src));
    }
    
    public static String hashUniqueId() {
    	return TokenGenerator.generateMachineToken((src) -> DigestUtils.sha256Hex(src));
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
        tokenStudioObject.put(UNIQUE_ID.getKey(), hashUniqueId());

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
        
        JSONObject jsonObject2 = new JSONObject();
        
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        CentralProcessor processor = hal.getProcessor();
        ProcessorIdentifier processorIdentifier = processor.getProcessorIdentifier();
        ComputerSystem cs = hal.getComputerSystem();//computer system
        Baseboard baseboard = cs.getBaseboard();//motherboard
        GlobalMemory memory = hal.getMemory();
        
        jsonObject2.put("board vendor", baseboard.getManufacturer());
        jsonObject2.put("board version", baseboard.getVersion());
        jsonObject2.put("processor", processorIdentifier.getName());
        jsonObject2.put("total memory", Math.ceil((memory.getTotal() /(1024d*1024*1024))) + "GB");
        tokenStudioObject.put("hardware", jsonObject2);

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
