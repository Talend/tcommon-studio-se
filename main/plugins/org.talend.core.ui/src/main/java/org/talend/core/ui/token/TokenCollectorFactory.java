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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Authenticator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.network.NetworkUtil;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.ui.branding.IBrandingService;

import us.monoid.json.JSONObject;
import us.monoid.web.AbstractContent;
import us.monoid.web.FormData;
import us.monoid.web.Resty;
import us.monoid.web.TextResource;
import us.monoid.web.mime.MultipartContent;

/**
 * ggu class global comment. Detailled comment
 */
public final class TokenCollectorFactory {

    private static Logger log = Logger.getLogger(TokenCollectorFactory.class);

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$

    private static TokenCollectorFactory factory;

    private static final Map<String, TokenInforProvider> providers;

    static {
        providers = new HashMap<String, TokenInforProvider>();

        Map<String, String> idWithPluginMap = new HashMap<String, String>();
        //
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IConfigurationElement[] configurationElements = registry
                .getConfigurationElementsFor("org.talend.core.runtime.tokenInfo_provider"); //$NON-NLS-1$
        for (IConfigurationElement ce : configurationElements) {
            String pluginName = ce.getContributor().getName();

            String id = ce.getAttribute("id"); //$NON-NLS-1$
            String name = ce.getAttribute("name"); //$NON-NLS-1$
            String description = ce.getAttribute("description"); //$NON-NLS-1$
            String productType = ce.getAttribute("productType"); //$NON-NLS-1$
            ITokenCollector collector = null;
            try {
                collector = (ITokenCollector) ce.createExecutableExtension("collector"); //$NON-NLS-1$
            } catch (CoreException e) {
                log.log(Priority.ERROR, "Can't create the collector for id: " + id + " in plugin:" + pluginName, e); //$NON-NLS-1$ //$NON-NLS-2$
            }

            TokenInforProvider provider = new TokenInforProvider(id, name, description, productType, collector);
            if (!providers.containsKey(id)) {
                providers.put(id, provider);
                idWithPluginMap.put(id, pluginName);
            } else {
                log.log(Priority.WARN, "there is  id: " + id + " to have been existed in plugin:" + idWithPluginMap.get(id) //$NON-NLS-1$ //$NON-NLS-2$
                        + " （current plugin is:" + pluginName + "）， will ignore this extension."); //$NON-NLS-1$ //$NON-NLS-2$
            }

        }
    }

    public static TokenCollectorFactory getFactory() {
        if (factory == null) {
            factory = new TokenCollectorFactory();
        }
        return factory;
    }

    public TokenInforProvider[] getProviders() {
        return providers.values().toArray(new TokenInforProvider[0]);
    }

