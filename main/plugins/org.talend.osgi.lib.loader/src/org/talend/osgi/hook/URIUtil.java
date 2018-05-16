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
package org.talend.osgi.hook;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.osgi.storage.BundleInfo.Generation;

/**
 * This class copies various methods from the URIUtil class in org.eclipse.equinox.common. Unless otherwise noted the
 * implementations here should mirror those in the common implementation.
 */
public class URIUtil {

    public static final String SCHEME_FILE = "file"; //$NON-NLS-1$

    private static final String UNC_PREFIX = "//"; //$NON-NLS-1$

    /**
     * Appends the given extension to the path of the give base URI and returns the corresponding new path.
     * 
     * @param base The base URI to append to
     * @param extension The path extension to be added
     * @return The appended URI
     */
    public static URI append(URI base, String extension) {
        try {
            String path = base.getPath();
            if (path == null) {
                return appendOpaque(base, extension);
            }
            // if the base is already a directory then resolve will just do the right thing
            if (path.endsWith("/")) {//$NON-NLS-1$
                URI result = base.resolve(extension);
                // Fix UNC paths that are incorrectly normalized by URI#resolve (see Java bug 4723726)
                String resultPath = result.getPath();
                if (path.startsWith(UNC_PREFIX) && (resultPath == null || !resultPath.startsWith(UNC_PREFIX))) {
                    result = new URI(result.getScheme(), "///" + result.getSchemeSpecificPart(), result.getFragment()); //$NON-NLS-1$
                }
                return result;
            }
            path = path + "/" + extension; //$NON-NLS-1$
            return new URI(base.getScheme(), base.getUserInfo(), base.getHost(), base.getPort(), path, base.getQuery(),
                    base.getFragment());
        } catch (URISyntaxException e) {
            // shouldn't happen because we started from a valid URI
            throw new RuntimeException(e);
        }
    }

    /**
     * Special case of appending to an opaque URI. Since opaque URIs have no path segment the best we can do is append
     * to the scheme-specific part
     */
    private static URI appendOpaque(URI base, String extension) throws URISyntaxException {
        String ssp = base.getSchemeSpecificPart();
        if (ssp.endsWith("/")) {
            ssp += extension;
        } else {
            ssp = ssp + "/" + extension; //$NON-NLS-1$
        }
        return new URI(base.getScheme(), ssp, base.getFragment());
    }

    /**
     * Returns a URI corresponding to the given unencoded string.
     * 
     * @throws URISyntaxException If the string cannot be formed into a valid URI
     */
    public static URI fromString(String uriString) throws URISyntaxException {
        int colon = uriString.indexOf(':');
        int hash = uriString.lastIndexOf('#');
        boolean noHash = hash < 0;
        if (noHash) {
            hash = uriString.length();
        }
        String scheme = colon < 0 ? null : uriString.substring(0, colon);
        String ssp = uriString.substring(colon + 1, hash);
        String fragment = noHash ? null : uriString.substring(hash + 1);
        // use java.io.File for constructing file: URIs
        if (scheme != null && scheme.equals(SCHEME_FILE)) {
            File file = new File(uriString.substring(5));
            if (file.isAbsolute()) {
                return file.toURI();
            }
            scheme = null;
            if (File.separatorChar != '/') {
                ssp = ssp.replace(File.separatorChar, '/');
            }
        }
        return new URI(scheme, ssp, fragment);
    }

    /*
     * Compares two URI for equality. Return false if one of them is null
     */
    public static boolean sameURI(URI url1, URI url2) {
        if (url1 == url2) {
            return true;
        }
        if (url1 == null || url2 == null) {
            return false;
        }
        if (url1.equals(url2)) {
            return true;
        }

        if (url1.isAbsolute() != url2.isAbsolute()) {
            return false;
        }

        // check if we have two local file references that are case variants
        File file1 = toFile(url1);
        return file1 == null ? false : file1.equals(toFile(url2));
    }

    /**
     * Returns the URI as a local file, or <code>null</code> if the given URI does not represent a local file.
     * 
     * @param uri The URI to return the file for
     * @return The local file corresponding to the given URI, or <code>null</code>
     */
    public static File toFile(URI uri) {
        try {
            if (!SCHEME_FILE.equalsIgnoreCase(uri.getScheme())) {
                return null;
            }
            // assume all illegal characters have been properly encoded, so use URI class to unencode
            return new File(uri);
        } catch (IllegalArgumentException e) {
            // File constructor does not support non-hierarchical URI
            String path = uri.getPath();
            // path is null for non-hierarchical URI such as file:c:/tmp
            if (path == null) {
                path = uri.getSchemeSpecificPart();
            }
            return new File(path);
        }
    }

    /**
     * Returns a string representation of the given URI that doesn't have illegal characters encoded. This string is
     * suitable for later passing to {@link #fromString(String)}.
     * 
     * @param uri The URI to convert to string format
     * @return An unencoded string representation of the URI
     */
    public static String toUnencodedString(URI uri) {
        StringBuffer result = new StringBuffer();
        String scheme = uri.getScheme();
        if (scheme != null) {
            result.append(scheme).append(':');
        }
        // there is always a ssp
        result.append(uri.getSchemeSpecificPart());
        String fragment = uri.getFragment();
        if (fragment != null) {
            result.append('#').append(fragment);
        }
        return result.toString();
    }

