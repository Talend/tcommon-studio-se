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
package org.talend.commons.ui.swt.drawing.link;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: LinkManager.java 318 2006-11-03 09:44:45 +0000 (ven., 03 nov. 2006) amaumont $
 * 
 * @param <G1> the graphical item of extremety 1
 * @param <D1> the data item of extremety 1
 * @param <G2> the graphical item of extremety 2
 * @param <D2> the data item of extremety 2
 */
public class LinksManager<G1, D1, G2, D2> {

    private List<LinkDescriptor<G1, D1, G2, D2>> links = new ArrayList<LinkDescriptor<G1, D1, G2, D2>>();

    private int currentNumberLinks = 0;

    private Map<G1, Set<LinkDescriptor<G1, D1, G2, D2>>> g1ToLinks = new HashMap<G1, Set<LinkDescriptor<G1, D1, G2, D2>>>();

    private Map<D1, Set<LinkDescriptor<G1, D1, G2, D2>>> d1ToLinks = new HashMap<D1, Set<LinkDescriptor<G1, D1, G2, D2>>>();

    private Map<G2, Set<LinkDescriptor<G1, D1, G2, D2>>> g2ToLinks = new HashMap<G2, Set<LinkDescriptor<G1, D1, G2, D2>>>();

    private Map<D2, Set<LinkDescriptor<G1, D1, G2, D2>>> d2ToLinks = new HashMap<D2, Set<LinkDescriptor<G1, D1, G2, D2>>>();

    public LinksManager() {
        super();
        currentNumberLinks = 0;
    }

    /**
     * DOC amaumont Comment method "addLink".
     * 
     * @param link
     */
    public void addLink(LinkDescriptor<G1, D1, G2, D2> link) {
        currentNumberLinks++;

        links.add(link);

        IExtremityLink<G1, D1> extremity1 = link.getExtremity1();
        IExtremityLink<G2, D2> extremity2 = link.getExtremity2();

        registerItem(link, g1ToLinks, extremity1.getGraphicalObject());
        registerItem(link, d1ToLinks, extremity1.getDataItem());
        registerItem(link, g2ToLinks, extremity2.getGraphicalObject());
        registerItem(link, d2ToLinks, extremity2.getDataItem());

    }

    /**
     * DOC amaumont Comment method "registerItem".
     * 
     * @param link
     * @param extremity
     */
    @SuppressWarnings("unchecked")
    private void registerItem(LinkDescriptor<G1, D1, G2, D2> link, Map gToLinks, Object item) {
        Set<LinkDescriptor<G1, D1, G2, D2>> linksFromItem = (Set<LinkDescriptor<G1, D1, G2, D2>>) gToLinks.get(item);
        if (linksFromItem == null) {
            linksFromItem = new HashSet<LinkDescriptor<G1, D1, G2, D2>>();
            gToLinks.put(item, linksFromItem);
        }
        linksFromItem.add(link);
    }

    /**
     * DOC amaumont Comment method "addLink".
     * 
     * @param link
     */
    public boolean removeLink(LinkDescriptor<G1, D1, G2, D2> link) {
        currentNumberLinks--;

        boolean removed = links.remove(link);

        g1ToLinks.remove(link.getExtremity1().getGraphicalObject());
        d1ToLinks.remove(link.getExtremity1().getDataItem());
        g2ToLinks.remove(link.getExtremity2().getGraphicalObject());
        d2ToLinks.remove(link.getExtremity2().getDataItem());

        return removed;
    }

    /**
     * DOC amaumont Comment method "clearLinks".
     */
    public void clearLinks() {
        links.clear();
        d1ToLinks.clear();
        g1ToLinks.clear();
        d2ToLinks.clear();
        g2ToLinks.clear();
        currentNumberLinks = 0;
    }

    /**
     * DOC amaumont Comment method "getLinks".
     * 
     * @return
     */
    public List<LinkDescriptor<G1, D1, G2, D2>> getLinks() {
        return this.links;
    }

    /**
     * Getter for currentNumberLinks.
     * 
     * @return the currentNumberLinks
     */
    public int getCurrentNumberLinks() {
        return this.currentNumberLinks;
    }

    public boolean removeLinksFromDataItem1(D1 dataItem1) {
        Set<LinkDescriptor<G1, D1, G2, D2>> linksFromD1 = d1ToLinks.get(dataItem1);
        if (linksFromD1 != null) {
            return removeLinks(linksFromD1);
        }
        return false;
    }

    public boolean removeLinksFromDataItem2(D2 dataItem2) {
        Set<LinkDescriptor<G1, D1, G2, D2>> linksFromD2 = d2ToLinks.get(dataItem2);
        if (linksFromD2 != null) {
            return removeLinks(linksFromD2);
        }
        return false;
    }

    /**
     * DOC amaumont Comment method "removeLinks".
     * 
     * @param linksFromG2
     */
    private boolean removeLinks(Set<LinkDescriptor<G1, D1, G2, D2>> linksFromG2) {
        boolean removed = false;
        for (LinkDescriptor<G1, D1, G2, D2> linkDescriptor : linksFromG2) {
            if (removeLink(linkDescriptor)) {
                removed = true;
            }
        }
        return removed;
    }

    /**
     * DOC amaumont Comment method "getLinksFromExtremity1".
     * 
     * @param treeItem
     */
    public Set<LinkDescriptor<G1, D1, G2, D2>> getLinksFromGraphicalExtremity1(G1 graphicalItem) {
        return getLinks(g1ToLinks, graphicalItem);
    }

    /**
     * DOC amaumont Comment method "getLinksFromExtremity1".
     * 
     * @param treeItem
     */
    public Set<LinkDescriptor<G1, D1, G2, D2>> getLinksFromDataExtremity1(D1 dataItem) {
        return getLinks(d1ToLinks, dataItem);
    }

    /**
     * DOC amaumont Comment method "getLinksFromExtremity1".
     * 
     * @param treeItem
     */
    public Set<LinkDescriptor<G1, D1, G2, D2>> getLinksFromGraphicalExtremity2(G2 graphicalItem) {
        return getLinks(g2ToLinks, graphicalItem);
    }

    /**
     * DOC amaumont Comment method "getLinksFromExtremity1".
     * 
     * @param treeItem
     */
    public Set<LinkDescriptor<G1, D1, G2, D2>> getLinksFromDataExtremity2(D2 dataItem) {
        return getLinks(d2ToLinks, dataItem);
    }

    /**
     * DOC amaumont Comment method "getLinks".
     * 
     * @param toLinks
     * @param dataItem
     * @return
     */
    @SuppressWarnings("unchecked")
    private Set<LinkDescriptor<G1, D1, G2, D2>> getLinks(Map objectToLinks, Object object) {
        Set<LinkDescriptor<G1, D1, G2, D2>> linksFound = (Set<LinkDescriptor<G1, D1, G2, D2>>) objectToLinks
                .get(object);
        if (linksFound == null) {
            return new HashSet<LinkDescriptor<G1, D1, G2, D2>>(0);
        }
        return new HashSet<LinkDescriptor<G1, D1, G2, D2>>(linksFound);
    }

    public void sortLinks(Comparator<LinkDescriptor<G1, D1, G2, D2>> comparator) {
        Collections.sort(links, comparator);
    }

}
