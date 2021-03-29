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
package org.talend.librariesmanager.nexus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.nexus.IRepositoryArtifactHandler;
import org.talend.core.nexus.NexusServerUtils;
import org.talend.core.nexus.RepositoryArtifactHandlerManager;
import org.talend.core.nexus.TalendMavenResolver;
import org.talend.core.nexus.ArtifactRepositoryBean.NexusType;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.librariesmanager.model.service.LocalLibraryManager;
import org.talend.librariesmanager.nexus.utils.NexusServerManagerProxy;
import org.talend.librariesmanager.nexus.utils.RestAPIUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * created by wchen on Aug 18, 2017 Detailled comment
 *
 */
public class Nexus3RepositoryHandlerTest {
    
    private static Properties nexusprops = new Properties();
    private static ArtifactRepositoryBean customNexusServer;
    private static IRepositoryArtifactHandler repHandler;
    private static String[]  types = new String[] {"jar", "pom", "exe", "zip", "dll"};
    
    @BeforeClass
    public static void init() throws FileNotFoundException, IOException  {
        URL entry = Platform.getBundle("org.talend.librariesmanager.test").getEntry("resources/nexus/nexus3.properties");
        nexusprops.load(new FileInputStream(FileLocator.toFileURL(entry).getFile()));
        customNexusServer = NexusServerManagerProxy.getInstance().getCustomNexusServer();
        repHandler = RepositoryArtifactHandlerManager.getRepositoryHandler(customNexusServer);
        createNexusRepository(customNexusServer.getRepositoryId(),"RELEASE");
        createNexusRepository(customNexusServer.getSnapshotRepId(),"SNAPSHOT");
    }
    
    @Test
    public void testGetRepositoryURL() {
        ArtifactRepositoryBean serverBean = new ArtifactRepositoryBean();
        serverBean.setServer("http://localhost:8081");
        serverBean.setRepositoryId("release-repository");
        serverBean.setSnapshotRepId("snapshot-repository");
        serverBean.setType(NexusType.NEXUS_3.name());
        Nexus3RepositoryHandler handler = new Nexus3RepositoryHandler();
        handler.setArtifactServerBean(serverBean);
        String releaseUrl = handler.getRepositoryURL(true);
        Assert.assertEquals("http://localhost:8081/repository/release-repository/", releaseUrl);
        String snapshotUrl = handler.getRepositoryURL(false);
        Assert.assertEquals("http://localhost:8081/repository/snapshot-repository/", snapshotUrl);
    }
    
    @Test
    public void testCheckConnectionOK() {
        Assert.assertTrue(repHandler.checkConnection());
    }
    
    @Test
    public void testCheckConnectionFalse() {
        ArtifactRepositoryBean dummyNexusServer = NexusServerManagerProxy.getInstance().getDummyNexusServer();
        IRepositoryArtifactHandler repHandler = RepositoryArtifactHandlerManager.getRepositoryHandler(dummyNexusServer);
        Assert.assertFalse(repHandler.checkConnection());
    }
    
    @Test
    public void testDeployOnSnapshot() throws Exception {
        for ( String type : types ) {
            String uri = "mvn:org.talend.libraries/test/6.0.0-SNAPSHOT/" + type;
            MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(uri);
            try {
                clearLocalFile(uri);
                Bundle bundle = Platform.getBundle("org.talend.librariesmanager.test");
                URL entry = bundle.getEntry("/lib/nexus.upload.test.old-1.0.0." + type);
                File originalJarFile = new File(FileLocator.toFileURL(entry).getFile());
                String originalSHA1 = getSha1(originalJarFile);
                // deploy the file to nexus 
                repHandler.deploy(originalJarFile, artifact.getGroupId(), artifact.getArtifactId(), artifact.getClassifier(), type, artifact.getVersion());
                File reolved = repHandler.resolve(artifact);
                assertEquals(originalSHA1,getSha1(reolved));
            } finally {
                deleteNexusArtifact(artifact, false, true);
            }
        }
    }
    
    @Test
    public void testDeployOnRelease() throws Exception {
        for ( String type : types ) {
            String uri = "mvn:org.talend.libraries/test/6.0.0/" + type ;
            MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(uri);
            try {
                clearLocalFile(uri);
                Bundle bundle = Platform.getBundle("org.talend.librariesmanager.test");
                URL entry = bundle.getEntry("/lib/nexus.upload.test.old-1.0.0." + type);
                File originalJarFile = new File(FileLocator.toFileURL(entry).getFile());
                String originalSHA1 = getSha1(originalJarFile);
                // deploy file to nexus 
                repHandler.deploy(originalJarFile, artifact.getGroupId(), artifact.getArtifactId(), artifact.getClassifier(), type , artifact.getVersion());
                File resolved = repHandler.resolve(artifact);
                assertEquals(originalSHA1,getSha1(resolved));
            } finally {
                deleteNexusArtifact(artifact, true, true);
            }
        }
    }
    
