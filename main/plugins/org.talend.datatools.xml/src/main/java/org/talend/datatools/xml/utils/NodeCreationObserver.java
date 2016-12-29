package org.talend.datatools.xml.utils;

import java.util.ArrayList;
import java.util.List;

public class NodeCreationObserver {

    public static boolean observe = false;

    private static List<ATreeNode> list = new ArrayList<ATreeNode>();

    public static void start() {
        list.clear();
        observe = true;
    }

    public static void stop() {
        observe = false;
    }

    public static boolean isRunning() {
        return observe;
    }

    public static void add(ATreeNode node) {
        list.add(node);
    }

    public static List<ATreeNode> getList() {
        return list;
    }

}
