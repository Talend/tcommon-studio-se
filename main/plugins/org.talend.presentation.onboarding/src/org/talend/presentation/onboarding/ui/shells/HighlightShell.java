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

import org.apache.log4j.Priority;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.talend.presentation.onboarding.exceptions.OnBoardingExceptionHandler;
import org.talend.presentation.onboarding.ui.managers.OnBoardingManager;
import org.talend.presentation.onboarding.utils.ObjectBox;
import org.talend.presentation.onboarding.utils.OnBoardingUtils;
import org.talend.presentation.onboarding.utils.WidgetFinder;

/**
 * created by cmeng on Sep 10, 2015 Detailled comment
 *
 */
public class HighlightShell {

    // 80%
    public static final int DEFAULT_ALPHA_VALUE = 204;

    public static final Color DEFAULT_BACKGROUND_COLOR = OnBoardingUtils.getColor(new RGB(0x0b, 0x1f, 0x30));

    private int sleepTime = 25;

    private int totalAnimateTime = 500;

    private int initialAlpha = 0;

    private int finalAlphaValue;

    private Color finalBackgroundColor;

    private Widget currentFocusedWidget;

    private volatile Rectangle currentFocusedRectangle;

    private ControlListener controlListenerForParent;

    private Listener widgetResizeListener;

    private Shell parentShell;

    // shell can't be extended, highlightShell
    private Shell hlShell;

    private OnBoardingManager onBoardingManager;

    private Thread moveThread;

    public HighlightShell(Shell parentShell, OnBoardingManager obManager) {
        this(parentShell, obManager, DEFAULT_ALPHA_VALUE, DEFAULT_BACKGROUND_COLOR);
    }

    public HighlightShell(Shell parentShell, OnBoardingManager obManager, int alpha, Color backroundColor) {
        hlShell = new Shell(parentShell, SWT.NO_TRIM | SWT.MODELESS | SWT.NO_FOCUS);
        this.parentShell = parentShell;
        this.onBoardingManager = obManager;
        this.finalAlphaValue = alpha;
        this.finalBackgroundColor = backroundColor;
        hlShell.setBackground(this.finalBackgroundColor);
        Rectangle clientArea = parentShell.getClientArea();
        Region region = new Region();
        region.add(clientArea);
        hlShell.setAlpha(initialAlpha);
        hlShell.setRegion(region);
        hlShell.setBounds(parentShell.getDisplay().map(parentShell, null, clientArea));
    }

