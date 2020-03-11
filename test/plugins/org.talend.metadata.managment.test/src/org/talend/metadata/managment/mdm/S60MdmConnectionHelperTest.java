package org.talend.metadata.managment.mdm;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.talend.core.utils.ReflectionUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ReflectionUtils.class, Logger.class })
@PowerMockIgnore("javax.crypto.*")
public class S60MdmConnectionHelperTest {

    @Test
    public void testCheckConnection() throws Exception {
        final String username = "username";
        final String password = "password";
        final String serverUrl = "http://localhost:8180/talendmdm/services?wsdl";

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

        S60MdmConnectionHelper helper = new S60MdmConnectionHelper();
        BindingProvider stub = (BindingProvider) helper.checkConnection(serverUrl, null, username, password);
        Map<String, Object> _requestContext = stub.getRequestContext();
        assertTrue(!_requestContext.isEmpty());
        assertTrue(_requestContext.containsKey(MessageContext.HTTP_REQUEST_HEADERS));
        Object obj = _requestContext.get(MessageContext.HTTP_REQUEST_HEADERS);
        assertTrue(obj instanceof Map);
        Map<String, List<String>> headers = (Map<String, List<String>>) obj;
        assertTrue(headers.containsKey("t_stoken"));
        assertTrue(!headers.get("t_stoken").isEmpty());
    }
}