    /**
     * Returns the URL as a URI. This method will handle broken URLs that are not properly encoded (for example they
     * contain unencoded space characters).
     */
    public static URI toURI(URL url) throws URISyntaxException {
        // URL behaves differently across platforms so for file: URLs we parse from string form
        if (SCHEME_FILE.equals(url.getProtocol())) {
            String pathString = url.toExternalForm().substring(5);
            // ensure there is a leading slash to handle common malformed URLs such as file:c:/tmp
            if (pathString.indexOf('/') != 0) {
                pathString = '/' + pathString;
            } else if (pathString.startsWith(UNC_PREFIX) && !pathString.startsWith(UNC_PREFIX, 2)) {
                // URL encodes UNC path with two slashes, but URI uses four (see bug 207103)
                pathString = UNC_PREFIX + pathString;
            }
            return new URI(SCHEME_FILE, null, pathString, null);
        }
        try {
            return new URI(url.toExternalForm());
        } catch (URISyntaxException e) {
            // try multi-argument URI constructor to perform encoding
            return new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(),
                    url.getRef());
        }
    }

    /**
     * Returns a URI as a URL.
     * 
     * @throws MalformedURLException
     */
    public static URL toURL(URI uri) throws MalformedURLException {
        return new URL(uri.toString());
    }

    /**
     * look into the bundle classpath header from the generation param to find the mvn url associated with the jar path
     * in the class path if any. The mvn url must not contain any repository url. This may be of the form:
     * 
     * <pre>
     * {@code
     * Bundle-ClassPath:
     * lib/any-existing.jar;mvn:org.talend.studio/any-existing/1.0.0,.
     * }
     * </pre>
     * 
     * @param path of the jar in the Bundle-Classpath to look for.
     * @param generation, used to find the Bundle-ClassPath header
     * @param onlyLocalUri, this will add a special repository URI for pax to ignore remote resolution.
     * @return the pax maven uri if found or null (see https://ops4j1.jira.com/wiki/display/paxurl/Mvn+Protocol)
     * @throws URISyntaxException if the uri is wrong
     */
    static public URI getMvnUri(String path, Generation generation, boolean onlyLocalUri) throws URISyntaxException {
        String bundleClasspath = generation.getHeaders().get("Bundle-ClassPath"); //$NON-NLS-1$
        if (bundleClasspath != null) {
            int entryStart = bundleClasspath.indexOf(path);
            if (entryStart >= 0) {// path found in the bundle classpath directive
                String mvnUri = null;
                // look for the mvn: protocol in the entry last parameter
                int entryEnd = bundleClasspath.indexOf(",", entryStart); //$NON-NLS-1$
                String entry = bundleClasspath.substring(entryStart, entryEnd != -1 ? entryEnd : bundleClasspath.length() - 1);
                int parameterIndex = entry.lastIndexOf(';');
                if (parameterIndex > 0 && parameterIndex < entry.length() - 1) {// found a paramter for this entry
                                                                                // so
                                                                                // check it is a mvn url then
                                                                                // returns
                                                                                // it.
                    String potentionMvnUrl = entry.substring(parameterIndex + 1);
                    if (potentionMvnUrl.startsWith("mvn:")) { //$NON-NLS-1$
                        mvnUri = potentionMvnUrl;
                    }
                }// no parameter for this entry
                if (mvnUri == null) {// no mvn urin found for the lib so use the default talend uri
                    String entryName = new File(path).getName();
                    // strip jar extension
                    if (entryName.endsWith(".jar")) { //$NON-NLS-1$
                        mvnUri = "mvn:org.talend.libraries/" + entryName.substring(0, entryName.length() - ".jar".length()) //$NON-NLS-1$
                                + "/6.0.0"; //$NON-NLS-1$
                    } else {// no a jar library so just try without stripping anything
                        mvnUri = "mvn:org.talend.libraries/" + entryName + "/6.0.0"; //$NON-NLS-1$ //$NON-NLS-2$
                    }
                }// else we already got the uri so keep going.
                if (onlyLocalUri) {
                    mvnUri = mvnUri.replace("mvn:", "mvn:localrepositories://!"); //$NON-NLS-1$//$NON-NLS-2$
                }// else return the original URI.
                return new URI(mvnUri);
            }// else path not found to return null
        }// else not a bundle with a class path definition so return null
        return null;
    }

    /**
     * look into uri and try ti find the version and add a -SNAPSHOT to it
     * 
     * @param mvnUri
     * @return the replaced URI if version could be updated or the original one
     * @throws URISyntaxException
     */
    public static URI addSnapshotToUri(URI mvnUri) throws URISyntaxException {
        String mvnUriStr = mvnUri.toASCIIString();
        String mvnPartStr = mvnUriStr;
        int repoDelimiter = mvnUriStr.lastIndexOf('!');
        if (repoDelimiter != -1) {
            mvnPartStr = mvnUriStr.substring(repoDelimiter + 1);
        }
        String[] segments = mvnPartStr.split("/"); //$NON-NLS-1$
        if (segments.length >= 3 && segments[2].trim().length() > 0) {
            String version = segments[2];
            if (!version.equals("LATEST") && !version.endsWith("SNAPSHOT")) { //$NON-NLS-1$ //$NON-NLS-2$
                segments[2] = segments[2] + "-SNAPSHOT"; //$NON-NLS-1$
            }// else LATEST or SNAPSHOT is DEFINED so do nothing
        }// no version specified so do nothing.
        String resultUriStr = repoDelimiter != -1 ? mvnUriStr.substring(0, repoDelimiter + 1) : ""; //$NON-NLS-1$
        resultUriStr += segments[0];
        for (int i = 1; i < segments.length; i++) {
            resultUriStr += '/' + segments[i];
        }
        return new URI(resultUriStr);
    }
}
