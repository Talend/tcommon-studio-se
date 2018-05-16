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
package org.talend.metadata.managment.ui.model.positional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.metadata.managment.ui.model.positional.GraphicRule.HorizontalMarkerEditor;
import org.talend.metadata.managment.ui.model.positional.GraphicRule.PositionMarkerEditor;

/**
 * DOC cantoine. FilePositionalViewer : This Class represent the Composite who shows the Text and the Position of
 * Delimiter
 * 
 * $Id: FilePositionalViewer.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */

public class FilePositionalViewer extends Composite {

    protected boolean isEnabled = true;

    protected static int largeurFont;

    protected static int decalScreen;

    private final Text text;

    private static List<VerticalMarkerEditor> listVerticalMarker;

    private static List<TopMarkerEditor> listTopMarkerEditor;

    private static FilePositionalViewer composite;

    protected static String fieldSeparatorValue;

    protected String fieldPositionText;

    public static TreeMap<Integer, Integer> positionBarre;

    private boolean erase = false;

    private static Text hiddenFieldSeparatorValueText;

    /**
     * DOC ocarbone Comment method "synchronise".
     */
    static void synchronise() {
        // refresh the value to run the listener on FileStep1
        hiddenFieldSeparatorValueText.setText(FilePositionalViewer.fieldSeparatorValue);
    }

    public FilePositionalViewer(Composite parent, int style) {
        super(parent, style);

        composite = this;

        text = new Text(this, SWT.MULTI | SWT.LEFT);
        text.setEditable(false);
        Font font = new Font(this.getDisplay(), "Courier", 12, SWT.NORMAL); //$NON-NLS-1$
        this.text.setFont(font);
        GC gc = new GC(this.text);
        FontMetrics fm = gc.getFontMetrics();
        int width = fm.getAverageCharWidth();
        this.largeurFont = width;
        this.decalScreen = text.getCaretLocation().x;

        gc.dispose();

        // tempo use to create a listener on FileStep1
        hiddenFieldSeparatorValueText = new Text(this, SWT.NONE);
        hiddenFieldSeparatorValueText.setVisible(false);

        listVerticalMarker = Collections.synchronizedList(new ArrayList<VerticalMarkerEditor>());
        listTopMarkerEditor = Collections.synchronizedList(new ArrayList<TopMarkerEditor>());

        positionBarre = new TreeMap<Integer, Integer>();

        addPaintListener(new PaintListener() {

            public void paintControl(PaintEvent e) {
                if (isEnabled) {
                    Rectangle bounds = ((Composite) e.widget).getBounds();
                    text.setBounds(bounds);
                    for (VerticalMarkerEditor marker : listVerticalMarker) {
                        marker.setBounds(marker.getPosX(), 0, 1, bounds.height);
                    }
                }
            }
        });

        /**
         * mouseAdapter. : This Listener draw the PositionDelimiter unit whith VerticalLine, PositionInformation and
         * IntervalInformation
         * 
         */
        MouseAdapter mouseAdapter = new MouseAdapter() {

            @Override
            public void mouseDown(MouseEvent e) {
                if (isEnabled) {
                    int posXpix = calculatePositionByPixel(e.x);
                    int posX = adjustPositionWithPixel(posXpix);

                    // eraseOrDrawVerticalMarker(posX);
                    if (!erase) {
                        drawVerticalMarker(posX);
                        GraphicRule.drawGraphicRule(posX);
                        GraphicRule.drawHorizontalRule(fieldPositionText, fieldSeparatorValue);
                    }
                    composite.layout();

                    synchronise();
                }
            }

        };
        text.addMouseListener(mouseAdapter);

        /**
         * mouseMoveListener. : This Listener choose the cursor when the pointer is on Text of PositionalText
         * 
         */
        MouseMoveListener mouseMoveListener = new MouseMoveListener() {

            public void mouseMove(MouseEvent e) {
                if (!isEnabled) {
                    setNoCursor();
                } else {
                    setDefaultCursor();
                }
            }

        };
        text.addMouseMoveListener(mouseMoveListener);

    }

    private void setNoCursor() {
        Cursor cursor = new Cursor(this.getDisplay(), SWT.CURSOR_NO);
        this.setCursor(cursor);
    }

    private void setDefaultCursor() {
        Cursor cursor = new Cursor(this.getDisplay(), 0);
        this.setCursor(cursor);
    }

    public void setText(String s) {
        this.text.setText(s);
        // setSeparatorValue("", true);
    }

