// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.librariesmanager.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.network.NetworkUtil;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.ModuleToInstall;
import org.talend.core.nexus.NexusServerBean;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.designer.maven.talendlib.TalendLibsServerManager;
import org.talend.librariesmanager.ui.i18n.Messages;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;

/**
 * created by Administrator on 2012-9-19 Detailled comment
 * 
 */
public class RemoteModulesHelper {

    private static final String SLASH = "/"; //$NON-NLS-1$ 

    // TODO to be removed after nexus server available
    public static final boolean nexus_available = true;

    // true if user was warned the network connection is not possible
    static private boolean alreadyWarnedAboutConnectionIssue = false;

    /**
     * created by sgandon on 24 sept. 2013 Detailled comment
     * 
     */
    private final class RemoteModulesFetchRunnable implements IRunnableWithProgress {

        private final boolean collectModulesWithJarName;

        /**
         * 
         */
        private final List<ModuleToInstall> toInstall;

        /**
         * 
         */
        private final Map<String, List<ModuleNeeded>> contextMap;

        /**
         * 
         * DOC wchen RemoteModulesFetchRunnable constructor comment.
         * 
         * @param requiredModules a map with key=mvnuri , value =list of ModuleNeeded with the same mvnuri
         * @param toInstall
         */
        private RemoteModulesFetchRunnable(Map<String, List<ModuleNeeded>> requiredModules, List<ModuleToInstall> toInstall,
                boolean collectModulesWithJarName) {
            this.toInstall = toInstall;
            this.contextMap = requiredModules;
            this.collectModulesWithJarName = collectModulesWithJarName;
        }

