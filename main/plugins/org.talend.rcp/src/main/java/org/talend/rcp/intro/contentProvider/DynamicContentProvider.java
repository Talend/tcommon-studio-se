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
package org.talend.rcp.intro.contentProvider;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.intro.config.IIntroContentProviderSite;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.html.TalendHtmlModelUtil;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.VersionUtils;
import org.talend.commons.utils.network.NetworkUtil;
import org.talend.core.CorePlugin;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.PluginChecker;
import org.talend.core.model.general.Project;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.core.ui.token.DefaultTokenCollector;
import org.talend.rcp.i18n.Messages;
import org.talend.repository.ProjectManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * DOC WCHEN keep this class for old branding system before feature TDI-18168
 */
public class DynamicContentProvider extends IntroProvider {

    public static final String OPEN_IN_BROWSER_URL = "http://org.eclipse.ui.intro/runAction?pluginId=org.talend.rcp&class=org.talend.rcp.intro.OpenWebBrowserAction&type=showUrl&url="; //$NON-NLS-1$

    public static final String ONLINE_PAGE_URL = "https://www.talend.com/builtin_news/index.php"; //$NON-NLS-1$

    public static final String CLOUD_PAGE_URL = "https://www.talend.com/builtin_news/oss/"; //$NON-NLS-1$

    public static final String TRY_CLOUD_URL = "https://integrationcloud.talend.com"; //$NON-NLS-1$

