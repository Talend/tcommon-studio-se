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
package org.talend.metadata.managment.ui.model.positional;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.model.utils.RepositoryManagerHelper;

/**
 * DOC cantoine. GraphicRule : This Class represent the Graphic rule who represent the Position of the PositionalText in
 * the Wizard Metadata FilePositional
 * 
 * $Id: GraphicRule.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */

public class GraphicRule extends ScrolledComposite {

    private boolean isEnabled = true;

    private static GraphicRule composite;

    protected static int fontWidth;

    protected static int decalTextScreen;

    private FilePositionalViewer positionalText;

    protected static List<PositionMarkerEditor> listPositionMarker;

    protected static List<HorizontalMarkerEditor> listHorizontalMarker;

    private int width = 60000;// Must a number smaller than 65535 2^16.

    public GraphicRule(Composite parent, int style) {
        super(parent, style);

        listPositionMarker = Collections.synchronizedList(new ArrayList<PositionMarkerEditor>());

        listHorizontalMarker = Collections.synchronizedList(new ArrayList<HorizontalMarkerEditor>());

        composite = this;

        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.widthHint = 500;
        gridData.heightHint = 92;
        composite.setLayoutData(gridData);
        final Color blanc = new Color(parent.getDisplay(), 255, 255, 255);
        composite.setBackground(blanc);
        blanc.dispose();
        addPaintListener(new PaintListener() {

            public void paintControl(PaintEvent e) {
                if (isEnabled) {
                    GC gc = e.gc;

                    gc.drawLine(decalTextScreen, 25, width + decalTextScreen, 25);

                    if (fontWidth <= 0) {
                        int tmw = gc.getFontMetrics().getAverageCharWidth();
                        fontWidth = tmw == 0 ? 10 : tmw;
                    }

                    int unite = 0;
                    int uniteRAZ = 0;

                    for (int i = 0; i < width; i++) {

                        if (i % fontWidth == 0) {
                            if (uniteRAZ == 5) {
                                String num = "" + unite; //$NON-NLS-1$
                                if (num.length() < 2) {
                                    gc.drawText(num, i - 2 + decalTextScreen, 0);

                                } else if (num.length() < 3) {
                                    gc.drawText(num, i - 5 + decalTextScreen, 0);
                                } else if (num.length() < 4) {
                                    gc.drawText(num, i - 8 + decalTextScreen, 0);
                                } else {
                                    gc.drawText(num, i - 11 + decalTextScreen, 0);
                                }
                                gc.drawLine(i + decalTextScreen, 15, i + decalTextScreen, 35);
                                uniteRAZ = 0;
                            } else {
                                gc.drawLine(i + decalTextScreen, 23, i + decalTextScreen, 27);
                            }
                            uniteRAZ += 1;
                            unite += 1;
                        }
                    }
                    gc.dispose();
                }
            }
        });

        /**
         * mouseMoveListener. : This Listener choose the cursor when the pointer is on the Top of Graphic Rule
         * 
         */
        MouseMoveListener mouseMoveListener = new MouseMoveListener() {

            public void mouseMove(MouseEvent e) {
                if (!isEnabled) {
                    setNoCursor();
                } else if (isEnabled && (e.y < 26)) {
                    setHandCursor();
                } else {
                    setDefaultCursor();
                }
            }

        };
        this.addMouseMoveListener(mouseMoveListener);

        /**
         * mouseMoveListener. : This Listener create a Position delimiter when you click(down) in the appropriate area
         * 
         */
        Listener graphicRuleEventListener = new Listener() {

            public void handleEvent(Event event) {

                if (isEnabled && (event.y < 26)) {

                    switch (event.type) {

                    case SWT.MouseDown:

                        int posXpix = positionalText.calculatePositionByPixel(event.x);
                        // for zh/ja/kr character.
                        String text = positionalText.getText().getText();
                        posXpix = getPosition(posXpix, text);
                        int posX = positionalText.adjustPositionWithPixel(posXpix);
                        positionalText.positionBarre.put(posX, FilePositionalViewer.calculatePositionByPixel(posX));
                        positionalText.setSeparatorValue(positionalText.calculateRegExp(), false);
                        FilePositionalViewer.synchronise();
                        break;
                    }
                }
            }
        };
        this.addListener(SWT.MouseDown, graphicRuleEventListener);

    }

