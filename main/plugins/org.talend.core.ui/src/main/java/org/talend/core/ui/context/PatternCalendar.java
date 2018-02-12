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
package org.talend.core.ui.context;

import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.swt.calendar.SWTCalendarWithTime;
import org.talend.commons.ui.swt.proposal.ContentProposalAdapterExtended;
import org.talend.commons.ui.swt.proposal.ProposalUtils;
import org.talend.core.ui.i18n.Messages;
import org.talend.core.ui.proposal.JavaSimpleDateFormatProposalProvider;

/**
 * DOC chuang class global comment. Detailled comment
 */
public class PatternCalendar extends SWTCalendarWithTime {

    private Text patternText;

    private String pattern;

    private Label exampleValue;

    private String defaultFormat = "yyyy-MM-dd HH:mm:ss"; //$NON-NLS-1$

    /**
     * DOC chuang PatternCalendar constructor comment.
     * 
     * @param parent
     * @param calendar
     * @param pattern2
     */
    public PatternCalendar(Composite parent, int style, String pattern, Calendar calendar) {
        super(parent, style);

        final Composite composite = new Composite(this, SWT.BORDER);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.marginWidth = 5;
        gridLayout.marginHeight = 5;
        composite.setLayout(gridLayout);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        composite.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));

        Label patternLabel = new Label(composite, SWT.NONE);
        patternLabel.setText(Messages.getString("PatternCalendar.pattern")); //$NON-NLS-1$
        patternLabel.setBackground(patternLabel.getParent().getBackground());

        patternText = new Text(composite, SWT.BORDER);
        patternText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        patternText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                onPatternChange();
            }

        });

        // install proposal
        JavaSimpleDateFormatProposalProvider proposalProvider = new JavaSimpleDateFormatProposalProvider();
        ContentProposalAdapterExtended contentProposalAdapter = ProposalUtils.getCommonProposal(patternText, proposalProvider);
        contentProposalAdapter.setFilterStyle(ContentProposalAdapterExtended.FILTER_NONE);
        contentProposalAdapter.setProposalAcceptanceStyle(ContentProposalAdapterExtended.PROPOSAL_INSERT);

        Label exampleLabel = new Label(composite, SWT.NONE);
        exampleLabel.setText(Messages.getString("PatternCalendar.example")); //$NON-NLS-1$
        exampleLabel.setBackground(patternLabel.getParent().getBackground());

        exampleValue = new Label(composite, SWT.NONE);
        exampleValue.setText(""); //$NON-NLS-1$
        exampleValue.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        if (calendar != null) {
            setCalendar(calendar);
        }
        if (pattern != null) {
            patternText.setText("\"" + pattern + "\""); //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            patternText.setText(defaultFormat); //$NON-NLS-1$
        }

    }

    /**
     * DOC chuang Comment method "onPatternChange".
     */
    protected void onPatternChange() {
        pattern = patternText.getText();
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        StringBuffer result = new StringBuffer();
        try {
            sdf.format(getCalendar().getTime(), result, new FieldPosition(0));
            exampleValue.setText(result.toString());
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }

    }

    public String getPatternText() {
        return pattern;
    }
}
