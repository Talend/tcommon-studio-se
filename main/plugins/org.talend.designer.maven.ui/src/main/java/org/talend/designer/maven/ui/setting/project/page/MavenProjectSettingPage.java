// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.maven.ui.setting.project.page;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.core.runtime.projectsetting.AbstractProjectSettingPage;
import org.talend.core.runtime.services.IFilterService;
import org.talend.designer.maven.DesignerMavenPlugin;
import org.talend.designer.maven.tools.AggregatorPomsHelper;
import org.talend.designer.maven.ui.i18n.Messages;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class MavenProjectSettingPage extends AbstractProjectSettingPage {

    private final static String LATEST_VERSION_DISPLAY = "version=latest"; //$NON-NLS-1$

    private final static String LATEST_VERSION_REAL = "version=-1.-1"; //$NON-NLS-1$

	private Text filterText;

	private String filter;

	private IPreferenceStore preferenceStore;

    private Button useProfileModuleCheckbox;

	public MavenProjectSettingPage() {
		noDefaultAndApplyButton();
	}

	@Override
	protected String getPreferenceName() {
		return DesignerMavenPlugin.PLUGIN_ID;
	}

	@Override
	protected void createFieldEditors() {
        if (!PluginChecker.isTIS()) {
            return;
        }
		Composite parent = getFieldEditorParent();
		parent.setLayout(new GridLayout());
		Button button = new Button(parent, SWT.NONE);
		button.setText(Messages.getString("ProjectPomProjectSettingPage.syncAllPomsButtonText")); //$NON-NLS-1$

		preferenceStore = getPreferenceStore();
		Label filterLabel = new Label(parent, SWT.NONE);
        filterLabel.setText(Messages.getString("ProjectPomProjectSettingPage_FilterPomLabel")); //$NON-NLS-1$
		filterText = new Text(parent, SWT.BORDER);
		filterText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        filter = preferenceStore.getString(MavenConstants.POM_FILTER);
        if (StringUtils.isBlank(filter)) {
            filter = ""; //$NON-NLS-1$
		}
        filter = getDisplayVersionFilter(filter);

        Label filterExampleLable = new Label(parent, SWT.NONE);
        filterExampleLable.setText(Messages.getString("MavenProjectSettingPage.filterExampleMessage")); //$NON-NLS-1$
        filterExampleLable.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        useProfileModuleCheckbox = new Button(parent, SWT.CHECK);
        useProfileModuleCheckbox.setText(Messages.getString("MavenProjectSettingPage.refModuleText")); //$NON-NLS-1$
        useProfileModuleCheckbox.setSelection(preferenceStore.getBoolean(MavenConstants.USE_PROFILE_MODULE));

        filterText.setText(filter);
		filterText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (GlobalServiceRegister.getDefault().isServiceRegistered(IFilterService.class)) {
					IFilterService service = (IFilterService) GlobalServiceRegister.getDefault()
							.getService(IFilterService.class);
                    String text = getRealVersionFilter(filterText.getText());
                    String filterError = service.checkFilterError(text);
                    if (StringUtils.isBlank(text) || filterError == null) {
						setErrorMessage(null);
                        filter = text;
						setValid(true);
						button.setEnabled(true);
					} else {
                        setErrorMessage(
                                Messages.getString("ProjectPomProjectSettingPage_FilterErrorMessage", filterError));
						setValid(false);
						button.setEnabled(false);
					}
				}
			}
		});

		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				try {
                    preferenceStore.setValue(MavenConstants.POM_FILTER, getRealVersionFilter(filter));
                    preferenceStore.setValue(MavenConstants.USE_PROFILE_MODULE, useProfileModuleCheckbox.getSelection());
					new AggregatorPomsHelper().syncAllPoms();
				} catch (Exception e) {
					ExceptionHandler.process(e);
                    if ("filter_parse_error".equals(e.getMessage())) { //$NON-NLS-1$
                        setErrorMessage(Messages.getString("ProjectPomProjectSettingPage_FilterErrorMessage")); //$NON-NLS-1$
					}
				}
			}

		});

	}

	@Override
	public boolean performOk() {
		boolean ok = super.performOk();
		if (preferenceStore != null) {
            preferenceStore.setValue(MavenConstants.POM_FILTER, getRealVersionFilter(filter));
            preferenceStore.setValue(MavenConstants.USE_PROFILE_MODULE, useProfileModuleCheckbox.getSelection());
		}
		return ok;
	}

    private String getRealVersionFilter(String displayVersion) {
        String realVersion = displayVersion;
        if (displayVersion != null && displayVersion.contains(LATEST_VERSION_DISPLAY)) {
            realVersion = displayVersion.replace(LATEST_VERSION_DISPLAY, LATEST_VERSION_REAL);
        }
        return realVersion;
    }

    private String getDisplayVersionFilter(String realVersion) {
        String displayVersion = realVersion;
        if (realVersion != null && realVersion.contains(LATEST_VERSION_REAL)) {
            displayVersion = realVersion.replace(LATEST_VERSION_REAL, LATEST_VERSION_DISPLAY);
        }
        return displayVersion;
    }

}
