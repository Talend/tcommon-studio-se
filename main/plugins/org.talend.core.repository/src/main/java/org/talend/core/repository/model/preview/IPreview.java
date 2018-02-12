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
package org.talend.core.repository.model.preview;

import org.eclipse.core.runtime.CoreException;
import org.talend.core.utils.CsvArray;
import org.talend.designer.runprocess.ProcessorException;

/**
 * Generates the preview of a file delimited input.
 * 
 * $Id: IPreview.java 1 2006-09-29 17:06:40 +0000 (星期五, 29 九月 2006) nrousseau $
 * 
 */
public interface IPreview {

    /**
     * Generates the preview of a file delimited input.
     * 
     * @param description Description a the input file.
     * @return The content of the preview, null in case of errors.
     * @throws CoreException
     * @throws ProcessorException
     */
    CsvArray preview(IProcessDescription description, String type) throws CoreException;

    /**
     * 
     * DOC YeXiaowei Comment method "preview".
     * 
     * @param description
     * @param type
     * @param errorOutputAsException
     * @return
     * @throws CoreException
     */
    CsvArray preview(IProcessDescription description, String type, boolean errorOutputAsException) throws CoreException;

    /**
     * Stop loading preview.
     */
    public void stopLoading();

    public boolean isTopPreview();
}