    @Test
    public void testUpdateOnSnapshot() throws Exception {
        for ( String type : types ) {
            String uri = "mvn:org.talend.libraries/test/6.0.0-SNAPSHOT/" + type ;
            MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(uri);
            try {
                clearLocalFile(uri);
                Bundle bundle = Platform.getBundle("org.talend.librariesmanager.test");
                URL entry = bundle.getEntry("/lib/nexus.upload.test.old-1.0.0." + type);
                File originalJarFile = new File(FileLocator.toFileURL(entry).getFile());
                entry = bundle.getEntry("/lib/nexus.upload.test.new-1.0.0." + type);
                File newJarFile = new File(FileLocator.toFileURL(entry).getFile());
                String originalSHA1 = getSha1(originalJarFile);
                String newJarSHA1 = getSha1(newJarFile);
                // deploy original jar without resolving
                repHandler.deploy(originalJarFile, artifact.getGroupId(), artifact.getArtifactId(), artifact.getClassifier(), type, artifact.getVersion());
                //deploy new jar
                repHandler.deploy(newJarFile, artifact.getGroupId(), artifact.getArtifactId(), artifact.getClassifier(), type, artifact.getVersion());
                //resolve and check the local file
                File resolved = repHandler.resolve(artifact);
                assertEquals(newJarSHA1,getSha1(resolved));
            } finally {
                deleteNexusArtifact(artifact, false, true);
            }
        }
    }
    
    @Test
    public void testResolveSha1NotExist() throws Exception {
        String uri = "mvn:org.talend.libraries/not-existing/6.0.0-SNAPSHOT/jar";
        MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(uri);
        try {
            File f = repHandler.resolve(artifact);
            assertNull(f);
        } catch (FileNotFoundException fnfe) {
            //It is one expected exception.
            return;
        }
    }
    
    @Test
    public void testSearchOnRelease() throws Exception {
        for (String type: types) {
            String uri = "mvn:org.talend.libraries/test/6.0.0/" + type;
            MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(uri);
            try {
                //Step1:Prepare the installed artifact
                Bundle bundle = Platform.getBundle("org.talend.librariesmanager.test");
                final URL entry = bundle.getEntry("/lib/nexus.upload.test.old-1.0.0." + type);
                File fileToBeInstalled = new File(FileLocator.toFileURL(entry).getFile());
                //Step2:Search empty
                List<MavenArtifact> searchRet = repHandler.search(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(), true, false);
                if ( searchRet != null && searchRet.size() > 0 ) {
                    Assert.fail("The artifact:" + uri +  " is not expected to intalled before the test");
                }
                //Step3:Deploy the artifact
                repHandler.deploy(fileToBeInstalled, artifact.getGroupId(), artifact.getArtifactId(), artifact.getClassifier(), artifact.getType(), artifact.getVersion());
                //Step4:Verify the search result
                searchRet = repHandler.search(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(), true, false);
                int i = 30;
                while ( searchRet.size() < 1 && i > 0 ) {
                  Thread.sleep(1000);
                  searchRet = repHandler.search(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(), true, false);
                  i--;
                }
                if (NexusServerUtils.IGNORED_TYPES.contains(type)) {
                    assertEquals("Should get 0 artifact when searching for ignored type of " + uri , 0, searchRet.size());
                } else {
                    assertEquals("Should get 1 artifact when searching for " + uri , 1, searchRet.size());
                    assertEquals(artifact.getArtifactId(), searchRet.get(0).getArtifactId());
                    assertEquals(artifact.getGroupId(), searchRet.get(0).getGroupId());
                    assertEquals(artifact.getVersion(), searchRet.get(0).getVersion());
                    assertEquals(artifact.getClassifier(), searchRet.get(0).getClassifier());
                    assertEquals(artifact.getType(), searchRet.get(0).getType());
                }
            } finally {
                deleteNexusArtifact(artifact, true, true);
                Thread.sleep(1000);
            }
        }
    }
    