    /**
     * DOC ocarbone Comment method "setSeparatorValue" used to synchronise the modification of FileStep1 on this
     * PositionalViewer.
     * 
     * @param s
     * @param eraseMarkersIsNedeed
     */
    public void setSeparatorValue(String s, boolean eraseMarkersIsNedeed) {
        if (eraseMarkersIsNedeed) {
            cleanAllMarkers();
        }

        if (s != null && !s.equals("") && !s.equals("*")) { //$NON-NLS-1$ //$NON-NLS-2$

            if (s.contains(",*")) { //$NON-NLS-1$
                s = s.substring(0, s.length() - 2);
            } else if (s.endsWith("*")) { //$NON-NLS-1$
                s = s.substring(0, s.length() - 1);
            }

            String[] drawLine = s.split(","); //$NON-NLS-1$
            int oldToPrint = 0;
            for (int i = 0; i < drawLine.length; i++) {
                String number = drawLine[i].trim();
                // for bug 5645
                number = TalendQuoteUtils.removeQuotes(number);
                if ("".equals(number)) { //$NON-NLS-1$
                    continue;
                }
                int num = 0;
                try {
                    num = new Integer(number).intValue();
                } catch (NumberFormatException e) {
                    continue;
                }
                if (num == 0) {
                    continue;
                }
                int lineToPrint = num + oldToPrint;
                drawVerticalMarker(adjustPositionWithPixel(lineToPrint));
                GraphicRule.drawGraphicRule(adjustPositionWithPixel(lineToPrint));

                oldToPrint = lineToPrint;
            }
            if (fieldPositionText != null && fieldSeparatorValue != null) {
                GraphicRule.drawHorizontalRule(fieldPositionText, fieldSeparatorValue);
            }
        }
    }

    /**
     * DOC ocarbone Comment method "setPositionValue" used to synchronise the modification of FileStep1 on this
     * PositionalViewer.
     * 
     * @param s
     */
    public void setPositionValue(String s, boolean eraseMarkersIsNedeed) {
        fieldPositionText = s;
        if (eraseMarkersIsNedeed) {
            cleanAllMarkers();
        }
        if (s != null & !s.equals("")) { //$NON-NLS-1$
            fieldPositionText = s;
            if (s != null & !s.equals("")) { //$NON-NLS-1$
                String[] drawLine = s.split(","); //$NON-NLS-1$
                for (int i = 0; i < drawLine.length; i++) {
                    drawVerticalMarker(adjustPositionWithPixel(new Integer(drawLine[i]).intValue()));
                    GraphicRule.drawGraphicRule(adjustPositionWithPixel(new Integer(drawLine[i]).intValue()));

                }
                if (fieldPositionText != null && fieldSeparatorValue != null) {
                    GraphicRule.drawHorizontalRule(fieldPositionText, fieldSeparatorValue);
                }
            }
        }
    }

    /**
     * DOC ocarbone Comment method "cleanAllMarkers".
     */
    public void cleanAllMarkers() {
        if (listVerticalMarker != null && !listVerticalMarker.isEmpty()) {
            for (VerticalMarkerEditor marker : listVerticalMarker) {
                marker.dispose();
            }
            listVerticalMarker.clear();
        }

        if (listTopMarkerEditor != null && !listTopMarkerEditor.isEmpty()) {
            for (TopMarkerEditor top : listTopMarkerEditor) {
                top.dispose();
            }
            listTopMarkerEditor.clear();
        }

        if (GraphicRule.listPositionMarker != null && !GraphicRule.listPositionMarker.isEmpty()) {
            for (PositionMarkerEditor position : GraphicRule.listPositionMarker) {
                position.dispose();
            }
            GraphicRule.listPositionMarker.clear();
        }

        if (GraphicRule.listHorizontalMarker != null && !GraphicRule.listHorizontalMarker.isEmpty()) {
            for (HorizontalMarkerEditor horizontal : GraphicRule.listHorizontalMarker) {
                horizontal.dispose();
            }
            GraphicRule.listHorizontalMarker.clear();
        }
        if (positionBarre != null) {
            positionBarre.clear();
        }
    }

