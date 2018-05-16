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
package org.talend.commons.ui.swt.geftree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * 
 * cli class global comment. Detailled comment
 */
@SuppressWarnings("unchecked")
public class TreeAnimation {

    public static final long DURATION = 210;

    public static long current;

    public static double progress;

    public static long start = -1;

    public static long finish;

    public static Viewport viewport;

    public static IFigure trackMe;

    public static IFigure showMe;

    public static Point trackLocation;

    public static boolean PLAYBACK;

    public static boolean RECORDING;

    public static Map initialStates;

    public static Map finalStates;

    public static void end() {
        Iterator iter = initialStates.keySet().iterator();
        while (iter.hasNext())
            ((IFigure) iter.next()).revalidate();
        initialStates = null;
        finalStates = null;
        PLAYBACK = false;
        trackMe = null;
        showMe = null;
        viewport = null;
    }

    public static void mark(IFigure figure) {
        trackMe = figure;
        trackLocation = trackMe.getBounds().getLocation();
        while (!(figure instanceof Viewport))
            figure = figure.getParent();
        viewport = (Viewport) figure;

        initialStates = new HashMap();
        finalStates = new HashMap();
        start = System.currentTimeMillis();
        finish = start + DURATION;
        current = start + 20;
    }

    public static void captureLayout(IFigure root) {
        RECORDING = true;
        while (root.getParent() != null)
            root = root.getParent();

        root.validate();
        Iterator iter = initialStates.keySet().iterator();
        while (iter.hasNext())
            recordFinalStates((IFigure) iter.next());
        RECORDING = false;
        PLAYBACK = true;
    }

    public static boolean playbackState(IFigure container) {
        if (!PLAYBACK)
            return false;
        List initial = (List) initialStates.get(container);
        if (initial == null) {
            System.out.println("Error playing back state");
            return false;
        }
        List target = (List) finalStates.get(container);
        List children = container.getChildren();
        Rectangle rect1, rect2;
        for (int i = 0; i < children.size(); i++) {
            IFigure child = (IFigure) children.get(i);
            rect1 = (Rectangle) initial.get(i);
            rect2 = (Rectangle) target.get(i);
            child.setBounds(new Rectangle((int) Math.round(progress * rect2.x + (1 - progress) * rect1.x), (int) Math
                    .round(progress * rect2.y + (1 - progress) * rect1.y), (int) Math.round(progress * rect2.width
                    + (1 - progress) * rect1.width), (int) Math.round(progress * rect2.height + (1 - progress) * rect1.height)));
            // child.invalidate();
        }
        return true;
    }

    public static void recordFinalStates(IFigure container) {
        List list = new ArrayList();
        finalStates.put(container, list);
        List children = container.getChildren();
        list.clear();
        for (int i = 0; i < children.size(); i++)
            list.add(((IFigure) children.get(i)).getBounds().getCopy());
    }

    public static void recordInitialState(IFigure container) {
        if (!RECORDING)
            return;
        List list = (List) initialStates.get(container);
        if (list != null)
            return;
        initialStates.put(container, list = new ArrayList());
        List children = container.getChildren();
        list.clear();
        for (int i = 0; i < children.size(); i++)
            list.add(((IFigure) children.get(i)).getBounds().getCopy());
    }

    public static void swap() {
        Map temp = finalStates;
        finalStates = initialStates;
        initialStates = temp;
    }

    public static boolean step() {
        current = System.currentTimeMillis() + 30;
        progress = (double) (current - start) / (finish - start);
        progress = Math.min(progress, 0.999);
        Iterator iter = initialStates.keySet().iterator();

        while (iter.hasNext())
            ((IFigure) iter.next()).revalidate();
        viewport.validate();

        Point loc = viewport.getViewLocation();
        loc.translate(trackMe.getBounds().getLocation().getDifference(trackLocation));
        viewport.setViewLocation(loc);
        trackLocation = trackMe.getBounds().getLocation();

        return current < finish;
    }

}