    private static final String LEVEL_SEPARATOR = "."; //$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.intro.config.IIntroXHTMLContentProvider#createContent(java.lang.String, org.w3c.dom.Element)
     */
    @Override
    public void createContent(String id, Element parent) {
        // content for latest modified jobs and business models
        List<IRepositoryViewObject> latestItems = new ArrayList<IRepositoryViewObject>();
        Document dom = parent.getOwnerDocument();
        String url = ""; //$NON-NLS-1$
        if (ERepositoryObjectType.PROCESS != null && ERepositoryObjectType.PROCESS.name().equals(id)) {
            latestItems = getLatestModifiedItems(ERepositoryObjectType.PROCESS, 8);
            url = "http://org.eclipse.ui.intro/runAction?pluginId=org.talend.designer.core&" //$NON-NLS-1$
                    + "class=org.talend.designer.core.ui.action.EditProcess&" //$NON-NLS-1$
                    + "id=org.talend.designer.core.actions.editprocess&nodeId="; //$NON-NLS-1$
            if (latestItems.size() == 0) {
                parent.appendChild(dom.createTextNode(Messages.getString("DynamicContentProvider.jobs"))); //$NON-NLS-1$
            }
        } else if (ERepositoryObjectType.SERVICESPORT != null && ERepositoryObjectType.SERVICESPORT.name().equals(id)) {
            ERepositoryObjectType serviceType = ERepositoryObjectType.valueOf(ERepositoryObjectType.class, "SERVICES"); //$NON-NLS-1$
            // latestItems = getLatestModifiedItems(ERepositoryObjectType.SERVICESPORT, 8);
            latestItems = getLatestModifiedItems(serviceType, 8);
            url = "http://org.eclipse.ui.intro/runAction?pluginId=org.talend.repository.services&" //$NON-NLS-1$
                    + "class=org.talend.repository.services.action.OpenWSDLEditorAction&" //$NON-NLS-1$
                    + "id=org.talend.repository.services.action.OpenWSDLEditorAction&nodeId="; //$NON-NLS-1$
            if (latestItems.size() == 0) {
                parent.appendChild(dom.createTextNode(Messages.getString("DynamicContentProvider.services"))); //$NON-NLS-1$
            }
        } else if (ERepositoryObjectType.PROCESS_ROUTE != null && ERepositoryObjectType.PROCESS_ROUTE.name().equals(id)) { // $NON-NLS-1$
            latestItems = getLatestModifiedItems(ERepositoryObjectType.PROCESS_ROUTE, 8);
            url = "http://org.eclipse.ui.intro/runAction?pluginId=org.talend.camel.designer&" //$NON-NLS-1$
                    + "class=org.talend.camel.designer.ui.EditCamelProcess&" //$NON-NLS-1$
                    + "id=org.talend.camel.designer.ui.EditCamelProcess&nodeId="; //$NON-NLS-1$
            if (latestItems.size() == 0) {
                parent.appendChild(dom.createTextNode(Messages.getString("DynamicContentProvider.routes"))); //$NON-NLS-1$
            }
        } else if (ERepositoryObjectType.BUSINESS_PROCESS != null && ERepositoryObjectType.BUSINESS_PROCESS.name().equals(id)) {
            latestItems = getLatestModifiedItems(ERepositoryObjectType.BUSINESS_PROCESS, 8);
            url = "http://org.eclipse.ui.intro/runAction?pluginId=org.talend.designer.business.diagram&" //$NON-NLS-1$
                    + "class=org.talend.designer.business.diagram.custom.actions.OpenDiagramAction&" //$NON-NLS-1$
                    + "id=org.talend.designer.business.diagram.Action2&nodeId="; //$NON-NLS-1$
            if (latestItems.size() == 0) {
                parent.appendChild(dom.createTextNode(Messages.getString("DynamicContentProvider.businessModels"))); //$NON-NLS-1$
            }
        } else if ("ANALYSIS".equals(id)) { //$NON-NLS-1$
            latestItems = getLatestModifiedItems(ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT, 8);
            url = "http://org.eclipse.ui.intro/runAction?pluginId=org.talend.dataprofiler.core&" //$NON-NLS-1$
                    + "class=org.talend.dataprofiler.core.ui.action.actions.OpenItemEditorAction&" //$NON-NLS-1$
                    + "id=org.talend.dataprofiler.core.ui.action.actions.OpenItemEditorAction&nodeId="; //$NON-NLS-1$
            if (latestItems.size() == 0) {
                parent.appendChild(dom.createTextNode(Messages.getString("DynamicContentProvider.analysis"))); //$NON-NLS-1$
            }
        } else if ("ALWAYS_WELCOME".equals(id) && PluginChecker.isTIS()) { //$NON-NLS-1$
            creatAlwaysWelcome(dom, parent);
        } else if ("CUSTOMER_PAGE".equals(id)) { //$NON-NLS-1$
            createNewsPage(dom, parent);
        } else if ("INTEGRATION_CLOUD".equals(id)) { //$NON-NLS-1$
            createCloudPage(dom, parent);
        } else if ("TOP_MESSAGE".equals(id)) { //$NON-NLS-1$
            createTopMessage(dom, parent);
        }

        for (IRepositoryViewObject object : latestItems) {
            Element hyperlink = dom.createElement("a"); //$NON-NLS-1$
            hyperlink.setAttribute("href", url + object.getId()); //$NON-NLS-1$
            hyperlink.setAttribute("class", "xh"); //$NON-NLS-1$ //$NON-NLS-2$
            hyperlink.setAttribute("title", "Modified at " + object.getModificationDate() + " by " + object.getAuthor() + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                    + "Created at " + object.getCreationDate() + " by " + object.getAuthor()); //$NON-NLS-1$ //$NON-NLS-2$
            hyperlink.appendChild(dom.createTextNode(object.getLabel() + " " + object.getVersion())); //$NON-NLS-1$
            parent.appendChild(hyperlink);
            parent.appendChild(dom.createElement("br")); //$NON-NLS-1$
        }

    }

