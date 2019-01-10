package org.talend.metadata.managment.ui.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.talend.core.model.metadata.builder.connection.AdditionalConnectionProperty;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.SAPConnection;
import org.talend.daikon.security.CryptoHelper;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;

public class OtherConnectionContextUtilsTest {

    private ContextType context;

    private SAPConnection source;
    
    private CryptoHelper cryptoHelper;

    @Before
    public void setUp() throws Exception {
        cryptoHelper = CryptoHelper.getDefault();
        context = createContextTypeForCloneSAPConnection();
        source = createConnectionForCloneSAPConnection();
    }

    @Test
    public void testCloneOriginalValueSAPConnection() {
        SAPConnection target = OtherConnectionContextUtils.cloneOriginalValueSAPConnection(source, context);
        assertEquals("001", target.getClient());
        assertEquals("a.b.c.d", target.getHost());
        assertEquals("Talend", target.getUsername());
        assertEquals("123456", target.getValue(target.getPassword(), false));
        assertEquals("00", target.getSystemNumber());
        assertEquals("EN", target.getLanguage());
        assertTrue(target.getAdditionalProperties()!= null && !target.getAdditionalProperties().isEmpty());
        AdditionalConnectionProperty property = target.getAdditionalProperties().get(0);
        assertEquals("jco.client.saprouter", property.getPropertyName());
        assertEquals("/H/sr2.cimt.de/S/3299/W/Talend2015!/H/", property.getValue());
    }

    private ContextType createContextTypeForCloneSAPConnection() {
        context = TalendFileFactory.eINSTANCE.createContextType();
        createContextParameter("SAPBW_Client", "001");
        createContextParameter("SAPBW_Host", "a.b.c.d");
        createContextParameter("SAPBW_Username", "Talend");
        ContextParameterType param = createContextParameter("SAPBW_Password", cryptoHelper.encrypt("123456"));
        param.setType("id_Password");
        param.setRawValue("123456");
        createContextParameter("SAPBW_SystemNumber", "00");
        createContextParameter("SAPBW_Language", "EN");
        createContextParameter("jco_client_saprouter", "/H/sr2.cimt.de/S/3299/W/Talend2015!/H/");
        return context;
    }

    private SAPConnection createConnectionForCloneSAPConnection() {
        source = ConnectionFactory.eINSTANCE.createSAPConnection();
        source.setContextMode(true);
        source.setClient("context.SAPBW_Client");
        source.setHost("context.SAPBW_Host");
        source.setUsername("context.SAPBW_Username");
        source.setPassword("context.SAPBW_Password");
        source.setSystemNumber("context.SAPBW_SystemNumber");
        source.setLanguage("context.SAPBW_Language");
        AdditionalConnectionProperty property = ConnectionFactory.eINSTANCE.createAdditionalConnectionProperty();
        property.setPropertyName("jco.client.saprouter");
        property.setValue("context.jco_client_saprouter");
        source.getAdditionalProperties().add(property);
        return source;
    }
    
    private ContextParameterType createContextParameter(String name, String value) {
        ContextParameterType param = TalendFileFactory.eINSTANCE.createContextParameterType();
        param.setName(name);
        param.setValue(value);
        context.getContextParameter().add(param);
        return param;
    }

}
