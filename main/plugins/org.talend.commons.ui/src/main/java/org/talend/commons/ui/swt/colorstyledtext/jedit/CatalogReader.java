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
package org.talend.commons.ui.swt.colorstyledtext.jedit;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: CatalogReader.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class CatalogReader {

    /**
     * 
     */
    private static final int A_50 = 50;

    public CatalogReader() {
        super();
    }

    public Mode[] read(String filename) {
        try {
            return readFile(CommonsPlugin.getFileInputStream(filename));
        } catch (Exception e) {
            // EditorPlugin.logError("Error reading catalog file " + mode.getFile(), e);
            // e.printStackTrace();
            ExceptionHandler.process(e);
            return new Mode[0];
        }
    }

    public Mode[] readFile(InputStream file) throws DocumentException, IOException {
        SAXReader reader = new SAXReader();
        Document doc = null;
        doc = reader.read(file);
        Element root = doc.getRootElement();
        List modeE = root.elements("MODE"); //$NON-NLS-1$
        List<Mode> modes = new ArrayList<Mode>(A_50);
        for (Iterator iter = modeE.iterator(); iter.hasNext();) {
            Element modeElement = (Element) iter.next();
            if (!Platform.getOS().equals(Platform.OS_AIX)) {
                modes.add(newMode(modeElement));
            }
        }
        return (Mode[]) modes.toArray(new Mode[modes.size()]);
    }

    private Mode newMode(Element modeElement) {
        return Mode.newMode(modeElement.attributeValue("NAME"), modeElement.attributeValue("FILE"), modeElement //$NON-NLS-1$ //$NON-NLS-2$
                .attributeValue("FILE_NAME_GLOB"), modeElement.attributeValue("FIRST_LINE_GLOB")); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * This method is used to create the "extensions" attribute for the editor XML.
     */
    // public static void main(String[] args) {
    // Set list = getListOfExtensions();
    // StringBuffer sb = new StringBuffer("extensions=\"");
    // for (Iterator iter = list.iterator(); iter.hasNext();) {
    // String ext = (String) iter.next();
    // sb.append(ext);
    // sb.append(",");
    // }
    // sb.setLength(sb.length() - 1);
    // sb.append("\"");
    // System.out.println(sb.toString());
    // }
    //
    // public static Set getListOfExtensions() {
    // CatalogReader c = new CatalogReader();
    // Mode[] modes = null;
    // try {
    // modes = c.readFile(new File("C:/workspaces/big/cbg.editor/modes/catalog"));
    // } catch (DocumentException e) {
    // // ignore
    // } catch (IOException e) {
    // // ignore
    // }
    // Set list = new TreeSet();
    // for (int i = 0; i < modes.length; i++) {
    // Mode mode = modes[i];
    // mode.appendExtensionsOnto(list);
    // }
    // return list;
    // }
}