    private boolean containSpecialCharacter(String string) {
        if (string != null && !string.trim().equals("")) { //$NON-NLS-1$
            if (string.matches("[\\u3041-\\u30FE]") || string.matches("[\\u4e00-\\u9fa5]") || string.matches("[\\u9fa5-\\uFFFF]")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                return true;
            }
        }
        return false;
    }

    private int getPosition(int position, String text) {
        if (position == 0 || text == null) {
            return position;
        }
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(text.getBytes())));
            List<String> list = new ArrayList<String>();
            int maximumRowsToPreview = RepositoryManagerHelper.getMaximumRowsToPreview();
            int nbLine = 0;
            String str = null;
            while ((str = br.readLine()) != null && nbLine <= maximumRowsToPreview) {
                nbLine++;
                list.add(str);
            }
            for (String string : list) {
                if (!string.trim().equals("")) { //$NON-NLS-1$
                    String[] strArray = string.split(""); //$NON-NLS-1$
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < strArray.length; i++) {
                        String s = strArray[i];
                        if (containSpecialCharacter(s)) {
                            s = " " + s; //$NON-NLS-1$
                        }
                        sb.append(s);
                    }
                    if (sb.length() > position) {
                        String charStr = String.valueOf(sb.toString().charAt(position));
                        if (containSpecialCharacter(charStr)) {
                            position++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return position;
    }

    public void setPositionalViewer(FilePositionalViewer positionalText) {
        this.positionalText = positionalText;
        this.fontWidth = positionalText.largeurFont;
        this.decalTextScreen = positionalText.decalScreen;
    }

    private void setDefaultCursor() {
        Cursor cursor = new Cursor(this.getDisplay(), SWT.CURSOR_ARROW);
        this.setCursor(cursor);
    }

    private void setHandCursor() {
        Cursor cursor = new Cursor(this.getDisplay(), SWT.CURSOR_HAND);
        this.setCursor(cursor);
    }

    private void setNoCursor() {
        Cursor cursor = new Cursor(this.getDisplay(), SWT.CURSOR_NO);
        this.setCursor(cursor);
    }

    /**
     * PositionMarkerEditor. : This inner class represents the Number in the Graphic Rule who's pointed the position of
     * a Positional delimiter.
     * 
     */
    public static class PositionMarkerEditor extends Canvas {

        int posX;

        public PositionMarkerEditor(Composite parent, int style) {
            super(parent, style);

            final Color blanc = new Color(parent.getDisplay(), 255, 255, 255);
            this.setBackground(blanc);
            blanc.dispose();
            addPaintListener(new PaintListener() {

                public void paintControl(PaintEvent e) {
                    GC gc = e.gc;
                    int posX = Math.round(((Canvas) e.widget).getLocation().x / fontWidth) + 1;
                    // int posX = ((((Canvas) e.widget).getLocation().x) + decalTextScreen) / fontWidth;
                    String position = "" + posX; //$NON-NLS-1$

                    if (position.length() < 2) {
                        gc.drawText(position, 3, 0);
                    } else if (position.length() < 3) {
                        gc.drawText(position, 0, 0);
                    } else if (position.length() < 4) {
                        posX++;
                        position = "" + posX; //$NON-NLS-1$
                        gc.drawText(position, 0, 0);
                    } else {
                        gc.drawText("...", 0, 0); //$NON-NLS-1$
                    }
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
     * drawGraphicRule. This method draw the position at the Position setting in parameter.
     * 
     * @param int posX : Position to draw the Position Number of the Positional delimiter
     */
    public static void drawGraphicRule(int posX) {

        if (((posX + decalTextScreen) / fontWidth) % 5 != 0) {
            PositionMarkerEditor pm = new PositionMarkerEditor(composite, SWT.NONE);
            if (((posX + decalTextScreen) / fontWidth) > 99) {
                pm.setBounds(posX - 8, 10, 18, 12);
            } else {
                pm.setBounds(posX - 5, 10, 12, 12);
            }
            listPositionMarker.add(pm);
            pm.setPosX(posX);
        }
    }

    /**
     * erasePositionMarker. This method draw the position at the Position setting in parameter.
     * 
     * @param PositionMarkerEditor pm : the Object PositionMarker who will be delete.
     * @param int posX : Initial position of Object (Axis of abscissas).
     * @param int erase : End position of Object (Axis of abscissas).
     */
    protected static void erasePositionMarker(PositionMarkerEditor pm, int posX, int erase) {
        pm.setBounds(erase, 0, 0, 0);
        pm.setPosX(erase);
    }

    /**
     * HorizontalMarkerEditor. : This inner class is a graphic Object who's representing an Interval in the Graphic
     * Rule.
     * 
     */
    public static class HorizontalMarkerEditor extends Canvas {

        int posX;

        public HorizontalMarkerEditor(Composite parent, int style, final int posIntInitial, final int posInt,
                final String interval) {
            super(parent, style);

            final Color blanc = new Color(parent.getDisplay(), 255, 255, 255);
            this.setBackground(blanc);
            blanc.dispose();
            addPaintListener(new PaintListener() {

                public void paintControl(PaintEvent e) {
                    GC gc = e.gc;

                    Color bleu = new Color(((Canvas) e.widget).getDisplay(), new RGB(0, 180, 255));
                    gc.setForeground(bleu);
                    bleu.dispose();

                    gc.drawText("<", posIntInitial - posIntInitial, 4); //$NON-NLS-1$

                    gc.drawText(">", posInt - 7 - posIntInitial, 4); //$NON-NLS-1$

                    gc.drawText(interval, (posInt / 2) - (posIntInitial / 2), 0);

                    gc.drawLine(posIntInitial - posIntInitial, 11, posInt - posIntInitial, 11);

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
     * drawHorizontalRule. This method draw the interval between two Positions delimiter.
     * 
     * @param String fieldValue : the fieldSeparator value
     * @param String intervalValue : the fieldPosition value
     */
    public static void drawHorizontalRule(String fieldValue, String intervalValue) {

        String[] valueSeparator = fieldValue.split(","); //$NON-NLS-1$
        String[] intervalSeparator = intervalValue.split(","); //$NON-NLS-1$

        int oldInit = 0;
        if (listHorizontalMarker != null && !listHorizontalMarker.isEmpty()) {
            Iterator<HorizontalMarkerEditor> iterate = listHorizontalMarker.iterator();
            while (iterate.hasNext()) {
                HorizontalMarkerEditor hmTemp = (HorizontalMarkerEditor) iterate.next();
                hmTemp.dispose();
            }
        }
        listHorizontalMarker.clear();

        if (fieldValue != null && !fieldValue.equals("") && intervalValue != null && !intervalValue.equals("")) { //$NON-NLS-1$ //$NON-NLS-2$
            for (int i = 0; i < valueSeparator.length; i++) {

                int position = new Integer(valueSeparator[i]).intValue();
                HorizontalMarkerEditor hm = new HorizontalMarkerEditor(composite, SWT.NONE, oldInit, (position * fontWidth)
                        + decalTextScreen, intervalSeparator[i]);
                hm.setBounds(oldInit, 27, ((position * fontWidth) + decalTextScreen) - oldInit, 16);

                hm.setPosX((position * fontWidth) + decalTextScreen);
                oldInit = (position * fontWidth) + decalTextScreen;

                listHorizontalMarker.add(hm);
            }
        }
    }

    /**
     * Getter for isEnabled.
     * 
     * @return the isEnabled
     */
    public boolean isEnabled() {
        return this.isEnabled;
    }

    /**
     * Sets the isEnabled.
     * 
     * @param isEnabled the isEnabled to set
     */
    public void setEnabled(boolean isEnabled) {
        super.setEnabled(isEnabled);
        this.isEnabled = isEnabled;
    }

}