    protected void eraseOrDrawVerticalMarker(int posX) {
        erase = false;
        if (listVerticalMarker != null && !listVerticalMarker.isEmpty()) {
            for (VerticalMarkerEditor marker : listVerticalMarker) {
                if (posX == marker.getPosX()) {
                    erase = true;
                    eraseVerticalMarker(marker, posX, -10);
                }
            }
            for (TopMarkerEditor top : listTopMarkerEditor) {
                if (posX == top.getPosX()) {
                    eraseTopMarker(top, posX, -10);
                }
            }
            for (PositionMarkerEditor position : GraphicRule.listPositionMarker) {
                GraphicRule.erasePositionMarker(position, position.posX, -10);
            }
        }
    }

    protected void drawVerticalMarker(int posX) {
        drawOralMarker(posX);
        VerticalMarkerEditor vm = new VerticalMarkerEditor(this, SWT.NONE);
        vm.setBounds(posX, 0, 1, text.getBounds().height);
        vm.setPosX(posX);
        vm.moveAbove(text);
        vm.setToolTipText("" + calculatePositionByPixel(posX)); //$NON-NLS-1$
        listVerticalMarker.add(vm);

        positionBarre.put(posX, calculatePositionByPixel(posX));

        fieldSeparatorValue = calculateRegExp();
        fieldPositionText = calculatePositionX();
    }

    protected void drawOralMarker(int posX) {
        TopMarkerEditor om = new TopMarkerEditor(this, SWT.NONE);
        om.setBounds(posX - 5, 0, 10, 10);
        om.setPosX(posX);
        om.moveAbove(text);
        om.setToolTipText("" + calculatePositionByPixel(posX)); //$NON-NLS-1$
        listTopMarkerEditor.add(om);
    }

    protected void eraseVerticalMarker(VerticalMarkerEditor vm, int posX, int erase) {
        vm.setBounds(erase, 0, 1, text.getBounds().height);
        vm.setPosX(erase);
        vm.moveAbove(text);
        vm.setToolTipText(null);
        positionBarre.remove(posX);
        fieldSeparatorValue = calculateRegExp();
        fieldPositionText = calculatePositionX();
        GraphicRule.drawHorizontalRule(fieldPositionText, fieldSeparatorValue);

    }

    protected void eraseTopMarker(TopMarkerEditor om, int posX, int erase) {
        om.setBounds(erase, 0, 1, text.getBounds().height);
        om.setPosX(erase);
        om.moveAbove(text);
        om.setToolTipText(null);
    }

    protected boolean translateVerticalMarker(VerticalMarkerEditor vm, int initialPosX, int futurPosX) {

        boolean onOtherMarker = true;

        int posXpix = calculatePositionByPixel(futurPosX);
        int posX = adjustPositionWithPixel(posXpix);

        int initialXpix = calculatePositionByPixel(initialPosX);

        for (VerticalMarkerEditor marker : listVerticalMarker) {
            marker.redraw();
            if (vm != marker) {
                if (posX == marker.getPosX()) {
                    onOtherMarker = false;
                    eraseVerticalMarker(marker, posX, -10);
                }
            }
        }
        for (GraphicRule.PositionMarkerEditor position : GraphicRule.listPositionMarker) {
            if (initialXpix == position.getPosX()) {
                GraphicRule.erasePositionMarker(position, initialXpix, -50);
            }
        }

        vm.setBounds(posX, 0, 1, text.getBounds().height);
        vm.setPosX(posX);
        vm.moveAbove(text);
        vm.setToolTipText("" + calculatePositionByPixel(posX)); //$NON-NLS-1$

        GraphicRule.drawGraphicRule(posX);

        positionBarre.remove(initialXpix);
        if (posXpix >= 0) {
            positionBarre.put(posX, posXpix);
        }
        fieldSeparatorValue = calculateRegExp();
        fieldPositionText = calculatePositionX();

        GraphicRule.drawHorizontalRule(fieldPositionText, fieldSeparatorValue);

        return onOtherMarker;
    }

    protected void translateTopMarker(TopMarkerEditor om, int initialPosX, int futurPosX) {

        int posXpix = calculatePositionByPixel(futurPosX);
        int posX = adjustPositionWithPixel(posXpix);

        for (TopMarkerEditor top : listTopMarkerEditor) {
            if (om != top) {
                if (posX == top.getPosX()) {
                    eraseTopMarker(top, posX, -10);
                }
            }
        }

        om.setBounds(posX - 5, 0, 10, 10);
        om.setPosX(posX);
        om.moveAbove(text);
        om.setToolTipText("" + calculatePositionByPixel(posX)); //$NON-NLS-1$
    }

