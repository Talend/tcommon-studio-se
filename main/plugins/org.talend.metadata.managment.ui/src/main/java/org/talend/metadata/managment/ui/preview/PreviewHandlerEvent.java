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
package org.talend.metadata.managment.ui.preview;

import org.eclipse.core.runtime.CoreException;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 * @param <R> result of preview
 */
public class PreviewHandlerEvent<R> {

    /**
     * Type of the preview handler event. <br/>
     * 
     * $Id$
     * 
     */
    public enum TYPE {
        PREVIEW_STARTED,
        PREVIEW_ENDED,
        PREVIEW_INTERRUPTED,
        PREVIEW_IN_ERROR,
    }

    private TYPE type;

    private AsynchronousPreviewHandler<R> source;

    private CoreException exception;

    /**
     * DOC amaumont PreviewHandlerEvent constructor comment.
     */
    public PreviewHandlerEvent(TYPE type, AsynchronousPreviewHandler<R> source) {
        super();
        this.type = type;
        this.source = source;
    }

    /**
     * DOC amaumont PreviewHandlerEvent constructor comment.
     * 
     * @param preview_in_error
     * @param name
     * @param e
     */
    public PreviewHandlerEvent(TYPE type, AsynchronousPreviewHandler<R> source, CoreException e) {
        this(type, source);
        this.exception = e;
    }

    /**
     * Getter for type.
     * 
     * @return the type
     */
    public TYPE getType() {
        return this.type;
    }

    /**
     * Getter for source.
     * 
     * @return the source
     */
    public AsynchronousPreviewHandler<R> getSource() {
        return this.source;
    }

    /**
     * Getter for exception.
     * 
     * @return the exception
     */
    public CoreException getException() {
        return this.exception;
    }

}
