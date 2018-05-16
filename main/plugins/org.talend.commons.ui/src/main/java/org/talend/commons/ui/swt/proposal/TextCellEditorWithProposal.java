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

import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.runtime.swt.proposal.IShowInvisibleCellEditorMethods;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: TextCellEditorWithProposal.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class TextCellEditorWithProposal extends TextCellEditor implements IShowInvisibleCellEditorMethods {

    private CommonTextCellEditorWithProposal commonTextEditor;

    private TableViewerCreatorColumn tableViewerCreatorColumn;

    public TextCellEditorWithProposal(Composite parent, int style, TableViewerCreatorColumn tableViewerCreatorColumn) {
        super(parent);
        setStyle(style);
        commonTextEditor.init(tableViewerCreatorColumn);
    }

    public TextCellEditorWithProposal(Composite parent, TableViewerCreatorColumn tableViewerCreatorColumn) {
        this(parent, SWT.NONE, tableViewerCreatorColumn);
    }

    @Override
    public void activate() {
        super.activate();
        commonTextEditor.activate();
        // System.out.println("previousActivatedIndex =" + previousActivatedIndex);
    }

    @Override
    public void create(Composite parent) {
        super.create(parent);
        commonTextEditor = new CommonTextCellEditorWithProposal(this, text);
        commonTextEditor.create(parent);
    }

    @Override
    protected void keyReleaseOccured(KeyEvent keyEvent) {
        commonTextEditor.keyReleaseOccured(keyEvent);
    }

    public IContentProposalProvider getProposalProvider() {
        return commonTextEditor.getProposalProvider();
    }

    public void setContentProposalProvider(IContentProposalProvider proposalProvider) {
        commonTextEditor.setContentProposalProvider(proposalProvider);
    }

    /**
     * Getter for contentProposalAdapter.
     * 
     * @return the contentProposalAdapter
     */
    public ContentProposalAdapterExtended getContentProposalAdapter() {
        return commonTextEditor.getContentProposalAdapter();
    }

    @Override
    public void fireApplyEditorValue() {
        super.fireApplyEditorValue();
    }

    @Override
    public void fireCancelEditor() {
        super.fireCancelEditor();
    }

}
