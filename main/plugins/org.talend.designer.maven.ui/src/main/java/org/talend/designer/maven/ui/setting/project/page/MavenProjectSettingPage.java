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

	private Text filterText;

	private String filter;

	private IPreferenceStore preferenceStore;

	public MavenProjectSettingPage() {
		noDefaultAndApplyButton();
	}

	@Override
	protected String getPreferenceName() {
		return DesignerMavenPlugin.PLUGIN_ID;
	}

	@Override
	protected void createFieldEditors() {
		Composite parent = getFieldEditorParent();
		parent.setLayout(new GridLayout());
		Button button = new Button(parent, SWT.NONE);
		button.setText(Messages.getString("ProjectPomProjectSettingPage.syncAllPomsButtonText")); //$NON-NLS-1$

		preferenceStore = getPreferenceStore();
		Label filterLabel = new Label(parent, SWT.NONE);
		filterLabel.setText(Messages.getString("ProjectPomProjectSettingPage_FilterPomLabel"));
		filterText = new Text(parent, SWT.BORDER);
		filterText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		if (StringUtils.isBlank(preferenceStore.getString(MavenConstants.POM_FILTER))) {
			filterText.setText("");
		} else {
			filterText.setText(preferenceStore.getString(MavenConstants.POM_FILTER));
		}
		filterText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (GlobalServiceRegister.getDefault().isServiceRegistered(IFilterService.class)) {
					IFilterService service = (IFilterService) GlobalServiceRegister.getDefault()
							.getService(IFilterService.class);
					if (StringUtils.isBlank(filterText.getText()) || service.checkFilterContent(filterText.getText())) {
						setErrorMessage(null);
						filter = filterText.getText();
						setValid(true);
						button.setEnabled(true);
					} else {
						setErrorMessage(Messages.getString("ProjectPomProjectSettingPage_FilterErrorMessage"));
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
					preferenceStore.setValue(MavenConstants.POM_FILTER, filter);
					new AggregatorPomsHelper().syncAllPoms();
				} catch (Exception e) {
					ExceptionHandler.process(e);
					if ("filter_parse_error".equals(e.getMessage())) {
						setErrorMessage(Messages.getString("ProjectPomProjectSettingPage_FilterErrorMessage"));
					}
				}
			}

		});

	}

	@Override
	public boolean performOk() {
		boolean ok = super.performOk();
		preferenceStore.setValue(MavenConstants.POM_FILTER, filter);
		return ok;
	}

}
