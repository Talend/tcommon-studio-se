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
package org.talend.core.model.genhtml;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.talend.commons.exception.ExceptionHandler;

/**
 * DOC Administrator class global comment. Detailled comment <br/>
 * 
 */
public class XMLHandler {

    /**
     * Generates xml file base on inputted path, file path and an instance of <code>Document</code>
     * 
     * @param tempFolderPath
     * @param filePath
     * @param document
     */
    public static void generateXMLFile(String tempFolderPath, String filePath, Document document) {
        XMLWriter output = null;
        FileOutputStream out = null;
        Writer writer = null;

        try {
            // OutputFormat format = OutputFormat.createPrettyPrint();
            out = new java.io.FileOutputStream(filePath);
            writer = new OutputStreamWriter(out, "UTF-8"); //$NON-NLS-1$

            OutputFormat format = OutputFormat.createPrettyPrint();
            output = new XMLWriter(writer, format) {

                /*
                 * (non-Javadoc)
                 * 
                 * @see org.dom4j.io.XMLWriter#writeDeclaration()
                 */
                @Override
                protected void writeDeclaration() throws IOException {
                    OutputFormat formatTmp = this.getOutputFormat();
                    String encoding = formatTmp.getEncoding();

                    // Only print of declaration is not suppressed
                    if (!formatTmp.isSuppressDeclaration()) {
                        // Assume 1.0 version
                        if (encoding.equals("UTF8")) { //$NON-NLS-1$
                            writer.write("<?xml version=\"1.1\""); //$NON-NLS-1$

                            if (!formatTmp.isOmitEncoding()) {
                                writer.write(" encoding=\"UTF-8\""); //$NON-NLS-1$
                            }

                            writer.write("?>"); //$NON-NLS-1$
                        } else {
                            writer.write("<?xml version=\"1.1\""); //$NON-NLS-1$

                            if (!formatTmp.isOmitEncoding()) {
                                writer.write(" encoding=\"" + encoding + "\""); //$NON-NLS-1$ //$NON-NLS-2$
                            }

                            writer.write("?>"); //$NON-NLS-1$
                        }

                        if (formatTmp.isNewLineAfterDeclaration()) {
                            println();
                        }
                    }
                }
            };
            output.setMaximumAllowedCharacter(127);
            output.write(document);
            output.flush();
        } catch (Exception e) {
            ExceptionHandler.process(e);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }

        }
    }

}
