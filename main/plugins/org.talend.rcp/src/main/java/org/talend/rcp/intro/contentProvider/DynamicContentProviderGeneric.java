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
package org.talend.rcp.intro.contentProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.core.CorePlugin;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.rcp.i18n.Messages;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * DOC WCHEN added for feature TDI-18168
 */
public class DynamicContentProviderGeneric extends DynamicContentProvider {

    private static final String BROWSER_URL = "http://org.eclipse.ui.intro/runAction?pluginId=org.talend.rcp&class=org.talend.rcp.intro.OpenWebBrowserAction&type="; //$NON-NLS-1$

    private static final String BROWSER_DEMO_URL = "http://org.eclipse.ui.intro/runAction?pluginId=org.talend.rcp&class=org.talend.rcp.intro.ImportDemoProjectAction&type="; //$NON-NLS-1$

    @Override
    public void createContent(String id, Element parent) {
        String dBranding = "default";
        String branding = System.getProperty("talend.license.branding");
        if (branding == null || "".equals(branding)) {
            branding = dBranding;
        }
        Document dom = parent.getOwnerDocument();

        String imgBrandingPath = "";
        String imgCommonPath = "";

        String content = dom.getDocumentURI();
        // baseUri like :file:///D:/Talend_trunk_gtk/org.talend.rcp.branding.generic/content/root.xhtml
        if (content != null && !"".equals(content)) {
            File file = new File(content);
            int index = content.indexOf(file.getName());
            imgBrandingPath = content.substring(0, index) + "../brandings/" + branding;
            imgCommonPath = content.substring(0, index);
        }

        // latest Items
        List<IRepositoryViewObject> latestItems = new ArrayList<IRepositoryViewObject>();
        String url = "";
        if ("TOP_IMAGE".equals(id)) {
            Element img = dom.createElement("img");
            img.setAttribute("src", imgBrandingPath + "/icons/welcome_logo.png");
            parent.appendChild(img);
        } else if ("BOTTOM_IMG".equals(id)) {
            Element img = dom.createElement("img");
            img.setAttribute("src", imgBrandingPath + "/icons/bottom.png");
            img.setAttribute("width", "880px");
            parent.appendChild(img);
        } else if (ERepositoryObjectType.PROCESS != null && ERepositoryObjectType.PROCESS.name().equals(id)) {
            Element span = dom.createElement("span");
            span.setAttribute("class", "style_1 style_2 style_3");
            span.appendChild(dom.createTextNode(Messages.getString("WelcomePageDynamicContentProvider.LatestItems.Title")));
            parent.appendChild(span);
            span.appendChild(dom.createElement("br"));
            String title = Messages.getString("WelcomePageDynamicContentProvider.LatestItemsJob.Title");
            createLatestItemTitlePart(dom, parent, imgCommonPath + "imgs/img_process.png", title);

            latestItems = getLatestModifiedItems(ERepositoryObjectType.PROCESS, 8);
            url = "http://org.eclipse.ui.intro/runAction?pluginId=org.talend.designer.core&"
                    + "class=org.talend.designer.core.ui.action.EditProcess&"
                    + "id=org.talend.designer.core.actions.editprocess&nodeId=";
            if (latestItems.size() == 0) {
                parent.appendChild(dom.createElement("br"));
            }
        } else if (ERepositoryObjectType.BUSINESS_PROCESS != null && ERepositoryObjectType.BUSINESS_PROCESS.name().equals(id)) {
            String title = Messages.getString("WelcomePageDynamicContentProvider.LatestItemsBusinessModel.Title");
            createLatestItemTitlePart(dom, parent, imgCommonPath + "imgs/img_businessProcess.png", title);
            latestItems = getLatestModifiedItems(ERepositoryObjectType.BUSINESS_PROCESS, 8);
            url = "http://org.eclipse.ui.intro/runAction?pluginId=org.talend.designer.business.diagram&"
                    + "class=org.talend.designer.business.diagram.custom.actions.OpenDiagramAction&"
                    + "id=org.talend.designer.business.diagram.Action2&nodeId=";
            if (latestItems.size() == 0) {
                parent.appendChild(dom.createElement("br"));
            }
        } else if ("ANALYSIS".equals(id) && isItemShow("ANALYSIS")) {
            String title = Messages.getString("WelcomePageDynamicContentProvider.LatestItemsAnalysis.Title");
            createLatestItemTitlePart(dom, parent, imgCommonPath + "imgs/chart_bar.png", title);
            latestItems = getLatestModifiedItems(ERepositoryObjectType.TDQ_ANALYSIS_ELEMENT, 8);
            url = "http://org.eclipse.ui.intro/runAction?pluginId=org.talend.dataprofiler.core&"
                    + "class=org.talend.dataprofiler.core.ui.action.actions.OpenItemEditorAction&"
                    + "id=org.talend.dataprofiler.core.ui.action.actions.OpenItemEditorAction&nodeId=";
            if (latestItems.size() == 0) {
                parent.appendChild(dom.createElement("br"));
            }
        } else if ("SERVICES".equals(id) && isItemShow("SERVICES")) {
            String title = Messages.getString("WelcomePageDynamicContentProvider.LatestItemsServices.Title");
            createLatestItemTitlePart(dom, parent, imgCommonPath + "imgs/img_service.png", title);
            ERepositoryObjectType repositoryServicesType = ERepositoryObjectType.valueOf(ERepositoryObjectType.class, "SERVICES");
            latestItems = getLatestModifiedItems(repositoryServicesType, 8);
            url = "http://org.eclipse.ui.intro/runAction?pluginId=org.talend.repository.services&"
                    + "class=org.talend.repository.services.action.OpenWSDLEditorAction&"
                    + "id=org.talend.repository.services.action.OpenWSDLEditorAction&nodeId=";
            if (latestItems.size() == 0) {
                parent.appendChild(dom.createElement("br"));
            }
        } else if (ERepositoryObjectType.PROCESS_ROUTE != null && ERepositoryObjectType.PROCESS_ROUTE.name().equals(id)) {
            String title = Messages.getString("WelcomePageDynamicContentProvider.LatestItemsRoutes.Title");
            createLatestItemTitlePart(dom, parent, imgCommonPath + "imgs/img_route.png", title);
            latestItems = getLatestModifiedItems(ERepositoryObjectType.PROCESS_ROUTE, 8);
            url = "http://org.eclipse.ui.intro/runAction?pluginId=org.talend.camel.designer&"
                    + "class=org.talend.camel.designer.ui.EditCamelProcess&"
                    + "id=org.talend.camel.designer.ui.EditCamelProcess&nodeId=";
            if (latestItems.size() == 0) {
                parent.appendChild(dom.createElement("br"));
            }
        } else if ("DATA_MODEL".equals(id) && isItemShow("MDM.DataModel")) {
            String title = Messages.getString("WelcomePageDynamicContentProvider.LatestItemsDataModel.Title");
            createLatestItemTitlePart(dom, parent, imgCommonPath + "imgs/img_datamodel.png", title);
            ERepositoryObjectType repositoryRoutesType = ERepositoryObjectType.valueOf(ERepositoryObjectType.class,
                    "MDM.DataModel");
            latestItems = getLatestModifiedItems(repositoryRoutesType, 8);
            url = "http://org.eclipse.ui.intro/runAction?pluginId=org.talend.mdm.repository&"
                    + "class=org.talend.mdm.repository.ui.actions.OpenObjectAction&"
                    + "id=org.talend.mdm.repository.ui.actions.OpenObjectAction&nodeId=";
            if (latestItems.size() == 0) {
                parent.appendChild(dom.createElement("br"));
            }
        } else if ("ALWAYS_WELCOME".equals(id)) {
            // content for always welcome check box
            IPreferenceStore store = CorePlugin.getDefault().getPreferenceStore();
            boolean showIntroConfig = store.getBoolean(ITalendCorePrefConstants.ALWAYS_WELCOME);
            url = "location.href='http://org.eclipse.ui.intro/runAction?pluginId=org.talend.rcp&"
                    + "class=org.talend.rcp.intro.AlwaysWelcomeAction'";
            Element input = dom.createElement("input");
            input.setAttribute("type", "checkbox");
            if (!showIntroConfig) {
                input.setAttribute("checked", "checked");
            }
            input.setAttribute("onclick", url);

            input.appendChild(dom.createTextNode(Messages.getString("DynamicContentProvider.isDisplayTitle")));
            parent.appendChild(input);
        } else if ("CUSTOMER_PAGE".equals(id)) {
            createOnlinePage(dom, parent);
        } else if ("CREATE_NEW_ITEM".equals(id)) {
            Element td = dom.createElement("td");
            setTDStyle(td);
            parent.appendChild(td);
            Element divRight = dom.createElement("div");
            divRight.setAttribute("id", "div_right_part");
            td.appendChild(divRight);
            parent = divRight;
            createTitlePart(dom, parent, Messages.getString("WelcomePageDynamicContentProvider.CreateNewTitle"));
            // create job
            String text = Messages.getString("WelcomePageDynamicContentProvider.LatestItemsJob.Title");
            url = "http://org.eclipse.ui.intro/runAction?pluginId=org.talend.designer.core&class=org.talend.designer.core.ui.action.CreateProcess&id=org.talend.designer.core.actions.createprocess&type=PROCESS";
            String title = "Create a data integration process";
            createANewItem(dom, parent, imgCommonPath + "imgs/img_process.png", text, title, url);

            // create business
            text = Messages.getString("WelcomePageDynamicContentProvider.CreateNewBusinessModelTitle");
            url = "http://org.eclipse.ui.intro/runAction?pluginId=org.talend.designer.business.diagram&class=org.talend.designer.business.diagram.custom.actions.CreateDiagramAction&id=org.talend.designer.business.diagram.custom.actions.CreateDiagramAction&type=BUSINESS_PROCESS";
            title = "Create a business model";
            createANewItem(dom, parent, imgCommonPath + "imgs/img_businessProcess.png", text, title, url);

            // create analysis
            if (isItemShow("ANALYSIS")) {
                text = Messages.getString("WelcomePageDynamicContentProvider.CreateNewAnalysisTitle");
                url = "http://org.eclipse.ui.intro/runAction?pluginId=org.talend.dataprofiler.core&class=org.talend.dataprofiler.core.ui.action.actions.CreateNewAnalysisAction&id=org.talend.dataprofiler.core.ui.action.actions.CreateNewAnalysisAction&type=ANALYSIS";
                title = "Create an analysis";
                createANewItem(dom, parent, imgCommonPath + "imgs/chart_bar.png", text, title, url);
            }

            // create DataModels
            if (isItemShow("MDM.DataModel")) {
                text = Messages.getString("WelcomePageDynamicContentProvider.CreateNewDataModelTitle");
                url = "http://org.eclipse.ui.intro/runAction?pluginId=org.talend.mdm.repository&class=org.talend.mdm.repository.ui.actions.datamodel.NewDataModelAction&id=org.talend.mdm.repository.ui.actions.datamodel.NewDataModelAction&type=MDM.DataModel";
                title = "Create a data model";
                createANewItem(dom, parent, imgCommonPath + "imgs/img_datamodel.png", text, title, url);
            }

            // create service
            if (isItemShow("SERVICES")) {
                text = Messages.getString("WelcomePageDynamicContentProvider.CreateNewServiceTitle");
                url = "http://org.eclipse.ui.intro/runAction?pluginId=org.talend.repository.services&class=org.talend.repository.services.action.CreateESBAction&id=org.talend.repository.services.action.CreateESBAction&type=SERVICES";
                title = "Create a service";
                createANewItem(dom, parent, imgCommonPath + "imgs/img_service.png", text, title, url);
            }

            // create route
            if (isItemShow("ROUTE")) {
                text = Messages.getString("WelcomePageDynamicContentProvider.CreateNewRouteTitle");
                url = "http://org.eclipse.ui.intro/runAction?pluginId=org.talend.camel.designer&class=org.talend.camel.designer.ui.CreateCamelProcess&id=org.talend.camel.designer.ui.CreateCamelProcess&type=ROUTE";
                title = "Create a route";
                createANewItem(dom, parent, imgCommonPath + "imgs/img_route.png", text, title, url);
            }

            // documentation
            title = Messages.getString("WelcomePageDynamicContentProvider.DocumentationTitle");
            // String huGuide = Messages.getString("WelcomePageDynamicContentProvider.DocumentationUserGuideTitle");
            // String hrGuide =
            // Messages.getString("WelcomePageDynamicContentProvider.DocumentationReferenceGuideTitle");
            String talendHelpCenter = Messages.getString("WelcomePageDynamicContentProvider.DocumentationTalendHelpCenter");
            String docForDownload = Messages.getString("WelcomePageDynamicContentProvider.DocumentationForDownload");
            String[] hyperlinkText = new String[] { talendHelpCenter, docForDownload };

            String[] urls = new String[] { BROWSER_URL + "showTalendHelpCenter", BROWSER_URL + "showUserGuide" };

            String[] extTexts = new String[2];
            parent.appendChild(dom.createElement("br"));
            createFixedPart(dom, parent, title, hyperlinkText, urls, extTexts);

            // getting started
            title = Messages.getString("WelcomePageDynamicContentProvider.GettingStartedTitle");
            String demoProject = Messages.getString("WelcomePageDynamicContentProvider.GettingStartedImportDemoTitle");
            String hTutorial = Messages.getString("WelcomePageDynamicContentProvider.GettingStartedTutorialsTitle");
            String hforum = Messages.getString("WelcomePageDynamicContentProvider.GettingStartedForumsTitle");
            String hTrainning = Messages.getString("WelcomePageDynamicContentProvider.GettingStartedTrainningTitle");
            hyperlinkText = new String[] { demoProject, hTutorial, hforum, hTrainning };

            urls = new String[] { BROWSER_DEMO_URL + "ShowDemos", BROWSER_URL + "showTutorials", BROWSER_URL + "showForums",
                    BROWSER_URL + "showTrainning", };

            String extDemo = ": " + Messages.getString("WelcomePageDynamicContentProvider.GettingStartedImportDemoBrief");
            String extTutorial = ": " + Messages.getString("WelcomePageDynamicContentProvider.GettingStartedTutorialsBrief");
            String extforum = ": " + Messages.getString("WelcomePageDynamicContentProvider.GettingStartedForumsBrief");
            String extTrainning = ": " + Messages.getString("WelcomePageDynamicContentProvider.GettingStartedTrainningBrief");

            extTexts = new String[] { extDemo, extTutorial, extforum, extTrainning };

            createFixedPart(dom, parent, title, hyperlinkText, urls, extTexts);

            // start now button
            Element imgP = dom.createElement("p");
            imgP.setAttribute("align", "center");
            parent.appendChild(imgP);

            Element imgLink = dom.createElement("a");
            imgLink.setAttribute("href", "http://org.eclipse.ui.intro/close");
            imgP.appendChild(imgLink);

            Element img = dom.createElement("img");
            img.setAttribute("src", imgCommonPath + "imgs/startNow.jpg");
            img.setAttribute("width", "120px");
            img.setAttribute("height", "25px");
            imgLink.appendChild(img);

        }

        for (int i = 0; i < latestItems.size(); i++) {
            IRepositoryViewObject object = latestItems.get(i);
            Element hyperlink = dom.createElement("a");
            hyperlink.setAttribute("class", "xh");
            hyperlink.setAttribute("href", url + object.getId());
            hyperlink.setAttribute("title", "Modified at " + object.getModificationDate() + " by " + object.getAuthor() + "\n"
                    + "Created at " + object.getCreationDate() + " by " + object.getAuthor());
            hyperlink.appendChild(dom.createTextNode(object.getLabel() + " " + object.getVersion()));
            parent.appendChild(hyperlink);
            if (i != latestItems.size() - 1) {
                parent.appendChild(dom.createElement("br"));
            }
        }
    }

