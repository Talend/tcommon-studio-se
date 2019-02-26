package org.talend.rcp.intro.linksbar;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.service.IExchangeService;
import org.talend.core.service.ITutorialsService;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.rcp.i18n.Messages;

public class LinkToolbarLabel extends LinksToolbarItem {
	private String url;
	private String tipText;

	public LinkToolbarLabel(String url, String tipText) {
		this.url = url;
		this.tipText = tipText;

	}

	@Override
	protected Control createControl(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);

		composite.setLayout(new FormLayout());

		Link ask = new Link(composite, SWT.NONE);
		FormData formData = new FormData();
		formData.bottom = new FormAttachment(90);
		ask.setLayoutData(formData);
		if (!PluginChecker.isTIS() && StringUtils.equals(LinksToolbarItem.CLOUD_URL, url)
				&& GlobalServiceRegister.getDefault().isServiceRegistered(IBrandingService.class)) {
			IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault()
					.getService(IBrandingService.class);
			String edition = brandingService.getAcronym();
			this.url = this.url.replace("dynamic_acronym", edition);//$NON-NLS-1$
		}
		ask.setText(url);
		ask.setToolTipText(Messages.getString(tipText)); // $NON-NLS-1$

		ask.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {

				if (StringUtils.contains(EXCHANGE_URL, event.text)) {
					IExchangeService service = (IExchangeService) GlobalServiceRegister.getDefault()
							.getService(IExchangeService.class);
					service.openExchangeEditor();
				} else if (StringUtils.contains(VIDEOS_URL, event.text)) {
					ITutorialsService service = (ITutorialsService) GlobalServiceRegister.getDefault()
							.getService(ITutorialsService.class);
					service.openTutorialsDialog();
				} else {
					openBrower(event.text);
				}
			}
		});
		return composite;
	}
}