    @Test
    public void testSearchOnSnapshot() throws Exception {
        String uri = "mvn:org.talend.libraries/test/6.0.0-SNAPSHOT/jar";
        MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(uri);
        try {
            //Step1:Prepare the installed artifact
            Bundle bundle = Platform.getBundle("org.talend.librariesmanager.test");
            final URL entry = bundle.getEntry("/lib/nexus.upload.test.old-1.0.0.jar");
            File fileToBeInstalled = new File(FileLocator.toFileURL(entry).getFile());
            //Step2:Search empty
            List<MavenArtifact> searchRet = repHandler.search(artifact.getGroupId(), artifact.getArtifactId(), null, false, true);
            if ( searchRet != null && searchRet.size() > 0 ) {
                Assert.fail("The artifact is not expected to intalled before the test");
            }
            //Step3:Deploy the artifact
            repHandler.deploy(fileToBeInstalled, artifact.getGroupId(), artifact.getArtifactId(), artifact.getClassifier(), "jar", artifact.getVersion());
            //Step4:Verify the search result
            //In snapshot repository, Timestamp is added in item version like 6.0.0-20201102.054042-1, 
            //the search result for given version like 6.0.0 will be empty. Currently the version will not be given in search criteria.
            searchRet = repHandler.search(artifact.getGroupId(), artifact.getArtifactId(), null, false, true);
            int i = 30;
            while ( searchRet.size() < 1 && i > 0 ) {
              Thread.sleep(1000);
              searchRet = repHandler.search(artifact.getGroupId(), artifact.getArtifactId(), null, false, true);
              i--;
            }
            debug("testSearchSnapshot:" + searchRet);
            assertEquals(1, searchRet.size());
            assertEquals(artifact.getArtifactId(), searchRet.get(0).getArtifactId());
            assertEquals(artifact.getGroupId(), searchRet.get(0).getGroupId());
            //assertEquals(artifact.getVersion(), searchRet.get(0).getVersion());
            assertEquals(artifact.getClassifier(), searchRet.get(0).getClassifier());
            assertEquals(artifact.getType(), searchRet.get(0).getType());
        } finally {
            deleteNexusArtifact(artifact, false, true);
            Thread.sleep(1000);
        }
    }
    
    @Test
    public void testPaginationSearchOnSnapshot() throws Exception {
        //Pagination is enabled by Nexus when a search returns 50 results
        String uri = "mvn:org.talend.libraries/test/6.0.0-SNAPSHOT/zip";
        MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(uri);
        List<MavenArtifact> searchRet = new ArrayList<MavenArtifact>();
        try {
            //Step1:Prepare the installed artifact
            Bundle bundle = Platform.getBundle("org.talend.librariesmanager.test");
            final URL entry = bundle.getEntry("/lib/nexus.upload.test.old-1.0.0.zip");
            File fileToBeInstalled = new File(FileLocator.toFileURL(entry).getFile());
            //Step2:Deploy the artifacts for 101 times on snapshot repository .
            int i = 51;
            while ( i> 0 ) {
              repHandler.deploy(fileToBeInstalled, artifact.getGroupId(), artifact.getArtifactId(), artifact.getClassifier(), "zip", artifact.getVersion());
              i--;
            }
            //Step3:Verify the search result. It will take some time for the deployment and wait for most 60 seconds
            searchRet = repHandler.search(artifact.getGroupId(), artifact.getArtifactId(), null, false, true);
            i = 6;
            while ( searchRet.size() < 51 && i > 0 ) {
              Thread.sleep(10000);
              searchRet = repHandler.search(artifact.getGroupId(), artifact.getArtifactId(), null, false, true);
              i--;
            }
            assertEquals(51, searchRet.size());
        } finally {
            deleteNexusArtifacts(searchRet, false, true);
        }
    }
    
    //@Test
    public void testUpdateMavenResolver() throws InvalidSyntaxException, IOException {
        Dictionary<String, String> props = new Hashtable<String, String>();
        props.put("org.ops4j.pax.url.mvn.socket.readTimeout", "59");
        props.put("org.ops4j.pax.url.mvn.socket.connectionTimeout", "119");
        repHandler.updateMavenResolver(TalendMavenResolver.TALEND_ARTIFACT_LIBRARIES_RESOLVER, props);
        
        BundleContext context = CoreRuntimePlugin.getInstance().getBundle().getBundleContext();
        ServiceReference ca = context.getServiceReference(ConfigurationAdmin.class);
        debug("" + ca);
        ConfigurationAdmin configAdmin = (ConfigurationAdmin) context.getService(ca);
        if ( configAdmin == null ) {
            Configuration conf = configAdmin.getConfiguration("(service.pid=org.ops4j.pax.url.mvn)");
            assertEquals("59", conf.getProperties().get("org.ops4j.pax.url.mvn.socket.readTimeout"));
            assertEquals("119", conf.getProperties().get("org.ops4j.pax.url.mvn.socket.connectionTimeout"));
         }
    }
    
    @Before
    @After
    public  void cleanup() {
        try {
            String uri = "mvn:org.talend.libraries/test/6.0.0-SNAPSHOT/jar";
            MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(uri);
            List<MavenArtifact> searchRet = repHandler.search(artifact.getGroupId(), artifact.getArtifactId(), null, false, true);
            deleteNexusArtifacts(searchRet,false,true);
            
            uri = "mvn:org.talend.libraries/test/6.0.0/jar";
            artifact = MavenUrlHelper.parseMvnUrl(uri);
            searchRet = repHandler.search(artifact.getGroupId(), artifact.getArtifactId(), null, true, false);
            deleteNexusArtifacts(searchRet,true,true);
        } catch ( Exception ex ) {
        }
    }
    
