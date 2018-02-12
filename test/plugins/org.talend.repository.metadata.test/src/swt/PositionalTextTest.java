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
package swt;

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
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.talend.metadata.managment.ui.model.positional.ResizeHelper;
import org.talend.repository.metadata.i18n.Messages;

/**
 * PositionalText
 * 
 * $Id: PositionalTextTest.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */

public class PositionalTextTest extends Composite {

    private static int largeurFont = 9;

    private static int decalScreen = 3;

    private Text text;

    private static List<VerticalMarkerEditor> listVerticalMarker;

    private static List<OvalMarkerEditor> listOvalMarkerEditor;

    private static PositionalTextTest composite;

    private String fieldSeparatorValue;

    private static TreeMap<Integer, Integer> positionBarre;

    private boolean erase = false;

    public PositionalTextTest(Composite parent, int style) {
        super(parent, style);

        composite = this;

        text = new Text(this, SWT.MULTI | SWT.V_SCROLL | SWT.NONE);
        text.setEditable(false);

        listVerticalMarker = Collections.synchronizedList(new ArrayList<VerticalMarkerEditor>());
        listOvalMarkerEditor = Collections.synchronizedList(new ArrayList<OvalMarkerEditor>());

        positionBarre = new TreeMap<Integer, Integer>();

        addPaintListener(new PaintListener() {

            public void paintControl(PaintEvent e) {
                Rectangle bounds = ((Composite) e.widget).getBounds();
                text.setBounds(bounds);
                for (VerticalMarkerEditor marker : listVerticalMarker) {
                    marker.setBounds(marker.getPosX(), 0, 1, bounds.height);
                }
            }
        });

        MouseAdapter mouseAdapter = new MouseAdapter() {

            public void mouseUp(MouseEvent e) {
                int posXpix = (e.x - decalScreen) / largeurFont; // ((Text) e.widget).getCaretLocation().x;
                int posX = (posXpix * largeurFont) + decalScreen;

                // eraseOrDrawVerticalMarker(posX);
                if (!erase) {
                    drawVerticalMarker(posX);
                }
                composite.layout();
            }
        };
        text.addMouseListener(mouseAdapter);
    }

    public String getFieldSeparatorValue() {
        return fieldSeparatorValue;
    }

    public Point convertToParentOrigin(Composite child, Point point) {
        Rectangle bounds = child.getBounds();
        return new Point(bounds.x + point.x, bounds.y + point.y);
    }

    protected void setText(String s) {
        // Font font = new Font(this.getDisplay().getCurrent(), "Courier", 12, SWT.NORMAL);
        // this.text.setFont(font);
        this.text.setText(s);
    }

    protected void setFieldSeparatorValue(String s) {

        if (s != null && !s.equals("")) { //$NON-NLS-1$
            fieldSeparatorValue = s;
            String[] drawLine = s.split(","); //$NON-NLS-1$
            int oldToPrint = 0;
            for (int i = 0; i < drawLine.length; i++) {
                // int line = (new Integer(drawLine[i]).intValue())+old;
                // createFilSeparatorValue((line*largeurFont)+decalScreen);
                // old += line;
                int lineToPrint = (new Integer(drawLine[i]).intValue()) + oldToPrint;
                // drawVerticalMarkerInit((lineToPrint*largeurFont)+decalScreen);
                drawVerticalMarker((lineToPrint * largeurFont) + decalScreen);
                oldToPrint = lineToPrint;
            }
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
            for (OvalMarkerEditor oval : listOvalMarkerEditor) {
                if (posX == oval.getPosX()) {
                    eraseOvalMarker(oval, posX, -10);
                }
            }
        }
    }