        @Override
        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
            final Set<String> mavenUriSet = contextMap.keySet();
            Map<String, MavenArtifact> requiredArtifacts = new HashMap<String, MavenArtifact>();
            Set<String> unavailableModules = new HashSet<String>();
            monitor.beginTask(Messages.getString("RemoteModulesHelper.fetch.module.info"), mavenUriSet.size() * 10 + 10);//$NON-NLS-1$
            // if the network is not valid, all jars are not available.
            boolean networkValid = NetworkUtil.isNetworkValid();
            if (!networkValid) {
                unavailableModules.addAll(mavenUriSet);
                if (!alreadyWarnedAboutConnectionIssue) {
                    log.warn("failed to connect to internet");
                    alreadyWarnedAboutConnectionIssue = true;
                }// else already warned so do nothing
            }
            if (networkValid) {
                try {
                    int index = 0;
                    int limit = 100;
                    final Iterator<String> iterator = mavenUriSet.iterator();
                    while (index < mavenUriSet.size()) {
                        // get block of 100 jars
                        String jarsToCheck = "";
                        while (index < limit && index < mavenUriSet.size()) {
                            index++;
                            String uriToCheck = iterator.next();
                            final MavenArtifact parseMvnUrl = MavenUrlHelper.parseMvnUrl(uriToCheck);
                            if (parseMvnUrl != null) {
                                requiredArtifacts.put(uriToCheck, parseMvnUrl);
                                jarsToCheck += parseMvnUrl.getArtifactId();
                                if (index < limit && index < mavenUriSet.size()) {
                                    jarsToCheck += ",";
                                }
                            }
                        }
                        limit += 100;
                        TalendLibsServerManager manager = TalendLibsServerManager.getInstance();
                        NexusServerBean nexusServer = manager.getLibrariesNexusServer();

                        List<MavenArtifact> searchResults = manager.search(nexusServer.getServer(), nexusServer.getUserName(),
                                nexusServer.getPassword(), nexusServer.getRepositoryId(), MavenConstants.DEFAULT_LIB_GROUP_ID,
                                jarsToCheck, null);
                        monitor.worked(10);
                        for (MavenArtifact artifact : searchResults) {
                            String artifactId = artifact.getArtifactId();
                            String packageName = artifact.getType();
                            if (packageName == null) {
                                packageName = MavenConstants.TYPE_JAR;
                            }
                            String version = artifact.getVersion();
                            String description = artifact.getDescription();
                            String license = artifact.getLicense();
                            String license_url = artifact.getLicenseUrl();
                            String packaging = artifact.getType();
                            String url = null;
                            if (artifact.getUrl() != null && !"".equals(artifact.getUrl())) {
                                url = artifact.getUrl();
                            }
                            ModuleToInstall m = new ModuleToInstall();
                            m.setName(artifactId + "." + packageName);
                            String mvnUri = MavenUrlHelper.generateMvnUrl(artifact.getGroupId(), artifactId, version, packaging,
                                    artifact.getClassifier());
                            m.setMavenUri(mvnUri);
                            m.setLicenseType(license);
                            m.setLicenseUrl(license_url);
                            m.setDescription(description);
                            m.setUrl_description(url);
                            m.setUrl_download(url);
                            if (artifact.getType() == null
                                    || "".equals(artifact.getType()) || MavenConstants.PACKAGING_POM.equals(artifact.getType())) { //$NON-NLS-1$
                                m.setDistribution(MavenConstants.DOWNLOAD_MANUAL);
                            } else {
                                m.setDistribution(artifact.getType());
                            }

                            cache.put(mvnUri, m);

                            monitor.worked(10);
                        }

                    }

                } catch (Exception e1) {
                    ExceptionHandler.process(e1);
                }
                for (String uri : requiredArtifacts.keySet()) {
                    String mvnUri = uri;
                    // incase the package is not set int mavenuri
                    final MavenArtifact artifact = requiredArtifacts.get(mvnUri);
                    if (artifact.getType() == null) {
                        artifact.setType(MavenConstants.TYPE_JAR);
                    }
                    mvnUri = MavenUrlHelper.generateMvnUrl(artifact.getGroupId(), artifact.getArtifactId(),
                            artifact.getVersion(), artifact.getType(), artifact.getClassifier());
                    ModuleToInstall moduleToInstall = cache.get(mvnUri);
                    if (moduleToInstall != null) {
                        setContext(moduleToInstall, uri, contextMap);
                        toInstall.add(moduleToInstall);
                    } else {
                        String artifactId = artifact.getArtifactId();
                        String type = artifact.getType();
                        if (type == null) {
                            type = MavenConstants.TYPE_JAR;
                        }
                        String name = artifactId + "." + type;
                        unavailableModules.add(uri);
                        ExceptionHandler.log("The download URL for " + name + " is not available");//$NON-NLS-1$//$NON-NLS-2$
                        if (CommonsPlugin.isDebugMode()) {
                            appendToLogFile(name + "\n");
                        }
                    }
                    monitor.worked(10);
                }
            }

            addUnavailableModulesToToBeInstalledModules(unavailableModules, toInstall, contextMap);

            if (collectModulesWithJarName) {
                collectModulesWithJarName(toInstall);
            }

