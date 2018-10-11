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
package org.talend.core.utils;

import java.io.ByteArrayInputStream;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @author hwang
 *
 */
public class TemplateFileUtils {
	
	public static final String LAUNCHER_ALL = "all"; //$NON-NLS-1$
	
	public static final String UNIX_ENVIRONMENT = "Unix"; //$NON-NLS-1$

    public static final String WINDOWS_ENVIRONMENT = "Windows"; //$NON-NLS-1$
	
	public static final String UNIX_LAUNCHER = "run.sh"; //$NON-NLS-1$

	public static final String WINDOWS_LAUNCHER = "run.bat"; //$NON-NLS-1$
	
	public static final String POWER_SHELL = "run.ps1"; //$NON-NLS-1$
	
	public static String handleAssemblyJobTemplate(String content, String launcher) throws DocumentException{
		if(launcher == null || launcher.equalsIgnoreCase(LAUNCHER_ALL)){
			return content;
		}
		ByteArrayInputStream source = new ByteArrayInputStream(content.getBytes());
		SAXReader reader = new SAXReader();   
		Document doc = reader.read(source);   
		Element root = doc.getRootElement();   
		Element files = root.element("files"); //$NON-NLS-1$
		if(files == null){
			return content;
		}
		for (Iterator j = files.elementIterator("file"); j.hasNext();) { //$NON-NLS-1$
			Element file = (Element) j.next();
			Element destName = file.element("destName"); //$NON-NLS-1$
			if(launcher.equalsIgnoreCase(UNIX_ENVIRONMENT)){
				if(destName.getStringValue().endsWith(WINDOWS_LAUNCHER)){
					files.remove(file);
				}
				if(destName.getStringValue().endsWith(POWER_SHELL)){
					files.remove(file);
				}
			}else if(launcher.equalsIgnoreCase(WINDOWS_ENVIRONMENT)){
				if(destName.getStringValue().endsWith(UNIX_LAUNCHER)){
					files.remove(file);
				}
			}
		}
		return root.asXML();
	}

}
