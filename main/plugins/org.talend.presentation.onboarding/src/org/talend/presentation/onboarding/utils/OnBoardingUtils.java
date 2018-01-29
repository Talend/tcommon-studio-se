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
package org.talend.presentation.onboarding.utils;

import java.io.StringReader;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Priority;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.intro.impl.html.IIntroHTMLConstants;
import org.osgi.framework.Bundle;
import org.talend.presentation.onboarding.exceptions.OnBoardingExceptionHandler;
import org.talend.presentation.onboarding.ui.managers.OnBoardingResourceManager;
import org.talend.presentation.onboarding.ui.runtimedata.OnBoardingJsonDoc;
import org.talend.presentation.onboarding.ui.runtimedata.OnBoardingPerspectiveBean;
import org.talend.presentation.onboarding.ui.runtimedata.OnBoardingRegistedResource;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

/**
 * created by cmeng on Sep 15, 2015 Detailled comment
 *
 */
public class OnBoardingUtils {

    private static Bundle bundle = null;

    public static Bundle getBundle() {
        if (bundle == null) {
            bundle = Platform.getBundle(OnBoardingConstants.PLUGIN_ID);
        }
        return bundle;
    }

    public static URL getResourceLocalURL(Bundle iBoundle, String path) {
        try {
            URL resourceURL = iBoundle.getEntry(path);
            return FileLocator.toFileURL(resourceURL);
        } catch (Throwable e) {
            OnBoardingExceptionHandler.process(e);
            return null;
        }
    }

    public static URL getResourceLocalURL(String path) {
        return getResourceLocalURL(getBundle(), path);
    }

    public static void setVisible(boolean visible, final Shell shell, final int totalAnimateTime, final int sleepTime,
            final int initialAlpha, final int finalAlphaValue, final Callable onFinish) {
        final int executeTimes = totalAnimateTime / sleepTime;
        // seems have bugs maybe related to multithread, need to check it in some day, currently always set it enable
        // after finished the execution
        // final boolean originalEnable = shell.getEnabled();
        shell.setEnabled(false);
        if (visible) {
            final int alpha = initialAlpha;
            shell.setAlpha(alpha);
            final int increment = (finalAlphaValue - alpha) / executeTimes + 1;
            shell.setVisible(visible);
            new Thread(new Runnable() {

                @Override
                public void run() {
                    final ObjectBox<Integer> alphaBox = new ObjectBox<Integer>();
                    final ObjectBox<Boolean> isContinue = new ObjectBox<Boolean>();
                    alphaBox.value = alpha;
                    isContinue.value = true;
                    for (int i = 1; i < executeTimes - 1; i++) {
                        alphaBox.value += increment;
                        Display.getDefault().syncExec(new Runnable() {

                            @Override
                            public void run() {
                                if (!isContinueExecute(shell)) {
                                    isContinue.value = false;
                                    return;
                                }
                                shell.setAlpha(alphaBox.value);
                            }
                        });
                        if (!isContinue.value) {
                            break;
                        }
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                            OnBoardingExceptionHandler.process(e, Priority.WARN);
                            break;
                        }
                    }
                    Display.getDefault().syncExec(new Runnable() {

                        @Override
                        public void run() {
                            if (!shell.isDisposed()) {
                                shell.setAlpha(finalAlphaValue);
                                // shell.setEnabled(originalEnable);
                                shell.setEnabled(true);
                            }
                        }
                    });
                    if (onFinish != null) {
                        try {
                            onFinish.call();
                        } catch (Exception e) {
                            OnBoardingExceptionHandler.process(e);
                        }
                    }
                }
            }).start();
        } else {
            int alpha = shell.getAlpha();
            int increment = (alpha - initialAlpha) / executeTimes + 1;
            for (int i = 1; i < executeTimes - 1; i++) {
                alpha -= increment;
                shell.setAlpha(alpha);
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    OnBoardingExceptionHandler.process(e, Priority.WARN);
                    break;
                }
            }
            shell.setAlpha(initialAlpha);

