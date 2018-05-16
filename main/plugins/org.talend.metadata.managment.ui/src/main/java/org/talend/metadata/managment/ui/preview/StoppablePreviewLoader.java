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
package org.talend.metadata.managment.ui.preview;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Label;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.swt.dialogs.EventLoopProgressMonitor;
import org.talend.metadata.managment.ui.i18n.Messages;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 * @param <R> result of preview
 */
public abstract class StoppablePreviewLoader<R> {

    private Label infoLabel;

    private IPreviewHandlerListener previewHandlerListener;

    // private EventLoopProgressMonitor monitorWrap;
    private IProgressMonitor monitorWrap;

    protected boolean breakInfiniteLoop;

    private AsynchronousPreviewHandler<R> previewHandler;

    /**
     * DOC amaumont StoppablePreviewLoader constructor comment.
     * 
     * @param xmlFilePreview
     * @param infoLabel
     */
    public StoppablePreviewLoader(AsynchronousPreviewHandler<R> previewHandler, Label infoLabel) {
        this.infoLabel = infoLabel;
        this.previewHandler = previewHandler;
    }

    /**
     * DOC amaumont Comment method "load".
     * 
     * @param processDescription
     */
    public void load(final ProcessDescription processDescription) {

        if (previewHandler != null && previewHandlerListener == null) {
            previewHandlerListener = new IPreviewHandlerListener<R>() {

                @Override
                public void handleEvent(final PreviewHandlerEvent<R> event) {

                    infoLabel.getDisplay().asyncExec(new Runnable() {

                        @Override
                        public void run() {

                            executePreviewHandlerEvent(event);

                        }
                    });

                }

            };
        }

        previewHandler.addListener(previewHandlerListener);

        infoLabel.getDisplay().syncExec(new Runnable() {

            @Override
            public void run() {
                previewHandler.launchPreview(processDescription, getShadowProcessType());
            }

        });

    }

    protected String getShadowProcessType() {
        return "FILE_XML"; //$NON-NLS-1$
    }

    /**
     * DOC amaumont Comment method "executePreviewHandlerEvent".
     * 
     * @param event
     */
    private void executePreviewHandlerEvent(final PreviewHandlerEvent<R> event) {
        final PreviewHandlerEvent.TYPE eventType = event.getType();
        final AsynchronousPreviewHandler<R> source = event.getSource();

        if (eventType == PreviewHandlerEvent.TYPE.PREVIEW_STARTED) {

            final IRunnableWithProgress op = new IRunnableWithProgress() {

                @Override
                public void run(final IProgressMonitor monitor) {
                    infoLabel.getDisplay().asyncExec(new Runnable() {

                        @Override
                        public void run() {

                            monitorWrap = new EventLoopProgressMonitor(monitor);

                            monitorWrap.beginTask("Loading preview...", IProgressMonitor.UNKNOWN); //$NON-NLS-1$

                            breakInfiniteLoop = false;

                            while (true) {
                                if (monitorWrap.isCanceled() || breakInfiniteLoop) {
                                    break;
                                }
                            }

                            if (monitorWrap.isCanceled()) {
                                source.stopPreviewProcess();
                            } else {
                                monitorWrap.done();
                            }

                        }
                    });
                }
            };

            infoLabel.getDisplay().syncExec(new Runnable() {

                @Override
                public void run() {

                    try {
                        new ProgressMonitorDialog(infoLabel.getShell()).run(true, true, op);
                    } catch (InvocationTargetException e) {
                        ExceptionHandler.process(e);
                    } catch (InterruptedException e) {
                        ExceptionHandler.process(e);
                    }
                }

            });

        } else if (eventType == PreviewHandlerEvent.TYPE.PREVIEW_INTERRUPTED) {

            breakInfiniteLoop = true;

            infoLabel.setText("   " + Messages.getString("StoppablePreviewLoader.inforLabelText.previewInterrupted")); //$NON-NLS-1$ //$NON-NLS-2$

        } else if (eventType == PreviewHandlerEvent.TYPE.PREVIEW_ENDED) {

            breakInfiniteLoop = true;

            infoLabel.setText("   " + Messages.getString("FileStep2.previewIsDone")); //$NON-NLS-1$ //$NON-NLS-2$
            infoLabel.getParent().layout();
            R result = source.getResult();
            // refresh TablePreview on this step
            previewEnded(result);

        } else if (eventType == PreviewHandlerEvent.TYPE.PREVIEW_IN_ERROR) {

            breakInfiniteLoop = true;

            final CoreException e = event.getException();
            previewInError(e);

        }
    }

    /**
     * DOC amaumont Comment method "previewEnded".
     * 
     * @param xmlArrayAsynchronousPreviewHandler<XmlArray>
     */
    protected abstract void previewEnded(R result);

    /**
     * DOC amaumont Comment method "previewInError".
     * 
     * @param e
     */
    public abstract void previewInError(CoreException e);

}
