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
package org.talend.designer.maven.model;

/**
 * created by ggu on 22 Jan 2015 Detailled comment
 *
 */
public class ProjectSystemFolder {

    /** Folder path */
    private String path = null;

    /** Output path */
    private String outputPath = null;

    public ProjectSystemFolder(String path) {
        this.path = path;
    }

    public ProjectSystemFolder(String path, String outputPath) {
        this(path);
        this.outputPath = outputPath;
    }

    public String getPath() {
        return this.path;
    }

    public String getOutputPath() {
        return this.outputPath;
    }

    /**
     * Returns true for source folder
     */
    boolean isSourceEntry() {
        return this.getOutputPath() != null;
    }

}
