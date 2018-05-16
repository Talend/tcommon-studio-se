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
package org.talend.core.ui.proposal;

import java.text.MessageFormat;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.talend.core.ui.i18n.Messages;
import org.talend.designer.rowgenerator.data.Function;
import org.talend.designer.rowgenerator.data.FunctionManager;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 */
public class RoutinesFunctionProposal implements IContentProposal {

    protected Function function;

    protected String method = ""; //$NON-NLS-1$

    public RoutinesFunctionProposal() {
        super();
    }

    /**
     * DOC ggu RoutinesProposal constructor comment.
     * 
     * @param function
     */
    public RoutinesFunctionProposal(Function function) {
        super();
        this.function = function;
        if (function != null) {
            this.method = FunctionManager.getFunctionMethod(function);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.fieldassist.IContentProposal#getContent()
     */
    @Override
    public String getContent() {
        return method;
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
        String message = Messages.getString("RoutinesFunctionProposal.Description");
        message += Messages.getString("RoutinesFunctionProposal.CreatedBy");
        message += Messages.getString("RoutinesFunctionProposal.ReturnType");
        message += Messages.getString("RoutinesFunctionProposal.VariableName");

        MessageFormat format = new MessageFormat(message);
        Object[] args = new Object[] { function.getDescription(),
                function.isUserDefined() ? Messages.getString("RoutinesFunctionProposal.User") : Messages //$NON-NLS-1$
                        .getString("RoutinesFunctionProposal.System"), function.getTalendType().getName(), method }; //$NON-NLS-1$
        return format.format(args);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.fieldassist.IContentProposal#getLabel()
     */
    @Override
    public String getLabel() {
        //        if ("".equals(function.getCategory())) { //$NON-NLS-1$
        // return function.getName();
        // }
        //        return function.getCategory() + "." + function.getName(); //$NON-NLS-1$
        return function.getFunctionString();
    }
}
