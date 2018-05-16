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
package org.talend.core.language;

import org.talend.core.runtime.i18n.Messages;

/**
 * Enum for available Code Languages in the application.
 * 
 * $Id: ECodeLanguage.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public enum ECodeLanguage {
    PERL("perl", "perl", "Perl"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    JAVA("java", "java", "Java"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    private ECodeLanguage(String name, String extension, String caseName) {
        this.name = name;
        this.extension = extension;
        this.caseName = caseName;
    }

    public static ECodeLanguage getCodeLanguage(String name) {
        for (ECodeLanguage codeLanguage : ECodeLanguage.values()) {
            if (codeLanguage.getName().equals(name)) {
                return codeLanguage;
            }
        }
        throw new UnsupportedOperationException(Messages.getString("ECodeLanguage.unknowLanguage")); //$NON-NLS-1$
    }

    private String name;

    private String extension;

    private String caseName;

    /**
     * Getter for extension.
     * 
     * @return the extension
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Sets the extension.
     * 
     * @param extension the extension to set
     */
    public void setExtension(String extension) {
        this.extension = extension;
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

    public String getCaseName() {
        return this.caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }
}
