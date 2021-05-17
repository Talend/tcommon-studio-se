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
package org.talend.librariesmanager.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;

/*
 * Created by bhe on Mar 8, 2020
 */
public class PomParser {

    private static final Logger LOGGER = Logger.getLogger(PomParser.class.getCanonicalName());

    private final Document doc;

    public PomParser(Document doc) {
        this.doc = doc;
    }

    public String getGroupId() {
        String val = "";
        if (this.doc != null) {
            XPath path = XPathFactory.newInstance().newXPath();
            try {
                String name = path.evaluate("/project/groupId", this.doc);
                if (StringUtils.isEmpty(name)) {
                    name = path.evaluate("/project/parent/groupId", this.doc);
                }
                if (!StringUtils.isEmpty(name)) {
                    val = name;
                }
            } catch (XPathExpressionException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        return val;
    }

    public String getArtifactId() {
        String val = "";
        if (this.doc != null) {
            XPath path = XPathFactory.newInstance().newXPath();
            try {
                String name = path.evaluate("/project/artifactId", this.doc);
                if (!StringUtils.isEmpty(name)) {
                    val = name;
                }
            } catch (XPathExpressionException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        return val;
    }

    public String getVersion() {
        String val = "";
        if (this.doc != null) {
            XPath path = XPathFactory.newInstance().newXPath();
            try {
                String name = path.evaluate("/project/version", this.doc);
                if (StringUtils.isEmpty(name)) {
                    name = path.evaluate("/project/parent/version", this.doc);
                }
                if (!StringUtils.isEmpty(name)) {
                    val = name;
                }
            } catch (XPathExpressionException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        return val;
    }

    public String getPackaging() {
        String val = "";
        if (this.doc != null) {
            XPath path = XPathFactory.newInstance().newXPath();
            try {
                String name = path.evaluate("/project/packaging", this.doc);
                if (!StringUtils.isEmpty(name)) {
                    val = name;
                }
            } catch (XPathExpressionException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        if (val.isEmpty() || val.equals("bundle")) {
            val = "jar";
        }
        return val;
    }
}
