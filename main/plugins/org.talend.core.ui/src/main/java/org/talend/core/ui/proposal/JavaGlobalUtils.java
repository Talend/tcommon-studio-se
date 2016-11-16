// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

public class JavaGlobalUtils {

    public static IContentProposal[] getProposals() {
        IContentProposal[] cp = new IContentProposal[] { new JavaGlobalVariableProposal("projectName", "Project Name"), //$NON-NLS-1$ //$NON-NLS-2$
                new JavaGlobalVariableProposal("jobName", "Job Name"), }; //$NON-NLS-1$ //$NON-NLS-2$
        return cp;
    }

    static class JavaGlobalVariableProposal implements IContentProposal {

        private String name;

        private String desc;

        private String code;

        private String display;

        public JavaGlobalVariableProposal(String name, String desc) {

            this.name = name;
            this.desc = desc;

            this.code = name;
            this.display = "global." + name; //$NON-NLS-1$

        }

        @Override
        public String getContent() {
            return code;
        }

        @Override
        public int getCursorPosition() {
            return getContent().length();
        }

        @Override
        public String getDescription() {
            String message = Messages.getString("JavaGlobalVariableProposal.Description"); //$NON-NLS-1$
            message += "\n\n"; //$NON-NLS-1$
            message += Messages.getString("JavaGlobalVariableProposal.VariableName"); //$NON-NLS-1$

            MessageFormat format = new MessageFormat(message);
            Object[] args = new Object[] { desc, code };
            return format.format(args);
        }

        @Override
        public String getLabel() {
            return display;
        }

    }
}