            monitor.done();
        }
    }

    /**
     * DOC sgandon Comment method "addUnavailableModulesToToBeInstalledModules".
     * 
     * @param unavailableModules
     * @param toInstall2
     * @param contextMap
     */
    protected void addUnavailableModulesToToBeInstalledModules(Set<String> unavailableModules, List<ModuleToInstall> toInstall2,
            Map<String, List<ModuleNeeded>> contextMap) {
        // add all unavailable modules, cause they need to be installed even if the are not available from remote
        // site.
        for (String mvnUri : unavailableModules) {
            ModuleToInstall m = createUnavailableModuleToInstall(mvnUri, contextMap);
            toInstall2.add(m);
        }
    }

    /**
     * DOC sgandon Comment method "createUnavailableModuleToInstall".
     * 
     * @param unavailableModuleName
     * @param contextMap, may be null
     * @return
     */
    private ModuleToInstall createUnavailableModuleToInstall(String mvnUri, Map<String, List<ModuleNeeded>> contextMap) {
        ModuleToInstall m = new ModuleToInstall();
        m.setDistribution(MavenConstants.DOWNLOAD_MANUAL);
        setContext(m, mvnUri, contextMap);
        if (contextMap != null) {
            final List<ModuleNeeded> neededModules = contextMap.get(mvnUri);
            m.setName(neededModules.get(0).getModuleName());
            m.setDescription(getFirstDescription(neededModules));
        } else {
            final MavenArtifact parseMvnUrl = MavenUrlHelper.parseMvnUrl(mvnUri);
            String name = parseMvnUrl.getArtifactId();
            String type = parseMvnUrl.getType();
            if (type == null) {
                type = MavenConstants.TYPE_JAR;
            }
            m.setName(name + "." + type);

        }

        return m;
    }

    /**
     * DOC sgandon Comment method "setContext".
     * 
     * @param m
     * @param contextMap
     */
    protected void setContext(ModuleToInstall m, String mvnUri, Map<String, List<ModuleNeeded>> contextMap) {
        if (contextMap != null) {
            List<ModuleNeeded> nm = contextMap.get(mvnUri);
            final String context = getContext(nm);
            if (context == null || "".equals(context)) {
                m.setContext("Current Operation");//$NON-NLS-1$
            } else {
                m.setContext(context);
            }
            m.setRequired(isRequired(nm));
        } else {
            m.setContext("Current Operation");//$NON-NLS-1$
            m.setRequired(true);
        }
    }

    /**
     * DOC sgandon Comment method "getDescription".
     * 
     * @param nm
     * @return
     */
    private String getFirstDescription(List<ModuleNeeded> neededModules) {
        if (neededModules == null) {
            return ""; //$NON-NLS-1$
        }
        for (ModuleNeeded module : neededModules) {
            if (module.getInformationMsg() != null && !"".equals(module.getInformationMsg())) { //$NON-NLS-1$
                return module.getInformationMsg();
            }
        }
        return ""; //$NON-NLS-1$

    }

    private void appendToLogFile(String logTxt) {
        Path absolutePath = new Path(Platform.getInstallLocation().getURL().getPath());
        File fullLogFile = new File(absolutePath.append("NotAvailableJarsFromNexus.txt").toPortableString());

        FileWriter writer = null;
        try {
            writer = new FileWriter(fullLogFile, true);
            writer.append(logTxt);
        } catch (IOException e) {
            // nothing
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
        }

    }

    private static Logger log = Logger.getLogger(RemoteModulesHelper.class);

    private static RemoteModulesHelper helper;

    // private static final String serviceUrl = "http://www.talend.com/TalendRegisterWS/modules.php";

    private static final String serviceUrl = "http://www.talendforge.org/modules/webservices/modules.php"; //$NON-NLS-1$

    private static final String SEPARATOR_DISPLAY = " | "; //$NON-NLS-1$

    private static final String SEPARATOR = "|"; //$NON-NLS-1$

    private static final String SEPARATOR_SLIP = "\\|"; //$NON-NLS-1$

    /**
     * key : mvnuri(without SANPSHOT in version) value : ModuleToInstall
     */
    private Map<String, ModuleToInstall> cache = new HashMap<String, ModuleToInstall>();

    private RemoteModulesHelper() {
    }

    public synchronized static RemoteModulesHelper getInstance() {
        if (helper == null) {
            helper = new RemoteModulesHelper();
        }
        return helper;
    }

    public RemoteModulesFetchRunnable createRemoteModuleFetchRunnable(final Map<String, List<ModuleNeeded>> requiredModules,
            final List<ModuleToInstall> toInstall, boolean collectModulesWithJarName) {
        return new RemoteModulesFetchRunnable(requiredModules, toInstall, collectModulesWithJarName);
    }

    private JSONObject readJsonFromUrl(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        String jsonText = "";
        JSONObject json = null;
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));//$NON-NLS-1$
            jsonText = readAll(rd);
            json = new JSONObject(jsonText);
        } catch (Exception e) {
            ExceptionHandler.process(new Exception(Messages.getString("RemoteModulesHelper.readJsonFromUrl.error")));
        } finally {
            is.close();
        }
        return json;
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private String getContext(List<ModuleNeeded> neededModules) {
        if (neededModules == null) {
            return "";
        }
        StringBuffer context = new StringBuffer();
        for (ModuleNeeded module : neededModules) {
            if (context.length() != 0) {
                context.append(SEPARATOR_DISPLAY);
            }
            if (module.getContext() != null) {
                context.append(module.getContext());
            }
        }
        return context.toString();

    }

    private boolean isRequired(List<ModuleNeeded> neededModules) {
        if (neededModules == null) {
            return false;
        }
        boolean isRequired = false;
        for (ModuleNeeded module : neededModules) {
            isRequired = isRequired | module.isRequired();
            if (isRequired) {
                return isRequired;
            }
        }
        return isRequired;
    }

    public String getLicenseUrl(String licenseType) {
        JSONObject message = new JSONObject();
        try {
            JSONObject child = new JSONObject();
            child.put("vaction", "getLicense");//$NON-NLS-1$
            child.put("label", licenseType);//$NON-NLS-1$
            message.put("module", child);//$NON-NLS-1$
            String url = serviceUrl + "?data=" + message;
            JSONObject resultStr = readJsonFromUrl(url);
            JSONArray jsonArray = resultStr.getJSONArray("result");//$NON-NLS-1$
            if (jsonArray != null) {
                JSONObject object = jsonArray.getJSONObject(0);
                if (object != null) {
                    return object.getString("licenseText");//$NON-NLS-1$
                }
            }
        } catch (JSONException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    /**
     * return a runnable to fetch remote modules or null if no nothing need be fetched, this means that toInstall array
     * is filled with existing modules found in cache. if module do not have mvnuri ,try to download module with all
     * mvnuris found form index in background.
     * 
     * @param collectModulesWithJarName if<true> then collect the modules with jarname and fill all mvnuri to
     * ModuleToInstall.mavenUris set to download all needed versions
     * */
    public RemoteModulesFetchRunnable getNotInstalledModulesRunnable(List<ModuleNeeded> neededModules,
            List<ModuleToInstall> toInstall, boolean collectModulesWithJarName) {
        Map<String, List<ModuleNeeded>> contextMap = new HashMap<String, List<ModuleNeeded>>();

        final Iterator<ModuleNeeded> iterator = neededModules.iterator();
        while (iterator.hasNext()) {
            ModuleNeeded module = iterator.next();
            String mvnUri = module.getMavenUri();
            if (mvnUri == null) {
                Set<String> configuredSnapshotMvnUris = new HashSet<String>();
                ILibraryManagerService librairesManagerService = (ILibraryManagerService) GlobalServiceRegister.getDefault()
                        .getService(ILibraryManagerService.class);
                final String mavenUriFromIndex = librairesManagerService.getMavenUriFromIndex(module.getModuleName());
                if (mavenUriFromIndex != null) {
                    final String[] split = mavenUriFromIndex.split(MavenUrlHelper.MVN_INDEX_SPLITER);
                    for (String fromIndex : split) {
                        configuredSnapshotMvnUris.add(fromIndex);
                    }
                }
                if (configuredSnapshotMvnUris.isEmpty()) {
                    mvnUri = module.getMavenUri(true);
                } else {
                    // add all mvnuris from index to try to download
                    for (String snapshotUri : configuredSnapshotMvnUris) {
                        String uri = MavenUrlHelper.generateUriFromSnapshot(snapshotUri);
                        if (uri != null) {
                            ModuleNeeded newModule = new ModuleNeeded(null, module.getModuleName(), null, true);
                            newModule.setMavenUri(uri);
                            if (!contextMap.keySet().contains(uri)) {
                                List<ModuleNeeded> modules = new ArrayList<ModuleNeeded>();
                                modules.add(module);
                                contextMap.put(uri, modules);
                            } else {
                                contextMap.get(uri).add(module);
                            }
                        }
                    }
                }
            }
            if (mvnUri != null) {
                if (!contextMap.keySet().contains(mvnUri)) {
                    List<ModuleNeeded> modules = new ArrayList<ModuleNeeded>();
                    modules.add(module);
                    contextMap.put(mvnUri, modules);
                } else {
                    contextMap.get(mvnUri).add(module);
                }
            }
        }
        Set<String> contextKeys = new HashSet<String>(contextMap.keySet());
        for (String mvnUri : contextKeys) {
            ModuleToInstall moduleToInstall = null;
            moduleToInstall = cache.get(mvnUri);
            if (moduleToInstall != null) {
                List<ModuleNeeded> moduleContext = contextMap.get(mvnUri);
                setContext(moduleToInstall, mvnUri, contextMap);
                if (moduleContext != null && moduleContext.size() > 0) {
                    for (ModuleNeeded needed : moduleContext) {
                        if (moduleToInstall.getName().equals(needed.getModuleName())) {
                            moduleToInstall.setRequired(needed.isRequired());
                        }
                    }
                }
                toInstall.add(moduleToInstall);
                contextMap.remove(mvnUri);
            }
        }

        if (contextMap.isEmpty()) {
            if (collectModulesWithJarName) {
                collectModulesWithJarName(toInstall);
            }
            return null;
        }

        // fetch the jars which are not in cache.
        return createRemoteModuleFetchRunnable(contextMap, toInstall, collectModulesWithJarName);

    }

    public RemoteModulesFetchRunnable getNotInstalledModulesRunnable(List<ModuleNeeded> neededModules,
            List<ModuleToInstall> toInstall) {
        return getNotInstalledModulesRunnable(neededModules, toInstall, false);
    }

    /**
     * 
     * collect all vesions to one ModuleToInstall to don't display several times for one jar in the dialog if need to
     * download all versions
     * 
     */
    public void collectModulesWithJarName(List<ModuleToInstall> toInstall) {
        // TODO????? if one jar with all mvnuris are on nexus server ,we will download all versions in background.
        // there might be problem of display on dialog when some version of mvnuri can't download, example :
        // groupid/jarA/6.1.0 --- on offical server -- show with download button
        // groupid/jarA/6.1.0 --- not on server -- it will show as DOWNLOAD_MANUAL
        List<ModuleToInstall> manualInstall = new ArrayList<ModuleToInstall>();
        Map<String, ModuleToInstall> nameAndModuleMap = new HashMap<String, ModuleToInstall>();
        for (ModuleToInstall module : toInstall) {
            if (MavenConstants.DOWNLOAD_MANUAL.equals(module.getDistribution())) {
                manualInstall.add(module);
            } else if (module.getMavenUri() != null) {
                final ModuleToInstall moduleToInstall = nameAndModuleMap.get(module.getName());
                if (moduleToInstall != null) {
                    moduleToInstall.getMavenUris().add(module.getMavenUri());
                } else {
                    module.getMavenUris().add(module.getMavenUri());
                    nameAndModuleMap.put(module.getName(), module);
                }
            }
        }
        toInstall.clear();
        toInstall.addAll(nameAndModuleMap.values());
        toInstall.addAll(manualInstall);
    }

    public String getLicenseContentByUrl(String licenseUrl) {
        if (licenseUrl != null && licenseUrl.length() > 0) {
            try {
                URL url = new URL(licenseUrl);
                URLConnection urlConnection = url.openConnection();
                Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
                if (headerFields != null) {
                    List<String> contentType = headerFields.get("Content-Type"); //$NON-NLS-1$
                    if (contentType != null) {
                        if (contentType.contains("text/plain")) { //$NON-NLS-1$
                            // Get the plain text from connection.
                            InputStream inputStream = urlConnection.getInputStream();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream(500);
                            byte[] b = new byte[1024];
                            int len;
                            while ((len = inputStream.read(b)) != -1) {
                                baos.write(b, 0, len);
                            }
                            inputStream.close();
                            return baos.toString();

                            // } else if (contentType.contains("text/html")) {
                            // return url too.
                        }
                    }
                }
                return licenseUrl; // else, just return the URL.
            } catch (MalformedURLException e) {
                //
            } catch (IOException e) {
                //
            }
        }
        return null;
    }

}