    protected String calculateRegExp() {

        String regExp = ""; //$NON-NLS-1$
        if (positionBarre != null && !positionBarre.isEmpty()) {
            Set cles = positionBarre.keySet();
            Iterator iterator = cles.iterator();
            Integer toAdd = 0;
            Integer toSubstract = 0;
            while (iterator.hasNext()) {
                Integer i = (Integer) iterator.next();
                toAdd = positionBarre.get(i) - toSubstract;
                if (regExp.equals("")) { //$NON-NLS-1$
                    regExp += toAdd;
                } else {
                    regExp += "," + toAdd; //$NON-NLS-1$
                }
                toSubstract = positionBarre.get(i);
            }
        }
        return regExp;
    }

    public String calculatePositionX() {

        String posX = ""; //$NON-NLS-1$
        if (positionBarre != null && !positionBarre.isEmpty()) {
            Set cles = positionBarre.keySet();
            Iterator iterator = cles.iterator();
            while (iterator.hasNext()) {
                Integer i = (Integer) iterator.next();
                if (posX.equals("")) { //$NON-NLS-1$
                    posX += positionBarre.get(i);
                } else {
                    posX += "," + positionBarre.get(i); //$NON-NLS-1$
                }
            }
        }
        return posX;
    }

    protected static int calculatePositionByPixel(int positionPixel) {
        int positionText = (positionPixel - decalScreen) / largeurFont;
        return positionText;
    }

    protected static int adjustPositionWithPixel(int positionX) {
        int positionPix = (positionX * largeurFont) + decalScreen;
        return positionPix;
    }

    /**
     * VerticalMarker.
     * 
     * 
     */
    public static class VerticalMarkerEditor extends Canvas {

        int posX;

        public VerticalMarkerEditor(Composite parent, int style) {
            super(parent, style);

            addPaintListener(new PaintListener() {

                public void paintControl(PaintEvent e) {
                    GC gc = e.gc;
                    Rectangle rec = ((Canvas) e.widget).getBounds();

                    Color orange = new Color(((Canvas) e.widget).getDisplay(), new RGB(255, 150, 0));
                    gc.setForeground(orange);
                    orange.dispose();
                    gc.drawLine(0, 0, 0, rec.height);

                    gc.dispose();
                }
            });
        }

        /**
         * Getter for posX.
         * 
         * @return the posX
         */
        public int getPosX() {
            return this.posX;
        }

        /**
         * Sets the posX.
         * 
         * @param posX the posX to set
         */
        public void setPosX(int posX) {
            this.posX = posX;
        }
    }

    /**
     * topMarker.
     * 
     * 
     */
    public static class TopMarkerEditor extends Canvas {

        int posX;

        boolean stopDrag = false;

        private final ResizeHelper resizeHelper = new ResizeHelper();

