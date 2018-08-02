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
package org.talend.core.model.genhtml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.Path;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.i18n.Messages;

/**
 * This class is used for transfering XML file to HTML file.
 * 
 * $Id: HTMLGenerator.java 2007-3-7,下午04:42:22 ftang $
 * 
 */
public class HTMLHandler {

    // create an instance of TransformerFactory
    private static TransformerFactory transformerFactory = TransformerFactory.newInstance();

    private static Map<String, Transformer> transformerCache = new HashMap<String, Transformer>();

    // fixed size
    private static LRUMap externalNodeFileCache = new LRUMap(10);

    /**
     * This method is used for generating HTML file base on given folder, job name and xsl file name.
     * 
     * @param tempFolderPath a string
     * @param jobName a string
     * @param htmlFileMap
     * @param xslFileName a string
     */
    public static void generateHTMLFile(String tempFolderPath, String xslFilePath, String xmlFilePath, String htmlFilePath,
            Map<String, Object> htmlFileMap) {

        Map<String, Object> nodeHTMLMap = htmlFileMap;
        generateHTMLFile(tempFolderPath, xslFilePath, xmlFilePath, htmlFilePath);

        File originalHtmlFile = new File(htmlFilePath);

        if (!originalHtmlFile.exists()) {
            return;
        }

        BufferedReader mainHTMLReader = null;
        BufferedWriter newMainHTMLWriter = null;
        // BufferedReader externalNodeHTMLReader = null;
        File newMainHTMLFile = null;
        try {

            mainHTMLReader = new BufferedReader(new FileReader(originalHtmlFile));

            newMainHTMLFile = new File(htmlFilePath + "temp"); //$NON-NLS-1$

            newMainHTMLWriter = new BufferedWriter(new FileWriter(newMainHTMLFile));
            String lineStr = ""; //$NON-NLS-1$
            String ended = Messages.getString("HTMLDocGenerator.ended");
            while ((lineStr = mainHTMLReader.readLine()) != null) {
                newMainHTMLWriter.write(lineStr);
                for (String key : htmlFileMap.keySet()) {
                    String compareStr = "<!--" + key + ended+"-->"; // tMap_1ended--> //$NON-NLS-1$ //$NON-NLS-2$
                    if (lineStr.indexOf(compareStr) != -1) {

                        if (htmlFileMap.get(key) instanceof URL) {
                            File externalNodeHTMLFile = new File(((URL) nodeHTMLMap.get(key)).getPath());

                            String content = (String) externalNodeFileCache.get(externalNodeHTMLFile.getAbsolutePath());
                            if (content == null) {
                                content = FileUtils.readFileToString(externalNodeHTMLFile);
                                // put file content into cache
                                externalNodeFileCache.put(externalNodeHTMLFile.getAbsolutePath(), content);
                            }
                            newMainHTMLWriter.write(content);
                        } else if (htmlFileMap.get(key) instanceof String) {
                            newMainHTMLWriter.write((String) htmlFileMap.get(key));
                        }
                    }
                    // htmlFileMap.remove(key);
                }
            }

        } catch (Exception e) {
            ExceptionHandler.process(e);
        } finally {
            try {
                if (mainHTMLReader != null) {
                    mainHTMLReader.close();
                }
                if (newMainHTMLWriter != null) {
                    newMainHTMLWriter.close();
                }
                // if (externalNodeHTMLReader != null) {
                // externalNodeHTMLReader.close();
                // }
            } catch (IOException e) {
                ExceptionHandler.process(e);
            }

            originalHtmlFile.delete();
            newMainHTMLFile.renameTo(new File(htmlFilePath));
            // System.out.println("isWorked= " + isWorked);

            // copy(htmlFilePath + "temp", htmlFilePath);
            //
            // System.out.println("tempFilePath:" + htmlFilePath + "temp");
            // System.out.println("htmlFilePath:" + htmlFilePath);
        }
    }

    /**
     * This method is used for generating HTML file base on given folder, job name and xsl file name.
     * 
     * @param tempFolderPath a string
     * @param jobNameOrComponentName a string
     * @param externalNodeHTMLList
     * @param xslFileName a string
     */
    public static void generateHTMLFile(String tempFolderPath, String xslFilePath, String xmlFilePath, String htmlFilePath) {
        FileOutputStream output = null;
        Writer writer = null;
        try {
            File xmlFile = new File(xmlFilePath);
            javax.xml.transform.Source xmlSource = new javax.xml.transform.stream.StreamSource(xmlFile);

            // will create the path needed
            Path htmlPath = new Path(htmlFilePath);
            File htmlFile = new File(htmlPath.removeLastSegments(1).toPortableString());
            htmlFile.mkdirs();

            output = new FileOutputStream(htmlFilePath);

            // Note that if the are chinese in the file, should set the encoding
            // type to "UTF-8", this is caused by DOM4J.
            writer = new BufferedWriter(new OutputStreamWriter(output, "UTF-8")); //$NON-NLS-1$

            javax.xml.transform.Result result = new javax.xml.transform.stream.StreamResult(writer);

            // clear cache, otherwise won't change style if have the same path as last
            transformerCache.clear();
            // get transformer from cache
            javax.xml.transform.Transformer trans = transformerCache.get(xslFilePath);
            if (trans == null) {
                File xsltFile = new File(xslFilePath);
                javax.xml.transform.Source xsltSource = new javax.xml.transform.stream.StreamSource(xsltFile);
                trans = transformerFactory.newTransformer(xsltSource);
                // put transformer into cache
                transformerCache.put(xslFilePath, trans);
            }

            trans.transform(xmlSource, result);

        } catch (Exception e) {
            ExceptionHandler.process(e);
        }

        finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
            try {
                if (writer != null) {
                    writer.close();
                }

            } catch (Exception e1) {
                ExceptionHandler.process(e1);
            }

        }
    }

    /**
     * This methos is used for coping file from one place to the other.
     * 
     * @param srcFilePath
     * @param destFilePath
     * @throws Exception
     */
    private static void copy(String srcFilePath, String destFilePath) {
        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            byte[] bytearray = new byte[512];
            int len = 0;
            input = new FileInputStream(srcFilePath);
            output = new FileOutputStream(destFilePath);
            while ((len = input.read(bytearray)) != -1) {
                output.write(bytearray, 0, len);
            }

        } catch (Exception fe) {
            ExceptionHandler.process(fe);
        }

        finally {
            if (input != null) {
                try {
                    input.close();
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
        }
    }

    public static void clearExternalNodeFileCache() {
        externalNodeFileCache.clear();
    }
}
