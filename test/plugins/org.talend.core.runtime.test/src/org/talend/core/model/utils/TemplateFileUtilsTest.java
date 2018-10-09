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
package org.talend.core.model.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.junit.Assert;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.talend.core.utils.TemplateFileUtils;

/**
 * @author hwang
 *
 */
public class TemplateFileUtilsTest {
	
	@Test
    public void testHhandleAssemblyJobTemplate() throws URISyntaxException, IOException, DocumentException {
		File file = getTemplateFile();
		try {
			FileInputStream is = new FileInputStream(file);
			if (is != null) {
	            try {
	            	StringWriter sw = new StringWriter(1000);
	                int c = 0;
	                while ((c = is.read()) != -1) {
	                    sw.write(c);
	                }
	                String content = sw.toString();
	                test1(content, "all");
	                test2(content, null);
	                test3(content, "Unix");
	                test4(content, "Windows");
	            } finally {
					is.close();
	            }
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void test1(String content, String launcher) throws DocumentException{
		Map<String, Element> map = test(content, launcher);
		Assert.assertTrue(map.size() == 3);
		Assert.assertTrue(map.get(TemplateFileUtils.WINDOWS_LAUNCHER) != null);
		Assert.assertTrue(map.get(TemplateFileUtils.POWER_SHELL) != null);
		Assert.assertTrue(map.get(TemplateFileUtils.UNIX_LAUNCHER) != null);
	}
	
	private void test2(String content, String launcher) throws DocumentException{
		Map<String, Element> map = test(content, launcher);
		Assert.assertTrue(map.size() == 3);
		Assert.assertTrue(map.get(TemplateFileUtils.WINDOWS_LAUNCHER) != null);
		Assert.assertTrue(map.get(TemplateFileUtils.POWER_SHELL) != null);
		Assert.assertTrue(map.get(TemplateFileUtils.UNIX_LAUNCHER) != null);
	}

	private void test3(String content, String launcher) throws DocumentException{
		Map<String, Element> map = test(content, launcher);
		Assert.assertTrue(map.size() == 1);
		Assert.assertTrue(map.get(TemplateFileUtils.UNIX_LAUNCHER) != null);
	}
	
	private void test4(String content, String launcher) throws DocumentException{
		Map<String, Element> map = test(content, launcher);
		Assert.assertTrue(map.size() == 2);
		Assert.assertTrue(map.get(TemplateFileUtils.WINDOWS_LAUNCHER) != null);
		Assert.assertTrue(map.get(TemplateFileUtils.POWER_SHELL) != null);
	}
	
	private Map<String, Element> test(String content, String launcher) throws DocumentException{
		Map<String, Element> map = new HashMap<String, Element>();
		String result = TemplateFileUtils.handleAssemblyJobTemplate(content, launcher);
		
		ByteArrayInputStream source = new ByteArrayInputStream(result.getBytes());
		SAXReader reader = new SAXReader();   
		Document doc = reader.read(source);   
		Element root = doc.getRootElement();   
		Element files = root.element("files"); //$NON-NLS-1$
		
		for (Iterator j = files.elementIterator("file"); j.hasNext();) { //$NON-NLS-1$
			Element file = (Element) j.next();
			Element destName = file.element("destName"); //$NON-NLS-1$
			
			if(destName.getStringValue().endsWith(TemplateFileUtils.WINDOWS_LAUNCHER)){
				map.put(TemplateFileUtils.WINDOWS_LAUNCHER, file);
			}
			if(destName.getStringValue().endsWith(TemplateFileUtils.UNIX_LAUNCHER)){
				map.put(TemplateFileUtils.UNIX_LAUNCHER, file);
			}
			if(destName.getStringValue().endsWith(TemplateFileUtils.POWER_SHELL)){
				map.put(TemplateFileUtils.POWER_SHELL, file);
			}
		}
		return map;
	}
	
	private File getTemplateFile() throws URISyntaxException, IOException {
		URI testFileToDeploy = FileLocator.toFileURL(this.getClass().getClassLoader().
				getResource("resources/template.xml")).toURI();//$NON-NLS-1$
		return new File(testFileToDeploy.getPath());
    }

}
