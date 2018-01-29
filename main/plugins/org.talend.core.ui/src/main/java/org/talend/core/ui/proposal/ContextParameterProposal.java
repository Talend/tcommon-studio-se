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
package org.talend.core.ui.proposal;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.ui.i18n.Messages;
import org.talend.repository.ProjectManager;

/**
 * Content proposal based on a IContextParameter. <br/>
 * 
 * $Id$
 * 
 */
public class ContextParameterProposal implements IContentProposal {

    private IContextParameter contextParameter;

    /**
     * Constructs a new ContextParameterProposal.
     */
    public ContextParameterProposal(IContextParameter contextParameter) {
        super();

        this.contextParameter = contextParameter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.fieldassist.IContentProposal#getContent()
     */
    @Override
    public String getContent() {

        ECodeLanguage language = ProjectManager.getInstance().getCurrentProject().getLanguage();
        return ContextParameterUtils.getNewScriptCode(contextParameter.getName(), language);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.fieldassist.IContentProposal#getCursorPosition()
     */
    @Override
    public int getCursorPosition() {
        return getContent().length();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.fieldassist.IContentProposal#getDescription()
     */
    @Override
    public String getDescription() {
        String desc = new String();
        if (!StringUtils.isEmpty(contextParameter.getComment())) {
            desc = contextParameter.getComment();
        } else {
            desc = Messages.getString("ContextParameterProposal.NoCommentAvaiable"); //$NON-NLS-1$
        }
        // TDI-30683:fix the NPE pb and another description match pb(need move "\n" from message.propreties to code).
        MessageFormat format = new MessageFormat(getDescriptionMessagePattern());
        if (contextParameter.getContext() != null) {
            Object[] replaceArgs = new Object[] { desc, contextParameter.getContext().getName(), contextParameter.getType(),
                    contextParameter.getName() };
            return format.format(replaceArgs);
        }
        return desc;

    }

    private String getDescriptionMessagePattern() {
        String message = Messages.getString("ContextParameterProposal.Description") + "\n\n"; //$NON-NLS-1$ //$NON-NLS-2$
        message += Messages.getString("ContextParameterProposal.ContextVariable") + "\n\n"; //$NON-NLS-1$ //$NON-NLS-2$
        message += Messages.getString("ContextParameterProposal.Type") + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
        message += Messages.getString("ContextParameterProposal.VariableName") + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
        return message;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.fieldassist.IContentProposal#getLabel()
     */
    @Override
    public String getLabel() {
        return ContextParameterUtils.JAVA_NEW_CONTEXT_PREFIX + contextParameter.getName();
    }

}