    protected void drawVerticalMarker(int posX) {

        drawOralMarker(posX);
        VerticalMarkerEditor vm = new VerticalMarkerEditor(this, SWT.NONE);
        vm.setBounds(posX, 0, 1, getParent().getBounds().height);
        vm.setPosX(posX);
        vm.moveAbove(text);
        listVerticalMarker.add(vm);

        // PTODO trouver la m锟絫hode pour d锟絫erminer 9 par rapport 锟�la FONT.
        // Position position = new Position(posX,(posX-3)/9);
        // Vu de ce c锟絫锟�mais sans grande r锟絧onse : FontMetrics fm = gc.getFontMetrics();
        positionBarre.put(posX, (posX - decalScreen) / largeurFont);

        fieldSeparatorValue = calculateRegExp(positionBarre);
        System.out.println(Messages.getString("PositionalTextTest.println.fieldValue") + fieldSeparatorValue); //$NON-NLS-1$
    }

    protected void drawOralMarker(int posX) {
        OvalMarkerEditor om = new OvalMarkerEditor(this, SWT.NONE);
        om.setBounds(posX - 5, 0, 10, 10);
        om.setPosX(posX);
        om.moveAbove(text);
        listOvalMarkerEditor.add(om);
    }

    protected void eraseVerticalMarker(VerticalMarkerEditor vm, int posX, int erase) {
        vm.setBounds(erase, 0, 1, getParent().getBounds().height);
        vm.setPosX(erase);
        vm.moveAbove(text);

        positionBarre.remove(posX);
        fieldSeparatorValue = calculateRegExp(positionBarre);
        System.out.println(Messages.getString("PositionalTextTest.println.fieldValue") + fieldSeparatorValue); //$NON-NLS-1$
    }

    protected void eraseOvalMarker(OvalMarkerEditor om, int posX, int erase) {
        om.setBounds(erase, 0, 1, getParent().getBounds().height);
        om.setPosX(erase);
        om.moveAbove(text);
    }

    protected boolean translateVerticalMarker(VerticalMarkerEditor vm, int initialPosX, int futurPosX) {

        boolean test = true;

        int posXpix = (futurPosX - decalScreen) / largeurFont;
        int posX = (posXpix * largeurFont) + decalScreen;

        int initialXpix = (initialPosX - decalScreen) / largeurFont;

        for (VerticalMarkerEditor marker : listVerticalMarker) {
            if (vm != marker) {
                if (posX == marker.getPosX()) {
                    test = false;
                    eraseVerticalMarker(marker, posX, -10);
                }
            }
        }

        positionBarre.remove(initialXpix);

        vm.setBounds(posX, 0, 1, getParent().getBounds().height);
        vm.setPosX(posX);
        vm.moveAbove(text);

        if (posXpix >= 0) {
            positionBarre.put(posX, posXpix);
        }
        fieldSeparatorValue = calculateRegExp(positionBarre);
        System.out.println(Messages.getString("PositionalTextTest.println.fieldValue") + fieldSeparatorValue); //$NON-NLS-1$

        return test;

    }

    protected void translateOvalMarker(OvalMarkerEditor om, int initialPosX, int futurPosX) {

        int posXpix = (futurPosX - decalScreen) / largeurFont;
        int posX = (posXpix * largeurFont) + decalScreen;

        for (OvalMarkerEditor oval : listOvalMarkerEditor) {
            if (om != oval) {
                if (posX == oval.getPosX()) {
                    eraseOvalMarker(oval, posX, -10);

                }
            }
        }

        om.setBounds(posX - 5, 0, 10, 10);
        om.setPosX(posX);
        om.moveAbove(text);
    }

