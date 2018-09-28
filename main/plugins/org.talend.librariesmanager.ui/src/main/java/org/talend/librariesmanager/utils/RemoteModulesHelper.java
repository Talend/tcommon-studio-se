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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
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
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.nexus.IRepositoryArtifactHandler;
import org.talend.core.nexus.RepositoryArtifactHandlerManager;
import org.talend.core.nexus.TalendLibsServerManager;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.librariesmanager.model.service.DynamicDistibutionLicenseUtil;
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
            final Set<String> mavenUrisTofetch = new HashSet<String>(contextMap.keySet());
            monitor.beginTask(Messages.getString("RemoteModulesHelper.fetch.module.info"), mavenUrisTofetch.size() * 10 + 10);//$NON-NLS-1$
            // fetch modules from local nexus first
            if (addCachedModulesToToBeInstallModules(toInstall, mavenUrisTofetch, contextMap, localCache)) {
                if (collectModulesWithJarName) {
                    collectModulesWithJarName(toInstall);
                }
                monitor.done();
                return;
            }
            // search from local nexus
            searchFromLocalNexus(mavenUrisTofetch, monitor);
            // check again after search
            if (addCachedModulesToToBeInstallModules(toInstall, mavenUrisTofetch, contextMap, localCache)) {
                if (collectModulesWithJarName) {
                    collectModulesWithJarName(toInstall);
                }
                monitor.done();
                return;
            }

            if (addCachedModulesToToBeInstallModules(toInstall, mavenUrisTofetch, contextMap, remoteCache)) {
                if (collectModulesWithJarName) {
                    collectModulesWithJarName(toInstall);
                }
                monitor.done();
                return;
            }

            Set<String> unavailableModules = new HashSet<String>();
            // if the network is not valid, all jars are not available.
            boolean networkValid = NetworkUtil.isNetworkValid();
            if (!networkValid) {
                if (!alreadyWarnedAboutConnectionIssue) {
                    log.warn("failed to connect to internet");
                    alreadyWarnedAboutConnectionIssue = true;
                } // else already warned so do nothing
            } else {
                searchFromRemoteNexus(mavenUrisTofetch, monitor);
                addCachedModulesToToBeInstallModules(toInstall, mavenUrisTofetch, contextMap, remoteCache);
            }

            unavailableModules.addAll(mavenUrisTofetch);
            addUnavailableModulesToToBeInstalledModules(unavailableModules, toInstall, contextMap);

            if (collectModulesWithJarName) {
                collectModulesWithJarName(toInstall);
            }
            Collections.sort(toInstall, new Comparator<ModuleToInstall>() {

                @Override
                public int compare(ModuleToInstall o1, ModuleToInstall o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            monitor.done();
        }

        private void searchFromLocalNexus(Set<String> mavenUristoSearch, IProgressMonitor monitor) {
            try {
                ArtifactRepositoryBean customNexusServer = TalendLibsServerManager.getInstance().getCustomNexusServer();
                IRepositoryArtifactHandler customerRepHandler = RepositoryArtifactHandlerManager
                        .getRepositoryHandler(customNexusServer);
                if (customerRepHandler != null) {
                // collect the groupIds to check
                Set<String> groupIds = new HashSet<String>();
                Set<String> snapshotgroupIds = new HashSet<String>();
                for (String mvnUri : mavenUristoSearch) {
                    final MavenArtifact parseMvnUrl = MavenUrlHelper.parseMvnUrl(mvnUri);
                    if (parseMvnUrl != null) {
                        if (parseMvnUrl.getVersion() != null && parseMvnUrl.getVersion().endsWith(MavenConstants.SNAPSHOT)) {
                            snapshotgroupIds.add(parseMvnUrl.getGroupId());
                        } else {
                            groupIds.add(parseMvnUrl.getGroupId());
                        }
                    }
                }

                    for (String groupId : groupIds) {
                        List<MavenArtifact> searchResults = customerRepHandler.search(groupId, null, null, true, false);
                        monitor.worked(10);
                        addModulesToCache(mavenUristoSearch, searchResults, localCache);
                    }
                    for (String groupId : snapshotgroupIds) {
                        List<MavenArtifact> searchResults = customerRepHandler.search(groupId, null, null, false, true);
                        monitor.worked(10);
                        addModulesToCache(mavenUristoSearch, searchResults, localCache);
                    }
                }

            } catch (Exception e1) {
                ExceptionHandler.process(e1);
            }
        }

        private void searchFromRemoteNexus(Set<String> mavenUristoSearch, IProgressMonitor monitor) {
            try {
                ArtifactRepositoryBean talendServer = TalendLibsServerManager.getInstance().getTalentArtifactServer();
                IRepositoryArtifactHandler talendRepositoryHander = RepositoryArtifactHandlerManager
                        .getRepositoryHandler(talendServer);
                if (talendRepositoryHander != null) {
                    final Iterator<String> iterator = mavenUristoSearch.iterator();
                    Map<String, List<StringBuffer>> groupIdAndJarsToCheck = new HashMap<>();
                    while (iterator.hasNext()) {
                        if (monitor.isCanceled()) {
                            break;
                        }
                        String uriToCheck = iterator.next();
                        final MavenArtifact parseMvnUrl = MavenUrlHelper.parseMvnUrl(uriToCheck);
                        if (parseMvnUrl != null) {
                            //
                            if (StringUtils.isNotEmpty(parseMvnUrl.getRepositoryUrl())) {
                                continue;
                            }
                            StringBuffer jarsToCheck = null;
                            List<StringBuffer> buffers = groupIdAndJarsToCheck.get(parseMvnUrl.getGroupId());
                            if (buffers == null) {
                                buffers = new ArrayList<>();
                                groupIdAndJarsToCheck.put(parseMvnUrl.getGroupId(), buffers);
                            }
                            if (buffers.isEmpty() || buffers.get(buffers.size() - 1).length() > 2000) {
                                jarsToCheck = new StringBuffer();
                                buffers.add(jarsToCheck);
                            } else {
                                jarsToCheck = buffers.get(buffers.size() - 1);
                            }
                            if (parseMvnUrl.getArtifactId() != null && !parseMvnUrl.getArtifactId().contains("$")) { //$NON-NLS-1$
                                jarsToCheck.append(parseMvnUrl.getArtifactId());
                                jarsToCheck.append(","); //$NON-NLS-1$
                            } else {
                                ExceptionHandler.log("wrong setup of artifact, cf:" + parseMvnUrl.getArtifactId()); //$NON-NLS-1$
                            }

                        }

                    }
                    for (String groupId : groupIdAndJarsToCheck.keySet()) {
                        List<StringBuffer> buffers = groupIdAndJarsToCheck.get(groupId);
                        for (StringBuffer toCheck : buffers) {
                            String jarsToCheck = toCheck.toString();
                            if (jarsToCheck.endsWith(",")) {
                                jarsToCheck = jarsToCheck.substring(0, jarsToCheck.length() - 1);
                            }
                            List<MavenArtifact> searchResults = talendRepositoryHander.search(groupId, jarsToCheck, null, true,
                                    false);
                            monitor.worked(10);
                            addModulesToCache(mavenUristoSearch, searchResults, remoteCache);

                        }
                    }
                }

            } catch (Exception e1) {
                ExceptionHandler.process(e1);
            }
        }

        private void addModulesToCache(Set<String> mavenUristoSearch, List<MavenArtifact> searchResults,
                Map<String, ModuleToInstall> theCache) {
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
                String url = null;
                if (artifact.getUrl() != null && !"".equals(artifact.getUrl())) {
                    url = artifact.getUrl();
                }
                ModuleToInstall m = new ModuleToInstall();
                String mvnUri = MavenUrlHelper.generateMvnUrl(artifact.getGroupId(), artifactId, version, packageName,
                        artifact.getClassifier());
                m.setMavenUri(mvnUri);
                List<ModuleNeeded> list = contextMap.get(mvnUri);
                if (list != null) {
                    for (ModuleNeeded module : list) {
                        if (mvnUri.equals(module.getMavenUri())) {
                            m.setName(module.getModuleName());
                        }
                    }
                }
                if (m.getName() == null) {
                    if (MavenConstants.DEFAULT_LIB_GROUP_ID.equals(artifact.getGroupId()) || StringUtils.isEmpty(version)) {
                        m.setName(artifactId + "." + packageName);
                    } else {
                        m.setName(artifactId + "-" + version + "." + packageName);
                    }
                }
                m.setLicenseType(license);
                m.setLicenseUrl(license_url);
                m.setDescription(description);
                m.setUrl_description(url);
                m.setUrl_download(url);
                if (StringUtils.isEmpty(artifact.getType()) || MavenConstants.PACKAGING_POM.equals(artifact.getType())) {
                    m.setDistribution(MavenConstants.DOWNLOAD_MANUAL);
                }
                if (theCache == localCache) {
                    m.setFromCustomNexus(true);
                }
                theCache.put(mvnUri, m);
            }
        }

        /**
         * 
         * DOC wchen Comment method "addCachedModulesToToBeInstallModules".
         * 
         * @param toInstall
         * @param mavenUrisTofetch
         * @param contextMap
         * @param theCache
         * @return if find all modules form the cache ,return true;
         */
        private boolean addCachedModulesToToBeInstallModules(List<ModuleToInstall> toInstall, Set<String> mavenUrisTofetch,
                Map<String, List<ModuleNeeded>> contextMap, Map<String, ModuleToInstall> theCache) {
            if (theCache.isEmpty()) {
                return false;
            }
            final Iterator<String> iterator = mavenUrisTofetch.iterator();
            while (iterator.hasNext()) {
                String mvnUri = iterator.next();
                ModuleToInstall moduleToInstall = null;
                moduleToInstall = theCache.get(mvnUri);
                if (moduleToInstall == null) {
                    // no packaging type maybe on default remote nexus:
                    MavenArtifact ma = MavenUrlHelper.parseMvnUrl(mvnUri);
                    if (ma != null) {
                        String newMvnUri = MavenUrlHelper.generateMvnUrl(ma.getGroupId(), ma.getArtifactId(), ma.getVersion(),
                                null, ma.getClassifier());
                        moduleToInstall = theCache.get(newMvnUri);
                    }
                }
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
                    iterator.remove();
                }
            }

            if (mavenUrisTofetch.isEmpty()) {
                return true;
            }
            return false;
        }

        /**
         * DOC sgandon Comment method "addUnavailableModulesToToBeInstalledModules".
         * 
         * @param unavailableModules
         * @param toInstall2
         * @param contextMap
         */
        protected void addUnavailableModulesToToBeInstalledModules(Set<String> unavailableModules,
                List<ModuleToInstall> toInstall2, Map<String, List<ModuleNeeded>> contextMap) {
            // add all unavailable modules, cause they need to be installed even if the are not available from remote
            // site.
            for (String mvnUri : unavailableModules) {
                ModuleToInstall m = createUnavailableModuleToInstall(mvnUri, contextMap);
                if (m != null) {
                    toInstall2.add(m);
                }
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
            m.setMavenUri(mvnUri);
            setContext(m, mvnUri, contextMap);
            String name = null;

            /**
             * For mvnUri with repository url, we can download it from the specified repository url dirrectly, so should
             * enable to download automatically instead of manually
             */
            boolean downloadManual = true;
            if (StringUtils.isNotEmpty(mvnUri)) {
                MavenArtifact parseMvnUrl = MavenUrlHelper.parseMvnUrl(mvnUri);
                if (parseMvnUrl == null) {
                    return null;
                }
                String repoUrl = parseMvnUrl.getRepositoryUrl();
                if (StringUtils.isNotEmpty(repoUrl)) {
                    downloadManual = false;
                    // try to setup license for the jars from distribution
                    DynamicDistibutionLicenseUtil.setupLicense(m, parseMvnUrl);
                }
            }
            if (downloadManual) {
                m.setDistribution(MavenConstants.DOWNLOAD_MANUAL);
            }

            if (contextMap != null) {
                final List<ModuleNeeded> neededModules = contextMap.get(mvnUri);
                name = neededModules.get(0).getModuleName();
                m.setName(name);
                m.setDescription(getFirstDescription(neededModules));
            }
            if (CommonsPlugin.isDebugMode()) {
                ExceptionHandler.log("The download URL for " + name + " is not available (" + mvnUri + ")");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                appendToLogFile(mvnUri + "\n");//$NON-NLS-1$
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
    }

    private static Logger log = Logger.getLogger(RemoteModulesHelper.class);

    private static RemoteModulesHelper helper;

    // private static final String serviceUrl = "http://www.talend.com/TalendRegisterWS/modules.php";

    private static final String serviceUrl = "https://www.talendforge.org/modules/webservices/modules.php"; //$NON-NLS-1$

    private static final String SEPARATOR_DISPLAY = " | "; //$NON-NLS-1$

    private static final String SEPARATOR = "|"; //$NON-NLS-1$

    private static final String SEPARATOR_SLIP = "\\|"; //$NON-NLS-1$

    /**
     * key : mvnuri(without SANPSHOT in version) value : ModuleToInstall
     */
    private Map<String, ModuleToInstall> remoteCache = new HashMap<String, ModuleToInstall>();

    private Map<String, ModuleToInstall> localCache = new HashMap<String, ModuleToInstall>();

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
                    String licenseText = object.getString("licenseText");//$NON-NLS-1$
                    if (licenseText != null) {
                        licenseText.replace("http://", "https://");//$NON-NLS-1$ //$NON-NLS-2$
                    }
                    return licenseText;
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
     */
    public RemoteModulesFetchRunnable getNotInstalledModulesRunnable(List<ModuleNeeded> neededModules,
            List<ModuleToInstall> toInstall, boolean collectModulesWithJarName) {
        Map<String, List<ModuleNeeded>> contextMap = new HashMap<String, List<ModuleNeeded>>();
        ILibraryManagerService librairesManagerService = (ILibraryManagerService) GlobalServiceRegister.getDefault()
                .getService(ILibraryManagerService.class);
        // collect mvnuri and modules incase many modules have the same mvnuri
        final Iterator<ModuleNeeded> iterator = neededModules.iterator();
        while (iterator.hasNext()) {
            ModuleNeeded module = iterator.next();
            String mvnUri = null;
            if (module.getCustomMavenUri() != null) {
                mvnUri = module.getCustomMavenUri();
            } else if (module.getMavenURIFromConfiguration() == null
                    && librairesManagerService.getMavenUriFromIndex(module.getModuleName()) != null) {
                Set<String> urisFromIndex = new HashSet<String>();
                final String mavenUriFromIndex = librairesManagerService.getMavenUriFromIndex(module.getModuleName());
                if (mavenUriFromIndex != null) {
                    final String[] split = mavenUriFromIndex.split(MavenUrlHelper.MVN_INDEX_SPLITER);
                    for (String fromIndex : split) {
                        urisFromIndex.add(fromIndex);
                    }
                }
                // add all mvnuris from index to try to download
                for (String uri : urisFromIndex) {
                    if (uri != null) {
                        uri = MavenUrlHelper.addTypeForMavenUri(uri, module.getModuleName());
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
            } else {
                mvnUri = module.getDefaultMavenURI();
            }
            if (mvnUri != null) {
                mvnUri = MavenUrlHelper.addTypeForMavenUri(mvnUri, module.getModuleName());
                if (!contextMap.keySet().contains(mvnUri)) {
                    List<ModuleNeeded> modules = new ArrayList<ModuleNeeded>();
                    modules.add(module);
                    contextMap.put(mvnUri, modules);
                } else {
                    contextMap.get(mvnUri).add(module);
                }
            }
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
     * collect all vesions to one ModuleToInstall to don't display several times for the same jar in the dialog if need
     * to download all versions
     * 
     */
    public void collectModulesWithJarName(List<ModuleToInstall> toInstall) {
        // fix for TUP-4942 incase the jar appear many times in the dialog
        List<ModuleToInstall> manualInstall = new ArrayList<ModuleToInstall>();
        Map<String, ModuleToInstall> nameAndModuleMap = new HashMap<String, ModuleToInstall>();
        for (ModuleToInstall module : toInstall) {
            ModuleToInstall moduleToInstall = nameAndModuleMap.get(module.getName());
            if (!MavenConstants.DOWNLOAD_MANUAL.equals(module.getDistribution()) && module.getMavenUri() != null) {
                if (moduleToInstall == null) {
                    moduleToInstall = module;
                    nameAndModuleMap.put(module.getName(), module);
                }
                moduleToInstall.getMavenUris().add(module.getMavenUri());
            }
        }
        for (ModuleToInstall module : toInstall) {
            if (MavenConstants.DOWNLOAD_MANUAL.equals(module.getDistribution())) {
                ModuleToInstall moduleToInstall = nameAndModuleMap.get(module.getName());
                if (moduleToInstall == null) {
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