    protected void creatAlwaysWelcome(Document dom, Element parent) {
        // content for always welcome check box
        IPreferenceStore store = CorePlugin.getDefault().getPreferenceStore();
        boolean showIntroConfig = store.getBoolean(ITalendCorePrefConstants.ALWAYS_WELCOME);
        String url = "location.href='http://org.eclipse.ui.intro/runAction?pluginId=org.talend.rcp&" //$NON-NLS-1$
                + "class=org.talend.rcp.intro.AlwaysWelcomeAction'"; //$NON-NLS-1$
        Element input = dom.createElement("input"); //$NON-NLS-1$
        input.setAttribute("type", "checkbox"); //$NON-NLS-1$ //$NON-NLS-2$
        if (!showIntroConfig) {
            input.setAttribute("checked", "checked"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        input.setAttribute("onclick", url); //$NON-NLS-1$

        input.appendChild(dom.createTextNode(Messages.getString("DynamicContentProvider.isDisplayTitle"))); //$NON-NLS-1$
        parent.appendChild(input);
    }

    protected String getOnlinePageURL(String onlinePage) {
        StringBuffer url = new StringBuffer();
        url.append(onlinePage);
        // edition
        String edition = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IBrandingService.class)) {
            IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault()
                    .getService(IBrandingService.class);
            edition = brandingService.getAcronym();
        }
        // version
        String version = VersionUtils.getVersion();
        StringBuffer sb = new StringBuffer();
        if (version != null && !"".equals(version)) { //$NON-NLS-1$
            StringTokenizer stringTokenizer = new StringTokenizer(version, LEVEL_SEPARATOR);
            try {
                sb.append(stringTokenizer.nextToken());
                sb.append(LEVEL_SEPARATOR);
                sb.append(stringTokenizer.nextToken());
                sb.append(LEVEL_SEPARATOR);
                sb.append(stringTokenizer.nextToken());
            } catch (NumberFormatException e) {
            }
        }
        //
        IPreferenceStore prefStore = CoreUIPlugin.getDefault().getPreferenceStore();
        boolean activeDataCollector = prefStore.getBoolean(ITalendCorePrefConstants.DATA_COLLECTOR_ENABLED);
        if (activeDataCollector && version != null && edition != null) {
            // uuid
            DefaultTokenCollector dtc = new DefaultTokenCollector();
            url.append("?version=").append(sb.toString()).append("&uid=").append(dtc.calcUniqueId()).append("&edition=") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    .append(edition);
        } else if (!activeDataCollector && version != null && edition != null) {
            url.append("?version=").append(sb.toString()).append("&edition=").append(edition); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return url.toString();
    }

    protected void createNewsPage(Document dom, Element parent) {
        createOnlinePage(dom, parent, ONLINE_PAGE_URL, Messages.getString("DynamicContentProvider.TalendNewsTitle")); //$NON-NLS-1$
    }

    protected void createCloudPage(Document dom, Element parent) {
        createOnlinePage(dom, parent, CLOUD_PAGE_URL, null);
    }

    protected void createOnlinePage(Document dom, Element parent, String onlinePageUrl, String title) {
        if (!NetworkUtil.isNetworkValid()) {
            setDIVStyle(dom, false);
            return;
        }
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(getOnlinePageURL(onlinePageUrl));
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET"); //$NON-NLS-1$
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setReadTimeout(2000);
            urlConnection.getInputStream();
            setDIVStyle(dom, true);
        } catch (Exception e) {
            setDIVStyle(dom, false);
            return;
        } finally {
            urlConnection.disconnect();
        }

        // online content

        Element tdElem = dom.createElement("td"); //$NON-NLS-1$
        setTDAttribute(tdElem);
        parent.appendChild(tdElem);

        Element div = dom.createElement("div"); //$NON-NLS-1$
        div.setAttribute("style", "overflow:auto;height:400px;width:260px;padding-left:20px;"); //$NON-NLS-1$ //$NON-NLS-2$
        tdElem.appendChild(div);

        if (title != null) {
            Element spanElem = dom.createElement("span"); //$NON-NLS-1$
            spanElem.setAttribute("class", "style_1 style_2 style_3"); //$NON-NLS-1$ //$NON-NLS-2$
            spanElem.appendChild(dom.createTextNode(title));
            div.appendChild(spanElem);
            div.appendChild(dom.createElement("br")); //$NON-NLS-1$
        }
        
        Element iFrame = dom.createElement("iframe"); //$NON-NLS-1$
        iFrame.setAttribute("src", getOnlinePageURL(onlinePageUrl)); //$NON-NLS-1$
        iFrame.setAttribute("frameborder", "0"); //$NON-NLS-1$ //$NON-NLS-2$
        iFrame.setAttribute("width", "240px"); //$NON-NLS-1$ //$NON-NLS-2$
        iFrame.setAttribute("height", "370px"); //$NON-NLS-1$ //$NON-NLS-2$
        iFrame.appendChild(dom.createTextNode(" ")); //$NON-NLS-1$
        div.appendChild(iFrame);
    }

