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
import org.eclipse.core.runtime.ListenerList;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.repository.model.preview.IPreview;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 * @param <R> result of preview
 */
public class AsynchronousPreviewHandler<R> {

    private IPreview preview;

    private R result;

    private ListenerList listeners = new ListenerList();

    private boolean previewStopped;

    /**
     * DOC amaumont PreviewHandler constructor comment.
     */
    public AsynchronousPreviewHandler(IPreview preview) {
        super();
        this.preview = preview;
    }

    /**
     * DOC amaumont Comment method "launchSynchronousPreview".
     * 
     * @throws CoreException
     */
    public void launchPreview(final ProcessDescription processDescription, final String type) {

        this.result = null;

        this.previewStopped = false;

        Thread thread = new Thread() {

            /*
             * (non-Javadoc)
             * 
             * @see java.lang.Thread#run()
             */
            @SuppressWarnings("unchecked")//$NON-NLS-1$
            @Override
            public void run() {
                result = null;
                try {
                    PreviewHandlerEvent event = new PreviewHandlerEvent(PreviewHandlerEvent.TYPE.PREVIEW_STARTED,
                            AsynchronousPreviewHandler.this);
                    fireEvent(event);

                    result = (R) preview.preview(processDescription, type);

                    event = new PreviewHandlerEvent(PreviewHandlerEvent.TYPE.PREVIEW_ENDED, AsynchronousPreviewHandler.this);
                    fireEvent(event);

                } catch (CoreException e) {
                    if (!previewStopped) {
                        PreviewHandlerEvent event = new PreviewHandlerEvent(PreviewHandlerEvent.TYPE.PREVIEW_IN_ERROR,
                                AsynchronousPreviewHandler.this, e);
                        fireEvent(event);
                        ExceptionHandler.process(e);
                    }

                } finally {

                    if (previewStopped) {
                        PreviewHandlerEvent event = new PreviewHandlerEvent(PreviewHandlerEvent.TYPE.PREVIEW_INTERRUPTED,
                                AsynchronousPreviewHandler.this);
                        fireEvent(event);
                    }

                }
            }

        };
        thread.start();
    }

    /**
     * Stop the preview process.
     */
    public void stopPreviewProcess() {
        previewStopped = true;
        preview.stopLoading();

    }

    /**
     * Getter for result.
     * 
     * @return the result
     */
    public R getResult() {
        return this.result;
    }

    public void addListener(IPreviewHandlerListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(IPreviewHandlerListener listener) {
        this.listeners.remove(listener);
    }

    /**
     * DOC amaumont Comment method "fireEvent".
     * 
     * @param event
     */
    protected void fireEvent(PreviewHandlerEvent event) {
        final Object[] listenerArray = listeners.getListeners();
        for (int i = 0; i < listenerArray.length; i++) {
            ((IPreviewHandlerListener) listenerArray[i]).handleEvent(event);
        }

    }

}