    public void priorCollect() throws Exception {
        if (isActiveAndValid(false)) { //
            for (TokenInforProvider tip : getProviders()) {
                try {
                    ITokenCollector collector = tip.getCollector();
                    if (collector != null) {
                        collector.priorCollect();
                    }
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
        }
    }

    public JSONObject collectTokenInfors() throws Exception {
        JSONObject result = new JSONObject();

        for (TokenInforProvider tip : getProviders()) {
            ITokenCollector collector = tip.getCollector();
            if (collector != null) {
                try {
                    JSONObject collectionObject = collector.collect();
                    if (collectionObject != null) {
                        TokenInforUtil.mergeJSON(collectionObject, result);
                    }
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
        }
        return result;
    }

    private boolean isActiveAndValid(boolean timeExpired) {
        final IPreferenceStore preferenceStore = CoreUIPlugin.getDefault().getPreferenceStore();
        if (preferenceStore.getBoolean(ITalendCorePrefConstants.DATA_COLLECTOR_ENABLED)) {
            String last = preferenceStore.getString(ITalendCorePrefConstants.DATA_COLLECTOR_LAST_TIME);
            int days = preferenceStore.getInt(ITalendCorePrefConstants.DATA_COLLECTOR_UPLOAD_PERIOD);
            
            long syncNb = preferenceStore.getLong(DefaultTokenCollector.COLLECTOR_SYNC_NB);
            if (syncNb < 15) {
                days = 2;
            }
            Date lastDate = null;
            if (last != null && !"".equals(last.trim())) { //$NON-NLS-1$
                // parse the last date;
                try {
                    lastDate = DATE_FORMAT.parse(last);
                } catch (ParseException ee) {
                    //
                }
            }
            Date curDate = new Date();
            Date addedDate = curDate;
            if (days > 0 && lastDate != null) {
                addedDate = TokenInforUtil.getDateAfter(lastDate, days);
            }
            //
            if (timeExpired) {
                if (addedDate.compareTo(curDate) <= 0) {
                    return true;
                }
            } else {
                return true; // only check active
            }
        }
        return false;

    }

    public boolean process() {
        boolean result = false;

        // collect
        try {
            if (isActiveAndValid(false)) {
                // collect the data each time, if the token is active
                TokenCollectorFactory.getFactory().priorCollect();
            }
            if (isActiveAndValid(true)) {
                send();
                result = true;
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }

        return result;
    }

    public void send() {
        send(false);
    }

    public void send(boolean background) {
        boolean isPoweredbyTalend = false;

        if (GlobalServiceRegister.getDefault().isServiceRegistered(IBrandingService.class)) {
            IBrandingService service = (IBrandingService) GlobalServiceRegister.getDefault().getService(IBrandingService.class);
            isPoweredbyTalend = service.isPoweredbyTalend();
        }
        if (isPoweredbyTalend) {
            Job job = new Job("Initialize token") {

                @Override
                protected IStatus run(IProgressMonitor monitor) {
                    if (NetworkUtil.isNetworkValid()) {
                        Authenticator defaultAuth = NetworkUtil.getDefaultAuthenticator();
                        try {
                            JSONObject tokenInfors = collectTokenInfors();
                            Resty r = new Resty();
                            // set back the rath for Resty.
                            Field rathField = Resty.class.getDeclaredField("rath"); //$NON-NLS-1$
                            rathField.setAccessible(true);
                            Authenticator auth = (Authenticator) rathField.get(null);
                            Authenticator.setDefault(auth);

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            GZIPOutputStream gzos = new GZIPOutputStream(baos);
                            gzos.write(tokenInfors.toString().getBytes());
                            gzos.close();
                            AbstractContent ac = Resty.content(baos.toByteArray());
                            baos.close();
                            MultipartContent mpc = Resty.form(new FormData("data", ac)); //$NON-NLS-1$

                            TextResource result = r.text("https://www.talend.com/TalendRegisterWS/tokenstudio_v2.php", mpc); //$NON-NLS-1$
                            String resultStr = new JSONObject(result.toString()).getString("result"); //$NON-NLS-1$
                            boolean okReturned = (resultStr != null && resultStr.endsWith("OK")); //$NON-NLS-1$
                            if (okReturned) {
                                // set new days
                                final IPreferenceStore preferenceStore = CoreUIPlugin.getDefault().getPreferenceStore();
                                preferenceStore.setValue(ITalendCorePrefConstants.DATA_COLLECTOR_LAST_TIME, DATE_FORMAT.format(new Date()));
                                if (preferenceStore instanceof ScopedPreferenceStore) {
                                    try {
                                        ((ScopedPreferenceStore) preferenceStore).save();
                                    } catch (IOException e) {
                                        ExceptionHandler.process(e);
                                    }
                                }
                                long syncNb = preferenceStore.getLong(DefaultTokenCollector.COLLECTOR_SYNC_NB);
                                syncNb++;
                                preferenceStore.setValue(DefaultTokenCollector.COLLECTOR_SYNC_NB, syncNb);
                            }
                        } catch (Exception e) {
                            ExceptionHandler.process(e);
                        } finally {
                            Authenticator.setDefault(defaultAuth);
                        }
                    }
                    return org.eclipse.core.runtime.Status.OK_STATUS;
                }

            };
            job.setUser(false);
            job.setSystem(true);
            job.setPriority(Job.LONG);
            job.schedule();
            job.wakeUp(); // start as soon as possible
            if (!background) {
                try {
                    job.join();
                } catch (InterruptedException e) {
                    // nothing
                }
            }
        }
    }
}