            // shell.setEnabled(originalEnable);
            shell.setEnabled(true);
            shell.setVisible(visible);
            if (onFinish != null) {
                try {
                    onFinish.call();
                } catch (Exception e) {
                    OnBoardingExceptionHandler.process(e);
                }
            }
        }
    }

    public static boolean isContinueExecute(Shell shell) {
        if (shell.isDisposed() || !shell.isVisible()) {
            return false;
        } else {
            return true;
        }
    }

    public static Color getColor(RGB rgb) {
        ColorRegistry colorRegistry = JFaceResources.getColorRegistry();
        String symbol = rgb.toString();
        Color color = colorRegistry.get(symbol);
        if (color == null) {
            colorRegistry.put(symbol, rgb);
        }
        return colorRegistry.get(symbol);
    }

    public static boolean isSupportBrowser() {
        final ObjectBox<Boolean> isSupportBrowser = new ObjectBox<Boolean>();
        isSupportBrowser.value = true;
        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {
                Shell shell = new Shell();
                Browser browser = null;
                try {
                    shell.setVisible(false);
                    browser = new Browser(shell, SWT.NONE);
                } catch (Throwable e) {
                    isSupportBrowser.value = false;
                } finally {
                    try {
                        if (browser != null) {
                            browser.dispose();
                        }
                        if (shell != null) {
                            shell.dispose();
                        }
                    } catch (Throwable e) {
                        // nothing need to do
                    }
                }
            }
        });
        return isSupportBrowser.value;
    }

    public static String getCurrentSelectedPerspectiveIdUIThread(final IWorkbenchWindow wbWindow) {
        final ObjectBox<String> perspId = new ObjectBox<String>();
        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {
                perspId.value = getCurrentSelectedPerspectiveId(wbWindow);
            }
        });
        return perspId.value;
    }

    /**
     * Should run this method in UI thread
     * 
     * @param wbWindow
     * @return
     */
    public static String getCurrentSelectedPerspectiveId(IWorkbenchWindow wbWindow) {
        String perspId = null;
        if (wbWindow == null || !PlatformUI.isWorkbenchRunning()) {
            return perspId;
        }
        IWorkbenchPage workbenchPage = wbWindow.getActivePage();
        if (workbenchPage == null) {
            return perspId;
        }
        IPerspectiveDescriptor perspDesc = workbenchPage.getPerspective();
        if (perspDesc != null) {
            perspId = perspDesc.getId();
        }
        return perspId;
    }

    public static Document convertStringToDocument(String htmlStr) {
        Document newDoc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            // if this is not set, Document.getElementsByTagNameNS() will fail.
            factory.setNamespaceAware(true);
            factory.setExpandEntityReferences(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            if (htmlStr == null || htmlStr.trim().isEmpty()) {
                newDoc = builder.newDocument();
            } else {
                InputSource inputSource = new InputSource(new StringReader(htmlStr));
                newDoc = builder.parse(inputSource);
            }
        } catch (Throwable e) {
            OnBoardingExceptionHandler.process(e);
        }
        return newDoc;
    }

    public static Element createDivElement(Document dom, String id) {
        Attr attrId = dom.createAttribute(IIntroHTMLConstants.ATTRIBUTE_ID);
        attrId.setValue(id);
        Element contentDiv = dom.createElement(IIntroHTMLConstants.ELEMENT_DIV);
        contentDiv.setAttributeNode(attrId);
        return contentDiv;
    }

    public static OnBoardingPerspectiveBean getDefaultPerspectiveBean(ObjectBox<String> jsonDocId) {
        OnBoardingPerspectiveBean perspBean = null;
        OnBoardingResourceManager resourceManager = OnBoardingResourceManager.getDefaultResourceManager();
        if (resourceManager == null) {
            return perspBean;
        }
        IWorkbench workbench = PlatformUI.getWorkbench();
        if (workbench == null || !PlatformUI.isWorkbenchRunning()) {
            return perspBean;
        }
        IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
        if (workbenchWindow == null) {
            return perspBean;
        }
        String perspId = OnBoardingUtils.getCurrentSelectedPerspectiveId(workbenchWindow);
        if (perspId == null || perspId.isEmpty()) {
            return perspBean;
        }
        jsonDocId.value = PlatformUI.getPreferenceStore().getString(OnBoardingResourceManager.DEFAULT_DOC_ID);
        if (jsonDocId.value == null || jsonDocId.value.isEmpty()) {
            Collection<OnBoardingRegistedResource> onBoardingRegistedResources = resourceManager.getAllRegistedResources();
            if (onBoardingRegistedResources != null && !onBoardingRegistedResources.isEmpty()) {
                Iterator<OnBoardingRegistedResource> iter = onBoardingRegistedResources.iterator();
                while (iter.hasNext()) {
                    try {
                        OnBoardingRegistedResource resource = iter.next();
                        OnBoardingJsonDoc jsonDoc = resource.getJsonDoc();
                        perspBean = jsonDoc.getPerspectiveBean(perspId);
                        if (perspBean != null) {
                            jsonDocId.value = jsonDoc.getId();
                            break;
                        }
                    } catch (Throwable e) {
                        OnBoardingExceptionHandler.process(e);
                    }
                }
                if (perspBean == null) {
                    try {
                        OnBoardingJsonDoc jsonDoc = onBoardingRegistedResources.iterator().next().getJsonDoc();
                        jsonDocId.value = jsonDoc.getId();
                        perspId = jsonDoc.getDefaultPerspId();
                        if (perspId != null) {
                            perspBean = jsonDoc.getPerspectiveBean(perspId);
                        }
                    } catch (Throwable e) {
                        OnBoardingExceptionHandler.process(e);
                    }
                }
            }
        } else {
            OnBoardingRegistedResource registedResource = resourceManager.getOnBoardingRegistedResource(jsonDocId.value);
            if (registedResource == null) {
                return perspBean;
            }
            try {
                OnBoardingJsonDoc jsonDoc = registedResource.getJsonDoc();
                perspBean = jsonDoc.getPerspectiveBean(perspId);
                if (perspBean == null) {
                    perspId = jsonDoc.getDefaultPerspId();
                    if (perspId != null) {
                        perspBean = jsonDoc.getPerspectiveBean(perspId);
                    }
                }
            } catch (Throwable e) {
                OnBoardingExceptionHandler.process(e);
            }
        }
        return perspBean;
    }
}
