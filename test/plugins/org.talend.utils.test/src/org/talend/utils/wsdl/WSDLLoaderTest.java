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
package org.talend.utils.wsdl;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.Map;

import org.junit.Test;

public class WSDLLoaderTest {

	@Test
	public void testCircularDependencyLoading() throws Exception {
		WSDLLoader wsdlLoader = new WSDLLoader();
		String wsdlLocation = getClass().getResource("TestService.wsdl").toExternalForm();
		String fileNameTemplate = "Test.%d.wsdl";
		Map<String, InputStream> result = wsdlLoader.load(wsdlLocation, fileNameTemplate);
		assertEquals(3, result.size());
		String resA = readWsdlStream(result.get(WSDLLoader.DEFAULT_FILENAME));
		assertTrue(resA.indexOf("wsdl:import") > 0);
		assertTrue(resA.indexOf("location=\"Test.0.wsdl\"") > 0);
		String resB = readWsdlStream(result.get("Test.0.wsdl"));
		assertTrue(resB.indexOf("wsdl:import") > 0);
		assertTrue(resB.indexOf("location=\"Test.1.wsdl\"") > 0);
		String resC = readWsdlStream(result.get("Test.1.wsdl"));
		// assertTrue(resC.indexOf("wsdl:import") > 0);
		// assertTrue(resC.indexOf("location=\"Test.0.wsdl\"") > 0);
		assertEquals(resA, resC);
	}

	private String readWsdlStream(InputStream wsdlStream) throws Exception {
		assertNotNull(wsdlStream);
		byte[] buf = new byte[2048];
		int len = wsdlStream.read(buf);
		assertTrue(len > 0);
		return new String(buf, 0, len, "UTF8");
	}
}
