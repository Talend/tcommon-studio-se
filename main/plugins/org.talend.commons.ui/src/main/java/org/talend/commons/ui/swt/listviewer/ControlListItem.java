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
package org.talend.commons.ui.swt.listviewer;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.i18n.Messages;

/**
 * Based on {@link org.eclipse.equinox.internal.p2.ui.discovery.util.ControlListItem} <br/>
 * If you can't find it, please refer to {@link org.eclipse.ui.internal.progress.ProgressInfoItem}.
 * 
 */
@SuppressWarnings("restriction")
public abstract class ControlListItem<T> extends Composite {

    static String DARK_COLOR_KEY = Messages.PLUGIN_ID + "." + ControlListItem.class.getSimpleName() + ".DARK_COLOR"; //$NON-NLS-1$ //$NON-NLS-2$

    interface IndexListener {

        /**
         * Select the item previous to the receiver.
         */
        public void selectPrevious();

        /**
         * Select the next previous to the receiver.
         */
        public void selectNext();

        /**
         * Select the receiver.
         */
        public void select();

        public void open();

    }

    IndexListener indexListener;

    private int currentIndex;

    private boolean selected;

    private final MouseAdapter mouseListener;

    private boolean isShowing = true;

    private final MouseTrackAdapter mouseTrackListener;

    private boolean hot;

    static {
        // Mac has different Gamma value
        int shift = "carbon".equals(SWT.getPlatform()) ? -25 : -10;//$NON-NLS-1$

        Color lightColor = PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND);

        // Determine a dark color by shifting the list color
        RGB darkRGB = new RGB(Math.max(0, lightColor.getRed() + shift), Math.max(0, lightColor.getGreen() + shift),
                Math.max(0, lightColor.getBlue() + shift));
        JFaceResources.getColorRegistry().put(DARK_COLOR_KEY, darkRGB);
    }

    /**
     * Create a new instance of the receiver with the specified parent, style and info object/
     * 
     * @param parent
     * @param style
     * @param element
     */
    public ControlListItem(Composite parent, int style, T element) {
        super(parent, style | SWT.NO_FOCUS);
        Assert.isNotNull(element);
        super.setData(element);
        setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
        mouseListener = doCreateMouseListener();
        mouseTrackListener = doCreateMouseTrackListener();
        registerChild(this);
        // Control[] children = getChildren();
        // for (Control child : children) {
        // registerChild(child);
        // }
        setHot(false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getData() {
        return (T) super.getData();
    }

    @Override
    public void setData(Object data) {
        throw new IllegalArgumentException();
    }

    private MouseTrackAdapter doCreateMouseTrackListener() {
        return new MouseTrackAdapter() {

            private int enterCount;

            @Override
            public void mouseEnter(MouseEvent e) {
                enterCount++;
                updateHotState();
            }

            @Override
            public void mouseExit(MouseEvent e) {
                enterCount--;
                getDisplay().asyncExec(new Runnable() {

                    @Override
                    public void run() {
                        if (!isDisposed()) {
                            updateHotState();
                        }
                    }
                });
            }

            private void updateHotState() {
                if (enterCount == 0) {
                    if (isHot()) {
                        setHot(false);
                    }
                } else {
                    if (!isHot()) {
                        setHot(true);
                    }
                }
            }
        };
    }

    private MouseAdapter doCreateMouseListener() {
        return new MouseAdapter() {

            @Override
            public void mouseDown(MouseEvent e) {
                if (indexListener != null) {
                    if (e.count == 2) {
                        indexListener.open();
                    } else {
                        indexListener.select();
                    }
                }
            }
        };
    }

    public boolean isHot() {
        return hot;
    }

    public void setHot(boolean hot) {
        this.hot = hot;
    }

    protected void registerChild(Control child) {
        child.addMouseListener(mouseListener);
        child.addMouseTrackListener(mouseTrackListener);

    }

    /**
     * Refresh the contents of the receiver.
     */
    protected abstract void refresh();

    /**
     * Set the color base on the index
     * 
     * @param index
     */
    public void updateColors(int index) {
        currentIndex = index;

        if (selected) {
            setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION));
            setForeground(getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT));
        } else {
            if (index % 2 == 0) {
                setBackground(JFaceResources.getColorRegistry().get(DARK_COLOR_KEY));
            } else {
                setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
            }
            setForeground(getDisplay().getSystemColor(SWT.COLOR_LIST_FOREGROUND));
        }
    }

    @Override
    public void setForeground(Color color) {
        super.setForeground(color);
        Control[] children = getChildren();
        for (Control child : children) {
            child.setForeground(color);
        }
    }

    @Override
    public void setBackground(Color color) {
        super.setBackground(color);
        Control[] children = getChildren();
        for (Control child : children) {
            child.setBackground(color);
        }
    }

    /**
     * Set the selection colors.
     * 
     * @param select boolean that indicates whether or not to show selection.
     */
    public void setSelected(boolean select) {
        selected = select;
        updateColors(currentIndex);
    }

    /**
     * Set the listener for index changes.
     * 
     * @param indexListener
     */
    void setIndexListener(IndexListener indexListener) {
        this.indexListener = indexListener;
    }

    /**
     * Return whether or not the receiver is selected.
     * 
     * @return boolean
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Set whether or not the receiver is being displayed based on the top and bottom of the currently visible area.
     * 
     * @param top
     * @param bottom
     */
    void setDisplayed(int top, int bottom) {
        int itemTop = getLocation().y;
        int itemBottom = itemTop + getBounds().height;
        setDisplayed(itemTop <= bottom && itemBottom > top);

    }

    /**
     * Set whether or not the receiver is being displayed
     * 
     * @param displayed
     */
    private void setDisplayed(boolean displayed) {
        // See if this element has been turned off
        boolean refresh = !isShowing && displayed;
        isShowing = displayed;
        if (refresh) {
            refresh();
        }
    }

}