    protected String calculateRegExp(TreeMap<Integer, Integer> positionBarre) {

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

    // protected String calculateRegExp(TreeMap<Integer, Integer> positionBarre, String fieldSeparatorValue){
    //
    // String regExp="";
    // if(fieldSeparatorValue != null && !fieldSeparatorValue.equals("")){
    // regExp = fieldSeparatorValue;
    // }
    //
    // if(positionBarre != null && !positionBarre.isEmpty()){
    // Set cles = positionBarre.keySet();
    // Iterator iterator = cles.iterator();
    // Integer toAdd = 0;
    // Integer toSubstract = 0;
    // while (iterator.hasNext()) {
    // Integer i = (Integer)iterator.next();
    // toAdd = positionBarre.get(i)-toSubstract;
    // if(regExp.equals("")){
    // regExp += toAdd;
    // }else{
    // regExp += ","+toAdd;
    // }
    // toSubstract = positionBarre.get(i);
    // }
    // }
    // return regExp;
    // }

    /**
     * VerticalMarker.
     * 
     * 
     */
    public static class VerticalMarkerEditor extends Canvas {

        int posX;

        private final ResizeHelper resizeHelper = new ResizeHelper();

        public VerticalMarkerEditor(Composite parent, int style) {
            super(parent, style);

            addPaintListener(new PaintListener() {

                public void paintControl(PaintEvent e) {
                    GC gc = e.gc;
                    // STYLE HAUT GRIS, RESTE NOIR
                    // gc.drawLine(0, 0, 0, 15);
                    // gc.drawLine(1, 0, 1, 15);
                    // gc.drawLine(2, 0, 2, 15);
                    // gc.fillRectangle(((Canvas) e.widget).getBounds());

                    // STYLE HAUT NOIR, RESTE GRIS
                    // gc.drawLine(0, 15, 0, ((Canvas) e.widget).getBounds().height);
                    // gc.drawLine(1, 15, 1, ((Canvas) e.widget).getBounds().height);
                    // gc.drawLine(2, 15, 2, ((Canvas) e.widget).getBounds().height);
                    // gc.fillRectangle(0,0,0,10);//((Canvas) e.widget).getBounds()

                    // STYLE TOUT NOIR
                    gc.drawLine(0, 0, 0, ((Canvas) e.widget).getBounds().height);
                    gc.dispose();
                }
            });

            MouseMoveListener mouseMoveListener = new MouseMoveListener() {

                public void mouseMove(MouseEvent e) {
                    setHandCursor();
                }

            };

            Listener verticalLineDragAndDrop = new Listener() {

                public void handleEvent(Event event) {

                    Point eventPoint = new Point(event.x, event.y);

                    switch (event.type) {
                    case SWT.MouseMove:
                        if (resizeHelper.isDragging()) {
                            Point newPoint = new Point(posX + eventPoint.x, eventPoint.y);
                            for (OvalMarkerEditor oval : listOvalMarkerEditor) {
                                if (posX == oval.getPosX()) {
                                    composite.translateOvalMarker(oval, (oval.posX * largeurFont) + decalScreen, newPoint.x);
                                }
                            }

                            for (VerticalMarkerEditor marker : listVerticalMarker) {
                                if (posX == marker.getPosX()) {
                                    if (!composite.translateVerticalMarker(marker, (marker.posX * largeurFont) + decalScreen,
                                            newPoint.x)) {

                                        event.type = SWT.MouseUp;
                                        break;
                                    }
                                }
                            }
                            resizeHelper.setLastDragPoint(newPoint);
                        }
                        break;
                    case SWT.MouseDown:
                        // composite.eraseOrDrawVerticalMarker(posX);
                        // if (!erase){
                        resizeHelper.startDrag(convertToParentOrigin(composite.text, new Point(event.x, event.y)));
                        // }
                        break;
                    case SWT.MouseUp:
                        resizeHelper.stopDrag();
                        break;
                    case SWT.MouseExit:
                        setDefaultCursor();
                        break;
                    }
                }
            };

            this.addListener(SWT.MouseMove, verticalLineDragAndDrop);
            this.addListener(SWT.MouseDown, verticalLineDragAndDrop);
            this.addListener(SWT.MouseUp, verticalLineDragAndDrop);

            this.addMouseMoveListener(mouseMoveListener);
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

        private void setDefaultCursor() {
            Cursor cursor = new Cursor(this.getDisplay(), 0);
            this.setCursor(cursor);
        }

        private void setHandCursor() {
            Cursor cursor = new Cursor(this.getDisplay(), SWT.CURSOR_HAND);
            this.setCursor(cursor);
        }

        public Point convertToParentOrigin(Text child, Point point) {
            Rectangle bounds = child.getBounds();
            return new Point(bounds.x + point.x, bounds.y + point.y);
        }

    }

    /**
     * OvalMarker.
     * 
     * 
     */
    public static class OvalMarkerEditor extends Canvas {

        int posX;

        private final ResizeHelper resizeHelper = new ResizeHelper();

        public OvalMarkerEditor(Composite parent, int style) {
            super(parent, style);

            addPaintListener(new PaintListener() {

                public void paintControl(PaintEvent e) {
                    GC gc = e.gc;
                    // gc.drawOval(1, 0, 7, 7);
                    // gc.drawLine(2, 0, 6, 13);
                    // gc.drawLine(8, 0, 4, 13);
                    // gc.drawLine(2, 0, 9, 0);

                    // AFFICHE LA POSITION AU DESSUS DU TRIANGLE
                    // int posX = ((((Canvas) e.widget).getLocation().x)+decalScreen)/largeurFont;
                    // String position = ""+posX;
                    // if(position.length()<2){
                    // gc.drawText(position, decalScreen, 0);
                    // } else if(position.length()<3){
                    // gc.drawText(position, 0, 0);
                    // }else{
                    // gc.drawText("...", 0, 0);
                    // }
                    gc.setLineWidth(1);
                    gc.setLineStyle(SWT.LINE_SOLID);
                    gc.setBackground(((Canvas) e.widget).getDisplay().getSystemColor(SWT.COLOR_BLACK));

                    // Triangle Tete en haut
                    gc.fillPolygon(new int[] { 5, 0, 10, 10, 0, 10 });
                    // Triangle Tete en bas
                    // gc.fillPolygon(new int[] { 0,0,10,0,5,10 });
                    gc.dispose();
                }
            });

            Listener triangleDragAndDrop = new Listener() {

                public void handleEvent(Event event) {

                    Point eventPoint = new Point(event.x, event.y);

                    switch (event.type) {
                    case SWT.MouseMove:
                        if (resizeHelper.isDragging()) {
                            Point newPoint = new Point(posX + eventPoint.x, eventPoint.y); // convertToParentOrigin(composite,
                            // )
                            for (VerticalMarkerEditor marker : listVerticalMarker) {
                                if (posX == marker.getPosX()) {
                                    if (!composite.translateVerticalMarker(marker, (marker.posX * largeurFont) + decalScreen,
                                            newPoint.x)) {

                                        event.type = SWT.MouseUp;
                                        break;
                                    }
                                    // OvalMarkerEditor vmTemp = (OvalMarkerEditor)(((Canvas) event.widget));
                                }
                            }
                            for (OvalMarkerEditor oval : listOvalMarkerEditor) {
                                if (posX == oval.getPosX()) {
                                    composite.translateOvalMarker(oval, (oval.posX * largeurFont) + decalScreen, newPoint.x);
                                }
                            }
                            resizeHelper.setLastDragPoint(newPoint);
                        }
                        break;
                    case SWT.MouseDown:
                        resizeHelper.startDrag(convertToParentOrigin(composite.text, new Point(event.x, event.y)));
                        break;
                    case SWT.MouseUp:
                        resizeHelper.stopDrag();
                        break;
                    case SWT.MouseExit:
                        setDefaultCursor();
                        break;
                    }
                }
            };

            MouseMoveListener mouseMoveListener = new MouseMoveListener() {

                public void mouseMove(MouseEvent e) {
                    setHandCursor();
                }

            };

            this.addListener(SWT.MouseMove, triangleDragAndDrop);
            this.addListener(SWT.MouseDown, triangleDragAndDrop);
            this.addListener(SWT.MouseUp, triangleDragAndDrop);

            this.addMouseMoveListener(mouseMoveListener);

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

        public Point convertToParentOrigin(Text child, Point point) {
            Rectangle bounds = child.getBounds();
            return new Point(bounds.x + point.x, bounds.y + point.y);
        }
    }
}
