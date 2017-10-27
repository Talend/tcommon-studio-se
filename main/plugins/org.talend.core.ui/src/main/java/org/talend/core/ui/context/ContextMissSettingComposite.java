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
package org.talend.core.ui.context;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.talend.commons.ui.swt.composites.MessagesComposite;
import org.talend.commons.ui.swt.composites.MessagesWithActionComposite;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerUIService;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.ui.i18n.Messages;

/**
 * created by ldong on Aug 18, 2014 Detailled comment
 * 
 */
public class ContextMissSettingComposite extends Composite {

    private boolean isVisibleTopMessages;

    private MessagesWithActionComposite messagesComp;

    public static final String REQUIRE_BUNDLE_NAME = "org.talend.libraries.nattable";

    private ILibraryManagerUIService libUiService;

    /**
     * DOC ldong ContextMissSettingComposite constructor comment.
     * 
     * @param parent
     * @param style
     */
    public ContextMissSettingComposite(Composite parent, int style) {
        super(parent, style);
        initialize(this);
    }

    private List<String> missModulesNeeded = new ArrayList<String>();

    private void initialize(Composite parent) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerUIService.class)) {
            libUiService = (ILibraryManagerUIService) GlobalServiceRegister.getDefault().getService(
                    ILibraryManagerUIService.class);
        }
        checkVisibleTopMessages();
        if (isVisibleTopMessages()) {
            disposeChildren();

            parent.setLayout(new GridLayout(3, false));
            messagesComp = createMessagesComposite(parent);
            GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
            messagesComp.setLayoutData(gridData);
            GridLayout layout2 = (GridLayout) messagesComp.getLayout();
            layout2.marginHeight = 0;
            layout2.marginTop = 0;
            layout2.marginBottom = 0;
            layout2.marginRight = 0;

            afterCreateMessagesComposite(messagesComp);
        }
    }

    private MessagesWithActionComposite createMessagesComposite(Composite parent) {
        MessagesWithActionComposite messagesComposite = new MessagesWithActionComposite(parent, SWT.TOP) {

            @Override
            protected void changeBackgroundColor(int status) {
                switch (status) {
                case IStatus.WARNING:
                    CoreUIPlugin.setCSSId(this, CSS_MESSAGES_WITH_ACTION_COMPOSITE_WARN);
                    break;
                case IStatus.ERROR:
                    CoreUIPlugin.setCSSId(this, CSS_MESSAGES_WITH_ACTION_COMPOSITE_ERROR);
                    break;
                default:
                }
            }
        };
        return messagesComposite;

    }

    private void checkVisibleTopMessages() {
        missModulesNeeded.clear();
        boolean allInstalled = true;
        List<String> updatedModules = new ArrayList<String>();
        if (libUiService != null) {
            allInstalled = libUiService.isModuleInstalledForBundle(REQUIRE_BUNDLE_NAME);
            if (!allInstalled) {
                updatedModules = libUiService.getNeedInstallModuleForBundle(REQUIRE_BUNDLE_NAME);
            }
        }

        missModulesNeeded.addAll(updatedModules);

        setVisibleTopMessage(!allInstalled);
    }

    public void afterCreateMessagesComposite(MessagesComposite messComposite) {
        messComposite.updateTopMessages(Messages.getString("ContextMissSettingComposite.missingModuleMessages"), IStatus.WARNING);
        if (messComposite instanceof MessagesWithActionComposite) {
            MessagesWithActionComposite messWithActionComposite = (MessagesWithActionComposite) messComposite;

            messWithActionComposite.updateActionButton(Messages.getString("ContextMissSettingComposite.installName") //$NON-NLS-1$
                    + "...", null);//$NON-NLS-1$
            messWithActionComposite.addActionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    if (!missModulesNeeded.isEmpty()) {
                        if (libUiService != null) {
                            for (String jarName : missModulesNeeded) {
                                libUiService.installModules(new String[] { jarName });
                            }
                            // after install the jar,need to refresh again the parent composite
                            refreshContextView();
                        }
                    }
                }
            });
        }
    }

    private void refreshContextView() {
        ((ContextNebulaGridComposite) this.getParent()).refresh();
    }

    protected void disposeChildren() {
        if (messagesComp != null && !messagesComp.isDisposed()) {
            // Empty the composite before use (kinda refresh) :
            Control[] ct = messagesComp.getChildren();
            for (int i = 0; i < ct.length; i++) {
                if (ct[i] != null) {
                    if (ct[i].getForeground() != null && !ct[i].getForeground().isDisposed()) {
                        ct[i].getForeground().dispose();
                    }
                    if (ct[i].getBackground() != null && !ct[i].getBackground().isDisposed()) {
                        ct[i].getBackground().dispose();
                    }
                }
                ct[i].dispose();
            }
        }
    }

    public void setVisibleTopMessage(boolean visible) {
        this.isVisibleTopMessages = visible;
        if (messagesComp != null && !messagesComp.isDisposed() && !isVisibleTopMessages) {
            messagesComp.dispose();
        }
    }

    public boolean isVisibleTopMessages() {
        return this.isVisibleTopMessages;
    }
}