    private void clearLocalFile(String uri) {
        LocalLibraryManager localLibraryManager = new LocalLibraryManager();
        String localJarPath = localLibraryManager.getJarPathFromMaven(uri);
        debug("localJarPath:" + localJarPath);
        // force to delete the file to have a valid test
        if (localJarPath != null) {
            org.talend.utils.io.FilesUtils.deleteFolder(new File(localJarPath).getParentFile().getParentFile(), true);
        }
        // file should not exist anymore
        assertNull(localLibraryManager.getJarPathFromMaven(uri));

    }
    
    private static void createNexusRepository(String repId,String versionPolicy) throws IOException {
        if (!"SNAPSHOT".equalsIgnoreCase(versionPolicy) && !"RELEASE".equalsIgnoreCase(versionPolicy)) {
            Assert.fail("Repository Version Policy must be SNAPSHOT OR RELEASE, but got " + versionPolicy);
        }
        String getstmt = customNexusServer.getServer() +
                nexusprops.getProperty("rep.list.endpoint").replace("{repid}", repId);
        try {
            String[] response = RestAPIUtil.doRequest(getstmt, "GET", customNexusServer.getUserName(), customNexusServer.getPassword(), null);
            if ( response[0].equals("200")) return;
        } catch (Exception ex ) {
            //Can not find and will create a new one
            debug("Can not find the repository named as " + repId + " and will create a new one :" + ex.getMessage());
        }
        String createstmt = customNexusServer.getServer() + nexusprops.getProperty("rep.create.endpoint");
        String jsonfilepath = FileLocator.toFileURL(Platform.getBundle("org.talend.librariesmanager.test").getEntry("resources/nexus/nexus3_create_rep.json")).getFile();
        String data = new String(Files.readAllBytes(new File(jsonfilepath).toPath())).replace("NEWREPNAME", repId).replace("NEWVERSIONPOLICY", versionPolicy.toUpperCase());
        try {
          String[] response = RestAPIUtil.doRequest(createstmt, "POST", customNexusServer.getUserName(), customNexusServer.getPassword(), data);
          if ( response[0].equals("201")) debug("Created the test repository successfully!");
        } catch (Exception ex ) {
            debug("Exception when create the repository of " + repId + ":" + ex );
        }
    }
    
    private static void deleteNexusArtifacts( List<MavenArtifact> artifacts,boolean isRelease, boolean skiperror) throws Exception{
        if (artifacts == null) return;
        for (MavenArtifact artifact : artifacts) {
            deleteNexusArtifact(artifact,isRelease,skiperror);
        }
    }
    
    private static void deleteNexusArtifact(MavenArtifact artifact, boolean fromRelease, boolean skiperror) throws Exception {
        try {
            StringBuffer searchstmt = new StringBuffer();
            searchstmt.append(customNexusServer.getServer())
            .append(nexusprops.getProperty("component.search.endpoint"))
            .append("?repository=").append( fromRelease ? customNexusServer.getRepositoryId() : customNexusServer.getSnapshotRepId())
            .append("&group=").append(artifact.getGroupId())
            .append("&name=").append(artifact.getArtifactId());
            if ( !artifact.getVersion().endsWith(MavenUrlHelper.VERSION_SNAPSHOT))
                searchstmt.append("&version=").append(artifact.getVersion());
            String[] resp = RestAPIUtil.doRequest(searchstmt.toString(), "GET", customNexusServer.getUserName(), customNexusServer.getPassword(), null);
            if ( !resp[0].equals("200")) return;
            JSONObject obj = JSONObject.fromObject(resp[1]);
            JSONArray items = obj.getJSONArray("items");
            if ( items.size() < 1) return;
            JSONObject item = (JSONObject) items.get(0);
            String componentid = item.getString("id");
            String deltestmt = customNexusServer.getServer() + nexusprops.getProperty("component.delete.endpoint").replace("{id}", componentid);
            RestAPIUtil.doRequest(deltestmt, "DELETE", customNexusServer.getUserName(), customNexusServer.getPassword(), null);
        } catch (Exception ex) {
            if (!skiperror) throw ex;
        }
    }
    
    private String getSha1(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        String sha1 = DigestUtils.shaHex(fis);
        fis.close();
        return sha1;
    }

    private String getSha1(String file) throws IOException {
        return getSha1(new File(file));
    }
    
    private static void debug(String message) {
        System.out.println("[DEBUG]:" + message);
    }

}
