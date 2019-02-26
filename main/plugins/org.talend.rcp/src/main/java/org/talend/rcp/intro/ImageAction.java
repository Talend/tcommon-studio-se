package org.talend.rcp.intro;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.program.Program;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.service.IExchangeService;
import org.talend.core.service.ITutorialsService;
import org.talend.rcp.i18n.Messages;
import org.talend.rcp.intro.linksbar.LinksToolbarItem;


public class ImageAction extends Action {

	private final IWorkbenchWindow window;
	private String url;

	public ImageAction(IWorkbenchWindow window, String imagePath, String url, String tipText) {
		this.window = window;
		this.url = url;
		ImageDescriptor imageDescriptorFromPlugin = AbstractUIPlugin.imageDescriptorFromPlugin("org.talend.rcp",
				imagePath);
		setImageDescriptor(imageDescriptorFromPlugin);
		setToolTipText(Messages.getString(tipText));
	}

	public void run() {
		if (window != null) {
			if (StringUtils.contains(LinksToolbarItem.EXCHANGE_ORIG_URL, url)) {
				IExchangeService service = (IExchangeService) GlobalServiceRegister.getDefault()
						.getService(IExchangeService.class);
				service.openExchangeEditor();
			} else if (StringUtils.contains(LinksToolbarItem.VIDEOS_ORIG_URL, url)) {
				ITutorialsService service = (ITutorialsService) GlobalServiceRegister.getDefault()
						.getService(ITutorialsService.class);
				service.openTutorialsDialog();
			} else {
				openBrower(url);
			}
		}
	}

	protected void openBrower(String url) {
		Program.launch(url);
	}
}