    private void addListeners() {
        controlListenerForParent = new ControlListener() {

            @Override
            public void controlResized(ControlEvent e) {
                if (hlShell.isDisposed()) {
                    removeListeners();
                    return;
                }
                if (currentFocusedWidget == null || currentFocusedWidget.isDisposed()) {
                    Rectangle clientArea = parentShell.getClientArea();
                    Rectangle mappedClientArea = parentShell.getDisplay().map(parentShell, null, clientArea);
                    hlShell.setBounds(mappedClientArea);
                    hlShell.setRegion(getNewRegion(clientArea, new Rectangle(0, 0, 0, 0)));
                }
            }

            @Override
            public void controlMoved(ControlEvent e) {
                if (hlShell.isDisposed()) {
                    removeListeners();
                    return;
                }
                Rectangle clientArea = parentShell.getDisplay().map(parentShell, null, parentShell.getClientArea());
                hlShell.setLocation(clientArea.x, clientArea.y);
            }
        };
        parentShell.addControlListener(controlListenerForParent);

        hlShell.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseDown(MouseEvent e) {
                if (hlShell.isDisposed()) {
                    removeListeners();
                    return;
                }
                // left click
                if (e.button == 1) {
                    onBoardingManager.close();
                }
            }
        });

        hlShell.addDisposeListener(new DisposeListener() {

            @Override
            public void widgetDisposed(DisposeEvent e) {
                onBoardingManager.close();
            }
        });
    }

    private void removeListeners() {
        if (controlListenerForParent != null) {
            parentShell.removeControlListener(controlListenerForParent);
        }
        if (widgetResizeListener != null && currentFocusedWidget != null && !currentFocusedWidget.isDisposed()) {
            currentFocusedWidget.removeListener(SWT.Resize, widgetResizeListener);
        }
    }

    public void focusOnWidgetInUIThread(final Widget widget) {
        hlShell.getDisplay().asyncExec(new Runnable() {

            @Override
            public void run() {
                focusOnWidget(widget);
            }
        });
    }

    private Rectangle getCurrentWidgetBounds() {
        if (currentFocusedWidget != null && !currentFocusedWidget.isDisposed()) {
            return WidgetFinder.getBoundsInUIThread(currentFocusedWidget);
        }
        return new Rectangle(0, 0, 0, 0);
    }

    private void focusOnWidget(final Widget widget) {
        if (!isContinueExecute()) {
            return;
        }

        if (widget != currentFocusedWidget) {
            if (currentFocusedWidget != null && widgetResizeListener != null && !currentFocusedWidget.isDisposed()) {
                currentFocusedWidget.removeListener(SWT.Resize, widgetResizeListener);
            }
            if (widget != null) {
                widgetResizeListener = new Listener() {

                    @Override
                    public void handleEvent(Event event) {
                        if (widget.isDisposed()) {
                            return;
                        }
                        if (hlShell.isDisposed() || widget != currentFocusedWidget) {
                            // remove this listener
                            widget.removeListener(SWT.Resize, this);
                            return;
                        }

                        if (moveThread != null && !moveThread.isInterrupted()) {
                            moveThread.interrupt();
                        }

                        onBoardingManager.getUiManager().refreshShellsBound();
                    }
                };
                widget.addListener(SWT.Resize, widgetResizeListener);
            } else {
                widgetResizeListener = null;
            }
        }

        if (moveThread != null) {
            moveThread.interrupt();
        }

        Widget oldWidget = currentFocusedWidget;
        currentFocusedWidget = widget;

        Rectangle newWidgetBounds = null;
        if (widget == null) {
            if (oldWidget == null) {
                newWidgetBounds = new Rectangle(0, 0, 0, 0);
            } else {
                if (currentFocusedRectangle == null) {
                    currentFocusedRectangle = WidgetFinder.getBounds(oldWidget);
                }
                // center to the widget
                newWidgetBounds = new Rectangle(currentFocusedRectangle.x + currentFocusedRectangle.width / 2,
                        currentFocusedRectangle.y + currentFocusedRectangle.height / 2, 0, 0);
            }
        } else {
            newWidgetBounds = WidgetFinder.getBounds(widget);
        }

        // get the old widget bounds
        Rectangle oldWidgetBounds = null;
        if (oldWidget == null) {
            // center to the widget
            oldWidgetBounds = new Rectangle(newWidgetBounds.x + newWidgetBounds.width / 2, newWidgetBounds.y
                    + newWidgetBounds.height / 2, 0, 0);
        } else {
            if (currentFocusedRectangle == null) {
                oldWidgetBounds = WidgetFinder.getBounds(oldWidget);
            } else {
                oldWidgetBounds = currentFocusedRectangle;
            }
        }

        final int leftXBegin = oldWidgetBounds.x;
        final int rightXBegin = oldWidgetBounds.x + oldWidgetBounds.width;
        final int topYBegin = oldWidgetBounds.y;
        final int bottomYBegin = oldWidgetBounds.y + oldWidgetBounds.height;
        if (leftXBegin != newWidgetBounds.x || topYBegin != newWidgetBounds.y
                || rightXBegin != (newWidgetBounds.x + newWidgetBounds.width)
                || bottomYBegin != (newWidgetBounds.y + newWidgetBounds.height)) {
            final Rectangle newWidgetBoundsFinal = newWidgetBounds;
            final Rectangle clientArea = parentShell.getClientArea();
            moveThread = new Thread(new Runnable() {

                @Override
                public void run() {

                    int leftX = leftXBegin;
                    int rightX = rightXBegin;
                    int topY = topYBegin;
                    int bottomY = bottomYBegin;
                    int executeTimes = totalAnimateTime / sleepTime;
                    int leftXLimit = newWidgetBoundsFinal.x;
                    int rightXLimit = newWidgetBoundsFinal.x + newWidgetBoundsFinal.width;
                    int topYLimit = newWidgetBoundsFinal.y;
                    int bottomYLimit = newWidgetBoundsFinal.y + newWidgetBoundsFinal.height;
                    int leftXIncrement = (leftXLimit - leftX) / executeTimes;
                    int rightXIncrement = (rightXLimit - rightX) / executeTimes;
                    int topYIncrement = (topYLimit - topY) / executeTimes;
                    int bottomYIncrement = (bottomYLimit - bottomY) / executeTimes;
                    if (leftXLimit - leftX < 0) {
                        leftXIncrement = leftXIncrement - 1;
                    } else {
                        leftXIncrement = leftXIncrement + 1;
                    }
                    if (rightXLimit - rightX < 0) {
                        rightXIncrement = rightXIncrement - 1;
                    } else {
                        rightXIncrement = rightXIncrement + 1;
                    }
                    if (topYLimit - topY < 0) {
                        topYIncrement = topYIncrement - 1;
                    } else {
                        topYIncrement = topYIncrement + 1;
                    }
                    if (bottomYLimit - bottomY < 0) {
                        bottomYIncrement = bottomYIncrement - 1;
                    } else {
                        bottomYIncrement = bottomYIncrement + 1;
                    }
                    final ObjectBox<Boolean> isContinue = new ObjectBox<Boolean>();
                    isContinue.value = true;
                    for (int i = 1; i < executeTimes - 1; i++) {
                        leftX = getLimitValue(leftXLimit, leftX, leftXIncrement);
                        rightX = getLimitValue(rightXLimit, rightX, rightXIncrement);
                        topY = getLimitValue(topYLimit, topY, topYIncrement);
                        bottomY = getLimitValue(bottomYLimit, bottomY, bottomYIncrement);
                        Rectangle highlightRect = new Rectangle(leftX, topY, rightX - leftX, bottomY - topY);
                        currentFocusedRectangle = highlightRect;
                        final Region region = getNewRegion(clientArea, currentFocusedRectangle);
                        if (Thread.currentThread().isInterrupted()) {
                            break;
                        }
                        try {
                            Display.getDefault().syncExec(new Runnable() {

                                @Override
                                public void run() {
                                    if (!isContinueExecute()) {
                                        isContinue.value = false;
                                        return;
                                    }
                                    hlShell.setRegion(region);
                                }
                            });
                            if (!isContinue.value) {
                                break;
                            }
                            Thread.sleep(sleepTime);
                        } catch (Exception e) {
                            OnBoardingExceptionHandler.process(e, Priority.INFO);
                            break;
                        }
                    }

                    Display.getDefault().syncExec(new Runnable() {

                        @Override
                        public void run() {
                            if (!hlShell.isDisposed()) {
                                refreshHighlightShell();
                            }
                            onBoardingManager.getUiManager().onHighlightShellMoveCompleted();
                        }
                    });
                }
            });
            moveThread.start();
        } else {
            onBoardingManager.getUiManager().onHighlightShellMoveCompleted();
        }

    }

    private int getLimitValue(int limitValue, int realValue, int increment) {
        realValue = realValue + increment;
        if (increment < 0) {
            if (realValue < limitValue) {
                return limitValue;
            }
        } else if (0 < increment) {
            if (limitValue < realValue) {
                return limitValue;
            }
        }
        return realValue;
    }

    private Region getNewRegion(Rectangle clientRect, Rectangle highlightRect) {
        Region region = new Region();

        region.add(clientRect);
        region.subtract(highlightRect);

        return region;
    }

    public void setVisible(boolean visible) {
        OnBoardingUtils.setVisible(visible, hlShell, totalAnimateTime, sleepTime, initialAlpha, finalAlphaValue, null);
    }

    private boolean isContinueExecute() {
        return OnBoardingUtils.isContinueExecute(hlShell);
    }

    public void open() {
        hlShell.open();
        addListeners();
        setVisible(true);
    }

    public void close() {
        if (hlShell.isDisposed()) {
            return;
        }
        hlShell.setEnabled(false);
        setVisible(false);
        removeListeners();
        hlShell.close();
    }

    public Shell getHighlightShell() {
        return this.hlShell;
    }

    public void refreshHighlightShell() {
        Rectangle clientArea = parentShell.getClientArea();
        Rectangle mappedClientArea = parentShell.getDisplay().map(parentShell, null, clientArea);
        hlShell.setBounds(mappedClientArea);
        currentFocusedRectangle = getCurrentWidgetBounds();
        hlShell.setRegion(getNewRegion(clientArea, currentFocusedRectangle));
    }

}
