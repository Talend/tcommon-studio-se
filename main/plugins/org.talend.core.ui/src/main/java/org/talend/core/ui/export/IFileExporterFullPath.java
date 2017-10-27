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
package org.talend.core.ui.export;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;

/**
 * Interface for file exporters of different file formats. Used by the zip and tar.gz exporters.
 * 
 * @since 3.1
 */
public interface IFileExporterFullPath {

    /**
     * Do all required cleanup now that we are finished with the currently-open file.
     * 
     * @throws IOException
     */
    public void finished() throws IOException;

    /**
     * Write the passed resource to the current archive
     * 
     * @param resource
     * @param destinationPath
     * @throws IOException
     * @throws CoreException
     */
    public void write(String resource, String destinationPath) throws IOException, CoreException;

    public void writeFolder(String destinationPath) throws IOException, CoreException;

}
