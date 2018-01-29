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
package org.talend.presentation.onboarding.ui.shells;

import java.util.concurrent.Callable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.talend.presentation.onboarding.ui.composites.OnBoardingContentComposite;
import org.talend.presentation.onboarding.ui.managers.OnBoardingManager;
import org.talend.presentation.onboarding.ui.runtimedata.OnBoardingPresentationData;
import org.talend.presentation.onboarding.utils.OnBoardingUtils;
import org.talend.presentation.onboarding.utils.WidgetFinder;

/**
 * created by cmeng on Sep 11, 2015 Detailled comment
 *
 */
public class OnBoardingShell {

    public static final int DEFAULT_ALPHA_VALUE = 255;

    public static final Color DEFAULT_BACKGROUND_COLOR = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);

    private Shell parentShell;

    // onBoardingShell
    private Shell obShell;

    private Color finalBackgroundColor;

    private OnBoardingPresentationData presentationData;

    private Widget focusedWidget;

    private ControlListener controlListenerForParent;

    private OnBoardingContentComposite contentComposite;

    private OnBoardingManager onBoardingManager;

    private Rectangle prevHighlightArea = null;

    private boolean isOpened = false;

    private int sleepTime = 25;

    // better half of HighlightShell
    private int totalAnimateTime = 250;

    private int initialAlpha = 0;

    private int finalAlphaValue = DEFAULT_ALPHA_VALUE;

    private int pointerHLength = 30;

    private int pointerVLength = 7;

    private int marginLength = 7;

    public OnBoardingShell(Shell parentShell, OnBoardingManager obManager) {
        this(parentShell, obManager, DEFAULT_BACKGROUND_COLOR);
    }

    public OnBoardingShell(Shell parentShell, OnBoardingManager obManager, Color backgroundColor) {
        obShell = new Shell(parentShell, SWT.NO_TRIM | SWT.MODELESS | SWT.NO_FOCUS);
        this.parentShell = parentShell;
        this.finalBackgroundColor = backgroundColor;
        this.onBoardingManager = obManager;
        obShell.setBackground(this.finalBackgroundColor);
    }

    private void addListeners() {
        controlListenerForParent = new ControlListener() {

            @Override
            public void controlResized(ControlEvent e) {
                if (!checkAvailable()) {
                    return;
                }
                refresh();
            }

            @Override
            public void controlMoved(ControlEvent e) {
                if (!checkAvailable()) {
                    return;
                }
                Rectangle clientArea = parentShell.getDisplay().map(parentShell, null, parentShell.getClientArea());
                obShell.setLocation(clientArea.x, clientArea.y);
            }

            private boolean checkAvailable() {
                if (obShell.isDisposed() || parentShell.isDisposed()) {
                    removeListeners();
                    return false;
                } else {
                    return true;
                }
            }
        };
        parentShell.addControlListener(controlListenerForParent);

        obShell.addDisposeListener(new DisposeListener() {

            @Override
            public void widgetDisposed(DisposeEvent e) {
                onBoardingManager.close();
            }
        });
    }

    private void removeListeners() {
        parentShell.removeControlListener(controlListenerForParent);
    }

    public Widget getFocusedWidget() {
        return this.focusedWidget;
    }

    public void setFocusedWidget(Widget focusedWidget) {
        this.focusedWidget = focusedWidget;
    }

    public OnBoardingPresentationData getPresentationData() {
        return this.presentationData;
    }

    public void setPresentationData(OnBoardingPresentationData presentationData) {
        this.presentationData = presentationData;
    }

    public Shell getOnBoardingShell() {
        return this.obShell;
    }

    public Shell getParentShell() {
        return this.parentShell;
    }

    public void open() {
        obShell.open();
        isOpened = true;
        addListeners();
    }

    public void close() {
        if (obShell.isDisposed()) {
            return;
        }
        removeListeners();
        obShell.close();
        isOpened = false;
    }

    public void setVisible(boolean visible) {
        Callable<Boolean> onShowAnimationFinish = null;
        if (visible) {
            onShowAnimationFinish = new Callable<Boolean>() {

                @Override
                public Boolean call() throws Exception {
                    onBoardingManager.getUiManager().onShowAnimationDone();
                    return null;
                }
            };
        }
        OnBoardingUtils.setVisible(visible, obShell, totalAnimateTime, sleepTime, initialAlpha, finalAlphaValue,
                onShowAnimationFinish);
    }

    public void refreshInUIThread() {
        obShell.getDisplay().asyncExec(new Runnable() {

            @Override
            public void run() {
                refresh();
            }
        });
    }

    public void refresh() {
        if (!isOpened) {
            this.open();
            isOpened = true;
        }

        Rectangle clientArea = parentShell.getClientArea();

        Rectangle contentArea = presentationData.getContentArea();

        Rectangle highlightArea = null;
        if (focusedWidget == null || focusedWidget.isDisposed()) {
            contentArea.x = clientArea.x + ((clientArea.width - contentArea.width) / 2);
            contentArea.y = clientArea.y + ((clientArea.height - contentArea.height) / 2);
            prevHighlightArea = new Rectangle(0, 0, 0, 0);
            // not use it by default
            presentationData.setPointerDirrection(null);
        } else {
            highlightArea = WidgetFinder.getBounds(focusedWidget);
            if (prevHighlightArea != null && prevHighlightArea.equals(highlightArea)) {
                return;
            } else {
                prevHighlightArea = highlightArea;
            }
            // not use it by default
            presentationData.setPointerDirrection(null);

            int topSpace = highlightArea.y - clientArea.y;
            int bottomSpace = (clientArea.y + clientArea.height) - (highlightArea.y + highlightArea.height);
            int leftSpace = highlightArea.x - clientArea.x;
            int rightSpace = (clientArea.x + clientArea.width) - (highlightArea.x + highlightArea.width);
            int usableHorizenSpace = 0;
            int usableVerticalSpace = 0;
            boolean useBottomSpace = true;
            boolean useRightSpace = true;
            if (topSpace < bottomSpace) {
                useBottomSpace = true;
                usableVerticalSpace = bottomSpace;
            } else {
                useBottomSpace = false;
                usableVerticalSpace = topSpace;
            }
            if (leftSpace <= rightSpace) {
                useRightSpace = true;
                usableHorizenSpace = rightSpace;
            } else {
                useRightSpace = false;
                usableHorizenSpace = leftSpace;
            }

            // no enough space either width or height
            if (usableHorizenSpace <= contentArea.width && usableVerticalSpace <= contentArea.height) {
                if (useBottomSpace) {
                    contentArea.y = (clientArea.y + clientArea.height) - contentArea.height;
                } else {
                    contentArea.y = 0;
                }
                if (useRightSpace) {
                    contentArea.x = (clientArea.x + clientArea.width) - contentArea.width;
                } else {
                    contentArea.x = 0;
                }
                presentationData.setContentArea(contentArea);
            } else if (usableHorizenSpace <= contentArea.width) {
                // no enough space in width
                useVerticalSpace(presentationData, clientArea, highlightArea, usableVerticalSpace, useBottomSpace);
            } else if (usableVerticalSpace <= contentArea.height) {
                // no enough space in height
                useHorizenSpace(presentationData, clientArea, highlightArea, usableHorizenSpace, useRightSpace);
            } else {
                // all are ok
                if ((usableHorizenSpace - contentArea.width) <= (usableVerticalSpace - contentArea.height)) {
                    // use top/bottom
                    useVerticalSpace(presentationData, clientArea, highlightArea, usableVerticalSpace, useBottomSpace);
                } else {
                    // use left/right
                    useHorizenSpace(presentationData, clientArea, highlightArea, usableHorizenSpace, useRightSpace);
                }
            }
        }

        boolean shouldSetVisible = false;
        boolean originalVisible = obShell.getVisible();
        if (contentComposite == null) {
            shouldSetVisible = true;
            obShell.setVisible(false);
            createContentComposite();
        }
        FormData formData = new FormData();
        formData.left = new FormAttachment(0, contentArea.x);
        formData.top = new FormAttachment(0, contentArea.y);
        formData.width = contentArea.width;
        formData.height = contentArea.height;
        contentComposite.setLayoutData(formData);
        contentComposite.refreshDocIfNeeded();

        obShell.setRegion(getRegion(presentationData));
        obShell.setBounds(parentShell.getDisplay().map(parentShell, null, clientArea));
        obShell.layout();

        if (shouldSetVisible) {
            this.setVisible(originalVisible);
        }

    }

    private void createContentComposite() {
        obShell.setLayout(new FormLayout());
        contentComposite = new OnBoardingContentComposite(obShell, SWT.NONE, onBoardingManager);
    }

    private void useHorizenSpace(OnBoardingPresentationData presData, Rectangle clientArea, Rectangle highlightArea,
            int usableHorizenSpace, boolean useRightSpace) {
        Rectangle contentArea = presData.getContentArea();
        // int pointerY = highlightArea.y + (highlightArea.height / 2);
        Point pointer = new Point(0, 0);
        if (useRightSpace) {
            if (contentArea.width + pointerVLength + marginLength <= usableHorizenSpace) {
                pointer.x = highlightArea.x + highlightArea.width + marginLength;
                // Point pointer = new Point(highlightArea.x + highlightArea.width + marginLength, pointerY);
                presData.setPointer(pointer);
                presData.setPointerDirrection(OnBoardingPresentationData.DIRECTION_LEFT_SIDE);
                contentArea.x = pointer.x + pointerVLength;
            } else if (contentArea.width + marginLength <= usableHorizenSpace) {
                contentArea.x = highlightArea.x + highlightArea.width + marginLength;
            } else {
                contentArea.x = clientArea.x + clientArea.width - contentArea.width;
            }
        } else {
            if (contentArea.width + pointerVLength + marginLength <= usableHorizenSpace) {
                pointer.x = highlightArea.x - marginLength;
                // Point pointer = new Point(highlightArea.x - marginLength, pointerY);
                presData.setPointer(pointer);
                presData.setPointerDirrection(OnBoardingPresentationData.DIRECTION_RIGHT_SIDE);
                contentArea.x = pointer.x - pointerVLength - contentArea.width;
            } else if (contentArea.width + marginLength <= usableHorizenSpace) {
                contentArea.x = highlightArea.x - marginLength - contentArea.width;
            } else {
                contentArea.x = 0;
            }
        }

        // pointer.y = highlightArea.y + (highlightArea.height / 2);
        pointer.y = highlightArea.y + pointerHLength;
        if ((highlightArea.y + highlightArea.height) / 2 < pointer.y) {
            pointer.y = highlightArea.y + (highlightArea.height / 2);
        }

        // vertical align: center
        // contentArea.y = highlightArea.y + ((highlightArea.height - contentArea.height) / 2);
        // vertical align: top
        contentArea.y = highlightArea.y;
        if (contentArea.y <= 0) {
            contentArea.y = 2;
        } else if (clientArea.y + clientArea.height <= contentArea.y + contentArea.height) {
            contentArea.y = (clientArea.y + clientArea.height) - contentArea.height - 2;
        }

        presData.setContentArea(contentArea);
    }

    private void useVerticalSpace(OnBoardingPresentationData presData, Rectangle clientArea, Rectangle highlightArea,
            int usableVerticalSpace, boolean useBottomSpace) {
        Rectangle contentArea = presData.getContentArea();
        int pointerX = highlightArea.x + (highlightArea.width / 2);
        if (useBottomSpace) {
            if (contentArea.height + pointerVLength + marginLength <= usableVerticalSpace) {
                Point pointer = new Point(pointerX, highlightArea.y + highlightArea.height + marginLength);
                presData.setPointer(pointer);
                presData.setPointerDirrection(OnBoardingPresentationData.DIRECTION_TOP_SIDE);
                contentArea.y = pointer.y + pointerVLength;
            } else if (contentArea.height + marginLength <= usableVerticalSpace) {
                contentArea.y = highlightArea.y + highlightArea.height + marginLength;
            } else {
                contentArea.y = clientArea.y + clientArea.height - contentArea.height;
            }
        } else {
            if (contentArea.height + pointerVLength + marginLength <= usableVerticalSpace) {
                Point pointer = new Point(pointerX, highlightArea.y - marginLength);
                presData.setPointer(pointer);
                presData.setPointerDirrection(OnBoardingPresentationData.DIRECTION_BOTTOM_SIDE);
                contentArea.y = pointer.y - pointerVLength - contentArea.height;
            } else if (contentArea.height + marginLength <= usableVerticalSpace) {
                contentArea.y = highlightArea.y - marginLength - contentArea.height;
            } else {
                contentArea.y = 0;
            }
        }
        contentArea.x = highlightArea.x + ((highlightArea.width - contentArea.width) / 2);
        if (contentArea.x <= 0) {
            contentArea.x = 2;
        } else if (clientArea.x + clientArea.width <= contentArea.x + contentArea.width) {
            contentArea.x = (clientArea.x + clientArea.width) - contentArea.width - 2;
        }
        presData.setContentArea(contentArea);
    }

    private static Region getRegion(OnBoardingPresentationData presentationData) {
        Region region = new Region();

        Rectangle contentRect = presentationData.getContentArea();
        int contentX = contentRect.x;
        int contentY = contentRect.y;
        int contentWidth = contentRect.width;
        int contentHeight = contentRect.height;

        int arc = 4;
        region.add(contentX, contentY, contentWidth, contentHeight);
        region.subtract(contentX, contentY, arc, arc);
        region.add(getCircle(arc, contentX + arc, contentY + arc));
        region.subtract(contentX + contentWidth - arc, contentY, arc, arc);
        region.add(getCircle(arc, contentX + contentWidth - arc, contentY + arc));
        region.subtract(contentX, contentY + contentHeight - arc, arc, arc);
        region.add(getCircle(arc, contentX + arc, contentY + contentHeight - arc));
        region.subtract(contentX + contentWidth - arc, contentY + contentHeight - arc, arc, arc);
        region.add(getCircle(arc, contentX + contentWidth - arc, contentY + contentHeight - arc));

        String pointerDirrection = presentationData.getPointerDirrection();
        Point pointer = presentationData.getPointer();
        if (pointerDirrection != null && pointer != null) {
            boolean addPointerArea = false;
            int pointers[] = new int[6];
            pointers[0] = pointer.x;
            pointers[1] = pointer.y;
            int minX = contentX + arc;
            int maxX = contentX + contentWidth - arc;
            int minY = contentY + arc;
            int maxY = contentY + contentHeight - arc;
            int width = 6;
            if (OnBoardingPresentationData.DIRECTION_TOP_SIDE.equals(pointerDirrection)) {
                if (pointer.y < contentY) {
                    addPointerArea = true;
                    int i = 2;
                    if (pointer.x < minX + width) {
                        pointers[i++] = minX;
                        pointers[i++] = contentY;
                        pointers[i++] = minX + width * 2;
                        pointers[i++] = contentY;
                    } else if (maxX - width < pointer.x) {
                        pointers[i++] = maxX - width * 2;
                        pointers[i++] = contentY;
                        pointers[i++] = maxX;
                        pointers[i++] = contentY;
                    } else {
                        pointers[i++] = pointer.x - width;
                        pointers[i++] = contentY;
                        pointers[i++] = pointer.x + width;
                        pointers[i++] = contentY;
                    }
                }
            } else if (OnBoardingPresentationData.DIRECTION_BOTTOM_SIDE.equals(pointerDirrection)) {
                if (contentY + contentHeight < pointer.y) {
                    addPointerArea = true;
                    int i = 2;
                    if (pointer.x < minX + width) {
                        pointers[i++] = minX + width * 2;
                        pointers[i++] = contentY + contentHeight;
                        pointers[i++] = minX;
                        pointers[i++] = contentY + contentHeight;
                    } else if (maxX - width < pointer.x) {
                        pointers[i++] = maxX;
                        pointers[i++] = contentY + contentHeight;
                        pointers[i++] = maxX - width * 2;
                        pointers[i++] = contentY + contentHeight;
                    } else {
                        pointers[i++] = pointer.x + width;
                        pointers[i++] = contentY + contentHeight;
                        pointers[i++] = pointer.x - width;
                        pointers[i++] = contentY + contentHeight;
                    }
                }
            } else if (OnBoardingPresentationData.DIRECTION_LEFT_SIDE.equals(pointerDirrection)) {
                if (pointer.x < contentX) {
                    addPointerArea = true;
                    int i = 2;
                    if (pointer.y < minY + width) {
                        pointers[i++] = contentX;
                        pointers[i++] = minY + width * 2;
                        pointers[i++] = contentX;
                        pointers[i++] = minY;
                    } else if (maxY - width < pointer.y) {
                        pointers[i++] = contentX;
                        pointers[i++] = maxY;
                        pointers[i++] = contentX;
                        pointers[i++] = maxY - width * 2;
                    } else {
                        pointers[i++] = contentX;
                        pointers[i++] = pointer.y + width;
                        pointers[i++] = contentX;
                        pointers[i++] = pointer.y - width;
                    }
                }
            } else if (OnBoardingPresentationData.DIRECTION_RIGHT_SIDE.equals(pointerDirrection)) {
                if (contentX + contentWidth < pointer.x) {
                    addPointerArea = true;
                    int i = 2;
                    if (pointer.y < minY + width) {
                        pointers[i++] = contentX + contentWidth;
                        pointers[i++] = minY;
                        pointers[i++] = contentX + contentWidth;
                        pointers[i++] = minY + width * 2;
                    } else if (maxY - width < pointer.y) {
                        pointers[i++] = contentX + contentWidth;
                        pointers[i++] = maxY - width * 2;
                        pointers[i++] = contentX + contentWidth;
                        pointers[i++] = maxY;
                    } else {
                        pointers[i++] = contentX + contentWidth;
                        pointers[i++] = pointer.y - width;
                        pointers[i++] = contentX + contentWidth;
                        pointers[i++] = pointer.y + width;
                    }
                }
            }
            if (addPointerArea) {
                region.add(pointers);
            }
        }

        return region;
    }

    private static int[] getCircle(int r, int offsetX, int offsetY) {
        int[] ring = new int[8 * r + 4];
        for (int i = 0; i < 2 * r + 1; i++) {
            int x = i - r;
            int y = (int) Math.sqrt(r * r - x * x);
            ring[2 * i] = offsetX + x;
            ring[2 * i + 1] = offsetY + y;
            ring[8 * r - 2 * i - 2] = offsetX + x;
            ring[8 * r - 2 * i - 1] = offsetY - y;
        }
        return ring;
    }

    public boolean isOpened() {
        return isOpened;
    }

}
