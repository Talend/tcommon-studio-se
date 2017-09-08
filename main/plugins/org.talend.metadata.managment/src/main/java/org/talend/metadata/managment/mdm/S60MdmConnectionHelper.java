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
package org.talend.metadata.managment.mdm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.talend.core.classloader.ClassLoaderFactory;
import org.talend.core.classloader.DynamicClassLoader;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.metadata.designerproperties.MDMVersions;
import org.talend.core.utils.ReflectionUtils;

/**
 * created by wchen on Apr 15, 2015 Detailled comment
 *
 */
public class S60MdmConnectionHelper extends AbsMdmConnectionHelper {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.metadata.managment.mdm.AbsMdmConnectionHelper#checkConnection(java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public Object checkConnection(String url, String universe, String userName, String password) throws Exception {
        BindingProvider stub = null;
        stub = getStub(this.getClass().getClassLoader(), url, universe, userName, password);

        return stub;
    }

    private BindingProvider getStub(ClassLoader classLoader, String url, String universe, String userName, String password)
            throws Exception {
        BindingProvider stub = null;
        String newUrl = url;
        if (!newUrl.trim().endsWith("?wsdl")) {
            newUrl = newUrl + "?wsdl";
        }
        Object serviceService = ReflectionUtils.newInstance("org.talend.mdm.webservice.TMDMService_Service", classLoader,
                new Object[] { new URL(newUrl) });
        Object invokeMethod = ReflectionUtils.invokeMethod(serviceService, "getTMDMPort", new Object[0]);
        if (invokeMethod instanceof BindingProvider) {
            stub = (BindingProvider) invokeMethod;
            Map<String, Object> requestContext = stub.getRequestContext();
            requestContext.put(javax.xml.ws.BindingProvider.SESSION_MAINTAIN_PROPERTY, false);
            requestContext.put(javax.xml.ws.BindingProvider.USERNAME_PROPERTY, userName);
            requestContext.put(javax.xml.ws.BindingProvider.PASSWORD_PROPERTY, password);
            Object wsping = ReflectionUtils.newInstance("org.talend.mdm.webservice.WSPing", classLoader, new Object[0]);
            ReflectionUtils.invokeMethod(stub, "ping", new Object[] { wsping });
        }
        return stub;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.metadata.managment.mdm.AbsMdmConnectionHelper#getPKs(org.apache.axis.client.Stub,
     * java.lang.String, java.lang.String)
     */
    @Override
    public List<String> getPKs(Object stub, String getDataPKsMethod, String dataPKsClass, String pkRegex,
            String getWsDataPKsMethod) throws Exception {
        List<String> dataModelStrs = new ArrayList<String>();
        Object modelPK = ReflectionUtils.newInstance(dataPKsClass, this.getClass().getClassLoader(), new Object[] { pkRegex });
        Object dataModelsPKArray = ReflectionUtils.invokeMethod(stub, getDataPKsMethod, new Object[] { modelPK });
        Object dataModels = ReflectionUtils.invokeMethod(dataModelsPKArray, getWsDataPKsMethod, new Object[0]);
        if (dataModels instanceof List) {
            List dataModelArray = (List) dataModels;
            for (Object dataModel : dataModelArray) {
                Object pk = ReflectionUtils.invokeMethod(dataModel, "getPk", new Object[0]);
                if (pk instanceof String) {
                    dataModelStrs.add((String) pk);
                }
            }
        }

        return dataModelStrs;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.metadata.managment.mdm.AbsMdmConnectionHelper#initConcept(org.talend.core.model.metadata.builder.
     * connection.MDMConnection, java.io.File)
     */
    @Override
    public void initConcept(MDMConnection mdmConn, File file) throws Exception {

        String userName = mdmConn.getUsername();
        String password = mdmConn.getValue(mdmConn.getPassword(), false);
        String universe = mdmConn.getUniverse();
        String datamodel = mdmConn.getDatamodel();
        String url = mdmConn.getServerUrl();
        Object stub = getStub(this.getClass().getClassLoader(), url, universe, userName, password);
        if (stub == null) {
            return;
        }

        Object wsping = ReflectionUtils.newInstance("org.talend.mdm.webservice.WSPing", this.getClass().getClassLoader(),
                new Object[0]);
        ReflectionUtils.invokeMethod(stub, "ping", new Object[] { wsping });

        // find data model pk
        Object wsModelPKs = ReflectionUtils.newInstance("org.talend.mdm.webservice.WSRegexDataModelPKs", this.getClass()
                .getClassLoader(), new Object[] { "" });
        Object dataModelPKs = ReflectionUtils.invokeMethod(stub, "getDataModelPKs", new Object[] { wsModelPKs });
        if (dataModelPKs == null) {
            return;
        }
        Object wsDataModelPKs = ReflectionUtils.invokeMethod(dataModelPKs, "getWsDataModelPKs", new Object[0]);
        Object findDataModelPK = null;
        if (wsDataModelPKs instanceof List) {
            List dataModelPKArray = (List) wsDataModelPKs;
            for (Object dataModelPK : dataModelPKArray) {
                Object pk = ReflectionUtils.invokeMethod(dataModelPK, "getPk", new Object[0]);
                if (datamodel != null && datamodel.equals(pk)) {
                    findDataModelPK = dataModelPK;
                    break;
                }
            }
        }

        if (findDataModelPK == null) {
            return;
        }

        // find data model
        Object wsDataModel = ReflectionUtils.newInstance("org.talend.mdm.webservice.WSGetDataModel", this.getClass()
                .getClassLoader(), new Object[] { findDataModelPK });
        Object dataModel = ReflectionUtils.invokeMethod(stub, "getDataModel", new Object[] { wsDataModel });
        if (dataModel == null) {
            return;
        }
        Object xsdSchema = ReflectionUtils.invokeMethod(dataModel, "getXsdSchema", new Object[0]);
        if (xsdSchema instanceof String) {
            writeInFile(file, (String) xsdSchema);
        }

    }

    @Override
    public String getXsdSchema(Object stub, String resName) throws Exception {
        String xsdSchema = "";
        DynamicClassLoader classLoader = ClassLoaderFactory.getClassLoader(MDMVersions.MDM_S60.name(), this.getClass()
                .getClassLoader());
        Object wsModelPKs = ReflectionUtils.newInstance("org.talend.mdm.webservice.WSDataModelPK", classLoader,
                new Object[] { resName });
        Object wsDataModel = ReflectionUtils.newInstance("org.talend.mdm.webservice.WSGetDataModel", classLoader,
                new Object[] { wsModelPKs });
        Object dataModel = ReflectionUtils.invokeMethod(stub, "getDataModel", new Object[] { wsDataModel });
        if (dataModel != null) {
            Object xsdSchemaObj = ReflectionUtils.invokeMethod(dataModel, "getXsdSchema", new Object[0]);
            if (xsdSchemaObj instanceof String) {
                xsdSchema = (String) xsdSchemaObj;
            }
        }
        return xsdSchema;
    }

    private static void writeInFile(File file, String schema) {
        StringReader reader = new StringReader(schema);
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
            char[] c = new char[1024];
            int l = 0;
            while ((l = reader.read(c)) != -1) {
                writer.write(c, 0, l);
            }
            writer.flush();
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.metadata.managment.mdm.AbsMdmConnectionHelper#resetUniverseUser(java.lang.Object,
     * java.lang.String)
     */
    @Override
    public void resetUniverseUser(Object stub, String userName) {
        // TODO Auto-generated method stub

    }

}
