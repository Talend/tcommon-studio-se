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
package org.talend.presentation.onboarding.ui.html;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.eclipse.ui.internal.intro.impl.html.IIntroHTMLConstants;
import org.eclipse.ui.internal.intro.impl.model.IntroContentProvider;
import org.eclipse.ui.internal.intro.impl.model.loader.ContentProviderManager;
import org.eclipse.ui.internal.intro.impl.model.loader.IntroContentParser;
import org.eclipse.ui.internal.intro.impl.model.util.ModelUtil;
import org.eclipse.ui.intro.config.IIntroContentProviderSite;
import org.eclipse.ui.intro.config.IIntroXHTMLContentProvider;
import org.talend.presentation.onboarding.i18n.Messages;
import org.talend.presentation.onboarding.interfaces.IOnBoardingJsonI18n;
import org.talend.presentation.onboarding.ui.managers.OnBoardingManager;
import org.talend.presentation.onboarding.ui.runtimedata.OnBoardingPageBean;
import org.talend.presentation.onboarding.ui.runtimedata.OnBoardingPresentationData;
import org.talend.presentation.onboarding.utils.OnBoardingConstants;
import org.talend.presentation.onboarding.utils.OnBoardingUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * DOC talend class global comment. Detailled comment
 */
@SuppressWarnings("restriction")
public class HtmlContentHelper {

    private OnBoardingManager onBoardingManager;

    public HtmlContentHelper(OnBoardingManager obManager) {
        onBoardingManager = obManager;
    }

    public String getHtmlContent(URL htmlUrl) throws IOException {
        String content = ""; //$NON-NLS-1$
        if (htmlUrl != null) {
            String result = htmlUrl.toExternalForm();
            if (result.startsWith("file:/")) { //$NON-NLS-1$
                if (result.startsWith("file:///") == false) { //$NON-NLS-1$
                    result = "file:///" + result.substring(6); //$NON-NLS-1$
                }
            }

            IntroContentParser parser = new IntroContentParser(result);
            Document dom = parser.getDocument();
            if (dom != null) {
                resolveInternationalization(dom);
                resolveDynamicContent(dom, null);
                content = IntroContentParser.convertToString(dom);
            }
        }

        return content;
    }

    private void resolveInternationalization(Document dom) {
        NodeList internationals = dom.getElementsByTagNameNS("*", //$NON-NLS-1$
                OnBoardingConstants.KEY_INTERNATIONAL);

        Node[] nodes = ModelUtil.getArray(internationals);
        for (Node node : nodes) {
            Element internationalElement = (Element) node;
            internationalElement.getParentNode().replaceChild(
                    dom.createTextNode(Messages.getString(internationalElement.getAttribute(IIntroHTMLConstants.ATTRIBUTE_ID))),
                    internationalElement);
        }
    }

    private Document resolveDynamicContent(Document dom, IIntroContentProviderSite site) {

        // get all content provider elements in DOM.
        NodeList contentProviders = dom.getElementsByTagNameNS("*", //$NON-NLS-1$
                IntroContentProvider.TAG_CONTENT_PROVIDER);

        List<OnBoardingPresentationData> presDatas = onBoardingManager.getPresentationDatas();
        if (presDatas == null) {
            return dom;
        }
        int index = onBoardingManager.getCurrentSelectedPresentationDataIndex();
        if (index < 0) {
            return dom;
        }
        OnBoardingPresentationData presData = onBoardingManager.getCurrentSelectedPresentationData();
        if (presData == null) {
            index = presDatas.size() - 1;
            onBoardingManager.setCurrentSelectedPresentationDataIndex(index);
            presData = onBoardingManager.getCurrentSelectedPresentationData();
        }
        OnBoardingPageBean docBean = presData.getPageBean();

        // get the array version of the nodelist to work around DOM api design.
        Node[] nodes = ModelUtil.getArray(contentProviders);
        for (Node node : nodes) {
            Element contentProviderElement = (Element) node;
            String id = contentProviderElement.getAttribute(IIntroHTMLConstants.ATTRIBUTE_ID);
            // if (OnBoardingConstants.HTML_CSS_PATH.equals(id)) {
            // Element link = dom.createElement(IIntroHTMLConstants.ELEMENT_LINK);
            //
            // Attr rel = dom.createAttribute(IIntroHTMLConstants.ATTRIBUTE_RELATIONSHIP);
            // rel.setValue(IIntroHTMLConstants.LINK_REL);
            // Attr href = dom.createAttribute(IIntroHTMLConstants.ATTRIBUTE_HREF);
            // href.setValue(OnBoardingUtils.getResourceLocalURL(OnBoardingConstants.ON_BOARDING_VIEW_CSS_PATH).toString());
            //
            // link.setAttributeNode(rel);
            // link.setAttributeNode(href);
            //
            // node.getParentNode().replaceChild(link, node);
            // } else
            if (OnBoardingConstants.HTML_STEP.equals(id)) {
                node.getParentNode().replaceChild(dom.createTextNode("" + (index + 1)), node); //$NON-NLS-1$
            } else if (OnBoardingConstants.HTML_TITLE.equals(id)) {
                node.getParentNode().replaceChild(
                        convertToNode(dom, site, docBean.getTitle(), OnBoardingConstants.HTML_DIV_TITLE_ID), node);
            } else if (OnBoardingConstants.HTML_CONTENT.equals(id)) {
                node.getParentNode().replaceChild(
                        convertToNode(dom, site, docBean.getContent(), OnBoardingConstants.HTML_DIV_CONTENT_ID), node);
            } else if (OnBoardingConstants.HTML_BULLETS.equals(id)) {
                Node parent = node.getParentNode();
                parent.removeChild(node);
                int totalSize = presDatas.size();
                for (int i = 0; i < totalSize; i++) {
                    Element li = dom.createElement(OnBoardingConstants.HTML_TAG_LI);
                    Element a = dom.createElement(IIntroHTMLConstants.ELEMENT_ANCHOR);
                    Attr href = dom.createAttribute(IIntroHTMLConstants.ATTRIBUTE_HREF);
                    href.setValue("javascript:void(0);"); //$NON-NLS-1$
                    Attr clazz = dom.createAttribute(IIntroHTMLConstants.ATTRIBUTE_CLASS);
                    if (index == i) {
                        clazz.setValue("active"); //$NON-NLS-1$
                    } else {
                        clazz.setValue(""); //$NON-NLS-1$
                    }
                    Attr onClick = dom.createAttribute("onClick"); //$NON-NLS-1$
                    onClick.setValue("showPage(" + i + ");"); //$NON-NLS-1$//$NON-NLS-2$

                    a.setAttributeNode(href);
                    a.setAttributeNode(clazz);
                    a.setAttributeNode(onClick);
                    a.setTextContent(" "); //$NON-NLS-1$

                    li.appendChild(a);
                    parent.appendChild(li);
                }
            } else if (OnBoardingConstants.HTML_MANAGER_ID.equals(id)) {
                node.getParentNode().replaceChild(dom.createTextNode(onBoardingManager.getManagerId()), node);
            }

        }

        if (index <= 0) {
            disableButton(dom, OnBoardingConstants.HTML_BUTTON_BACK);
        }
        // can't use <else if>, example: when there is only 1 page
        if (presDatas.size() - 1 <= index) {
            Element skipButton = ModelUtil.getElementById(dom, OnBoardingConstants.HTML_BUTTON_SKIP);
            Node oldText = skipButton.getFirstChild();
            skipButton.replaceChild(dom.createTextNode(Messages.getString("onBoardingComposite.button.skip.letMeTry")), oldText); //$NON-NLS-1$

            disableButton(dom, OnBoardingConstants.HTML_BUTTON_NEXT);
        }
        return dom;
    }

