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
package org.talend.core.model.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.i18n.Messages;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * DOC chuger class global comment. Detailled comment <br/>
 * 
 * $Id: XmlArray.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public class XmlArray {

    private static final String TAG_ARRAY = "array"; //$NON-NLS-1$

    private static final String TAG_ROW = "row"; //$NON-NLS-1$

    private static final String TAG_FIELD = "field"; //$NON-NLS-1$

    private static int rowLimit = CoreRuntimePlugin.getInstance().getCoreService().getPreferenceStore()
            .getInt(ITalendCorePrefConstants.PREVIEW_LIMIT);;

    private List<XmlRow> rows;

    /**
     * Set the value of row limit back to default in preference.
     * 
     * yzhang Comment method "setLimitToDefault".
     */
    public static void setLimitToDefault() {
        XmlArray.rowLimit = CoreRuntimePlugin.getInstance().getCoreService().getPreferenceStore()
                .getInt(ITalendCorePrefConstants.PREVIEW_LIMIT);
    }

    /**
     * Sets the rowLimit.
     * 
     * @param rowLimit the rowLimit to set
     */
    public static void setRowLimit(int limit) {
        if (limit < 0) {
            setLimitToDefault();
        } else {
            XmlArray.rowLimit = limit;
        }
    }

    /**
     * Getter for rowLimit.
     * 
     * @return the rowLimit
     */
    public static int getRowLimit() {
        return rowLimit;
    }

    /**
     * Constructs a new XmlArray.
     */
    private XmlArray() {
        super();

        rows = new ArrayList<XmlRow>();
    }

    /**
     * Getter for rows.
     * 
     * @return the rows
     */
    public List<XmlRow> getRows() {
        return this.rows;
    }

    /**
     * Adds a row at the end of the array.
     * 
     * @param row Row to add.
     */
    public void add(XmlRow row) {
        rows.add(row);
    }

    public static XmlArray createFrom(InputStream is) throws IOException, ParserConfigurationException, SAXException {
        XmlArray array = new XmlArray();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(is);
        Node root = document.getFirstChild();
        if (!TAG_ARRAY.equals(root.getNodeName())) {
            throw new IOException(Messages.getString("XmlArray.InvalidFile")); //$NON-NLS-1$
        }

        // Rows
        NodeList rows = root.getChildNodes();
        int counter = 0;
        for (int r = 0; r < rows.getLength(); r++) {
            Node row = rows.item(r);
            if (TAG_ROW.equals(row.getNodeName())) {
                XmlRow xRow = new XmlRow();

                NodeList fields = row.getChildNodes();
                for (int f = 0; f < fields.getLength(); f++) {
                    Node field = fields.item(f);
                    if (TAG_FIELD.equals(field.getNodeName())) {
                        XmlField xField = new XmlField(field.getTextContent());
                        xRow.add(xField);
                    }
                }

                array.add(xRow);
                counter++;
                if (counter >= rowLimit) {
                    break;
                }
            }
        }

        return array;
    }
}
