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
package org.talend.commons.i18n;

import java.util.Collections;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * DOC wzhang class global comment. Detailled comment
 */
public class BabiliUpdateUtil {

    private static final String REVERSION_LIST = "http://talendforge.org/babili/api/get_labels.php"; //$NON-NLS-1$

    /**
     * 
     * DOC wzhang Comment method "getBabiliList". get the list from Babili.
     * 
     * @param language
     * @param validated
     * @param version
     * @return
     * @throws Exception
     * @deprecated
     */
    public static List<BabiliInfo> getBabiliList(String language, boolean validated, String version, IProgressMonitor monitor)
            throws Exception {
        StringBuffer url = new StringBuffer();
        url.append(REVERSION_LIST).append("?language=").append(language).append("&validated_only=").append(validated).append( //$NON-NLS-1$ //$NON-NLS-2$
                "&release=").append(version); //$NON-NLS-1$
        checkProcessCancel(monitor);
        String jsonContent = sendGetRequest(url.toString());
        checkProcessCancel(monitor);
        return parseJsonObject(jsonContent, BabiliInfo.class, monitor);
    }

    /**
     * 
     * DOC wzhang Comment method "sendGetRequest".
     * 
     * @param url
     * @return
     * @throws Exception
     * @deprecated
     */
    public static String sendGetRequest(String url) throws Exception {
        HttpClient httpclient = new HttpClient();
        GetMethod getMethod = new GetMethod(url);
        httpclient.executeMethod(getMethod);
        String response = getMethod.getResponseBodyAsString();
        getMethod.releaseConnection();
        return response;
    }

    /**
     * 
     * DOC wzhang Comment method "parseJsonObject".
     * 
     * @param jsonContent
     * @param clazz
     * @return
     * @throws Exception
     * @deprecated
     */
    public static List parseJsonObject(String jsonContent, Class clazz, IProgressMonitor monitor) throws Exception {
        // need factory for creating parser to use
        // JsonFactory jf = new JsonFactory();
        // List result = (List) new JavaTypeMapper().read(jf.createJsonParser(new StringReader(jsonContent)));
        // List objList = new ArrayList(result.size());
        // for (int i = 0; i < result.size(); i++) {
        // checkProcessCancel(monitor);
        // Object obj = clazz.newInstance();
        // Object source = result.get(i);
        // BeanUtils.copyProperties(obj, source);
        // objList.add(obj);
        // }
        // return objList;
        return Collections.emptyList();
    }

    /**
     * 
     * wzhang Comment method "checkProcessCancel".
     * 
     * @param monitor
     * @throws ImportBabiliCancelException
     * @deprecated
     */
    public static void checkProcessCancel(IProgressMonitor monitor) throws ImportBabiliCancelException {
        if (monitor != null && monitor.isCanceled()) {
            throw new ImportBabiliCancelException();
        }
    }
}