    private void createLatestItemTitlePart(Document dom, Element parent, String imgPath, String title) {
        Element img = dom.createElement("img");
        img.setAttribute("style", "padding-top:10px;margin-left:15px;");
        img.setAttribute("src", imgPath);
        Element span2 = dom.createElement("span");
        span2.setAttribute("class", "style_3");
        span2.appendChild(dom.createTextNode(title));
        parent.appendChild(img);
        parent.appendChild(span2);
        parent.appendChild(dom.createElement("br"));
        Element blockquote = dom.createElement("blockquote");
        blockquote.setAttribute("style", "margin-left:30px;");
        parent.appendChild(blockquote);
    }

    @Override
    protected void setTDAttribute(Element tdElem) {
        setTDStyle(tdElem);
    }

    /**
     * 
     * DOC create title for the CREATE_NEW_ITEM in the second column
     * 
     * @param dom
     * @param parent
     * @param title
     */
    private void createTitlePart(Document dom, Element parent, String title) {
        Element span = dom.createElement("span");
        span.setAttribute("class", "style_1 style_2 style_3");
        span.appendChild(dom.createTextNode(title));
        parent.appendChild(span);
    }

    /**
     * 
     * DOC create a new create item for the CREATE_NEW_ITEM in the second column
     * 
     * @param dom
     * @param parent
     * @param title
     */
    private void createANewItem(Document dom, Element parent, String imgPath, String text, String title, String url) {
        parent.appendChild(dom.createElement("br"));
        Element imgJob = dom.createElement("img");
        imgJob.setAttribute("style", "padding-top:10px;margin-left:15px;");
        imgJob.setAttribute("src", imgPath);
        parent.appendChild(imgJob);
        Element hyperlinkJob = dom.createElement("a");
        hyperlinkJob.setAttribute("class", "xh");
        hyperlinkJob.setAttribute("title", title);
        hyperlinkJob.setAttribute("href", url);
        Element span2 = dom.createElement("span");
        span2.setAttribute("class", "style_3");
        span2.appendChild(dom.createTextNode(text));
        hyperlinkJob.appendChild(span2);
        parent.appendChild(hyperlinkJob);

    }

    /**
     * 
     * DOC create fixed part for the CREATE_NEW_ITEM in the second column
     * 
     * @param dom
     * @param parent
     * @param title
     */
    private void createFixedPart(Document dom, Element parent, String pTitle, String hyperlinkText[], String urls[],
            String extText[]) {
        Element span = dom.createElement("p");
        span.setAttribute("class", "style_1 style_2 style_3");
        span.setAttribute("style", "padding-top:10px;");
        span.appendChild(dom.createTextNode(pTitle));
        parent.appendChild(span);
        Element blockquote = dom.createElement("blockquote");
        parent.appendChild(blockquote);
        Element p = dom.createElement("p");
        blockquote.appendChild(p);
        for (int i = 0; i < hyperlinkText.length; i++) {
            Element hyperlink = dom.createElement("a");
            hyperlink.setAttribute("href", urls[i]);
            p.appendChild(hyperlink);
            hyperlink.appendChild(dom.createTextNode(hyperlinkText[i]));
            if (extText[i] != null && !"".equals(extText[i])) {
                p.appendChild(dom.createTextNode(extText[i]));
            }
            if (i != hyperlinkText.length - 1) {
                p.appendChild(dom.createElement("br"));
            }

        }
    }

}
