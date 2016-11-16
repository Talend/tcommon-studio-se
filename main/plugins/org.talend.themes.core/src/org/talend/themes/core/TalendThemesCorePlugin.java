package org.talend.themes.core;

import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.e4.ui.css.swt.theme.ITheme;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.themes.core.elements.interfaces.ITalendThemeChangeListener;

/**
 * The activator class controls the plug-in life cycle
 */
public class TalendThemesCorePlugin extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.talend.themes.core"; //$NON-NLS-1$

    // The shared instance
    private static TalendThemesCorePlugin plugin;

    private ServiceRegistration<EventHandler> themeChangeHandlerService;

    /**
     * The constructor
     */
    public TalendThemesCorePlugin() {
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        registThemeChangeListener(context);
    }

    private void registThemeChangeListener(BundleContext context) {
        Dictionary<String, String> properties = new Hashtable<String, String>();
        properties.put(EventConstants.EVENT_TOPIC, IThemeEngine.Events.THEME_CHANGED);
        themeChangeHandlerService = context.registerService(EventHandler.class, new EventHandler() {

            @Override
            public void handleEvent(Event event) {
                IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                if (workbenchWindow == null) {
                    return;
                }
                try {
                    IEditorReference[] references = workbenchWindow.getActivePage().getEditorReferences();
                    if (references != null) {
                        Object propValue = event.getProperty(IThemeEngine.Events.THEME);
                        if (!(propValue instanceof ITheme)) {
                            return;
                        }
                        ITheme newTheme = (ITheme) propValue;
                        String newThemeId = newTheme.getId();
                        org.eclipse.ui.themes.ITheme curTheme = PlatformUI.getWorkbench().getThemeManager().getCurrentTheme();
                        String curThemeId = null;
                        if (curTheme != null) {
                            curThemeId = curTheme.getId();
                        }
                        for (IEditorReference reference : references) {
                            if (reference != null) {
                                IEditorPart editorPart = reference.getEditor(false);
                                if (editorPart instanceof ITalendThemeChangeListener) {
                                    ((ITalendThemeChangeListener) editorPart).onThemeChanging(newThemeId, curThemeId);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    CommonExceptionHandler.process(e);
                }
            }
        }, properties);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        themeChangeHandlerService.unregister();
        super.stop(context);
    }

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static TalendThemesCorePlugin getDefault() {
        return plugin;
    }

}
