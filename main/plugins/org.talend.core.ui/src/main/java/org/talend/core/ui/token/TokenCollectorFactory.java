// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Level;
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
import org.talend.commons.utils.network.TalendProxySelector;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.ui.branding.IBrandingService;

import us.monoid.json.JSONObject;

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
                        + " (current plugin is:" + pluginName + "), will ignore this extension."); //$NON-NLS-1$ //$NON-NLS-2$
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
    
    private final int magic_mainthreadStackTraceNum = 28;
    private Thread monitorSendThread = new Thread(new Runnable() {
        public void run() {
            while (true) {
                try {
                    long millis = 12 * 60 * 60 * 1000L;//check every half day
                    millis = Long.getLong("studio.token.send", millis);
                    if (dataCollectorEnabled()) {
                        if (isTimeToSend()) {//need send
                            if(studioInIdle()) {
                                //if free, send in background
                                send(true);
                            } else {
                                Thread.sleep(10000L);
                                continue;
                            }
                        }
                    }
                    Thread.sleep(millis);
                } catch (InterruptedException e) {
                    ExceptionHandler.process(e);
                } catch(Exception e) {
                    ExceptionHandler.process(e);
                }
            }
        }

    });
    
    private boolean studioInIdle() {
        ProxyRepositoryFactory repoFactory = ProxyRepositoryFactory.getInstance();
        boolean repositoryBusy = repoFactory.isRepositoryBusy();
        
        boolean threadIdle = false;
        Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
        if (allStackTraces != null) {
            Optional<Thread> mainThread = allStackTraces.keySet().stream()
                    .filter(t -> t.isAlive() && !t.isDaemon() && t.getName().equalsIgnoreCase("main")).findFirst();
            if (mainThread.isPresent()) {
                threadIdle = (magic_mainthreadStackTraceNum >= mainThread.get().getStackTrace().length);
            }
        }
        return !repositoryBusy && threadIdle;
    }
    
    private boolean isTimeToSend() {
        Date lastDate = lastSendDate();
        final IPreferenceStore preferenceStore = CoreUIPlugin.getDefault().getPreferenceStore();
        int days = preferenceStore.getInt(ITalendCorePrefConstants.DATA_COLLECTOR_UPLOAD_PERIOD);
        Date curDate = new Date();
        Date expectedSendDate = curDate;
        if (days > 0 && lastDate != null) {
            expectedSendDate = TokenInforUtil.getDateAfter(lastDate, days);
        }
        if (expectedSendDate.compareTo(curDate) < 0) {
            return true;
        }
        
        return false;
    }
    
    public void monitor() {
        monitorSendThread.start();
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
        if (dataCollectorEnabled()) {
            //
            if (timeExpired) {
                Date lastDate = lastSendDate();
                
                final IPreferenceStore preferenceStore = CoreUIPlugin.getDefault().getPreferenceStore();
                int days = preferenceStore.getInt(ITalendCorePrefConstants.DATA_COLLECTOR_UPLOAD_PERIOD);
                long syncNb = preferenceStore.getLong(DefaultTokenCollector.COLLECTOR_SYNC_NB);
                if (syncNb < 15) {
                    days = 2;
                }
                Date curDate = new Date();
                Date addedDate = curDate;
                if (days > 0 && lastDate != null) {
                    addedDate = TokenInforUtil.getDateAfter(lastDate, days);
                }
                if (addedDate.compareTo(curDate) <= 0) {
                    return true;
                }
            } else {
                return true; // only check active
            }
        }
        return false;

    }
    
    private boolean dataCollectorEnabled() {
        final IPreferenceStore preferenceStore = CoreUIPlugin.getDefault().getPreferenceStore();
        return preferenceStore.getBoolean(ITalendCorePrefConstants.DATA_COLLECTOR_ENABLED);
    }
    
    private Date lastSendDate() {
        final IPreferenceStore preferenceStore = CoreUIPlugin.getDefault().getPreferenceStore();
        String last = preferenceStore.getString(ITalendCorePrefConstants.DATA_COLLECTOR_LAST_TIME);
        Date lastDate = null;
        if (last != null && !"".equals(last.trim())) { //$NON-NLS-1$
            // parse the last date;
            try {
                lastDate = DATE_FORMAT.parse(last);
            } catch (ParseException ee) {
                //
            }
        }
        return lastDate;
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

    public void processWithoutWait() {
        try {
            log.log(Level.INFO, "Start to collect data");
            priorCollect();
            log.log(Level.INFO, "End of collect data");
            log.log(Level.INFO, "Start to send data");
            send();
            log.log(Level.INFO, "End of send data");
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    public void send() {
        send(false);
    }

    public void send(boolean background) {
        boolean isPoweredbyTalend = false;

        if (GlobalServiceRegister.getDefault().isServiceRegistered(IBrandingService.class)) {
            IBrandingService service = GlobalServiceRegister.getDefault().getService(IBrandingService.class);
            isPoweredbyTalend = service.isPoweredbyTalend();
        }
        if (isPoweredbyTalend) {
            Job job = new Job("Initialize token") {

                @Override
                protected IStatus run(IProgressMonitor monitor) {
                    if (NetworkUtil.isNetworkValid()) {
                        try {
                            JSONObject tokenInfors = collectTokenInfors();

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            GZIPOutputStream gzos = new GZIPOutputStream(baos);
                            gzos.write(tokenInfors.toString().getBytes());
                            gzos.close();
                            byte[] data = baos.toByteArray();
                            baos.close();

                            String responseString = sendData(data);
                            String resultStr = new JSONObject(responseString).getString("result"); //$NON-NLS-1$
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

    private void addProxy(String url, HttpClientBuilder clientBuilder) throws URISyntaxException {
        TalendProxySelector proxySelector = TalendProxySelector.getInstance();
        final List<Proxy> proxyList = proxySelector.getDefaultProxySelector().select(new URI(url));
        Proxy usedProxy = null;
        if (proxyList != null && !proxyList.isEmpty()) {
            usedProxy = proxyList.get(0);
        }

        if (usedProxy != null) {
            if (!Type.DIRECT.equals(usedProxy.type())) {
                final Proxy finalProxy = usedProxy;
                InetSocketAddress address = (InetSocketAddress) finalProxy.address();
                String proxyServer = address.getHostString();
                int proxyPort = address.getPort();
                PasswordAuthentication proxyAuthentication = proxySelector.getHttpPasswordAuthentication();
                if (proxyAuthentication != null) {
                    String proxyUser = proxyAuthentication.getUserName();
                    if (StringUtils.isNotBlank(proxyUser)) {
                        String proxyPassword = "";
                        char[] passwordChars = proxyAuthentication.getPassword();
                        if (passwordChars != null) {
                            proxyPassword = new String(passwordChars);
                        }
                        BasicCredentialsProvider credProvider = new BasicCredentialsProvider();
                        credProvider.setCredentials(new AuthScope(proxyServer, proxyPort),
                                new UsernamePasswordCredentials(proxyUser, proxyPassword));
                        clientBuilder.setDefaultCredentialsProvider(credProvider);
                    }
                }
                HttpHost proxyHost = new HttpHost(proxyServer, proxyPort);
                clientBuilder.setProxy(proxyHost);
            }
        }
    }

    private String sendData(byte[] data) throws Exception {
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        try {
            final String url = "https://www.talend.com/TalendRegisterWS/tokenstudio_v2.php";

            HttpClientBuilder clientBuilder = HttpClients.custom();
            clientBuilder.disableCookieManagement();
            addProxy(url, clientBuilder);
            client = clientBuilder.build();

            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(RequestConfig.DEFAULT);

            MultipartEntityBuilder dataBuilder = MultipartEntityBuilder.create();
            dataBuilder.addPart("data", new ByteArrayBody(data, null));
            HttpEntity reqEntity = dataBuilder.build();
            httpPost.setEntity(reqEntity);

            response = client.execute(httpPost, HttpClientContext.create());
            StatusLine statusLine = response.getStatusLine();
            String responseStr = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            if (HttpURLConnection.HTTP_OK != statusLine.getStatusCode()) {
                throw new Exception(statusLine.toString() + ", server message: [" + responseStr + "]");
            }
            return responseStr;
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (Throwable e) {
                    ExceptionHandler.process(e);
                }
            }
            if (client != null) {
                try {
                    client.close();
                } catch (Throwable e) {
                    ExceptionHandler.process(e);
                }
            }
        }
    }
    
    public void reset() {
        for (TokenInforProvider tip : getProviders()) {
            ITokenCollector collector = tip.getCollector();
            if (collector != null) {
                try {
                    collector.reset();
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
        }
    }

}