    protected void createTopMessage(Document dom, Element parent) {
        Element topMessageElem = dom.createElement("div"); //$NON-NLS-1$
        topMessageElem.setAttribute("style", //$NON-NLS-1$
                "position: absolute;left: 660px;top: 22px;width: 200px;text-align: center;line-height: 28px;letter-spacing: 1px;"); //$NON-NLS-1$
        parent.appendChild(topMessageElem);

        Element ticHrefElem = dom.createElement("a"); //$NON-NLS-1$
        String url = TRY_CLOUD_URL;
        try {
            url = URLEncoder.encode(url, "UTF-8"); //$NON-NLS-1$
        } catch (Exception e) {
            org.talend.commons.exception.ExceptionHandler.process(e);
        }
        ticHrefElem.setAttribute("href", OPEN_IN_BROWSER_URL + url); //$NON-NLS-1$
        ticHrefElem.setAttribute("style", //$NON-NLS-1$
                "color: #c3d600;font-size: 20px;text-decoration: none;font-family: Calibri, Candara, Segoe, Segoe UI, Optima, Arial, sans-serif;"); //$NON-NLS-1$
        topMessageElem.appendChild(ticHrefElem);

        Text tryCloudTextNode = dom.createTextNode(Messages.getString("DynamicContentProvider.tryCloud")); //$NON-NLS-1$
        ticHrefElem.appendChild(tryCloudTextNode);
    }

    protected void setTDAttribute(Element tdElem) {
        tdElem.setAttribute("class", "separator"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    protected List<IRepositoryViewObject> getLatestModifiedItems(ERepositoryObjectType type, int count) {
        List<IRepositoryViewObject> latestItems = new ArrayList<IRepositoryViewObject>();
        try {
            Project currentProject = ProjectManager.getInstance().getCurrentProject();
            if (currentProject != null) {
                List<IRepositoryViewObject> all = ProxyRepositoryFactory.getInstance().getAll(currentProject, type);
                IRepositoryViewObject[] data = new IRepositoryViewObject[all.size()];
                all.toArray(data);
                for (int i = 0; i < data.length && i < count; i++) {
                    for (int j = data.length - 1; j > i; j--) {
                        Date modificationDate = data[j].getModificationDate();
                        Date modificationDate2 = data[j - 1].getModificationDate();
                        if (modificationDate == null) {
                            modificationDate = data[j].getCreationDate();
                        }
                        if (modificationDate2 == null) {
                            modificationDate2 = data[j - 1].getCreationDate();
                        }
                        if (modificationDate != null && modificationDate2 != null && modificationDate.after(modificationDate2)) {
                            IRepositoryViewObject temp = data[j - 1];
                            data[j - 1] = data[j];
                            data[j] = temp;
                        }
                    }
                    latestItems.add(data[i]);
                }

            }
            return latestItems;
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
        return latestItems;
    }

    private void setDIVStyle(Document dom, boolean online) {
        NodeList elementsByTagName = dom.getElementsByTagName("div"); //$NON-NLS-1$
        // Node[] array = ModelUtil.getArray(elementsByTagName);
        Node[] array = TalendHtmlModelUtil.getArray(elementsByTagName);
        Element leftDiv = null;
        Element rightDiv = null;
        for (Node element : array) {
            Element node = (Element) element;
            if ("div_left_part".equals(node.getAttribute("id"))) { //$NON-NLS-1$ //$NON-NLS-2$
                leftDiv = node;
            } else if ("div_right_part".equals(node.getAttribute("id"))) { //$NON-NLS-1$ //$NON-NLS-2$
                rightDiv = node;
            }

        }

        if (leftDiv != null) {
            if (online) {
                leftDiv.setAttribute("class", "left_1"); //$NON-NLS-1$ //$NON-NLS-2$
            } else {
                leftDiv.setAttribute("class", "left"); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }

        if (rightDiv != null) {
            if (online) {
                rightDiv.setAttribute("class", "right_1"); //$NON-NLS-1$ //$NON-NLS-2$
            } else {
                rightDiv.setAttribute("class", "right"); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.intro.config.IIntroContentProvider#createContent(java.lang.String, java.io.PrintWriter)
     */
    @Override
    public void createContent(String id, PrintWriter out) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.intro.config.IIntroContentProvider#createContent(java.lang.String,
     * org.eclipse.swt.widgets.Composite, org.eclipse.ui.forms.widgets.FormToolkit)
     */
    @Override
    public void createContent(String id, Composite parent, FormToolkit toolkit) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.intro.config.IIntroContentProvider#dispose()
     */
    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.intro.config.IIntroContentProvider#init(org.eclipse.ui.intro.config.IIntroContentProviderSite)
     */
    @Override
    public void init(IIntroContentProviderSite site) {
    }

}
