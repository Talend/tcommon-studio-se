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
package org.talend.core.model.metadata;

import org.talend.core.runtime.i18n.Messages;

/**
 * Enum for available Code Languages in the application.
 * 
 * $Id: EMetadataEncoding.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public enum EMetadataEncoding {

    UTF8("UTF-8", Messages.getString("EMetadataEncoding.UTF8")), //$NON-NLS-1$ //$NON-NLS-2$
    UTF16("UTF-16", Messages.getString("EMetadataEncoding.UTF16")), //$NON-NLS-1$ //$NON-NLS-2$
    UTF16LE("UTF-16LE", Messages.getString("EMetadataEncoding.UTF16LE")), //$NON-NLS-1$ //$NON-NLS-2$
    UTF16BE("UTF-16BE", Messages.getString("EMetadataEncoding.UTF16BE")), //$NON-NLS-1$ //$NON-NLS-2$
    UTF7("UTF-7", Messages.getString("EMetadataEncoding.UTF7")), //$NON-NLS-1$ //$NON-NLS-2$
    ISO88591("ISO-8859-1", Messages.getString("EMetadataEncoding.Latin1")), //$NON-NLS-1$ //$NON-NLS-2$
    ISO88592("ISO-8859-2", Messages.getString("EMetadataEncoding.Latin2")), //$NON-NLS-1$ //$NON-NLS-2$
    ISO88593("ISO-8859-3", Messages.getString("EMetadataEncoding.Latin3")), //$NON-NLS-1$ //$NON-NLS-2$
    ISO88594("ISO-8859-4", Messages.getString("EMetadataEncoding.Latin4")), //$NON-NLS-1$ //$NON-NLS-2$
    ISO88595("ISO-8859-5", Messages.getString("EMetadataEncoding.Cyrillic")), //$NON-NLS-1$ //$NON-NLS-2$
    ISO88596("ISO-8859-6", Messages.getString("EMetadataEncoding.Arabic")), //$NON-NLS-1$ //$NON-NLS-2$
    ISO88597("ISO-8859-7", Messages.getString("EMetadataEncoding.Greek")), //$NON-NLS-1$ //$NON-NLS-2$
    ISO88598("ISO-8859-8", Messages.getString("EMetadataEncoding.Hebrew")), //$NON-NLS-1$ //$NON-NLS-2$
    ISO88599("ISO-8859-9", Messages.getString("EMetadataEncoding.Latin5")), //$NON-NLS-1$ //$NON-NLS-2$
    ISO885910("ISO-8859-10", Messages.getString("EMetadataEncoding.Latin6")), //$NON-NLS-1$ //$NON-NLS-2$
    ISO885915("ISO-8859-15", Messages.getString("EMetadataEncoding.Latin9")), //$NON-NLS-1$ //$NON-NLS-2$
    windows1252("windows-1252", Messages.getString("EMetadataEncoding.Microsoft_Latin1")), //$NON-NLS-1$ //$NON-NLS-2$
    BIG5("BIG5", Messages.getString("EMetadataEncoding.Big5")), //$NON-NLS-1$ //$NON-NLS-2$
    GB18030("GB18030", Messages.getString("EMetadataEncoding.GB18030")), //$NON-NLS-1$ //$NON-NLS-2$
    GB2312("GB2312", Messages.getString("EMetadataEncoding.GB2312")), //$NON-NLS-1$ //$NON-NLS-2$
    EUC_CN("EUC_CN", Messages.getString("EMetadataEncoding.EUC_CN")); //$NON-NLS-1$ //$NON-NLS-2$

    /*
     * EUC_JP Code-JP Unix �tendu Japonais EUC_KR Code-KR Unix �tendu Cor�en EUC_TW Code-TW Unix �tendu Chinois
     * traditionnel, ta�wanais GBK Standard national �tendu Chinois simplifi� ISO 2022-CN ("GB") GB 2312-80
     */

    private EMetadataEncoding(String name, String label) {
        this.name = name;
        this.label = label;
    }

    public static EMetadataEncoding getMetadataEncoding(String name) {
        for (EMetadataEncoding metadataEncoding : EMetadataEncoding.values()) {
            if (metadataEncoding.getName().equals(name)) {
                return metadataEncoding;
            }
        }
        return UTF8;
    }

    private String name;

    private String label;

    /**
     * Getter for Label.
     * 
     * @return the Label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the Label.
     * 
     * @param extension the extension to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Getter for name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

}