    private void disableButton(Document dom, String buttonId) {
        // Element button = dom.getElementById(buttonId);
        Element button = ModelUtil.getElementById(dom, buttonId);
        String buttonCssClasses = button.getAttribute(IIntroHTMLConstants.ATTRIBUTE_CLASS);
        buttonCssClasses = buttonCssClasses + " " + OnBoardingConstants.HTML_CSS_CLASS_BUTTON_DISABLED; //$NON-NLS-1$
        button.setAttribute(IIntroHTMLConstants.ATTRIBUTE_CLASS, buttonCssClasses);
    }

    private Node convertToNode(Document dom, IIntroContentProviderSite site, String content, String divId) {
        Node newNode = null;

        // avoid exception
        String safeContent = "<div id='" + divId + "'>" + content + "</div>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        Document contentDom = OnBoardingUtils.convertStringToDocument(safeContent);
        if (contentDom != null) {
            resolveI18nFromJsonDoc(contentDom);
            contentDom = resolveDynamicContentFromJsonDoc(contentDom, site);
            NodeList children = contentDom.getChildNodes();
            if (children != null && 0 < children.getLength()) {
                newNode = dom.adoptNode(children.item(0).cloneNode(true));
            }
        }

        return newNode;
    }

    private Document resolveDynamicContentFromJsonDoc(Document dom, IIntroContentProviderSite site) {
        // get all content provider elements in DOM.
        NodeList contentProviders = dom.getElementsByTagNameNS("*", //$NON-NLS-1$
                IntroContentProvider.TAG_CONTENT_PROVIDER);

        // get the array version of the nodelist to work around DOM api design.
        Node[] nodes = ModelUtil.getArray(contentProviders);
        for (Node node : nodes) {
            Element contentProviderElement = (Element) node;
            IntroContentProvider provider = new IntroContentProvider(contentProviderElement, OnBoardingUtils.getBundle());
            // If we've already loaded the content provider for this element,
            // retrieve it, otherwise load the class.
            IIntroXHTMLContentProvider providerClass = (IIntroXHTMLContentProvider) ContentProviderManager.getInst()
                    .getContentProvider(provider);
            if (providerClass == null) {
                // content provider never created before, create it.
                providerClass = (IIntroXHTMLContentProvider) ContentProviderManager.getInst().createContentProvider(provider,
                        site);
            }

            if (providerClass != null) {
                // create a div with the same id as the contentProvider, pass it
                // as the parent to create the specialized content, and then
                // replace the contentProvider element with this div.
                Element contentDiv = OnBoardingUtils.createDivElement(dom, provider.getId());
                providerClass.createContent(provider.getId(), contentDiv);

                contentProviderElement.getParentNode().replaceChild(contentDiv, contentProviderElement);
            } else {
                // we couldn't load the content provider, so add any alternate
                // text content if there is any.
                // INTRO: do it. 3.0 intro content style uses text element as
                // alt text. We can load XHTML content here.
            }
        }
        return dom;
    }

    private void resolveI18nFromJsonDoc(Document dom) {
        IOnBoardingJsonI18n i18n = onBoardingManager.getI18n();
        if (i18n == null) {
            return;
        }

        NodeList internationals = dom.getElementsByTagNameNS("*", //$NON-NLS-1$
                OnBoardingConstants.KEY_INTERNATIONAL);

        Node[] nodes = ModelUtil.getArray(internationals);
        for (Node node : nodes) {
            Element internationalElement = (Element) node;
            internationalElement.getParentNode().replaceChild(
                    dom.createTextNode(i18n.getI18NString(internationalElement.getAttribute(IIntroHTMLConstants.ATTRIBUTE_ID))),
                    internationalElement);
        }
    }
}