        public TopMarkerEditor(Composite parent, int style) {
            super(parent, style);

            addPaintListener(new PaintListener() {

                public void paintControl(PaintEvent e) {
                    GC gc = e.gc;
                    gc.setLineWidth(1);
                    gc.setLineStyle(SWT.LINE_SOLID);
                    Color orange = new Color(((Canvas) e.widget).getDisplay(), new RGB(255, 150, 0));
                    // gc.setBackground(((Canvas) e.widget).getDisplay().getSystemColor(SWT.COLOR_BLACK));
                    gc.setBackground(orange);
                    orange.dispose();
                    // Triangle Tete en haut
                    gc.fillPolygon(new int[] { 5, 0, 10, 10, 0, 10 });
                    // Triangle Tete en bas
                    // gc.fillPolygon(new int[] { 0, 0, 10, 0, 5, 10 });
                    gc.dispose();
                }
            });

            Listener triangleDragAndDrop = new Listener() {

                public void handleEvent(Event event) {

                    Point eventPoint = new Point(event.x, event.y);
                    Point newPoint = new Point(posX + eventPoint.x, eventPoint.y);

                    // switch (event.button) {
                    //
                    // case 3:
                    //
                    // System.out.println("Click DROIT");
                    //
                    // break;
                    // case 1:
                    //
                    // System.out.println("Click GAUCHE");
                    //
                    // break;
                    // }

                    switch (event.type) {

                    case SWT.MouseMove:

                        if (!stopDrag) {

                            if (eventPoint.y <= -5) { // && eventPoint.y >= -20
                                setDeleteCursor();
                            } else {
                                setHandCursor();
                                if (resizeHelper.isDragging()) {
                                    for (VerticalMarkerEditor marker : listVerticalMarker) {
                                        if (posX == marker.getPosX()) {
                                            if (!composite.translateVerticalMarker(marker, adjustPositionWithPixel(marker.posX),
                                                    newPoint.x)) {
                                                stopDrag = true;
                                                event.type = SWT.MouseUp;
                                                break;
                                            }
                                        }
                                    }
                                    for (TopMarkerEditor top : listTopMarkerEditor) {
                                        if (posX == top.getPosX()) {
                                            composite.translateTopMarker(top, adjustPositionWithPixel(top.posX), newPoint.x);
                                        }
                                    }
                                    resizeHelper.setLastDragPoint(newPoint);
                                }
                            }

                        }

                        break;
                    case SWT.MouseDown:
                        resizeHelper.startDrag(convertToParentOrigin(composite.text, new Point(event.x, event.y)));
                        break;
                    case SWT.MouseUp:
                        if (eventPoint.y <= -5) { // && eventPoint.y >= -20
                            for (PositionMarkerEditor position : GraphicRule.listPositionMarker) {
                                if (posX == position.getPosX()) {
                                    GraphicRule.erasePositionMarker(position, position.getPosX(), -10);
                                }
                            }
                            for (VerticalMarkerEditor marker : listVerticalMarker) {
                                if (posX == marker.getPosX()) {
                                    composite.eraseVerticalMarker(marker, marker.getPosX(), -10);
                                }
                            }
                            for (TopMarkerEditor top : listTopMarkerEditor) {
                                if (posX == top.getPosX()) {
                                    composite.eraseTopMarker(top, top.getPosX(), -10);
                                }
                            }
                            setDefaultCursor();
                            resizeHelper.stopDrag();
                        } else {
                            setDefaultCursor();
                            resizeHelper.stopDrag();
                        }
                        break;
                    case SWT.MouseExit:
                        setDefaultCursor();
                        stopDrag = false;
                        break;
                    }
                    FilePositionalViewer.synchronise();
                }
            };

            this.addListener(SWT.MouseMove, triangleDragAndDrop);
            this.addListener(SWT.MouseDown, triangleDragAndDrop);
            this.addListener(SWT.MouseUp, triangleDragAndDrop);
            this.addListener(SWT.MouseExit, triangleDragAndDrop);

            // this.addListener(3, triangleDragAndDrop);
            // this.addListener(1, triangleDragAndDrop);

        }

        /**
         * Getter for posX.
         * 
         * @return the posX
         */
        public int getPosX() {
            return this.posX;
        }

        /**
         * Sets the posX.
         * 
         * @param posX the posX to set
         */
        public void setPosX(int posX) {
            this.posX = posX;
        }

        public void setNewString(String s) {
        }

        private void setDefaultCursor() {
            Cursor cursor = new Cursor(this.getDisplay(), 0);
            this.setCursor(cursor);
        }

        private void setHandCursor() {
            Cursor cursor = new Cursor(this.getDisplay(), SWT.CURSOR_HAND);
            this.setCursor(cursor);
        }

        private void setDeleteCursor() {
            Cursor cursor = new Cursor(this.getDisplay(), ImageProvider.getImageDesc(EImage.DELETE_ICON).getImageData(), 0, 0);
            this.setCursor(cursor);
        }

        public Point convertToParentOrigin(Text child, Point point) {
            Rectangle bounds = child.getBounds();
            return new Point(bounds.x + point.x, bounds.y + point.y);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Composite#layout()
     * 
     * Resize the Text before the composite.
     */
    @Override
    public void layout() {
        super.layout();
        text.pack();
    }

    /**
     * Getter for text.
     * 
     * @return the text
     */
    public Text getText() {
        return this.text;
    }

    /**
     * Getter for text.
     * 
     * @return the text
     */
    public Text getFieldSeparatorValue() {
        return this.hiddenFieldSeparatorValueText;
    }

    /**
     * DOC ocarbone Comment method "getPositionValue". return String calculatePositionX(positionBarre)
     */
    public String getSeparatorValue() {
        return calculateRegExp();
    }

    /**
     * Getter for isEnabled.
     * 
     * @return the isEnabled
     */
    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    /**
     * Sets the isEnabled.
     * 
     * @param isEnabled the isEnabled to set
     */
    @Override
    public void setEnabled(boolean isEnabled) {
        super.setEnabled(isEnabled);
        this.isEnabled = isEnabled;
    }

}
