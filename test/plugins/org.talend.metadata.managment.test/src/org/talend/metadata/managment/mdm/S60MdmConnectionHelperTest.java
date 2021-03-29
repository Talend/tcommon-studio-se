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
package org.talend.metadata.managment.mdm;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.times;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.service.IMDMWebServiceHook;
import org.talend.core.utils.ReflectionUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ReflectionUtils.class, Logger.class, GlobalServiceRegister.class })
@PowerMockIgnore({ "javax.crypto.*", "org.eclipse.osgi.*" })
public class S60MdmConnectionHelperTest {

    @Test
    public void testCheckConnection() throws Exception {
        final String username = "username";
        final String password = "password";
        final String serverUrl = "http://localhost:8180/talendmdm/services?wsdl";

        // mock BindingProvider
        Map<String, Object> requestContext = new HashMap<>();
        BindingProvider mockStub = Mockito.mock(BindingProvider.class);
        Mockito.when(mockStub.getRequestContext()).thenReturn(requestContext);

        PowerMockito.mockStatic(ReflectionUtils.class);
        Object mockServiceService = PowerMockito.mock(Object.class);
        PowerMockito
                .when(ReflectionUtils.newInstance("org.talend.mdm.webservice.TMDMService_Service",
                        getClass().getClassLoader(), new Object[] { new URL(serverUrl) }))
                .thenReturn(mockServiceService);
        PowerMockito.when(ReflectionUtils.class, "invokeMethod", same(mockServiceService), eq("getTMDMPort"), any(Object[].class),
                any(Class[].class))
                .thenReturn(mockStub);
        
        // mock IMDMWebServiceHook
        IMDMWebServiceHook mockWebServiceHook = Mockito.mock(IMDMWebServiceHook.class);

        PowerMockito.mockStatic(GlobalServiceRegister.class);
        GlobalServiceRegister mockGlobalServiceRegister = PowerMockito.mock(GlobalServiceRegister.class);
        PowerMockito.when(GlobalServiceRegister.getDefault()).thenReturn(mockGlobalServiceRegister);
        PowerMockito.when(GlobalServiceRegister.getDefault().isServiceRegistered(IMDMWebServiceHook.class)).thenReturn(true);
        PowerMockito.when(GlobalServiceRegister.getDefault().getService(IMDMWebServiceHook.class)).thenReturn(mockWebServiceHook);

        // call & verify
        S60MdmConnectionHelper helper = new S60MdmConnectionHelper();
        helper.checkConnection(serverUrl, null, username, password);
        Mockito.verify(mockWebServiceHook, times(1)).preRequestSendingHook(any(Map.class), anyString());
    }
}
