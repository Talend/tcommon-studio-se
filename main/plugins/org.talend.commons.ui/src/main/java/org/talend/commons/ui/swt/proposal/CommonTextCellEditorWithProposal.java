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
package org.talend.commons.ui.swt.proposal;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.ui.runtime.swt.proposal.IContentProposalExtendedListener;
import org.talend.commons.ui.runtime.swt.proposal.IShowInvisibleCellEditorMethods;
import org.talend.commons.ui.runtime.thread.AsynchronousThreading;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;

/**
 * This class extends the ExtendedTextCellEditor to add the Expression Builder to tMapper. Only tMapper can use this
 * class. <br/>
 * 
 */
public class CommonTextCellEditorWithProposal {

    private ContentProposalAdapterExtended contentProposalAdapter;

    protected Point selectionBeforeFocusLost;

    private TableViewerCreator tableViewerCreator;

    protected Point selection;

    private int previousActivatedIndex;

    private TableViewerCreatorColumn tableViewerCreatorColumn;

    private TextCellEditor textCellEditor;

    private Text text;

    public CommonTextCellEditorWithProposal(TextCellEditor textCellEditor, Text text) {
        super();
        this.textCellEditor = textCellEditor;
        this.text = text;
    }

    /**
     * DOC amaumont Comment method "init".
     * 
     * @param tableViewerCreatorColumn
     */
    public void init(TableViewerCreatorColumn tableViewerCreatorColumn) {
        this.tableViewerCreatorColumn = tableViewerCreatorColumn;
        this.tableViewerCreator = tableViewerCreatorColumn.getTableViewerCreator();
    }

    public void activate() {
        previousActivatedIndex = tableViewerCreator.getModifiedObjectInfo().getCurrentModifiedIndex();
        // System.out.println("previousActivatedIndex =" + previousActivatedIndex);
    }

    public void create(Composite parent) {
        text.addFocusListener(new FocusAdapter() {

            public void focusLost(FocusEvent e) {
                selectionBeforeFocusLost = text.getSelection();
                if (contentProposalAdapter != null && contentProposalAdapter.isProposalOpened()) {
                    new AsynchronousThreading(50, false, text.getDisplay(), new Runnable() {

                        public void run() {
                            if (contentProposalAdapter != null && contentProposalAdapter.isProposalOpened()
                                    && contentProposalAdapter.hasFocus()) {
                                activateCellEditorAsynch(selectionBeforeFocusLost, false);
                            } else {
                                contentProposalAdapter.close();
                            }
                        }
                    }).start();
                }
            }

        });

        initProposal();
    }

    protected void keyReleaseOccured(KeyEvent keyEvent) {
        boolean proposalOpened = contentProposalAdapter.isProposalOpened();
        if (!proposalOpened) {

            if (keyEvent.character == '\r') { // Return key
                if (text != null && !text.isDisposed() && (text.getStyle() & SWT.MULTI) != 0) {
                    if ((keyEvent.stateMask & SWT.CTRL) != 0) {
                        return;
                    }
                }
                keyEvent.doit = false;
            }
            cellEditorClassKeyReleasedOccured(keyEvent);
        }
    }

    private void cellEditorClassKeyReleasedOccured(KeyEvent keyEvent) {
        if (keyEvent.character == '\u001b') { // Escape character
            ((IShowInvisibleCellEditorMethods) textCellEditor).fireCancelEditor();
        } else if (keyEvent.character == '\r') { // Return key
            ((IShowInvisibleCellEditorMethods) textCellEditor).fireApplyEditorValue();
            textCellEditor.deactivate();
        }
    }

    /**
     * DOC amaumont Comment method "initProposal".
     */
    private void initProposal() {
        if (contentProposalAdapter == null) {
            contentProposalAdapter = ProposalUtils.getCommonProposal(textCellEditor);
            contentProposalAdapter.addContentProposalListener(new IContentProposalExtendedListener() {

                public void proposalBeforeModifyControl(IContentProposal proposal) {
                    if (!text.isFocusControl() && contentProposalAdapter.hasFocus()) {
                        activateCellEditor();
                        text.setSelection(new Point(selectionBeforeFocusLost.x, selectionBeforeFocusLost.y));
                    }
                }

                public void proposalClosed() {
                }

                public void proposalOpened() {
                }

                public void proposalAccepted(IContentProposal proposal) {
                    selectionBeforeFocusLost = text.getSelection();
                    activateCellEditorAsynch(null, false);
                }

            });
        }

    }

    public IContentProposalProvider getProposalProvider() {
        if (contentProposalAdapter != null) {
            return contentProposalAdapter.getContentProposalProvider();
        }
        return null;
    }

    public void setContentProposalProvider(IContentProposalProvider proposalProvider) {
        if (contentProposalAdapter != null) {
            contentProposalAdapter.setContentProposalProvider(proposalProvider);
        }
    }

    private void activateCellEditor() {
        // System.out.println("activateCellEditor");

        // System.out.println("Activating index=" + previousActivatedIndex);

        Object previousModifiedBean = this.tableViewerCreator.getTableViewer().getElementAt(previousActivatedIndex);
        int indexColumn = tableViewerCreatorColumn.getIndex();
        tableViewerCreator.getTableViewer().editElement(previousModifiedBean, indexColumn);
    }

    private void activateCellEditorAsynch(final Point selection, final boolean testFocus) {

        new AsynchronousThreading(50, false, text.getDisplay(), new Runnable() {

            public void run() {

                // System.out.println("active async");

                if (!text.isDisposed()) {

                    Point newSelection = selection;
                    if (!text.isFocusControl() && testFocus || !testFocus) {
                        // System.out.println("activateCellEditorAsynch");
                        activateCellEditor();
                    }
                    if (selection == null) {
                        newSelection = selectionBeforeFocusLost;
                    }
                    // if (!cellEditorLocationHasChanged) {
                    text.setSelection(new Point(newSelection.x, newSelection.y));
                    // }
                }
            }

        }).start();
    }

    /**
     * Getter for contentProposalAdapter.
     * 
     * @return the contentProposalAdapter
     */
    public ContentProposalAdapterExtended getContentProposalAdapter() {
        return this.contentProposalAdapter;
    }

}
